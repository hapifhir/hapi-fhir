package ca.uhn.fhir.jpa.migrate;

import org.flywaydb.core.api.configuration.FluentConfiguration;
import org.flywaydb.core.internal.database.base.Table;
import org.flywaydb.core.internal.database.derby.DerbyConnection;
import org.flywaydb.core.internal.database.derby.DerbyDatabase;
import org.flywaydb.core.internal.database.derby.DerbySchema;
import org.flywaydb.core.internal.database.derby.DerbyTable;
import org.flywaydb.core.internal.database.h2.H2Connection;
import org.flywaydb.core.internal.database.h2.H2Database;
import org.flywaydb.core.internal.database.h2.H2Schema;
import org.flywaydb.core.internal.database.h2.H2Table;
import org.flywaydb.core.internal.jdbc.JdbcConnectionFactory;
import org.flywaydb.core.internal.jdbc.JdbcTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.SQLException;

public class HapiMigrationLockSvc {
	private static final Logger ourLog = LoggerFactory.getLogger(HapiMigrationLockSvc.class);
	private final DataSource theDataSource;
	private final DriverTypeEnum theDriverType;
	private final Table myLockTable;
	private final String myMigrationTablename;

	public HapiMigrationLockSvc(DataSource theDataSource, DriverTypeEnum theDriverType, String myMigrationTablename) {
		this.theDataSource = theDataSource;
		this.theDriverType = theDriverType;
		this.myMigrationTablename = myMigrationTablename;
		myLockTable = buildTable(theDataSource, theDriverType);
	}

	private Table buildTable(DataSource theDataSource, DriverTypeEnum theDriverType) {
		try {
			JdbcTemplate jdbcTemplate = new JdbcTemplate(theDataSource.getConnection());
			FluentConfiguration configuration = new FluentConfiguration().dataSource(theDataSource);
			JdbcConnectionFactory connectionFactory = new JdbcConnectionFactory(theDataSource, configuration, null);

			String schemaName = theDataSource.getConnection().getSchema();
			switch (theDriverType) {
				case H2_EMBEDDED: {
					H2Database database = new H2Database(configuration, connectionFactory, null);
					H2Connection connection = database.getMainConnection();
					H2Schema schema = (H2Schema) connection.getSchema(schemaName);
					return new H2Table(jdbcTemplate, database, schema, myMigrationTablename);
				}
				case DERBY_EMBEDDED: {
					DerbyDatabase database = new DerbyDatabase(configuration, connectionFactory, null);
					DerbyConnection connection = database.getMainConnection();
					DerbySchema schema = (DerbySchema) connection.getSchema(schemaName);
					return new DerbyTable(jdbcTemplate, database, schema, myMigrationTablename);
				}
				default:
					throw new UnsupportedOperationException("Driver type not supported: " + theDriverType);
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public void lock() {
		ourLog.info("Locking Migration Table...");
		myLockTable.lock();
		ourLog.info("Migration Table Locked");
	}

	public void unlock() {
		myLockTable.unlock();
		ourLog.info("Migration Table Unlocked");
	}
}
