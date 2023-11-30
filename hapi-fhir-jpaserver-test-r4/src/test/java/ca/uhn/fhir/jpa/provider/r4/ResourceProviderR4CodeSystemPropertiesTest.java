package ca.uhn.fhir.jpa.provider.r4;

import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.jpa.provider.BaseResourceProviderR4Test;
import ca.uhn.fhir.jpa.provider.CodeSystemLookupWithPropertiesUtil;
import ca.uhn.fhir.rest.gclient.IOperationUntypedWithInputAndPartialOutput;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.CodeSystem.ConceptDefinitionComponent;
import org.hl7.fhir.r4.model.CodeSystem.ConceptPropertyComponent;
import org.hl7.fhir.r4.model.CodeType;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Parameters.ParametersParameterComponent;
import org.hl7.fhir.r4.model.StringType;
import org.hl7.fhir.r4.model.UriType;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
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
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ResourceProviderR4CodeSystemPropertiesTest extends BaseResourceProviderR4Test {
   public static Stream<Arguments> parametersLookup() {
      return CodeSystemLookupWithPropertiesUtil.parametersLookupWithProperties();
   }

   @ParameterizedTest
   @MethodSource(value = "parametersLookup")
   public void testLookup_withProperties_returnsCorrectParameters(List<String> theLookupProperties, List<String> theExpectedReturnedProperties) {
      // setup
      CodeSystem codeSystem = new CodeSystem();
      codeSystem.setId(ourCodeSystemId);
      codeSystem.setUrl(ourCodeSystemUrl);
      ConceptDefinitionComponent concept = codeSystem.addConcept().setCode(ourCode)
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
         assertEquals(property.getCode().equals(ourPropertyC) ? "code" : "string", parameter.getName());
         assertEquals(property.getCode(), ((StringType)parameter.getValue()).getValue());

         parameter = parameterPartIterator.next();
         assertEquals("value", parameter.getName());
         assertTrue(property.getValue().equalsShallow(parameter.getValue()));
      }
   }
}
