package ca.uhn.fhir.jpa.util;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.config.HibernatePropertiesProvider;
import ca.uhn.fhir.jpa.migrate.DriverTypeEnum;
import ca.uhn.fhir.jpa.migrate.JdbcUtils;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.dialect.IHapiFhirDialect;
import org.apache.commons.collections4.SetUtils;
import org.hibernate.dialect.Dialect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Set;
import java.util.TreeSet;

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
	public PartitionedIdModeVerificationSvc(PartitionSettings thePartitionSettings, HibernatePropertiesProvider theHibernatePropertiesProvider, PlatformTransactionManager theTxManager) {
		myPartitionSettings = thePartitionSettings;
		myHibernatePropertiesProvider = theHibernatePropertiesProvider;
		myTxManager = theTxManager;
	}


	@EventListener(classes = {ContextRefreshedEvent.class})
	public void verifyPartitionedIdMode() throws SQLException {

		DataSource dataSource = myHibernatePropertiesProvider.getDataSource();

		Dialect dialect = myHibernatePropertiesProvider.getDialect();
		if (!(dialect instanceof IHapiFhirDialect)) {
			ourLog.warn("Dialect is not a HAPI FHIR dialect: {}", dialect);
			return;
		}

		DriverTypeEnum driverType = ((IHapiFhirDialect) dialect).getDriverType();
		TransactionTemplate transactionTemplate = new TransactionTemplate(myTxManager);
		DriverTypeEnum.ConnectionProperties cp = new DriverTypeEnum.ConnectionProperties(dataSource, transactionTemplate, driverType);
		Set<String> pkColumns = JdbcUtils.getPrimaryKeyColumns(cp, "HFJ_RESOURCE");
		if (pkColumns.isEmpty()) {
			return;
		}

		if (!myPartitionSettings.isPartitionIdsInPrimaryKeys()) {
			if (!SetUtils.isEqualSet(pkColumns, Set.of("RES_ID"))) {
				throw new ConfigurationException(Msg.code(2563) + "System is configured in Partitioned ID mode but the database schema is not correct for this. Found HFJ_RESOURCE PK: " + new TreeSet<>(pkColumns));
			}
		} else {
			if (!SetUtils.isEqualSet(pkColumns, Set.of("RES_ID", "PARTITION_ID"))) {
				throw new ConfigurationException(Msg.code(2564) + "System is configured in Partitioned ID mode but the database schema is not correct for this. Found HFJ_RESOURCE PK: " + new TreeSet<>(pkColumns));
			}
		}
	}
}
