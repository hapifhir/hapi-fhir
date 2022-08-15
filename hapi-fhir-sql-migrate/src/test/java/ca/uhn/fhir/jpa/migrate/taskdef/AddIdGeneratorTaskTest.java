package ca.uhn.fhir.jpa.migrate.taskdef;

import ca.uhn.fhir.jpa.migrate.JdbcUtils;
import ca.uhn.fhir.jpa.migrate.tasks.api.BaseMigrationTasks;
import ca.uhn.fhir.jpa.migrate.tasks.api.Builder;
import ca.uhn.fhir.util.VersionEnum;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.sql.SQLException;
import java.util.function.Supplier;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.empty;

public class AddIdGeneratorTaskTest extends BaseTest {


	@ParameterizedTest(name = "{index}: {0}")
	@MethodSource("data")
	public void testAddIdGenerator(Supplier<TestDatabaseDetails> theTestDatabaseDetails) throws SQLException {
		before(theTestDatabaseDetails);

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
