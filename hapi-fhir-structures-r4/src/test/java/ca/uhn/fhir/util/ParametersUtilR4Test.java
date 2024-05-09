package ca.uhn.fhir.util;

import ca.uhn.fhir.context.FhirContext;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.r4.model.IntegerType;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.StringType;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ParametersUtilR4Test {
	private static final String TEST_PERSON_ID = "Person/32768";
	private static FhirContext ourFhirContext = FhirContext.forR4();

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
		assertThat(values).containsExactly("VALUE1", "VALUE2");
	}

	@Test
	public void testGetValueAsInteger() {
		Parameters p = new Parameters();
		p.addParameter()
			.setName("foo")
			.setValue(new IntegerType(123));

		Optional<Integer> value = ParametersUtil.getNamedParameterValueAsInteger(FhirContext.forR4(), p, "foo");
		assertThat(value).isPresent();
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
		assertThat(values).hasSize(3);
		assertEquals(TEST_PERSON_ID, values.get(0));
		assertEquals(TEST_PERSON_ID, values.get(1));
		assertEquals(TEST_PERSON_ID, values.get(2));
	}

	@Test
	public void testAddPartDecimalNoScientificNotation() {
		// setup
		Double decimalValue = Double.valueOf("10000000");
		IBaseParameters parameters = ParametersUtil.newInstance(ourFhirContext);
		IBase resultPart = ParametersUtil.addParameterToParameters(ourFhirContext, parameters, "link");

		// execute
		ParametersUtil.addPartDecimal(ourFhirContext, resultPart, "linkCreated", decimalValue);

		// verify
		String expected = BigDecimal.valueOf(decimalValue).toPlainString();
		List<String> results = ParametersUtil.getNamedParameterPartAsString(ourFhirContext, parameters, "link", "linkCreated");
		assertEquals(expected, results.get(0));
	}
}
