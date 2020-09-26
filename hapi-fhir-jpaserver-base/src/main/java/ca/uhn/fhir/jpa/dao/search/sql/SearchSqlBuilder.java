package ca.uhn.fhir.jpa.dao.search.sql;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.dao.search.querystack.QueryStack3;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.entity.ModelConfig;
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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class SearchSqlBuilder {

	private static final Logger ourLog = LoggerFactory.getLogger(SearchSqlBuilder.class);
	private final String myBindVariableSubstitutionBase;
	private final ArrayList<Object> myBindVariableValues;
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
	private boolean myHaveAtLeastOnePredicate;

	/**
	 * Constructor
	 */
	public SearchSqlBuilder(FhirContext theFhirContext, ModelConfig theModelConfig, PartitionSettings thePartitionSettings, RequestPartitionId theRequestPartitionId, String theResourceType, SqlBuilderFactory theSqlBuilderFactory) {
		this(theFhirContext, theModelConfig, thePartitionSettings, theRequestPartitionId, theResourceType, theSqlBuilderFactory, UUID.randomUUID().toString() + "-", new ArrayList<>());
	}

	/**
	 * Constructor for child SQL Builders
	 */
	private SearchSqlBuilder(FhirContext theFhirContext, ModelConfig theModelConfig, PartitionSettings thePartitionSettings, RequestPartitionId theRequestPartitionId, String theResourceType, SqlBuilderFactory theSqlBuilderFactory, String theBindVariableSubstitutionBase, ArrayList<Object> theBindVariableValues) {
		myFhirContext = theFhirContext;
		myModelConfig = theModelConfig;
		myPartitionSettings = thePartitionSettings;
		myRequestPartitionId = theRequestPartitionId;
		myResourceType = theResourceType;
		mySqlBuilderFactory = theSqlBuilderFactory;

		mySpec = new DbSpec();
		mySchema = mySpec.addDefaultSchema();
		mySelect = new SelectQuery();

		myBindVariableSubstitutionBase = theBindVariableSubstitutionBase;
		myBindVariableValues = theBindVariableValues;
	}

	public FhirContext getFhirContext() {
		return myFhirContext;
	}

	/**
	 * Add and return a predicate builder (or a root query if no root query exists yet) for selecting on a Composite Unique search parameter
	 */
	public CompositeUniqueSearchParameterPredicateBuilder addCompositeUniquePredicateBuilder() {
		CompositeUniqueSearchParameterPredicateBuilder retVal = mySqlBuilderFactory.newCompositeUniqueSearchParameterPredicateBuilder(this);
		addTable(retVal, null);
		return retVal;
	}


	/**
	 * Add and return a predicate builder (or a root query if no root query exists yet) for selecting on a COORDS search parameter
	 */
	public CoordsIndexTable addCoordsSelector(@Nullable DbColumn theSourceJoinColumn) {
		CoordsIndexTable retVal = mySqlBuilderFactory.coordsIndexTable(this);
		addTable(retVal, theSourceJoinColumn);
		return retVal;
	}

	/**
	 * Add and return a predicate builder (or a root query if no root query exists yet) for selecting on a DATE search parameter
	 */
	public DateIndexTable addDateSelector(@Nullable DbColumn theSourceJoinColumn) {
		DateIndexTable retVal = mySqlBuilderFactory.dateIndexTable(this);
		addTable(retVal, theSourceJoinColumn);
		return retVal;
	}

	/**
	 * Add and return a predicate builder (or a root query if no root query exists yet) for selecting on a NUMBER search parameter
	 */
	public NumberIndexTable addNumberSelector(@Nullable DbColumn theSourceJoinColumn) {
		NumberIndexTable retVal = mySqlBuilderFactory.numberIndexTable(this);
		addTable(retVal, theSourceJoinColumn);
		return retVal;
	}


	/**
	 * Add and return a predicate builder (or a root query if no root query exists yet) for selecting on a QUANTITY search parameter
	 */
	public QuantityIndexTable addQuantity(@Nullable DbColumn theSourceJoinColumn) {
		QuantityIndexTable retVal = mySqlBuilderFactory.quantityIndexTable(this);
		addTable(retVal, theSourceJoinColumn);
		return retVal;
	}

	/**
	 * Add and return a predicate builder (or a root query if no root query exists yet) for selecting on a <code>_source</code> search parameter
	 */
	public SourcePredicateBuilder addSourcePredicateBuilder(@Nullable DbColumn theSourceJoinColumn) {
		SourcePredicateBuilder retVal = mySqlBuilderFactory.newSourcePredicateBuilder(this);
		addTable(retVal, theSourceJoinColumn);
		return retVal;
	}


	// FIXME: remove
	public ResourceLinkIndexTable addEverythingSelector(QueryStack3 theQueryStack, String theResourceName, Long theTargetPid) {
		assert myCurrentIndexTable == null;

//		if (theTargetPid != null) {
		return addReferenceSelector(theQueryStack, null);
//		} else {
//			getOrCreateResourceTableRoot();
//
//			ResourceLinkIndexTable retVal = mySqlBuilderFactory.referenceIndexTable(theQueryStack, this);
//			DbTable fromTable = myCurrentIndexTable.getTable();
//			DbTable toTable = retVal.getTable();
//			DbColumn fromColumn = myCurrentIndexTable.getResourceIdColumn();
//			DbColumn toColumn = retVal.getColumnTargetResourceId();
//			Join join = new DbJoin(mySpec, fromTable, toTable, new DbColumn[]{fromColumn}, new DbColumn[]{toColumn});
//			mySelect.addJoins(SelectQuery.JoinType.LEFT_OUTER, join);
//			mySelect.addColumns(retVal.getColumnSrcResourceId());
//			return retVal;
//		}
	}


	/**
	 * Add and return a predicate builder (or a root query if no root query exists yet) for selecting on a REFERENCE search parameter
	 */
	public ResourceLinkIndexTable addReferenceSelector(QueryStack3 theQueryStack, @Nullable DbColumn theSourceJoinColumn) {
		ResourceLinkIndexTable retVal = mySqlBuilderFactory.referenceIndexTable(theQueryStack, this, false);
		addTable(retVal, theSourceJoinColumn);
		return retVal;
	}

	/**
	 * Add and return a predicate builder (or a root query if no root query exists yet) for selecting on a reosource link where the
	 * source and target are reversed. This is used for _has queries.
	 */
	public ResourceLinkIndexTable addReferenceSelectorReversed(QueryStack3 theQueryStack, DbColumn theSourceJoinColumn) {
		ResourceLinkIndexTable retVal = mySqlBuilderFactory.referenceIndexTable(theQueryStack, this, true);
		addTable(retVal, theSourceJoinColumn);
		return retVal;
	}

	/**
	 * Add and return a predicate builder (or a root query if no root query exists yet) for selecting on a STRING search parameter
	 */
	public StringIndexTable addStringSelector(@Nullable DbColumn theSourceJoinColumn) {
		StringIndexTable retVal = mySqlBuilderFactory.stringIndexTable(this);
		addTable(retVal, theSourceJoinColumn);
		return retVal;
	}

	/**
	 * Add and return a predicate builder (or a root query if no root query exists yet) for selecting on a <code>_tag</code> search parameter
	 */
	public TagPredicateBuilder addTagSelector(@Nullable DbColumn theSourceJoinColumn) {
		TagPredicateBuilder retVal = mySqlBuilderFactory.newTagPredicateBuilder(this);
		addTable(retVal, theSourceJoinColumn);
		return retVal;
	}

	/**
	 * Add and return a predicate builder (or a root query if no root query exists yet) for selecting on a TOKEN search parameter
	 */
	public TokenIndexTable addTokenSelector(@Nullable DbColumn theSourceJoinColumn) {
		TokenIndexTable retVal = mySqlBuilderFactory.tokenIndexTable(this);
		addTable(retVal, theSourceJoinColumn);
		return retVal;
	}

	/**
	 * Add and return a predicate builder (or a root query if no root query exists yet) for selecting on a <code>:missing</code> search parameter
	 */
	public SearchParamPresentPredicateBuilder addSearchParamPresentPredicateBuilder(@Nullable DbColumn theSourceJoinColumn) {
		SearchParamPresentPredicateBuilder retVal = mySqlBuilderFactory.searchParamPresentPredicateBuilder(this);
		addTable(retVal, theSourceJoinColumn);
		return retVal;
	}

	/**
	 * Add and return a predicate builder (or a root query if no root query exists yet) for selecting on a URI search parameter
	 */
	public UriIndexTable addUriSelector(@Nullable DbColumn theSourceJoinColumn) {
		UriIndexTable retVal = mySqlBuilderFactory.uriIndexTable(this);
		addTable(retVal, theSourceJoinColumn);
		return retVal;
	}


	public ResourceIdPredicateBuilder3 newResourceIdBuilder() {
		return mySqlBuilderFactory.resourceId(this);
	}


	/**
	 * Add and return a predicate builder (or a root query if no root query exists yet) for an arbitrary table
	 */
	private void addTable(BaseIndexTable theIndexTable, @Nullable DbColumn theSourceJoinColumn) {
		if (theSourceJoinColumn != null) {
			DbTable fromTable = theSourceJoinColumn.getTable();
			DbTable toTable = theIndexTable.getTable();
			DbColumn toColumn = theIndexTable.getResourceIdColumn();
			addJoin(fromTable, toTable, theSourceJoinColumn, toColumn);
		} else {
			if (myCurrentIndexTable == null) {
				mySelect.addColumns(theIndexTable.getResourceIdColumn());
				mySelect.addFromTable(theIndexTable.getTable());
			} else {
				DbTable fromTable = myCurrentIndexTable.getTable();
				DbTable toTable = theIndexTable.getTable();
				DbColumn fromColumn = myCurrentIndexTable.getResourceIdColumn();
				DbColumn toColumn = theIndexTable.getResourceIdColumn();
				addJoin(fromTable, toTable, fromColumn, toColumn);
			}
			myCurrentIndexTable = theIndexTable;
		}
	}

	public void addJoin(DbTable theFromTable, DbTable theToTable, DbColumn theFromColumn, DbColumn theToColumn) {
		Join join = new DbJoin(mySpec, theFromTable, theToTable, new DbColumn[]{theFromColumn}, new DbColumn[]{theToColumn});
		mySelect.addJoins(SelectQuery.JoinType.LEFT_OUTER, join);
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
		for (int i = 0, startIdx = 0; ; i++) {
			int idx = b.indexOf("?", startIdx);
			if (idx == -1) {
				break;
			}
			String nextValue = bindVariables.get(i).toString();
			b.replace(idx, idx + 1, "'" + nextValue + "'");
			startIdx = idx + nextValue.length();
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

	public ResourceSqlTable getOrCreateResourceTableRoot() {
		if (myResourceTableRoot == null) {
			ResourceSqlTable resourceTable = mySqlBuilderFactory.resourceTable(this);
			addTable(resourceTable, null);
			Condition typeAndDeletionPredicate = resourceTable.createResourceTypeAndNonDeletedPredicates();
			addPredicate(typeAndDeletionPredicate);
			myResourceTableRoot = resourceTable;
		}
		return myResourceTableRoot;
	}

	/**
	 * The SQL Builder library has one annoying limitation, which is that it does not use/understand bind variables
	 * for its generated SQL. So we work around this by replacing our contents with a string in the SQL consisting
	 * of <code>[random UUID]-[value index]</code> and then
	 */
	public String generatePlaceholder(Object theValue) {
		String placeholder = myBindVariableSubstitutionBase + myBindVariableValues.size();
		myBindVariableValues.add(theValue);
		return placeholder;
	}

	public List<String> generatePlaceholders(Collection<?> theValues) {
		return theValues
			.stream()
			.map(t -> generatePlaceholder(t))
			.collect(Collectors.toList());
	}


	public void setMatchNothing() {
		myMatchNothing = true;
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

	public void addPredicate(@Nonnull Condition theCondition) {
		assert theCondition != null;
		mySelect.addCondition(theCondition);
		myHaveAtLeastOnePredicate = true;
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

	public SearchSqlBuilder newChildSqlBuilder() {
		return new SearchSqlBuilder(myFhirContext, myModelConfig, myPartitionSettings, myRequestPartitionId, myResourceType, mySqlBuilderFactory, myBindVariableSubstitutionBase, myBindVariableValues);
	}

	public SelectQuery getSelect() {
		return mySelect;
	}

	public boolean haveAtLeastOnePredicate() {
		return myHaveAtLeastOnePredicate;
	}


	/**
	 * Represents the SQL generated by this query
	 */
	public static class GeneratedSql {
		private final String mySql;
		private final List<Object> myBindVariables;
		private final boolean myMatchNothing;

		public GeneratedSql(boolean theMatchNothing, String theSql, List<Object> theBindVariables) {
			// FIXME: remove or make this only happen in unit tests
			assert Pattern.compile("=['0-9]").matcher(theSql.replace(" ", "")).find() == false : "Non-bound SQL parameter found: " + theSql;
			assert Pattern.compile("in\\(['0-9]").matcher(theSql.toLowerCase().replace(" ", "")).find() == false : "Non-bound SQL parameter found: " + theSql;

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
