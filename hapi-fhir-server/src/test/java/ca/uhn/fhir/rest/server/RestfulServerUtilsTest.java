package ca.uhn.fhir.rest.server;

import ca.uhn.fhir.rest.api.PreferHandlingEnum;
import ca.uhn.fhir.rest.api.PreferHeader;
import ca.uhn.fhir.rest.api.PreferReturnEnum;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RestfulServerUtilsTest{

	@Test
	public void testParsePreferReturn() {
		PreferHeader header = RestfulServerUtils.parsePreferHeader(null,"return=representation");
		assertEquals(PreferReturnEnum.REPRESENTATION, header.getReturn());
		assertFalse(header.getRespondAsync());
	}

	@Test
	public void testParsePreferReturnAndAsync() {
		PreferHeader header = RestfulServerUtils.parsePreferHeader(null,"return=OperationOutcome; respond-async");
		assertEquals(PreferReturnEnum.OPERATION_OUTCOME, header.getReturn());
		assertTrue(header.getRespondAsync());
	}

	@Test
	public void testParsePreferAsync() {
		PreferHeader header = RestfulServerUtils.parsePreferHeader(null,"respond-async");
		assertEquals(null, header.getReturn());
		assertTrue(header.getRespondAsync());
	}

	@Test
	public void testParseHandlingLenient() {
		PreferHeader header = RestfulServerUtils.parsePreferHeader(null,"handling=lenient");
		assertEquals(null, header.getReturn());
		assertFalse(header.getRespondAsync());
		assertEquals(PreferHandlingEnum.LENIENT, header.getHanding());
	}

	@Test
	public void testParseHandlingLenientAndReturnRepresentation_CommaSeparatd() {
		PreferHeader header = RestfulServerUtils.parsePreferHeader(null,"handling=lenient, return=representation");
		assertEquals(PreferReturnEnum.REPRESENTATION, header.getReturn());
		assertFalse(header.getRespondAsync());
		assertEquals(PreferHandlingEnum.LENIENT, header.getHanding());
	}

	@Test
	public void testParseHandlingLenientAndReturnRepresentation_SemicolonSeparatd() {
		PreferHeader header = RestfulServerUtils.parsePreferHeader(null,"handling=lenient; return=representation");
		assertEquals(PreferReturnEnum.REPRESENTATION, header.getReturn());
		assertFalse(header.getRespondAsync());
		assertEquals(PreferHandlingEnum.LENIENT, header.getHanding());
	}
}
