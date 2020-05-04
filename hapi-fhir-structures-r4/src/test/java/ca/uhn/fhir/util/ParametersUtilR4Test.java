package ca.uhn.fhir.util;

import ca.uhn.fhir.context.FhirContext;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.r4.model.IntegerType;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.StringType;
import org.junit.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ParametersUtilR4Test {

	@Test
	public void testCreateParameters() {
		FhirContext ctx = FhirContext.forR4();

		IBaseParameters parameters = ParametersUtil.newInstance(ctx);
		ParametersUtil.addParameterToParameters(ctx, parameters, "someString", "string", "someStringValue");
		ParametersUtil.addParameterToParameters(ctx, parameters, "someDate", "date", "2019");

		String encoded = ctx.newJsonParser().encodeResourceToString(parameters);
		assertEquals("{\"resourceType\":\"Parameters\",\"parameter\":[{\"name\":\"someString\",\"valueString\":\"someStringValue\"},{\"name\":\"someDate\",\"valueDate\":\"2019\"}]}", encoded);
	}

	@Test
	public void testGetValues(){
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
		MatcherAssert.assertThat(values, Matchers.contains("VALUE1", "VALUE2"));
	}

	@Test
	public void testGetValueAsInteger(){
		Parameters p = new Parameters();
		p.addParameter()
			.setName("foo")
			.setValue(new IntegerType(123));

		Optional<Integer> value = ParametersUtil.getNamedParameterValueAsInteger(FhirContext.forR4(), p, "foo");
		assertTrue(value.isPresent());
		assertEquals(123, value.get().intValue());
	}

}
