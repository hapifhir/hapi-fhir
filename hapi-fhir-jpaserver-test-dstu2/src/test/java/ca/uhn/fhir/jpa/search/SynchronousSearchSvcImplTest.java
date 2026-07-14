package ca.uhn.fhir.jpa.search;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertFalse;

import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.dao.ISearchResultConsumer;
import ca.uhn.fhir.jpa.dao.SearchProgressTracker;
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
import org.mockito.Mock;
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

	@SuppressWarnings("unused")
	@Mock
	private IInterceptorBroadcaster myInterceptorBroadcaster;

	@InjectMocks
	private SynchronousSearchSvcImpl mySynchronousSearchSvc;

	@BeforeEach
	public void before() {
		mySynchronousSearchSvc.setContext(ourCtx);
		mySynchronousSearchSvc.mySearchBuilderFactory = mySearchBuilderFactory;

		when(mySearchBuilderFactory.newSearchBuilder(any(), any()))
			.thenReturn(mySearchBuilder);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testSynchronousSearch() {
		SearchParameterMap params = new SearchParameterMap();

		List<JpaPid> pids = createPidSequence(800);
		mockSearch(pids);

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
	public void testSynchronousSearch_ReturnsPageIds() {
		SearchParameterMap params = new SearchParameterMap();

		SearchProgressTracker tracker = new SearchProgressTracker(false, 0, 0, "current-page-id", "prev-page-id", "next-page-id");
		List<JpaPid> pids = createPidSequence(800);
		mockSearch(pids, tracker);

		doAnswer(loadPids()).when(mySearchBuilder)
			.loadResourcesByPid(any(), any(), any(), anyBoolean(), any());

		IBundleProvider result = mySynchronousSearchSvc.executeQuery( "Patient", params, RequestPartitionId.allPartitions());
		assertNull(result.getUuid());
		assertFalse(result.isEmpty());
		assertEquals("current-page-id", result.getCurrentPageId());
		assertEquals("prev-page-id", result.getPreviousPageId());
		assertEquals("next-page-id", result.getNextPageId());

		List<IBaseResource> resources = result.getResources(0, 1000);
		assertThat(resources).hasSize(790);
		assertEquals("10", resources.get(0).getIdElement().getValueAsString());
		assertEquals("799", resources.get(789).getIdElement().getValueAsString());
	}

	@Test
	public void testSynchronousSearchWithOffset() {
		SearchParameterMap params = new SearchParameterMap();
		params.setCount(10);
		params.setOffset(10);
		params.setSearchTotalMode(SearchTotalModeEnum.ACCURATE);

		List<JpaPid> pids = createPidSequence(30);
		when(mySearchBuilder.createCountQuery(any(SearchParameterMap.class), any(String.class),nullable(RequestDetails.class), nullable(RequestPartitionId.class))).thenReturn(20L);
		mockSearch(pids.subList(10, 20));

		doAnswer(loadPids()).when(mySearchBuilder).loadResourcesByPid(any(), any(), any(), anyBoolean(), any());

		IBundleProvider result = mySynchronousSearchSvc.executeQuery("Patient", params, RequestPartitionId.allPartitions());

		List<IBaseResource> resources = result.getResources(0, 1000);
		assertThat(resources).hasSize(10);
		assertEquals("20", resources.get(0).getIdElement().getValueAsString());
	}

	@Test
	public void testSynchronousSearchUpTo() {
		when(myStorageSettings.getDefaultTotalMode()).thenReturn(null);

		SearchParameterMap params = new SearchParameterMap();
		params.setLoadSynchronousUpTo(100);

		List<JpaPid> pids = createPidSequence(800);
		mockSearch(pids);

		pids = createPidSequence(110);
		List<JpaPid> finalPids = pids;
		doAnswer(loadPids()).when(mySearchBuilder).loadResourcesByPid(argThat(ids -> ids.containsAll(finalPids)), any(), any(), anyBoolean(), nullable(RequestDetails.class));

		IBundleProvider result = mySynchronousSearchSvc.executeQuery("Patient", params,   RequestPartitionId.allPartitions());

		List<IBaseResource> resources = result.getResources(0, 1000);
		assertThat(resources).hasSize(100);
		assertEquals("10", resources.get(0).getIdElement().getValueAsString());
		assertEquals("109", resources.get(99).getIdElement().getValueAsString());
	}


	private void mockSearch(List<JpaPid> pids) {
		SearchProgressTracker tracker = new SearchProgressTracker(false, 0, 0);
		mockSearch(pids, tracker);
	}

	@SuppressWarnings("unchecked")
	private void mockSearch(List<JpaPid> pids, SearchProgressTracker tracker) {
		when(mySearchBuilder.performSearchForPids(any(), any(), any(), any(), any())).thenAnswer(t->{
			ISearchResultConsumer<JpaPid> consumer = t.getArgument(0, ISearchResultConsumer.class);
			for (JpaPid pid : pids) {
				ISearchResultConsumer.Outcome outcome = consumer.consume(null, pid);
				if (!outcome.isContinue()) {
					break;
				}
			}
			return tracker;
		});
	}
}
