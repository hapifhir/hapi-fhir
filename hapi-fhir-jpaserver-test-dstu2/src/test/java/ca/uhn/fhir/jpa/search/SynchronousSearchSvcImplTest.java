package ca.uhn.fhir.jpa.search;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertFalse;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.SearchTotalModeEnum;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SynchronousSearchSvcImplTest extends BaseSearchSvc {

	@InjectMocks
	private SynchronousSearchSvcImpl mySynchronousSearchSvc;

	@BeforeEach
	public void before() {
		mySynchronousSearchSvc.setContext(ourCtx);
		mySynchronousSearchSvc.mySearchBuilderFactory = mySearchBuilderFactory;
	}

	@Test
	public void testSynchronousSearch() {
		when(mySearchBuilderFactory.newSearchBuilder(any(), any(), any()))
			.thenReturn(mySearchBuilder);

		SearchParameterMap params = new SearchParameterMap();

		List<JpaPid> pids = createPidSequence(800);
		when(mySearchBuilder.createQuery(any(SearchParameterMap.class), any(), any(), nullable(RequestPartitionId.class)))
			.thenReturn(new BaseSearchSvc.ResultIterator(pids.iterator()));

		doAnswer(loadPids()).when(mySearchBuilder)
			.loadResourcesByPid(any(Collection.class), any(Collection.class), any(List.class), anyBoolean(), any());

		IBundleProvider result = mySynchronousSearchSvc.executeQuery( "Patient", params, RequestPartitionId.allPartitions());
		assertNull(result.getUuid());
		assertFalse(result.isEmpty());

		List<IBaseResource> resources = result.getResources(0, 1000);
		assertThat(resources).hasSize(790);
		assertEquals("10", resources.get(0).getIdElement().getValueAsString());
		assertEquals("799", resources.get(789).getIdElement().getValueAsString());
	}

	@Test
	public void testSynchronousSearchWithOffset() {
		when(mySearchBuilderFactory.newSearchBuilder(any(), any(), any())).thenReturn(mySearchBuilder);

		SearchParameterMap params = new SearchParameterMap();
		params.setCount(10);
		params.setOffset(10);
		params.setSearchTotalMode(SearchTotalModeEnum.ACCURATE);

		List<JpaPid> pids = createPidSequence(30);
		when(mySearchBuilder.createCountQuery(any(SearchParameterMap.class), any(String.class),nullable(RequestDetails.class), nullable(RequestPartitionId.class))).thenReturn(20L);
		when(mySearchBuilder.createQuery(any(SearchParameterMap.class), any(), nullable(RequestDetails.class), nullable(RequestPartitionId.class))).thenReturn(new BaseSearchSvc.ResultIterator(pids.subList(10, 20).iterator()));

		doAnswer(loadPids()).when(mySearchBuilder).loadResourcesByPid(any(Collection.class), any(Collection.class), any(List.class), anyBoolean(), any());

		IBundleProvider result = mySynchronousSearchSvc.executeQuery("Patient", params, RequestPartitionId.allPartitions());

		List<IBaseResource> resources = result.getResources(0, 1000);
		assertThat(resources).hasSize(10);
		assertEquals("20", resources.get(0).getIdElement().getValueAsString());
	}

	@Test
	public void testSynchronousSearchUpTo() {
		when(mySearchBuilderFactory.newSearchBuilder(any(), any(), any())).thenReturn(mySearchBuilder);
		when(myStorageSettings.getDefaultTotalMode()).thenReturn(null);

		SearchParameterMap params = new SearchParameterMap();
		params.setLoadSynchronousUpTo(100);

		List<JpaPid> pids = createPidSequence(800);
		when(mySearchBuilder.createQuery(any(SearchParameterMap.class), any(), nullable(RequestDetails.class), nullable(RequestPartitionId.class)))
			.thenReturn(new BaseSearchSvc.ResultIterator(pids.iterator()));

		pids = createPidSequence(110);
		List<JpaPid> finalPids = pids;
		doAnswer(loadPids()).when(mySearchBuilder).loadResourcesByPid(argThat(ids -> ids.containsAll(finalPids)), any(Collection.class), any(List.class), anyBoolean(), nullable(RequestDetails.class));

		IBundleProvider result = mySynchronousSearchSvc.executeQuery("Patient", params,   RequestPartitionId.allPartitions());

		List<IBaseResource> resources = result.getResources(0, 1000);
		assertThat(resources).hasSize(100);
		assertEquals("10", resources.get(0).getIdElement().getValueAsString());
		assertEquals("109", resources.get(99).getIdElement().getValueAsString());
	}

}
