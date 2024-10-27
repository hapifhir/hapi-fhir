package ca.uhn.fhir.jpa.dao.r4;

import static org.junit.jupiter.api.Assertions.assertEquals;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.dao.data.IResourceHistoryProvenanceDao;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.CarePlan;
import org.hl7.fhir.r4.model.DateType;
import org.hl7.fhir.r4.model.DecimalType;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.HealthcareService;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Location;
import org.hl7.fhir.r4.model.Meta;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Practitioner;
import org.hl7.fhir.r4.model.PractitionerRole;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.RiskAssessment;
import org.hl7.fhir.r4.model.SearchParameter;
import org.hl7.fhir.r4.model.ValueSet;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

@SuppressWarnings({"Duplicates"})
public class FhirResourceDaoR4FilterTest extends BaseJpaR4Test {

	private static final Logger ourLog = LoggerFactory.getLogger(FhirResourceDaoR4FilterTest.class);
	@Autowired
	private IResourceHistoryProvenanceDao myResourceProvenanceDao;
	@Autowired
	@Qualifier("myHealthcareServiceDaoR4")
	protected IFhirResourceDao<HealthcareService> myHealthcareServiceDao;

	@AfterEach
	public void after() {
		myStorageSettings.setFilterParameterEnabled(new JpaStorageSettings().isFilterParameterEnabled());
	}

	@BeforeEach
	public void before() {
		myStorageSettings.setFilterParameterEnabled(true);
	}

	@Test
	public void testMalformedFilter() {
		SearchParameterMap map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add(Constants.PARAM_FILTER, new StringParam("name eq smith))"));
		try {
			myPatientDao.search(map);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals(Msg.code(1221) + "Error parsing _filter syntax: " + Msg.code(1056) + "Expression did not terminate at 13", e.getMessage());
		}
	}

	@Test
	public void testBrackets() {

		Patient p = new Patient();
		p.addName().setFamily("Smith").addGiven("John");
		p.setActive(true);
		String id1 = myPatientDao.create(p).getId().toUnqualifiedVersionless().getValue();

		p = new Patient();
		p.addName().setFamily("Jones").addGiven("Frank");
		p.setActive(false);
		String id2 = myPatientDao.create(p).getId().toUnqualifiedVersionless().getValue();

		SearchParameterMap map;
		List<String> found;

		map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add(Constants.PARAM_FILTER, new StringParam("name eq smith"));
		found = toUnqualifiedVersionlessIdValues(myPatientDao.search(map));
		assertThat(found).containsExactlyInAnyOrder(id1);

		map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add(Constants.PARAM_FILTER, new StringParam("(name eq smith) or (name eq jones)"));
		found = toUnqualifiedVersionlessIdValues(myPatientDao.search(map));
		assertThat(found).containsExactlyInAnyOrder(id1, id2);

	}

	@Test
	public void testStringComparatorEq() {

		Patient p = new Patient();
		p.addName().setFamily("Smith").addGiven("John");
		p.setActive(true);
		String id1 = myPatientDao.create(p).getId().toUnqualifiedVersionless().getValue();

		p = new Patient();
		p.addName().setFamily("Jones").addGiven("Frank");
		p.setActive(false);
		String id2 = myPatientDao.create(p).getId().toUnqualifiedVersionless().getValue();

		SearchParameterMap map;
		List<String> found;

		map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add(Constants.PARAM_FILTER, new StringParam("name eq smi"));
		found = toUnqualifiedVersionlessIdValues(myPatientDao.search(map));
		assertThat(found).isEmpty();

		map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add(Constants.PARAM_FILTER, new StringParam("name eq smith"));
		found = toUnqualifiedVersionlessIdValues(myPatientDao.search(map));
		assertThat(found).containsExactlyInAnyOrder(id1);

	}

	@Test
	public void testReferenceComparatorEq() {

		Patient p = new Patient();
		p.addName().setFamily("Smith").addGiven("John");
		p.setActive(true);
		IIdType ptId = myPatientDao.create(p).getId().toUnqualifiedVersionless();

		p = new Patient();
		p.addName().setFamily("Smith").addGiven("John2");
		p.setActive(true);
		IIdType ptId2 = myPatientDao.create(p).getId().toUnqualifiedVersionless();

		p = new Patient();
		p.addName().setFamily("Smith").addGiven("John3");
		p.setActive(true);
		IIdType ptId3 = myPatientDao.create(p).getId().toUnqualifiedVersionless();

		CarePlan cp = new CarePlan();
		cp.getSubject().setReference(ptId.getValue());
		String cpId = myCarePlanDao.create(cp).getId().toUnqualifiedVersionless().getValue();

		cp = new CarePlan();
		cp.addActivity().getDetail().addPerformer().setReference(ptId2.getValue());
		String cpId2 = myCarePlanDao.create(cp).getId().toUnqualifiedVersionless().getValue();

		SearchParameterMap map;
		List<String> found;

		map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add(Constants.PARAM_FILTER, new StringParam("subject eq " + ptId.getValue()));
		found = toUnqualifiedVersionlessIdValues(myCarePlanDao.search(map));
		assertThat(found).containsExactlyInAnyOrder(cpId);

		map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add(Constants.PARAM_FILTER, new StringParam("subject eq " + ptId.getIdPart()));
		found = toUnqualifiedVersionlessIdValues(myCarePlanDao.search(map));
		assertThat(found).containsExactlyInAnyOrder(cpId);

		map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add(Constants.PARAM_FILTER, new StringParam("(subject eq " + ptId.getIdPart() + ") or (performer eq " + ptId2.getValue() + ")"));
		found = toUnqualifiedVersionlessIdValues(myCarePlanDao.search(map));
		assertThat(found).containsExactlyInAnyOrder(cpId, cpId2);

	}

	@Test
	public void testSourceComparatorEq() {
		myStorageSettings.setStoreMetaSourceInformation(JpaStorageSettings.StoreMetaSourceInformationEnum.SOURCE_URI_AND_REQUEST_ID);

		Patient p = new Patient();
		p.getMeta().setSource("http://source");
		p.addName().setFamily("Smith").addGiven("John");
		p.setActive(true);
		String ptId = myPatientDao.create(p).getId().toUnqualifiedVersionless().getValue();

		p = new Patient();
		p.addName().setFamily("Smith").addGiven("John2");
		p.setActive(true);
		myPatientDao.create(p).getId().toUnqualifiedVersionless();

		SearchParameterMap map;
		List<String> found;

		runInTransaction(() -> {
			ourLog.info("Provenance:\n * {}", myResourceProvenanceDao.findAll().stream().map(t -> t.toString()).collect(Collectors.joining("\n * ")));
		});

		map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add(Constants.PARAM_FILTER, new StringParam("_source eq http://source"));
		myCaptureQueriesListener.clear();
		found = toUnqualifiedVersionlessIdValues(myPatientDao.search(map));
		myCaptureQueriesListener.logSelectQueriesForCurrentThread(0);
		assertThat(found).containsExactlyInAnyOrder(ptId);

	}

	@Test
	public void testFilterDisabled() {
		myStorageSettings.setFilterParameterEnabled(false);

		SearchParameterMap map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add(Constants.PARAM_FILTER, new StringParam("name eq smith"));
		try {
			myPatientDao.search(map);
		} catch (InvalidRequestException e) {
			assertEquals(Msg.code(1222) + "_filter parameter is disabled on this server", e.getMessage());
		}
	}

	/**
	 * Use the Encounter DAO to find things that would match a Patient
	 */
	@Test
	public void testRetrieveDifferentTypeEq() {

		Patient p = new Patient();
		p.addName().setFamily("Smith").addGiven("John");
		p.setActive(true);
		String id1 = myPatientDao.create(p).getId().toUnqualifiedVersionless().getValue();
		String idVal = id1.split("/")[1];

		SearchParameterMap map;
		List<String> found;

		map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add(Constants.PARAM_FILTER, new StringParam(String.format("status eq active or _id eq %s", idVal)));
		myCaptureQueriesListener.clear();
		found = toUnqualifiedVersionlessIdValues(myEncounterDao.search(map));
		myCaptureQueriesListener.logSelectQueriesForCurrentThread(0);
		assertThat(found).isEmpty();

		map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add(Constants.PARAM_FILTER, new StringParam(String.format("status eq inactive or _id eq %s", idVal)));
		found = toUnqualifiedVersionlessIdValues(myEncounterDao.search(map));
		assertThat(found).isEmpty();

		map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add(Constants.PARAM_FILTER, new StringParam(String.format("status eq inactive or _id eq Patient/FOO")));
		found = toUnqualifiedVersionlessIdValues(myEncounterDao.search(map));
		assertThat(found).isEmpty();

		map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add(Constants.PARAM_FILTER, new StringParam(String.format("_id eq %s", idVal)));
		found = toUnqualifiedVersionlessIdValues(myEncounterDao.search(map));
		assertThat(found).isEmpty();

	}

	@Test
	public void testStringComparatorNe() {

		Patient p = new Patient();
		p.addName().setFamily("Smith").addGiven("John");
		p.setActive(true);
		String id1 = myPatientDao.create(p).getId().toUnqualifiedVersionless().getValue();

		p = new Patient();
		p.addName().setFamily("Jones").addGiven("Frank");
		p.setActive(false);
		String id2 = myPatientDao.create(p).getId().toUnqualifiedVersionless().getValue();

		SearchParameterMap map;
		List<String> found;

		map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add(Constants.PARAM_FILTER, new StringParam("family ne smith"));
		found = toUnqualifiedVersionlessIdValues(myPatientDao.search(map));

		assertThat(found)
			.hasSize(1)
			.containsExactlyInAnyOrder(id2)
			.doesNotContain(id1);

		map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add(Constants.PARAM_FILTER, new StringParam("family ne jones"));
		found = toUnqualifiedVersionlessIdValues(myPatientDao.search(map));
		assertThat(found)
			.hasSize(1)
			.containsExactlyInAnyOrder(id1)
			.doesNotContain(id2);

		map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add(Constants.PARAM_FILTER, new StringParam("given ne john"));
		found = toUnqualifiedVersionlessIdValues(myPatientDao.search(map));
		assertThat(found).containsExactlyInAnyOrder(id2);
		assertThat(found).doesNotContain(id1);
		assertThat(found)
			.hasSize(1)
			.containsExactlyInAnyOrder(id2)
			.doesNotContain(id1);

		map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add(Constants.PARAM_FILTER, new StringParam("given ne frank"));
		found = toUnqualifiedVersionlessIdValues(myPatientDao.search(map));
		assertThat(found).containsExactlyInAnyOrder(id1);
		assertThat(found).doesNotContain(id2);

	}

	@Test
	public void testReferenceComparatorNe() {

		Patient p = new Patient();
		p.addName().setFamily("Smith").addGiven("John");
		p.setActive(true);
		IIdType ptId = myPatientDao.create(p).getId().toUnqualifiedVersionless();

		p = new Patient();
		p.addName().setFamily("Smith").addGiven("John2");
		p.setActive(true);
		IIdType ptId2 = myPatientDao.create(p).getId().toUnqualifiedVersionless();

		CarePlan cp = new CarePlan();
		cp.getSubject().setReference(ptId.getValue());
		myCarePlanDao.create(cp).getId().toUnqualifiedVersionless().getValue();

		cp = new CarePlan();
		cp.getSubject().setReference(ptId2.getValue());
		cp.addActivity().getDetail().addPerformer().setReference(ptId2.getValue());
		String cpId2 = myCarePlanDao.create(cp).getId().toUnqualifiedVersionless().getValue();

		SearchParameterMap map;
		List<String> found;

		map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add(Constants.PARAM_FILTER, new StringParam("subject ne " + ptId.getValue()));
		myCaptureQueriesListener.clear();
		found = toUnqualifiedVersionlessIdValues(myCarePlanDao.search(map));
		myCaptureQueriesListener.logSelectQueriesForCurrentThread(0);
		assertThat(found).containsExactlyInAnyOrder(cpId2);

		map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add(Constants.PARAM_FILTER, new StringParam("(subject ne " + ptId.getIdPart() + ") and (performer ne " + ptId2.getValue() + ")"));
		found = toUnqualifiedVersionlessIdValues(myCarePlanDao.search(map));
		assertThat(found).isEmpty();

	}


	@Test
	public void testStringComparatorCo() {

		Patient p = new Patient();
		p.addName().setFamily("Smith").addGiven("John");
		p.setActive(true);
		String id1 = myPatientDao.create(p).getId().toUnqualifiedVersionless().getValue();

		p = new Patient();
		p.addName().setFamily("Jones").addGiven("Frank");
		p.setActive(false);
		String id2 = myPatientDao.create(p).getId().toUnqualifiedVersionless().getValue();

		SearchParameterMap map;
		List<String> found;

		map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add(Constants.PARAM_FILTER, new StringParam("name co smi"));
		found = toUnqualifiedVersionlessIdValues(myPatientDao.search(map));
		assertThat(found).containsExactlyInAnyOrder(id1);

		map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add(Constants.PARAM_FILTER, new StringParam("name co smith"));
		found = toUnqualifiedVersionlessIdValues(myPatientDao.search(map));
		assertThat(found).containsExactlyInAnyOrder(id1);

		map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add(Constants.PARAM_FILTER, new StringParam("given co frank"));
		found = toUnqualifiedVersionlessIdValues(myPatientDao.search(map));
		assertThat(found).containsExactlyInAnyOrder(id2);

		map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add(Constants.PARAM_FILTER, new StringParam("family co jones"));
		found = toUnqualifiedVersionlessIdValues(myPatientDao.search(map));
		assertThat(found).containsExactlyInAnyOrder(id2);

	}

	@Test
	public void testStringComparatorSw() {

		Patient p = new Patient();
		p.addName().setFamily("Smith").addGiven("John");
		p.setActive(true);
		String id1 = myPatientDao.create(p).getId().toUnqualifiedVersionless().getValue();

		p = new Patient();
		p.addName().setFamily("Jones").addGiven("Frank");
		p.setActive(false);
		String id2 = myPatientDao.create(p).getId().toUnqualifiedVersionless().getValue();

		SearchParameterMap map;
		List<String> found;

		map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add(Constants.PARAM_FILTER, new StringParam("name sw smi"));
		found = toUnqualifiedVersionlessIdValues(myPatientDao.search(map));
		assertThat(found).containsExactlyInAnyOrder(id1);

		map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add(Constants.PARAM_FILTER, new StringParam("name sw mi"));
		found = toUnqualifiedVersionlessIdValues(myPatientDao.search(map));
		assertThat(found).isEmpty();

		map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add(Constants.PARAM_FILTER, new StringParam("given sw fr"));
		found = toUnqualifiedVersionlessIdValues(myPatientDao.search(map));
		assertThat(found).containsExactlyInAnyOrder(id2);

	}

	@Test
	public void testStringComparatorEw() {

		Patient p = new Patient();
		p.addName().setFamily("Smith").addGiven("John");
		p.setActive(true);
		String id1 = myPatientDao.create(p).getId().toUnqualifiedVersionless().getValue();

		p = new Patient();
		p.addName().setFamily("Jones").addGiven("Frank");
		p.setActive(false);
		String id2 = myPatientDao.create(p).getId().toUnqualifiedVersionless().getValue();

		SearchParameterMap map;
		List<String> found;

		map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add(Constants.PARAM_FILTER, new StringParam("family ew ith"));
		found = toUnqualifiedVersionlessIdValues(myPatientDao.search(map));
		assertThat(found).containsExactlyInAnyOrder(id1);

		map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add(Constants.PARAM_FILTER, new StringParam("name ew it"));
		found = toUnqualifiedVersionlessIdValues(myPatientDao.search(map));
		assertThat(found).isEmpty();

		map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add(Constants.PARAM_FILTER, new StringParam("given ew nk"));
		found = toUnqualifiedVersionlessIdValues(myPatientDao.search(map));
		assertThat(found).containsExactlyInAnyOrder(id2);

	}

	@Test
	public void testStringComparatorGt() {

		Patient p = new Patient();
		p.addName().setFamily("Smith").addGiven("John");
		p.setActive(true);
		String id1 = myPatientDao.create(p).getId().toUnqualifiedVersionless().getValue();

		p = new Patient();
		p.addName().setFamily("Jones").addGiven("Frank");
		p.setActive(false);
		String id2 = myPatientDao.create(p).getId().toUnqualifiedVersionless().getValue();

		SearchParameterMap map;
		List<String> found;

		map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add(Constants.PARAM_FILTER, new StringParam("family gt jones"));
		found = toUnqualifiedVersionlessIdValues(myPatientDao.search(map));
		assertThat(found).containsExactlyInAnyOrder(id1);

		map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add(Constants.PARAM_FILTER, new StringParam("family gt arthur"));
		found = toUnqualifiedVersionlessIdValues(myPatientDao.search(map));
		assertThat(found).containsExactlyInAnyOrder(id1, id2);

		map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add(Constants.PARAM_FILTER, new StringParam("given gt john"));
		found = toUnqualifiedVersionlessIdValues(myPatientDao.search(map));
		assertThat(found).isEmpty();

	}

	@Test
	public void testStringComparatorLt() {

		Patient p = new Patient();
		p.addName().setFamily("Smith").addGiven("John");
		p.setActive(true);
		String id1 = myPatientDao.create(p).getId().toUnqualifiedVersionless().getValue();

		p = new Patient();
		p.addName().setFamily("Jones").addGiven("Frank");
		p.setActive(false);
		String id2 = myPatientDao.create(p).getId().toUnqualifiedVersionless().getValue();

		SearchParameterMap map;
		List<String> found;

		map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add(Constants.PARAM_FILTER, new StringParam("family lt smith"));
		found = toUnqualifiedVersionlessIdValues(myPatientDao.search(map));
		assertThat(found).containsExactlyInAnyOrder(id2);

		map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add(Constants.PARAM_FILTER, new StringParam("family lt walker"));
		found = toUnqualifiedVersionlessIdValues(myPatientDao.search(map));
		assertThat(found).containsExactlyInAnyOrder(id1, id2);

		map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add(Constants.PARAM_FILTER, new StringParam("given lt frank"));
		found = toUnqualifiedVersionlessIdValues(myPatientDao.search(map));
		assertThat(found).isEmpty();

	}

	@Test
	public void testStringComparatorGe() {

		Patient p = new Patient();
		p.addName().setFamily("Smith").addGiven("John");
		p.setActive(true);
		String id1 = myPatientDao.create(p).getId().toUnqualifiedVersionless().getValue();

		p = new Patient();
		p.addName().setFamily("Jones").addGiven("Frank");
		p.setActive(false);
		String id2 = myPatientDao.create(p).getId().toUnqualifiedVersionless().getValue();

		SearchParameterMap map;
		List<String> found;

		map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add(Constants.PARAM_FILTER, new StringParam("family ge jones"));
		myCaptureQueriesListener.clear();
		found = toUnqualifiedVersionlessIdValues(myPatientDao.search(map));
		myCaptureQueriesListener.logSelectQueriesForCurrentThread(0);
		assertThat(found).containsExactlyInAnyOrder(id1, id2);

		map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add(Constants.PARAM_FILTER, new StringParam("family ge justin"));
		found = toUnqualifiedVersionlessIdValues(myPatientDao.search(map));
		assertThat(found).containsExactlyInAnyOrder(id1);

		map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add(Constants.PARAM_FILTER, new StringParam("family ge arthur"));
		found = toUnqualifiedVersionlessIdValues(myPatientDao.search(map));
		assertThat(found).containsExactlyInAnyOrder(id1, id2);

		map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add(Constants.PARAM_FILTER, new StringParam("given ge jon"));
		found = toUnqualifiedVersionlessIdValues(myPatientDao.search(map));
		assertThat(found).isEmpty();

	}

	@Test
	public void testStringComparatorLe() {

		Patient p = new Patient();
		p.addName().setFamily("Smith").addGiven("John");
		p.setActive(true);
		String id1 = myPatientDao.create(p).getId().toUnqualifiedVersionless().getValue();

		p = new Patient();
		p.addName().setFamily("Jones").addGiven("Frank");
		p.setActive(false);
		String id2 = myPatientDao.create(p).getId().toUnqualifiedVersionless().getValue();

		SearchParameterMap map;
		List<String> found;

		map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add(Constants.PARAM_FILTER, new StringParam("family le smith"));
		found = toUnqualifiedVersionlessIdValues(myPatientDao.search(map));
		assertThat(found).containsExactlyInAnyOrder(id1, id2);

		map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add(Constants.PARAM_FILTER, new StringParam("family le jones"));
		found = toUnqualifiedVersionlessIdValues(myPatientDao.search(map));
		assertThat(found).containsExactlyInAnyOrder(id2);

		map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add(Constants.PARAM_FILTER, new StringParam("family le jackson"));
		found = toUnqualifiedVersionlessIdValues(myPatientDao.search(map));
		assertThat(found).isEmpty();

	}

	@Test
	public void testDateComparatorEq() {

		Patient p = new Patient();
		p.addName().setFamily("Smith").addGiven("John");
		p.setBirthDateElement(new DateType("1955-01-01"));
		p.setActive(true);
		String id1 = myPatientDao.create(p).getId().toUnqualifiedVersionless().getValue();

		SearchParameterMap map;
		List<String> found;

		map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add(Constants.PARAM_FILTER, new StringParam("birthdate eq 1955-01-01"));
		found = toUnqualifiedVersionlessIdValues(myPatientDao.search(map));
		assertThat(found).containsExactlyInAnyOrder(id1);

	}

	@Test
	public void testDateComparatorNe() {

		Patient p = new Patient();
		p.addName().setFamily("Smith").addGiven("John");
		p.setBirthDateElement(new DateType("1955-01-01"));
		p.setActive(true);
		String id1 = myPatientDao.create(p).getId().toUnqualifiedVersionless().getValue();

		SearchParameterMap map;
		List<String> found;

		map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add(Constants.PARAM_FILTER, new StringParam("birthdate ne 1955-01-01"));
		found = toUnqualifiedVersionlessIdValues(myPatientDao.search(map));
		assertThat(found).isEmpty();

		map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add(Constants.PARAM_FILTER, new StringParam("birthdate ne 1995-01-01"));
		found = toUnqualifiedVersionlessIdValues(myPatientDao.search(map));
		assertThat(found).containsExactlyInAnyOrder(id1);

	}

	@Test
	public void testDateComparatorGt() {

		Patient p = new Patient();
		p.addName().setFamily("Smith").addGiven("John");
		p.setBirthDateElement(new DateType("1955-01-01"));
		p.setActive(true);
		String id1 = myPatientDao.create(p).getId().toUnqualifiedVersionless().getValue();

		SearchParameterMap map;
		List<String> found;

		map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add(Constants.PARAM_FILTER, new StringParam("birthdate gt 1954-12-31"));
		found = toUnqualifiedVersionlessIdValues(myPatientDao.search(map));
		assertThat(found).containsExactlyInAnyOrder(id1);

		map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add(Constants.PARAM_FILTER, new StringParam("birthdate gt 1955-01-01"));
		myCaptureQueriesListener.clear();
		found = toUnqualifiedVersionlessIdValues(myPatientDao.search(map));
		myCaptureQueriesListener.logSelectQueriesForCurrentThread(0);
		assertThat(found).isEmpty();

	}

	@Test
	public void testDateComparatorLt() {

		Patient p = new Patient();
		p.addName().setFamily("Smith").addGiven("John");
		p.setBirthDateElement(new DateType("1955-01-01"));
		p.setActive(true);
		String id1 = myPatientDao.create(p).getId().toUnqualifiedVersionless().getValue();

		SearchParameterMap map;
		List<String> found;

		map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add(Constants.PARAM_FILTER, new StringParam("birthdate lt 1955-01-02"));
		myCaptureQueriesListener.clear();
		found = toUnqualifiedVersionlessIdValues(myPatientDao.search(map));
		myCaptureQueriesListener.logSelectQueriesForCurrentThread(0);
		assertThat(found).containsExactlyInAnyOrder(id1);

		map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add(Constants.PARAM_FILTER, new StringParam("birthdate lt 1955-01-01"));
		found = toUnqualifiedVersionlessIdValues(myPatientDao.search(map));
		assertThat(found).isEmpty();

	}

	@Test
	public void testDateComparatorGe() {

		Patient p = new Patient();
		p.addName().setFamily("Smith").addGiven("John");
		p.setBirthDateElement(new DateType("1955-01-01"));
		p.setActive(true);
		String id1 = myPatientDao.create(p).getId().toUnqualifiedVersionless().getValue();

		SearchParameterMap map;
		List<String> found;

		map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add(Constants.PARAM_FILTER, new StringParam("birthdate ge 1955-01-01"));
		found = toUnqualifiedVersionlessIdValues(myPatientDao.search(map));
		assertThat(found).containsExactlyInAnyOrder(id1);

		map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add(Constants.PARAM_FILTER, new StringParam("birthdate ge 1954-12-31"));
		found = toUnqualifiedVersionlessIdValues(myPatientDao.search(map));
		assertThat(found).containsExactlyInAnyOrder(id1);

		map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add(Constants.PARAM_FILTER, new StringParam("birthdate ge 1955-01-02"));
		found = toUnqualifiedVersionlessIdValues(myPatientDao.search(map));
		assertThat(found).isEmpty();

	}

	@Test
	public void testDateComparatorLe() {

		Patient p = new Patient();
		p.addName().setFamily("Smith").addGiven("John");
		p.setBirthDateElement(new DateType("1955-01-01"));
		p.setActive(true);
		String id1 = myPatientDao.create(p).getId().toUnqualifiedVersionless().getValue();

		SearchParameterMap map;
		List<String> found;

		map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add(Constants.PARAM_FILTER, new StringParam("birthdate le 1955-01-01"));
		found = toUnqualifiedVersionlessIdValues(myPatientDao.search(map));
		assertThat(found).containsExactlyInAnyOrder(id1);

		map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add(Constants.PARAM_FILTER, new StringParam("birthdate le 1954-12-31"));
		found = toUnqualifiedVersionlessIdValues(myPatientDao.search(map));
		assertThat(found).isEmpty();

		map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add(Constants.PARAM_FILTER, new StringParam("birthdate le 1955-01-02"));
		found = toUnqualifiedVersionlessIdValues(myPatientDao.search(map));
		assertThat(found).containsExactlyInAnyOrder(id1);

	}

	@Test
	public void testNumericComparatorEq() {

		RiskAssessment ra1 = new RiskAssessment();
		RiskAssessment ra2 = new RiskAssessment();

		RiskAssessment.RiskAssessmentPredictionComponent component = new RiskAssessment.RiskAssessmentPredictionComponent();
		DecimalType doseNumber = new DecimalType(0.25);
		component.setProbability(doseNumber);
		ra1.addPrediction(component);
		String raId1 = myRiskAssessmentDao.create(ra1).getId().toUnqualifiedVersionless().getValue();

		component = new RiskAssessment.RiskAssessmentPredictionComponent();
		doseNumber = new DecimalType(0.3);
		component.setProbability(doseNumber);
		ra2.addPrediction(component);
		String raId2 = myRiskAssessmentDao.create(ra2).getId().toUnqualifiedVersionless().getValue();

		SearchParameterMap map;
		List<String> found;

		map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add(Constants.PARAM_FILTER, new StringParam("probability eq 0.25"));
		found = toUnqualifiedVersionlessIdValues(myRiskAssessmentDao.search(map));
		assertThat(found).containsExactlyInAnyOrder(raId1);

		map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add(Constants.PARAM_FILTER, new StringParam("probability eq 0.3"));
		found = toUnqualifiedVersionlessIdValues(myRiskAssessmentDao.search(map));
		assertThat(found).containsExactlyInAnyOrder(raId2);

		map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add(Constants.PARAM_FILTER, new StringParam("probability eq 0.1"));
		found = toUnqualifiedVersionlessIdValues(myRiskAssessmentDao.search(map));
		assertThat(found).isEmpty();

	}

	@Test
	public void testNumericComparatorNe() {

		RiskAssessment ra1 = new RiskAssessment();
		RiskAssessment ra2 = new RiskAssessment();

		RiskAssessment.RiskAssessmentPredictionComponent component = new RiskAssessment.RiskAssessmentPredictionComponent();
		DecimalType doseNumber = new DecimalType(0.25);
		component.setProbability(doseNumber);
		ra1.addPrediction(component);
		String raId1 = myRiskAssessmentDao.create(ra1).getId().toUnqualifiedVersionless().getValue();

		component = new RiskAssessment.RiskAssessmentPredictionComponent();
		doseNumber = new DecimalType(0.3);
		component.setProbability(doseNumber);
		ra2.addPrediction(component);
		String raId2 = myRiskAssessmentDao.create(ra2).getId().toUnqualifiedVersionless().getValue();

		SearchParameterMap map;
		List<String> found;

		map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add(Constants.PARAM_FILTER, new StringParam("probability ne 0.25"));
		found = toUnqualifiedVersionlessIdValues(myRiskAssessmentDao.search(map));
		assertThat(found).containsExactlyInAnyOrder(raId2);

		map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add(Constants.PARAM_FILTER, new StringParam("probability ne 0.3"));
		found = toUnqualifiedVersionlessIdValues(myRiskAssessmentDao.search(map));
		assertThat(found).containsExactlyInAnyOrder(raId1);

		map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add(Constants.PARAM_FILTER, new StringParam("probability ne 0.1"));
		found = toUnqualifiedVersionlessIdValues(myRiskAssessmentDao.search(map));
		assertThat(found).containsExactlyInAnyOrder(raId1, raId2);

	}

	@Test
	public void testNumericComparatorGt() {

		RiskAssessment ra1 = new RiskAssessment();
		RiskAssessment ra2 = new RiskAssessment();

		RiskAssessment.RiskAssessmentPredictionComponent component = new RiskAssessment.RiskAssessmentPredictionComponent();
		DecimalType doseNumber = new DecimalType(0.25);
		component.setProbability(doseNumber);
		ra1.addPrediction(component);
		String raId1 = myRiskAssessmentDao.create(ra1).getId().toUnqualifiedVersionless().getValue();

		component = new RiskAssessment.RiskAssessmentPredictionComponent();
		doseNumber = new DecimalType(0.3);
		component.setProbability(doseNumber);
		ra2.addPrediction(component);
		String raId2 = myRiskAssessmentDao.create(ra2).getId().toUnqualifiedVersionless().getValue();

		SearchParameterMap map;
		List<String> found;

		map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add(Constants.PARAM_FILTER, new StringParam("probability gt 0.25"));
		found = toUnqualifiedVersionlessIdValues(myRiskAssessmentDao.search(map));
		assertThat(found).containsExactlyInAnyOrder(raId2);

		map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add(Constants.PARAM_FILTER, new StringParam("probability gt 0.3"));
		found = toUnqualifiedVersionlessIdValues(myRiskAssessmentDao.search(map));
		assertThat(found).isEmpty();

	}

	@Test
	public void testNumericComparatorLt() {

		RiskAssessment ra1 = new RiskAssessment();
		RiskAssessment ra2 = new RiskAssessment();

		RiskAssessment.RiskAssessmentPredictionComponent component = new RiskAssessment.RiskAssessmentPredictionComponent();
		DecimalType doseNumber = new DecimalType(0.25);
		component.setProbability(doseNumber);
		ra1.addPrediction(component);
		String raId1 = myRiskAssessmentDao.create(ra1).getId().toUnqualifiedVersionless().getValue();

		component = new RiskAssessment.RiskAssessmentPredictionComponent();
		doseNumber = new DecimalType(0.3);
		component.setProbability(doseNumber);
		ra2.addPrediction(component);
		String raId2 = myRiskAssessmentDao.create(ra2).getId().toUnqualifiedVersionless().getValue();

		SearchParameterMap map;
		List<String> found;

		map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add(Constants.PARAM_FILTER, new StringParam("probability lt 0.3"));
		found = toUnqualifiedVersionlessIdValues(myRiskAssessmentDao.search(map));
		assertThat(found).containsExactlyInAnyOrder(raId1);

		map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add(Constants.PARAM_FILTER, new StringParam("probability lt 0.25"));
		found = toUnqualifiedVersionlessIdValues(myRiskAssessmentDao.search(map));
		assertThat(found).isEmpty();

	}

	@Test
	public void testNumericComparatorGe() {

		RiskAssessment ra1 = new RiskAssessment();
		RiskAssessment ra2 = new RiskAssessment();

		RiskAssessment.RiskAssessmentPredictionComponent component = new RiskAssessment.RiskAssessmentPredictionComponent();
		DecimalType doseNumber = new DecimalType(0.25);
		component.setProbability(doseNumber);
		ra1.addPrediction(component);
		String raId1 = myRiskAssessmentDao.create(ra1).getId().toUnqualifiedVersionless().getValue();

		component = new RiskAssessment.RiskAssessmentPredictionComponent();
		doseNumber = new DecimalType(0.3);
		component.setProbability(doseNumber);
		ra2.addPrediction(component);
		String raId2 = myRiskAssessmentDao.create(ra2).getId().toUnqualifiedVersionless().getValue();

		SearchParameterMap map;
		List<String> found;

		map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add(Constants.PARAM_FILTER, new StringParam("probability ge 0.25"));
		found = toUnqualifiedVersionlessIdValues(myRiskAssessmentDao.search(map));
		assertThat(found).containsExactlyInAnyOrder(raId1, raId2);

		map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add(Constants.PARAM_FILTER, new StringParam("probability ge 0.3"));
		found = toUnqualifiedVersionlessIdValues(myRiskAssessmentDao.search(map));
		assertThat(found).containsExactlyInAnyOrder(raId2);

	}

	@Test
	public void testNumericComparatorLe() {

		RiskAssessment ra1 = new RiskAssessment();
		RiskAssessment ra2 = new RiskAssessment();

		RiskAssessment.RiskAssessmentPredictionComponent component = new RiskAssessment.RiskAssessmentPredictionComponent();
		DecimalType doseNumber = new DecimalType(0.25);
		component.setProbability(doseNumber);
		ra1.addPrediction(component);
		String raId1 = myRiskAssessmentDao.create(ra1).getId().toUnqualifiedVersionless().getValue();

		component = new RiskAssessment.RiskAssessmentPredictionComponent();
		doseNumber = new DecimalType(0.3);
		component.setProbability(doseNumber);
		ra2.addPrediction(component);
		String raId2 = myRiskAssessmentDao.create(ra2).getId().toUnqualifiedVersionless().getValue();

		SearchParameterMap map;
		List<String> found;

		map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add(Constants.PARAM_FILTER, new StringParam("probability le 0.25"));
		found = toUnqualifiedVersionlessIdValues(myRiskAssessmentDao.search(map));
		assertThat(found).containsExactlyInAnyOrder(raId1);

		map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add(Constants.PARAM_FILTER, new StringParam("probability le 0.3"));
		found = toUnqualifiedVersionlessIdValues(myRiskAssessmentDao.search(map));
		assertThat(found).containsExactlyInAnyOrder(raId1, raId2);

	}

	@Test
	public void testSearchUriEq() {

		ValueSet vs1 = new ValueSet();
		vs1.setUrl("http://hl7.org/foo/baz");
		IIdType vsId1 = myValueSetDao.create(vs1, mySrd).getId().toUnqualifiedVersionless();

		ValueSet vs2 = new ValueSet();
		vs2.setUrl("http://hl7.org/foo/bar");
		IIdType vsId2 = myValueSetDao.create(vs2, mySrd).getId().toUnqualifiedVersionless();

		IBundleProvider result;
		result = myValueSetDao.search(SearchParameterMap.newSynchronous().add(Constants.PARAM_FILTER,
			new StringParam("url eq http://hl7.org/foo/baz")));
		assertThat(toUnqualifiedVersionlessIds(result)).containsExactlyInAnyOrder(vsId1);

		result = myValueSetDao.search(SearchParameterMap.newSynchronous().add(Constants.PARAM_FILTER,
			new StringParam("url eq http://hl7.org/foo/bar")));
		assertThat(toUnqualifiedVersionlessIds(result)).containsExactlyInAnyOrder(vsId2);

		result = myValueSetDao.search(SearchParameterMap.newSynchronous().add(Constants.PARAM_FILTER,
			new StringParam("url eq http://hl7.org/foo/bar/bar")));
		assertThat(toUnqualifiedVersionlessIds(result)).isEmpty();

	}

	@Test
	public void testSearchUriNe() {

		ValueSet vs1 = new ValueSet();
		vs1.setUrl("http://hl7.org/foo/baz");
		IIdType vsId1 = myValueSetDao.create(vs1, mySrd).getId().toUnqualifiedVersionless();

		ValueSet vs2 = new ValueSet();
		vs2.setUrl("http://hl7.org/foo/bar");
		IIdType vsId2 = myValueSetDao.create(vs2, mySrd).getId().toUnqualifiedVersionless();

		IBundleProvider result;
		result = myValueSetDao.search(SearchParameterMap.newSynchronous().add(Constants.PARAM_FILTER,
			new StringParam("url ne http://hl7.org/foo/baz")));
		assertThat(toUnqualifiedVersionlessIds(result)).containsExactlyInAnyOrder(vsId2);

		result = myValueSetDao.search(SearchParameterMap.newSynchronous().add(Constants.PARAM_FILTER,
			new StringParam("url ne http://hl7.org/foo/bar")));
		assertThat(toUnqualifiedVersionlessIds(result)).containsExactlyInAnyOrder(vsId1);

		result = myValueSetDao.search(SearchParameterMap.newSynchronous().add(Constants.PARAM_FILTER,
			new StringParam("url ne http://hl7.org/foo/baz and url ne http://hl7.org/foo/bar")));
		assertThat(toUnqualifiedVersionlessIds(result)).isEmpty();

	}

	@Test
	public void testSearchUriCo() {

		ValueSet vs1 = new ValueSet();
		vs1.setUrl("http://hl7.org/foo/baz");
		IIdType vsId1 = myValueSetDao.create(vs1, mySrd).getId().toUnqualifiedVersionless();

		ValueSet vs2 = new ValueSet();
		vs2.setUrl("http://hl7.org/foo/bar");
		IIdType vsId2 = myValueSetDao.create(vs2, mySrd).getId().toUnqualifiedVersionless();

		IBundleProvider result;
		result = myValueSetDao.search(SearchParameterMap.newSynchronous().add(Constants.PARAM_FILTER,
			new StringParam("url co http://hl7.org/foo")));
		assertThat(toUnqualifiedVersionlessIds(result)).containsExactlyInAnyOrder(vsId1, vsId2);

		result = myValueSetDao.search(SearchParameterMap.newSynchronous().add(Constants.PARAM_FILTER,
			new StringParam("url co baz")));
		assertThat(toUnqualifiedVersionlessIds(result)).containsExactlyInAnyOrder(vsId1);

		result = myValueSetDao.search(SearchParameterMap.newSynchronous().add(Constants.PARAM_FILTER,
			new StringParam("url co http://hl7.org/foo/bat")));
		assertThat(toUnqualifiedVersionlessIds(result)).isEmpty();

	}

	@Test
	public void testSearchUriGt() {

		ValueSet vs1 = new ValueSet();
		vs1.setUrl("http://hl7.org/foo/baz");
		IIdType vsId1 = myValueSetDao.create(vs1, mySrd).getId().toUnqualifiedVersionless();

		ValueSet vs2 = new ValueSet();
		vs2.setUrl("http://hl7.org/foo/bar");
		IIdType vsId2 = myValueSetDao.create(vs2, mySrd).getId().toUnqualifiedVersionless();

		IBundleProvider result;
		result = myValueSetDao.search(SearchParameterMap.newSynchronous().add(Constants.PARAM_FILTER,
			new StringParam("url gt http://hl7.org/foo")));
		assertThat(toUnqualifiedVersionlessIds(result)).containsExactlyInAnyOrder(vsId1, vsId2);

		result = myValueSetDao.search(SearchParameterMap.newSynchronous().add(Constants.PARAM_FILTER,
			new StringParam("url gt http://hl7.org/foo/bar")));
		assertThat(toUnqualifiedVersionlessIds(result)).containsExactlyInAnyOrder(vsId1);

		result = myValueSetDao.search(SearchParameterMap.newSynchronous().add(Constants.PARAM_FILTER,
			new StringParam("url gt http://hl7.org/foo/baza")));
		assertThat(toUnqualifiedVersionlessIds(result)).isEmpty();

	}

	@Test
	public void testSearchUriLt() {

		ValueSet vs1 = new ValueSet();
		vs1.setUrl("http://hl7.org/foo/baz");
		IIdType vsId1 = myValueSetDao.create(vs1, mySrd).getId().toUnqualifiedVersionless();

		ValueSet vs2 = new ValueSet();
		vs2.setUrl("http://hl7.org/foo/bar");
		IIdType vsId2 = myValueSetDao.create(vs2, mySrd).getId().toUnqualifiedVersionless();

		IBundleProvider result;
		result = myValueSetDao.search(SearchParameterMap.newSynchronous().add(Constants.PARAM_FILTER,
			new StringParam("url lt http://hl7.org/foo")));
		assertThat(toUnqualifiedVersionlessIds(result)).isEmpty();

		result = myValueSetDao.search(SearchParameterMap.newSynchronous().add(Constants.PARAM_FILTER,
			new StringParam("url lt http://hl7.org/foo/baz")));
		assertThat(toUnqualifiedVersionlessIds(result)).containsExactlyInAnyOrder(vsId2);

		result = myValueSetDao.search(SearchParameterMap.newSynchronous().add(Constants.PARAM_FILTER,
			new StringParam("url lt http://hl7.org/foo/bara")));
		assertThat(toUnqualifiedVersionlessIds(result)).containsExactlyInAnyOrder(vsId2);

	}

	@Test
	public void testSearchUriGe() {

		ValueSet vs1 = new ValueSet();
		vs1.setUrl("http://hl7.org/foo/baz");
		IIdType vsId1 = myValueSetDao.create(vs1, mySrd).getId().toUnqualifiedVersionless();

		ValueSet vs2 = new ValueSet();
		vs2.setUrl("http://hl7.org/foo/bar");
		IIdType vsId2 = myValueSetDao.create(vs2, mySrd).getId().toUnqualifiedVersionless();

		IBundleProvider result;
		result = myValueSetDao.search(SearchParameterMap.newSynchronous().add(Constants.PARAM_FILTER,
			new StringParam("url ge http://hl7.org/foo/bar")));
		assertThat(toUnqualifiedVersionlessIds(result)).containsExactlyInAnyOrder(vsId1, vsId2);

		result = myValueSetDao.search(SearchParameterMap.newSynchronous().add(Constants.PARAM_FILTER,
			new StringParam("url ge http://hl7.org/foo/baza")));
		assertThat(toUnqualifiedVersionlessIds(result)).isEmpty();

	}

	@Test
	public void testSearchUriLe() {

		ValueSet vs1 = new ValueSet();
		vs1.setUrl("http://hl7.org/foo/baz");
		IIdType vsId1 = myValueSetDao.create(vs1, mySrd).getId().toUnqualifiedVersionless();

		ValueSet vs2 = new ValueSet();
		vs2.setUrl("http://hl7.org/foo/bar");
		IIdType vsId2 = myValueSetDao.create(vs2, mySrd).getId().toUnqualifiedVersionless();

		IBundleProvider result;
		result = myValueSetDao.search(SearchParameterMap.newSynchronous().add(Constants.PARAM_FILTER,
			new StringParam("url le http://hl7.org/foo/baz")));
		assertThat(toUnqualifiedVersionlessIds(result)).containsExactlyInAnyOrder(vsId1, vsId2);

		result = myValueSetDao.search(SearchParameterMap.newSynchronous().add(Constants.PARAM_FILTER,
			new StringParam("url le http://hl7.org/foo/bar")));
		assertThat(toUnqualifiedVersionlessIds(result)).containsExactlyInAnyOrder(vsId2);

		result = myValueSetDao.search(SearchParameterMap.newSynchronous().add(Constants.PARAM_FILTER,
			new StringParam("url lt http://hl7.org/foo/baza")));
		assertThat(toUnqualifiedVersionlessIds(result)).containsExactlyInAnyOrder(vsId1, vsId2);

	}

	@Test
	public void testSearchUriSw() {

		ValueSet vs1 = new ValueSet();
		vs1.setUrl("http://hl7.org/foo/baz");
		IIdType vsId1 = myValueSetDao.create(vs1, mySrd).getId().toUnqualifiedVersionless();

		ValueSet vs2 = new ValueSet();
		vs2.setUrl("http://hl7.org/foo/bar");
		IIdType vsId2 = myValueSetDao.create(vs2, mySrd).getId().toUnqualifiedVersionless();

		IBundleProvider result;
		result = myValueSetDao.search(SearchParameterMap.newSynchronous().add(Constants.PARAM_FILTER,
			new StringParam("url sw http://hl7.org")));
		assertThat(toUnqualifiedVersionlessIds(result)).containsExactlyInAnyOrder(vsId1, vsId2);

		result = myValueSetDao.search(SearchParameterMap.newSynchronous().add(Constants.PARAM_FILTER,
			new StringParam("url sw hl7.org/foo/bar")));
		assertThat(toUnqualifiedVersionlessIds(result)).isEmpty();

	}

	@Test
	public void testSearchUriEw() {

		ValueSet vs1 = new ValueSet();
		vs1.setUrl("http://hl7.org/foo/baz");
		IIdType vsId1 = myValueSetDao.create(vs1, mySrd).getId().toUnqualifiedVersionless();

		ValueSet vs2 = new ValueSet();
		vs2.setUrl("http://hl7.org/foo/bar");
		IIdType vsId2 = myValueSetDao.create(vs2, mySrd).getId().toUnqualifiedVersionless();

		IBundleProvider result;
		result = myValueSetDao.search(SearchParameterMap.newSynchronous().add(Constants.PARAM_FILTER,
			new StringParam("url ew baz")));
		assertThat(toUnqualifiedVersionlessIds(result)).containsExactlyInAnyOrder(vsId1);

		result = myValueSetDao.search(SearchParameterMap.newSynchronous().add(Constants.PARAM_FILTER,
			new StringParam("url ew ba")));
		assertThat(toUnqualifiedVersionlessIds(result)).isEmpty();

	}

	@Test
	public void testUnknownSearchParam() {
		SearchParameterMap map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add(Constants.PARAM_FILTER, new StringParam("foo eq smith"));
		try {
			myPatientDao.search(map);
		} catch (InvalidRequestException e) {
			assertEquals(Msg.code(1206) + "Unknown search parameter \"foo\" for resource type \"Patient\". Valid search parameters for this search are: [_content, _id, _lastUpdated, _profile, _security, _source, _tag, _text, active, address, address-city, address-country, address-postalcode, address-state, address-use, birthdate, death-date, deceased, email, family, gender, general-practitioner, given, identifier, language, link, name, organization, phone, phonetic, telecom]", e.getMessage());
		}
	}

	@Test
	public void testSearchWithChainingForNonExistingReference() {
		SearchParameter searchParameter = new SearchParameter();
		searchParameter.setId("isplaceholder");
		searchParameter.setUrl("http://hl7.org/fhir/SearchParameter/isplaceholder");
		searchParameter.setName("isplaceholder");
		searchParameter.setStatus(Enumerations.PublicationStatus.ACTIVE);
		searchParameter.setVersion("1.0.1");
		searchParameter.setDescription("Custom SearchParameter for HCSC");
		searchParameter.setCode("isplaceholder");
		searchParameter.addBase("PractitionerRole");
		searchParameter.addBase("Organization");
		searchParameter.addBase("OrganizationAffiliation");
		searchParameter.addBase("Location");
		searchParameter.addBase("Practitioner");
		searchParameter.addBase("HealthcareService");
		searchParameter.setType(Enumerations.SearchParamType.TOKEN);
		searchParameter.setExpression("extension.where(url='http://hapifhir.io/fhir/StructureDefinition/resource-placeholder').exists()");
		searchParameter.setXpathUsage(SearchParameter.XPathUsageType.NORMAL);
		mySearchParameterDao.update(searchParameter);
		mySearchParamRegistry.forceRefresh();

		Location l = new Location();
		l.setId("loc-real");
		l.setMeta(new Meta().addTag("http://www.smilecdr.com/custom_tag/uuid",
			"51f922e6-1ae6-48c4-a41e-0f978326000dSP1975060918", ""));
		myLocationDao.update(l);

		HealthcareService hs = new HealthcareService();
		hs.setId("hs-real");
		hs.setMeta(new Meta().addTag("http://www.smilecdr.com/custom_tag/uuid",
			"51f922e6-1ae6-48c4-a41e-0f978326000dSP1975060918", ""));
		hs.setActive(true);
		myHealthcareServiceDao.update(hs);

		Practitioner p = new Practitioner();
		p.setId("prac-real");
		Identifier identifier = new Identifier();
		identifier.setSystem("http://hl7.org/fhir/sid/us-npi");
		identifier.setValue("1234567890");
		p.setActive(true);
		p.setGender(Enumerations.AdministrativeGender.MALE);
		p.addIdentifier(identifier);
		myPractitionerDao.update(p);
		{
			for (int i = 0; i < 5; i++) {
				PractitionerRole pr = new PractitionerRole();
				pr.setId("ref" + i + 1 + "-prac-loc-hcs");
				pr.setMeta(new Meta().addTag("http://www.smilecdr.com/custom_tag/uuid",
					"51f922e6-1ae6-48c4-a41e-0f978326000dSP1975060918", ""));
				pr.setPractitioner(new Reference("Practitioner/prac-real"));
				pr.addLocation(new Reference("Location/loc-real"));
				pr.addHealthcareService(new Reference("HealthcareService/hs-real"));
				myPractitionerRoleDao.create(pr, mySrd).getId().toUnqualifiedVersionless().getValue();
			}
		}
		SearchParameterMap spMap;
		List<IBaseResource> actual;

		spMap = SearchParameterMap.newSynchronous();
		spMap.add(Constants.PARAM_TAG, new TokenParam("51f922e6-1ae6-48c4-a41e-0f978326000dSP1975060918"));
		spMap.add(Constants.PARAM_FILTER, new StringParam("service.isplaceholder eq false or organization.isplaceholder eq false or practitioner.isplaceholder eq false or location.isplaceholder eq false"));
		spMap.setOffset(0);
		IBundleProvider search = myPractitionerRoleDao.search(spMap);
		actual = search.getResources(0, 100);

		assertThat(actual).hasSize(5);
	}
}
