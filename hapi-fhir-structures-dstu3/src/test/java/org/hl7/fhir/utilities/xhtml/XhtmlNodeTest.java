package org.hl7.fhir.utilities.xhtml;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class XhtmlNodeTest {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(XhtmlNodeTest.class);
	@Test
	public void testParseXhtmlUnqualified() {
		
		XhtmlNode node = new XhtmlNode();
		node.setValueAsString("<div xmlns=\"http://www.w3.org/1999/xhtml\">" + 
				"<img src=\"http://pbs.twimg.com/profile_images/544507893991485440/r_vo3uj2_bigger.png\" alt=\"Twitter Avatar\"/>" + 
				"@fhirabend" + 
				"</div>");
		
		String output = node.getValueAsString();
		ourLog.info(output);
		
		assertEquals("<div xmlns=\"http://www.w3.org/1999/xhtml\"><img src=\"http://pbs.twimg.com/profile_images/544507893991485440/r_vo3uj2_bigger.png\" alt=\"Twitter Avatar\"/>@fhirabend</div>", output);
	}
	
	@Test
	public void testParseXhtmlQualified() {
		
		XhtmlNode node = new XhtmlNode();
		node.setValueAsString("<xhtml:div xmlns:xhtml=\"http://www.w3.org/1999/xhtml\">" + 
				"<xhtml:img src=\"http://pbs.twimg.com/profile_images/544507893991485440/r_vo3uj2_bigger.png\" alt=\"Twitter Avatar\"/>" + 
				"@fhirabend" + 
				"</xhtml:div>");
		
		String output = node.getValueAsString();
		ourLog.info(output);
		
		assertEquals("<div xmlns=\"http://www.w3.org/1999/xhtml\"><img src=\"http://pbs.twimg.com/profile_images/544507893991485440/r_vo3uj2_bigger.png\" alt=\"Twitter Avatar\"/>@fhirabend</div>", output);
	}
	
}
