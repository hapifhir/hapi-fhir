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

public class HapiMigrationLockSvc {
	private static final Logger ourLog = LoggerFactory.getLogger(HapiMigrationLockSvc.class);
	private final DataSource myDataSource;
	private final DriverTypeEnum myDriverType;
	private final Table myLockTable;
	private final String myMigrationTablename;

	public HapiMigrationLockSvc(DataSource theDataSource, DriverTypeEnum theDriverType, String myMigrationTablename) {
		this.myDataSource = theDataSource;
		this.myDriverType = theDriverType;
		this.myMigrationTablename = myMigrationTablename;
		myLockTable = buildTable();
	}

	private Table buildTable() {
		try {
			FluentConfiguration configuration = new FluentConfiguration().dataSource(myDataSource);
			JdbcConnectionFactory connectionFactory = new JdbcConnectionFactory(myDataSource, configuration, null);

			String schemaName;

			try (Connection connection = myDataSource.getConnection()) {
				schemaName = connection.getSchema();
			}

			switch (myDriverType) {
				case H2_EMBEDDED: {
					H2Database database = new H2Database(configuration, connectionFactory, null);
					return database.getMainConnection().getSchema(schemaName).getTable(myMigrationTablename);
				}
				case DERBY_EMBEDDED: {
					DerbyDatabase database = new DerbyDatabase(configuration, connectionFactory, null);
					return database.getMainConnection().getSchema(schemaName).getTable(myMigrationTablename);
				}
				case ORACLE_12C: {
					OracleDatabase database = new OracleDatabase(configuration, connectionFactory, null);
					return database.getMainConnection().getSchema(schemaName).getTable(myMigrationTablename);
				}
				case POSTGRES_9_4: {
					PostgreSQLDatabase database = new PostgreSQLDatabase(configuration, connectionFactory, null);
					return database.getMainConnection().getSchema(schemaName).getTable(myMigrationTablename);
				}
				case COCKROACHDB_21_1: {
					CockroachDBDatabase database = new CockroachDBDatabase(configuration, connectionFactory, null);
					return database.getMainConnection().getSchema(schemaName).getTable(myMigrationTablename);
				}
				case MARIADB_10_1: {
					MariaDBDatabase database = new MariaDBDatabase(configuration, connectionFactory, null);
					return database.getMainConnection().getSchema(schemaName).getTable(myMigrationTablename);
				}
				case MYSQL_5_7: {
					MySQLDatabase database = new MySQLDatabase(configuration, connectionFactory, null);
					return database.getMainConnection().getSchema(schemaName).getTable(myMigrationTablename);
				}
				case MSSQL_2012: {
					SQLServerDatabase database = new SQLServerDatabase(configuration, connectionFactory, null);
					return database.getMainConnection().getSchema(schemaName).getTable(myMigrationTablename);
				}
				default:
					throw new UnsupportedOperationException("Driver type not supported: " + myDriverType);
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
