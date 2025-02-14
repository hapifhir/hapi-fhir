/*-
 * #%L
 * HAPI FHIR JPA Server
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
package ca.uhn.fhir.jpa.util;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.config.HibernatePropertiesProvider;
import ca.uhn.fhir.jpa.migrate.DriverTypeEnum;
import ca.uhn.fhir.jpa.migrate.JdbcUtils;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.dialect.IHapiFhirDialect;
import ca.uhn.fhir.system.HapiSystemProperties;
import org.apache.commons.collections4.SetUtils;
import org.hibernate.dialect.Dialect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.sql.SQLException;
import java.util.Set;
import java.util.TreeSet;
import javax.sql.DataSource;

/**
 * This bean simply performs a startup check that the database schema is
 * appropriate for the Partitioned ID mode set in configuration.
 * If partitioned ID mode is active then the PARTITION_ID column
 * should be a part of primary keys.
 */
public class PartitionedIdModeVerificationSvc {

	private static final Logger ourLog = LoggerFactory.getLogger(PartitionedIdModeVerificationSvc.class);

	private final PartitionSettings myPartitionSettings;
	private final HibernatePropertiesProvider myHibernatePropertiesProvider;
	private final PlatformTransactionManager myTxManager;

	/**
	 * Constructor
	 */
	public PartitionedIdModeVerificationSvc(
			PartitionSettings thePartitionSettings,
			HibernatePropertiesProvider theHibernatePropertiesProvider,
			PlatformTransactionManager theTxManager) {
		myPartitionSettings = thePartitionSettings;
		myHibernatePropertiesProvider = theHibernatePropertiesProvider;
		myTxManager = theTxManager;
	}

	@EventListener(classes = {ContextRefreshedEvent.class})
	public void verifyPartitionedIdMode() throws SQLException {

		DataSource dataSource = myHibernatePropertiesProvider.getDataSource();
		boolean expectDatabasePartitionMode = myPartitionSettings.isDatabasePartitionMode();

		Dialect dialect = myHibernatePropertiesProvider.getDialect();
		if (!(dialect instanceof IHapiFhirDialect)) {
			ourLog.warn("Dialect is not a HAPI FHIR dialect: {}", dialect);
			return;
		}

		DriverTypeEnum driverType = ((IHapiFhirDialect) dialect).getDriverType();
		TransactionTemplate transactionTemplate = new TransactionTemplate(myTxManager);
		DriverTypeEnum.ConnectionProperties cp =
				new DriverTypeEnum.ConnectionProperties(dataSource, transactionTemplate, driverType);
		verifySchemaIsAppropriateForDatabasePartitionMode(cp, expectDatabasePartitionMode);
	}

	public static void verifySchemaIsAppropriateForDatabasePartitionMode(
			DriverTypeEnum.ConnectionProperties cp, boolean expectDatabasePartitionMode) throws SQLException {

		if (HapiSystemProperties.isDisableDatabasePartitionModeSchemaCheck()) {
			return;
		}

		Set<String> pkColumns = JdbcUtils.getPrimaryKeyColumns(cp, "HFJ_RESOURCE");
		if (pkColumns.isEmpty()) {
			return;
		}

		if (!expectDatabasePartitionMode) {
			if (!SetUtils.isEqualSet(pkColumns, Set.of("RES_ID"))) {
				throw new ConfigurationException(Msg.code(2563)
						+ "System is configured in Partitioned ID mode but the database schema is not correct for this. Found HFJ_RESOURCE PK: "
						+ new TreeSet<>(pkColumns));
			}
		} else {
			if (!SetUtils.isEqualSet(pkColumns, Set.of("RES_ID", "PARTITION_ID"))) {
				throw new ConfigurationException(Msg.code(2564)
						+ "System is configured in Partitioned ID mode but the database schema is not correct for this. Found HFJ_RESOURCE PK: "
						+ new TreeSet<>(pkColumns));
			}
		}
	}
}
