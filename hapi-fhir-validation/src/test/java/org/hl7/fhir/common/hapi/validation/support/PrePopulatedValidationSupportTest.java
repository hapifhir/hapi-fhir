package org.hl7.fhir.common.hapi.validation.support;

import ca.uhn.fhir.context.FhirContext;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.StructureDefinition;
import org.hl7.fhir.r4.model.ValueSet;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertSame;

public class PrePopulatedValidationSupportTest {

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

		assertSame(cs, mySvc.fetchCodeSystem("http://cs"));
		assertSame(vs, mySvc.fetchValueSet("http://vs"));
		assertSame(sd, mySvc.fetchStructureDefinition("http://sd"));

	}

}
