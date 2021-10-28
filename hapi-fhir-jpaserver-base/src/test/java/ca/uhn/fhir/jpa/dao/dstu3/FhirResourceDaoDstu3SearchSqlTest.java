package ca.uhn.fhir.jpa.dao.dstu3;

import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class FhirResourceDaoDstu3SearchSqlTest extends BaseJpaDstu3Test {
	private static final Logger ourLog = LoggerFactory.getLogger(FhirResourceDaoDstu3SearchSqlTest.class);

	@Test
	public void testSearchCondition_ByPatientAndCategories() {
		Patient patient = new Patient();
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

		myMemoryCacheService.invalidateAllCaches();

		// Search
		myCaptureQueriesListener.clear();
		// http://127.0.0.1:8000/Condition
		// ?_count=300
		// &category=ACCEVN%2CACCMNTRG%2CMIDTX
		// &patient=Patient%2F123
		SearchParameterMap map = new SearchParameterMap();
		map.setLoadSynchronous(false);
		map.setCount(300);
		map.add("patient", new ReferenceParam(patientId.toString()));
		map.add("category", new TokenOrListParam(null, "ACCEVN", "ACCMNTRG", "MIDTX"));

		IBundleProvider outcome = null;
		String uuid = null;
		for (int i = 0; i < 3000; i += 300) {
			myCaptureQueriesListener.clear();
			if (outcome == null) {
				outcome = myConditionDao.search(map);
				uuid = outcome.getUuid();
			} else {
				outcome = myPagingProvider.retrieveResultList(mySrd, uuid);
			}
			List<IBaseResource> resources = outcome.getResources(i, i + 300);
			ourLog.info("Batch {}-{} returned {} resources", i, i+300, resources.size());
			List<SqlQuery> query = myCaptureQueriesListener
				.getSelectQueries()
				.stream()
				.filter(t -> t.getSql(false, false).toLowerCase(Locale.ROOT).contains("res_id not in"))
				.collect(Collectors.toList());
			for (SqlQuery next : query) {
				String sql = next.getSql(false, false);
				int numSQLQueryparams = StringUtils.countMatches(sql, "?");
				ourLog.info("SQL: {}", sql);
				ourLog.info("SQL has {} params", numSQLQueryparams);
				assertTrue((numSQLQueryparams <= 1000),
					"The generated SQL should never have more than 1,000 parameters! " +
					"The number of parameters is " + numSQLQueryparams + " and the SQL is " + sql);
			}
		}
		ourLog.info("DONE SQL Queries!\n");
	}
}
