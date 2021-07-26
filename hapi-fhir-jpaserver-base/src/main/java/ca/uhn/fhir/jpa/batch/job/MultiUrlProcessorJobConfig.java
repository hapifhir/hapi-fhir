package ca.uhn.fhir.jpa.batch.job;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.batch.job.model.RequestListJson;
import ca.uhn.fhir.jpa.batch.listener.PidReaderCounterListener;
import ca.uhn.fhir.jpa.batch.reader.ReverseCronologicalBatchResourcePidReader;
import ca.uhn.fhir.jpa.batch.writer.SqlExecutorWriter;
import ca.uhn.fhir.jpa.searchparam.MatchUrlService;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersValidator;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.context.annotation.Bean;

import javax.annotation.Nonnull;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MultiUrlProcessorJobConfig {
	private static final int MINUTES_IN_FUTURE_TO_PROCESS_FROM = 1;

	@Nonnull
	public static JobParameters buildJobParameters(Integer theBatchSize, List<String> theUrlList, List<RequestPartitionId> theRequestPartitionIds) {
		Map<String, JobParameter> map = new HashMap<>();
		RequestListJson requestListJson = RequestListJson.fromUrlStringsAndRequestPartitionIds(theUrlList, theRequestPartitionIds);
		map.put(ReverseCronologicalBatchResourcePidReader.JOB_PARAM_REQUEST_LIST, new JobParameter(requestListJson.toString()));
		map.put(ReverseCronologicalBatchResourcePidReader.JOB_PARAM_START_TIME, new JobParameter(DateUtils.addMinutes(new Date(), MultiUrlProcessorJobConfig.MINUTES_IN_FUTURE_TO_PROCESS_FROM)));
		if (theBatchSize != null) {
			map.put(ReverseCronologicalBatchResourcePidReader.JOB_PARAM_BATCH_SIZE, new JobParameter(theBatchSize.longValue()));
		}
		JobParameters parameters = new JobParameters(map);
		return parameters;
	}

	@Bean
	public JobParametersValidator multiUrlProcessorParameterValidator(FhirContext theFhirContext, MatchUrlService theMatchUrlService, DaoRegistry theDaoRegistry) {
		return new MultiUrlJobParameterValidator(ProviderConstants.OPERATION_DELETE_EXPUNGE, theMatchUrlService, theDaoRegistry);
	}

	@Bean
	@StepScope
	public SqlExecutorWriter sqlExecutorWriter() {
		return new SqlExecutorWriter();
	}

	@Bean
	@StepScope
	public PidReaderCounterListener pidCountRecorderListener() {
		return new PidReaderCounterListener();
	}

	@Bean
	@StepScope
	public ReverseCronologicalBatchResourcePidReader reverseCronologicalBatchResourcePidReader() {
		return new ReverseCronologicalBatchResourcePidReader();
	}
}
