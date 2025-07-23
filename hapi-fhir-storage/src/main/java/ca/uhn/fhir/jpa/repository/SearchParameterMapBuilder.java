package ca.uhn.fhir.jpa.repository;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.model.api.IQueryParameterOr;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.repository.impl.DefaultSearchQueryBuilder;
import ca.uhn.fhir.rest.param.BaseOrListParam;
import ca.uhn.fhir.rest.param.ReferenceOrListParam;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.StringOrListParam;
import com.google.common.collect.Multimap;
import org.apache.commons.lang3.Validate;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class SearchParameterMapBuilder extends DefaultSearchQueryBuilder {
	public SearchParameterMap build() {
		var p = new Processor();

		Multimap<String, List<IQueryParameterType>> multiMap = getDelegate().toMultiMap();
		multiMap.keySet().forEach(key -> p.process(key, multiMap.get(key)));

		return p.myResult;
	}

	interface IOrListBuilder extends Function<Collection<IQueryParameterType>,IQueryParameterOr<? extends IQueryParameterType>> {
	}

	private static class Processor {
		static final Map<Class<? extends IQueryParameterType>, IOrListBuilder> ourMap = Map.of(
			ReferenceParam.class, Processor::makeReferenceOrList
		);
		// fixme we won't need a fhir context after https://github.com/hapifhir/hapi-fhir/pull/7136 merges.
		public static final FhirContext DUMMY_FHIR_CONTEXT = FhirContext.forR4();
		final Map<String, BiConsumer<String,Collection<List<IQueryParameterType>>>> ourProcessors = Map.of(
			"_count", this::processCount
		);

		private void processCount(String theKey, Collection<List<IQueryParameterType>> theValues) {
			Validate.isTrue(theValues.size() == 1, "Only one _count parameter is allowed");
			List<IQueryParameterType> nextOrList = theValues.iterator().next();
			Validate.isTrue(nextOrList.size() == 1, "Only one value is allowed for _count");
			IQueryParameterType nextValue = nextOrList.get(0);
			Validate.isTrue(nextValue instanceof IQueryParameterType, "Invalid _count value: " + nextValue);
			myResult.setCount(Integer.parseInt(((IQueryParameterType) nextValue).getValueAsQueryToken(null)));
		}

		final SearchParameterMap myResult = new SearchParameterMap();

		void process(String theKey, Collection<List<IQueryParameterType>> theValues) {
			ourProcessors.getOrDefault(theKey, this::defaultProcessor)
				.accept(theKey, theValues);
		}

		private void defaultProcessor(String theKey, Collection<List<IQueryParameterType>> theValues) {
			for (List<IQueryParameterType> nextOrList : theValues) {
				myResult.add(theKey, toOrList(theKey, nextOrList));
			}
		}

		IQueryParameterOr<?> toOrList(String theKey, List<IQueryParameterType> theOrList) {
			validateHomogeneousList(theKey, theOrList);
			if (theOrList.isEmpty()) {
				return new StringOrListParam();
			}
			IQueryParameterType iQueryParameterType = theOrList.get(0);
			return getIOrListBuilder(iQueryParameterType).apply(theOrList);
		}

		IOrListBuilder getIOrListBuilder(IQueryParameterType theParam) {
			return ourMap.get(theParam.getClass());
		}

		static IQueryParameterOr<ReferenceParam> makeReferenceOrList(Collection<IQueryParameterType> theParameterValues) {
			ReferenceOrListParam referenceOrListParam = new ReferenceOrListParam();
			addAll(referenceOrListParam, theParameterValues);
			return referenceOrListParam;
		}

		static <T extends IQueryParameterType> Collection<T> validateAndCast(Class<T> theReferenceParamClass, Collection<IQueryParameterType> theParameterValues) {
			theParameterValues.forEach(nextValue -> Validate.isTrue(theReferenceParamClass.isInstance(nextValue)));
			//noinspection unchecked
			return (Collection<T>) theParameterValues;
		}

		void validateHomogeneousList(String theKey, List<IQueryParameterType> theOrList) {
			// verify modifiers and types are all the same
			// fixme test and impl
		}

		@SuppressWarnings("unchecked")
		public static void addAll(BaseOrListParam theOrList, Collection<? extends IQueryParameterType> theValues) {
			for (IQueryParameterType next : theValues) {
				theOrList.addOr(next);
			}
		}

	}

}
