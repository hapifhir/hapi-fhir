package ca.uhn.fhir.jpa.migrate.taskdef.containertests;

import ca.uhn.fhir.jpa.migrate.DriverTypeEnum;
import ca.uhn.fhir.jpa.migrate.JdbcUtils;
import ca.uhn.fhir.jpa.migrate.taskdef.BaseTask;
import ca.uhn.fhir.jpa.migrate.taskdef.ColumnTypeEnum;
import ca.uhn.fhir.jpa.migrate.taskdef.MigrationTaskExecutionResultEnum;
import ca.uhn.fhir.jpa.migrate.taskdef.ModifyColumnTask;
import ca.uhn.fhir.jpa.migrate.tasks.api.Builder;
import jakarta.annotation.Nonnull;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.testcontainers.containers.MSSQLServerContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers(disabledWithoutDocker=true)
public class SqlServerStandardCollectedMigrationTest extends BaseCollectedMigrationTaskSuite {

	@RegisterExtension
	static TestContainerDatabaseMigrationExtension ourContainerExtension =
		new TestContainerDatabaseMigrationExtension(
			DriverTypeEnum.MSSQL_2012,
			new MSSQLServerContainer<>("mcr.microsoft.com/mssql/server:2019-latest")
		.withEnv("ACCEPT_EULA", "Y")
		.withEnv("MSSQL_PID", "Standard") // Product id: Sql Server Enterprise vs Standard vs Developer vs ????
		);

	@Override
	@Nonnull
	protected DriverTypeEnum.ConnectionProperties getConnectionProperties() {
		return ourContainerExtension.getConnectionProperties();
	}

	@Nested
	class ModifyColumnTaskTests implements BaseMigrationTaskTestSuite {
		@Override
		public Support getSupport() {
			return SqlServerStandardCollectedMigrationTest.this.getSupport();
		}

		@Test
		public void testIncreaseVarcharColumnSize() throws SQLException {
			int expectedColumnSize = 200;

			// given an existing varchar2 column of a specific size
			Builder builder = getSupport().getBuilder();
			String tableName = "TABLE_MODIFY" + System.currentTimeMillis();
			String columnName = "col1";
			Builder.BuilderAddTableByColumns tableBuilder = builder.addTableByColumns("1", tableName, "id");
			tableBuilder.addColumn("id").nonNullable().type(ColumnTypeEnum.LONG);
			tableBuilder.addColumn(columnName).nonNullable().type(ColumnTypeEnum.STRING, 100);
			getSupport().executeAndClearPendingTasks();

			// when we increase the column size
			ModifyColumnTask task = new ModifyColumnTask("1", "123456.7");
			task.setTableName(tableName);
			task.setColumnName(columnName);
			task.setColumnType(ColumnTypeEnum.STRING);
			task.setColumnLength(expectedColumnSize);
			task.setNullable(false);
			builder.onTable(tableName).addTask(task);

			getSupport().executeAndClearPendingTasks();

			// we wait since the ONLINE path is async.
			Awaitility.await("increasing column size").atMost(10, TimeUnit.SECONDS).untilAsserted(
				() -> assertThat(new JdbcUtils.ColumnType(ColumnTypeEnum.STRING, expectedColumnSize)).isEqualTo(JdbcUtils.getColumnType(getConnectionProperties(), tableName, columnName)));

			// then assert that we specify the collation with the type expansion
			assertThat(task.getExecutionResult()).isEqualTo(MigrationTaskExecutionResultEnum.APPLIED);
			List<BaseTask.ExecutedStatement> executedStatements = task.getExecutedStatements();
			assertThat(executedStatements).hasSize(1);

			String executedSql = executedStatements.get(0).getSql();
			assertThat(executedSql).contains("alter column COL1 varchar2(200 char) not null");

		}
	}

}
