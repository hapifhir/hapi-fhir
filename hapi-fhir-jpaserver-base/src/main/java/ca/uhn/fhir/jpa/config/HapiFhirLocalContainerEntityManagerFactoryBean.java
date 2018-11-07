package ca.uhn.fhir.jpa.config;

import org.hibernate.cfg.AvailableSettings;
import org.hibernate.query.criteria.LiteralHandlingMode;
import org.hibernate.resource.jdbc.spi.PhysicalConnectionHandlingMode;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import java.util.Map;

/**
 * This class is an extension of the Spring/Hibernate LocalContainerEntityManagerFactoryBean
 * that sets some sensible default property values
 */
public class HapiFhirLocalContainerEntityManagerFactoryBean extends LocalContainerEntityManagerFactoryBean {
	@Override
	public Map<String, Object> getJpaPropertyMap() {
		Map<String, Object> retVal = super.getJpaPropertyMap();

		if (!retVal.containsKey(AvailableSettings.CRITERIA_LITERAL_HANDLING_MODE)) {
			retVal.put(AvailableSettings.CRITERIA_LITERAL_HANDLING_MODE, LiteralHandlingMode.BIND);
		}

		if (!retVal.containsKey(AvailableSettings.CONNECTION_HANDLING)) {
			retVal.put(AvailableSettings.CONNECTION_HANDLING, PhysicalConnectionHandlingMode.DELAYED_ACQUISITION_AND_HOLD);
		}

		/*
		 * Set some performance options
		 */

		if (!retVal.containsKey(AvailableSettings.STATEMENT_BATCH_SIZE)) {
			retVal.put(AvailableSettings.STATEMENT_BATCH_SIZE, "30");
		}

		if (!retVal.containsKey(AvailableSettings.ORDER_INSERTS)) {
			retVal.put(AvailableSettings.ORDER_INSERTS, "true");
		}

		if (!retVal.containsKey(AvailableSettings.ORDER_UPDATES)) {
			retVal.put(AvailableSettings.ORDER_UPDATES, "true");
		}

		if (!retVal.containsKey(AvailableSettings.BATCH_VERSIONED_DATA)) {
			retVal.put(AvailableSettings.BATCH_VERSIONED_DATA, "true");
		}

		return retVal;
	}


}
