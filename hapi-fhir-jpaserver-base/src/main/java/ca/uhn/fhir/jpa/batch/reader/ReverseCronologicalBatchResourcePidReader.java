package ca.uhn.fhir.jpa.batch.reader;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.delete.job.DeleteExpungeJobConfig;
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

public class ReverseCronologicalBatchResourcePidReader implements ItemReader<List<Long>>, ItemStream {
	private static final String CURRENT_URL_INDEX = "current.url-index";
	private static final String CURRENT_THRESHOLD_LOW = "current.threshold-low";
	private static final String CURRENT_THRESHOLD_HIGH = "current.threshold-high";
	private static final Integer DEFAULT_SEARCH_COUNT = 100;

	@Autowired
	private FhirContext myFhirContext;
	@Autowired
	private MatchUrlService myMatchUrlService;
	@Autowired
	private DaoRegistry myDaoRegistry;

	private List<String> myUrlList;
	private Integer mySearchCount = DEFAULT_SEARCH_COUNT;
	private int urlIndex = 0;
	private final Map<Integer, Instant> myThresholdHighByUrlIndex = new HashMap<>();

	@Autowired
	public void setUrlList(@Value("#{jobParameters['" + DeleteExpungeJobConfig.JOB_PARAM_URL_LIST + "']}") String theUrlListString) {
		UrlListJson urlListJson = UrlListJson.fromJson(theUrlListString);
		myUrlList = urlListJson.getUrlList();
	}

	// FIXME KHS test
	@Autowired
	public void setSearchCount(@Value("#{jobParameters['" + DeleteExpungeJobConfig.JOB_PARAM_SEARCH_COUNT + "']}") Integer theSearchCount) {
		mySearchCount = theSearchCount;
	}

	@Override
	public List<Long> read() throws Exception {
		while (urlIndex < myUrlList.size()) {
			List<Long> nextBatch;
			nextBatch = getNextBatch(urlIndex);
			if (nextBatch.isEmpty()) {
				++urlIndex;
				continue;
			}

			return nextBatch;
		}
		return null;
	}

	private List<Long> getNextBatch(int theUrlIndex) {
		ResourceSearch resourceSearch = myMatchUrlService.getResourceSearch(myUrlList.get(theUrlIndex));
		SearchParameterMap map = resourceSearch.getSearchParameterMap();

		Instant highThreshold = myThresholdHighByUrlIndex.get(theUrlIndex);
		if (highThreshold != null) {
			map.setLastUpdated(new DateRangeParam().setUpperBoundInclusive(Date.from(highThreshold)));
		}

		map.setCount(mySearchCount);
		map.setLoadSynchronous(true);
		SortSpec sort = new SortSpec(Constants.PARAM_LASTUPDATED, SortOrderEnum.DESC);
		map.setSort(sort);

		// Perform the search
		IFhirResourceDao<?> dao = myDaoRegistry.getResourceDao(resourceSearch.getResourceName());
		List<Long> retval = dao.searchForIds(map, buildSystemRequestDetails()).stream()
			.map(ResourcePersistentId::getIdAsLong)
			.collect(Collectors.toList());

		if (!retval.isEmpty()) {
			// Adjust the high threshold to be the earliest resource in the batch we found
			Long pidOfOldestResourceInBatch = retval.get(retval.size() - 1);
			IBaseResource earliestResource = dao.readByPid(new ResourcePersistentId(pidOfOldestResourceInBatch));
			myThresholdHighByUrlIndex.put(urlIndex, earliestResource.getMeta().getLastUpdated().toInstant());
		}

		return retval;
	}

	@NotNull
	private SystemRequestDetails buildSystemRequestDetails() {
		return new SystemRequestDetails();
	}

	@Override
	public void open(ExecutionContext executionContext) throws ItemStreamException {
		if (executionContext.containsKey(CURRENT_URL_INDEX)) {
			urlIndex = new Long(executionContext.getLong(CURRENT_URL_INDEX)).intValue();
		}
		for (int index = 0; index < myUrlList.size(); ++index) {
			String key = highKey(index);
			if (executionContext.containsKey(key)) {
				myThresholdHighByUrlIndex.put(index, Instant.ofEpochSecond(executionContext.getLong(key)));
			}
		}
	}

	private static String lowKey(int theIndex) {
		return CURRENT_THRESHOLD_LOW + "." + theIndex;
	}

	private static String highKey(int theIndex) {
		return CURRENT_THRESHOLD_HIGH + "." + theIndex;
	}

	@Override
	public void update(ExecutionContext executionContext) throws ItemStreamException {
		executionContext.putLong(CURRENT_URL_INDEX, urlIndex);
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
