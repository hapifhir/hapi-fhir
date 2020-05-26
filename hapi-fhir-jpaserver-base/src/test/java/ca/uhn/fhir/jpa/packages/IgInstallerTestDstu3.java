package ca.uhn.fhir.jpa.packages;

import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.dao.dstu3.BaseJpaDstu3Test;
import org.hl7.fhir.utilities.cache.IPackageCacheManager;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class IgInstallerTestDstu3 extends BaseJpaDstu3Test {

	@Autowired
	private DaoConfig daoConfig;
	@Autowired
	private NpmInstallerSvc igInstaller;
	@Autowired
	private IPackageCacheManager myPackageCacheManager;

	@Before
	public void before() throws IOException {
		InputStream stream;

		stream = IgInstallerTestDstu3.class.getResourceAsStream("/packages/erroneous-ig.tar.gz");
		myPackageCacheManager.addPackageToCache("erroneous-ig", "1.0.0", stream, "erroneous-ig");

		stream = IgInstallerTestDstu3.class.getResourceAsStream("/packages/NHSD.Assets.STU3.tar.gz");
		myPackageCacheManager.addPackageToCache("NHSD.Assets.STU3", "1.0.0", stream, "NHSD.Assets.STU3");

		stream = IgInstallerTestDstu3.class.getResourceAsStream("/packages/basisprofil.de.tar.gz");
		myPackageCacheManager.addPackageToCache("basisprofil.de", "0.2.40", stream, "basisprofil.de");
	}

	@Test
	public void negativeTestInstallFromCache() {
		// Unknown base of StructureDefinitions
		try {
			igInstaller.install(new NpmInstallationSpec().setPackageId("erroneous-ig").setPackageVersion("1.0.0").setInstallMode(NpmInstallationSpec.InstallModeEnum.CACHE_AND_INSTALL));
			fail();
		} catch (ImplementationGuideInstallationException e) {
			assertEquals("Error installing IG erroneous-ig#1.0.2: ca.uhn.fhir.jpa.packages.ImplementationGuideInstallationException: Failure when generating snapshot of StructureDefinition: StructureDefinition/ff1cb7e5-041d-425b-9410-3b538445bce9/_history/1", e.getMessage());
		}
	}

	@Test
	public void installFromCache() {
		daoConfig.setAllowExternalReferences(true);
		igInstaller.install(new NpmInstallationSpec().setPackageId("NHSD.Assets.STU3").setPackageVersion("1.0.0").setInstallMode(NpmInstallationSpec.InstallModeEnum.CACHE_AND_INSTALL));
	}

	@Test
	public void installFromCache2() {
		igInstaller.install(new NpmInstallationSpec().setPackageId("basisprofil.de").setPackageVersion("0.2.40").setInstallMode(NpmInstallationSpec.InstallModeEnum.CACHE_AND_INSTALL));
	}
}
