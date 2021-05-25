package ca.uhn.fhir.jpa.delete.job;

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

import ca.uhn.fhir.jpa.bulk.export.job.BulkExportJobConfig;
import ca.uhn.fhir.jpa.bulk.imprt.api.IBulkDataImportSvc;
import ca.uhn.fhir.jpa.bulk.imprt.model.BulkImportJobJson;
import org.slf4j.Logger;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

import static org.slf4j.LoggerFactory.getLogger;

public class DeleteExpungePartitioner implements Partitioner {
	public static final String FILE_INDEX = "fileIndex";
	public static final String ROW_PROCESSING_MODE = "rowProcessingMode";

	private static final Logger ourLog = getLogger(DeleteExpungePartitioner.class);

	@Value("#{jobParameters['" + DeleteExpungeJobConfig.JOB_UUID_PARAMETER + "']}")
	private String myJobUUID;

	@Autowired
	private IBulkDataImportSvc myBulkDataImportSvc;

	@Nonnull
	@Override
	public Map<String, ExecutionContext> partition(int gridSize) {
		Map<String, ExecutionContext> retVal = new HashMap<>();

		BulkImportJobJson job = myBulkDataImportSvc.fetchJob(myJobUUID);

		for (int i = 0; i < job.getFileCount(); i++) {

			ExecutionContext context = new ExecutionContext();
			context.putString(DeleteExpungeJobConfig.JOB_UUID_PARAMETER, myJobUUID);
			context.putInt(FILE_INDEX, i);
			context.put(ROW_PROCESSING_MODE, job.getProcessingMode());

			String key = "FILE" + i;
			retVal.put(key, context);
		}

		return retVal;
	}


}
