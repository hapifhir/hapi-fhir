package ca.uhn.fhir.jpa.mdm.config;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.subscription.channel.subscription.IChannelNamer;
import ca.uhn.fhir.mdm.api.IMdmSettings;
import ca.uhn.fhir.mdm.rules.json.MdmRulesJson;
import ca.uhn.fhir.model.api.IFhirVersion;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.ResourceGoneException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.util.ExtensionUtil;
import ca.uhn.fhir.util.HapiExtensions;
import org.hl7.fhir.instance.model.api.IBaseExtension;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.Extension;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Subscription;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MdmSubscriptionLoaderTest {
	@Mock
	IFhirResourceDao<IBaseResource> mySubscriptionDao;

	@Mock
	DaoRegistry myDaoRegistry;

	@Mock
	IMdmSettings myMdmSettings;

	@Spy
	FhirContext myFhirContext = FhirContext.forR4Cached();

	@Mock
	IChannelNamer myChannelNamer;

	@InjectMocks
	MdmSubscriptionLoader mySvc = new MdmSubscriptionLoader();

	@AfterEach
	public void after() {
		verifyNoMoreInteractions(mySubscriptionDao);
	}

	@Test
	public void updateIfNotPresent_createSubscriptionIfItWasDeleted() {
		Subscription subscription = new Subscription();
		IdType id = new IdType("2401");
		subscription.setIdElement(id);
		when(mySubscriptionDao.read(eq(id), any())).thenThrow(new ResourceGoneException(""));
		mySvc.updateIfNotPresent(subscription);
		verify(mySubscriptionDao).update(eq(subscription), any(RequestDetails.class));
	}

	@Test
	public void updateIfNotPresent_createSubscriptionIfItDoesNotExist() {
		Subscription subscription = new Subscription();
		IdType id = new IdType("2401");
		subscription.setIdElement(id);
		when(mySubscriptionDao.read(eq(id), any())).thenThrow(new ResourceNotFoundException(""));
		mySvc.updateIfNotPresent(subscription);
		verify(mySubscriptionDao).update(eq(subscription), any(RequestDetails.class));
	}

	@Test
	public void updateIfNotPresent_createSubscriptionExists() {
		Subscription subscription = new Subscription();
		IdType id = new IdType("2401");
		subscription.setIdElement(id);
		when(mySubscriptionDao.read(eq(id), any())).thenReturn(subscription);
		mySvc.updateIfNotPresent(subscription);
		verify(mySubscriptionDao, never()).update(any(),  any(RequestDetails.class));
	}

	@Test
	public void testDaoUpdateMdmSubscriptions() {
		MdmRulesJson mdmRulesJson = new MdmRulesJson();
		mdmRulesJson.setMdmTypes(Arrays.asList("Patient"));
		when(myMdmSettings.getMdmRules()).thenReturn(mdmRulesJson);
		when(myChannelNamer.getChannelName(any(), any())).thenReturn("Test");
		when(myDaoRegistry.getResourceDao(eq("Subscription"))).thenReturn(mySubscriptionDao);
		when(mySubscriptionDao.read(any(), any())).thenThrow(new ResourceGoneException(""));

		mySvc.daoUpdateMdmSubscriptions();

		ArgumentCaptor<Subscription> subscriptionCaptor = ArgumentCaptor.forClass(Subscription.class);
		verify(mySubscriptionDao).update(subscriptionCaptor.capture(),  any(RequestDetails.class));

		IBaseExtension extension = ExtensionUtil.getExtensionByUrl(subscriptionCaptor.getValue(), HapiExtensions.EXTENSION_SUBSCRIPTION_CROSS_PARTITION);

		assertNotNull(extension);

		assertTrue(((BooleanType)extension.getValue()).booleanValue());
	}
}
