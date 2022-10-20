package ca.uhn.fhir.jpa.migrate;

import org.flywaydb.core.internal.database.DatabaseType;
import org.flywaydb.core.internal.database.InsertRowLock;
import org.flywaydb.core.internal.database.base.Database;
import org.flywaydb.core.internal.database.cockroachdb.CockroachDBDatabaseType;
import org.flywaydb.core.internal.database.derby.DerbyDatabaseType;
import org.flywaydb.core.internal.database.h2.H2DatabaseType;
import org.flywaydb.core.internal.database.oracle.OracleDatabaseType;
import org.flywaydb.core.internal.database.postgresql.PostgreSQLDatabaseType;
import org.flywaydb.core.internal.jdbc.JdbcTemplate;
import org.flywaydb.database.mysql.MySQLDatabaseType;
import org.flywaydb.database.mysql.mariadb.MariaDBDatabaseType;
import org.flywaydb.database.sqlserver.SQLServerDatabaseType;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class HapiMigrationLock implements AutoCloseable {

	public static final int DEFAULT_LOCK_TIMEOUT_MINUTES = 10;
	private final InsertRowLock myInsertRowLock;
	private final DataSource myDataSource;
	private final DriverTypeEnum myDriverType;
	private final String myMigrationTableName;
	private Connection myConnection;

	public HapiMigrationLock(JdbcTemplate theJdbcTemplate) {
	}

	public HapiMigrationLock(DataSource theDataSource, DriverTypeEnum theDriverType, String theMigrationTableName) throws SQLException {
		myDataSource = theDataSource;
		myDriverType = theDriverType;
		myMigrationTableName = theMigrationTableName;

		myConnection = theDataSource.getConnection();
		JdbcTemplate jdbcTemplate = new JdbcTemplate(myConnection, toFlywayDatabaseType(theDriverType));
		Database database = newFlywayDatabase(theDriverType);

		myInsertRowLock = new InsertRowLock(jdbcTemplate, DEFAULT_LOCK_TIMEOUT_MINUTES);
		lock();
	}

	private static Database newFlywayDatabase(DriverTypeEnum theDriverType) {
	}

	private static DatabaseType toFlywayDatabaseType(DriverTypeEnum theDriverType) {
		switch (theDriverType) {
			case MSSQL_2012:
				return new SQLServerDatabaseType();
			case MYSQL_5_7:
				return new MySQLDatabaseType();
			case MARIADB_10_1:
				return new MariaDBDatabaseType();
			case POSTGRES_9_4:
				return new PostgreSQLDatabaseType();
			case ORACLE_12C:
				return new OracleDatabaseType();
			case COCKROACHDB_21_1:
				return new CockroachDBDatabaseType();
			case DERBY_EMBEDDED:
				return new DerbyDatabaseType();
			case H2_EMBEDDED:
				return new H2DatabaseType();
			default:
				throw new IllegalArgumentException("Unknown driver type: " + theDriverType);
		}
	}

	private void lock() {
		myInsertRowLock.doLock(getInsertStatementTemplate(), getUpdateLockStatement(), getDeleteExpiredLockStatement, getBooleanTrue());
	}


	@Override
	public void close() {
		myInsertRowLock.doUnlock(getUnlockTemplate());
		if (myConnection != null) {
			myConnection.close();
		}
	}
}
