package ca.uhn.fhir.jpa.dao.search.sql;

import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamToken;
import ca.uhn.fhir.util.VersionIndependentConcept;
import com.healthmarketscience.sqlbuilder.BinaryCondition;
import com.healthmarketscience.sqlbuilder.ComboCondition;
import com.healthmarketscience.sqlbuilder.Condition;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbColumn;

import java.util.List;

public class TokenIndexTable extends BaseSearchParamIndexTable {

	private final DbColumn myColumnResId;
	private final DbColumn myColumnHashSystemAndValue;
	private final DbColumn myColumnHashSystem;
	private final DbColumn myColumnHashValue;

	/**
	 * Constructor
	 */
	public TokenIndexTable(SearchSqlBuilder theSearchSqlBuilder) {
		super(theSearchSqlBuilder, theSearchSqlBuilder.addTable("HFJ_SPIDX_TOKEN"));
		myColumnResId = getTable().addColumn("RES_ID");
		myColumnHashSystem = getTable().addColumn("HASH_SYS");
		myColumnHashSystemAndValue = getTable().addColumn("HASH_SYS_AND_VALUE");
		myColumnHashValue = getTable().addColumn("HASH_VALUE");
	}

	@Override
	public DbColumn getResourceIdColumn() {
		return myColumnResId;
	}

	public void addPredicateSystemAndValue(String theParamName, String theSystem, String theValue) {
		long hash = ResourceIndexedSearchParamToken.calculateHashSystemAndValue(getPartitionSettings(), getRequestPartitionId(), getResourceType(), theParamName, theSystem, theValue);
		String placeholderValue = generatePlaceholder(hash);
		Condition condition = BinaryCondition.equalTo(myColumnHashSystemAndValue, placeholderValue);
		addCondition(condition);
	}


	public ComboCondition createPredicateOrList(String theSearchParamName, List<VersionIndependentConcept> theCodes) {
		Condition[] conditions = new Condition[theCodes.size()];
		for (int i = 0; i < conditions.length; i++) {

			VersionIndependentConcept nextToken = theCodes.get(i);
			long hash;
			DbColumn column;
			if (nextToken.getSystem() == null) {
				hash = ResourceIndexedSearchParamToken.calculateHashValue(getPartitionSettings(), getRequestPartitionId(), getResourceType(), theSearchParamName, nextToken.getCode());
				column = myColumnHashValue;
			} else if (nextToken.getCode() == null) {
				hash = ResourceIndexedSearchParamToken.calculateHashSystem(getPartitionSettings(), getRequestPartitionId(), getResourceType(), theSearchParamName, nextToken.getSystem());
				column = myColumnHashSystem;
			} else {
				hash = ResourceIndexedSearchParamToken.calculateHashSystemAndValue(getPartitionSettings(), getRequestPartitionId(), getResourceType(), theSearchParamName, nextToken.getSystem(), nextToken.getCode());
				column = myColumnHashSystemAndValue;
			}

			String valuePlaceholder = generatePlaceholder(hash);
			conditions[i] = BinaryCondition.equalTo(column, valuePlaceholder);
		}

		return ComboCondition.or(conditions);
	}
}
