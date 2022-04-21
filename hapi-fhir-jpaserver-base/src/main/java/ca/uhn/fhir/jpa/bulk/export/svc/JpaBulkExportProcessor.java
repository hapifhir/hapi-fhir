package ca.uhn.fhir.jpa.bulk.export.svc;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.model.BulkExportJobInfo;
import ca.uhn.fhir.jpa.bulk.export.api.IBulkExportProcessor;
import ca.uhn.fhir.jpa.bulk.export.model.BulkExportJobStatusEnum;
import ca.uhn.fhir.jpa.bulk.export.model.ExportPIDIteratorParameters;
import ca.uhn.fhir.jpa.dao.IResultIterator;
import ca.uhn.fhir.jpa.dao.ISearchBuilder;
import ca.uhn.fhir.jpa.dao.SearchBuilderFactory;
import ca.uhn.fhir.jpa.dao.data.IBulkExportCollectionDao;
import ca.uhn.fhir.jpa.dao.data.IBulkExportJobDao;
import ca.uhn.fhir.jpa.entity.BulkExportCollectionEntity;
import ca.uhn.fhir.jpa.entity.BulkExportCollectionFileEntity;
import ca.uhn.fhir.jpa.entity.BulkExportJobEntity;
import ca.uhn.fhir.jpa.model.search.SearchRuntimeDetails;
import ca.uhn.fhir.jpa.searchparam.MatchUrlService;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import org.hl7.fhir.instance.model.api.IIdType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class JpaBulkExportProcessor implements IBulkExportProcessor {
	private static final Logger ourLog = LoggerFactory.getLogger(JpaBulkExportProcessor.class);

	@Autowired
	private FhirContext myContext;

	@Autowired
	private MatchUrlService myMatchUrlService;

	@Autowired
	private DaoRegistry myDaoRegistry;

	@Autowired
	protected SearchBuilderFactory mySearchBuilderFactory;

	@Autowired
	private BulkExportDaoSvc myBulkExportDaoSvc;

	@Autowired
	private IBulkExportJobDao myBulkExportJobDao;

	@Autowired
	private IBulkExportCollectionDao myCollectionDao;

	@Override
	public Iterator<ResourcePersistentId> getResourcePidIterator(ExportPIDIteratorParameters theParams) {
		String resourceType = theParams.getResourceType();
		String jobId = theParams.getJobId();
		RuntimeResourceDefinition def = myContext.getResourceDefinition(resourceType);

		List<SearchParameterMap> maps = null;
		if (theParams.getFilters() != null && !theParams.getFilters().isEmpty()) {
			maps = new ArrayList<>();
			for (String filter : theParams.getFilters()) {
				if (filter.startsWith(resourceType)) {
					SearchParameterMap spm = buildSearchParameterMapForTypeFilter(filter, def, theParams.getStartDate());
					maps.add(spm);
				}
			}
		}

		if (maps == null) {
			SearchParameterMap defaultMap = new SearchParameterMap();
			enhanceSearchParameterMapWithCommonParameters(defaultMap, theParams.getStartDate());
			maps = Collections.singletonList(defaultMap);
		}

		IFhirResourceDao<?> dao = myDaoRegistry.getResourceDao(resourceType);
		ISearchBuilder searchBuilder = mySearchBuilderFactory.newSearchBuilder(
			dao,
			resourceType,
			def.getImplementingClass()
		);

		Set<ResourcePersistentId> pids = new HashSet<>();
		for (SearchParameterMap map : maps) {
			IResultIterator resultIterator = searchBuilder.createQuery(map,
				new SearchRuntimeDetails(null, jobId),
				null,
				RequestPartitionId.allPartitions());
			while (resultIterator.hasNext()) {
				pids.add(resultIterator.next());
			}
		}

		return pids.iterator();
	}

	@Override
	public BulkExportJobInfo getJobInfo(String theJobId) {
		Optional<BulkExportJobEntity> jobOp = myBulkExportJobDao.findByJobId(theJobId);

		if (jobOp.isPresent()) {
			BulkExportJobEntity jobEntity = jobOp.get();
			BulkExportJobInfo jobInfo = new BulkExportJobInfo();

			jobInfo.setJobId(jobEntity.getJobId());
			Set<String> resourceTypes = new HashSet<>();
			for (BulkExportCollectionEntity collection : jobEntity.getCollections()) {
				resourceTypes.add(collection.getResourceType());
			}
			jobInfo.setResourceTypes(resourceTypes);
			return jobInfo;
		}
		else {
			return null;
		}
	}

	private SearchParameterMap buildSearchParameterMapForTypeFilter(String theFilter, RuntimeResourceDefinition theDef, Date theSinceDate) {
		SearchParameterMap searchParameterMap = myMatchUrlService.translateMatchUrl(theFilter, theDef);
		enhanceSearchParameterMapWithCommonParameters(searchParameterMap, theSinceDate);
		return searchParameterMap;
	}

	private void enhanceSearchParameterMapWithCommonParameters(SearchParameterMap map, Date theSinceDate) {
		map.setLoadSynchronous(true);
		if (theSinceDate != null) {
			map.setLastUpdated(new DateRangeParam(theSinceDate, null));
		}
	}

	@Override
	public void setJobStatus(String theJobId, BulkExportJobStatusEnum theStatus) {
		myBulkExportDaoSvc.setJobToStatus(theJobId, theStatus, null);
	}

	@Override
	public BulkExportJobStatusEnum getJobStatus(String theJobId) {
		Optional<BulkExportJobEntity> jobOp = myBulkExportJobDao.findByJobId(theJobId);

		if (jobOp.isPresent()) {
			return jobOp.get().getStatus();
		}
		else {
			String msg = "Invalid id. No such job : " + theJobId;
			ourLog.error(msg);
			throw new ResourceNotFoundException(theJobId);
		}
	}

	@Override
	public void addFileToCollection(String theJobId, String theResourceType, IIdType theBinaryId) {
		Map<Long, String> collectionMap = myBulkExportDaoSvc.getBulkJobCollectionIdToResourceTypeMap(theJobId);

		Long collectionId = null;
		for (Map.Entry<Long, String> entrySet : collectionMap.entrySet()) {
			if (entrySet.getValue().equals(theResourceType)) {
				collectionId = entrySet.getKey();
				break;
			}
		}

		if (collectionId == null) {
			String msg = "No matching collection for resource type "
				+ theResourceType
				+ " for job "
				+ theJobId;
			ourLog.error(msg);

			throw new InvalidRequestException(msg);
		}

		BulkExportCollectionFileEntity file = new BulkExportCollectionFileEntity();
		file.setResource(theBinaryId.getIdPart());

		myBulkExportDaoSvc.addFileToCollectionWithId(collectionId, file);
	}
}
