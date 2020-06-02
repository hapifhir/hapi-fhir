package ca.uhn.fhir.jpa.bulk.batch;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.dao.IResultIterator;
import ca.uhn.fhir.jpa.dao.ISearchBuilder;
import ca.uhn.fhir.jpa.dao.SearchBuilderFactory;
import ca.uhn.fhir.jpa.dao.data.IBulkExportJobDao;
import ca.uhn.fhir.jpa.entity.BulkExportCollectionEntity;
import ca.uhn.fhir.jpa.entity.BulkExportJobEntity;
import ca.uhn.fhir.jpa.model.search.SearchRuntimeDetails;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import ca.uhn.fhir.rest.param.DateRangeParam;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.support.AbstractItemCountingItemStreamItemReader;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class BulkItemReader extends AbstractItemCountingItemStreamItemReader<ResourcePersistentId> {
	private static final Logger ourLog = LoggerFactory.getLogger(BulkItemReader.class);


	@Autowired
	private IBulkExportJobDao myBulkExportJobDao;

	@Autowired
	private DaoRegistry myDaoRegistry;

	@Autowired
	private FhirContext myContext;

	@Autowired
	private SearchBuilderFactory mySearchBuilderFactory;

	private BulkExportJobEntity myJobEntity;

	private String myJobUUID;

	private IResultIterator myResultIterator;


	@Override
	protected ResourcePersistentId doRead() throws Exception {
		if (myJobEntity == null) {
			loadData();
		}
		if (myResultIterator.hasNext()) {
			return myResultIterator.next();
		} else {
			return null;
		}
	}


	private void loadData() {
		Optional<BulkExportJobEntity> jobOpt = myBulkExportJobDao.findByJobId(myJobUUID);
		if (!jobOpt.isPresent()) {
			ourLog.info("Job appears to be deleted");
			return;
		}
		myJobEntity = jobOpt.get();
		ourLog.info("Bulk export starting generation for batch export job: {}", myJobEntity);

		for (BulkExportCollectionEntity nextCollection : myJobEntity.getCollections()) {
			String nextType = nextCollection.getResourceType();
			IFhirResourceDao dao = myDaoRegistry.getResourceDao(nextType);

			ourLog.info("Bulk export assembling export of type {} for job {}", nextType, myJobUUID);

			Class<? extends IBaseResource> nextTypeClass = myContext.getResourceDefinition(nextType).getImplementingClass();
			ISearchBuilder sb = mySearchBuilderFactory.newSearchBuilder(dao, nextType, nextTypeClass);

			SearchParameterMap map = new SearchParameterMap();
			map.setLoadSynchronous(true);
			if (myJobEntity.getSince() != null) {
				map.setLastUpdated(new DateRangeParam(myJobEntity.getSince(), null));
			}

			myResultIterator = sb.createQuery(map, new SearchRuntimeDetails(null, myJobUUID), null, RequestPartitionId.allPartitions());
		}


		ourLog.info("Bulk export completed job in");
	}


	@Override
	protected void doOpen() throws Exception {

	}

	@Override
	protected void doClose() throws Exception {

	}

	public void setJobUUID(String theUUID) {
		this.myJobUUID = theUUID;
	}
}
