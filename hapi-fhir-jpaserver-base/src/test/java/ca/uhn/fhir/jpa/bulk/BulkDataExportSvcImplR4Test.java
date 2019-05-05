package ca.uhn.fhir.jpa.bulk;

import ca.uhn.fhir.jpa.dao.data.IBulkExportJobDao;
import ca.uhn.fhir.jpa.dao.r4.BaseJpaR4Test;
import com.google.common.collect.Sets;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertNotNull;

public class BulkDataExportSvcImplR4Test extends BaseJpaR4Test {
	@Autowired
	private IBulkExportJobDao myBulkExportJobDao;
	@Autowired
	private IBulkDataExportSvc myBulkDataExportSvc;

	@Test
	public void testCreateBulkLoad() {

		IBulkDataExportSvc.NewJobInfo jobDetails = myBulkDataExportSvc.submitJob(Sets.newHashSet("Patient, Onservation"), null, null);
		assertNotNull(jobDetails);

	}

}
