package ca.uhn.fhir.jpa.dao.dstu3;

import ca.uhn.fhir.jpa.dao.data.ISearchDao;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.test.BaseJpaDstu3Test;
import ca.uhn.fhir.jpa.util.SqlQuery;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.TokenOrListParam;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.dstu3.model.CodeableConcept;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.Condition;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.dstu3.model.Reference;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.lessThan;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class FhirResourceDaoDstu3SearchSqlTest extends BaseJpaDstu3Test {
	private static final Logger ourLog = LoggerFactory.getLogger(FhirResourceDaoDstu3SearchSqlTest.class);

	@Autowired
	private ISearchDao mySearchEntityDao;

	@Test
	public void testSearchCondition_ByPatientAndCategories() {
		Patient patient = new Patient();
		//patient.addIdentifier().setSystem("http://mydomain.com/patient-identifier").setValue("PID1");
		IIdType patientId = myPatientDao.create(patient).getId().toUnqualifiedVersionless();

		int numResources = 3000;
		for (int i = 1; i <= numResources; i++) {
			Condition condition = new Condition();
			condition.setSubject(new Reference(patientId));
			List<CodeableConcept> categories = new ArrayList<>();
			categories.add(new CodeableConcept()
				.addCoding(new Coding("acc_condcat_fkc", "ACCEVN", "Access Event")));
			categories.add(new CodeableConcept()
				.addCoding(new Coding("acc_careplncat_fkc", "ACCMNTRG", "Access Monitoring")));
			categories.add(new CodeableConcept()
				.addCoding(new Coding("acc_condcat_fkc", "MIDTX", "During Tx")));
			condition.setCategory(categories);
			IIdType conditionId = myConditionDao.create(condition).getId().toUnqualifiedVersionless();
			if (i % 500 == 0) {
				ourLog.info("Created Condition with ID: " + conditionId);
			}
		}

		// Search
		// http://127.0.0.1:8000/Condition
		// ?_count=300
		// &category=ACCEVN%2CACCMNTRG%2CMIDTX
		// &patient=Patient%2F123
		SearchParameterMap map = new SearchParameterMap();
		map.setCount(300);
		map.add("patient", new ReferenceParam(patientId.toString()));
		map.add("category", new TokenOrListParam(null, "ACCEVN", "ACCMNTRG", "MIDTX")); // , = %2C

		IBundleProvider outcome = null;
		String uuid = null;
		for (int i = 0; i < 3000; i += 300) {

			ourLog.info("Starting batch {}-{}", i, i+300);
			myCaptureQueriesListener.clear();
			if (outcome == null) {
				outcome = myConditionDao.search(map);
				uuid = outcome.getUuid();
			} else {
				outcome = myPagingProvider.retrieveResultList(mySrd, uuid);
			}

			List<IBaseResource> resources = outcome.getResources(i, i + 300);
			ourLog.info("Batch {}-{} returned {} resources", i, i+300, resources.size());
			assertEquals(300, resources.size());

			List<SqlQuery> query = myCaptureQueriesListener.getSelectQueries();
			for (SqlQuery next : query) {
				String sql = next.getSql(false, false);
				int paramCount = StringUtils.countMatches(sql, "?");
				ourLog.info("SQL has {} params", paramCount);
				assertThat("SQL has >1000 params: " + sql, paramCount, lessThan(1000));
				if (sql.contains("HASH_VALUE IN")) {
					sql = next.getSql(true, false);
					ourLog.info("SQL: {}", sql);
				}
			}

		}
		
	}
}
