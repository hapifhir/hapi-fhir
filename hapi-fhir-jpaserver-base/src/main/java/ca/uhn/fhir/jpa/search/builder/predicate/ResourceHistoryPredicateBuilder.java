package ca.uhn.fhir.jpa.search.builder.predicate;

import ca.uhn.fhir.jpa.search.builder.sql.SearchQueryBuilder;

import static ca.uhn.fhir.jpa.search.builder.predicate.ResourceHistoryProvenancePredicateBuilder.CONTAINS_MODIFIER_DISABLED_MSG_CODE;

public class ResourceHistoryPredicateBuilder extends BaseResourceHistoryPredicateBuilder
		implements ISourcePredicateBuilder {

	public ResourceHistoryPredicateBuilder(SearchQueryBuilder theSearchSqlBuilder) {
		super(theSearchSqlBuilder, theSearchSqlBuilder.addTable("HFJ_RES_VER"), "RES_ID");
	}

	@Override
	protected int getContainsModifierDisabledCode() {
		return CONTAINS_MODIFIER_DISABLED_MSG_CODE;
	}
}
