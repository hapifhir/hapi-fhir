package ca.uhn.fhir.jpa.dao;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.jpa.searchparam.MatchUrlService;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.util.MemoryCacheService;

import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.storage.TransactionDetails;

import ca.uhn.fhir.rest.param.DateRangeParam;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

import org.hl7.fhir.r4.model.Patient;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.ArgumentMatchers.any;

import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Mockito.when;

import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MatchResourceUrlServiceTest {
	@Spy
	private JpaStorageSettings myStorageSettings = new JpaStorageSettings();
	@Spy
	private MemoryCacheService myMemoryCacheService = new MemoryCacheService(myStorageSettings);

	@Mock
	private TransactionDetails myTransactionDetails;

	@Mock
	private RequestDetails myRequestDetails;

	@Mock
	private DaoRegistry myDaoRegistry;

	@Mock
	private IFhirResourceDao myFhirResourceDao;

	@Mock
	private FhirContext myCtx = FhirContext.forR4();

	@Mock
	private MatchUrlService myMatchUrlSvc;

	@InjectMocks
	private MatchResourceUrlService<JpaPid> myMatchResourceUrlSvc = new MatchResourceUrlService();

	@BeforeEach
	public void beforeEach() {
		myMemoryCacheService.invalidateAllCaches();
	}

	@Test
	void testProcessMatchUrlUsingCacheOnly_shouldNotReturnPidsFromWrongPartition() {
		myStorageSettings.setMatchUrlCacheEnabled(true);

		String matchUrl = "Patient?identifier=test|123";
		final int partitionId = 1;
		JpaPid cachedPid = JpaPid.fromId(1L);
		cachedPid.setPartitionId(partitionId);

		myMatchResourceUrlSvc.matchUrlResolved(myTransactionDetails, "Patient", matchUrl, cachedPid);

		JpaPid pid = myMatchResourceUrlSvc.processMatchUrlUsingCacheOnly("Patient", matchUrl, RequestPartitionId.fromPartitionId(1));
		assertNotNull(pid);
		assertThat(pid.getPartitionId()).isEqualTo(partitionId);
		assertThat(pid.getId()).isEqualTo(1L);

		pid = myMatchResourceUrlSvc.processMatchUrlUsingCacheOnly("Patient", matchUrl, RequestPartitionId.allPartitions());
		assertNotNull(pid);
		assertThat(pid.getPartitionId()).isEqualTo(partitionId);

		pid = myMatchResourceUrlSvc.processMatchUrlUsingCacheOnly("Patient", matchUrl, RequestPartitionId.fromPartitionId(2));
		assertNull(pid);

		pid = myMatchResourceUrlSvc.processMatchUrlUsingCacheOnly("Patient", matchUrl, RequestPartitionId.fromPartitionId(null));
		assertNull(pid);
	}

	@Test
	void testProcessMatchUrl_storesFoundMatchInCache() {
		myStorageSettings.setMatchUrlCacheEnabled(true);

		String matchUrl = "Patient?identifier=test|123";
		final int partitionId = 1;
		JpaPid cachedPid = JpaPid.fromId(1L);
		cachedPid.setPartitionId(partitionId);

		SearchParameterMap sp = new SearchParameterMap();
		sp.setLastUpdated(new DateRangeParam().setLowerBound("2024").setUpperBound("2025"));

		when(myDaoRegistry.getResourceDao(Patient.class)).thenReturn(myFhirResourceDao);
		when(myFhirResourceDao.searchForIds(any(), any(), any())).thenReturn(List.of(cachedPid));
		when(myMatchUrlSvc.translateMatchUrl(any(), any())).thenReturn(sp);

		myMatchResourceUrlSvc.processMatchUrl(matchUrl, Patient.class, myTransactionDetails, myRequestDetails, RequestPartitionId.fromPartitionId(1));

		JpaPid pid = myMatchResourceUrlSvc.processMatchUrlUsingCacheOnly("Patient", matchUrl, RequestPartitionId.fromPartitionId(1));
		assertNotNull(pid);
		assertThat(pid.getPartitionId()).isEqualTo(partitionId);
		assertThat(pid.getId()).isEqualTo(1L);
	}
}
