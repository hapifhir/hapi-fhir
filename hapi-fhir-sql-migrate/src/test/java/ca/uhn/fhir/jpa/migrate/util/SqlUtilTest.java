package ca.uhn.fhir.jpa.migrate.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SqlUtilTest {

	@Test
	public void testSplit() {
		String input = """
			select
			  *
			       -- COMMENT
			  FROM FOO;
			
			-- Also a comment
			
			  ;
			      select BLAH
			""";
		List<String> statements = SqlUtil.splitSqlFileIntoStatements(input);
		assertEquals(2, statements.size());
		assertEquals("select  *  FROM FOO", statements.get(0).replace("\n ", " "));
		assertEquals("select BLAH", statements.get(1).replace("\n ", " "));
	}


	@ParameterizedTest
	@ValueSource(strings = {
		"primary key (PID, PARTITION_ID)",
		"primary key (    PID ,   PARTITION_ID   )",
		"PRIMARY KEY (PID,PARTITION_ID)"
	})
	public void testParseCreateTableStatementPrimaryKey_PostgresFormat(String thePrimaryKeyLine) {
		String input = """
			create table HFJ_IDX_CMB_TOK_NU (
			    PID bigint not null,
			    PARTITION_ID integer not null,
			    PARTITION_DATE date,
			    HASH_COMPLETE bigint not null,
			    IDX_STRING varchar(500) not null,
			    RES_ID bigint,
			    THE_PK_LINE
			);""";
		input = input.replace("THE_PK_LINE", thePrimaryKeyLine);

		Optional<SqlUtil.CreateTablePrimaryKey> output = SqlUtil.parseCreateTableStatementPrimaryKey(input);

		assertTrue(output.isPresent());
		assertEquals("HFJ_IDX_CMB_TOK_NU", output.get().getTableName());
		assertThat(output.get().getPrimaryKeyColumns()).asList().containsExactly("PID", "PARTITION_ID");
	}

	@Test
	public void testParseCreateTableStatementPrimaryKey_NoPk(String thePrimaryKeyLine) {
		String input = """
			create table HFJ_IDX_CMB_TOK_NU (
			    PID bigint not null,
			    PARTITION_ID integer not null
			);""";
		input = input.replace("THE_PK_LINE", thePrimaryKeyLine);

		Optional<SqlUtil.CreateTablePrimaryKey> output = SqlUtil.parseCreateTableStatementPrimaryKey(input);
		assertFalse(output.isPresent());
	}

}
