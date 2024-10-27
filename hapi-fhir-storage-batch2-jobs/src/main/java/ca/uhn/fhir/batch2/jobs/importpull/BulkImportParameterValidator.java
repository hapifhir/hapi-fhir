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
package ca.uhn.fhir.batch2.jobs.importpull;

import ca.uhn.fhir.batch2.api.IJobParametersValidator;
import ca.uhn.fhir.batch2.importpull.models.Batch2BulkImportPullJobParameters;
import ca.uhn.fhir.jpa.bulk.imprt.api.IBulkDataImportSvc;
import ca.uhn.fhir.jpa.bulk.imprt.model.BulkImportJobJson;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class BulkImportParameterValidator implements IJobParametersValidator<Batch2BulkImportPullJobParameters> {
	private static final Logger ourLog = getLogger(BulkImportParameterValidator.class);

	private final IBulkDataImportSvc myBulkDataImportSvc;

	public BulkImportParameterValidator(IBulkDataImportSvc theIBulkDataImportSvc) {
		myBulkDataImportSvc = theIBulkDataImportSvc;
	}

	@Nullable
	@Override
	public List<String> validate(
			RequestDetails theRequestDetails, @Nonnull Batch2BulkImportPullJobParameters theParameters) {
		ourLog.info("BulkImportPull parameter validation begin");

		ArrayList<String> errors = new ArrayList<>();

		if (theParameters.getBatchSize() <= 0) {
			errors.add("Batch size must be positive");
		}

		String jobId = theParameters.getJobId();
		if (StringUtils.isEmpty(jobId)) {
			errors.add("Bulk Import Pull requires an existing job id");
		} else {
			BulkImportJobJson job = myBulkDataImportSvc.fetchJob(jobId);

			if (job == null) {
				errors.add("There is no persistent job that exists with UUID: " + jobId);
			}
		}

		ourLog.info("BulkImportPull parameter validation end");

		return errors;
	}
}
