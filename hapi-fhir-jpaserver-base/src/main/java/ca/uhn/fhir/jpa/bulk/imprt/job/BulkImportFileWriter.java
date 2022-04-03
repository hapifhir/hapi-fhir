package ca.uhn.fhir.jpa.bulk.imprt.job;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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
import ca.uhn.fhir.jpa.batch.config.BatchConstants;
import ca.uhn.fhir.jpa.bulk.imprt.model.JobFileRowProcessingModeEnum;
import ca.uhn.fhir.jpa.bulk.imprt.model.ParsedBulkImportRecord;
import ca.uhn.fhir.jpa.partition.SystemRequestDetails;
import ca.uhn.fhir.util.StopWatch;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.List;

public class BulkImportFileWriter implements ItemWriter<ParsedBulkImportRecord> {

	private static final Logger ourLog = LoggerFactory.getLogger(BulkImportFileWriter.class);
	@Value("#{stepExecutionContext['" + BatchConstants.JOB_UUID_PARAMETER + "']}")
	private String myJobUuid;
	@Value("#{stepExecutionContext['" + BulkImportPartitioner.FILE_INDEX + "']}")
	private int myFileIndex;
	@Value("#{stepExecutionContext['" + BulkImportPartitioner.ROW_PROCESSING_MODE + "']}")
	private JobFileRowProcessingModeEnum myRowProcessingMode;
	@Autowired
	private DaoRegistry myDaoRegistry;

	@SuppressWarnings({"SwitchStatementWithTooFewBranches", "rawtypes", "unchecked"})
	@Override
	public void write(List<? extends ParsedBulkImportRecord> theItemLists) throws Exception {
		assert TransactionSynchronizationManager.isActualTransactionActive();

		String offsets = "unknown";
		if (theItemLists.size() > 0) {
			offsets = theItemLists.get(0).getLineIndex() + " - " + theItemLists.get(theItemLists.size()-1).getLineIndex();
		}

		ourLog.info("Beginning bulk import write {} rows Job[{}] FileIndex[{}] Offset[{}]", theItemLists.size(), myJobUuid, myFileIndex, offsets);
		StopWatch sw = new StopWatch();

		for (ParsedBulkImportRecord nextItem : theItemLists) {

			SystemRequestDetails requestDetails = new SystemRequestDetails();
			requestDetails.setTenantId(nextItem.getTenantName());

			// Yeah this is a lame switch - We'll add more later I swear
			switch (myRowProcessingMode) {
				default:
				case FHIR_TRANSACTION:
					IFhirSystemDao systemDao = myDaoRegistry.getSystemDao();
					IBaseResource inputBundle = nextItem.getRowContent();
					systemDao.transactionNested(requestDetails, inputBundle);
					break;
			}

		}

		ourLog.info("Completed bulk import write {} rows Job[{}] FileIndex[{}] Offset[{}] in {}", theItemLists.size(), myJobUuid, myFileIndex, offsets, sw);
	}

}
