package ca.uhn.fhir.jpa.provider.r4;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.model.entity.ModelConfig;
import ca.uhn.fhir.parser.StrictErrorHandler;
import ca.uhn.fhir.rest.client.interceptor.CapturingInterceptor;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.DateTimeType;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Observation.ObservationStatus;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Quantity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;


public class ResourceProviderSearchModifierR4Test extends BaseResourceProviderR4Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ResourceProviderSearchModifierR4Test.class);
	private CapturingInterceptor myCapturingInterceptor = new CapturingInterceptor();

	@Override
	@AfterEach
	public void after() throws Exception {
		super.after();

		myDaoConfig.setAllowMultipleDelete(new DaoConfig().isAllowMultipleDelete());
		myDaoConfig.setAllowExternalReferences(new DaoConfig().isAllowExternalReferences());
		myDaoConfig.setReuseCachedSearchResultsForMillis(new DaoConfig().getReuseCachedSearchResultsForMillis());
		myDaoConfig.setCountSearchResultsUpTo(new DaoConfig().getCountSearchResultsUpTo());
		myDaoConfig.setSearchPreFetchThresholds(new DaoConfig().getSearchPreFetchThresholds());
		myDaoConfig.setAllowContainsSearches(new DaoConfig().isAllowContainsSearches());
		myDaoConfig.setIndexMissingFields(new DaoConfig().getIndexMissingFields());

		myModelConfig.setIndexIdentifierOfType(new ModelConfig().isIndexIdentifierOfType());

		myClient.unregisterInterceptor(myCapturingInterceptor);
	}

	@BeforeEach
	@Override
	public void before() throws Exception {
		super.before();
		myFhirContext.setParserErrorHandler(new StrictErrorHandler());

		myDaoConfig.setAllowMultipleDelete(true);
		myClient.registerInterceptor(myCapturingInterceptor);
		myDaoConfig.setSearchPreFetchThresholds(new DaoConfig().getSearchPreFetchThresholds());
	}

	@BeforeEach
	public void beforeDisableResultReuse() {
		myDaoConfig.setReuseCachedSearchResultsForMillis(null);
	}

	@Test
	public void testSearch_SingleCode_not_modifier() throws Exception {

		List<IIdType> obsList = createObs(10, false);
		
		String uri = ourServerBase + "/Observation?code:not=2345-3";
		List<String> ids = searchAndReturnUnqualifiedVersionlessIdValues(uri);
		
		assertEquals(9, ids.size());
		assertEquals(obsList.get(0).toString(), ids.get(0));
		assertEquals(obsList.get(1).toString(), ids.get(1));
		assertEquals(obsList.get(2).toString(), ids.get(2));
		assertEquals(obsList.get(4).toString(), ids.get(3));
		assertEquals(obsList.get(5).toString(), ids.get(4));
		assertEquals(obsList.get(6).toString(), ids.get(5));
		assertEquals(obsList.get(7).toString(), ids.get(6));
		assertEquals(obsList.get(8).toString(), ids.get(7));
		assertEquals(obsList.get(9).toString(), ids.get(8));
	}

	@Test
	public void testSearch_SingleCode_multiple_not_modifier() throws Exception {

		List<IIdType> obsList = createObs(10, false);
		
		String uri = ourServerBase + "/Observation?code:not=2345-3&code:not=2345-7&code:not=2345-9";
		List<String> ids = searchAndReturnUnqualifiedVersionlessIdValues(uri);
		
		assertEquals(7, ids.size());
		assertEquals(obsList.get(0).toString(), ids.get(0));
		assertEquals(obsList.get(1).toString(), ids.get(1));
		assertEquals(obsList.get(2).toString(), ids.get(2));
		assertEquals(obsList.get(4).toString(), ids.get(3));
		assertEquals(obsList.get(5).toString(), ids.get(4));
		assertEquals(obsList.get(6).toString(), ids.get(5));
		assertEquals(obsList.get(8).toString(), ids.get(6));
	}
	
	@Test
	public void testSearch_SingleCode_mix_modifier() throws Exception {

		List<IIdType> obsList = createObs(10, false);
		
		// Observation?code:not=2345-3&code:not=2345-7&code:not=2345-9
		// slower than Observation?code:not=2345-3&code=2345-7&code:not=2345-9
		String uri = ourServerBase + "/Observation?code:not=2345-3&code=2345-7&code:not=2345-9";
		List<String> ids = searchAndReturnUnqualifiedVersionlessIdValues(uri);
		
		assertEquals(1, ids.size());
		assertEquals(obsList.get(7).toString(), ids.get(0));
	}
	
	@Test
	public void testSearch_SingleCode_or_not_modifier() throws Exception {

		List<IIdType> obsList = createObs(10, false);
		
		String uri = ourServerBase + "/Observation?code:not=2345-3,2345-7,2345-9";
		List<String> ids = searchAndReturnUnqualifiedVersionlessIdValues(uri);
		
		assertEquals(7, ids.size());
		assertEquals(obsList.get(0).toString(), ids.get(0));
		assertEquals(obsList.get(1).toString(), ids.get(1));
		assertEquals(obsList.get(2).toString(), ids.get(2));
		assertEquals(obsList.get(4).toString(), ids.get(3));
		assertEquals(obsList.get(5).toString(), ids.get(4));
		assertEquals(obsList.get(6).toString(), ids.get(5));
		assertEquals(obsList.get(8).toString(), ids.get(6));
	}
	
	@Test
	public void testSearch_MultiCode_not_modifier() throws Exception {

		List<IIdType> obsList = createObs(10, true);
		
		String uri = ourServerBase + "/Observation?code:not=2345-3";
		List<String> ids = searchAndReturnUnqualifiedVersionlessIdValues(uri);
		
		assertEquals(8, ids.size());
		assertEquals(obsList.get(0).toString(), ids.get(0));
		assertEquals(obsList.get(1).toString(), ids.get(1));
		assertEquals(obsList.get(4).toString(), ids.get(2));
		assertEquals(obsList.get(5).toString(), ids.get(3));
		assertEquals(obsList.get(6).toString(), ids.get(4));
		assertEquals(obsList.get(7).toString(), ids.get(5));
		assertEquals(obsList.get(8).toString(), ids.get(6));
		assertEquals(obsList.get(9).toString(), ids.get(7));
	}
	
	@Test
	public void testSearch_MultiCode_multiple_not_modifier() throws Exception {

		List<IIdType> obsList = createObs(10, true);
		
		String uri = ourServerBase + "/Observation?code:not=2345-3&code:not=2345-4";
		List<String> ids = searchAndReturnUnqualifiedVersionlessIdValues(uri);
		
		assertEquals(7, ids.size());
		assertEquals(obsList.get(0).toString(), ids.get(0));
		assertEquals(obsList.get(1).toString(), ids.get(1));
		assertEquals(obsList.get(5).toString(), ids.get(2));
		assertEquals(obsList.get(6).toString(), ids.get(3));
		assertEquals(obsList.get(7).toString(), ids.get(4));
		assertEquals(obsList.get(8).toString(), ids.get(5));
		assertEquals(obsList.get(9).toString(), ids.get(6));
	}
	
	@Test
	public void testSearch_MultiCode_mix_modifier() throws Exception {

		List<IIdType> obsList = createObs(10, true);
		
		String uri = ourServerBase + "/Observation?code:not=2345-3&code=2345-7&code:not=2345-9";
		List<String> ids = searchAndReturnUnqualifiedVersionlessIdValues(uri);
		
		assertEquals(2, ids.size());
		assertEquals(obsList.get(6).toString(), ids.get(0));
		assertEquals(obsList.get(7).toString(), ids.get(1));
	}
	
	@Test
	public void testSearch_MultiCode_or_not_modifier() throws Exception {

		List<IIdType> obsList = createObs(10, true);
		
		String uri = ourServerBase + "/Observation?code:not=2345-3,2345-7,2345-9";
		List<String> ids = searchAndReturnUnqualifiedVersionlessIdValues(uri);
		
		assertEquals(4, ids.size());
		assertEquals(obsList.get(0).toString(), ids.get(0));
		assertEquals(obsList.get(1).toString(), ids.get(1));
		assertEquals(obsList.get(4).toString(), ids.get(2));
		assertEquals(obsList.get(5).toString(), ids.get(3));
	}

	@Test
	public void testSearch_OfType_PartialBlocked() {
		myModelConfig.setIndexIdentifierOfType(true);

		try {
			String uri = ourServerBase + "/Patient?identifier:of-type=A";
			myClient.search().byUrl(uri).execute();
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("HTTP 400 Bad Request: " + Msg.code(2013) +  "Invalid parameter value for :of-type query", e.getMessage());
		}

		try {
			String uri = ourServerBase + "/Patient?identifier:of-type=A|B";
			myClient.search().byUrl(uri).execute();
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("HTTP 400 Bad Request: " + Msg.code(2014) + "Invalid parameter value for :of-type query", e.getMessage());
		}

		try {
			String uri = ourServerBase + "/Patient?identifier:of-type=A|B|";
			myClient.search().byUrl(uri).execute();
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("HTTP 400 Bad Request: " + Msg.code(2014) + "Invalid parameter value for :of-type query", e.getMessage());
		}

		try {
			String uri = ourServerBase + "/Patient?identifier:of-type=|B|C";
			myClient.search().byUrl(uri).execute();
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("HTTP 400 Bad Request: " + Msg.code(2013) + "Invalid parameter value for :of-type query", e.getMessage());
		}

		try {
			String uri = ourServerBase + "/Patient?identifier:of-type=||C";
			myClient.search().byUrl(uri).execute();
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("HTTP 400 Bad Request: " + Msg.code(2013) + "Invalid parameter value for :of-type query", e.getMessage());
		}

		try {
			String uri = ourServerBase + "/Patient?identifier:of-type=|B|";
			myClient.search().byUrl(uri).execute();
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("HTTP 400 Bad Request: " + Msg.code(2013) + "Invalid parameter value for :of-type query", e.getMessage());
		}

		try {
			String uri = ourServerBase + "/Patient?identifier:of-type=A||";
			myClient.search().byUrl(uri).execute();
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("HTTP 400 Bad Request: " + Msg.code(2014) + "Invalid parameter value for :of-type query", e.getMessage());
		}

		try {
			String uri = ourServerBase + "/Patient?identifier:of-type=||";
			myClient.search().byUrl(uri).execute();
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("HTTP 400 Bad Request: " + Msg.code(2013) + "Invalid parameter value for :of-type query", e.getMessage());
		}

	}
	
	private List<String> searchAndReturnUnqualifiedVersionlessIdValues(String uri) throws IOException {
		List<String> ids;
		HttpGet get = new HttpGet(uri);

		try (CloseableHttpResponse response = ourHttpClient.execute(get)) {
			String resp = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info("Response was: {}", resp);
			Bundle bundle = myFhirContext.newXmlParser().parseResource(Bundle.class, resp);
			ourLog.info("Bundle: \n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(bundle));
			ids = toUnqualifiedVersionlessIdValues(bundle);
		}
		return ids;
	}

	private List<IIdType> createObs(int obsNum, boolean isMultiple) {
		
		Patient patient = new Patient();
		patient.addIdentifier().setSystem("urn:system").setValue("001");
		patient.addName().setFamily("Tester").addGiven("Joe");
		IIdType pid = myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless();
		
		List<IIdType> obsIds = new ArrayList<>();
		IIdType obsId = null;
		for (int i=0; i<obsNum; i++) {
			Observation obs = new Observation();
			obs.setStatus(ObservationStatus.FINAL);
			obs.getSubject().setReferenceElement(pid);
			obs.setEffective(new DateTimeType("2001-02-01"));
		
			CodeableConcept cc = obs.getCode();
			cc.addCoding().setCode("2345-"+i).setSystem("http://loinc.org");
			obs.setValue(new Quantity().setValue(200));	

			if (isMultiple) {
				cc = obs.getCode();
				cc.addCoding().setCode("2345-"+(i+1)).setSystem("http://loinc.org");
				obs.setValue(new Quantity().setValue(300));	
			}

			obsId = myObservationDao.create(obs).getId().toUnqualifiedVersionless();
			obsIds.add(obsId);
		}
		
		return obsIds;
	}
}
