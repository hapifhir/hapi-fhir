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
public interface AddIndexTaskITTestSuite extends BaseMigrationTaskTestSuite {

	@Test
	default void testAddIndexOnline_createsIndex() throws SQLException {
		// given
		Builder builder = getSupport().getBuilder();
		String tableName = "TABLE_ADD" + System.currentTimeMillis();
		Builder.BuilderAddTableByColumns tableBuilder = builder.addTableByColumns("1", tableName, "id");
		tableBuilder.addColumn("id").nonNullable().type(ColumnTypeEnum.LONG);
		tableBuilder.addColumn("col1").nullable().type(ColumnTypeEnum.STRING, 100);
		getSupport().executeAndClearPendingTasks();

		// when
		builder.onTable(tableName)
			.addIndex("2", "FOO")
			.unique(false)
			.online(true)
			.withColumns("col1");
		getSupport().executeAndClearPendingTasks();

		// then

		// we wait since the ONLINE path is async.
		Awaitility.await("index FOO exists").atMost(10, TimeUnit.SECONDS).untilAsserted(
			() -> Assertions.assertThat(JdbcUtils.getIndexNames(getSupport().getConnectionProperties(), tableName)).contains("FOO"));
	}

}
