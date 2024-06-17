package ca.uhn.fhir.jpa.dao.r5;

import static org.junit.jupiter.api.Assertions.assertTrue;
import ca.uhn.fhir.jpa.esr.ExternallyStoredResourceAddress;
import ca.uhn.fhir.jpa.esr.ExternallyStoredResourceAddressMetadataKey;
import ca.uhn.fhir.jpa.esr.ExternallyStoredResourceServiceRegistry;
import ca.uhn.fhir.jpa.esr.IExternallyStoredResourceService;
import ca.uhn.fhir.jpa.model.entity.ResourceEncodingEnum;
import ca.uhn.fhir.jpa.model.entity.ResourceHistoryTable;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r5.model.Patient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class ExternallyStoredResourceR5Test extends BaseJpaR5Test {

	public static final String MY_PROVIDER_ID = "my-provider-id";
	public static final String ADDRESS_123 = "address_123";
	@Autowired
	private ExternallyStoredResourceServiceRegistry myProviderRegistry;

	@Mock
	private IExternallyStoredResourceService myProvider;

	@BeforeEach
	public void beforeEach() {
		when(myProvider.getId()).thenReturn(MY_PROVIDER_ID);
		myProviderRegistry.registerProvider(myProvider);
	}

	@AfterEach
	public void afterEach() {
		myProviderRegistry.clearProviders();
	}

	@Test
	public void testCreate() {
		// Test
		IIdType id = storePatientWithExternalAddress();

		// Verify
		runInTransaction(()->{
			ResourceTable resource = myResourceTableDao.getReferenceById(id.getIdPartAsLong());
			assertNotNull(resource);
			ResourceHistoryTable history = myResourceHistoryTableDao.findForIdAndVersionAndFetchProvenance(id.getIdPartAsLong(), 1L);
			assertNotNull(history);
			assertEquals(ResourceEncodingEnum.ESR, history.getEncoding());
			assertEquals(MY_PROVIDER_ID + ":" + ADDRESS_123, history.getResourceTextVc());
		});

	}


	@Test
	public void testRead() {
		// Setup
		IIdType id = storePatientWithExternalAddress();
		Patient patient = new Patient();
		patient.setActive(true);
		when(myProvider.fetchResource(any())).thenReturn(patient);

		// Test
		Patient fetchedPatient = myPatientDao.read(id, mySrd);
		assertTrue(fetchedPatient.getActive());

	}

	private IIdType storePatientWithExternalAddress() {
		Patient p = new Patient();
		ExternallyStoredResourceAddress address = new ExternallyStoredResourceAddress(MY_PROVIDER_ID, ADDRESS_123);
		ExternallyStoredResourceAddressMetadataKey.INSTANCE.put(p, address);
		IIdType id = myPatientDao.create(p, mySrd).getId().toUnqualifiedVersionless();
		return id;
	}


}
