package ca.uhn.fhir.jpa.packages;

import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.dao.r4.BaseJpaR4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

import static org.junit.Assert.assertTrue;

public class IgInstallerTestR4 extends BaseJpaR4Test {

	@Autowired
	public DaoConfig daoConfig;
	@Autowired
	public IgInstaller igInstaller;

	@Test
	public void installIgForR4FromUrl() throws IOException {
		assertTrue(igInstaller.install("https://build.fhir.org/ig/hl7dk/dk-medcom/package.tgz"));
	}

	@Test
	public void installIgForR4FromSimplifier() {
		assertTrue(igInstaller.install("de.basisprofil.r4", "0.9.5"));
	}
}
