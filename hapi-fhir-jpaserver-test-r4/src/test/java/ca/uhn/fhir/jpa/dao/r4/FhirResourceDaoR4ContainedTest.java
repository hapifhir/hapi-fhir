package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.TokenAndListParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.util.BundleBuilder;
import com.apicatalog.jsonld.StringUtils;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Address;
import org.hl7.fhir.r4.model.Address.AddressUse;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.DateTimeType;
import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.Encounter.EncounterParticipantComponent;
import org.hl7.fhir.r4.model.Encounter.EncounterStatus;
import org.hl7.fhir.r4.model.Endpoint;
import org.hl7.fhir.r4.model.Enumerations.AdministrativeGender;
import org.hl7.fhir.r4.model.Location;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Practitioner;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.ServiceRequest;
import org.hl7.fhir.r4.model.ServiceRequest.ServiceRequestIntent;
import org.hl7.fhir.r4.model.ServiceRequest.ServiceRequestStatus;
import org.intellij.lang.annotations.Language;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class FhirResourceDaoR4ContainedTest extends BaseJpaR4Test {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirResourceDaoR4ContainedTest.class);

	@BeforeEach
	public void before() throws Exception {
		myStorageSettings.setIndexOnContainedResources(true);
	}

	@AfterEach
	public void after() throws Exception {
		myStorageSettings.setIndexOnContainedResources(false);
	}

	@Test
	public void testCreateSimpleContainedResourceIndexWithGeneratedId() {

		Patient p = new Patient();
		p.addName().setFamily("Smith").addGiven("John");
		
		Observation obs = new Observation();
		obs.getCode().setText("Some Observation");
		obs.setSubject(new Reference(p));
		 				
		ourLog.debug("Input: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(obs));

		IIdType id = myObservationDao.create(obs, mySrd).getId().toUnqualifiedVersionless();

		Observation createdObs = myObservationDao.read(id);
		
		ourLog.debug("Output: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(createdObs));
		
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
		assertThat(toUnqualifiedVersionlessIdValues(myObservationDao.search(map))).containsExactlyInAnyOrder(toValues(id));
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
		
		ourLog.debug("Input: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(obs));

		IIdType id = myObservationDao.create(obs, mySrd).getId().toUnqualifiedVersionless();

		Observation createdObs = myObservationDao.read(id);
		
		ourLog.debug("Output: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(createdObs));
		
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

		assertThat(toUnqualifiedVersionlessIdValues(myObservationDao.search(map))).containsExactlyInAnyOrder(toValues(id));
	}

	@Test
	public void containedResource_withInternalReferenceToContainer_works() {
		// setup
		SystemRequestDetails rd = new SystemRequestDetails();

		Organization org = new Organization();
//		Location location = new Location();
//		location.setManagingOrganization(new Reference("#"));
//		org.addContained(location);
		org.addIdentifier()
			.setSystem("http://example.com")
			.setValue("123456");
		org.setActive(true);
		Endpoint endpoint = new Endpoint();
		endpoint.setStatus(Endpoint.EndpointStatus.ACTIVE);
		endpoint.addPayloadType().addCoding()
				.setSystem("http://endpoint.com")
					.setCode("code");
		endpoint.setAddress("http://endpoint.com/home");
		Coding connectionType = new Coding();
		connectionType.setSystem("http://connectiontype.com");
		connectionType.setCode("abc");
		endpoint.setConnectionType(connectionType);
		endpoint.setManagingOrganization(new Reference("#"));
		org.setEndpoint(List.of(new Reference(endpoint)));

		Bundle bundleBuilder = new BundleBuilder(myFhirContext)
			.addTransactionCreateEntry(org).andThen().getBundleTyped();

		// test
		mySystemDao.transaction(new SystemRequestDetails(), bundleBuilder);

		// validate
		SearchParameterMap spm = new SearchParameterMap();
		spm.setLoadSynchronous(true);
		spm.add("identifier", new TokenParam("http://example.com", "123456"));
		IBundleProvider results = myOrganizationDao.search(spm, rd);
		assertFalse(results.getAllResources().isEmpty());

		Organization retOrg = (Organization) results.getAllResources().get(0);
		assertEquals(1, retOrg.getEndpoint().size());
		Reference containedEndpointRef = retOrg.getEndpoint().get(0);
		assertNotNull(containedEndpointRef.getResource());
		// TODO - is this correct?
		Endpoint retEndpoint = (Endpoint) containedEndpointRef.getResource();
		assertEquals("#", retEndpoint.getManagingOrganization().getReference());
	}

	public static Stream<Arguments> generateTestCases2() {
		String uuid = UUID.randomUUID().toString();
		return Stream.of(
			Arguments.of(uuid, true, true, true, null),
			Arguments.of(uuid, true, true, false, "There is a reference that begins with #, but no resource with this ID is contained"),
			Arguments.of(uuid, true, false, true, null),
			Arguments.of(uuid, true, false, false, null),
			Arguments.of(uuid, false, true, true, null),
			Arguments.of(uuid, false, true, false, "There is a reference that begins with #, but no resource with this ID is contained"),
			Arguments.of(uuid, false, false, true, null),
			Arguments.of(uuid, false, false, false, null)
		);
	}


	@ParameterizedTest(name = "encode={1}, refByID={2}, addedToContainedList={3}, failureMsg={4}")
	@MethodSource("generateTestCases2")
	public void testJavaContainedBuildingCases(String theContainedId, boolean theShouldEncodeFirst, boolean theShouldSetReferenceById, boolean theShouldAddToContainedList, String theExceptionMessage) {
		Patient p = new Patient();
		Organization org = new Organization();
		if (theShouldAddToContainedList) {
			p.addContained(org);
		}

		if (theShouldSetReferenceById) {
			org.setId(theContainedId);
			p.setManagingOrganization(new Reference("#" + org.getId()));
		} else {
			p.setManagingOrganization(new Reference(org));
			p.getManagingOrganization().setResource(org);
		}

		Bundle bundleBuilder = new BundleBuilder(myFhirContext).addTransactionCreateEntry(p).andThen().getBundleTyped();
		try {

			if (theShouldEncodeFirst) {
				//When
				String encoded = myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(bundleBuilder);
				ourLog.info("Encoded value is:\n***{}\n***", encoded);
			}
			//When
			mySystemDao.transaction(new SystemRequestDetails(), bundleBuilder);

			if (theExceptionMessage != null) {
				fail("Expected message containing: " + theExceptionMessage);
			}

			//Then
			assertPatientSuccesfullyContainedOrganization(theContainedId, theShouldSetReferenceById);
		} catch (Exception e) {
			//Then, if our test case expects an exception, assert on it.
			if (!StringUtils.isBlank(theExceptionMessage)) {
				assertThat(e.getMessage()).containsIgnoringCase(theExceptionMessage);
			} else{ //Otherwise, our test case did not expect an exception, but we got one! rethrow it.
				throw e;
			}
		}
	}

	void assertPatientSuccesfullyContainedOrganization(String theContainedId, boolean theShouldSetReferenceById) {
		IBundleProvider search = myPatientDao.search(new SearchParameterMap().setLoadSynchronous(true));
		List<IBaseResource> allResources = search.getAllResources();
		Patient pat = (Patient) allResources.get(0);
		assertThat(pat.getContained()).hasSize(1);
		Organization foundOrg = (Organization) pat.getContained().get(0);

		if (theShouldSetReferenceById) {
			assertThat(pat.getManagingOrganization().getReference()).isEqualTo("#" + theContainedId);
		} else {
			assertThat(pat.getManagingOrganization().getReference()).isEqualTo("#" + foundOrg.getId());
			assertThat(foundOrg.getId()).hasSize(36); //uuid length.
		}
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
				
		ourLog.debug("Input: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(patient));

		IIdType id = myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless();

		Patient createdPatient = myPatientDao.read(id);
		
		ourLog.debug("Output: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(createdPatient));
		
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

		assertThat(toUnqualifiedVersionlessIdValues(myPatientDao.search(map))).containsExactlyInAnyOrder(toValues(id));
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
		
		ourLog.debug("Input: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(encounter));

		IIdType id = myEncounterDao.create(encounter, mySrd).getId().toUnqualifiedVersionless();

		Encounter createdEncounter = myEncounterDao.read(id);
		
		ourLog.debug("Output: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(createdEncounter));
		
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

		assertThat(toUnqualifiedVersionlessIdValues(myEncounterDao.search(map))).containsExactlyInAnyOrder(toValues(id));
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
