package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.dao.data.IResourceProvenanceDao;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import org.hamcrest.Matchers;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.CarePlan;
import org.hl7.fhir.r4.model.DateType;
import org.hl7.fhir.r4.model.DecimalType;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.RiskAssessment;
import org.hl7.fhir.r4.model.ValueSet;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.empty;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@SuppressWarnings({"Duplicates"})
public class FhirResourceDaoR4FilterLegacySearchBuilderTest extends BaseJpaR4Test {

	private static final Logger ourLog = LoggerFactory.getLogger(FhirResourceDaoR4FilterLegacySearchBuilderTest.class);
	@Autowired
	private IResourceProvenanceDao myResourceProvenanceDao;

	@AfterEach
	public void after() {
		myDaoConfig.setFilterParameterEnabled(new DaoConfig().isFilterParameterEnabled());
		myDaoConfig.setUseLegacySearchBuilder(false);
	}

	@BeforeEach
	public void before() {
		myDaoConfig.setFilterParameterEnabled(true);
		myDaoConfig.setUseLegacySearchBuilder(true);
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
			assertEquals(Msg.code(1015) + "Error parsing _filter syntax: " + Msg.code(1056) + "Expression did not terminate at 13", e.getMessage());
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
		assertThat(found, containsInAnyOrder(id1));

		map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add(Constants.PARAM_FILTER, new StringParam("(name eq smith) or (name eq jones)"));
		found = toUnqualifiedVersionlessIdValues(myPatientDao.search(map));
		assertThat(found, containsInAnyOrder(id1, id2));

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
		assertThat(found, empty());

		map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add(Constants.PARAM_FILTER, new StringParam("name eq smith"));
		found = toUnqualifiedVersionlessIdValues(myPatientDao.search(map));
		assertThat(found, containsInAnyOrder(id1));

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
		assertThat(found, containsInAnyOrder(cpId));

		map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add(Constants.PARAM_FILTER, new StringParam("subject eq " + ptId.getIdPart()));
		found = toUnqualifiedVersionlessIdValues(myCarePlanDao.search(map));
		assertThat(found, containsInAnyOrder(cpId));

		map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add(Constants.PARAM_FILTER, new StringParam("(subject eq " + ptId.getIdPart() + ") or (performer eq " + ptId2.getValue() + ")"));
		found = toUnqualifiedVersionlessIdValues(myCarePlanDao.search(map));
		assertThat(found, containsInAnyOrder(cpId, cpId2));

	}

	@Test
	public void testSourceComparatorEq() {
		myDaoConfig.setStoreMetaSourceInformation(DaoConfig.StoreMetaSourceInformationEnum.SOURCE_URI_AND_REQUEST_ID);

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
		assertThat(found, containsInAnyOrder(ptId));

	}

	@Test
	public void testFilterDisabled() {
		myDaoConfig.setFilterParameterEnabled(false);

		SearchParameterMap map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add(Constants.PARAM_FILTER, new StringParam("name eq smith"));
		try {
			myPatientDao.search(map);
		} catch (InvalidRequestException e) {
			assertEquals(Msg.code(1016) + "_filter parameter is disabled on this server", e.getMessage());
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
		assertThat(found, empty());

		map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add(Constants.PARAM_FILTER, new StringParam(String.format("status eq inactive or _id eq %s", idVal)));
		found = toUnqualifiedVersionlessIdValues(myEncounterDao.search(map));
		assertThat(found, empty());

		map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add(Constants.PARAM_FILTER, new StringParam(String.format("status eq inactive or _id eq Patient/FOO")));
		found = toUnqualifiedVersionlessIdValues(myEncounterDao.search(map));
		assertThat(found, empty());

		map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add(Constants.PARAM_FILTER, new StringParam(String.format("_id eq %s", idVal)));
		found = toUnqualifiedVersionlessIdValues(myEncounterDao.search(map));
		assertThat(found, empty());

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
		assertThat(found, containsInAnyOrder(id2));
		assertThat(found, containsInAnyOrder(Matchers.not(id1)));

		map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add(Constants.PARAM_FILTER, new StringParam("family ne jones"));
		found = toUnqualifiedVersionlessIdValues(myPatientDao.search(map));
		assertThat(found, containsInAnyOrder(id1));
		assertThat(found, containsInAnyOrder(Matchers.not(id2)));

		map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add(Constants.PARAM_FILTER, new StringParam("given ne john"));
		found = toUnqualifiedVersionlessIdValues(myPatientDao.search(map));
		assertThat(found, containsInAnyOrder(id2));
		assertThat(found, containsInAnyOrder(Matchers.not(id1)));

		map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add(Constants.PARAM_FILTER, new StringParam("given ne frank"));
		found = toUnqualifiedVersionlessIdValues(myPatientDao.search(map));
		assertThat(found, containsInAnyOrder(id1));
		assertThat(found, containsInAnyOrder(Matchers.not(id2)));

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
		assertThat(found, containsInAnyOrder(cpId2));

		map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add(Constants.PARAM_FILTER, new StringParam("(subject ne " + ptId.getIdPart() + ") and (performer ne " + ptId2.getValue() + ")"));
		found = toUnqualifiedVersionlessIdValues(myCarePlanDao.search(map));
		assertThat(found, empty());

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
		assertThat(found, containsInAnyOrder(id1));

		map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add(Constants.PARAM_FILTER, new StringParam("name co smith"));
		found = toUnqualifiedVersionlessIdValues(myPatientDao.search(map));
		assertThat(found, containsInAnyOrder(id1));

		map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add(Constants.PARAM_FILTER, new StringParam("given co frank"));
		found = toUnqualifiedVersionlessIdValues(myPatientDao.search(map));
		assertThat(found, containsInAnyOrder(id2));

		map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add(Constants.PARAM_FILTER, new StringParam("family co jones"));
		found = toUnqualifiedVersionlessIdValues(myPatientDao.search(map));
		assertThat(found, containsInAnyOrder(id2));

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
		assertThat(found, containsInAnyOrder(id1));

		map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add(Constants.PARAM_FILTER, new StringParam("name sw mi"));
		found = toUnqualifiedVersionlessIdValues(myPatientDao.search(map));
		assertThat(found, empty());

		map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add(Constants.PARAM_FILTER, new StringParam("given sw fr"));
		found = toUnqualifiedVersionlessIdValues(myPatientDao.search(map));
		assertThat(found, containsInAnyOrder(id2));

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
		assertThat(found, containsInAnyOrder(id1));

		map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add(Constants.PARAM_FILTER, new StringParam("name ew it"));
		found = toUnqualifiedVersionlessIdValues(myPatientDao.search(map));
		assertThat(found, empty());

		map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add(Constants.PARAM_FILTER, new StringParam("given ew nk"));
		found = toUnqualifiedVersionlessIdValues(myPatientDao.search(map));
		assertThat(found, containsInAnyOrder(id2));

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
		assertThat(found, containsInAnyOrder(id1));

		map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add(Constants.PARAM_FILTER, new StringParam("family gt arthur"));
		found = toUnqualifiedVersionlessIdValues(myPatientDao.search(map));
		assertThat(found, containsInAnyOrder(id1, id2));

		map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add(Constants.PARAM_FILTER, new StringParam("given gt john"));
		found = toUnqualifiedVersionlessIdValues(myPatientDao.search(map));
		assertThat(found, empty());

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
		assertThat(found, containsInAnyOrder(id2));

		map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add(Constants.PARAM_FILTER, new StringParam("family lt walker"));
		found = toUnqualifiedVersionlessIdValues(myPatientDao.search(map));
		assertThat(found, containsInAnyOrder(id1, id2));

		map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add(Constants.PARAM_FILTER, new StringParam("given lt frank"));
		found = toUnqualifiedVersionlessIdValues(myPatientDao.search(map));
		assertThat(found, empty());

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
		assertThat(found, containsInAnyOrder(id1, id2));

		map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add(Constants.PARAM_FILTER, new StringParam("family ge justin"));
		found = toUnqualifiedVersionlessIdValues(myPatientDao.search(map));
		assertThat(found, containsInAnyOrder(id1));

		map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add(Constants.PARAM_FILTER, new StringParam("family ge arthur"));
		found = toUnqualifiedVersionlessIdValues(myPatientDao.search(map));
		assertThat(found, containsInAnyOrder(id1, id2));

		map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add(Constants.PARAM_FILTER, new StringParam("given ge jon"));
		found = toUnqualifiedVersionlessIdValues(myPatientDao.search(map));
		assertThat(found, empty());

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
		assertThat(found, containsInAnyOrder(id1, id2));

		map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add(Constants.PARAM_FILTER, new StringParam("family le jones"));
		found = toUnqualifiedVersionlessIdValues(myPatientDao.search(map));
		assertThat(found, containsInAnyOrder(id2));

		map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add(Constants.PARAM_FILTER, new StringParam("family le jackson"));
		found = toUnqualifiedVersionlessIdValues(myPatientDao.search(map));
		assertThat(found, empty());

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
		assertThat(found, containsInAnyOrder(id1));

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
		assertThat(found, empty());

		map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add(Constants.PARAM_FILTER, new StringParam("birthdate ne 1995-01-01"));
		found = toUnqualifiedVersionlessIdValues(myPatientDao.search(map));
		assertThat(found, containsInAnyOrder(id1));

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
		assertThat(found, containsInAnyOrder(id1));

		map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add(Constants.PARAM_FILTER, new StringParam("birthdate gt 1955-01-01"));
		myCaptureQueriesListener.clear();
		found = toUnqualifiedVersionlessIdValues(myPatientDao.search(map));
		myCaptureQueriesListener.logSelectQueriesForCurrentThread(0);
		assertThat(found, empty());

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
		assertThat(found, containsInAnyOrder(id1));

		map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add(Constants.PARAM_FILTER, new StringParam("birthdate lt 1955-01-01"));
		found = toUnqualifiedVersionlessIdValues(myPatientDao.search(map));
		assertThat(found, empty());

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
		assertThat(found, containsInAnyOrder(id1));

		map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add(Constants.PARAM_FILTER, new StringParam("birthdate ge 1954-12-31"));
		found = toUnqualifiedVersionlessIdValues(myPatientDao.search(map));
		assertThat(found, containsInAnyOrder(id1));

		map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add(Constants.PARAM_FILTER, new StringParam("birthdate ge 1955-01-02"));
		found = toUnqualifiedVersionlessIdValues(myPatientDao.search(map));
		assertThat(found, empty());

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
		assertThat(found, containsInAnyOrder(id1));

		map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add(Constants.PARAM_FILTER, new StringParam("birthdate le 1954-12-31"));
		found = toUnqualifiedVersionlessIdValues(myPatientDao.search(map));
		assertThat(found, empty());

		map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add(Constants.PARAM_FILTER, new StringParam("birthdate le 1955-01-02"));
		found = toUnqualifiedVersionlessIdValues(myPatientDao.search(map));
		assertThat(found, containsInAnyOrder(id1));

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
		assertThat(found, containsInAnyOrder(raId1));

		map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add(Constants.PARAM_FILTER, new StringParam("probability eq 0.3"));
		found = toUnqualifiedVersionlessIdValues(myRiskAssessmentDao.search(map));
		assertThat(found, containsInAnyOrder(raId2));

		map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add(Constants.PARAM_FILTER, new StringParam("probability eq 0.1"));
		found = toUnqualifiedVersionlessIdValues(myRiskAssessmentDao.search(map));
		assertThat(found, empty());

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
		assertThat(found, containsInAnyOrder(raId2));

		map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add(Constants.PARAM_FILTER, new StringParam("probability ne 0.3"));
		found = toUnqualifiedVersionlessIdValues(myRiskAssessmentDao.search(map));
		assertThat(found, containsInAnyOrder(raId1));

		map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add(Constants.PARAM_FILTER, new StringParam("probability ne 0.1"));
		found = toUnqualifiedVersionlessIdValues(myRiskAssessmentDao.search(map));
		assertThat(found, containsInAnyOrder(raId1, raId2));

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
		assertThat(found, containsInAnyOrder(raId2));

		map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add(Constants.PARAM_FILTER, new StringParam("probability gt 0.3"));
		found = toUnqualifiedVersionlessIdValues(myRiskAssessmentDao.search(map));
		assertThat(found, empty());

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
		assertThat(found, containsInAnyOrder(raId1));

		map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add(Constants.PARAM_FILTER, new StringParam("probability lt 0.25"));
		found = toUnqualifiedVersionlessIdValues(myRiskAssessmentDao.search(map));
		assertThat(found, empty());

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
		assertThat(found, containsInAnyOrder(raId1, raId2));

		map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add(Constants.PARAM_FILTER, new StringParam("probability ge 0.3"));
		found = toUnqualifiedVersionlessIdValues(myRiskAssessmentDao.search(map));
		assertThat(found, containsInAnyOrder(raId2));

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
		assertThat(found, containsInAnyOrder(raId1));

		map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add(Constants.PARAM_FILTER, new StringParam("probability le 0.3"));
		found = toUnqualifiedVersionlessIdValues(myRiskAssessmentDao.search(map));
		assertThat(found, containsInAnyOrder(raId1, raId2));

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
		assertThat(toUnqualifiedVersionlessIds(result), containsInAnyOrder(vsId1));

		result = myValueSetDao.search(SearchParameterMap.newSynchronous().add(Constants.PARAM_FILTER,
			new StringParam("url eq http://hl7.org/foo/bar")));
		assertThat(toUnqualifiedVersionlessIds(result), containsInAnyOrder(vsId2));

		result = myValueSetDao.search(SearchParameterMap.newSynchronous().add(Constants.PARAM_FILTER,
			new StringParam("url eq http://hl7.org/foo/bar/bar")));
		assertThat(toUnqualifiedVersionlessIds(result), empty());

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
		assertThat(toUnqualifiedVersionlessIds(result), containsInAnyOrder(vsId2));

		result = myValueSetDao.search(SearchParameterMap.newSynchronous().add(Constants.PARAM_FILTER,
			new StringParam("url ne http://hl7.org/foo/bar")));
		assertThat(toUnqualifiedVersionlessIds(result), containsInAnyOrder(vsId1));

		result = myValueSetDao.search(SearchParameterMap.newSynchronous().add(Constants.PARAM_FILTER,
			new StringParam("url ne http://hl7.org/foo/baz and url ne http://hl7.org/foo/bar")));
		assertThat(toUnqualifiedVersionlessIds(result), empty());

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
		assertThat(toUnqualifiedVersionlessIds(result), containsInAnyOrder(vsId1, vsId2));

		result = myValueSetDao.search(SearchParameterMap.newSynchronous().add(Constants.PARAM_FILTER,
			new StringParam("url co baz")));
		assertThat(toUnqualifiedVersionlessIds(result), containsInAnyOrder(vsId1));

		result = myValueSetDao.search(SearchParameterMap.newSynchronous().add(Constants.PARAM_FILTER,
			new StringParam("url co http://hl7.org/foo/bat")));
		assertThat(toUnqualifiedVersionlessIds(result), empty());

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
		assertThat(toUnqualifiedVersionlessIds(result), containsInAnyOrder(vsId1, vsId2));

		result = myValueSetDao.search(SearchParameterMap.newSynchronous().add(Constants.PARAM_FILTER,
			new StringParam("url gt http://hl7.org/foo/bar")));
		assertThat(toUnqualifiedVersionlessIds(result), containsInAnyOrder(vsId1));

		result = myValueSetDao.search(SearchParameterMap.newSynchronous().add(Constants.PARAM_FILTER,
			new StringParam("url gt http://hl7.org/foo/baza")));
		assertThat(toUnqualifiedVersionlessIds(result), empty());

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
		assertThat(toUnqualifiedVersionlessIds(result), empty());

		result = myValueSetDao.search(SearchParameterMap.newSynchronous().add(Constants.PARAM_FILTER,
			new StringParam("url lt http://hl7.org/foo/baz")));
		assertThat(toUnqualifiedVersionlessIds(result), containsInAnyOrder(vsId2));

		result = myValueSetDao.search(SearchParameterMap.newSynchronous().add(Constants.PARAM_FILTER,
			new StringParam("url lt http://hl7.org/foo/bara")));
		assertThat(toUnqualifiedVersionlessIds(result), containsInAnyOrder(vsId2));

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
		assertThat(toUnqualifiedVersionlessIds(result), containsInAnyOrder(vsId1, vsId2));

		result = myValueSetDao.search(SearchParameterMap.newSynchronous().add(Constants.PARAM_FILTER,
			new StringParam("url ge http://hl7.org/foo/baza")));
		assertThat(toUnqualifiedVersionlessIds(result), empty());

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
		assertThat(toUnqualifiedVersionlessIds(result), containsInAnyOrder(vsId1, vsId2));

		result = myValueSetDao.search(SearchParameterMap.newSynchronous().add(Constants.PARAM_FILTER,
			new StringParam("url le http://hl7.org/foo/bar")));
		assertThat(toUnqualifiedVersionlessIds(result), containsInAnyOrder(vsId2));

		result = myValueSetDao.search(SearchParameterMap.newSynchronous().add(Constants.PARAM_FILTER,
			new StringParam("url lt http://hl7.org/foo/baza")));
		assertThat(toUnqualifiedVersionlessIds(result), containsInAnyOrder(vsId1, vsId2));

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
		assertThat(toUnqualifiedVersionlessIds(result), containsInAnyOrder(vsId1, vsId2));

		result = myValueSetDao.search(SearchParameterMap.newSynchronous().add(Constants.PARAM_FILTER,
			new StringParam("url sw hl7.org/foo/bar")));
		assertThat(toUnqualifiedVersionlessIds(result), empty());

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
		assertThat(toUnqualifiedVersionlessIds(result), containsInAnyOrder(vsId1));

		result = myValueSetDao.search(SearchParameterMap.newSynchronous().add(Constants.PARAM_FILTER,
			new StringParam("url ew ba")));
		assertThat(toUnqualifiedVersionlessIds(result), empty());

	}

	@Test
	public void testUnknownSearchParam() {
		SearchParameterMap map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add(Constants.PARAM_FILTER, new StringParam("foo eq smith"));
		try {
			myPatientDao.search(map);
		} catch (InvalidRequestException e) {
			assertEquals(Msg.code(1018) + "Invalid search parameter specified, foo, for resource type Patient", e.getMessage());
		}
	}


}
