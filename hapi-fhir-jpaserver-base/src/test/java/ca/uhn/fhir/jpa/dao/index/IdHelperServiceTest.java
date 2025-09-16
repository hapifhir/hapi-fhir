package ca.uhn.fhir.jpa.dao.index;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.svc.ResolveIdentityMode;
import ca.uhn.fhir.jpa.dao.data.IResourceTableDao;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.cross.IResourceLookup;
import ca.uhn.fhir.jpa.model.cross.JpaResourceLookup;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.jpa.util.MemoryCacheService;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaQuery;

import java.util.ArrayList;
import java.util.Collection;

import org.hibernate.sql.results.internal.TupleImpl;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Patient;

import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;

import static org.mockito.ArgumentMatchers.anyBoolean;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.matches;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
public class IdHelperServiceTest {

	@InjectMocks
	private final IdHelperService myHelperSvc = new IdHelperService();

	@Mock
	protected IResourceTableDao myResourceTableDao;

	@Mock
	private JpaStorageSettings myStorageSettings;

	@Spy
	private FhirContext myFhirCtx = FhirContext.forR4Cached();

	@Spy
	private MemoryCacheService myMemoryCacheService = new MemoryCacheService(new JpaStorageSettings());

	@Mock(answer = Answers.RETURNS_DEEP_STUBS)
	private EntityManager myEntityManager;

	@Mock
	private PartitionSettings myPartitionSettings;

	@Mock
	private TypedQuery myTypedQuery;

	@BeforeEach
	void setUp() {
		myHelperSvc.setDontCheckActiveTransactionForUnitTest(true);

		// lenient because some tests require this setup, and others do not
		lenient().doReturn(true).when(myStorageSettings).isDeleteEnabled();
	}

	@Test
	public void testResolveResourceIdentities_usingCache_defaultFunctionality() {
		// ARRANGE
		RequestPartitionId partitionId = RequestPartitionId.allPartitions();
		String resourceType = "Patient";
		String resourceForcedId = "AAA";
		JpaPid resourcePid = JpaPid.fromId(1L);
		var cacheHit = new JpaResourceLookup(resourceType, resourceForcedId, resourcePid, null, resourcePid.getPartitionablePartitionId());
		var cacheKey = new MemoryCacheService.ForcedIdCacheKey(resourceType, resourceForcedId, RequestPartitionId.allPartitions());
		myMemoryCacheService.invalidateCaches(MemoryCacheService.CacheEnum.RESOURCE_LOOKUP_BY_FORCED_ID);
		myMemoryCacheService.put(MemoryCacheService.CacheEnum.RESOURCE_LOOKUP_BY_FORCED_ID, cacheKey, List.of(cacheHit));

		// ACT
		Map<IIdType, IResourceLookup<JpaPid>> result = myHelperSvc.resolveResourceIdentities(
			partitionId,
			List.of(new IdDt(resourceType, resourceForcedId)),
			ResolveIdentityMode.includeDeleted().cacheOk() // use cache
		);

		// ASSERT
		assertEquals(1, result.size());
		final IResourceLookup<JpaPid> jpaPidIResourceLookup = result.values().iterator().next();
		assertEquals(resourceForcedId, jpaPidIResourceLookup.getFhirId());
		assertEquals(resourceType, jpaPidIResourceLookup.getResourceType());
		assertEquals(resourcePid, jpaPidIResourceLookup.getPersistentId());
		assertNull(jpaPidIResourceLookup.getDeleted());
	}

	@Test
	public void testResolveResourceIdentities_withDuplicateIds_usingCache_returnsSingleResult() {
		// ARRANGE
		RequestPartitionId partitionId = RequestPartitionId.allPartitions();
		String resourceType = "Patient";
		String resourceForcedId = "AAA";
		JpaPid resourcePid = JpaPid.fromId(1L);
		var cacheHit = new JpaResourceLookup(resourceType, resourceForcedId, resourcePid, null, resourcePid.getPartitionablePartitionId());
		var cacheKey = new MemoryCacheService.ForcedIdCacheKey(resourceType, resourceForcedId, RequestPartitionId.allPartitions());
		myMemoryCacheService.invalidateCaches(MemoryCacheService.CacheEnum.RESOURCE_LOOKUP_BY_FORCED_ID);
		myMemoryCacheService.put(MemoryCacheService.CacheEnum.RESOURCE_LOOKUP_BY_FORCED_ID, cacheKey, List.of(cacheHit));
		Collection<IIdType> idsToResolve = List.of(
			new IdDt(resourceType, resourceForcedId),
			new IdDt(resourceType, resourceForcedId)
		);

		// ACT
		Map<IIdType, IResourceLookup<JpaPid>> result = myHelperSvc.resolveResourceIdentities(
			partitionId,
			idsToResolve, // contains duplicates
			ResolveIdentityMode.includeDeleted().cacheOk() // use cache
		);

		// ASSERT
		assertEquals(1, result.size());
		final IResourceLookup<JpaPid> jpaPidIResourceLookup = result.values().iterator().next();
		assertEquals(resourceForcedId, jpaPidIResourceLookup.getFhirId());
		assertEquals(resourceType, jpaPidIResourceLookup.getResourceType());
		assertEquals(resourcePid, jpaPidIResourceLookup.getPersistentId());
		assertNull(jpaPidIResourceLookup.getDeleted());
	}

	@Test
	public void testResolveResourceIdentities_usingDb_defaultFunctionality() {
		// ARRANGE
		lenient().doReturn(JpaStorageSettings.ClientIdStrategyEnum.ALPHANUMERIC).when(myStorageSettings).getResourceClientIdStrategy();
		RequestPartitionId partitionId = RequestPartitionId.fromPartitionIdAndName(1, "partition");
		String resourceType = "Patient";
		String resourceForcedId = "AAA";
		Object[] tuple = new Object[]{
			JpaPid.fromId(1L),
			"Patient",
			"AAA",
			new Date(),
			null
		};
		when(myEntityManager.createQuery(any(CriteriaQuery.class))).thenReturn(myTypedQuery);
		when(myTypedQuery.getResultList()).thenReturn(List.of(
			new TupleImpl(null, tuple)
		));

		// ACT
		Map<IIdType, IResourceLookup<JpaPid>> result = myHelperSvc.resolveResourceIdentities(
			partitionId,
			List.of(new IdDt(resourceType, resourceForcedId)),
			ResolveIdentityMode.includeDeleted().noCacheUnlessDeletesDisabled() // do not use cache
		);

		// ASSERT
		assertEquals(1, result.size());
		final IResourceLookup<JpaPid> jpaPidIResourceLookup = result.values().iterator().next();
		assertEquals(tuple[0], jpaPidIResourceLookup.getPersistentId());
		assertEquals(tuple[1], jpaPidIResourceLookup.getResourceType());
		assertEquals(tuple[3], jpaPidIResourceLookup.getDeleted());
	}

	@Test
	public void testResolveResourceIdentities_withDuplicateIds_usingDb_returnsSingleResult() {
		// ARRANGE
		lenient().doReturn(JpaStorageSettings.ClientIdStrategyEnum.ALPHANUMERIC).when(myStorageSettings).getResourceClientIdStrategy();
		RequestPartitionId partitionId = RequestPartitionId.fromPartitionIdAndName(1, "partition");
		String type1 = "Patient";
		String resource1ForcedId = "AAA";
		Object[] res1 = new Object[]{
			JpaPid.fromId(1L),
			type1,
			resource1ForcedId,
			new Date(),
			null
		};

		String type2 = "Practitioner";
		String resource2ForcedId = "BBB";
		Object[] res2 = new Object[]{
			JpaPid.fromId(2L),
			type2,
			resource2ForcedId,
			new Date(),
			null
		};

		when(myEntityManager.createQuery(any(CriteriaQuery.class))).thenReturn(myTypedQuery);
		when(myTypedQuery.getResultList()).thenReturn(List.of(
			new TupleImpl(null, res1),
			new TupleImpl(null, res2)
		));
		Collection<IIdType> idsToResolve = List.of(
			new IdDt(type1, resource1ForcedId),
			new IdDt(type2, resource2ForcedId),
			new IdDt(type1, resource1ForcedId)
		);

		// ACT
		Map<IIdType, IResourceLookup<JpaPid>> result = myHelperSvc.resolveResourceIdentities(
			partitionId,
			idsToResolve, // contains duplicates
			ResolveIdentityMode.includeDeleted().noCacheUnlessDeletesDisabled()
		);

		// ASSERT
		assertEquals(2, result.size());
		assertThat(result.keySet()).extracting(IIdType::getValue).containsExactlyInAnyOrder("Patient/AAA", "Practitioner/BBB");
		final List<Map.Entry<IIdType, IResourceLookup<JpaPid>>> entries = result.entrySet().stream().toList();
		entries.forEach(entry -> {
			IIdType id = entry.getKey();
			IResourceLookup<JpaPid> resolved = entry.getValue();
			if (id.getValue().equals("Patient/AAA")) {
				assertEquals(res1[0], resolved.getPersistentId());
				assertEquals(res1[1], resolved.getResourceType());
				assertEquals(res1[3], resolved.getDeleted());
			} else if (id.getValue().equals("Practitioner/BBB")) {
				assertEquals(res2[0], resolved.getPersistentId());
				assertEquals(res2[1], resolved.getResourceType());
				assertEquals(res2[3], resolved.getDeleted());
			} else {
				Assertions.fail("Unexpected ID: " + id.getValue());
			}
		});
	}

	@Test
	public void testResolveResourceIdentity_defaultFunctionality() {
		lenient().doReturn(JpaStorageSettings.ClientIdStrategyEnum.ALPHANUMERIC).when(myStorageSettings).getResourceClientIdStrategy();

		RequestPartitionId partitionId = RequestPartitionId.fromPartitionIdAndName(1, "partition");
		String resourceType = "Patient";
		String resourceForcedId = "AAA";

		Object[] tuple = new Object[]{
			JpaPid.fromId(1L),
			"Patient",
			"AAA",
			new Date(),
			null
		};

		when(myEntityManager.createQuery(any(CriteriaQuery.class))).thenReturn(myTypedQuery);
		when(myTypedQuery.getResultList()).thenReturn(List.of(
			new TupleImpl(null, tuple)
		));

		IResourceLookup<JpaPid> result = myHelperSvc.resolveResourceIdentity(partitionId, resourceType, resourceForcedId, ResolveIdentityMode.includeDeleted().noCacheUnlessDeletesDisabled());
		assertEquals(tuple[0], result.getPersistentId());
		assertEquals(tuple[1], result.getResourceType());
		assertEquals(tuple[3], result.getDeleted());
	}

	@Test
	public void testResolveResourceIdentity_withPersistentIdOfResourceWithForcedIdAndDefaultClientIdStrategy_returnsNotFound() {
		RequestPartitionId partitionId = RequestPartitionId.fromPartitionIdAndName(1, "partition");
		String resourceType = "Patient";

		Object[] tuple = new Object[]{
			JpaPid.fromId(1L),
			"Patient",
			"AAA",
			new Date(),
			null
		};

		when(myEntityManager.createQuery(any(CriteriaQuery.class))).thenReturn(myTypedQuery);
		when(myTypedQuery.getResultList()).thenReturn(List.of(
			new TupleImpl(null, tuple)
		));

		try {
			// Search by the PID of the resource that has a client assigned FHIR Id
			myHelperSvc.resolveResourceIdentity(partitionId, resourceType, "1", ResolveIdentityMode.includeDeleted().cacheOk());
			fail();
		} catch(ResourceNotFoundException e) {
			assertThat(e.getMessage()).isEqualTo("HAPI-2001: Resource Patient/1 is not known");
		}
	}
}
