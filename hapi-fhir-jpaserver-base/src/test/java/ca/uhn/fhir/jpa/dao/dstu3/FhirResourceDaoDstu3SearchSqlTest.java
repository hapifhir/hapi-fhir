package ca.uhn.fhir.jpa.dao.dstu3;

import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.util.SqlQuery;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.param.HasParam;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.TokenOrListParam;
import ca.uhn.fhir.rest.param.TokenParam;
import com.ctc.wstx.shaded.msv_core.verifier.jarv.Const;
import org.hl7.fhir.dstu3.model.Condition;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.dstu3.model.Reference;
import org.hl7.fhir.dstu3.model.StringType;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.dstu3.model.CodeableConcept;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.Enumerations;
import org.hl7.fhir.dstu3.model.Identifier;
import org.hl7.fhir.dstu3.model.Practitioner;
import org.hl7.fhir.dstu3.model.PractitionerRole;
import org.hl7.fhir.dstu3.model.SearchParameter;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
			ourLog.info("Created Condition with ID: " + conditionId);
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
		IBundleProvider outcome = myConditionDao.search(map);
		String uuid = outcome.getUuid();
		ourLog.info("** Search returned UUID: {}", uuid);
		List<String> ids = toUnqualifiedVersionlessIdValues(outcome, 0, 300, true);
		//assertEquals(201, myDatabaseBackedPagingProvider.retrieveResultList(null, uuid).size().intValue());
		ourLog.info("Batch 0-300 returned " + ids.size() + " ids.");

		ids = toUnqualifiedVersionlessIdValues(outcome, 300, 600, false);
		ourLog.info("Batch 300-600 returned " + ids.size() + " ids.");
		ids = toUnqualifiedVersionlessIdValues(outcome, 600, 900, false);
		ourLog.info("Batch 600-900 returned " + ids.size() + " ids.");
		ids = toUnqualifiedVersionlessIdValues(outcome, 900, 1200, false);
		ourLog.info("Batch 900-1200 returned " + ids.size() + " ids.");

		//List<IBaseResource> resources = outcome.getResources(0, 300);
		//ourLog.info("Batch 0-300 returned " + resources.size() + " resources.");
		//resources = outcome.getResources(300, 600);
		//ourLog.info("Batch 300-600 returned " + resources.size() + " resources.");
		//resources = outcome.getResources(600, 900);
		//ourLog.info("Batch 600-900 returned " + resources.size() + " resources.");
		//resources = outcome.getResources(900, 1200);
		//ourLog.info("Batch 900-1200 returned " + resources.size() + " resources.");

//		assertEquals(3, myCaptureQueriesListener.countSelectQueries());
		for (SqlQuery query : myCaptureQueriesListener.getSelectQueriesForCurrentThread()) {
			ourLog.info("SQL Query:\n" + query.getSql(true, true));
		}
		ourLog.info("DONE SQL Queries!\n");

	}
}
