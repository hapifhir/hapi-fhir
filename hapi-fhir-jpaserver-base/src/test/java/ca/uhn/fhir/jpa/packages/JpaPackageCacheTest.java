package ca.uhn.fhir.jpa.packages;

import ca.uhn.fhir.jpa.dao.r4.BaseJpaR4Test;
import org.hl7.fhir.utilities.cache.IPackageCacheManager;
import org.hl7.fhir.utilities.cache.NpmPackage;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.*;

public class JpaPackageCacheTest extends BaseJpaR4Test {

	@Autowired
	private IPackageCacheManager myPackageCacheManager;

	@Test
	public void testSavePackage() throws IOException {
		try (InputStream stream = IgInstallerTestDstu3.class.getResourceAsStream("/ca/uhn/fhir/jpa/packages/basisprofil.de.tar.gz")) {
			myPackageCacheManager.addPackageToCache("basisprofil.de", "0.2.40", stream, "basisprofil.de");
		}

		NpmPackage pkg;

		pkg = myPackageCacheManager.loadPackage("basisprofil.de", null);
		assertEquals("0.2.40", pkg.version());

		pkg = myPackageCacheManager.loadPackage("basisprofil.de", "0.2.40");
		assertEquals("0.2.40", pkg.version());

		pkg = myPackageCacheManager.loadPackage("basisprofil.de", "99");
		assertEquals(null, pkg);
	}


	@Test
	public void testSavePackageCorrectFhirVersion() {

	}

}
