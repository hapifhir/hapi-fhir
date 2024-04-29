/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.search.builder.sql;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.config.HibernatePropertiesProvider;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.jpa.model.entity.StorageSettings;
import ca.uhn.fhir.jpa.search.builder.QueryStack;
import ca.uhn.fhir.jpa.search.builder.predicate.BaseJoiningPredicateBuilder;
import ca.uhn.fhir.jpa.search.builder.predicate.ComboNonUniqueSearchParameterPredicateBuilder;
import ca.uhn.fhir.jpa.search.builder.predicate.ComboUniqueSearchParameterPredicateBuilder;
import ca.uhn.fhir.jpa.search.builder.predicate.CoordsPredicateBuilder;
import ca.uhn.fhir.jpa.search.builder.predicate.DatePredicateBuilder;
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
import ca.uhn.fhir.rest.param.DateParam;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.rest.param.ParamPrefixEnum;
import com.healthmarketscience.sqlbuilder.BinaryCondition;
import com.healthmarketscience.sqlbuilder.ComboCondition;
import com.healthmarketscience.sqlbuilder.ComboExpression;
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
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.SQLServerDialect;
import org.hibernate.dialect.pagination.AbstractLimitHandler;
import org.hibernate.query.internal.QueryOptionsImpl;
import org.hibernate.query.spi.Limit;
import org.hibernate.query.spi.QueryOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static ca.uhn.fhir.rest.param.ParamPrefixEnum.GREATERTHAN;
import static ca.uhn.fhir.rest.param.ParamPrefixEnum.GREATERTHAN_OR_EQUALS;
import static ca.uhn.fhir.rest.param.ParamPrefixEnum.LESSTHAN;
import static ca.uhn.fhir.rest.param.ParamPrefixEnum.LESSTHAN_OR_EQUALS;
import static ca.uhn.fhir.rest.param.ParamPrefixEnum.NOT_EQUAL;
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
	private final StorageSettings myStorageSettings;
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
	private int myNextNearnessColumnId = 0;
	private DbColumn mySelectedResourceIdColumn;

	/**
	 * Constructor
	 */
	public SearchQueryBuilder(
			FhirContext theFhirContext,
			StorageSettings theStorageSettings,
			PartitionSettings thePartitionSettings,
			RequestPartitionId theRequestPartitionId,
			String theResourceType,
			SqlObjectFactory theSqlBuilderFactory,
			HibernatePropertiesProvider theDialectProvider,
			boolean theCountQuery) {
		this(
				theFhirContext,
				theStorageSettings,
				thePartitionSettings,
				theRequestPartitionId,
				theResourceType,
				theSqlBuilderFactory,
				UUID.randomUUID() + "-",
				theDialectProvider.getDialect(),
				theCountQuery,
				new ArrayList<>());
	}

	/**
	 * Constructor for child SQL Builders
	 */
	private SearchQueryBuilder(
			FhirContext theFhirContext,
			StorageSettings theStorageSettings,
			PartitionSettings thePartitionSettings,
			RequestPartitionId theRequestPartitionId,
			String theResourceType,
			SqlObjectFactory theSqlBuilderFactory,
			String theBindVariableSubstitutionBase,
			Dialect theDialect,
			boolean theCountQuery,
			ArrayList<Object> theBindVariableValues) {
		myFhirContext = theFhirContext;
		myStorageSettings = theStorageSettings;
		myPartitionSettings = thePartitionSettings;
		myRequestPartitionId = theRequestPartitionId;
		myResourceType = theResourceType;
		mySqlBuilderFactory = theSqlBuilderFactory;
		myCountQuery = theCountQuery;
		myDialect = theDialect;
		if (myDialect instanceof org.hibernate.dialect.MySQLDialect) {
			dialectIsMySql = true;
		}
		if (myDialect instanceof org.hibernate.dialect.SQLServerDialect) {
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
		ComboUniqueSearchParameterPredicateBuilder retVal =
				mySqlBuilderFactory.newComboUniqueSearchParameterPredicateBuilder(this);
		addTable(retVal, null);
		return retVal;
	}

	/**
	 * Add and return a predicate builder (or a root query if no root query exists yet) for selecting on a Composite Unique search parameter
	 */
	public ComboNonUniqueSearchParameterPredicateBuilder addComboNonUniquePredicateBuilder() {
		ComboNonUniqueSearchParameterPredicateBuilder retVal =
				mySqlBuilderFactory.newComboNonUniqueSearchParameterPredicateBuilder(this);
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
	 * Create, add and return a predicate builder (or a root query if no root query exists yet) for selecting on a DATE search parameter
	 */
	public DatePredicateBuilder addDatePredicateBuilder(@Nullable DbColumn theSourceJoinColumn) {
		DatePredicateBuilder retVal = mySqlBuilderFactory.dateIndexTable(this);
		addTable(retVal, theSourceJoinColumn);
		return retVal;
	}

	/**
	 * Create a predicate builder for selecting on a DATE search parameter
	 */
	public DatePredicateBuilder createDatePredicateBuilder() {
		return mySqlBuilderFactory.dateIndexTable(this);
	}

	/**
	 * Create, add and return a predicate builder (or a root query if no root query exists yet) for selecting on a NUMBER search parameter
	 */
	public NumberPredicateBuilder addNumberPredicateBuilder(@Nullable DbColumn theSourceJoinColumn) {
		NumberPredicateBuilder retVal = createNumberPredicateBuilder();
		addTable(retVal, theSourceJoinColumn);
		return retVal;
	}

	/**
	 * Create a predicate builder for selecting on a NUMBER search parameter
	 */
	public NumberPredicateBuilder createNumberPredicateBuilder() {
		return mySqlBuilderFactory.numberIndexTable(this);
	}

	/**
	 * Add and return a predicate builder (or a root query if no root query exists yet) for selecting on the Resource table
	 */
	public ResourceTablePredicateBuilder addResourceTablePredicateBuilder(@Nullable DbColumn theSourceJoinColumn) {
		ResourceTablePredicateBuilder retVal = mySqlBuilderFactory.resourceTable(this);
		addTable(retVal, theSourceJoinColumn);
		return retVal;
	}

	/**
	 * Create, add and return a predicate builder (or a root query if no root query exists yet) for selecting on a QUANTITY search parameter
	 */
	public QuantityPredicateBuilder addQuantityPredicateBuilder(@Nullable DbColumn theSourceJoinColumn) {
		QuantityPredicateBuilder retVal = createQuantityPredicateBuilder();
		addTable(retVal, theSourceJoinColumn);

		return retVal;
	}

	/**
	 * Create a predicate builder for selecting on a QUANTITY search parameter
	 */
	public QuantityPredicateBuilder createQuantityPredicateBuilder() {
		return mySqlBuilderFactory.quantityIndexTable(this);
	}

	public QuantityNormalizedPredicateBuilder addQuantityNormalizedPredicateBuilder(
			@Nullable DbColumn theSourceJoinColumn) {

		QuantityNormalizedPredicateBuilder retVal = mySqlBuilderFactory.quantityNormalizedIndexTable(this);
		addTable(retVal, theSourceJoinColumn);

		return retVal;
	}

	/**
	 * Add and return a predicate builder (or a root query if no root query exists yet) for selecting on a <code>_source</code> search parameter
	 */
	public SourcePredicateBuilder addSourcePredicateBuilder(
			@Nullable DbColumn theSourceJoinColumn, SelectQuery.JoinType theJoinType) {
		SourcePredicateBuilder retVal = mySqlBuilderFactory.newSourcePredicateBuilder(this);
		addTable(retVal, theSourceJoinColumn, theJoinType);
		return retVal;
	}

	/**
	 * Create, add and return a predicate builder (or a root query if no root query exists yet) for selecting on a REFERENCE search parameter
	 */
	public ResourceLinkPredicateBuilder addReferencePredicateBuilder(
			QueryStack theQueryStack, @Nullable DbColumn theSourceJoinColumn) {
		ResourceLinkPredicateBuilder retVal = createReferencePredicateBuilder(theQueryStack);
		addTable(retVal, theSourceJoinColumn);
		return retVal;
	}

	/**
	 * Create a predicate builder for selecting on a REFERENCE search parameter
	 */
	public ResourceLinkPredicateBuilder createReferencePredicateBuilder(QueryStack theQueryStack) {
		return mySqlBuilderFactory.referenceIndexTable(theQueryStack, this, false);
	}

	/**
	 * Add and return a predicate builder (or a root query if no root query exists yet) for selecting on a resource link where the
	 * source and target are reversed. This is used for _has queries.
	 */
	public ResourceLinkPredicateBuilder addReferencePredicateBuilderReversed(
			QueryStack theQueryStack, DbColumn theSourceJoinColumn) {
		ResourceLinkPredicateBuilder retVal = mySqlBuilderFactory.referenceIndexTable(theQueryStack, this, true);
		addTable(retVal, theSourceJoinColumn);
		return retVal;
	}

	/**
	 * Create, add and return a predicate builder (or a root query if no root query exists yet) for selecting on a STRING search parameter
	 */
	public StringPredicateBuilder addStringPredicateBuilder(@Nullable DbColumn theSourceJoinColumn) {
		StringPredicateBuilder retVal = createStringPredicateBuilder();
		addTable(retVal, theSourceJoinColumn);
		return retVal;
	}

	/**
	 * Create a predicate builder for selecting on a STRING search parameter
	 */
	public StringPredicateBuilder createStringPredicateBuilder() {
		return mySqlBuilderFactory.stringIndexTable(this);
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
	 * Create, add and return a predicate builder (or a root query if no root query exists yet) for selecting on a TOKEN search parameter
	 */
	public TokenPredicateBuilder addTokenPredicateBuilder(@Nullable DbColumn theSourceJoinColumn) {
		TokenPredicateBuilder retVal = createTokenPredicateBuilder();
		addTable(retVal, theSourceJoinColumn);
		return retVal;
	}

	/**
	 * Create a predicate builder for selecting on a TOKEN search parameter
	 */
	public TokenPredicateBuilder createTokenPredicateBuilder() {
		return mySqlBuilderFactory.tokenIndexTable(this);
	}

	public void addCustomJoin(
			SelectQuery.JoinType theJoinType, DbTable theFromTable, DbTable theToTable, Condition theCondition) {
		mySelect.addCustomJoin(theJoinType, theFromTable, theToTable, theCondition);
	}

	public ComboCondition createOnCondition(DbColumn theSourceColumn, DbColumn theTargetColumn) {
		ComboCondition onCondition = ComboCondition.and();
		onCondition.addCondition(BinaryCondition.equalTo(theSourceColumn, theTargetColumn));

		return onCondition;
	}

	/**
	 * Add and return a predicate builder (or a root query if no root query exists yet) for selecting on a <code>:missing</code> search parameter
	 */
	public SearchParamPresentPredicateBuilder addSearchParamPresentPredicateBuilder(
			@Nullable DbColumn theSourceJoinColumn) {
		SearchParamPresentPredicateBuilder retVal = mySqlBuilderFactory.searchParamPresentPredicateBuilder(this);
		addTable(retVal, theSourceJoinColumn);
		return retVal;
	}

	/**
	 * Create, add and return a predicate builder (or a root query if no root query exists yet) for selecting on a URI search parameter
	 */
	public UriPredicateBuilder addUriPredicateBuilder(@Nullable DbColumn theSourceJoinColumn) {
		UriPredicateBuilder retVal = createUriPredicateBuilder();
		addTable(retVal, theSourceJoinColumn);
		return retVal;
	}

	/**
	 * Create a predicate builder for selecting on a URI search parameter
	 */
	public UriPredicateBuilder createUriPredicateBuilder() {
		return mySqlBuilderFactory.uriIndexTable(this);
	}

	public SqlObjectFactory getSqlBuilderFactory() {
		return mySqlBuilderFactory;
	}

	public ResourceIdPredicateBuilder newResourceIdBuilder() {
		return mySqlBuilderFactory.resourceId(this);
	}

	/**
	 * Add and return a predicate builder (or a root query if no root query exists yet) for an arbitrary table
	 */
	private void addTable(BaseJoiningPredicateBuilder thePredicateBuilder, @Nullable DbColumn theSourceJoinColumn) {
		addTable(thePredicateBuilder, theSourceJoinColumn, SelectQuery.JoinType.INNER);
	}

	private void addTable(
			BaseJoiningPredicateBuilder thePredicateBuilder,
			@Nullable DbColumn theSourceJoinColumn,
			SelectQuery.JoinType theJoinType) {
		if (theSourceJoinColumn != null) {
			DbTable fromTable = theSourceJoinColumn.getTable();
			DbTable toTable = thePredicateBuilder.getTable();
			DbColumn toColumn = thePredicateBuilder.getResourceIdColumn();
			addJoin(fromTable, toTable, theSourceJoinColumn, toColumn, theJoinType);
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
					mySelect.addCustomColumns(
							FunctionCall.count().setIsDistinct(true).addColumnParams(root.getResourceIdColumn()));
				} else {
					mySelectedResourceIdColumn = root.getResourceIdColumn();
					mySelect.addColumns(mySelectedResourceIdColumn);
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
			addJoin(fromTable, toTable, fromColumn, toColumn, theJoinType);
		}
	}

	public void addJoin(
			DbTable theFromTable,
			DbTable theToTable,
			DbColumn theFromColumn,
			DbColumn theToColumn,
			SelectQuery.JoinType theJoinType) {
		Join join = new DbJoin(
				mySpec, theFromTable, theToTable, new DbColumn[] {theFromColumn}, new DbColumn[] {theToColumn});
		mySelect.addJoins(theJoinType, join);
	}

	public void addJoin(DbTable theFromTable, DbTable theToTable, DbColumn theFromColumn, DbColumn theToColumn) {
		Join join = new DbJoin(
				mySpec, theFromTable, theToTable, new DbColumn[] {theFromColumn}, new DbColumn[] {theToColumn});
		mySelect.addJoins(SelectQuery.JoinType.INNER, join);
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
			Limit selection = new Limit();
			selection.setFirstRow(offset);
			selection.setMaxRows(maxResultsToFetch);
			QueryOptions queryOptions = new QueryOptionsImpl();
			sql = limitHandler.processSql(sql, selection, queryOptions);

			int startOfQueryParameterIndex = 0;

			boolean isSqlServer = (myDialect instanceof SQLServerDialect);
			if (isSqlServer) {

				/*
				 * SQL server requires an ORDER BY clause to be present in the SQL if there is
				 * an OFFSET/FETCH FIRST clause, so if there isn't already an ORDER BY clause,
				 * the dialect will automatically add an order by with a pseudo-column name. This
				 * happens in SQLServer2012LimitHandler.
				 *
				 * But, SQL Server also pukes if you include an ORDER BY on a column that you
				 * aren't also SELECTing, if the select statement is DISTINCT. Who knows why SQL
				 * Server is so picky.. but anyhow, this causes an issue, so we manually replace
				 * the pseudo-column with an actual selected column.
				 */
				if (sql.startsWith("SELECT DISTINCT ")) {
					if (sql.contains("order by @@version")) {
						if (mySelectedResourceIdColumn != null) {
							sql = sql.replace(
									"order by @@version", "order by " + mySelectedResourceIdColumn.getColumnNameSQL());
						}
					}
				}

				// The SQLServerDialect has a bunch of one-off processing to deal with rules on when
				// a limit can be used, so we can't rely on the flags that the limithandler exposes since
				// the exact structure of the query depends on the parameters
				if (sql.contains("top(?)")) {
					bindVariables.add(0, maxResultsToFetch);
				}
				if (sql.contains("offset 0 rows fetch first ? rows only")) {
					bindVariables.add(maxResultsToFetch);
				}
				if (sql.contains("offset ? rows fetch next ? rows only")) {
					bindVariables.add(theOffset);
					bindVariables.add(maxResultsToFetch);
				}
				if (offset != null && sql.contains("rownumber_")) {
					bindVariables.add(theOffset + 1);
					bindVariables.add(theOffset + maxResultsToFetch + 1);
				}

			} else if (limitHandler.supportsVariableLimit()) {

				boolean bindLimitParametersFirst = limitHandler.bindLimitParametersFirst();
				if (limitHandler.useMaxForLimit() && offset != null) {
					maxResultsToFetch = maxResultsToFetch + offset;
				}

				if (limitHandler.bindLimitParametersInReverseOrder()) {
					startOfQueryParameterIndex = bindCountParameter(
							bindVariables,
							maxResultsToFetch,
							limitHandler,
							startOfQueryParameterIndex,
							bindLimitParametersFirst);
					bindOffsetParameter(
							bindVariables, offset, limitHandler, startOfQueryParameterIndex, bindLimitParametersFirst);
				} else {
					startOfQueryParameterIndex = bindOffsetParameter(
							bindVariables, offset, limitHandler, startOfQueryParameterIndex, bindLimitParametersFirst);
					bindCountParameter(
							bindVariables,
							maxResultsToFetch,
							limitHandler,
							startOfQueryParameterIndex,
							bindLimitParametersFirst);
				}
			}
		}

		return new GeneratedSql(myMatchNothing, sql, bindVariables);
	}

	private int bindCountParameter(
			List<Object> bindVariables,
			Integer maxResultsToFetch,
			AbstractLimitHandler limitHandler,
			int startOfQueryParameterIndex,
			boolean bindLimitParametersFirst) {
		if (limitHandler.supportsLimit()) {
			if (bindLimitParametersFirst) {
				bindVariables.add(startOfQueryParameterIndex++, maxResultsToFetch);
			} else {
				bindVariables.add(maxResultsToFetch);
			}
		}
		return startOfQueryParameterIndex;
	}

	public int bindOffsetParameter(
			List<Object> theBindVariables,
			@Nullable Integer theOffset,
			AbstractLimitHandler theLimitHandler,
			int theStartOfQueryParameterIndex,
			boolean theBindLimitParametersFirst) {
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
	public BaseJoiningPredicateBuilder getOrCreateFirstPredicateBuilder(
			boolean theIncludeResourceTypeAndNonDeletedFlag) {
		if (myFirstPredicateBuilder == null) {
			getOrCreateResourceTablePredicateBuilder(theIncludeResourceTypeAndNonDeletedFlag);
		}
		return myFirstPredicateBuilder;
	}

	public ResourceTablePredicateBuilder getOrCreateResourceTablePredicateBuilder() {
		return getOrCreateResourceTablePredicateBuilder(true);
	}

	public ResourceTablePredicateBuilder getOrCreateResourceTablePredicateBuilder(
			boolean theIncludeResourceTypeAndNonDeletedFlag) {
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
		return theValues.stream().map(this::generatePlaceholder).collect(Collectors.toList());
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

	public StorageSettings getStorageSettings() {
		return myStorageSettings;
	}

	public void addPredicate(@Nonnull Condition theCondition) {
		assert theCondition != null;
		mySelect.addCondition(theCondition);
		myHaveAtLeastOnePredicate = true;
	}

	public ComboCondition addPredicateLastUpdated(DateRangeParam theDateRange) {
		ResourceTablePredicateBuilder resourceTableRoot = getOrCreateResourceTablePredicateBuilder(false);
		return addPredicateLastUpdated(theDateRange, resourceTableRoot);
	}

	public ComboCondition addPredicateLastUpdated(
			DateRangeParam theDateRange, ResourceTablePredicateBuilder theResourceTablePredicateBuilder) {
		List<Condition> conditions = new ArrayList<>(2);
		BinaryCondition condition;

		if (isNotEqualsComparator(theDateRange)) {
			condition = createConditionForValueWithComparator(
					LESSTHAN,
					theResourceTablePredicateBuilder.getLastUpdatedColumn(),
					theDateRange.getLowerBoundAsInstant());
			conditions.add(condition);
			condition = createConditionForValueWithComparator(
					GREATERTHAN,
					theResourceTablePredicateBuilder.getLastUpdatedColumn(),
					theDateRange.getUpperBoundAsInstant());
			conditions.add(condition);
			return ComboCondition.or(conditions.toArray(new Condition[0]));
		}

		if (theDateRange.getLowerBoundAsInstant() != null) {
			condition = createConditionForValueWithComparator(
					GREATERTHAN_OR_EQUALS,
					theResourceTablePredicateBuilder.getLastUpdatedColumn(),
					theDateRange.getLowerBoundAsInstant());
			conditions.add(condition);
		}

		if (theDateRange.getUpperBoundAsInstant() != null) {
			condition = createConditionForValueWithComparator(
					LESSTHAN_OR_EQUALS,
					theResourceTablePredicateBuilder.getLastUpdatedColumn(),
					theDateRange.getUpperBoundAsInstant());
			conditions.add(condition);
		}

		return ComboCondition.and(conditions.toArray(new Condition[0]));
	}

	private boolean isNotEqualsComparator(DateRangeParam theDateRange) {
		if (theDateRange != null) {
			DateParam lb = theDateRange.getLowerBound();
			DateParam ub = theDateRange.getUpperBound();

			return lb != null
					&& ub != null
					&& lb.getPrefix().equals(NOT_EQUAL)
					&& ub.getPrefix().equals(NOT_EQUAL);
		}
		return false;
	}

	public void addResourceIdsPredicate(List<Long> thePidList) {
		DbColumn resourceIdColumn = getOrCreateFirstPredicateBuilder().getResourceIdColumn();
		InCondition predicate = new InCondition(resourceIdColumn, generatePlaceholders(thePidList));
		addPredicate(predicate);
	}

	public void excludeResourceIdsPredicate(Set<JpaPid> theExistingPidSetToExclude) {

		// Do  nothing if it's empty
		if (theExistingPidSetToExclude == null || theExistingPidSetToExclude.isEmpty()) return;

		List<Long> excludePids = JpaPid.toLongList(theExistingPidSetToExclude);

		ourLog.trace("excludePids = {}", excludePids);

		DbColumn resourceIdColumn = getOrCreateFirstPredicateBuilder().getResourceIdColumn();
		InCondition predicate = new InCondition(resourceIdColumn, generatePlaceholders(excludePids));
		predicate.setNegate(true);
		addPredicate(predicate);
	}

	public BinaryCondition createConditionForValueWithComparator(
			ParamPrefixEnum theComparator, DbColumn theColumn, Object theValue) {
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
			case STARTS_AFTER:
			case APPROXIMATE:
			case ENDS_BEFORE:
			case EQUAL:
			default:
				throw new IllegalArgumentException(Msg.code(1263));
		}
	}

	public SearchQueryBuilder newChildSqlBuilder() {
		return new SearchQueryBuilder(
				myFhirContext,
				myStorageSettings,
				myPartitionSettings,
				myRequestPartitionId,
				myResourceType,
				mySqlBuilderFactory,
				myBindVariableSubstitutionBase,
				myDialect,
				false,
				myBindVariableValues);
	}

	public SelectQuery getSelect() {
		return mySelect;
	}

	public boolean haveAtLeastOnePredicate() {
		return myHaveAtLeastOnePredicate;
	}

	public void addSortCoordsNear(
			CoordsPredicateBuilder theCoordsBuilder,
			double theLatitudeValue,
			double theLongitudeValue,
			boolean theAscending) {
		FunctionCall absLatitude = new FunctionCall("ABS");
		String latitudePlaceholder = generatePlaceholder(theLatitudeValue);
		ComboExpression absLatitudeMiddle = new ComboExpression(
				ComboExpression.Op.SUBTRACT, theCoordsBuilder.getColumnLatitude(), latitudePlaceholder);
		absLatitude = absLatitude.addCustomParams(absLatitudeMiddle);

		FunctionCall absLongitude = new FunctionCall("ABS");
		String longitudePlaceholder = generatePlaceholder(theLongitudeValue);
		ComboExpression absLongitudeMiddle = new ComboExpression(
				ComboExpression.Op.SUBTRACT, theCoordsBuilder.getColumnLongitude(), longitudePlaceholder);
		absLongitude = absLongitude.addCustomParams(absLongitudeMiddle);

		ComboExpression sum = new ComboExpression(ComboExpression.Op.ADD, absLatitude, absLongitude);
		String ordering;
		if (theAscending) {
			ordering = "";
		} else {
			ordering = " DESC";
		}

		String columnName = "MHD" + (myNextNearnessColumnId++);
		mySelect.addAliasedColumn(sum, columnName);
		mySelect.addCustomOrderings(columnName + ordering);
	}

	public void addSortString(DbColumn theColumnValueNormalized, boolean theAscending) {
		addSortString(theColumnValueNormalized, theAscending, false);
	}

	public void addSortString(DbColumn theColumnValueNormalized, boolean theAscending, boolean theUseAggregate) {
		OrderObject.NullOrder nullOrder = OrderObject.NullOrder.LAST;
		addSortString(theColumnValueNormalized, theAscending, nullOrder, theUseAggregate);
	}

	public void addSortNumeric(DbColumn theColumnValueNormalized, boolean theAscending) {
		addSortNumeric(theColumnValueNormalized, theAscending, false);
	}

	public void addSortNumeric(DbColumn theColumnValueNormalized, boolean theAscending, boolean theUseAggregate) {
		OrderObject.NullOrder nullOrder = OrderObject.NullOrder.LAST;
		addSortNumeric(theColumnValueNormalized, theAscending, nullOrder, theUseAggregate);
	}

	public void addSortDate(DbColumn theColumnValueNormalized, boolean theAscending) {
		addSortDate(theColumnValueNormalized, theAscending, false);
	}

	public void addSortDate(DbColumn theColumnValueNormalized, boolean theAscending, boolean theUseAggregate) {
		OrderObject.NullOrder nullOrder = OrderObject.NullOrder.LAST;
		addSortDate(theColumnValueNormalized, theAscending, nullOrder, theUseAggregate);
	}

	public void addSortString(
			DbColumn theTheColumnValueNormalized,
			boolean theTheAscending,
			OrderObject.NullOrder theNullOrder,
			boolean theUseAggregate) {
		if ((dialectIsMySql || dialectIsMsSql)) {
			// MariaDB, MySQL and MSSQL do not support "NULLS FIRST" and "NULLS LAST" syntax.
			String direction = theTheAscending ? " ASC" : " DESC";
			String sortColumnName =
					theTheColumnValueNormalized.getTable().getAlias() + "." + theTheColumnValueNormalized.getName();
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
			sortColumnName = formatColumnNameForAggregate(theTheAscending, theUseAggregate, sortColumnName);
			sortColumnNameBuilder.append(sortColumnName).append(direction);
			mySelect.addCustomOrderings(sortColumnNameBuilder.toString());
		} else {
			addSort(theTheColumnValueNormalized, theTheAscending, theNullOrder, theUseAggregate);
		}
	}

	private static String formatColumnNameForAggregate(
			boolean theTheAscending, boolean theUseAggregate, String sortColumnName) {
		if (theUseAggregate) {
			String aggregateFunction;
			if (theTheAscending) {
				aggregateFunction = "MIN";
			} else {
				aggregateFunction = "MAX";
			}
			sortColumnName = aggregateFunction + "(" + sortColumnName + ")";
		}
		return sortColumnName;
	}

	public void addSortNumeric(
			DbColumn theTheColumnValueNormalized,
			boolean theAscending,
			OrderObject.NullOrder theNullOrder,
			boolean theUseAggregate) {
		if ((dialectIsMySql || dialectIsMsSql)) {
			// MariaDB, MySQL and MSSQL do not support "NULLS FIRST" and "NULLS LAST" syntax.
			// Null values are always treated as less than non-null values.
			// As such special handling is required here.
			String direction;
			String sortColumnName =
					theTheColumnValueNormalized.getTable().getAlias() + "." + theTheColumnValueNormalized.getName();
			if ((theAscending && theNullOrder == OrderObject.NullOrder.LAST)
					|| (!theAscending && theNullOrder == OrderObject.NullOrder.FIRST)) {
				// Negating the numeric column value and reversing the sort order will ensure that the rows appear
				// in the correct order with nulls appearing first or last as needed.
				direction = theAscending ? " DESC" : " ASC";
				sortColumnName = "-" + sortColumnName;
			} else {
				direction = theAscending ? " ASC" : " DESC";
			}
			sortColumnName = formatColumnNameForAggregate(theAscending, theUseAggregate, sortColumnName);
			mySelect.addCustomOrderings(sortColumnName + direction);
		} else {
			addSort(theTheColumnValueNormalized, theAscending, theNullOrder, theUseAggregate);
		}
	}

	public void addSortDate(
			DbColumn theTheColumnValueNormalized,
			boolean theTheAscending,
			OrderObject.NullOrder theNullOrder,
			boolean theUseAggregate) {
		if ((dialectIsMySql || dialectIsMsSql)) {
			// MariaDB, MySQL and MSSQL do not support "NULLS FIRST" and "NULLS LAST" syntax.
			String direction = theTheAscending ? " ASC" : " DESC";
			String sortColumnName =
					theTheColumnValueNormalized.getTable().getAlias() + "." + theTheColumnValueNormalized.getName();
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
			sortColumnName = formatColumnNameForAggregate(theTheAscending, theUseAggregate, sortColumnName);
			sortColumnNameBuilder.append(sortColumnName).append(direction);
			mySelect.addCustomOrderings(sortColumnNameBuilder.toString());
		} else {
			addSort(theTheColumnValueNormalized, theTheAscending, theNullOrder, theUseAggregate);
		}
	}

	private void addSort(
			DbColumn theTheColumnValueNormalized,
			boolean theTheAscending,
			OrderObject.NullOrder theNullOrder,
			boolean theUseAggregate) {
		OrderObject.Dir direction = theTheAscending ? OrderObject.Dir.ASCENDING : OrderObject.Dir.DESCENDING;
		Object columnToOrder = theTheColumnValueNormalized;
		if (theUseAggregate) {
			if (theTheAscending) {
				columnToOrder = FunctionCall.min().addColumnParams(theTheColumnValueNormalized);
			} else {
				columnToOrder = FunctionCall.max().addColumnParams(theTheColumnValueNormalized);
			}
		}
		OrderObject orderObject = new OrderObject(direction, columnToOrder);
		orderObject.setNullOrder(theNullOrder);
		mySelect.addCustomOrderings(orderObject);
	}

	/**
	 * If set to true (default is false), force the generated SQL to start
	 * with the {@link ca.uhn.fhir.jpa.model.entity.ResourceTable HFJ_RESOURCE}
	 * table at the root of the query.
	 * <p>
	 * This seems to perform better if there are multiple joins on the
	 * resource ID table.
	 */
	public void setNeedResourceTableRoot(boolean theNeedResourceTableRoot) {
		myNeedResourceTableRoot = theNeedResourceTableRoot;
	}
}
