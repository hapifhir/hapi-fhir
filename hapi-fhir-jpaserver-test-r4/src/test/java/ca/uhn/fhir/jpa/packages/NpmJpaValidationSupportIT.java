package ca.uhn.fhir.jpa.packages;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.implementationguide.ImplementationGuideCreator;
import ca.uhn.fhir.jpa.packages.util.PackageUtils;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.test.util.LogbackTestExtension;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.SearchParameter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class NpmJpaValidationSupportIT extends BaseJpaR4Test {
	@Autowired
	private NpmJpaValidationSupport mySvc;

	@Autowired
	private IPackageInstallerSvc myPackageInstallerSvc;

	@RegisterExtension
	public final LogbackTestExtension myLogbackTestExtension = new LogbackTestExtension(NpmJpaValidationSupport.class);

	@Test
	public void fetchAllSearchParameters_withStoredIG_returnsSPsFromIG() throws IOException {
		String name = "ig-test-dir";
		Path tempDirPath = createTempDir(name);

		ImplementationGuideCreator igCreator = new ImplementationGuideCreator(myFhirContext);
		igCreator.setDirectory(tempDirPath);

		SearchParameter generated = generateSP();
		igCreator.addSPToIG(generated);

		PackageInstallationSpec spec = createAndInstallPackageSpec(igCreator, PackageInstallationSpec.InstallModeEnum.STORE_ONLY);

		// test
		List<IBaseResource> sps = mySvc.fetchAllSearchParameters();

		// verify
		assertNotNull(sps);
		assertEquals(1, sps.size());
		IBaseResource r = sps.get(0);
		assertTrue(r instanceof SearchParameter);
		SearchParameter sp = (SearchParameter)r;
		assertEquals(generated.getName(), sp.getName());
		assertEquals(generated.getUrl(), sp.getUrl());
		NpmPackageMetadataLiteJson metadata = PackageUtils.getPackageMetadata(sp);
		assertNotNull(metadata);
		assertEquals(spec.getName(), metadata.getName());
		assertEquals(spec.getVersion(), metadata.getVersion());
	}

	@Test
	public void fetchAllSearchParameters_multipleVersions_returnVersionsHigherToLower() throws IOException {
		String name = "ig-test-dir";
		// deliberately not in order to ensure the loading order
		// is always in order
		String[] versions = new String[] { "1.1.1", "3.1.2", "2.2.2" };

		List<String> orderedVersions = new ArrayList<>();
		SearchParameter generated = generateSP();
		for (String version : versions) {
			Path tempDirPath = createTempDir(name + version);
			orderedVersions.add(version);

			ImplementationGuideCreator igCreator = new ImplementationGuideCreator(myFhirContext, name, version);
			igCreator.setDirectory(tempDirPath);
			generated.setDescription("My SP for " + version);
			igCreator.addSPToIG(generated);

			PackageInstallationSpec spec = createAndInstallPackageSpec(igCreator, PackageInstallationSpec.InstallModeEnum.STORE_ONLY);
		}
		orderedVersions.sort(String::compareTo);

		// test
		List<IBaseResource> sps = mySvc.fetchAllSearchParameters();

		// verify
		assertNotNull(sps);
		assertEquals(orderedVersions.size(), sps.size());
		for (int i = 0; i < sps.size(); i++) {
			SearchParameter sp = (SearchParameter) sps.get(i);
			String version = orderedVersions.get(i);

			assertTrue(sp.getDescription()
				.contains(version),
				String.format("Expected %s but found %s", version, sp.getDescription()));
		}
	}

	@Test
	public void fetchAllSearchParameters_multipleVersion_returnsAllSPs() throws IOException {
		String name = "ig-test-dir";
		String[] versions = new String[] { "1.1.1", "1.2.2" };

		SearchParameter generated = generateSP();
		for (String version : versions) {
			Path tempDirPath = createTempDir(name + version);

			ImplementationGuideCreator igCreator = new ImplementationGuideCreator(myFhirContext, name, version);
			igCreator.setDirectory(tempDirPath);
			igCreator.addSPToIG(generated);

			PackageInstallationSpec spec = createAndInstallPackageSpec(igCreator, PackageInstallationSpec.InstallModeEnum.STORE_ONLY);
		}

		// test
		List<IBaseResource> sps = mySvc.fetchAllSearchParameters();

		// verify
		assertNotNull(sps);
		assertEquals(2, sps.size());

		Map<String, NpmPackageMetadataLiteJson> map = new HashMap<>();
		for (IBaseResource r : sps) {
			assertTrue(r instanceof SearchParameter);
			SearchParameter sp = (SearchParameter) r;

			assertEquals(generated.getUrl(), sp.getUrl());
			assertEquals(generated.getName(), sp.getName());
			NpmPackageMetadataLiteJson metadataLiteJson = PackageUtils.getPackageMetadata(sp);
			assertNotNull(metadataLiteJson);

			map.put(metadataLiteJson.getVersion(), metadataLiteJson);
		}
		assertEquals(versions.length, map.size());
		for (String version : versions) {
			assertTrue(map.containsKey(version));
			NpmPackageMetadataLiteJson metadataLiteJson = map.get(version);
			assertEquals(name, metadataLiteJson.getName());
		}
	}

	@Test
	public void fetchAllSearchParameters_multipleFhirVersions_returnsOnlySPsMatchingFhirVersion() throws IOException {
		FhirContext dstu3 = FhirContext.forDstu3();
		Path path1 = createTempDir("pkg1");
		Path path2 = createTempDir("pkg2");

		ImplementationGuideCreator igFhir4 = new ImplementationGuideCreator(myFhirContext, "r4.test.ig", "1.1.1");
		igFhir4.setDirectory(path1);
		ImplementationGuideCreator igFhir3dstu = new ImplementationGuideCreator(dstu3, "dstu3.test.ig", "1.1.1");
		igFhir3dstu.setDirectory(path2);

		{
			SearchParameter spr4 = new SearchParameter();
			spr4.setStatus(Enumerations.PublicationStatus.ACTIVE);
			spr4.setType(Enumerations.SearchParamType.STRING);
			spr4.setExpression("Patient.name.given");
			spr4.setName("helloWorld");
			spr4.setCode("helloWorld");
			spr4.setDescription("description");
			spr4.addBase("Patient");
			spr4.setUrl("http://localhost/ig-test-dir/spr4");
			igFhir4.addSPToIG(spr4);
		}
		{
			org.hl7.fhir.dstu3.model.SearchParameter spdstu3 = new org.hl7.fhir.dstu3.model.SearchParameter();
			spdstu3.setStatus(org.hl7.fhir.dstu3.model.Enumerations.PublicationStatus.ACTIVE);
			spdstu3.setType(org.hl7.fhir.dstu3.model.Enumerations.SearchParamType.STRING);
			spdstu3.setExpression("Patient.name.given");
			spdstu3.setName("helloWorld");
			spdstu3.setCode("helloWorld");
			spdstu3.setDescription("description");
			spdstu3.addBase("Patient");
			spdstu3.setUrl("http://localhost/ig-test-dir/spdstu3");
			igFhir3dstu.addSPToIG(spdstu3);
		}

		createAndInstallPackageSpec(igFhir4, PackageInstallationSpec.InstallModeEnum.STORE_ONLY);
		createAndInstallPackageSpec(igFhir3dstu, PackageInstallationSpec.InstallModeEnum.STORE_ONLY);

		// test
		List<IBaseResource> sps = mySvc.fetchAllSearchParameters();

		// verify
		assertNotNull(sps);
		assertEquals(1, sps.size());
		assertTrue(sps.stream()
			.anyMatch(sp -> sp instanceof SearchParameter));
		assertFalse(sps.stream()
			.anyMatch(sp -> sp instanceof org.hl7.fhir.dstu3.model.SearchParameter));

		assertTrue(myLogbackTestExtension.getLogEvents()
			.stream().anyMatch(e -> e.getFormattedMessage().contains("Encountered an NPM package with an incompatible fhir version")));
	}

	private SearchParameter generateSP() {
		SearchParameter sp = new SearchParameter();
		sp.setUrl("http://localhost/ig-test-dir/sp1");
		sp.setCode("helloWorld");
		sp.setName(sp.getCode());
		sp.setDescription("description");
		sp.setType(Enumerations.SearchParamType.STRING);
		sp.addBase("Patient");
		sp.setExpression("Patient.name.given");
		sp.setStatus(Enumerations.PublicationStatus.ACTIVE);
		return sp;
	}

	private PackageInstallationSpec createAndInstallPackageSpec(ImplementationGuideCreator theIgCreator,
																PackageInstallationSpec.InstallModeEnum theInstallModeEnum) throws IOException {
		// create a source directory
		Path outputFileName = theIgCreator.createTestIG();

		// add some NPM package
		PackageInstallationSpec spec = new PackageInstallationSpec()
			.setName(theIgCreator.getPackageName())
			.setVersion(theIgCreator.getPackageVersion())
			.setInstallMode(theInstallModeEnum)
			.setPackageContents(Files.readAllBytes(outputFileName))
			;
		PackageInstallOutcomeJson outcome = myPackageInstallerSvc.install(spec);

		assertNotNull(outcome);
		assertTrue(outcome.getMessage()
			.stream().anyMatch(m -> m.contains("Successfully added package")),
			String.join(", ", outcome.getMessage()));
		return spec;
	}

	private Path createTempDir(String theName) throws IOException {
		Path tempDirPath = Files.createTempDirectory(theName);
		File tempFile = new File(tempDirPath.toString());
		tempFile.deleteOnExit();
		return tempDirPath;
	}
}
