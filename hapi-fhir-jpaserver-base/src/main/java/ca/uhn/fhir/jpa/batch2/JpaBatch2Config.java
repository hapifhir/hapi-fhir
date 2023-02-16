package ca.uhn.fhir.jpa.batch2;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2023 Smile CDR, Inc.
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

import ca.uhn.fhir.batch2.api.IJobPersistence;
import ca.uhn.fhir.batch2.config.BaseBatch2Config;
import ca.uhn.fhir.batch2.coordinator.SynchronizedJobPersistenceWrapper;
import ca.uhn.fhir.jpa.bulk.export.job.BulkExportJobConfig;
import ca.uhn.fhir.jpa.dao.data.IBatch2JobInstanceRepository;
import ca.uhn.fhir.jpa.dao.data.IBatch2WorkChunkRepository;
import ca.uhn.fhir.system.HapiSystemProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManager;

@Configuration
@Import({
	BulkExportJobConfig.class
})
public class JpaBatch2Config extends BaseBatch2Config {

	@Bean
	public IJobPersistence batch2JobInstancePersister(IBatch2JobInstanceRepository theJobInstanceRepository, IBatch2WorkChunkRepository theWorkChunkRepository, PlatformTransactionManager theTransactionManager, EntityManager theEntityManager) {
		return new JpaJobPersistenceImpl(theJobInstanceRepository, theWorkChunkRepository, theTransactionManager, theEntityManager);
	}

	@Primary
	@Bean
	public IJobPersistence batch2JobInstancePersisterWrapper(IBatch2JobInstanceRepository theJobInstanceRepository, IBatch2WorkChunkRepository theWorkChunkRepository, PlatformTransactionManager theTransactionManager, EntityManager theEntityManager) {
		IJobPersistence retVal = batch2JobInstancePersister(theJobInstanceRepository, theWorkChunkRepository, theTransactionManager, theEntityManager);
		// Avoid H2 synchronization issues caused by
		// https://github.com/h2database/h2database/issues/1808
		if (HapiSystemProperties.isUnitTestModeEnabled()) {
			retVal = new SynchronizedJobPersistenceWrapper(retVal);
		}
		return retVal;
	}


}
