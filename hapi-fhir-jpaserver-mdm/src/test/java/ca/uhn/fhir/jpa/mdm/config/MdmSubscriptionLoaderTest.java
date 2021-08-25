package ca.uhn.fhir.jpa.mdm.config;

import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.rest.server.exceptions.ResourceGoneException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Subscription;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MdmSubscriptionLoaderTest {
	@Mock
	IFhirResourceDao<IBaseResource> mySubscriptionDao;

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
		when(mySubscriptionDao.read(id)).thenThrow(new ResourceGoneException(""));
		mySvc.updateIfNotPresent(subscription);
		verify(mySubscriptionDao).update(subscription);
	}

	@Test
	public void updateIfNotPresent_createSubscriptionIfItDoesNotExist() {
		Subscription subscription = new Subscription();
		IdType id = new IdType("2401");
		subscription.setIdElement(id);
		when(mySubscriptionDao.read(id)).thenThrow(new ResourceNotFoundException(""));
		mySvc.updateIfNotPresent(subscription);
		verify(mySubscriptionDao).update(subscription);
	}

	@Test
	public void updateIfNotPresent_createSubscriptionExists() {
		Subscription subscription = new Subscription();
		IdType id = new IdType("2401");
		subscription.setIdElement(id);
		when(mySubscriptionDao.read(id)).thenReturn(subscription);
		mySvc.updateIfNotPresent(subscription);
		verify(mySubscriptionDao, never()).update(any());
	}
}
