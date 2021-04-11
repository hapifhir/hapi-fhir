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

import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirSystemDao;
import ca.uhn.fhir.jpa.bulk.export.job.BulkExportJobConfig;
import ca.uhn.fhir.jpa.bulk.imp.model.JobFileRowProcessingModeEnum;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

public class BulkImportFileWriter implements ItemWriter<IBaseResource> {

	private static final Logger ourLog = LoggerFactory.getLogger(BulkImportFileWriter.class);
	@Value("#{stepExecutionContext['" + BulkExportJobConfig.JOB_UUID_PARAMETER + "']}")
	private String myJobUuid;
	@Value("#{stepExecutionContext['" + BulkImportPartitioner.FILE_INDEX + "']}")
	private int myFileIndex;
	@Value("#{stepExecutionContext['" + BulkImportPartitioner.ROW_PROCESSING_MODE + "']}")
	private JobFileRowProcessingModeEnum myRowProcessingMode;
	@Autowired
	private DaoRegistry myDaoRegistry;

	@SuppressWarnings({"SwitchStatementWithTooFewBranches", "rawtypes", "unchecked"})
	@Override
	public void write(List<? extends IBaseResource> theItemLists) throws Exception {
		ourLog.info("Beginning bulk import write {} chunks Job[{}] FileIndex[{}]", theItemLists.size(), myJobUuid, myFileIndex);

		for (IBaseResource nextItem : theItemLists) {

			// Yeah this is a lame switch - We'll add more later I swear
			switch (myRowProcessingMode) {
				default:
				case FHIR_TRANSACTION:
					IFhirSystemDao systemDao = myDaoRegistry.getSystemDao();
					systemDao.transactionNested(null, nextItem);
					break;
			}

		}

	}

}
