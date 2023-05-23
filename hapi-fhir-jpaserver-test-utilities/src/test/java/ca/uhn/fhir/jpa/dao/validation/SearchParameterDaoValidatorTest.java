package ca.uhn.fhir.jpa.dao.validation;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.searchparam.registry.SearchParameterCanonicalizer;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;
import ca.uhn.fhir.util.HapiExtensions;
import ca.uhn.hapi.converters.canonical.VersionCanonicalizer;
import org.hl7.fhir.r5.model.BooleanType;
import org.hl7.fhir.r5.model.Enumerations;
import org.hl7.fhir.r5.model.Extension;
import org.hl7.fhir.r5.model.SearchParameter;
import org.hl7.fhir.r5.model.SearchParameter.SearchParameterComponentComponent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.stream.Stream;

import static org.hl7.fhir.r5.model.Enumerations.PublicationStatus.ACTIVE;
import static org.hl7.fhir.r5.model.Enumerations.SearchParamType.COMPOSITE;
import static org.hl7.fhir.r5.model.Enumerations.SearchParamType.REFERENCE;
import static org.hl7.fhir.r5.model.Enumerations.SearchParamType.TOKEN;
import static org.hl7.fhir.r5.model.Enumerations.VersionIndependentResourceTypesAll.OBSERVATION;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SearchParameterDaoValidatorTest {

	@Spy
	private FhirContext myFhirContext = FhirContext.forR4Cached();
	@Mock
	private ISearchParamRegistry mySearchParamRegistry;
	@Spy
	private JpaStorageSettings myStorageSettings = new JpaStorageSettings();
	@InjectMocks
	private SearchParameterDaoValidator mySvc;

	private VersionCanonicalizer myVersionCanonicalizer = new VersionCanonicalizer(myFhirContext);

	private SearchParameterCanonicalizer mySearchParameterCanonicalizer = new SearchParameterCanonicalizer(myFhirContext);

	private RuntimeSearchParam myObservationPatientRuntimeSearchParam;
	private RuntimeSearchParam myObservationCodeRuntimeSearchParam;

	private static String SP_COMPONENT_DEFINITION_OF_TYPE_TOKEN = "SearchParameter/observation-code";
	private static String SP_COMPONENT_DEFINITION_OF_TYPE_REFERENCE = "SearchParameter/observation-patient";

	@BeforeEach
	public void before() {

		org.hl7.fhir.r5.model.SearchParameter observationCodeSp = createSearchParameter(TOKEN,"SearchParameter/observation-code", "observation-code", "Observation.code");
		myObservationCodeRuntimeSearchParam = mySearchParameterCanonicalizer.canonicalizeSearchParameter(observationCodeSp);

		org.hl7.fhir.r5.model.SearchParameter observationPatientSp = createSearchParameter(REFERENCE, "SearchParameter/observation-patient", "observation-patient", "Observation.subject.where(resolve() is Patient");
		myObservationPatientRuntimeSearchParam = mySearchParameterCanonicalizer.canonicalizeSearchParameter(observationPatientSp);

	}

	@Test
	public void testValidateSubscription() {
		org.hl7.fhir.r4.model.SearchParameter sp = new org.hl7.fhir.r4.model.SearchParameter();
		sp.setId("SearchParameter/patient-eyecolour");
		sp.setUrl("http://example.org/SearchParameter/patient-eyecolour");
		sp.addBase("Patient");
		sp.setCode("eyecolour");
		sp.setType(org.hl7.fhir.r4.model.Enumerations.SearchParamType.TOKEN);
		sp.setStatus(org.hl7.fhir.r4.model.Enumerations.PublicationStatus.ACTIVE);
		sp.setExpression("Patient.extension('http://foo')");
		sp.addTarget("Patient");

		SearchParameter canonicalSp = myVersionCanonicalizer.searchParameterToCanonical(sp);
		mySvc.validate(canonicalSp);
	}

	@Test
	public void testValidateSubscriptionWithCustomType() {
		org.hl7.fhir.r4.model.SearchParameter sp = new org.hl7.fhir.r4.model.SearchParameter();
		sp.setId("SearchParameter/meal-chef");
		sp.setUrl("http://example.org/SearchParameter/meal-chef");
		sp.addBase("Meal");
		sp.setCode("chef");
		sp.setType(org.hl7.fhir.r4.model.Enumerations.SearchParamType.REFERENCE);
		sp.setStatus(org.hl7.fhir.r4.model.Enumerations.PublicationStatus.ACTIVE);
		sp.setExpression("Meal.chef");
		sp.addTarget("Chef");

		SearchParameter canonicalSp = myVersionCanonicalizer.searchParameterToCanonical(sp);
		mySvc.validate(canonicalSp);
	}

	@ParameterizedTest
	@MethodSource("extensionProvider")
	public void testMethodValidate_nonUniqueComboAndCompositeSearchParamWithComponentOfTypeReference_isNotAllowed(Extension theExtension) {

		//todo: define provide method getActiveSearchParamByComponentDefinition with appropriate tests
		when(mySearchParamRegistry.getActiveSearchParamByComponentDefinition(eq(SP_COMPONENT_DEFINITION_OF_TYPE_TOKEN))).thenReturn(myObservationCodeRuntimeSearchParam);
		when(mySearchParamRegistry.getActiveSearchParamByComponentDefinition(eq(SP_COMPONENT_DEFINITION_OF_TYPE_REFERENCE))).thenReturn(myObservationPatientRuntimeSearchParam);

		SearchParameter sp = createSearchParameter(COMPOSITE, "SearchParameter/patient-code", "patient-code", "Observation");
		sp.addExtension(theExtension);

		sp.addComponent(new SearchParameterComponentComponent()
			.setDefinition(SP_COMPONENT_DEFINITION_OF_TYPE_TOKEN));
		sp.addComponent(new SearchParameterComponentComponent()
			.setDefinition(SP_COMPONENT_DEFINITION_OF_TYPE_REFERENCE));

		try {
			mySvc.validate(sp);
			fail();
		} catch (UnprocessableEntityException ex){
			assertTrue(ex.getMessage().startsWith("HAPI-2347: "));
			assertTrue(ex.getMessage().contains("Invalid component search parameter type: REFERENCE in component.definition: SearchParameter/observation-patient"));
		}

	}

	@Test
	public void testMethodValidate_uniqueComboSearchParamWithComponentOfTypeReference_isValid() {

		when(mySearchParamRegistry.getActiveSearchParamByComponentDefinition(eq(SP_COMPONENT_DEFINITION_OF_TYPE_TOKEN))).thenReturn(myObservationCodeRuntimeSearchParam);
		when(mySearchParamRegistry.getActiveSearchParamByComponentDefinition(eq(SP_COMPONENT_DEFINITION_OF_TYPE_REFERENCE))).thenReturn(myObservationPatientRuntimeSearchParam);

		SearchParameter sp = createSearchParameter(COMPOSITE, "SearchParameter/patient-code", "patient-code", "Observation");
		sp.addExtension(new Extension(HapiExtensions.EXT_SP_UNIQUE, new BooleanType(true)));

		sp.addComponent(new SearchParameterComponentComponent()
			.setDefinition(SP_COMPONENT_DEFINITION_OF_TYPE_TOKEN));
		sp.addComponent(new SearchParameterComponentComponent()
			.setDefinition(SP_COMPONENT_DEFINITION_OF_TYPE_REFERENCE));

		mySvc.validate(sp);

	}

	@ParameterizedTest
	@MethodSource("compositeSpProvider")
	// we're testing for:
	// SP of type composite,
	// SP of type combo composite non-unique,
	// SP of type combo composite unique,
	public void testMethodValidate_allCompositeSpTypesWithComponentOfValidType_isValid(SearchParameter theSearchParameter) {


		theSearchParameter.addComponent(new SearchParameter.SearchParameterComponentComponent().setDefinition("SP_COMPONENT_DEFINITION_OF_TYPE_TOKEN"));
		// todo: add other valid sp types

		mySvc.validate(theSearchParameter);

	}

	private static org.hl7.fhir.r5.model.SearchParameter createSearchParameter(Enumerations.SearchParamType theToken, String theId, String theCodeValue, String theExpression){

		SearchParameter retVal = new SearchParameter();
		retVal.setId(theId);
		retVal.setUrl("http://example.org/" + theId);
		retVal.addBase(OBSERVATION);
		retVal.setCode(theCodeValue);
		retVal.setType(COMPOSITE);
		retVal.setStatus(ACTIVE);
		retVal.setExpression(theExpression);

		return retVal;
	}

	static Stream<Arguments> extensionProvider() {
		return Stream.of(
			Arguments.of(
				new Extension(HapiExtensions.EXT_SP_UNIQUE, new BooleanType(false)), // composite SP of type combo with non-unique index
				null) // composite SP
		);
	}

	static Stream<Arguments> compositeSpProvider() {
		return Stream.of(
			Arguments.of(
				createSearchParameter(Enumerations.SearchParamType.COMPOSITE, "SearchParameter/any-type", "any-type", "Observation")
					.addExtension(new Extension(HapiExtensions.EXT_SP_UNIQUE, new BooleanType(false))), // composite SP of type combo with non-unique index

				createSearchParameter(Enumerations.SearchParamType.COMPOSITE, "SearchParameter/any-type", "any-type", "Observation")
					.addExtension(new Extension(HapiExtensions.EXT_SP_UNIQUE, new BooleanType(true))), // composite SP of type combo with unique index

				createSearchParameter(Enumerations.SearchParamType.COMPOSITE, "SearchParameter/any-type", "any-type", "Observation")) // composite SP
		);
	}

}
