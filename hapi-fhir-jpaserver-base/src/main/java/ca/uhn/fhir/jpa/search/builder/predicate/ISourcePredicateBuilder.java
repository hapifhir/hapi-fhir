package ca.uhn.fhir.jpa.search.builder.predicate;

import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.model.api.IQueryParameterType;
import com.healthmarketscience.sqlbuilder.Condition;

public interface ISourcePredicateBuilder {

	Condition createPredicateMissingSourceUri();

	Condition createPredicateSourceUri(String theSourceUri);

	Condition createPredicateRequestId(String theRequestId);

	Condition createPredicateSourceUriWithModifiers(IQueryParameterType theQueryParameter, JpaStorageSettings theStorageSetting, String theSourceUri);
}
