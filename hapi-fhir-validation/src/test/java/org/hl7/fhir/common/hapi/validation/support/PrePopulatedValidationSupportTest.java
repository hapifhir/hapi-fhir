package org.hl7.fhir.common.hapi.validation.support;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.fhirpath.BaseValidationTestWithInlineMocks;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.StructureDefinition;
import org.hl7.fhir.r4.model.ValueSet;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class PrePopulatedValidationSupportTest extends BaseValidationTestWithInlineMocks {

	private final PrePopulatedValidationSupport mySvc = new PrePopulatedValidationSupport(FhirContext.forR4Cached());

	@Test
	public void testAddResource() {

		CodeSystem cs = new CodeSystem();
		cs.setUrl("http://cs");
		mySvc.addResource(cs);

		ValueSet vs = new ValueSet();
		vs.setUrl("http://vs");
		mySvc.addResource(vs);

		StructureDefinition sd = new StructureDefinition();
		sd.setUrl("http://sd");
		mySvc.addResource(sd);

		assertThat(mySvc.fetchCodeSystem("http://cs")).isSameAs(cs);
		assertThat(mySvc.fetchValueSet("http://vs")).isSameAs(vs);
		assertThat(mySvc.fetchStructureDefinition("http://sd")).isSameAs(sd);

	}

	@Test
	public void testAddBinary() {
		final Map<String, byte[]> EXPECTED_BINARIES_MAP = Map.of(
			"dummyBinary1.txt", "myDummyContent1".getBytes(),
			"dummyBinary2.txt", "myDummyContent2".getBytes()
		);

		for (Map.Entry<String,byte[]> entry : EXPECTED_BINARIES_MAP.entrySet()) {
			mySvc.addBinary(entry.getValue(),entry.getKey());
		}

		for (Map.Entry<String,byte[]> entry : EXPECTED_BINARIES_MAP.entrySet()) {
			assertThat(mySvc.fetchBinary(entry.getKey())).containsExactly(entry.getValue());
		}
	}

	/**
	 * A CodeSystem stored without a version answers for a named version only while it is the sole definition
	 * of that system. Once a versioned copy is stored too, a version nobody stored must not fall back to the
	 * unversioned one - which is stored last here, so it is the one a bare-URL lookup finds.
	 */
	// Created by Claude Opus 5
	@Test
	public void fetchCodeSystem_namedVersionNotStored_fallsBackToTheUnversionedCopyOnlyWhenNoVersionIsStored() {
		// Setup
		CodeSystem unversioned = new CodeSystem();
		unversioned.setUrl("http://cs");
		PrePopulatedValidationSupport onlyUnversioned = new PrePopulatedValidationSupport(FhirContext.forR4Cached());
		onlyUnversioned.addCodeSystem(unversioned);

		CodeSystem versioned = new CodeSystem();
		versioned.setUrl("http://cs");
		versioned.setVersion("1.0.0");
		mySvc.addCodeSystem(versioned);
		mySvc.addCodeSystem(unversioned);

		// Test & Verify
		assertThat(onlyUnversioned.fetchCodeSystem("http://cs", "2.0.0")).isSameAs(unversioned);
		assertThat(onlyUnversioned.isCodeSystemSupported(null, "http://cs", "2.0.0")).isTrue();
		assertThat(mySvc.fetchCodeSystem("http://cs", "1.0.0")).isSameAs(versioned);
		assertThat(mySvc.fetchCodeSystem("http://cs", "2.0.0")).isNull();
		assertThat(mySvc.isCodeSystemSupported(null, "http://cs", "2.0.0")).isFalse();
	}
}
