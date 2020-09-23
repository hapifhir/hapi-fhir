package ca.uhn.fhir.jpa.dao.search.sql;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.model.entity.BaseResourceIndexedSearchParam;
import com.healthmarketscience.sqlbuilder.BinaryCondition;
import com.healthmarketscience.sqlbuilder.ComboCondition;
import com.healthmarketscience.sqlbuilder.Condition;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbColumn;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbTable;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseSearchParamIndexTable extends BaseIndexTable {

	private final DbColumn myColumnMissing;
	private final DbColumn myColumnResType;
	private final DbColumn myColumnParamName;
	private final DbColumn myColumnResId;
	private final DbColumn myColumnHashIdentity;

	public BaseSearchParamIndexTable(SearchSqlBuilder theSearchSqlBuilder, DbTable theTable) {
		super(theSearchSqlBuilder, theTable);

		myColumnResId = getTable().addColumn("RES_ID");
		myColumnMissing = theTable.addColumn("SP_MISSING");
		myColumnResType = theTable.addColumn("RES_TYPE");
		myColumnParamName = theTable.addColumn("SP_NAME");
		myColumnHashIdentity = theTable.addColumn("HASH_IDENTITY");
	}

	public DbColumn getColumnHashIdentity() {
		return myColumnHashIdentity;
	}

	public DbColumn getResourceTypeColumn() {
		return myColumnResType;
	}

	public DbColumn getColumnParamName() {
		return myColumnParamName;
	}

	public DbColumn getMissingColumn() {
		return myColumnMissing;
	}

	@Override
	public DbColumn getResourceIdColumn() {
		return myColumnResId;
	}

	public void addPartitionMissing(String theResourceName, String theParamName, boolean theMissing) {
		ComboCondition condition = ComboCondition.and(
			BinaryCondition.equalTo(getResourceTypeColumn(), generatePlaceholder(theResourceName)),
			BinaryCondition.equalTo(getColumnParamName(), generatePlaceholder(theParamName)),
			// FIXME: deal with oracle here
			BinaryCondition.equalTo(getMissingColumn(), generatePlaceholder(theMissing))
		);
		addCondition(condition);
	}

	public Condition combineParamIndexPredicateWithParamNamePredicate(String theResourceName, String theParamName, Condition thePredicate, RequestPartitionId theRequestPartitionId) {
		List<Condition> andPredicates = new ArrayList<>();

		long hashIdentity = BaseResourceIndexedSearchParam.calculateHashIdentity(getPartitionSettings(), getRequestPartitionId(), theResourceName, theParamName);
		String hashIdentityVal = generatePlaceholder(hashIdentity);
		Condition hashIdentityPredicate = BinaryCondition.equalTo(myColumnHashIdentity, hashIdentityVal);
		andPredicates.add(hashIdentityPredicate);
		andPredicates.add(thePredicate);

		return ComboCondition.and(andPredicates.toArray(new Condition[0]));
	}
}
