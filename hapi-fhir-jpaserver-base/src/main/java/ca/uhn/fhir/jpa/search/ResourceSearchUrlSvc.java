package ca.uhn.fhir.jpa.search;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.jpa.dao.data.IResourceSearchUrlDao;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.jpa.model.entity.ResourceSearchUrlEntity;
import ca.uhn.fhir.jpa.searchparam.MatchUrlService;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
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
public class ResourceSearchUrlSvc {
	private static final Logger ourLog = LoggerFactory.getLogger(ResourceSearchUrlSvc.class);

	@Autowired
	private IResourceSearchUrlDao myResourceSearchUrlDao;

	@Autowired
	private PlatformTransactionManager myTxManager;

	@Autowired
	private MatchUrlService myMatchUrlService;

	@Autowired
	private FhirContext myFhirContext;

	public void deleteEntriesOlderThan(Date theCutoffDate) {
		TransactionTemplate tt = new TransactionTemplate(myTxManager);

		tt.execute(theStatus -> {
				ourLog.info("About to delete SearchUrl which are older than {}", theCutoffDate);
				int deletedCount = myResourceSearchUrlDao.deleteAllWhereCreatedBefore(theCutoffDate);
				ourLog.info("Deleted {} SearchUrls", deletedCount);
				return null;
			}
		);

	}

	public void deleteByResId(long theResId){
		TransactionTemplate tt = new TransactionTemplate(myTxManager);

		tt.execute(theStatus -> {
			myResourceSearchUrlDao.deleteByResId(theResId);
			return null;
		});
	}

	public void storeMatchUrlForResource(String theResourceName, String theMatchUrl, JpaPid theResourcePersistentId) {
		String canonicalizedUrlForStorage = createCanonicalizedUrlForStorage(theResourceName, theMatchUrl);

		ResourceSearchUrlEntity searchUrlEntity = ResourceSearchUrlEntity.from(canonicalizedUrlForStorage, (Long)theResourcePersistentId.getId());
		myResourceSearchUrlDao.save(searchUrlEntity);

	}

	private String createCanonicalizedUrlForStorage(String theResourceName, String theMatchUrl){

		RuntimeResourceDefinition resourceDef = myFhirContext.getResourceDefinition(theResourceName);
		SearchParameterMap matchUrlSearchParameterMap = myMatchUrlService.translateMatchUrl(theMatchUrl, resourceDef);

		String canonicalizedMatchUrl = matchUrlSearchParameterMap.toNormalizedQueryString(myFhirContext);

		return theResourceName + canonicalizedMatchUrl;
	}


}
