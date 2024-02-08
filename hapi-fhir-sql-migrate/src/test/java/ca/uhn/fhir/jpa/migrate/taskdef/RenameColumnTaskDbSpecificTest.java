package ca.uhn.fhir.jpa.migrate.taskdef;

import ca.uhn.fhir.jpa.migrate.DriverTypeEnum;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class RenameColumnTaskDbSpecificTest {

	@Test
	public void testBuildSqlStatementForMySql() {
		assertThat(createRenameColumnSql(DriverTypeEnum.MYSQL_5_7)).isEqualTo("ALTER TABLE SOMETABLE CHANGE COLUMN `myTextCol` `TEXTCOL` integer null");
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
		assertThat(createRenameColumnSql(DriverTypeEnum.DERBY_EMBEDDED)).isEqualTo("RENAME COLUMN SOMETABLE.myTextCol TO TEXTCOL");
	}

	@Test
	public void testBuildSqlStatementForMariaDB() {
		assertThat(createRenameColumnSql(DriverTypeEnum.MARIADB_10_1)).isEqualTo("ALTER TABLE SOMETABLE CHANGE COLUMN `myTextCol` `TEXTCOL` integer null");
	}

	@Test
	public void testBuildSqlStatementForPostgres() {
		assertThat(createRenameColumnSql(DriverTypeEnum.POSTGRES_9_4)).isEqualTo("ALTER TABLE SOMETABLE RENAME COLUMN myTextCol TO TEXTCOL");
	}

	@Test
	public void testBuildSqlStatementForMsSql() {
		assertThat(createRenameColumnSql(DriverTypeEnum.MSSQL_2012)).isEqualTo("sp_rename 'SOMETABLE.myTextCol', 'TEXTCOL', 'COLUMN'");
	}

	@Test
	public void testBuildSqlStatementForOracle() {
		assertThat(createRenameColumnSql(DriverTypeEnum.ORACLE_12C)).isEqualTo("ALTER TABLE SOMETABLE RENAME COLUMN myTextCol TO TEXTCOL");
	}

	@Test
	public void testBuildSqlStatementForH2() {
		assertThat(createRenameColumnSql(DriverTypeEnum.H2_EMBEDDED)).isEqualTo("ALTER TABLE SOMETABLE ALTER COLUMN myTextCol RENAME TO TEXTCOL");
	}

}
