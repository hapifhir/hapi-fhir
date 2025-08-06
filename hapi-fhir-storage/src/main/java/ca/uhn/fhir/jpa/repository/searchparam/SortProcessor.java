package ca.uhn.fhir.jpa.repository.searchparam;

import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.api.SortSpec;

import java.util.List;
import java.util.function.Consumer;

class SortProcessor implements ISpecialParameterProcessor {
	@Override
	public void process(
			String theKey, List<IQueryParameterType> theSortItems, SearchParameterMap theSearchParameterMap) {
		List<SortSpec> sortSpecs = theSortItems.stream()
				.map(ISpecialParameterProcessor::paramAsQueryString)
				.map(SortSpec::fromR3OrLaterParameterValue)
				.toList();

		// SortSpec is an intrusive linked list, with the head as a bare pointer in the SearchParameterMap.
		Consumer<SortSpec> sortAppendAction = theSearchParameterMap::setSort;
		for (SortSpec sortSpec : sortSpecs) {
			sortAppendAction.accept(sortSpec);
			// we append at the tail
			sortAppendAction = sortSpec::setChain;
		}
	}
}
