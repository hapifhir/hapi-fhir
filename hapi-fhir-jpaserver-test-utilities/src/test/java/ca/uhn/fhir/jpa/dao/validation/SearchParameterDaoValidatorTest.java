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
import org.hl7.fhir.r5.model.StringType;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.hl7.fhir.r5.model.Enumerations.PublicationStatus.ACTIVE;
import static org.hl7.fhir.r5.model.Enumerations.SearchParamType.COMPOSITE;
import static org.hl7.fhir.r5.model.Enumerations.SearchParamType.DATE;
import static org.hl7.fhir.r5.model.Enumerations.SearchParamType.NUMBER;
import static org.hl7.fhir.r5.model.Enumerations.SearchParamType.QUANTITY;
import static org.hl7.fhir.r5.model.Enumerations.SearchParamType.REFERENCE;
import static org.hl7.fhir.r5.model.Enumerations.SearchParamType.STRING;
import static org.hl7.fhir.r5.model.Enumerations.SearchParamType.TOKEN;
import static org.hl7.fhir.r5.model.Enumerations.SearchParamType.URI;
import static org.hl7.fhir.r5.model.Enumerations.VersionIndependentResourceTypesAll.OBSERVATION;
import static org.hl7.fhir.r5.model.Enumerations.VersionIndependentResourceTypesAll.PATIENT;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
public class SearchParameterDaoValidatorTest {

    private static final String SP_COMPONENT_DEFINITION_OF_TYPE_TOKEN = "SearchParameter/observation-code";
    private static final String SP_COMPONENT_DEFINITION_OF_TYPE_REFERENCE = "SearchParameter/observation-patient";
    private static final String SP_COMPONENT_DEFINITION_OF_TYPE_STRING = "SearchParameter/observation-markdown";
    private static final String SP_COMPONENT_DEFINITION_OF_TYPE_DATE = "SearchParameter/observation-date";
    private static final String SP_COMPONENT_DEFINITION_OF_TYPE_QUANTITY = "SearchParameter/observation-code";
    private static final String SP_COMPONENT_DEFINITION_OF_TYPE_URI = "SearchParameter/component-value-canonical";
    private static final String SP_COMPONENT_DEFINITION_OF_TYPE_NUMBER = "SearchParameter/component-value-number";
    @Spy
    private FhirContext myFhirContext = FhirContext.forR5Cached();
    private final VersionCanonicalizer myVersionCanonicalizer = new VersionCanonicalizer(myFhirContext);
    private final SearchParameterCanonicalizer mySearchParameterCanonicalizer = new SearchParameterCanonicalizer(myFhirContext);
    @Mock
    private ISearchParamRegistry mySearchParamRegistry;
    @Spy
    private JpaStorageSettings myStorageSettings = new JpaStorageSettings();
    @InjectMocks
    private SearchParameterDaoValidator mySvc;

    @BeforeEach
    public void before() {
        createAndMockSearchParameter(TOKEN, SP_COMPONENT_DEFINITION_OF_TYPE_TOKEN, "observation-code", "Observation.code");
        createAndMockSearchParameter(REFERENCE, SP_COMPONENT_DEFINITION_OF_TYPE_REFERENCE, "observation-patient", "Observation.subject.where(resolve() is Patient");
        createAndMockSearchParameter(STRING, SP_COMPONENT_DEFINITION_OF_TYPE_DATE, "observation-category", "Observation.value.ofType(markdown)");
        createAndMockSearchParameter(DATE, SP_COMPONENT_DEFINITION_OF_TYPE_STRING, "observation-date", "Observation.value.ofType(dateTime)");
        createAndMockSearchParameter(QUANTITY, SP_COMPONENT_DEFINITION_OF_TYPE_QUANTITY, "observation-quantity", "Observation.value.ofType(Quantity)");
        createAndMockSearchParameter(URI, SP_COMPONENT_DEFINITION_OF_TYPE_URI, "observation-component-value-canonical", "Observation.component.value.ofType(canonical)");
        createAndMockSearchParameter(NUMBER, SP_COMPONENT_DEFINITION_OF_TYPE_NUMBER, "observation-component-value-number", "Observation.component.valueInteger");
    }

    private void createAndMockSearchParameter(Enumerations.SearchParamType theType, String theDefinition, String theCodeValue, String theExpression) {
        SearchParameter observationCodeSp = createSearchParameter(theType, theDefinition, theCodeValue, theExpression);
        RuntimeSearchParam observationCodeRuntimeSearchParam = mySearchParameterCanonicalizer.canonicalizeSearchParameter(observationCodeSp);
        lenient().when(mySearchParamRegistry.getActiveSearchParamByUrl(eq(theDefinition))).thenReturn(observationCodeRuntimeSearchParam);
    }
    
    @Test
    public void testValidateSubscription() {
        SearchParameter sp = new SearchParameter();
        sp.setId("SearchParameter/patient-eyecolour");
        sp.setUrl("http://example.org/SearchParameter/patient-eyecolour");
        sp.addBase(PATIENT);
        sp.setCode("eyecolour");
        sp.setType(TOKEN);
        sp.setStatus(ACTIVE);
        sp.setExpression("Patient.extension('http://foo')");
        sp.addTarget(PATIENT);

        SearchParameter canonicalSp = myVersionCanonicalizer.searchParameterToCanonical(sp);
        mySvc.validate(canonicalSp);
    }

    @Test
    public void testValidateSubscriptionWithCustomType() {
        SearchParameter sp = new SearchParameter();
        sp.setId("SearchParameter/meal-chef");
        sp.setUrl("http://example.org/SearchParameter/meal-chef");
        sp.addExtension(new Extension(HapiExtensions.EXTENSION_SEARCHPARAM_CUSTOM_BASE_RESOURCE).setValue(new StringType("Meal")));
        sp.addExtension(new Extension(HapiExtensions.EXTENSION_SEARCHPARAM_CUSTOM_TARGET_RESOURCE).setValue(new StringType("Chef")));
        sp.setCode("chef");
        sp.setType(REFERENCE);
        sp.setStatus(ACTIVE);
        sp.setExpression("Meal.chef");

        SearchParameter canonicalSp = myVersionCanonicalizer.searchParameterToCanonical(sp);
        mySvc.validate(canonicalSp);
    }

    @ParameterizedTest
    @MethodSource("extensionProvider")
    public void testMethodValidate_nonUniqueComboAndCompositeSearchParamWithComponentOfTypeReference_isNotAllowed(Extension theExtension) {
        SearchParameter sp = createSearchParameter(COMPOSITE, "SearchParameter/patient-code", "patient-code", "Observation");
        sp.addExtension(theExtension);

        sp.addComponent(new SearchParameterComponentComponent().setDefinition(SP_COMPONENT_DEFINITION_OF_TYPE_TOKEN));
        sp.addComponent(new SearchParameterComponentComponent().setDefinition(SP_COMPONENT_DEFINITION_OF_TYPE_REFERENCE));

        try {
            mySvc.validate(sp);
			fail("");
        } catch (UnprocessableEntityException ex) {
					assertThat(ex.getMessage()).startsWith("HAPI-2347: ");
					assertThat(ex.getMessage()).contains("Invalid component search parameter type: REFERENCE in component.definition: http://example.org/SearchParameter/observation-patient");
        }
    }

    @Test
    public void testMethodValidate_uniqueComboSearchParamWithComponentOfTypeReference_isValid() {
        SearchParameter sp = createSearchParameter(COMPOSITE, "SearchParameter/patient-code", "patient-code", "Observation");
        sp.addExtension(new Extension(HapiExtensions.EXT_SP_UNIQUE, new BooleanType(true)));

        sp.addComponent(new SearchParameterComponentComponent()
                .setDefinition(SP_COMPONENT_DEFINITION_OF_TYPE_TOKEN));
        sp.addComponent(new SearchParameterComponentComponent()
                .setDefinition(SP_COMPONENT_DEFINITION_OF_TYPE_REFERENCE));

        mySvc.validate(sp);
    }

    @ParameterizedTest
    @MethodSource("comboSpProvider")
    public void testMethodValidate_comboSearchParamsWithNumberUriComponents_isValid(SearchParameter theSearchParameter) {
        theSearchParameter.addComponent(new SearchParameterComponentComponent()
                .setDefinition(SP_COMPONENT_DEFINITION_OF_TYPE_URI));
        theSearchParameter.addComponent(new SearchParameterComponentComponent()
                .setDefinition(SP_COMPONENT_DEFINITION_OF_TYPE_NUMBER));

        mySvc.validate(theSearchParameter);
    }

    @Test
    public void testMethodValidate_compositeSearchParamsWithNumberUriComponents_isNotAllowed() {
        SearchParameter sp = createSearchParameter(COMPOSITE, "SearchParameter/component-value-uri-number", "component-value-uri-number", "Observation");

        sp.addComponent(new SearchParameterComponentComponent().setDefinition(SP_COMPONENT_DEFINITION_OF_TYPE_URI));
        sp.addComponent(new SearchParameterComponentComponent().setDefinition(SP_COMPONENT_DEFINITION_OF_TYPE_NUMBER));

        try {
            mySvc.validate(sp);
			fail("");
        } catch (UnprocessableEntityException ex) {
					assertThat(ex.getMessage()).startsWith("HAPI-2347: ");
					assertThat(ex.getMessage()).contains("Invalid component search parameter type: URI in component.definition: http://example.org/SearchParameter/component-value-canonical");
        }
    }

    @ParameterizedTest
    @MethodSource("compositeSpProvider")
    // we're testing for:
    // SP of type composite,
    // SP of type combo composite non-unique,
    // SP of type combo composite unique,
    public void testMethodValidate_allCompositeSpTypesWithComponentOfValidType_isValid(SearchParameter theSearchParameter) {

        theSearchParameter.addComponent(new SearchParameter.SearchParameterComponentComponent()
                .setDefinition(SP_COMPONENT_DEFINITION_OF_TYPE_TOKEN).setExpression("Observation"));
        theSearchParameter.addComponent(new SearchParameter.SearchParameterComponentComponent()
                .setDefinition(SP_COMPONENT_DEFINITION_OF_TYPE_QUANTITY).setExpression("Observation"));
        theSearchParameter.addComponent(new SearchParameter.SearchParameterComponentComponent()
                .setDefinition(SP_COMPONENT_DEFINITION_OF_TYPE_STRING).setExpression("Observation"));
        theSearchParameter.addComponent(new SearchParameter.SearchParameterComponentComponent()
                .setDefinition(SP_COMPONENT_DEFINITION_OF_TYPE_DATE).setExpression("Observation"));

        mySvc.validate(theSearchParameter);
    }

    private static SearchParameter createSearchParameter(Enumerations.SearchParamType theType, String theId, String theCodeValue, String theExpression) {

        SearchParameter retVal = new SearchParameter();
        retVal.setId(theId);
        retVal.setUrl("http://example.org/" + theId);
        retVal.addBase(OBSERVATION);
        retVal.setCode(theCodeValue);
        retVal.setType(theType);
        retVal.setStatus(ACTIVE);
        retVal.setExpression(theExpression);

        return retVal;
    }

    static Stream<Arguments> extensionProvider() {
        return Stream.of(
                Arguments.of(
                        new Extension(HapiExtensions.EXT_SP_UNIQUE, new BooleanType(false))), // composite SP of type combo with non-unique index
                Arguments.of((Object) null) // composite SP
        );
    }

    static Stream<Arguments> comboSpProvider() {
        return Stream.of(
                Arguments.of(createSearchParameter(Enumerations.SearchParamType.COMPOSITE, "SearchParameter/any-type", "any-type", "Observation")
                        .addExtension(new Extension(HapiExtensions.EXT_SP_UNIQUE, new BooleanType(false)))), // composite SP of type combo with non-unique index

                Arguments.of(createSearchParameter(Enumerations.SearchParamType.COMPOSITE, "SearchParameter/any-type", "any-type", "Observation")
                        .addExtension(new Extension(HapiExtensions.EXT_SP_UNIQUE, new BooleanType(true)))) // composite SP of type combo with unique index
        );
    }

    static Stream<Arguments> compositeSpProvider() {
        return Stream.concat(comboSpProvider(), Stream.of(
                Arguments.of(createSearchParameter(Enumerations.SearchParamType.COMPOSITE, "SearchParameter/any-type", "any-type", "Observation")) // composite SP
        ));
    }
}
