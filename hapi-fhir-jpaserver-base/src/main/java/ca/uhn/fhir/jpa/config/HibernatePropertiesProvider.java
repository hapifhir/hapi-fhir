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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import java.util.concurrent.atomic.AtomicInteger;
import javax.sql.DataSource;

public class HibernatePropertiesProvider {

	/**
	 * The lowest SQL Server database compatibility level which supports the OPENJSON
	 * table valued function. Equates to SQL Server 2016+
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
	 * Returns true when the SQL Server database behind this provider supports the
	 * OPENJSON table-valued function, which requires a database compatibility level of
	 * 130 (SQL Server 2016) or higher.
	 * Returns false if the DB probe failed, or if OPENJSON is not supported.
	 */
	private boolean isSqlServerJsonSupported() {
		Boolean cached = mySqlServerJsonSupported;
		if (cached != null) {
			return cached;
		}

		synchronized (this) {
			cached = mySqlServerJsonSupported;
			if (cached != null) {
				return cached;
			}

			Boolean probeResult = probeSqlServerJsonSupport();
			if (probeResult != null) {
				if (!probeResult) {
					ourLog.warn(
							"This SQL Server database is running at a compatibility level below {}, so the OPENJSON function is not available. "
									+ "Searches or patient compartment authorization parameters containing large numbers (thousands) of resource IDs "
									+ "will continue to be sent as one bind parameter per ID, which can exceed DB parameter limits. "
									+ "Raise the database compatibility level to {} if you perform such searches.",
							MINIMUM_SQL_SERVER_OPENJSON_COMPATIBILITY_LEVEL,
							MINIMUM_SQL_SERVER_OPENJSON_COMPATIBILITY_LEVEL);
				}
				mySqlServerJsonSupported = probeResult;
				return probeResult;
			}

			// The probe failed. Retry 3 times before giving up and defaulting to unsupported.
			if (mySqlServerJsonProbeFailureCount.incrementAndGet() >= MAX_SQL_SERVER_JSON_PROBE_FAILURES) {
				ourLog.warn(
						"Could not determine the compatibility level of this SQL Server database after {} attempts. "
								+ "Searches or patient compartment authorization parameters containing large numbers (thousands) of resource IDs "
								+ "will continue to be sent as one bind parameter per ID, which can exceed DB parameter limits.",
						MAX_SQL_SERVER_JSON_PROBE_FAILURES);
				mySqlServerJsonSupported = Boolean.FALSE;
			}
			return false;
		}
	}

	/**
	 * Probes a SQL Server database to determine if it supports the OPENJSON
	 * table-valued function, or null if the probe failed to produce one.
	 */
	@Nullable
	private Boolean probeSqlServerJsonSupport() {
		try {
			// JdbcTemplate uses the connection of the current transaction rather than taking another from the pool
			Integer compatibilityLevel = new JdbcTemplate(getDataSource())
					.query(
							"SELECT compatibility_level FROM sys.databases WHERE name = DB_NAME()",
							(ResultSetExtractor<Integer>) t -> t.next() ? t.getInt(1) : null);
			if (compatibilityLevel != null) {
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
