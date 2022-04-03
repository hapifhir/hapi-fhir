package ca.uhn.fhir.jpa.bulk.export.job;

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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.batch.config.BatchConstants;
import ca.uhn.fhir.jpa.batch.processor.GoldenResourceAnnotatingProcessor;
import ca.uhn.fhir.jpa.batch.processor.PidToIBaseResourceProcessor;
import ca.uhn.fhir.jpa.bulk.export.svc.BulkExportDaoSvc;
import ca.uhn.fhir.jpa.dao.mdm.MdmExpansionCacheSvc;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersValidator;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import java.util.ArrayList;
import java.util.List;

/**
 * Spring batch Job configuration file. Contains all necessary plumbing to run a
 * Bulk Export job.
 */
@Configuration
public class BulkExportJobConfig {
	public static final int CHUNK_SIZE = 100;

	@Autowired
	private FhirContext myFhirContext;

	@Autowired
	private DaoRegistry myDaoRegistry;

	@Autowired
	private StepBuilderFactory myStepBuilderFactory;

	@Autowired
	private JobBuilderFactory myJobBuilderFactory;

	@Autowired
	private PidToIBaseResourceProcessor myPidToIBaseResourceProcessor;

	@Autowired
	private GoldenResourceAnnotatingProcessor myGoldenResourceAnnotatingProcessor;

	@Bean
	public BulkExportDaoSvc bulkExportDaoSvc() {
		return new BulkExportDaoSvc();
	}


	@Bean
	@Lazy
	@JobScope
	public MdmExpansionCacheSvc mdmExpansionCacheSvc() {
		return new MdmExpansionCacheSvc();
	}


	@Bean
	@Lazy
	public Job bulkExportJob() {
		return myJobBuilderFactory.get(BatchConstants.BULK_EXPORT_JOB_NAME)
			.validator(bulkExportJobParameterValidator())
			.start(createBulkExportEntityStep())
			.next(bulkExportPartitionStep())
			.next(closeJobStep())
			.build();
	}

	@Bean
	@Lazy
	@StepScope
	public CompositeItemProcessor<List<ResourcePersistentId>, List<IBaseResource>> inflateResourceThenAnnotateWithGoldenResourceProcessor() {
		CompositeItemProcessor processor = new CompositeItemProcessor<>();
		ArrayList<ItemProcessor> delegates = new ArrayList<>();
		delegates.add(myPidToIBaseResourceProcessor);
		delegates.add(myGoldenResourceAnnotatingProcessor);
		processor.setDelegates(delegates);
		return processor;
	}

	@Bean
	@Lazy
	public Job groupBulkExportJob() {
		return myJobBuilderFactory.get(BatchConstants.GROUP_BULK_EXPORT_JOB_NAME)
			.validator(groupBulkJobParameterValidator())
			.validator(bulkExportJobParameterValidator())
			.start(createBulkExportEntityStep())
			.next(groupPartitionStep())
			.next(closeJobStep())
			.build();
	}

	@Bean
	@Lazy
	public Job patientBulkExportJob() {
		return myJobBuilderFactory.get(BatchConstants.PATIENT_BULK_EXPORT_JOB_NAME)
			.validator(bulkExportJobParameterValidator())
			.start(createBulkExportEntityStep())
			.next(patientPartitionStep())
			.next(closeJobStep())
			.build();
	}

	@Bean
	public GroupIdPresentValidator groupBulkJobParameterValidator() {
		return new GroupIdPresentValidator();
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
	public JobParametersValidator bulkExportJobParameterValidator() {
		return new BulkExportJobParameterValidator();
	}

	//Writers
	@Bean
	public Step groupBulkExportGenerateResourceFilesStep() {
		return myStepBuilderFactory.get("groupBulkExportGenerateResourceFilesStep")
			.<List<ResourcePersistentId>, List<IBaseResource>>chunk(CHUNK_SIZE) //1000 resources per generated file, as the reader returns 10 resources at a time.
			.reader(groupBulkItemReader())
			.processor(inflateResourceThenAnnotateWithGoldenResourceProcessor())
			.writer(resourceToFileWriter())
			.listener(bulkExportGenerateResourceFilesStepListener())
			.build();
	}

	@Bean
	public Step bulkExportGenerateResourceFilesStep() {
		return myStepBuilderFactory.get(BatchConstants.BULK_EXPORT_GENERATE_RESOURCE_FILES_STEP)
			.<List<ResourcePersistentId>, List<IBaseResource>>chunk(CHUNK_SIZE) //1000 resources per generated file, as the reader returns 10 resources at a time.
			.reader(bulkItemReader())
			.processor(myPidToIBaseResourceProcessor)
			.writer(resourceToFileWriter())
			.listener(bulkExportGenerateResourceFilesStepListener())
			.build();
	}

	@Bean
	public Step patientBulkExportGenerateResourceFilesStep() {
		return myStepBuilderFactory.get("patientBulkExportGenerateResourceFilesStep")
			.<List<ResourcePersistentId>, List<IBaseResource>>chunk(CHUNK_SIZE) //1000 resources per generated file, as the reader returns 10 resources at a time.
			.reader(patientBulkItemReader())
			.processor(myPidToIBaseResourceProcessor)
			.writer(resourceToFileWriter())
			.listener(bulkExportGenerateResourceFilesStepListener())
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
	public BulkExportGenerateResourceFilesStepListener bulkExportGenerateResourceFilesStepListener() {
		return new BulkExportGenerateResourceFilesStepListener();
	}

	@Bean
	public Step bulkExportPartitionStep() {
		return myStepBuilderFactory.get("partitionStep")
			.partitioner(BatchConstants.BULK_EXPORT_GENERATE_RESOURCE_FILES_STEP, bulkExportResourceTypePartitioner())
			.step(bulkExportGenerateResourceFilesStep())
			.build();
	}

	@Bean
	public Step groupPartitionStep() {
		return myStepBuilderFactory.get("partitionStep")
			.partitioner("groupBulkExportGenerateResourceFilesStep", bulkExportResourceTypePartitioner())
			.step(groupBulkExportGenerateResourceFilesStep())
			.build();
	}

	@Bean
	public Step patientPartitionStep() {
		return myStepBuilderFactory.get("partitionStep")
			.partitioner("patientBulkExportGenerateResourceFilesStep", bulkExportResourceTypePartitioner())
			.step(patientBulkExportGenerateResourceFilesStep())
			.build();
	}


	@Bean
	@StepScope
	public GroupBulkItemReader groupBulkItemReader() {
		return new GroupBulkItemReader();
	}

	@Bean
	@StepScope
	public PatientBulkItemReader patientBulkItemReader() {
		return new PatientBulkItemReader();
	}

	@Bean
	@StepScope
	public BulkItemReader bulkItemReader() {
		return new BulkItemReader();
	}

	@Bean
	@JobScope
	public ResourceTypePartitioner bulkExportResourceTypePartitioner() {
		return new ResourceTypePartitioner();
	}

	@Bean
	@StepScope
	public ResourceToFileWriter resourceToFileWriter() {
		return new ResourceToFileWriter(myFhirContext, myDaoRegistry);
	}

}
