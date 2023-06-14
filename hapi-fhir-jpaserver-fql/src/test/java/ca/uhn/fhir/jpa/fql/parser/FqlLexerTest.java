package ca.uhn.fhir.jpa.fql.parser;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;

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
		List<String> allTokens = fqlLexer.allTokens(FqlParser.WHERE_CLAUSE_CHARACTERS);
		assertThat(allTokens.toString(), allTokens, contains(
			"from", "Patient", "search",
			"_has:Observation:subject:device.identifier", "=", "'1234-5'",
			"select", "name.family"
		));

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


}
