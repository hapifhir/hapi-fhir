package ca.uhn.fhir.jpa.dao.index.searchparameter;

import ca.uhn.fhir.context.FhirContext;
import org.hl7.fhir.dstu3.model.SearchParameter;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r5.model.Enumerations;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;

class CanonicalSearchParameterTest {


	private CanonicalSearchParameter buildCanonicalParam() {
		return new CanonicalSearchParameter(
			"patient-resource-sp",
			"http://example.org/sp/patient/resource-placeholder",
			"patient-resource-placeholder",
			"active",
			"Search for resources populated with the resource-placeholder extension",
			"resource-placeholder",
			Collections.singletonList("Patient"),
			"token",
			"Patient.extension('http://hapifhir.io/fhir/StructureDefinition/resource-placeholder').value.as(boolean)"
		);
	}

	@Test
	public void testCanonicalSearchParamSupportsDstu3() {
		CanonicalSearchParameter canonicalSearchParameter = buildCanonicalParam();
		IBaseResource decanonicalized = canonicalSearchParameter.decanonicalize(FhirContext.forDstu3());

		assertTrue(decanonicalized instanceof SearchParameter);
		assertThat(((SearchParameter) decanonicalized).getStatus(), is(equalTo(org.hl7.fhir.dstu3.model.Enumerations.PublicationStatus.ACTIVE)));
	}

	@Test
	public void testCanonicalSearchParamSupportsR4() {
		CanonicalSearchParameter canonicalSearchParameter = buildCanonicalParam();
		IBaseResource decanonicalized = canonicalSearchParameter.decanonicalize(FhirContext.forR4());

		assertTrue(decanonicalized instanceof org.hl7.fhir.r4.model.SearchParameter);
		assertThat(((org.hl7.fhir.r4.model.SearchParameter) decanonicalized).getStatus(), is(equalTo(org.hl7.fhir.r4.model.Enumerations.PublicationStatus.ACTIVE)));
	}

	@Test
	public void testCanonicalSearchParamSupportsR5() {
		CanonicalSearchParameter canonicalSearchParameter = buildCanonicalParam();
		IBaseResource decanonicalized = canonicalSearchParameter.decanonicalize(FhirContext.forR5());

		assertTrue(decanonicalized instanceof org.hl7.fhir.r5.model.SearchParameter);
		assertThat(((org.hl7.fhir.r5.model.SearchParameter) decanonicalized).getStatus(), is(equalTo(Enumerations.PublicationStatus.ACTIVE)));
	}
}
