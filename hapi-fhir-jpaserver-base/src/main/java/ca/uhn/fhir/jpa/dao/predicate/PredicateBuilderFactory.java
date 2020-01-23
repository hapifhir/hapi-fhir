package ca.uhn.fhir.jpa.dao.predicate;

import ca.uhn.fhir.jpa.dao.SearchBuilder;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.stereotype.Service;

@Service
public abstract class PredicateBuilderFactory {
	@Lookup
	public abstract PredicateBuilderCoords newPredicateBuilderCoords(SearchBuilder theSearchBuilder);
	@Lookup
	public abstract PredicateBuilderDate newPredicateBuilderDate(SearchBuilder theSearchBuilder);
	@Lookup
	public abstract PredicateBuilderNumber newPredicateBuilderNumber(SearchBuilder theSearchBuilder);
	@Lookup
	public abstract PredicateBuilderQuantity newPredicateBuilderQuantity(SearchBuilder theSearchBuilder);
	@Lookup
	public abstract PredicateBuilderString newPredicateBuilderString(SearchBuilder theSearchBuilder);
	@Lookup
	public abstract PredicateBuilderTag newPredicateBuilderTag(SearchBuilder theSearchBuilder);
	@Lookup
	public abstract PredicateBuilderToken newPredicateBuilderToken(SearchBuilder theSearchBuilder);
	@Lookup
	public abstract PredicateBuilderUri newPredicateBuilderUri(SearchBuilder theSearchBuilder);
}
