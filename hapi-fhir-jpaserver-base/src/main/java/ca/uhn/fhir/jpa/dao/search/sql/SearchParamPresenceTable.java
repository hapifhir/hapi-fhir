package ca.uhn.fhir.jpa.dao.search.sql;

import ca.uhn.fhir.jpa.model.entity.SearchParamPresent;
import com.healthmarketscience.sqlbuilder.BinaryCondition;
import com.healthmarketscience.sqlbuilder.Condition;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbColumn;

public class SearchParamPresenceTable extends BaseIndexTable {

	private final DbColumn myColumnResId;
	private final DbColumn myColumnHashPresence;

	/**
	 * Constructor
	 */
	public SearchParamPresenceTable(SearchSqlBuilder theSearchSqlBuilder) {
		super(theSearchSqlBuilder, theSearchSqlBuilder.addTable("HFJ_RES_PARAM_PRESENT"));

		myColumnResId = getTable().addColumn("RES_ID");
		myColumnHashPresence = getTable().addColumn("HASH_PRESENCE");
	}

	@Override
	public DbColumn getResourceIdColumn() {
		return myColumnResId;
	}

	public void addPredicatePresence(String theParamName, boolean thePresence) {
		long hash = SearchParamPresent.calculateHashPresence(getPartitionSettings(), getRequestPartitionId(), getResourceType(), theParamName, thePresence);
		String placeholderValue = generatePlaceholder(hash);
		Condition condition = BinaryCondition.equalTo(myColumnHashPresence, placeholderValue);
		addCondition(condition);
	}


}
