package ca.uhn.fhir.jpa.search.builder.predicate;

import ca.uhn.fhir.jpa.search.builder.models.MissingQueryParameterPredicateParams;
import com.healthmarketscience.sqlbuilder.Condition;

public interface ICanMakeMissingParamPredicate {

	Condition createPredicateParamMissingValue(MissingQueryParameterPredicateParams theParams);
}
