/*-
 * #%L
 * hapi-fhir-storage-batch2-jobs
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
package ca.uhn.fhir.batch2.jobs.termcodesystem.codesystemdelete;

import ca.uhn.fhir.batch2.api.IJobCompletionHandler;
import ca.uhn.fhir.batch2.api.JobCompletionDetails;
import ca.uhn.fhir.jpa.term.api.ITermCodeSystemDeleteJobSvc;
import ca.uhn.fhir.jpa.term.models.TermCodeSystemDeleteJobParameters;

public class DeleteCodeSystemCompletionHandler implements IJobCompletionHandler<TermCodeSystemDeleteJobParameters> {

	private final ITermCodeSystemDeleteJobSvc myTermCodeSystemSvc;

	public DeleteCodeSystemCompletionHandler(ITermCodeSystemDeleteJobSvc theCodeSystemDeleteJobSvc) {
		myTermCodeSystemSvc = theCodeSystemDeleteJobSvc;
	}

	@Override
	public void jobComplete(JobCompletionDetails<TermCodeSystemDeleteJobParameters> theDetails) {
		myTermCodeSystemSvc.notifyJobComplete(theDetails.getInstance().getInstanceId());
	}
}
