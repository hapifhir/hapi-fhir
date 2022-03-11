package ca.uhn.fhir.jpa.provider.r4;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.model.entity.ModelConfig;
import ca.uhn.fhir.parser.StrictErrorHandler;
import ca.uhn.fhir.rest.client.interceptor.CapturingInterceptor;
import ca.uhn.fhir.rest.server.exceptions.MethodNotAllowedException;
import ca.uhn.fhir.util.UrlUtil;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.CarePlan;
import org.hl7.fhir.r4.model.CarePlan.CarePlanIntent;
import org.hl7.fhir.r4.model.CarePlan.CarePlanStatus;
import org.hl7.fhir.r4.model.ClinicalImpression;
import org.hl7.fhir.r4.model.ClinicalImpression.ClinicalImpressionStatus;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.DecimalType;
import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.Encounter.EncounterStatus;
import org.hl7.fhir.r4.model.HumanName;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Quantity;
import org.hl7.fhir.r4.model.Resource;
import org.hl7.fhir.r4.model.RiskAssessment;
import org.hl7.fhir.r4.model.RiskAssessment.RiskAssessmentStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class ResourceProviderR4SearchContainedTest extends BaseResourceProviderR4Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ResourceProviderR4SearchContainedTest.class);
	@Autowired
	@Qualifier("myClinicalImpressionDaoR4")
	protected IFhirResourceDao<ClinicalImpression> myClinicalImpressionDao;
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
		myModelConfig.setIndexOnContainedResources(false);
		myModelConfig.setIndexOnContainedResources(new ModelConfig().isIndexOnContainedResources());
	}

	@BeforeEach
	@Override
	public void before() throws Exception {
		super.before();
		myFhirContext.setParserErrorHandler(new StrictErrorHandler());

		myDaoConfig.setAllowMultipleDelete(true);
		myClient.registerInterceptor(myCapturingInterceptor);
		myDaoConfig.setSearchPreFetchThresholds(new DaoConfig().getSearchPreFetchThresholds());
		myModelConfig.setIndexOnContainedResources(true);
		myDaoConfig.setReuseCachedSearchResultsForMillis(null);
	}

	@Test
	public void testContainedDisabled() throws Exception {
		myModelConfig.setIndexOnContainedResources(false);

		String uri = ourServerBase + "/Observation?subject.name=Smith&_contained=true";
		try (CloseableHttpResponse response = ourHttpClient.execute(new HttpGet(uri))) {
			String resp = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(resp);
			assertEquals(MethodNotAllowedException.STATUS_CODE, response.getStatusLine().getStatusCode());
			assertThat(resp, containsString(">" + Msg.code(984) + "Searching with _contained mode enabled is not enabled on this server"));
		}
	}

	@Test
	public void testContainedBoth() throws Exception {
		String uri = ourServerBase + "/Observation?subject.name=Smith&_contained=both";
		try (CloseableHttpResponse response = ourHttpClient.execute(new HttpGet(uri))) {
			String resp = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(resp);
			assertEquals(MethodNotAllowedException.STATUS_CODE, response.getStatusLine().getStatusCode());
			assertThat(resp, containsString("Contained mode 'both' is not currently supported"));
		}
	}

	@Test
	public void testContainedSearchByName() throws Exception {

		IIdType oid1;

		{
			Patient p = new Patient();
			p.setId("patient1");
			p.addName().setFamily("Smith").addGiven("John");

			Observation obs = new Observation();
			obs.getCode().setText("Observation 1");
			obs.getContained().add(p);
			obs.getSubject().setReference("#patient1");

			oid1 = myObservationDao.create(obs, mySrd).getId().toUnqualifiedVersionless();

			ourLog.info("Input: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(obs));
		}

		{
			Patient p = new Patient();
			p.setId("patient1");
			p.addName().setFamily("Doe").addGiven("Jane");

			Observation obs = new Observation();
			obs.getCode().setText("Observation 2");
			obs.getContained().add(p);
			obs.getSubject().setReference("#patient1");

			myObservationDao.create(obs, mySrd).getId().toUnqualifiedVersionless();

			ourLog.info("Input: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(obs));
		}

		{
			Patient p = new Patient();
			p.setId("patient1");
			p.addName().setFamily("Jones").addGiven("Peter");

			Observation obs = new Observation();
			obs.getCode().setText("Observation 2");
			obs.getContained().add(p);
			obs.getSubject().setReference("#patient1");

			myObservationDao.create(obs, mySrd).getId().toUnqualifiedVersionless();

			ourLog.info("Input: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(obs));
		}


		//-- Simple name match
		String uri = ourServerBase + "/Observation?subject.name=Smith&_contained=true";
		List<String> oids = searchAndReturnUnqualifiedVersionlessIdValues(uri);

		assertEquals(1L, oids.size());
		assertThat(oids, contains(oid1.getValue()));

		//-- Simple name match with or
		uri = ourServerBase + "/Observation?subject.name=Smith,Jane&_contained=true";
		oids = searchAndReturnUnqualifiedVersionlessIdValues(uri);

		assertEquals(2L, oids.size());
		//assertEquals(oids.toString(), "[Observation/1, Observation/2]");

		//-- Simple name match with qualifier
		uri = ourServerBase + "/Observation?subject.name:exact=Smith&_contained=true";
		oids = searchAndReturnUnqualifiedVersionlessIdValues(uri);

		assertEquals(1L, oids.size());
		assertThat(oids, contains(oid1.getValue()));

		//-- Simple name match with and
		uri = ourServerBase + "/Observation?subject.family=Smith&subject.given=John&_contained=true";
		oids = searchAndReturnUnqualifiedVersionlessIdValues(uri);

		assertEquals(1L, oids.size());
		assertThat(oids, contains(oid1.getValue()));

	}

	@Test
	public void testContainedSearchByDate() throws Exception {

		IIdType oid1;
		IIdType oid3;

		{
			Patient p = new Patient();
			p.setId("patient1");
			p.addName().setFamily("Smith").addGiven("John");
			p.getBirthDateElement().setValueAsString("2000-01-01");

			Observation obs = new Observation();
			obs.getCode().setText("Observation 1");
			obs.getContained().add(p);
			obs.getSubject().setReference("#patient1");

			oid1 = myObservationDao.create(obs, mySrd).getId().toUnqualifiedVersionless();

			ourLog.info("Input: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(obs));
		}

		{
			Patient p = new Patient();
			p.setId("patient1");
			p.addName().setFamily("Doe").addGiven("Jane");
			p.getBirthDateElement().setValueAsString("2000-02-01");

			Observation obs = new Observation();
			obs.getCode().setText("Observation 2");
			obs.getContained().add(p);
			obs.getSubject().setReference("#patient1");

			myObservationDao.create(obs, mySrd).getId().toUnqualifiedVersionless();

			ourLog.info("Input: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(obs));
		}

		{
			Patient p = new Patient();
			p.setId("patient1");
			p.addName().setFamily("Jones").addGiven("Peter");
			p.getBirthDateElement().setValueAsString("2000-03-01");

			Observation obs = new Observation();
			obs.getCode().setText("Observation 2");
			obs.getContained().add(p);
			obs.getSubject().setReference("#patient1");

			oid3 = myObservationDao.create(obs, mySrd).getId().toUnqualifiedVersionless();

			ourLog.info("Input: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(obs));
		}

		//-- Search by date default op
		String uri = ourServerBase + "/Observation?subject.birthdate=2000-01-01&_contained=true";
		List<String> oids = searchAndReturnUnqualifiedVersionlessIdValues(uri);

		assertEquals(1L, oids.size());
		assertThat(oids, contains(oid1.getValue()));

		//-- Search by date op=eq
		uri = ourServerBase + "/Observation?subject.birthdate=eq2000-01-01&_contained=true";
		oids = searchAndReturnUnqualifiedVersionlessIdValues(uri);

		assertEquals(1L, oids.size());
		assertThat(oids, contains(oid1.getValue()));

		//-- Search by date op=eq, with or
		uri = ourServerBase + "/Observation?subject.birthdate=2000-01-01,2000-02-01&_contained=true";
		oids = searchAndReturnUnqualifiedVersionlessIdValues(uri);

		assertEquals(2L, oids.size());
		//assertEquals(oids.toString(), "[Observation/1, Observation/2]");

		//-- Simple name match with op = gt
		uri = ourServerBase + "/Observation?subject.birthdate=gt2000-02-10&_contained=true";
		oids = searchAndReturnUnqualifiedVersionlessIdValues(uri);

		assertEquals(1L, oids.size());
		assertThat(oids, contains(oid3.getValue()));

		//-- Simple name match with AND
		uri = ourServerBase + "/Observation?subject.family=Smith&subject.birthdate=eq2000-01-01&_contained=true";
		oids = searchAndReturnUnqualifiedVersionlessIdValues(uri);

		assertEquals(1L, oids.size());
		assertThat(oids, contains(oid1.getValue()));

		//-- Simple name match with AND - not found
		uri = ourServerBase + "/Observation?subject.family=Smith&subject.birthdate=eq2000-02-01&_contained=true";
		oids = searchAndReturnUnqualifiedVersionlessIdValues(uri);

		assertEquals(0L, oids.size());
	}

	@Test
	public void testContainedSearchByNumber() throws Exception {

		IIdType cid1;

		{
			Patient p = new Patient();
			p.setId("patient1");
			p.addName().setFamily("Smith").addGiven("John");
			p.getBirthDateElement().setValueAsString("2000-01-01");


			RiskAssessment risk = new RiskAssessment();
			risk.setId("risk1");
			risk.setStatus(RiskAssessmentStatus.CORRECTED);
			risk.getSubject().setReference("#patient1");
			risk.getPredictionFirstRep().setProbability(new DecimalType(2));

			ClinicalImpression imp = new ClinicalImpression();
			imp.setStatus(ClinicalImpressionStatus.COMPLETED);

			imp.getContained().add(p);
			imp.getSubject().setReference("#patient1");

			imp.getContained().add(risk);
			imp.getInvestigationFirstRep().getItemFirstRep().setReference("#risk1");

			ourLog.info("Input: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(imp));

			cid1 = myClinicalImpressionDao.create(imp, mySrd).getId().toUnqualifiedVersionless();

			ClinicalImpression createdImp = myClinicalImpressionDao.read(cid1);

			ourLog.info("Output: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(createdImp));
		}

		{
			Patient p = new Patient();
			p.setId("patient1");
			p.addName().setFamily("Smith").addGiven("John");
			p.getBirthDateElement().setValueAsString("2000-01-01");


			RiskAssessment risk = new RiskAssessment();
			risk.setId("risk1");
			risk.setStatus(RiskAssessmentStatus.CORRECTED);
			risk.getSubject().setReference("#patient1");
			risk.getPredictionFirstRep().setProbability(new DecimalType(5));

			ClinicalImpression imp = new ClinicalImpression();
			imp.setStatus(ClinicalImpressionStatus.COMPLETED);

			imp.getContained().add(p);
			imp.getSubject().setReference("#patient1");

			imp.getContained().add(risk);
			imp.getInvestigationFirstRep().getItemFirstRep().setReference("#risk1");

			ourLog.info("Input: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(imp));

			IIdType cid2 = myClinicalImpressionDao.create(imp, mySrd).getId().toUnqualifiedVersionless();

			ClinicalImpression createdImp = myClinicalImpressionDao.read(cid2);

			ourLog.info("Output: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(createdImp));
		}

		{
			Patient p = new Patient();
			p.setId("patient1");
			p.addName().setFamily("Smith").addGiven("John");
			p.getBirthDateElement().setValueAsString("2000-01-01");


			RiskAssessment risk = new RiskAssessment();
			risk.setId("risk1");
			risk.setStatus(RiskAssessmentStatus.CORRECTED);
			risk.getSubject().setReference("#patient1");
			risk.getPredictionFirstRep().setProbability(new DecimalType(10));

			ClinicalImpression imp = new ClinicalImpression();
			imp.setStatus(ClinicalImpressionStatus.COMPLETED);

			imp.getContained().add(p);
			imp.getSubject().setReference("#patient1");

			imp.getContained().add(risk);
			imp.getInvestigationFirstRep().getItemFirstRep().setReference("#risk1");

			ourLog.info("Input: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(imp));

			IIdType cid3 = myClinicalImpressionDao.create(imp, mySrd).getId().toUnqualifiedVersionless();

			ClinicalImpression createdImp = myClinicalImpressionDao.read(cid3);

			ourLog.info("Output: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(createdImp));
		}

		//-- Search by number
		String uri = ourServerBase + "/ClinicalImpression?investigation.probability=2&_contained=true";
		List<String> cids = searchAndReturnUnqualifiedVersionlessIdValues(uri);

		assertEquals(1L, cids.size());
		assertThat(cids, contains(cid1.getValue()));


		//-- Search by number with op = eq
		uri = ourServerBase + "/ClinicalImpression?investigation.probability=eq2&_contained=true";
		cids = searchAndReturnUnqualifiedVersionlessIdValues(uri);

		assertEquals(1L, cids.size());
		assertThat(cids, contains(cid1.getValue()));


		//-- Search by number with op = eq and or
		uri = ourServerBase + "/ClinicalImpression?investigation.probability=eq2,10&_contained=true";
		cids = searchAndReturnUnqualifiedVersionlessIdValues(uri);
		assertEquals(2L, cids.size());

		//-- Search by number with op = lt 
		uri = ourServerBase + "/ClinicalImpression?investigation.probability=lt4&_contained=true";
		cids = searchAndReturnUnqualifiedVersionlessIdValues(uri);

		assertEquals(1L, cids.size());
		assertThat(cids, contains(cid1.getValue()));
	}

	@Test
	public void testContainedSearchByQuantity() throws Exception {

		IIdType eid1;
		{
			Encounter encounter = new Encounter();
			encounter.setStatus(EncounterStatus.ARRIVED);

			Patient patient = new Patient();
			patient.setId("patient1");
			patient.addName().setFamily("Doe").addGiven("Jane");
			encounter.getSubject().setReference("#patient1");
			encounter.getContained().add(patient);

			Observation obs = new Observation();
			obs.setId("obs1");
			obs.addIdentifier().setSystem("urn:system").setValue("FOO");
			obs.getSubject().setReference("#patient1");
			CodeableConcept cc = obs.getCode();
			cc.addCoding().setCode("2345-7").setSystem("http://loinc.org");
			Quantity quantity = obs.getValueQuantity();
			quantity.setValue(200);
			encounter.addReasonReference().setReference("#obs1");
			encounter.getContained().add(obs);

			ourLog.info("Input: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(encounter));

			eid1 = myEncounterDao.create(encounter, mySrd).getId().toUnqualifiedVersionless();

			Encounter createdEncounter = myEncounterDao.read(eid1);

			ourLog.info("Output: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(createdEncounter));
		}


		{
			Encounter encounter = new Encounter();
			encounter.setStatus(EncounterStatus.ARRIVED);

			Patient patient = new Patient();
			patient.setId("patient1");
			patient.addName().setFamily("Doe").addGiven("Jane");
			encounter.getSubject().setReference("#patient1");
			encounter.getContained().add(patient);

			Observation obs = new Observation();
			obs.setId("obs1");
			obs.addIdentifier().setSystem("urn:system").setValue("FOO");
			obs.getSubject().setReference("#patient1");
			CodeableConcept cc = obs.getCode();
			cc.addCoding().setCode("2345-7").setSystem("http://loinc.org");
			Quantity quantity = obs.getValueQuantity();
			quantity.setValue(300);
			encounter.addReasonReference().setReference("#obs1");
			encounter.getContained().add(obs);

			ourLog.info("Input: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(encounter));

			IIdType eid2 = myEncounterDao.create(encounter, mySrd).getId().toUnqualifiedVersionless();

			Encounter createdEncounter = myEncounterDao.read(eid2);

			ourLog.info("Output: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(createdEncounter));
		}

		{
			Encounter encounter = new Encounter();
			encounter.setStatus(EncounterStatus.ARRIVED);

			Patient patient = new Patient();
			patient.setId("patient1");
			patient.addName().setFamily("Doe").addGiven("Jane");
			encounter.getSubject().setReference("#patient1");
			encounter.getContained().add(patient);

			Observation obs = new Observation();
			obs.setId("obs1");
			obs.addIdentifier().setSystem("urn:system").setValue("FOO");
			obs.getSubject().setReference("#patient1");
			CodeableConcept cc = obs.getCode();
			cc.addCoding().setCode("2345-7").setSystem("http://loinc.org");
			Quantity quantity = obs.getValueQuantity();
			quantity.setValue(400);
			encounter.addReasonReference().setReference("#obs1");
			encounter.getContained().add(obs);

			ourLog.info("Input: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(encounter));

			IIdType eid3 = myEncounterDao.create(encounter, mySrd).getId().toUnqualifiedVersionless();

			Encounter createdEncounter = myEncounterDao.read(eid3);

			ourLog.info("Output: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(createdEncounter));
		}

		//-- Search by quantity
		String uri = ourServerBase + "/Encounter?reason-reference.combo-value-quantity=200&_contained=true";
		List<String> eids = searchAndReturnUnqualifiedVersionlessIdValues(uri);

		assertEquals(1L, eids.size());
		assertThat(eids, contains(eid1.getValue()));


		//-- Search by quantity
		uri = ourServerBase + "/Encounter?reason-reference.combo-value-quantity=le400&_contained=true";
		eids = searchAndReturnUnqualifiedVersionlessIdValues(uri);

		assertEquals(3L, eids.size());

	}

	@Test
	public void testContainedSearchByToken() throws Exception {

		IIdType eid1;
		{
			Encounter encounter = new Encounter();
			encounter.setStatus(EncounterStatus.ARRIVED);

			Patient patient = new Patient();
			patient.setId("patient1");
			patient.addName().setFamily("Doe").addGiven("Jane");
			encounter.getSubject().setReference("#patient1");
			encounter.getContained().add(patient);

			Observation obs = new Observation();
			obs.setId("obs1");
			obs.addIdentifier().setSystem("urn:system").setValue("FOO");
			obs.getSubject().setReference("#patient1");
			CodeableConcept cc = obs.getCode();
			cc.addCoding().setCode("2345-7").setSystem("http://loinc.org");
			Quantity quantity = obs.getValueQuantity();
			quantity.setValue(200);
			encounter.addReasonReference().setReference("#obs1");
			encounter.getContained().add(obs);

			ourLog.info("Input: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(encounter));

			eid1 = myEncounterDao.create(encounter, mySrd).getId().toUnqualifiedVersionless();

			Encounter createdEncounter = myEncounterDao.read(eid1);

			ourLog.info("Output: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(createdEncounter));
		}


		{
			Encounter encounter = new Encounter();
			encounter.setStatus(EncounterStatus.ARRIVED);

			Patient patient = new Patient();
			patient.setId("patient1");
			patient.addName().setFamily("Doe").addGiven("Jane");
			encounter.getSubject().setReference("#patient1");
			encounter.getContained().add(patient);

			Observation obs = new Observation();
			obs.setId("obs1");
			obs.addIdentifier().setSystem("urn:system").setValue("FOO");
			obs.getSubject().setReference("#patient1");
			CodeableConcept cc = obs.getCode();
			cc.addCoding().setCode("2345-8").setSystem("http://loinc.org");
			Quantity quantity = obs.getValueQuantity();
			quantity.setValue(300);
			encounter.addReasonReference().setReference("#obs1");
			encounter.getContained().add(obs);

			ourLog.info("Input: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(encounter));

			IIdType eid2 = myEncounterDao.create(encounter, mySrd).getId().toUnqualifiedVersionless();

			Encounter createdEncounter = myEncounterDao.read(eid2);

			ourLog.info("Output: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(createdEncounter));
		}

		{
			Encounter encounter = new Encounter();
			encounter.setStatus(EncounterStatus.ARRIVED);

			Patient patient = new Patient();
			patient.setId("patient1");
			patient.addName().setFamily("Doe").addGiven("Jane");
			encounter.getSubject().setReference("#patient1");
			encounter.getContained().add(patient);

			Observation obs = new Observation();
			obs.setId("obs1");
			obs.addIdentifier().setSystem("urn:system").setValue("FOO");
			obs.getSubject().setReference("#patient1");
			CodeableConcept cc = obs.getCode();
			cc.addCoding().setCode("2345-9").setSystem("http://loinc.org");
			Quantity quantity = obs.getValueQuantity();
			quantity.setValue(400);
			encounter.addReasonReference().setReference("#obs1");
			encounter.getContained().add(obs);

			ourLog.info("Input: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(encounter));

			IIdType eid3 = myEncounterDao.create(encounter, mySrd).getId().toUnqualifiedVersionless();

			Encounter createdEncounter = myEncounterDao.read(eid3);

			ourLog.info("Output: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(createdEncounter));
		}

		//-- Search by code
		String uri = ourServerBase + "/Encounter?reason-reference.code=http://" + UrlUtil.escapeUrlParam("loinc.org|2345-7") + "&_contained=true";
		List<String> eids = searchAndReturnUnqualifiedVersionlessIdValues(uri);

		assertEquals(1L, eids.size());
		assertThat(eids, contains(eid1.getValue()));

	}

	@Test
	public void testContainedSearchByComposite() throws Exception {

		IIdType eid2;
		{
			Encounter encounter = new Encounter();
			encounter.setStatus(EncounterStatus.ARRIVED);

			Patient patient = new Patient();
			patient.setId("patient1");
			patient.addName().setFamily("Doe").addGiven("Jane");
			encounter.getSubject().setReference("#patient1");
			encounter.getContained().add(patient);

			Observation obs = new Observation();
			obs.setId("obs1");
			obs.addIdentifier().setSystem("urn:system").setValue("FOO");
			obs.getSubject().setReference("#patient1");
			CodeableConcept cc = obs.getCode();
			cc.addCoding().setCode("2345-7").setSystem("http://loinc.org");
			Quantity quantity = obs.getValueQuantity();
			quantity.setValue(200);
			encounter.addReasonReference().setReference("#obs1");
			encounter.getContained().add(obs);

			ourLog.info("Input: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(encounter));

			IIdType eid1 = myEncounterDao.create(encounter, mySrd).getId().toUnqualifiedVersionless();

			Encounter createdEncounter = myEncounterDao.read(eid1);

			ourLog.info("Output: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(createdEncounter));
		}


		{
			Encounter encounter = new Encounter();
			encounter.setStatus(EncounterStatus.ARRIVED);

			Patient patient = new Patient();
			patient.setId("patient1");
			patient.addName().setFamily("Doe").addGiven("Jane");
			encounter.getSubject().setReference("#patient1");
			encounter.getContained().add(patient);

			Observation obs = new Observation();
			obs.setId("obs1");
			obs.addIdentifier().setSystem("urn:system").setValue("FOO");
			obs.getSubject().setReference("#patient1");
			CodeableConcept cc = obs.getCode();
			cc.addCoding().setCode("2345-8").setSystem("http://loinc.org");
			Quantity quantity = obs.getValueQuantity();
			quantity.setValue(300);
			encounter.addReasonReference().setReference("#obs1");
			encounter.getContained().add(obs);

			ourLog.info("Input: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(encounter));

			eid2 = myEncounterDao.create(encounter, mySrd).getId().toUnqualifiedVersionless();

			Encounter createdEncounter = myEncounterDao.read(eid2);

			ourLog.info("Output: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(createdEncounter));
		}

		{
			Encounter encounter = new Encounter();
			encounter.setStatus(EncounterStatus.ARRIVED);

			Patient patient = new Patient();
			patient.setId("patient1");
			patient.addName().setFamily("Doe").addGiven("Jane");
			encounter.getSubject().setReference("#patient1");
			encounter.getContained().add(patient);

			Observation obs = new Observation();
			obs.setId("obs1");
			obs.addIdentifier().setSystem("urn:system").setValue("FOO");
			obs.getSubject().setReference("#patient1");
			CodeableConcept cc = obs.getCode();
			cc.addCoding().setCode("2345-9").setSystem("http://loinc.org");
			Quantity quantity = obs.getValueQuantity();
			quantity.setValue(400);
			encounter.addReasonReference().setReference("#obs1");
			encounter.getContained().add(obs);

			ourLog.info("Input: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(encounter));

			IIdType eid3 = myEncounterDao.create(encounter, mySrd).getId().toUnqualifiedVersionless();

			Encounter createdEncounter = myEncounterDao.read(eid3);

			ourLog.info("Output: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(createdEncounter));
		}

		//-- Search by composite
		String uri = ourServerBase + "/Encounter?reason-reference.combo-code-value-quantity=http://" + UrlUtil.escapeUrlParam("loinc.org|2345-8$300") + "&_contained=true";
		List<String> eids = searchAndReturnUnqualifiedVersionlessIdValues(uri);

		assertEquals(1L, eids.size());
		assertThat(eids, contains(eid2.getValue()));

		//-- Search by composite - not found
		uri = ourServerBase + "/Encounter?reason-reference.combo-code-value-quantity=http://" + UrlUtil.escapeUrlParam("loinc.org|2345-7$300") + "&_contained=true";
		eids = searchAndReturnUnqualifiedVersionlessIdValues(uri);

		assertEquals(0L, eids.size());

	}


	@Test
	public void testContainedSearchByUri() throws Exception {

		IIdType oid1;

		{
			Patient p = new Patient();
			p.setId("patient1");
			p.addName().setFamily("Smith").addGiven("John");
			p.getBirthDateElement().setValueAsString("2000-01-01");

			CarePlan carePlan = new CarePlan();
			carePlan.setId("carePlan1");
			carePlan.setStatus(CarePlanStatus.ACTIVE);
			carePlan.setIntent(CarePlanIntent.ORDER);
			carePlan.getSubject().setReference("#patient1");
			carePlan.addInstantiatesUri("http://www.hl7.com");

			Observation obs = new Observation();
			obs.getCode().setText("Observation 1");
			obs.getContained().add(p);
			obs.getSubject().setReference("#patient1");
			obs.getContained().add(carePlan);
			obs.getBasedOnFirstRep().setReference("#carePlan1");


			oid1 = myObservationDao.create(obs, mySrd).getId().toUnqualifiedVersionless();

			ourLog.info("Input: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(obs));

			Observation createdObs = myObservationDao.read(oid1);

			ourLog.info("Output: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(createdObs));
		}

		{
			Patient p = new Patient();
			p.setId("patient2");
			p.addName().setFamily("Smith").addGiven("John");
			p.getBirthDateElement().setValueAsString("2000-01-01");

			CarePlan carePlan = new CarePlan();
			carePlan.setId("carePlan2");
			carePlan.setStatus(CarePlanStatus.ACTIVE);
			carePlan.setIntent(CarePlanIntent.ORDER);
			carePlan.getSubject().setReference("#patient2");
			carePlan.addInstantiatesUri("http://www2.hl7.com");

			Observation obs = new Observation();
			obs.getCode().setText("Observation 2");
			obs.getContained().add(p);
			obs.getSubject().setReference("#patient2");
			obs.getContained().add(carePlan);
			obs.getBasedOnFirstRep().setReference("#carePlan2");

			myObservationDao.create(obs, mySrd).getId().toUnqualifiedVersionless();

			ourLog.info("Input: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(obs));
		}

		{
			Patient p = new Patient();
			p.setId("patient3");
			p.addName().setFamily("Smith").addGiven("John");
			p.getBirthDateElement().setValueAsString("2000-01-01");

			CarePlan carePlan = new CarePlan();
			carePlan.setId("carePlan3");
			carePlan.setStatus(CarePlanStatus.ACTIVE);
			carePlan.setIntent(CarePlanIntent.ORDER);
			carePlan.getSubject().setReference("#patient3");
			carePlan.addInstantiatesUri("http://www2.hl7.com");

			Observation obs = new Observation();
			obs.getCode().setText("Observation 3");
			obs.getContained().add(p);
			obs.getSubject().setReference("#patient3");
			obs.getContained().add(carePlan);
			obs.getBasedOnFirstRep().setReference("#carePlan3");

			myObservationDao.create(obs, mySrd).getId().toUnqualifiedVersionless();

			ourLog.info("Input: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(obs));
		}

		//-- Search by uri
		String uri = ourServerBase + "/Observation?based-on.instantiates-uri=http://www.hl7.com";
		List<String> oids = searchAndReturnUnqualifiedVersionlessIdValues(uri);

		assertEquals(1L, oids.size());
		assertThat(oids, contains(oid1.getValue()));

		//-- Search by uri more than 1 results
		uri = ourServerBase + "/Observation?based-on.instantiates-uri=http://www2.hl7.com";
		oids = searchAndReturnUnqualifiedVersionlessIdValues(uri);

		assertEquals(2L, oids.size());

		//-- Search by uri with 'or'
		uri = ourServerBase + "/Observation?based-on.instantiates-uri=http://www.hl7.com,http://www2.hl7.com";
		oids = searchAndReturnUnqualifiedVersionlessIdValues(uri);

		assertEquals(3L, oids.size());

	}

	@Test
	public void testUpdateContainedResource() throws Exception {

		IIdType oid1;

		{
			Patient p = new Patient();
			p.setId("patient1");
			p.addName().setFamily("Smith").addGiven("John");

			Observation obs = new Observation();
			obs.getCode().setText("Observation 1");
			obs.getContained().add(p);
			obs.getSubject().setReference("#patient1");

			oid1 = myObservationDao.create(obs, mySrd).getId().toUnqualifiedVersionless();
			logAllStringIndexes("subject.family");

			ourLog.info("Input: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(obs));

			Observation createdObs = myObservationDao.read(oid1);

			//-- changed the last name to Doe
			List<Resource> containedResources = createdObs.getContained();

			for (Resource res : containedResources) {
				if (res instanceof Patient) {
					Patient p1 = (Patient) res;
					HumanName name = p1.getNameFirstRep();
					name.setFamily("Doe");
					break;
				}
			}

			// -- update
			myObservationDao.update(createdObs, mySrd).getId().toUnqualifiedVersionless();
			logAllStringIndexes("subject.family");

		}

		{
			Patient p = new Patient();
			p.setId("patient1");
			p.addName().setFamily("Doe").addGiven("Jane");

			Observation obs = new Observation();
			obs.getCode().setText("Observation 2");
			obs.getContained().add(p);
			obs.getSubject().setReference("#patient1");

			myObservationDao.create(obs, mySrd).getId().toUnqualifiedVersionless();
			logAllStringIndexes("subject.family");

			ourLog.info("Input: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(obs));
		}


		{
			Patient p = new Patient();
			p.setId("patient1");
			p.addName().setFamily("Jones").addGiven("Peter");

			Observation obs = new Observation();
			obs.getCode().setText("Observation 2");
			obs.getContained().add(p);
			obs.getSubject().setReference("#patient1");

			myObservationDao.create(obs, mySrd).getId().toUnqualifiedVersionless();

			ourLog.info("Input: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(obs));
		}


		//-- No Obs with Patient Smith
		String uri = ourServerBase + "/Observation?subject.family=Smith&_contained=true";
		List<String> oids = searchAndReturnUnqualifiedVersionlessIdValues(uri);

		assertEquals(0L, oids.size());

		//-- Two Obs with Patient Doe
		uri = ourServerBase + "/Observation?subject.family=Doe&_contained=true";
		oids = searchAndReturnUnqualifiedVersionlessIdValues(uri);

		assertEquals(2L, oids.size());
	}


	@Test
	public void testDeleteContainedResource() throws Exception {

		IIdType oid1;

		{
			Patient p1 = new Patient();
			p1.setId("patient1");
			p1.addName().setFamily("Smith").addGiven("John");

			Patient p2 = new Patient();
			p2.setId("patient2");
			p2.addName().setFamily("Doe").addGiven("Jane");

			Observation obs = new Observation();
			obs.getCode().setText("Observation 1");
			obs.getContained().add(p1);
			obs.getSubject().setReference("#patient1");

			oid1 = myObservationDao.create(obs, mySrd).getId().toUnqualifiedVersionless();

			// -- remove contained resource
			obs.getContained().remove(p1);
			// -- add new contained resource
			obs.getContained().add(p2);
			obs.getSubject().setReference("#patient2");

			ourLog.info("Input: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(obs));

			// -- update
			oid1 = myObservationDao.update(obs, mySrd).getId().toUnqualifiedVersionless();

			Observation updatedObs = myObservationDao.read(oid1);

			ourLog.info("Output: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(updatedObs));
		}

		//-- No Obs with Patient Smith
		String uri = ourServerBase + "/Observation?subject.family=Smith&_contained=true";
		List<String> oids = searchAndReturnUnqualifiedVersionlessIdValues(uri);

		assertEquals(0L, oids.size());

		//-- 1 Obs with Patient Doe
		uri = ourServerBase + "/Observation?subject.family=Doe&_contained=true";
		oids = searchAndReturnUnqualifiedVersionlessIdValues(uri);

		assertEquals(1L, oids.size());
		assertThat(oids, contains(oid1.getValue()));
	}

	//See https://github.com/hapifhir/hapi-fhir/issues/2887
	@Test
	public void testContainedResourceParameterIsUsedInCache() {

	}

	private List<String> searchAndReturnUnqualifiedVersionlessIdValues(String uri) throws IOException {
		List<String> ids;
		HttpGet get = new HttpGet(uri);

		try (CloseableHttpResponse response = ourHttpClient.execute(get)) {
			String resp = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(resp);
			Bundle bundle = myFhirContext.newXmlParser().parseResource(Bundle.class, resp);
			ids = toUnqualifiedVersionlessIdValues(bundle);
		}
		return ids;
	}

}
