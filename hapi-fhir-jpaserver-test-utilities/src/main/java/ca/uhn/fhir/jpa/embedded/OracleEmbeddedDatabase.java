package ca.uhn.fhir.jpa.embedded;

import ca.uhn.fhir.jpa.migrate.DriverTypeEnum;
import org.testcontainers.containers.OracleContainer;

/**
 * For testing purposes.
 * <br/><br/>
 * Embedded database that uses a {@link DriverTypeEnum#ORACLE_12C} driver
 * and a dockerized Testcontainer.
 * @see <a href="https://www.testcontainers.org/modules/databases/oraclexe/">Oracle TestContainer</a>
 */
public class OracleEmbeddedDatabase extends JpaEmbeddedDatabase {

	private final OracleContainer myContainer;

	public OracleEmbeddedDatabase(){
		myContainer = new OracleContainer("gvenzl/oracle-xe:21-slim-faststart")
            .withPrivilegedMode(true);
        myContainer.start();
		super.initialize(DriverTypeEnum.ORACLE_12C, myContainer.getJdbcUrl(), myContainer.getUsername(), myContainer.getPassword());
	}

	@Override
	public void stop() {
		myContainer.stop();
	}

	@Override
	public void clearDatabase() {
		dropTables();
		dropSequences();
	}

	private void dropTables() {
		// TODO ND
	}

	private void dropSequences() {
		// TODO ND
	}
}
