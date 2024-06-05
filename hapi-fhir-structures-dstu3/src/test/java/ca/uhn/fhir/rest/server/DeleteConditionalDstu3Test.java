package ca.uhn.fhir.rest.server;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNull;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.annotation.ConditionalUrlParam;
import ca.uhn.fhir.rest.annotation.Delete;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.test.utilities.HttpClientExtension;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import ca.uhn.fhir.util.TestUtil;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import static org.assertj.core.api.Assertions.assertThat;

public class DeleteConditionalDstu3Test {
	private static final FhirContext ourCtx = FhirContext.forDstu3Cached();
	private static IGenericClient ourHapiClient;
	private static String ourLastConditionalUrl;
	private static IdType ourLastIdParam;
	private static boolean ourLastRequestWasDelete;
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(DeleteConditionalDstu3Test.class);

	@RegisterExtension
	private RestfulServerExtension ourServer  = new RestfulServerExtension(ourCtx)
		 .setDefaultResponseEncoding(EncodingEnum.XML)
		 .registerProvider(new PatientProvider())
		 .withPagingProvider(new FifoMemoryPagingProvider(100))
		 .setDefaultPrettyPrint(false);

	@RegisterExtension
	private HttpClientExtension ourClient = new HttpClientExtension();


	@BeforeEach
	public void before() {
		ourLastConditionalUrl = null;
		ourLastIdParam = null;
		ourLastRequestWasDelete = false;
	}


	
	@Test
	public void testSearchStillWorks() throws Exception {

		Patient patient = new Patient();
		patient.addIdentifier().setValue("002");

		ourServer
			 .getFhirClient()
			.delete()
			.resourceConditionalByType(Patient.class)
			.where(Patient.IDENTIFIER.exactly().systemAndIdentifier("SOMESYS","SOMEID"))
			.execute();

		assertTrue(ourLastRequestWasDelete);
		assertNull(ourLastIdParam);
		assertEquals("Patient?identifier=SOMESYS%7CSOMEID", ourLastConditionalUrl);

	}


	
	@AfterAll
	public static void afterClassClearContext() throws Exception {
		TestUtil.randomizeLocaleAndTimezone();
	}
		
	
	public static class PatientProvider implements IResourceProvider {

		@Delete()
		public MethodOutcome deletePatient(@IdParam IdType theIdParam, @ConditionalUrlParam String theConditional) {
			ourLastRequestWasDelete = true;
			ourLastConditionalUrl = theConditional;
			ourLastIdParam = theIdParam;
			return new MethodOutcome(new IdType("Patient/001/_history/002"));
		}
		
		@Override
		public Class<? extends IBaseResource> getResourceType() {
			return Patient.class;
		}

	}

}
