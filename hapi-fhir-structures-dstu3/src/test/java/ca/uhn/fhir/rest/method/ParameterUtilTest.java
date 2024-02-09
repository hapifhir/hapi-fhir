package ca.uhn.fhir.rest.method;

import static org.assertj.core.api.Assertions.assertThat;

import org.hl7.fhir.dstu3.model.IdType;
import org.junit.jupiter.api.Test;

import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.param.ParameterUtil;

public class ParameterUtilTest {

	@Test
	public void testEscapeAndUrlEncode() {
		assertThat(ParameterUtil.escapeAndUrlEncode("123$123")).isEqualTo("123%5C%24123");
	}

	@Test
	public void testConvertIdToType() {
		IdDt id = new IdDt("Patient/123");
		IdType id2 = ParameterUtil.convertIdToType(id, IdType.class);
		assertThat(id2.getValue()).isEqualTo("Patient/123");
	}
	
}
