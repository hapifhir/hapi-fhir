package ca.uhn.fhir.jpa.dao.search;

import ca.uhn.fhir.jpa.model.search.ExtendedFulltextSearchParamRegistry;
import ca.uhn.fhir.rest.api.SortOrderEnum;
import ca.uhn.fhir.rest.api.SortSpec;
import com.google.common.annotations.VisibleForTesting;
import org.hibernate.search.engine.search.sort.dsl.SearchSortFactory;
import org.hibernate.search.engine.search.sort.dsl.SortFinalStep;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

/**
 * Used to build freetext sort clauses based on received parameters and fields registered at index-write time.
 */
public class ExtendedFulltextSortHelperImpl implements IExtendedFulltextSortHelper {
	private static final Logger ourLog = LoggerFactory.getLogger(ExtendedFulltextSortHelperImpl.class);

	private final ExtendedFulltextSearchParamRegistry myFulltextParameterRegistry;

	public ExtendedFulltextSortHelperImpl(ExtendedFulltextSearchParamRegistry theFulltextParameterRegistry) {
		myFulltextParameterRegistry = theFulltextParameterRegistry;
	}


	@Override
	public SortFinalStep getSortClauses(SearchSortFactory theSortFactory, SortSpec theSortParams) {
		var sortStep = theSortFactory.composite();
		Optional<SortFinalStep> sortClauseOpt = getSortClause(theSortFactory, theSortParams);
		sortClauseOpt.ifPresent(sortStep::add);

		SortSpec nextParam = theSortParams.getChain();
		while( nextParam != null ) {
			sortClauseOpt = getSortClause(theSortFactory, nextParam);
			sortClauseOpt.ifPresent(sortStep::add);

			nextParam = nextParam.getChain();
		}

		return sortStep;
	}


	private Optional<SortFinalStep> getSortClause(SearchSortFactory theF, SortSpec theSortSpec) {
		List<String> paramFieldNameList = myFulltextParameterRegistry.getFieldPaths(theSortSpec.getParamName());
		if (paramFieldNameList.isEmpty()) {
			ourLog.warn("Unable to sort by parameter '" + theSortSpec.getParamName() + "'. Sort parameter ignored.");
			return Optional.empty();
		}

		var sortFinalStep = theF.composite();
		for (String fieldName : paramFieldNameList) {
			var sortStep = theF.field(fieldName);

			if (theSortSpec.getOrder().equals(SortOrderEnum.DESC)) {
				sortStep.desc();
			} else {
				sortStep.asc();
			}

			// field could have no value
			sortFinalStep.add( sortStep.missing().last() );
		}

		return Optional.of(sortFinalStep);
	}



}
