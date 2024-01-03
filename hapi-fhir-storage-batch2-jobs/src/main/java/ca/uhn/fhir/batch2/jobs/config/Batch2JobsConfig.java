/*-
 * #%L
 * hapi-fhir-storage-batch2-jobs
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
package ca.uhn.fhir.batch2.jobs.config;

import ca.uhn.fhir.batch2.jobs.export.BulkExportAppCtx;
import ca.uhn.fhir.batch2.jobs.expunge.DeleteExpungeAppCtx;
import ca.uhn.fhir.batch2.jobs.importpull.BulkImportPullConfig;
import ca.uhn.fhir.batch2.jobs.imprt.BulkImportAppCtx;
import ca.uhn.fhir.batch2.jobs.reindex.ReindexAppCtx;
import ca.uhn.fhir.batch2.jobs.termcodesystem.TermCodeSystemJobConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

// When you define a new batch job, add it here.
@Configuration
@Import({
	BatchCommonCtx.class,
	BulkImportAppCtx.class,
	ReindexAppCtx.class,
	DeleteExpungeAppCtx.class,
	BulkExportAppCtx.class,
	TermCodeSystemJobConfig.class,
	BulkImportPullConfig.class,
})
public class Batch2JobsConfig {}
