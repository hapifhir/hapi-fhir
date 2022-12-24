package ca.uhn.fhir.lifecycle;

import ca.uhn.fhir.jpa.bulk.export.api.IBulkDataExportJobSchedulingHelper;
import ca.uhn.fhir.jpa.bulk.imprt.api.IBulkDataImportSvc;
import ca.uhn.fhir.jpa.model.sched.ISchedulerService;
import org.springframework.beans.factory.annotation.Autowired;

public class JpaLifecycle extends BaseHapiLifecycle {
	@Autowired
	IBulkDataImportSvc myBulkDataImportSvc;
	@Autowired
	IBulkDataExportJobSchedulingHelper myBulkDataExportJobSchedulingHelper;
	@Autowired
	ISchedulerService mySchedulerService;

	public JpaLifecycle() {}

	@Override
	public void startup() {
		myBulkDataImportSvc.start();
		myBulkDataExportJobSchedulingHelper.start();
		mySchedulerService.start();
	}

	@Override
	public void shutdown() {
		mySchedulerService.stop();
	}
}
