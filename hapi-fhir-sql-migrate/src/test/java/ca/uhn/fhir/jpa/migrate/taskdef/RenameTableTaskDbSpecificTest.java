package ca.uhn.fhir.jpa.migrate.taskdef;

import ca.uhn.fhir.jpa.migrate.DriverTypeEnum;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RenameTableTaskDbSpecificTest {

	private String createRenameTableSql(DriverTypeEnum theDriverTypeEnum) {
		RenameTableTask task = new RenameTableTask("1", "1", "oldname","newname");
		task.setDriverType(theDriverTypeEnum);

		return task.buildRenameTableSqlStatement();
	}

	@Test
	public void testBuildSqlStatementForMySql() {
		assertEquals("rename table oldname to newname", createRenameTableSql(DriverTypeEnum.MYSQL_5_7));
	}

	@Test
	public void testBuildSqlStatementForDerby() {
		assertEquals("rename table oldname to newname", createRenameTableSql(DriverTypeEnum.DERBY_EMBEDDED));
	}

	@Test
	public void testBuildSqlStatementForOracle() {
		assertEquals("alter table oldname rename to newname", createRenameTableSql(DriverTypeEnum.ORACLE_12C));
	}

	@Test
	public void testBuildSqlStatementForMariaDB() {
		assertEquals("alter table oldname rename to newname", createRenameTableSql(DriverTypeEnum.MARIADB_10_1));
	}

	@Test
	public void testBuildSqlStatementForPostgres() {
		assertEquals("alter table oldname rename to newname", createRenameTableSql(DriverTypeEnum.POSTGRES_9_4));
	}

	@Test
	public void testBuildSqlStatementForH2() {
		assertEquals("alter table oldname rename to newname", createRenameTableSql(DriverTypeEnum.H2_EMBEDDED));
	}

	@Test
	public void testBuildSqlStatementForMsSql() {
		assertEquals("sp_rename 'oldname', 'newname'", createRenameTableSql(DriverTypeEnum.MSSQL_2012));
	}

}
