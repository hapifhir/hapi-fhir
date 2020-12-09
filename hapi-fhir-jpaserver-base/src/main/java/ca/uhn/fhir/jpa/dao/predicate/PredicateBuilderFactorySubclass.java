package ca.uhn.fhir.jpa.dao.predicate;

import ca.uhn.fhir.jpa.dao.LegacySearchBuilder;
import org.springframework.beans.factory.annotation.Lookup;

public class PredicateBuilderFactorySubclass extends PredicateBuilderFactory {
	@Lookup
	public PredicateBuilderCoords newPredicateBuilderCoords(LegacySearchBuilder theSearchBuilder) {
		return null;
	}

	@Lookup
	public PredicateBuilderDate newPredicateBuilderDate(LegacySearchBuilder theSearchBuilder) {
		return null;
	}

	@Lookup
	public PredicateBuilderNumber newPredicateBuilderNumber(LegacySearchBuilder theSearchBuilder) {
		return null;
	}

	@Lookup
	public PredicateBuilderQuantity newPredicateBuilderQuantity(LegacySearchBuilder theSearchBuilder) {
		return null;
	}

	@Lookup
	public PredicateBuilderReference newPredicateBuilderReference(LegacySearchBuilder theSearchBuilder, PredicateBuilder thePredicateBuilder) {
		return null;
	}

	@Lookup
	public PredicateBuilderResourceId newPredicateBuilderResourceId(LegacySearchBuilder theSearchBuilder) {
		return null;
	}

	@Lookup
	public PredicateBuilderString newPredicateBuilderString(LegacySearchBuilder theSearchBuilder) {
		return null;
	}

	@Lookup
	public PredicateBuilderTag newPredicateBuilderTag(LegacySearchBuilder theSearchBuilder) {
		return null;
	}

	@Lookup
	public PredicateBuilderToken newPredicateBuilderToken(LegacySearchBuilder theSearchBuilder, PredicateBuilder thePredicateBuilder) {
		return null;
	}

	@Lookup
	public PredicateBuilderUri newPredicateBuilderUri(LegacySearchBuilder theSearchBuilder) {
		return null;
	}
}
