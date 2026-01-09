package ca.uhn.fhir.jpa.repository.searchparam;

import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.model.api.IQueryParameterType;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

import static ca.uhn.fhir.jpa.repository.searchparam.ISpecialParameterProcessor.paramAsQueryString;

/**
 * A processor that takes the last value of a parameter and converts it to a type T while treating null or blank as null.
 * @param <T> the type used in the SearchParameterMap setter.
 */
class LastValueWinsParameterProcessor<T> implements ISpecialParameterProcessor {
	private final Function<String, T> myConverter;
	private final BiConsumer<SearchParameterMap, T> mySearchParameterMapSetter;

	LastValueWinsParameterProcessor(
			Function<String, T> theConverter, BiConsumer<SearchParameterMap, T> theSearchParameterMapSetter) {
		myConverter = theConverter;
		mySearchParameterMapSetter = theSearchParameterMapSetter;
	}

	@Override
	public void process(
			String theParamName, List<IQueryParameterType> theValues, SearchParameterMap theSearchParameterMap) {

		T converted = null;
		if (CollectionUtils.isNotEmpty(theValues)) {
			IQueryParameterType lastParameter = theValues.get(theValues.size() - 1);
			String lastValue = paramAsQueryString(lastParameter);
			converted = myConverter.apply(lastValue);
		}

		mySearchParameterMapSetter.accept(theSearchParameterMap, converted);
	}

	/**
	 * Build a processor that takes the last value of a parameter, converts it to a type,
	 * and sets a single value on the SearchParameterMap.
	 * Treats null or blank values as null.
	 * @param <T> the type used in the SearchParameterMap setter.
	 */
	public static <T> ISpecialParameterProcessor lastValueWins(
			Function<String, T> theConverter, BiConsumer<SearchParameterMap, T> theSearchParameterMapSetter) {
		return new LastValueWinsParameterProcessor<>(theConverter, theSearchParameterMapSetter);
	}
}
