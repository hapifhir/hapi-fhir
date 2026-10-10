package ca.uhn.fhir.jpa.searchparam.registry;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.model.api.ExtensionDt;
import ca.uhn.fhir.model.dstu2.valueset.ConformanceResourceStatusEnum;
import ca.uhn.fhir.model.dstu2.valueset.ResourceTypeEnum;
import ca.uhn.fhir.model.dstu2.valueset.SearchParamTypeEnum;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.rest.api.RestSearchParameterTypeEnum;
import ca.uhn.fhir.util.HapiExtensions;
import ca.uhn.hapi.converters.canonical.VersionCanonicalizer;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.CodeType;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.SearchParameter;
import org.hl7.fhir.r4.model.StringType;
import org.hl7.fhir.r4.model.Type;
import org.hl7.fhir.r4.model.UriType;
import org.hl7.fhir.r4.model.UrlType;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

import static ca.uhn.fhir.util.HapiExtensions.EXTENSION_SEARCHPARAM_CUSTOM_BASE_RESOURCE;
import static ca.uhn.fhir.util.HapiExtensions.EXTENSION_SEARCHPARAM_CUSTOM_TARGET_RESOURCE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
public class SearchParameterCanonicalizerTest {
	private static final Logger ourLog = LoggerFactory.getLogger(SearchParameterCanonicalizerTest.class);

	ca.uhn.fhir.model.dstu2.resource.SearchParameter initSearchParamDstu2(){
		ca.uhn.fhir.model.dstu2.resource.SearchParameter sp = new ca.uhn.fhir.model.dstu2.resource.SearchParameter();
		sp.setId("SearchParameter/meal-chef");
		sp.setUrl("http://example.org/SearchParameter/meal-chef");
		sp.setBase(ResourceTypeEnum.RESOURCE);
		sp.setCode("chef");
		sp.setType(SearchParamTypeEnum.REFERENCE);
		sp.setStatus(ConformanceResourceStatusEnum.ACTIVE);
		sp.setXpath("Meal.chef | Observation.subject");
		sp.addTarget(ResourceTypeEnum.RESOURCE);
		sp.addTarget(ResourceTypeEnum.OBSERVATION);
		sp.addUndeclaredExtension(new ExtensionDt(false, EXTENSION_SEARCHPARAM_CUSTOM_BASE_RESOURCE, new StringDt("Meal")));
		sp.addUndeclaredExtension(new ExtensionDt(false, EXTENSION_SEARCHPARAM_CUSTOM_TARGET_RESOURCE, new StringDt("Chef")));
		return sp;
	}

	org.hl7.fhir.dstu3.model.SearchParameter initSearchParamDstu3(){
		org.hl7.fhir.dstu3.model.SearchParameter sp = new org.hl7.fhir.dstu3.model.SearchParameter();
		sp.setId("SearchParameter/meal-chef");
		sp.setUrl("http://example.org/SearchParameter/meal-chef");
		sp.addBase("Resource");
		sp.addBase("Patient");
		sp.setCode("chef");
		sp.setType(org.hl7.fhir.dstu3.model.Enumerations.SearchParamType.REFERENCE);
		sp.setStatus(org.hl7.fhir.dstu3.model.Enumerations.PublicationStatus.ACTIVE);
		sp.setExpression("Meal.chef | Observation.subject");
		sp.addTarget("Resource");
		sp.addTarget("Observation");
		sp.addExtension(EXTENSION_SEARCHPARAM_CUSTOM_BASE_RESOURCE, new org.hl7.fhir.dstu3.model.StringType("Meal"));
		sp.addExtension(EXTENSION_SEARCHPARAM_CUSTOM_TARGET_RESOURCE, new org.hl7.fhir.dstu3.model.StringType("Chef"));
		return sp;
	}

	IBaseResource initSearchParamR4(){
		SearchParameter sp = new SearchParameter();
		sp.setId("SearchParameter/meal-chef");
		sp.setUrl("http://example.org/SearchParameter/meal-chef");
		sp.addBase("Resource");
		sp.addBase("Patient");
		sp.setCode("chef");
		sp.setType(Enumerations.SearchParamType.REFERENCE);
		sp.setStatus(Enumerations.PublicationStatus.ACTIVE);
		sp.setExpression("Meal.chef | Observation.subject");
		sp.addTarget("Resource");
		sp.addTarget("Observation");
		sp.addExtension(EXTENSION_SEARCHPARAM_CUSTOM_BASE_RESOURCE, new StringType("Meal"));
		sp.addExtension(EXTENSION_SEARCHPARAM_CUSTOM_TARGET_RESOURCE, new StringType("Chef"));
		return sp;
	}

	IBaseResource initSearchParamR4B(){
		org.hl7.fhir.r4b.model.SearchParameter sp = new org.hl7.fhir.r4b.model.SearchParameter();
		sp.setId("SearchParameter/meal-chef");
		sp.setUrl("http://example.org/SearchParameter/meal-chef");
		sp.addBase("Resource");
		sp.addBase("Patient");
		sp.setCode("chef");
		sp.setType(org.hl7.fhir.r4b.model.Enumerations.SearchParamType.REFERENCE);
		sp.setStatus(org.hl7.fhir.r4b.model.Enumerations.PublicationStatus.ACTIVE);
		sp.setExpression("Meal.chef | Observation.subject");
		sp.addTarget("Resource");
		sp.addTarget("Observation");
		sp.addExtension(EXTENSION_SEARCHPARAM_CUSTOM_BASE_RESOURCE, new org.hl7.fhir.r4b.model.StringType("Meal"));
		sp.addExtension(EXTENSION_SEARCHPARAM_CUSTOM_TARGET_RESOURCE, new org.hl7.fhir.r4b.model.StringType("Chef"));
		return sp;
	}

	IBaseResource initSearchParamR5(){
		org.hl7.fhir.r5.model.SearchParameter sp = new org.hl7.fhir.r5.model.SearchParameter();
		sp.setId("SearchParameter/meal-chef");
		sp.setUrl("http://example.org/SearchParameter/meal-chef");
		sp.addBase(org.hl7.fhir.r5.model.Enumerations.VersionIndependentResourceTypesAll.RESOURCE);
		sp.addBase(org.hl7.fhir.r5.model.Enumerations.VersionIndependentResourceTypesAll.PATIENT);
		sp.setCode("chef");
		sp.setType(org.hl7.fhir.r5.model.Enumerations.SearchParamType.REFERENCE);
		sp.setStatus(org.hl7.fhir.r5.model.Enumerations.PublicationStatus.ACTIVE);
		sp.setExpression("Meal.chef | Observation.subject");
		sp.addTarget(org.hl7.fhir.r5.model.Enumerations.VersionIndependentResourceTypesAll.RESOURCE);
		sp.addTarget(org.hl7.fhir.r5.model.Enumerations.VersionIndependentResourceTypesAll.OBSERVATION);
		sp.addExtension(EXTENSION_SEARCHPARAM_CUSTOM_BASE_RESOURCE, new org.hl7.fhir.r5.model.StringType("Meal"));
		sp.addExtension(EXTENSION_SEARCHPARAM_CUSTOM_TARGET_RESOURCE, new org.hl7.fhir.r5.model.StringType("Chef"));
		return sp;
	}


	@ParameterizedTest
	@ValueSource(booleans = {false, true})
	public void testCanonicalizeSearchParameterWithCustomType(boolean theConvertToR5) {
		SearchParameter sp = new SearchParameter();
		sp.setId("SearchParameter/meal-chef");
		sp.setUrl("http://example.org/SearchParameter/meal-chef");
		sp.addBase("Meal");
		sp.addBase("Patient");
		sp.setCode("chef");
		sp.setType(Enumerations.SearchParamType.REFERENCE);
		sp.setStatus(Enumerations.PublicationStatus.ACTIVE);
		sp.setExpression("Meal.chef | Observation.subject");
		sp.addTarget("Chef");
		sp.addTarget("Observation");
		IBaseResource searchParamToCanonicalize = sp;
		SearchParameterCanonicalizer svc;
		if (theConvertToR5) {
			VersionCanonicalizer versionCanonicalizer = new VersionCanonicalizer(FhirContext.forR4Cached());
			searchParamToCanonicalize = versionCanonicalizer.searchParameterToCanonical(sp);
			FhirContext fhirContextR5 = FhirContext.forR5Cached();
			ourLog.info("R5 Subscription: {}", fhirContextR5.newJsonParser().setPrettyPrint(true).encodeResourceToString(searchParamToCanonicalize));
				svc = new SearchParameterCanonicalizer(fhirContextR5);
		} else {
			svc = new SearchParameterCanonicalizer(FhirContext.forR4Cached());
		}

		RuntimeSearchParam output = svc.canonicalizeSearchParameter(searchParamToCanonicalize);
		assertEquals("chef", output.getName());
		assertEquals(RestSearchParameterTypeEnum.REFERENCE, output.getParamType());
		assertEquals(RuntimeSearchParam.RuntimeSearchParamStatusEnum.ACTIVE, output.getStatus());
		assertThat(output.getPathsSplit()).containsExactlyInAnyOrder("Meal.chef", "Observation.subject");
		assertThat(output.getBase()).containsExactlyInAnyOrder("Meal", "Patient");
		assertThat(output.getTargets()).containsExactly("Chef", "Observation");
	}

	@ParameterizedTest
	@ValueSource(strings = {"Dstu2", "Dstu3", "R4", "R4B", "R5"})
	public void testCanonicalizeSearchParameterWithCustomTypeAllVersion(String version) {
		SearchParameterCanonicalizer svc;
		IBaseResource searchParamToCanonicalize;

		switch (version){
			case "Dstu2":
				searchParamToCanonicalize = initSearchParamDstu2();
				svc = new SearchParameterCanonicalizer(FhirContext.forDstu2Cached());
				break;
			case "Dstu3":
				searchParamToCanonicalize = initSearchParamDstu3();
				svc = new SearchParameterCanonicalizer(FhirContext.forDstu3Cached());
				break;
			case "R4":
				searchParamToCanonicalize = initSearchParamR4();
				svc = new SearchParameterCanonicalizer(FhirContext.forR4Cached());
				break;
			case "R4B":
				searchParamToCanonicalize = initSearchParamR4B();
				svc = new SearchParameterCanonicalizer(FhirContext.forR4BCached());
				break;
			default:
				searchParamToCanonicalize = initSearchParamR5();
				svc = new SearchParameterCanonicalizer(FhirContext.forR5Cached());
				break;
		}

		RuntimeSearchParam output = svc.canonicalizeSearchParameter(searchParamToCanonicalize);
		assertEquals("chef", output.getName());
		assertEquals(RestSearchParameterTypeEnum.REFERENCE, output.getParamType());
		assertEquals(RuntimeSearchParam.RuntimeSearchParamStatusEnum.ACTIVE, output.getStatus());
		assertThat(output.getPathsSplit()).containsExactlyInAnyOrder("Meal.chef", "Observation.subject");
		// DSTU2 Resources must only have 1 base
		if ("Dstu2".equals(version)){
			assertThat(output.getBase()).containsExactlyInAnyOrder("Meal");
		} else {
			assertThat(output.getBase()).containsExactlyInAnyOrder("Meal", "Patient");
		}
		assertThat(output.getTargets()).containsExactlyInAnyOrder("Chef", "Observation");
		assertThat(output.getBase()).doesNotContain("Resource");
		assertThat(output.getTargets()).doesNotContain("Resource");
	}

	@ParameterizedTest
	@CsvSource(useHeadersInDisplayName = true, textBlock = """
		Type
		code
		uri
		url
		Identifier
		Coding
		""")
	void testCanonicalizeComboSearchParameterWithAllowList(String theType) {
		Type value = switch(theType) {
			case "code" -> new CodeType().setValue("hello");
			case "uri" -> new UriType().setValue("hello");
			case "url" -> new UrlType().setValue("hello");
			case "Identifier" -> new Identifier().setSystem("http://system").setValue("value");
			case "Coding" -> new Coding().setSystem("http://system").setCode("value");
			default -> throw new IllegalArgumentException("Unknown type: " + theType);
		};

		SearchParameter sp = new SearchParameter();
		sp.setId("SearchParameter/patient-names-and-maritalstatus");
		sp.setType(Enumerations.SearchParamType.COMPOSITE);
		sp.setStatus(Enumerations.PublicationStatus.ACTIVE);
		sp.addBase("Patient");
		sp.addComponent()
			.setExpression("Patient")
			.setDefinition("SearchParameter/patient-family")
				.addExtension(HapiExtensions.EXT_SP_COMBO_COMPONENT_VALUE_ALLOWLIST, value);
		sp.addExtension()
			.setUrl(HapiExtensions.EXT_SP_UNIQUE)
			.setValue(new BooleanType(false));

		// Test
		SearchParameterCanonicalizer svc = new SearchParameterCanonicalizer(FhirContext.forR4Cached());
		RuntimeSearchParam canonicalized = svc.canonicalizeSearchParameter(sp);

		// Verify
		Set<RuntimeSearchParam.ComboInclude> allowList = canonicalized.getComponents().get(0).getValueAllowList();
		if (value.hasPrimitiveValue()) {
			assertNull(allowList.iterator().next().system());
			assertEquals("hello", allowList.iterator().next().value());
		} else {
			assertEquals("http://system", allowList.iterator().next().system());
			assertEquals("value", allowList.iterator().next().value());
		}
	}

}
