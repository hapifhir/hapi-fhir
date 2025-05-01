package org.hl7.fhir.common.hapi.validation.validator;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.DefaultProfileValidationSupport;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.context.support.ValidationSupportContext;
import ca.uhn.fhir.fhirpath.BaseValidationTestWithInlineMocks;
import ca.uhn.hapi.converters.canonical.VersionCanonicalizer;
import org.hl7.fhir.r4.model.StructureDefinition;
import org.hl7.fhir.r5.model.PackageInformation;
import org.hl7.fhir.r5.model.Resource;
import org.hl7.fhir.r5.model.ValueSet;
import org.hl7.fhir.utilities.validation.ValidationOptions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class VersionSpecificWorkerContextWrapperTest extends BaseValidationTestWithInlineMocks {

	private static final byte[] EXPECTED_BINARY_CONTENT_1 = "dummyBinaryContent1".getBytes();
	private static final byte[] EXPECTED_BINARY_CONTENT_2 = "dummyBinaryContent2".getBytes();
	private static final String EXPECTED_BINARY_KEY_1 = "dummyBinaryKey1";
	private static final String EXPECTED_BINARY_KEY_2 = "dummyBinaryKey2";
	private static final String NON_EXISTENT_BINARY_KEY = "nonExistentBinaryKey";

	private static final FhirContext ourCtx = FhirContext.forR4Cached();
	private IValidationSupport myValidationSupport;
	private ValidationSupportContext myValidationSupportContext;
	private VersionSpecificWorkerContextWrapper myWorkerContextWrapper;

	public void setupValidationForBinary() {
		myValidationSupport = mockValidationSupportWithTwoBinaries();
		myValidationSupportContext = mockValidationSupportContext(myValidationSupport);
		VersionCanonicalizer versionCanonicalizer = new VersionCanonicalizer(ourCtx);
		myWorkerContextWrapper = new VersionSpecificWorkerContextWrapper(myValidationSupportContext, versionCanonicalizer);
	}

	public void setupValidation() {
		myValidationSupport = mockValidationSupport();
		myValidationSupportContext = mockValidationSupportContext(myValidationSupport);
		VersionCanonicalizer versionCanonicalizer = new VersionCanonicalizer(ourCtx);
		myWorkerContextWrapper = new VersionSpecificWorkerContextWrapper(myValidationSupportContext, versionCanonicalizer);
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
		when(mockValidationSupport.getFhirContext()).thenReturn(ourCtx);
		return mockValidationSupport;
	}

	@Test
	public void hasBinaryKey_returnsExpected() {
		setupValidationForBinary();
		assertThat(myWorkerContextWrapper.hasBinaryKey(EXPECTED_BINARY_KEY_1)).as("wrapper should have binary key " + EXPECTED_BINARY_KEY_1).isTrue();
		assertThat(myWorkerContextWrapper.hasBinaryKey(EXPECTED_BINARY_KEY_2)).as("wrapper should have binary key " + EXPECTED_BINARY_KEY_1).isTrue();
		assertThat(myWorkerContextWrapper.hasBinaryKey(NON_EXISTENT_BINARY_KEY)).as("wrapper should not have binary key " + NON_EXISTENT_BINARY_KEY).isFalse();

	}

	@Test
	public void getBinaryForKey_returnsExpected() {
		setupValidationForBinary();
		assertThat(myWorkerContextWrapper.getBinaryForKey(EXPECTED_BINARY_KEY_1)).containsExactly(EXPECTED_BINARY_CONTENT_1);
		assertThat(myWorkerContextWrapper.getBinaryForKey(EXPECTED_BINARY_KEY_2)).containsExactly(EXPECTED_BINARY_CONTENT_2);
		assertThat(myWorkerContextWrapper.getBinaryForKey(NON_EXISTENT_BINARY_KEY)).as("wrapper should return null for binary key " + NON_EXISTENT_BINARY_KEY).isNull();
	}

	@Test
	public void validateCode_codeInValueSet_resolvesCodeSystemFromValueSet() {
		// setup
		setupValidation();

		org.hl7.fhir.r5.model.ValueSet valueSet = new ValueSet();
		valueSet.getCompose().addInclude().setSystem("http://codesystems.com/system").addConcept().setCode("code0");
		valueSet.getCompose().addInclude().setSystem("http://codesystems.com/system2").addConcept().setCode("code2");
		when(myValidationSupport.validateCodeInValueSet(any(), any(), any(), any(), any(), any())).thenReturn(mock(IValidationSupport.CodeValidationResult.class));

		// execute
		myWorkerContextWrapper.validateCode(new ValidationOptions(), "code0", valueSet);

		// verify
		verify(myValidationSupport, times(1)).validateCodeInValueSet(any(), any(), eq("http://codesystems.com/system"), eq("code0"), any(), any());
		verify(myValidationSupport, times(1)).validateCode(any(), any(), eq("http://codesystems.com/system"), eq("code0"), any(), any());
	}

	@Test
	public void validateCode_codeNotInValueSet_doesNotResolveSystem() {
		setupValidation();

		org.hl7.fhir.r5.model.ValueSet valueSet = new org.hl7.fhir.r5.model.ValueSet();
		valueSet.getCompose().addInclude().setSystem("http://codesystems.com/system").addConcept().setCode("code0");
		valueSet.getCompose().addInclude().setSystem("http://codesystems.com/system2").addConcept().setCode("code2");

		// execute
		myWorkerContextWrapper.validateCode(new ValidationOptions(), "code1", valueSet);

		// verify
		verify(myValidationSupport, times(1)).validateCodeInValueSet(any(), any(), eq(null), eq("code1"), any(), any());
		verify(myValidationSupport, never()).validateCode(any(), any(), any(), any(), any(), any());
	}

	@Test
	public void isPrimitive_primitive() {
		// setup
		setupValidation();

		List<StructureDefinition> structDefs = createPrimitiveStructureDefinitions();

		when(myValidationSupportContext.getRootValidationSupport().<StructureDefinition>fetchAllStructureDefinitions()).thenReturn(structDefs);
		assertThat(myWorkerContextWrapper.isPrimitiveType("boolean")).isTrue();

		// try again to check if lookup after cache is built is working
		assertThat(myWorkerContextWrapper.isPrimitiveType("string")).isTrue();
	}

	@Test
	public void isPrimitive_not_primitive() {
		// setup
		setupValidation();

		List<StructureDefinition> structDefs = createPrimitiveStructureDefinitions();

		when(myValidationSupportContext.getRootValidationSupport().<StructureDefinition>fetchAllStructureDefinitions()).thenReturn(structDefs);
		assertThat(myWorkerContextWrapper.isPrimitiveType("Person")).isFalse();

		// try again to check if lookup after cache is built is working
		assertThat(myWorkerContextWrapper.isPrimitiveType("Organization")).isFalse();

		// Assert that unknown types are not regarded as primitive
		assertThat(myWorkerContextWrapper.isPrimitiveType("Unknown")).isFalse();
	}

	@Test
	public void fetchResource_withResource() {
		// setup
		setupValidation();

		StructureDefinition expected = new StructureDefinition();
		expected.setUrl("http://foo");
		expected.getSnapshot().addElement().setId("FOO");
		when(myValidationSupportContext.getRootValidationSupport().fetchResource(isNull(), eq("http://foo"))).thenReturn(expected);

		// Test
		org.hl7.fhir.r5.model.StructureDefinition actual = (org.hl7.fhir.r5.model.StructureDefinition) myWorkerContextWrapper.fetchResource(Resource.class, "http://foo");

		// Verify
		assertThat(actual.getSnapshot().getElementFirstRep().getId()).isEqualTo("FOO");
	}

	@Test
	public void fetchResource_withStructureDefinition() {
		// setup
		setupValidation();

		StructureDefinition expected = new StructureDefinition();
		expected.setUrl("http://foo");
		expected.getSnapshot().addElement().setId("FOO");
		expected.setUserData(DefaultProfileValidationSupport.SOURCE_PACKAGE_ID, "hl7.fhir.r999.core");
		when(myValidationSupportContext.getRootValidationSupport().fetchResource(any(), eq("http://foo"))).thenReturn(expected);

		// Test
		org.hl7.fhir.r5.model.StructureDefinition actual = myWorkerContextWrapper.fetchResource(org.hl7.fhir.r5.model.StructureDefinition.class, "http://foo");

		// Verify
		assertThat(actual.getSnapshot().getElementFirstRep().getId()).isEqualTo("FOO");
		PackageInformation sourcePackage = actual.getSourcePackage();
		assertThat(sourcePackage.getId()).isEqualTo("hl7.fhir.r999.core");
	}

	private List<StructureDefinition> createPrimitiveStructureDefinitions() {
		StructureDefinition stringType = createPrimitive("string");
		StructureDefinition boolType = createPrimitive("boolean");
		StructureDefinition personType = createComplex("Person");
		StructureDefinition orgType = createComplex("Organization");

		return List.of(personType, boolType, orgType, stringType);
	}

	private StructureDefinition createComplex(String name){
		return createStructureDefinition(name).setKind(StructureDefinition.StructureDefinitionKind.COMPLEXTYPE);
	}

	private StructureDefinition createPrimitive(String name){
		return createStructureDefinition(name).setKind(StructureDefinition.StructureDefinitionKind.PRIMITIVETYPE);
	}

	private StructureDefinition createStructureDefinition(String name) {
		StructureDefinition sd = new StructureDefinition();
		sd.setUrl("http://hl7.org/fhir/StructureDefinition/" + name).setName(name);
		sd.getSnapshot().addElement().setId("FOO");
		return sd;
	}
}
