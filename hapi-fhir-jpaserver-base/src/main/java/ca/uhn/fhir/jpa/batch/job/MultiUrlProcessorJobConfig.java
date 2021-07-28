package ca.uhn.fhir.jpa.batch.job;

import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.batch.listener.PidReaderCounterListener;
import ca.uhn.fhir.jpa.batch.reader.ReverseCronologicalBatchResourcePidReader;
import ca.uhn.fhir.jpa.batch.writer.SqlExecutorWriter;
import ca.uhn.fhir.jpa.searchparam.MatchUrlService;
import org.springframework.batch.core.JobParametersValidator;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.context.annotation.Bean;

public class MultiUrlProcessorJobConfig {
	public static final int MINUTES_IN_FUTURE_TO_PROCESS_FROM = 1;

	@Bean
	public JobParametersValidator multiUrlProcessorParameterValidator(MatchUrlService theMatchUrlService, DaoRegistry theDaoRegistry) {
		return new MultiUrlJobParameterValidator(theMatchUrlService, theDaoRegistry);
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
