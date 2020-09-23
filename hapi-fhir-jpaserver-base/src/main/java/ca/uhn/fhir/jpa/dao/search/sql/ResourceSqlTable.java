package ca.uhn.fhir.jpa.dao.search.sql;

import com.healthmarketscience.sqlbuilder.BinaryCondition;
import com.healthmarketscience.sqlbuilder.UnaryCondition;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbColumn;

public class ResourceSqlTable extends BaseIndexTable {
	private final DbColumn myColumnResId;
	private final DbColumn myColumnResDeletedAt;
	private final DbColumn myColumnResType;
	private final DbColumn myColumnLastUpdated;

	/**
	 * Constructor
	 */
	public ResourceSqlTable(SearchSqlBuilder theSearchSqlBuilder) {
		super(theSearchSqlBuilder, theSearchSqlBuilder.addTable("HFJ_RESOURCE"));
		myColumnResId = getTable().addColumn("RES_ID");
		myColumnResType = getTable().addColumn("RES_TYPE");
		myColumnResDeletedAt = getTable().addColumn("RES_DELETED_AT");
		myColumnLastUpdated = getTable().addColumn("RES_UPDATED");
	}


	@Override
	public DbColumn getResourceIdColumn() {
		return myColumnResId;
	}

	public void addResourceTypeAndNonDeletedPredicates() {
		addCondition(BinaryCondition.equalTo(myColumnResType, generatePlaceholder(getResourceType())));
		addCondition(UnaryCondition.isNull(myColumnResDeletedAt));
	}

	public DbColumn getLastUpdatedColumn() {
		return myColumnLastUpdated;
	}
}
