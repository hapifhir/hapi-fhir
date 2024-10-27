/*-
 * #%L
 * HAPI FHIR JPA Server
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
package ca.uhn.fhir.jpa.config.util;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.config.HapiFhirHibernateJpaDialect;
import ca.uhn.fhir.jpa.config.HapiFhirLocalContainerEntityManagerFactoryBean;
import ca.uhn.fhir.jpa.util.ISequenceValueMassager;
import ca.uhn.fhir.util.ReflectionUtil;
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

		public MyHibernatePersistenceProvider(JpaStorageSettings theStorageSettings) {
			myStorageSettings = theStorageSettings;
		}

		/**
		 * @see MyEntityManagerFactoryBuilderImpl for an explanation of why we do this
		 */
		@Override
		protected EntityManagerFactoryBuilder getEntityManagerFactoryBuilder(
				PersistenceUnitInfo info, Map<?, ?> integration) {
			return new MyEntityManagerFactoryBuilderImpl(info, integration);
		}

		/**
		 * This class extends the default hibernate EntityManagerFactoryBuilder in order to
		 * register a custom service (the {@link ISequenceValueMassager}, which is used in
		 * {@link ca.uhn.fhir.jpa.model.dialect.HapiSequenceStyleGenerator}.
		 * <p>
		 * In Hibernate 5 we didn't need to do this, since we could just register
		 * the service with Spring and Hibernate would ask Spring for it. This no longer
		 * seems to work in Hibernate 6, so we now have to manually register it.
		 */
		private class MyEntityManagerFactoryBuilderImpl extends EntityManagerFactoryBuilderImpl {
			public MyEntityManagerFactoryBuilderImpl(PersistenceUnitInfo theInfo, Map<?, ?> theIntegration) {
				super(new PersistenceUnitInfoDescriptor(theInfo), (Map) theIntegration);
			}

			@Override
			protected StandardServiceRegistryBuilder getStandardServiceRegistryBuilder(BootstrapServiceRegistry bsr) {
				StandardServiceRegistryBuilder retVal = super.getStandardServiceRegistryBuilder(bsr);
				ISequenceValueMassager sequenceValueMassager =
						ReflectionUtil.newInstance(myStorageSettings.getSequenceValueMassagerClass());
				retVal.addService(ISequenceValueMassager.class, sequenceValueMassager);
				return retVal;
			}
		}
	}
}
