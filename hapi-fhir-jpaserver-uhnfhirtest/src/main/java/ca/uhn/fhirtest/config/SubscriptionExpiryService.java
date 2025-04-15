package ca.uhn.fhirtest.config;

import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.model.sched.HapiJob;
import ca.uhn.fhir.jpa.model.sched.ISchedulerService;
import ca.uhn.fhir.jpa.model.sched.ScheduledJobDefinition;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.model.dstu2.resource.Subscription;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.param.DateParam;
import ca.uhn.fhir.rest.param.ParamPrefixEnum;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.subscription.SubscriptionConstants;
import ca.uhn.fhir.util.SubscriptionUtil;
import org.apache.commons.lang3.time.DateUtils;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;

import java.util.Date;

import static ca.uhn.fhir.rest.api.Constants.PARAM_LASTUPDATED;

public class SubscriptionExpiryService {
	private static final Logger ourLog = LoggerFactory.getLogger(SubscriptionExpiryService.class);

	@Autowired
	private ISchedulerService mySchedulerSvc;

	@Autowired
	private DaoRegistry myDaoRegistry;

	@EventListener(ContextRefreshedEvent.class)
	public void start() {
		ScheduledJobDefinition jobDetail = new ScheduledJobDefinition();
		jobDetail.setId(SubscriptionExpiryServiceJob.class.getName());
		jobDetail.setJobClass(SubscriptionExpiryServiceJob.class);
		mySchedulerSvc.scheduleLocalJob(DateUtils.MILLIS_PER_HOUR, jobDetail);

		doPass();
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	private void doPass() {
		Date cutoff = DateUtils.addHours(new Date(), -1);

		SearchParameterMap params = new SearchParameterMap();
		params.setLoadSynchronousUpTo(1000);
		params.add(Subscription.SP_STATUS, new TokenParam("active"));
		params.add(PARAM_LASTUPDATED, new DateParam(ParamPrefixEnum.LESSTHAN, cutoff));

		IFhirResourceDao subscriptionDao = myDaoRegistry.getResourceDao("Subscription");
		IBundleProvider results = subscriptionDao.search(params, new SystemRequestDetails());
		for (IBaseResource subscriptionToDeactivate : results.getAllResources()) {
			ourLog.info("Deactivating Subscription (status=off): {}", subscriptionToDeactivate.getIdElement());
			SubscriptionUtil.setStatus(
					myDaoRegistry.getFhirContext(), subscriptionToDeactivate, SubscriptionConstants.OFF_STATUS);
			subscriptionDao.update(subscriptionToDeactivate, new SystemRequestDetails());
		}
	}

	public static class SubscriptionExpiryServiceJob implements HapiJob {

		@Autowired
		private SubscriptionExpiryService mySvc;

		@Override
		public void execute(JobExecutionContext context) {
			mySvc.doPass();
		}
	}
}
