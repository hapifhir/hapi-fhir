package org.hl7.fhir.dstu3.hapi.validation;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.DefaultProfileValidationSupport;
import ca.uhn.fhir.fhirpath.BaseValidationTestWithInlineMocks;
import org.hl7.fhir.r4.model.CodeSystem;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class DefaultProfileValidationSupportR4Test extends BaseValidationTestWithInlineMocks {

	private static FhirContext ourCtx = FhirContext.forR4Cached();
	private DefaultProfileValidationSupport mySvc = new DefaultProfileValidationSupport(ourCtx);
	
	@Test
	public void testGetStructureDefinitionsWithRelativeUrls() {
		assertThat(mySvc.fetchStructureDefinition("http://hl7.org/fhir/StructureDefinition/Extension")).isNotNull();
		assertThat(mySvc.fetchStructureDefinition("StructureDefinition/Extension")).isNotNull();
		assertThat(mySvc.fetchStructureDefinition("Extension")).isNotNull();

		assertThat(mySvc.fetchStructureDefinition("http://hl7.org/fhir/StructureDefinition/Extension2")).isNull();
		assertThat(mySvc.fetchStructureDefinition("StructureDefinition/Extension2")).isNull();
		assertThat(mySvc.fetchStructureDefinition("Extension2")).isNull();

	}
	
	@Test
	public void testLoadCodeSystemWithVersion() {
		CodeSystem cs = (CodeSystem) mySvc.fetchCodeSystem("http://terminology.hl7.org/CodeSystem/v2-0291");
		assertThat(cs).isNotNull();
		String version = cs.getVersion();
		assertThat(version).isEqualTo("2.9");

		cs = (CodeSystem) mySvc.fetchCodeSystem("http://terminology.hl7.org/CodeSystem/v2-0291|" + version);
		assertThat(cs).isNotNull();

		cs = (CodeSystem) mySvc.fetchCodeSystem("http://terminology.hl7.org/CodeSystem/v2-0291|999");
		assertThat(cs).isNotNull();
	}
}
