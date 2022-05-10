package ca.uhn.fhir.jpa.batch.listener;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.AfterRead;
import org.springframework.batch.core.annotation.BeforeStep;

import java.util.List;

/**
 * Add the number of pids processed to the execution context so we can track progress of the job
 */
public class PidReaderCounterListener {
	public static final String RESOURCE_TOTAL_PROCESSED = "resource.total.processed";

	private StepExecution myStepExecution;
	private Long myTotalPidsProcessed = 0L;

	@BeforeStep
	public void setStepExecution(StepExecution stepExecution) {
		myStepExecution = stepExecution;
	}

	@AfterRead
	public void afterRead(List<Long> thePids) {
		myTotalPidsProcessed += thePids.size();
		myStepExecution.getExecutionContext().putLong(RESOURCE_TOTAL_PROCESSED, myTotalPidsProcessed);
	}
}
