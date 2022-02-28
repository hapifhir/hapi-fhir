package ca.uhn.fhir.jpa.subscription.match.registry;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.cache.IResourceChangeListenerCache;
import ca.uhn.fhir.jpa.cache.IResourceChangeListenerRegistry;
import ca.uhn.fhir.jpa.model.sched.ISchedulerService;
import ca.uhn.fhir.jpa.partition.SystemRequestDetails;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.subscription.match.matcher.subscriber.SubscriptionActivatingSubscriber;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.server.SimpleBundleProvider;
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Subscription;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SubscriptionLoaderTest {

	private Logger ourLogger;

	@Spy
	private FhirContext myFhirContext = FhirContext.forR4Cached();

	@Mock
	private Appender<ILoggingEvent> myAppender;

	@Mock
	private SubscriptionRegistry mySubscriptionRegistry;

	@Mock
	private DaoRegistry myDaoRegistery;

	@Mock
	private SubscriptionActivatingSubscriber mySubscriptionActivatingInterceptor;

	@Mock
	private ISearchParamRegistry mySearchParamRegistry;

	@Mock
	private IResourceChangeListenerRegistry myResourceChangeListenerRegistry;

	@Mock
	private ISchedulerService mySchedulerSvc;

	// used in init. But can be used elsewhere
	@Mock
	private IResourceChangeListenerCache mySubscriptionCache;

	@Mock
	private SubscriptionCanonicalizer mySubscriptionCanonicalizer;

	@InjectMocks
	private SubscriptionLoader mySubscriptionLoader;

	private Level myStoredLogLevel;

	@BeforeEach
	public void init() {
		ourLogger = (Logger) LoggerFactory.getLogger(SubscriptionLoader.class);

		myStoredLogLevel = ourLogger.getLevel();
		ourLogger.addAppender(myAppender);

		when(myResourceChangeListenerRegistry.registerResourceResourceChangeListener(
			anyString(),
			any(SearchParameterMap.class),
			any(SubscriptionLoader.class),
			anyLong()
		)).thenReturn(mySubscriptionCache);

		mySubscriptionLoader.registerListener();
	}

	@AfterEach
	public void end() {
		ourLogger.detachAppender(myAppender);
		ourLogger.setLevel(myStoredLogLevel);
	}

	private IBundleProvider getSubscriptionList(List<IBaseResource> theReturnedResource) {
		IBundleProvider subscriptionList = new SimpleBundleProvider(theReturnedResource);

		subscriptionList.getAllResources().addAll(theReturnedResource);
		return subscriptionList;
	}

	@Test
	public void syncSubscriptions_withInactiveSubscriptionFailing_Syncs() {
		// setup
		Subscription subscription = new Subscription();
		subscription.setId("Subscription/123");
		subscription.setError("THIS IS AN ERROR");
		IFhirResourceDao<Subscription> subscriptionDao = mock(IFhirResourceDao.class);

		ourLogger.setLevel(Level.ERROR);

		// when
		when(myDaoRegistery.getSubscriptionDao())
				.thenReturn(subscriptionDao);
		when(myDaoRegistery.isResourceTypeSupported(anyString()))
				.thenReturn(true);
		when(subscriptionDao.search(any(SearchParameterMap.class), any(SystemRequestDetails.class)))
			.thenReturn(getSubscriptionList(
				Collections.singletonList(subscription)
			));
		when(mySchedulerSvc.isStopping())
				.thenReturn(false);

		when(mySubscriptionActivatingInterceptor.activateSubscriptionIfRequired(any(IBaseResource.class)))
				.thenReturn(false);

		when(mySubscriptionCanonicalizer.getSubscriptionStatus(any())).thenReturn(SubscriptionConstants.REQUESTED_STATUS);
		
		// test
		mySubscriptionLoader.syncSubscriptions();

		// verify
		verify(subscriptionDao)
			.search(any(SearchParameterMap.class), any(SystemRequestDetails.class));

		ArgumentCaptor<ILoggingEvent> eventCaptor = ArgumentCaptor.forClass(ILoggingEvent.class);
		verify(myAppender).doAppend(eventCaptor.capture());
		String actual = "Subscription "
			+ subscription.getIdElement().getIdPart()
			+ " could not be activated.";
		String msg = eventCaptor.getValue().getMessage();
		Assertions.assertTrue(msg
			.contains(actual),
			String.format("Expected %s, actual %s", msg, actual));
		Assertions.assertTrue(msg.contains(subscription.getError()));
	}
}
