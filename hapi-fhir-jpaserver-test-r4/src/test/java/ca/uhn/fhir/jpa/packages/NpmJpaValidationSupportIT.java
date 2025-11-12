package ca.uhn.fhir.jpa.packages;

import ca.uhn.fhir.implementationguide.ImplementationGuideCreator;
import ca.uhn.fhir.jpa.api.svc.IPackageInstallerSvc;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.test.util.LogbackTestExtension;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.Questionnaire;
import org.hl7.fhir.r4.model.SearchParameter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
	public void loadIGAndFetchSpecificResource(@TempDir Path theTempDirPath) throws IOException {
		// create an IG
		ImplementationGuideCreator igCreator = new ImplementationGuideCreator(myFhirContext);
		igCreator.setDirectory(theTempDirPath);

		// create some resources
		String spUrl = "http://localhost/ig-test-dir/sp";
		String qUrl = "http://localhost/ig-test-dir/q";
		for (int i = 0; i < 2; i++) {
			SearchParameter sp = new SearchParameter();
			sp.setUrl(spUrl + i);
			sp.setCode("helloWorld" + i);
			sp.setName(sp.getCode());
			sp.setDescription("description");
			sp.setType(Enumerations.SearchParamType.STRING);
			sp.addBase("Patient");
			sp.setExpression("Patient.name.given");
			sp.setStatus(Enumerations.PublicationStatus.ACTIVE);
			igCreator.addResourceToIG(sp.getName(), sp);

			Questionnaire questionnaire = new Questionnaire();
			questionnaire.setUrl(qUrl + i);
			questionnaire.setStatus(Enumerations.PublicationStatus.ACTIVE);
			questionnaire.setName("questionnaire" + i);
			igCreator.addResourceToIG(questionnaire.getName(), questionnaire);

			// others?
		}

		PackageInstallationSpec installationSpec = createAndInstallPackageSpec(igCreator, PackageInstallationSpec.InstallModeEnum.STORE_ONLY);

		IBaseResource sp = mySvc.fetchResource("SearchParameter", spUrl + "1");
		assertNotNull(sp);
		assertTrue(sp instanceof SearchParameter);
		assertEquals(spUrl + "1", ((SearchParameter)sp).getUrl());
		IBaseResource q = mySvc.fetchResource("Questionnaire", qUrl + "1");
		assertNotNull(q);
		assertTrue(q instanceof Questionnaire);
		assertEquals(qUrl + "1", ((Questionnaire)q).getUrl());
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
}
