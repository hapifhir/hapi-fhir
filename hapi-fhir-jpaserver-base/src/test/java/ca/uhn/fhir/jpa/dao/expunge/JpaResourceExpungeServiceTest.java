package ca.uhn.fhir.jpa.dao.expunge;

import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.dao.data.IResourceHistoryTableDao;
import ca.uhn.fhir.jpa.dao.data.IResourceHistoryTagDao;
import ca.uhn.fhir.jpa.dao.data.IResourceTableDao;
import ca.uhn.fhir.jpa.esr.ExternallyStoredResourceServiceRegistry;
import ca.uhn.fhir.jpa.esr.IExternallyStoredResourceService;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.jpa.model.entity.ResourceEncodingEnum;
import ca.uhn.fhir.jpa.model.entity.ResourceHistoryTable;
import ca.uhn.fhir.jpa.model.entity.ResourceHistoryTablePk;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class JpaResourceExpungeServiceTest {

	@InjectMocks
	@Spy
	private JpaResourceExpungeService myService = spy(new JpaResourceExpungeService());

	@Mock
	private IResourceTableDao myResourceTableDao;

	@Mock
	private IResourceHistoryTableDao myResourceHistoryTableDao;

	@Mock
	private RequestDetails myRequestDetails;

	@Mock
	private ResourceTable myResourceTable;

	@Mock
	private ExternallyStoredResourceServiceRegistry myExternallyStoredResourceServiceRegistry;

	@Mock
	private IExternallyStoredResourceService myExternalStorageProvider;

	@Mock
	private IInterceptorBroadcaster myInterceptorBroadcaster;

	@Mock
	private JpaStorageSettings myStorageSettings;

	@Mock
	private IResourceHistoryTagDao myResourceHistoryTagDao;

	@Test
	public void testExpungeDoesNotDeleteAllSearchParams() {
		when(myResourceTableDao.findById(any(JpaPid.class))).thenReturn(Optional.of(myResourceTable));
		when(myResourceTable.getIdDt()).thenReturn(new IdDt());
		when(myResourceTable.getId()).thenReturn(new JpaPid());
		myService.expungeCurrentVersionOfResource(myRequestDetails, JpaPid.fromId(1L), new AtomicInteger(1));
		verify(myService, never()).deleteAllSearchParams(any());
	}

	@Test
	void testExpungeHistoricalVersions_withEsrResource_expungesEsrTagAndHistoryEntity() {
		// setup
		ResourceHistoryTablePk historyPk = new ResourceHistoryTablePk();
		ResourceHistoryTable historyVersion = new ResourceHistoryTable();
		historyVersion.setResourceTable(myResourceTable);
		historyVersion.setVersion(1L);
		historyVersion.setEncoding(ResourceEncodingEnum.ESR);
		historyVersion.setResourceTextVc("esr-provider:address");

		when(myResourceHistoryTableDao.findById(historyPk)).thenReturn(Optional.of(historyVersion));
		when(myExternallyStoredResourceServiceRegistry.getProvider("esr-provider"))
			.thenReturn(myExternalStorageProvider);

		// execute
		myService.expungeHistoricalVersions(myRequestDetails, List.of(historyPk), new AtomicInteger(10));

		// verify
		verify(myExternallyStoredResourceServiceRegistry, times(1)).getProvider("esr-provider");
		verify(myExternalStorageProvider, times(1)).deleteResource("address");
		verify(myResourceHistoryTagDao, times(1)).deleteByPid(historyPk);
		verify(myResourceHistoryTableDao, times(1)).deleteByPid(historyPk);
	}

	@ParameterizedTest
	@EnumSource(value = ResourceEncodingEnum.class, names = {"JSON", "JSONC", "DEL"})
	void testExpungeHistoricalVersions_withNonEsrResource_expungesTagAndHistoryEntity(ResourceEncodingEnum theEncoding) {
		// setup
		ResourceHistoryTablePk historyPk = new ResourceHistoryTablePk();
		ResourceHistoryTable historyVersion = new ResourceHistoryTable();
		historyVersion.setResourceTable(myResourceTable);
		historyVersion.setVersion(1L);
		historyVersion.setEncoding(theEncoding);

		when(myResourceHistoryTableDao.findById(historyPk)).thenReturn(Optional.of(historyVersion));

		// execute
		myService.expungeHistoricalVersions(myRequestDetails, List.of(historyPk), new AtomicInteger(10));

		// verify
		verify(myExternallyStoredResourceServiceRegistry, never()).getProvider(any());
		verify(myExternalStorageProvider, never()).deleteResource(any());
		verify(myResourceHistoryTagDao, times(1)).deleteByPid(historyPk);
		verify(myResourceHistoryTableDao, times(1)).deleteByPid(historyPk);
	}
}
