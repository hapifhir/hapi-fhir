package ca.uhn.fhir.jpa.migrate.taskdef;

import ca.uhn.fhir.jpa.migrate.JdbcUtils;
import ca.uhn.fhir.jpa.migrate.tasks.api.BaseMigrationTasks;
import ca.uhn.fhir.jpa.migrate.tasks.api.Builder;
import ca.uhn.fhir.util.VersionEnum;
import org.junit.Test;

import java.sql.SQLException;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertThat;

public class AddTableByColumnTaskTest extends BaseTest {

	@Test
	public void testAddTable() throws SQLException {

		MyMigrationTasks migrator = new MyMigrationTasks();
		getMigrator().addTasks(migrator.getTasks(VersionEnum.V3_3_0, VersionEnum.V3_6_0));
		getMigrator().migrate();

		assertThat(JdbcUtils.getTableNames(getConnectionProperties()), containsInAnyOrder("FOO_TABLE", "TGT_TABLE"));
	}


	private static class MyMigrationTasks extends BaseMigrationTasks<VersionEnum> {
		public MyMigrationTasks() {
			Builder v = forVersion(VersionEnum.V3_5_0);

			Builder.BuilderWithTableName targetTable = v.addTableByColumns("1", "TGT_TABLE", "PID");
			targetTable.addColumn("2", "PID").nonNullable().type(BaseTableColumnTypeTask.ColumnTypeEnum.LONG);

			Builder.BuilderAddTableByColumns fooTable = v.addTableByColumns("3", "FOO_TABLE", "PID");
			fooTable.addColumn("PID").nonNullable().type(BaseTableColumnTypeTask.ColumnTypeEnum.LONG);
			fooTable.addColumn("HELLO").nullable().type(BaseTableColumnTypeTask.ColumnTypeEnum.STRING, 200);
			fooTable.addColumn("COL_REF").nullable().type(BaseTableColumnTypeTask.ColumnTypeEnum.LONG);
			fooTable.addIndex("4", "IDX_HELLO").unique(true).withColumns("HELLO");
			fooTable.addForeignKey("5", "FK_REF").toColumn("COL_REF").references("TGT_TABLE", "PID");

		}
	}
}
