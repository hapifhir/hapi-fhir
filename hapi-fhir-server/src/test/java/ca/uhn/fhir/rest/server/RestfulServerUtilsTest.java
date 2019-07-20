package ca.uhn.fhir.rest.server;

import ca.uhn.fhir.rest.api.PreferHeader;
import org.junit.Test;

import static org.junit.Assert.*;

public class RestfulServerUtilsTest{

	@Test
	public void testParsePreferReturn() {
		PreferHeader header = RestfulServerUtils.parsePreferHeader(null,"return=representation");
		assertEquals(PreferHeader.PreferReturnEnum.REPRESENTATION, header.getReturn());
		assertFalse(header.getRespondAsync());
	}

	@Test
	public void testParsePreferReturnAndAsync() {
		PreferHeader header = RestfulServerUtils.parsePreferHeader(null,"return=OperationOutcome; respond-async");
		assertEquals(PreferHeader.PreferReturnEnum.OPERATION_OUTCOME, header.getReturn());
		assertTrue(header.getRespondAsync());
	}

	@Test
	public void testParsePreferAsync() {
		PreferHeader header = RestfulServerUtils.parsePreferHeader(null,"respond-async");
		assertEquals(null, header.getReturn());
		assertTrue(header.getRespondAsync());
	}
}
