package ca.uhn.fhir.rest.server.util;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class NarrativeUtilTest {

	@ParameterizedTest
	@CsvSource({
		"<div><SPAN ID=\"foo\">hello</SPAN></div> , <div xmlns=\"http://www.w3.org/1999/xhtml\"><span id=\"foo\">hello</span></div>",
		"<div><span id=\"foo\">hello</span></div> , <div xmlns=\"http://www.w3.org/1999/xhtml\"><span id=\"foo\">hello</span></div>",
		"<div><SPAN ONCLICK=\"hello()\">hello</SPAN></div> , <div xmlns=\"http://www.w3.org/1999/xhtml\">hello</div>",
		"<div><span onclick=\"hello()\">hello</span></div> , <div xmlns=\"http://www.w3.org/1999/xhtml\">hello</div>",
		"<div><a href=\"http://goodbye\">hello</a></div> , <div xmlns=\"http://www.w3.org/1999/xhtml\">hello</div>",
		"<div><table><tr><td>hello</td></tr></table></div> , <div xmlns=\"http://www.w3.org/1999/xhtml\"><table><tbody><tr><td>hello</td></tr></tbody></table></div>",
		"<div><span style=\"font-size: 100px;\">hello</span></div> , <div xmlns=\"http://www.w3.org/1999/xhtml\"><span style=\"font-size:100px\">hello</span></div>",
		"<div><span style=\"background: url('test.jpg')\">hello</span></div> , <div xmlns=\"http://www.w3.org/1999/xhtml\">hello</div>",
		"hello , <div xmlns=\"http://www.w3.org/1999/xhtml\">hello</div>",
		"empty , null",
		"null , null"
	})
	public void testValidateIsCaseInsensitive(String theHtml, String theExpected) {
		String output = NarrativeUtil.sanitize(fixNull(theHtml));
		assertEquals(fixNull(theExpected), output);
	}

	private String fixNull(String theExpected) {
		if ("null".equals(theExpected)) {
			return null;
		}
		if ("empty".equals(theExpected)) {
			return "";
		}
		return theExpected;
	}

}
