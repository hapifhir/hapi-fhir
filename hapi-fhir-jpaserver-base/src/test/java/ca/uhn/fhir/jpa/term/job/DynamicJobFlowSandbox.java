package ca.uhn.fhir.jpa.term.job;

/*
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

import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.builder.SimpleJobBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.job.flow.support.SimpleFlow;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Not intended to ever run. Used as a sandbox for "interesting" jobs
 */
public class DynamicJobFlowSandbox {
	protected static final Logger ourLog = LoggerFactory.getLogger(TermCodeSystemDeleteJobTest.class);


	@Autowired
	private JobBuilderFactory myJobBuilderFactory;

	@Autowired
	private StepBuilderFactory myStepBuilderFactory;

	private List<Long> versionPidList = Lists.newArrayList(3L, 5L);

	@Bean
	public Job testJob() {
		SimpleJobBuilder jobBuilder = myJobBuilderFactory.get("job")
			.start(stepPreFlow());

			// add a flow for each Pid
		List<Flow> flowForEachPidList = versionPidList.stream().map(this::getFlowForPid).collect(Collectors.toList());
		flowForEachPidList.forEach( flowForPid -> jobBuilder.on("COMPLETED").to(flowForPid) );

		return jobBuilder.next(stepPostFlow()).build();
	}


	private Flow getFlowForPid(Long theLong) {
		return new FlowBuilder<SimpleFlow>("flow-for-Pid-" + theLong)
			.start(flowStep1(theLong))
			.next(fllowStep2(theLong))
			.build();
	}



	public Step flowStep1(long theLong) {
		String name = "flow-step-1-for-Pid-" + theLong;
		return myStepBuilderFactory.get(name)
			.tasklet((contribution, chunkContext) -> {
				ourLog.info("\n\n" + name + " executed\n\n");
				return RepeatStatus.FINISHED;
			})
			.build();
	}


	public Step fllowStep2(long theLong) {
		String name = "flow-step-2-for-Pid-" + theLong;
		return myStepBuilderFactory.get(name)
			.tasklet((contribution, chunkContext) -> {
				ourLog.info("\n\n" + name + " executed\n\n");
				return RepeatStatus.FINISHED;
			})
			.build();
	}


	public Step stepPreFlow() {
		return myStepBuilderFactory.get("step-pre-flow")
			.tasklet((contribution, chunkContext) -> {
				ourLog.info("\n\nstep-pre-flow executed\n\n");
				return RepeatStatus.FINISHED;
			})
			.build();
	}


	public Step stepPostFlow() {
		return myStepBuilderFactory.get("step-post-flow")
			.tasklet((contribution, chunkContext) -> {
				ourLog.info("\n\nstep-post-flow executed\n\n");
				return RepeatStatus.FINISHED;
			})
			.build();
	}


}



