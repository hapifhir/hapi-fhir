package ca.uhn.fhir.jpa.migrate.taskdef;

import ca.uhn.fhir.jpa.migrate.JdbcUtils;
import ca.uhn.fhir.jpa.migrate.tasks.api.BaseMigrationTasks;
import ca.uhn.fhir.util.VersionEnum;
import org.junit.Test;

import java.sql.SQLException;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.empty;
import static org.junit.Assert.assertThat;

public class AddIdGeneratorTaskTest extends BaseTest {


	@Test
	public void testAddIdGenerator() throws SQLException {
		assertThat(JdbcUtils.getSequenceNames(getConnectionProperties()), empty());

		MyMigrationTasks migrator = new MyMigrationTasks();
		getMigrator().addTasks(migrator.getTasks(VersionEnum.V3_3_0, VersionEnum.V3_6_0));
		getMigrator().migrate();

		assertThat(JdbcUtils.getSequenceNames(getConnectionProperties()), containsInAnyOrder("SEQ_FOO"));

		// Second time, should produce no action
		migrator = new MyMigrationTasks();
		getMigrator().addTasks(migrator.getTasks(VersionEnum.V3_3_0, VersionEnum.V3_6_0));
		getMigrator().migrate();

		assertThat(JdbcUtils.getSequenceNames(getConnectionProperties()), containsInAnyOrder("SEQ_FOO"));

	}



	private static class MyMigrationTasks extends BaseMigrationTasks<VersionEnum> {

		public MyMigrationTasks() {
			Builder v = forVersion(VersionEnum.V3_5_0);
			v.addIdGenerator("1", "SEQ_FOO");
		}


	}

}
