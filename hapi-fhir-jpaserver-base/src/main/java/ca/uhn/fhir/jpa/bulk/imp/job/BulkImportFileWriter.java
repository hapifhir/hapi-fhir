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
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;

public class BulkImportFileWriter implements ItemWriter<List<IBaseResource>> {

	private static final Logger ourLog = LoggerFactory.getLogger(BulkImportFileWriter.class);
	@Value("#{stepExecutionContext['" + BulkExportJobConfig.JOB_UUID_PARAMETER + "']}")
	private String myJobUuid;
	@Value("#{stepExecutionContext['" + BulkImportPartitioner.FILE_INDEX + "']}")
	private int myFileIndex;
	@Value("#{stepExecutionContext['" + BulkImportPartitioner.ROW_PROCESSING_MODE + "']}")
	private JobFileRowProcessingModeEnum myRowProcessingMode;
	@Autowired
	private DaoRegistry myDaoRegistry;
	@Autowired
	private PlatformTransactionManager myTxManager;

	@SuppressWarnings({"SwitchStatementWithTooFewBranches", "rawtypes", "unchecked"})
	@Override
	public void write(List<? extends List<IBaseResource>> theItemLists) throws Exception {
		ourLog.info("Beginning bulk import write chunk Job[{}] FileIndex[{}]", myJobUuid, myFileIndex);

		for (List<IBaseResource> nextList : theItemLists) {
			for (IBaseResource nextItem : nextList) {

				// Yeah this is a lame switch - We'll add more later I swear
				switch (myRowProcessingMode) {
					default:
					case FHIR_TRANSACTION:
						IFhirSystemDao systemDao = myDaoRegistry.getSystemDao();

						TransactionTemplate txTemplate = new TransactionTemplate(myTxManager);
						txTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_NOT_SUPPORTED);
						txTemplate.afterPropertiesSet();
						txTemplate.executeWithoutResult(tx -> {
							systemDao.transaction(null, nextItem);
						});

						break;
				}

			}
		}
	}
}
