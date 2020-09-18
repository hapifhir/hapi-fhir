package ca.uhn.fhir.jpa.dao.search.sql;

import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamString;
import com.healthmarketscience.sqlbuilder.BinaryCondition;
import com.healthmarketscience.sqlbuilder.ComboCondition;
import com.healthmarketscience.sqlbuilder.Condition;
import com.healthmarketscience.sqlbuilder.NotCondition;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbColumn;

import javax.annotation.Nonnull;

public class StringIndexTable extends BaseSearchParamIndexTable {

	private final DbColumn myColumnResId;
	private final DbColumn myColumnValueExact;
	private final DbColumn myColumnValueNormalized;
	private final DbColumn myColumnHashNormPrefix;
	private final DbColumn myColumnHashIdentity;
	private final DbColumn myColumnHashExact;

	/**
	 * Constructor
	 */
	public StringIndexTable(SearchSqlBuilder theSearchSqlBuilder) {
		super(theSearchSqlBuilder, theSearchSqlBuilder.addTable("HFJ_SPIDX_STRING"));
		myColumnResId = getTable().addColumn("RES_ID");
		myColumnValueExact = getTable().addColumn("SP_VALUE_EXACT");
		myColumnValueNormalized = getTable().addColumn("SP_VALUE_NORMALIZED");
		myColumnHashNormPrefix = getTable().addColumn("HASH_NORM_PREFIX");
		myColumnHashIdentity = getTable().addColumn("HASH_IDENTITY");
		myColumnHashExact = getTable().addColumn("HASH_EXACT");
	}

	@Override
	public DbColumn getResourceIdColumn() {
		return myColumnResId;
	}

	public void addPredicateExact(String theParamName, String theValueExact) {
		addCondition(createPredicateExact(theParamName, theValueExact));
	}

	@Nonnull
	public Condition createPredicateExact(String theTheParamName, String theTheValueExact) {
		long hash = ResourceIndexedSearchParamString.calculateHashExact(getPartitionSettings(), getRequestPartitionId(), getResourceType(), theTheParamName, theTheValueExact);
		String placeholderValue = generatePlaceholder(hash);
		return BinaryCondition.equalTo(myColumnHashExact, placeholderValue);
	}

	@Nonnull
	public Condition createPredicateNormalLike(String theParamName, String theNormalizedString, String theLikeExpression) {
		Long hash = ResourceIndexedSearchParamString.calculateHashNormalized(getPartitionSettings(), getRequestPartitionId(), getModelConfig(), getResourceType(), theParamName, theNormalizedString);
		Condition hashPredicate = BinaryCondition.equalTo(myColumnHashNormPrefix, generatePlaceholder(hash));
		Condition valuePredicate = BinaryCondition.like(myColumnValueNormalized, generatePlaceholder(theLikeExpression));
		return ComboCondition.and(hashPredicate, valuePredicate);
	}

	@Nonnull
	public Condition createPredicateNormal(String theParamName, String theNormalizedString) {
		Long hash = ResourceIndexedSearchParamString.calculateHashNormalized(getPartitionSettings(), getRequestPartitionId(), getModelConfig(), getResourceType(), theParamName, theNormalizedString);
		Condition hashPredicate = BinaryCondition.equalTo(myColumnHashNormPrefix, generatePlaceholder(hash));
		Condition valuePredicate = BinaryCondition.equalTo(myColumnValueNormalized, generatePlaceholder(theNormalizedString));
		return ComboCondition.and(hashPredicate, valuePredicate);
	}

	@Nonnull
	public Condition createPredicateLikeExpressionOnly(String theParamName, String theLikeExpression, boolean theInverse) {
		long hashIdentity = ResourceIndexedSearchParamString.calculateHashIdentity(getPartitionSettings(), getRequestPartitionId(), getResourceType(), theParamName);
		BinaryCondition identityPredicate = BinaryCondition.equalTo(myColumnHashIdentity, generatePlaceholder(hashIdentity));
		BinaryCondition likePredicate = BinaryCondition.like(myColumnValueNormalized, generatePlaceholder(theLikeExpression));
		Condition retVal = ComboCondition.and(identityPredicate, likePredicate);
		if (theInverse) {
			retVal = new NotCondition(retVal);
		}
		return retVal;
	}
}
