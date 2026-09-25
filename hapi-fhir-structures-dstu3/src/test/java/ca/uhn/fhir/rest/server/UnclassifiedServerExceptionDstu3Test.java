package ca.uhn.fhir.rest.server;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.server.exceptions.UnclassifiedServerFailureException;
import ca.uhn.fhir.test.utilities.HttpTestResponse;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import ca.uhn.fhir.util.TestUtil;
import org.hl7.fhir.dstu3.model.OperationOutcome;
import org.hl7.fhir.dstu3.model.OperationOutcome.IssueType;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class UnclassifiedServerExceptionDstu3Test {

	private static final FhirContext ourCtx = FhirContext.forDstu3Cached();
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(UnclassifiedServerExceptionDstu3Test.class);
	public static UnclassifiedServerFailureException ourException;

	@RegisterExtension
	private RestfulServerExtension ourServer  = new RestfulServerExtension(ourCtx)
		 .setDefaultResponseEncoding(EncodingEnum.XML)
		 .registerProvider(new DummyPatientResourceProvider())
		 .withPagingProvider(new FifoMemoryPagingProvider(10))
		 .setDefaultPrettyPrint(false);

	@Test
	public void testSearch() throws Exception {
		
		OperationOutcome operationOutcome = new OperationOutcome();
		operationOutcome.addIssue().setCode(IssueType.BUSINESSRULE);
		ourException = new UnclassifiedServerFailureException(477, "SOME MESSAGE", operationOutcome);

		HttpTestResponse response = ourServer.fhirRequest("/Patient").get();
		String responseContent = response.getBody();
		ourLog.info("HTTP {} {}", response.getStatusCode(), response.getReasonPhrase());
		ourLog.info(responseContent);
		response.assertStatus(477);
		//assertEquals("SOME MESSAGE", status.getStatusLine().getReasonPhrase());
		assertThat(responseContent).contains("business-rule");

	}

	@AfterAll
	public static void afterClassClearContext() throws Exception {
		TestUtil.randomizeLocaleAndTimezone();
	}


	public static class DummyPatientResourceProvider implements IResourceProvider {

		@Override
		public Class<? extends IBaseResource> getResourceType() {
			return Patient.class;
		}

		@Search()
		public List<Patient> search() {
			throw ourException;
		}

	}

}
