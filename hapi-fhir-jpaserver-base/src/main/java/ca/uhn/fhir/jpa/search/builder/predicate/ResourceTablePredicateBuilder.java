package ca.uhn.fhir.jpa.search.builder.predicate;

import ca.uhn.fhir.jpa.search.builder.sql.SearchSqlBuilder;
import com.healthmarketscience.sqlbuilder.BinaryCondition;
import com.healthmarketscience.sqlbuilder.Condition;
import com.healthmarketscience.sqlbuilder.NotCondition;
import com.healthmarketscience.sqlbuilder.UnaryCondition;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbColumn;

import java.util.Set;

import static ca.uhn.fhir.jpa.search.builder.QueryStack.toAndPredicate;
import static ca.uhn.fhir.jpa.search.builder.QueryStack.toEqualToOrInPredicate;

public class ResourceTablePredicateBuilder extends BaseJoiningPredicateBuilder {
	private final DbColumn myColumnResId;
	private final DbColumn myColumnResDeletedAt;
	private final DbColumn myColumnResType;
	private final DbColumn myColumnLastUpdated;
	private final DbColumn myColumnLanguage;

	/**
	 * Constructor
	 */
	public ResourceTablePredicateBuilder(SearchSqlBuilder theSearchSqlBuilder) {
		super(theSearchSqlBuilder, theSearchSqlBuilder.addTable("HFJ_RESOURCE"));
		myColumnResId = getTable().addColumn("RES_ID");
		myColumnResType = getTable().addColumn("RES_TYPE");
		myColumnResDeletedAt = getTable().addColumn("RES_DELETED_AT");
		myColumnLastUpdated = getTable().addColumn("RES_UPDATED");
		myColumnLanguage = getTable().addColumn("RES_LANGUAGE");
	}


	@Override
	public DbColumn getResourceIdColumn() {
		return myColumnResId;
	}

	public Condition createResourceTypeAndNonDeletedPredicates() {
		BinaryCondition typePredicate = null;
		if (getResourceType() != null) {
			typePredicate = BinaryCondition.equalTo(myColumnResType, generatePlaceholder(getResourceType()));
		}
		return toAndPredicate(
			typePredicate,
			UnaryCondition.isNull(myColumnResDeletedAt)
		);
	}

	public DbColumn getLastUpdatedColumn() {
		return myColumnLastUpdated;
	}

	public Condition createLanguagePredicate(Set<String> theValues, boolean theNegated) {
		Condition condition = toEqualToOrInPredicate(myColumnLanguage, generatePlaceholders(theValues));
		if (theNegated) {
			condition = new NotCondition(condition);
		}
		return condition;
	}

	public DbColumn getColumnLastUpdated() {
		return myColumnLastUpdated;
	}
}
