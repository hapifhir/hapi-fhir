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

public class DropIdGeneratorTaskTest extends BaseTest {


	public DropIdGeneratorTaskTest(Supplier<TestDatabaseDetails> theTestDatabaseDetails) {
		super(theTestDatabaseDetails);
	}

	@Test
	public void testAddIdGenerator() throws SQLException {
		executeSql("create sequence SEQ_FOO start with 1 increment by 50");
		assertThat(JdbcUtils.getSequenceNames(getConnectionProperties()), containsInAnyOrder("SEQ_FOO"));

		MyMigrationTasks migrator = new MyMigrationTasks();
		getMigrator().addTasks(migrator.getTasks(VersionEnum.V3_3_0, VersionEnum.V3_6_0));
		getMigrator().migrate();

		assertThat(JdbcUtils.getSequenceNames(getConnectionProperties()), empty());
	}



	private static class MyMigrationTasks extends BaseMigrationTasks<VersionEnum> {

		public MyMigrationTasks() {
			Builder v = forVersion(VersionEnum.V3_5_0);
			v.dropIdGenerator("1", "SEQ_FOO");
		}


	}

}
