package ca.uhn.fhir.jpa.delete.job;

import ca.uhn.fhir.jpa.bulk.export.job.BulkExportJobConfig;
import ca.uhn.fhir.jpa.delete.model.UrlListJson;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

public class UrlListReader implements ItemReader<String> {
	@Value("#{jobParameters['" + DeleteExpungeJobConfig.JOB_PARAM_URL_LIST + "']}")
	protected String myUrlListJson;
	private int index = 0;

	@Override
	public String read() throws Exception {
		UrlListJson urlListJson = UrlListJson.fromJson(myUrlListJson);
		List<String> urlList = urlListJson.getUrlList();
		if (index >= urlList.size()) {
			return null;
		}
		return urlList.get(index++);
	}
}
