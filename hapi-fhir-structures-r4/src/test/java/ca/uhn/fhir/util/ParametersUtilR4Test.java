package ca.uhn.fhir.util;

import ca.uhn.fhir.context.FhirContext;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.StringType;
import org.junit.Test;

import java.util.List;

public class ParametersUtilR4Test {

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

}
