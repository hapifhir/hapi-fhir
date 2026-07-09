package ca.uhn.fhir.jpa.packages.loader;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.packages.NpmPackageFactory;
import ca.uhn.test.util.LogbackTestExtension;
import ca.uhn.test.util.LogbackTestExtensionAssert;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Device;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.utilities.npm.NpmPackage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

// Created by claude-sonnet-4-6
class PackageResourceParsingSvcTest {

	private static final FhirContext CTX = FhirContext.forR4Cached();
	private final PackageResourceParsingSvc mySvc = new PackageResourceParsingSvc(CTX);

	@RegisterExtension
	LogbackTestExtension myLogCapture =
			new LogbackTestExtension(LoggerFactory.getLogger(PackageResourceParsingSvc.class));

	private static NpmPackage buildPackage() {
		return new NpmPackageFactory(CTX)
				.name("test.pkg")
				.version("1.0.0")
				.addResource("Patient-p1", new Patient().setId("p1"))
				.addResource("Organization-o1", new Organization().setId("o1"))
				.addResourceToFolder("example", "Patient-ep1", new Patient().setId("ep1"))
				.addResourceToFolder("example", "Device-d1", new Device().setId("d1"))
				.createPackage();
	}

	@Test
	void parseResourcesFromFolder_existingFolder_returnsAllResourcesRegardlessOfType() {
		NpmPackage pkg = buildPackage();

		List<IBaseResource> resources = mySvc.parseResourcesFromFolder("example", pkg);

		assertThat(resources).hasSize(2)
			.extracting(IBase::fhirType).containsExactlyInAnyOrder("Patient", "Device");
	}

	@Test
	void parseResourcesFromFolder_missingFolder_returnsEmptyAndLogsWarning() {
		NpmPackage pkg = buildPackage();

		List<IBaseResource> resources = mySvc.parseResourcesFromFolder("nonexistent", pkg);

		assertThat(resources).isEmpty();
		LogbackTestExtensionAssert.assertThat(myLogCapture).hasWarnMessage("nonexistent");
	}

	@Test
	void parseResourcesFromFolder_defaultPackageFolder_returnsAllResources() {
		NpmPackage pkg = buildPackage();

		List<IBaseResource> resources = mySvc.parseResourcesFromFolder("package", pkg);

		assertThat(resources).hasSize(2)
			.extracting(IBase::fhirType).containsExactlyInAnyOrder("Patient", "Organization");
	}

	@Test
	void parseResourcesFromFolders_multipleFolders_returnsCombinedResources() {
		NpmPackage pkg = buildPackage();

		List<IBaseResource> resources = mySvc.parseResourcesFromFolders(Set.of("package", "example"), pkg);

		assertThat(resources).hasSize(4);
	}

	@Test
	void parseResourcesFromFolders_mixOfExistingAndMissing_returnsOnlyExisting() {
		NpmPackage pkg = buildPackage();

		List<IBaseResource> resources = mySvc.parseResourcesFromFolders(Set.of("example", "nonexistent"), pkg);

		assertThat(resources).hasSize(2);
		LogbackTestExtensionAssert.assertThat(myLogCapture).hasWarnMessage("nonexistent");
	}

	@Test
	void parseResourcesOfTypesFromFolder_variousTypeSelections_returnsOnlyMatchingResources() {
		NpmPackage pkg = buildPackage();

		List<IBaseResource> singleType = mySvc.parseResourcesOfTypesFromFolder(List.of("Patient"), "example", pkg);
		List<IBaseResource> multipleTypes =
				mySvc.parseResourcesOfTypesFromFolder(List.of("Patient", "Device"), "example", pkg);
		List<IBaseResource> nonExistentType =
				mySvc.parseResourcesOfTypesFromFolder(List.of("Observation"), "example", pkg);

		assertThat(singleType).hasSize(1);
		assertThat(singleType.get(0).fhirType()).isEqualTo("Patient");
		assertThat(multipleTypes).hasSize(2);
		assertThat(nonExistentType).isEmpty();
	}

	@Test
	void parseResourcesOfType_sameTypeInPackageAndExampleFolders_onlyReturnsResourcesFromRequestedFolder() {
		NpmPackage pkg = buildPackage();

		List<IBaseResource> packageResources = mySvc.parseResourcesOfType("Patient", "package", pkg);
		List<IBaseResource> exampleResources = mySvc.parseResourcesOfType("Patient", "example", pkg);
		List<IBaseResource> defaultFolderResources = mySvc.parseResourcesOfType("Patient", pkg);

		assertThat(packageResources).hasSize(1);
		assertThat(packageResources.get(0).getIdElement().getIdPart()).isEqualTo("p1");
		assertThat(exampleResources).hasSize(1);
		assertThat(exampleResources.get(0).getIdElement().getIdPart()).isEqualTo("ep1");
		assertThat(defaultFolderResources).as("2-arg overload defaults to the 'package' folder")
			.isEqualTo(packageResources);
	}

	@Test
	void parseResourcesOfType_absentFolderOrTypeNotInFolder_returnsEmpty() {
		NpmPackage pkg = buildPackage();

		List<IBaseResource> missingFolder = mySvc.parseResourcesOfType("Patient", "nonexistent", pkg);
		List<IBaseResource> typeNotInFolder = mySvc.parseResourcesOfType("Observation", "package", pkg);

		assertThat(missingFolder).isEmpty();
		assertThat(typeNotInFolder).isEmpty();
		LogbackTestExtensionAssert.assertThat(myLogCapture).isEmpty();
	}

	@Test
	void parseResourcesOfType_multiplePatientFilesWithNarrative_parsesAllAndSuppressesNarratives() {
		Patient p1 = new Patient();
		p1.setId("p1");
		p1.getText().setDivAsString("<div>some narrative</div>");
		NpmPackage pkg = new NpmPackageFactory(CTX)
				.name("test.pkg")
				.version("1.0.0")
				.addResource("Patient-p1", p1)
				.addResource("Patient-p2", new Patient().setId("p2"))
				.createPackage();

		List<IBaseResource> resources = mySvc.parseResourcesOfType("Patient", pkg);

		assertThat(resources).extracting(r -> r.getIdElement().getIdPart())
			.containsExactlyInAnyOrder("p1", "p2");
		assertThat(resources).extracting(r -> ((Patient) r).getText().isEmpty())
			.as("narratives are suppressed on every parsed resource")
			.containsOnly(true);
	}

	@Test
	void parseResourcesFromFolders_emptyFolderNames_returnsEmptyWithoutLogging() {
		NpmPackage pkg = buildPackage();

		List<IBaseResource> resources = mySvc.parseResourcesFromFolders(Set.of(), pkg);

		assertThat(resources).isEmpty();
		LogbackTestExtensionAssert.assertThat(myLogCapture).isEmpty();
	}
}
