package ca.uhn.fhir.jpa.packages;

import ca.uhn.fhir.jpa.dao.r4.BaseJpaR4Test;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import org.hl7.fhir.utilities.npm.IPackageCacheManager;
import org.hl7.fhir.utilities.npm.NpmPackage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class JpaPackageCacheTest extends BaseJpaR4Test {

	@Autowired
	private IPackageCacheManager myPackageCacheManager;

	@Test
	public void testSavePackage() throws IOException {
		try (InputStream stream = IgInstallerDstu3Test.class.getResourceAsStream("/packages/basisprofil.de.tar.gz")) {
			myPackageCacheManager.addPackageToCache("basisprofil.de", "0.2.40", stream, "basisprofil.de");
		}

		NpmPackage pkg;

		pkg = myPackageCacheManager.loadPackage("basisprofil.de", null);
		assertEquals("0.2.40", pkg.version());

		pkg = myPackageCacheManager.loadPackage("basisprofil.de", "0.2.40");
		assertEquals("0.2.40", pkg.version());

		try {
			myPackageCacheManager.loadPackage("basisprofil.de", "99");
			fail();
		} catch (ResourceNotFoundException e) {
			assertEquals("Unable to locate package basisprofil.de#99", e.getMessage());
		}
	}


	@Test
	public void testSavePackageCorrectFhirVersion() {

	}

}
