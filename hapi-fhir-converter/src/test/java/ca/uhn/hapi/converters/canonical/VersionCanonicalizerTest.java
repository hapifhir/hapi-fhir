package ca.uhn.hapi.converters.canonical;

import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.model.dstu2.composite.CodingDt;
import ca.uhn.fhir.model.dstu2.resource.Conformance;
import ca.uhn.fhir.util.HapiExtensions;
import org.hl7.fhir.instance.model.api.IBaseCoding;
import org.hl7.fhir.r4.model.CodeType;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r5.model.CapabilityStatement;
import org.hl7.fhir.r5.model.Enumeration;
import org.hl7.fhir.r5.model.SearchParameter;
import org.hl7.fhir.r5.model.StructureDefinition;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.stream.Collectors;

import static ca.uhn.fhir.util.ExtensionUtil.getExtensionPrimitiveValues;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class VersionCanonicalizerTest {
	@Nested
	class VersionCanonicalizerR4 {

		private static final FhirVersionEnum FHIR_VERSION = FhirVersionEnum.R4;
		private static final VersionCanonicalizer ourCanonicalizer = new VersionCanonicalizer(FHIR_VERSION);
		@Test
		public void testToCanonical_SearchParameterNoCustomResourceType_ConvertedCorrectly() {
			org.hl7.fhir.r4.model.SearchParameter input = new org.hl7.fhir.r4.model.SearchParameter();
			input.addBase("Patient");
			input.addBase("Observation");
			input.addTarget("Organization");

			// Test
			org.hl7.fhir.r5.model.SearchParameter actual = ourCanonicalizer.searchParameterToCanonical(input);

			// Verify
			assertThat(actual.getBase().stream().map(Enumeration::getCode).collect(Collectors.toList())).containsExactly("Patient", "Observation");
			assertThat(actual.getTarget().stream().map(Enumeration::getCode).collect(Collectors.toList())).containsExactly("Organization");
			assertThat(getExtensionPrimitiveValues(actual, HapiExtensions.EXTENSION_SEARCHPARAM_CUSTOM_BASE_RESOURCE)).isEmpty();
			assertThat(getExtensionPrimitiveValues(actual, HapiExtensions.EXTENSION_SEARCHPARAM_CUSTOM_TARGET_RESOURCE)).isEmpty();

		}

		@Test
		public void testToCanonical_SearchParameterWithCustomResourceType__ConvertedCorrectly() {
			// Setup
			org.hl7.fhir.r4.model.SearchParameter input = new org.hl7.fhir.r4.model.SearchParameter();
			input.addBase("Base1");
			input.addBase("Base2");
			input.addTarget("Target1");
			input.addTarget("Target2");

			// Test
			org.hl7.fhir.r5.model.SearchParameter actual = ourCanonicalizer.searchParameterToCanonical(input);

			// Verify
			assertThat(actual.getBase().stream().map(Enumeration::getCode).collect(Collectors.toList())).isEmpty();
			assertThat(actual.getTarget().stream().map(Enumeration::getCode).collect(Collectors.toList())).isEmpty();
			assertThat(getExtensionPrimitiveValues(actual, HapiExtensions.EXTENSION_SEARCHPARAM_CUSTOM_BASE_RESOURCE)).containsExactly("Base1", "Base2");
			assertThat(getExtensionPrimitiveValues(actual, HapiExtensions.EXTENSION_SEARCHPARAM_CUSTOM_TARGET_RESOURCE)).containsExactly("Target1", "Target2");
			// Original shouldn't be modified
			assertThat(input.getBase().stream().map(CodeType::getCode).toList()).containsExactly("Base1", "Base2");
			assertThat(input.getTarget().stream().map(CodeType::getCode).toList()).containsExactly("Target1", "Target2");

		}
	}

	@Nested
	class VersionCanonicalizerDstu2 {
		private static final FhirVersionEnum FHIR_VERSION = FhirVersionEnum.DSTU2;
		private static final VersionCanonicalizer ourCanonicalizer = new VersionCanonicalizer(FHIR_VERSION);

		@Test
		public void testToCanonical_Coding_ConvertSuccessful() {
			IBaseCoding coding = new CodingDt("dstuSystem", "dstuCode");
			Coding convertedCoding = ourCanonicalizer.codingToCanonical(coding);
			assertEquals("dstuCode", convertedCoding.getCode());
			assertEquals("dstuSystem", convertedCoding.getSystem());
		}

		@Test
		public void testFromCanonical_SearchParameter_ConvertSuccessful() {
			SearchParameter inputR5 = new SearchParameter();
			inputR5.setUrl("http://foo");
			ca.uhn.fhir.model.dstu2.resource.SearchParameter outputDstu2 = (ca.uhn.fhir.model.dstu2.resource.SearchParameter) ourCanonicalizer.searchParameterFromCanonical(inputR5);
			assertEquals("http://foo", outputDstu2.getUrl());
		}

		@Test
		public void testFromCanonical_CapabilityStatement_ConvertSuccessful() {
			CapabilityStatement inputR5 = new CapabilityStatement();
			inputR5.setUrl("http://foo");
			Conformance conformance = (Conformance) ourCanonicalizer.capabilityStatementFromCanonical(inputR5);
			assertEquals("http://foo", conformance.getUrl());
		}

		@Test
		public void testFromCanonical_StructureDefinition_ConvertSuccessful() {
			StructureDefinition inputR5 = new StructureDefinition();
			inputR5.setId("123");
			ca.uhn.fhir.model.dstu2.resource.StructureDefinition structureDefinition = (ca.uhn.fhir.model.dstu2.resource.StructureDefinition) ourCanonicalizer.structureDefinitionFromCanonical(inputR5);
			assertEquals("StructureDefinition/123", structureDefinition.getId().getValue());
		}

		@Test
		public void testFromCanonical_Parameters_ConvertSuccessful() {
			org.hl7.fhir.r4.model.Parameters inputR4 = new Parameters();
			inputR4.setParameter("paramA", "1");
			ca.uhn.fhir.model.dstu2.resource.Parameters parameters = (ca.uhn.fhir.model.dstu2.resource.Parameters) ourCanonicalizer.parametersFromCanonical(inputR4);
			assertNotNull(parameters.getParameter());
			assertEquals("paramA", parameters.getParameter().get(0).getName());
		}
	}
}
