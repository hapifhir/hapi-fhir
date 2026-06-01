package ca.uhn.fhir.jpa.migrate.taskdef;

import ca.uhn.fhir.jpa.migrate.DriverTypeEnum;
import ca.uhn.fhir.jpa.migrate.JdbcUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

public class AddForeignKeyTaskTest extends BaseTest {


	@ParameterizedTest(name = "{index}: {0}")
	@MethodSource("data")
	public void testAddForeignKey(Supplier<TestDatabaseDetails> theTestDatabaseDetails) throws SQLException {
		before(theTestDatabaseDetails);

		executeSql("create table HOME (PID bigint not null, TEXTCOL varchar(255), primary key (PID))");
		executeSql("create table FOREIGNTBL (PID bigint not null, HOMEREF bigint)");
		assertThat(JdbcUtils.getForeignKeys(getConnectionProperties(), "HOME", "FOREIGNTBL")).isEmpty();

		AddForeignKeyTask task = new AddForeignKeyTask("1", "1");
		task.setTableName("FOREIGNTBL");
		task.setColumnName("HOMEREF");
		task.setConstraintName("FK_HOME_FOREIGN");
		task.setForeignColumnName("PID");
		task.setForeignTableName("HOME");
		getMigrator().addTask(task);

		getMigrator().migrate();

		assertThat(JdbcUtils.getForeignKeys(getConnectionProperties(), "HOME", "FOREIGNTBL")).containsExactly("FK_HOME_FOREIGN");

		// Make sure additional calls don't crash
		getMigrator().migrate();
		getMigrator().migrate();
	}

	@ParameterizedTest(name = "{index}: {0}")
	@MethodSource("data")
	void testAddCompositeForeignKey(Supplier<TestDatabaseDetails> theTestDatabaseDetails) throws SQLException {
		// setup
		before(theTestDatabaseDetails);

		executeSql("create table PARENT_TBL (PID bigint not null, PARTITION_ID bigint not null, TEXTCOL varchar(255), primary key (PID, PARTITION_ID))");
		executeSql("create table CHILD_TBL (PID bigint not null, PARENT_PID bigint, PARENT_PARTITION_ID bigint)");
		assertThat(JdbcUtils.getForeignKeys(getConnectionProperties(), "PARENT_TBL", "CHILD_TBL")).isEmpty();

		AddForeignKeyTask task = new AddForeignKeyTask("1", "1");
		task.setTableName("CHILD_TBL");
		task.setColumnNames(List.of("PARENT_PID", "PARENT_PARTITION_ID"));
		task.setConstraintName("FK_CHILD_PARENT");
		task.setForeignColumnNames(List.of("PID", "PARTITION_ID"));
		task.setForeignTableName("PARENT_TBL");
		getMigrator().addTask(task);

		// execute
		getMigrator().migrate();

		assertThat(JdbcUtils.getForeignKeys(getConnectionProperties(), "PARENT_TBL", "CHILD_TBL"))
			.containsExactly("FK_CHILD_PARENT");

		// validate, make sure additional calls don't crash
		getMigrator().migrate();
		getMigrator().migrate();
	}

	/**
	 * Verifies MySQL/MariaDB SQL generation quotes column names with backticks.
	 */
	@Test
	void testAddCompositeForeignKey_withMySql_quotesColumnNamesWithBackticks() throws SQLException {
		// setup
		try (MockedStatic<JdbcUtils> jdbcUtils = Mockito.mockStatic(JdbcUtils.class)) {
			jdbcUtils.when(() -> JdbcUtils.getForeignKeys(any(), anyString(), anyString()))
					.thenReturn(Collections.emptySet());

			AddForeignKeyTask task = new AddForeignKeyTask("1", "1");
			task.setTableName("CHILD_TBL");
			task.setColumnNames(List.of("PARENT_PID", "PARTITION_ID"));
			task.setConstraintName("FK_CHILD_PARENT");
			task.setForeignColumnNames(List.of("PID", "PARTITION_ID"));
			task.setForeignTableName("PARENT_TBL");
			task.setDriverType(DriverTypeEnum.MYSQL_5_7);
			// Dry run skips actual SQL execution but still captures statements for verification
			task.setDryRun(true);

			// execute
			task.doExecute();
			List<BaseTask.ExecutedStatement> executedStatements = task.getExecutedStatements();

			// validate
			assertThat(executedStatements).hasSize(1);
			String sql = executedStatements.get(0).getSql();
			assertThat(sql).contains("(`PARENT_PID`, `PARTITION_ID`)");
			assertThat(sql).contains("(`PID`, `PARTITION_ID`)");
		}
	}

}
