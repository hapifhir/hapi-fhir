package ca.uhn.fhir.batch2.jobs.export;

import ca.uhn.fhir.batch2.model.StatusEnum;
import ca.uhn.fhir.jpa.api.svc.IBatch2JobRunner;
import ca.uhn.fhir.jpa.bulk.export.model.BulkExportJobStatusEnum;
import org.slf4j.Logger;

import static org.slf4j.LoggerFactory.getLogger;

public class BulkExportUtil {
	private static final Logger ourLog = getLogger(BulkExportUtil.class);

	private BulkExportUtil() {

	}

	/**
	 * Converts Batch2 StatusEnum -> BulkExportJobStatusEnum
	 */
	public static BulkExportJobStatusEnum fromBatchStatus(StatusEnum status) {
		switch (status) {
			case QUEUED:
			case FINALIZE:
				return BulkExportJobStatusEnum.SUBMITTED;
			case COMPLETED :
				return BulkExportJobStatusEnum.COMPLETE;
			case IN_PROGRESS:
				return BulkExportJobStatusEnum.BUILDING;
			default:
				ourLog.warn("Unrecognized status {}; treating as FAILED/CANCELLED/ERRORED", status.name());
			case FAILED:
			case CANCELLED:
			case ERRORED:
				return BulkExportJobStatusEnum.ERROR;
		}
	}
}
