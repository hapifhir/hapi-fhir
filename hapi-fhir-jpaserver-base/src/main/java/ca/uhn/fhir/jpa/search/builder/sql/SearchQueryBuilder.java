package ca.uhn.fhir.jpa.search.builder.sql;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.config.HibernatePropertiesProvider;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.entity.ModelConfig;
import ca.uhn.fhir.jpa.search.builder.QueryStack;
import ca.uhn.fhir.jpa.search.builder.predicate.BaseJoiningPredicateBuilder;
import ca.uhn.fhir.jpa.search.builder.predicate.ComboNonUniqueSearchParameterPredicateBuilder;
import ca.uhn.fhir.jpa.search.builder.predicate.ComboUniqueSearchParameterPredicateBuilder;
import ca.uhn.fhir.jpa.search.builder.predicate.CoordsPredicateBuilder;
import ca.uhn.fhir.jpa.search.builder.predicate.DatePredicateBuilder;
import ca.uhn.fhir.jpa.search.builder.predicate.ForcedIdPredicateBuilder;
import ca.uhn.fhir.jpa.search.builder.predicate.NumberPredicateBuilder;
import ca.uhn.fhir.jpa.search.builder.predicate.QuantityNormalizedPredicateBuilder;
import ca.uhn.fhir.jpa.search.builder.predicate.QuantityPredicateBuilder;
import ca.uhn.fhir.jpa.search.builder.predicate.ResourceIdPredicateBuilder;
import ca.uhn.fhir.jpa.search.builder.predicate.ResourceLinkPredicateBuilder;
import ca.uhn.fhir.jpa.search.builder.predicate.ResourceTablePredicateBuilder;
import ca.uhn.fhir.jpa.search.builder.predicate.SearchParamPresentPredicateBuilder;
import ca.uhn.fhir.jpa.search.builder.predicate.SourcePredicateBuilder;
import ca.uhn.fhir.jpa.search.builder.predicate.StringPredicateBuilder;
import ca.uhn.fhir.jpa.search.builder.predicate.TagPredicateBuilder;
import ca.uhn.fhir.jpa.search.builder.predicate.TokenPredicateBuilder;
import ca.uhn.fhir.jpa.search.builder.predicate.UriPredicateBuilder;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import ca.uhn.fhir.rest.param.DateParam;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.rest.param.ParamPrefixEnum;
import com.healthmarketscience.sqlbuilder.BinaryCondition;
import com.healthmarketscience.sqlbuilder.ComboCondition;
import com.healthmarketscience.sqlbuilder.Condition;
import com.healthmarketscience.sqlbuilder.FunctionCall;
import com.healthmarketscience.sqlbuilder.InCondition;
import com.healthmarketscience.sqlbuilder.OrderObject;
import com.healthmarketscience.sqlbuilder.SelectQuery;
import com.healthmarketscience.sqlbuilder.dbspec.Join;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbColumn;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbJoin;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbSchema;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbSpec;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbTable;
import org.apache.commons.lang3.Validate;
import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.SQLServerDialect;
import org.hibernate.dialect.pagination.AbstractLimitHandler;
import org.hibernate.engine.spi.RowSelection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static ca.uhn.fhir.rest.param.ParamPrefixEnum.*;
import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;

public class SearchQueryBuilder {

	private static final Logger ourLog = LoggerFactory.getLogger(SearchQueryBuilder.class);
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
	private final SqlObjectFactory mySqlBuilderFactory;
	private final boolean myCountQuery;
	private final Dialect myDialect;
	private boolean myMatchNothing;
	private ResourceTablePredicateBuilder myResourceTableRoot;
	private boolean myHaveAtLeastOnePredicate;
	private BaseJoiningPredicateBuilder myFirstPredicateBuilder;
	private boolean dialectIsMsSql;
	private boolean dialectIsMySql;
	private boolean myNeedResourceTableRoot;

	/**
	 * Constructor
	 */
	public SearchQueryBuilder(FhirContext theFhirContext, ModelConfig theModelConfig, PartitionSettings thePartitionSettings, RequestPartitionId theRequestPartitionId, String theResourceType, SqlObjectFactory theSqlBuilderFactory, HibernatePropertiesProvider theDialectProvider, boolean theCountQuery) {
		this(theFhirContext, theModelConfig, thePartitionSettings, theRequestPartitionId, theResourceType, theSqlBuilderFactory, UUID.randomUUID() + "-", theDialectProvider.getDialect(), theCountQuery, new ArrayList<>());
	}

	/**
	 * Constructor for child SQL Builders
	 */
	private SearchQueryBuilder(FhirContext theFhirContext, ModelConfig theModelConfig, PartitionSettings thePartitionSettings, RequestPartitionId theRequestPartitionId, String theResourceType, SqlObjectFactory theSqlBuilderFactory, String theBindVariableSubstitutionBase, Dialect theDialect, boolean theCountQuery, ArrayList<Object> theBindVariableValues) {
		myFhirContext = theFhirContext;
		myModelConfig = theModelConfig;
		myPartitionSettings = thePartitionSettings;
		myRequestPartitionId = theRequestPartitionId;
		myResourceType = theResourceType;
		mySqlBuilderFactory = theSqlBuilderFactory;
		myCountQuery = theCountQuery;
		myDialect = theDialect;
		if (myDialect instanceof org.hibernate.dialect.MySQLDialect){
			dialectIsMySql = true;
		}
		if (myDialect instanceof org.hibernate.dialect.SQLServerDialect){
			dialectIsMsSql = true;
		}


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
	public ComboUniqueSearchParameterPredicateBuilder addComboUniquePredicateBuilder() {
		ComboUniqueSearchParameterPredicateBuilder retVal = mySqlBuilderFactory.newComboUniqueSearchParameterPredicateBuilder(this);
		addTable(retVal, null);
		return retVal;
	}

	/**
	 * Add and return a predicate builder (or a root query if no root query exists yet) for selecting on a Composite Unique search parameter
	 */
	public ComboNonUniqueSearchParameterPredicateBuilder addComboNonUniquePredicateBuilder() {
		ComboNonUniqueSearchParameterPredicateBuilder retVal = mySqlBuilderFactory.newComboNonUniqueSearchParameterPredicateBuilder(this);
		addTable(retVal, null);
		return retVal;
	}

	/**
	 * Add and return a predicate builder (or a root query if no root query exists yet) for selecting on a COORDS search parameter
	 */
	public CoordsPredicateBuilder addCoordsPredicateBuilder(@Nullable DbColumn theSourceJoinColumn) {
		CoordsPredicateBuilder retVal = mySqlBuilderFactory.coordsPredicateBuilder(this);
		addTable(retVal, theSourceJoinColumn);
		return retVal;
	}

	/**
	 * Add and return a predicate builder (or a root query if no root query exists yet) for selecting on a DATE search parameter
	 */
	public DatePredicateBuilder addDatePredicateBuilder(@Nullable DbColumn theSourceJoinColumn) {
		DatePredicateBuilder retVal = mySqlBuilderFactory.dateIndexTable(this);
		addTable(retVal, theSourceJoinColumn);
		return retVal;
	}

	/**
	 * Add and return a predicate builder for selecting a forced ID. This is only intended for use with sorts so it can not
	 * be the root query.
	 */
	public ForcedIdPredicateBuilder addForcedIdPredicateBuilder(@Nonnull DbColumn theSourceJoinColumn) {
		Validate.isTrue(theSourceJoinColumn != null);

		ForcedIdPredicateBuilder retVal = mySqlBuilderFactory.newForcedIdPredicateBuilder(this);
		addTable(retVal, theSourceJoinColumn);
		return retVal;
	}


	/**
	 * Add and return a predicate builder (or a root query if no root query exists yet) for selecting on a NUMBER search parameter
	 */
	public NumberPredicateBuilder addNumberPredicateBuilder(@Nullable DbColumn theSourceJoinColumn) {
		NumberPredicateBuilder retVal = mySqlBuilderFactory.numberIndexTable(this);
		addTable(retVal, theSourceJoinColumn);
		return retVal;
	}

	/**
	 * Add and return a predicate builder (or a root query if no root query exists yet) for selecting on a QUANTITY search parameter
	 */
	public ResourceTablePredicateBuilder addResourceTablePredicateBuilder(@Nullable DbColumn theSourceJoinColumn) {
		ResourceTablePredicateBuilder retVal = mySqlBuilderFactory.resourceTable(this);
		addTable(retVal, theSourceJoinColumn);
		return retVal;
	}


	/**
	 * Add and return a predicate builder (or a root query if no root query exists yet) for selecting on a QUANTITY search parameter
	 */
	public QuantityPredicateBuilder addQuantityPredicateBuilder(@Nullable DbColumn theSourceJoinColumn) {
		
		QuantityPredicateBuilder retVal = mySqlBuilderFactory.quantityIndexTable(this);
		addTable(retVal, theSourceJoinColumn);
		
		return retVal;
	}

	public QuantityNormalizedPredicateBuilder addQuantityNormalizedPredicateBuilder(@Nullable DbColumn theSourceJoinColumn) {
		
		QuantityNormalizedPredicateBuilder retVal = mySqlBuilderFactory.quantityNormalizedIndexTable(this);
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

	/**
	 * Add and return a predicate builder (or a root query if no root query exists yet) for selecting on a REFERENCE search parameter
	 */
	public ResourceLinkPredicateBuilder addReferencePredicateBuilder(QueryStack theQueryStack, @Nullable DbColumn theSourceJoinColumn) {
		ResourceLinkPredicateBuilder retVal = mySqlBuilderFactory.referenceIndexTable(theQueryStack, this, false);
		addTable(retVal, theSourceJoinColumn);
		return retVal;
	}

	/**
	 * Add and return a predicate builder (or a root query if no root query exists yet) for selecting on a reosource link where the
	 * source and target are reversed. This is used for _has queries.
	 */
	public ResourceLinkPredicateBuilder addReferencePredicateBuilderReversed(QueryStack theQueryStack, DbColumn theSourceJoinColumn) {
		ResourceLinkPredicateBuilder retVal = mySqlBuilderFactory.referenceIndexTable(theQueryStack, this, true);
		addTable(retVal, theSourceJoinColumn);
		return retVal;
	}

	/**
	 * Add and return a predicate builder (or a root query if no root query exists yet) for selecting on a STRING search parameter
	 */
	public StringPredicateBuilder addStringPredicateBuilder(@Nullable DbColumn theSourceJoinColumn) {
		StringPredicateBuilder retVal = mySqlBuilderFactory.stringIndexTable(this);
		addTable(retVal, theSourceJoinColumn);
		return retVal;
	}

	/**
	 * Add and return a predicate builder (or a root query if no root query exists yet) for selecting on a <code>_tag</code> search parameter
	 */
	public TagPredicateBuilder addTagPredicateBuilder(@Nullable DbColumn theSourceJoinColumn) {
		TagPredicateBuilder retVal = mySqlBuilderFactory.newTagPredicateBuilder(this);
		addTable(retVal, theSourceJoinColumn);
		return retVal;
	}

	/**
	 * Add and return a predicate builder (or a root query if no root query exists yet) for selecting on a TOKEN search parameter
	 */
	public TokenPredicateBuilder addTokenPredicateBuilder(@Nullable DbColumn theSourceJoinColumn) {
		TokenPredicateBuilder retVal = mySqlBuilderFactory.tokenIndexTable(this);
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
	public UriPredicateBuilder addUriPredicateBuilder(@Nullable DbColumn theSourceJoinColumn) {
		UriPredicateBuilder retVal = mySqlBuilderFactory.uriIndexTable(this);
		addTable(retVal, theSourceJoinColumn);
		return retVal;
	}


	public ResourceIdPredicateBuilder newResourceIdBuilder() {
		return mySqlBuilderFactory.resourceId(this);
	}


	/**
	 * Add and return a predicate builder (or a root query if no root query exists yet) for an arbitrary table
	 */
	private void addTable(BaseJoiningPredicateBuilder thePredicateBuilder, @Nullable DbColumn theSourceJoinColumn) {
		if (theSourceJoinColumn != null) {
			DbTable fromTable = theSourceJoinColumn.getTable();
			DbTable toTable = thePredicateBuilder.getTable();
			DbColumn toColumn = thePredicateBuilder.getResourceIdColumn();
			addJoin(fromTable, toTable, theSourceJoinColumn, toColumn);
		} else {
			if (myFirstPredicateBuilder == null) {

				BaseJoiningPredicateBuilder root;
				if (!myNeedResourceTableRoot) {
					root = thePredicateBuilder;
				} else {
					if (thePredicateBuilder instanceof ResourceTablePredicateBuilder) {
						root = thePredicateBuilder;
					} else {
						root = mySqlBuilderFactory.resourceTable(this);
					}
				}

				if (myCountQuery) {
					mySelect.addCustomColumns(FunctionCall.count().setIsDistinct(true).addColumnParams(root.getResourceIdColumn()));
				} else {
					mySelect.addColumns(root.getResourceIdColumn());
				}
				mySelect.addFromTable(root.getTable());
				myFirstPredicateBuilder = root;

				if (!myNeedResourceTableRoot || (thePredicateBuilder instanceof ResourceTablePredicateBuilder)) {
					return;
				}
			}

			DbTable fromTable = myFirstPredicateBuilder.getTable();
			DbTable toTable = thePredicateBuilder.getTable();
			DbColumn fromColumn = myFirstPredicateBuilder.getResourceIdColumn();
			DbColumn toColumn = thePredicateBuilder.getResourceIdColumn();
			addJoin(fromTable, toTable, fromColumn, toColumn);

		}
	}

	public void addJoin(DbTable theFromTable, DbTable theToTable, DbColumn theFromColumn, DbColumn theToColumn) {
		Join join = new DbJoin(mySpec, theFromTable, theToTable, new DbColumn[]{theFromColumn}, new DbColumn[]{theToColumn});
		mySelect.addJoins(SelectQuery.JoinType.LEFT_OUTER, join);
	}

	/**
	 * Generate and return the SQL generated by this builder
	 */
	public GeneratedSql generate(@Nullable Integer theOffset, @Nullable Integer theMaxResultsToFetch) {

		getOrCreateFirstPredicateBuilder();

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

		Integer maxResultsToFetch = theMaxResultsToFetch;
		Integer offset = theOffset;
		if (offset != null && offset == 0) {
			offset = null;
		}
		if (maxResultsToFetch != null || offset != null) {

			maxResultsToFetch = defaultIfNull(maxResultsToFetch, 10000);
			
			AbstractLimitHandler limitHandler = (AbstractLimitHandler) myDialect.getLimitHandler();
			RowSelection selection = new RowSelection();
			selection.setFirstRow(offset);
			selection.setMaxRows(maxResultsToFetch);
			sql = limitHandler.processSql(sql, selection);

			int startOfQueryParameterIndex = 0;

			boolean isSqlServer = (myDialect instanceof SQLServerDialect);
			if (isSqlServer) {

				// The SQLServerDialect has a bunch of one-off processing to deal with rules on when
				// a limit can be used, so we can't rely on the flags that the limithandler exposes since
				// the exact structure of the query depends on the parameters
				if (sql.contains("top(?)")) {
					bindVariables.add(0, maxResultsToFetch);
				}
				if (sql.contains("offset 0 rows fetch next ? rows only")) {
					bindVariables.add(maxResultsToFetch);
				}
				if (sql.contains("offset ? rows fetch next ? rows only")) {
					bindVariables.add(theOffset);
					bindVariables.add(maxResultsToFetch);
				}
				if (offset != null && sql.contains("__row__")) {
					bindVariables.add(theOffset + 1);
					bindVariables.add(theOffset + maxResultsToFetch + 1);
				}

			} else if (limitHandler.supportsVariableLimit()) {

				boolean bindLimitParametersFirst = limitHandler.bindLimitParametersFirst();
				if (limitHandler.useMaxForLimit() && offset != null) {
					maxResultsToFetch = maxResultsToFetch + offset;
				}

				if (limitHandler.bindLimitParametersInReverseOrder()) {
					startOfQueryParameterIndex = bindCountParameter(bindVariables, maxResultsToFetch, limitHandler, startOfQueryParameterIndex, bindLimitParametersFirst);
					bindOffsetParameter(bindVariables, offset, limitHandler, startOfQueryParameterIndex, bindLimitParametersFirst);
				} else {
					startOfQueryParameterIndex = bindOffsetParameter(bindVariables, offset, limitHandler, startOfQueryParameterIndex, bindLimitParametersFirst);
					bindCountParameter(bindVariables, maxResultsToFetch, limitHandler, startOfQueryParameterIndex, bindLimitParametersFirst);
				}
			}
		}

		return new GeneratedSql(myMatchNothing, sql, bindVariables);
	}

	private int bindCountParameter(List<Object> bindVariables, Integer maxResultsToFetch, AbstractLimitHandler limitHandler, int startOfQueryParameterIndex, boolean bindLimitParametersFirst) {
		if (limitHandler.supportsLimit()) {
			if (bindLimitParametersFirst) {
				bindVariables.add(startOfQueryParameterIndex++, maxResultsToFetch);
			} else {
				bindVariables.add(maxResultsToFetch);
			}
		}
		return startOfQueryParameterIndex;
	}

	public int bindOffsetParameter(List<Object> theBindVariables, @Nullable Integer theOffset, AbstractLimitHandler theLimitHandler, int theStartOfQueryParameterIndex, boolean theBindLimitParametersFirst) {
		if (theLimitHandler.supportsLimitOffset() && theOffset != null) {
			if (theBindLimitParametersFirst) {
				theBindVariables.add(theStartOfQueryParameterIndex++, theOffset);
			} else {
				theBindVariables.add(theOffset);
			}
		}
		return theStartOfQueryParameterIndex;
	}

	/**
	 * If at least one predicate builder already exists, return the last one added to the chain. If none has been selected, create a builder on HFJ_RESOURCE, add it and return it.
	 */
	public BaseJoiningPredicateBuilder getOrCreateFirstPredicateBuilder() {
		return getOrCreateFirstPredicateBuilder(true);
	}

	/**
	 * If at least one predicate builder already exists, return the last one added to the chain. If none has been selected, create a builder on HFJ_RESOURCE, add it and return it.
	 */
	public BaseJoiningPredicateBuilder getOrCreateFirstPredicateBuilder(boolean theIncludeResourceTypeAndNonDeletedFlag) {
		if (myFirstPredicateBuilder == null) {
			getOrCreateResourceTablePredicateBuilder(theIncludeResourceTypeAndNonDeletedFlag);
		}
		return myFirstPredicateBuilder;
	}

	public ResourceTablePredicateBuilder getOrCreateResourceTablePredicateBuilder() {
		return getOrCreateResourceTablePredicateBuilder(true);
	}

	public ResourceTablePredicateBuilder getOrCreateResourceTablePredicateBuilder(boolean theIncludeResourceTypeAndNonDeletedFlag) {
		if (myResourceTableRoot == null) {
			ResourceTablePredicateBuilder resourceTable = mySqlBuilderFactory.resourceTable(this);
			addTable(resourceTable, null);
			if (theIncludeResourceTypeAndNonDeletedFlag) {
				Condition typeAndDeletionPredicate = resourceTable.createResourceTypeAndNonDeletedPredicates();
				addPredicate(typeAndDeletionPredicate);
			}
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

	public int countBindVariables() {
		return myBindVariableValues.size();
	}


	public void setMatchNothing() {
		myMatchNothing = true;
	}

	public DbTable addTable(String theTableName) {
		return mySchema.addTable(theTableName);
	}

	public PartitionSettings getPartitionSettings() {
		return myPartitionSettings;
	}

	public RequestPartitionId getRequestPartitionId() {
		return myRequestPartitionId;
	}

	public String getResourceType() {
		return myResourceType;
	}

	public ModelConfig getModelConfig() {
		return myModelConfig;
	}

	public void addPredicate(@Nonnull Condition theCondition) {
		assert theCondition != null;
		mySelect.addCondition(theCondition);
		myHaveAtLeastOnePredicate = true;
	}

	public ComboCondition addPredicateLastUpdated(DateRangeParam theDateRange) {
		ResourceTablePredicateBuilder resourceTableRoot = getOrCreateResourceTablePredicateBuilder(false);
		List<Condition> conditions = new ArrayList<>(2);
		BinaryCondition condition;

		if (isNotEqualsComparator(theDateRange)) {
			condition = createConditionForValueWithComparator(LESSTHAN, resourceTableRoot.getLastUpdatedColumn(), theDateRange.getLowerBoundAsInstant());
			conditions.add(condition);
			condition = createConditionForValueWithComparator(GREATERTHAN, resourceTableRoot.getLastUpdatedColumn(), theDateRange.getUpperBoundAsInstant());
			conditions.add(condition);
			return ComboCondition.or(conditions.toArray(new Condition[0]));
		}

		if (theDateRange.getLowerBoundAsInstant() != null) {
			condition = createConditionForValueWithComparator(GREATERTHAN_OR_EQUALS, resourceTableRoot.getLastUpdatedColumn(), theDateRange.getLowerBoundAsInstant());
			conditions.add(condition);
		}

		if (theDateRange.getUpperBoundAsInstant() != null) {
			condition = createConditionForValueWithComparator(LESSTHAN_OR_EQUALS, resourceTableRoot.getLastUpdatedColumn(), theDateRange.getUpperBoundAsInstant());
			conditions.add(condition);
		}

		return ComboCondition.and(conditions.toArray(new Condition[0]));
	}

	private boolean isNotEqualsComparator(DateRangeParam theDateRange) {
		if (theDateRange != null) {
			DateParam lb = theDateRange.getLowerBound();
			DateParam ub = theDateRange.getUpperBound();

			return lb != null && ub != null && lb.getPrefix().equals(NOT_EQUAL) && ub.getPrefix().equals(NOT_EQUAL);
		}
		return false;
	}


	public void addResourceIdsPredicate(List<Long> thePidList) {
		DbColumn resourceIdColumn = getOrCreateFirstPredicateBuilder().getResourceIdColumn();
		InCondition predicate = new InCondition(resourceIdColumn, generatePlaceholders(thePidList));
		addPredicate(predicate);
	}

	public void excludeResourceIdsPredicate(Set<ResourcePersistentId> theExsitinghPidSetToExclude) {
		
		// Do  nothing if it's empty
		if (theExsitinghPidSetToExclude == null || theExsitinghPidSetToExclude.isEmpty())
			return;
		
		List<Long> excludePids = ResourcePersistentId.toLongList(theExsitinghPidSetToExclude);
		
		ourLog.trace("excludePids = " + excludePids);
		
		DbColumn resourceIdColumn = getOrCreateFirstPredicateBuilder().getResourceIdColumn();
		InCondition predicate = new InCondition(resourceIdColumn, generatePlaceholders(excludePids));
		predicate.setNegate(true);
		addPredicate(predicate);
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
			case NOT_EQUAL:
				return BinaryCondition.notEqualTo(theColumn, generatePlaceholder(theValue));
			default:
				throw new IllegalArgumentException(Msg.code(1263));
		}
	}

	public SearchQueryBuilder newChildSqlBuilder() {
		return new SearchQueryBuilder(myFhirContext, myModelConfig, myPartitionSettings, myRequestPartitionId, myResourceType, mySqlBuilderFactory, myBindVariableSubstitutionBase, myDialect, false, myBindVariableValues);
	}

	public SelectQuery getSelect() {
		return mySelect;
	}

	public boolean haveAtLeastOnePredicate() {
		return myHaveAtLeastOnePredicate;
	}

	public void addSortString(DbColumn theColumnValueNormalized, boolean theAscending) {
		OrderObject.NullOrder nullOrder = OrderObject.NullOrder.LAST;
		addSortString(theColumnValueNormalized, theAscending, nullOrder);
	}

	public void addSortNumeric(DbColumn theColumnValueNormalized, boolean theAscending) {
		OrderObject.NullOrder nullOrder = OrderObject.NullOrder.LAST;
		addSortNumeric(theColumnValueNormalized, theAscending, nullOrder);
	}

	public void addSortDate(DbColumn theColumnValueNormalized, boolean theAscending) {
		OrderObject.NullOrder nullOrder = OrderObject.NullOrder.LAST;
		addSortDate(theColumnValueNormalized, theAscending, nullOrder);
	}

	public void addSortString(DbColumn theTheColumnValueNormalized, boolean theTheAscending, OrderObject.NullOrder theNullOrder) {
		if ((dialectIsMySql || dialectIsMsSql)) {
			// MariaDB, MySQL and MSSQL do not support "NULLS FIRST" and "NULLS LAST" syntax.
			String direction = theTheAscending ? " ASC" : " DESC";
			String sortColumnName = theTheColumnValueNormalized.getTable().getAlias() + "." + theTheColumnValueNormalized.getName();
			final StringBuilder sortColumnNameBuilder = new StringBuilder();
			// The following block has been commented out for performance.
			// Uncomment if NullOrder is needed for MariaDB, MySQL or MSSQL
			/*
			// Null values are always treated as less than non-null values.
			if ((theTheAscending && theNullOrder == OrderObject.NullOrder.LAST)
				|| (!theTheAscending && theNullOrder == OrderObject.NullOrder.FIRST)) {
				// In this case, precede the "order by" column with a case statement that returns
				// 1 for null and 0 non-null so that nulls will be sorted as greater than non-nulls.
				sortColumnNameBuilder.append( "CASE WHEN " ).append( sortColumnName ).append( " IS NULL THEN 1 ELSE 0 END" ).append(direction).append(", ");
			}
		   */
			sortColumnNameBuilder.append(sortColumnName).append(direction);
			mySelect.addCustomOrderings(sortColumnNameBuilder.toString());
		} else {
			addSort(theTheColumnValueNormalized, theTheAscending, theNullOrder);
		}
	}

	public void addSortNumeric(DbColumn theTheColumnValueNormalized, boolean theTheAscending, OrderObject.NullOrder theNullOrder) {
		if ((dialectIsMySql || dialectIsMsSql)) {
			// MariaDB, MySQL and MSSQL do not support "NULLS FIRST" and "NULLS LAST" syntax.
			// Null values are always treated as less than non-null values.
			// As such special handling is required here.
			String direction;
			String sortColumnName = theTheColumnValueNormalized.getTable().getAlias() + "." + theTheColumnValueNormalized.getName();
			if ((theTheAscending && theNullOrder == OrderObject.NullOrder.LAST)
				|| (!theTheAscending && theNullOrder == OrderObject.NullOrder.FIRST)) {
				// Negating the numeric column value and reversing the sort order will ensure that the rows appear
				// in the correct order with nulls appearing first or last as needed.
				direction = theTheAscending ? " DESC" : " ASC";
				sortColumnName = "-" + sortColumnName;
			} else {
				direction = theTheAscending ? " ASC" : " DESC";
			}
			mySelect.addCustomOrderings(sortColumnName + direction);
		} else {
			addSort(theTheColumnValueNormalized, theTheAscending, theNullOrder);
		}
	}

	public void addSortDate(DbColumn theTheColumnValueNormalized, boolean theTheAscending, OrderObject.NullOrder theNullOrder) {
		if ((dialectIsMySql || dialectIsMsSql)) {
			// MariaDB, MySQL and MSSQL do not support "NULLS FIRST" and "NULLS LAST" syntax.
			String direction = theTheAscending ? " ASC" : " DESC";
			String sortColumnName = theTheColumnValueNormalized.getTable().getAlias() + "." + theTheColumnValueNormalized.getName();
			final StringBuilder sortColumnNameBuilder = new StringBuilder();
			// The following block has been commented out for performance.
			// Uncomment if NullOrder is needed for MariaDB, MySQL or MSSQL
			/*
			// Null values are always treated as less than non-null values.
			if ((theTheAscending && theNullOrder == OrderObject.NullOrder.LAST)
				|| (!theTheAscending && theNullOrder == OrderObject.NullOrder.FIRST)) {
				// In this case, precede the "order by" column with a case statement that returns
				// 1 for null and 0 non-null so that nulls will be sorted as greater than non-nulls.
				sortColumnNameBuilder.append( "CASE WHEN " ).append( sortColumnName ).append( " IS NULL THEN 1 ELSE 0 END" ).append(direction).append(", ");
			}
 		   */
			sortColumnNameBuilder.append(sortColumnName).append(direction);
			mySelect.addCustomOrderings(sortColumnNameBuilder.toString());
		} else {
			addSort(theTheColumnValueNormalized, theTheAscending, theNullOrder);
		}
	}

	private void addSort(DbColumn theTheColumnValueNormalized, boolean theTheAscending, OrderObject.NullOrder theNullOrder) {
		OrderObject.Dir direction = theTheAscending ? OrderObject.Dir.ASCENDING : OrderObject.Dir.DESCENDING;
		OrderObject orderObject = new OrderObject(direction, theTheColumnValueNormalized);
		orderObject.setNullOrder(theNullOrder);
		mySelect.addCustomOrderings(orderObject);
	}

	/**
	 * If set to true (default is false), force the generated SQL to start
	 * with the {@link ca.uhn.fhir.jpa.model.entity.ResourceTable HFJ_RESOURCE}
	 * table at the root of the query.
	 *
	 * This seems to perform better if there are multiple joins on the
	 * resource ID table.
	 */
	public void setNeedResourceTableRoot(boolean theNeedResourceTableRoot) {
		myNeedResourceTableRoot = theNeedResourceTableRoot;
	}
}
