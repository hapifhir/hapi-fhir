package ca.uhn.fhir.jpa.embedded;

import ca.uhn.fhir.jpa.migrate.DriverTypeEnum;
import ca.uhn.fhir.jpa.migrate.JdbcUtils;
import ca.uhn.fhir.test.utilities.docker.RequiresDocker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;

// Created by claude-opus-5

/**
 * Verifies that JDBC metadata introspection used by the migrator behaves consistently
 * across every embedded database we can start. The unit tests in hapi-fhir-sql-migrate
 * only cover H2 and Derby, so driver-specific metadata differences are invisible there.
 */
@RequiresDocker
public class HapiEmbeddedDbSupportCompatibilityTest {

	private static final Logger ourLog = LoggerFactory.getLogger(HapiEmbeddedDbSupportCompatibilityTest.class);

	@RegisterExtension
	public static HapiEmbeddedDatabasesExtension myEmbeddedServersExtension = new HapiEmbeddedDatabasesExtension();

	private JpaEmbeddedDatabase myCurrentDatabase;

	public static Stream<JpaEmbeddedDatabase> getEmbeddedDatabases() {
		return myEmbeddedServersExtension.getAllEmbeddedDatabases().stream();
	}

	@AfterEach
	public void afterEach() {
		if (myCurrentDatabase != null) {
			myCurrentDatabase.clearDatabase();
			myCurrentDatabase = null;
		}
	}

	/**
	 * This test is to check a contract that dbs we support should fulfill.
	 * Namely, that DELETE_RULE is *always* populated and matches what
	 * is set on the fk constraints.
	 * See {@link JdbcUtils#getForeignKeysAndRuleset(DriverTypeEnum.ConnectionProperties, String, String)}
	 */
	@ParameterizedTest
	@MethodSource("getEmbeddedDatabases")
	public void getForeignKeysAndRuleset_singleColumnForeignKeys_reportsDeleteCascadeAccurately(
			JpaEmbeddedDatabase theDatabase) throws SQLException {
		// setup
		myCurrentDatabase = theDatabase;
		DriverTypeEnum driverType = theDatabase.getDriverType();
		ourLog.info("Checking foreign key delete rules for {}", driverType);

		theDatabase.executeSqlAsBatch(List.of(
				"CREATE TABLE CUSTOMERS (ID int not null, NAME varchar(255), primary key (ID))",
				"CREATE TABLE ORDERS_CASCADE (ID int not null, CUSTOMERID int)",
				"CREATE TABLE ORDERS_PLAIN (ID int not null, CUSTOMERID int)",
				"ALTER TABLE ORDERS_CASCADE ADD CONSTRAINT FK_ORDERS_CASCADE FOREIGN KEY (CUSTOMERID)"
						+ " REFERENCES CUSTOMERS (ID) ON DELETE CASCADE",
				"ALTER TABLE ORDERS_PLAIN ADD CONSTRAINT FK_ORDERS_PLAIN FOREIGN KEY (CUSTOMERID)"
						+ " REFERENCES CUSTOMERS (ID)"));

		// execute
		Map<String, Boolean> cascading =
				JdbcUtils.getForeignKeysAndRuleset(getConnectionProperties(theDatabase), "CUSTOMERS", "ORDERS_CASCADE");
		Map<String, Boolean> nonCascading =
				JdbcUtils.getForeignKeysAndRuleset(getConnectionProperties(theDatabase), "CUSTOMERS", "ORDERS_PLAIN");

		// validate
		assertThat(cascading)
				.as("ON DELETE CASCADE must be reported as cascading on %s", driverType)
				.containsOnly(entry("FK_ORDERS_CASCADE", true));
		assertThat(nonCascading)
				.as("a foreign key with no delete rule must not be reported as cascading on %s", driverType)
				.containsOnly(entry("FK_ORDERS_PLAIN", false));
	}

	@ParameterizedTest
	@MethodSource("getEmbeddedDatabases")
	public void getForeignKeysAndRuleset_multiColumnForeignKey_reportsSingleEntry(JpaEmbeddedDatabase theDatabase)
			throws SQLException {
		// setup
		myCurrentDatabase = theDatabase;
		DriverTypeEnum driverType = theDatabase.getDriverType();

		theDatabase.executeSqlAsBatch(List.of(
				"CREATE TABLE COMPOSITE_PARENT (ID1 int not null, ID2 int not null, primary key (ID1, ID2))",
				"CREATE TABLE COMPOSITE_CHILD (PID1 int, PID2 int)",
				"ALTER TABLE COMPOSITE_CHILD ADD CONSTRAINT FK_COMPOSITE FOREIGN KEY (PID1, PID2)"
						+ " REFERENCES COMPOSITE_PARENT (ID1, ID2) ON DELETE CASCADE"));

		// execute
		// a multi-column foreign key yields one metadata row per column, all carrying the same delete rule
		Map<String, Boolean> foreignKeys = JdbcUtils.getForeignKeysAndRuleset(
				getConnectionProperties(theDatabase), "COMPOSITE_PARENT", "COMPOSITE_CHILD");

		// validate
		assertThat(foreignKeys)
				.as("a multi-column foreign key must collapse to one cascading entry on %s", driverType)
				.containsOnly(entry("FK_COMPOSITE", true));
	}

	/**
	 * The returned properties are deliberately not closed - the underlying DataSource is owned by
	 * the embedded database and shared with every other test running against that container.
	 */
	private DriverTypeEnum.ConnectionProperties getConnectionProperties(JpaEmbeddedDatabase theDatabase) {
		return theDatabase.getDriverType().newConnectionProperties(theDatabase.getDataSource());
	}
}
