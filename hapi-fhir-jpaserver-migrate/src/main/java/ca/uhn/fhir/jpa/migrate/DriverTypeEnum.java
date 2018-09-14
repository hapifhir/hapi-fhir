package ca.uhn.fhir.jpa.migrate;

import org.apache.commons.lang3.Validate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Nonnull;

/*-
 * #%L
 * HAPI FHIR JPA Server - Migration
 * %%
 * Copyright (C) 2014 - 2018 University Health Network
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

public enum DriverTypeEnum {

	DERBY_EMBEDDED("org.apache.derby.jdbc.EmbeddedDriver", true),
	MARIADB_10_1("org.mariadb.jdbc.Driver", false),

	// Formerly com.mysql.jdbc.Driver
	MYSQL_5_7("com.mysql.cj.jdbc.Driver", false),

	POSTGRES_9_4("org.postgresql.Driver", false),

	ORACLE_12C("oracle.jdbc.OracleDriver", false),

	MSSQL_2012("com.microsoft.sqlserver.jdbc.SQLServerDataSource", false),

	;

	private String myDriverClassName;
	private boolean myDerby;

	/**
	 * Constructor
	 */
	DriverTypeEnum(String theDriverClassName, boolean theDerby) {
		myDriverClassName = theDriverClassName;
		myDerby = theDerby;
	}


	public ConnectionProperties newConnectionProperties(String theUrl, String theUsername, String thePassword) {
		SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
		dataSource.setAutoCommit(false);
		dataSource.setDriverClassName(myDriverClassName);
		dataSource.setUrl(theUrl);
		dataSource.setUsername(theUsername);
		dataSource.setPassword(thePassword);
		dataSource.setSuppressClose(true);

		DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();
		transactionManager.setDataSource(dataSource);
		transactionManager.afterPropertiesSet();

		TransactionTemplate txTemplate = new TransactionTemplate();
		txTemplate.setTransactionManager(transactionManager);
		txTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
		txTemplate.afterPropertiesSet();

		return new ConnectionProperties(dataSource, txTemplate, this);
	}

	public static class ConnectionProperties {

		private final DriverTypeEnum myDriverType;
		private final SingleConnectionDataSource myDataSource;
		private final TransactionTemplate myTxTemplate;
		/**
		 * Constructor
		 */
		public ConnectionProperties(SingleConnectionDataSource theDataSource, TransactionTemplate theTxTemplate, DriverTypeEnum theDriverType) {
			Validate.notNull(theDataSource);
			Validate.notNull(theTxTemplate);
			Validate.notNull(theDriverType);

			myDataSource = theDataSource;
			myTxTemplate = theTxTemplate;
			myDriverType = theDriverType;
		}

		public DriverTypeEnum getDriverType() {
			return myDriverType;
		}

		@Nonnull
		public SingleConnectionDataSource getDataSource() {
			return myDataSource;
		}

		@Nonnull
		public JdbcTemplate newJdbcTemplate() {
			JdbcTemplate jdbcTemplate = new JdbcTemplate();
			jdbcTemplate.setDataSource(myDataSource);
			return jdbcTemplate;
		}

		@Nonnull
		public TransactionTemplate getTxTemplate() {
			return myTxTemplate;
		}

		public void close() {
			myDataSource.destroy();
		}
	}
}
