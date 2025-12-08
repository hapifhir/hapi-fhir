/*-
 * #%L
 * HAPI-FHIR Storage Batch2 Jobs
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

import ca.uhn.fhir.batch2.jobs.bulkmodify.reindex.ReindexProvider;
import ca.uhn.fhir.batch2.jobs.bulkmodify.reindex.ReindexV3JobAppCtx;
import ca.uhn.fhir.batch2.jobs.reindex.svcs.ReindexJobService;
import ca.uhn.fhir.batch2.jobs.reindex.v2.ReindexV2Config;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({ReindexV2Config.class, ReindexV3JobAppCtx.class})
public class ReindexAppCtx {

	@Autowired
	private DaoRegistry myRegistry;

	/* Shared services */

	@Bean
	public ReindexProvider reindexProvider() {
		return new ReindexProvider();
	}

	@Bean
	public ReindexJobService jobService() {
		return new ReindexJobService(myRegistry);
	}
}
