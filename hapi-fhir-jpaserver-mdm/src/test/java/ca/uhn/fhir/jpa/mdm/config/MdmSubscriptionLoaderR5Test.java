package ca.uhn.fhir.jpa.mdm.config;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.subscription.channel.subscription.IChannelNamer;
import ca.uhn.fhir.jpa.subscription.match.registry.SubscriptionLoader;
import ca.uhn.fhir.jpa.topic.SubscriptionTopicLoader;
import ca.uhn.fhir.mdm.api.IMdmSettings;
import ca.uhn.fhir.mdm.rules.json.MdmRulesJson;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.ResourceGoneException;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r5.model.Subscription;
import org.hl7.fhir.r5.model.SubscriptionTopic;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MdmSubscriptionLoaderR5Test {

    @Mock
    IFhirResourceDao<IBaseResource> mySubscriptionDao;
    @Mock
    IFhirResourceDao<SubscriptionTopic> mySubscriptionTopicDao;
    @Mock
    DaoRegistry myDaoRegistry;
    @Mock
    IMdmSettings myMdmSettings;
    @Spy
    FhirContext myFhirContext = FhirContext.forR5Cached();
    @Mock
    IChannelNamer myChannelNamer;
    @Mock
    SubscriptionLoader mySubscriptionLoader;
    @Mock
    SubscriptionTopicLoader mySubscriptionTopicLoader;
    @InjectMocks
    MdmSubscriptionLoader mySvc = new MdmSubscriptionLoader();

    @AfterEach
    public void after() {
        verifyNoMoreInteractions(mySubscriptionTopicDao);
    }

	@Test
	public void testDaoUpdateMdmSubscriptions_withR5FhirContext_createsCorrectSubscriptions() {
		// setup
		MdmRulesJson mdmRulesJson = new MdmRulesJson();
		mdmRulesJson.setMdmTypes(Arrays.asList("Patient"));
		when(myMdmSettings.getMdmRules()).thenReturn(mdmRulesJson);
		when(myChannelNamer.getChannelName(any(), any())).thenReturn("Test");
		when(myDaoRegistry.getResourceDao(eq("Subscription"))).thenReturn(mySubscriptionDao);
        when(myDaoRegistry.getResourceDao(eq("SubscriptionTopic"))).thenReturn(mySubscriptionTopicDao);
		when(mySubscriptionDao.read(any(), any(RequestDetails.class))).thenThrow(new ResourceGoneException(""));

		// execute
		mySvc.daoUpdateMdmSubscriptions();

		// verify SubscriptionTopic
		ArgumentCaptor<SubscriptionTopic> subscriptionTopicCaptor = ArgumentCaptor.forClass(SubscriptionTopic.class);
		verify(mySubscriptionTopicDao).update(subscriptionTopicCaptor.capture(), any(RequestDetails.class));

        SubscriptionTopic subscriptionTopic = subscriptionTopicCaptor.getValue();
		assertNotNull(subscriptionTopic);
		assertEquals("mdm-subscription-topic", subscriptionTopic.getId());
		assertThat(subscriptionTopic.getResourceTrigger()).hasSize(1);
		SubscriptionTopic.SubscriptionTopicResourceTriggerComponent triggerComponent = subscriptionTopic.getResourceTrigger().get(0);
		assertEquals("Patient", triggerComponent.getResource());

		// verify Subscription
		ArgumentCaptor<Subscription> subscriptionCaptor = ArgumentCaptor.forClass(Subscription.class);
		verify(mySubscriptionDao).update(subscriptionCaptor.capture(), any(RequestDetails.class));

        Subscription subscription = subscriptionCaptor.getValue();
		assertNotNull(subscription);
		assertEquals("mdm-subscription", subscription.getId());
	}
}
