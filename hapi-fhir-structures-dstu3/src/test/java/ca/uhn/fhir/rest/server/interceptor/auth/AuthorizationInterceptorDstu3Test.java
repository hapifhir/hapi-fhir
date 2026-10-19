package ca.uhn.fhir.rest.server.interceptor.auth;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.api.AddProfileTagEnum;
import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.rest.annotation.GraphQL;
import ca.uhn.fhir.rest.annotation.GraphQLQueryUrl;
import ca.uhn.fhir.rest.annotation.History;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.annotation.Transaction;
import ca.uhn.fhir.rest.annotation.TransactionParam;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.FifoMemoryPagingProvider;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import ca.uhn.fhir.util.TestUtil;
import org.hl7.fhir.dstu3.model.Binary;
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.Parameters;
import org.hl7.fhir.dstu3.model.Resource;
import org.hl7.fhir.dstu3.model.ResourceType;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.nio.charset.Charset;
import java.util.Collections;
import java.util.List;

public class AuthorizationInterceptorDstu3Test {

	private static final FhirContext ourCtx = FhirContext.forDstu3Cached();
	private static List<Resource> ourReturn;
	private static List<IBaseResource> ourDeleted;

	@RegisterExtension
	private RestfulServerExtension ourServer  = new RestfulServerExtension(ourCtx)
		 .registerProvider(new PlainProvider())
		 .withPagingProvider(new FifoMemoryPagingProvider(100));

	@BeforeEach
	public void before() {
		ourCtx.setAddProfileTagWhenEncoding(AddProfileTagEnum.NEVER);
		ourServer.getInterceptorService().unregisterAllInterceptors();
		ourServer.getRestfulServer().setTenantIdentificationStrategy(null);
		ourReturn = null;
		ourDeleted = null;
	}

	@Test
	public void testTransactionWithPatch() {

		ourServer.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow("transactions").transaction().withAnyOperation().andApplyNormalRules().andThen()
					.allow("read patient").patch().allRequests().andThen()
					.allow().write().resourcesOfType("Patient").withAnyId().andThen()
					.build();
			}
		});

		// Payload
		Binary binary = new Binary();
		binary.setContent("{}".getBytes(Charset.defaultCharset()));
		binary.setContentType(Constants.CT_JSON_PATCH);

		// Request is a transaction with 1 search
		Bundle requestBundle = new Bundle();
		requestBundle.setType(Bundle.BundleType.TRANSACTION);
		requestBundle.addEntry()
			.setResource(binary)
			.getRequest()
			.setUrl(ResourceType.Patient.name() + "/123");

		/*
		 * Response is a transaction response containing the search results
		 */
		Bundle responseBundle = new Bundle();
		responseBundle.setType(Bundle.BundleType.TRANSACTION);
		ourReturn = Collections.singletonList(responseBundle);

		ourServer.fhirRequest("/").post(requestBundle).assertStatus(200);
	}


	public static class PlainProvider {

		@History()
		public List<Resource> history() {
			return (ourReturn);
		}

		@Operation(name = "opName", idempotent = true)
		public Parameters operation() {
			return (Parameters) new Parameters().setId("1");
		}

		@Operation(name = "process-message", idempotent = true)
		public Parameters processMessage(@OperationParam(name = "content") Bundle theInput) {
			return (Parameters) new Parameters().setId("1");
		}

		@GraphQL
		public String processGraphQlRequest(ServletRequestDetails theRequestDetails, @IdParam IIdType theId, @GraphQLQueryUrl String theQuery) {
			return "{'foo':'bar'}";
		}

		@Transaction()
		public Bundle search(ServletRequestDetails theRequestDetails, IInterceptorBroadcaster theInterceptorBroadcaster, @TransactionParam Bundle theInput) {
			if (ourDeleted != null) {
				for (IBaseResource next : ourDeleted) {
					HookParams params = new HookParams()
						.add(IBaseResource.class, next)
						.add(RequestDetails.class, theRequestDetails)
						.add(ServletRequestDetails.class, theRequestDetails);
					theInterceptorBroadcaster.callHooks(Pointcut.STORAGE_PRESTORAGE_RESOURCE_DELETED, params);
				}
			}
			return (Bundle) ourReturn.get(0);
		}

	}

	@AfterAll
	public static void afterClassClearContext() {
		TestUtil.randomizeLocaleAndTimezone();
	}
}
