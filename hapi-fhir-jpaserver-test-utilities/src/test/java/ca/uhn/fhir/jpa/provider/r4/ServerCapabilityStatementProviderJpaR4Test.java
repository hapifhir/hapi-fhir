package ca.uhn.fhir.jpa.provider.r4;

import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.packages.PackageInstallationSpec;
import ca.uhn.fhir.rest.api.CacheControlDirective;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.server.provider.ServerCapabilityStatementProvider;
import ca.uhn.fhir.util.ClasspathUtil;
import org.apache.commons.lang3.StringUtils;
import org.hamcrest.Matchers;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.CapabilityStatement;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.SearchParameter;
import org.hl7.fhir.r4.model.StructureDefinition;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class ServerCapabilityStatementProviderJpaR4Test extends BaseResourceProviderR4Test {

	private static final Logger ourLog = LoggerFactory.getLogger(ServerCapabilityStatementProviderJpaR4Test.class);

	@Test
	public void testBuiltInSearchParameters() {
		CapabilityStatement cs = myClient.capabilities().ofType(CapabilityStatement.class).execute();
		CapabilityStatement.CapabilityStatementRestResourceComponent resource = cs.getRest().get(0).getResource().get(0);
		List<String> definitions = resource.getSearchParam()
			.stream()
			.filter(t -> isNotBlank(t.getDefinition()))
			.map(t->t.getDefinition())
			.sorted()
			.collect(Collectors.toList());
		assertThat(definitions.toString(), definitions, Matchers.contains(
			"http://hl7.org/fhir/SearchParameter/Account-identifier",
			"http://hl7.org/fhir/SearchParameter/Account-name",
			"http://hl7.org/fhir/SearchParameter/Account-owner",
			"http://hl7.org/fhir/SearchParameter/Account-patient",
			"http://hl7.org/fhir/SearchParameter/Account-period",
			"http://hl7.org/fhir/SearchParameter/Account-status",
			"http://hl7.org/fhir/SearchParameter/Account-subject",
			"http://hl7.org/fhir/SearchParameter/Account-type",
			"http://hl7.org/fhir/SearchParameter/DomainResource-text",
			"http://hl7.org/fhir/SearchParameter/Resource-content",
			"http://hl7.org/fhir/SearchParameter/Resource-id",
			"http://hl7.org/fhir/SearchParameter/Resource-lastUpdated",
			"http://hl7.org/fhir/SearchParameter/Resource-profile",
			"http://hl7.org/fhir/SearchParameter/Resource-security",
			"http://hl7.org/fhir/SearchParameter/Resource-source",
			"http://hl7.org/fhir/SearchParameter/Resource-tag"
		));
	}

	@Test
	public void testCorrectResourcesReflected() {
		CapabilityStatement cs = myClient.capabilities().ofType(CapabilityStatement.class).execute();

		List<String> resourceTypes = cs.getRest().get(0).getResource().stream().map(t -> t.getType()).collect(Collectors.toList());
		assertThat(resourceTypes, hasItems("Patient", "Observation", "SearchParameter"));
	}


	@Test
	public void testNoDuplicateResourceOperationNames() {
		CapabilityStatement cs = myClient.capabilities().ofType(CapabilityStatement.class).execute();
		ourLog.info(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(cs));
		for (CapabilityStatement.CapabilityStatementRestResourceComponent next : cs.getRestFirstRep().getResource()) {
			List<String> opNames = next
				.getOperation()
				.stream()
				.map(t -> t.getName())
				.sorted()
				.collect(Collectors.toList());
			ourLog.info("System ops: {}", opNames);
			assertEquals(opNames.stream().distinct().sorted().collect(Collectors.toList()), opNames);
		}
	}

	@Test
	public void testNoDuplicateSystemOperationNames() {
		CapabilityStatement cs = myClient.capabilities().ofType(CapabilityStatement.class).execute();
		ourLog.info(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(cs));
		List<String> systemOpNames = cs
			.getRestFirstRep()
			.getOperation()
			.stream()
			.map(t -> t.getName())
			.sorted()
			.collect(Collectors.toList());
		ourLog.info("System ops: {}", systemOpNames);
		assertEquals(systemOpNames.stream().distinct().sorted().collect(Collectors.toList()), systemOpNames);
	}

	@Test
	public void testCustomSearchParamsReflectedInSearchParams() {
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
	public void testLastUpdatedIncluded() {
		CapabilityStatement cs = myClient.capabilities().ofType(CapabilityStatement.class).execute();

		List<CapabilityStatement.CapabilityStatementRestResourceSearchParamComponent> fooSearchParams = findSearchParams(cs, "Patient", "_lastUpdated");
		assertEquals(1, fooSearchParams.size());
		assertEquals("_lastUpdated", fooSearchParams.get(0).getName());
		assertEquals("http://hl7.org/fhir/SearchParameter/Resource-lastUpdated", fooSearchParams.get(0).getDefinition());
		assertEquals("When the resource version last changed", fooSearchParams.get(0).getDocumentation());
		assertEquals(Enumerations.SearchParamType.DATE, fooSearchParams.get(0).getType());

	}

	@Override
	@AfterEach
	public void after() throws Exception {
		super.after();
		ourCapabilityStatementProvider.setRestResourceRevIncludesEnabled(ServerCapabilityStatementProvider.DEFAULT_REST_RESOURCE_REV_INCLUDES_ENABLED);
		myDaoConfig.setFilterParameterEnabled(new DaoConfig().isFilterParameterEnabled());
	}


	@Test
	public void testFormats() {
		CapabilityStatement cs = myClient
			.capabilities()
			.ofType(CapabilityStatement.class)
			.cacheControl(CacheControlDirective.noCache())
			.execute();
		List<String> formats = cs
			.getFormat()
			.stream()
			.map(t -> t.getCode())
			.collect(Collectors.toList());
		assertThat(formats.toString(), formats, hasItems(
			"application/x-turtle",
			"ttl",
			"application/fhir+xml",
			"application/fhir+json",
			"json",
			"xml",
			"html/xml",
			"html/json"));
	}

	@Test
	public void testCustomSearchParamsReflectedInIncludesAndRevIncludes_TargetSpecified() {
		SearchParameter fooSp = new SearchParameter();
		fooSp.addBase("Observation");
		fooSp.setCode("foo");
		fooSp.setUrl("http://acme.com/foo");
		fooSp.setType(Enumerations.SearchParamType.REFERENCE);
		fooSp.setTitle("FOO SP");
		fooSp.setDescription("This is a search param!");
		fooSp.setExpression("Observation.subject");
		fooSp.addTarget("Patient");
		fooSp.setXpathUsage(org.hl7.fhir.r4.model.SearchParameter.XPathUsageType.NORMAL);
		fooSp.setStatus(org.hl7.fhir.r4.model.Enumerations.PublicationStatus.ACTIVE);
		mySearchParameterDao.create(fooSp);
		mySearchParamRegistry.forceRefresh();

		ourCapabilityStatementProvider.setRestResourceRevIncludesEnabled(true);

		CapabilityStatement cs = myClient
			.capabilities()
			.ofType(CapabilityStatement.class)
			.cacheControl(CacheControlDirective.noCache())
			.execute();

		List<String> includes = findIncludes(cs, "Patient");
		assertThat(includes.toString(), includes, containsInAnyOrder("*", "Patient:general-practitioner", "Patient:link", "Patient:organization"));

		includes = findIncludes(cs, "Observation");
		assertThat(includes.toString(), includes, containsInAnyOrder("*", "Observation:based-on", "Observation:derived-from", "Observation:device", "Observation:encounter", "Observation:focus", "Observation:foo", "Observation:has-member", "Observation:part-of", "Observation:patient", "Observation:performer", "Observation:specimen", "Observation:subject"));

		List<String> revIncludes = findRevIncludes(cs, "Patient");
		assertThat(revIncludes.toString(), revIncludes, hasItems(
			"Account:patient",  // Standard SP reference
			"Observation:foo",  // Standard SP reference with no explicit target
			"Provenance:entity" // Reference in custom SP
			));
		assertThat(revIncludes.toString(), revIncludes, not(hasItem(
			"CarePlan:based-on" // Standard SP reference with non-matching target
		)));

	}

	@Nonnull
	private List<String> findIncludes(CapabilityStatement theCapabilityStatement, String theResourceName) {
		return theCapabilityStatement
			.getRest()
			.stream()
			.flatMap(t -> t.getResource().stream())
			.filter(t -> t.getType().equals(theResourceName))
			.flatMap(t -> t.getSearchInclude().stream())
			.map(t -> t.getValue())
			.collect(Collectors.toList());
	}

	@Nonnull
	private List<String> findRevIncludes(CapabilityStatement theCapabilityStatement, String theResourceName) {
		return theCapabilityStatement
			.getRest()
			.stream()
			.flatMap(t -> t.getResource().stream())
			.filter(t -> t.getType().equals(theResourceName))
			.flatMap(t -> t.getSearchRevInclude().stream())
			.map(t -> t.getValue())
			.collect(Collectors.toList());
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

	/**
	 * Universal profiles like vitalsigns should not be excluded
	 */
	@Test
	public void testRegisteredProfilesReflected_Universal() throws IOException {
		StructureDefinition sd = loadResourceFromClasspath(StructureDefinition.class, "/r4/r4-create-structuredefinition-vital-signs.json");
		ourLog.info("Stored SD to ID: {}", myStructureDefinitionDao.update(sd).getId());

		CapabilityStatement cs = myClient.capabilities().ofType(CapabilityStatement.class).execute();

		List<String> supportedProfiles = findSupportedProfiles(cs, "Observation");
		assertThat(supportedProfiles.toString(), supportedProfiles, containsInAnyOrder(
			"http://hl7.org/fhir/StructureDefinition/vitalsigns"
		));
	}

	@Test
	public void testRegisteredProfilesReflected_StoredInPackageRegistry() throws IOException {
		byte[] bytes = ClasspathUtil.loadResourceAsByteArray("/packages/UK.Core.r4-1.1.0.tgz");
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

	@Test
	public void testFilterProperlyReported() {
		myDaoConfig.setFilterParameterEnabled(false);
		CapabilityStatement cs = myClient.capabilities().ofType(CapabilityStatement.class).execute();
		assertThat(findSearchParams(cs, "Patient", Constants.PARAM_FILTER), hasSize(0));
	}


	@Test
	public void testBuiltInParametersHaveAppropriateUrl() throws IOException {
		Bundle allSearchParamBundle = loadResourceFromClasspath(Bundle.class, "org/hl7/fhir/r4/model/sp/search-parameters.json");
		Set<String> allSearchParamUrls = allSearchParamBundle
			.getEntry()
			.stream()
			.map(t -> (SearchParameter) t.getResource())
			.map(t -> t.getUrl())
			.filter(StringUtils::isNotBlank)
			.collect(Collectors.toSet());

		CapabilityStatement cs = myClient.capabilities().ofType(CapabilityStatement.class).execute();
		for (CapabilityStatement.CapabilityStatementRestResourceComponent nextResource : cs.getRestFirstRep().getResource()) {
			for (CapabilityStatement.CapabilityStatementRestResourceSearchParamComponent nextSp : nextResource.getSearchParam()) {
				if (nextSp.getName().equals("_has")) {
					if (nextSp.getDefinition() == null) {
						continue;
					}
				}
				if (!allSearchParamUrls.contains(nextSp.getDefinition())) {
					fail("Invalid search parameter: " + nextSp.getName() + " has definition URL: " + nextSp.getDefinition());
				}
			}
		}
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
