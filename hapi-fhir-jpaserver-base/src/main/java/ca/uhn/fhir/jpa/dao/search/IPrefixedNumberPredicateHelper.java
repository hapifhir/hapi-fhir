package ca.uhn.fhir.jpa.dao.search;

import ca.uhn.fhir.rest.param.ParamPrefixEnum;
import org.hibernate.search.engine.search.predicate.dsl.BooleanPredicateClausesStep;
import org.hibernate.search.engine.search.predicate.dsl.SearchPredicateFactory;

public interface IPrefixedNumberPredicateHelper {

	void addPredicate(SearchPredicateFactory theFactory, BooleanPredicateClausesStep<?> theBool, ParamPrefixEnum thePrefix, double theValue, String thePropertyPath);

}
