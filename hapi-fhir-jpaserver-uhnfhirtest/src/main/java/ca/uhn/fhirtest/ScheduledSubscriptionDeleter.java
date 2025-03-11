package ca.uhn.fhirtest;

import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import org.apache.commons.lang3.time.DateUtils;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * This is just a quick and dirty utility class to purge subscriptions on the
 * public test server after 1 day. It uses a Timer to automatically check for old
 * subscriptions periodically, and if it finds any with a lastUpdated date more than
 * 24 hours ago it deletes them. This is to prevent people's subscription testing
 * from hanging around and gumming up the server.
 */
public class ScheduledSubscriptionDeleter {

	private static final Logger ourLog = LoggerFactory.getLogger(ScheduledSubscriptionDeleter.class);

	@Autowired
	private DaoRegistry myDaoRegistry;

	private Timer myTimer;

	@EventListener(ContextRefreshedEvent.class)
	public void start() {
		if (myTimer == null) {
			myTimer = new Timer();
			myTimer.scheduleAtFixedRate(new MyTimerTask(), 0, DateUtils.MILLIS_PER_HOUR);
		}
	}

	@EventListener(ContextClosedEvent.class)
	public void stop() {
		myTimer.cancel();
		myTimer = null;
	}

	private class MyTimerTask extends TimerTask {

		@Override
		public void run() {
			deleteOldSubscriptions();
		}

		private void deleteOldSubscriptions() {
			if (myDaoRegistry.isResourceTypeSupported("Subscription")) {
				int count = 10;
				Date cutoff = DateUtils.addDays(new Date(), -1);

				IFhirResourceDao<?> subscriptionDao = myDaoRegistry.getResourceDao("Subscription");
				SearchParameterMap params = SearchParameterMap.newSynchronous().setCount(count);
				IBundleProvider subscriptions = subscriptionDao.search(params, new SystemRequestDetails());
				for (IBaseResource next : subscriptions.getResources(0, count)) {
					if (next.getMeta().getLastUpdated().before(cutoff)) {
						ourLog.info("Auto deleting old subscription: {}", next.getIdElement());
						subscriptionDao.delete(
								next.getIdElement().toUnqualifiedVersionless(), new SystemRequestDetails());
					}
				}
			}
		}
	}
}
