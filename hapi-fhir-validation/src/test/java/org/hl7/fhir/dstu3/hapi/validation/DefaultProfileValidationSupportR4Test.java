package org.hl7.fhir.dstu3.hapi.validation;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.DefaultProfileValidationSupport;
import ca.uhn.fhir.fhirpath.BaseValidationTestWithInlineMocks;
import ca.uhn.fhir.validation.FhirValidator;
import ca.uhn.fhir.validation.ValidationResult;
import org.hl7.fhir.common.hapi.validation.support.ValidationSupportChain;
import org.hl7.fhir.common.hapi.validation.validator.FhirInstanceValidator;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.StructureDefinition;
import org.hl7.fhir.r4.model.ValueSet;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DefaultProfileValidationSupportR4Test extends BaseValidationTestWithInlineMocks {

	private static final Logger ourLog = LoggerFactory.getLogger(DefaultProfileValidationSupportR4Test.class);
	private static final FhirContext ourCtx = FhirContext.forR4Cached();
	private final DefaultProfileValidationSupport mySvc = new DefaultProfileValidationSupport(ourCtx);

	@Test
	public void testGetStructureDefinitionsWithRelativeUrls() {
		assertNotNull(mySvc.fetchStructureDefinition("http://hl7.org/fhir/StructureDefinition/Extension"));
		assertNotNull(mySvc.fetchStructureDefinition("StructureDefinition/Extension"));
		assertNotNull(mySvc.fetchStructureDefinition("Extension"));

		assertNull(mySvc.fetchStructureDefinition("http://hl7.org/fhir/StructureDefinition/Extension2"));
		assertNull(mySvc.fetchStructureDefinition("StructureDefinition/Extension2"));
		assertNull(mySvc.fetchStructureDefinition("Extension2"));

	}

	@Test
	public void testLoadCodeSystemWithVersion() {
		CodeSystem cs = (CodeSystem) mySvc.fetchCodeSystem("http://terminology.hl7.org/CodeSystem/v2-0291");
		assertNotNull(cs);
		String version = cs.getVersion();
		assertEquals("2.9", version);

		cs = (CodeSystem) mySvc.fetchCodeSystem("http://terminology.hl7.org/CodeSystem/v2-0291|" + version);
		assertNotNull(cs);

		cs = (CodeSystem) mySvc.fetchCodeSystem("http://terminology.hl7.org/CodeSystem/v2-0291|999");
		assertNotNull(cs);
	}

	/**
	 * The version-aware forms have to reach the same version handling as a packed canonical, and through a
	 * chain as well as directly - the chain splits the canonical and hands the module the two parts. Every
	 * other test of that routing uses a mock, which cannot fail the way a real module would.
	 */
	// Created by Claude Opus 5
	@Test
	public void testFetchCodeSystem_versionAsItsOwnParameter_matchesThePackedCanonical() {
		String url = "http://terminology.hl7.org/CodeSystem/v2-0291";
		ValidationSupportChain chain = new ValidationSupportChain(mySvc);

		CodeSystem direct = (CodeSystem) mySvc.fetchCodeSystem(url, "2.9");
		CodeSystem throughChain = (CodeSystem) chain.fetchCodeSystem(url, "2.9");
		CodeSystem throughChainAsCanonical = (CodeSystem) chain.fetchCodeSystem(url + "|2.9");

		assertNotNull(direct);
		assertEquals("2.9", direct.getVersion());
		assertNotNull(throughChain);
		assertNotNull(throughChainAsCanonical);

		// the rest of the fetch family routes the same way
		String valueSetUrl = "http://hl7.org/fhir/ValueSet/administrative-gender";
		assertNotNull(chain.fetchValueSet(valueSetUrl, "4.0.1"));
		assertNotNull(chain.fetchValueSet(valueSetUrl + "|4.0.1"));
		assertNotNull(chain.fetchResource(ValueSet.class, valueSetUrl, "4.0.1"));
	}

	/**
	 * The spec ships exactly one definition of each StructureDefinition, so naming a version cannot select
	 * between them and must not make the lookup fail. Unlike the CodeSystem and ValueSet path, this one had
	 * no canonical handling at all, so every version-specific StructureDefinition read as absent.
	 */
	// Created by Claude Opus 5
	@Test
	public void testFetchStructureDefinition_withAnyVersion_resolvesTheDefinitionTheSpecShips() {
		String url = "http://hl7.org/fhir/StructureDefinition/Patient";
		ValidationSupportChain chain = new ValidationSupportChain(mySvc);

		assertNotNull(mySvc.fetchStructureDefinition(url), "no version");
		assertNotNull(mySvc.fetchStructureDefinition(url, "4.0.1"), "version as a parameter");
		assertNotNull(mySvc.fetchStructureDefinition(url + "|4.0.1"), "packed canonical");
		assertNotNull(mySvc.fetchStructureDefinition(url, "999"), "unknown version");
		assertNotNull(chain.fetchStructureDefinition(url, "4.0.1"), "through a chain");
		assertNotNull(chain.fetchResource(StructureDefinition.class, url, "4.0.1"), "through fetchResource");
	}

	/**
	 * This module ignores the version for hl7.org and terminology.hl7.org URLs - the spec ships one
	 * definition of each - so an unknown version still resolves. testLoadCodeSystemWithVersion names that
	 * for the packed canonical; this names that the parameterized form and the chain agree with it, rather
	 * than quietly becoming stricter.
	 */
	// Created by Claude Opus 5
	@Test
	public void testFetchCodeSystem_unknownVersionOfAnHl7CodeSystem_stillResolves() {
		String url = "http://terminology.hl7.org/CodeSystem/v2-0291";
		ValidationSupportChain chain = new ValidationSupportChain(mySvc);

		assertNotNull(mySvc.fetchCodeSystem(url, "999"));
		assertNotNull(chain.fetchCodeSystem(url, "999"));
	}

	/**
	 * A StructureDefinition from the base specification is the same resource whether it is asked for with or
	 * without its version, so fetching it both ways must not list it twice.
	 */
	// Created by Claude Opus 5
	@Test
	public void testFetchAllStructureDefinitions_baseDefinitionFetchedWithAndWithoutVersion_isListedOnce() {
		ValidationSupportChain chain = new ValidationSupportChain(mySvc);
		String url = "http://hl7.org/fhir/StructureDefinition/Patient";
		chain.fetchAllStructureDefinitions();
		IBaseResource patient = chain.fetchStructureDefinition(url);
		assertNotNull(chain.fetchStructureDefinition(url, "4.0.1"));

		List<IBaseResource> all = chain.fetchAllStructureDefinitions();

		assertEquals(1, all.stream().filter(t -> t == patient).count());
	}

	@Test
	public void testValidateBuiltInProfile() {
		IBaseResource address = mySvc.fetchStructureDefinition("http://hl7.org/fhir/StructureDefinition/Address");
		ourLog.info("SD: {}", ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(address));

		FhirValidator val = ourCtx.newValidator();
		val.registerValidatorModule(new FhirInstanceValidator(ourCtx));

		ValidationResult result = val.validateWithResult(address);
		ourLog.info("Validation: {}", ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(result.toOperationOutcome()));
		assertTrue(result.isSuccessful());
	}

	@Test
	public void testFetchAllSearchParams() {
		// Test
		List<IBaseResource> allSps = mySvc.fetchAllSearchParameters();

		// Verify
		assertNotNull(allSps);
		assertEquals(1375, allSps.size());
	}

}
