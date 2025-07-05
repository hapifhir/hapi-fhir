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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.testcontainers.containers.JdbcDatabaseContainer;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import javax.sql.DataSource;

// Created by Claude Sonnet 4
/**
 * Base class for container-based embedded databases that provides lazy initialization.
 * Containers are only started when first accessed, reducing memory pressure when multiple
 * database types are configured but only one is used per test.
 */
public abstract class LazyJpaContainerDatabase extends JpaEmbeddedDatabase {

	private static final Logger ourLog = LoggerFactory.getLogger(LazyJpaContainerDatabase.class);

	protected final Supplier<JdbcDatabaseContainer<?>> myContainerSupplier;
	protected JdbcDatabaseContainer<?> myContainer;
	private boolean myIsInitialized = false;

	protected LazyJpaContainerDatabase(Supplier<JdbcDatabaseContainer<?>> theContainerSupplier) {
		myContainerSupplier = theContainerSupplier;
	}

	/**
	 * Ensures the container is started and database is initialized.
	 * This method is idempotent - multiple calls will not restart the container.
	 */
	protected synchronized void ensureStarted() {
		if (!myIsInitialized) {
			ourLog.info("Starting container for database type: {}", getDriverType());
			myContainer = myContainerSupplier.get();
			myContainer.start();
			super.initialize(
					getDriverType(), myContainer.getJdbcUrl(), myContainer.getUsername(), myContainer.getPassword());
			myIsInitialized = true;
			ourLog.info("Container started successfully for database type: {}", getDriverType());
		}
	}

	@Override
	public DataSource getDataSource() {
		ensureStarted();
		return super.getDataSource();
	}

	@Override
	public JdbcTemplate getJdbcTemplate() {
		ensureStarted();
		return super.getJdbcTemplate();
	}

	@Override
	public void executeSqlAsBatch(String theSql) {
		ensureStarted();
		super.executeSqlAsBatch(theSql);
	}

	@Override
	public void executeSqlAsBatch(List<String> theStatements) {
		ensureStarted();
		super.executeSqlAsBatch(theStatements);
	}

	@Override
	public String getUrl() {
		ensureStarted();
		return super.getUrl();
	}

	@Override
	public String getUsername() {
		ensureStarted();
		return super.getUsername();
	}

	@Override
	public String getPassword() {
		ensureStarted();
		return super.getPassword();
	}

	@Override
	public List<Map<String, Object>> query(String theSql) {
		ensureStarted();
		return super.query(theSql);
	}

	@Override
	public void insertTestData(String theSql) {
		ensureStarted();
		super.insertTestData(theSql);
	}

	@Override
	public void clearDatabase() {
		if (myIsInitialized) {
			// Subclasses must implement the actual clear logic
			doClearDatabase();
		}
	}

	@Override
	public void disableConstraints() {
		ensureStarted();
		doDisableConstraints();
	}

	@Override
	public void enableConstraints() {
		ensureStarted();
		doEnableConstraints();
	}

	/**
	 * Subclasses must implement the actual clear database logic
	 */
	protected abstract void doClearDatabase();

	/**
	 * Subclasses must implement the actual disable constraints logic
	 */
	protected abstract void doDisableConstraints();

	/**
	 * Subclasses must implement the actual enable constraints logic
	 */
	protected abstract void doEnableConstraints();

	@Override
	public void stop() {
		if (myContainer != null && myContainer.isRunning()) {
			ourLog.info("Stopping container for database type: {}", getDriverType());
			myContainer.stop();
			myIsInitialized = false;
			ourLog.info("Container stopped for database type: {}", getDriverType());
		}
	}

	/**
	 * @return true if the container has been started and is running
	 */
	public boolean isStarted() {
		return myIsInitialized && myContainer != null && myContainer.isRunning();
	}

	/**
	 * Subclasses must implement this to return the appropriate driver type
	 */
	public abstract DriverTypeEnum getDriverType();
}
