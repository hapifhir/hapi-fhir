/*-
 * #%L
 * hapi-fhir-storage-batch2-jobs
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
package ca.uhn.fhir.batch2.jobs.reindex;

import ca.uhn.fhir.batch2.api.IJobCoordinator;
import ca.uhn.fhir.batch2.api.IJobPartitionProvider;
import ca.uhn.fhir.batch2.jobs.reindex.svcs.ReindexJobService;
import ca.uhn.fhir.batch2.jobs.reindex.v1.ReindexV1Config;
import ca.uhn.fhir.batch2.jobs.reindex.v2.ReindexV2Config;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirSystemDao;
import ca.uhn.fhir.jpa.api.svc.IIdHelperService;
import ca.uhn.fhir.jpa.dao.tx.HapiTransactionService;
import ca.uhn.fhir.rest.api.server.storage.IResourcePersistentId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({ReindexV1Config.class, ReindexV2Config.class})
public class ReindexAppCtx {

	@Autowired
	private HapiTransactionService myHapiTransactionService;

	@Autowired
	private IFhirSystemDao<?, ?> mySystemDao;

	@Autowired
	private DaoRegistry myRegistry;

	@Autowired
	private IIdHelperService<IResourcePersistentId<?>> myIdHelperService;

	/* Shared services */

	@Bean
	public ReindexProvider reindexProvider(
			FhirContext theFhirContext,
			IJobCoordinator theJobCoordinator,
			IJobPartitionProvider theJobPartitionHandler) {
		return new ReindexProvider(theFhirContext, theJobCoordinator, theJobPartitionHandler);
	}

	@Bean
	public ReindexJobService jobService() {
		return new ReindexJobService(myRegistry);
	}
}
