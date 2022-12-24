package ca.uhn.fhir.lifecycle;

import ca.uhn.fhir.batch2.api.IJobMaintenanceService;
import ca.uhn.fhir.jpa.bulk.export.api.IBulkDataExportJobSchedulingHelper;
import ca.uhn.fhir.jpa.bulk.imprt.api.IBulkDataImportSvc;
import ca.uhn.fhir.jpa.model.sched.ISchedulerService;
import ca.uhn.fhir.jpa.search.reindex.IResourceReindexingSvc;
import ca.uhn.fhir.jpa.term.api.ITermDeferredStorageSvc;
import ca.uhn.fhir.jpa.term.api.ITermReadSvc;
import org.springframework.beans.factory.annotation.Autowired;

public class JpaLifecycle extends BaseHapiLifecycle {
	@Autowired
	IBulkDataImportSvc myBulkDataImportSvc;
	@Autowired
	IBulkDataExportJobSchedulingHelper myBulkDataExportJobSchedulingHelper;
	@Autowired
	ISchedulerService mySchedulerService;
	@Autowired
	IJobMaintenanceService myJobMaintenanceService;
	@Autowired
	ITermDeferredStorageSvc myTermDeferredStorageSvc;
	@Autowired
	ITermReadSvc myTermReadSvc;
	@Autowired
	IResourceReindexingSvc myResourceReindexingSvc;

	public JpaLifecycle() {}

	@Override
	public void startup() {
		myBulkDataImportSvc.start();
		myBulkDataExportJobSchedulingHelper.start();
		myTermReadSvc.start();
		myResourceReindexingSvc.start();

		mySchedulerService.start();

		myBulkDataImportSvc.scheduleJobs();
		myBulkDataExportJobSchedulingHelper.scheduleJobs();
		myJobMaintenanceService.scheduleJobs();
		myTermDeferredStorageSvc.scheduleJobs();
		myTermReadSvc.scheduleJobs();
		myResourceReindexingSvc.scheduleJobs();
	}

	@Override
	public void shutdown() {
		mySchedulerService.stop();
	}
}
