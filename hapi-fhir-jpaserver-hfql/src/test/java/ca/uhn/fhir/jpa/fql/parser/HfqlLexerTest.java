package ca.uhn.fhir.jpa.fql.parser;

import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class HfqlLexerTest {

	@Test
	void testSimpleStatement() {
		String input = """
					from Patient
					select
					   name.given[0],
					   name.family
			""";
		List<String> allTokens = new HfqlLexer(input).allTokens();
		assertThat(allTokens).containsExactly("from", "Patient", "select", "name.given[0]", ",", "name.family");
	}

	@Test
	void testSelectStar() {
		String input = """
					from Patient
					select
					   *
			""";
		List<String> allTokens = new HfqlLexer(input).allTokens();
		assertThat(allTokens).containsExactly("from", "Patient", "select", "*");
	}

	@Test
	void testQuotedString() {
		String input = """
			from
			  Patient
			where
			  name.given = 'Foo \\' Chalmers'
			select
			  name.given[0],\s
			  name.family
			  """;
		List<String> allTokens = new HfqlLexer(input).allTokens();
		assertThat(allTokens).containsExactly("from", "Patient", "where", "name.given", "=", "'Foo ' Chalmers'", "select", "name.given[0]", ",", "name.family");

	}

	@Test
	void testSearchParamWithQualifiers() {
		String input = """
			from
			  Patient
			search
			  _has:Observation:subject:device.identifier='1234-5'
			select
			  name.family
			  """;
		HfqlLexer hfqlLexer = new HfqlLexer(input);

		assertEquals("from", hfqlLexer.getNextToken(HfqlLexerOptions.HFQL_TOKEN).getToken());
		assertEquals("Patient", hfqlLexer.getNextToken(HfqlLexerOptions.HFQL_TOKEN).getToken());
		assertEquals("search", hfqlLexer.getNextToken(HfqlLexerOptions.HFQL_TOKEN).getToken());
		assertEquals("_has:Observation:subject:device.identifier", hfqlLexer.getNextToken(HfqlLexerOptions.SEARCH_PARAMETER_NAME).getToken());
		assertEquals("=", hfqlLexer.getNextToken(HfqlLexerOptions.HFQL_TOKEN).getToken());
		assertEquals("'1234-5'", hfqlLexer.getNextToken(HfqlLexerOptions.HFQL_TOKEN).getToken());
		assertEquals("select", hfqlLexer.getNextToken(HfqlLexerOptions.HFQL_TOKEN).getToken());
		assertEquals("name.family", hfqlLexer.getNextToken(HfqlLexerOptions.HFQL_TOKEN).getToken());

	}

	@Test
	void testInList() {
		String input = """
			from StructureDefinition
			    where url in ('foo' | 'bar')
			select
			    Name: name,
			    URL: url
			""";
		List<String> allTokens = new HfqlLexer(input).allTokens();
		assertThat(allTokens).containsExactly("from", "StructureDefinition", "where", "url", "in", "(", "'foo'", "|", "'bar'", ")", "select", "Name", ":", "name", ",", "URL", ":", "url");
	}

	@Test
	void testFhirPathSelector() {
		String input = """
					from Patient
					select 
						( Observation.value.ofType ( Quantity ) ).unit,
						name.family.length()
			""";
		HfqlLexer lexer = new HfqlLexer(input);
		assertEquals("from", lexer.getNextToken().getToken());
		assertEquals("Patient", lexer.getNextToken().getToken());
		assertEquals("select", lexer.getNextToken().getToken());
		assertEquals("( Observation.value.ofType ( Quantity ) ).unit", lexer.getNextToken(HfqlLexerOptions.FHIRPATH_EXPRESSION).getToken());
		assertEquals(",", lexer.getNextToken().getToken());
		assertEquals("name.family.length()", lexer.getNextToken(HfqlLexerOptions.FHIRPATH_EXPRESSION).getToken());
	}


	@Test
	void testOptionChangeIsRespected() {
		// Setup
		String input = """
					from Patient
					select 
						( Observation.value.ofType ( Quantity ) ).unit,
						name.family.length()
			""";
		HfqlLexer lexer = new HfqlLexer(input);
		assertEquals("from", lexer.getNextToken().getToken());
		assertEquals("Patient", lexer.getNextToken().getToken());
		assertEquals("select", lexer.getNextToken().getToken());

		// Test + Verify
		assertEquals("(", lexer.peekNextToken(HfqlLexerOptions.HFQL_TOKEN).getToken());
		assertEquals("( Observation.value.ofType ( Quantity ) ).unit", lexer.peekNextToken(HfqlLexerOptions.FHIRPATH_EXPRESSION).getToken());
		assertEquals("(", lexer.peekNextToken(HfqlLexerOptions.HFQL_TOKEN).getToken());
		assertEquals("( Observation.value.ofType ( Quantity ) ).unit", lexer.getNextToken(HfqlLexerOptions.FHIRPATH_EXPRESSION).getToken());
	}

	@ParameterizedTest
	@CsvSource(textBlock = """
		>= , false , HFQL_TOKEN
		<= , false , HFQL_TOKEN
		!= , false , HFQL_TOKEN
		=  , false , HFQL_TOKEN
		>= , true  , HFQL_TOKEN
		<= , true  , HFQL_TOKEN
		!= , true  , HFQL_TOKEN
		~  , true  , HFQL_TOKEN
		=  , true  , HFQL_TOKEN
		>= , false , FHIRPATH_EXPRESSION
		<= , false , FHIRPATH_EXPRESSION
		!= , false , FHIRPATH_EXPRESSION
		=  , false , FHIRPATH_EXPRESSION
		>= , true  , FHIRPATH_EXPRESSION
		<= , true  , FHIRPATH_EXPRESSION
		!= , true  , FHIRPATH_EXPRESSION
		~  , true  , FHIRPATH_EXPRESSION
		=  , true  , FHIRPATH_EXPRESSION
		>= , false , FHIRPATH_EXPRESSION_PART
		<= , false , FHIRPATH_EXPRESSION_PART
		!= , false , FHIRPATH_EXPRESSION_PART
		=  , false , FHIRPATH_EXPRESSION_PART
		>= , true  , FHIRPATH_EXPRESSION_PART
		<= , true  , FHIRPATH_EXPRESSION_PART
		!= , true  , FHIRPATH_EXPRESSION_PART
		~  , true  , FHIRPATH_EXPRESSION_PART
		=  , true  , FHIRPATH_EXPRESSION_PART
		"""
	)
	void testComparators(String theComparator, boolean thePad, HfqlLexerOptions theOptions) {
		String input = """
			SELECT
			   id
			FROM
			   Patient
			WHERE
			   meta.lastUpdated >= '2023-10-09'
			""";

		String comparator = theComparator.trim();
		if (thePad) {
			input = input.replace(" >= ", " " + comparator + " ");
		} else {
			input = input.replace(" >= ", comparator);
		}

		List<String> allTokens = new HfqlLexer(input).allTokens(theOptions);

		List<String> expectedItems = new ArrayList<>();
		expectedItems.add("SELECT");
		expectedItems.add("id");
		expectedItems.add("FROM");
		expectedItems.add("Patient");
		expectedItems.add("WHERE");
		if (theOptions == HfqlLexerOptions.FHIRPATH_EXPRESSION_PART) {
			expectedItems.add("meta");
			expectedItems.add(".");
			expectedItems.add("lastUpdated");
		} else {
			expectedItems.add("meta.lastUpdated");
		}
		expectedItems.add(comparator);
		expectedItems.add("'2023-10-09'");

		assertThat(allTokens).as(allTokens.toString()).containsExactly(expectedItems.toArray(new String[0]));
	}


	@ParameterizedTest
	@CsvSource({
		"token1 token2 'token3, HFQL_TOKEN",
		"foo.bar(blah, FHIRPATH_EXPRESSION",
		"foo.bar((blah.baz), FHIRPATH_EXPRESSION",
	})
	void testIncompleteFragment_String(String theInput, HfqlLexerOptions theOptions) {
		HfqlLexer lexer = new HfqlLexer(theInput);
		try {
			while (lexer.hasNextToken(theOptions)) {
				lexer.consumeNextToken();
			}
			fail();
		} catch (InvalidRequestException e) {
			assertThat(e.getMessage()).contains("Unexpected end of string");
		}
	}


}
