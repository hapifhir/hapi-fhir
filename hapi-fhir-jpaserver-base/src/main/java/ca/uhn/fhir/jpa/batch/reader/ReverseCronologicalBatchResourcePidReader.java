package ca.uhn.fhir.jpa.batch.reader;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.delete.model.UrlListJson;
import ca.uhn.fhir.jpa.partition.SystemRequestDetails;
import ca.uhn.fhir.jpa.searchparam.MatchUrlService;
import ca.uhn.fhir.jpa.searchparam.ResourceSearch;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.SortOrderEnum;
import ca.uhn.fhir.rest.api.SortSpec;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import ca.uhn.fhir.rest.param.DateRangeParam;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemStream;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * This Spring Batch reader takes 4 parameters:
 * {@link #JOB_PARAM_URL_LIST}: A list of URLs to searchfor
 * {@link #JOB_PARAM_TENANT_ID}: The tenant to perform the search with (or null)
 * {@link #JOB_PARAM_BATCH_SIZE}: The number of resources to return with each search.  If ommitted, {@link DaoConfig#getExpungeBatchSize} will be used.
 * {@link #JOB_PARAM_START_TIME}: The latest timestamp of resources to search for
 * <p>
 * The reader will return at most {@link #JOB_PARAM_BATCH_SIZE} pids every time it is called, or null
 * once no more matching resources are available.  It returns the resources in reverse chronological order
 * and stores where it's at in the Spring Batch execution context with the key {@link #CURRENT_THRESHOLD_HIGH}
 * appended with "." and the index number of the url list item it has gotten up to.  This is to permit
 * restarting jobs that use this reader so it can pick up where it left off.
 */
public class ReverseCronologicalBatchResourcePidReader implements ItemReader<List<Long>>, ItemStream {

	public static final String JOB_PARAM_URL_LIST = "url-list";
	public static final String JOB_PARAM_BATCH_SIZE = "batch-size";
	public static final String JOB_PARAM_START_TIME = "start-time";
	public static final String JOB_PARAM_TENANT_ID = "tenant-id";

	private static final String CURRENT_URL_INDEX = "current.url-index";
	private static final String CURRENT_THRESHOLD_HIGH = "current.threshold-high";
	private static final Logger ourLog = LoggerFactory.getLogger(ReverseCronologicalBatchResourcePidReader.class);

	@Autowired
	private FhirContext myFhirContext;
	@Autowired
	private MatchUrlService myMatchUrlService;
	@Autowired
	private DaoRegistry myDaoRegistry;
	@Autowired
	private DaoConfig myDaoConfig;

	private List<String> myUrlList;
	private Integer myBatchSize;
	private Instant myStartTime;
	private int myUrlIndex = 0;
	private final Map<Integer, Instant> myThresholdHighByUrlIndex = new HashMap<>();
	private String myTenantId;

	@Autowired
	public void setUrlList(@Value("#{jobParameters['" + JOB_PARAM_URL_LIST + "']}") String theUrlListString) {
		UrlListJson urlListJson = UrlListJson.fromJson(theUrlListString);
		myUrlList = urlListJson.getUrlList();
	}

	@Autowired
	public void setBatchSize(@Value("#{jobParameters['" + JOB_PARAM_BATCH_SIZE + "']}") Integer theBatchSize) {
		myBatchSize = theBatchSize;
	}

	@Autowired
	public void setStartTime(@Value("#{jobParameters['" + JOB_PARAM_START_TIME + "']}") Date theStartTime) {
		myStartTime = theStartTime.toInstant();
	}

	@Autowired
	public void setTenantId(@Value("#{jobParameters['" + JOB_PARAM_TENANT_ID + "']}") String theTenantId) {
		myTenantId = theTenantId;
	}

	@Override
	public List<Long> read() throws Exception {
		while (myUrlIndex < myUrlList.size()) {
			List<Long> nextBatch;
			nextBatch = getNextBatch(myUrlIndex);
			if (nextBatch.isEmpty()) {
				++myUrlIndex;
				continue;
			}

			return nextBatch;
		}
		return null;
	}

	private List<Long> getNextBatch(int theUrlIndex) {
		ResourceSearch resourceSearch = myMatchUrlService.getResourceSearch(myUrlList.get(theUrlIndex));
		SearchParameterMap map = resourceSearch.getSearchParameterMap();

		map.setLastUpdated(new DateRangeParam().setUpperBoundInclusive(Date.from(myThresholdHighByUrlIndex.get(theUrlIndex))));

		map.setLoadSynchronousUpTo(myBatchSize);
		SortSpec sort = new SortSpec(Constants.PARAM_LASTUPDATED, SortOrderEnum.DESC);
		map.setSort(sort);

		// Perform the search
		IFhirResourceDao<?> dao = myDaoRegistry.getResourceDao(resourceSearch.getResourceName());
		List<Long> retval = dao.searchForIds(map, buildSystemRequestDetails()).stream()
			.map(ResourcePersistentId::getIdAsLong)
			.collect(Collectors.toList());

		if (ourLog.isDebugEnabled()) {
			ourLog.debug("Search for {}{} returned {} results", resourceSearch.getResourceName(), map.toNormalizedQueryString(myFhirContext), retval.size());
			ourLog.debug("Results: {}", retval);
		}

		if (!retval.isEmpty()) {
			// Adjust the high threshold to be the earliest resource in the batch we found
			Long pidOfOldestResourceInBatch = retval.get(retval.size() - 1);
			IBaseResource earliestResource = dao.readByPid(new ResourcePersistentId(pidOfOldestResourceInBatch));
			myThresholdHighByUrlIndex.put(myUrlIndex, earliestResource.getMeta().getLastUpdated().toInstant());
		}

		return retval;
	}

	@NotNull
	private SystemRequestDetails buildSystemRequestDetails() {
		SystemRequestDetails retval = new SystemRequestDetails();
		retval.setTenantId(myTenantId);
		return retval;
	}

	@Override
	public void open(ExecutionContext executionContext) throws ItemStreamException {
		if (myBatchSize == null) {
			myBatchSize = myDaoConfig.getExpungeBatchSize();
		}
		if (executionContext.containsKey(CURRENT_URL_INDEX)) {
			myUrlIndex = new Long(executionContext.getLong(CURRENT_URL_INDEX)).intValue();
		}
		for (int index = 0; index < myUrlList.size(); ++index) {
			String key = highKey(index);
			if (executionContext.containsKey(key)) {
				myThresholdHighByUrlIndex.put(index, Instant.ofEpochSecond(executionContext.getLong(key)));
			} else {
				myThresholdHighByUrlIndex.put(index, myStartTime);
			}
		}
	}

	private static String highKey(int theIndex) {
		return CURRENT_THRESHOLD_HIGH + "." + theIndex;
	}

	@Override
	public void update(ExecutionContext executionContext) throws ItemStreamException {
		executionContext.putLong(CURRENT_URL_INDEX, myUrlIndex);
		for (int index = 0; index < myUrlList.size(); ++index) {
			Instant instant = myThresholdHighByUrlIndex.get(index);
			if (instant != null) {
				executionContext.putLong(highKey(index), instant.getEpochSecond());
			}
		}
	}

	@Override
	public void close() throws ItemStreamException {
	}
}
