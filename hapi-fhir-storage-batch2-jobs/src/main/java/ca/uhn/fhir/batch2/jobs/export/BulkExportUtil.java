package ca.uhn.fhir.batch2.jobs.export;

/*-
 * #%L
 * hapi-fhir-storage-batch2-jobs
 * %%
 * Copyright (C) 2014 - 2023 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

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
