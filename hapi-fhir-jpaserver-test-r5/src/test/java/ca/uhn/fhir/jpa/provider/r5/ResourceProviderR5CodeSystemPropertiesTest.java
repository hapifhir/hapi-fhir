package ca.uhn.fhir.jpa.provider.r5;

import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.jpa.provider.CodeSystemLookupWithPropertiesUtil;
import ca.uhn.fhir.rest.gclient.IOperationUntypedWithInputAndPartialOutput;
import org.hl7.fhir.r5.model.CodeSystem;
import org.hl7.fhir.r5.model.CodeType;
import org.hl7.fhir.r5.model.Parameters;
import org.hl7.fhir.r5.model.StringType;
import org.hl7.fhir.r5.model.UriType;
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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ResourceProviderR5CodeSystemPropertiesTest extends BaseResourceProviderR5Test {
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


      if (theExpectedReturnedProperties.isEmpty()) {
         assertFalse(parameters.hasParameter("property"));
         return;
      }

      assertTrue(parameters.hasParameter("property"));
      Iterator<Parameters.ParametersParameterComponent> parameterPropertyIterator = parameters.getParameters("property").iterator();

      Iterator<CodeSystem.ConceptPropertyComponent> propertyIterator = concept.getProperty().stream()
            .filter(property -> theExpectedReturnedProperties.contains(property.getCode())).iterator();

      while (propertyIterator.hasNext()) {
         CodeSystem.ConceptPropertyComponent property = propertyIterator.next();

         assertTrue(parameterPropertyIterator.hasNext());
         Parameters.ParametersParameterComponent parameter = parameterPropertyIterator.next();
         Iterator<Parameters.ParametersParameterComponent> parameterPartIterator = parameter.getPart().iterator();

         parameter = parameterPartIterator.next();
         assertEquals("code", parameter.getName());
         assertEquals(property.getCode(), ((CodeType)parameter.getValue()).getCode());

         parameter = parameterPartIterator.next();
         assertEquals("value", parameter.getName());
         assertTrue(property.getValue().equalsShallow(parameter.getValue()));
      }
   }
}
