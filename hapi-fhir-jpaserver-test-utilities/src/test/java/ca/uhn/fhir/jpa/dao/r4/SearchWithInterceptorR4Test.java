package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.interceptor.api.IAnonymousInterceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.jpa.util.SqlQueryList;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.param.TokenParam;
import org.hl7.fhir.r4.model.Condition;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SuppressWarnings({"Duplicates"})
public class SearchWithInterceptorR4Test extends BaseJpaR4Test {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(SearchWithInterceptorR4Test.class);


	@Test
	public void testRawSql_Search() {
		myDaoConfig.setAdvancedLuceneIndexing(false);

		IAnonymousInterceptor interceptor = (pointcut, params) -> {
			RequestDetails requestDetails = params.get(RequestDetails.class);
			SqlQueryList sqlQueries = params.get(SqlQueryList.class);
			assertNotNull(requestDetails);
			assertNotNull(sqlQueries);
			SqlQueryList existing = (SqlQueryList) requestDetails.getUserData().get("QUERIES");
			if (existing != null) {
				existing.addAll(sqlQueries);
			} else {
				requestDetails.getUserData().put("QUERIES", sqlQueries);
			}
		};
		try {
			myInterceptorRegistry.registerAnonymousInterceptor(Pointcut.JPA_PERFTRACE_RAW_SQL, interceptor);

			Patient patient = new Patient();
			String patientId = myPatientDao.create(patient).getId().toUnqualifiedVersionless().getValue();

			Condition conditionS = new Condition();
			conditionS.getCode().addCoding().setSystem("http://snomed.info/sct").setCode("123");
			conditionS.getSubject().setReference(patientId);
			myConditionDao.create(conditionS);

			Condition conditionA = new Condition();
			conditionA.getCode().addCoding().setSystem("http://snomed.info/sct").setCode("123");
			conditionA.getAsserter().setReference(patientId);
			myConditionDao.create(conditionA);

			SearchParameterMap map = new SearchParameterMap();
			map.add(Condition.SP_CODE, new TokenParam("http://snomed.info/sct", "123"));

			IBundleProvider results = myConditionDao.search(map, mySrd);
			List<String> ids = toUnqualifiedVersionlessIdValues(results);
			assertEquals(2, ids.size());

			SqlQueryList list = (SqlQueryList) mySrd.getUserData().get("QUERIES");
			assertEquals(1, list.size());
			String query = list.get(0).getSql(true, false);
			ourLog.info("Query: {}", query);

			assertThat(query, containsString("HASH_SYS_AND_VALUE = '3788488238034018567'"));

		} finally {
			myInterceptorRegistry.unregisterInterceptor(interceptor);
		}
	}



}
