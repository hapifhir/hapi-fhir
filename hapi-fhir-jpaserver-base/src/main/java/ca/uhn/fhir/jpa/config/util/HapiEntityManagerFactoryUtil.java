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
package ca.uhn.fhir.jpa.config.util;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.config.HapiFhirHibernateJpaDialect;
import ca.uhn.fhir.jpa.config.HapiFhirLocalContainerEntityManagerFactoryBean;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.jpa.util.ISequenceValueMassager;
import ca.uhn.fhir.util.ReflectionUtil;
import ca.uhn.hapi.fhir.sql.hibernatesvc.HapiHibernateDialectSettingsService;
import jakarta.persistence.spi.PersistenceUnitInfo;
import org.hibernate.boot.registry.BootstrapServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.hibernate.jpa.boot.internal.EntityManagerFactoryBuilderImpl;
import org.hibernate.jpa.boot.internal.PersistenceUnitInfoDescriptor;
import org.hibernate.jpa.boot.spi.EntityManagerFactoryBuilder;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import java.util.Map;
import java.util.Objects;
import java.util.Properties;

public final class HapiEntityManagerFactoryUtil {
	private HapiEntityManagerFactoryUtil() {}

	/**
	 * This method provides a partially completed entity manager
	 * factory with HAPI FHIR customizations
	 */
	public static LocalContainerEntityManagerFactoryBean newEntityManagerFactory(
			ConfigurableListableBeanFactory myConfigurableListableBeanFactory,
			FhirContext theFhirContext,
			JpaStorageSettings theStorageSettings) {

		LocalContainerEntityManagerFactoryBean retVal =
				new HapiFhirLocalContainerEntityManagerFactoryBean(myConfigurableListableBeanFactory);

		configureEntityManagerFactory(retVal, theFhirContext, theStorageSettings);

		PartitionSettings partitionSettings = myConfigurableListableBeanFactory.getBean(PartitionSettings.class);
		if (partitionSettings.isDatabasePartitionMode()) {
			Properties properties = new Properties();
			properties.put(JpaConstants.HAPI_DATABASE_PARTITION_MODE, Boolean.toString(true));
			// Despite the fast that the name make it sound purely like a setter, the method
			// below just merges this property in, so it won't get overwritten later or
			// overwrite other properties
			retVal.setJpaProperties(properties);
		}

		return retVal;
	}

	public static void configureEntityManagerFactory(
			LocalContainerEntityManagerFactoryBean theFactory,
			FhirContext theFhirContext,
			JpaStorageSettings theStorageSettings) {
		theFactory.setJpaDialect(new HapiFhirHibernateJpaDialect(theFhirContext.getLocalizer()));
		theFactory.setPackagesToScan("ca.uhn.fhir.jpa.model.entity", "ca.uhn.fhir.jpa.entity");
		theFactory.setPersistenceProvider(new MyHibernatePersistenceProvider(theStorageSettings));
	}

	private static class MyHibernatePersistenceProvider extends HibernatePersistenceProvider {

		private final JpaStorageSettings myStorageSettings;
		private boolean myDatabasePartitionMode;

		public MyHibernatePersistenceProvider(JpaStorageSettings theStorageSettings) {
			myStorageSettings = theStorageSettings;
		}

		/**
		 * @see MyEntityManagerFactoryBuilderImpl for an explanation of why we do this
		 */
		@Override
		protected EntityManagerFactoryBuilder getEntityManagerFactoryBuilder(
				PersistenceUnitInfo info, Map<?, ?> theIntegration) {

			String databasePartitionModeString = (String) theIntegration.get(JpaConstants.HAPI_DATABASE_PARTITION_MODE);
			databasePartitionModeString =
					Objects.toString(databasePartitionModeString, JpaConstants.HAPI_DATABASE_PARTITION_MODE_DEFAULT);
			myDatabasePartitionMode = Boolean.parseBoolean(databasePartitionModeString);

			return new MyEntityManagerFactoryBuilderImpl(info, theIntegration);
		}

		protected boolean isDatabasePartitionMode() {
			return myDatabasePartitionMode;
		}

		/**
		 * This class extends the default hibernate EntityManagerFactoryBuilder in order to
		 * register a custom service (the {@link ISequenceValueMassager}), which is used in
		 * {@link ca.uhn.fhir.jpa.model.dialect.HapiSequenceStyleGenerator}.
		 * <p>
		 * In Hibernate 5 we didn't need to do this, since we could just register
		 * the service with Spring and Hibernate would ask Spring for it. This no longer
		 * seems to work in Hibernate 6, so we now have to manually register it.
		 */
		private class MyEntityManagerFactoryBuilderImpl extends EntityManagerFactoryBuilderImpl {

			@SuppressWarnings({"unchecked"})
			private MyEntityManagerFactoryBuilderImpl(PersistenceUnitInfo theInfo, Map<?, ?> theIntegration) {
				super(new PersistenceUnitInfoDescriptor(theInfo), (Map<String, Object>) theIntegration);
			}

			@Override
			protected StandardServiceRegistryBuilder getStandardServiceRegistryBuilder(
					BootstrapServiceRegistry theBootstrapServiceRegistry) {
				HapiHibernateDialectSettingsService service = new HapiHibernateDialectSettingsService();
				service.setDatabasePartitionMode(isDatabasePartitionMode());

				StandardServiceRegistryBuilder retVal =
						super.getStandardServiceRegistryBuilder(theBootstrapServiceRegistry);
				ISequenceValueMassager sequenceValueMassager =
						ReflectionUtil.newInstance(myStorageSettings.getSequenceValueMassagerClass());
				retVal.addService(ISequenceValueMassager.class, sequenceValueMassager);
				retVal.addService(HapiHibernateDialectSettingsService.class, service);
				return retVal;
			}
		}
	}
}
