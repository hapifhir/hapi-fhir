/*-
 * #%L
 * HAPI FHIR JPA Model
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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

import com.google.common.base.Strings;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.query.criteria.ValueHandlingMode;
import org.hibernate.resource.jdbc.spi.PhysicalConnectionHandlingMode;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.orm.hibernate5.SpringBeanContainer;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * This class is an extension of the Spring/Hibernate LocalContainerEntityManagerFactoryBean
 * that sets some sensible default property values
 */
public class HapiFhirLocalContainerEntityManagerFactoryBean extends LocalContainerEntityManagerFactoryBean {

	// https://stackoverflow.com/questions/57902388/how-to-inject-spring-beans-into-the-hibernate-envers-revisionlistener
	ConfigurableListableBeanFactory myConfigurableListableBeanFactory;

	public HapiFhirLocalContainerEntityManagerFactoryBean(
			ConfigurableListableBeanFactory theConfigurableListableBeanFactory) {
		myConfigurableListableBeanFactory = theConfigurableListableBeanFactory;
	}

	@Override
	public Map<String, Object> getJpaPropertyMap() {
		Map<String, Object> retVal = super.getJpaPropertyMap();

		// SOMEDAY these defaults can be set in the constructor.  setJpaProperties does a merge.
		if (!retVal.containsKey(AvailableSettings.CRITERIA_VALUE_HANDLING_MODE)) {
			retVal.put(AvailableSettings.CRITERIA_VALUE_HANDLING_MODE, ValueHandlingMode.BIND);
		}

		if (!retVal.containsKey(AvailableSettings.CONNECTION_HANDLING)) {
			retVal.put(
					AvailableSettings.CONNECTION_HANDLING,
					PhysicalConnectionHandlingMode.DELAYED_ACQUISITION_AND_RELEASE_AFTER_TRANSACTION);
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
		// Why is this here, you ask? LocalContainerEntityManagerFactoryBean actually clobbers the setting hibernate
		// needs
		// in order to be able to resolve beans, so we add it back in manually here
		if (!retVal.containsKey(AvailableSettings.BEAN_CONTAINER)) {
			retVal.put(AvailableSettings.BEAN_CONTAINER, new SpringBeanContainer(myConfigurableListableBeanFactory));
		}

		return retVal;
	}

	/**
	 * Helper to add hook to property.
	 *
	 * Listener properties are comma-separated lists, so we can't just overwrite or default it.
	 */
	void addHibernateHook(String thePropertyName, String theHookFQCN) {
		Map<String, Object> retVal = super.getJpaPropertyMap();
		List<String> listeners = new ArrayList<>();

		{
			String currentListeners = (String) retVal.get(thePropertyName);
			if (!Strings.isNullOrEmpty(currentListeners)) {
				listeners.addAll(Arrays.asList(currentListeners.split(",")));
			}
		}

		// add if missing
		if (!listeners.contains(theHookFQCN)) {
			listeners.add(theHookFQCN);
			retVal.put(thePropertyName, String.join(",", listeners));
		}
	}
}
