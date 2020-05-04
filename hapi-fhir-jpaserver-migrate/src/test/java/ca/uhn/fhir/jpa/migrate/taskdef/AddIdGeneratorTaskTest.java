package ca.uhn.fhir.jpa.migrate.taskdef;

import ca.uhn.fhir.jpa.migrate.JdbcUtils;
import ca.uhn.fhir.jpa.migrate.tasks.api.BaseMigrationTasks;
import ca.uhn.fhir.jpa.migrate.tasks.api.Builder;
import ca.uhn.fhir.util.VersionEnum;
import org.junit.Test;

import java.sql.SQLException;
import java.util.function.Supplier;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.empty;
import static org.junit.Assert.assertThat;

public class AddIdGeneratorTaskTest extends BaseTest {


	public AddIdGeneratorTaskTest(Supplier<TestDatabaseDetails> theTestDatabaseDetails) {
		super(theTestDatabaseDetails);
	}

	@Test
	public void testAddIdGenerator() throws SQLException {
		assertThat(JdbcUtils.getSequenceNames(getConnectionProperties()), empty());

		MyMigrationTasks migrationTasks = new MyMigrationTasks("123456.7");
		getMigrator().addTasks(migrationTasks.getTasks(VersionEnum.V3_3_0, VersionEnum.V3_6_0));
		getMigrator().migrate();

		assertThat(JdbcUtils.getSequenceNames(getConnectionProperties()), containsInAnyOrder("SEQ_FOO"));

		// Second time, should produce no action
		migrationTasks = new MyMigrationTasks("123456.8");
		getMigrator().addTasks(migrationTasks.getTasks(VersionEnum.V3_3_0, VersionEnum.V3_6_0));
		getMigrator().migrate();

		assertThat(JdbcUtils.getSequenceNames(getConnectionProperties()), containsInAnyOrder("SEQ_FOO"));

	}



	private static class MyMigrationTasks extends BaseMigrationTasks<VersionEnum> {

		public MyMigrationTasks(String theVersion) {
			Builder v = forVersion(VersionEnum.V3_5_0);
			v.addIdGenerator(theVersion, "SEQ_FOO");
		}


	}

}
