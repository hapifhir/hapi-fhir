package ca.uhn.fhir.jpa.dao.search;

import ca.uhn.fhir.jpa.model.search.ExtendedFulltextSearchParamRegistry;
import ca.uhn.fhir.rest.api.SortOrderEnum;
import ca.uhn.fhir.rest.api.SortSpec;
import org.hibernate.search.engine.search.sort.dsl.CompositeSortComponentsStep;
import org.hibernate.search.engine.search.sort.dsl.FieldSortMissingValueBehaviorStep;
import org.hibernate.search.engine.search.sort.dsl.FieldSortOptionsStep;
import org.hibernate.search.engine.search.sort.dsl.SearchSortFactory;
import org.hibernate.search.engine.search.sort.dsl.SortFinalStep;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings({"rawtypes", "unchecked"})
class ExtendedFulltextSortHelperImplTest {

	@Mock
	private ExtendedFulltextSearchParamRegistry mockExtendedFulltextSearchParamRegistry;

	@InjectMocks
	private ExtendedFulltextSortHelperImpl tested;

	@Mock private SearchSortFactory mockSearchSortFactory;

	@Mock private CompositeSortComponentsStep mockCompositeSortComponentsStep;
	@Mock private FieldSortOptionsStep mockFieldSortOptionsStep;
	@Mock private FieldSortMissingValueBehaviorStep mockFieldSortMissingValueBehaviorStep;

	@Nested
	public class TestgetSortClauseMethod {

		@Test
		void noFilePathsReturnsEmpty() {
			SortSpec sortSpec = new SortSpec();
			sortSpec.setParamName("param-name");
			sortSpec.setOrder(SortOrderEnum.DESC);

			when(mockExtendedFulltextSearchParamRegistry.getFieldPaths("param-name")).thenReturn(Collections.emptyList() );
			when(mockSearchSortFactory.composite()).thenReturn(mockCompositeSortComponentsStep);

			SortFinalStep sortFieldStep = tested.getSortClauses(mockSearchSortFactory, sortSpec);

			verify(mockCompositeSortComponentsStep, never()).add(any(SortFinalStep.class));
			assertTrue(sortFieldStep != null);
			verify(mockSearchSortFactory, only()).composite();
			verify(mockFieldSortOptionsStep, never()).desc();
			verify(mockFieldSortMissingValueBehaviorStep, never()).last();
		}

		@Test
		void forOneSortParam() {
			SortSpec sortSpec = new SortSpec();
			sortSpec.setParamName("param-name");
			sortSpec.setOrder(SortOrderEnum.DESC);

			when(mockExtendedFulltextSearchParamRegistry.getFieldPaths("param-name"))
				.thenReturn( List.of("sp.field-1", "sp.field-2") );
			when(mockSearchSortFactory.composite()).thenReturn(mockCompositeSortComponentsStep);
			when(mockSearchSortFactory.field("sp.field-1")).thenReturn(mockFieldSortOptionsStep);
			when(mockSearchSortFactory.field("sp.field-2")).thenReturn(mockFieldSortOptionsStep);
			when(mockFieldSortOptionsStep.missing()).thenReturn(mockFieldSortMissingValueBehaviorStep);

			tested.getSortClauses(mockSearchSortFactory, sortSpec);

			verify(mockSearchSortFactory, times(2)).composite();
			verify(mockSearchSortFactory).field("sp.field-1");
			verify(mockSearchSortFactory).field("sp.field-2");
			verify(mockFieldSortOptionsStep, times(2)).desc();
			verify(mockFieldSortMissingValueBehaviorStep, times(2)).last();
		}

		@Test
		void forMultipleSortParams() {
			SortSpec sortSpec = new SortSpec();
			sortSpec.setParamName("param-name-A");
			sortSpec.setOrder(SortOrderEnum.DESC);

			SortSpec sortSpec2 = new SortSpec();
			sortSpec2.setParamName("param-name-B");
			sortSpec2.setOrder(SortOrderEnum.ASC);

			sortSpec.setChain(sortSpec2);

			when(mockExtendedFulltextSearchParamRegistry.getFieldPaths("param-name-A"))
				.thenReturn( List.of("sp.param-name-A.field-1", "sp.param-name-A.field-2") );
			when(mockExtendedFulltextSearchParamRegistry.getFieldPaths("param-name-B"))
				.thenReturn( List.of("sp.param-name-B.field-1", "sp.param-name-B.field-2") );
			when(mockSearchSortFactory.composite()).thenReturn(mockCompositeSortComponentsStep);
			when(mockSearchSortFactory.field("sp.param-name-A.field-1")).thenReturn(mockFieldSortOptionsStep);
			when(mockSearchSortFactory.field("sp.param-name-A.field-2")).thenReturn(mockFieldSortOptionsStep);
			when(mockSearchSortFactory.field("sp.param-name-B.field-1")).thenReturn(mockFieldSortOptionsStep);
			when(mockSearchSortFactory.field("sp.param-name-B.field-2")).thenReturn(mockFieldSortOptionsStep);
			when(mockFieldSortOptionsStep.missing()).thenReturn(mockFieldSortMissingValueBehaviorStep);

			tested.getSortClauses(mockSearchSortFactory, sortSpec);

			verify(mockSearchSortFactory, times(3)).composite();
			verify(mockSearchSortFactory).field("sp.param-name-A.field-1");
			verify(mockSearchSortFactory).field("sp.param-name-A.field-2");
			verify(mockSearchSortFactory).field("sp.param-name-B.field-1");
			verify(mockSearchSortFactory).field("sp.param-name-B.field-2");
			verify(mockFieldSortOptionsStep, times(2)).desc();
			verify(mockFieldSortOptionsStep, times(2)).asc();
			verify(mockFieldSortMissingValueBehaviorStep, times(4)).last();
		}
	}
}
