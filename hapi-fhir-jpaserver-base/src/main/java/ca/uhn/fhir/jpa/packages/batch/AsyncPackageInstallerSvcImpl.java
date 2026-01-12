/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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
// Created by claude-opus-4-5-20250101
package ca.uhn.fhir.jpa.packages.batch;

import ca.uhn.fhir.batch2.api.IJobCoordinator;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.JobInstanceStartRequest;
import ca.uhn.fhir.batch2.model.StatusEnum;
import ca.uhn.fhir.jpa.batch.models.Batch2JobStartResponse;
import ca.uhn.fhir.jpa.packages.PackageInstallOutcomeJson;
import ca.uhn.fhir.jpa.packages.PackageInstallationSpec;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.util.JsonUtil;
import jakarta.annotation.Nullable;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Implementation of {@link IAsyncPackageInstallerSvc} that uses the Batch2 framework
 * to execute package installations asynchronously.
 *
 * @since 8.2.0
 */
public class AsyncPackageInstallerSvcImpl implements IAsyncPackageInstallerSvc {

	private static final Logger ourLog = LoggerFactory.getLogger(AsyncPackageInstallerSvcImpl.class);

	@Autowired
	private IJobCoordinator myJobCoordinator;

	@Override
	public String startAsyncInstall(PackageInstallationSpec theSpec) {
		ourLog.info("Starting async package installation for {}#{}", theSpec.getName(), theSpec.getVersion());

		// Convert spec to job parameters (validates that packageContents is null)
		PackageInstallJobParameters params = PackageInstallJobParameters.fromSpec(theSpec);

		// Create the job start request
		JobInstanceStartRequest request = new JobInstanceStartRequest();
		request.setJobDefinitionId(PackageInstallJobConfig.JOB_PACKAGE_INSTALL);
		request.setParameters(params);

		// Start the job
		Batch2JobStartResponse response = myJobCoordinator.startInstance(request);
		String jobId = response.getInstanceId();

		ourLog.info(
				"Async package installation job started with ID: {} for {}#{}",
				jobId,
				theSpec.getName(),
				theSpec.getVersion());

		return jobId;
	}

	@Override
	public JobInstance getJobStatus(String theJobId) {
		if (StringUtils.isBlank(theJobId)) {
			throw new IllegalArgumentException("Job ID must not be blank");
		}

		try {
			return myJobCoordinator.getInstance(theJobId);
		} catch (ResourceNotFoundException e) {
			throw new ResourceNotFoundException("Package installation job not found: " + theJobId);
		}
	}

	@Override
	@Nullable
	public PackageInstallOutcomeJson getCompletedOutcome(String theJobId) {
		JobInstance instance = getJobStatus(theJobId);

		if (instance.getStatus() != StatusEnum.COMPLETED) {
			return null;
		}

		// Try to get the report from the job instance
		String reportJson = instance.getReport();
		if (StringUtils.isBlank(reportJson)) {
			ourLog.warn("Completed job {} has no report", theJobId);
			return new PackageInstallOutcomeJson();
		}

		try {
			PackageInstallReportJson report = JsonUtil.deserialize(reportJson, PackageInstallReportJson.class);
			return report.toOutcomeJson();
		} catch (Exception e) {
			ourLog.warn("Failed to parse job report for {}: {}", theJobId, e.getMessage());
			return new PackageInstallOutcomeJson();
		}
	}

	@Override
	public boolean isAsyncInstallAvailable() {
		return myJobCoordinator != null;
	}

	/**
	 * For testing purposes only.
	 */
	public void setJobCoordinator(IJobCoordinator theJobCoordinator) {
		myJobCoordinator = theJobCoordinator;
	}
}
