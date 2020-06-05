package ca.uhn.fhir.jpa.bulk.batch;

import ca.uhn.fhir.jpa.dao.data.IBulkExportCollectionFileDao;
import ca.uhn.fhir.jpa.entity.BulkExportCollectionFileEntity;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class BulkExportCollectionFileDaoSvc {
	private static final Logger ourLog = getLogger(BulkExportCollectionFileDaoSvc.class);

	@Autowired
	private IBulkExportCollectionFileDao myBulkExportCollectionFileDao;

	@Transactional
	public void save(BulkExportCollectionFileEntity theBulkExportCollectionEntity) {
		myBulkExportCollectionFileDao.saveAndFlush(theBulkExportCollectionEntity);
	}

}
