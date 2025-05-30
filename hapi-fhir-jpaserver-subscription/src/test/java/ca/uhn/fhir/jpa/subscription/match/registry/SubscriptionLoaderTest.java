package ca.uhn.fhir.jpa.subscription.match.registry;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.cache.IResourceChangeListenerCache;
import ca.uhn.fhir.jpa.cache.IResourceChangeListenerRegistry;
import ca.uhn.fhir.jpa.model.sched.ISchedulerService;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.subscription.match.matcher.subscriber.SubscriptionActivatingListener;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.server.SimpleBundleProvider;
import ca.uhn.fhir.rest.server.exceptions.ResourceGoneException;
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;
import ca.uhn.fhir.subscription.SubscriptionConstants;
import ca.uhn.test.util.LogbackTestExtension;
import ca.uhn.test.util.LogbackTestExtensionAssert;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Subscription;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SubscriptionLoaderTest {

	@Spy
	private FhirContext myFhirContext = FhirContext.forR4Cached();

	@RegisterExtension
	LogbackTestExtension myLogCapture = new LogbackTestExtension(SubscriptionLoader.class);

	@Mock
	private SubscriptionRegistry mySubscriptionRegistry;

	@Mock
	private DaoRegistry myDaoRegistry;

	@Mock
	private SubscriptionActivatingListener mySubscriptionActivatingInterceptor;

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

	@Mock
	private IFhirResourceDao mySubscriptionDao;
	@InjectMocks
	private SubscriptionLoader mySubscriptionLoader;

	@BeforeEach
	public void init() {
		when(myResourceChangeListenerRegistry.registerResourceResourceChangeListener(
			anyString(),
			any(SearchParameterMap.class),
			any(SubscriptionLoader.class),
			anyLong()
		)).thenReturn(mySubscriptionCache);

		when(myDaoRegistry.getResourceDaoOrNull("Subscription")).thenReturn(mySubscriptionDao);

		mySubscriptionLoader.registerListener();
	}

	private IBundleProvider getSubscriptionList(List<IBaseResource> theReturnedResource) {
		IBundleProvider subscriptionList = new SimpleBundleProvider(theReturnedResource);

		subscriptionList.getAllResources().addAll(theReturnedResource);
		return subscriptionList;
	}

	@Test
	public void testUnfetchableResourceDoesntBlockLoading() {
		// setup
		Subscription subscription = new Subscription();
		subscription.setId("Subscription/A");

		// when
		when(myDaoRegistry.getResourceDao("Subscription"))
			.thenReturn(mySubscriptionDao);
		when(myDaoRegistry.isResourceTypeSupported("Subscription"))
			.thenReturn(true);
		when(mySubscriptionDao.read(eq(new IdType("Subscription/123")), any())).thenThrow(new ResourceGoneException(new IdType("Subscription/123")));
		when(mySubscriptionDao.read(eq(new IdType("Subscription/A")), any())).thenReturn(subscription);

		// test
		mySubscriptionLoader.handleInit(List.of(new IdType("Subscription/123"), new IdType("Subscription/A")));

		// verify
		verify(mySubscriptionDao).read(eq(new IdType("Subscription/123")), any());
		verify(mySubscriptionDao).read(eq(new IdType("Subscription/A")), any());
		verifyNoMoreInteractions(mySubscriptionDao);
		verify(mySubscriptionRegistry, times(1)).unregisterAllSubscriptionsNotInCollection(any());
		verify(mySubscriptionRegistry, times(1)).registerSubscriptionUnlessAlreadyRegistered(subscription);
		verifyNoMoreInteractions(mySubscriptionRegistry);
	}



	@Test
	public void refreshCache_withInactiveSubscriptionFailing_Syncs() {
		// setup
		Subscription subscription = new Subscription();
		subscription.setId("Subscription/123");
		subscription.setError("THIS IS AN ERROR");

		// when
		when(myDaoRegistry.getResourceDao("Subscription"))
			.thenReturn(mySubscriptionDao);
		when(myDaoRegistry.isResourceTypeSupported("Subscription"))
				.thenReturn(true);
		when(mySubscriptionDao.searchForResources(any(SearchParameterMap.class), any(SystemRequestDetails.class)))
			.thenReturn(List.of(subscription));

		when(mySubscriptionActivatingInterceptor.activateSubscriptionIfRequired(any(IBaseResource.class)))
			.thenReturn(false);

		when(mySubscriptionActivatingInterceptor.isChannelTypeSupported(any(IBaseResource.class)))
			.thenReturn(true);

		when(mySubscriptionCanonicalizer.getSubscriptionStatus(any())).thenReturn(SubscriptionConstants.REQUESTED_STATUS);

		// test
		mySubscriptionLoader.requestRefresh();

		// verify
		verify(mySubscriptionDao)
			.searchForResources(any(SearchParameterMap.class), any(SystemRequestDetails.class));

		LogbackTestExtensionAssert.assertThat(myLogCapture).hasErrorMessage(
			"Subscription " + subscription.getIdElement().getIdPart() + " could not be activated.");
		LogbackTestExtensionAssert.assertThat(myLogCapture).hasMessage(subscription.getError());
	}
}
