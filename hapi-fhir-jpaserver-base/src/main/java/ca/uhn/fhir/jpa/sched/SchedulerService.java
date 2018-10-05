package ca.uhn.fhir.jpa.sched;

import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.Validate;
import org.quartz.*;
import org.quartz.impl.JobDetailImpl;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Properties;
import java.util.Set;

public class SchedulerService implements ISchedulerService {

	private static final Logger ourLog = LoggerFactory.getLogger(SchedulerService.class);
	private Scheduler myScheduler;

	@PostConstruct
	public void start() throws SchedulerException {
		StdSchedulerFactory factory = new StdSchedulerFactory();
		factory.initialize(quartzProperties());
		myScheduler = factory.getScheduler();

		ourLog.info("Starting task scheduler");
		myScheduler.start();
	}

	@PreDestroy
	public void stop() throws SchedulerException {
		ourLog.info("Shutting down task scheduler...");
		myScheduler.shutdown(true);
	}


	public void scheduleFixedDelay(long theIntervalMillis, ScheduledJobDefinition theJobDefinition) {
		Validate.isTrue(theIntervalMillis >= 100);

		Validate.notNull(theJobDefinition);
		Validate.notNull(theJobDefinition.getJobClass());
		Validate.notBlank(theJobDefinition.getName());

		JobKey jobKey = new JobKey(theJobDefinition.getName());

		JobDetailImpl jobDetail = new JobDetailImpl();
		jobDetail.setJobClass(theJobDefinition.getJobClass());
		jobDetail.setKey(jobKey);
		jobDetail.setName(theJobDefinition.getName());

		ScheduleBuilder<? extends Trigger> schedule = SimpleScheduleBuilder
			.simpleSchedule()
			.withIntervalInMilliseconds(theIntervalMillis)
			.repeatForever();

		Trigger trigger = TriggerBuilder.newTrigger()
			.forJob(jobDetail)
			.startNow()
			.withSchedule(schedule)
			.build();

		Set<? extends Trigger> triggers = Sets.newHashSet(trigger);
		try {
			myScheduler.scheduleJob(jobDetail, triggers, true);
		} catch (SchedulerException e) {
			ourLog.error("Failed to schedule job", e);
			throw new InternalErrorException(e);
		}
	}


	protected Properties quartzProperties() {
		Properties properties = new Properties();
		properties.put("org.quartz.threadPool.threadCount", "4");
		properties.put("org.quartz.threadPool.threadNamePrefix", myThread);
		return properties;
	}


}
