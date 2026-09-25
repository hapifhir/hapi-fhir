package ca.uhn.fhir.jpa.subscription.submit.interceptor;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.model.config.SubscriptionSettings;
import ca.uhn.fhir.jpa.subscription.match.matcher.matching.IResourceModifiedConsumer;
import ca.uhn.fhir.jpa.subscription.model.ResourceModifiedMessage;
import ca.uhn.fhir.subscription.api.IResourceModifiedMessagePersistenceSvc;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.MessageDeliveryException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SubscriptionMatcherInterceptorTest {

	@Mock
	private FhirContext myFhirContext;

	@Mock
	private SubscriptionSettings mySubscriptionSettings;

	@Mock
	private IResourceModifiedConsumer myResourceModifiedConsumer;

	@Mock
	private IResourceModifiedMessagePersistenceSvc myResourceModifiedMessagePersistenceSvc;

	@Mock
	private ResourceModifiedMessage theResourceModifiedMessage;

	@InjectMocks
	private SubscriptionMatcherInterceptor subscriptionMatcherInterceptor;

	@Test
	void testProcessResourceModifiedMessageQueuedImmediatelySuccess() {
		// Arrange
		when(mySubscriptionSettings.isSubscriptionChangeQueuedImmediately()).thenReturn(true);
		when(theResourceModifiedMessage.hasResourceType(myFhirContext, "Subscription")).thenReturn(true);

		// Act
		subscriptionMatcherInterceptor.processResourceModifiedMessage(theResourceModifiedMessage);

		// Assert
		verify(myResourceModifiedConsumer, times(1)).submitResourceModified(theResourceModifiedMessage);
		verify(myResourceModifiedMessagePersistenceSvc, never()).persist(any());
	}

	@Test
	void testProcessResourceModifiedMessageQueuedImmediatelyFailure() {
		// Arrange
		when(mySubscriptionSettings.isSubscriptionChangeQueuedImmediately()).thenReturn(true);
		when(theResourceModifiedMessage.hasResourceType(myFhirContext, "Subscription")).thenReturn(true);
		doThrow(new MessageDeliveryException("Submission failure")).when(myResourceModifiedConsumer).submitResourceModified(theResourceModifiedMessage);

		// Act
		subscriptionMatcherInterceptor.processResourceModifiedMessage(theResourceModifiedMessage);

		// Assert
		verify(myResourceModifiedConsumer, times(1)).submitResourceModified(theResourceModifiedMessage);
		verify(myResourceModifiedMessagePersistenceSvc, times(1)).persist(theResourceModifiedMessage);
	}

	@Test
	void testProcessResourceModifiedMessageNotQueuedImmediately() {
		// Arrange
		when(mySubscriptionSettings.isSubscriptionChangeQueuedImmediately()).thenReturn(false);

		// Act
		subscriptionMatcherInterceptor.processResourceModifiedMessage(theResourceModifiedMessage);

		// Assert
		verify(myResourceModifiedConsumer, never()).submitResourceModified(any());
		verify(myResourceModifiedMessagePersistenceSvc, times(1)).persist(theResourceModifiedMessage);
	}

	@Test
	void resourceDeletedDoesNotMutateSharedResourceId() {
		FhirContext ctx = FhirContext.forR4Cached();
		SubscriptionMatcherInterceptor interceptor = spy(new SubscriptionMatcherInterceptor());
		interceptor.setFhirContext(ctx);
		doNothing().when(interceptor).processResourceModifiedEvent(any(), any(), any());

		Patient patient = new Patient();
		patient.setId("Patient/123/_history/2");
		IdType previousId = new IdType("Patient/123/_history/1");

		interceptor.resourceDeleted(patient, null, previousId);

		assertThat(patient.getIdElement().getValue()).isEqualTo("Patient/123/_history/2");
		ArgumentCaptor<IBaseResource> matchingResource = ArgumentCaptor.forClass(IBaseResource.class);
		verify(interceptor)
				.processResourceModifiedEvent(
						matchingResource.capture(), eq(ResourceModifiedMessage.OperationTypeEnum.DELETE), isNull());
		assertThat(matchingResource.getValue()).isNotSameAs(patient);
		assertThat(matchingResource.getValue().getIdElement().getValue()).isEqualTo("Patient/123/_history/1");
	}
}
