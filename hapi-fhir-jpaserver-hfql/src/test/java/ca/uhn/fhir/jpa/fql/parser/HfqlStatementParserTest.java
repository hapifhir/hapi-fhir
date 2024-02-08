package ca.uhn.fhir.jpa.fql.parser;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.DataFormatException;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@TestMethodOrder(MethodOrderer.MethodName.class)
@SuppressWarnings("SqlDialectInspection")
public class HfqlStatementParserTest {

	private static final FhirContext ourCtx = FhirContext.forR4Cached();

	@Test
	public void testCountAndGroup() {
		String input = """
					select Count(*), name.given, name.family
					from Patient
					group by name.given, name.family
			""";

		HfqlStatement statement = parse(input);
		assertThat(statement.getFromResourceName()).isEqualTo("Patient");
		assertThat(statement.getSelectClauses().size()).isEqualTo(3);
		assertThat(statement.getSelectClauses().get(0).getClause()).isEqualTo("*");
		assertThat(statement.getSelectClauses().get(0).getOperator()).isEqualTo(HfqlStatement.SelectClauseOperator.COUNT);
		assertThat(statement.getSelectClauses().get(0).getAlias()).isEqualTo("Count(*)");
		assertThat(statement.getSelectClauses().get(1).getClause()).isEqualTo("name.given");
		assertThat(statement.getSelectClauses().get(1).getOperator()).isEqualTo(HfqlStatement.SelectClauseOperator.SELECT);
		assertThat(statement.getSelectClauses().get(2).getClause()).isEqualTo("name.family");
		assertThat(statement.getSelectClauses().get(2).getOperator()).isEqualTo(HfqlStatement.SelectClauseOperator.SELECT);
		assertThat(statement.getGroupByClauses().size()).isEqualTo(2);
		assertThat(statement.getGroupByClauses()).containsExactly("name.given", "name.family");

	}


	@Test
	public void testFromSelect() {
		String input = """
					from Patient
					select
					   name.given[0],
					   name.family
			""";

		HfqlStatement statement = parse(input);
		assertThat(statement.getFromResourceName()).isEqualTo("Patient");
		assertThat(statement.getSelectClauses().size()).isEqualTo(2);
		assertThat(statement.getSelectClauses().get(0).getClause()).isEqualTo("name.given[0]");
		assertThat(statement.getSelectClauses().get(0).getAlias()).isEqualTo("name.given[0]");
		assertThat(statement.getSelectClauses().get(0).getOperator()).isEqualTo(HfqlStatement.SelectClauseOperator.SELECT);
		assertThat(statement.getSelectClauses().get(1).getClause()).isEqualTo("name.family");
		assertThat(statement.getSelectClauses().get(1).getAlias()).isEqualTo("name.family");
	}

	@Test
	public void testSelect_SearchMatchWithEscapedCommaInArgument() {
		String input = """
					select name.given
					from Patient
					where
						id in search_match('name', 'A,B\\,B')
			""";

		HfqlStatement statement = parse(input);
		assertThat(statement.getFromResourceName()).isEqualTo("Patient");
		assertThat(statement.getSelectClauses().size()).isEqualTo(1);
		assertThat(statement.getSelectClauses().get(0).getClause()).isEqualTo("name.given");
		assertThat(statement.getSelectClauses().get(0).getAlias()).isEqualTo("name.given");
		assertThat(statement.getSelectClauses().get(0).getOperator()).isEqualTo(HfqlStatement.SelectClauseOperator.SELECT);

		assertThat(statement.getWhereClauses().size()).isEqualTo(1);
		assertThat(statement.getWhereClauses().get(0).getLeft()).isEqualTo("id");
		assertThat(statement.getWhereClauses().get(0).getOperator()).isEqualTo(HfqlStatement.WhereClauseOperatorEnum.SEARCH_MATCH);
		assertThat(statement.getWhereClauses().get(0).getRight()).containsExactly("'name'", "'A,B\\,B'");

	}

	@Test
	public void testSelect_ValueWithPrefix() {
		String input = """
					SELECT id
					FROM Observation
					WHERE
					   id in search_match('value-quantity', 'lt500') AND
					   Patient.meta.versionId = '2' AND
					   value.ofType(string).lower().contains('running')
					ORDER BY id DESC
			""";

		HfqlStatement statement = parse(input);
		assertThat(statement.getFromResourceName()).isEqualTo("Observation");
		assertThat(statement.getWhereClauses().size()).isEqualTo(3);

		assertThat(statement.getWhereClauses().get(0).getLeft()).isEqualTo("id");
		assertThat(statement.getWhereClauses().get(0).getOperator()).isEqualTo(HfqlStatement.WhereClauseOperatorEnum.SEARCH_MATCH);
		assertThat(statement.getWhereClauses().get(0).getRight()).containsExactly("'value-quantity'", "'lt500'");

		assertThat(statement.getWhereClauses().get(1).getLeft()).isEqualTo("Patient.meta.versionId");
		assertThat(statement.getWhereClauses().get(1).getOperator()).isEqualTo(HfqlStatement.WhereClauseOperatorEnum.EQUALS);
		assertThat(statement.getWhereClauses().get(1).getRight()).containsExactly("'2'");

		assertThat(statement.getWhereClauses().get(2).getLeft()).isEqualTo("value.ofType(string).lower().contains('running')");
		assertThat(statement.getWhereClauses().get(2).getOperator()).isEqualTo(HfqlStatement.WhereClauseOperatorEnum.UNARY_BOOLEAN);
		assertThat(statement.getWhereClauses().get(2).getRight()).isEmpty();

		assertThat(statement.getOrderByClauses().size()).isEqualTo(1);
		assertThat(statement.getOrderByClauses().get(0).getClause()).isEqualTo("id");
		assertThat(statement.getOrderByClauses().get(0).isAscending()).isFalse();
	}

	@Test
	public void testWhere_UnaryBooleanAsLastStatement() {
		String input = """
					SELECT id
					FROM Observation
					WHERE
						id in search_match('code', 'http://loinc.org|34752-6')
						AND
					   value.ofType(string).lower().contains('running')
			""";

		HfqlStatement statement = parse(input);
		assertThat(statement.getFromResourceName()).isEqualTo("Observation");
		assertThat(statement.getWhereClauses().size()).isEqualTo(2);

		assertThat(statement.getWhereClauses().get(0).getLeft()).isEqualTo("id");
		assertThat(statement.getWhereClauses().get(0).getOperator()).isEqualTo(HfqlStatement.WhereClauseOperatorEnum.SEARCH_MATCH);
		assertThat(statement.getWhereClauses().get(0).getRight()).containsExactly("'code'", "'http://loinc.org|34752-6'");

		assertThat(statement.getWhereClauses().get(1).getLeft()).isEqualTo("value.ofType(string).lower().contains('running')");
		assertThat(statement.getWhereClauses().get(1).getOperator()).isEqualTo(HfqlStatement.WhereClauseOperatorEnum.UNARY_BOOLEAN);
		assertThat(statement.getWhereClauses().get(1).getRight()).isEmpty();
	}


	@Test
	public void testSelectFrom() {
		String input = """
					select
					   name.given[0],
					   name.family
					from Patient
			""";

		HfqlStatement statement = parse(input);
		assertThat(statement.getFromResourceName()).isEqualTo("Patient");
		assertThat(statement.getSelectClauses().size()).isEqualTo(2);
		assertThat(statement.getSelectClauses().get(0).getClause()).isEqualTo("name.given[0]");
		assertThat(statement.getSelectClauses().get(0).getAlias()).isEqualTo("name.given[0]");
		assertThat(statement.getSelectClauses().get(1).getClause()).isEqualTo("name.family");
		assertThat(statement.getSelectClauses().get(1).getAlias()).isEqualTo("name.family");
	}

	@Test
	public void testSelectComplexFhirPath_StringConcat() {
		String input = """
					SELECT FullName: Patient.name.given + ' ' + Patient.name.family
					FROM Patient
			""";

		HfqlStatement statement = parse(input);
		assertThat(statement.getFromResourceName()).isEqualTo("Patient");
		assertThat(statement.getSelectClauses().size()).isEqualTo(1);
		assertThat(statement.getSelectClauses().get(0).getClause()).isEqualTo("Patient.name.given + ' ' + Patient.name.family");
		assertThat(statement.getSelectClauses().get(0).getAlias()).isEqualTo("FullName");

	}

	@Test
	public void testSelectComplexFhirPath_StringConcat2() {
		String input = """
			SELECT
			   COL1: identifier[0].system + '|' + identifier[0].value,
			   identifier[0].system + '|' + identifier[0].value AS COL2,
			   identifier[0].system + '|' + identifier[0].value
			FROM
			   Patient
			""";

		HfqlStatement statement = parse(input);
		assertThat(statement.getFromResourceName()).isEqualTo("Patient");
		assertThat(statement.getSelectClauses().size()).isEqualTo(3);
		assertThat(statement.getSelectClauses().get(0).getClause()).isEqualTo("identifier[0].system + '|' + identifier[0].value");
		assertThat(statement.getSelectClauses().get(0).getAlias()).isEqualTo("COL1");
		assertThat(statement.getSelectClauses().get(1).getClause()).isEqualTo("identifier[0].system + '|' + identifier[0].value");
		assertThat(statement.getSelectClauses().get(1).getAlias()).isEqualTo("COL2");
		assertThat(statement.getSelectClauses().get(2).getClause()).isEqualTo("identifier[0].system + '|' + identifier[0].value");
		assertThat(statement.getSelectClauses().get(2).getAlias()).isEqualTo("identifier[0].system + '|' + identifier[0].value");

	}

	@Test
	public void testSelectDuplicateColumnsWithNoAlias() {
		String input = """
			SELECT
				name, name, name
			FROM
			   Patient
			""";

		HfqlStatement statement = parse(input);
		assertThat(statement.getFromResourceName()).isEqualTo("Patient");
		assertThat(statement.getSelectClauses().size()).isEqualTo(3);
		assertThat(statement.getSelectClauses().get(0).getClause()).isEqualTo("name");
		assertThat(statement.getSelectClauses().get(0).getAlias()).isEqualTo("name");
		assertThat(statement.getSelectClauses().get(1).getClause()).isEqualTo("name");
		assertThat(statement.getSelectClauses().get(1).getAlias()).isEqualTo("name2");
		assertThat(statement.getSelectClauses().get(2).getClause()).isEqualTo("name");
		assertThat(statement.getSelectClauses().get(2).getAlias()).isEqualTo("name3");

	}

	@Test
	public void testSelectAs() {
		String input = """
					SELECT Patient.name.given + ' ' + Patient.name.family as FullName
					FROM Patient
			""";

		HfqlStatement statement = parse(input);
		assertThat(statement.getFromResourceName()).isEqualTo("Patient");
		assertThat(statement.getSelectClauses().size()).isEqualTo(1);
		assertThat(statement.getSelectClauses().get(0).getClause()).isEqualTo("Patient.name.given + ' ' + Patient.name.family");
		assertThat(statement.getSelectClauses().get(0).getAlias()).isEqualTo("FullName");

	}

	@Test
	public void testSelectWhere_GreaterThan() {
		String input = """
					select id
					from Observation
					where
					   value.ofType(Quantity).value > 100
			""";

		HfqlStatement statement = parse(input);
		assertThat(statement.getWhereClauses().size()).isEqualTo(1);
		assertThat(statement.getWhereClauses().get(0).getLeft()).isEqualTo("value.ofType(Quantity).value");
		assertThat(statement.getWhereClauses().get(0).getRightAsStrings()).containsExactly(">", "100");
		assertThat(statement.getWhereClauses().get(0).getOperator()).isEqualTo(HfqlStatement.WhereClauseOperatorEnum.UNARY_BOOLEAN);
	}

	@Test
	public void testSelectOrderBy() {
		String input = """
					select id, name.family
					from Observation
					order by name.family, count(*)
			""";

		HfqlStatement statement = parse(input);
		assertThat(statement.getSelectClauses().stream().map(t -> t.getAlias()).collect(Collectors.toList())).containsExactly("id", "name.family");
		assertThat(statement.getOrderByClauses().size()).isEqualTo(2);
		assertThat(statement.getOrderByClauses().get(0).getClause()).isEqualTo("name.family");
		assertThat(statement.getOrderByClauses().get(0).isAscending()).isTrue();
		assertThat(statement.getOrderByClauses().get(1).getClause()).isEqualTo("count(*)");
		assertThat(statement.getOrderByClauses().get(1).isAscending()).isTrue();
	}

	@Test
	public void testSelectOrderBy_Directional() {
		String input = """
					select id, name.family
					from Observation
					order by name.family DESC, id ASC
			""";

		HfqlStatement statement = parse(input);
		assertThat(statement.getSelectClauses().stream().map(t -> t.getAlias()).collect(Collectors.toList())).containsExactly("id", "name.family");
		assertThat(statement.getOrderByClauses().size()).isEqualTo(2);
		assertThat(statement.getOrderByClauses().get(0).getClause()).isEqualTo("name.family");
		assertThat(statement.getOrderByClauses().get(0).isAscending()).isFalse();
		assertThat(statement.getOrderByClauses().get(1).getClause()).isEqualTo("id");
		assertThat(statement.getOrderByClauses().get(1).isAscending()).isTrue();
	}

	private HfqlStatement parse(String theInput) {
		return new HfqlStatementParser(ourCtx, theInput).parse();
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

		HfqlStatement statement = parse(input);
		assertThat(statement.getFromResourceName()).isEqualTo("Patient");
		assertThat(statement.getSelectClauses().size()).isEqualTo(2);
		assertThat(statement.getSelectClauses().get(0).getClause()).isEqualTo("name.given[0]");
		assertThat(statement.getSelectClauses().get(1).getClause()).isEqualTo("name.family");
		assertThat(statement.getWhereClauses().size()).isEqualTo(2);
		assertThat(statement.getWhereClauses().get(0).getLeft()).isEqualTo("name.given");
		assertThat(statement.getWhereClauses().get(0).getOperator()).isEqualTo(HfqlStatement.WhereClauseOperatorEnum.EQUALS);
		assertThat(statement.getWhereClauses().get(0).getRight()).containsExactly("'Foo ' Chalmers'");
		assertThat(statement.getWhereClauses().get(1).getLeft()).isEqualTo("name.family");
		assertThat(statement.getWhereClauses().get(1).getRight()).containsExactly("'blah'");
		assertThat(statement.getWhereClauses().get(1).getOperator()).isEqualTo(HfqlStatement.WhereClauseOperatorEnum.EQUALS);
	}

	@Test
	public void testFromSearchWhereSelect() {
		String input = """
			from
			  Observation
			where
			  subject.name in ('foo' | 'bar')
			and
			  id in search_match('_id', '123')
			and
			  status = 'final'
			select
			  id
			""";

		HfqlStatement statement = parse(input);
		assertThat(statement.getFromResourceName()).isEqualTo("Observation");
		assertThat(statement.getSelectClauses().size()).isEqualTo(1);
		assertThat(statement.getSelectClauses().get(0).getClause()).isEqualTo("id");
		assertThat(statement.getWhereClauses().size()).isEqualTo(3);
		assertThat(statement.getWhereClauses().get(0).getLeft()).isEqualTo("subject.name");
		assertThat(statement.getWhereClauses().get(0).getOperator()).isEqualTo(HfqlStatement.WhereClauseOperatorEnum.IN);
		assertThat(statement.getWhereClauses().get(0).getRight()).containsExactly("'foo'", "'bar'");
		assertThat(statement.getWhereClauses().get(1).getLeft()).isEqualTo("id");
		assertThat(statement.getWhereClauses().get(1).getOperator()).isEqualTo(HfqlStatement.WhereClauseOperatorEnum.SEARCH_MATCH);
		assertThat(statement.getWhereClauses().get(1).getRight()).containsExactly("'_id'", "'123'");
		assertThat(statement.getWhereClauses().get(2).getLeft()).isEqualTo("status");
		assertThat(statement.getWhereClauses().get(2).getOperator()).isEqualTo(HfqlStatement.WhereClauseOperatorEnum.EQUALS);
		assertThat(statement.getWhereClauses().get(2).getRight()).containsExactly("'final'");

	}

	@Test
	public void testFromWhereSelect_RichSearchExpression() {
		String input = """
			from
			  Observation
			where
			  id in search_match('_has:Observation:subject:device.identifier', '1234-5')
			select
			  id
			""";

		HfqlStatement statement = parse(input);
		assertThat(statement.getFromResourceName()).isEqualTo("Observation");
		assertThat(statement.getSelectClauses().size()).isEqualTo(1);
		assertThat(statement.getSelectClauses().get(0).getClause()).isEqualTo("id");
		assertThat(statement.getWhereClauses().size()).isEqualTo(1);
		assertThat(statement.getWhereClauses().get(0).getLeft()).isEqualTo("id");
		assertThat(statement.getWhereClauses().get(0).getOperator()).isEqualTo(HfqlStatement.WhereClauseOperatorEnum.SEARCH_MATCH);
		assertThat(statement.getWhereClauses().get(0).getRight()).containsExactly("'_has:Observation:subject:device.identifier'", "'1234-5'");

	}

	@Test
	public void testFromSearchWhereSelectLimit() {
		String input = """
			from
			  Observation
			where
			  id in search_match('subject.name', 'foo', 'bar')
			and
			  id in search_match('_id', '123')
			and
			  id in search_match('status', 'final')
			select
			  id
			limit 123
			""";

		HfqlStatement statement = parse(input);
		assertThat(statement.getFromResourceName()).isEqualTo("Observation");
		assertThat(statement.getSelectClauses().size()).isEqualTo(1);
		assertThat(statement.getSelectClauses().get(0).getClause()).isEqualTo("id");
		assertThat(statement.getWhereClauses().size()).isEqualTo(3);
		assertThat(statement.getWhereClauses().get(0).getLeft()).isEqualTo("id");
		assertThat(statement.getWhereClauses().get(0).getOperator()).isEqualTo(HfqlStatement.WhereClauseOperatorEnum.SEARCH_MATCH);
		assertThat(statement.getWhereClauses().get(0).getRight()).containsExactly("'subject.name'", "'foo'", "'bar'");
		assertThat(statement.getWhereClauses().get(1).getLeft()).isEqualTo("id");
		assertThat(statement.getWhereClauses().get(1).getOperator()).isEqualTo(HfqlStatement.WhereClauseOperatorEnum.SEARCH_MATCH);
		assertThat(statement.getWhereClauses().get(1).getRight()).containsExactly("'_id'", "'123'");
		assertThat(statement.getWhereClauses().get(2).getLeft()).isEqualTo("id");
		assertThat(statement.getWhereClauses().get(2).getOperator()).isEqualTo(HfqlStatement.WhereClauseOperatorEnum.SEARCH_MATCH);
		assertThat(statement.getWhereClauses().get(2).getRight()).containsExactly("'status'", "'final'");
		assertThat(statement.getLimit()).isEqualTo(123);
	}

	@Test
	public void testNamedSelectClauseWithFhirPath() {
		String input = """
			select
			   Weight: value.ofType(Quantity).value,
			   Unit: value.ofType(Quantity).unit
			from Observation
			""";

		HfqlStatement statement = parse(input);
		assertThat(statement.getFromResourceName()).isEqualTo("Observation");
		assertThat(statement.getSelectClauses().size()).isEqualTo(2);
		assertThat(statement.getSelectClauses().get(0).getClause()).isEqualTo("value.ofType(Quantity).value");
		assertThat(statement.getSelectClauses().get(0).getAlias()).isEqualTo("Weight");
		assertThat(statement.getSelectClauses().get(1).getClause()).isEqualTo("value.ofType(Quantity).unit");
		assertThat(statement.getSelectClauses().get(1).getAlias()).isEqualTo("Unit");

	}


	@Test
	public void testFromWhereSelect_InClauseAndNamedSelects() {
		// One select with spaces, one without
		String input = """
			from
			    StructureDefinition
			where
			    id in search_match('url', 'foo', 'bar')
			select
			    Name : name,
			    URL:url
			""";
		HfqlStatement statement = parse(input);
		assertThat(statement.getFromResourceName()).isEqualTo("StructureDefinition");
		assertThat(statement.getSelectClauses().size()).isEqualTo(2);
		assertThat(statement.getSelectClauses().get(0).getClause()).isEqualTo("name");
		assertThat(statement.getSelectClauses().get(0).getAlias()).isEqualTo("Name");
		assertThat(statement.getSelectClauses().get(1).getClause()).isEqualTo("url");
		assertThat(statement.getSelectClauses().get(1).getAlias()).isEqualTo("URL");
		assertThat(statement.getWhereClauses().size()).isEqualTo(1);
		assertThat(statement.getWhereClauses().get(0).getLeft()).isEqualTo("id");
		assertThat(statement.getWhereClauses().get(0).getOperator()).isEqualTo(HfqlStatement.WhereClauseOperatorEnum.SEARCH_MATCH);
		assertThat(statement.getWhereClauses().get(0).getRight()).containsExactly("'url'", "'foo'", "'bar'");

	}

	@Test
	public void testError_InvalidStart() {
		String input = """
			blah""";
		DataFormatException ex = assertThrows(DataFormatException.class, () -> parse(input));
		assertThat(ex.getMessage()).isEqualTo("Unexpected token (expected \"SELECT\") at position [line=0, column=0]: blah");
	}

	@Test
	public void testError_DuplicateSelectAliases() {
		String input = """
			SELECT id as id, name as id
			FROM Patient
			""";
		DataFormatException ex = assertThrows(DataFormatException.class, () -> parse(input));
		assertThat(ex.getMessage()).isEqualTo("HAPI-2414: Duplicate SELECT column alias: id");
	}

	@Test
	public void testError_InvalidOrder() {
		String input = """
			select id
			from Patient
			order foo
			""";
		DataFormatException ex = assertThrows(DataFormatException.class, () -> parse(input));
		assertThat(ex.getMessage()).isEqualTo("Unexpected token (expected \"BY\") at position [line=2, column=6]: foo");
	}

	@Test
	public void testError_InvalidFrom() {
		String input = """
			from Blah""";
		DataFormatException ex = assertThrows(DataFormatException.class, () -> parse(input));
		assertThat(ex.getMessage()).isEqualTo("Invalid FROM statement. Unknown resource type 'Blah' at position: [line=0, column=5]");
	}

	@Test
	public void testError_InvalidLimit() {
		String input = """
			from Patient
			select name.given
			limit foo
			""";
		DataFormatException ex = assertThrows(DataFormatException.class, () -> parse(input));
		assertThat(ex.getMessage()).isEqualTo("Unexpected token (expected integer value) at position [line=2, column=6]: foo");
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
		assertThat(ex.getMessage()).isEqualTo("Unexpected token (expected quoted string) at position [line=3, column=3]: (");
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
		assertThat(ex.getMessage()).isEqualTo("Unexpected token (expected \"(\") at position [line=3, column=14]: in");
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
		assertThat(ex.getMessage()).isEqualTo("Unexpected token at position [line=3, column=22]: 'bar'");
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
		assertThat(ex.getMessage()).isEqualTo("Unexpected end of stream");
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
		assertThat(ex.getMessage()).isEqualTo("Unexpected end of stream");
	}

	@Test
	public void testError_MultipleWhere() {
		String input = """
			from
			  Patient
			where
			  _id = '123'
			where
			  name.family = 'Foo'
			select
			  name.given[0],
			  name.family
			""";

		DataFormatException ex = assertThrows(DataFormatException.class, () -> parse(input));
		assertThat(ex.getMessage()).isEqualTo("Unexpected token at position [line=4, column=0]: where");
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
		assertThat(ex.getMessage()).isEqualTo("Unexpected token at position [line=5, column=0]: from");
	}

	@Test
	public void testError_NoText() {
		String input = "  \n  ";
		DataFormatException ex = assertThrows(DataFormatException.class, () -> parse(input));
		assertThat(ex.getMessage()).isEqualTo("Unexpected end of stream (expected \"FROM\")");
	}

	@Test
	public void testError_MissingSelect() {
		String input = """
			from Patient where""";
		DataFormatException ex = assertThrows(DataFormatException.class, () -> parse(input));
		assertThat(ex.getMessage()).isEqualTo("Unexpected end of stream (expected \"SELECT\")");
	}

}
