package ca.uhn.fhir.jpa.bulk.job;

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

import ca.uhn.fhir.jpa.batch.processors.PidToIBaseResourceProcessor;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersValidator;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import java.util.List;

/**
 * Spring batch Job configuration file. Contains all necessary plumbing to run a
 * Bulk Export job.
 */
@Configuration
public class BulkExportJobConfig {

	@Autowired
	private StepBuilderFactory myStepBuilderFactory;

	@Autowired
	private JobBuilderFactory myJobBuilderFactory;

	@Autowired
	private PidToIBaseResourceProcessor myPidToIBaseResourceProcessor;

	@Bean
	@Lazy
	public Job bulkExportJob() {
		return myJobBuilderFactory.get("bulkExportJob")
			.validator(bulkJobParameterValidator())
			.start(createBulkExportEntityStep())
			.next(partitionStep())
			.next(closeJobStep())
			.build();
	}

	@Bean
	public Step createBulkExportEntityStep() {
		return myStepBuilderFactory.get("createBulkExportEntityStep")
			.tasklet(createBulkExportEntityTasklet())
			.listener(bulkExportCreateEntityStepListener())
			.build();
	}

	@Bean
	public CreateBulkExportEntityTasklet createBulkExportEntityTasklet() {
		return new CreateBulkExportEntityTasklet();
	}

	@Bean
	public JobParametersValidator bulkJobParameterValidator() {
		return new BulkExportJobParameterValidator();
	}

	@Bean
	public Step bulkExportGenerateResourceFilesStep() {
		return myStepBuilderFactory.get("bulkExportGenerateResourceFilesStep")
			.<List<ResourcePersistentId>, List<IBaseResource>> chunk(100) //1000 resources per generated file, as the reader returns 10 resources at a time.
			.reader(bulkItemReader())
			.processor(myPidToIBaseResourceProcessor)
			.writer(resourceToFileWriter())
			.listener(bulkExportGenrateResourceFilesStepListener())
			.build();
	}

	@Bean
	@JobScope
	public BulkExportJobCloser bulkExportJobCloser() {
		return new BulkExportJobCloser();
	}

	@Bean
	public Step closeJobStep() {
		return myStepBuilderFactory.get("closeJobStep")
			.tasklet(bulkExportJobCloser())
			.build();
	}

	@Bean
	@JobScope
	public BulkExportCreateEntityStepListener bulkExportCreateEntityStepListener() {
		return new BulkExportCreateEntityStepListener();
	}

	@Bean
	@JobScope
	public BulkExportGenerateResourceFilesStepListener bulkExportGenrateResourceFilesStepListener() {
		return new BulkExportGenerateResourceFilesStepListener();
	}

	@Bean
	public Step partitionStep() {
		return myStepBuilderFactory.get("partitionStep")
			.partitioner("bulkExportGenerateResourceFilesStep", bulkExportResourceTypePartitioner())
			.step(bulkExportGenerateResourceFilesStep())
			.build();
	}

	@Bean
	@StepScope
	public BulkItemReader bulkItemReader(){
		return new BulkItemReader();
	}

	@Bean
	@JobScope
	public ResourceTypePartitioner bulkExportResourceTypePartitioner() {
		return new ResourceTypePartitioner();
	}

	@Bean
	@StepScope
	public ItemWriter<List<IBaseResource>> resourceToFileWriter() {
		return new ResourceToFileWriter();
	}

}
