package ca.uhn.fhir.jpa.migrate.taskdef;

import ca.uhn.fhir.jpa.migrate.DriverTypeEnum;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RenameColumnTaskDbSpecificTest {

	@Test
	public void testBuildSqlStatementForMySql() {
		assertEquals("ALTER TABLE SOMETABLE CHANGE COLUMN `myTextCol` `TEXTCOL` integer null", createRenameColumnSql(DriverTypeEnum.MYSQL_5_7));
	}

	private String createRenameColumnSql(DriverTypeEnum theDriverTypeEnum) {
		RenameColumnTask task = new RenameColumnTask("1", "1");
		task.setDriverType(theDriverTypeEnum);
		task.setTableName("SOMETABLE");
		task.setOldName("myTextCol");
		task.setNewName("TEXTCOL");
		return task.buildRenameColumnSqlStatement("integer", "null");
	}

	@Test
	public void testBuildSqlStatementForDerby() {
		assertEquals("RENAME COLUMN SOMETABLE.myTextCol TO TEXTCOL", createRenameColumnSql(DriverTypeEnum.DERBY_EMBEDDED));
	}

	@Test
	public void testBuildSqlStatementForMariaDB() {
		assertEquals("ALTER TABLE SOMETABLE CHANGE COLUMN `myTextCol` `TEXTCOL` integer null", createRenameColumnSql(DriverTypeEnum.MARIADB_10_1));
	}

	@Test
	public void testBuildSqlStatementForPostgres() {
		assertEquals("ALTER TABLE SOMETABLE RENAME COLUMN myTextCol TO TEXTCOL", createRenameColumnSql(DriverTypeEnum.POSTGRES_9_4));
	}

	@Test
	public void testBuildSqlStatementForMsSql() {
		assertEquals("sp_rename 'SOMETABLE.myTextCol', 'TEXTCOL', 'COLUMN'", createRenameColumnSql(DriverTypeEnum.MSSQL_2012));
	}

	@Test
	public void testBuildSqlStatementForOracle() {
		assertEquals("ALTER TABLE SOMETABLE RENAME COLUMN myTextCol TO TEXTCOL", createRenameColumnSql(DriverTypeEnum.ORACLE_12C));
	}

	@Test
	public void testBuildSqlStatementForH2() {
		assertEquals("ALTER TABLE SOMETABLE ALTER COLUMN myTextCol RENAME TO TEXTCOL", createRenameColumnSql(DriverTypeEnum.H2_EMBEDDED));
	}

}
