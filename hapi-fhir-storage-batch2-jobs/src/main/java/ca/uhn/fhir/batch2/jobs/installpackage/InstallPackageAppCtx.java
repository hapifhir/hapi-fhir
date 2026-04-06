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

import ca.uhn.fhir.batch2.jobs.installpackage.model.InstallationOutcomeJson;
import ca.uhn.fhir.batch2.jobs.installpackage.model.PackageContentsJson;
import ca.uhn.fhir.batch2.jobs.installpackage.model.PackageInstallationJobParameters;
import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.jpa.packages.PackageInstallOutcomeJson;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static ca.uhn.fhir.util.Batch2JobDefinitionConstants.INSTALL_PACKAGE;

@Configuration
public class InstallPackageAppCtx {

	@Bean("installPackageJobDefinition")
	public JobDefinition<PackageInstallationJobParameters> installPackageJobDefinition() {
		return JobDefinition.newBuilder()
				.setJobDefinitionId(INSTALL_PACKAGE)
				.setJobDescription("Install NPM Package")
				.setJobDefinitionVersion(1)
				.setParametersType(PackageInstallationJobParameters.class)
				.gatedExecution()
				.addFirstStep("fetch-package", "Fetch the NPM Package", PackageContentsJson.class, fetchPackageStep())
				.addIntermediateStep(
						"process-package",
						"Install the contents of the NPM package",
						InstallationOutcomeJson.class,
						processPackageStep())
				.addFinalReducerStep(
						"finalize",
						"Refresh caches and consolidate output",
						PackageInstallOutcomeJson.class,
						finalizeInstallationStep())
				.build();
	}

	@Bean
	public FetchPackageStep fetchPackageStep() {
		return new FetchPackageStep();
	}

	@Bean
	public ProcessPackageStep processPackageStep() {
		return new ProcessPackageStep();
	}

	@Bean
	public FinalizeInstallationStep finalizeInstallationStep() {
		return new FinalizeInstallationStep();
	}
}
