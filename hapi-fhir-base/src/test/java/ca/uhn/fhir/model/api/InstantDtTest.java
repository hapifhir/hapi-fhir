package ca.uhn.fhir.model.api;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Test;

import ca.uhn.fhir.model.primitive.InstantDt;

public class InstantDtTest {

	@Test
	public void testParseHandlesMillis() {
		InstantDt dt = new InstantDt();
		dt.setValueAsString("2015-06-22T15:44:32.831-04:00");
		Date date = dt.getValue();
		
		InstantDt dt2 = new InstantDt();
		dt2.setValue(date);
		dt2.setTimeZoneZulu(true);
		String string = dt2.getValueAsString();
		
		assertEquals("2015-06-22T19:44:32.831Z", string);
	}
	
	
}
