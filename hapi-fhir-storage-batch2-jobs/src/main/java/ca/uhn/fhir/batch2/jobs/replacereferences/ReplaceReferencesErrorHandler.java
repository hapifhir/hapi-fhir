/*-
 * #%L
 * hapi-fhir-storage-batch2-jobs
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
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
package ca.uhn.fhir.batch2.jobs.replacereferences;

import ca.uhn.fhir.batch2.api.IJobCompletionHandler;
import ca.uhn.fhir.batch2.api.JobCompletionDetails;
import ca.uhn.fhir.batch2.util.Batch2TaskHelper;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import org.hl7.fhir.r4.model.Task;

/**
 * This class is the error handler for ReplaceReferences and Merge jobs.
 * It updates the status of the associated task.
 */
public class ReplaceReferencesErrorHandler<PT extends ReplaceReferencesJobParameters>
		implements IJobCompletionHandler<PT> {

	private final Batch2TaskHelper myBatch2TaskHelper;
	private final IFhirResourceDao<Task> myTaskDao;

	public ReplaceReferencesErrorHandler(Batch2TaskHelper theBatch2TaskHelper, IFhirResourceDao<Task> theTaskDao) {
		myBatch2TaskHelper = theBatch2TaskHelper;
		myTaskDao = theTaskDao;
	}

	@Override
	public void jobComplete(JobCompletionDetails<PT> theDetails) {

		PT jobParameters = theDetails.getParameters();

		SystemRequestDetails requestDetails =
				SystemRequestDetails.forRequestPartitionId(jobParameters.getPartitionId());

		myBatch2TaskHelper.updateTaskStatusOnJobCompletion(myTaskDao, requestDetails, theDetails);
	}
}
