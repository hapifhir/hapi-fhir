package ca.uhn.fhir.jpa.bulk.job;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.batch.log.Logs;
import ca.uhn.fhir.jpa.dao.ISearchBuilder;
import ca.uhn.fhir.jpa.dao.SearchBuilderFactory;
import ca.uhn.fhir.jpa.dao.data.IBulkExportJobDao;
import ca.uhn.fhir.jpa.entity.BulkExportJobEntity;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.jpa.searchparam.MatchUrlService;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.util.UrlUtil;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public abstract class BaseBulkItemReader implements ItemReader<List<ResourcePersistentId>> {
	private static final Logger ourLog = Logs.getBatchTroubleshootingLog();

	@Value("#{stepExecutionContext['resourceType']}")
	protected String myResourceType;
	@Value("#{jobExecutionContext['" + BulkExportJobConfig.JOB_UUID_PARAMETER + "']}")
	protected String myJobUUID;
	@Value("#{jobParameters['" + BulkExportJobConfig.READ_CHUNK_PARAMETER + "']}")
	protected Long myReadChunkSize;
	@Autowired
	protected DaoRegistry myDaoRegistry;
	@Autowired
	protected FhirContext myContext;
	@Autowired
	private IBulkExportJobDao myBulkExportJobDao;
	@Autowired
	protected SearchBuilderFactory mySearchBuilderFactory;
	@Autowired
	private MatchUrlService myMatchUrlService;

	private ISearchBuilder mySearchBuilder;
	private BulkExportJobEntity myJobEntity;
	private RuntimeResourceDefinition myResourceDefinition;

	private Iterator<ResourcePersistentId> myPidIterator;

	/**
	 * Get and cache an ISearchBuilder for the given resource type this partition is responsible for.
	 */
	protected ISearchBuilder getSearchBuilderForLocalResourceType() {
		if (mySearchBuilder == null) {
			IFhirResourceDao<?> dao = myDaoRegistry.getResourceDao(myResourceType);
			RuntimeResourceDefinition def = myContext.getResourceDefinition(myResourceType);
			Class<? extends IBaseResource> nextTypeClass = def.getImplementingClass();
			mySearchBuilder = mySearchBuilderFactory.newSearchBuilder(dao, myResourceType, nextTypeClass);
		}
		return mySearchBuilder;
	}

	/**
	 * Generate the list of pids of all resources of the given myResourceType, which reference any group member of the given myGroupId.
	 * Store them in a member iterator.
	 */
	protected void loadResourcePids() {
		//Initialize an array to hold the pids of the target resources to be exported.
		myPidIterator = getResourcePidIterator();
	}

	abstract Iterator<ResourcePersistentId> getResourcePidIterator();

	protected SearchParameterMap createSearchParameterMapForJob() {
		BulkExportJobEntity jobEntity = getJobEntity();
		RuntimeResourceDefinition theDef = getResourceDefinition();
		SearchParameterMap map = new SearchParameterMap();
		Map<String, String[]> requestUrl = UrlUtil.parseQueryStrings(jobEntity.getRequest());
		String[] typeFilters = requestUrl.get(JpaConstants.PARAM_EXPORT_TYPE_FILTER);
		if (typeFilters != null) {
			Optional<String> filter = Arrays.stream(typeFilters).filter(t -> t.startsWith(myResourceType + "?")).findFirst();
			if (filter.isPresent()) {
				String matchUrl = filter.get();
				map = myMatchUrlService.translateMatchUrl(matchUrl, theDef);
			}
		}
		if (jobEntity.getSince() != null) {
			map.setLastUpdated(new DateRangeParam(jobEntity.getSince(), null));
		}
		map.setLoadSynchronous(true);
		return map;
	}

	protected RuntimeResourceDefinition getResourceDefinition() {
		if (myResourceDefinition == null) {
			myResourceDefinition = myContext.getResourceDefinition(myResourceType);
		}
		return myResourceDefinition;
	}

	protected BulkExportJobEntity getJobEntity() {
		if (myJobEntity == null) {
			Optional<BulkExportJobEntity> jobOpt = myBulkExportJobDao.findByJobId(myJobUUID);
			if (jobOpt.isPresent()) {
				myJobEntity = jobOpt.get();
			} else {
				String errorMessage = String.format("Job with UUID %s does not exist!", myJobUUID);
				throw new IllegalStateException(errorMessage);
			}
		}
		return myJobEntity;
	}

	@Override
	public List<ResourcePersistentId> read() {

		ourLog.info("Bulk export starting generation for batch export job: [{}] with resourceType [{}] and UUID [{}]", getJobEntity(), myResourceType, myJobUUID);

		if (myPidIterator == null) {
			loadResourcePids();
		}

		int count = 0;
		List<ResourcePersistentId> outgoing = new ArrayList<>();
		while (myPidIterator.hasNext() && count < myReadChunkSize) {
			outgoing.add(myPidIterator.next());
			count += 1;
		}

		return outgoing.size() == 0 ? null : outgoing;

	}
}
