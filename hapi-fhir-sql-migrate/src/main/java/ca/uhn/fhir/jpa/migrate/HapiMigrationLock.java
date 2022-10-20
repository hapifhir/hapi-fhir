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

public class HapiMigrationLock implements AutoCloseable {
	private static final Logger ourLog = LoggerFactory.getLogger(HapiMigrationLock.class);
	private final DriverTypeEnum myDriverType;
	private final Table myLockTableConnection;
	private final String myMigrationTablename;
	//	private final PlatformTransactionManager myTransactionManager;
//	private final DefaultTransactionStatus myActiveTransaction;
//	private final Connection myConnection;
//	private final JdbcTemplate myJdbcTemplate;
	private JdbcConnectionFactory myConnectionFactory;

	public HapiMigrationLock(DataSource theDataSource, DriverTypeEnum theDriverType, String myMigrationTablename) {
		this.myDriverType = theDriverType;
		this.myMigrationTablename = myMigrationTablename;
//		myTransactionManager = new org.springframework.jdbc.datasource.DataSourceTransactionManager(theDataSource);


//		DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
//		definition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
//		definition.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);
//		myActiveTransaction = (DefaultTransactionStatus) myTransactionManager.getTransaction(definition);

//		JdbcTransactionObjectSupport jdbcTransactionObjectSupport = (JdbcTransactionObjectSupport) myActiveTransaction.getTransaction();
//		myConnection = jdbcTransactionObjectSupport.getConnectionHolder().getConnection();
		myLockTableConnection = openLockTableConnection(theDataSource);
//		myConnection = myLockTableConnection.getDatabase().getMainConnection().getJdbcConnection();
//		myJdbcTemplate = new JdbcTemplate(myConnection);

		ourLog.info("Locking Migration Table {}", myLockTableConnection.getDatabase().getMainConnection().getJdbcConnection());
		lock();
		ourLog.info("Locked Migration Table {}", myLockTableConnection.getDatabase().getMainConnection().getJdbcConnection());
	}

	private void lock() {
		try {
			myLockTableConnection.getDatabase().getMainConnection().getJdbcConnection().setAutoCommit(false);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		myLockTableConnection.lock();
	}

	@Override
	public void close() {
		if (myLockTableConnection == null) {
			return;
		}

		myLockTableConnection.unlock();

		ourLog.info("Unlocked Migration Table {}", myLockTableConnection.getDatabase().getMainConnection().getJdbcConnection());

		// This will commit our transaction and release the lock
//		myActiveTransaction.flush();
		myLockTableConnection.getDatabase().close();
		myConnectionFactory.close();
	}

	private Table openLockTableConnection(DataSource theDataSource) {
		try {
			FluentConfiguration configuration = new FluentConfiguration().dataSource(theDataSource);
			myConnectionFactory = new JdbcConnectionFactory(theDataSource, configuration, null);
			String schemaName;
			try (Connection connection = theDataSource.getConnection()) {
				schemaName = connection.getSchema();
			}

			switch (myDriverType) {
				case H2_EMBEDDED: {
					H2Database database = new H2Database(configuration, myConnectionFactory, null);
					return database.getMainConnection().getSchema(schemaName).getTable(myMigrationTablename);
				}
				case DERBY_EMBEDDED: {
					DerbyDatabase database = new DerbyDatabase(configuration, myConnectionFactory, null);
					return database.getMainConnection().getSchema(schemaName).getTable(myMigrationTablename);
				}
				case ORACLE_12C: {
					OracleDatabase database = new OracleDatabase(configuration, myConnectionFactory, null);
					return database.getMainConnection().getSchema(schemaName).getTable(myMigrationTablename);
				}
				case POSTGRES_9_4: {
					PostgreSQLDatabase database = new PostgreSQLDatabase(configuration, myConnectionFactory, null);
					return database.getMainConnection().getSchema(schemaName).getTable(myMigrationTablename);
				}
				case COCKROACHDB_21_1: {
					CockroachDBDatabase database = new CockroachDBDatabase(configuration, myConnectionFactory, null);
					return database.getMainConnection().getSchema(schemaName).getTable(myMigrationTablename);
				}
				case MARIADB_10_1: {
					MariaDBDatabase database = new MariaDBDatabase(configuration, myConnectionFactory, null);
					return database.getMainConnection().getSchema(schemaName).getTable(myMigrationTablename);
				}
				case MYSQL_5_7: {
					MySQLDatabase database = new MySQLDatabase(configuration, myConnectionFactory, null);
					return database.getMainConnection().getSchema(schemaName).getTable(myMigrationTablename);
				}
				case MSSQL_2012: {
					SQLServerDatabase database = new SQLServerDatabase(configuration, myConnectionFactory, null);
					return database.getMainConnection().getSchema(schemaName).getTable(myMigrationTablename);
				}
				default:
					throw new UnsupportedOperationException("Driver type not supported: " + myDriverType);
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

}
