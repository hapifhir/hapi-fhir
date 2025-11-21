package ca.uhn.fhir.jpa.repository.searchparam;

import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.model.api.Include;
import jakarta.annotation.Nonnull;

import java.util.List;
import java.util.function.BiConsumer;

class IncludeParameterProcessor implements ISpecialParameterProcessor {
	private final boolean myIterationFlag;
	private final BiConsumer<SearchParameterMap, Include> mySetter;

	private IncludeParameterProcessor(boolean theIterationFlag, BiConsumer<SearchParameterMap, Include> theSetter) {
		myIterationFlag = theIterationFlag;
		mySetter = theSetter;
	}

	@Override
	public void process(String theKey, List<IQueryParameterType> v, SearchParameterMap theSearchParameterMap) {
		v.stream()
				.map(ISpecialParameterProcessor::paramAsQueryString)
				.map(s -> new Include(s, myIterationFlag))
				.forEach(i -> mySetter.accept(theSearchParameterMap, i));
	}

	@Nonnull
	static IncludeParameterProcessor includeProcessor(boolean theIterationFlag) {
		return new IncludeParameterProcessor(theIterationFlag, SearchParameterMap::addInclude);
	}

	@Nonnull
	static IncludeParameterProcessor revincludeProcessor(boolean theIterationFlag) {
		return new IncludeParameterProcessor(theIterationFlag, SearchParameterMap::addRevInclude);
	}
}
