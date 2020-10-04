package ca.uhn.fhir.jpa.search.builder.predicate;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.model.entity.BaseResourceIndexedSearchParam;
import ca.uhn.fhir.jpa.search.builder.sql.SearchQueryBuilder;
import com.healthmarketscience.sqlbuilder.BinaryCondition;
import com.healthmarketscience.sqlbuilder.ComboCondition;
import com.healthmarketscience.sqlbuilder.Condition;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbColumn;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbTable;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

import static ca.uhn.fhir.jpa.search.builder.QueryStack.toAndPredicate;

public abstract class BaseSearchParamPredicateBuilder extends BaseJoiningPredicateBuilder {

	private final DbColumn myColumnMissing;
	private final DbColumn myColumnResType;
	private final DbColumn myColumnParamName;
	private final DbColumn myColumnResId;
	private final DbColumn myColumnHashIdentity;

	public BaseSearchParamPredicateBuilder(SearchQueryBuilder theSearchSqlBuilder, DbTable theTable) {
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

	public Condition combineWithHashIdentityPredicate(String theResourceName, String theParamName, Condition thePredicate) {
		List<Condition> andPredicates = new ArrayList<>();

		Condition hashIdentityPredicate = createHashIdentityPredicate(theResourceName, theParamName);
		andPredicates.add(hashIdentityPredicate);
		andPredicates.add(thePredicate);

		return toAndPredicate(andPredicates);
	}

	@Nonnull
	public Condition createHashIdentityPredicate(String theResourceType, String theParamName) {
		long hashIdentity = BaseResourceIndexedSearchParam.calculateHashIdentity(getPartitionSettings(), getRequestPartitionId(), theResourceType, theParamName);
		String hashIdentityVal = generatePlaceholder(hashIdentity);
		return BinaryCondition.equalTo(myColumnHashIdentity, hashIdentityVal);
	}

	public Condition createPredicateParamMissingForNonReference(String theResourceName, String theParamName, Boolean theMissing, RequestPartitionId theRequestPartitionId) {
		ComboCondition condition = ComboCondition.and(
			BinaryCondition.equalTo(getResourceTypeColumn(), generatePlaceholder(theResourceName)),
			BinaryCondition.equalTo(getColumnParamName(), generatePlaceholder(theParamName)),
			BinaryCondition.equalTo(getMissingColumn(), generatePlaceholder(theMissing))
		);
		return combineWithRequestPartitionIdPredicate(theRequestPartitionId, condition);

	}
}
