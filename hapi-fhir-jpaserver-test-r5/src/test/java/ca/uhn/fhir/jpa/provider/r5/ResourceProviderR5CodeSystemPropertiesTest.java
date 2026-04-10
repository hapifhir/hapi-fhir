package ca.uhn.fhir.jpa.provider.r5;

import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.context.support.LookupCodeRequest;
import ca.uhn.fhir.context.support.ValidationSupportContext;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.jpa.provider.CodeSystemLookupWithPropertiesUtil;
import ca.uhn.fhir.rest.gclient.IOperationUntypedWithInputAndPartialOutput;
import org.hl7.fhir.common.hapi.validation.util.TermConceptPropertyTypeEnum;
import org.hl7.fhir.r5.model.BooleanType;
import org.hl7.fhir.r5.model.CodeSystem;
import org.hl7.fhir.r5.model.CodeSystem.ConceptPropertyComponent;
import org.hl7.fhir.r5.model.CodeType;
import org.hl7.fhir.r5.model.Coding;
import org.hl7.fhir.r5.model.DateTimeType;
import org.hl7.fhir.r5.model.DecimalType;
import org.hl7.fhir.r5.model.IntegerType;
import org.hl7.fhir.r5.model.Parameters;
import org.hl7.fhir.r5.model.Parameters.ParametersParameterComponent;
import org.hl7.fhir.r5.model.StringType;
import org.hl7.fhir.r5.model.UriType;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

import static ca.uhn.fhir.jpa.provider.CodeSystemLookupWithPropertiesUtil.ourCode;
import static ca.uhn.fhir.jpa.provider.CodeSystemLookupWithPropertiesUtil.ourCodeSystemId;
import static ca.uhn.fhir.jpa.provider.CodeSystemLookupWithPropertiesUtil.ourCodeSystemUrl;
import static ca.uhn.fhir.jpa.provider.CodeSystemLookupWithPropertiesUtil.ourPropertyA;
import static ca.uhn.fhir.jpa.provider.CodeSystemLookupWithPropertiesUtil.ourPropertyB;
import static ca.uhn.fhir.jpa.provider.CodeSystemLookupWithPropertiesUtil.ourPropertyC;
import static ca.uhn.fhir.jpa.provider.CodeSystemLookupWithPropertiesUtil.ourPropertyValueA;
import static ca.uhn.fhir.jpa.provider.CodeSystemLookupWithPropertiesUtil.ourPropertyValueB;
import static ca.uhn.fhir.jpa.provider.CodeSystemLookupWithPropertiesUtil.propertyCode;
import static ca.uhn.fhir.jpa.provider.CodeSystemLookupWithPropertiesUtil.propertyCodeSystem;
import static ca.uhn.fhir.jpa.provider.CodeSystemLookupWithPropertiesUtil.propertyDisplay;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ResourceProviderR5CodeSystemPropertiesTest extends BaseResourceProviderR5Test {

	@ParameterizedTest
	@EnumSource(TermConceptPropertyTypeEnum.class)
	void testLookup_WithProperties_VariousPropertyTypes(TermConceptPropertyTypeEnum theType) {
		// Setup
		createCodeSystemWithConceptAndPropertyOfType(theType);

		// Test
		Parameters output = myClient
			.operation()
			.onType(CodeSystem.class)
			.named(JpaConstants.OPERATION_LOOKUP)
			.withParameter(Parameters.class, "system", new UriType(ourCodeSystemUrl))
			.andParameter("code", new CodeType("code"))
			.andParameter("property", new CodeType("property-code"))
			.execute();

		// Verify
		ParametersParameterComponent part = output.getParameter("property").getPart("value");
		switch (theType) {
			case STRING -> assertEquals("value", ((StringType) part.getValue()).getValueAsString());
			case CODE -> assertEquals("value", ((CodeType) part.getValue()).getValueAsString());
			case BOOLEAN -> assertEquals(Boolean.TRUE, ((BooleanType) part.getValue()).getValue());
			case INTEGER -> assertEquals(123, ((IntegerType) part.getValue()).getValue());
			case DECIMAL -> assertEquals(1.23, ((DecimalType) part.getValue()).getValue().doubleValue(), 0.000001);
			case DATETIME -> assertEquals("2022-01-01", ((DateTimeType) part.getValue()).getValueAsString());
			case CODING -> {
				assertEquals("value-system", ((Coding) part.getValue()).getSystem());
				assertEquals("value-code", ((Coding) part.getValue()).getCode());
				assertEquals("value-display", ((Coding) part.getValue()).getDisplay());
			}
			default -> throw new IllegalArgumentException("Unsupported property type: " + theType);
		}
	}


	@ParameterizedTest
	@EnumSource(TermConceptPropertyTypeEnum.class)
	void testLookupViaValidationSupport_WithProperties_VariousPropertyTypes(TermConceptPropertyTypeEnum theType) {
		// Setup
		createCodeSystemWithConceptAndPropertyOfType(theType);

		// Test
		LookupCodeRequest lookupRequest = new LookupCodeRequest(ourCodeSystemUrl, "code", null, List.of("property-code"));
		IValidationSupport.LookupCodeResult lookupResult = myValidationSupport.lookupCode(new ValidationSupportContext(myValidationSupport), lookupRequest);

		// Verify
		assertNotNull(lookupResult);
		assertEquals(1, lookupResult.getProperties().size());

		IValidationSupport.BaseConceptProperty property = lookupResult.getProperties().get(0);
		assertEquals("property-code", property.getPropertyName());
		switch (theType) {
			case STRING -> assertEquals("value", ((IValidationSupport.StringConceptProperty) property).getValue());
			case CODE -> assertEquals("value", ((IValidationSupport.CodeConceptProperty) property).getValue());
			case BOOLEAN -> assertEquals(Boolean.TRUE, ((IValidationSupport.BooleanConceptProperty) property).getValue());
			case INTEGER -> assertEquals(123, ((IValidationSupport.IntegerConceptProperty) property).getValue());
			case DECIMAL -> assertEquals(1.23, ((IValidationSupport.DecimalConceptProperty) property).getValue().doubleValue(), 0.000001);
			case DATETIME -> assertEquals("2022-01-01", ((IValidationSupport.DateTimeConceptProperty) property).getValue());
			case CODING -> {
				assertEquals("value-system", ((IValidationSupport.CodingConceptProperty) property).getCodeSystem());
				assertEquals("value-code", ((IValidationSupport.CodingConceptProperty) property).getCode());
				assertEquals("value-display", ((IValidationSupport.CodingConceptProperty) property).getDisplay());
			}
			default -> throw new IllegalArgumentException("Unsupported property type: " + theType);
		}
	}

	private void createCodeSystemWithConceptAndPropertyOfType(TermConceptPropertyTypeEnum theType) {
		CodeSystem codeSystem = new CodeSystem();
		codeSystem.setId(ourCodeSystemId);
		codeSystem.setUrl(ourCodeSystemUrl);
		CodeSystem.ConceptDefinitionComponent concept = codeSystem.addConcept().setCode("code").setDisplay("display");
		ConceptPropertyComponent property = concept.addProperty();
		property.setCode("property-code");
		switch (theType) {
			case CODE -> property.setValue(new CodeType("value"));
			case STRING -> property.setValue(new StringType("value"));
			case CODING -> property.setValue(new Coding("value-system", "value-code", "value-display"));
			case BOOLEAN -> property.setValue(new BooleanType(true));
			case INTEGER -> property.setValue(new IntegerType(123));
			case DECIMAL -> property.setValue(new DecimalType(1.23));
			case DATETIME -> property.setValue(new DateTimeType("2022-01-01"));
			default -> throw new IllegalArgumentException("Unsupported property type: " + theType);
		}
		myCodeSystemDao.create(codeSystem, newSrd());
	}

	@ParameterizedTest
	@MethodSource(value = "parametersLookup")
	void testLookup_withProperties_returnsCorrectParameters(List<String> theLookupProperties, List<String> theExpectedReturnedProperties) {
		// setup
		CodeSystem codeSystem = new CodeSystem();
		codeSystem.setId(ourCodeSystemId);
		codeSystem.setUrl(ourCodeSystemUrl);
		CodeSystem.ConceptDefinitionComponent concept = codeSystem.addConcept().setCode(ourCode)
			.addProperty(new ConceptPropertyComponent().setCode(ourPropertyA).setValue(new StringType(ourPropertyValueA)))
			.addProperty(new ConceptPropertyComponent().setCode(ourPropertyB).setValue(new StringType(ourPropertyValueB)))
			.addProperty(new ConceptPropertyComponent().setCode(ourPropertyC).setValue(new Coding(propertyCodeSystem, propertyCode, propertyDisplay)));
		myCodeSystemDao.create(codeSystem, mySrd);

		// test
		IOperationUntypedWithInputAndPartialOutput<Parameters> respParam = myClient
			.operation()
			.onType(CodeSystem.class)
			.named(JpaConstants.OPERATION_LOOKUP)
			.withParameter(Parameters.class, "code", new CodeType(ourCode))
			.andParameter("system", new UriType(ourCodeSystemUrl));

		theLookupProperties.forEach(p -> respParam.andParameter("property", new CodeType(p)));
		Parameters parameters = respParam.execute();

		// verify
		if (theExpectedReturnedProperties.isEmpty()) {
			assertFalse(parameters.hasParameter("property"));
			return;
		}

		assertTrue(parameters.hasParameter("property"));
		Iterator<ParametersParameterComponent> parameterPropertyIterator = parameters.getParameters("property").iterator();

		Iterator<ConceptPropertyComponent> propertyIterator = concept.getProperty().stream()
			.filter(property -> theExpectedReturnedProperties.contains(property.getCode())).iterator();

		while (propertyIterator.hasNext()) {
			ConceptPropertyComponent property = propertyIterator.next();

			assertTrue(parameterPropertyIterator.hasNext());
			ParametersParameterComponent parameter = parameterPropertyIterator.next();
			Iterator<ParametersParameterComponent> parameterPartIterator = parameter.getPart().iterator();

			parameter = parameterPartIterator.next();
			assertEquals("code", parameter.getName());
			assertEquals(property.getCode(), ((CodeType) parameter.getValue()).getValue());

			parameter = parameterPartIterator.next();
			assertEquals("value", parameter.getName());
			assertTrue(property.getValue().equalsShallow(parameter.getValue()));

			assertFalse(parameterPartIterator.hasNext());
		}
	}

	public static Stream<Arguments> parametersLookup() {
		return CodeSystemLookupWithPropertiesUtil.parametersLookupWithProperties();
	}
}
