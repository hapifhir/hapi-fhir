package ca.uhn.fhir.jpa.packages;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.support.IValidationSupport;
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
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class NpmJpaValidationSupportIT extends BaseJpaR4Test {
	@Autowired
	private NpmJpaValidationSupport mySvc;

	@Autowired
	private IPackageInstallerSvc myPackageInstallerSvc;

	@RegisterExtension
	public final LogbackTestExtension myLogbackTestExtension = new LogbackTestExtension(NpmJpaValidationSupport.class);

	@Test
	public void loadIGAndFetchSpecificResource_throughGenericIValidationsupport(@TempDir Path theTempDirPath) throws IOException {
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
		}

		PackageInstallationSpec installationSpec = createAndInstallPackageSpec(igCreator, PackageInstallationSpec.InstallModeEnum.STORE_ONLY);

		IValidationSupport support = mySvc;

		SearchParameter sp = support.fetchResource(SearchParameter.class, spUrl + "1");
		assertNotNull(sp);
		assertEquals(spUrl + "1", sp.getUrl());
		Questionnaire q = support.fetchResource(Questionnaire.class, qUrl + "1");
		assertNotNull(q);
		assertEquals(qUrl + "1", q.getUrl());
	}

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
		}

		PackageInstallationSpec installationSpec = createAndInstallPackageSpec(igCreator, PackageInstallationSpec.InstallModeEnum.STORE_ONLY);

		// test
		IBaseResource sp = mySvc.fetchResource("SearchParameter", spUrl + "1");

		// validation
		assertNotNull(sp);
		assertTrue(sp instanceof SearchParameter);
		assertEquals(spUrl + "1", ((SearchParameter)sp).getUrl());
		IBaseResource q = mySvc.fetchResource("Questionnaire", qUrl + "1");
		assertNotNull(q);
		assertTrue(q instanceof Questionnaire);
		assertEquals(qUrl + "1", ((Questionnaire)q).getUrl());
	}

	@Test
	public void loadIGAndFetchResource_usingWrongFhirVersion_throws(@TempDir Path theTempDirPath) throws IOException {
		// create an IG
		ImplementationGuideCreator igCreator = new ImplementationGuideCreator(myFhirContext);
		igCreator.setDirectory(theTempDirPath);

		String spUrl = "http://localhost/ig-test-dir/sp-1";
		{
			SearchParameter sp = new SearchParameter();
			sp.setUrl(spUrl);
			sp.setCode("helloWorld");
			sp.setName(sp.getCode());
			sp.setDescription("description");
			sp.setType(Enumerations.SearchParamType.STRING);
			sp.addBase("Patient");
			sp.setExpression("Patient.name.given");
			sp.setStatus(Enumerations.PublicationStatus.ACTIVE);
			igCreator.addResourceToIG(sp.getName(), sp);
		}

		PackageInstallationSpec installationSpec = createAndInstallPackageSpec(igCreator, PackageInstallationSpec.InstallModeEnum.STORE_ONLY);

		IValidationSupport validationSupport = mySvc;

		// test
		try {
			ca.uhn.fhir.model.dstu2.resource.SearchParameter sp = validationSupport.fetchResource(ca.uhn.fhir.model.dstu2.resource.SearchParameter.class, spUrl);
			fail();
		} catch (ConfigurationException ex) {
			assertTrue(ex.getLocalizedMessage().contains("This context is for FHIR version \"R4\""));
		}
	}

	@Test
	public void loadIGAndFetchResource_wrongUrl_returnsNull(@TempDir Path theTempDirPath) throws IOException {
		// create an IG
		ImplementationGuideCreator igCreator = new ImplementationGuideCreator(myFhirContext);
		igCreator.setDirectory(theTempDirPath);

		String spUrl = "http://localhost/ig-test-dir/sp-1";
		{
			SearchParameter sp = new SearchParameter();
			sp.setUrl(spUrl);
			sp.setCode("helloWorld");
			sp.setName(sp.getCode());
			sp.setDescription("description");
			sp.setType(Enumerations.SearchParamType.STRING);
			sp.addBase("Patient");
			sp.setExpression("Patient.name.given");
			sp.setStatus(Enumerations.PublicationStatus.ACTIVE);
			igCreator.addResourceToIG(sp.getName(), sp);
		}

		PackageInstallationSpec installationSpec = createAndInstallPackageSpec(igCreator, PackageInstallationSpec.InstallModeEnum.STORE_ONLY);

		IValidationSupport validationSupport = mySvc;

		// test
		SearchParameter sp = validationSupport.fetchResource(SearchParameter.class, spUrl + "2");

		// validate
		assertNull(sp);
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
