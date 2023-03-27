/*-
 * #%L
 * HAPI FHIR Storage api
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
package ca.uhn.fhir.jpa.bulk.export.provider;

import ca.uhn.fhir.jpa.bulk.export.model.BulkExportJobStatusEnum;

public class JobInfo {
	/**
	 * Legacy - but this jobId is not the job id
	 * but the actual id of the record storing metadata of the job
	 */
	private String myJobMetadataId;
	private BulkExportJobStatusEnum myStatus;

	public String getJobMetadataId() {
		return myJobMetadataId;
	}

	public JobInfo setJobMetadataId(String theJobId) {
		myJobMetadataId = theJobId;
		return this;
	}

	public BulkExportJobStatusEnum getStatus() {
		return myStatus;
	}

	public JobInfo setStatus(BulkExportJobStatusEnum theStatus) {
		myStatus = theStatus;
		return this;
	}
}
