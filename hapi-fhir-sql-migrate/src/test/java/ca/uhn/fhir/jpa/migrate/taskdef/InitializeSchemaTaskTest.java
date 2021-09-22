package ca.uhn.fhir.jpa.migrate.taskdef;

import ca.uhn.fhir.jpa.migrate.DriverTypeEnum;
import ca.uhn.fhir.jpa.migrate.JdbcUtils;
import ca.uhn.fhir.jpa.migrate.tasks.api.ISchemaInitializationProvider;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;

public class InitializeSchemaTaskTest extends BaseTest {

	@ParameterizedTest(name = "{index}: {0}")
	@MethodSource("data")
	public void testInitializeTwice(Supplier<TestDatabaseDetails> theTestDatabaseDetails) throws SQLException {
		before(theTestDatabaseDetails);

		InitializeSchemaTask task = new InitializeSchemaTask("1", "1", new TestProvider());
		getMigrator().addTask(task);
		getMigrator().migrate();
		assertThat(JdbcUtils.getTableNames(getConnectionProperties()), containsInAnyOrder("SOMETABLE"));

		// Second migrate runs without issue
		getMigrator().removeAllTasksForUnitTest();
		InitializeSchemaTask identicalTask = new InitializeSchemaTask("1", "1", new TestProvider());
		getMigrator().addTask(identicalTask);
		getMigrator().migrate();
	}

	private class TestProvider implements ISchemaInitializationProvider {
		@Override
		public List<String> getSqlStatements(DriverTypeEnum theDriverType) {
			return Collections.singletonList("create table SOMETABLE (PID bigint not null, TEXTCOL varchar(255))");
		}

		@Override
		public String getSchemaExistsIndicatorTable() {
			return "DONT_MATCH_ME";
		}

		@Override
		public String getSchemaDescription() {
			return "TEST";
		}

		@Override
		public ISchemaInitializationProvider setSchemaDescription(String theSchemaDescription) {
			return this;
		}

		@Override
		public boolean canInitializeSchema() {
			return false;
		}

		@Override
		public boolean equals(Object theO) {
			if (this == theO) return true;

			if (theO == null || getClass() != theO.getClass()) return false;

			TestProvider that = (TestProvider) theO;

			return size() == that.size();
		}

		private int size() {
			return getSqlStatements(getDriverType()).size();
		}

		// This could be stricter, but we don't want this to be brittle.
		@Override
		public int hashCode() {
			return new HashCodeBuilder(17, 37)
				.append(size())
				.toHashCode();
		}
	}
}
