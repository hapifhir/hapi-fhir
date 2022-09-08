package ca.uhn.fhir.jpa.search.builder.predicate;

import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.search.builder.models.MissingQueryParameterPredicateParams;
import com.healthmarketscience.sqlbuilder.Condition;

public interface ICanMakeMissingParamPredicate {
	/**
	 * Creates the condition for searching for a missing field
	 * for a given SearchParameter type.
	 *
	 * Only use if {@link DaoConfig#getIndexMissingFields()} is disabled
	 */
	Condition createPredicateParamMissingValue(MissingQueryParameterPredicateParams theParams);
}
