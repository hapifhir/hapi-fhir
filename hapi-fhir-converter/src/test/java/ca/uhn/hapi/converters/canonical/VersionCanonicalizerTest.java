package ca.uhn.hapi.converters.canonical;

import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.model.dstu2.composite.CodingDt;
import org.hl7.fhir.instance.model.api.IBaseCoding;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.ValueSet;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VersionCanonicalizerTest {

	@Test
	public void testToCanonicalCoding() {
		VersionCanonicalizer canonicalizer = new VersionCanonicalizer(FhirVersionEnum.DSTU2);
		IBaseCoding coding = new CodingDt("dstuSystem", "dstuCode");
		Coding convertedCoding = canonicalizer.codingToCanonical(coding);
		assertEquals("dstuCode", convertedCoding.getCode());
		assertEquals("dstuSystem", convertedCoding.getSystem());
	}

	@Test
	public void testCanonicalizeDstu2ValueSet_WithCodeSystem() {
		VersionCanonicalizer canonicalizer = new VersionCanonicalizer(FhirVersionEnum.DSTU2);

		ca.uhn.fhir.model.dstu2.resource.ValueSet input = new ca.uhn.fhir.model.dstu2.resource.ValueSet();
		input.setUrl("http://vs");
		input.getCodeSystem()
			.setSystem("http://cs")
			.addConcept()
			.setCode("code")
			.setDisplay("display");
		input.getCompose()
			.addInclude()
			.setSystem("http://cs")
			.addConcept()
			.setCode("code2");
		input.getCompose()
			.addInclude()
			.setSystem("http://cs")
			.addConcept()
			.setCode("code3");

		ValueSet output = canonicalizer.valueSetToCanonical(input);
		assertEquals("http://vs", output.getUrl());
		assertEquals(2, output.getCompose().getInclude().size());
		assertEquals("code2", output.getCompose().getInclude().get(0).getConcept().get(0).getCode());
		assertEquals("code3", output.getCompose().getInclude().get(1).getConcept().get(0).getCode());
	}


}
