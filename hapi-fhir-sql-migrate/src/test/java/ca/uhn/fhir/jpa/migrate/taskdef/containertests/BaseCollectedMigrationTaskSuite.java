package ca.uhn.fhir.jpa.migrate.taskdef.containertests;

import ca.uhn.fhir.jpa.migrate.DriverTypeEnum;
import ca.uhn.fhir.jpa.migrate.taskdef.AddIndexTaskITTestSuite;
import ca.uhn.fhir.jpa.migrate.taskdef.DropIndexTaskITTestSuite;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import jakarta.annotation.Nonnull;

import static ca.uhn.fhir.jpa.migrate.taskdef.containertests.BaseMigrationTaskTestSuite.Support;

/**
 * Collects all our task suites in a single class so we can run them on each engine.
 */
public abstract class BaseCollectedMigrationTaskSuite {

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
	class AddIndexTaskTests implements AddIndexTaskITTestSuite {
		@Override
		public Support getSupport() {
			return BaseCollectedMigrationTaskSuite.this.getSupport();
		}
	}

	@Nested
	class DropIndexTaskTests implements DropIndexTaskITTestSuite {
		@Override
		public Support getSupport() {
			return BaseCollectedMigrationTaskSuite.this.getSupport();
		}
	}

	@Test
	void testNothing() {
		// an empty test to quiet sonar
		Assertions.assertTrue(true);
	}

}
