package ca.uhn.fhir.jpa.dao.index;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.cache.ISearchParamIdentityCacheSvc;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.jpa.model.entity.BaseResourceIndex;
import ca.uhn.fhir.jpa.model.entity.BaseResourceIndexedSearchParam;
import ca.uhn.fhir.jpa.model.entity.PartitionablePartitionId;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamNumber;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamString;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamToken;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamUri;
import ca.uhn.fhir.jpa.model.entity.ResourceLink;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.model.entity.StorageSettings;
import ca.uhn.fhir.jpa.searchparam.extractor.ResourceIndexedSearchParams;
import ca.uhn.fhir.jpa.util.AddRemoveCount;
import ca.uhn.fhir.rest.api.RestSearchParameterTypeEnum;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DaoSearchParamSynchronizerTest {
	private static final String GRITTSCORE = "grittscore";

	private static final ResourceIndexedSearchParamNumber EXISTING_SEARCH_PARAM_NUMBER = new ResourceIndexedSearchParamNumber(new PartitionSettings(), "Patient", GRITTSCORE, BigDecimal.valueOf(10));
	private static final ResourceIndexedSearchParamNumber THE_SEARCH_PARAM_NUMBER = new ResourceIndexedSearchParamNumber(new PartitionSettings(), "Patient", GRITTSCORE, BigDecimal.valueOf(12));

	private final DaoSearchParamSynchronizer subject = new DaoSearchParamSynchronizer();

	private ResourceIndexedSearchParams theParams;

	@Mock
	private ResourceTable theEntity;

	@Mock
	private ResourceTable existingEntity;

	@Mock
	private EntityManager entityManager;

	@Mock
	private ISearchParamIdentityCacheSvc searchParamIdentityCacheSvc;

	private final PartitionSettings myPartitionSettings = new PartitionSettings();
	private ResourceIndexedSearchParams existingParams;

	@BeforeEach
	void setUp() {
		when(theEntity.isParamsNumberPopulated()).thenReturn(true);
		when(theEntity.getParamsNumber()).thenReturn(List.of(THE_SEARCH_PARAM_NUMBER));
		when(theEntity.getId()).thenReturn(JpaPid.fromId(1L));
		when(theEntity.getPartitionId()).thenReturn(new PartitionablePartitionId());
		when(existingEntity.isParamsNumberPopulated()).thenReturn(true);
		when(existingEntity.getParamsNumber()).thenReturn(List.of(EXISTING_SEARCH_PARAM_NUMBER));

		theParams = ResourceIndexedSearchParams.withLists(theEntity);
		existingParams = ResourceIndexedSearchParams.withLists(existingEntity);

		final ResourceTable resourceTable = new ResourceTable();
		resourceTable.setIdForUnitTest(1L);
		resourceTable.setResourceType("Patient");
		EXISTING_SEARCH_PARAM_NUMBER.setResource(resourceTable);
		THE_SEARCH_PARAM_NUMBER.setResource(resourceTable);

		subject.setEntityManager(entityManager);
		subject.setStorageSettings(new JpaStorageSettings());
		subject.setSearchParamIdentityCacheSvc(searchParamIdentityCacheSvc);
	}

	/**
	 * A registered {@link ICustomIndexSynchronizer} is invoked once per resource sync with the
	 * extracted params and the entity, so an extension (e.g. Smile CDR compressed token indexing)
	 * can reconcile its own secondary index.
	 *
	 * <p>Created by claude-opus-4-8.</p>
	 */
	@Test
	void synchronize_invokesRegisteredCustomIndexSynchronizer() {
		ICustomIndexSynchronizer customSynchronizer = mock(ICustomIndexSynchronizer.class);
		when(customSynchronizer.synchronize(any(), any(), any(), any(), anyBoolean()))
			.thenReturn(new AddRemoveCount());
		subject.setCustomIndexSynchronizers(List.of(customSynchronizer));
		ResourceIndexedSearchParams newParams = ResourceIndexedSearchParams.withSets();
		newParams.myTokenParams.add(new ResourceIndexedSearchParamToken(
			new PartitionSettings(), "Patient", "status", "http://hl7.org/fhir/patient-status", "active"));
		ResourceIndexedSearchParams existing = ResourceIndexedSearchParams.empty();

		subject.synchronizeSearchParamsToDatabase(null, null, newParams, theEntity, existing, false);

		verify(customSynchronizer, times(1)).synchronize(null, null, newParams, theEntity, false);
	}

	/**
	 * The {@link AddRemoveCount} returned by a custom synchronizer is added to the overall
	 * synchronization total — the extension reports what it wrote and the aggregation policy stays
	 * with the caller: 1 built-in token row + 3 custom adds, 0 built-in removes + 2 custom removes.
	 */
	@Test
	void synchronize_addsCustomSynchronizerCountsToTotal() {
		ICustomIndexSynchronizer customSynchronizer = mock(ICustomIndexSynchronizer.class);
		AddRemoveCount customDelta = new AddRemoveCount();
		customDelta.addToAddCount(3);
		customDelta.addToRemoveCount(2);
		when(customSynchronizer.synchronize(any(), any(), any(), any(), anyBoolean())).thenReturn(customDelta);
		subject.setCustomIndexSynchronizers(List.of(customSynchronizer));
		ResourceIndexedSearchParams newParams = ResourceIndexedSearchParams.withSets();
		newParams.myTokenParams.add(new ResourceIndexedSearchParamToken(
			new PartitionSettings(), "Patient", "status", "http://hl7.org/fhir/patient-status", "active"));

		AddRemoveCount retVal = subject.synchronizeSearchParamsToDatabase(
			null, null, newParams, theEntity, ResourceIndexedSearchParams.empty(), false);

		assertThat(retVal.getAddCount()).isEqualTo(4);
		assertThat(retVal.getRemoveCount()).isEqualTo(2);
	}

	/**
	 * With no registered synchronizer (vanilla HAPI), no custom index synchronization happens — the
	 * default behavior is unchanged.
	 *
	 * <p>Created by claude-opus-4-8.</p>
	 */
	@Test
	void synchronize_withNoCustomSynchronizer_writesBuiltInTokenIndex() {
		when(theEntity.getResourceType()).thenReturn("Patient");
		ResourceIndexedSearchParamToken tokenParam = new ResourceIndexedSearchParamToken(
			new PartitionSettings(), "Patient", "status", "http://hl7.org/fhir/patient-status", "active");
		ResourceIndexedSearchParams newParams = ResourceIndexedSearchParams.withSets();
		newParams.myTokenParams.add(tokenParam);

		subject.synchronizeSearchParamsToDatabase(
			null, null, newParams, theEntity, ResourceIndexedSearchParams.empty(), false);

		// Built-in token index written by default → the new token row is persisted.
		verify(entityManager, times(1)).persist(tokenParam);
	}

	/**
	 * When an {@link IBuiltInIndexWritePolicy} vetoes the built-in TOKEN index, the legacy token row
	 * is NOT written, while other param types (e.g. string) are still written — proving suppression
	 * is scoped to the vetoed param type only.
	 *
	 * <p>Created by claude-opus-4-8.</p>
	 */
	@Test
	void synchronize_whenPolicySuppressesTokenIndex_skipsTokenButWritesOtherTypes() {
		when(theEntity.getResourceType()).thenReturn("Patient");
		IBuiltInIndexWritePolicy suppressTokens =
			(resourceType, paramType) -> paramType != RestSearchParameterTypeEnum.TOKEN;
		subject.setBuiltInIndexWritePolicies(List.of(suppressTokens));

		ResourceIndexedSearchParamToken tokenParam = new ResourceIndexedSearchParamToken(
			new PartitionSettings(), "Patient", "status", "http://hl7.org/fhir/patient-status", "active");
		ResourceIndexedSearchParamString stringParam = new ResourceIndexedSearchParamString(
			new PartitionSettings(), new StorageSettings(), "Patient", "name", "smith", "SMITH");
		ResourceIndexedSearchParams newParams = ResourceIndexedSearchParams.withSets();
		newParams.myTokenParams.add(tokenParam);
		newParams.myStringParams.add(stringParam);

		subject.synchronizeSearchParamsToDatabase(
			null, null, newParams, theEntity, ResourceIndexedSearchParams.empty(), false);

		verify(entityManager, never()).persist(tokenParam);
		verify(entityManager, times(1)).persist(stringParam);
	}

	/**
	 * The write policy is consulted uniformly for every scalar built-in index type, not just TOKEN:
	 * a policy vetoing STRING suppresses the string row while the token row is still written.
	 */
	@Test
	void synchronize_whenPolicySuppressesStringType_skipsStringButWritesToken() {
		when(theEntity.getResourceType()).thenReturn("Patient");
		IBuiltInIndexWritePolicy suppressStrings =
			(resourceType, paramType) -> paramType != RestSearchParameterTypeEnum.STRING;
		subject.setBuiltInIndexWritePolicies(List.of(suppressStrings));

		ResourceIndexedSearchParamToken tokenParam = new ResourceIndexedSearchParamToken(
			new PartitionSettings(), "Patient", "status", "http://hl7.org/fhir/patient-status", "active");
		ResourceIndexedSearchParamString stringParam = new ResourceIndexedSearchParamString(
			new PartitionSettings(), new StorageSettings(), "Patient", "name", "smith", "SMITH");
		ResourceIndexedSearchParams newParams = ResourceIndexedSearchParams.withSets();
		newParams.myTokenParams.add(tokenParam);
		newParams.myStringParams.add(stringParam);

		subject.synchronizeSearchParamsToDatabase(
			null, null, newParams, theEntity, ResourceIndexedSearchParams.empty(), false);

		verify(entityManager, never()).persist(stringParam);
		verify(entityManager, times(1)).persist(tokenParam);
	}

	/**
	 * The write policy is consulted once per scalar index collection — STRING, TOKEN, NUMBER,
	 * QUANTITY (regular and normalized), DATE, URI and SPECIAL (coords) — and never for resource
	 * links or combo indexes, which carry no search-parameter semantics.
	 */
	@Test
	void synchronize_policyConsultedForAllScalarIndexTypes() {
		when(theEntity.getResourceType()).thenReturn("Patient");
		List<RestSearchParameterTypeEnum> consulted = new ArrayList<>();
		IBuiltInIndexWritePolicy recordingPolicy = (resourceType, paramType) -> {
			consulted.add(paramType);
			return true;
		};
		subject.setBuiltInIndexWritePolicies(List.of(recordingPolicy));

		ResourceIndexedSearchParams newParams = ResourceIndexedSearchParams.withSets();
		newParams.myTokenParams.add(new ResourceIndexedSearchParamToken(
			new PartitionSettings(), "Patient", "status", "http://hl7.org/fhir/patient-status", "active"));

		subject.synchronizeSearchParamsToDatabase(
			null, null, newParams, theEntity, ResourceIndexedSearchParams.empty(), false);

		assertThat(consulted).containsExactlyInAnyOrder(
			RestSearchParameterTypeEnum.STRING,
			RestSearchParameterTypeEnum.TOKEN,
			RestSearchParameterTypeEnum.NUMBER,
			RestSearchParameterTypeEnum.QUANTITY,
			RestSearchParameterTypeEnum.QUANTITY,
			RestSearchParameterTypeEnum.DATE,
			RestSearchParameterTypeEnum.URI,
			RestSearchParameterTypeEnum.SPECIAL);
	}

	@Test
	void synchronizeSearchParamsNumberOnlyValuesDifferent() {
		when(theEntity.getResourceType()).thenReturn("Patient");
		final AddRemoveCount addRemoveCount =
			subject.synchronizeSearchParamsToDatabase(null, null, theParams, theEntity, existingParams, false);

		assertEquals(0, addRemoveCount.getRemoveCount());
		assertEquals(1, addRemoveCount.getAddCount());

		verify(entityManager, never()).remove(any(BaseResourceIndex.class));
		verify(entityManager, times(1)).persist(THE_SEARCH_PARAM_NUMBER);
		long expectedSpIdentity = BaseResourceIndexedSearchParam
			.calculateHashIdentity(myPartitionSettings, RequestPartitionId.defaultPartition(myPartitionSettings), "Patient", GRITTSCORE);
		verify(searchParamIdentityCacheSvc, times(1))
			.findOrCreateSearchParamIdentity(expectedSpIdentity, "Patient", GRITTSCORE);
	}

	/**
	 * Verifies that {@code synchronize()} populates the {@code @ManyToOne} entity reference on a new
	 * token search param so that Hibernate's {@code InsertActionSorter} can detect the FK dependency
	 * to {@code HFJ_RESOURCE} when JDBC batch inserts are active (SMILE-11562).
	 *
	 * <p>Created by claude-sonnet-4-6.</p>
	 */
	@Test
	void synchronize_setsEntityReferenceOnNewTokenSearchParam() {
		when(theEntity.getResourceType()).thenReturn("Patient");
		ResourceIndexedSearchParamToken tokenParam = new ResourceIndexedSearchParamToken(
			new PartitionSettings(), "Patient", "status", "http://hl7.org/fhir/patient-status", "active");
		ResourceIndexedSearchParams newParams = ResourceIndexedSearchParams.withSets();
		newParams.myTokenParams.add(tokenParam);

		subject.synchronizeSearchParamsToDatabase(
			null, null, newParams, theEntity, ResourceIndexedSearchParams.empty(), false);

		assertThat(tokenParam.getResource()).isSameAs(theEntity);
	}

	/**
	 * Verifies that {@code synchronize()} populates the {@code @ManyToOne} entity reference on a new
	 * string search param so that Hibernate's {@code InsertActionSorter} can detect the FK dependency
	 * to {@code HFJ_RESOURCE} when JDBC batch inserts are active (SMILE-11562).
	 *
	 * <p>Created by claude-sonnet-4-6.</p>
	 */
	@Test
	void synchronize_setsEntityReferenceOnNewStringSearchParam() {
		when(theEntity.getResourceType()).thenReturn("Patient");
		ResourceIndexedSearchParamString stringParam = new ResourceIndexedSearchParamString(
			new PartitionSettings(), new StorageSettings(), "Patient", "name", "smith", "SMITH");
		ResourceIndexedSearchParams newParams = ResourceIndexedSearchParams.withSets();
		newParams.myStringParams.add(stringParam);

		subject.synchronizeSearchParamsToDatabase(
			null, null, newParams, theEntity, ResourceIndexedSearchParams.empty(), false);

		assertThat(stringParam.getResource()).isSameAs(theEntity);
	}

	/**
	 * Verifies that {@code synchronize()} populates the {@code @ManyToOne} entity reference on a new
	 * URI search param so that Hibernate's {@code InsertActionSorter} can detect the FK dependency
	 * to {@code HFJ_RESOURCE} when JDBC batch inserts are active (SMILE-11562).
	 *
	 * <p>Created by claude-sonnet-4-6.</p>
	 */
	@Test
	void synchronize_setsEntityReferenceOnNewUriSearchParam() {
		when(theEntity.getResourceType()).thenReturn("Patient");
		ResourceIndexedSearchParamUri uriParam = new ResourceIndexedSearchParamUri(
			new PartitionSettings(), "Patient", "url", "http://example.org");
		ResourceIndexedSearchParams newParams = ResourceIndexedSearchParams.withSets();
		newParams.myUriParams.add(uriParam);

		subject.synchronizeSearchParamsToDatabase(
			null, null, newParams, theEntity, ResourceIndexedSearchParams.empty(), false);

		assertThat(uriParam.getResource()).isSameAs(theEntity);
	}

	/**
	 * Verifies that {@code synchronize()} populates the target {@code @ManyToOne} entity reference on
	 * a new resource link so that Hibernate's {@code InsertActionSorter} can detect the FK dependency
	 * to the target {@code HFJ_RESOURCE} row when JDBC batch inserts are active (SMILE-11562).
	 *
	 * <p>Created by claude-sonnet-4-6.</p>
	 */
	@Test
	void synchronize_setsTargetEntityReferenceOnNewResourceLink() {
		ResourceLink link = new ResourceLink();
		link.setTargetResource("Composition", 42L, "42");
		ResourceIndexedSearchParams newParams = ResourceIndexedSearchParams.withSets();
		newParams.myLinks.add(link);

		subject.synchronizeSearchParamsToDatabase(
			null, null, newParams, theEntity, ResourceIndexedSearchParams.empty(), false);

		// Verify getReference() was called with the correct target PID — this is how we confirm
		// that setTargetResourceTable() was invoked, since myTargetResource has no public getter
		// (it is the @ManyToOne field inspected internally by Hibernate's InsertActionSorter).
		verify(entityManager).getReference(ResourceTable.class, JpaPid.fromId(42L, (Integer) null));
	}
}
