package ca.uhn.fhir.jpa.packages;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

// Created by Claude Opus 4.6
class PackageDependencyRegistryTest {

	private final PackageInstallationSpec mySingleVersionSpec = new PackageInstallationSpec()
			.setVersionPolicy(PackageInstallationSpec.VersionPolicyEnum.SINGLE_VERSION);
	private final PackageInstallationSpec myMultiVersionSpec = new PackageInstallationSpec()
			.setVersionPolicy(PackageInstallationSpec.VersionPolicyEnum.MULTI_VERSION);

	@Test
	void isRedundant_sameVersion_skipsInBothModes() {
		PackageDependencyRegistry registry = new PackageDependencyRegistry();
		registry.addDependency("hl7.terminology.r4", "6.2.0");

		assertThat(registry.isRedundant("hl7.terminology.r4", "6.2.0", mySingleVersionSpec)).isTrue();
		assertThat(registry.isRedundant("hl7.terminology.r4", "6.2.0", myMultiVersionSpec)).isTrue();
	}

	@Test
	void isRedundant_olderVersion_singleVersionMode_skips() {
		PackageDependencyRegistry registry = new PackageDependencyRegistry();
		registry.addDependency("hl7.terminology.r4", "6.2.0");

		assertThat(registry.isRedundant("hl7.terminology.r4", "5.4.0", mySingleVersionSpec)).isTrue();
	}

	@Test
	void isRedundant_olderVersion_multiVersionMode_doesNotSkip() {
		PackageDependencyRegistry registry = new PackageDependencyRegistry();
		registry.addDependency("hl7.terminology.r4", "6.2.0");

		assertThat(registry.isRedundant("hl7.terminology.r4", "5.4.0", myMultiVersionSpec)).isFalse();
	}

	@Test
	void isRedundant_newerVersion_doesNotSkip() {
		PackageDependencyRegistry registry = new PackageDependencyRegistry();
		registry.addDependency("hl7.terminology.r4", "5.4.0");

		assertThat(registry.isRedundant("hl7.terminology.r4", "6.2.0", mySingleVersionSpec)).isFalse();
		assertThat(registry.isRedundant("hl7.terminology.r4", "6.2.0", myMultiVersionSpec)).isFalse();
	}

	@Test
	void isRedundant_differentPackage_doesNotSkip() {
		PackageDependencyRegistry registry = new PackageDependencyRegistry();
		registry.addDependency("hl7.terminology.r4", "6.2.0");

		assertThat(registry.isRedundant("hl7.fhir.r4.core", "4.0.1", mySingleVersionSpec)).isFalse();
	}

	@Test
	void isRedundant_neverSeen_doesNotSkip() {
		PackageDependencyRegistry registry = new PackageDependencyRegistry();

		assertThat(registry.isRedundant("hl7.terminology.r4", "5.4.0", mySingleVersionSpec)).isFalse();
	}

	@Test
	void addDependency_tracksHighestVersion() {
		PackageDependencyRegistry registry = new PackageDependencyRegistry();
		registry.addDependency("hl7.terminology.r4", "5.4.0");
		registry.addDependency("hl7.terminology.r4", "6.2.0");

		assertThat(registry.isRedundant("hl7.terminology.r4", "5.4.0", mySingleVersionSpec)).isTrue();
		assertThat(registry.isRedundant("hl7.terminology.r4", "6.2.0", mySingleVersionSpec)).isTrue();
	}

	@Test
	void isRedundant_nullVersion_treatedAsSameVersion() {
		PackageDependencyRegistry registry = new PackageDependencyRegistry();
		registry.addDependency("my.package", null);

		assertThat(registry.isRedundant("my.package", null, mySingleVersionSpec)).isTrue();
		assertThat(registry.isRedundant("my.package", null, myMultiVersionSpec)).isTrue();
	}

	@Test
	void addDependency_doesNotDowngradeTrackedVersion() {
		PackageDependencyRegistry registry = new PackageDependencyRegistry();
		registry.addDependency("hl7.terminology.r4", "6.2.0");
		registry.addDependency("hl7.terminology.r4", "5.4.0");

		assertThat(registry.isRedundant("hl7.terminology.r4", "5.4.0", mySingleVersionSpec)).isTrue();
		assertThat(registry.isRedundant("hl7.terminology.r4", "6.2.0", mySingleVersionSpec)).isTrue();
	}
}
