package ca.uhn.fhir.jpa.search.builder;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.server.util.FhirContextSearchParamRegistry;
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SearchBuilderTest {

	public static final FhirContext ourCtx = FhirContext.forR4Cached();
	@Spy
	private FhirContext myFhirContext = ourCtx;

	@Spy
	private ISearchParamRegistry mySearchParamRegistry = new FhirContextSearchParamRegistry(myFhirContext);

	@Spy
	private PartitionSettings myPartitionSettings = new PartitionSettings();

	@Mock(strictness = Mock.Strictness.LENIENT)
	private DaoRegistry myDaoRegistry;

	@InjectMocks
	private SearchBuilder mySearchBuilder;

	@BeforeEach
	public void beforeEach() {
		mySearchBuilder.setResourceName("QuestionnaireResponse");
		when(myDaoRegistry.getRegisteredDaoTypes()).thenReturn(ourCtx.getResourceTypes());
	}

	@Test
	void testCalculateIndexUriIdentityHashesForResourceTypes_Include_Null() {
		Set<Long> types = mySearchBuilder.calculateIndexUriIdentityHashesForResourceTypes(new SystemRequestDetails(), null, false).myHashIdentityValues;
		// There are only 12 resource types that actually can be linked to by the QuestionnaireResponse
		// resource via canonical references in any parameters
		assertThat(types).hasSize(1);
	}

	@Test
	void testCalculateIndexUriIdentityHashesForResourceTypes_Include_Nonnull() {
		Set<String> inputTypes = Set.of("Questionnaire");
		Set<Long> types = mySearchBuilder.calculateIndexUriIdentityHashesForResourceTypes(new SystemRequestDetails(), inputTypes, false).myHashIdentityValues;
		// Just the one that we actually specified
		assertThat(types).hasSize(1);
	}

	@Test
	void testCalculateIndexUriIdentityHashesForResourceTypes_RevInclude_Null() {
		Set<Long> types = mySearchBuilder.calculateIndexUriIdentityHashesForResourceTypes(new SystemRequestDetails(), null, true).myHashIdentityValues;
		// Revincludes are really hard to figure out the potential resource types for, so we just need to
		// use all active resource types
		assertThat(types).hasSize(146);
	}

	@Test
	void testPartitionBySizeAndPartitionId_ReuseIfSmallEnoughAndAllSamePartition() {
		List<JpaPid> input = List.of(
			JpaPid.fromId(100L, 1),
			JpaPid.fromId(101L, 1)
		);
		Iterable<Collection<JpaPid>> actual = SearchBuilder.partitionBySizeAndPartitionId(input, 3);
		assertSame(input, actual.iterator().next());
	}

	@Test
	void testPartitionBySizeAndPartitionId_Partitioned() {
		List<JpaPid> input = List.of(
			JpaPid.fromId(0L),
			JpaPid.fromId(1L),
			JpaPid.fromId(2L),
			JpaPid.fromId(3L),
			JpaPid.fromId(100L, 1),
			JpaPid.fromId(101L, 1),
			JpaPid.fromId(102L, 1),
			JpaPid.fromId(103L, 1),
			JpaPid.fromId(200L, 2),
			JpaPid.fromId(201L, 2),
			JpaPid.fromId(202L, 2),
			JpaPid.fromId(203L, 2)
		);

		// Test
		Iterable<Collection<JpaPid>> actual = SearchBuilder.partitionBySizeAndPartitionId(input, 3);

		// Verify
		assertThat(actual).asList().containsExactlyInAnyOrder(
			List.of(
				JpaPid.fromId(0L),
				JpaPid.fromId(1L),
				JpaPid.fromId(2L)),
			List.of(
				JpaPid.fromId(3L)),
			List.of(
				JpaPid.fromId(100L, 1),
				JpaPid.fromId(101L, 1),
				JpaPid.fromId(102L, 1)),
			List.of(
				JpaPid.fromId(103L, 1)),
			List.of(
				JpaPid.fromId(200L, 2),
				JpaPid.fromId(201L, 2),
				JpaPid.fromId(202L, 2)),
			List.of(
				JpaPid.fromId(203L, 2)
			)
		);
	}

}
