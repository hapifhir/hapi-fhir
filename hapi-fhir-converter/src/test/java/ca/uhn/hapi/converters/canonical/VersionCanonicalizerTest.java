package ca.uhn.hapi.converters.canonical;

import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.model.dstu2.composite.CodingDt;
import ca.uhn.fhir.util.HapiExtensions;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.convertors.factory.VersionConvertorFactory_40_50;
import org.hl7.fhir.instance.model.api.IBaseCoding;
import org.hl7.fhir.instance.model.api.IBaseHasExtensions;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r4.model.CodeType;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r5.model.Base;
import org.hl7.fhir.r5.model.Enumeration;
import org.hl7.fhir.r5.model.SearchParameter;
import org.junit.jupiter.api.Test;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.stream.Collectors;

import static ca.uhn.fhir.util.ExtensionUtil.getExtensionPrimitiveValues;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
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

	@Test
	public void testToCanonicalSearchParameter_NoCustomResourceType() {
		// Setup
		VersionCanonicalizer canonicalizer = new VersionCanonicalizer(FhirVersionEnum.R4);

		org.hl7.fhir.r4.model.SearchParameter input = new org.hl7.fhir.r4.model.SearchParameter();
		input.addBase("Patient");
		input.addBase("Observation");
		input.addTarget("Organization");

		// Test
		org.hl7.fhir.r5.model.SearchParameter actual = canonicalizer.searchParameterToCanonical(input);

		// Verify
		assertThat(actual.getBase().stream().map(Enumeration::getCode).collect(Collectors.toList()), contains("Patient", "Observation"));
		assertThat(actual.getTarget().stream().map(Enumeration::getCode).collect(Collectors.toList()), contains("Organization"));
		assertThat(getExtensionPrimitiveValues(actual, HapiExtensions.EXTENSION_SEARCHPARAM_CUSTOM_BASE_RESOURCE), empty());
		assertThat(getExtensionPrimitiveValues(actual, HapiExtensions.EXTENSION_SEARCHPARAM_CUSTOM_TARGET_RESOURCE), empty());

	}

	@Test
	public void testToCanonicalSearchParameter_WithCustomResourceType() {
		// Setup
		VersionCanonicalizer canonicalizer = new VersionCanonicalizer(FhirVersionEnum.R4);

		org.hl7.fhir.r4.model.SearchParameter input = new org.hl7.fhir.r4.model.SearchParameter();
		input.addBase("Base1");
		input.addBase("Base2");
		input.addTarget("Target1");
		input.addTarget("Target2");

		// Test
		org.hl7.fhir.r5.model.SearchParameter actual = canonicalizer.searchParameterToCanonical(input);

		// Verify
		assertThat(actual.getBase().stream().map(Enumeration::getCode).collect(Collectors.toList()), empty());
		assertThat(actual.getTarget().stream().map(Enumeration::getCode).collect(Collectors.toList()), empty());
		assertThat(getExtensionPrimitiveValues(actual, HapiExtensions.EXTENSION_SEARCHPARAM_CUSTOM_BASE_RESOURCE), contains("Base1", "Base2"));
		assertThat(getExtensionPrimitiveValues(actual, HapiExtensions.EXTENSION_SEARCHPARAM_CUSTOM_TARGET_RESOURCE), contains("Target1", "Target2"));
		// Original shouldn't be modified
		assertThat(input.getBase().stream().map(CodeType::getCode).toList(), contains("Base1", "Base2"));
		assertThat(input.getTarget().stream().map(CodeType::getCode).toList(), contains("Target1", "Target2"));

	}


}
