package ca.uhn.fhir.jpa.migrate;

/*-
 * #%L
 * HAPI FHIR Server - SQL Migration
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import org.flywaydb.core.api.configuration.FluentConfiguration;
import org.flywaydb.core.internal.database.base.Database;
import org.flywaydb.core.internal.database.base.Table;
import org.flywaydb.core.internal.database.cockroachdb.CockroachDBDatabase;
import org.flywaydb.core.internal.database.derby.DerbyDatabase;
import org.flywaydb.core.internal.database.h2.H2Database;
import org.flywaydb.core.internal.database.oracle.OracleDatabase;
import org.flywaydb.core.internal.database.postgresql.PostgreSQLDatabase;
import org.flywaydb.core.internal.jdbc.JdbcConnectionFactory;
import org.flywaydb.database.mysql.MySQLDatabase;
import org.flywaydb.database.mysql.mariadb.MariaDBDatabase;
import org.flywaydb.database.sqlserver.SQLServerDatabase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class HapiMigrationLock implements AutoCloseable {
	private static final Logger ourLog = LoggerFactory.getLogger(HapiMigrationLock.class);
	private final DriverTypeEnum myDriverType;
	private final Table myLockTableConnection;
	private final String myMigrationTablename;

	public HapiMigrationLock(DataSource theDataSource, DriverTypeEnum theDriverType, String myMigrationTablename) {
		this.myDriverType = theDriverType;
		this.myMigrationTablename = myMigrationTablename;

		myLockTableConnection = openLockTableConnection(theDataSource);

		ourLog.debug("Locking Migration Table");
		myLockTableConnection.lock();
		ourLog.debug("Locked Migration Table");
	}

	@Override
	public void close() {
		if (myLockTableConnection == null) {
			return;
		}

		myLockTableConnection.unlock();
		ourLog.debug("Unlocked Migration Table");

		// Close the connection we opened in openLockTableConnection()
		myLockTableConnection.getDatabase().close();
	}

	private Table openLockTableConnection(DataSource theDataSource) {
		try {
			FluentConfiguration configuration = new FluentConfiguration().dataSource(theDataSource);
			JdbcConnectionFactory connectionFactory = new JdbcConnectionFactory(theDataSource, configuration, null);
			String schemaName;
			try (Connection connection = theDataSource.getConnection()) {
				schemaName = connection.getSchema();
			}

			Database database;
			switch (myDriverType) {
				case H2_EMBEDDED:
					database = new H2Database(configuration, connectionFactory, null);
					break;
				case DERBY_EMBEDDED:
					database = new DerbyDatabase(configuration, connectionFactory, null);
					break;
				case ORACLE_12C:
					database = new OracleDatabase(configuration, connectionFactory, null);
					break;
				case POSTGRES_9_4:
					database = new PostgreSQLDatabase(configuration, connectionFactory, null);
					break;
				case COCKROACHDB_21_1:
					database = new CockroachDBDatabase(configuration, connectionFactory, null);
					break;
				case MARIADB_10_1:
					database = new MariaDBDatabase(configuration, connectionFactory, null);
					break;
				case MYSQL_5_7:
					database = new MySQLDatabase(configuration, connectionFactory, null);
					break;
				case MSSQL_2012:
					database = new SQLServerDatabase(configuration, connectionFactory, null);
					break;
				default:
					throw new UnsupportedOperationException("Driver type not supported: " + myDriverType);
			}
			// The Flyway table lock mechanism requires auto-commit to be disabled on the connection
			database.getMainConnection().getJdbcConnection().setAutoCommit(false);
			return database.getMainConnection().getSchema(schemaName).getTable(myMigrationTablename);
		} catch (SQLException e) {
			throw new RuntimeException("Failed to open connection to database migration table " + myMigrationTablename, e);
		}
	}

}
