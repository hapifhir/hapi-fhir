package ca.uhn.fhir.jpa.dao.r5.dbpartitionmode;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.ExpressionVisitorAdapter;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.ParenthesedExpressionList;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.FromItem;
import net.sf.jsqlparser.statement.select.Join;
import net.sf.jsqlparser.statement.select.LateralSubSelect;
import net.sf.jsqlparser.statement.select.ParenthesedFromItem;
import net.sf.jsqlparser.statement.select.ParenthesedSelect;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectItem;
import net.sf.jsqlparser.statement.select.SetOperationList;
import net.sf.jsqlparser.statement.update.Update;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.sql.DataSource;

/**
 * Checks SQL against the rules for joining partitioned tables (every table whose primary key includes
 * {@literal PARTITION_ID}). Databases that require partitioned tables to be joined on {@literal PARTITION_ID}, such
 * as distributed PostgreSQL, reject a query that breaks them:
 * <ul>
 * <li>A correlated subquery between two partitioned tables must match {@literal PARTITION_ID} to
 * {@literal PARTITION_ID}. Matching it to {@literal TARGET_RES_PARTITION_ID} is rejected.</li>
 * <li>Inside such a subquery, every join to a partitioned table must also match {@literal PARTITION_ID} to
 * {@literal PARTITION_ID}. Optional, because only the SQL generated for PostgreSQL follows it: other databases keep
 * such joins in chained searches with contained resource indexing.</li>
 * <li>An outer join to a partitioned table must match its {@literal PARTITION_ID} to {@literal PARTITION_ID} or
 * {@literal TARGET_RES_PARTITION_ID} of the other table.</li>
 * </ul>
 * Other inner joins are not checked, because such databases can run them as repartition joins. The rules come from
 * queries run on a distributed PostgreSQL cluster, not from its documentation; {@link PartitionIdJoinCheckerTest}
 * has an example of each.
 */
// Created by Claude Opus 5.5
class PartitionIdJoinChecker {

	private static final String PARTITION_ID = "PARTITION_ID";
	private static final String TARGET_RES_PARTITION_ID = "TARGET_RES_PARTITION_ID";

	private final Set<String> myPartitionedTables;
	private final boolean myCheckJoinsInCorrelatedSubqueries;

	/**
	 * @param theCheckJoinsInCorrelatedSubqueries whether joins inside a correlated subquery must match on
	 * 	{@literal PARTITION_ID}
	 */
	PartitionIdJoinChecker(Set<String> thePartitionedTables, boolean theCheckJoinsInCorrelatedSubqueries) {
		myPartitionedTables = thePartitionedTables.stream().map(PartitionIdJoinChecker::upper).collect(Collectors.toSet());
		myCheckJoinsInCorrelatedSubqueries = theCheckJoinsInCorrelatedSubqueries;
	}

	/**
	 * @return the partitioned tables in database partition mode: those whose primary key includes
	 * 	{@literal PARTITION_ID}
	 */
	static Set<String> loadPartitionedTables(DataSource theDataSource) throws SQLException {
		String sql = "SELECT kcu.TABLE_NAME FROM INFORMATION_SCHEMA.TABLE_CONSTRAINTS tc "
				+ "JOIN INFORMATION_SCHEMA.KEY_COLUMN_USAGE kcu "
				+ "ON kcu.CONSTRAINT_SCHEMA = tc.CONSTRAINT_SCHEMA AND kcu.CONSTRAINT_NAME = tc.CONSTRAINT_NAME "
				// PostgreSQL reports column names in lower case
				+ "WHERE tc.CONSTRAINT_TYPE = 'PRIMARY KEY' AND UPPER(kcu.COLUMN_NAME) = 'PARTITION_ID'";
		Set<String> retVal = new HashSet<>();
		try (Connection connection = theDataSource.getConnection();
				java.sql.Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery(sql)) {
			while (resultSet.next()) {
				retVal.add(upper(resultSet.getString(1)));
			}
		}
		return retVal;
	}

	/**
	 * @return one description per outer join or correlated subquery that breaks the rules; empty if none
	 * @throws JSQLParserException if the SQL cannot be parsed
	 */
	List<String> findViolations(String theSql) throws JSQLParserException {
		net.sf.jsqlparser.statement.Statement statement = CCJSqlParserUtil.parse(theSql);
		List<String> retVal = new ArrayList<>();
		if (statement instanceof Select select) {
			checkSelect(select, null, retVal);
		} else if (statement instanceof Update update) {
			Scope scope = new Scope(null);
			scope.addTable(update.getTable());
			checkLevel(scope, update.getJoins(), update.getWhere(), List.of(), retVal);
		} else if (statement instanceof Delete delete) {
			Scope scope = new Scope(null);
			scope.addTable(delete.getTable());
			checkLevel(scope, delete.getJoins(), delete.getWhere(), List.of(), retVal);
		} else if (statement instanceof Insert insert && insert.getSelect() != null) {
			checkSelect(insert.getSelect(), null, retVal);
		}
		return retVal;
	}

	private void checkSelect(Select theSelect, Scope theParent, List<String> theViolations) {
		if (theSelect instanceof PlainSelect plainSelect) {
			Scope scope = new Scope(theParent);
			addFromItem(plainSelect.getFromItem(), scope, theViolations);
			List<Expression> selectExpressions = new ArrayList<>();
			if (plainSelect.getSelectItems() != null) {
				for (SelectItem<?> item : plainSelect.getSelectItems()) {
					selectExpressions.add(item.getExpression());
				}
			}
			if (plainSelect.getHaving() != null) {
				selectExpressions.add(plainSelect.getHaving());
			}
			checkLevel(scope, plainSelect.getJoins(), plainSelect.getWhere(), selectExpressions, theViolations);
		} else if (theSelect instanceof SetOperationList setOperationList) {
			for (Select select : setOperationList.getSelects()) {
				checkSelect(select, theParent, theViolations);
			}
		} else if (theSelect instanceof ParenthesedSelect parenthesedSelect) {
			checkSelect(parenthesedSelect.getSelect(), theParent, theViolations);
		}
	}

	/**
	 * Checks one query level: its joins, the subqueries in its expressions, and, when it is itself a subquery,
	 * how it is correlated with the enclosing query.
	 */
	private void checkLevel(
			Scope theScope,
			List<Join> theJoins,
			Expression theWhere,
			List<Expression> theOtherExpressions,
			List<String> theViolations) {
		List<Join> joins = theJoins != null ? theJoins : List.of();
		for (Join join : joins) {
			addFromItem(join.getRightItem(), theScope, theViolations);
		}

		List<Expression> conditions = new ArrayList<>();
		for (Join join : joins) {
			conditions.addAll(join.getOnExpressions());
			if (join.isLeft() || join.isRight() || join.isFull() || join.isOuter()) {
				checkOuterJoin(join, theScope, theViolations);
			}
		}
		if (theWhere != null) {
			conditions.add(theWhere);
		}

		Collector collector = new Collector();
		conditions.forEach(t -> t.accept(collector, null));
		theOtherExpressions.forEach(t -> t.accept(collector, null));
		for (Select subSelect : collector.mySubSelects) {
			checkSelect(subSelect, theScope, theViolations);
		}

		if (theScope.myParent != null
				&& checkCorrelation(theScope, collector.myColumns, conditions, theViolations)
				&& myCheckJoinsInCorrelatedSubqueries) {
			checkJoinsInCorrelatedSubquery(theScope, joins, theWhere, theViolations);
		}
	}

	private void addFromItem(FromItem theFromItem, Scope theScope, List<String> theViolations) {
		if (theFromItem instanceof Table table) {
			theScope.addTable(table);
		} else if (theFromItem instanceof LateralSubSelect lateral) {
			checkSelect(lateral.getSelect(), theScope, theViolations);
			theScope.addDerived(lateral.getAlias() != null ? lateral.getAlias().getName() : null);
		} else if (theFromItem instanceof ParenthesedSelect derived) {
			// A derived table in FROM can't refer to the enclosing query, so it is checked on its own
			checkSelect(derived.getSelect(), null, theViolations);
			theScope.addDerived(derived.getAlias() != null ? derived.getAlias().getName() : null);
		} else if (theFromItem instanceof ParenthesedFromItem parenthesed) {
			addFromItem(parenthesed.getFromItem(), theScope, theViolations);
			checkLevel(theScope, parenthesed.getJoins(), null, List.of(), theViolations);
		}
	}

	private void checkOuterJoin(Join theJoin, Scope theScope, List<String> theViolations) {
		String joinedAlias = aliasOf(theJoin.getRightItem());
		String joinedTable = theScope.myAliasToTable.get(joinedAlias);
		if (!isPartitioned(joinedTable)) {
			return;
		}

		Collector collector = new Collector();
		theJoin.getOnExpressions().forEach(t -> t.accept(collector, null));
		Set<String> partitionMatches = partitionIdMatches(theJoin.getOnExpressions(), theScope, true);
		Set<String> reported = new HashSet<>();
		for (Column column : collector.myColumns) {
			String alias = qualifierOf(column, theScope);
			String table = theScope.myAliasToTable.get(alias);
			if (alias == null || alias.equals(joinedAlias) || !isPartitioned(table) || !reported.add(alias)) {
				continue;
			}
			if (!partitionMatches.contains(pairKey(joinedAlias, alias))) {
				theViolations.add("outer join of " + joinedTable + " (" + joinedAlias + ") to " + table + " (" + alias
						+ ") does not match on " + PARTITION_ID);
			}
		}
	}

	/**
	 * @return whether the subquery refers to a partitioned table of an enclosing query
	 */
	private boolean checkCorrelation(
			Scope theScope, List<Column> theColumns, List<Expression> theConditions, List<String> theViolations) {
		Set<String> innerAliases = partitionedAliases(theScope);
		if (innerAliases.isEmpty()) {
			return false;
		}

		Map<String, String> outerAliasToTable = new LinkedHashMap<>();
		for (Column column : theColumns) {
			String alias = qualifierOf(column, theScope);
			if (alias != null && !theScope.myAliasToTable.containsKey(alias)) {
				String table = theScope.myParent.findTable(alias);
				if (isPartitioned(table)) {
					outerAliasToTable.put(alias, table);
				}
			}
		}

		Set<String> partitionMatches = partitionIdMatches(theConditions, theScope, false);
		for (Map.Entry<String, String> outer : outerAliasToTable.entrySet()) {
			boolean matched = innerAliases.stream().anyMatch(t -> partitionMatches.contains(pairKey(t, outer.getKey())));
			if (!matched) {
				List<String> innerTables = innerAliases.stream()
						.map(theScope.myAliasToTable::get)
						.toList();
				theViolations.add("correlated subquery on " + innerTables + " refers to " + outer.getValue() + " ("
						+ outer.getKey() + ") without matching on " + PARTITION_ID);
			}
		}
		return !outerAliasToTable.isEmpty();
	}

	/**
	 * Such databases cannot repartition a join inside a correlated subquery, so it must match on
	 * {@literal PARTITION_ID} like the correlation itself.
	 */
	private void checkJoinsInCorrelatedSubquery(
			Scope theScope, List<Join> theJoins, Expression theWhere, List<String> theViolations) {
		Set<String> partitionedAliases = partitionedAliases(theScope);
		for (Join join : theJoins) {
			String joinedAlias = aliasOf(join.getRightItem());
			if (!partitionedAliases.contains(joinedAlias)) {
				continue;
			}
			List<Expression> conditions = new ArrayList<>(join.getOnExpressions());
			if (theWhere != null) {
				conditions.add(theWhere);
			}
			Set<String> partitionMatches = partitionIdMatches(conditions, theScope, false);
			boolean matched = partitionedAliases.stream()
					.anyMatch(t -> !t.equals(joinedAlias) && partitionMatches.contains(pairKey(t, joinedAlias)));
			if (!matched) {
				theViolations.add("join of " + theScope.myAliasToTable.get(joinedAlias) + " (" + joinedAlias
						+ ") in a correlated subquery does not match " + PARTITION_ID + " to " + PARTITION_ID);
			}
		}
	}

	private Set<String> partitionedAliases(Scope theScope) {
		return theScope.myAliasToTable.entrySet().stream()
				.filter(t -> isPartitioned(t.getValue()))
				.map(Map.Entry::getKey)
				.collect(Collectors.toCollection(LinkedHashSet::new));
	}

	/**
	 * @param theAllowTargetPartitionId whether {@code a.PARTITION_ID = b.TARGET_RES_PARTITION_ID} counts as a match
	 * @return the alias pairs whose {@literal PARTITION_ID} columns are compared for equality in a top-level
	 * 	conjunct, written either as {@code a.PARTITION_ID = b.PARTITION_ID} or inside a tuple comparison
	 */
	private Set<String> partitionIdMatches(
			Iterable<Expression> theConditions, Scope theScope, boolean theAllowTargetPartitionId) {
		Set<String> retVal = new HashSet<>();
		for (Expression condition : theConditions) {
			for (Expression conjunct : conjuncts(condition)) {
				if (conjunct instanceof EqualsTo equalsTo) {
					addPartitionIdMatches(
							equalsTo.getLeftExpression(),
							equalsTo.getRightExpression(),
							theScope,
							theAllowTargetPartitionId,
							retVal);
				}
			}
		}
		return retVal;
	}

	private void addPartitionIdMatches(
			Expression theLeft,
			Expression theRight,
			Scope theScope,
			boolean theAllowTargetPartitionId,
			Set<String> theMatches) {
		if (theLeft instanceof Column left && theRight instanceof Column right) {
			String leftName = left.getColumnName();
			String rightName = right.getColumnName();
			boolean exact = PARTITION_ID.equalsIgnoreCase(leftName) && PARTITION_ID.equalsIgnoreCase(rightName);
			boolean target = theAllowTargetPartitionId
					&& ((PARTITION_ID.equalsIgnoreCase(leftName) && TARGET_RES_PARTITION_ID.equalsIgnoreCase(rightName))
							|| (TARGET_RES_PARTITION_ID.equalsIgnoreCase(leftName)
									&& PARTITION_ID.equalsIgnoreCase(rightName)));
			if (exact || target) {
				String leftAlias = qualifierOf(left, theScope);
				String rightAlias = qualifierOf(right, theScope);
				if (leftAlias != null && rightAlias != null) {
					theMatches.add(pairKey(leftAlias, rightAlias));
				}
			}
		} else if (theLeft instanceof ExpressionList<?> left
				&& theRight instanceof ExpressionList<?> right
				&& left.size() == right.size()) {
			for (int i = 0; i < left.size(); i++) {
				addPartitionIdMatches(left.get(i), right.get(i), theScope, theAllowTargetPartitionId, theMatches);
			}
		}
	}

	private static List<Expression> conjuncts(Expression theExpression) {
		if (theExpression instanceof AndExpression and) {
			List<Expression> retVal = new ArrayList<>(conjuncts(and.getLeftExpression()));
			retVal.addAll(conjuncts(and.getRightExpression()));
			return retVal;
		}
		if (theExpression instanceof ParenthesedExpressionList<?> parenthesed && parenthesed.size() == 1) {
			return conjuncts(parenthesed.get(0));
		}
		return List.of(theExpression);
	}

	private boolean isPartitioned(String theTable) {
		return theTable != null && myPartitionedTables.contains(theTable);
	}

	private static String qualifierOf(Column theColumn, Scope theScope) {
		Table table = theColumn.getTable();
		if (table != null && table.getName() != null) {
			return upper(table.getName());
		}
		if (theScope.myAliasToTable.size() == 1) {
			return theScope.myAliasToTable.keySet().iterator().next();
		}
		return null;
	}

	private static String aliasOf(FromItem theFromItem) {
		if (theFromItem.getAlias() != null) {
			return upper(theFromItem.getAlias().getName());
		}
		if (theFromItem instanceof Table table) {
			return upper(table.getName());
		}
		return null;
	}

	private static String pairKey(String theFirst, String theSecond) {
		return theFirst.compareTo(theSecond) <= 0 ? theFirst + "|" + theSecond : theSecond + "|" + theFirst;
	}

	private static String upper(String theName) {
		return theName.replace("\"", "").toUpperCase(Locale.ROOT);
	}

	/**
	 * The tables one query level reads, by alias. A derived table maps to an empty name.
	 */
	private static class Scope {
		private final Scope myParent;
		private final Map<String, String> myAliasToTable = new LinkedHashMap<>();

		private Scope(Scope theParent) {
			myParent = theParent;
		}

		private void addTable(Table theTable) {
			if (theTable != null) {
				myAliasToTable.put(aliasOf(theTable), upper(theTable.getName()));
			}
		}

		private void addDerived(String theAlias) {
			if (theAlias != null) {
				myAliasToTable.put(upper(theAlias), "");
			}
		}

		private String findTable(String theAlias) {
			for (Scope scope = this; scope != null; scope = scope.myParent) {
				if (scope.myAliasToTable.containsKey(theAlias)) {
					return scope.myAliasToTable.get(theAlias);
				}
			}
			return null;
		}
	}

	/**
	 * Collects the columns and subqueries of an expression, without descending into the subqueries.
	 */
	private static class Collector extends ExpressionVisitorAdapter<Void> {
		private final List<Column> myColumns = new ArrayList<>();
		private final List<Select> mySubSelects = new ArrayList<>();

		@Override
		public <S> Void visit(Column theColumn, S theContext) {
			myColumns.add(theColumn);
			return null;
		}

		@Override
		public <S> Void visit(ParenthesedSelect theSelect, S theContext) {
			mySubSelects.add(theSelect);
			return null;
		}

		@Override
		public <S> Void visit(Select theSelect, S theContext) {
			mySubSelects.add(theSelect);
			return null;
		}
	}
}
