package ca.uhn.fhir.jpa.packages;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.util.ClasspathUtil;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.utilities.npm.NpmPackage;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Set;

import static ca.uhn.fhir.jpa.packages.AdditionalResourcesParser.bundleAdditionalResources;
import static ca.uhn.fhir.jpa.packages.AdditionalResourcesParser.getAdditionalResources;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AdditionalResourcesParserTest {

	@Test
	public void testBundleAdditionalResourcesSingleFolder() throws IOException {

		// Arrange
		var packageAsBytes = ClasspathUtil.loadResourceAsStream("/cqf-ccc.tgz").readAllBytes();
		var packageInstallationSpec = new PackageInstallationSpec().setPackageContents(packageAsBytes).setName("fhir.cqf.ccc").setVersion("0.1.0");

		// Act
		var bundle = (Bundle) bundleAdditionalResources(Set.of("tests/"), packageInstallationSpec, FhirContext.forR4Cached());

		// Assert
		assertEquals(2, bundle.getEntry().size());
	}

	@Test
	public void testBundleAdditionalResourcesMultipleFolders() throws IOException {

		// Arrange
		var packageAsBytes = ClasspathUtil.loadResourceAsStream("/cqf-ccc.tgz").readAllBytes();
		var packageInstallationSpec = new PackageInstallationSpec().setPackageContents(packageAsBytes).setName("fhir.cqf.ccc").setVersion("0.1.0");

		// Act
		var bundle = (Bundle) bundleAdditionalResources(Set.of("tests/", "package"), packageInstallationSpec, FhirContext.forR4Cached());

		// Assert
		assertEquals(14, bundle.getEntry().size());
	}

	@Test
	public void testBundleAdditionalResourcesUnknownFolder() throws IOException {

		// Arrange
		var packageAsBytes = ClasspathUtil.loadResourceAsStream("/cqf-ccc.tgz").readAllBytes();
		var packageInstallationSpec = new PackageInstallationSpec().setPackageContents(packageAsBytes).setName("fhir.cqf.ccc").setVersion("0.1.0");

		// Act
		var bundle = (Bundle) bundleAdditionalResources(Set.of("testsUnknown/", "testsUnknown"), packageInstallationSpec, FhirContext.forR4Cached());

		// Assert
		assertEquals(0, bundle.getEntry().size());
	}

	@Test
	public void testGetAdditionalResources() throws IOException {
		// Arrange
		var npmPackage = NpmPackage.fromPackage(ClasspathUtil.loadResourceAsStream("/cqf-ccc.tgz"));

		// Act
		var resourceList = getAdditionalResources(Set.of("tests/"), npmPackage, FhirContext.forR4Cached());

		// Assert
		assertEquals(2, resourceList.size());
	}
}
