package ca.uhn.fhir.rest.server;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.rest.annotation.ConditionalUrlParam;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.OptionalParam;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.annotation.Update;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.test.utilities.HttpClientExtension;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import ca.uhn.fhir.util.TestUtil;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UpdateDstu2Test {
	private static final FhirContext ourCtx = FhirContext.forDstu2Cached();
	private static String ourLastConditionalUrl;
	private static IdDt ourLastId;
	private static IdDt ourLastIdParam;
	private static boolean ourLastRequestWasSearch;
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(UpdateDstu2Test.class);
	private static InstantDt ourSetLastUpdated;

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
		ourLastId = null;
		ourLastConditionalUrl = null;
		ourLastIdParam = null;
		ourLastRequestWasSearch = false;
	}


	
	@Test
	public void testSearchStillWorks() throws Exception {

		Patient patient = new Patient();
		patient.addIdentifier().setValue("002");

		HttpGet httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient?_pretty=true");

		HttpResponse status = ourClient.execute(httpGet);

		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Response was:\n{}", responseContent);

		assertTrue(ourLastRequestWasSearch);
		assertNull(ourLastId);
		assertNull(ourLastIdParam);
		assertNull(ourLastConditionalUrl);

	}

	@Test
	public void testUpdateWithConditionalUrl() throws Exception {

		Patient patient = new Patient();
		patient.addIdentifier().setValue("002");

		HttpPut httpPost = new HttpPut(ourServer.getBaseUrl() + "/Patient?identifier=system%7C001");
		httpPost.setEntity(new StringEntity(ourCtx.newXmlParser().encodeResourceToString(patient), ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));

		HttpResponse status = ourClient.execute(httpPost);

		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Response was:\n{}", responseContent);

		assertEquals(200, status.getStatusLine().getStatusCode());
		assertNull(status.getFirstHeader("location"));
		assertEquals(ourServer.getBaseUrl() + "/Patient/001/_history/002", status.getFirstHeader("content-location").getValue());

		assertNull(ourLastId.getValue());
		assertNull(ourLastIdParam);
		assertEquals("Patient?identifier=system%7C001", ourLastConditionalUrl);

	}


	@Test
	public void testUpdateWithoutConditionalUrl() throws Exception {

		Patient patient = new Patient();
		patient.setId("2");
		patient.addIdentifier().setValue("002");

		HttpPut httpPost = new HttpPut(ourServer.getBaseUrl() + "/Patient/2");
		httpPost.setEntity(new StringEntity(ourCtx.newXmlParser().encodeResourceToString(patient), ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));

		HttpResponse status = ourClient.execute(httpPost);

		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Response was:\n{}", responseContent);

		assertEquals(200, status.getStatusLine().getStatusCode());
		assertNull(status.getFirstHeader("location"));
		assertEquals(ourServer.getBaseUrl() + "/Patient/001/_history/002", status.getFirstHeader("content-location").getValue());

		assertEquals("Patient/2", ourLastId.toUnqualified().getValue());
		assertEquals("Patient/2", ourLastIdParam.toUnqualified().getValue());
		assertNull(ourLastConditionalUrl);

	}

	
	@AfterAll
	public static void afterClassClearContext() throws Exception {
		TestUtil.randomizeLocaleAndTimezone();
	}
		
	
	
	public static class PatientProvider implements IResourceProvider {

		@Override
		public Class<? extends IResource> getResourceType() {
			return Patient.class;
		}

		@Search
		public List<IResource> search(@OptionalParam(name="foo") StringDt theString) {
			ourLastRequestWasSearch = true;
			return new ArrayList<IResource>();
		}
		
		@Update()
		public MethodOutcome updatePatient(@ResourceParam Patient thePatient, @ConditionalUrlParam String theConditional, @IdParam IdDt theIdParam) {
			ourLastConditionalUrl = theConditional;
			ourLastId = thePatient.getId();
			ourLastIdParam = theIdParam;
			MethodOutcome retVal = new MethodOutcome(new IdDt("Patient/001/_history/002"));
			
			ResourceMetadataKeyEnum.UPDATED.put(thePatient, ourSetLastUpdated);
			
			retVal.setResource(thePatient);
			return retVal;
		}

	}

}
