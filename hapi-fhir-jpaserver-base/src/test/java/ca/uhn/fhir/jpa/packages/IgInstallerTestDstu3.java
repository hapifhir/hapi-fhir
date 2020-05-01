package ca.uhn.fhir.jpa.packages;

import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.dao.dstu3.BaseJpaDstu3Test;
import org.hl7.fhir.dstu3.model.StructureDefinition;
import org.hl7.fhir.utilities.cache.NpmPackage;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.Collection;

import static org.junit.Assert.*;

public class IgInstallerTestDstu3 extends BaseJpaDstu3Test {

	@Autowired
	private DaoConfig daoConfig;
	@Autowired
	private IgInstaller igInstaller;

	@Before
	public void before() {
		daoConfig.addTreatReferencesAsLogical("http://ehealth.sundhed.dk/vs/*");
		daoConfig.addTreatReferencesAsLogical("http://ehealth.sundhed.dk/cs/*");
	}

	@Test
	public void installIgForDstu3() throws IOException {
		assertTrue(igInstaller.install("https://docs.ehealth.sundhed.dk/latest/ig/package.tgz"));
	}
}
