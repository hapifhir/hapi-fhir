package ca.uhn.fhir.jpa.migrate.taskdef;

import ca.uhn.fhir.jpa.migrate.DriverTypeEnum;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RenameTableTaskDbSpecificTest {

	private String createRenameTableSql(DriverTypeEnum theDriverTypeEnum) {
		RenameTableTask task = new RenameTableTask("1", "1", "NEWNAME");
		task.setDriverType(theDriverTypeEnum);
		task.setTableName("SOMETABLE");

		return task.buildRenameTableSqlStatement();
	}

	@Test
	public void testBuildSqlStatementForMySql() {
		assertEquals("RENAME TABLE SOMETABLE TO NEWNAME", createRenameTableSql(DriverTypeEnum.MYSQL_5_7));
	}

	@Test
	public void testBuildSqlStatementForDerby() {
		assertEquals("RENAME TABLE SOMETABLE TO NEWNAME", createRenameTableSql(DriverTypeEnum.DERBY_EMBEDDED));
	}

	@Test
	public void testBuildSqlStatementForOracle() {
		assertEquals("ALTER TABLE SOMETABLE RENAME TO NEWNAME", createRenameTableSql(DriverTypeEnum.ORACLE_12C));
	}

	@Test
	public void testBuildSqlStatementForMariaDB() {
		assertEquals("ALTER TABLE SOMETABLE RENAME TO NEWNAME", createRenameTableSql(DriverTypeEnum.MARIADB_10_1));
	}

	@Test
	public void testBuildSqlStatementForPostgres() {
		assertEquals("ALTER TABLE SOMETABLE RENAME TO NEWNAME", createRenameTableSql(DriverTypeEnum.POSTGRES_9_4));
	}

	@Test
	public void testBuildSqlStatementForH2() {
		assertEquals("ALTER TABLE SOMETABLE RENAME TO NEWNAME", createRenameTableSql(DriverTypeEnum.H2_EMBEDDED));
	}

	@Test
	public void testBuildSqlStatementForMsSql() {
		assertEquals("sp_rename 'SOMETABLE', 'NEWNAME'", createRenameTableSql(DriverTypeEnum.MSSQL_2012));
	}

}
