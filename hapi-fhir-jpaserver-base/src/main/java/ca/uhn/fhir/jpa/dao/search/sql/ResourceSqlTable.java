package ca.uhn.fhir.jpa.dao.search.sql;

import com.healthmarketscience.sqlbuilder.BinaryCondition;
import com.healthmarketscience.sqlbuilder.Condition;
import com.healthmarketscience.sqlbuilder.InCondition;
import com.healthmarketscience.sqlbuilder.NotCondition;
import com.healthmarketscience.sqlbuilder.UnaryCondition;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbColumn;

import java.util.Set;

import static ca.uhn.fhir.jpa.dao.search.querystack.QueryStack3.toAndPredicate;
import static ca.uhn.fhir.jpa.dao.search.querystack.QueryStack3.toEqualToOrInPredicate;

public class ResourceSqlTable extends BaseIndexTable {
	private final DbColumn myColumnResId;
	private final DbColumn myColumnResDeletedAt;
	private final DbColumn myColumnResType;
	private final DbColumn myColumnLastUpdated;
	private final DbColumn myColumnLanguage;

	/**
	 * Constructor
	 */
	public ResourceSqlTable(SearchSqlBuilder theSearchSqlBuilder) {
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
		return toAndPredicate(
			BinaryCondition.equalTo(myColumnResType, generatePlaceholder(getResourceType())),
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
}
