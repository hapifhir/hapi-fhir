/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.config;

import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.util.ReflectionUtil;
import com.google.common.annotations.VisibleForTesting;
import jakarta.annotation.Nullable;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.hibernate.dialect.Dialect;
import org.hibernate.search.engine.cfg.BackendSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.concurrent.atomic.AtomicInteger;
import javax.sql.DataSource;

public class HibernatePropertiesProvider {

	/**
	 * The lowest SQL Server database compatibility level which supports the OPENJSON
	 * table valued function.
	 */
	public static final int MINIMUM_SQL_SERVER_OPENJSON_COMPATIBILITY_LEVEL = 130;

	private static final int MAX_SQL_SERVER_JSON_PROBE_FAILURES = 3;

	private static final Logger ourLog = LoggerFactory.getLogger(HibernatePropertiesProvider.class);

	@Autowired
	private LocalContainerEntityManagerFactoryBean myEntityManagerFactory;

	private final AtomicInteger mySqlServerJsonProbeFailureCount = new AtomicInteger(0);

	private Dialect myDialect;
	private String myHibernateSearchBackend;
	// If the dialect is SQL Server, store whether it supports JSON, since checking requires probing the DB
	private volatile Boolean mySqlServerJsonSupported;

	@Autowired
	private JpaStorageSettings myStorageSettings;

	@VisibleForTesting
	public void setDialectForUnitTest(Dialect theDialect) {
		myDialect = theDialect;
	}

	@VisibleForTesting
	public void setSqlServerJsonSupportedForUnitTest(@Nullable Boolean theSqlServerJsonSupported) {
		mySqlServerJsonSupported = theSqlServerJsonSupported;
		if (theSqlServerJsonSupported == null) {
			mySqlServerJsonProbeFailureCount.set(0);
		}
	}

	public Dialect getDialect() {
		Dialect dialect = myDialect;
		if (dialect == null) {
			String dialectClass =
					(String) myEntityManagerFactory.getJpaPropertyMap().get("hibernate.dialect");
			dialect = ReflectionUtil.newInstanceOrReturnNull(dialectClass, Dialect.class);
			Validate.notNull(dialect, "Unable to create instance of class: %s", dialectClass);
			myDialect = dialect;
		}

		return dialect;
	}

	public String getHibernateSearchBackend() {
		String hibernateSearchBackend = myHibernateSearchBackend;
		if (StringUtils.isBlank(hibernateSearchBackend)) {
			hibernateSearchBackend = (String)
					myEntityManagerFactory.getJpaPropertyMap().get(BackendSettings.backendKey(BackendSettings.TYPE));
			Validate.notNull(
					hibernateSearchBackend, BackendSettings.backendKey(BackendSettings.TYPE) + " property is unset!");
			myHibernateSearchBackend = hibernateSearchBackend;
		}
		return myHibernateSearchBackend;
	}

	public DataSource getDataSource() {
		return myEntityManagerFactory.getDataSource();
	}

	public boolean isOracleDialect() {
		return getDialect() instanceof org.hibernate.dialect.OracleDialect;
	}

	/**
	 * Returns true if the database type supports JSON-array unpacking
	 *
	 * @since 8.14.0
	 */
	public boolean isJsonUnpackingSupported() {
		if (!(getDialect() instanceof org.hibernate.dialect.SQLServerDialect)) {
			return true;
		}

		return isSqlServerJsonSupported();
	}

	/**
	 * Returns <code>true</code> when the SQL Server database behind this provider supports the
	 * <code>OPENJSON</code> table-valued function, which requires a database compatibility level of
	 * {@value #MINIMUM_SQL_SERVER_OPENJSON_COMPATIBILITY_LEVEL} (SQL Server 2016) or higher.
	 * <p>
	 * The database is probed the first time this method is called and the answer is cached for the
	 * lifetime of this provider, so that the probe never runs at startup. Once the answer is cached - whether
	 * that is "supported", or "not supported" because the compatibility level is too low, or because the
	 * probe could not get a definitive answer after repeated attempts - a WARN is logged exactly once per
	 * provider if the outcome is "not supported". Intermediate probe failures, before the cache settles,
	 * are only logged at debug. The probe runs until it gets a definitive answer - at most
	 * {@value #MAX_SQL_SERVER_JSON_PROBE_FAILURES} times if it keeps failing - after which the failure
	 * itself is cached as "not supported", so a database whose compatibility level can never be determined
	 * costs a bounded number of extra connection attempts rather than one per over-threshold search forever.
	 * </p>
	 */
	private boolean isSqlServerJsonSupported() {
		Boolean cached = mySqlServerJsonSupported;
		if (cached != null) {
			return cached;
		}

		Boolean probeResult = probeSqlServerJsonSupport();
		if (probeResult != null) {
			if (!probeResult) {
				ourLog.warn(
						"This SQL Server database is running at a compatibility level below {}, so the OPENJSON function is not available. "
								+ "Large resource ID lists will continue to be sent as one bind parameter per ID, which can exceed the number of "
								+ "bind parameters the database accepts in a single statement. Raise the database compatibility level to {} "
								+ "(SQL Server 2016) or higher to avoid this.",
						MINIMUM_SQL_SERVER_OPENJSON_COMPATIBILITY_LEVEL,
						MINIMUM_SQL_SERVER_OPENJSON_COMPATIBILITY_LEVEL);
			}
			mySqlServerJsonSupported = probeResult;
			return probeResult;
		}

		// The probe did not produce a definitive answer. Two threads racing here can each increment this
		// counter and both re-probe on their next call - benign, since the probe is read-only and
		// idempotent. Once enough consecutive failures have piled up, give up and cache "false" so a
		// permanently unreadable sys.databases table does not cost one connection attempt per search.
		if (mySqlServerJsonProbeFailureCount.incrementAndGet() >= MAX_SQL_SERVER_JSON_PROBE_FAILURES) {
			ourLog.warn(
					"Could not determine the compatibility level of this SQL Server database after {} attempts, so falling back "
							+ "to sending large resource ID lists as one bind parameter per ID, which can exceed the number of bind "
							+ "parameters the database accepts in a single statement. Raise the database compatibility level to "
							+ MINIMUM_SQL_SERVER_OPENJSON_COMPATIBILITY_LEVEL
							+ " (SQL Server 2016) or higher to enable the OPENJSON function.",
					MAX_SQL_SERVER_JSON_PROBE_FAILURES);
			mySqlServerJsonSupported = Boolean.FALSE;
		}
		return false;
	}

	/**
	 * Probes the database for a definitive answer to whether it supports the <code>OPENJSON</code>
	 * table-valued function, or <code>null</code> if the probe failed to produce one - either because the
	 * query raised an exception, or because it returned no row. A definitive <code>false</code> - this is
	 * not a SQL Server dialect at all - is not a failure, and is returned directly.
	 */
	@Nullable
	private Boolean probeSqlServerJsonSupport() {
		if (!(getDialect() instanceof org.hibernate.dialect.SQLServerDialect)) {
			return false;
		}

		try (Connection connection = getDataSource().getConnection();
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery(
						"SELECT compatibility_level FROM sys.databases WHERE name = DB_NAME()")) {
			if (resultSet.next()) {
				int compatibilityLevel = resultSet.getInt(1);
				ourLog.debug("SQL Server database compatibility level is {}", compatibilityLevel);
				return compatibilityLevel >= MINIMUM_SQL_SERVER_OPENJSON_COMPATIBILITY_LEVEL;
			}
			ourLog.debug("SQL Server compatibility level probe returned no row for this database");
		} catch (Exception e) {
			ourLog.debug("Failed to probe the compatibility level of this SQL Server database", e);
		}
		return null;
	}
}
