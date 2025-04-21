package ca.uhn.fhir.jpa.subscription.match.matcher.subscriber;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.model.config.SubscriptionSettings;
import ca.uhn.fhir.jpa.subscription.match.registry.SubscriptionCanonicalizer;
import ca.uhn.fhir.jpa.subscription.match.registry.SubscriptionRegistry;
import ca.uhn.fhir.jpa.subscription.model.ResourceModifiedJsonMessage;
import ca.uhn.fhir.jpa.subscription.model.ResourceModifiedMessage;
import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.server.messaging.BaseResourceMessage;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.r4.model.InstantType;
import org.hl7.fhir.r4.model.Subscription;
import org.hl7.fhir.r4.model.codesystems.SubscriptionStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.system.OutputCaptureExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class, OutputCaptureExtension.class})
public class SubscriptionRegisteringListenerTest {

	@Spy
	private FhirContext myFhirContext = FhirContext.forR4Cached();
	@Mock
	private SubscriptionRegistry mySubscriptionRegistry;
	@Spy
	private SubscriptionCanonicalizer mySubscriptionCanonicalizer = new SubscriptionCanonicalizer(myFhirContext, new SubscriptionSettings());
	@Mock
	private DaoRegistry myDaoRegistry;
	@Mock
	private IFhirResourceDao<Subscription> mySubscriptionDao;
	@InjectMocks
	private SubscriptionRegisteringListener mySubscriptionRegisteringListener;
	@Captor
	private ArgumentCaptor<RequestDetails> requestDetailsCaptor;

	private Subscription mySubscription;

	@BeforeEach
	public void beforeEach() {
		mySubscription = buildSubscription();
	}

	@Nonnull
	private static Subscription buildSubscription() {
		Subscription subscription = new Subscription();
		subscription.setId("Subscription/testrest");
		subscription.setStatus(Subscription.SubscriptionStatus.ACTIVE);
		return subscription;
	}

	@Test
	public void testHandleMessageSubscriptionResourceGone(){
		ResourceModifiedMessage resourceModifiedMessage = new ResourceModifiedMessage(myFhirContext, mySubscription, BaseResourceMessage.OperationTypeEnum.CREATE);
		ResourceModifiedJsonMessage message = new ResourceModifiedJsonMessage(resourceModifiedMessage);

		when(myDaoRegistry.getResourceDao("Subscription")).thenReturn(mySubscriptionDao);
		Subscription deletedSubscription = buildSubscription();
		ResourceMetadataKeyEnum.DELETED_AT.put(deletedSubscription, InstantType.withCurrentTime());
		when(mySubscriptionDao.read(any(), any(), eq(true))).thenReturn(deletedSubscription);

		mySubscriptionRegisteringListener.handleMessage(message);
		verify(mySubscriptionRegistry, times(1)).unregisterSubscriptionIfRegistered(any());
		verify(mySubscriptionRegistry, never()).registerSubscriptionUnlessAlreadyRegistered(any());
	}

	@Test
	public void testHandleMessageSubscriptionActiveStatus(){
		ResourceModifiedMessage resourceModifiedMessage = new ResourceModifiedMessage(myFhirContext, mySubscription, BaseResourceMessage.OperationTypeEnum.CREATE);
		ResourceModifiedJsonMessage message = new ResourceModifiedJsonMessage(resourceModifiedMessage);

		when(myDaoRegistry.getResourceDao("Subscription")).thenReturn(mySubscriptionDao);
		when(mySubscriptionDao.read(any(), any(), eq(true))).thenReturn(mySubscription);
		when(mySubscriptionCanonicalizer.getSubscriptionStatus(mySubscription)).thenReturn(SubscriptionStatus.ACTIVE.toCode());

		mySubscriptionRegisteringListener.handleMessage(message);
		verify(mySubscriptionRegistry, never()).unregisterSubscriptionIfRegistered(any());
		verify(mySubscriptionRegistry, times(1)).registerSubscriptionUnlessAlreadyRegistered(any());
	}

	@Test
	public void testHandleMessageSubscriptionErrorStatus(){
		ResourceModifiedMessage resourceModifiedMessage = new ResourceModifiedMessage(myFhirContext, mySubscription, BaseResourceMessage.OperationTypeEnum.CREATE);
		ResourceModifiedJsonMessage message = new ResourceModifiedJsonMessage(resourceModifiedMessage);

		when(myDaoRegistry.getResourceDao("Subscription")).thenReturn(mySubscriptionDao);
		when(mySubscriptionDao.read(any(), any(), eq(true))).thenReturn(mySubscription);
		when(mySubscriptionCanonicalizer.getSubscriptionStatus(mySubscription)).thenReturn(SubscriptionStatus.ERROR.toCode());

		mySubscriptionRegisteringListener.handleMessage(message);
		verify(mySubscriptionRegistry, times(1)).unregisterSubscriptionIfRegistered(any());
		verify(mySubscriptionRegistry, never()).registerSubscriptionUnlessAlreadyRegistered(any());
	}

	@Test
	public void testHandleMessagePartitionWithNullPartitionName(){
		List<Integer> partitionIds = Arrays.asList((Integer)null);
		List<String> partitionNames = Arrays.asList((String)null);
		LocalDate localDate = null;

		RequestPartitionId requestPartitionId = RequestPartitionId.forPartitionIdsAndNames(partitionNames, partitionIds, localDate);
		ResourceModifiedMessage resourceModifiedMessage = new ResourceModifiedMessage(myFhirContext, mySubscription, BaseResourceMessage.OperationTypeEnum.CREATE);
		resourceModifiedMessage.setPartitionId(requestPartitionId);
		ResourceModifiedJsonMessage message = new ResourceModifiedJsonMessage(resourceModifiedMessage);

		when(myDaoRegistry.getResourceDao("Subscription")).thenReturn(mySubscriptionDao);
		when(mySubscriptionDao.read(any(), requestDetailsCaptor.capture(), eq(true))).thenReturn(mySubscription);
		when(mySubscriptionCanonicalizer.getSubscriptionStatus(mySubscription)).thenReturn(SubscriptionStatus.ACTIVE.toCode());

		mySubscriptionRegisteringListener.handleMessage(message);
		SystemRequestDetails details = (SystemRequestDetails)requestDetailsCaptor.getValue();

		// ensure partitions with list of names containing null use the default partition
		assertNull(details.getRequestPartitionId().getPartitionNames());
		assertNull(details.getRequestPartitionId().getFirstPartitionIdOrNull());
		verify(mySubscriptionRegistry, never()).unregisterSubscriptionIfRegistered(any());
		verify(mySubscriptionRegistry, times(1)).registerSubscriptionUnlessAlreadyRegistered(any());
	}

	@Test
	public void testHandleMessageWithNullPartition(){
		RequestPartitionId requestPartitionId = null;
		ResourceModifiedMessage resourceModifiedMessage = new ResourceModifiedMessage(myFhirContext, mySubscription, BaseResourceMessage.OperationTypeEnum.CREATE);
		resourceModifiedMessage.setPartitionId(requestPartitionId);
		ResourceModifiedJsonMessage message = new ResourceModifiedJsonMessage(resourceModifiedMessage);

		when(myDaoRegistry.getResourceDao("Subscription")).thenReturn(mySubscriptionDao);
		when(mySubscriptionDao.read(any(), requestDetailsCaptor.capture(), eq(true))).thenReturn(mySubscription);
		when(mySubscriptionCanonicalizer.getSubscriptionStatus(mySubscription)).thenReturn(SubscriptionStatus.ACTIVE.toCode());

		mySubscriptionRegisteringListener.handleMessage(message);
		SystemRequestDetails details = (SystemRequestDetails)requestDetailsCaptor.getValue();

		// ensure partitions that are null use the default partition
		assertNull(details.getRequestPartitionId().getPartitionNames());
		assertNull(details.getRequestPartitionId().getFirstPartitionIdOrNull());
		verify(mySubscriptionRegistry, never()).unregisterSubscriptionIfRegistered(any());
		verify(mySubscriptionRegistry, times(1)).registerSubscriptionUnlessAlreadyRegistered(any());
	}
}
