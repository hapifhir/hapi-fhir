/*-
 * #%L
 * HAPI FHIR JPA Server Test Utilities
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
package ca.uhn.fhir.jpa.test.config;

import ca.uhn.fhir.batch2.api.IJobCoordinator;
import ca.uhn.fhir.batch2.api.IJobMaintenanceService;
import ca.uhn.fhir.batch2.api.IJobPersistence;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.config.ThreadPoolFactoryConfig;
import ca.uhn.fhir.jpa.binary.api.IBinaryStorageSvc;
import ca.uhn.fhir.jpa.binstore.MemoryBinaryStorageSvcImpl;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.searchparam.submit.config.SearchParamSubmitterConfig;
import ca.uhn.fhir.jpa.subscription.channel.config.SubscriptionChannelConfig;
import ca.uhn.fhir.jpa.subscription.match.config.SubscriptionProcessorConfig;
import ca.uhn.fhir.jpa.subscription.match.deliver.email.IEmailSender;
import ca.uhn.fhir.jpa.subscription.match.deliver.resthook.SubscriptionDeliveringRestHookSubscriber;
import ca.uhn.fhir.jpa.model.config.SubscriptionSettings;
import ca.uhn.fhir.jpa.subscription.submit.config.SubscriptionSubmitterConfig;
import ca.uhn.fhir.jpa.term.TermCodeSystemDeleteJobSvcWithUniTestFailures;
import ca.uhn.fhir.jpa.term.api.ITermCodeSystemDeleteJobSvc;
import ca.uhn.fhir.jpa.test.Batch2JobHelper;
import ca.uhn.fhir.jpa.test.util.StoppableSubscriptionDeliveringRestHookSubscriber;
import ca.uhn.fhir.jpa.test.util.SubscriptionTestUtil;
import ca.uhn.fhir.jpa.util.LoggingEmailSender;
import ca.uhn.fhir.system.HapiTestSystemProperties;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.jpa.JpaTransactionManager;

@Configuration
@Import({
	SubscriptionSubmitterConfig.class,
	SubscriptionProcessorConfig.class,
	SubscriptionChannelConfig.class,
	SearchParamSubmitterConfig.class,
	ThreadPoolFactoryConfig.class
})
public class TestJPAConfig {
	@Bean
	public JpaStorageSettings storageSettings() {
		JpaStorageSettings retVal = new JpaStorageSettings();

		if (HapiTestSystemProperties.isMassIngestionModeEnabled()) {
			retVal.setMassIngestionMode(true);
		}

		return retVal;
	}

	@Bean
	public SubscriptionSettings subscriptionSettings() {
		return new SubscriptionSettings();
	}

	@Bean
	public PartitionSettings partitionSettings() {
		return new PartitionSettings();
	}

	@Bean
	@Primary
	public JpaTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
		JpaTransactionManager retVal = new JpaTransactionManager();
		retVal.setEntityManagerFactory(entityManagerFactory);
		return retVal;
	}

	@Lazy
	@Bean
	public SubscriptionTestUtil subscriptionTestUtil() {
		return new SubscriptionTestUtil();
	}

	@Bean
	@Primary
	public SubscriptionDeliveringRestHookSubscriber stoppableSubscriptionDeliveringRestHookSubscriber() {
		return new StoppableSubscriptionDeliveringRestHookSubscriber();
	}

	@Bean
	public Batch2JobHelper batch2JobHelper(IJobMaintenanceService theJobMaintenanceService, IJobCoordinator theJobCoordinator, IJobPersistence theJobPersistence) {
		return new Batch2JobHelper(theJobMaintenanceService, theJobCoordinator, theJobPersistence);
	}

	/**
	 * Replace the HAPI FHIR bean of this type with a version that extends the built-in one
	 * and adds manual failures for unit tests
	 */
	@Bean
	@Primary
	public ITermCodeSystemDeleteJobSvc termCodeSystemService() {
		return new TermCodeSystemDeleteJobSvcWithUniTestFailures();
	}

	@Bean
	@Lazy
	public IBinaryStorageSvc binaryStorage() {
		return new MemoryBinaryStorageSvcImpl();
	}

	@Bean
	public IEmailSender emailSender(){
		return new LoggingEmailSender();
	}
}
