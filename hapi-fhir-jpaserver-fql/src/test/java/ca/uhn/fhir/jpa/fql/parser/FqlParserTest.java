package ca.uhn.fhir.jpa.fql.parser;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.DataFormatException;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@TestMethodOrder(MethodOrderer.MethodName.class)
@SuppressWarnings("SqlDialectInspection")
public class FqlParserTest {

	private static final FhirContext ourCtx = FhirContext.forR4Cached();

	@Test
	public void testFromSelect() {
		String input = """
					from Patient
					select
					   name.given[0],
					   name.family
			""";

		FqlStatement statement = parse(input);
		assertEquals("Patient", statement.getFromResourceName());
		assertEquals(2, statement.getSelectClauses().size());
		assertEquals("name.given[0]", statement.getSelectClauses().get(0).getClause());
		assertEquals("name.given[0]", statement.getSelectClauses().get(0).getAlias());
		assertEquals("name.family", statement.getSelectClauses().get(1).getClause());
		assertEquals("name.family", statement.getSelectClauses().get(1).getAlias());
	}

	@Test
	public void testSelectFrom() {
		String input = """
					select
					   name.given[0],
					   name.family
					from Patient
			""";

		FqlStatement statement = parse(input);
		assertEquals("Patient", statement.getFromResourceName());
		assertEquals(2, statement.getSelectClauses().size());
		assertEquals("name.given[0]", statement.getSelectClauses().get(0).getClause());
		assertEquals("name.given[0]", statement.getSelectClauses().get(0).getAlias());
		assertEquals("name.family", statement.getSelectClauses().get(1).getClause());
		assertEquals("name.family", statement.getSelectClauses().get(1).getAlias());
	}

	private FqlStatement parse(String theInput) {
		return new FqlParser(ourCtx, theInput).parse();
	}

	@Test
	public void testFromWhereSelect() {
		String input = """
			from
			  Patient
			where
			  name.given = 'Foo \\' Chalmers' and
			  name.family = 'blah'
			select
			  name.given[0],
			  name.family
			""";

		FqlStatement statement = parse(input);
		assertEquals("Patient", statement.getFromResourceName());
		assertEquals(2, statement.getSelectClauses().size());
		assertEquals("name.given[0]", statement.getSelectClauses().get(0).getClause());
		assertEquals("name.family", statement.getSelectClauses().get(1).getClause());
		assertEquals(2, statement.getWhereClauses().size());
		assertEquals("name.given", statement.getWhereClauses().get(0).getLeft());
		assertEquals(FqlStatement.WhereClauseOperator.EQUALS, statement.getWhereClauses().get(0).getOperator());
		assertThat(statement.getWhereClauses().get(0).getRight(), contains("'Foo ' Chalmers'"));
		assertEquals("name.family", statement.getWhereClauses().get(1).getLeft());
		assertThat(statement.getWhereClauses().get(1).getRight(), contains("'blah'"));
		assertEquals(FqlStatement.WhereClauseOperator.EQUALS, statement.getWhereClauses().get(1).getOperator());
	}

	@Test
	public void testFromSearchWhereSelect() {
		String input = """
			from
			  Observation
			search
			  subject.name in ('foo' | 'bar') and _id='123'
			where
			  status = 'final'
			select
			  id
			""";

		FqlStatement statement = parse(input);
		assertEquals("Observation", statement.getFromResourceName());
		assertEquals(1, statement.getSelectClauses().size());
		assertEquals("id", statement.getSelectClauses().get(0).getClause());
		assertEquals(2, statement.getSearchClauses().size());
		assertEquals("subject.name", statement.getSearchClauses().get(0).getLeft());
		assertEquals(FqlStatement.WhereClauseOperator.IN, statement.getSearchClauses().get(0).getOperator());
		assertThat(statement.getSearchClauses().get(0).getRight(), contains("'foo'", "'bar'"));
		assertEquals("_id", statement.getSearchClauses().get(1).getLeft());
		assertEquals(FqlStatement.WhereClauseOperator.EQUALS, statement.getSearchClauses().get(1).getOperator());
		assertThat(statement.getSearchClauses().get(1).getRight(), contains("'123'"));
		assertEquals(1, statement.getWhereClauses().size());
		assertEquals("status", statement.getWhereClauses().get(0).getLeft());
		assertEquals(FqlStatement.WhereClauseOperator.EQUALS, statement.getWhereClauses().get(0).getOperator());
		assertThat(statement.getWhereClauses().get(0).getRight(), contains("'final'"));

	}

	@Test
	public void testFromSearchSelect_RichSearchExpression() {
		String input = """
			from
			  Observation
			search
			  _has:Observation:subject:device.identifier='1234-5'
			select
			  id
			""";

		FqlStatement statement = parse(input);
		assertEquals("Observation", statement.getFromResourceName());
		assertEquals(1, statement.getSelectClauses().size());
		assertEquals("id", statement.getSelectClauses().get(0).getClause());
		assertEquals(1, statement.getSearchClauses().size());
		assertEquals("_has:Observation:subject:device.identifier", statement.getSearchClauses().get(0).getLeft());
		assertEquals(FqlStatement.WhereClauseOperator.EQUALS, statement.getSearchClauses().get(0).getOperator());
		assertThat(statement.getSearchClauses().get(0).getRight(), contains("'1234-5'"));

	}

	@Test
	public void testFromSearchWhereSelectLimit() {
		String input = """
			from
			  Observation
			search
			  subject.name in ('foo' | 'bar') and _id='123'
			where
			  status = 'final'
			select
			  id
			limit 123
			""";

		FqlStatement statement = parse(input);
		assertEquals("Observation", statement.getFromResourceName());
		assertEquals(1, statement.getSelectClauses().size());
		assertEquals("id", statement.getSelectClauses().get(0).getClause());
		assertEquals(2, statement.getSearchClauses().size());
		assertEquals("subject.name", statement.getSearchClauses().get(0).getLeft());
		assertEquals(FqlStatement.WhereClauseOperator.IN, statement.getSearchClauses().get(0).getOperator());
		assertThat(statement.getSearchClauses().get(0).getRight(), contains("'foo'", "'bar'"));
		assertEquals("_id", statement.getSearchClauses().get(1).getLeft());
		assertEquals(FqlStatement.WhereClauseOperator.EQUALS, statement.getSearchClauses().get(1).getOperator());
		assertThat(statement.getSearchClauses().get(1).getRight(), contains("'123'"));
		assertEquals(1, statement.getWhereClauses().size());
		assertEquals("status", statement.getWhereClauses().get(0).getLeft());
		assertEquals(FqlStatement.WhereClauseOperator.EQUALS, statement.getWhereClauses().get(0).getOperator());
		assertThat(statement.getWhereClauses().get(0).getRight(), contains("'final'"));
		assertEquals(123, statement.getLimit());
	}

	@Test
	public void testFromWhereSelect_InClauseAndNamedSelects() {
		// One select with spaces, one without
		String input = """
			from
			    StructureDefinition
			where
			    url in ('foo' | 'bar')
			select
			    Name : name,
			    URL:url
			""";
		FqlStatement statement = parse(input);
		assertEquals("StructureDefinition", statement.getFromResourceName());
		assertEquals(2, statement.getSelectClauses().size());
		assertEquals("name", statement.getSelectClauses().get(0).getClause());
		assertEquals("Name", statement.getSelectClauses().get(0).getAlias());
		assertEquals("url", statement.getSelectClauses().get(1).getClause());
		assertEquals("URL", statement.getSelectClauses().get(1).getAlias());
		assertEquals(1, statement.getWhereClauses().size());
		assertEquals("url", statement.getWhereClauses().get(0).getLeft());
		assertEquals(FqlStatement.WhereClauseOperator.IN, statement.getWhereClauses().get(0).getOperator());
		assertThat(statement.getWhereClauses().get(0).getRight(), contains(
			"'foo'", "'bar'"
		));

	}

	@Test
	public void testError_InvalidStart() {
		String input = """
			blah""";
		DataFormatException ex = assertThrows(DataFormatException.class, () -> parse(input));
		assertEquals("Unexpected token (expected \"SELECT\") at position [line=0, column=0]: blah", ex.getMessage());
	}

	@Test
	public void testError_InvalidFrom() {
		String input = """
			from Blah""";
		DataFormatException ex = assertThrows(DataFormatException.class, () -> parse(input));
		assertEquals("Invalid FROM statement. Unknown resource type 'Blah' at position: [line=0, column=5]", ex.getMessage());
	}

	@Test
	public void testError_InvalidLimit() {
		String input = """
			from Patient
			select name.given
			limit foo
			""";
		DataFormatException ex = assertThrows(DataFormatException.class, () -> parse(input));
		assertEquals("Unexpected token (expected integer value) at position [line=2, column=6]: foo", ex.getMessage());
	}

	@Test
	public void testError_InvalidSelect_EqualsParens() {
		String input = """
				from
				  Patient
				where
				  name.given = ('Foo')
			""";
		DataFormatException ex = assertThrows(DataFormatException.class, () -> parse(input));
		assertEquals("Unexpected token (expected quoted string) at position [line=3, column=3]: (", ex.getMessage());
	}

	@Test
	public void testError_InvalidSelect_InWithoutParens() {
		String input = """
				from
				  Patient
				where
				  name.given in 'Foo'
			""";
		DataFormatException ex = assertThrows(DataFormatException.class, () -> parse(input));
		assertEquals("Unexpected token (expected \"(\") at position [line=3, column=14]: in", ex.getMessage());
	}

	@Test
	public void testError_InvalidSelect_InWithoutPipe() {
		String input = """
				from
				  Patient
				where
				  name.given in ('foo' 'bar')
			""";
		DataFormatException ex = assertThrows(DataFormatException.class, () -> parse(input));
		assertEquals("Unexpected token at position [line=3, column=22]: 'bar'", ex.getMessage());
	}

	@Test
	public void testError_InvalidSelect_InWithoutContent() {
		String input = """
				from
				  Patient
				where
				  name.given in
			""";
		DataFormatException ex = assertThrows(DataFormatException.class, () -> parse(input));
		assertEquals("Unexpected end of stream", ex.getMessage());
	}

	@Test
	public void testError_InvalidSelect_InWithoutEnd() {
		String input = """
				from
				  Patient
				where
				  name.given in ('foo' | 'bar'
			""";
		DataFormatException ex = assertThrows(DataFormatException.class, () -> parse(input));
		assertEquals("Unexpected end of stream", ex.getMessage());
	}

	@Test
	public void testError_MultipleWhere() {
		String input = """
			from
			  Patient
			where
			  name.given = 'Foo'
			search
			  _id = '123'
			where
			  name.family = 'Foo'
			select
			  name.given[0],
			  name.family
			""";

		DataFormatException ex = assertThrows(DataFormatException.class, () -> parse(input));
		assertEquals("Unexpected token at position [line=6, column=0]: where", ex.getMessage());
	}

	@Test
	public void testError_MultipleFrom() {
		String input = """
			from
			  Patient
			select
			  name.given[0],
			  name.family
			from
			  Patient
			""";

		DataFormatException ex = assertThrows(DataFormatException.class, () -> parse(input));
		assertEquals("Unexpected token at position [line=5, column=0]: from", ex.getMessage());
	}

	@Test
	public void testError_NoText() {
		String input = "  \n  ";
		DataFormatException ex = assertThrows(DataFormatException.class, () -> parse(input));
		assertEquals("Unexpected end of stream (expected \"FROM\")", ex.getMessage());
	}

	@Test
	public void testError_MissingSelect() {
		String input = """
			from Patient where""";
		DataFormatException ex = assertThrows(DataFormatException.class, () -> parse(input));
		assertEquals("Unexpected end of stream (expected \"SELECT\")", ex.getMessage());
	}

}
