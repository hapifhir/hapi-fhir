package ca.uhn.fhir.jpa.batch.job;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2021 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

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
