package org.hl7.fhir.utilities.xhtml;

import static org.junit.Assert.*;

import java.io.IOException;

import org.hl7.fhir.exceptions.FHIRFormatError;
import org.junit.Test;

public class XhtmlParserTest {

	private XhtmlParser p = new XhtmlParser();
	/**
	 * See #636
	 */
	@Test
	public void testParseLiteralByName() throws Exception {
		String input = "<div>&alpha;</div>";
		XhtmlDocument parsed = p.parse(input, null);
		
		assertEquals(1, parsed.getChildNodes().size());
		assertEquals(XhtmlNode.class, parsed.getChildNodes().get(0).getClass());
		XhtmlNode node = parsed.getChildNodes().get(0);
		assertEquals("<div xmlns=\"http://www.w3.org/1999/xhtml\">α</div>", node.getValue());
	}
	
	/**
	 * See #636
	 */
	@Test
	public void testParseLiteralByDecimal() throws Exception {
		String input = "<div>&#945;</div>";
		XhtmlDocument parsed = p.parse(input, null);
		
		assertEquals(1, parsed.getChildNodes().size());
		assertEquals(XhtmlNode.class, parsed.getChildNodes().get(0).getClass());
		XhtmlNode node = parsed.getChildNodes().get(0);
		assertEquals("<div xmlns=\"http://www.w3.org/1999/xhtml\">α</div>", node.getValue());
	}

	/**
	 * See #636
	 */
	@Test
	public void testParseLiteralByHex() throws Exception {
		String input = "<div>&#x03B1;</div>";
		XhtmlDocument parsed = p.parse(input, null);
		
		assertEquals(1, parsed.getChildNodes().size());
		assertEquals(XhtmlNode.class, parsed.getChildNodes().get(0).getClass());
		XhtmlNode node = parsed.getChildNodes().get(0);
		assertEquals("<div xmlns=\"http://www.w3.org/1999/xhtml\">α</div>", node.getValue());
	}
}
