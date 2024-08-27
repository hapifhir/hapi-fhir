package ca.uhn.fhir.rest.server.interceptor.auth;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.model.api.IQueryParameterOr;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.annotation.ConditionalUrlParam;
import ca.uhn.fhir.rest.annotation.Create;
import ca.uhn.fhir.rest.annotation.Delete;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.OptionalParam;
import ca.uhn.fhir.rest.annotation.Patch;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.annotation.Transaction;
import ca.uhn.fhir.rest.annotation.TransactionParam;
import ca.uhn.fhir.rest.annotation.Update;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.PatchTypeEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.interceptor.LoggingInterceptor;
import ca.uhn.fhir.rest.param.BaseAndListParam;
import ca.uhn.fhir.rest.param.ReferenceAndListParam;
import ca.uhn.fhir.rest.param.StringAndListParam;
import ca.uhn.fhir.rest.param.TokenAndListParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.param.TokenParamModifier;
import ca.uhn.fhir.rest.server.FifoMemoryPagingProvider;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.ForbiddenOperationException;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import ca.uhn.fhir.util.BundleBuilder;
import ca.uhn.fhir.util.TestUtil;
import ca.uhn.fhir.util.UrlUtil;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.Resource;
import org.hl7.fhir.r4.model.ValueSet;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ca.uhn.fhir.util.UrlUtil.escapeUrlParam;
import static java.util.Collections.singletonList;
import static java.util.Collections.singletonMap;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SearchNarrowingInterceptorTest {
	private static final Logger ourLog = LoggerFactory.getLogger(SearchNarrowingInterceptorTest.class);
	private static final FhirContext ourCtx = FhirContext.forR4Cached();
	public static final String FOO_CS_URL = "http://foo";
	public static final String CODE_PREFIX = "CODE";
	private static String ourLastHitMethod;
	private static TokenAndListParam ourLastIdParam;
	private static TokenAndListParam ourLastCodeParam;
	private static ReferenceAndListParam ourLastSubjectParam;
	private static ReferenceAndListParam ourLastPatientParam;
	private static ReferenceAndListParam ourLastPerformerParam;
	private static StringAndListParam ourLastNameParam;
	private static List<Resource> ourReturn;
	private static AuthorizedList ourNextAuthorizedList;
	private static Bundle.BundleEntryRequestComponent ourLastBundleRequest;
	private static String ourLastConditionalUrl;

	private IGenericClient myClient;
	@Mock
	private IValidationSupport myValidationSupport;
	private MySearchNarrowingInterceptor myInterceptor;

	@RegisterExtension
	private RestfulServerExtension myRestfulServerExtension = new RestfulServerExtension(ourCtx)
		.registerProvider(new DummyObservationResourceProvider())
		.registerProvider(new DummyPatientResourceProvider())
		.registerProvider(new DummySystemProvider())
		.withPagingProvider(new FifoMemoryPagingProvider(100));

	@BeforeEach
	public void before() {
		ourLastHitMethod = null;
		ourReturn = Collections.emptyList();
		ourLastIdParam = null;
		ourLastNameParam = null;
		ourLastSubjectParam = null;
		ourLastPatientParam = null;
		ourLastPerformerParam = null;
		ourLastCodeParam = null;
		ourNextAuthorizedList = null;
		ourLastConditionalUrl = null;

		myInterceptor = new MySearchNarrowingInterceptor();
		myInterceptor.setNarrowConditionalUrls(true);

		myRestfulServerExtension.registerInterceptor(myInterceptor);

		myClient = myRestfulServerExtension.getFhirClient();
	}

	@AfterEach
	public void afterEach() {
		myRestfulServerExtension.unregisterInterceptor(myInterceptor);
	}

	@Test
	public void testReturnNull() {

		ourNextAuthorizedList = null;

		myClient
			.search()
			.forResource("Patient")
			.execute();

		assertEquals("Patient.search", ourLastHitMethod);
		assertNull(ourLastCodeParam);
		assertNull(ourLastSubjectParam);
		assertNull(ourLastPerformerParam);
		assertNull(ourLastPatientParam);
		assertNull(ourLastIdParam);
	}

	@Test
	public void testNarrowCode_NotInSelected_ClientRequestedNoParams() {
		ourNextAuthorizedList = new AuthorizedList()
			.addCodeNotInValueSet("Observation", "code", "http://myvs");

		myClient
			.search()
			.forResource("Observation")
			.execute();

		assertEquals("Observation.search", ourLastHitMethod);
		assertEquals(1, ourLastCodeParam.size());
		assertEquals(1, ourLastCodeParam.getValuesAsQueryTokens().get(0).size());
		assertEquals(TokenParamModifier.NOT_IN, ourLastCodeParam.getValuesAsQueryTokens().get(0).getValuesAsQueryTokens().get(0).getModifier());
		assertEquals("http://myvs", ourLastCodeParam.getValuesAsQueryTokens().get(0).getValuesAsQueryTokens().get(0).getValue());
		assertNull(ourLastCodeParam.getValuesAsQueryTokens().get(0).getValuesAsQueryTokens().get(0).getSystem());
		assertNull(ourLastSubjectParam);
		assertNull(ourLastPerformerParam);
		assertNull(ourLastPatientParam);
		assertNull(ourLastIdParam);
	}

	@Test
	public void testNarrowCode_InSelected_ClientRequestedNoParams() {
		ourNextAuthorizedList = new AuthorizedList()
			.addCodeInValueSet("Observation", "code", "http://myvs");

		myClient
			.search()
			.forResource("Observation")
			.execute();

		assertEquals("Observation.search", ourLastHitMethod);
		assertEquals(1, ourLastCodeParam.size());
		assertEquals(1, ourLastCodeParam.getValuesAsQueryTokens().get(0).size());
		assertEquals(TokenParamModifier.IN, ourLastCodeParam.getValuesAsQueryTokens().get(0).getValuesAsQueryTokens().get(0).getModifier());
		assertEquals("http://myvs", ourLastCodeParam.getValuesAsQueryTokens().get(0).getValuesAsQueryTokens().get(0).getValue());
		assertNull(ourLastCodeParam.getValuesAsQueryTokens().get(0).getValuesAsQueryTokens().get(0).getSystem());
		assertNull(ourLastSubjectParam);
		assertNull(ourLastPerformerParam);
		assertNull(ourLastPatientParam);
		assertNull(ourLastIdParam);
	}

	@Test
	public void testNarrowCode_InSelected_ClientRequestedNoParams_LargeValueSet() {
		myInterceptor.setPostFilterLargeValueSetThreshold(50);
		myInterceptor.setValidationSupport(myValidationSupport);
		when(myValidationSupport.getFhirContext()).thenReturn(ourCtx);
		when(myValidationSupport.expandValueSet(any(), any(), eq("http://large-vs")))
			.thenReturn(createValueSetWithCodeCount(100));

		ourNextAuthorizedList = new AuthorizedList()
			.addCodeInValueSet("Observation", "code", "http://large-vs");

		myClient
			.search()
			.forResource("Observation")
			.execute();

		assertEquals("Observation.search", ourLastHitMethod);
		assertNull(ourLastCodeParam);
		assertNull(ourLastSubjectParam);
		assertNull(ourLastPerformerParam);
		assertNull(ourLastPatientParam);
		assertNull(ourLastIdParam);
	}

	@Test
	public void testNarrowCode_InSelected_ClientRequestedBundleWithNoParams() {
		ourNextAuthorizedList = new AuthorizedList()
			.addCodeInValueSet("Observation", "code", "http://myvs");

		Bundle bundle = new Bundle();
		bundle.setType(Bundle.BundleType.TRANSACTION);
		bundle.addEntry().getRequest().setMethod(Bundle.HTTPVerb.GET).setUrl("Observation?subject=Patient/123");
		ourLog.debug(ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(bundle));

		myClient
			.transaction()
			.withBundle(bundle)
			.execute();

		assertEquals("transaction", ourLastHitMethod);
		String expectedUrl = "Observation?" +
			escapeUrlParam("code:in") +
			"=" +
			escapeUrlParam("http://myvs") +
			"&subject=" +
			escapeUrlParam("Patient/123");
		assertEquals(expectedUrl, ourLastBundleRequest.getUrl());

	}

	@Test
	public void testNarrowCode_InSelected_ClientRequestedOtherInParam() {
		ourNextAuthorizedList = new AuthorizedList()
			.addCodeInValueSet("Observation", "code", "http://myvs");

		myClient.registerInterceptor(new LoggingInterceptor(false));
		myClient
			.search()
			.forResource("Observation")
			.where(singletonMap("code", singletonList(new TokenParam("http://othervs").setModifier(TokenParamModifier.IN))))
			.execute();

		assertEquals("Observation.search", ourLastHitMethod);
		assertEquals(2, ourLastCodeParam.size());
		assertEquals(1, ourLastCodeParam.getValuesAsQueryTokens().get(0).size());
		assertEquals(TokenParamModifier.IN, ourLastCodeParam.getValuesAsQueryTokens().get(0).getValuesAsQueryTokens().get(0).getModifier());
		assertEquals("http://othervs", ourLastCodeParam.getValuesAsQueryTokens().get(0).getValuesAsQueryTokens().get(0).getValue());
		assertNull(ourLastCodeParam.getValuesAsQueryTokens().get(0).getValuesAsQueryTokens().get(0).getSystem());
		assertEquals(1, ourLastCodeParam.getValuesAsQueryTokens().get(1).size());
		assertEquals(TokenParamModifier.IN, ourLastCodeParam.getValuesAsQueryTokens().get(1).getValuesAsQueryTokens().get(0).getModifier());
		assertEquals("http://myvs", ourLastCodeParam.getValuesAsQueryTokens().get(1).getValuesAsQueryTokens().get(0).getValue());
		assertNull(ourLastCodeParam.getValuesAsQueryTokens().get(1).getValuesAsQueryTokens().get(0).getSystem());
		assertNull(ourLastSubjectParam);
		assertNull(ourLastPerformerParam);
		assertNull(ourLastPatientParam);
		assertNull(ourLastIdParam);
	}

	@Test
	public void testNarrowCode_InSelected_DifferentResource() {
		ourNextAuthorizedList = new AuthorizedList()
			.addCodeInValueSet("Procedure", "code", "http://myvs");

		myClient
			.search()
			.forResource("Observation")
			.execute();

		assertEquals("Observation.search", ourLastHitMethod);
		assertNull(ourLastCodeParam);
	}

	@Test
	public void testNarrowCompartment_ObservationsByPatientContext_ClientRequestedNoParams() {
		ourNextAuthorizedList = new AuthorizedList()
			.addCompartments("Patient/123", "Patient/456");

		myClient
			.search()
			.forResource("Observation")
			.execute();

		assertEquals("Observation.search", ourLastHitMethod);
		assertNull(ourLastIdParam);
		assertNull(ourLastCodeParam);
		assertNull(ourLastSubjectParam);
		assertNull(ourLastPerformerParam);
		assertThat(toStrings(ourLastPatientParam)).containsExactly("Patient/123,Patient/456");
	}

	@Test
	public void testNarrowCompartment_ObservationsByPatientContext_ClientRequestedBundleNoParams() {

		ourNextAuthorizedList = new AuthorizedList().addCompartments("Patient/123", "Patient/456");

		Bundle bundle = new Bundle();
		bundle.setType(Bundle.BundleType.TRANSACTION);
		bundle.addEntry().getRequest().setMethod(Bundle.HTTPVerb.GET).setUrl("Patient");
		ourLog.debug(ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(bundle));

		myClient
			.transaction()
			.withBundle(bundle)
			.execute();

		assertEquals("transaction", ourLastHitMethod);
		assertEquals("Patient?_id=" + UrlUtil.escapeUrlParam("Patient/123,Patient/456"), ourLastBundleRequest.getUrl());
	}
	@Test
	public void testNarrow_OnlyAppliesToSearches() {
		ourNextAuthorizedList = new AuthorizedList().addCompartments("Patient/123");


		Observation o = new Observation();
		o.setSubject(new Reference("Patient/456"));

		{
			//Create a resource outside of the referenced compartment
			Bundle bundle = new Bundle();
			bundle.setType(Bundle.BundleType.TRANSACTION);
			bundle.addEntry().getRequest().setMethod(Bundle.HTTPVerb.POST).setUrl("Observation");
			myClient.transaction().withBundle(bundle).execute();
		}

		{
			Bundle bundle = new Bundle();
			bundle.setType(Bundle.BundleType.TRANSACTION);
			bundle.addEntry().getRequest().setMethod(Bundle.HTTPVerb.PUT).setUrl("Observation");

			//Update a resource outside of the referenced compartment
			myClient.transaction().withBundle(bundle).execute();
		}
	}

	@Test
	public void testNarrowCompartment_PatientByPatientContext_ClientRequestedNoParams() {

		ourNextAuthorizedList = new AuthorizedList().addCompartments("Patient/123", "Patient/456");

		myClient
			.search()
			.forResource("Patient")
			.execute();

		assertEquals("Patient.search", ourLastHitMethod);
		assertNull(ourLastNameParam);
		assertThat(toStrings(ourLastIdParam)).containsExactly("Patient/123,Patient/456");
	}

	@Test
	public void testNarrowCompartment_PatientByPatientContext_ClientRequestedSomeOverlap() {

		ourNextAuthorizedList = new AuthorizedList().addCompartments("Patient/123", "Patient/456");

		myClient
			.search()
			.forResource("Patient")
			.where(IAnyResource.RES_ID.exactly().codes("Patient/123", "Patient/999"))
			.execute();

		assertEquals("Patient.search", ourLastHitMethod);
		assertNull(ourLastNameParam);
		assertThat(toStrings(ourLastIdParam)).containsExactly("Patient/123");
	}

	@Test
	public void testNarrowCompartment_ObservationsByPatientContext_ClientRequestedSomeOverlap() {

		ourNextAuthorizedList = new AuthorizedList().addCompartments("Patient/123", "Patient/456");

		myClient
			.search()
			.forResource("Observation")
			.where(Observation.PATIENT.hasAnyOfIds("Patient/456", "Patient/777"))
			.and(Observation.PATIENT.hasAnyOfIds("Patient/456", "Patient/888"))
			.execute();

		assertEquals("Observation.search", ourLastHitMethod);
		assertNull(ourLastIdParam);
		assertNull(ourLastCodeParam);
		assertNull(ourLastSubjectParam);
		assertNull(ourLastPerformerParam);
		assertThat(toStrings(ourLastPatientParam)).containsExactly("Patient/456", "Patient/456");
	}

	@Test
	public void testNarrowCompartment_ObservationsByPatientContext_ClientRequestedSomeOverlap_ShortIds() {

		ourNextAuthorizedList = new AuthorizedList().addCompartments("Patient/123", "Patient/456");

		myClient
			.search()
			.forResource("Observation")
			.where(Observation.PATIENT.hasAnyOfIds("456", "777"))
			.and(Observation.PATIENT.hasAnyOfIds("456", "888"))
			.execute();

		assertEquals("Observation.search", ourLastHitMethod);
		assertNull(ourLastIdParam);
		assertNull(ourLastCodeParam);
		assertNull(ourLastSubjectParam);
		assertNull(ourLastPerformerParam);
		assertThat(toStrings(ourLastPatientParam)).containsExactly("456", "456");
	}

	@Test
	public void testNarrowCompartment_ObservationsByPatientContext_ClientRequestedSomeOverlap_UseSynonym() {

		ourNextAuthorizedList = new AuthorizedList().addCompartments("Patient/123", "Patient/456");

		myClient
			.search()
			.forResource("Observation")
			.where(Observation.SUBJECT.hasAnyOfIds("Patient/456", "Patient/777"))
			.and(Observation.SUBJECT.hasAnyOfIds("Patient/456", "Patient/888"))
			.execute();

		assertEquals("Observation.search", ourLastHitMethod);
		assertNull(ourLastIdParam);
		assertNull(ourLastCodeParam);
		assertThat(toStrings(ourLastSubjectParam)).containsExactly("Patient/456", "Patient/456");
		assertNull(ourLastPerformerParam);
		assertNull(ourLastPatientParam);
	}

	@Test
	public void testNarrowCompartment_ObservationsByPatientContext_ClientRequestedNoOverlap() {

		ourNextAuthorizedList = new AuthorizedList().addCompartments("Patient/123", "Patient/456");

		try {
			myClient
				.search()
				.forResource("Observation")
				.where(Observation.PATIENT.hasAnyOfIds("Patient/111", "Patient/777"))
				.and(Observation.PATIENT.hasAnyOfIds("Patient/111", "Patient/888"))
				.execute();

			fail("Expected a 403 error");
		} catch (ForbiddenOperationException e) {
			assertEquals(Constants.STATUS_HTTP_403_FORBIDDEN, e.getStatusCode());
		}

		assertNull(ourLastHitMethod);
	}

	@Test
	public void testNarrowCompartment_ObservationsByPatientContext_ClientRequestedNoOverlap_UseSynonym() {

		ourNextAuthorizedList = new AuthorizedList().addCompartments("Patient/123", "Patient/456");

		try {
			myClient
				.search()
				.forResource("Observation")
				.where(Observation.SUBJECT.hasAnyOfIds("Patient/111", "Patient/777"))
				.and(Observation.SUBJECT.hasAnyOfIds("Patient/111", "Patient/888"))
				.execute();

			fail("Expected a 403 error");
		} catch (ForbiddenOperationException e) {
			assertEquals(Constants.STATUS_HTTP_403_FORBIDDEN, e.getStatusCode());
		}

		assertNull(ourLastHitMethod);
	}

	@Test
	public void testNarrowCompartment_ObservationsByPatientContext_ClientRequestedBadParameter() {

		ourNextAuthorizedList = new AuthorizedList().addCompartments("Patient/123", "Patient/456");

		try {
			myClient
				.search()
				.forResource("Observation")
				.where(Observation.PATIENT.hasAnyOfIds("Patient/"))
				.execute();

			fail("Expected a 403 error");
		} catch (ForbiddenOperationException e) {
			assertEquals(Constants.STATUS_HTTP_403_FORBIDDEN, e.getStatusCode());
		}

		assertNull(ourLastHitMethod);
	}

	@Test
	public void testNarrowCompartment_ObservationsByPatientContext_ClientRequestedBadPermission() {

		ourNextAuthorizedList = new AuthorizedList().addCompartments("Patient/");

		try {
			myClient
				.search()
				.forResource("Observation")
				.where(Observation.PATIENT.hasAnyOfIds("Patient/111", "Patient/777"))
				.execute();

			fail("Expected a 403 error");
		} catch (ForbiddenOperationException e) {
			assertEquals(Constants.STATUS_HTTP_403_FORBIDDEN, e.getStatusCode());
		}

		assertNull(ourLastHitMethod);
	}

	/**
	 * Should not make any changes
	 */
	@Test
	public void testNarrowResources_ObservationsByPatientResources_ClientRequestedNoParams() {
		ourNextAuthorizedList = new AuthorizedList()
			.addResources("Patient/123", "Patient/456");

		myClient
			.search()
			.forResource("Observation")
			.execute();

		assertEquals("Observation.search", ourLastHitMethod);
		assertNull(ourLastIdParam);
		assertNull(ourLastCodeParam);
		assertNull(ourLastSubjectParam);
		assertNull(ourLastPerformerParam);
		assertNull(ourLastPatientParam);
	}

	@Test
	public void testNarrowResources_PatientByPatientResources_ClientRequestedNoParams() {
		ourNextAuthorizedList = new AuthorizedList()
			.addResources("Patient/123", "Patient/456");

		myClient
			.search()
			.forResource("Patient")
			.execute();

		assertEquals("Patient.search", ourLastHitMethod);
		assertNull(ourLastCodeParam);
		assertNull(ourLastSubjectParam);
		assertNull(ourLastPerformerParam);
		assertNull(ourLastPatientParam);
		assertThat(toStrings(ourLastIdParam)).containsExactly("Patient/123,Patient/456");
	}

	@Test
	public void testNarrowCompartment_ConditionalCreate_Patient() {
		ourNextAuthorizedList = new AuthorizedList()
			 .addResources("Patient/123", "Patient/456");

		myClient
			 .create()
			 .resource(new Patient().setActive(true))
			 .conditionalByUrl("Patient?active=true")
			 .execute();

		assertEquals("Patient.create", ourLastHitMethod);
		assertThat(ourLastConditionalUrl).startsWith("/Patient?");
		assertThat(ourLastConditionalUrl).contains("active=true");
		assertThat(ourLastConditionalUrl).contains("_id=" + escapeUrlParam("Patient/123,Patient/456"));
	}

	@Test
	public void testNarrowCompartment_ConditionalCreate_Patient_Disabled() {
		ourNextAuthorizedList = new AuthorizedList()
			 .addResources("Patient/123", "Patient/456");
		myInterceptor.setNarrowConditionalUrls(false);

		myClient
			 .create()
			 .resource(new Patient().setActive(true))
			 .conditionalByUrl("Patient?active=true")
			 .execute();

		assertEquals("Patient.create", ourLastHitMethod);
		assertEquals("/Patient?active=true", ourLastConditionalUrl);
	}

	@Test
	public void testNarrowCompartment_ConditionalCreate_Patient_ReturnNull() {
		ourNextAuthorizedList = null;

		myClient
			 .create()
			 .resource(new Patient().setActive(true))
			 .conditionalByUrl("Patient?active=true")
			 .execute();

		assertEquals("Patient.create", ourLastHitMethod);
		assertEquals("/Patient?active=true", ourLastConditionalUrl);
	}

	@Test
	public void testNarrowCompartment_ConditionalCreate_Observation() {
		ourNextAuthorizedList = new AuthorizedList()
			 .addCompartments("Patient/123", "Patient/456");

		myClient
			 .create()
			 .resource(new Observation().setStatus(Observation.ObservationStatus.FINAL))
			 .conditionalByUrl("Observation?status=final")
			 .execute();

		assertEquals("Observation.create", ourLastHitMethod);
		assertThat(ourLastConditionalUrl).startsWith("/Observation?");
		assertThat(ourLastConditionalUrl).contains("status=final");
		assertThat(ourLastConditionalUrl).contains("patient=" + escapeUrlParam("Patient/123,Patient/456"));
	}

	@Test
	public void testNarrowCompartment_ConditionalCreate_Observation_InTransaction() {
		ourNextAuthorizedList = new AuthorizedList()
			 .addCompartments("Patient/123", "Patient/456");

		BundleBuilder bb = new BundleBuilder(ourCtx);
		bb.addTransactionCreateEntry(new Observation().setStatus(Observation.ObservationStatus.FINAL)).conditional("Observation?status=final");

		myClient
			 .transaction()
			 .withBundle(bb.getBundle())
			 .execute();

		assertEquals("transaction", ourLastHitMethod);
		assertEquals("Observation", ourLastBundleRequest.getUrl());
		assertThat(ourLastBundleRequest.getIfNoneExist()).startsWith("Observation?");
		assertThat(ourLastBundleRequest.getIfNoneExist()).contains("status=final");
		assertThat(ourLastBundleRequest.getIfNoneExist()).contains("patient=" + escapeUrlParam("Patient/123,Patient/456"));
	}

	@Test
	public void testNarrowCompartment_ConditionalCreate_Observation_InTransaction_Disabled() {
		ourNextAuthorizedList = new AuthorizedList()
			 .addCompartments("Patient/123", "Patient/456");
		myInterceptor.setNarrowConditionalUrls(false);

		BundleBuilder bb = new BundleBuilder(ourCtx);
		bb.addTransactionCreateEntry(new Observation().setStatus(Observation.ObservationStatus.FINAL)).conditional("Observation?status=final");

		myClient
			 .transaction()
			 .withBundle(bb.getBundle())
			 .execute();

		assertEquals("transaction", ourLastHitMethod);
		assertEquals("Observation", ourLastBundleRequest.getUrl());
		assertEquals("Observation?status=final", ourLastBundleRequest.getIfNoneExist());
	}

	@Test
	public void testNarrowCompartment_ConditionalUpdate_Patient() {
		ourNextAuthorizedList = new AuthorizedList()
			 .addResources("Patient/123", "Patient/456");

		myClient
			 .update()
			 .resource(new Patient().setActive(true))
			 .conditionalByUrl("Patient?active=true")
			 .execute();

		assertEquals("Patient.update", ourLastHitMethod);
		assertThat(ourLastConditionalUrl).startsWith("Patient?");
		assertThat(ourLastConditionalUrl).contains("active=true");
		assertThat(ourLastConditionalUrl).contains("_id=" + escapeUrlParam("Patient/123,Patient/456"));
	}

	@Test
	public void testNarrowCompartment_ConditionalUpdate_Patient_Disabled() {
		ourNextAuthorizedList = new AuthorizedList()
			 .addResources("Patient/123", "Patient/456");
		myInterceptor.setNarrowConditionalUrls(false);

		myClient
			 .update()
			 .resource(new Patient().setActive(true))
			 .conditionalByUrl("Patient?active=true")
			 .execute();

		assertEquals("Patient.update", ourLastHitMethod);
		assertEquals("Patient?active=true", ourLastConditionalUrl);
	}

	@Test
	public void testNarrowCompartment_ConditionalUpdate_Observation() {
		ourNextAuthorizedList = new AuthorizedList()
			 .addCompartments("Patient/123", "Patient/456");

		myClient
			 .update()
			 .resource(new Observation().setStatus(Observation.ObservationStatus.FINAL))
			 .conditionalByUrl("Observation?status=final")
			 .execute();

		assertEquals("Observation.update", ourLastHitMethod);
		assertThat(ourLastConditionalUrl).startsWith("Observation?");
		assertThat(ourLastConditionalUrl).contains("status=final");
		assertThat(ourLastConditionalUrl).contains("patient=" + escapeUrlParam("Patient/123,Patient/456"));
	}

	@Test
	public void testNarrowCompartment_ConditionalUpdate_Observation_InTransaction() {
		ourNextAuthorizedList = new AuthorizedList()
			 .addCompartments("Patient/123", "Patient/456");

		BundleBuilder bb = new BundleBuilder(ourCtx);
		bb.addTransactionUpdateEntry(new Observation().setStatus(Observation.ObservationStatus.FINAL)).conditional("Observation?status=final");

		myClient
			 .transaction()
			 .withBundle(bb.getBundle())
			 .execute();

		assertEquals("transaction", ourLastHitMethod);
		assertThat(ourLastBundleRequest.getUrl()).startsWith("Observation?");
		assertThat(ourLastBundleRequest.getUrl()).contains("status=final");
		assertThat(ourLastBundleRequest.getUrl()).contains("patient=" + escapeUrlParam("Patient/123,Patient/456"));
	}

	@Test
	public void testNarrowCompartment_ConditionalUpdate_Observation_InTransaction_NoConditionalUrl() {
		ourNextAuthorizedList = new AuthorizedList()
			 .addCompartments("Patient/123", "Patient/456");

		BundleBuilder bb = new BundleBuilder(ourCtx);
		bb.addTransactionUpdateEntry(new Observation().setStatus(Observation.ObservationStatus.FINAL).setId("Observation/ABC"));

		myClient
			 .transaction()
			 .withBundle(bb.getBundle())
			 .execute();

		assertEquals("transaction", ourLastHitMethod);
		assertEquals("Observation/ABC", ourLastBundleRequest.getUrl());
	}

	@Test
	public void testNarrowCompartment_ConditionalDelete_Patient() {
		ourNextAuthorizedList = new AuthorizedList()
			 .addResources("Patient/123", "Patient/456");

		myClient
			 .delete()
			 .resourceConditionalByUrl("Patient?active=true")
			 .execute();

		assertEquals("Patient.delete", ourLastHitMethod);
		assertThat(ourLastConditionalUrl).startsWith("Patient?");
		assertThat(ourLastConditionalUrl).contains("active=true");
		assertThat(ourLastConditionalUrl).contains("_id=" + escapeUrlParam("Patient/123,Patient/456"));
	}

	@Test
	public void testNarrowCompartment_ConditionalDelete_Observation() {
		ourNextAuthorizedList = new AuthorizedList()
			 .addCompartments("Patient/123", "Patient/456");

		myClient
			 .delete()
			 .resourceConditionalByUrl("Observation?status=final")
			 .execute();

		assertEquals("Observation.delete", ourLastHitMethod);
		assertThat(ourLastConditionalUrl).startsWith("Observation?");
		assertThat(ourLastConditionalUrl).contains("status=final");
		assertThat(ourLastConditionalUrl).contains("patient=" + escapeUrlParam("Patient/123,Patient/456"));
	}

	@Test
	public void testNarrowCompartment_ConditionalDelete_Observation_InTransaction() {
		ourNextAuthorizedList = new AuthorizedList()
			 .addCompartments("Patient/123", "Patient/456");

		BundleBuilder bb = new BundleBuilder(ourCtx);
		bb.addTransactionDeleteConditionalEntry("Observation?status=final");

		myClient
			 .transaction()
			 .withBundle(bb.getBundle())
			 .execute();

		assertEquals("transaction", ourLastHitMethod);
		assertThat(ourLastBundleRequest.getUrl()).startsWith("Observation?");
		assertThat(ourLastBundleRequest.getUrl()).contains("status=final");
		assertThat(ourLastBundleRequest.getUrl()).contains("patient=" + escapeUrlParam("Patient/123,Patient/456"));
	}

	@Test
	public void testNarrowCompartment_ConditionalPatch_Patient() {
		ourNextAuthorizedList = new AuthorizedList()
			 .addResources("Patient/123", "Patient/456");

		myClient
			 .patch()
			 .withFhirPatch(new Parameters())
			 .conditional(Patient.class)
			 .whereMap(Map.of("active", List.of("true")))
			 .execute();

		assertEquals("Patient.patch", ourLastHitMethod);
		assertThat(ourLastConditionalUrl).startsWith("Patient?");
		assertThat(ourLastConditionalUrl).contains("active=true");
		assertThat(ourLastConditionalUrl).contains("_id=" + escapeUrlParam("Patient/123,Patient/456"));
	}

	@Test
	public void testNarrowCompartment_ConditionalPatch_Observation() {
		ourNextAuthorizedList = new AuthorizedList()
			 .addCompartments("Patient/123", "Patient/456");

		myClient
			 .patch()
			 .withFhirPatch(new Parameters())
			 .conditional(Observation.class)
			 .whereMap(Map.of("status", List.of("final")))
			 .execute();

		assertEquals("Observation.patch", ourLastHitMethod);
		assertThat(ourLastConditionalUrl).startsWith("Observation?");
		assertThat(ourLastConditionalUrl).contains("status=final");
		assertThat(ourLastConditionalUrl).contains("patient=" + escapeUrlParam("Patient/123,Patient/456"));
	}

	@Test
	public void testNarrowCompartment_ConditionalPatch_Observation_InTransaction() {
		ourNextAuthorizedList = new AuthorizedList()
			 .addCompartments("Patient/123", "Patient/456");

		BundleBuilder bb = new BundleBuilder(ourCtx);
		bb.addTransactionFhirPatchEntry(new Parameters()).conditional("Observation?status=final");

		myClient
			 .transaction()
			 .withBundle(bb.getBundle())
			 .execute();

		assertEquals("transaction", ourLastHitMethod);
		assertThat(ourLastBundleRequest.getUrl()).startsWith("Observation?");
		assertThat(ourLastBundleRequest.getUrl()).contains("status=final");
		assertThat(ourLastBundleRequest.getUrl()).contains("patient=" + escapeUrlParam("Patient/123,Patient/456"));
	}

	@Test
	public void testTransactionWithNonConditionalDeleteNotModified() {
		ourNextAuthorizedList = new AuthorizedList()
			 .addCompartments("Patient/123", "Patient/456");

		BundleBuilder bb = new BundleBuilder(ourCtx);
		bb.addTransactionDeleteEntry("Patient", "ABC");

		myClient
			 .transaction()
			 .withBundle(bb.getBundle())
			 .execute();

		assertEquals("transaction", ourLastHitMethod);
		assertEquals("Patient/ABC", ourLastBundleRequest.getUrl());
	}


	private List<String> toStrings(BaseAndListParam<? extends IQueryParameterOr<?>> theParams) {
		List<? extends IQueryParameterOr<? extends IQueryParameterType>> valuesAsQueryTokens = theParams.getValuesAsQueryTokens();

		return valuesAsQueryTokens
			.stream()
			.map(IQueryParameterOr::getValuesAsQueryTokens)
			.map(t -> t
				.stream()
				.map(j -> j.getValueAsQueryToken(ourCtx))
				.collect(Collectors.joining(",")))
			.collect(Collectors.toList());
	}

	@Nonnull
	private static IValidationSupport.ValueSetExpansionOutcome createValueSetWithCodeCount(int theCount) {
		ValueSet valueSet = new ValueSet();
		valueSet.getExpansion().setTotal(theCount);
		for (int i = 0; i < theCount; i++) {
			valueSet
				.getExpansion()
				.addContains()
				.setSystem(FOO_CS_URL)
				.setCode(CODE_PREFIX + i);
		}

		return new IValidationSupport.ValueSetExpansionOutcome(valueSet);
	}

	@AfterAll
	public static void afterClassClearContext() throws Exception {
		TestUtil.randomizeLocaleAndTimezone();
	}

	@SuppressWarnings("unused")
	public static class DummyPatientResourceProvider implements IResourceProvider {

		@Override
		public Class<? extends IBaseResource> getResourceType() {
			return Patient.class;
		}

		@Search()
		public List<Resource> search(
			@OptionalParam(name = "_id") TokenAndListParam theIdParam,
			@OptionalParam(name = "name") StringAndListParam theNameParam
		) {
			ourLastHitMethod = "Patient.search";
			ourLastIdParam = theIdParam;
			ourLastNameParam = theNameParam;
			return ourReturn;
		}

		@Create
		public MethodOutcome create(@ResourceParam IBaseResource theResource, @ConditionalUrlParam String theConditionalUrl) {
			ourLastHitMethod = "Patient.create";
			ourLastConditionalUrl = theConditionalUrl;
			return new MethodOutcome(new IdType("Patient/123"), true);
		}

		@Update
		public MethodOutcome update(@ResourceParam IBaseResource theResource, @ConditionalUrlParam String theConditionalUrl) {
			ourLastHitMethod = "Patient.update";
			ourLastConditionalUrl = theConditionalUrl;
			return new MethodOutcome(new IdType("Patient/123"), true);
		}

		@Delete
		public MethodOutcome delete(@IdParam IIdType theId, @ConditionalUrlParam String theConditionalUrl) {
			ourLastHitMethod = "Patient.delete";
			ourLastConditionalUrl = theConditionalUrl;
			return new MethodOutcome(new IdType("Patient/123"), true);
		}

		@Patch
		public MethodOutcome patch(@IdParam IIdType theId, @ResourceParam IBaseResource theResource, @ConditionalUrlParam String theConditionalUrl, PatchTypeEnum thePatchType) {
			ourLastHitMethod = "Patient.patch";
			ourLastConditionalUrl = theConditionalUrl;
			return new MethodOutcome(new IdType("Patient/123"), true);
		}

	}

	@SuppressWarnings("unused")
	public static class DummyObservationResourceProvider implements IResourceProvider {

		@Override
		public Class<? extends IBaseResource> getResourceType() {
			return Observation.class;
		}


		@Search()
		public List<Resource> search(
			@OptionalParam(name = "_id") TokenAndListParam theIdParam,
			@OptionalParam(name = Observation.SP_SUBJECT) ReferenceAndListParam theSubjectParam,
			@OptionalParam(name = Observation.SP_PATIENT) ReferenceAndListParam thePatientParam,
			@OptionalParam(name = Observation.SP_PERFORMER) ReferenceAndListParam thePerformerParam,
			@OptionalParam(name = Observation.SP_CODE) TokenAndListParam theCodeParam
		) {
			ourLastHitMethod = "Observation.search";
			ourLastIdParam = theIdParam;
			ourLastSubjectParam = theSubjectParam;
			ourLastPatientParam = thePatientParam;
			ourLastPerformerParam = thePerformerParam;
			ourLastCodeParam = theCodeParam;
			return ourReturn;
		}

		@Create
		public MethodOutcome create(@ResourceParam IBaseResource theResource, @ConditionalUrlParam String theConditionalUrl) {
			ourLastHitMethod = "Observation.create";
			ourLastConditionalUrl = theConditionalUrl;
			return new MethodOutcome(new IdType("Observation/123"), true);
		}

		@Update
		public MethodOutcome update(@ResourceParam IBaseResource theResource, @ConditionalUrlParam String theConditionalUrl) {
			ourLastHitMethod = "Observation.update";
			ourLastConditionalUrl = theConditionalUrl;
			return new MethodOutcome(new IdType("Observation/123"), true);
		}

		@Delete
		public MethodOutcome delete(@IdParam IIdType theId, @ConditionalUrlParam String theConditionalUrl) {
			ourLastHitMethod = "Observation.delete";
			ourLastConditionalUrl = theConditionalUrl;
			return new MethodOutcome(new IdType("Observation/123"), true);
		}

		@Patch
		public MethodOutcome patch(@IdParam IIdType theId, @ResourceParam IBaseResource theResource, @ConditionalUrlParam String theConditionalUrl, PatchTypeEnum thePatchType) {
			ourLastHitMethod = "Observation.patch";
			ourLastConditionalUrl = theConditionalUrl;
			return new MethodOutcome(new IdType("Observation/123"), true);
		}

	}

	public static class DummySystemProvider {
		@Transaction
		public Bundle transaction(@TransactionParam Bundle theInput) {
			ourLastHitMethod = "transaction";
			ourLastBundleRequest = theInput.getEntry().get(0).getRequest();
			return theInput;
		}
	}

	private static class MySearchNarrowingInterceptor extends SearchNarrowingInterceptor {
		@Override
		protected AuthorizedList buildAuthorizedList(RequestDetails theRequestDetails) {
			if (ourNextAuthorizedList == null) {
				return null;
			}
			return ourNextAuthorizedList;
		}
	}


}
