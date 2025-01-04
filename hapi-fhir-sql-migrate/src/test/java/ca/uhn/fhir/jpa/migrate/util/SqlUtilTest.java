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

	@SuppressWarnings("TextBlockMigration")
	@Test
	public void testParseCreateTableStatementPrimaryKey_PostgresFormat_WithTabs() {
		String input = "create table HFJ_RES_TAG (\n" +
			"\t\tPID bigint not null,\n" +
			"\t\tPARTITION_ID integer not null,\n" +
			"\t\tPARTITION_DATE date,\n" +
			"\t\tTAG_ID bigint,\n" +
			"\t\tRES_ID bigint,\n" +
			"\t\tRES_TYPE varchar(40) not null,\n" +
			"\t\tprimary key (PID, PARTITION_ID),\n" +
			"\t\tconstraint IDX_RESTAG_TAGID unique (PARTITION_ID, RES_ID, TAG_ID)\n" +
			"\t)";

		// This sequence is the thing we're making sure still works. If the input string
		// no longer contains it, we've probably accidentally reformatted the problematic
		// string away
		assertThat(input).contains("create table HFJ_RES_TAG (\n\t\tPID bigint not null,\n");

		Optional<SqlUtil.CreateTablePrimaryKey> output = SqlUtil.parseCreateTableStatementPrimaryKey(input);

		assertTrue(output.isPresent());
		assertEquals("HFJ_RES_TAG", output.get().getTableName());
		assertThat(output.get().getPrimaryKeyColumns()).asList().containsExactly("PID", "PARTITION_ID");
	}

	@Test
	public void testParseCreateTableStatementPrimaryKey_NoPk() {
		String input = """
			create table HFJ_IDX_CMB_TOK_NU (
			    PID bigint not null,
			    PARTITION_ID integer not null
			);""";

		Optional<SqlUtil.CreateTablePrimaryKey> output = SqlUtil.parseCreateTableStatementPrimaryKey(input);
		assertFalse(output.isPresent());
	}

	@Test
	public void testParseAlterTableAddConstraintConstraintForeignKey() {
		String input = """
			alter table if exists MPI_LINK
				add constraint FK_EMPI_LINK_GOLDEN_RESOURCE
				foreign key (GOLDEN_RESOURCE_PID ,     GOLDEN_RESOURCE_PARTITION_ID)
				references HFJ_RESOURCE
			""";

		SqlUtil.AlterTableAddConstraint output = SqlUtil.parseAlterTableAddConstraintConstraintForeignKey(input).orElseThrow();
		assertEquals("MPI_LINK", output.getTableName());
		assertEquals("FK_EMPI_LINK_GOLDEN_RESOURCE", output.getConstraintName());
		assertThat(output.getColumns()).asList().containsExactly("GOLDEN_RESOURCE_PID", "GOLDEN_RESOURCE_PARTITION_ID");
		assertEquals("HFJ_RESOURCE", output.getReferences());
	}

	@SuppressWarnings("TextBlockMigration")
	@Test
	public void testParseAlterTableAddConstraintConstraintForeignKey_WithNewLine() {
		String input = "alter table if exists HFJ_BLK_EXPORT_COLFILE \n" +
			"       add constraint FK_BLKEXCOLFILE_COLLECT \n" +
			"       foreign key (COLLECTION_PID) \n" +
			"       references HFJ_BLK_EXPORT_COLLECTION";

		// This sequence is the thing we're making sure still works. If the input string
		// no longer contains it, we've probably accidentally reformatted the problematic
		// string away
		assertThat(input).contains("if exists HFJ_BLK_EXPORT_COLFILE \n ");

		SqlUtil.AlterTableAddConstraint output = SqlUtil.parseAlterTableAddConstraintConstraintForeignKey(input).orElseThrow();
		assertEquals("HFJ_BLK_EXPORT_COLFILE", output.getTableName());
		assertEquals("FK_BLKEXCOLFILE_COLLECT", output.getConstraintName());
		assertThat(output.getColumns()).asList().containsExactly("COLLECTION_PID");
		assertEquals("HFJ_BLK_EXPORT_COLLECTION", output.getReferences());
	}



}
