package ca.uhn.fhir.util;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

public class UrlPathTokenizerTest {

	@Test
	void urlPathTokenizer_withValidPath_tokenizesCorrectly() {
		UrlPathTokenizer tokenizer = new UrlPathTokenizer("/root/subdir/subsubdir/file.html");
		assertTrue(tokenizer.hasMoreTokens());
		assertThat(tokenizer.countTokens()).isEqualTo(4);
		assertThat(tokenizer.nextTokenUnescapedAndSanitized()).isEqualTo("root");
		assertThat(tokenizer.nextTokenUnescapedAndSanitized()).isEqualTo("subdir");
		assertThat(tokenizer.nextTokenUnescapedAndSanitized()).isEqualTo("subsubdir");
		assertThat(tokenizer.nextTokenUnescapedAndSanitized()).isEqualTo("file.html");
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
		assertThat(tokenizer.countTokens()).isEqualTo(0);
	}

	@Test
	void urlPathTokenizer_withNullPath_returnsEmpty() {
		UrlPathTokenizer tokenizer = new UrlPathTokenizer(null);
		assertThat(tokenizer.countTokens()).isEqualTo(0);
	}

	@Test
	void urlPathTokenizer_withSinglePathElement_returnsSingleToken() {
		UrlPathTokenizer tokenizer = new UrlPathTokenizer("hello");
		assertTrue(tokenizer.hasMoreTokens());
		assertThat(tokenizer.nextTokenUnescapedAndSanitized()).isEqualTo("hello");
	}

	@Test
	void urlPathTokenizer_withEscapedPath_shouldUnescape() {
		UrlPathTokenizer tokenizer = new UrlPathTokenizer("Homer%20Simpson");
		assertTrue(tokenizer.hasMoreTokens());
		assertThat(tokenizer.nextTokenUnescapedAndSanitized()).isEqualTo("Homer Simpson");

		tokenizer = new UrlPathTokenizer("hack%2Fslash");
		assertTrue(tokenizer.hasMoreTokens());
		assertThat(tokenizer.nextTokenUnescapedAndSanitized()).isEqualTo("hack/slash");
	}

	@Test
	void urlPathTokenizer_peek_shouldNotConsumeTokens() {
		UrlPathTokenizer tokenizer = new UrlPathTokenizer("this/that");
		assertThat(tokenizer.countTokens()).isEqualTo(2);
		tokenizer.peek();
		assertThat(tokenizer.countTokens()).isEqualTo(2);
	}

	@Test
	void urlPathTokenizer_withSuspiciousCharacters_sanitizesCorrectly() {
		UrlPathTokenizer tokenizer = new UrlPathTokenizer("<DROP TABLE USERS>");
		assertTrue(tokenizer.hasMoreTokens());
		assertThat(tokenizer.nextTokenUnescapedAndSanitized()).isEqualTo("&lt;DROP TABLE USERS&gt;");

		tokenizer = new UrlPathTokenizer("'\n\r\"");
		assertTrue(tokenizer.hasMoreTokens());
		assertThat(tokenizer.nextTokenUnescapedAndSanitized()).isEqualTo("&apos;&#10;&#13;&quot;");
	}
}
