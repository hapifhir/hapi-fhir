package ca.uhn.fhir.jpa.migrate.taskdef;

import ca.uhn.fhir.jpa.migrate.DriverTypeEnum;
import ca.uhn.fhir.jpa.migrate.JdbcUtils;
import ca.uhn.fhir.jpa.migrate.taskdef.containertests.BaseMigrationTaskTestSuite;
import ca.uhn.fhir.jpa.migrate.tasks.api.Builder;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public interface AddForeignKeyTaskITTestSuite extends BaseMigrationTaskTestSuite {

	@Test
	default void addForeignKey_multiCols_works() throws SQLException {
		// setup
		// the database is shared by every test on this engine, so suffix our object names to keep them unique
		String suffix = String.valueOf(System.currentTimeMillis());
		addMultiColForeignKeyTasks(suffix);

		// test
		getSupport().executeAndClearPendingTasks();

		// validate
		Set<String> fks = JdbcUtils.getForeignKeys(getSupport().getConnectionProperties(),
			"HOME" + suffix,
			"FOREIGNTBL" + suffix);
		assertFalse(fks.isEmpty());
		assertTrue(fks.contains("FK_HOME_FOREIGN" + suffix));

		// sanity check - re-declaring the same tasks against the same objects and running them again
		// is a no-op, not a failure
		addMultiColForeignKeyTasks(suffix);
		getSupport().executeAndClearPendingTasks();

		fks = JdbcUtils.getForeignKeys(getSupport().getConnectionProperties(),
			"HOME" + suffix,
			"FOREIGNTBL" + suffix);
		assertTrue(fks.contains("FK_HOME_FOREIGN" + suffix));
	}

	@Test
	default void addForeignKey_withDeleteCascade_addsForeignKey() throws SQLException {
		// setup
		// the database is shared by every test on this engine, so suffix our object names to keep them unique
		String suffix = String.valueOf(System.currentTimeMillis());
		addForeignKeyTasksWithOptionalDeleteCascade(suffix, true);

		// test
		getSupport().executeAndClearPendingTasks();

		// verify
		Map<String, Boolean> fks = JdbcUtils.getForeignKeysAndDeelteCascadeRule(getSupport().getConnectionProperties(),
			"CUSTOMERS" + suffix,
			"ORDERS" + suffix);
		assertFalse(fks.isEmpty());
		assertTrue(fks.get("FK_CO_ID" + suffix));

		// sanity check - re-declaring the same tasks against the same objects and running them again
		// is a no-op, not a failure
		addForeignKeyTasksWithOptionalDeleteCascade(suffix, true);
		getSupport().executeAndClearPendingTasks();

		fks = JdbcUtils.getForeignKeysAndDeelteCascadeRule(getSupport().getConnectionProperties(),
			"CUSTOMERS" + suffix,
			"ORDERS" + suffix);
		assertTrue(fks.get("FK_CO_ID" + suffix));
	}

	@Test
	default void getForeignKeysAndRuleset_singleColumnForeignKeys_reportsDeleteCascadeAccurately() throws SQLException {
		// setup
		String table1 = "CASCADE_" + String.valueOf(System.currentTimeMillis());
		String table2 = "NONCASCADE_" + String.valueOf(System.currentTimeMillis());
		addForeignKeyTasksWithOptionalDeleteCascade(table1, true);
		addForeignKeyTasksWithOptionalDeleteCascade(table2, false);
		getSupport().executeAndClearPendingTasks();

		DriverTypeEnum.ConnectionProperties properties = getSupport().getConnectionProperties();

		// test
		Map<String, Boolean> cascading =
			JdbcUtils.getForeignKeysAndDeelteCascadeRule(properties, "CUSTOMERS" + table1, "ORDERS" + table1);
		Map<String, Boolean> nonCascading =
			JdbcUtils.getForeignKeysAndDeelteCascadeRule(properties, "CUSTOMERS" + table2, "ORDERS" + table2);


		// verify
		assertThat(cascading)
			.as("ON DELETE CASCADE must be reported as cascading on %s", properties.getDriverType())
			.containsOnly(entry("FK_CO_ID" + table1, true));
		assertThat(nonCascading)
			.as("a foreign key with no delete rule must not be reported as cascading on %s", properties.getDriverType())
			.containsOnly(entry("FK_CO_ID" + table2, false));
	}

	@Test
	default void getForeignKeysAndRuleset_multiColumnForeignKey_reportsSingleEntry() throws SQLException {
		// setup
		String suffix = String.valueOf(System.currentTimeMillis());
		addMultiColForeignKeyTasks(suffix);
		getSupport().executeAndClearPendingTasks();

		// test
		// a multi-column foreign key yields one metadata row per column, all carrying the same delete rule
		Map<String, Boolean> foreignKeys = JdbcUtils.getForeignKeysAndDeelteCascadeRule(
			getSupport().getConnectionProperties(), "HOME" + suffix, "FOREIGNTBL" + suffix);

		// validate
		assertThat(foreignKeys)
			.as("a multi-column foreign key must collapse to one cascading entry on %s", getSupport().getConnectionProperties().getDriverType())
			.containsOnly(entry("FK_HOME_FOREIGN" + suffix, false));
	}

	/**
	 * Queues the tasks for {@link #addForeignKey_multiCols_works()}.
	 * Extracted so the same task definitions can be queued (and so executed) more than once.
	 *
	 * @param theSuffix appended to every object name to keep it unique within the shared database.
	 *                     Pass the same value to re-declare the same objects.
	 */
	private void addMultiColForeignKeyTasks(String theSuffix) {
		Builder builder = getSupport().getBuilder();
		Builder.BuilderAddTableByColumns table = builder.addTableByColumns("1", "HOME" + theSuffix, "PID1", "PID2");
		table.addColumn("PID1").nonNullable().type(ColumnTypeEnum.LONG);
		table.addColumn("PID2").nonNullable().type(ColumnTypeEnum.LONG);
		table.addColumn("TEXTCOL").nullable().type(ColumnTypeEnum.STRING, 255);

		Builder.BuilderAddTableByColumns table2 = builder.addTableByColumns("2", "FOREIGNTBL" + theSuffix, "PID");
		table2.addColumn("PID").nonNullable().type(ColumnTypeEnum.LONG);
		table2.addColumn("HOMEREF1").nullable().type(ColumnTypeEnum.LONG);
		table2.addColumn("HOMEREF2").nullable().type(ColumnTypeEnum.LONG);
		table2.addForeignKey("3", "FK_HOME_FOREIGN" + theSuffix)
			.toColumns("HOMEREF1", "HOMEREF2")
			.references("HOME" + theSuffix, "PID1", "PID2");
	}

	/**
	 * Queues the tasks for {@link #addForeignKey_withDeleteCascade_addsForeignKey()}.
	 * Extracted so the same task definitions can be queued (and so executed) more than once.
	 *
	 * @param theSuffix appended to every object name to keep it unique within the shared database.
	 *                     Pass the same value to re-declare the same objects.
	 */
	private void addForeignKeyTasksWithOptionalDeleteCascade(String theSuffix, boolean theDeleteCascade) {
		Builder builder = getSupport().getBuilder();
		Builder.BuilderAddTableByColumns table = builder.addTableByColumns("10", "CUSTOMERS" + theSuffix, "ID");
		table.addColumn("ID").nonNullable().type(ColumnTypeEnum.INT);
		table.addColumn("NAME").nullable().type(ColumnTypeEnum.STRING, 200);

		Builder.BuilderAddTableByColumns table2 = builder.addTableByColumns("20", "ORDERS" + theSuffix, "ID");
		table2.addColumn("ID").nonNullable().type(ColumnTypeEnum.INT);
		table2.addColumn("CUSTOMERID").nonNullable().type(ColumnTypeEnum.INT);
		Builder.BuilderWithTableName.BuilderAddForeignKey.BuilderAddForeignKeyToColumn key = table2.addForeignKey("20260813.30", "FK_CO_ID" + theSuffix)
			.toColumn("CUSTOMERID");
		if (theDeleteCascade) {
			key.withDeleteCascade();
		}
		key.references("CUSTOMERS" + theSuffix, "ID");
	}

}
