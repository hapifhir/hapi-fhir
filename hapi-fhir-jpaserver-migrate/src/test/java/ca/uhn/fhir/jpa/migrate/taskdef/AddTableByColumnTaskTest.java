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
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.MatcherAssert.assertThat;

public class AddTableByColumnTaskTest extends BaseTest {

	@ParameterizedTest(name = "{index}: {0}")
	@MethodSource("data")
	public void testAddTable(Supplier<TestDatabaseDetails> theTestDatabaseDetails) throws SQLException {
		before(theTestDatabaseDetails);

		MyMigrationTasks migrator = new MyMigrationTasks();
		getMigrator().addTasks(migrator.getTasks(VersionEnum.V3_3_0, VersionEnum.V3_6_0));
		getMigrator().migrate();

		assertThat(JdbcUtils.getTableNames(getConnectionProperties()), containsInAnyOrder("FOO_TABLE", "TGT_TABLE"));
		Set<String> indexes = JdbcUtils.getIndexNames(getConnectionProperties(), "FOO_TABLE")
			.stream()
			.filter(s -> !s.startsWith("FK_REF_INDEX_"))
			.filter(s -> !s.startsWith("PRIMARY_KEY_"))
			.collect(Collectors.toSet());

		// Derby auto-creates constraints with a system name for unique indexes
		if (getDriverType().equals(DriverTypeEnum.DERBY_EMBEDDED)) {
			indexes.removeIf(t->t.startsWith("SQL"));
		}

		assertThat(indexes.toString(), indexes, containsInAnyOrder("IDX_BONJOUR"));
	}

	private static class MyMigrationTasks extends BaseMigrationTasks<VersionEnum> {
		public MyMigrationTasks() {
			Builder v = forVersion(VersionEnum.V3_5_0);

			Builder.BuilderWithTableName targetTable = v.addTableByColumns("1", "TGT_TABLE", "PID");
			targetTable.addColumn("2", "PID").nonNullable().type(ColumnTypeEnum.LONG);

			Builder.BuilderAddTableByColumns fooTable = v.addTableByColumns("3", "FOO_TABLE", "PID");
			fooTable.addColumn("PID").nonNullable().type(ColumnTypeEnum.LONG);
			fooTable.addColumn("HELLO").nullable().type(ColumnTypeEnum.STRING, 200);
			fooTable.addColumn("GOODBYE").nullable().type(ColumnTypeEnum.STRING, 200);
			fooTable.addColumn("COL_REF").nullable().type(ColumnTypeEnum.LONG);
			fooTable.addIndex("4", "IDX_HELLO").unique(true).withColumns("HELLO");
			fooTable.addIndex("5", "IDX_GOODBYE").unique(true).withColumnsStub("GOODBYE");
			fooTable.dropIndexStub("6", "IDX_HELLO");
			fooTable.addForeignKey("7", "FK_REF").toColumn("COL_REF").references("TGT_TABLE", "PID");

			Builder.BuilderWithTableName renameIndexTable = v.onTable("FOO_TABLE");
			renameIndexTable.renameIndex("8", "IDX_HELLO", "IDX_BONJOUR");
		}
	}
}
