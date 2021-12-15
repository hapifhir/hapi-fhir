package ca.uhn.fhir.jpa.subscription.match.matcher.subscriber;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.partition.SystemRequestDetails;
import ca.uhn.fhir.jpa.subscription.match.matcher.matching.SubscriptionStrategyEvaluator;
import ca.uhn.fhir.jpa.subscription.match.registry.SubscriptionCanonicalizer;
import ca.uhn.fhir.jpa.subscription.match.registry.SubscriptionConstants;
import ca.uhn.fhir.jpa.subscription.match.registry.SubscriptionRegistry;
import ca.uhn.fhir.jpa.subscription.model.CanonicalSubscriptionChannelType;
import ca.uhn.fhir.rest.server.exceptions.ResourceGoneException;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Subscription;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.internal.util.collections.Sets;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.LoggerFactory;

import java.util.List;

@ExtendWith(MockitoExtension.class)
public class SubscriptionActivatingSubscriberTest {

	private Logger ourLogger;

	@Mock
	private Appender<ILoggingEvent> myAppender;

	@Spy
	private FhirContext fhirContext = FhirContext.forR4Cached();

	@Mock
	private SubscriptionRegistry mySubscriptionRegistry;

	@Mock
	private DaoRegistry myDaoRegistry;

	@Mock
	private SubscriptionCanonicalizer mySubscriptionCanonicallizer;

	@Mock
	private DaoConfig myDaoConfig;

	@Mock
	private SubscriptionStrategyEvaluator mySubscriptionStrategyEvaluator;

	@InjectMocks
	private SubscriptionActivatingSubscriber mySubscriptionActivatingSubscriber;

	private Level myStoredLogLevel;

	@BeforeEach
	public void init() {
		ourLogger = (Logger) LoggerFactory.getLogger(SubscriptionActivatingSubscriber.class);

		myStoredLogLevel = ourLogger.getLevel();
		ourLogger.addAppender(myAppender);
	}

	@AfterEach
	public void end() {
		ourLogger.detachAppender(myAppender);
		ourLogger.setLevel(myStoredLogLevel);
	}

	@Test
	public void activateSubscriptionIfRequired_activationFails_setsStatusOfSubscriptionToError() {
		CanonicalSubscriptionChannelType type = CanonicalSubscriptionChannelType.RESTHOOK;
		Subscription subscription = new Subscription();
		subscription.setId("Subscription/123");
		String exceptionMsg = "Gone Exception";
		int totalInfoLogs = 1;

		ourLogger.setLevel(Level.ERROR);
		IFhirResourceDao dao = Mockito.mock(IFhirResourceDao.class);

		// when
		Mockito.when(mySubscriptionCanonicallizer.getChannelType(Mockito.any(IBaseResource.class)))
			.thenReturn(type);
		Mockito.when(myDaoConfig.getSupportedSubscriptionTypes())
			.thenReturn(Sets.newSet(type.toCanonical()));
		Mockito.when(mySubscriptionCanonicallizer.getSubscriptionStatus(Mockito.any(IBaseResource.class)))
			.thenReturn(SubscriptionConstants.REQUESTED_STATUS);
		Mockito.when(myDaoRegistry.getSubscriptionDao())
			.thenReturn(dao);
		Mockito.when(dao.read(Mockito.any(IIdType.class), Mockito.any(SystemRequestDetails.class)))
			.thenThrow(new ResourceGoneException(exceptionMsg));

		// test
		boolean isActivated = mySubscriptionActivatingSubscriber.activateSubscriptionIfRequired(subscription);

		// verify
		Assertions.assertFalse(isActivated);
		ArgumentCaptor<IBaseResource> captor = ArgumentCaptor.forClass(IBaseResource.class);
		Mockito.verify(dao).update(captor.capture(), Mockito.any(SystemRequestDetails.class));
		IBaseResource savedResource = captor.getValue();
		Assertions.assertTrue(savedResource instanceof Subscription);
		Assertions.assertEquals(Subscription.SubscriptionStatus.ERROR, ((Subscription)savedResource).getStatus());

		ArgumentCaptor<ILoggingEvent> appenderCaptor = ArgumentCaptor.forClass(ILoggingEvent.class);
		Mockito.verify(myAppender, Mockito.times(totalInfoLogs))
			.doAppend(appenderCaptor.capture());
		List<ILoggingEvent> events = appenderCaptor.getAllValues();
		Assertions.assertEquals(totalInfoLogs, events.size());
		Assertions.assertTrue(events.get(0).getMessage().contains(exceptionMsg));
	}
}
