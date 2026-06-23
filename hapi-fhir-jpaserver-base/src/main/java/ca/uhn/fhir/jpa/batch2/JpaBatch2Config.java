/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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

import ca.uhn.fhir.batch2.api.IJobPersistence;
import ca.uhn.fhir.batch2.config.BaseBatch2Config;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.jpa.batch2.jobs.term.custom.ImportCustomTerminologyJobAppCtx;
import ca.uhn.fhir.jpa.batch2.jobs.term.icd.ImportIcdJobAppCtx;
import ca.uhn.fhir.jpa.batch2.jobs.term.loinc.ImportLoincJobAppCtx;
import ca.uhn.fhir.jpa.batch2.jobs.term.snomedct.ImportSnomedCtJobAppCtx;
import ca.uhn.fhir.jpa.batch2.jobs.term.valueset.PreExpandValueSetJobAppCtx;
import ca.uhn.fhir.jpa.bulk.export.job.BulkExportJobConfig;
import ca.uhn.fhir.jpa.dao.data.IBatch2AttachmentChunkRepository;
import ca.uhn.fhir.jpa.dao.data.IBatch2AttachmentRepository;
import ca.uhn.fhir.jpa.dao.data.IBatch2JobInstanceRepository;
import ca.uhn.fhir.jpa.dao.data.IBatch2WorkChunkMetadataViewRepository;
import ca.uhn.fhir.jpa.dao.data.IBatch2WorkChunkRepository;
import ca.uhn.fhir.jpa.dao.tx.IHapiTransactionService;
import jakarta.persistence.EntityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
	BulkExportJobConfig.class,
	ImportLoincJobAppCtx.class,
	ImportSnomedCtJobAppCtx.class,
	ImportIcdJobAppCtx.class,
	ImportCustomTerminologyJobAppCtx.class,
	PreExpandValueSetJobAppCtx.class
})
public class JpaBatch2Config extends BaseBatch2Config {

	@Bean
	public IJobPersistence batch2JobInstancePersister(
			IBatch2AttachmentRepository theAttachmentRepository,
			IBatch2JobInstanceRepository theJobInstanceRepository,
			IBatch2WorkChunkRepository theWorkChunkRepository,
			IBatch2WorkChunkMetadataViewRepository theWorkChunkMetadataViewRepo,
			IHapiTransactionService theTransactionService,
			EntityManager theEntityManager,
			IInterceptorBroadcaster theInterceptorBroadcaster,
			IBatch2AttachmentChunkRepository theAttachmentChunkRepository) {
		return new JpaJobPersistenceImpl(
				theAttachmentRepository,
				theJobInstanceRepository,
				theWorkChunkRepository,
				theWorkChunkMetadataViewRepo,
				theTransactionService,
				theEntityManager,
				theInterceptorBroadcaster,
				theAttachmentChunkRepository);
	}
}
