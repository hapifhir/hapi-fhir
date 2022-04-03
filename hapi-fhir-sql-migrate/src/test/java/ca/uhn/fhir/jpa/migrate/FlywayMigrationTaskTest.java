package ca.uhn.fhir.jpa.migrate;

import ca.uhn.fhir.jpa.migrate.taskdef.BaseTask;
import ca.uhn.fhir.jpa.migrate.taskdef.InitializeSchemaTask;
import ca.uhn.fhir.jpa.migrate.tasks.api.ISchemaInitializationProvider;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.flywaydb.core.api.migration.Context;
import org.flywaydb.core.internal.database.DatabaseTypeRegister;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FlywayMigrationTaskTest {
	TestTask myTestTask = new TestTask();
	@Mock
	private FlywayMigrator myFlywayMigrator;
	@Mock
	private Context myContext;

	@Test
	public void schemaInitializedStubsFollowingMigration() {
		when(myFlywayMigrator.isSchemaWasInitialized()).thenReturn(true);
		FlywayMigrationTask task = new FlywayMigrationTask(myTestTask, myFlywayMigrator);
		task.migrate(myContext);
		assertTrue(myTestTask.isDoNothing());
	}

	@Test
	public void schemaNotInitializedStubsFollowingMigration() {
		when(myFlywayMigrator.isSchemaWasInitialized()).thenReturn(false);
		FlywayMigrationTask task = new FlywayMigrationTask(myTestTask, myFlywayMigrator);
		task.migrate(myContext);
		assertFalse(myTestTask.isDoNothing());
	}

	@Test
	public void schemaInitializedStubsFollowingMigrationExceptInitSchemaTask() {
		when(myFlywayMigrator.isSchemaWasInitialized()).thenReturn(true);
		InitializeSchemaTask initSchemaTask = new TestInitializeSchemaTask(false);
		FlywayMigrationTask task = new FlywayMigrationTask(initSchemaTask, myFlywayMigrator);
		task.migrate(myContext);
		assertFalse(myTestTask.isDoNothing());
	}

	@Test
	public void schemaInitializedSetsInitializedFlag() {
		InitializeSchemaTask initSchemaTask = new TestInitializeSchemaTask(true);
		FlywayMigrationTask task = new FlywayMigrationTask(initSchemaTask, myFlywayMigrator);
		task.migrate(myContext);
		verify(myFlywayMigrator, times(1)).setSchemaWasInitialized(true);
	}


	@Test
	public void nonInitSchemaInitializedSetsInitializedFlag() {
		InitializeSchemaTask initSchemaTask = new TestInitializeSchemaTask(false);
		FlywayMigrationTask task = new FlywayMigrationTask(initSchemaTask, myFlywayMigrator);
		task.migrate(myContext);
		verify(myFlywayMigrator, never()).setSchemaWasInitialized(true);
	}

	// Can't use @Mock since BaseTask.equals is final
	private class TestTask extends BaseTask {
		protected TestTask() {
			super("1", "1");
		}

		@Override
		public void validate() {
			// do nothing
		}

		@Override
		protected void doExecute() throws SQLException {
			// do nothing
		}

		@Override
		protected void generateHashCode(HashCodeBuilder theBuilder) {
			// do nothing
		}

		@Override
		protected void generateEquals(EqualsBuilder theBuilder, BaseTask theOtherObject) {
			// do nothing
		}
	}

	private class TestInitializeSchemaTask extends InitializeSchemaTask {
		private final boolean myInitializedSchema;

		public TestInitializeSchemaTask(boolean theInitializedSchema) {
			super("1", "1", mock(ISchemaInitializationProvider.class));
			myInitializedSchema = theInitializedSchema;
		}

		@Override
		public void execute() throws SQLException {
			// nothing
		}

		@Override
		public boolean initializedSchema() {
			return myInitializedSchema;
		}
	}
}
