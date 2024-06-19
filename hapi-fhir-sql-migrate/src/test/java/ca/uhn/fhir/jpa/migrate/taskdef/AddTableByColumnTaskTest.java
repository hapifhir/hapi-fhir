package ca.uhn.fhir.jpa.migrate.taskdef;

import ca.uhn.fhir.jpa.migrate.DriverTypeEnum;
import ca.uhn.fhir.jpa.migrate.JdbcUtils;
import ca.uhn.fhir.jpa.migrate.tasks.api.BaseMigrationTasks;
import ca.uhn.fhir.jpa.migrate.tasks.api.Builder;
import ca.uhn.fhir.util.VersionEnum;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.sql.SQLException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AddTableByColumnTaskTest extends BaseTest {

	@ParameterizedTest(name = "{index}: {0}")
	@MethodSource("data")
	public void testAddTable(Supplier<TestDatabaseDetails> theTestDatabaseDetails) throws SQLException {
		before(theTestDatabaseDetails);

		MyMigrationTasks migrator = new MyMigrationTasks();
		getMigrator().addTasks(migrator.getTaskList(VersionEnum.V3_3_0, VersionEnum.V3_6_0));
		getMigrator().migrate();

		assertThat(JdbcUtils.getTableNames(getConnectionProperties())).containsExactlyInAnyOrder("FOO_TABLE", "TGT_TABLE");
		Set<String> indexes = JdbcUtils.getIndexNames(getConnectionProperties(), "FOO_TABLE")
			.stream()
			.filter(s -> !s.startsWith("FK_REF_INDEX_"))
			.filter(s -> !s.startsWith("PRIMARY_KEY_"))
			.collect(Collectors.toSet());

		// Derby auto-creates constraints with a system name for unique indexes
		if (getDriverType().equals(DriverTypeEnum.DERBY_EMBEDDED)) {
			indexes.removeIf(t -> t.startsWith("SQL"));
		}

		assertThat(indexes).as(indexes.toString()).containsExactlyInAnyOrder("IDX_BONJOUR");
	}

	@Test
	public void testLowercaseColumnsNoOverridesDefaultSorting() {
		final String tableName = "table_3_columns";
		final String columnName1 = "a_column";
		final String columnName3 = "z_column";
		final String columnNameId = "id";
		final DriverTypeEnum driverType = DriverTypeEnum.MSSQL_2012;
		final ColumnTypeEnum columnType = ColumnTypeEnum.STRING;

		final AddTableByColumnTask addTableByColumnTask = new AddTableByColumnTask();
		addTableByColumnTask.setTableName(tableName);
		addTableByColumnTask.setDriverType(driverType);
		addTableByColumnTask.setPkColumns(Collections.singletonList(columnNameId));

		addTableByColumnTask.addAddColumnTask(buildAddColumnTask(driverType, columnType, tableName, columnName3, true, 10, Collections.emptySet()));
		addTableByColumnTask.addAddColumnTask(buildAddColumnTask(driverType, columnType, tableName, columnNameId, false, 25, Collections.emptySet()));
		addTableByColumnTask.addAddColumnTask(buildAddColumnTask(driverType, columnType, tableName, columnName1, true, 20, Collections.emptySet()));

		final String actualCreateTableSql = addTableByColumnTask.generateSQLCreateScript();
		assertEquals(actualCreateTableSql, "CREATE TABLE table_3_columns ( z_column varchar(10), id varchar(25)  not null, a_column varchar(20),  PRIMARY KEY (id) )");;
	}

	@Test
	public void testLowercaseColumnsNvarcharOverrideDefaultSorting() {
		final String tableName = "table_3_columns";
		final String columnName1 = "a_column";
		final String columnName3 = "z_column";
		final String columnNameId = "id";
		final DriverTypeEnum driverType = DriverTypeEnum.MSSQL_2012;
		final ColumnTypeEnum columnType = ColumnTypeEnum.STRING;
		final ColumnDriverMappingOverride override = new ColumnDriverMappingOverride(columnType, driverType, "nvarchar(?)");

		final AddTableByColumnTask addTableByColumnTask = new AddTableByColumnTask();
		addTableByColumnTask.setTableName(tableName);
		addTableByColumnTask.setDriverType(driverType);
		addTableByColumnTask.setPkColumns(Collections.singletonList(columnNameId));

		addTableByColumnTask.addAddColumnTask(buildAddColumnTask(driverType, columnType, tableName, columnName3, true, 10, Collections.singleton(override)));
		addTableByColumnTask.addAddColumnTask(buildAddColumnTask(driverType, columnType, tableName, columnNameId, false, 25, Collections.singleton(override)));
		addTableByColumnTask.addAddColumnTask(buildAddColumnTask(driverType, columnType, tableName, columnName1, true, 20, Collections.singleton(override)));

		final String actualCreateTableSql = addTableByColumnTask.generateSQLCreateScript();
		assertEquals(actualCreateTableSql, "CREATE TABLE table_3_columns ( z_column nvarchar(10), id nvarchar(25)  not null, a_column nvarchar(20),  PRIMARY KEY (id) )");;
	}

	@Test
	public void testLowercaseColumnsNoOverridesCustomSorting() {
		final String tableName = "table_4_columns";
		final String columnName1 = "a_column";
		final String columnName2 = "b_column";
		final String columnName3 = "z_column";
		final String columnNameId = "id";
		final DriverTypeEnum driverType = DriverTypeEnum.MSSQL_2012;
		final ColumnTypeEnum columnType = ColumnTypeEnum.STRING;
		final ColumnDriverMappingOverride override = new ColumnDriverMappingOverride(columnType, driverType, "nvarchar(?)");
		final Comparator<AddColumnTask> comparator = (theTask1, theTask2) -> {
			if (columnNameId.equals(theTask1.getColumnName())) {
				return -1;
			}

			return theTask1.getColumnName().compareTo(theTask2.getColumnName());
		};

		final AddTableByColumnTask addTableByColumnTask = new AddTableByColumnTask("1", "1", comparator);
		addTableByColumnTask.setTableName(tableName);
		addTableByColumnTask.setDriverType(driverType);
		addTableByColumnTask.setPkColumns(Collections.singletonList(columnNameId));

		addTableByColumnTask.addAddColumnTask(buildAddColumnTask(driverType, columnType, tableName, columnName3, true, 10, Collections.singleton(override)));
		addTableByColumnTask.addAddColumnTask(buildAddColumnTask(driverType, columnType, tableName, columnName2, false, 15, Collections.singleton(override)));
		addTableByColumnTask.addAddColumnTask(buildAddColumnTask(driverType, columnType, tableName, columnName1, true, 20, Collections.singleton(override)));
		addTableByColumnTask.addAddColumnTask(buildAddColumnTask(driverType, columnType, tableName, columnNameId, false, 25, Collections.singleton(override)));

		final String actualCreateTableSql = addTableByColumnTask.generateSQLCreateScript();
		assertEquals(actualCreateTableSql, "CREATE TABLE table_4_columns ( id nvarchar(25)  not null, a_column nvarchar(20), b_column nvarchar(15)  not null, z_column nvarchar(10),  PRIMARY KEY (id) )");;
	}

	private static AddColumnTask buildAddColumnTask(DriverTypeEnum theDriverTypeEnum, ColumnTypeEnum theColumnTypeEnum, String theTableName, String theColumnName, boolean theNullable, int theColumnLength, Set<ColumnDriverMappingOverride> theColumnDriverMappingOverrides) {
		final AddColumnTask task = AddColumnTask.lowerCase(theColumnDriverMappingOverrides);

		task.setTableName(theTableName);
		task.setColumnName(theColumnName);
		task.setColumnType(theColumnTypeEnum);
		task.setDriverType(theDriverTypeEnum);
		task.setNullable(theNullable);
		task.setColumnLength(theColumnLength);

		return task;
	}

	private static class MyMigrationTasks extends BaseMigrationTasks<VersionEnum> {
		public MyMigrationTasks() {
			Builder v = forVersion(VersionEnum.V3_5_0);

			Builder.BuilderWithTableName targetTable = v.addTableByColumns("1", "TGT_TABLE", "PID");
			targetTable.addColumn("2", "PID").nonNullable().type(ColumnTypeEnum.LONG);
			targetTable.addColumn("3", "PID2").nonNullable().type(ColumnTypeEnum.LONG);

			Builder.BuilderAddTableByColumns fooTable = v.addTableByColumns("4", "FOO_TABLE", "PID");
			fooTable.addColumn("PID").nonNullable().type(ColumnTypeEnum.LONG);
			fooTable.addColumn("HELLO").nullable().type(ColumnTypeEnum.STRING, 200);
			fooTable.addColumn("GOODBYE").nullable().type(ColumnTypeEnum.STRING, 200);
			fooTable.addColumn("COL_REF").nullable().type(ColumnTypeEnum.LONG);
			fooTable.addIndex("5", "IDX_HELLO").unique(true).withColumns("HELLO");
			fooTable.addIndex("6", "IDX_GOODBYE").unique(true).withColumnsStub("GOODBYE");
			fooTable.dropIndexStub("7", "IDX_HELLO");
			fooTable.addForeignKey("8", "FK_REF").toColumn("COL_REF").references("TGT_TABLE", "PID");
			fooTable.addForeignKey("9", "FK_REF_INVALID").toColumn("COL_REF_INVALID").references("TGT_TABLE", "PID2").failureAllowed();

			Builder.BuilderWithTableName renameIndexTable = v.onTable("FOO_TABLE");
			renameIndexTable.renameIndex("10", "IDX_HELLO", "IDX_BONJOUR");
		}
	}
}
