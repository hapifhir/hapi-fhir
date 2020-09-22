package ca.uhn.fhir.jpa.dao.search.sql;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.entity.ModelConfig;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.param.DateParam;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.rest.param.ParamPrefixEnum;
import com.healthmarketscience.sqlbuilder.BinaryCondition;
import com.healthmarketscience.sqlbuilder.ComboCondition;
import com.healthmarketscience.sqlbuilder.Condition;
import com.healthmarketscience.sqlbuilder.SelectQuery;
import com.healthmarketscience.sqlbuilder.dbspec.Join;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbColumn;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbJoin;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbSchema;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbSpec;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;

public class SearchSqlBuilder {

	private static final Logger ourLog = LoggerFactory.getLogger(SearchSqlBuilder.class);
	private final String myBindVariableSubstitutionBase = UUID.randomUUID().toString() + "-";
	private final ArrayList<Object> myBindVariableValues = new ArrayList<>();
	private final DbSpec mySpec;
	private final DbSchema mySchema;
	private final SelectQuery mySelect;
	private final PartitionSettings myPartitionSettings;
	private final RequestPartitionId myRequestPartitionId;
	private final String myResourceType;
	private final ModelConfig myModelConfig;
	private final FhirContext myFhirContext;
	private final SqlBuilderFactory mySqlBuilderFactory;
	private BaseIndexTable myCurrentIndexTable;
	private boolean myMatchNothing;
	private ResourceSqlTable myResourceTableRoot;

	/**
	 * Constructor
	 */
	public SearchSqlBuilder(FhirContext theFhirContext, ModelConfig theModelConfig, PartitionSettings thePartitionSettings, RequestPartitionId theRequestPartitionId, String theResourceType, SqlBuilderFactory theSqlBuilderFactory) {
		myFhirContext = theFhirContext;
		myModelConfig = theModelConfig;
		myPartitionSettings = thePartitionSettings;
		myRequestPartitionId = theRequestPartitionId;
		myResourceType = theResourceType;
		mySqlBuilderFactory = theSqlBuilderFactory;

		mySpec = new DbSpec();
		mySchema = mySpec.addDefaultSchema();
		mySelect = new SelectQuery();

	}

	public FhirContext getFhirContext() {
		return myFhirContext;
	}

	/**
	 * Add and return a join (or a root query if no root query exists yet) for selecting on s COORDS search parameter
	 */
	public CoordsIndexTable addCoordsSelector() {
		CoordsIndexTable retVal = mySqlBuilderFactory.coordsIndexTable(this);
		addTable(retVal);
		return retVal;
	}

	/**
	 * Add and return a join (or a root query if no root query exists yet) for selecting on s DATE search parameter
	 */
	public DateIndexTable addDateSelector() {
		DateIndexTable retVal = mySqlBuilderFactory.dateIndexTable(this);
		addTable(retVal);
		return retVal;
	}

	/**
	 * Add and return a join (or a root query if no root query exists yet) for selecting on s NUMBER search parameter
	 */
	public NumberIndexTable addNumberSelector() {
		NumberIndexTable retVal = mySqlBuilderFactory.numberIndexTable(this);
		addTable(retVal);
		return retVal;
	}


	/**
	 * Add and return a join (or a root query if no root query exists yet) for selecting on s QUANTITY search parameter
	 */
	public QuantityIndexTable addQuantity() {
		QuantityIndexTable retVal = mySqlBuilderFactory.quantityIndexTable(this);
		addTable(retVal);
		return retVal;
	}


	/**
	 * Add and return a join (or a root query if no root query exists yet) for selecting on s REFERENCE search parameter
	 */
	public ReferenceIndexTable addReferenceSelector() {
		ReferenceIndexTable retVal = mySqlBuilderFactory.referenceIndexTable(this);
		addTable(retVal);
		return retVal;
	}

	/**
	 * Add and return a join (or a root query if no root query exists yet) for selecting on s STRING search parameter
	 */
	public StringIndexTable addStringSelector() {
		StringIndexTable retVal = mySqlBuilderFactory.stringIndexTable(this);
		addTable(retVal);
		return retVal;
	}

	/**
	 * Add and return a join (or a root query if no root query exists yet) for selecting on s TOKEN search parameter
	 */
	public TokenIndexTable addTokenSelector() {
		TokenIndexTable retVal = mySqlBuilderFactory.tokenIndexTable(this);
		addTable(retVal);
		return retVal;
	}

	/**
	 * Add and return a join (or a root query if no root query exists yet) for selecting on a <code>:missing</code> search parameter
	 */
	public SearchParamPresenceTable addSearchParamPresenceSelector() {
		SearchParamPresenceTable retVal = mySqlBuilderFactory.searchParamPresenceTable(this);
		addTable(retVal);
		return retVal;
	}

	/**
	 * Add and return a join (or a root query if no root query exists yet) for selecting on s URI search parameter
	 */
	public UriIndexTable addUriSelector() {
		UriIndexTable retVal = mySqlBuilderFactory.uriIndexTable(this);
		addTable(retVal);
		return retVal;
	}


	/**
	 * Add and return a join (or a root query if no root query exists yet) for an arbitrary table
	 */
	private void addTable(BaseIndexTable theIndexTable) {
		if (myCurrentIndexTable == null) {
			mySelect.addColumns(theIndexTable.getResourceIdColumn());
			mySelect.addFromTable(theIndexTable.getTable());
		} else {
			Join join = new DbJoin(mySpec, myCurrentIndexTable.getTable(), theIndexTable.getTable(), new DbColumn[]{myCurrentIndexTable.getResourceIdColumn()}, new DbColumn[]{theIndexTable.getResourceIdColumn()});
			mySelect.addJoins(SelectQuery.JoinType.LEFT_OUTER, join);
		}
		myCurrentIndexTable = theIndexTable;
	}

	/**
	 * Generate and return the SQL generated by this builder
	 */
	public GeneratedSql generate() {

		getOrCreateQueryRootTable();

		mySelect.validate();
		String sql = mySelect.toString();

		List<Object> bindVariables = new ArrayList<>();
		while (true) {

			int idx = sql.indexOf(myBindVariableSubstitutionBase);
			if (idx == -1) {
				break;
			}

			int endIdx = sql.indexOf("'", idx + myBindVariableSubstitutionBase.length());
			String substitutionIndexString = sql.substring(idx + myBindVariableSubstitutionBase.length(), endIdx);
			int substitutionIndex = Integer.parseInt(substitutionIndexString);
			bindVariables.add(myBindVariableValues.get(substitutionIndex));

			sql = sql.substring(0, idx - 1) + "?" + sql.substring(endIdx + 1);
		}

		// FIXME: needed?
		StringBuilder b = new StringBuilder(sql);
		for (int i = 0; ; i++) {
			int idx = b.indexOf("?");
			if (idx == -1) {
				break;
			}
			b.replace(idx, idx + 1, bindVariables.get(i).toString());
		}
		ourLog.info("SQL: {}", b);

		return new GeneratedSql(myMatchNothing, sql, bindVariables);
	}

	/**
	 * If a query root already exists, return it. If none has been selected, create a root on HFJ_RESOURCE
	 */
	public BaseIndexTable getOrCreateQueryRootTable() {
		if (myCurrentIndexTable == null) {
			getOrCreateResourceTableRoot();
		}
		return myCurrentIndexTable;
	}

	private ResourceSqlTable getOrCreateResourceTableRoot() {
		if (myResourceTableRoot == null) {
			ResourceSqlTable resourceTable = mySqlBuilderFactory.resourceTable(this);
			resourceTable.addResourceTypeAndNonDeletedPredicates();
			addTable(resourceTable);
			myResourceTableRoot = resourceTable;
		}
		return myResourceTableRoot;
	}

	/**
	 * The SQL Builder library has one annoying limitation, which is that it does not use/understand bind variables
	 * for its generated SQL. So we work around this by replacing our contents with a string in the SQL consisting
	 * of <code>[random UUID]-[value index]</code> and then
	 */
	String generatePlaceholder(Object theValue) {
		String placeholder = myBindVariableSubstitutionBase + myBindVariableValues.size();
		myBindVariableValues.add(theValue);
		return placeholder;
	}

	List<String> generatePlaceholders(List<?> theValues) {
		return theValues
			.stream()
			.map(t -> generatePlaceholder(t))
			.collect(Collectors.toList());
	}


	public void setMatchNothing() {
		myMatchNothing = true;
	}

	void addCondition(Condition theCondition) {
		assert theCondition != null;
		mySelect.addCondition(theCondition);
	}

	DbTable addTable(String theTableName) {
		return mySchema.addTable(theTableName);
	}

	PartitionSettings getPartitionSettings() {
		return myPartitionSettings;
	}

	RequestPartitionId getRequestPartitionId() {
		return myRequestPartitionId;
	}

	String getResourceType() {
		return myResourceType;
	}

	ModelConfig getModelConfig() {
		return myModelConfig;
	}

	public void addPredicate(Condition theCondition) {
		mySelect.addCondition(theCondition);
	}

	public ComboCondition addPredicateLastUpdated(DateRangeParam theDateRange) {
		ResourceSqlTable resourceTableRoot = getOrCreateResourceTableRoot();

		List<Condition> conditions = new ArrayList<>(2);
		if (theDateRange.getLowerBoundAsInstant() != null) {
			BinaryCondition condition = createConditionForValueWithComparator(ParamPrefixEnum.GREATERTHAN_OR_EQUALS, resourceTableRoot.getLastUpdatedColumn(), theDateRange.getLowerBoundAsInstant());
			conditions.add(condition);
		}

		if (theDateRange.getUpperBoundAsInstant() != null) {
			BinaryCondition condition = createConditionForValueWithComparator(ParamPrefixEnum.LESSTHAN_OR_EQUALS, resourceTableRoot.getLastUpdatedColumn(), theDateRange.getUpperBoundAsInstant());
			conditions.add(condition);
		}

		return ComboCondition.and(conditions.toArray(new Condition[0]));
	}


	public BinaryCondition createConditionForValueWithComparator(ParamPrefixEnum theComparator, DbColumn theColumn, Object theValue) {
		switch (theComparator) {
			case LESSTHAN:
				return BinaryCondition.lessThan(theColumn, generatePlaceholder(theValue));
			case LESSTHAN_OR_EQUALS:
				return BinaryCondition.lessThanOrEq(theColumn, generatePlaceholder(theValue));
			case GREATERTHAN:
				return BinaryCondition.greaterThan(theColumn, generatePlaceholder(theValue));
			case GREATERTHAN_OR_EQUALS:
				return BinaryCondition.greaterThanOrEq(theColumn, generatePlaceholder(theValue));
			default:
				throw new IllegalArgumentException();
		}
	}


	/**
	 * Represents the SQL generated by this query
	 */
	public static class GeneratedSql {
		private final String mySql;
		private final List<Object> myBindVariables;
		private final boolean myMatchNothing;

		public GeneratedSql(boolean theMatchNothing, String theSql, List<Object> theBindVariables) {
			myMatchNothing = theMatchNothing;
			mySql = theSql;
			myBindVariables = theBindVariables;
		}

		public boolean isMatchNothing() {
			return myMatchNothing;
		}

		public List<Object> getBindVariables() {
			return myBindVariables;
		}

		public String getSql() {
			return mySql;
		}
	}
}
