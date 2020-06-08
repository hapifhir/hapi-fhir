package ca.uhn.fhir.jpa.bulk.job;

import ca.uhn.fhir.jpa.batch.processors.PidToIBaseResourceProcessor;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;

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

	@Autowired
	private TaskExecutor myTaskExecutor;

	@Bean
	public Job bulkExportJob() {
		return myJobBuilderFactory.get("bulkExportJob")
			.start(partitionStep())
			.listener(bulkExportJobCompletionListener())
			.build();
	}

	@Bean
	public Step bulkExportGenerateResourceFilesStep() {
		return myStepBuilderFactory.get("bulkExportGenerateResourceFilesStep")
			.<ResourcePersistentId, IBaseResource> chunk(1000) //1000 resources per generated file
			//TODO should we potentially make this configurable?
			.reader(bulkItemReader(null))
			.processor(myPidToIBaseResourceProcessor)
			.writer(resourceToFileWriter())
			.build();
	}

	@Bean
	@JobScope
	public BulkExportJobStatusChangeListener bulkExportJobCompletionListener() {
		return new BulkExportJobStatusChangeListener();
	}

	@Bean
	public Step partitionStep() {
		return myStepBuilderFactory.get("partitionStep")
			.partitioner("bulkExportGenerateResourceFilesStep", partitioner(null))
			.step(bulkExportGenerateResourceFilesStep())
			.taskExecutor(myTaskExecutor)
			.build();
	}

	@Bean
	@StepScope
	public BulkItemReader bulkItemReader(@Value("#{jobParameters['jobUUID']}") String theJobUUID) {
		BulkItemReader bulkItemReader = new BulkItemReader();
		bulkItemReader.setJobUUID(theJobUUID);
		return bulkItemReader;
	}

	@Bean
	@JobScope
	public ResourceTypePartitioner partitioner(@Value("#{jobParameters['jobUUID']}") String theJobUUID) {
		return new ResourceTypePartitioner(theJobUUID);
	}

	@Bean
	@StepScope
	public ItemWriter<IBaseResource> resourceToFileWriter() {
		return new ResourceToFileWriter();
	}

}
