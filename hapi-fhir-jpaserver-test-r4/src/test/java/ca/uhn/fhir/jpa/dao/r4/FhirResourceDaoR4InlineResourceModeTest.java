package ca.uhn.fhir.jpa.dao.r4;

import static org.junit.jupiter.api.Assertions.assertTrue;
import ca.uhn.fhir.jpa.dao.data.IResourceHistoryTableDao;
import ca.uhn.fhir.jpa.model.entity.ResourceEncodingEnum;
import ca.uhn.fhir.jpa.model.entity.ResourceHistoryTable;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.rest.param.HistorySearchDateRangeParam;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class FhirResourceDaoR4InlineResourceModeTest extends BaseJpaR4Test {

	@Test
	public void testRetrieveNonInlinedResource() {
		IIdType id = createPatient(withActiveTrue());
		Long pid = id.getIdPartAsLong();

		relocateResourceTextToCompressedColumn(pid, 1L);

		runInTransaction(()->{
			ResourceHistoryTable historyEntity = myResourceHistoryTableDao.findForIdAndVersionAndFetchProvenance(pid, 1);
			assertNotNull(historyEntity.getResource());
			assertNull(historyEntity.getResourceTextVc());
			assertEquals(ResourceEncodingEnum.JSONC, historyEntity.getEncoding());
		});

		// Read
		validatePatient(myPatientDao.read(id.withVersion(null), mySrd));

		// VRead
		validatePatient(myPatientDao.read(id.withVersion("1"), mySrd));

		// Search (Sync)
		validatePatient(myPatientDao.search(SearchParameterMap.newSynchronous(), mySrd).getResources(0, 1).get(0));

		// Search (Async)
		validatePatient(myPatientDao.search(new SearchParameterMap(), mySrd).getResources(0, 1).get(0));

		// History
		validatePatient(myPatientDao.history(id, new HistorySearchDateRangeParam(new HashMap<>(), new DateRangeParam(), 0), mySrd).getResources(0, 1).get(0));
	}


	private void validatePatient(IBaseResource theRead) {
		assertTrue(((Patient) theRead).getActive());
	}


}
