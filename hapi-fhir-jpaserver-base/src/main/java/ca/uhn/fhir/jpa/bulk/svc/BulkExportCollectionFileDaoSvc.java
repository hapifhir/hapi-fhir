package ca.uhn.fhir.jpa.bulk.svc;

import ca.uhn.fhir.jpa.dao.data.IBulkExportCollectionFileDao;
import ca.uhn.fhir.jpa.entity.BulkExportCollectionFileEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class BulkExportCollectionFileDaoSvc {

	@Autowired
	private IBulkExportCollectionFileDao myBulkExportCollectionFileDao;

	@Transactional
	public void save(BulkExportCollectionFileEntity theBulkExportCollectionEntity) {
		myBulkExportCollectionFileDao.saveAndFlush(theBulkExportCollectionEntity);
	}

}
