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

}
