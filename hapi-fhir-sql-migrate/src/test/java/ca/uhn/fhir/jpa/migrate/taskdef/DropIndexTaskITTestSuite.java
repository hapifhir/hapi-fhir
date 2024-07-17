package ca.uhn.fhir.jpa.migrate.taskdef;

import ca.uhn.fhir.jpa.migrate.JdbcUtils;
import ca.uhn.fhir.jpa.migrate.taskdef.containertests.BaseMigrationTaskTestSuite;
import ca.uhn.fhir.jpa.migrate.tasks.api.Builder;
import org.assertj.core.api.Assertions;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

/**
 * Integration tests for AddIndexTask.
 */
public interface DropIndexTaskITTestSuite extends BaseMigrationTaskTestSuite {


	@Test
	default void testDropIndex_dropsIndex() throws SQLException {
		// given
		Builder builder = getSupport().getBuilder();
		String tableName = "INDEX_DROP" + System.currentTimeMillis();
		Builder.BuilderAddTableByColumns tableBuilder = builder.addTableByColumns("1", tableName, "id");
		tableBuilder.addColumn("id").nonNullable().type(ColumnTypeEnum.LONG);
		tableBuilder.addColumn("col1").nullable().type(ColumnTypeEnum.STRING, 100);
		builder.onTable(tableName)
			.addIndex("2", "FOO")
			.unique(false)
			.online(false)
			.withColumns("col1");
		getSupport().executeAndClearPendingTasks();
		Assertions.assertThat(JdbcUtils.getIndexNames(getSupport().getConnectionProperties(), tableName)).contains("FOO");

		// when
		builder.onTable(tableName)
			.dropIndex("2", "FOO");
		getSupport().executeAndClearPendingTasks();

		// then
		Assertions.assertThat(JdbcUtils.getIndexNames(getSupport().getConnectionProperties(), tableName))
				.as("index FOO does not exist")
				.doesNotContain("FOO");
	}

	@Test
	default void testDropIndexOnline_dropsIndex() throws SQLException {
		// given
		Builder builder = getSupport().getBuilder();
		String tableName = "INDEX_DROP" + System.currentTimeMillis();
		Builder.BuilderAddTableByColumns tableBuilder = builder.addTableByColumns("1", tableName, "id");
		tableBuilder.addColumn("id").nonNullable().type(ColumnTypeEnum.LONG);
		tableBuilder.addColumn("col1").nullable().type(ColumnTypeEnum.STRING, 100);
		builder.onTable(tableName)
			.addIndex("2", "FOO")
			.unique(false)
			.online(false)
			.withColumns("col1");
		getSupport().executeAndClearPendingTasks();
		Assertions.assertThat(JdbcUtils.getIndexNames(getSupport().getConnectionProperties(), tableName)).contains("FOO");

		// when
		builder.onTable(tableName)
			.dropIndexOnline("2", "FOO");
		getSupport().executeAndClearPendingTasks();

		// then

		// we wait since the ONLINE path is async.
		Awaitility.await("index FOO does not exist").atMost(10, TimeUnit.SECONDS).untilAsserted(
			() -> Assertions.assertThat(JdbcUtils.getIndexNames(getSupport().getConnectionProperties(), tableName)).doesNotContain("FOO"));
	}

}
