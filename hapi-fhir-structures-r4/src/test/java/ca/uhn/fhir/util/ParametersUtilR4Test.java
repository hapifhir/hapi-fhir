package ca.uhn.fhir.util;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import org.hamcrest.Matchers;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.r4.model.IntegerType;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.StringType;
import org.hl7.fhir.r4.model.Type;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ParametersUtilR4Test {
	private static final Logger ourLog = LoggerFactory.getLogger(ParametersUtilR4Test.class);

	private static final String TEST_PERSON_ID = "Person/32768";
	private static final FhirContext ourFhirContext = FhirContext.forR4();

	private IParser myParser;

	@BeforeEach
	public void init() {
		myParser = ourFhirContext.newJsonParser();
	}

	@Test
	public void testCreateParameters() {

		IBaseParameters parameters = ParametersUtil.newInstance(ourFhirContext);
		ParametersUtil.addParameterToParameters(ourFhirContext, parameters, "someString", "string", "someStringValue");
		ParametersUtil.addParameterToParameters(ourFhirContext, parameters, "someDate", "date", "2019");

		String encoded = ourFhirContext.newJsonParser().encodeResourceToString(parameters);
		assertEquals("{\"resourceType\":\"Parameters\",\"parameter\":[{\"name\":\"someString\",\"valueString\":\"someStringValue\"},{\"name\":\"someDate\",\"valueDate\":\"2019\"}]}", encoded);
	}

	@Test
	public void testGetValues() {
		Parameters p = new Parameters();
		p.addParameter()
			.setName("foo")
			.setValue(new StringType("VALUE1"));
		p.addParameter()
			.setName("foo")
			.setValue(new StringType("VALUE2"));
		p.addParameter()
			.setName("foo");
		p.addParameter()
			.setName("bar")
			.setValue(new StringType("VALUE3"));
		p.addParameter()
			.setValue(new StringType("VALUE4"));

		List<String> values = ParametersUtil.getNamedParameterValuesAsString(FhirContext.forR4(), p, "foo");
		assertThat(values, Matchers.contains("VALUE1", "VALUE2"));
	}

	@Test
	public void testGetValueAsInteger() {
		Parameters p = new Parameters();
		p.addParameter()
			.setName("foo")
			.setValue(new IntegerType(123));

		Optional<Integer> value = ParametersUtil.getNamedParameterValueAsInteger(FhirContext.forR4(), p, "foo");
		assertTrue(value.isPresent());
		assertEquals(123, value.get().intValue());
	}

	@Test
	public void testGetNamedParameterPartAsString() {
		IBaseParameters parameters = ParametersUtil.newInstance(ourFhirContext);
		for (int i = 0; i < 3; ++i) {
			IBase resultPart = ParametersUtil.addParameterToParameters(ourFhirContext, parameters, "link");
			ParametersUtil.addPartString(ourFhirContext, resultPart, "personId", TEST_PERSON_ID);
		}
		List<String> values = ParametersUtil.getNamedParameterPartAsString(ourFhirContext, parameters, "link", "personId");
		assertThat(values, hasSize(3));
		assertThat(values.get(0), is(TEST_PERSON_ID));
		assertThat(values.get(1), is(TEST_PERSON_ID));
		assertThat(values.get(2), is(TEST_PERSON_ID));
	}

	@Test
	public void testParametersGetByName() {
		Parameters p = new Parameters();
		p.addParameter().setName("my-parameter").setValue(new StringType("my-value"));
		p.addParameter().setName("my-other-parameter").setValue(new StringType("my-other-value"));
		Type parameter = p.getParameter("my-parameter");
		assertThat(parameter.primitiveValue(), is("my-value"));
	}

	private Parameters createParametersWithPart(String theParamName) {
		Parameters.ParametersParameterComponent sub = new Parameters.ParametersParameterComponent();
		sub.setName("Sub-p1").setValue(new StringType("1"));

		Parameters p = new Parameters();
		p.addParameter().setName(theParamName)
			.setPart(Collections.singletonList(sub));

		return p;
	}

	@Test
	public void getByName_nestedParametersManuallyConstructed_works() {
		// setup
		String parameterName = "P1";

		Parameters p = createParametersWithPart(parameterName);

		// test
		verifyParameterGetByName(p, parameterName);
	}

	@Test
	public void getByName_nestedParametersParsedFromJson_works() {
		String jsonParams = """
			{
				 "resourceType": "Parameters",
				 "parameter": [
					  {
							"name": "P1",
							"part": [
								 {
									  "name": "Sub-p1",
									  "valueString": "1"
								 }
							]
					  }
				 ]
			}
			""";

		Parameters parameters = myParser.parseResource(Parameters.class, jsonParams);
		verifyParameterGetByName(parameters, "P1");
	}

	private void verifyParameterGetByName(Parameters theParameters, String theParamName) {
		assertNotNull(theParameters.getParameter(theParamName));
		List<Type> sub = theParameters.getParameters(theParamName);
		assertNotNull(sub);
		assertEquals(1, sub.size());
		assertNotNull(sub.get(0));
	}
}
