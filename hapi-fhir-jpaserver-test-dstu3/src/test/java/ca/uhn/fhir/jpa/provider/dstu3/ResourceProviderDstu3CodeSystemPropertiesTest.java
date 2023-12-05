package ca.uhn.fhir.jpa.provider.dstu3;

import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.test.utilities.LookupCodeTestUtil;
import ca.uhn.fhir.rest.gclient.IOperationUntypedWithInputAndPartialOutput;
import org.hl7.fhir.dstu3.model.CodeSystem;
import org.hl7.fhir.dstu3.model.CodeSystem.ConceptDefinitionComponent;
import org.hl7.fhir.dstu3.model.CodeSystem.ConceptPropertyComponent;
import org.hl7.fhir.dstu3.model.CodeType;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.Parameters;
import org.hl7.fhir.dstu3.model.Parameters.ParametersParameterComponent;
import org.hl7.fhir.dstu3.model.StringType;
import org.hl7.fhir.dstu3.model.UriType;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

import static ca.uhn.fhir.test.utilities.LookupCodeTestUtil.ourCode;
import static ca.uhn.fhir.test.utilities.LookupCodeTestUtil.ourCodeSystemId;
import static ca.uhn.fhir.test.utilities.LookupCodeTestUtil.ourCodeSystemUrl;
import static ca.uhn.fhir.test.utilities.LookupCodeTestUtil.ourPropertyA;
import static ca.uhn.fhir.test.utilities.LookupCodeTestUtil.ourPropertyB;
import static ca.uhn.fhir.test.utilities.LookupCodeTestUtil.ourPropertyC;
import static ca.uhn.fhir.test.utilities.LookupCodeTestUtil.ourPropertyValueA;
import static ca.uhn.fhir.test.utilities.LookupCodeTestUtil.ourPropertyValueB;
import static ca.uhn.fhir.test.utilities.LookupCodeTestUtil.propertyCode;
import static ca.uhn.fhir.test.utilities.LookupCodeTestUtil.propertyCodeSystem;
import static ca.uhn.fhir.test.utilities.LookupCodeTestUtil.propertyDisplay;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ResourceProviderDstu3CodeSystemPropertiesTest extends BaseResourceProviderDstu3Test {

   public static Stream<Arguments> parametersLookup() {
      return LookupCodeTestUtil.parametersLookupWithProperties();
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

      Iterator<ParametersParameterComponent> paramIterator = parameters.getParameter().iterator();
      ParametersParameterComponent parameter = null;
      while (paramIterator.hasNext()) {
         ParametersParameterComponent currentParameter = paramIterator.next();
         if (currentParameter.getName().equals("property")) {
            parameter = currentParameter;
            break;
         }
      }

      if (theExpectedReturnedProperties.isEmpty()) {
         assertNull(parameter);
         return;
      }

      Iterator<ConceptPropertyComponent> propertyIterator = concept.getProperty().stream()
            .filter(property -> theExpectedReturnedProperties.contains(property.getCode())).iterator();

      while (propertyIterator.hasNext()) {
         ConceptPropertyComponent property = propertyIterator.next();
         assertNotNull(parameter);

         Iterator<ParametersParameterComponent> parameterPartIterator = parameter.getPart().iterator();

         parameter = parameterPartIterator.next();
         assertEquals(property.getCode().equals(ourPropertyC) ? "code" : "string", parameter.getName());
         assertEquals(property.getCode(), ((StringType)parameter.getValue()).getValue());

         parameter = parameterPartIterator.next();
         assertEquals("value", parameter.getName());
         assertTrue(property.getValue().equalsShallow(parameter.getValue()));

         if (paramIterator.hasNext()) {
            parameter = paramIterator.next();
         }
      }
   }
}
