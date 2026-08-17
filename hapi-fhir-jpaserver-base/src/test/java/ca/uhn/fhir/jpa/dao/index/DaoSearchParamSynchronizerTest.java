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
import ca.uhn.fhir.jpa.search.builder.predicate.BaseSearchParamPredicateBuilder;
import ca.uhn.fhir.jpa.search.builder.sql.SearchQueryBuilder;
import ca.uhn.fhir.jpa.searchparam.extractor.ResourceIndexedSearchParams;
import ca.uhn.fhir.jpa.util.AddRemoveCount;
import ca.uhn.fhir.rest.api.RestSearchParameterTypeEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
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

	@Test
	void synchronize_invokesRegisteredProviderForItsType() {
		TestIndexProvider provider = new TestIndexProvider(RestSearchParameterTypeEnum.TOKEN, null);
		subject.setSearchParamIndexProviderRegistry(new SearchParamIndexProviderRegistry(List.of(provider)));
		ResourceIndexedSearchParams newParams = ResourceIndexedSearchParams.withSets();
		ResourceIndexedSearchParamToken tokenParam = new ResourceIndexedSearchParamToken(
			new PartitionSettings(), "Patient", "status", "http://hl7.org/fhir/patient-status", "active");
		newParams.myTokenParams.add(tokenParam);

		subject.synchronizeSearchParamsToDatabase(
			null, null, newParams, theEntity, ResourceIndexedSearchParams.empty(), false);

		// invoked exactly once with token params of the resource
		assertThat(provider.mySynchronizeCount).isEqualTo(1);
		assertThat(provider.myLastNewParams).singleElement().isSameAs(tokenParam);
		assertThat(provider.myLastEntity).isSameAs(theEntity);
		assertThat(provider.myLastResourceIsBeingCreated).isFalse();
	}

	@Test
	void synchronize_addsProviderCountsToTotal() {
		AddRemoveCount customDelta = new AddRemoveCount();
		customDelta.addToAddCount(3);
		customDelta.addToRemoveCount(2);
		TestIndexProvider provider =
			new TestIndexProvider(RestSearchParameterTypeEnum.TOKEN, null).withDelta(customDelta);
		subject.setSearchParamIndexProviderRegistry(new SearchParamIndexProviderRegistry(List.of(provider)));
		ResourceIndexedSearchParams newParams = ResourceIndexedSearchParams.withSets();
		newParams.myTokenParams.add(new ResourceIndexedSearchParamToken(
			new PartitionSettings(), "Patient", "status", "http://hl7.org/fhir/patient-status", "active"));

		AddRemoveCount retVal = subject.synchronizeSearchParamsToDatabase(
			null, null, newParams, theEntity, ResourceIndexedSearchParams.empty(), false);

		assertThat(retVal.getAddCount()).isEqualTo(4);
		assertThat(retVal.getRemoveCount()).isEqualTo(2);
	}

	@Test
	void synchronize_withNoCustomProvider_writesBuiltInTokenIndex() {
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

	@Test
	void synchronize_whenProviderSuppressesTokenIndex_skipsTokenButWritesOtherTypes() {
		when(theEntity.getResourceType()).thenReturn("Patient");
		TestIndexProvider suppressTokens = new TestIndexProvider(null, RestSearchParameterTypeEnum.TOKEN);
		subject.setSearchParamIndexProviderRegistry(new SearchParamIndexProviderRegistry(List.of(suppressTokens)));

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

	@Test
	void synchronize_builtInSuppressionConsultedForTokenOnly() {
		when(theEntity.getResourceType()).thenReturn("Patient");
		TestIndexProvider testIndexProvider = new TestIndexProvider(null, null);
		subject.setSearchParamIndexProviderRegistry(new SearchParamIndexProviderRegistry(List.of(testIndexProvider)));

		ResourceIndexedSearchParams newParams = ResourceIndexedSearchParams.withSets();
		newParams.myTokenParams.add(new ResourceIndexedSearchParamToken(
			new PartitionSettings(), "Patient", "status", "http://hl7.org/fhir/patient-status", "active"));

		subject.synchronizeSearchParamsToDatabase(
			null, null, newParams, theEntity, ResourceIndexedSearchParams.empty(), false);

		assertThat(testIndexProvider.mySuppressQueries).containsExactly(RestSearchParameterTypeEnum.TOKEN);
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

	/**
	 * Test {@link ISearchParamIndexProvider} that records how it was invoked. It supports (for writes)
	 * a single configurable param type and optionally suppresses the built-in index for another.
	 */
	private static class TestIndexProvider implements ISearchParamIndexProvider {
		private final RestSearchParameterTypeEnum myHandledType;
		private final RestSearchParameterTypeEnum mySuppressedType;
		private AddRemoveCount myDelta = new AddRemoveCount();

		private int mySynchronizeCount = 0;
		private Collection<? extends BaseResourceIndex> myLastNewParams;
		private ResourceTable myLastEntity;
		private boolean myLastResourceIsBeingCreated;
		private final List<RestSearchParameterTypeEnum> mySuppressQueries = new ArrayList<>();

		TestIndexProvider(RestSearchParameterTypeEnum theHandledType, RestSearchParameterTypeEnum theSuppressedType) {
			myHandledType = theHandledType;
			mySuppressedType = theSuppressedType;
		}

		TestIndexProvider withDelta(AddRemoveCount theDelta) {
			myDelta = theDelta;
			return this;
		}

		@Override
		public boolean supports(SearchParamIndexRouting theRouting) {
			return myHandledType != null && theRouting.getParamType() == myHandledType;
		}

		@Override
		public Optional<BaseSearchParamPredicateBuilder> createPredicateBuilder(
				SearchQueryBuilder theSqlBuilder, String theParamName) {
			return Optional.empty();
		}

		@Override
		public AddRemoveCount synchronize(
				RequestDetails theRequestDetails,
				Collection<? extends BaseResourceIndex> theExtractedParams,
				ResourceTable theEntity,
				boolean theResourceIsBeingCreated) {
			mySynchronizeCount++;
			myLastNewParams = theExtractedParams;
			myLastEntity = theEntity;
			myLastResourceIsBeingCreated = theResourceIsBeingCreated;
			return myDelta;
		}

		@Override
		public void deleteByResourceId(JpaPid theResourceId) {
			// no-op test provider
		}

		@Override
		public boolean suppressesBuiltInIndex(SearchParamIndexRouting theRouting) {
			mySuppressQueries.add(theRouting.getParamType());
			return mySuppressedType != null && theRouting.getParamType() == mySuppressedType;
		}
	}
}
