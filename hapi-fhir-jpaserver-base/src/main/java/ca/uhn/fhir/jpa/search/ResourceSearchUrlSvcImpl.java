package ca.uhn.fhir.jpa.search;

import ca.uhn.fhir.jpa.api.svc.IResourceSearchUrlSvc;
import ca.uhn.fhir.jpa.dao.data.IResourceSearchUrlDao;
import ca.uhn.fhir.jpa.model.entity.ResourceSearchUrlEntity;
import ca.uhn.fhir.rest.api.server.storage.IResourcePersistentId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Date;

@Service
public class ResourceSearchUrlSvcImpl implements IResourceSearchUrlSvc {
	private static final Logger ourLog = LoggerFactory.getLogger(ResourceSearchUrlSvcImpl.class);

	@Autowired
	private IResourceSearchUrlDao myResourceSearchUrlDao;

	@Autowired
	private PlatformTransactionManager myTxManager;

	@Override
	public void saveResourceSearchUrl(String theCanonicalizedUrlForStorage, IResourcePersistentId theResourcePersistentId) {
		ResourceSearchUrlEntity searchUrlEntity = ResourceSearchUrlEntity.from(theCanonicalizedUrlForStorage, (Long)theResourcePersistentId.getId());
		myResourceSearchUrlDao.save(searchUrlEntity);

	}

	@Override
	public void deleteEntriesOlderThan(Date theCutoffDate) {
		TransactionTemplate tt = new TransactionTemplate(myTxManager);

		tt.execute(theStatus -> {
				Slice<Long> toDelete = myResourceSearchUrlDao.findWhereCreatedBefore(theCutoffDate, PageRequest.of(0, 2000));
				ourLog.info("About to delete {} SearchUrl which are older than {}", toDelete.getNumberOfElements(), theCutoffDate);
				myResourceSearchUrlDao.deleteAllById(toDelete);

				return null;
			}
		);

	}

}
