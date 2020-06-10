package ca.uhn.fhir.jpa.batch.config;

import org.springframework.batch.core.configuration.annotation.BatchConfigurer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.explore.support.MapJobExplorerFactoryBean;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.MapJobRepositoryFactoryBean;
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import javax.annotation.PostConstruct;

@EnableBatchProcessing
@Configuration
public class InMemoryJobRepositoryBatchConfig implements BatchConfigurer {

	private PlatformTransactionManager myPlatformTransactionManager;
	private JobLauncher myJobLauncher;
	private JobRepository myJobRepository;
	private JobExplorer myJobExplorer;

	@Override
	public PlatformTransactionManager getTransactionManager() {
		return myPlatformTransactionManager;
	}

	@Override
	public JobRepository getJobRepository() {
		return myJobRepository;
	}

	@Override
	public JobLauncher getJobLauncher() {
		return myJobLauncher;
	}

	@Override
	public JobExplorer getJobExplorer() {
		return myJobExplorer;
	}

	@PostConstruct
	public void setup() throws Exception{
		if (myPlatformTransactionManager == null) {
			myPlatformTransactionManager = new ResourcelessTransactionManager();
		}
		MapJobRepositoryFactoryBean jobRepositoryFactoryBean = new MapJobRepositoryFactoryBean(myPlatformTransactionManager);
		jobRepositoryFactoryBean.afterPropertiesSet();
		myJobRepository = jobRepositoryFactoryBean.getObject();

		MapJobExplorerFactoryBean jobExplorerFactoryBean = new MapJobExplorerFactoryBean(jobRepositoryFactoryBean);
		jobExplorerFactoryBean.afterPropertiesSet();
		myJobExplorer = jobExplorerFactoryBean.getObject();

		SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
		jobLauncher.setJobRepository(myJobRepository);
		jobLauncher.afterPropertiesSet();
		myJobLauncher = jobLauncher;
	}
}
