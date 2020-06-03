package ca.uhn.fhir.jpa.bulk.batch;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.dao.IResultIterator;
import ca.uhn.fhir.jpa.dao.ISearchBuilder;
import ca.uhn.fhir.jpa.dao.SearchBuilderFactory;
import ca.uhn.fhir.jpa.dao.data.IBulkExportJobDao;
import ca.uhn.fhir.jpa.entity.BulkExportJobEntity;
import ca.uhn.fhir.jpa.model.search.SearchRuntimeDetails;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import ca.uhn.fhir.rest.param.DateRangeParam;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.support.AbstractItemCountingItemStreamItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
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

	@Value("#{stepExecutionContext['resourceType']}")
	private String myResourceType;


	Iterator<ResourcePersistentId> myPidIterator;


	protected ResourcePersistentId doRead() throws Exception {
		if (myPidIterator == null) {
			loadResourcePids();
		}
		if (myPidIterator.hasNext()) {
			return myPidIterator.next();
		} else {
			return null;
		}
	}

	@Override
	protected void doOpen() throws Exception {

	}

	@Override
	protected void doClose() throws Exception {

	}


	private void loadResourcePids() {
		Optional<BulkExportJobEntity> jobOpt = myBulkExportJobDao.findByJobId(myJobUUID);
		if (!jobOpt.isPresent()) {
			ourLog.info("Job appears to be deleted");
			return;
		}
		myJobEntity = jobOpt.get();
		ourLog.info("Bulk export starting generation for batch export job: {}", myJobEntity);

		IFhirResourceDao dao = myDaoRegistry.getResourceDao(myResourceType);

		ourLog.info("Bulk export assembling export of type {} for job {}", myResourceType, myJobUUID);

		Class<? extends IBaseResource> nextTypeClass = myContext.getResourceDefinition(myResourceType).getImplementingClass();
		ISearchBuilder sb = mySearchBuilderFactory.newSearchBuilder(dao, myResourceType, nextTypeClass);

		SearchParameterMap map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		if (myJobEntity.getSince() != null) {
			map.setLastUpdated(new DateRangeParam(myJobEntity.getSince(), null));
		}

		IResultIterator myResultIterator = sb.createQuery(map, new SearchRuntimeDetails(null, myJobUUID), null, RequestPartitionId.allPartitions());
		List<ResourcePersistentId> myReadPids = new ArrayList<>();
		while (myResultIterator.hasNext()) {
			myReadPids.add(myResultIterator.next());
		}
		myPidIterator = myReadPids.iterator();
	}

	public void setJobUUID(String theUUID) {
		this.myJobUUID = theUUID;
	}

}
