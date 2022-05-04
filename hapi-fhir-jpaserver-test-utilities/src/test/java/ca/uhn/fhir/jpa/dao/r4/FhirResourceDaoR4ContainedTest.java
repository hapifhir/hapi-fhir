package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Address;
import org.hl7.fhir.r4.model.Address.AddressUse;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.DateTimeType;
import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.Encounter.EncounterParticipantComponent;
import org.hl7.fhir.r4.model.Encounter.EncounterStatus;
import org.hl7.fhir.r4.model.Enumerations.AdministrativeGender;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Practitioner;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.ServiceRequest;
import org.hl7.fhir.r4.model.ServiceRequest.ServiceRequestIntent;
import org.hl7.fhir.r4.model.ServiceRequest.ServiceRequestStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class FhirResourceDaoR4ContainedTest extends BaseJpaR4Test {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirResourceDaoR4ContainedTest.class);

	@BeforeEach
	public void before() throws Exception {
		myModelConfig.setIndexOnContainedResources(true);
	}

	@AfterEach
	public void after() throws Exception {
		myModelConfig.setIndexOnContainedResources(false);
	}

	@Test
	public void testCreateSimpleContainedResourceIndexWithGeneratedId() {

		Patient p = new Patient();
		p.addName().setFamily("Smith").addGiven("John");
		
		Observation obs = new Observation();
		obs.getCode().setText("Some Observation");
		obs.setSubject(new Reference(p));
		 				
		ourLog.info("Input: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(obs));

		IIdType id = myObservationDao.create(obs, mySrd).getId().toUnqualifiedVersionless();

		Observation createdObs = myObservationDao.read(id);
		
		ourLog.info("Output: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(createdObs));
		
		runInTransaction(()->{
			ourLog.info("String indexes:\n * {}", myResourceIndexedSearchParamStringDao.findAll().stream().map(t->t.toString()).collect(Collectors.joining("\n * ")));

			Long i = myEntityManager
				.createQuery("SELECT count(s) FROM ResourceIndexedSearchParamString s WHERE s.myParamName = 'subject.family' AND s.myResourceType = 'Observation'", Long.class)
				.getSingleResult();
			assertEquals(1L, i.longValue());
		});
		
		SearchParameterMap map;

		map = new SearchParameterMap();
		map.add("subject", new ReferenceParam("name", "Smith"));
		assertThat(toUnqualifiedVersionlessIdValues(myObservationDao.search(map)), containsInAnyOrder(toValues(id)));
	}
	
	@Test
	public void testCreateSimpleContainedResourceIndexUserDefinedId() {

		Patient p = new Patient();
		p.setId("fooId");
		p.addName().setFamily("Smith").addGiven("John");
		
		Observation obs = new Observation();
		obs.getCode().setText("Some Observation");
		obs.getContained().add(p);
		obs.getSubject().setReference("#fooId");
		
		ourLog.info("Input: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(obs));

		IIdType id = myObservationDao.create(obs, mySrd).getId().toUnqualifiedVersionless();

		Observation createdObs = myObservationDao.read(id);
		
		ourLog.info("Output: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(createdObs));
		
		runInTransaction(()->{
			Long i = myEntityManager
				.createQuery("SELECT count(s) FROM ResourceIndexedSearchParamString s WHERE s.myParamName = 'subject.family' AND s.myResourceType = 'Observation'", Long.class)
				.getSingleResult();
			assertEquals(1L, i.longValue());
		});

		SearchParameterMap map;

		map = new SearchParameterMap();
		map.add("subject", new ReferenceParam("name", "Smith"));
		map.setLoadSynchronous(true);

		assertThat(toUnqualifiedVersionlessIdValues(myObservationDao.search(map)), containsInAnyOrder(toValues(id)));

	}
	
	
	@Test
	public void testCreateMultipleContainedResourceIndex() {

		Practitioner prac1 = new Practitioner();
		prac1.setId("prac1");
		prac1.setActive(true);
		prac1.setGender(AdministrativeGender.FEMALE);
		prac1.addName().setFamily("Smith").addGiven("John");
		Address address = prac1.addAddress();
		address.setUse(AddressUse.WORK);
		address.addLine("534 Erewhon St");
		address.setCity("PleasantVille");
		address.setState("NY");
		address.setPostalCode("12345");
		
		Organization org1 = new Organization();
		org1.setId("org1");
		org1.setActive(true);
		org1.setName("org name 1");

		Organization org2 = new Organization();
		org2.setId("org2");
		org2.setActive(false);
		org2.setName("org name 2");

		Patient patient = new Patient();
		patient.getContained().add(prac1);
		patient.getContained().add(org1);
		patient.getContained().add(org2);
		patient.addName().setFamily("Doe").addGiven("Jane");
		patient.addGeneralPractitioner().setReference("#prac1");
		patient.addGeneralPractitioner().setReference("#org1");
		patient.getManagingOrganization().setReference("#org2");
				
		ourLog.info("Input: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(patient));

		IIdType id = myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless();

		Patient createdPatient = myPatientDao.read(id);
		
		ourLog.info("Output: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(createdPatient));
		
		runInTransaction(()->{
			Long i = myEntityManager
				.createQuery("SELECT count(s) FROM ResourceIndexedSearchParamString s WHERE s.myParamName = 'general-practitioner.family' AND s.myResourceType = 'Patient'", Long.class)
				.getSingleResult();
			assertEquals(1L, i.longValue());

			i = myEntityManager
				.createQuery("SELECT count(s) FROM ResourceIndexedSearchParamString s WHERE s.myParamName = 'general-practitioner.name' AND s.myResourceType = 'Patient'", Long.class)
				.getSingleResult();
			assertEquals(3L, i.longValue());

			i = myEntityManager
					.createQuery("SELECT count(s) FROM ResourceIndexedSearchParamString s WHERE s.myParamName = 'organization.name' AND s.myResourceType = 'Patient'", Long.class)
					.getSingleResult();
		    assertEquals(1L, i.longValue());
		});
		
		SearchParameterMap map;

		map = new SearchParameterMap();
		map.add("general-practitioner", new ReferenceParam("family", "Smith"));

		assertThat(toUnqualifiedVersionlessIdValues(myPatientDao.search(map)), containsInAnyOrder(toValues(id)));
	}
	
	@Test
	public void testCreateComplexContainedResourceIndex() {

		Encounter encounter = new Encounter();
		encounter.setStatus(EncounterStatus.ARRIVED);
		
		Patient patient = new Patient();
		patient.setId("patient1");
		patient.addName().setFamily("Doe").addGiven("Jane");
		encounter.getSubject().setReference("#patient1");
		encounter.getContained().add(patient);
		
		ServiceRequest serviceRequest = new ServiceRequest();
		serviceRequest.setId("serviceRequest1");
		serviceRequest.setStatus(ServiceRequestStatus.ACTIVE);
		serviceRequest.setIntent(ServiceRequestIntent.ORDER);
		serviceRequest.setAuthoredOnElement(new DateTimeType("2021-02-23"));
		encounter.addBasedOn().setReference("#serviceRequest1");
		encounter.getContained().add(serviceRequest);

		Practitioner prac1 = new Practitioner();
		prac1.setId("prac1");
		prac1.setActive(true);
		prac1.setGender(AdministrativeGender.FEMALE);
		prac1.addName().setFamily("Smith").addGiven("John");
		EncounterParticipantComponent participient = encounter.addParticipant();
		participient.getIndividual().setReference("#prac1");
		encounter.getContained().add(prac1);
		
		Observation obs = new Observation();
		obs.setId("obs1");
		obs.addIdentifier().setSystem("urn:system").setValue("FOO");
		obs.getSubject().setReference("#patient1");
		CodeableConcept cc = obs.getCode();
		cc.addCoding().setCode("2345-7").setSystem("http://loinc.org");
		encounter.addReasonReference().setReference("#obs1");
		encounter.getContained().add(obs);
		
		ourLog.info("Input: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(encounter));

		IIdType id = myEncounterDao.create(encounter, mySrd).getId().toUnqualifiedVersionless();

		Encounter createdEncounter = myEncounterDao.read(id);
		
		ourLog.info("Output: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(createdEncounter));
		
		runInTransaction(()->{
			// The practitioner
			Long i = myEntityManager
				.createQuery("SELECT count(s) FROM ResourceIndexedSearchParamString s WHERE s.myParamName = 'participant.family' AND s.myResourceType = 'Encounter'", Long.class)
				.getSingleResult();
			assertEquals(1L, i.longValue());

			// The Patient
			i = myEntityManager
				.createQuery("SELECT count(s) FROM ResourceIndexedSearchParamString s WHERE s.myParamName = 'subject.family' AND s.myResourceType = 'Encounter'", Long.class)
				.getSingleResult();
			assertEquals(1L, i.longValue());

			// The Observation
			i = myEntityManager
					.createQuery("SELECT count(s) FROM ResourceIndexedSearchParamToken s WHERE s.myParamName = 'reason-reference.code' AND s.myResourceType = 'Encounter'", Long.class)
					.getSingleResult();
		    assertEquals(1L, i.longValue());
			i = myEntityManager
					.createQuery("SELECT count(s) FROM ResourceIndexedSearchParamToken s WHERE s.myParamName = 'reason-reference.combo-code' AND s.myResourceType = 'Encounter'", Long.class)
					.getSingleResult();
		    assertEquals(1L, i.longValue());
		    
		    // The ServiceRequest
			i = myEntityManager
					.createQuery("SELECT count(s) FROM ResourceIndexedSearchParamDate s WHERE s.myParamName = 'based-on.authored' AND s.myResourceType = 'Encounter'", Long.class)
					.getSingleResult();
		    assertEquals(1L, i.longValue());
		});
		
		SearchParameterMap map;

		map = new SearchParameterMap();
		map.add("based-on", new ReferenceParam("authored", "2021-02-23"));

		assertThat(toUnqualifiedVersionlessIdValues(myEncounterDao.search(map)), containsInAnyOrder(toValues(id)));
	}
	
	@Test
	public void testSearchWithNotSupportedSearchParameter() {

		SearchParameterMap map;

		map = new SearchParameterMap();
		map.add("subject", new ReferenceParam("marital-status", "M"));

		try {
			IBundleProvider outcome = myObservationDao.search(map);
			outcome.getResources(0, 1).get(0);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals(Msg.code(1214) + "Invalid parameter chain: subject.marital-status", e.getMessage());
		}
		
	}
}
