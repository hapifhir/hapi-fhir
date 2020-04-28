package ca.uhn.fhir.jpa.packages;

import ca.uhn.fhir.jpa.dao.dstu3.BaseJpaDstu3Test;
import org.hl7.fhir.dstu3.model.StructureDefinition;
import org.hl7.fhir.utilities.cache.NpmPackage;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.Collection;

import static org.junit.Assert.*;

public class IgInstallerForDstu3Test extends BaseJpaDstu3Test {

	@Autowired
	private IgInstallerForDstu3 igInstaller;

	@Test
	public void TestLoadIgFromUrl() throws IOException {
		NpmPackage ig = igInstaller.loadIg("https://docs.ehealth.sundhed.dk/latest/ig/package.tgz");
	}

	@Test
	public void TestLoadIgWithDependentPackagesFromUrl() throws IOException {
		NpmPackage ig = igInstaller.loadIg("https://build.fhir.org/ig/hl7dk/dk-medcom/package.tgz");
	}

	@Test
	public void TestFetchDependenciesForIG() throws IOException {
		NpmPackage ig = igInstaller.loadIg("https://build.fhir.org/ig/hl7dk/dk-medcom/package.tgz");
		igInstaller.fetchAndInstallDependencies(ig);
	}

	@Test
	public void TestGetStructureDefinitionsFromPackage() throws IOException {
		NpmPackage ig = igInstaller.loadIg("https://docs.ehealth.sundhed.dk/latest/ig/package.tgz");
		Collection<StructureDefinition> sds = igInstaller.fetchResourcesOfType(StructureDefinition.class, ig);
		assertFalse(sds.isEmpty());
	}

	@Test
	public void TestGenerateSnapshots() throws IOException {
		NpmPackage ig = igInstaller.loadIg("dk.ehealth.sundhed.fhir.ig.core", "dev");
		igInstaller.generateSnapshots(ig);
	}

	@Test
	public void TestInstallPackage() throws IOException {
		NpmPackage ig = igInstaller.loadIg("https://docs.ehealth.sundhed.dk/latest/ig/package.tgz");
		igInstaller.install(ig);
	}

	@Test
	public void TestLoadIGFromSimplifier() throws IOException {
		NpmPackage ig = igInstaller.loadIg("nictiz.fhir.nl.stu3.eoverdracht", "0.1.0-beta1");
		igInstaller.install(ig);
	}
}
