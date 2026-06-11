package ca.uhn.fhir.jpa.packages;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.implementationguide.NpmPackageFactory;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Device;
import org.hl7.fhir.r4.model.Measure;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.utilities.npm.NpmPackage;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static ca.uhn.fhir.jpa.packages.AdditionalResourcesParser.bundleAdditionalResources;
import static ca.uhn.fhir.jpa.packages.AdditionalResourcesParser.getAdditionalResources;
import static org.assertj.core.api.Assertions.assertThat;

// Created by Claude Opus 4.6
class AdditionalResourcesParserTest {

	private static final FhirContext ourFhirContext = FhirContext.forR4Cached();

	private static NpmPackageFactory newPackageFactory() {
		return new NpmPackageFactory(ourFhirContext)
				.name("fhir.cqf.ccc")
				.version("0.1.0")
				.addResource("Patient-patient-1", new Patient().setId("patient-1"))
				.addResource("Observation-obs-1", new Observation().setId("obs-1"))
				.addResource("Device-device-1", new Device().setId("device-1"))
				.addResource("Measure-measure-1", new Measure().setId("measure-1"))
				.addResourceToFolder("tests", "Patient-test-patient-1", new Patient().setId("test-patient-1"))
				.addResourceToFolder("tests", "Observation-test-obs-1", new Observation().setId("test-obs-1"))
				.addResourceToFolder("tests", "Device-test-device-1", new Device().setId("test-device-1"));
	}

	@Test
	void testGetAdditionalResources_singleFolder_returnsResources() {
		NpmPackage npmPackage = newPackageFactory().createPackage();

		List<IBaseResource> resources = getAdditionalResources(Set.of("tests"), npmPackage, ourFhirContext);

		assertThat(resources).hasSize(3);
	}

	@Test
	void testGetAdditionalResources_multipleFolders_returnsCombinedResources() {
		NpmPackage npmPackage = newPackageFactory().createPackage();

		List<IBaseResource> resources = getAdditionalResources(Set.of("tests", "package"), npmPackage, ourFhirContext);

		assertThat(resources).hasSize(7);
	}

	@Test
	void testGetAdditionalResources_unknownFolder_returnsEmptyAndLogsWarning() {
		NpmPackage npmPackage = newPackageFactory().createPackage();

		Logger logger = (Logger) LoggerFactory.getLogger(AdditionalResourcesParser.class);
		ListAppender<ILoggingEvent> listAppender = new ListAppender<>();
		listAppender.start();
		logger.addAppender(listAppender);
		try {
			List<IBaseResource> resources = getAdditionalResources(Set.of("nonexistent"), npmPackage, ourFhirContext);

			assertThat(resources).isEmpty();
			assertThat(listAppender.list)
					.extracting(ILoggingEvent::getFormattedMessage)
					.anyMatch(msg -> msg.contains("nonexistent"));
			assertThat(listAppender.list)
					.extracting(ILoggingEvent::getLevel)
					.contains(Level.WARN);
		} finally {
			logger.detachAppender(listAppender);
		}
	}

	@Test
	void testGetAdditionalResources_emptyFolderSet_returnsEmpty() {
		NpmPackage npmPackage = newPackageFactory().createPackage();

		List<IBaseResource> resources = getAdditionalResources(Collections.emptySet(), npmPackage, ourFhirContext);

		assertThat(resources).isEmpty();
	}

	@Test
	void testGetAdditionalResources_mixOfKnownAndUnknownFolders_returnsOnlyKnown() {
		NpmPackage npmPackage = newPackageFactory().createPackage();

		List<IBaseResource> resources = getAdditionalResources(Set.of("tests", "nonexistent"), npmPackage, ourFhirContext);

		assertThat(resources).hasSize(3);
	}

	@Test
	void testBundleAdditionalResources_singleFolder_returnsBundleWithEntries() throws IOException {
		var packageAsBytes = newPackageFactory().createPackageBytes();
		var spec = new PackageInstallationSpec()
				.setPackageContents(packageAsBytes)
				.setName("fhir.cqf.ccc")
				.setVersion("0.1.0");

		var bundle = (Bundle) bundleAdditionalResources(Set.of("tests"), spec, ourFhirContext);

		assertThat(bundle.getEntry()).hasSize(3);
	}

	@Test
	void testBundleAdditionalResources_unknownFolder_returnsEmptyBundle() throws IOException {
		var packageAsBytes = newPackageFactory().createPackageBytes();
		var spec = new PackageInstallationSpec()
				.setPackageContents(packageAsBytes)
				.setName("fhir.cqf.ccc")
				.setVersion("0.1.0");

		var bundle = (Bundle) bundleAdditionalResources(Set.of("nonexistent"), spec, ourFhirContext);

		assertThat(bundle.getEntry()).isEmpty();
	}
}
