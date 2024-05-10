package ca.uhn.fhir.jpa.dao.search;

import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.rest.api.RestSearchParameterTypeEnum;
import ca.uhn.fhir.rest.api.SortOrderEnum;
import ca.uhn.fhir.rest.api.SortSpec;
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;
import ca.uhn.fhir.rest.server.util.ResourceSearchParams;
import org.hibernate.search.engine.search.sort.dsl.CompositeSortComponentsStep;
import org.hibernate.search.engine.search.sort.dsl.FieldSortMissingValueBehaviorStep;
import org.hibernate.search.engine.search.sort.dsl.FieldSortOptionsStep;
import org.hibernate.search.engine.search.sort.dsl.SearchSortFactory;
import org.hibernate.search.engine.search.sort.dsl.SortFinalStep;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings({"rawtypes", "unchecked"})
class HSearchSortHelperImplTest {

	@InjectMocks
	@Spy private HSearchSortHelperImpl tested;

	@Mock private ISearchParamRegistry mockSearchParamRegistry;
	@Mock private ResourceSearchParams mockResourceSearchParams;
	@Mock private RuntimeSearchParam mockRuntimeSearchParam;

	@Mock private SearchSortFactory mockSearchSortFactory;
	@Mock private CompositeSortComponentsStep mockCompositeSortComponentsStep;
	@Mock private FieldSortOptionsStep mockFieldSortOptionsStep;
	@Mock private SortFinalStep mockSortFinalStep;
	@Mock private FieldSortMissingValueBehaviorStep mockFieldSortMissingValueBehaviorStep;



	/**
	 * Validates gets from map theParamType and replaces '*' in name by theParamName
	 */
	@Test
	void testGetSortPropertyList() {
		SortSpec sortSpec = new SortSpec();
		sortSpec.setParamName("_tag");

		List<String> sortPropertyList = tested.getSortPropertyList(RestSearchParameterTypeEnum.TOKEN, "the-param-name");

		assertThat(sortPropertyList).containsExactly("nsp.the-param-name.token.system", "nsp.the-param-name.token.code");
	}

	/**
	 * Validates invokes SearchParamRegistry.getActiveSearchParams for received resourceTypeName and returns the
	 * RuntimeSearchParam for the param name
	 */
	@Test
	void testGetParamType() {
		SortSpec sortSpec = new SortSpec();
		sortSpec.setParamName("_tag");
		when(mockSearchParamRegistry.getActiveSearchParams("Observation")).thenReturn(mockResourceSearchParams);
		when(mockResourceSearchParams.get("the-param-name")).thenReturn(mockRuntimeSearchParam);
		when(mockRuntimeSearchParam.getParamType()).thenReturn(RestSearchParameterTypeEnum.TOKEN);

		Optional<RestSearchParameterTypeEnum> paramType = tested.getParamType("Observation", "the-param-name");

		verify(mockSearchParamRegistry, times(1)).getActiveSearchParams("Observation");
		verify(mockResourceSearchParams, times(1)).get("the-param-name");
		assertFalse(paramType.isEmpty());
	}

	@Test
	void testGetSortClause() {
		SortSpec sortSpec = new SortSpec();
		sortSpec.setParamName("_tag");
		sortSpec.setOrder(SortOrderEnum.DESC);
		doReturn(Optional.of(RestSearchParameterTypeEnum.TOKEN)).when(tested).getParamType("Observation", "_tag");
		doReturn(List.of("aaa._tag.bbb.ccc", "ddd._tag.eee.fff")).when(tested).getSortPropertyList(RestSearchParameterTypeEnum.TOKEN, "_tag");
		when(mockSearchSortFactory.composite()).thenReturn(mockCompositeSortComponentsStep);
		when(mockSearchSortFactory.field("aaa._tag.bbb.ccc")).thenReturn(mockFieldSortOptionsStep);
		when(mockSearchSortFactory.field("ddd._tag.eee.fff")).thenReturn(mockFieldSortOptionsStep);
		when(mockFieldSortOptionsStep.missing()).thenReturn(mockFieldSortMissingValueBehaviorStep);

		Optional<SortFinalStep> sortFieldStepOpt = tested.getSortClause(mockSearchSortFactory, sortSpec, "Observation");

		assertFalse(sortFieldStepOpt.isEmpty());
		verify(mockSearchSortFactory, times(1)).composite();
		verify(mockSearchSortFactory, times(1)).field("aaa._tag.bbb.ccc");
		verify(mockSearchSortFactory, times(1)).field("ddd._tag.eee.fff");
		verify(mockFieldSortOptionsStep, times(2)).desc();
		verify(mockFieldSortMissingValueBehaviorStep, times(2)).last();
	}

	@Test
	void testGetSortClauses() {
		SortSpec sortSpec = new SortSpec();
		sortSpec.setParamName("_tag");

		SortSpec sortSpec2 = new SortSpec();
		sortSpec2.setParamName("param-name-B");
		sortSpec2.setOrder(SortOrderEnum.ASC);

		sortSpec.setChain(sortSpec2);

		when(mockSearchSortFactory.composite()).thenReturn(mockCompositeSortComponentsStep);
		doReturn(Optional.of(mockSortFinalStep)).when(tested).getSortClause(mockSearchSortFactory, sortSpec, "Observation");
		doReturn(Optional.of(mockSortFinalStep)).when(tested).getSortClause(mockSearchSortFactory, sortSpec2, "Observation");

		SortFinalStep sortFinalStep = tested.getSortClauses(mockSearchSortFactory, sortSpec, "Observation");

		verify(mockSearchSortFactory, times(1)).composite();
		verify(tested, times(1)).getSortClause(mockSearchSortFactory, sortSpec, "Observation");
		verify(tested, times(1)).getSortClause(mockSearchSortFactory, sortSpec2, "Observation");
		verify(mockCompositeSortComponentsStep, times(2)).add(mockSortFinalStep);

	}

}
