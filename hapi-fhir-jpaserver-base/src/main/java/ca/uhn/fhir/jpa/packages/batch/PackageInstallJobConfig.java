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

import ca.uhn.fhir.batch2.model.JobDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Spring configuration for the package installation batch job.
 *
 * @since 8.2.0
 */
@Configuration
public class PackageInstallJobConfig {

	/**
	 * Job definition ID for package installation.
	 */
	public static final String JOB_PACKAGE_INSTALL = "PACKAGE_INSTALL";

	/**
	 * Defines the package installation job with two steps:
	 * <ol>
	 *   <li>Install package - performs the actual package installation</li>
	 *   <li>Generate report - creates a summary report for the job (reduction step)</li>
	 * </ol>
	 * <p>
	 * The job uses gated execution to ensure all installation chunks complete before
	 * the report is generated. The final reduction step aggregates all outcomes and
	 * stores the report in the job instance.
	 * </p>
	 *
	 * @return the job definition
	 */
	@Bean
	public JobDefinition<PackageInstallJobParameters> packageInstallJobDefinition() {
		return JobDefinition.newBuilder()
				.setJobDefinitionId(JOB_PACKAGE_INSTALL)
				.setJobDescription("Install FHIR packages asynchronously")
				.setJobDefinitionVersion(1)
				.gatedExecution()
				.setParametersType(PackageInstallJobParameters.class)
				.addFirstStep(
						"install-package",
						"Install the FHIR package",
						PackageInstallStepOutcomeJson.class,
						installPackageStep())
				.addFinalReducerStep(
						"generate-report",
						"Generate installation report",
						PackageInstallReportJson.class,
						generatePackageInstallReportStep())
				.build();
	}

	@Bean
	public InstallPackageStep installPackageStep() {
		return new InstallPackageStep();
	}

	@Bean
	public GeneratePackageInstallReportStep generatePackageInstallReportStep() {
		return new GeneratePackageInstallReportStep();
	}

	@Bean
	public AsyncPackageInstallerSvcImpl asyncPackageInstallerSvc() {
		return new AsyncPackageInstallerSvcImpl();
	}
}
