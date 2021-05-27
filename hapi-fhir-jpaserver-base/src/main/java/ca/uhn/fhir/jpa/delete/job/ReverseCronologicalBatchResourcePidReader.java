package ca.uhn.fhir.jpa.delete.job;

import ca.uhn.fhir.jpa.delete.model.UrlListJson;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Value;

import java.time.Instant;
import java.util.List;
import java.util.Map;

public class ReverseCronologicalBatchResourcePidReader implements ItemReader<String> {
	@Value("#{jobParameters['" + DeleteExpungeJobConfig.JOB_PARAM_URL_LIST + "']}")
	protected String myUrlListJson;
	private int urlIndex = 0;
	// 		job.setThresholdHigh(DateUtils.addMinutes(new Date(), 5));
	private Map<Integer, Instant> myThresholdHighByUrlIndex;
	private Map<Integer, Instant> myThresholdLowByUrlIndex;

	@Override
	public String read() throws Exception {
		UrlListJson urlListJson = UrlListJson.fromJson(myUrlListJson);
		List<String> urlList = urlListJson.getUrlList();
		if (urlIndex >= urlList.size()) {
			return null;
		}
		return urlList.get(urlIndex++);
	}
}
