package ca.uhn.fhir.jpa.search.builder;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.rest.server.util.FhirContextSearchParamRegistry;
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
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
		Set<Long> types = mySearchBuilder.calculateIndexUriIdentityHashesForResourceTypes(null, false);
		// There are only 12 resource types that actually can be linked to by the QuestionnaireResponse
		// resource via canonical references in any parameters
		assertThat(types).hasSize(1);
	}

	@Test
	void testCalculateIndexUriIdentityHashesForResourceTypes_Include_Nonnull() {
		Set<String> inputTypes = Set.of("Questionnaire");
		Set<Long> types = mySearchBuilder.calculateIndexUriIdentityHashesForResourceTypes(inputTypes, false);
		// Just the one that we actually specified
		assertThat(types).hasSize(1);
	}

	@Test
	void testCalculateIndexUriIdentityHashesForResourceTypes_RevInclude_Null() {
		Set<Long> types = mySearchBuilder.calculateIndexUriIdentityHashesForResourceTypes(null, true);
		// Revincludes are really hard to figure out the potential resource types for, so we just need to
		// use all active resource types
		assertThat(types).hasSize(146);
	}

}
