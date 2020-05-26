package ca.uhn.fhir.jpa.packages;

import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.dao.r4.BaseJpaR4Test;
import org.hl7.fhir.utilities.cache.IPackageCacheManager;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class IgInstallerTestR4 extends BaseJpaR4Test {

	@Autowired
	public DaoConfig daoConfig;
	@Autowired
	public IgInstallerSvc igInstaller;
	@Autowired
	private IPackageCacheManager myPackageCacheManager;

	@Before
	public void before() throws IOException {
		InputStream stream;
		stream = IgInstallerTestDstu3.class.getResourceAsStream("NHSD.Assets.STU3.tar.gz");
		myPackageCacheManager.addPackageToCache("NHSD.Assets.STU3", "1.0.0", stream, "NHSD.Assets.STU3");
	}

	@Test
	public void negativeTestInstallFromCacheVersionMismatch() {
		daoConfig.setAllowExternalReferences(true);
		try {
			igInstaller.install("NHSD.Assets.STU3", "1.2.0");
			fail();
		} catch (ImplementationGuideInstallationException e) {
			assertEquals("Could not load NPM package NHSD.Assets.STU3#1.2.0", e.getMessage());
		}
	}
}
