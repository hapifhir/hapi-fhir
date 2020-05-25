package ca.uhn.fhir.jpa.packages;

import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.dao.dstu3.BaseJpaDstu3Test;
import org.hl7.fhir.utilities.cache.PackageCacheManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.fail;

public class IgInstallerTestDstu3 extends BaseJpaDstu3Test {

	@Autowired
	private DaoConfig daoConfig;
	@Autowired
	private IgInstallerSvc igInstaller;

	@BeforeEach
	public void before() throws IOException {
		PackageCacheManager packageCacheManager = new PackageCacheManager(true, 1);
		InputStream stream;
		stream = IgInstallerTestDstu3.class.getResourceAsStream("erroneous-ig.tar.gz");
		packageCacheManager.addPackageToCache("erroneous-ig", "1.0.0", stream, "erroneous-ig");
		stream = IgInstallerTestDstu3.class.getResourceAsStream("NHSD.Assets.STU3.tar.gz");
		packageCacheManager.addPackageToCache("NHSD.Assets.STU3", "1.0.0", stream, "NHSD.Assets.STU3");
		stream = IgInstallerTestDstu3.class.getResourceAsStream("basisprofil.de.tar.gz");
		packageCacheManager.addPackageToCache("basisprofil.de", "0.2.40", stream, "basisprofil.de");
	}

	@Test
	public void negativeTestInstallFromCache() {
		// Unknown base of StructureDefinitions
		try {
			igInstaller.install("erroneous-ig", "1.0.0");
			fail();
		} catch (ImplementationGuideInstallationException e) {
			// good
		}
	}

	@Test
	public void installFromCache() {
		daoConfig.setAllowExternalReferences(true);
		igInstaller.install("NHSD.Assets.STU3", "1.2.0");
	}

	@Test
	public void installFromCache2() {
		igInstaller.install("basisprofil.de", "0.2.40");
	}
}
