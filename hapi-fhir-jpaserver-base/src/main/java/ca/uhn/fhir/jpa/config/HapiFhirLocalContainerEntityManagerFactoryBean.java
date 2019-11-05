package ca.uhn.fhir.jpa.config;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2019 University Health Network
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
