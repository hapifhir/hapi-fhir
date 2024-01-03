/*-
 * #%L
 * HAPI FHIR JPA Server - HFQL Driver
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
package ca.uhn.fhir.jpa.fql.executor;

import ca.uhn.fhir.context.BaseRuntimeChildDefinition;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.fhirpath.FhirPathExecutionException;
import ca.uhn.fhir.fhirpath.IFhirPath;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.fql.parser.HfqlFhirPathParser;
import ca.uhn.fhir.jpa.fql.parser.HfqlStatement;
import ca.uhn.fhir.jpa.fql.parser.HfqlStatementParser;
import ca.uhn.fhir.jpa.fql.util.HfqlConstants;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.searchparam.util.JpaParamUtil;
import ca.uhn.fhir.model.api.IQueryParameterAnd;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.QualifiedParamList;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.param.DateOrListParam;
import ca.uhn.fhir.rest.param.DateParam;
import ca.uhn.fhir.rest.param.ParamPrefixEnum;
import ca.uhn.fhir.rest.param.ParameterUtil;
import ca.uhn.fhir.rest.param.QualifierDetails;
import ca.uhn.fhir.rest.param.TokenOrListParam;
import ca.uhn.fhir.rest.server.IPagingProvider;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;
import ca.uhn.fhir.rest.server.util.ResourceSearchParams;
import ca.uhn.fhir.util.UrlUtil;
import com.google.common.collect.Lists;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r4.model.DateTimeType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;
import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * This class could be considered the main entrypoint into the HFQL executor.
 * It receives a raw HFQL query, parses it, executes it, and returns a result set.
 * Conceptually the {@link #executeInitialSearch(String, Integer, RequestDetails)}
 * method can be thought of like the JPA DAO <code>search</code> method, and the
 * {@link #executeContinuation(HfqlStatement, String, int, Integer, RequestDetails)}
 * can be thought of like loading a subsequent page of the search results.
 * <p>
 * Both of these methods return an {@link IHfqlExecutionResult}, which is essentially
 * a result row iterator.
 */
public class HfqlExecutor implements IHfqlExecutor {
	public static final int BATCH_SIZE = 1000;
	public static final String[] EMPTY_STRING_ARRAY = new String[0];
	public static final Set<GroupByKey> NULL_GROUP_BY_KEY = Set.of(new GroupByKey(List.of()));
	private static final Logger ourLog = LoggerFactory.getLogger(HfqlExecutor.class);

	@Autowired
	private DaoRegistry myDaoRegistry;

	@Autowired
	private FhirContext myFhirContext;

	@Autowired
	private IPagingProvider myPagingProvider;

	@Autowired
	private ISearchParamRegistry mySearchParamRegistry;

	/**
	 * Constructor
	 */
	public HfqlExecutor() {
		super();
	}

	@Override
	public IHfqlExecutionResult executeInitialSearch(
			String theStatement, Integer theLimit, RequestDetails theRequestDetails) {
		try {
			return doExecuteInitialSearch(theStatement, theLimit, theRequestDetails);
		} catch (Exception e) {
			ourLog.warn("Failed to execute HFFQL statement", e);
			return StaticHfqlExecutionResult.withError(defaultIfNull(e.getMessage(), "(no message)"));
		}
	}

	@Nonnull
	private IHfqlExecutionResult doExecuteInitialSearch(
			String theStatement, Integer theLimit, RequestDetails theRequestDetails) {
		HfqlStatementParser parser = new HfqlStatementParser(myFhirContext, theStatement);
		HfqlStatement statement = parser.parse();
		IFhirResourceDao dao = myDaoRegistry.getResourceDao(statement.getFromResourceName());
		if (dao == null) {
			throw new DataFormatException(
					Msg.code(2406) + "Unknown or unsupported FROM type: " + statement.getFromResourceName());
		}

		massageSelectColumnNames(statement);
		populateSelectColumnDataTypes(statement);
		validateWhereClauses(statement);
		massageWhereClauses(statement);

		SearchParameterMap map = new SearchParameterMap();
		addHfqlWhereClausesToSearchParameterMap(statement, map);

		Integer limit = theLimit;
		if (statement.hasOrderClause()) {
			/*
			 * If we're ordering search results, we need to load all available data in order
			 * to sort it because we handle ordering in application code currently. A good
			 * future optimization would be to handle ordering in the database when possible,
			 * but we can't always do that because the query can specify an order on any
			 * arbitrary FHIRPath expression.
			 */
			limit = null;
		} else if (statement.getLimit() != null) {
			limit = limit == null ? statement.getLimit() : Math.min(limit, statement.getLimit());
		}

		HfqlExecutionContext executionContext = new HfqlExecutionContext(myFhirContext.newFhirPath());
		IBundleProvider outcome = dao.search(map, theRequestDetails);
		Predicate<IBaseResource> whereClausePredicate = newWhereClausePredicate(executionContext, statement);

		IHfqlExecutionResult executionResult;
		if (statement.hasCountClauses()) {
			executionResult = executeCountClause(statement, executionContext, outcome, whereClausePredicate);
		} else {
			executionResult = new LocalSearchHfqlExecutionResult(
					statement, outcome, executionContext, limit, 0, whereClausePredicate, myFhirContext);
		}

		if (statement.hasOrderClause()) {
			executionResult = createOrderedResult(statement, executionResult);
		}

		return executionResult;
	}

	private void validateWhereClauses(HfqlStatement theStatement) {
		for (HfqlStatement.WhereClause next : theStatement.getWhereClauses()) {
			if (isDataValueWhereClause(next)) {
				if (next.getLeft().matches("^[a-zA-Z]+$")) {
					RuntimeResourceDefinition resDef =
							myFhirContext.getResourceDefinition(theStatement.getFromResourceName());
					if (resDef.getChildByName(next.getLeft()) == null) {
						throw new InvalidRequestException(
								Msg.code(2429) + "Resource type " + theStatement.getFromResourceName()
										+ " does not have a root element named '" + next.getLeft() + "'");
					}
				}
			}
		}
	}

	/**
	 * If the user has included a WHERE clause that has a FHIRPath expression but
	 * could actually be satisfied by a Search Parameter, we'll insert a
	 * search_match expression so that it's more efficient.
	 */
	private void massageWhereClauses(HfqlStatement theStatement) {
		String fromResourceName = theStatement.getFromResourceName();
		ResourceSearchParams activeSearchParams = mySearchParamRegistry.getActiveSearchParams(fromResourceName);

		for (HfqlStatement.WhereClause nextWhereClause : theStatement.getWhereClauses()) {

			String left = null;
			List<String> rightValues = null;
			String comparator;
			if (isDataValueWhereClause(nextWhereClause)) {
				left = nextWhereClause.getLeft();
				comparator = "";
				rightValues = nextWhereClause.getRightAsStrings();
			} else if (nextWhereClause.getOperator() == HfqlStatement.WhereClauseOperatorEnum.UNARY_BOOLEAN
					&& nextWhereClause.getRightAsStrings().size() > 1) {
				left = nextWhereClause.getLeft();
				rightValues = nextWhereClause
						.getRightAsStrings()
						.subList(1, nextWhereClause.getRightAsStrings().size());
				switch (nextWhereClause.getRightAsStrings().get(0)) {
					case "=":
						comparator = "";
						break;
					case "<":
						comparator = ParamPrefixEnum.LESSTHAN.getValue();
						break;
					case "<=":
						comparator = ParamPrefixEnum.LESSTHAN_OR_EQUALS.getValue();
						break;
					case ">":
						comparator = ParamPrefixEnum.GREATERTHAN.getValue();
						break;
					case ">=":
						comparator = ParamPrefixEnum.GREATERTHAN_OR_EQUALS.getValue();
						break;
					case "!=":
						comparator = ParamPrefixEnum.NOT_EQUAL.getValue();
						break;
					case "~":
						comparator = ParamPrefixEnum.APPROXIMATE.getValue();
						break;
					default:
						left = null;
						comparator = null;
						rightValues = null;
				}
			} else {
				comparator = null;
			}

			if (left != null) {
				if (isFhirPathExpressionEquivalent("id", left, fromResourceName)) {
					// This is an expression for Resource.id
					nextWhereClause.setLeft("id");
					nextWhereClause.setOperator(HfqlStatement.WhereClauseOperatorEnum.SEARCH_MATCH);
					String joinedParamValues =
							rightValues.stream().map(ParameterUtil::escape).collect(Collectors.joining(","));
					nextWhereClause.setRight(Constants.PARAM_ID, joinedParamValues);
				} else if (isFhirPathExpressionEquivalent("meta.lastUpdated", left, fromResourceName)) {
					// This is an expression for Resource.meta.lastUpdated
					nextWhereClause.setLeft("id");
					nextWhereClause.setOperator(HfqlStatement.WhereClauseOperatorEnum.SEARCH_MATCH);
					String joinedParamValues = rightValues.stream()
							.map(value -> comparator + ParameterUtil.escape(value))
							.collect(Collectors.joining(","));
					nextWhereClause.setRight(Constants.PARAM_LASTUPDATED, joinedParamValues);
				}
			}
		}
	}

	private void addHfqlWhereClausesToSearchParameterMap(HfqlStatement statement, SearchParameterMap map) {
		List<HfqlStatement.WhereClause> searchClauses = statement.getWhereClauses();
		for (HfqlStatement.WhereClause nextSearchClause : searchClauses) {
			if (nextSearchClause.getOperator() != HfqlStatement.WhereClauseOperatorEnum.SEARCH_MATCH) {
				continue;
			}

			if (!"id".equals(nextSearchClause.getLeft())) {
				throw new InvalidRequestException(
						Msg.code(2412) + "search_match function can only be applied to the id element");
			}

			if (nextSearchClause.getRight().size() != 2) {
				throw new InvalidRequestException(Msg.code(2413) + "search_match function requires 2 arguments");
			}

			List<String> argumentStrings = nextSearchClause.getRightAsStrings();
			String paramName = argumentStrings.get(0);
			String paramValueUnsplit = argumentStrings.get(1);
			List<String> paramValues = QualifiedParamList.splitQueryStringByCommasIgnoreEscape(null, paramValueUnsplit);

			if (paramName.equals(Constants.PARAM_ID)) {
				map.add(Constants.PARAM_ID, new TokenOrListParam(null, paramValues.toArray(EMPTY_STRING_ARRAY)));
			} else if (paramName.equals(Constants.PARAM_LASTUPDATED)) {
				DateOrListParam param = new DateOrListParam();
				for (String nextValue : paramValues) {
					param.addOr(new DateParam(nextValue));
				}
				map.add(Constants.PARAM_LASTUPDATED, param);
			} else if (paramName.startsWith("_")) {
				throw newInvalidRequestExceptionUnknownSearchParameter(paramName);
			} else {
				QualifierDetails qualifiedParamName = QualifierDetails.extractQualifiersFromParameterName(paramName);

				RuntimeSearchParam searchParam = mySearchParamRegistry.getActiveSearchParam(
						statement.getFromResourceName(), qualifiedParamName.getParamName());
				if (searchParam == null) {
					throw newInvalidRequestExceptionUnknownSearchParameter(paramName);
				}

				QualifiedParamList values = new QualifiedParamList();
				values.setQualifier(qualifiedParamName.getWholeQualifier());
				values.addAll(paramValues);
				IQueryParameterAnd<?> andParam = JpaParamUtil.parseQueryParams(
						myFhirContext, searchParam.getParamType(), paramName, List.of(values));
				map.add(qualifiedParamName.getParamName(), andParam);
			}
		}
	}

	private IHfqlExecutionResult createOrderedResult(
			HfqlStatement theStatement, IHfqlExecutionResult theExecutionResult) {
		List<IHfqlExecutionResult.Row> rows = new ArrayList<>();
		while (theExecutionResult.hasNext()) {
			IHfqlExecutionResult.Row nextRow = theExecutionResult.getNextRow();
			rows.add(nextRow);
			Validate.isTrue(
					rows.size() <= HfqlConstants.ORDER_AND_GROUP_LIMIT,
					"Can not ORDER BY result sets over %d results",
					HfqlConstants.ORDER_AND_GROUP_LIMIT);
		}

		List<Integer> orderColumnIndexes = theStatement.getOrderByClauses().stream()
				.map(t -> {
					int index = theStatement.findSelectClauseIndex(t.getClause());
					if (index == -1) {
						throw new InvalidRequestException(
								Msg.code(2407) + "Invalid/unknown ORDER BY clause: " + t.getClause());
					}
					return index;
				})
				.collect(Collectors.toList());
		List<Boolean> orderAscending = theStatement.getOrderByClauses().stream()
				.map(HfqlStatement.OrderByClause::isAscending)
				.collect(Collectors.toList());

		Comparator<IHfqlExecutionResult.Row> comparator = null;
		for (int i = 0; i < orderColumnIndexes.size(); i++) {
			int columnIndex = orderColumnIndexes.get(i);
			HfqlDataTypeEnum dataType = theExecutionResult
					.getStatement()
					.getSelectClauses()
					.get(columnIndex)
					.getDataType();
			Comparator<IHfqlExecutionResult.Row> nextComparator = newRowComparator(columnIndex, dataType);
			if (!orderAscending.get(i)) {
				nextComparator = nextComparator.reversed();
			}
			if (comparator == null) {
				comparator = nextComparator;
			} else {
				comparator = comparator.thenComparing(nextComparator);
			}
		}

		rows.sort(comparator);
		for (int i = 0; i < rows.size(); i++) {
			rows.set(i, rows.get(i).toRowOffset(i));
		}

		List<List<Object>> rowData =
				rows.stream().map(IHfqlExecutionResult.Row::getRowValues).collect(Collectors.toList());
		return new StaticHfqlExecutionResult(null, theStatement, rowData);
	}

	@Override
	public IHfqlExecutionResult executeContinuation(
			HfqlStatement theStatement,
			String theSearchId,
			int theStartingOffset,
			Integer theLimit,
			RequestDetails theRequestDetails) {
		IBundleProvider resultList = myPagingProvider.retrieveResultList(theRequestDetails, theSearchId);
		HfqlExecutionContext executionContext = new HfqlExecutionContext(myFhirContext.newFhirPath());
		Predicate<IBaseResource> whereClausePredicate = newWhereClausePredicate(executionContext, theStatement);
		return new LocalSearchHfqlExecutionResult(
				theStatement,
				resultList,
				executionContext,
				theLimit,
				theStartingOffset,
				whereClausePredicate,
				myFhirContext);
	}

	private IHfqlExecutionResult executeCountClause(
			HfqlStatement theStatement,
			HfqlExecutionContext theExecutionContext,
			IBundleProvider theOutcome,
			Predicate<IBaseResource> theWhereClausePredicate) {

		Set<String> selectClauses = theStatement.getSelectClauses().stream()
				.filter(t -> t.getOperator() == HfqlStatement.SelectClauseOperator.SELECT)
				.map(HfqlStatement.SelectClause::getClause)
				.collect(Collectors.toSet());
		for (String next : selectClauses) {
			if (!theStatement.getGroupByClauses().contains(next)) {
				throw newInvalidRequestCountWithSelectOnNonGroupedClause(next);
			}
		}
		Set<String> countClauses = theStatement.getSelectClauses().stream()
				.filter(t -> t.getOperator() == HfqlStatement.SelectClauseOperator.COUNT)
				.map(HfqlStatement.SelectClause::getClause)
				.collect(Collectors.toSet());

		Map<GroupByKey, Map<String, AtomicInteger>> keyCounter = new HashMap<>();

		int offset = 0;
		int batchSize = 1000;
		while (theOutcome.size() == null || theOutcome.sizeOrThrowNpe() > offset) {
			List<IBaseResource> resources = theOutcome.getResources(offset, offset + batchSize);

			for (IBaseResource nextResource : resources) {

				if (nextResource != null && theWhereClausePredicate.test(nextResource)) {

					List<List<String>> groupByClauseValues = new ArrayList<>();

					for (String nextClause : theStatement.getGroupByClauses()) {
						List<String> nextClauseValues =
								theExecutionContext.evaluate(nextResource, nextClause, IPrimitiveType.class).stream()
										.map(IPrimitiveType::getValueAsString)
										.collect(Collectors.toList());
						if (nextClauseValues.isEmpty()) {
							nextClauseValues.add(null);
						}
						groupByClauseValues.add(nextClauseValues);
					}
					Set<GroupByKey> allKeys = createCrossProduct(groupByClauseValues);

					for (GroupByKey nextKey : allKeys) {

						Map<String, AtomicInteger> counts = keyCounter.computeIfAbsent(nextKey, t -> new HashMap<>());
						if (keyCounter.size() >= HfqlConstants.ORDER_AND_GROUP_LIMIT) {
							throw new InvalidRequestException(Msg.code(2402) + "Can not group on > "
									+ HfqlConstants.ORDER_AND_GROUP_LIMIT + " terms");
						}
						for (String nextCountClause : countClauses) {
							if (!nextCountClause.equals("*")) {
								if (theExecutionContext
										.evaluateFirst(nextResource, nextCountClause, IBase.class)
										.isEmpty()) {
									continue;
								}
							}
							counts.computeIfAbsent(nextCountClause, k -> new AtomicInteger())
									.incrementAndGet();
						}
					}
				}
			}

			offset += batchSize;
		}

		List<List<Object>> rows = new ArrayList<>();
		for (Map.Entry<GroupByKey, Map<String, AtomicInteger>> nextEntry : keyCounter.entrySet()) {
			List<Object> nextRow = new ArrayList<>();

			for (HfqlStatement.SelectClause nextSelectClause : theStatement.getSelectClauses()) {
				if (nextSelectClause.getOperator() == HfqlStatement.SelectClauseOperator.SELECT) {
					int groupByIndex = theStatement.getGroupByClauses().indexOf(nextSelectClause.getClause());
					nextRow.add(nextEntry.getKey().getNames().get(groupByIndex));
				} else {
					AtomicInteger counter = nextEntry.getValue().get(nextSelectClause.getClause());
					if (counter != null) {
						nextRow.add(counter.intValue());
					} else {
						nextRow.add(0);
					}
				}
			}

			rows.add(nextRow);
		}

		return new StaticHfqlExecutionResult(null, theStatement, rows);
	}

	private Set<GroupByKey> createCrossProduct(List<List<String>> theGroupByClauseValues) {
		if (theGroupByClauseValues.isEmpty()) {
			return NULL_GROUP_BY_KEY;
		}
		Set<GroupByKey> retVal = new HashSet<>();
		List<String> valueHolder = new ArrayList<>();
		createCrossProductRecurse(theGroupByClauseValues, retVal, valueHolder);
		return retVal;
	}

	private void createCrossProductRecurse(
			List<List<String>> theGroupByClauseValues,
			Set<GroupByKey> theGroupsSetToPopulate,
			List<String> theCurrentValueChain) {
		List<String> nextOptions = theGroupByClauseValues.get(0);
		for (String nextOption : nextOptions) {
			theCurrentValueChain.add(nextOption);

			if (theGroupByClauseValues.size() == 1) {
				theGroupsSetToPopulate.add(new GroupByKey(theCurrentValueChain));
			} else {
				createCrossProductRecurse(
						theGroupByClauseValues.subList(1, theGroupByClauseValues.size()),
						theGroupsSetToPopulate,
						theCurrentValueChain);
			}

			theCurrentValueChain.remove(theCurrentValueChain.size() - 1);
		}
	}

	private Predicate<IBaseResource> newWhereClausePredicate(
			HfqlExecutionContext theExecutionContext, HfqlStatement theStatement) {
		return r -> {
			for (HfqlStatement.WhereClause nextWhereClause : theStatement.getWhereClauses()) {

				boolean haveMatch;
				try {
					switch (nextWhereClause.getOperator()) {
						case SEARCH_MATCH:
							// These are handled earlier so we don't need to test here
							haveMatch = true;
							break;
						case UNARY_BOOLEAN: {
							haveMatch = evaluateWhereClauseUnaryBoolean(theExecutionContext, r, nextWhereClause);
							break;
						}
						case EQUALS:
						case IN:
						default: {
							haveMatch = evaluateWhereClauseBinaryEqualsOrIn(theExecutionContext, r, nextWhereClause);
							break;
						}
					}
				} catch (FhirPathExecutionException e) {
					String expression =
							nextWhereClause.getOperator() == HfqlStatement.WhereClauseOperatorEnum.UNARY_BOOLEAN
									? nextWhereClause.asUnaryExpression()
									: nextWhereClause.getLeft();
					throw new InvalidRequestException(Msg.code(2403) + "Unable to evaluate FHIRPath expression \""
							+ expression + "\". Error: " + e.getMessage());
				}

				if (!haveMatch) {
					return false;
				}
			}

			return true;
		};
	}

	private void populateSelectColumnDataTypes(HfqlStatement statement) {
		HfqlFhirPathParser fhirPathParser = new HfqlFhirPathParser(myFhirContext);
		for (HfqlStatement.SelectClause nextSelectClause : statement.getSelectClauses()) {
			HfqlDataTypeEnum nextType;
			if (nextSelectClause.getOperator() == HfqlStatement.SelectClauseOperator.COUNT) {
				nextType = HfqlDataTypeEnum.INTEGER;
			} else {
				String clause = nextSelectClause.getClause();
				if (clause.equals("meta.versionId")) {
					// FHIR's versionId field is a string, but in HAPI FHIR JPA it can only ever be a long so we'll
					// use that type
					nextType = HfqlDataTypeEnum.LONGINT;
				} else {
					nextType = fhirPathParser.determineDatatypeForPath(statement.getFromResourceName(), clause);
					nextType = defaultIfNull(nextType, HfqlDataTypeEnum.STRING);
				}
			}
			nextSelectClause.setDataType(nextType);
		}
	}

	/**
	 * This method replaces a SELECT-ed column named "*" with a collection of
	 * available column names for the given resource type.
	 */
	private void massageSelectColumnNames(HfqlStatement theHfqlStatement) {

		List<HfqlStatement.SelectClause> selectClauses = theHfqlStatement.getSelectClauses();
		for (int i = 0; i < selectClauses.size(); i++) {
			HfqlStatement.SelectClause selectClause = selectClauses.get(i);
			if (selectClause.getOperator() == HfqlStatement.SelectClauseOperator.SELECT) {
				if ("*".equals(selectClause.getClause())) {
					resolveAndReplaceStarInSelectClauseAtIndex(theHfqlStatement, selectClauses, i);
				}
			}
		}
	}

	private void resolveAndReplaceStarInSelectClauseAtIndex(
			HfqlStatement theHfqlStatement, List<HfqlStatement.SelectClause> theSelectClauses, int theIndex) {
		String resourceName = theHfqlStatement.getFromResourceName();
		TreeSet<String> allLeafPaths = findLeafPaths(resourceName);

		theSelectClauses.remove(theIndex);
		List<String> reversedLeafPaths = new ArrayList<>(allLeafPaths);
		reversedLeafPaths = Lists.reverse(reversedLeafPaths);
		reversedLeafPaths.forEach(t -> theSelectClauses.add(theIndex, new HfqlStatement.SelectClause(t).setAlias(t)));
	}

	@Nonnull
	private TreeSet<String> findLeafPaths(String theResourceName) {
		TreeSet<String> allLeafPaths = new TreeSet<>();
		RuntimeResourceDefinition def = myFhirContext.getResourceDefinition(theResourceName);
		for (BaseRuntimeChildDefinition nextChild : def.getChildren()) {
			for (String next : nextChild.getValidChildNames()) {
				if (!"extension".equals(next) && !"modifierExtension".equals(next)) {
					allLeafPaths.add(next);
				}
			}
		}
		return allLeafPaths;
	}

	/**
	 * Columns to return, per {@link java.sql.DatabaseMetaData#getTables(String, String, String, String[])}
	 * <OL>
	 * <LI><B>TABLE_CAT</B> String {@code =>} table catalog (may be {@code null})
	 * <LI><B>TABLE_SCHEM</B> String {@code =>} table schema (may be {@code null})
	 * <LI><B>TABLE_NAME</B> String {@code =>} table name
	 * <LI><B>TABLE_TYPE</B> String {@code =>} table type.  Typical types are "TABLE",
	 * "VIEW", "SYSTEM TABLE", "GLOBAL TEMPORARY",
	 * "LOCAL TEMPORARY", "ALIAS", "SYNONYM".
	 * <LI><B>REMARKS</B> String {@code =>} explanatory comment on the table (may be {@code null})
	 * <LI><B>TYPE_CAT</B> String {@code =>} the types catalog (may be {@code null})
	 * <LI><B>TYPE_SCHEM</B> String {@code =>} the types schema (may be {@code null})
	 * <LI><B>TYPE_NAME</B> String {@code =>} type name (may be {@code null})
	 * <LI><B>SELF_REFERENCING_COL_NAME</B> String {@code =>} name of the designated
	 * "identifier" column of a typed table (may be {@code null})
	 * <LI><B>REF_GENERATION</B> String {@code =>} specifies how values in
	 * SELF_REFERENCING_COL_NAME are created. Values are
	 * "SYSTEM", "USER", "DERIVED". (may be {@code null})
	 * </OL>
	 */
	@Override
	public IHfqlExecutionResult introspectTables() {
		List<String> columns = List.of(
				"TABLE_CAT",
				"TABLE_SCHEM",
				"TABLE_NAME",
				"TABLE_TYPE",
				"REMARKS",
				"TYPE_CAT",
				"TYPE_SCHEM",
				"TYPE_NAME",
				"SELF_REFERENCING_COL_NAME",
				"REF_GENERATION");
		List<HfqlDataTypeEnum> dataTypes = List.of(
				HfqlDataTypeEnum.STRING,
				HfqlDataTypeEnum.STRING,
				HfqlDataTypeEnum.STRING,
				HfqlDataTypeEnum.STRING,
				HfqlDataTypeEnum.STRING,
				HfqlDataTypeEnum.STRING,
				HfqlDataTypeEnum.STRING,
				HfqlDataTypeEnum.STRING,
				HfqlDataTypeEnum.STRING,
				HfqlDataTypeEnum.STRING);
		List<List<Object>> rows = new ArrayList<>();

		TreeSet<String> resourceTypes = new TreeSet<>(myFhirContext.getResourceTypes());
		for (String next : resourceTypes) {
			rows.add(Lists.newArrayList(null, null, next, "TABLE", null, null, null, null, null, null));
		}

		return new StaticHfqlExecutionResult(null, columns, dataTypes, rows);
	}

	/**
	 * Columns from {@link java.sql.DatabaseMetaData#getColumns(String, String, String, String)}
	 *
	 * <OL>
	 * <LI><B>TABLE_CAT</B> String {@code =>} table catalog (may be {@code null})
	 * <LI><B>TABLE_SCHEM</B> String {@code =>} table schema (may be {@code null})
	 * <LI><B>TABLE_NAME</B> String {@code =>} table name
	 * <LI><B>COLUMN_NAME</B> String {@code =>} column name
	 * <LI><B>DATA_TYPE</B> int {@code =>} SQL type from java.sql.Types
	 * <LI><B>TYPE_NAME</B> String {@code =>} Data source dependent type name,
	 * for a UDT the type name is fully qualified
	 * <LI><B>COLUMN_SIZE</B> int {@code =>} column size.
	 * <LI><B>BUFFER_LENGTH</B> is not used.
	 * <LI><B>DECIMAL_DIGITS</B> int {@code =>} the number of fractional digits. Null is returned for data types where
	 * DECIMAL_DIGITS is not applicable.
	 * <LI><B>NUM_PREC_RADIX</B> int {@code =>} Radix (typically either 10 or 2)
	 * <LI><B>NULLABLE</B> int {@code =>} is NULL allowed.
	 * <UL>
	 * <LI> columnNoNulls - might not allow {@code NULL} values
	 * <LI> columnNullable - definitely allows {@code NULL} values
	 * <LI> columnNullableUnknown - nullability unknown
	 * </UL>
	 * <LI><B>REMARKS</B> String {@code =>} comment describing column (may be {@code null})
	 * <LI><B>COLUMN_DEF</B> String {@code =>} default value for the column, which should be interpreted as a string when the value is enclosed in single quotes (may be {@code null})
	 * <LI><B>SQL_DATA_TYPE</B> int {@code =>} unused
	 * <LI><B>SQL_DATETIME_SUB</B> int {@code =>} unused
	 * <LI><B>CHAR_OCTET_LENGTH</B> int {@code =>} for char types the
	 * maximum number of bytes in the column
	 * <LI><B>ORDINAL_POSITION</B> int {@code =>} index of column in table
	 * (starting at 1)
	 * <LI><B>IS_NULLABLE</B> String  {@code =>} ISO rules are used to determine the nullability for a column.
	 * <UL>
	 * <LI> YES           --- if the column can include NULLs
	 * <LI> NO            --- if the column cannot include NULLs
	 * <LI> empty string  --- if the nullability for the
	 * column is unknown
	 * </UL>
	 * <LI><B>SCOPE_CATALOG</B> String {@code =>} catalog of table that is the scope
	 * of a reference attribute ({@code null} if DATA_TYPE isn't REF)
	 * <LI><B>SCOPE_SCHEMA</B> String {@code =>} schema of table that is the scope
	 * of a reference attribute ({@code null} if the DATA_TYPE isn't REF)
	 * <LI><B>SCOPE_TABLE</B> String {@code =>} table name that this the scope
	 * of a reference attribute ({@code null} if the DATA_TYPE isn't REF)
	 * <LI><B>SOURCE_DATA_TYPE</B> short {@code =>} source type of a distinct type or user-generated
	 * Ref type, SQL type from java.sql.Types ({@code null} if DATA_TYPE
	 * isn't DISTINCT or user-generated REF)
	 * <LI><B>IS_AUTOINCREMENT</B> String  {@code =>} Indicates whether this column is auto incremented
	 * <UL>
	 * <LI> YES           --- if the column is auto incremented
	 * <LI> NO            --- if the column is not auto incremented
	 * <LI> empty string  --- if it cannot be determined whether the column is auto incremented
	 * </UL>
	 * <LI><B>IS_GENERATEDCOLUMN</B> String  {@code =>} Indicates whether this is a generated column
	 * <UL>
	 * <LI> YES           --- if this a generated column
	 * <LI> NO            --- if this not a generated column
	 * <LI> empty string  --- if it cannot be determined whether this is a generated column
	 * </UL>
	 * </OL>
	 *
	 * @param theTableName  The table name or null
	 * @param theColumnName The column name or null
	 */
	@Override
	public IHfqlExecutionResult introspectColumns(@Nullable String theTableName, @Nullable String theColumnName) {
		List<String> columns = List.of(
				"TABLE_CAT",
				"TABLE_SCHEM",
				"TABLE_NAME",
				"COLUMN_NAME",
				"DATA_TYPE",
				"TYPE_NAME",
				"COLUMN_SIZE",
				"BUFFER_LENGTH",
				"DECIMAL_DIGITS",
				"NUM_PREC_RADIX",
				"NULLABLE",
				"REMARKS",
				"COLUMN_DEF",
				"SQL_DATA_TYPE",
				"SQL_DATETIME_SUB",
				"CHAR_OCTET_LENGTH",
				"ORDINAL_POSITION",
				"IS_NULLABLE",
				"SCOPE_CATALOG",
				"SCOPE_SCHEMA",
				"SCOPE_TABLE",
				"SOURCE_DATA_TYPE",
				"IS_AUTOINCREMENT",
				"IS_GENERATEDCOLUMN");
		List<HfqlDataTypeEnum> dataTypes = List.of(
				HfqlDataTypeEnum.STRING, // TABLE_CAT
				HfqlDataTypeEnum.STRING, // TABLE_SCHEM
				HfqlDataTypeEnum.STRING, // TABLE_NAME
				HfqlDataTypeEnum.STRING, // COLUMN_NAME
				HfqlDataTypeEnum.INTEGER, // DATA_TYPE
				HfqlDataTypeEnum.STRING, // TYPE_NAME
				HfqlDataTypeEnum.INTEGER, // COLUMN_SIZE
				HfqlDataTypeEnum.STRING, // BUFFER_LENGTH
				HfqlDataTypeEnum.INTEGER, // DECIMAL_DIGITS
				HfqlDataTypeEnum.INTEGER, // NUM_PREC_RADIX
				HfqlDataTypeEnum.INTEGER, // NULLABLE
				HfqlDataTypeEnum.STRING, // REMARKS
				HfqlDataTypeEnum.STRING, // COLUMN_DEF
				HfqlDataTypeEnum.INTEGER, // SQL_DATA_TYPE
				HfqlDataTypeEnum.INTEGER, // SQL_DATETIME_SUB
				HfqlDataTypeEnum.INTEGER, // CHAR_OCTET_LENGTH
				HfqlDataTypeEnum.INTEGER, // ORDINAL_POSITION
				HfqlDataTypeEnum.STRING, // IS_NULLABLE
				HfqlDataTypeEnum.STRING, // SCOPE_CATALOG
				HfqlDataTypeEnum.STRING, // SCOPE_SCHEMA
				HfqlDataTypeEnum.STRING, // SCOPE_TABLE
				HfqlDataTypeEnum.STRING, // SOURCE_DATA_TYPE
				HfqlDataTypeEnum.STRING, // IS_AUTOINCREMENT
				HfqlDataTypeEnum.STRING // IS_GENERATEDCOLUMN
				);

		List<List<Object>> rows = new ArrayList<>();
		for (String nextResourceType : new TreeSet<>(myFhirContext.getResourceTypes())) {
			if (isBlank(theTableName) || theTableName.equals(nextResourceType)) {
				TreeSet<String> leafPaths = findLeafPaths(nextResourceType);
				int position = 1;
				for (String nextLeafPath : leafPaths) {
					if (isBlank(theColumnName) || theColumnName.equals(nextLeafPath)) {
						rows.add(Lists.newArrayList(
								null,
								null,
								nextResourceType,
								nextLeafPath,
								Types.VARCHAR,
								"string",
								-1,
								null,
								null,
								null,
								1, // nullable
								null,
								null,
								null,
								null,
								null,
								position++,
								"YES",
								null,
								null,
								null,
								null,
								"NO",
								"NO"));
					}
				}
			}
		}

		return new StaticHfqlExecutionResult(null, columns, dataTypes, rows);
	}

	private static boolean isFhirPathExpressionEquivalent(
			String wantedExpression, String actualExpression, String fromResourceName) {
		if (wantedExpression.equals(actualExpression)) {
			return true;
		}
		if (("Resource." + wantedExpression).equals(actualExpression)) {
			return true;
		}
		return (fromResourceName + "." + wantedExpression).equals(actualExpression);
	}

	/**
	 * Returns {@literal true} if a where clause has an operator of
	 * {@link ca.uhn.fhir.jpa.fql.parser.HfqlStatement.WhereClauseOperatorEnum#EQUALS}
	 * or
	 * {@link ca.uhn.fhir.jpa.fql.parser.HfqlStatement.WhereClauseOperatorEnum#IN}
	 */
	private static boolean isDataValueWhereClause(HfqlStatement.WhereClause next) {
		return next.getOperator() == HfqlStatement.WhereClauseOperatorEnum.EQUALS
				|| next.getOperator() == HfqlStatement.WhereClauseOperatorEnum.IN;
	}

	@SuppressWarnings("unchecked")
	static Comparator<IHfqlExecutionResult.Row> newRowComparator(int columnIndex, HfqlDataTypeEnum dataType) {
		return Comparator.comparing(new RowValueExtractor(columnIndex, dataType));
	}

	private static boolean evaluateWhereClauseUnaryBoolean(
			HfqlExecutionContext theExecutionContext, IBaseResource r, HfqlStatement.WhereClause theNextWhereClause) {
		boolean haveMatch = false;

		String fullExpression = theNextWhereClause.asUnaryExpression();

		List<IPrimitiveType> values = theExecutionContext.evaluate(r, fullExpression, IPrimitiveType.class);
		for (IPrimitiveType<?> nextValue : values) {
			if (Boolean.TRUE.equals(nextValue.getValue())) {
				haveMatch = true;
				break;
			}
		}
		return haveMatch;
	}

	private static boolean evaluateWhereClauseBinaryEqualsOrIn(
			HfqlExecutionContext theExecutionContext, IBaseResource r, HfqlStatement.WhereClause theNextWhereClause) {
		boolean haveMatch = false;
		List<IBase> values = theExecutionContext.evaluate(r, theNextWhereClause.getLeft(), IBase.class);
		for (IBase nextValue : values) {
			for (String nextRight : theNextWhereClause.getRight()) {
				String expression = "$this = " + nextRight;
				IPrimitiveType outcome = theExecutionContext
						.evaluateFirst(nextValue, expression, IPrimitiveType.class)
						.orElseThrow(IllegalStateException::new);
				Boolean value = (Boolean) outcome.getValue();
				haveMatch = value;
				if (haveMatch) {
					break;
				}
			}
			if (haveMatch) {
				break;
			}
		}
		return haveMatch;
	}

	@Nonnull
	private static InvalidRequestException newInvalidRequestExceptionUnknownSearchParameter(String theParamName) {
		return new InvalidRequestException(
				"Unknown/unsupported search parameter: " + UrlUtil.sanitizeUrlPart(theParamName));
	}

	@Nonnull
	private static InvalidRequestException newInvalidRequestCountWithSelectOnNonGroupedClause(String theClause) {
		return new InvalidRequestException(
				"Unable to select on non-grouped column in a count expression: " + UrlUtil.sanitizeUrlPart(theClause));
	}

	private static class RowValueExtractor implements Function<IHfqlExecutionResult.Row, Comparable> {
		private final int myColumnIndex;
		private final HfqlDataTypeEnum myDataType;

		public RowValueExtractor(int theColumnIndex, HfqlDataTypeEnum theDataType) {
			myColumnIndex = theColumnIndex;
			myDataType = theDataType;
		}

		@Override
		public Comparable apply(IHfqlExecutionResult.Row theRow) {
			Comparable retVal = (Comparable) theRow.getRowValues().get(myColumnIndex);
			switch (myDataType) {
				case STRING:
				case TIME:
				case JSON:
					retVal = defaultIfNull(retVal, "");
					break;
				case LONGINT:
				case INTEGER:
					if (retVal instanceof Number) {
						return retVal;
					} else if (retVal == null) {
						retVal = Long.MIN_VALUE;
					} else {
						retVal = Long.parseLong((String) retVal);
					}
					break;
				case BOOLEAN:
					if (retVal == null) {
						retVal = Boolean.FALSE;
					} else {
						retVal = Boolean.parseBoolean((String) retVal);
					}
					break;
				case DATE:
				case TIMESTAMP:
					if (retVal != null) {
						retVal = new DateTimeType((String) retVal).getValue();
					}
					if (retVal == null) {
						retVal = new Date(Long.MIN_VALUE);
					}
					break;
				case DECIMAL:
					if (retVal == null) {
						retVal = BigDecimal.valueOf(Long.MIN_VALUE);
					} else {
						retVal = new BigDecimal((String) retVal);
					}
					break;
			}
			return retVal;
		}
	}

	private static class GroupByKey {
		private final int myHashCode;
		private List<String> myNames;

		/**
		 * @param theNames A copy of the list will be stored
		 */
		public GroupByKey(List<String> theNames) {
			myNames = new ArrayList<>(theNames);

			HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
			myNames.forEach(hashCodeBuilder::append);
			myHashCode = hashCodeBuilder.toHashCode();
		}

		@Override
		public boolean equals(Object theO) {
			boolean retVal = false;
			if (theO instanceof GroupByKey) {
				List<String> otherNames = ((GroupByKey) theO).myNames;
				retVal = ListUtils.isEqualList(myNames, otherNames);
			}
			return retVal;
		}

		@Override
		public int hashCode() {
			return myHashCode;
		}

		public List<String> getNames() {
			return myNames;
		}
	}

	public static class HfqlExecutionContext {

		private final Map<String, IFhirPath.IParsedExpression> myFhirPathExpressionMap = new HashMap<>();
		private final IFhirPath myFhirPath;

		public HfqlExecutionContext(IFhirPath theFhirPath) {
			myFhirPath = theFhirPath;
		}

		public <T extends IBase> List<T> evaluate(IBase theInput, String thePath, Class<T> theReturnType) {
			IFhirPath.IParsedExpression parsedExpression = getParsedExpression(thePath);
			return myFhirPath.evaluate(theInput, parsedExpression, theReturnType);
		}

		<T extends IBase> Optional<T> evaluateFirst(IBase theInput, String thePath, Class<T> theReturnType) {
			IFhirPath.IParsedExpression parsedExpression = getParsedExpression(thePath);
			return myFhirPath.evaluateFirst(theInput, parsedExpression, theReturnType);
		}

		private IFhirPath.IParsedExpression getParsedExpression(String thePath) {
			IFhirPath.IParsedExpression parsedExpression = myFhirPathExpressionMap.get(thePath);
			if (parsedExpression == null) {
				try {
					parsedExpression = myFhirPath.parse(thePath);
				} catch (Exception e) {
					throw new InvalidRequestException(Msg.code(2404) + e.getMessage(), e);
				}
				myFhirPathExpressionMap.put(thePath, parsedExpression);
			}
			return parsedExpression;
		}
	}
}
