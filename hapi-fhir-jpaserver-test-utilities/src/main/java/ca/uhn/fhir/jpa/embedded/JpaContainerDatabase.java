package ca.uhn.fhir.jpa.embedded;

import org.testcontainers.containers.JdbcDatabaseContainer;


/**
 * Wrapper to support JPA tests.
 */
public abstract class JpaContainerDatabase extends JpaEmbeddedDatabase {
	protected final JdbcDatabaseContainer<?> myContainer;

	protected JpaContainerDatabase(JdbcDatabaseContainer<?> theContainer) {
		myContainer = theContainer;
		myContainer.start();
	}

	@Override
	public void stop() {
		myContainer.stop();
	}
}
