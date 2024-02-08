package ca.uhn.fhir.jpa.provider.r5;

import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.rest.gclient.IOperationUntypedWithInputAndPartialOutput;
import ca.uhn.fhir.jpa.provider.CodeSystemLookupWithPropertiesUtil;
import org.hl7.fhir.r5.model.CodeSystem;
import org.hl7.fhir.r5.model.CodeSystem.ConceptPropertyComponent;
import org.hl7.fhir.r5.model.CodeType;
import org.hl7.fhir.r5.model.Coding;
import org.hl7.fhir.r5.model.Parameters;
import org.hl7.fhir.r5.model.Parameters.ParametersParameterComponent;
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
import static ca.uhn.fhir.jpa.provider.CodeSystemLookupWithPropertiesUtil.ourPropertyC;
import static ca.uhn.fhir.jpa.provider.CodeSystemLookupWithPropertiesUtil.ourPropertyValueA;
import static ca.uhn.fhir.jpa.provider.CodeSystemLookupWithPropertiesUtil.ourPropertyValueB;
import static ca.uhn.fhir.jpa.provider.CodeSystemLookupWithPropertiesUtil.propertyCode;
import static ca.uhn.fhir.jpa.provider.CodeSystemLookupWithPropertiesUtil.propertyCodeSystem;
import static ca.uhn.fhir.jpa.provider.CodeSystemLookupWithPropertiesUtil.propertyDisplay;
import static org.assertj.core.api.Assertions.assertThat;

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
			assertThat(parameters.hasParameter("property")).isFalse();
			return;
		}

		assertThat(parameters.hasParameter("property")).isTrue();
		Iterator<ParametersParameterComponent> parameterPropertyIterator = parameters.getParameters("property").iterator();

		Iterator<ConceptPropertyComponent> propertyIterator = concept.getProperty().stream()
			.filter(property -> theExpectedReturnedProperties.contains(property.getCode())).iterator();

		while (propertyIterator.hasNext()) {
			ConceptPropertyComponent property = propertyIterator.next();

			assertThat(parameterPropertyIterator.hasNext()).isTrue();
			ParametersParameterComponent parameter = parameterPropertyIterator.next();
			Iterator<ParametersParameterComponent> parameterPartIterator = parameter.getPart().iterator();

			parameter = parameterPartIterator.next();
			assertThat(parameter.getName()).isEqualTo("code");
			assertThat(((CodeType) parameter.getValue()).getValue()).isEqualTo(property.getCode());

			parameter = parameterPartIterator.next();
			assertThat(parameter.getName()).isEqualTo("value");
			assertThat(property.getValue().equalsShallow(parameter.getValue())).isTrue();

			assertThat(parameterPartIterator.hasNext()).isFalse();
		}
	}
}
