package ca.uhn.fhir.jpa.searchparam.extractor;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.api.model.PersistentIdToForcedIdMap;
import ca.uhn.fhir.jpa.api.svc.IIdHelperService;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.jpa.model.entity.ResourceLink;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.test.utilities.MockInvoker;
import org.hl7.fhir.r4.model.Reference;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SearchParamExtractorServiceTest {

	private SearchParamExtractorService mySvc;
	@Mock
	private IInterceptorBroadcaster myRequestInterceptorBroadcaster;
	@Mock
	private IInterceptorBroadcaster myJpaInterceptorBroadcaster;
	@Mock
	private IIdHelperService<JpaPid> myIdHelperService;

	@BeforeEach
	public void before() {
		mySvc = new SearchParamExtractorService();
		mySvc.setInterceptorBroadcasterForUnitTest(myJpaInterceptorBroadcaster);
	}

	@Test
	public void testHandleWarnings() {
		ISearchParamExtractor.SearchParamSet<Object> searchParamSet = new ISearchParamExtractor.SearchParamSet<>();
		searchParamSet.addWarning("help i'm a bug");
		searchParamSet.addWarning("Spiff");

		AtomicInteger counter = new AtomicInteger();

		when(myJpaInterceptorBroadcaster.hasHooks(eq(Pointcut.JPA_PERFTRACE_WARNING))).thenReturn(true);
		when(myJpaInterceptorBroadcaster.getInvokersForPointcut(eq(Pointcut.JPA_PERFTRACE_WARNING))).thenReturn(MockInvoker.list((Consumer<HookParams>) params->counter.incrementAndGet()));

		ServletRequestDetails requestDetails = new ServletRequestDetails(myRequestInterceptorBroadcaster);
		SearchParamExtractorService.handleWarnings(requestDetails, myJpaInterceptorBroadcaster, searchParamSet);

		verify(myJpaInterceptorBroadcaster, times(3)).hasHooks(eq(Pointcut.JPA_PERFTRACE_WARNING));
		verify(myRequestInterceptorBroadcaster, times(2)).hasHooks(eq(Pointcut.JPA_PERFTRACE_WARNING));
		assertEquals(2, counter.get());
	}

	/**
	 * Test that findMatchingResourceLink handles null values from targetResourceIdMap.get() without throwing NPE.
	 * This scenario occurs when a ResourceLink's targetResourcePk is not present in the map.
	 */
	// Created by Claude Sonnet 4.5
	@Test
	void testFindMatchingResourceLink_whenTargetResourceIdMapReturnsNull_shouldNotThrowNPE() throws Exception {
		// Setup
		FhirContext ctx = FhirContext.forR4();
		SearchParamExtractorService svc = new SearchParamExtractorService();
		svc.setContextForUnitTest(ctx);
		svc.setIdHelperServiceForUnitTest(myIdHelperService);

		// Create PathAndRef with a reference to Patient/123
		Reference reference = new Reference("Patient/123");
		PathAndRef pathAndRef = new PathAndRef("member", "Group.member.entity", reference, false);

		// Create a ResourceLink with a target PID that won't be in the map
		ResourceLink resourceLink = new ResourceLink();
		resourceLink.setSourcePath("Group.member.entity");
		resourceLink.setTargetResource("Patient", 999L, "Patient/999");

		List<ResourceLink> existingResourceLinks = new ArrayList<>();
		existingResourceLinks.add(resourceLink);

		// Create a map that contains a different PID, so our lookup returns null
		Map<JpaPid, Optional<String>> idMap = new HashMap<>();
		JpaPid differentPid = JpaPid.fromId(888L);
		idMap.put(differentPid, Optional.of("Patient/888"));
		PersistentIdToForcedIdMap<JpaPid> targetResourceIdMap = new PersistentIdToForcedIdMap<>(idMap);

		// Mock the IdHelperService to return our map
		when(myIdHelperService.translatePidsToForcedIds(any())).thenReturn(targetResourceIdMap);

		// Use reflection to call the private method
		Method method = SearchParamExtractorService.class.getDeclaredMethod(
			"findMatchingResourceLink",
			PathAndRef.class,
			Collection.class
		);
		method.setAccessible(true);

		// Execute - should not throw NullPointerException
		Optional<ResourceLink> result = unsafeCast(method.invoke(svc, pathAndRef, existingResourceLinks));

		// Verify - should return empty Optional since no match was found
		assertThat(result).isEmpty();
	}

	@SuppressWarnings("unchecked")
	private static <T> T unsafeCast(Object theObject) {
		return (T) theObject;
	}
}
