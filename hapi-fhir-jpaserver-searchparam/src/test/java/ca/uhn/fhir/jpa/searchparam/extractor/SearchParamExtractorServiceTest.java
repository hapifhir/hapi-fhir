package ca.uhn.fhir.jpa.searchparam.extractor;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.api.model.PersistentIdToForcedIdMap;
import ca.uhn.fhir.jpa.api.svc.IIdHelperService;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.jpa.model.entity.ResourceLink;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.test.utilities.MockInvoker;
import org.hl7.fhir.r4.model.Group;
import org.hl7.fhir.r4.model.Reference;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Method;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
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
	 * GL-7767: Test that findMatchingResourceLink handles null values from
	 * targetResourceIdMap.get() without throwing NPE. This scenario occurs during
	 * $reindex when a ResourceLink's targetResourcePk is present in the link but
	 * NOT present in the translated ID map (e.g., chained reference SearchParameter
	 * like Consent.organization.identifier).
	 *
	 * Before the fix (HAPI v8.8.0, commit ebca61d28f5), calling .isPresent() on a
	 * null return from Map.get() caused an NPE that crashed the reindex batch job.
	 */
	// Created by claude-opus-4-6
	@Test
	void testFindMatchingResourceLink_whenTargetPidNotInMap_returnsEmptyWithoutNPE() throws Exception {
		// Setup
		FhirContext ctx = FhirContext.forR4();
		SearchParamExtractorService svc = new SearchParamExtractorService();
		svc.setContextForUnitTest(ctx);
		svc.setIdHelperServiceForUnitTest(myIdHelperService);

		// Create PathAndRef referencing Organization/456
		Reference reference = new Reference("Organization/456");
		PathAndRef pathAndRef = new PathAndRef("organization", "Consent.organization", reference, false);

		// Create a ResourceLink with targetResourcePk=999 (a PID that will NOT be in the map)
		ResourceTable sourceTable = new ResourceTable();
		sourceTable.setResourceType("Consent");
		sourceTable.setId(JpaPid.fromId(1L));
		ResourceLink resourceLink = ResourceLink.forLocalReference(
			ResourceLink.ResourceLinkForLocalReferenceParams.instance()
				.setSourcePath("Consent.organization")
				.setSourceResource(sourceTable)
				.setTargetResourceType("Organization")
				.setTargetResourcePid(999L)
				.setTargetResourceId("456")
				.setUpdated(Date.from(Instant.now())));

		List<ResourceLink> existingResourceLinks = new ArrayList<>();
		existingResourceLinks.add(resourceLink);

		// Mock translatePidsToForcedIds to return a map that does NOT contain PID 999
		// Map.get() for a missing key returns null — this is the bug trigger
		Map<JpaPid, Optional<String>> idMap = new HashMap<>();
		// Map is intentionally empty for PID 999
		PersistentIdToForcedIdMap<JpaPid> persistentIdMap = new PersistentIdToForcedIdMap<>(idMap);
		when(myIdHelperService.translatePidsToForcedIds(any())).thenReturn(persistentIdMap);

		// Use reflection to call the private method
		Method method = SearchParamExtractorService.class.getDeclaredMethod(
			"findMatchingResourceLink",
			PathAndRef.class,
			Collection.class
		);
		method.setAccessible(true);

		// Execute — should return empty Optional, NOT throw NullPointerException
		Optional<ResourceLink> result = unsafeCast(method.invoke(svc, pathAndRef, existingResourceLinks));

		// Verify
		assertThat(result).isEmpty();
	}

	/**
	 * Adjacent test: When targetResourcePk IS present in the map but with Optional.empty()
	 * (no forced ID), the ID comparison is skipped and no match is found.
	 * This tests the code path at lines 860-864 where idPartOpt.isPresent() is false.
	 */
	// Created by claude-opus-4-6
	@Test
	void testFindMatchingResourceLink_whenTargetPidInMapWithEmptyOptional_returnsEmpty() throws Exception {
		// Setup
		FhirContext ctx = FhirContext.forR4();
		SearchParamExtractorService svc = new SearchParamExtractorService();
		svc.setContextForUnitTest(ctx);
		svc.setIdHelperServiceForUnitTest(myIdHelperService);

		// Create PathAndRef referencing Organization/456
		Reference reference = new Reference("Organization/456");
		PathAndRef pathAndRef = new PathAndRef("organization", "Consent.organization", reference, false);

		// Create a ResourceLink with targetResourcePk=999 that IS in the map
		ResourceTable sourceTable = new ResourceTable();
		sourceTable.setResourceType("Consent");
		sourceTable.setId(JpaPid.fromId(1L));
		ResourceLink resourceLink = ResourceLink.forLocalReference(
			ResourceLink.ResourceLinkForLocalReferenceParams.instance()
				.setSourcePath("Consent.organization")
				.setSourceResource(sourceTable)
				.setTargetResourceType("Organization")
				.setTargetResourcePid(999L)
				.setTargetResourceId("456")
				.setUpdated(Date.from(Instant.now())));

		List<ResourceLink> existingResourceLinks = new ArrayList<>();
		existingResourceLinks.add(resourceLink);

		// Mock translatePidsToForcedIds to return a map with PID 999 mapped to Optional.empty()
		JpaPid targetPid = JpaPid.fromId(999L);
		Map<JpaPid, Optional<String>> idMap = new HashMap<>();
		idMap.put(targetPid, Optional.empty());
		PersistentIdToForcedIdMap<JpaPid> persistentIdMap = new PersistentIdToForcedIdMap<>(idMap);
		when(myIdHelperService.translatePidsToForcedIds(any())).thenReturn(persistentIdMap);

		// Use reflection to call the private method
		Method method = SearchParamExtractorService.class.getDeclaredMethod(
			"findMatchingResourceLink",
			PathAndRef.class,
			Collection.class
		);
		method.setAccessible(true);

		// Execute — should return empty because idPartOpt.isPresent() is false,
		// so hasMatchingResourceId stays false
		Optional<ResourceLink> result = unsafeCast(method.invoke(svc, pathAndRef, existingResourceLinks));

		// Verify
		assertThat(result).isEmpty();
	}

	/**
	 * Adjacent test: When all ResourceLinks have null targetResourcePk (e.g., logical
	 * references only), the pids set is empty and the method should return empty
	 * without calling translatePidsToForcedIds.
	 */
	// Created by claude-opus-4-6
	@Test
	void testFindMatchingResourceLink_whenAllLinksHaveNullPk_returnsEmpty() throws Exception {
		// Setup
		FhirContext ctx = FhirContext.forR4();
		SearchParamExtractorService svc = new SearchParamExtractorService();
		svc.setContextForUnitTest(ctx);
		svc.setIdHelperServiceForUnitTest(myIdHelperService);

		// Create PathAndRef referencing Organization/456
		Reference reference = new Reference("Organization/456");
		PathAndRef pathAndRef = new PathAndRef("organization", "Consent.organization", reference, false);

		// Create a ResourceLink via forLogicalReference — no targetResourcePk
		ResourceTable sourceTable = new ResourceTable();
		sourceTable.setResourceType("Consent");
		sourceTable.setId(JpaPid.fromId(1L));
		ResourceLink resourceLink = ResourceLink.forLogicalReference(
			"Consent.organization", sourceTable, "http://example.com/Organization/456", Date.from(Instant.now()));

		List<ResourceLink> existingResourceLinks = new ArrayList<>();
		existingResourceLinks.add(resourceLink);

		// Use reflection to call the private method
		Method method = SearchParamExtractorService.class.getDeclaredMethod(
			"findMatchingResourceLink",
			PathAndRef.class,
			Collection.class
		);
		method.setAccessible(true);

		// Execute — should return empty because pids set is empty (early return)
		Optional<ResourceLink> result = unsafeCast(method.invoke(svc, pathAndRef, existingResourceLinks));

		// Verify — empty result and translatePidsToForcedIds was never called
		assertThat(result).isEmpty();
		verify(myIdHelperService, times(0)).translatePidsToForcedIds(any());
	}

	@SuppressWarnings("unchecked")
	private static <T> T unsafeCast(Object theObject) {
		return (T) theObject;
	}
}
