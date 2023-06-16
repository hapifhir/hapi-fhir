package ca.uhn.fhir.jpa.fql.parser;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class FqlLexerTest {

	@Test
	public void testSimpleStatement() {
		String input = """
					from Patient
					select
					   name.given[0],
					   name.family
			""";
		List<String> allTokens = new FqlLexer(input).allTokens();
		assertThat(allTokens, contains(
			"from", "Patient", "select", "name.given[0]", ",", "name.family"
		));
	}

	@Test
	public void testSelectStar() {
		String input = """
					from Patient
					select
					   *
			""";
		List<String> allTokens = new FqlLexer(input).allTokens();
		assertThat(allTokens, contains(
			"from", "Patient", "select", "*"
		));
	}

	@Test
	public void testQuotedString() {
		String input = """
			from
			  Patient
			where
			  name.given = 'Foo \\' Chalmers'
			select
			  name.given[0],\s
			  name.family
			  """;
		List<String> allTokens = new FqlLexer(input).allTokens();
		assertThat(allTokens, contains(
			"from", "Patient", "where",
			"name.given", "=", "'Foo ' Chalmers'",
			"select", "name.given[0]",
			",", "name.family"
		));

	}

	@Test
	public void testSearchParamWithQualifiers() {
		String input = """
			from
			  Patient
			search
			  _has:Observation:subject:device.identifier='1234-5'
			select
			  name.family
			  """;
		FqlLexer fqlLexer = new FqlLexer(input);

		assertEquals("from", fqlLexer.getNextToken(FqlLexerOptions.DEFAULT).getToken());
		assertEquals("Patient", fqlLexer.getNextToken(FqlLexerOptions.DEFAULT).getToken());
		assertEquals("search", fqlLexer.getNextToken(FqlLexerOptions.DEFAULT).getToken());
		assertEquals("_has:Observation:subject:device.identifier", fqlLexer.getNextToken(FqlLexerOptions.SEARCH_PARAMETER_NAME).getToken());
		assertEquals("=", fqlLexer.getNextToken(FqlLexerOptions.DEFAULT).getToken());
		assertEquals("'1234-5'", fqlLexer.getNextToken(FqlLexerOptions.DEFAULT).getToken());
		assertEquals("select", fqlLexer.getNextToken(FqlLexerOptions.DEFAULT).getToken());
		assertEquals("name.family", fqlLexer.getNextToken(FqlLexerOptions.DEFAULT).getToken());

	}

	@Test
	public void testInList() {
		String input = """
			from StructureDefinition
			    where url in ('foo' | 'bar')
			select
			    Name: name,
			    URL: url
			""";
		List<String> allTokens = new FqlLexer(input).allTokens();
		assertThat(allTokens, contains(
			"from", "StructureDefinition", "where",
			"url", "in", "(", "'foo'", "|", "'bar'", ")",
			"select",
			"Name", ":", "name", ",",
			"URL", ":", "url"
		));
	}

	@Test
	public void testFhirPathSelector() {
		String input = """
					from Patient
					select 
						( Observation.value.ofType ( Quantity ) ).unit,
						name.family.length()
			""";
		FqlLexer lexer = new FqlLexer(input);
		assertEquals("from", lexer.getNextToken().getToken());
		assertEquals("Patient", lexer.getNextToken().getToken());
		assertEquals("select", lexer.getNextToken().getToken());
		assertEquals("( Observation.value.ofType ( Quantity ) ).unit", lexer.getNextToken(FqlLexerOptions.FHIRPATH_EXPRESSION).getToken());
		assertEquals(",", lexer.getNextToken().getToken());
		assertEquals("name.family.length()", lexer.getNextToken(FqlLexerOptions.FHIRPATH_EXPRESSION).getToken());
	}


	@Test
	public void testOptionChangeIsRespected() {
		// Setup
		String input = """
					from Patient
					select 
						( Observation.value.ofType ( Quantity ) ).unit,
						name.family.length()
			""";
		FqlLexer lexer = new FqlLexer(input);
		assertEquals("from", lexer.getNextToken().getToken());
		assertEquals("Patient", lexer.getNextToken().getToken());
		assertEquals("select", lexer.getNextToken().getToken());

		// Test + Verify
		assertEquals("(", lexer.peekNextToken(FqlLexerOptions.DEFAULT).getToken());
		assertEquals("( Observation.value.ofType ( Quantity ) ).unit", lexer.peekNextToken(FqlLexerOptions.FHIRPATH_EXPRESSION).getToken());
		assertEquals("(", lexer.peekNextToken(FqlLexerOptions.DEFAULT).getToken());
		assertEquals("( Observation.value.ofType ( Quantity ) ).unit", lexer.getNextToken(FqlLexerOptions.FHIRPATH_EXPRESSION).getToken());
	}

}
