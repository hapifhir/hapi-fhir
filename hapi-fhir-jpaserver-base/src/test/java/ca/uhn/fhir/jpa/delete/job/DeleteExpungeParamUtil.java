package ca.uhn.fhir.jpa.delete.job;

import ca.uhn.fhir.jpa.delete.model.UrlListJson;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

import static ca.uhn.fhir.jpa.delete.job.DeleteExpungeJobConfig.JOB_PARAM_URL_LIST;

public class DeleteExpungeParamUtil {

	@Nonnull
	static JobParameters buildJobParameters(String... theUrls) {
		Map<String, JobParameter> map = new HashMap<>();
		UrlListJson urlListJson = UrlListJson.fromUrlStrings(theUrls);
		map.put(JOB_PARAM_URL_LIST, new JobParameter(urlListJson.toString()));
		JobParameters parameters = new JobParameters(map);
		return parameters;
	}
}
