package ca.uhn.fhirtest.interceptor;

import ca.uhn.fhir.jpa.model.sched.HapiJob;
import ca.uhn.fhir.jpa.model.sched.IHasScheduledJobs;
import ca.uhn.fhir.jpa.model.sched.ISchedulerService;
import ca.uhn.fhir.jpa.model.sched.ScheduledJobDefinition;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.client.apache.ApacheRestfulClientFactory;
import ca.uhn.fhir.rest.server.interceptor.InterceptorAdapter;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.util.UrlUtil;
import jakarta.annotation.PreDestroy;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import static org.apache.commons.lang3.StringUtils.defaultIfBlank;
import static org.apache.commons.lang3.StringUtils.isBlank;

public class AnalyticsInterceptor extends InterceptorAdapter implements IHasScheduledJobs {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(AnalyticsInterceptor.class);
	private final LinkedList<AnalyticsEvent> myEventBuffer = new LinkedList<>();
	private String myAnalyticsTid;
	private int myCollectThreshold = 100000;
	private String myHostname;
	private HttpClient myHttpClient;
	private long mySubmitPeriod = 60000;
	private int mySubmitThreshold = 1000;

	/**
	 * Constructor
	 */
	public AnalyticsInterceptor() {
		myHttpClient = new ApacheRestfulClientFactory().getNativeHttpClient();
		try {
			myHostname = InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			myHostname = "Unknown";
		}
	}

	@Override
	public void scheduleJobs(ISchedulerService theSchedulerService) {
		ScheduledJobDefinition jobDetail = new ScheduledJobDefinition();
		jobDetail.setId(getClass().getName());
		jobDetail.setJobClass(Job.class);
		theSchedulerService.scheduleLocalJob(5000, jobDetail);
	}

	@PreDestroy
	public void stop() throws IOException {
		if (myHttpClient instanceof CloseableHttpClient) {
			((CloseableHttpClient) myHttpClient).close();
		}
	}

	private void doFlush() {
		List<AnalyticsEvent> eventsToFlush;
		synchronized (myEventBuffer) {
			int size = myEventBuffer.size();
			if (size > 20) {
				size = 20;
			}
			eventsToFlush = new ArrayList<>(size);
			for (int i = 0; i < size; i++) {
				AnalyticsEvent nextEvent = myEventBuffer.pollFirst();
				if (nextEvent != null) {
					eventsToFlush.add(nextEvent);
				}
			}
		}

		StringBuilder b = new StringBuilder();
		for (AnalyticsEvent next : eventsToFlush) {
			b.append("v=1");
			b.append("&tid=").append(myAnalyticsTid);

			b.append("&t=event");
			b.append("&an=")
					.append(UrlUtil.escapeUrlParam(myHostname))
					.append('+')
					.append(UrlUtil.escapeUrlParam(next.getApplicationName()));
			b.append("&ec=").append(next.getResourceName());
			b.append("&ea=").append(next.getRestOperation());

			b.append("&cid=").append(next.getClientId());
			b.append("&uip=").append(UrlUtil.escapeUrlParam(next.getSourceIp()));
			b.append("&ua=").append(UrlUtil.escapeUrlParam(next.getUserAgent()));
			b.append("\n");
		}

		String contents = b.toString();
		HttpPost post = new HttpPost("https://www.google-analytics.com/batch");
		post.setEntity(new StringEntity(contents, ContentType.APPLICATION_FORM_URLENCODED));
		try (CloseableHttpResponse response = (CloseableHttpResponse) myHttpClient.execute(post)) {
			ourLog.trace("Analytics response: {}", response);
			ourLog.info(
					"Flushed {} analytics events and got HTTP {} {}",
					eventsToFlush.size(),
					response.getStatusLine().getStatusCode(),
					response.getStatusLine().getReasonPhrase());
		} catch (Exception e) {
			ourLog.error("Failed to submit analytics:", e);
		}
	}

	private synchronized void flush() {
		int pendingEvents;
		synchronized (myEventBuffer) {
			pendingEvents = myEventBuffer.size();
		}

		if (pendingEvents == 0) {
			return;
		}

		if (System.currentTimeMillis() > mySubmitPeriod) {
			doFlush();
			return;
		}

		if (pendingEvents >= mySubmitThreshold) {
			doFlush();
		}
	}

	@Override
	public void incomingRequestPreHandled(RestOperationTypeEnum theOperation, RequestDetails theRequest) {
		ServletRequestDetails details = (ServletRequestDetails) theRequest;

		// Make sure we only send one event per request
		if (details.getUserData().containsKey(getClass().getName())) {
			return;
		}
		details.getUserData().put(getClass().getName(), "");

		String sourceIp = details.getHeader("x-forwarded-for");
		if (isBlank(sourceIp)) {
			sourceIp = details.getServletRequest().getRemoteAddr();
		}
		if (sourceIp.contains(", ")) {
			sourceIp = sourceIp.substring(0, sourceIp.indexOf(", "));
		}

		AnalyticsEvent event = new AnalyticsEvent();
		event.setSourceIp(sourceIp);
		event.setRestOperation(theOperation);
		event.setUserAgent(details.getHeader("User-Agent"));
		event.setApplicationName(details.getServletRequest().getServletPath());
		event.setRestOperation(theOperation);
		event.setResourceName(defaultIfBlank(details.getResourceName(), "SERVER"));
		event.setClientId(UUID.randomUUID().toString());

		synchronized (myEventBuffer) {
			if (myEventBuffer.size() > myCollectThreshold) {
				ourLog.warn(
						"Not collecting analytics on request! Event buffer has {} items in it", myEventBuffer.size());
			}
			myEventBuffer.add(event);
		}
	}

	public void setAnalyticsTid(String theAnalyticsTid) {
		myAnalyticsTid = theAnalyticsTid;
	}

	public static class Job implements HapiJob {
		@Autowired
		private AnalyticsInterceptor myAnalyticsInterceptor;

		@Override
		public void execute(JobExecutionContext theContext) {
			myAnalyticsInterceptor.flush();
		}
	}

	public static class AnalyticsEvent {
		private String myApplicationName;
		private String myClientId;
		private String myResourceName;
		private RestOperationTypeEnum myRestOperation;
		private String mySourceIp;

		private String myUserAgent;

		String getApplicationName() {
			return myApplicationName;
		}

		void setApplicationName(String theApplicationName) {
			myApplicationName = theApplicationName;
		}

		String getClientId() {
			return myClientId;
		}

		void setClientId(String theClientId) {
			myClientId = theClientId;
		}

		String getResourceName() {
			return myResourceName;
		}

		void setResourceName(String theResourceName) {
			myResourceName = theResourceName;
		}

		RestOperationTypeEnum getRestOperation() {
			return myRestOperation;
		}

		void setRestOperation(RestOperationTypeEnum theRestOperation) {
			myRestOperation = theRestOperation;
		}

		String getSourceIp() {
			return mySourceIp;
		}

		void setSourceIp(String theSourceIp) {
			mySourceIp = theSourceIp;
		}

		String getUserAgent() {
			return myUserAgent;
		}

		void setUserAgent(String theUserAgent) {
			myUserAgent = theUserAgent;
		}
	}
}
