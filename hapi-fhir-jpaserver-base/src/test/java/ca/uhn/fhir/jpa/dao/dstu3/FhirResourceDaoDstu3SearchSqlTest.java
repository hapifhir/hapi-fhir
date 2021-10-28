package ca.uhn.fhir.jpa.dao.dstu3;

import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.util.SqlQuery;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.param.HasParam;
import ca.uhn.fhir.rest.param.ReferenceParam;
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

public class FhirResourceDaoDstu3SearchSqlTest extends BaseJpaDstu3Test {
	private static final Logger ourLog = LoggerFactory.getLogger(FhirResourceDaoDstu3SearchSqlTest.class);

	@Test
	public void testSearchByPractitionerRole_has() {
		//
		// TODO KBD Try This:
		// Start with a fork of the actual branch they are using
		// Make sure your data actually matches the patterns they have (do they really have a 1:1 ratio of PractitionerRole to Practitioner?)
		// Make the SearchParameterMap not be synchronous
		// Then call results.getResources(0,300)  followed by results.getResources(300, 600) etc etc
		//

		Patient patient = new Patient();
		//patient.addIdentifier().setSystem("http://mydomain.com/patient-identifier").setValue("PID1");
		IIdType patientId = myPatientDao.create(patient).getId().toUnqualifiedVersionless();

		int numResources = 3000;
		for (int i = 1; i <= numResources; i++) {
			Condition condition = new Condition();
			condition.setSubject(new Reference(patientId));
			//condition.getIdentifier().add(new Identifier().setSystem("http://mydomain.com/condition-identifier").setValue(new Integer("CID"+i).toString()));
			List<CodeableConcept> categories = new ArrayList<>();
			categories.add(new CodeableConcept()
				.addCoding(new Coding("acc_condcat_fkc", "ACCEVN", "Access Event")));
			categories.add(new CodeableConcept()
				.addCoding(new Coding("acc_careplncat_fkc", "ACCMNTRG", "Access Monitoring")));
			categories.add(new CodeableConcept()
				.addCoding(new Coding("acc_condcat_fkc", "MIDTX", "During Tx")));
			condition.setCategory(categories);
			IIdType conditionId = myConditionDao.create(condition).getId().toUnqualifiedVersionless();
			System.out.println("Created Condition with ID: " + conditionId);
		}

		myMemoryCacheService.invalidateAllCaches();

		// Search
		myCaptureQueriesListener.clear();
		// http://127.0.0.1:8000/Condition
		// ?_count=300
		// &category=ACCEVN%2CACCMNTRG%2CMIDTX
		// &patient=Patient%2F123
		SearchParameterMap map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.setCount(300);
		map.add("patient", new ReferenceParam(patientId.toString()));
		//map.add("category", new TokenParam("ACCEVN,ACCMNTRG,MIDTX")); // , = %2C
		IBundleProvider outcome = myConditionDao.search(map);
		List<IBaseResource> resources = outcome.getResources(0, 300);
		System.out.println("Batch 0-300 returned " + resources.size() + " resources.");
		resources = outcome.getResources(300, 600);
		System.out.println("Batch 300-600 returned " + resources.size() + " resources.");
		resources = outcome.getResources(600, 900);
		System.out.println("Batch 600-900 returned " + resources.size() + " resources.");
		resources = outcome.getResources(900, 1200);
		System.out.println("Batch 900-1200 returned " + resources.size() + " resources.");

//		assertEquals(3, myCaptureQueriesListener.countSelectQueries());
		for (SqlQuery query : myCaptureQueriesListener.getSelectQueriesForCurrentThread()) {
			System.out.println("SQL Query:\n" + query.getSql(true, true));
		}
		System.out.println("DONE SQL Queries!\n");

	}
}
