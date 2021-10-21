package ca.uhn.fhir.jpa.batch;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2021 Smile CDR, Inc.
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

import ca.uhn.fhir.jpa.batch.mdm.job.MdmClearJobConfig;
import ca.uhn.fhir.jpa.bulk.export.job.BulkExportJobConfig;
import ca.uhn.fhir.jpa.bulk.imprt.job.BulkImportJobConfig;
import ca.uhn.fhir.jpa.delete.job.DeleteExpungeJobConfig;
import ca.uhn.fhir.jpa.reindex.job.ReindexEverythingJobConfig;
import ca.uhn.fhir.jpa.reindex.job.ReindexJobConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
//When you define a new batch job, add it here.
@Import({
	CommonBatchJobConfig.class,
	BulkExportJobConfig.class,
	BulkImportJobConfig.class,
	DeleteExpungeJobConfig.class,
	ReindexJobConfig.class,
	ReindexEverythingJobConfig.class,
	MdmClearJobConfig.class
})
public class BatchJobsConfig {
}
