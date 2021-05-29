package ca.uhn.fhir.jpa.batch.reader;

import ca.uhn.fhir.jpa.delete.job.DeleteExpungeJobConfig;
import ca.uhn.fhir.jpa.delete.model.UrlListJson;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemStream;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReverseCronologicalBatchResourcePidReader implements ItemReader<List<Long>>, ItemStream {
	private static final String CURRENT_URL_INDEX = "current.url-index";
	private static final String CURRENT_THRESHOLD_LOW = "current.threshold-low";
	private static final String CURRENT_THRESHOLD_HIGH = "current.threshold-high";

	private List<String> myUrlList;
	private int urlIndex = 0;
	// 		job.setThresholdHigh(DateUtils.addMinutes(new Date(), 5));
	private Map<Integer, Instant> myThresholdHighByUrlIndex = new HashMap<>();
	private Map<Integer, Instant> myThresholdLowByUrlIndex = new HashMap<>();
	// FIXME KHS remove
	public static int counter = 0;

	@Autowired
	public void setUrlList(@Value("#{jobParameters['" + DeleteExpungeJobConfig.JOB_PARAM_URL_LIST + "']}") String theUrlListString) {
		UrlListJson urlListJson = UrlListJson.fromJson(theUrlListString);
		myUrlList = urlListJson.getUrlList();
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
		// FIXME KHS
		List<Long> retval = new ArrayList<>();
		++counter;
		if (counter % 5 != 0) {
			retval.add(49L);
			retval.add(2401L);
		}
		return retval;
	}

	@Override
	public void open(ExecutionContext executionContext) throws ItemStreamException {
		if (executionContext.containsKey(CURRENT_URL_INDEX)) {
			urlIndex = new Long(executionContext.getLong(CURRENT_URL_INDEX)).intValue();
		}
		for (int index = 0; index < myUrlList.size(); ++index) {
			setThreshold(executionContext, myThresholdLowByUrlIndex, index, lowKey(index), Instant.MIN);
			setThreshold(executionContext, myThresholdHighByUrlIndex, index, highKey(index), Instant.MAX);
		}
	}

	private void setThreshold(ExecutionContext executionContext, Map<Integer, Instant> theThresholdMap, int theIndex, String theKey, Instant theDefaultValue) {
		Instant value = theDefaultValue;
		if (executionContext.containsKey(theKey)) {
			value = Instant.ofEpochSecond(executionContext.getLong(theKey));
		}
		theThresholdMap.put(theIndex, value);
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
			executionContext.putLong(lowKey(index), myThresholdLowByUrlIndex.get(index).getEpochSecond());
			executionContext.putLong(highKey(index), myThresholdHighByUrlIndex.get(index).getEpochSecond());
		}
	}

	@Override
	public void close() throws ItemStreamException {
	}
}
