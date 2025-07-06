/*-
 * #%L
 * HAPI FHIR JPA Server Test Utilities
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.embedded;

import ca.uhn.fhir.jpa.migrate.DriverTypeEnum;
import ca.uhn.fhir.jpa.migrate.util.SqlUtil;
import jakarta.annotation.PreDestroy;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import javax.sql.DataSource;

/**
 * For testing purposes.
 * <br/><br/>
 * Provides embedded database functionality with lazy initialization. Inheritors of this class will have access to a datasource and JDBC Template for executing queries.
 * Database initialization is deferred until first access to improve memory usage and startup performance.
 */
public abstract class JpaEmbeddedDatabase {

	private static final Logger ourLog = LoggerFactory.getLogger(JpaEmbeddedDatabase.class);

	private DriverTypeEnum myDriverType;
	private String myUsername;
	private String myPassword;
	private String myUrl;
	private DriverTypeEnum.ConnectionProperties myConnectionProperties;
	private JdbcTemplate myJdbcTemplate;
	private Connection myConnection;

	// Lazy initialization support
	private boolean myIsInitialized = false;
	private Supplier<InitializationData> myInitializationSupplier;
	private Object myContainerReference; // Reference to container for lifecycle management

	/**
	 * Data needed to initialize the database connection
	 */
	public static class InitializationData {
		private final DriverTypeEnum driverType;
		private final String url;
		private final String username;
		private final String password;
		private final Object containerReference; // Optional container reference for lifecycle management

		public InitializationData(DriverTypeEnum theDriverType, String theUrl, String theUsername, String thePassword) {
			this(theDriverType, theUrl, theUsername, thePassword, null);
		}

		public InitializationData(
				DriverTypeEnum theDriverType,
				String theUrl,
				String theUsername,
				String thePassword,
				Object theContainerReference) {
			driverType = theDriverType;
			url = theUrl;
			username = theUsername;
			password = thePassword;
			containerReference = theContainerReference;
		}

		public DriverTypeEnum getDriverType() {
			return driverType;
		}

		public String getUrl() {
			return url;
		}

		public String getUsername() {
			return username;
		}

		public String getPassword() {
			return password;
		}

		public Object getContainerReference() {
			return containerReference;
		}
	}

	/**
	 * Constructor for immediate initialization (backwards compatibility)
	 */
	protected JpaEmbeddedDatabase() {
		// Default constructor for immediate initialization
	}

	/**
	 * Constructor for lazy initialization
	 */
	protected JpaEmbeddedDatabase(Supplier<InitializationData> theInitializationSupplier) {
		myInitializationSupplier = theInitializationSupplier;
	}

	@PreDestroy
	public abstract void stop();

	public abstract void disableConstraints();

	public abstract void enableConstraints();

	public abstract void clearDatabase();

	/**
	 * Ensures the database is initialized. This method is idempotent.
	 */
	protected synchronized void ensureInitialized() {
		if (!myIsInitialized) {
			if (myInitializationSupplier != null) {
				// Lazy initialization
				InitializationData data = myInitializationSupplier.get();
				ourLog.info("Initializing database for driver type: {}", data.getDriverType());
				doInitialize(data.getDriverType(), data.getUrl(), data.getUsername(), data.getPassword());
				myContainerReference = data.getContainerReference();
			}
			// If myInitializationSupplier is null, assume initialize() was called directly (immediate initialization)
			myIsInitialized = true;
		}
	}

	/**
	 * Initialize immediately (backwards compatibility)
	 */
	public void initialize(DriverTypeEnum theDriverType, String theUrl, String theUsername, String thePassword) {
		doInitialize(theDriverType, theUrl, theUsername, thePassword);
		myIsInitialized = true;
	}

	/**
	 * Perform the actual initialization
	 */
	private void doInitialize(DriverTypeEnum theDriverType, String theUrl, String theUsername, String thePassword) {
		myDriverType = theDriverType;
		myUsername = theUsername;
		myPassword = thePassword;
		myUrl = theUrl;
		myConnectionProperties = theDriverType.newConnectionProperties(theUrl, theUsername, thePassword);
		myJdbcTemplate = myConnectionProperties.newJdbcTemplate();
		try {
			myConnection = myConnectionProperties.getDataSource().getConnection();
		} catch (SQLException theE) {
			throw new RuntimeException(theE);
		}
	}

	public abstract DriverTypeEnum getDriverType();

	public String getUsername() {
		ensureInitialized();
		return myUsername;
	}

	public String getPassword() {
		ensureInitialized();
		return myPassword;
	}

	public String getUrl() {
		ensureInitialized();
		return myUrl;
	}

	public JdbcTemplate getJdbcTemplate() {
		ensureInitialized();
		return myJdbcTemplate;
	}

	public DataSource getDataSource() {
		ensureInitialized();
		return myConnectionProperties.getDataSource();
	}

	public void insertTestData(String theSql) {
		ensureInitialized();
		disableConstraints();
		executeSqlAsBatch(theSql);
		enableConstraints();
	}

	public void executeSqlAsBatch(String theSql) {
		ensureInitialized();
		List<String> statements = SqlUtil.splitSqlFileIntoStatements(theSql);
		executeSqlAsBatch(statements);
	}

	public void executeSqlAsBatch(List<String> theStatements) {
		ensureInitialized();
		try (final Statement statement = myConnection.createStatement()) {
			for (String sql : theStatements) {
				if (!StringUtils.isBlank(sql)) {
					statement.addBatch(sql);
					ourLog.debug("Added to batch: {}", sql);
				}
			}
			statement.executeBatch();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public List<Map<String, Object>> query(String theSql) {
		ensureInitialized();
		return getJdbcTemplate().queryForList(theSql);
	}

	/**
	 * @return true if the database has been initialized
	 */
	public boolean isInitialized() {
		return myIsInitialized;
	}

	/**
	 * @return the container reference if this is a container-based database, null otherwise
	 */
	protected Object getContainerReference() {
		return myContainerReference;
	}
}
