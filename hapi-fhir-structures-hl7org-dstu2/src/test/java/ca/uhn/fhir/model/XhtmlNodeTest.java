package ca.uhn.fhir.model;

import static org.junit.Assert.assertEquals;

import org.hl7.fhir.dstu2.model.Narrative;
import org.hl7.fhir.utilities.xhtml.XhtmlNode;
import org.junit.Ignore;
import org.junit.Test;

public class XhtmlNodeTest {

	@Test
	@Ignore
	public void testNamespaces() {
		
		Narrative type = new Narrative();
		XhtmlNode div = type.getDiv();
		div.setValue("<xhtml:div xmlns:xhtml=\"http://www.w3.org/1999/xhtml\">hello</xhtml:div>");
		
		assertEquals("<xhtml:div xmlns:xhtml=\"http://www.w3.org/1999/xhtml\">hello</xhtml:div>", div.getValue());

	}
	
}
