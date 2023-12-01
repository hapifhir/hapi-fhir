package ca.uhn.fhir.jpa.provider.dstu3;

import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.jpa.provider.CodeSystemLookupWithPropertiesUtil;
import ca.uhn.fhir.rest.gclient.IOperationUntypedWithInputAndPartialOutput;
import org.hl7.fhir.dstu3.model.CodeSystem;
import org.hl7.fhir.dstu3.model.CodeType;
import org.hl7.fhir.dstu3.model.Parameters;
import org.hl7.fhir.dstu3.model.StringType;
import org.hl7.fhir.dstu3.model.UriType;
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
import static ca.uhn.fhir.jpa.provider.CodeSystemLookupWithPropertiesUtil.ourPropertyValueA;
import static ca.uhn.fhir.jpa.provider.CodeSystemLookupWithPropertiesUtil.ourPropertyValueB;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ResourceProviderDstu3CodeSystemPropertiesTest extends BaseResourceProviderDstu3Test {

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
      CodeSystem.ConceptDefinitionComponent concept = codeSystem.addConcept().setCode(ourCode);
      CodeSystem.ConceptPropertyComponent propertyComponent = new CodeSystem.ConceptPropertyComponent()
            .setCode(ourPropertyA).setValue(new StringType(ourPropertyValueA));
      concept.addProperty(propertyComponent);
      propertyComponent = new CodeSystem.ConceptPropertyComponent()
            .setCode(ourPropertyB).setValue(new StringType(ourPropertyValueB));
      concept.addProperty(propertyComponent);
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

      Iterator<Parameters.ParametersParameterComponent> paramIterator = parameters.getParameter().iterator();
      Parameters.ParametersParameterComponent parameter = null;
      while (paramIterator.hasNext()) {
         Parameters.ParametersParameterComponent currentParameter = paramIterator.next();
         if (currentParameter.getName().equals("property")) {
            parameter = currentParameter;
            break;
         }
      }

      if (theExpectedReturnedProperties.isEmpty()) {
         assertNull(parameter);
         return;
      }

      Iterator<CodeSystem.ConceptPropertyComponent> propertyIterator = concept.getProperty().stream()
            .filter(property -> theExpectedReturnedProperties.contains(property.getCode())).iterator();

      while (propertyIterator.hasNext()) {
         CodeSystem.ConceptPropertyComponent property = propertyIterator.next();
         assertNotNull(parameter);

         Iterator<Parameters.ParametersParameterComponent> parameterPartIterator = parameter.getPart().iterator();

         parameter = parameterPartIterator.next();
         assertEquals("code", parameter.getName());
         assertEquals(property.getCode(), ((CodeType)parameter.getValue()).getValue());

         parameter = parameterPartIterator.next();
         assertEquals("value", parameter.getName());
         assertTrue(property.getValue().equalsShallow(parameter.getValue()));

         if (paramIterator.hasNext()) {
            parameter = paramIterator.next();
         }
      }
   }
}
