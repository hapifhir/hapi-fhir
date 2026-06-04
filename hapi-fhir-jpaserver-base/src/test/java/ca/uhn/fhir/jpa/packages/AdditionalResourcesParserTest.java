package ca.uhn.fhir.jpa.packages;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.util.ClasspathUtil;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.utilities.npm.NpmPackage;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static ca.uhn.fhir.jpa.packages.AdditionalResourcesParser.bundleAdditionalResources;
import static ca.uhn.fhir.jpa.packages.AdditionalResourcesParser.getAdditionalResources;
import static org.assertj.core.api.Assertions.assertThat;

// Created by Claude Opus 4.6
class AdditionalResourcesParserTest {

	private static final FhirContext ourFhirContext = FhirContext.forR4Cached();
	private static final String TEST_PACKAGE_PATH = "/cqf-ccc.tgz";

	private static NpmPackage loadTestPackage() throws IOException {
		try (InputStream is = ClasspathUtil.loadResourceAsStream(TEST_PACKAGE_PATH)) {
			return NpmPackage.fromPackage(is);
		}
	}

	private static byte[] loadTestPackageBytes() throws IOException {
		try (InputStream is = ClasspathUtil.loadResourceAsStream(TEST_PACKAGE_PATH)) {
			return is.readAllBytes();
		}
	}

	@Test
	void testGetAdditionalResources_singleFolder_returnsResources() throws IOException {
		NpmPackage npmPackage = loadTestPackage();

		List<IBaseResource> resources = getAdditionalResources(Set.of("tests"), npmPackage, ourFhirContext);

		assertThat(resources).hasSize(3);
	}

	@Test
	void testGetAdditionalResources_multipleFolders_returnsCombinedResources() throws IOException {
		NpmPackage npmPackage = loadTestPackage();

		List<IBaseResource> resources = getAdditionalResources(Set.of("tests", "package"), npmPackage, ourFhirContext);

		assertThat(resources).hasSize(7);
	}

	@Test
	void testGetAdditionalResources_unknownFolder_returnsEmptyAndLogsWarning() throws IOException {
		NpmPackage npmPackage = loadTestPackage();

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
	void testGetAdditionalResources_emptyFolderSet_returnsEmpty() throws IOException {
		NpmPackage npmPackage = loadTestPackage();

		List<IBaseResource> resources = getAdditionalResources(Collections.emptySet(), npmPackage, ourFhirContext);

		assertThat(resources).isEmpty();
	}

	@Test
	void testGetAdditionalResources_mixOfKnownAndUnknownFolders_returnsOnlyKnown() throws IOException {
		NpmPackage npmPackage = loadTestPackage();

		List<IBaseResource> resources = getAdditionalResources(Set.of("tests", "nonexistent"), npmPackage, ourFhirContext);

		assertThat(resources).hasSize(3);
	}

	@Test
	void testBundleAdditionalResources_singleFolder_returnsBundleWithEntries() throws IOException {
		var packageAsBytes = loadTestPackageBytes();
		var spec = new PackageInstallationSpec()
				.setPackageContents(packageAsBytes)
				.setName("fhir.cqf.ccc")
				.setVersion("0.1.0");

		var bundle = (Bundle) bundleAdditionalResources(Set.of("tests"), spec, ourFhirContext);

		assertThat(bundle.getEntry()).hasSize(3);
	}

	@Test
	void testBundleAdditionalResources_unknownFolder_returnsEmptyBundle() throws IOException {
		var packageAsBytes = loadTestPackageBytes();
		var spec = new PackageInstallationSpec()
				.setPackageContents(packageAsBytes)
				.setName("fhir.cqf.ccc")
				.setVersion("0.1.0");

		var bundle = (Bundle) bundleAdditionalResources(Set.of("nonexistent"), spec, ourFhirContext);

		assertThat(bundle.getEntry()).isEmpty();
	}
}
