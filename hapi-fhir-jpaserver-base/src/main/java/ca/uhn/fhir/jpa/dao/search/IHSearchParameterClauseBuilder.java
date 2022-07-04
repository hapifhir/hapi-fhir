package ca.uhn.fhir.jpa.dao.search;

import ca.uhn.fhir.rest.param.ParamPrefixEnum;
import org.hibernate.search.engine.search.predicate.dsl.BooleanPredicateClausesStep;

public interface IHSearchParameterClauseBuilder {

	void addPredicate(ParamPrefixEnum thePrefix, BooleanPredicateClausesStep<?> thePredicateStep, String theFieldPath, double theValue);

}
