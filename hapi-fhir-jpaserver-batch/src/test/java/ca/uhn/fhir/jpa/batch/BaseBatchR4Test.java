package ca.uhn.fhir.jpa.batch;

import ca.uhn.fhir.jpa.batch.config.BatchJobConfig;
import ca.uhn.fhir.jpa.batch.config.InMemoryJobRepositoryBatchConfig;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.PlatformTransactionManager;

import static org.slf4j.LoggerFactory.getLogger;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {BatchJobConfig.class, InMemoryJobRepositoryBatchConfig.class})
abstract public class BaseBatchR4Test {
 	private static final Logger ourLog = getLogger(BaseBatchR4Test.class);

 	@Autowired
	protected PlatformTransactionManager myPlatformTransactionManager;
	@Autowired
	protected JobLauncher myJobLauncher;
	@Autowired
	protected Job myJob;

}
