package ca.uhn.fhir.jpa.packages;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.context.support.CanonicalResourceIdentifierRequest;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.StructureDefinition;
import org.hl7.fhir.r4.model.ValueSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NpmJpaValidationSupportTest {

	private static final FhirContext FHIR_CONTEXT = FhirContext.forR4Cached();

	private static final String IDENTIFIER_SYSTEM = "urn:ietf:rfc:3986";
	private static final String IDENTIFIER_VALUE = "urn:oid:1.2.3.4";
	private static final String VERSION = "4.0.0";

	@Mock
	private IHapiPackageCacheManager myPackageCacheManager;

	private NpmJpaValidationSupport myTestClass;

	@BeforeEach
	void beforeEach() {
		myTestClass = new NpmJpaValidationSupport();

		ReflectionTestUtils.setField(
				myTestClass,
				"myFhirContext",
				FHIR_CONTEXT);

		ReflectionTestUtils.setField(
				myTestClass,
				"myHapiPackageCacheManager",
				myPackageCacheManager);
	}

	@Test
	void fetchCodeSystemByIdentifierLoadsOnlyCodeSystemAssets() {
		CodeSystem codeSystem = new CodeSystem()
				.setUrl("https://example.org/CodeSystem/example")
				.setVersion(VERSION);

		codeSystem.addIdentifier()
				.setSystem(IDENTIFIER_SYSTEM)
				.setValue(IDENTIFIER_VALUE);

		prepareAssets("CodeSystem", codeSystem);

		IBaseResource result =
				myTestClass.fetchCanonicalResourceByIdentifier(
						newRequest("CodeSystem", VERSION));

		assertSame(codeSystem, result);

		verify(myPackageCacheManager)
				.loadPackageAssetsByType(
						FhirVersionEnum.R4,
						"CodeSystem");
	}

	@Test
	void fetchValueSetByIdentifierLoadsOnlyValueSetAssets() {
		ValueSet valueSet = new ValueSet()
				.setUrl("https://example.org/ValueSet/example")
				.setVersion(VERSION);

		valueSet.addIdentifier()
				.setSystem(IDENTIFIER_SYSTEM)
				.setValue(IDENTIFIER_VALUE);

		prepareAssets("ValueSet", valueSet);

		IBaseResource result =
				myTestClass.fetchCanonicalResourceByIdentifier(
						newRequest("ValueSet", VERSION));

		assertSame(valueSet, result);

		verify(myPackageCacheManager)
				.loadPackageAssetsByType(
						FhirVersionEnum.R4,
						"ValueSet");
	}

	@Test
	void fetchStructureDefinitionByIdentifierLoadsOnlyStructureDefinitionAssets() {
		StructureDefinition structureDefinition =
				new StructureDefinition()
						.setUrl("https://example.org/StructureDefinition/example")
						.setVersion(VERSION);

		structureDefinition.addIdentifier()
				.setSystem(IDENTIFIER_SYSTEM)
				.setValue(IDENTIFIER_VALUE);

		prepareAssets(
				"StructureDefinition",
				structureDefinition);

		IBaseResource result =
				myTestClass.fetchCanonicalResourceByIdentifier(
						newRequest("StructureDefinition", VERSION));

		assertSame(structureDefinition, result);

		verify(myPackageCacheManager)
				.loadPackageAssetsByType(
						FhirVersionEnum.R4,
						"StructureDefinition");
	}

	@Test
	void wrongVersionReturnsNull() {
		ValueSet valueSet = new ValueSet()
				.setUrl("https://example.org/ValueSet/example")
				.setVersion(VERSION);

		valueSet.addIdentifier()
				.setSystem(IDENTIFIER_SYSTEM)
				.setValue(IDENTIFIER_VALUE);

		prepareAssets("ValueSet", valueSet);

		IBaseResource result =
				myTestClass.fetchCanonicalResourceByIdentifier(
						newRequest("ValueSet", "5.0.0"));

		assertNull(result);
	}

	@Test
	void unsupportedResourceTypeDoesNotLoadPackageAssets() {
		IBaseResource result =
				myTestClass.fetchCanonicalResourceByIdentifier(
						newRequest("ConceptMap", null));

		assertNull(result);
		verifyNoInteractions(myPackageCacheManager);
	}

	private void prepareAssets(
			String theResourceType,
			IBaseResource... theResources) {

		when(myPackageCacheManager.loadPackageAssetsByType(
						FhirVersionEnum.R4,
						theResourceType))
				.thenReturn(List.of(theResources));
	}

	private static CanonicalResourceIdentifierRequest newRequest(
			String theResourceType,
			String theVersion) {

		return new CanonicalResourceIdentifierRequest(
				theResourceType,
				IDENTIFIER_SYSTEM,
				IDENTIFIER_VALUE,
				theVersion);
	}
}
