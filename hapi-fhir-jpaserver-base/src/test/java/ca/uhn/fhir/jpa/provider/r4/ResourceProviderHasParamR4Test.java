package ca.uhn.fhir.jpa.provider.r4;

import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.parser.StrictErrorHandler;
import ca.uhn.fhir.rest.client.interceptor.CapturingInterceptor;
import ca.uhn.fhir.util.UrlUtil;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.DateTimeType;
import org.hl7.fhir.r4.model.Device;
import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Observation.ObservationComponentComponent;
import org.hl7.fhir.r4.model.Observation.ObservationStatus;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Period;
import org.hl7.fhir.r4.model.Quantity;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.StringType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;


public class ResourceProviderHasParamR4Test extends BaseResourceProviderR4Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ResourceProviderHasParamR4Test.class);
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
		//myDaoConfig.setUseLegacySearchBuilder(true);
	}

	@BeforeEach
	public void beforeDisableResultReuse() {
		myDaoConfig.setReuseCachedSearchResultsForMillis(null);
	}


	@Test
	public void testHasParameter() throws Exception {
		
		IIdType pid0;
		{
			Patient patient = new Patient();
			patient.addIdentifier().setSystem("urn:system").setValue("001");
			patient.addName().setFamily("Tester").addGiven("Joe");
			pid0 = myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless();
		}
		{
			Patient patient = new Patient();
			patient.addIdentifier().setSystem("urn:system").setValue("001");
			patient.addName().setFamily("Tester").addGiven("Joe");
			myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless();
		}
		{
			Observation obs = new Observation();
			obs.addIdentifier().setSystem("urn:system").setValue("FOO");
			obs.getSubject().setReferenceElement(pid0);
			myObservationDao.create(obs, mySrd);
		}
		{
			Device device = new Device();
			device.addIdentifier().setValue("DEVICEID");
			IIdType devId = myDeviceDao.create(device, mySrd).getId().toUnqualifiedVersionless();

			Observation obs = new Observation();
			obs.addIdentifier().setSystem("urn:system").setValue("NOLINK");
			obs.setDevice(new Reference(devId));
			myObservationDao.create(obs, mySrd);
			
			ourLog.info("Observation: \n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(obs));
		}

		
		String uri = ourServerBase + "/Patient?_has:Observation:subject:identifier=" + UrlUtil.escapeUrlParam("urn:system|FOO");
		List<String> ids = searchAndReturnUnqualifiedVersionlessIdValues(uri);
		assertThat(ids, contains(pid0.getValue()));
	}

	@Test
	public void testMultipleHasParametersOfSameType() throws Exception {
		
		IIdType pid0;
		{
			Patient patient = new Patient();
			patient.addIdentifier().setSystem("urn:system").setValue("001");
			patient.addName().setFamily("Tester").addGiven("Joe");
			pid0 = myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless();
			
		}
		{
			Patient patient = new Patient();
			patient.addIdentifier().setSystem("urn:system").setValue("001");
			patient.addName().setFamily("Tester").addGiven("Joe");
			myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless();
		}
		{
			Observation obs = new Observation();
			obs.addIdentifier().setSystem("urn:system").setValue("FOO");
			obs.getSubject().setReferenceElement(pid0);
			myObservationDao.create(obs, mySrd);
		}
		{
			Device device = new Device();
			device.addIdentifier().setValue("DEVICEID");
			IIdType devId = myDeviceDao.create(device, mySrd).getId().toUnqualifiedVersionless();

			Observation obs = new Observation();
			obs.addIdentifier().setSystem("urn:system").setValue("NOLINK");
			obs.setDevice(new Reference(devId));
			myObservationDao.create(obs, mySrd);
		}

		{
			Encounter encounter = new Encounter();
			encounter.addIdentifier().setValue("theEncounter");
			encounter.getClass_().setCode("IMP").setSystem("urn:system");
			encounter.getPeriod().setStartElement(new DateTimeType("2020-01-01")).setEndElement(new DateTimeType("2020-09-30"));
			encounter.setSubject(new Reference(pid0));
						
			myEncounterDao.create(encounter, mySrd);
			
			ourLog.info("Encounter: \n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(encounter));
		}
		
		String uri = ourServerBase + "/Patient?_has:Encounter:subject:class=" + UrlUtil.escapeUrlParam("urn:system|IMP") + "&_has:Encounter:subject:date=gt1950";
		
		ourLog.info("uri = " + uri);
		
		List<String> ids = searchAndReturnUnqualifiedVersionlessIdValues(uri);
		assertThat(ids, contains(pid0.getValue()));
	}
	
	@Test
	public void testMultipleHasParametersOfDifferentType() throws Exception {
		
		IIdType pid0;
		{
			Patient patient = new Patient();
			patient.addIdentifier().setSystem("urn:system").setValue("001");
			patient.addName().setFamily("Tester").addGiven("Joe");
			pid0 = myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless();
			
		}
		{
			Patient patient = new Patient();
			patient.addIdentifier().setSystem("urn:system").setValue("001");
			patient.addName().setFamily("Tester").addGiven("Joe");
			myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless();
		}
		{
			Observation obs = new Observation();
			obs.addIdentifier().setSystem("urn:system").setValue("FOO");
			obs.getSubject().setReferenceElement(pid0);
			CodeableConcept cc = obs.getCode();
			cc.addCoding().setCode("2345-7").setSystem("http://loinc.org");
			myObservationDao.create(obs, mySrd);
			
			ourLog.info("Observation: \n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(obs));
		}
		{
			Device device = new Device();
			device.addIdentifier().setValue("DEVICEID");
			IIdType devId = myDeviceDao.create(device, mySrd).getId().toUnqualifiedVersionless();

			Observation obs = new Observation();
			obs.addIdentifier().setSystem("urn:system").setValue("NOLINK");
			obs.setDevice(new Reference(devId));
			myObservationDao.create(obs, mySrd);
		}

		{
			Encounter encounter = new Encounter();
			encounter.addIdentifier().setValue("theEncounter");
			encounter.getClass_().setCode("IMP").setSystem("urn:system");
			encounter.getPeriod().setStartElement(new DateTimeType("2020-01-01")).setEndElement(new DateTimeType("2020-09-30"));
			encounter.setSubject(new Reference(pid0));
						
			myEncounterDao.create(encounter, mySrd);
			ourLog.info("Encounter: \n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(encounter));
		}
		
		String uri = ourServerBase + "/Patient?_has:Observation:patient:code=http://" + UrlUtil.escapeUrlParam("loinc.org|2345-7") + "&_has:Encounter:subject:date=gt1950";
		
		ourLog.info("uri = " + uri);
		
		List<String> ids = searchAndReturnUnqualifiedVersionlessIdValues(uri);
		assertThat(ids, contains(pid0.getValue()));
	}
	
	@Test
	public void testHasCompositeParameterCodeValueQuantity() throws Exception {
		IIdType pid0;
		{
			Patient patient = new Patient();
			patient.addIdentifier().setSystem("urn:system").setValue("001");
			patient.addName().setFamily("Tester").addGiven("Joe");
			pid0 = myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless();
		}
		{
			Patient patient = new Patient();
			patient.addIdentifier().setSystem("urn:system").setValue("001");
			patient.addName().setFamily("Tester").addGiven("Joe");
			myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless();
		}
		{
			Observation obs = new Observation();
			obs.addIdentifier().setSystem("urn:system").setValue("FOO");
			obs.getSubject().setReferenceElement(pid0);
			CodeableConcept cc = obs.getCode();
			cc.addCoding().setCode("2345-7").setSystem("http://loinc.org");
			
			Quantity q = new Quantity();
			q.setValue(200);
			obs.setValue(q);
			
			myObservationDao.create(obs, mySrd);
			
			ourLog.info("Observation: \n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(obs));
		}
		{
			Device device = new Device();
			device.addIdentifier().setValue("DEVICEID");
			IIdType devId = myDeviceDao.create(device, mySrd).getId().toUnqualifiedVersionless();

			Observation obs = new Observation();
			obs.addIdentifier().setSystem("urn:system").setValue("NOLINK");
			obs.setDevice(new Reference(devId));
			myObservationDao.create(obs, mySrd);
		}

		String uri = ourServerBase + "/Patient?_has:Observation:subject:code-value-quantity=http://" + UrlUtil.escapeUrlParam("loinc.org|2345-7$gt180");
		ourLog.info("uri = " + uri);
		List<String> ids = searchAndReturnUnqualifiedVersionlessIdValues(uri);
		assertThat(ids, contains(pid0.getValue()));
	}

	@Test
	public void testHasCompositeParameterCodeValueDate() throws Exception {
		IIdType pid0;
		{
			Patient patient = new Patient();
			patient.addIdentifier().setSystem("urn:system").setValue("001");
			patient.addName().setFamily("Tester").addGiven("Joe");
			pid0 = myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless();
		}
		{
			Patient patient = new Patient();
			patient.addIdentifier().setSystem("urn:system").setValue("001");
			patient.addName().setFamily("Tester").addGiven("Joe");
			myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless();
		}
		{
			Observation obs = new Observation();
			obs.addIdentifier().setSystem("urn:system").setValue("FOO");
			obs.getSubject().setReferenceElement(pid0);
			CodeableConcept cc = obs.getCode();
			cc.addCoding().setCode("2345-7").setSystem("http://loinc.org");
						
			Period period = new Period();
			period.setStartElement(new DateTimeType("2020-01-01"));
			period.setEndElement(new DateTimeType("2020-09-30"));
			obs.setValue(period);
						
			myObservationDao.create(obs, mySrd);
			
			ourLog.info("Observation: \n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(obs));
		}
		{
			Device device = new Device();
			device.addIdentifier().setValue("DEVICEID");
			IIdType devId = myDeviceDao.create(device, mySrd).getId().toUnqualifiedVersionless();

			Observation obs = new Observation();
			obs.addIdentifier().setSystem("urn:system").setValue("NOLINK");
			obs.setDevice(new Reference(devId));
			myObservationDao.create(obs, mySrd);
		}

		String uri = ourServerBase + "/Patient?_has:Observation:subject:code-value-date=http://" + UrlUtil.escapeUrlParam("loinc.org|2345-7$gt2019");
		ourLog.info("uri = " + uri);
		List<String> ids = searchAndReturnUnqualifiedVersionlessIdValues(uri);
		assertThat(ids, contains(pid0.getValue()));
	}

	@Test
	public void testHasCompositeParameterCompCodeValueQuantity() throws Exception {
		IIdType pid0;
		{
			Patient patient = new Patient();
			patient.addIdentifier().setSystem("urn:system").setValue("001");
			patient.addName().setFamily("Tester").addGiven("Joe");
			pid0 = myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless();
		}
		{
			Patient patient = new Patient();
			patient.addIdentifier().setSystem("urn:system").setValue("001");
			patient.addName().setFamily("Tester").addGiven("Joe");
			myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless();
		}
		{
			Observation obs = new Observation();
			obs.addIdentifier().setSystem("urn:system").setValue("FOO");
			obs.getSubject().setReferenceElement(pid0);
			CodeableConcept cc = obs.getCode();
			cc.addCoding().setCode("2345-7").setSystem("http://loinc.org");
						
			Period period = new Period();
			period.setStartElement(new DateTimeType("2020-01-01"));
			period.setEndElement(new DateTimeType("2020-09-30"));
			obs.setValue(period);
						
			ObservationComponentComponent comp = obs.addComponent();
			CodeableConcept compCC = comp.getCode();
			compCC.addCoding().setCode("2345-8").setSystem("http://loinc.org");
			
			Quantity q = new Quantity();
			q.setValue(200);
			comp.setValue(q);
			
			myObservationDao.create(obs, mySrd);
			
			ourLog.info("Observation: \n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(obs));
		}
		{
			Device device = new Device();
			device.addIdentifier().setValue("DEVICEID");
			IIdType devId = myDeviceDao.create(device, mySrd).getId().toUnqualifiedVersionless();

			Observation obs = new Observation();
			obs.addIdentifier().setSystem("urn:system").setValue("NOLINK");
			obs.setDevice(new Reference(devId));
			myObservationDao.create(obs, mySrd);
		}

		String uri = ourServerBase + "/Patient?_has:Observation:subject:combo-code-value-quantity=http://" + UrlUtil.escapeUrlParam("loinc.org|2345-8$200");
		ourLog.info("uri = " + uri);
		List<String> ids = searchAndReturnUnqualifiedVersionlessIdValues(uri);
		assertThat(ids, contains(pid0.getValue()));
	}

	@Test
	public void testHasCompositeParameterCodeValueStringOr() throws Exception {
		IIdType pid0;
		IIdType pid1;
		{
			Patient patient = new Patient();
			patient.addIdentifier().setSystem("urn:system").setValue("001");
			patient.addName().setFamily("Tester").addGiven("Joe");
			pid0 = myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless();
		}
		{
			Patient patient = new Patient();
			patient.addIdentifier().setSystem("urn:system").setValue("001");
			patient.addName().setFamily("Tester").addGiven("Joe");
			pid1 = myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless();
		}
		{
			Observation obs = new Observation();
			obs.addIdentifier().setSystem("urn:system").setValue("FOO");
			obs.getSubject().setReferenceElement(pid0);
			CodeableConcept cc = obs.getCode();
			cc.addCoding().setCode("2345-7").setSystem("http://loinc.org");
			
			obs.setValue(new StringType("100"));
			
			myObservationDao.create(obs, mySrd);
			
			ourLog.info("Observation: \n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(obs));
		}
		
		{
			Observation obs = new Observation();
			obs.addIdentifier().setSystem("urn:system").setValue("BAR");
			obs.getSubject().setReferenceElement(pid1);
			CodeableConcept cc = obs.getCode();
			cc.addCoding().setCode("2345-7").setSystem("http://loinc.org");
			
			obs.setValue(new StringType("200"));
			
			myObservationDao.create(obs, mySrd);
			
			ourLog.info("Observation: \n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(obs));
		}
		{
			Device device = new Device();
			device.addIdentifier().setValue("DEVICEID");
			IIdType devId = myDeviceDao.create(device, mySrd).getId().toUnqualifiedVersionless();

			Observation obs = new Observation();
			obs.addIdentifier().setSystem("urn:system").setValue("NOLINK");
			obs.setDevice(new Reference(devId));
			myObservationDao.create(obs, mySrd);
		}

		String uri = ourServerBase + "/Patient?_has:Observation:subject:code-value-string=http://" + UrlUtil.escapeUrlParam("loinc.org|2345-7$100,http://loinc.org|2345-7$200");
		ourLog.info("uri = " + uri);
		
		List<String> ids = searchAndReturnUnqualifiedVersionlessIdValues(uri);
		assertEquals(2, ids.size());
	}
	
	@Test
	public void testHasCompositeParameterWithNoResult() throws Exception {
		IIdType pid0;
		{
			Patient patient = new Patient();
			patient.addIdentifier().setSystem("urn:system").setValue("001");
			patient.addName().setFamily("Tester").addGiven("Joe");
			pid0 = myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless();
		}
		{
			Patient patient = new Patient();
			patient.addIdentifier().setSystem("urn:system").setValue("001");
			patient.addName().setFamily("Tester").addGiven("Joe");
			myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless();
		}
		{
			Observation obs = new Observation();
			obs.addIdentifier().setSystem("urn:system").setValue("FOO");
			obs.getSubject().setReferenceElement(pid0);
			CodeableConcept cc = obs.getCode();
			cc.addCoding().setCode("2345-7").setSystem("http://loinc.org");
			
			Quantity q = new Quantity();
			q.setValue(200);
			obs.setValue(q);
			
			myObservationDao.create(obs, mySrd);
			
			ourLog.info("Observation: \n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(obs));
		}
		{
			Device device = new Device();
			device.addIdentifier().setValue("DEVICEID");
			IIdType devId = myDeviceDao.create(device, mySrd).getId().toUnqualifiedVersionless();

			Observation obs = new Observation();
			obs.addIdentifier().setSystem("urn:system").setValue("NOLINK");
			obs.setDevice(new Reference(devId));
			myObservationDao.create(obs, mySrd);
		}

		String uri = ourServerBase + "/Patient?_has:Observation:subject:code-value-quantity=http://" + UrlUtil.escapeUrlParam("loinc.org|2345-7$lt180");
		ourLog.info("uri = " + uri);
		List<String> ids = searchAndReturnUnqualifiedVersionlessIdValues(uri);
		assertEquals(0, ids.size());
	}
	
	@Test
	public void testMultipleHasParameterWithCompositeOfSameType() throws Exception {
		IIdType pid0;
		{
			Patient patient = new Patient();
			patient.addIdentifier().setSystem("urn:system").setValue("001");
			patient.addName().setFamily("Tester").addGiven("Joe");
			pid0 = myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless();
		}
		{
			Patient patient = new Patient();
			patient.addIdentifier().setSystem("urn:system").setValue("001");
			patient.addName().setFamily("Tester").addGiven("Joe");
			myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless();
		}
		{
			Observation obs = new Observation();
			obs.addIdentifier().setSystem("urn:system").setValue("FOO");
			obs.getSubject().setReferenceElement(pid0);
			CodeableConcept cc = obs.getCode();
			cc.addCoding().setCode("2345-7").setSystem("http://loinc.org");
			
			Quantity q = new Quantity();
			q.setValue(200);
			obs.setValue(q);
			
			myObservationDao.create(obs, mySrd);
			
			ourLog.info("Observation: \n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(obs));
		}
		{
			Device device = new Device();
			device.addIdentifier().setValue("DEVICEID");
			IIdType devId = myDeviceDao.create(device, mySrd).getId().toUnqualifiedVersionless();

			Observation obs = new Observation();
			obs.addIdentifier().setSystem("urn:system").setValue("NOLINK");
			obs.setDevice(new Reference(devId));
			myObservationDao.create(obs, mySrd);
		}

		String uri = ourServerBase + "/Patient?_has:Observation:subject:code-value-quantity=http://" + UrlUtil.escapeUrlParam("loinc.org|2345-7$gt180") + "&_has:Observation:subject:identifier=" + UrlUtil.escapeUrlParam("urn:system|FOO");
		
		ourLog.info("uri = " + uri);
		List<String> ids = searchAndReturnUnqualifiedVersionlessIdValues(uri);
		assertThat(ids, contains(pid0.getValue()));
	}
	
	@Test
	public void testMultipleHasParameterWithCompositeOfDifferentType() throws Exception {
		
		IIdType pid0;
		{
			Patient patient = new Patient();
			patient.addIdentifier().setSystem("urn:system").setValue("001");
			patient.addName().setFamily("Tester").addGiven("Joe");
			pid0 = myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless();
			
		}
		{
			Patient patient = new Patient();
			patient.addIdentifier().setSystem("urn:system").setValue("001");
			patient.addName().setFamily("Tester").addGiven("Joe");
			myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless();
		}
		{
			Observation obs = new Observation();
			obs.addIdentifier().setSystem("urn:system").setValue("FOO");
			obs.getSubject().setReferenceElement(pid0);
			CodeableConcept cc = obs.getCode();
			cc.addCoding().setCode("2345-7").setSystem("http://loinc.org");
			
			Quantity q = new Quantity();
			q.setValue(200);
			obs.setValue(q);
			
			myObservationDao.create(obs, mySrd);
			
			ourLog.info("Observation: \n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(obs));
		}
		{
			Device device = new Device();
			device.addIdentifier().setValue("DEVICEID");
			IIdType devId = myDeviceDao.create(device, mySrd).getId().toUnqualifiedVersionless();

			Observation obs = new Observation();
			obs.addIdentifier().setSystem("urn:system").setValue("NOLINK");
			obs.setDevice(new Reference(devId));
			myObservationDao.create(obs, mySrd);
		}

		{
			Encounter encounter = new Encounter();
			encounter.addIdentifier().setValue("theEncounter");
			encounter.getClass_().setCode("IMP").setSystem("urn:system");
			encounter.getPeriod().setStartElement(new DateTimeType("2020-01-01")).setEndElement(new DateTimeType("2020-09-30"));
			encounter.setSubject(new Reference(pid0));
						
			myEncounterDao.create(encounter, mySrd);
			ourLog.info("Encounter: \n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(encounter));
		}
		
		String uri = ourServerBase + "/Patient?_has:Observation:subject:code-value-quantity=http://" + UrlUtil.escapeUrlParam("loinc.org|2345-7$gt180") + "&_has:Encounter:subject:date=gt1950" + "&_has:Encounter:subject:class=" + UrlUtil.escapeUrlParam("urn:system|IMP");
		
		ourLog.info("uri = " + uri);
		List<String> ids = searchAndReturnUnqualifiedVersionlessIdValues(uri);
				
		assertThat(ids, contains(pid0.getValue()));
	}

	@Test
	public void testMultipleHasParameter_NOT_IN() throws Exception {

		for (int i=0; i<10; i++) {
			createPatientWithObs(10);
		}

		String uri = ourServerBase + "/Patient?_has:Observation:subject:code-value-quantity=http://" + UrlUtil.escapeUrlParam("loinc.org|2345-7$gt180") + "&_has:Observation:subject:date=gt1950" + "&_has:Observation:subject:status=final&_count=4";

		ourLog.info("uri = " + uri);
		myCaptureQueriesListener.clear();

		searchAndReturnUnqualifiedVersionlessIdValues(uri);

		List<String> queries = myCaptureQueriesListener.getSelectQueries().stream().map(t -> t.getSql(true, false)).collect(Collectors.toList());

		List<String> notInListQueries = new ArrayList<>();
		for (String query : queries) {
			ourLog.info("Query: {}", query);
			if (query.contains("RES_ID NOT IN"))
				notInListQueries.add(query);
		}

		assertNotEquals(0, notInListQueries.size());
	}

	private List<String> searchAndReturnUnqualifiedVersionlessIdValues(String uri) throws IOException {
		List<String> ids;
		HttpGet get = new HttpGet(uri);

		try (CloseableHttpResponse response = ourHttpClient.execute(get)) {
			String resp = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info("Response was: {}", resp);
			Bundle bundle = myFhirContext.newXmlParser().parseResource(Bundle.class, resp);
			ids = toUnqualifiedVersionlessIdValues(bundle);
		}
		return ids;
	}

	private void createPatientWithObs(int obsNum) {
		
		Patient patient = new Patient();
		patient.addIdentifier().setSystem("urn:system").setValue("001");
		patient.addName().setFamily("Tester").addGiven("Joe");
		IIdType pid = myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless();
		
		Observation o1 = new Observation();
		o1.setStatus(ObservationStatus.FINAL);
		o1.getSubject().setReferenceElement(pid);
		o1.setEffective(new DateTimeType("2001-02-01"));
		CodeableConcept cc = o1.getCode();
		cc.addCoding().setCode("2345-7").setSystem("http://loinc.org");
		o1.setValue(new Quantity().setValue(200));	
		cc = new CodeableConcept();
		cc.addCoding().setCode("2345-7").setSystem("http://loinc.org");
		o1.addCategory(cc);
		
		for (int i=0; i<obsNum; i++) {
			myObservationDao.create(o1).getId().toUnqualifiedVersionless();
		}
	}
}
