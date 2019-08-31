package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.jpa.dao.DaoConfig;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.util.TestUtil;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import org.hamcrest.Matchers;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.CarePlan;
import org.hl7.fhir.r4.model.Patient;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.*;

@SuppressWarnings({"Duplicates"})
public class FhirResourceDaoR4FilterTest extends BaseJpaR4Test {

	@After
	public void after() {
		myDaoConfig.setFilterParameterEnabled(new DaoConfig().isFilterParameterEnabled());
	}

	@Before
	public void before() {
		myDaoConfig.setFilterParameterEnabled(true);
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
			assertEquals("Error parsing _filter syntax: Expression did not terminate at 13", e.getMessage());
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
		assertThat(found, Matchers.empty());

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
	public void testFilterDisabled() {
		myDaoConfig.setFilterParameterEnabled(false);

		SearchParameterMap map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add(Constants.PARAM_FILTER, new StringParam("name eq smith"));
		try {
			myPatientDao.search(map);
		} catch (InvalidRequestException e) {
			assertEquals("_filter parameter is disabled on this server", e.getMessage());
		}
	}

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

}
