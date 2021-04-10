package ca.uhn.fhir.jpa.bulk.imp.job;

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
import ca.uhn.fhir.jpa.bulk.imp.api.IBulkDataImportSvc;
import org.slf4j.Logger;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

import static org.slf4j.LoggerFactory.getLogger;

public class BulkImportPartitioner implements Partitioner {
	public static final String FILE_INDEX = "fileIndex";
	private static final Logger ourLog = getLogger(BulkImportPartitioner.class);
	@Value("#{jobExecutionContext['jobUUID']}")
	private String myJobUUID;

	@Autowired
	private IBulkDataImportSvc myBulkDataImportSvc;

	@Nonnull
	@Override
	public Map<String, ExecutionContext> partition(int gridSize) {
		Map<String, ExecutionContext> retVal = new HashMap<>();

		int fileCount = myBulkDataImportSvc.getFileCount(myJobUUID);

		for (int i = 0; i < fileCount; i++) {

			ExecutionContext context = new ExecutionContext();
			context.putString(BulkExportJobConfig.JOB_UUID_PARAMETER, myJobUUID);
			context.putInt(FILE_INDEX, i);

			String key = "FILE" + i;
			retVal.put(key, context);
		}

		return retVal;
	}


}
