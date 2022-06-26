package ca.uhn.fhir.jpa.dao.search;

import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.api.RestSearchParameterTypeEnum;
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;
import org.hibernate.search.engine.search.predicate.dsl.BooleanPredicateClausesStep;
import org.hibernate.search.engine.search.predicate.dsl.SearchPredicateFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public abstract class HSearchParamHelper<T extends IQueryParameterType> {
	protected final Logger ourLog = LoggerFactory.getLogger(getClass());

	@Autowired
	private ISearchParamRegistry mySearchParamRegistry;

	/**
	 * Map of specific typed children, which must self-register in constructor
	 */
	private static final Map<RestSearchParameterTypeEnum, HSearchParamHelper<? extends IQueryParameterType>> ourTypedHelperMap = new HashMap<>();


	public abstract <P extends IQueryParameterType> Optional<Object> getParamPropertyValue(P theParam, String thePropName);

	protected abstract RestSearchParameterTypeEnum getParamEnumType();

	public abstract boolean isNested();


	public static void registerChildHelper(HSearchParamHelper<?> theChild) {
		ourTypedHelperMap.put(theChild.getParamEnumType(), theChild);
	}

	public List<String> getParamPropertiesForParameter(String theParamName, IQueryParameterType theParam) {
		return getParamProperties(theParam).stream()
			.map(p -> mergeParamIntoProperty(p, theParamName)) .collect(Collectors.toList());
	}

	protected String mergeParamIntoProperty(String thePropertyName, String theParameterName) {
		return thePropertyName.replace("*", theParameterName);
	}

	public abstract List<String> getParamProperties(IQueryParameterType theParam);

	public static Map<RestSearchParameterTypeEnum, HSearchParamHelper<? extends IQueryParameterType>> getTypeHelperMap() {
		return HSearchParamHelper.ourTypedHelperMap;
	}


	/**
	 * Addition of clauses for most parameter types. Overrides for NUMBER, QUANTITY, etc
	 */
	public <P extends IQueryParameterType> void addOrClauses(SearchPredicateFactory theFactory,
				BooleanPredicateClausesStep<?> theBool, String theParamName, P theParam) {

		List<String> paramProperties = getParamPropertiesForParameter(theParamName, theParam);
		for (String paramProperty : paramProperties) {
			Optional<Object> paramPropertyValue = getParamPropertyValue(theParam, paramProperty);
			paramPropertyValue.ifPresent( v -> theBool.must( theFactory.match().field(paramProperty).matching(v) ));
		}
	}

	public void processOrTerms(SearchPredicateFactory theFactory, BooleanPredicateClausesStep<?> theBool,
					List<IQueryParameterType> theOrTerms, String theParamName, HSearchParamHelper<?> theParamHelper) {

		if (theOrTerms.size() == 1) {
			theParamHelper.addOrClauses(theFactory, theBool, theParamName, theOrTerms.get(0));
			return;
		}

		// multiple or predicates must be in must group with multiple should(s) with a minimumShouldMatchNumber(1)
		theBool.must(theFactory.bool(b2 -> {
			b2.minimumShouldMatchNumber(1);

			for (IQueryParameterType orTerm : theOrTerms) {
				var paramBool = theFactory.bool();
				theParamHelper.addOrClauses(theFactory, paramBool, theParamName, orTerm);
				b2.should(paramBool);
			}
		}));

	}
}
