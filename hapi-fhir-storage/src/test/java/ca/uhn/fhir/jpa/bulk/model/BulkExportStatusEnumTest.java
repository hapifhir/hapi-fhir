package ca.uhn.fhir.jpa.bulk.model;

import ca.uhn.fhir.jpa.bulk.export.model.BulkExportJobStatusEnum;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.BatchStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BulkExportStatusEnumTest {
	@Test
	public void testFromBatchStatus(){
		assertEquals(BulkExportJobStatusEnum.fromBatchStatus(BatchStatus.STARTED), BulkExportJobStatusEnum.BUILDING);
		assertEquals(BulkExportJobStatusEnum.fromBatchStatus(BatchStatus.STARTING), BulkExportJobStatusEnum.SUBMITTED);
		assertEquals(BulkExportJobStatusEnum.fromBatchStatus(BatchStatus.COMPLETED), BulkExportJobStatusEnum.COMPLETE);
		assertEquals(BulkExportJobStatusEnum.fromBatchStatus(BatchStatus.STOPPING), BulkExportJobStatusEnum.ERROR);
		assertEquals(BulkExportJobStatusEnum.fromBatchStatus(BatchStatus.STOPPED), BulkExportJobStatusEnum.ERROR);
		assertEquals(BulkExportJobStatusEnum.fromBatchStatus(BatchStatus.FAILED), BulkExportJobStatusEnum.ERROR);
		assertEquals(BulkExportJobStatusEnum.fromBatchStatus(BatchStatus.ABANDONED), BulkExportJobStatusEnum.ERROR);
		assertEquals(BulkExportJobStatusEnum.fromBatchStatus(BatchStatus.UNKNOWN), BulkExportJobStatusEnum.ERROR);
	}
}
