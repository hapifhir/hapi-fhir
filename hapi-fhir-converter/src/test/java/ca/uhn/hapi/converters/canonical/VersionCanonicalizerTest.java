package ca.uhn.hapi.converters.canonical;

import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.model.dstu2.composite.CodingDt;
import org.hl7.fhir.instance.model.api.IBaseCoding;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r5.model.SearchParameter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
	public void testFromCanonicalSearchParameter() {
		VersionCanonicalizer canonicalizer = new VersionCanonicalizer(FhirVersionEnum.DSTU2);

		SearchParameter inputR5 = new SearchParameter();
		inputR5.setUrl("http://foo");
		ca.uhn.fhir.model.dstu2.resource.SearchParameter outputDstu2 = (ca.uhn.fhir.model.dstu2.resource.SearchParameter) canonicalizer.searchParameterFromCanonical(inputR5);
		assertEquals("http://foo", outputDstu2.getUrl());
	}

}
