package ca.uhn.fhir.jpa.packages.util;

import ca.uhn.fhir.jpa.packages.PackageInstallOutcomeJson;
import ca.uhn.fhir.jpa.packages.PackageInstallationSpec;
import org.hl7.fhir.utilities.json.model.JsonObject;
import org.hl7.fhir.utilities.npm.NpmPackage;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class PackageUtilsTest {

	@Test
	public void testExtractDependentPackages() {
		// set up
		JsonObject dependencies = new JsonObject();
		dependencies.add("hl7.fhir.us.core", "3.1.1");
		dependencies.add("de.fhir.medication", "1.0.4");
		dependencies.add("hl7.fhir.uv.cqm", "2.0.0");

		JsonObject npm = new JsonObject();
		npm.add("name", "packageName");
		npm.add("version", "packageVersion");
		npm.add("dependencies", dependencies);

		NpmPackage npmPackage = NpmPackage.empty();
		npmPackage.setNpm(npm);

		PackageInstallationSpec packageInstallationSpec = new PackageInstallationSpec();
		packageInstallationSpec.addDependencyExclude("^de\\..*");

		PackageInstallOutcomeJson outcome = new PackageInstallOutcomeJson();

		// execute
		List<PackageUtils.DependentPackage> actualPackages =
			PackageUtils.extractDependentPackages(npmPackage, packageInstallationSpec, outcome);

		// validate
		assertThat(actualPackages).hasSize(2);
		assertThat(actualPackages.get(0).name()).isEqualTo("hl7.fhir.us.core");
		assertThat(actualPackages.get(0).version()).isEqualTo("3.1.1");
		assertThat(actualPackages.get(1).name()).isEqualTo("hl7.fhir.uv.cqm");
		assertThat(actualPackages.get(1).version()).isEqualTo("2.0.0");

		assertThat(outcome.getMessage())
			.containsExactlyInAnyOrder("Package packageName#packageVersion depends on package hl7.fhir.us.core#3.1.1",
				"Package packageName#packageVersion depends on package de.fhir.medication#1.0.4",
				"Not installing dependency de.fhir.medication because it matches exclude criteria: ^de\\..*",
				"Package packageName#packageVersion depends on package hl7.fhir.uv.cqm#2.0.0");
	}

	@Test
	public void testExtractDependentPackages_packageHasNoDependencies_returnEmptyList() {
		// set up
		JsonObject npm = new JsonObject();
		npm.add("name", "packageName");
		npm.add("version", "packageVersion");

		NpmPackage npmPackage = NpmPackage.empty();
		npmPackage.setNpm(npm);

		PackageInstallationSpec packageInstallationSpec = new PackageInstallationSpec();

		PackageInstallOutcomeJson outcome = new PackageInstallOutcomeJson();

		// execute
		List<PackageUtils.DependentPackage> actualPackages =
			PackageUtils.extractDependentPackages(npmPackage, packageInstallationSpec, outcome);

		// validate
		assertThat(actualPackages).isEmpty();
	}
}
