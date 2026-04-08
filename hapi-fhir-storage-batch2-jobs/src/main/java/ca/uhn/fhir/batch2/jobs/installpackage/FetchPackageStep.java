/*-
 * #%L
 * HAPI-FHIR Storage Batch2 Jobs
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
package ca.uhn.fhir.batch2.jobs.installpackage;

import ca.uhn.fhir.batch2.api.IFirstJobStepWorker;
import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.api.VoidModel;
import ca.uhn.fhir.batch2.jobs.installpackage.model.PackageContentsJson;
import ca.uhn.fhir.batch2.jobs.installpackage.model.PackageInstallationJobParameters;
import ca.uhn.fhir.jpa.packages.loader.IPackageLoader;
import ca.uhn.fhir.jpa.packages.loader.NpmPackageData;
import jakarta.annotation.Nonnull;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.Base64;

public class FetchPackageStep implements IFirstJobStepWorker<PackageInstallationJobParameters, PackageContentsJson> {

	private IPackageLoader myPackageLoader;

	public FetchPackageStep(IPackageLoader thePackageLoader) {
		this.myPackageLoader = thePackageLoader;
	}

	@Nonnull
	@Override
	public RunOutcome run(
			@Nonnull StepExecutionDetails<PackageInstallationJobParameters, VoidModel> theStepExecutionDetails,
			@Nonnull IJobDataSink<PackageContentsJson> theDataSink)
			throws JobExecutionFailedException {

		try {
			NpmPackageData npmPackage = myPackageLoader.fetchPackageFromPackageSpec(
					theStepExecutionDetails.getParameters().getInstallationSpec());
			PackageContentsJson contents = new PackageContentsJson();
			contents.setContents(Base64.getEncoder().encode(npmPackage.getBytes()));
			theDataSink.accept(contents);
		} catch (IOException e) {
			// We're only concerned with the happy path for now - error handling is a separate MR
		}

		return RunOutcome.SUCCESS;
	}
}
