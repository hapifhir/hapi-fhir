package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.param.TokenParam;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FhirResourceDaoR4SearchSqlTest extends BaseJpaR4Test {

	@Test
	public void testSingleRegularSearchParam() {

		// One regular search param
		myCaptureQueriesListener.clear();
		SearchParameterMap map = SearchParameterMap.newSynchronous(Patient.SP_NAME, new StringParam("FOO"));
		myPatientDao.search(map);
		assertEquals(1, myCaptureQueriesListener.countSelectQueries());
		String sql = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(0).getSql(false, false);
		assertEquals("SELECT t0.RES_ID FROM HFJ_SPIDX_STRING t0 WHERE ((t0.HASH_NORM_PREFIX = ?) AND (t0.SP_VALUE_NORMALIZED LIKE ?))", sql);

	}

	@Test
	public void testTwoRegularSearchParams() {

		// Two regular search param
		myCaptureQueriesListener.clear();
		SearchParameterMap map = SearchParameterMap.newSynchronous()
			.add(Patient.SP_NAME, new StringParam("FOO"))
			.add(Patient.SP_GENDER, new TokenParam("a", "b"));
		myPatientDao.search(map);
		assertEquals(1, myCaptureQueriesListener.countSelectQueries());
		String sql = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(0).getSql(false, false);
		assertEquals("SELECT t0.RES_ID FROM HFJ_SPIDX_STRING t0 LEFT OUTER JOIN HFJ_SPIDX_TOKEN t1 ON (t0.RES_ID = t1.RES_ID) WHERE (((t0.HASH_NORM_PREFIX = ?) AND (t0.SP_VALUE_NORMALIZED LIKE ?)) AND (t1.HASH_SYS_AND_VALUE = ?))", sql);


	}


}
