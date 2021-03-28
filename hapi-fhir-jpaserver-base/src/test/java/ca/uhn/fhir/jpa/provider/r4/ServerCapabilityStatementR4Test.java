package ca.uhn.fhir.jpa.provider.r4;

import ca.uhn.fhir.jpa.packages.PackageInstallationSpec;
import org.hl7.fhir.r4.model.CapabilityStatement;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.SearchParameter;
import org.hl7.fhir.r4.model.StructureDefinition;
import org.junit.jupiter.api.Test;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasItems;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ServerCapabilityStatementR4Test extends BaseResourceProviderR4Test {

	@Test
	public void testCorrectResourcesReflected() {
		CapabilityStatement cs = myClient.capabilities().ofType(CapabilityStatement.class).execute();

		List<String> resourceTypes = cs.getRest().get(0).getResource().stream().map(t -> t.getType()).collect(Collectors.toList());
		assertThat(resourceTypes, hasItems("Patient", "Observation", "SearchParameter"));
	}

	@Test
	public void testCustomSearchParamsReflected() {
		SearchParameter fooSp = new SearchParameter();
		fooSp.addBase("Patient");
		fooSp.setCode("foo");
		fooSp.setUrl("http://acme.com/foo");
		fooSp.setType(org.hl7.fhir.r4.model.Enumerations.SearchParamType.TOKEN);
		fooSp.setTitle("FOO SP");
		fooSp.setDescription("This is a search param!");
		fooSp.setExpression("Patient.gender");
		fooSp.setXpathUsage(org.hl7.fhir.r4.model.SearchParameter.XPathUsageType.NORMAL);
		fooSp.setStatus(org.hl7.fhir.r4.model.Enumerations.PublicationStatus.ACTIVE);
		mySearchParameterDao.create(fooSp);
		mySearchParamRegistry.forceRefresh();

		CapabilityStatement cs = myClient.capabilities().ofType(CapabilityStatement.class).execute();

		List<CapabilityStatement.CapabilityStatementRestResourceSearchParamComponent> fooSearchParams = findSearchParams(cs, "Patient", "foo");
		assertEquals(1, fooSearchParams.size());
		assertEquals("foo", fooSearchParams.get(0).getName());
		assertEquals("http://acme.com/foo", fooSearchParams.get(0).getDefinition());
		assertEquals("This is a search param!", fooSearchParams.get(0).getDocumentation());
		assertEquals(Enumerations.SearchParamType.TOKEN, fooSearchParams.get(0).getType());

	}

	@Test
	public void testRegisteredProfilesReflected_StoredInServer() throws IOException {
		StructureDefinition sd = loadResourceFromClasspath(StructureDefinition.class, "/r4/StructureDefinition-kfdrc-patient.json");
		myStructureDefinitionDao.update(sd);
		StructureDefinition sd2 = loadResourceFromClasspath(StructureDefinition.class, "/r4/StructureDefinition-kfdrc-patient-no-phi.json");
		myStructureDefinitionDao.update(sd2);

		CapabilityStatement cs = myClient.capabilities().ofType(CapabilityStatement.class).execute();

		List<String> supportedProfiles = findSupportedProfiles(cs, "Patient");
		assertThat(supportedProfiles.toString(), supportedProfiles, containsInAnyOrder(
			"http://fhir.kids-first.io/StructureDefinition/kfdrc-patient",
			"http://fhir.kids-first.io/StructureDefinition/kfdrc-patient-no-phi"
		));
	}

	@Test
	public void testRegisteredProfilesReflected_StoredInPackageRegistry() throws IOException {
		byte[] bytes = loadClasspathBytes("/packages/UK.Core.r4-1.1.0.tgz");
		PackageInstallationSpec spec = new PackageInstallationSpec()
			.setName("UK.Core.r4")
			.setVersion("1.1.0")
			.setInstallMode(PackageInstallationSpec.InstallModeEnum.STORE_ONLY)
			.setPackageContents(bytes);
		myPackageInstallerSvc.install(spec);

		CapabilityStatement cs = myClient.capabilities().ofType(CapabilityStatement.class).execute();

		List<String> supportedProfiles = findSupportedProfiles(cs, "Patient");
		assertThat(supportedProfiles.toString(), supportedProfiles, containsInAnyOrder(
			"https://fhir.nhs.uk/R4/StructureDefinition/UKCore-Patient"
		));
	}

	@Nonnull
	private List<String> findSupportedProfiles(CapabilityStatement theCapabilityStatement, String theResourceType) {
		assertEquals(1, theCapabilityStatement.getRest().size());
		return theCapabilityStatement
			.getRest()
			.get(0)
			.getResource()
			.stream()
			.filter(t -> t.getType().equals(theResourceType))
			.findFirst()
			.orElseThrow(() -> new IllegalStateException())
			.getSupportedProfile()
			.stream()
			.map(t -> t.getValue())
			.collect(Collectors.toList());
	}

	@Nonnull
	private List<CapabilityStatement.CapabilityStatementRestResourceSearchParamComponent> findSearchParams(CapabilityStatement theCapabilityStatement, String theResourceType, String theParamName) {
		assertEquals(1, theCapabilityStatement.getRest().size());
		return theCapabilityStatement
			.getRest()
			.get(0)
			.getResource()
			.stream()
			.filter(t -> t.getType().equals(theResourceType))
			.findFirst()
			.orElseThrow(() -> new IllegalStateException())
			.getSearchParam()
			.stream()
			.filter(t -> t.getName().equals(theParamName))
			.collect(Collectors.toList());
	}

}
