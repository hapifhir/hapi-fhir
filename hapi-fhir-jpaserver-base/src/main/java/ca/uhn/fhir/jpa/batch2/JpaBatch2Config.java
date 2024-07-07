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
package ca.uhn.fhir.jpa.batch2;

import ca.uhn.fhir.batch2.api.IJobPartitionProvider;
import ca.uhn.fhir.batch2.api.IJobPersistence;
import ca.uhn.fhir.batch2.config.BaseBatch2Config;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.jpa.bulk.export.job.BulkExportJobConfig;
import ca.uhn.fhir.jpa.dao.data.IBatch2JobInstanceRepository;
import ca.uhn.fhir.jpa.dao.data.IBatch2WorkChunkMetadataViewRepository;
import ca.uhn.fhir.jpa.dao.data.IBatch2WorkChunkRepository;
import ca.uhn.fhir.jpa.dao.tx.IHapiTransactionService;
import ca.uhn.fhir.jpa.partition.IPartitionLookupSvc;
import ca.uhn.fhir.jpa.partition.IRequestPartitionHelperSvc;
import jakarta.persistence.EntityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({BulkExportJobConfig.class})
public class JpaBatch2Config extends BaseBatch2Config {

	@Bean
	public IJobPersistence batch2JobInstancePersister(
			IBatch2JobInstanceRepository theJobInstanceRepository,
			IBatch2WorkChunkRepository theWorkChunkRepository,
			IBatch2WorkChunkMetadataViewRepository theWorkChunkMetadataViewRepo,
			IHapiTransactionService theTransactionService,
			EntityManager theEntityManager,
			IInterceptorBroadcaster theInterceptorBroadcaster) {
		return new JpaJobPersistenceImpl(
				theJobInstanceRepository,
				theWorkChunkRepository,
				theWorkChunkMetadataViewRepo,
				theTransactionService,
				theEntityManager,
				theInterceptorBroadcaster);
	}

	@Bean
	public IJobPartitionProvider jobPartitionProvider(
			IRequestPartitionHelperSvc theRequestPartitionHelperSvc, IPartitionLookupSvc thePartitionLookupSvc) {
		return new JpaJobPartitionProvider(theRequestPartitionHelperSvc, thePartitionLookupSvc);
	}
}
