package ca.uhn.fhir.jpa.bulk.imp.job;

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
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.List;

import static ca.uhn.fhir.jpa.batch.BatchJobsConfig.BULK_IMPORT_JOB_NAME;

/**
 * Spring batch Job configuration file. Contains all necessary plumbing to run a
 * Bulk Export job.
 */
@Configuration
public class BulkImportJobConfig {

	// FIXME: remove comments and unused

	@Autowired
	private StepBuilderFactory myStepBuilderFactory;

	@Autowired
	private JobBuilderFactory myJobBuilderFactory;

	@Bean(name = BULK_IMPORT_JOB_NAME)
	@Lazy
	public Job bulkImportJob() {
		return myJobBuilderFactory.get(BULK_IMPORT_JOB_NAME)
			.validator(bulkImportJobParameterValidator())
			// FIXME: remove
//			.start(createBulkImportEntityStep())
//			.next(bulkImportPartitionStep())
			.start(bulkImportPartitionStep())
			.next(bulkImportCloseJobStep())
			.build();
	}

	@Bean
	public JobParametersValidator bulkImportJobParameterValidator() {
		return new BulkImportJobParameterValidator();
	}

	// FIXME: remove
	@Bean
	public Step createBulkImportEntityStep() {
		return myStepBuilderFactory.get("createBulkExportEntityStep")
			.tasklet(createBulkImportEntityTasklet())
			.listener(activateBulkImportEntityStepListener())
			.build();
	}

	@Bean
	public CreateBulkImportEntityTasklet createBulkImportEntityTasklet() {
		return new CreateBulkImportEntityTasklet();
	}

	@Bean
	@JobScope
	public ActivateBulkImportEntityStepListener activateBulkImportEntityStepListener() {
		return new ActivateBulkImportEntityStepListener();
	}

	@Bean
	public Step bulkImportPartitionStep() {
		return myStepBuilderFactory.get("bulkImportPartitionStep")
			.partitioner("bulkImportPartitionStep", bulkImportPartitioner())
			.step(bulkImportProcessFilesStep())
			.listener(activateBulkImportEntityStepListener())
			.gridSize(10)
			.build();
	}

	@Bean
	public Step bulkImportCloseJobStep() {
		return myStepBuilderFactory.get("bulkImportCloseJobStep")
			.tasklet(bulkImportJobCloser())
			.build();
	}

	@Bean
	@JobScope
	public BulkImportJobCloser bulkImportJobCloser() {
		return new BulkImportJobCloser();
	}

	@Bean
	@JobScope
	public BulkImportPartitioner bulkImportPartitioner() {
		return new BulkImportPartitioner();
	}


	@Bean
	public Step bulkImportProcessFilesStep() {
		return myStepBuilderFactory.get("groupBulkExportGenerateResourceFilesStep")
			.<String, List<IBaseResource>>chunk(5) // FIXME: what does the chunk size do
			.reader(bulkImportFileReader())
			.processor(bulkImportParseFileProcessor())
			.writer(bulkImportFileWriter())
			.listener(bulkImportStepListener())
			.taskExecutor(bulkImportTaskExecutor())
			.build();
	}

	@Bean
	public TaskExecutor bulkImportTaskExecutor() {
		ThreadPoolTaskExecutor retVal = new ThreadPoolTaskExecutor();
		retVal.setThreadNamePrefix("BulkImport-");
		retVal.setMaxPoolSize(20);
		return retVal;
	}

	@Bean
	@StepScope
	public ItemWriter<List<IBaseResource>> bulkImportFileWriter() {
		return new BulkImportFileWriter();
	}


	@Bean
	@StepScope
	public BulkImportFileReader bulkImportFileReader() {
		return new BulkImportFileReader();
	}

	@Bean
	@StepScope
	public BulkImportFileProcessor bulkImportParseFileProcessor() {
		return new BulkImportFileProcessor();
	}

	@Bean
	@StepScope
	public BulkImportStepListener bulkImportStepListener() {
		return new BulkImportStepListener();
	}

//	@Bean
//	@Lazy
//	@StepScope
//	public CompositeItemProcessor<List<ResourcePersistentId>, List<IBaseResource>> inflateResourceThenAnnotateWithGoldenResourceProcessor() {
//		CompositeItemProcessor processor = new CompositeItemProcessor<>();
//		ArrayList<ItemProcessor> delegates = new ArrayList<>();
//		delegates.add(myPidToIBaseResourceProcessor);
//		delegates.add(myGoldenResourceAnnotatingProcessor);
//		processor.setDelegates(delegates);
//		return processor;
//	}
//
//	@Bean
//	@Lazy
//	public Job groupBulkExportJob() {
//		return myJobBuilderFactory.get(BatchJobsConfig.GROUP_BULK_EXPORT_JOB_NAME)
//			.validator(groupBulkJobParameterValidator())
//			.validator(bulkJobParameterValidator())
//			.start(createBulkExportEntityStep())
//			.next(groupPartitionStep())
//			.next(closeJobStep())
//			.build();
//	}
//
//	@Bean
//	@Lazy
//	public Job patientBulkExportJob() {
//		return myJobBuilderFactory.get(BatchJobsConfig.PATIENT_BULK_EXPORT_JOB_NAME)
//			.validator(bulkJobParameterValidator())
//			.start(createBulkExportEntityStep())
//			.next(patientPartitionStep())
//			.next(closeJobStep())
//			.build();
//	}
//
//	@Bean
//	public GroupIdPresentValidator groupBulkJobParameterValidator() {
//		return new GroupIdPresentValidator();
//	}
//
//	@Bean
//	public Step createBulkExportEntityStep() {
//		return myStepBuilderFactory.get("createBulkExportEntityStep")
//			.tasklet(createBulkExportEntityTasklet())
//			.listener(bulkExportCreateEntityStepListener())
//			.build();
//	}
////
//
//	//Writers
//	@Bean
//	public Step groupBulkExportGenerateResourceFilesStep() {
//		return myStepBuilderFactory.get("groupBulkExportGenerateResourceFilesStep")
//			.<List<ResourcePersistentId>, List<IBaseResource>> chunk(CHUNK_SIZE) //1000 resources per generated file, as the reader returns 10 resources at a time.
//			.reader(groupBulkItemReader())
//			.processor(inflateResourceThenAnnotateWithGoldenResourceProcessor())
//			.writer(resourceToFileWriter())
//			.listener(bulkExportGenerateResourceFilesStepListener())
//			.build();
//	}
//
//	@Bean
//	public Step bulkExportGenerateResourceFilesStep() {
//		return myStepBuilderFactory.get("bulkExportGenerateResourceFilesStep")
//			.<List<ResourcePersistentId>, List<IBaseResource>> chunk(CHUNK_SIZE) //1000 resources per generated file, as the reader returns 10 resources at a time.
//			.reader(bulkItemReader())
//			.processor(myPidToIBaseResourceProcessor)
//			.writer(resourceToFileWriter())
//			.listener(bulkExportGenerateResourceFilesStepListener())
//			.build();
//	}
//	@Bean
//	public Step patientBulkExportGenerateResourceFilesStep() {
//		return myStepBuilderFactory.get("patientBulkExportGenerateResourceFilesStep")
//			.<List<ResourcePersistentId>, List<IBaseResource>> chunk(CHUNK_SIZE) //1000 resources per generated file, as the reader returns 10 resources at a time.
//			.reader(patientBulkItemReader())
//			.processor(myPidToIBaseResourceProcessor)
//			.writer(resourceToFileWriter())
//			.listener(bulkExportGenerateResourceFilesStepListener())
//			.build();
//	}
//
//	@Bean
//	@JobScope
//	public BulkExportJobCloser bulkExportJobCloser() {
//		return new BulkExportJobCloser();
//	}
//
//	@Bean
//	public Step closeJobStep() {
//		return myStepBuilderFactory.get("closeJobStep")
//			.tasklet(bulkExportJobCloser())
//			.build();
//	}
//
//
//	@Bean
//	@JobScope
//	public BulkExportGenerateResourceFilesStepListener bulkExportGenerateResourceFilesStepListener() {
//		return new BulkExportGenerateResourceFilesStepListener();
//	}
//
//
//	@Bean
//	public Step groupPartitionStep() {
//		return myStepBuilderFactory.get("partitionStep")
//			.partitioner("groupBulkExportGenerateResourceFilesStep", bulkExportResourceTypePartitioner())
//			.step(groupBulkExportGenerateResourceFilesStep())
//			.build();
//	}
//
//	@Bean
//	public Step patientPartitionStep() {
//		return myStepBuilderFactory.get("partitionStep")
//			.partitioner("patientBulkExportGenerateResourceFilesStep", bulkExportResourceTypePartitioner())
//			.step(patientBulkExportGenerateResourceFilesStep())
//			.build();
//	}
//
//
//
//	@Bean
//	@StepScope
//	public PatientBulkItemReader patientBulkItemReader() {
//		return new PatientBulkItemReader();
//	}
//
//	@Bean
//	@StepScope
//	public BulkItemReader bulkItemReader(){
//		return new BulkItemReader();
//	}
//
//
//	@Bean
//	@StepScope
//	public ResourceToFileWriter resourceToFileWriter() {
//		return new ResourceToFileWriter();
//	}

}
