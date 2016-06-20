package ca.uhn.fhir.rest.method;

import static org.junit.Assert.*;

import org.hl7.fhir.dstu3.model.IdType;
import org.junit.Test;

import ca.uhn.fhir.model.primitive.IdDt;

public class MethodUtilTest {

	@Test
	public void testConvertIdToType() {
		IdDt id = new IdDt("Patient/123");
		IdType id2 = MethodUtil.convertIdToType(id, IdType.class);
		assertEquals("Patient/123", id2.getValue());
	}
	
}
