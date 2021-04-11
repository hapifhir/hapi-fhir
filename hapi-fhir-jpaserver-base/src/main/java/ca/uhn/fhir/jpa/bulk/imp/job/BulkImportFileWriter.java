package ca.uhn.fhir.jpa.bulk.imp.job;

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
