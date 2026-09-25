package ca.uhn.fhir.rest.server;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.rest.annotation.ConditionalUrlParam;
import ca.uhn.fhir.rest.annotation.Create;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.OptionalParam;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.test.utilities.HttpTestResponse;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import ca.uhn.fhir.util.TestUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CreateConditionalTest {

	private static final FhirContext ourCtx = FhirContext.forDstu2Cached();
	private static String ourLastConditionalUrl;
	private static IdDt ourLastId;
	private static IdDt ourLastIdParam;
	private static boolean ourLastRequestWasSearch;
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(CreateConditionalTest.class);

	@RegisterExtension
	public static final RestfulServerExtension ourServer  = new RestfulServerExtension(ourCtx)
		.setDefaultResponseEncoding(EncodingEnum.XML)
		.registerProvider(new PatientProvider())
		.withPagingProvider(new FifoMemoryPagingProvider(100))
		.setDefaultPrettyPrint(false);

	@BeforeEach
	public void before() {
		ourLastId = null;
		ourLastConditionalUrl = null;
		ourLastIdParam = null;
		ourLastRequestWasSearch = false;
	}


	
	@Test
	public void testCreateWithConditionalUrl() throws Exception {

		Patient patient = new Patient();
		patient.addIdentifier().setValue("002");

		HttpTestResponse status = ourServer.fhirRequest("/Patient").withHeader(Constants.HEADER_IF_NONE_EXIST, "Patient?identifier=system%7C001")
			.post(ourCtx.newXmlParser().encodeResourceToString(patient), Constants.CT_FHIR_XML);

		String responseContent = status.getBody();

		ourLog.info("Response was:\n{}", responseContent);

		status.assertStatus(201);
		assertEquals(ourServer.getBaseUrl() + "/Patient/001/_history/002", status.getHeader("location"));
		assertEquals(ourServer.getBaseUrl() + "/Patient/001/_history/002", status.getHeader("content-location"));

		assertNull(ourLastId.getValue());
		assertNull(ourLastIdParam);
		assertEquals("Patient?identifier=system%7C001", ourLastConditionalUrl);

	}

	@Test
	public void testCreateWithoutConditionalUrl() throws Exception {

		Patient patient = new Patient();
		patient.addIdentifier().setValue("002");

		HttpTestResponse status = ourServer.fhirRequest("/Patient?_format=true&_pretty=true").post(ourCtx.newXmlParser().encodeResourceToString(patient), Constants.CT_FHIR_XML);

		String responseContent = status.getBody();

		ourLog.info("Response was:\n{}", responseContent);

		status.assertStatus(201);
		assertEquals(ourServer.getBaseUrl() + "/Patient/001/_history/002", status.getHeader("location"));
		assertEquals(ourServer.getBaseUrl() + "/Patient/001/_history/002", status.getHeader("content-location"));

		assertNull(ourLastId.toUnqualified().getValue());
		assertNull(ourLastIdParam);
		assertNull(ourLastConditionalUrl);

	}

	@Test
	public void testSearchStillWorks() throws Exception {

		Patient patient = new Patient();
		patient.addIdentifier().setValue("002");

		String responseContent = ourServer.fhirRequest("/Patient?_pretty=true").get().getBody();

		ourLog.info("Response was:\n{}", responseContent);

		assertTrue(ourLastRequestWasSearch);
		assertNull(ourLastId);
		assertNull(ourLastIdParam);
		assertNull(ourLastConditionalUrl);

	}

	
	@AfterAll
	public static void afterClassClearContext() throws Exception {
		TestUtil.randomizeLocaleAndTimezone();
	}
		
	
	public static class PatientProvider implements IResourceProvider {

		@Create()
		public MethodOutcome createPatient(@ResourceParam Patient thePatient, @ConditionalUrlParam String theConditional, @IdParam IdDt theIdParam) {
			ourLastConditionalUrl = theConditional;
			ourLastId = thePatient.getId();
			ourLastIdParam = theIdParam;
			return new MethodOutcome(new IdDt("Patient/001/_history/002"));
		}

		@Override
		public Class<? extends IResource> getResourceType() {
			return Patient.class;
		}
		
		@Search
		public List<IResource> search(@OptionalParam(name="foo") StringDt theString) {
			ourLastRequestWasSearch = true;
			return new ArrayList<>();
		}

	}

}
