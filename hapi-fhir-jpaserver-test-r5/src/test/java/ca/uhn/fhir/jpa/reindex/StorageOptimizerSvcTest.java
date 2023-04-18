package ca.uhn.fhir.jpa.reindex;

import ca.uhn.fhir.jpa.dao.r5.BaseJpaR5Test;
import ca.uhn.fhir.jpa.model.entity.ResourceHistoryTable;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import org.hl7.fhir.r5.model.Patient;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class StorageOptimizerSvcTest extends BaseJpaR5Test {

	@Test
	public void testOptimizeCurrentVersions() {
		myDaoConfig.setInlineResourceTextBelowSize(0);
		String[] expectedNames = new String[10];
		for (int i = 0; i < 10; i++) {
			expectedNames[i] = "FAM" + i;
			createPatient(withActiveTrue(), withFamily(expectedNames[i]));
		}

		runInTransaction(() -> {
			ResourceHistoryTable resource = myResourceHistoryTableDao.findAll().iterator().next();
			assertNull(resource.getResourceTextVc());
			assertNotNull(resource.getResource());
		});
		List<String> names = myPatientDao
			.search(SearchParameterMap.newSynchronous(), mySrd)
			.getAllResources()
			.stream()
			.map(t -> ((Patient) t).getNameFirstRep().getFamily())
			.toList();
		assertThat(names, containsInAnyOrder(expectedNames));

	}


}
