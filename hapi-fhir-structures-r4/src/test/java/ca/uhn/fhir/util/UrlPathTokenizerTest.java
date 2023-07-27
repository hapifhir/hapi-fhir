package ca.uhn.fhir.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UrlPathTokenizerTest {

	@Test
	void urlPathTokenizer_withValidPath_tokenizesCorrectly() {
		UrlPathTokenizer tokenizer = new UrlPathTokenizer("/root/subdir/subsubdir/file.html");
		assertTrue(tokenizer.hasMoreTokens());
		assertEquals(4, tokenizer.countTokens());
		assertEquals("root", tokenizer.nextTokenUnescapedAndSanitized());
		assertEquals("subdir", tokenizer.nextTokenUnescapedAndSanitized());
		assertEquals("subsubdir", tokenizer.nextTokenUnescapedAndSanitized());
		assertEquals("file.html", tokenizer.nextTokenUnescapedAndSanitized());
		assertFalse(tokenizer.hasMoreTokens());
	}

	@ParameterizedTest
	@ValueSource(strings = {
		"",               // actually empty
		"///////",        // effectively empty
		"//  / / /  /   " // effectively empty with extraneous whitespace
	})
	void urlPathTokenizer_withEmptyPath_returnsEmpty(String thePath) {
		UrlPathTokenizer tokenizer = new UrlPathTokenizer(thePath);
		assertEquals(0, tokenizer.countTokens());
	}

	@Test
	void urlPathTokenizer_withNullPath_returnsEmpty() {
		UrlPathTokenizer tokenizer = new UrlPathTokenizer(null);
		assertEquals(0, tokenizer.countTokens());
	}

	@Test
	void urlPathTokenizer_withSinglePathElement_returnsSingleToken() {
		UrlPathTokenizer tokenizer = new UrlPathTokenizer("hello");
		assertTrue(tokenizer.hasMoreTokens());
		assertEquals("hello", tokenizer.nextTokenUnescapedAndSanitized());
	}

	@Test
	void urlPathTokenizer_withEscapedPath_shouldUnescape() {
		UrlPathTokenizer tokenizer = new UrlPathTokenizer("Homer%20Simpson");
		assertTrue(tokenizer.hasMoreTokens());
		assertEquals("Homer Simpson", tokenizer.nextTokenUnescapedAndSanitized());

		tokenizer = new UrlPathTokenizer("hack%2Fslash");
		assertTrue(tokenizer.hasMoreTokens());
		assertEquals("hack/slash", tokenizer.nextTokenUnescapedAndSanitized());
	}

	@Test
	void urlPathTokenizer_peek_shouldNotConsumeTokens() {
		UrlPathTokenizer tokenizer = new UrlPathTokenizer("this/that");
		assertEquals(2, tokenizer.countTokens());
		tokenizer.peek();
		assertEquals(2, tokenizer.countTokens());
	}

	@Test
	void urlPathTokenizer_withSuspiciousCharacters_sanitizesCorrectly() {
		UrlPathTokenizer tokenizer = new UrlPathTokenizer("<DROP TABLE USERS>");
		assertTrue(tokenizer.hasMoreTokens());
		assertEquals("&lt;DROP TABLE USERS&gt;", tokenizer.nextTokenUnescapedAndSanitized());

		tokenizer = new UrlPathTokenizer("'\n\r\"");
		assertTrue(tokenizer.hasMoreTokens());
		assertEquals("&apos;&#10;&#13;&quot;", tokenizer.nextTokenUnescapedAndSanitized());
	}
}
