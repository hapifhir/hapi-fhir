package ca.uhn.fhir.jpa.migrate.taskdef;

import ca.uhn.fhir.jpa.migrate.JdbcUtils;
import ca.uhn.fhir.jpa.migrate.taskdef.containertests.BaseMigrationTaskTestSuite;
import ca.uhn.fhir.jpa.migrate.tasks.api.Builder;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for ModifyColumnTask.
 */
public interface ModifyColumnTaskTestSuite extends BaseMigrationTaskTestSuite {

	@Test
	default void testModifyColumnSize_whenColumnOfStringType_willAcceptUnicodeCharacters() throws SQLException {
		// given
		Builder builder = getSupport().getBuilder();
		String tableName = "TABLE_COLUMN_STRING" + System.currentTimeMillis();
		String stringColumnName = "col1";
		Builder.BuilderAddTableByColumns tableBuilder = builder.addTableByColumns("1", tableName, "id");
		tableBuilder.addColumn("id").nonNullable().type(ColumnTypeEnum.LONG);
		tableBuilder.addColumn(stringColumnName).nonNullable().type(ColumnTypeEnum.STRING, 1);

		// when increasing the size of the string column to N = 5
		builder.onTable(tableName)
			.modifyColumn("2", stringColumnName)
			.nonNullable()
			.withType(ColumnTypeEnum.STRING, 5);

		getSupport().executeAndClearPendingTasks();
		assertThat(JdbcUtils.getColumnNames(getSupport().getConnectionProperties(), tableName)).contains(stringColumnName);

		// then we can insert the maximum set number of characters including characters taking 2 bytes of storage
		ExecuteRawSqlTask executeRawSqlTask = new ExecuteRawSqlTask("1", "1");
		executeRawSqlTask.addSql("insert into " + tableName + " values (1, '㏰㏱㏲㏳㋿')"); // U+32F0\U+32F1\U+32F2\U+32F3\U+32FF

		builder.addTask(executeRawSqlTask);
		getSupport().executeAndClearPendingTasks();
	}

}
