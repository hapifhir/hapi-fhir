/*-
 * #%L
 * HAPI FHIR JPA Model
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
package ca.uhn.fhir.jpa.config;

import ca.uhn.hapi.fhir.sql.hibernatesvc.DatabasePartitionModeIdFilteringMappingContributor;
import com.google.common.base.Strings;
import jakarta.annotation.Nonnull;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.spi.PersistenceUnitInfo;
import org.apache.commons.lang3.Validate;
import org.hibernate.boot.registry.classloading.spi.ClassLoaderService;
import org.hibernate.boot.spi.AdditionalMappingContributor;
import org.hibernate.cfg.BatchSettings;
import org.hibernate.cfg.JdbcSettings;
import org.hibernate.cfg.ManagedBeanSettings;
import org.hibernate.cfg.QuerySettings;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.query.criteria.ValueHandlingMode;
import org.hibernate.resource.jdbc.spi.PhysicalConnectionHandlingMode;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.orm.hibernate5.SpringBeanContainer;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
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

	@Nonnull
	@Override
	public Map<String, Object> getJpaPropertyMap() {
		Map<String, Object> retVal = super.getJpaPropertyMap();

		// SOMEDAY these defaults can be set in the constructor.  setJpaProperties does a merge.
		if (!retVal.containsKey(QuerySettings.CRITERIA_VALUE_HANDLING_MODE)) {
			retVal.put(QuerySettings.CRITERIA_VALUE_HANDLING_MODE, ValueHandlingMode.BIND);
		}

		if (!retVal.containsKey(JdbcSettings.CONNECTION_HANDLING)) {
			retVal.put(
					JdbcSettings.CONNECTION_HANDLING,
					PhysicalConnectionHandlingMode.DELAYED_ACQUISITION_AND_RELEASE_AFTER_TRANSACTION);
		}

		/*
		 * Set some performance options
		 */

		if (!retVal.containsKey(BatchSettings.STATEMENT_BATCH_SIZE)) {
			retVal.put(BatchSettings.STATEMENT_BATCH_SIZE, "30");
		}

		if (!retVal.containsKey(BatchSettings.ORDER_INSERTS)) {
			retVal.put(BatchSettings.ORDER_INSERTS, "true");
		}

		if (!retVal.containsKey(BatchSettings.ORDER_UPDATES)) {
			retVal.put(BatchSettings.ORDER_UPDATES, "true");
		}

		if (!retVal.containsKey(BatchSettings.BATCH_VERSIONED_DATA)) {
			retVal.put(BatchSettings.BATCH_VERSIONED_DATA, "true");
		}
		// Why is this here, you ask? LocalContainerEntityManagerFactoryBean actually clobbers the setting hibernate
		// needs in order to be able to resolve beans, so we add it back in manually here
		if (!retVal.containsKey(ManagedBeanSettings.BEAN_CONTAINER)) {
			retVal.put(ManagedBeanSettings.BEAN_CONTAINER, new SpringBeanContainer(myConfigurableListableBeanFactory));
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

	@Override
	protected void postProcessEntityManagerFactory(
			EntityManagerFactory theEntityManagerFactory, PersistenceUnitInfo thePersistenceUnitInfo) {
		super.postProcessEntityManagerFactory(theEntityManagerFactory, thePersistenceUnitInfo);

		/*
		 * Verify that the ConditionalIdMappingContributor is on the classpath. If this
		 * isn't present, we won't filter the partition IDs from PKs which means we
		 * won't be backward compatible with previous releases.
		 */
		SessionFactoryImplementor sessionFactory = (SessionFactoryImplementor) theEntityManagerFactory;
		ClassLoaderService classLoaderService =
				sessionFactory.getServiceRegistry().getService(ClassLoaderService.class);
		Validate.notNull(classLoaderService, "No classloader service available");
		Collection<AdditionalMappingContributor> additionalMappingContributors =
				classLoaderService.loadJavaServices(AdditionalMappingContributor.class);
		boolean haveConditionalMappingContributor = additionalMappingContributors.stream()
				.anyMatch(t -> t instanceof DatabasePartitionModeIdFilteringMappingContributor);
		Validate.isTrue(
				haveConditionalMappingContributor,
				"No " + DatabasePartitionModeIdFilteringMappingContributor.class.getSimpleName()
						+ " found registered with Hibernate. Verify that hapi-fhir-jpa-hibernate-services is on your classpath. Can not start.");
	}
}
