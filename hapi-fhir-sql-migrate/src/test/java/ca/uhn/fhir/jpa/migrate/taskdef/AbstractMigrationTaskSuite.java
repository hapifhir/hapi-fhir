package ca.uhn.fhir.jpa.migrate.taskdef;

import ca.uhn.fhir.jpa.migrate.DriverTypeEnum;
import ca.uhn.fhir.jpa.migrate.taskdef.containertests.BaseMigrationTaskTestSuite;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;

import javax.annotation.Nonnull;

import static ca.uhn.fhir.jpa.migrate.taskdef.containertests.BaseMigrationTaskTestSuite.Support;

/**
 * Collects all our task suites in a single class so we can run them on each engine.
 */
public abstract class AbstractMigrationTaskSuite {

	/**
	 * Per-test supplier for db-access, migration task list, etc.
	 */
	BaseMigrationTaskTestSuite.Support mySupport;

	@BeforeEach
	void setUp() {
		DriverTypeEnum.ConnectionProperties connectionProperties = getConnectionProperties();
		mySupport = Support.supportFrom(connectionProperties);
	}

	/**
	 * Handle on concrete class container connection info.
	 */
	@Nonnull
	protected abstract DriverTypeEnum.ConnectionProperties getConnectionProperties();



	final public BaseMigrationTaskTestSuite.Support getSupport() {
		return mySupport;
	}


	@Nested
	class AddIndexTaskTests implements AddIndexTaskTestSuite {
		@Override
		public Support getSupport() {
			return AbstractMigrationTaskSuite.this.getSupport();
		}
	}

}
