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
import ca.uhn.fhir.jpa.bulk.imp.model.BulkImportJobFileJson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

public class BulkImportFileReader implements ItemReader<BulkImportJobFileJson> {

	@Autowired
	private IBulkDataImportSvc myBulkDataImportSvc;

	@Value("#{stepExecutionContext['" + BulkExportJobConfig.JOB_UUID_PARAMETER + "']}")
	private String myJobUuid;
	@Value("#{stepExecutionContext['" + BulkImportPartitioner.FILE_INDEX + "']}")
	private int myFileIndex;

	private boolean myDone = false;

	@Override
	public BulkImportJobFileJson read() throws Exception {
		if (myDone) {
			return null;
		} else {
			myDone = true;
		}

		BulkImportJobFileJson retVal = myBulkDataImportSvc.fetchFile(myJobUuid, myFileIndex);

		ourLog.info("Reading file index {} for job: {}", myFileIndex, myJobUuid);
		return retVal;
	}
private static final Logger ourLog = LoggerFactory.getLogger(BulkImportFileReader.class);
}
