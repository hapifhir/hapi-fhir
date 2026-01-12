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

import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.jpa.packages.PackageInstallOutcomeJson;
import ca.uhn.fhir.jpa.packages.PackageInstallationSpec;
import jakarta.annotation.Nullable;

/**
 * Service interface for asynchronous package installation via Batch2.
 *
 * @since 8.2.0
 */
public interface IAsyncPackageInstallerSvc {

	/**
	 * Starts an asynchronous package installation job.
	 * <p>
	 * Note: The installation specification must not contain inline package contents (packageContents).
	 * For async installation, use either packageUrl or name+version.
	 * </p>
	 *
	 * @param theSpec the installation specification
	 * @return the job instance ID for polling
	 * @throws IllegalArgumentException if packageContents is not null
	 */
	String startAsyncInstall(PackageInstallationSpec theSpec);

	/**
	 * Gets the current status of an async installation job.
	 *
	 * @param theJobId the job instance ID
	 * @return the job instance with current status
	 */
	JobInstance getJobStatus(String theJobId);

	/**
	 * Gets the installation outcome if the job is complete.
	 *
	 * @param theJobId the job instance ID
	 * @return the outcome, or null if the job is not complete or has no outcome
	 */
	@Nullable
	PackageInstallOutcomeJson getCompletedOutcome(String theJobId);

	/**
	 * Checks if async package installation is available.
	 * This may return false if the Batch2 framework is not configured.
	 *
	 * @return true if async installation is available
	 */
	boolean isAsyncInstallAvailable();
}
