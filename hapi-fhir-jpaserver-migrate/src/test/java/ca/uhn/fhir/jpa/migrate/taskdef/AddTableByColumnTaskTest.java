package ca.uhn.fhir.jpa.migrate.taskdef;

import ca.uhn.fhir.jpa.migrate.JdbcUtils;
import ca.uhn.fhir.jpa.migrate.tasks.api.BaseMigrationTasks;
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

		assertThat(JdbcUtils.getTableNames(getConnectionProperties()), containsInAnyOrder("FOO_TABLE"));


	}


	private static class MyMigrationTasks extends BaseMigrationTasks<VersionEnum> {

		public MyMigrationTasks() {
			Builder v = forVersion(VersionEnum.V3_5_0);
			Builder.BuilderAddTableByColumns fooTable = v.addTableByColumns("FOO_TABLE", "PID");
			fooTable.addColumn("PID").nonNullable().type(BaseTableColumnTypeTask.ColumnTypeEnum.LONG);
			fooTable.addColumn("HELLO").nullable().type(BaseTableColumnTypeTask.ColumnTypeEnum.STRING, 200);
		}


	}
}
