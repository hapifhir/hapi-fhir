package org.hl7.fhir.common.hapi.validation.validator;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.context.support.ValidationSupportContext;
import ca.uhn.fhir.fhirpath.BaseValidationTestWithInlineMocks;
import ca.uhn.fhir.i18n.HapiLocalizer;
import ca.uhn.hapi.converters.canonical.VersionCanonicalizer;

import org.hl7.fhir.r5.model.Resource;
import org.hl7.fhir.r5.model.StructureDefinition;
import org.hl7.fhir.r5.model.StructureDefinition.StructureDefinitionKind;
import org.hl7.fhir.r5.model.ValueSet;
import org.hl7.fhir.utilities.validation.ValidationOptions;
import org.junit.jupiter.api.Test;
import org.mockito.quality.Strictness;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.withSettings;

import java.util.List;

public class VersionSpecificWorkerContextWrapperTest extends BaseValidationTestWithInlineMocks {

	final byte[] EXPECTED_BINARY_CONTENT_1 = "dummyBinaryContent1".getBytes();
	final byte[] EXPECTED_BINARY_CONTENT_2 = "dummyBinaryContent2".getBytes();
	final String EXPECTED_BINARY_KEY_1 = "dummyBinaryKey1";
	final String EXPECTED_BINARY_KEY_2 = "dummyBinaryKey2";
	final String NON_EXISTENT_BINARY_KEY = "nonExistentBinaryKey";

	@Test
	public void hasBinaryKey_normally_returnsExpected() {

		IValidationSupport validationSupport = mockValidationSupportWithTwoBinaries();

		ValidationSupportContext mockContext = mockValidationSupportContext(validationSupport);

		VersionCanonicalizer versionCanonicalizer = new VersionCanonicalizer(FhirContext.forR5Cached());
		VersionSpecificWorkerContextWrapper wrapper = new VersionSpecificWorkerContextWrapper(mockContext, versionCanonicalizer);

		assertThat(wrapper.hasBinaryKey(EXPECTED_BINARY_KEY_1)).as("wrapper should have binary key " + EXPECTED_BINARY_KEY_1).isTrue();
		assertThat(wrapper.hasBinaryKey(EXPECTED_BINARY_KEY_2)).as("wrapper should have binary key " + EXPECTED_BINARY_KEY_1).isTrue();
		assertThat(wrapper.hasBinaryKey(NON_EXISTENT_BINARY_KEY)).as("wrapper should not have binary key " + NON_EXISTENT_BINARY_KEY).isFalse();

	}

	@Test
	public void getBinaryForKey_normally_returnsExpected() {

		IValidationSupport validationSupport = mockValidationSupportWithTwoBinaries();

		ValidationSupportContext mockContext = mockValidationSupportContext(validationSupport);

		VersionCanonicalizer versionCanonicalizer = new VersionCanonicalizer(FhirContext.forR5Cached());
		VersionSpecificWorkerContextWrapper wrapper = new VersionSpecificWorkerContextWrapper(mockContext, versionCanonicalizer);

		assertThat(wrapper.getBinaryForKey(EXPECTED_BINARY_KEY_1)).containsExactly(EXPECTED_BINARY_CONTENT_1);
		assertThat(wrapper.getBinaryForKey(EXPECTED_BINARY_KEY_2)).containsExactly(EXPECTED_BINARY_CONTENT_2);
		assertThat(wrapper.getBinaryForKey(NON_EXISTENT_BINARY_KEY)).as("wrapper should return null for binary key " + NON_EXISTENT_BINARY_KEY).isNull();
	}

	@Test
	public void cacheResource_normally_executesWithoutException() {

		IValidationSupport validationSupport = mockValidationSupport();

		ValidationSupportContext mockContext = mockValidationSupportContext(validationSupport);

		VersionCanonicalizer versionCanonicalizer = new VersionCanonicalizer(FhirContext.forR5Cached());
		VersionSpecificWorkerContextWrapper wrapper = new VersionSpecificWorkerContextWrapper(mockContext, versionCanonicalizer);

		wrapper.cacheResource(mock(Resource.class));
	}

	@Test
	public void validateCode_normally_resolvesCodeSystemFromValueSet() {
		// setup
		IValidationSupport validationSupport = mockValidationSupport();
		ValidationSupportContext mockContext = mockValidationSupportContext(validationSupport);
		VersionCanonicalizer versionCanonicalizer = new VersionCanonicalizer(FhirContext.forR5Cached());
		VersionSpecificWorkerContextWrapper wrapper = new VersionSpecificWorkerContextWrapper(mockContext, versionCanonicalizer);

		ValueSet valueSet = new ValueSet();
		valueSet.getCompose().addInclude().setSystem("http://codesystems.com/system").addConcept().setCode("code0");
		valueSet.getCompose().addInclude().setSystem("http://codesystems.com/system2").addConcept().setCode("code2");
		when(validationSupport.fetchResource(eq(ValueSet.class), eq("http://somevalueset"))).thenReturn(valueSet);
		when(validationSupport.validateCodeInValueSet(any(), any(), any(), any(), any(), any())).thenReturn(new IValidationSupport.CodeValidationResult());

		// execute
		wrapper.validateCode(new ValidationOptions(), "code0", valueSet);

		// verify
		verify(validationSupport, times(1)).validateCodeInValueSet(any(), any(), eq("http://codesystems.com/system"), eq("code0"), any(), any());
		verify(validationSupport, times(1)).validateCode(any(), any(), eq("http://codesystems.com/system"), eq("code0"), any(), any());
	}

	@Test
	public void isPrimitive_primitive() {
		// setup
		IValidationSupport validationSupport = mockValidationSupport();
		ValidationSupportContext mockContext = mockValidationSupportContext(validationSupport);
		VersionCanonicalizer versionCanonicalizer = new VersionCanonicalizer(FhirContext.forR5Cached());
		VersionSpecificWorkerContextWrapper wrapper = new VersionSpecificWorkerContextWrapper(mockContext, versionCanonicalizer);

		List<StructureDefinition> structDefs = createStructureDefinitions();

		when(mockContext.getRootValidationSupport().<StructureDefinition>fetchAllStructureDefinitions()).thenReturn(structDefs);
		assertThat(wrapper.isPrimitiveType("boolean")).isTrue();

		// try again to check if lookup after cache is built is working
		assertThat(wrapper.isPrimitiveType("string")).isTrue();
	}

	@Test
	public void isPrimitive_not_primitive() {
		// setup
		IValidationSupport validationSupport = mockValidationSupport();
		ValidationSupportContext mockContext = mockValidationSupportContext(validationSupport);
		VersionCanonicalizer versionCanonicalizer = new VersionCanonicalizer(FhirContext.forR5Cached());
		VersionSpecificWorkerContextWrapper wrapper = new VersionSpecificWorkerContextWrapper(mockContext, versionCanonicalizer);

		List<StructureDefinition> structDefs = createStructureDefinitions();

		when(mockContext.getRootValidationSupport().<StructureDefinition>fetchAllStructureDefinitions()).thenReturn(structDefs);
		assertThat(wrapper.isPrimitiveType("Person")).isFalse();

		// try again to check if lookup after cache is built is working
		assertThat(wrapper.isPrimitiveType("Organization")).isFalse();

		// Assert that unknown types are not regarded as primitive
		assertThat(wrapper.isPrimitiveType("Unknown")).isFalse();
	}

	private List<StructureDefinition> createStructureDefinitions() {
		StructureDefinition stringType = createPrimitive("string");
		StructureDefinition boolType = createPrimitive("boolean");
		StructureDefinition personType = createComplex("Person");
		StructureDefinition orgType = createComplex("Organization");

		return List.of(personType, boolType, orgType, stringType);
	}

	private StructureDefinition createComplex(String name){
		return createStructureDefinition(name).setKind(StructureDefinitionKind.COMPLEXTYPE);
	}

	private StructureDefinition createPrimitive(String name){
		return createStructureDefinition(name).setKind(StructureDefinitionKind.PRIMITIVETYPE);
	}

	private StructureDefinition createStructureDefinition(String name) {
		StructureDefinition sd = new StructureDefinition();
		sd.setUrl("http://hl7.org/fhir/StructureDefinition/"+name).setName(name);
		return sd;
	}

	private IValidationSupport mockValidationSupportWithTwoBinaries() {
		IValidationSupport validationSupport;
		validationSupport = mockValidationSupport();
		when(validationSupport.fetchBinary(EXPECTED_BINARY_KEY_1)).thenReturn(EXPECTED_BINARY_CONTENT_1);
		when(validationSupport.fetchBinary(EXPECTED_BINARY_KEY_2)).thenReturn(EXPECTED_BINARY_CONTENT_2);
		return validationSupport;
	}


	private static ValidationSupportContext mockValidationSupportContext(IValidationSupport validationSupport) {
		ValidationSupportContext mockContext;
		mockContext = mock(ValidationSupportContext.class);
		when(mockContext.getRootValidationSupport()).thenReturn(validationSupport);
		return mockContext;
	}


	private static IValidationSupport mockValidationSupport() {
		IValidationSupport mockValidationSupport;
		mockValidationSupport = mock(IValidationSupport.class);
		FhirContext mockFhirContext = mock(FhirContext.class, withSettings().strictness(Strictness.LENIENT));
		when(mockFhirContext.getLocalizer()).thenReturn(new HapiLocalizer());
		when(mockFhirContext.getVersion()).thenReturn(FhirVersionEnum.R4.getVersionImplementation());
		when(mockValidationSupport.getFhirContext()).thenReturn(mockFhirContext);
		return mockValidationSupport;
	}
}
