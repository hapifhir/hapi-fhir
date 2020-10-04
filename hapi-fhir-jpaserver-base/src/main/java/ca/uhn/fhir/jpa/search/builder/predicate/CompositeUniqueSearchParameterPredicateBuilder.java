package ca.uhn.fhir.jpa.search.builder.predicate;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.search.builder.sql.SearchQueryBuilder;
import com.healthmarketscience.sqlbuilder.BinaryCondition;
import com.healthmarketscience.sqlbuilder.Condition;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbColumn;

public class CompositeUniqueSearchParameterPredicateBuilder extends BaseSearchParamPredicateBuilder {

	private final DbColumn myColumnString;

	/**
	 * Constructor
	 */
	public CompositeUniqueSearchParameterPredicateBuilder(SearchQueryBuilder theSearchSqlBuilder) {
		super(theSearchSqlBuilder, theSearchSqlBuilder.addTable("HFJ_IDX_CMP_STRING_UNIQ"));

		myColumnString = getTable().addColumn("IDX_STRING");
	}


	public Condition createPredicateIndexString(RequestPartitionId theRequestPartitionId, String theIndexString) {
		BinaryCondition predicate = BinaryCondition.equalTo(myColumnString, generatePlaceholder(theIndexString));
		return combineWithRequestPartitionIdPredicate(theRequestPartitionId, predicate);
	}
}
