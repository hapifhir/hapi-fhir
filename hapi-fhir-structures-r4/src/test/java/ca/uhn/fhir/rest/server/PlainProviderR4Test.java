package ca.uhn.fhir.rest.server;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.annotation.Count;
import ca.uhn.fhir.rest.annotation.History;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.annotation.Since;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.provider.HashMapResourceProvider;
import ca.uhn.fhir.test.utilities.HttpClientExtension;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import ca.uhn.fhir.util.TestUtil;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.HumanName;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.InstantType;
import org.hl7.fhir.r4.model.IntegerType;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Resource;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class PlainProviderR4Test {

	private static final FhirContext ourCtx = FhirContext.forR4Cached();
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(PlainProviderR4Test.class);

	@RegisterExtension
	public RestfulServerExtension ourServer = new RestfulServerExtension(ourCtx)
		 .registerProvider(new HashMapResourceProvider<>(ourCtx, Observation.class))
		 .withPagingProvider(new FifoMemoryPagingProvider(100))
		 .setDefaultResponseEncoding(EncodingEnum.XML)
		 .withServletPath("/fhir/context/*");

	@RegisterExtension
	private HttpClientExtension ourClient = new HttpClientExtension();

	@Test
	public void testGlobalHistory() throws Exception {
		GlobalHistoryProvider provider = new GlobalHistoryProvider();
		ourServer.registerProvider(provider);

		String baseUri = ourServer.getBaseUrl();
		HttpResponse status = ourClient.execute(new HttpGet(baseUri + "/_history?_since=2012-01-02T00%3A01%3A02&_count=12"));

		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());
		ourLog.info("Response was:\n{}", responseContent);
		assertEquals(200, status.getStatusLine().getStatusCode());
		Bundle bundle = ourCtx.newXmlParser().parseResource(Bundle.class, responseContent);
		assertThat(bundle.getEntry()).hasSize(3);
		
		assertThat(provider.myLastSince.getValueAsString()).startsWith("2012-01-02T00:01:02");
		assertEquals("12", provider.myLastCount.getValueAsString());

		status = ourClient.execute(new HttpGet(baseUri + "/_history?&_count=12"));
		responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());
		assertEquals(200, status.getStatusLine().getStatusCode());
		bundle = ourCtx.newXmlParser().parseResource(Bundle.class, responseContent);
		assertThat(bundle.getEntry()).hasSize(3);
		assertNull(provider.myLastSince);
		assertEquals("12", provider.myLastCount.getValueAsString());
		
		status =ourClient.execute(new HttpGet(baseUri + "/_history?_since=2012-01-02T00%3A01%3A02"));
		responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());
		assertEquals(200, status.getStatusLine().getStatusCode());
		bundle = ourCtx.newXmlParser().parseResource(Bundle.class, responseContent);
		assertThat(bundle.getEntry()).hasSize(3);
		assertThat(provider.myLastSince.getValueAsString()).startsWith("2012-01-02T00:01:02");
		assertNull(provider.myLastCount);
	}

	@Test
	public void testGlobalHistoryNoParams() throws Exception {
		GlobalHistoryProvider provider = new GlobalHistoryProvider();
		ourServer.registerProvider(provider);

		String baseUri = ourServer.getBaseUrl();
		CloseableHttpResponse status = ourClient.execute(new HttpGet(baseUri + "/_history"));
		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());
		assertEquals(200, status.getStatusLine().getStatusCode());
		Bundle bundle = ourCtx.newXmlParser().parseResource(Bundle.class, responseContent);
		assertThat(bundle.getEntry()).hasSize(3);
		assertNull(provider.myLastSince);
		assertNull(provider.myLastCount);
		
	}

	@Test
	public void testSearchByParamIdentifier() throws Exception {
		ourServer.registerProvider(new SearchProvider());

		String baseUri = ourServer.getBaseUrl();
		String uri = baseUri + "/Patient?identifier=urn:hapitest:mrns%7C00001";
		HttpGet httpGet = new HttpGet(uri);
		try (CloseableHttpResponse status = ourClient.execute(httpGet)) {

			String responseContent = IOUtils.toString(status.getEntity().getContent());
			IOUtils.closeQuietly(status.getEntity().getContent());
			ourLog.info("Response was:\n{}", responseContent);

			assertEquals(200, status.getStatusLine().getStatusCode());
			Bundle bundle = ourCtx.newXmlParser().parseResource(Bundle.class, responseContent);

			assertThat(bundle.getEntry()).hasSize(1);

			Patient patient = (Patient) bundle.getEntry().get(0).getResource();
			assertEquals("PatientOne", patient.getName().get(0).getGiven().get(0).getValue());

			assertEquals(uri.replace(":hapitest:", "%3Ahapitest%3A"), bundle.getLink("self").getUrl());
		}

	}
	
	@AfterAll
	public static void afterClassClearContext() {
		TestUtil.randomizeLocaleAndTimezone();
	}

	private static Organization createOrganization() {
		Organization retVal = new Organization();
		retVal.setId("1");
		retVal.addIdentifier();
		retVal.getIdentifier().get(0).setUse(Identifier.IdentifierUse.OFFICIAL);
		retVal.getIdentifier().get(0).setSystem(("urn:hapitest:mrns"));
		retVal.getIdentifier().get(0).setValue("00001");
		retVal.setName("Test Org");
		return retVal;
	}

	private static Patient createPatient() {
		Patient patient = new Patient();
		patient.setId("1");
		patient.addIdentifier();
		patient.getIdentifier().get(0).setUse(Identifier.IdentifierUse.OFFICIAL);
		patient.getIdentifier().get(0).setSystem(("urn:hapitest:mrns"));
		patient.getIdentifier().get(0).setValue("00001");
		patient.addName();
		patient.getName().get(0).setFamily("Test");
		patient.getName().get(0).addGiven("PatientOne");
		patient.getGenderElement().setValueAsString("male");
		return patient;
	}

	public static class GlobalHistoryProvider {

		private IntegerType myLastCount;
		private InstantType myLastSince;

		@History
		public List<IBaseResource> getGlobalHistory(@Since InstantType theSince, @Count IntegerType theCount) {
			myLastSince = theSince;
			myLastCount = theCount;
			ArrayList<IBaseResource> retVal = new ArrayList<>();

			Resource p = createPatient();
			p.setId(new IdType("1"));
			p.getMeta().setVersionId("A");
			p.getMeta().getLastUpdatedElement().setValueAsString("2012-01-01T01:00:01");
			retVal.add(p);

			p = createPatient();
			p.setId(new IdType("1"));
			p.getMeta().setVersionId("B");
			p.getMeta().getLastUpdatedElement().setValueAsString("2012-01-01T01:00:03");
			retVal.add(p);

			p = createOrganization();
			p.setId(new IdType("1"));
			p.getMeta().setVersionId("A");
			p.getMeta().getLastUpdatedElement().setValueAsString("2013-01-01T01:00:01");
			retVal.add(p);

			return retVal;
		}

	}


	public static class SearchProvider {

		@Search(type = Patient.class)
		public Patient findPatient(@RequiredParam(name = Patient.SP_IDENTIFIER) TokenParam theIdentifier) {
			for (Patient next : getIdToPatient().values()) {
				for (Identifier nextId : next.getIdentifier()) {
					if (nextId.getSystem().equals(theIdentifier.getSystem()) && nextId.getValue().equals(theIdentifier.getValue())) {
						return next;
					}
				}
			}
			return null;
		}

		public Map<String, Patient> getIdToPatient() {
			Map<String, Patient> idToPatient = new HashMap<>();
			{
				Patient patient = createPatient();
				idToPatient.put("1", patient);
			}
			{
				Patient patient = new Patient();
				patient.getIdentifier().add(new Identifier());
				patient.getIdentifier().get(0).setUse(Identifier.IdentifierUse.OFFICIAL);
				patient.getIdentifier().get(0).setSystem(("urn:hapitest:mrns"));
				patient.getIdentifier().get(0).setValue("00002");
				patient.getName().add(new HumanName());
				patient.getName().get(0).setFamily("Test");
				patient.getName().get(0).addGiven("PatientTwo");
				patient.getGenderElement().setValueAsString("female");
				idToPatient.put("2", patient);
			}
			return idToPatient;
		}

		/**
		 * Retrieve the resource by its identifier
		 * 
		 * @param theId
		 *            The resource identity
		 * @return The resource
		 */
		@Read(type = Patient.class)
		public Patient getPatientById(@IdParam IdType theId) {
			return getIdToPatient().get(theId.getValue());
		}

	}

}
