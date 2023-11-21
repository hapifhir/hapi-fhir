package ca.uhn.fhir.jpa.mdm.config;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.ResourceGoneException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import org.hl7.fhir.r5.model.IdType;
import org.hl7.fhir.r5.model.SubscriptionTopic;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MdmSubscriptionLoaderR5Test {

	@Mock
	IFhirResourceDao<SubscriptionTopic> mySubscriptionTopicDao;

	@Mock
	DaoRegistry myDaoRegistry;

	@Spy
	FhirContext myFhirContext = FhirContext.forR5Cached();

	@InjectMocks
	MdmSubscriptionLoader mySvc = new MdmSubscriptionLoader();

	private SubscriptionTopic subscriptionTopic;
	private IdType topicId;

	@BeforeEach
	public void before() {
		subscriptionTopic = new SubscriptionTopic();
		topicId = new IdType("2401");
		subscriptionTopic.setIdElement(topicId);

		when(myDaoRegistry.getResourceDao(eq("SubscriptionTopic"))).thenReturn(mySubscriptionTopicDao);
	}

	@AfterEach
	public void after() {
		verifyNoMoreInteractions(mySubscriptionTopicDao);
	}

	@ParameterizedTest
	@MethodSource("paramsProvider")
	public void testUpdateIfNotPresent_withSubscriptionTopicDeleted_createsNewSubscriptionTopic(Exception theException) {
		when(mySubscriptionTopicDao.read(eq(topicId), any(RequestDetails.class))).thenThrow(theException);
		mySvc.updateIfNotPresent(subscriptionTopic);
		verify(mySubscriptionTopicDao).update(eq(subscriptionTopic), any(RequestDetails.class));
	}

	protected static Stream<Arguments> paramsProvider(){
		return Stream.of(
				Arguments.arguments(new ResourceGoneException("")),
				Arguments.arguments(new ResourceNotFoundException(""))
		);
	}

	@Test
	public void testUpdateIfNotPresent_withSubscriptionTopicCreated_doesNotCreateSubscriptionTopic() {
		when(mySubscriptionTopicDao.read(eq(topicId), any(RequestDetails.class))).thenReturn(subscriptionTopic);
		mySvc.updateIfNotPresent(subscriptionTopic);
		verify(mySubscriptionTopicDao, never()).update(any(), any(RequestDetails.class));
	}
}
