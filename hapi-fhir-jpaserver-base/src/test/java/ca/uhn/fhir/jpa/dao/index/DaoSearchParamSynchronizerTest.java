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
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

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
	void synchronizeSearchParamsNumberOnlyValuesDifferent() {
		when(theEntity.getResourceType()).thenReturn("Patient");
		final AddRemoveCount addRemoveCount = subject.synchronizeSearchParamsToDatabase(null, null, theParams, theEntity, existingParams);

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

		subject.synchronizeSearchParamsToDatabase(null, null, newParams, theEntity, ResourceIndexedSearchParams.empty());

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

		subject.synchronizeSearchParamsToDatabase(null, null, newParams, theEntity, ResourceIndexedSearchParams.empty());

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

		subject.synchronizeSearchParamsToDatabase(null, null, newParams, theEntity, ResourceIndexedSearchParams.empty());

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

		subject.synchronizeSearchParamsToDatabase(null, null, newParams, theEntity, ResourceIndexedSearchParams.empty());

		// Verify getReference() was called with the correct target PID — this is how we confirm
		// that setTargetResourceTable() was invoked, since myTargetResource has no public getter
		// (it is the @ManyToOne field inspected internally by Hibernate's InsertActionSorter).
		verify(entityManager).getReference(ResourceTable.class, JpaPid.fromId(42L, (Integer) null));
	}
}
