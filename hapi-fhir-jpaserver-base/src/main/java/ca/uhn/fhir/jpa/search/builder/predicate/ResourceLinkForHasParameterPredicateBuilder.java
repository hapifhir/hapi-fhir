package ca.uhn.fhir.jpa.search.builder.predicate;

import ca.uhn.fhir.jpa.search.builder.QueryStack;
import ca.uhn.fhir.jpa.search.builder.sql.SearchQueryBuilder;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbColumn;

public class ResourceLinkForHasParameterPredicateBuilder extends ResourceLinkPredicateBuilder {

	/**
	 * Constructor
	 */
	public ResourceLinkForHasParameterPredicateBuilder(QueryStack theQueryStack, SearchQueryBuilder theSearchSqlBuilder) {
		super(theQueryStack, theSearchSqlBuilder);
	}

	@Override
	public DbColumn getResourceTypeColumn() {
		return myColumnTargetResourceType;
	}

	@Override
	public DbColumn getPartitionIdColumn() {
		return myColumnTargetPartitionId;
	}

	@Override
	public DbColumn getResourceIdColumn() {
		return myColumnTargetResourceId;
	}


}
