/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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

import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.jpa.search.builder.predicate.BaseJoiningPredicateBuilder;
import ca.uhn.fhir.jpa.util.QueryParameterUtils;
import com.healthmarketscience.sqlbuilder.BinaryCondition;
import com.healthmarketscience.sqlbuilder.ComboCondition;
import com.healthmarketscience.sqlbuilder.Condition;
import com.healthmarketscience.sqlbuilder.InCondition;
import com.healthmarketscience.sqlbuilder.NotCondition;
import com.healthmarketscience.sqlbuilder.Subquery;
import com.healthmarketscience.sqlbuilder.UnaryCondition;
import org.apache.commons.lang3.Validate;

import java.util.Collection;
import java.util.List;

/**
 * Utility for building IN/NOT IN predicates that are compatible across all database
 * vendors, including those that do not support row-value constructors (e.g. MSSQL).
 * <p>
 * In database partition mode, HAPI FHIR generates tuple predicates such as
 * {@code (PARTITION_ID, RES_ID) NOT IN (SELECT ...)} which are not supported by MSSQL.
 * <p>
 * This utility provides cross-database alternatives following the same strategies used by
 * Hibernate's {@code AbstractSqlAstTranslator} when
 * {@code Dialect.supportsRowValueConstructorSyntaxInInSubQuery()} returns {@code false}:
 * @see <a href="https://github.com/hibernate/hibernate-orm/blob/main/hibernate-core/src/main/java/org/hibernate/sql/ast/spi/AbstractSqlAstTranslator.java">
 *     Hibernate AbstractSqlAstTranslator (emulateTupleComparison)</a>
 */
public final class TuplePredicateBuilder {

	private final SearchQueryBuilder mySearchQueryBuilder;

	public TuplePredicateBuilder(SearchQueryBuilder theSearchQueryBuilder) {
		mySearchQueryBuilder = theSearchQueryBuilder;
	}

	/**
	 * Creates a NOT IN condition using the optimal strategy for database partition mode.
	 * <p>
	 * For single-column (non-partition mode): {@code col NOT IN (SELECT ...)}
	 * <br>
	 * For multi-column (partition mode): {@code NOT EXISTS (SELECT 1 FROM ... WHERE s0.col1 = t0.col1 AND s0.col2 = t0.col2)}
	 *
	 * @param theSubQueryBuilder the child SQL builder
	 * @param theSubQueryPredicateBuilder the predicate builder for the subquery's root table
	 * @param theOuterJoinColumns the outer query's join columns
	 */
	public Condition toNotInSubquery(
			SearchQueryBuilder theSubQueryBuilder,
			BaseJoiningPredicateBuilder theSubQueryPredicateBuilder,
			PartitionableJoinColumns theOuterJoinColumns) {
		if (mySearchQueryBuilder.isIncludePartitionIdInJoins()) {
			addCorrelationPredicates(theSubQueryBuilder, theSubQueryPredicateBuilder, theOuterJoinColumns);
			return new NotCondition(UnaryCondition.exists(new Subquery(theSubQueryBuilder.getSelect())));
		}
		return new InCondition(
						new ColumnTupleObject(theOuterJoinColumns.getResourceIdColumn()),
						new Subquery(theSubQueryBuilder.getSelect()))
				.setNegate(true);
	}

	/**
	 * Creates an OR'd EXISTS condition from multiple child subqueries with correlated predicates
	 * on PARTITION_ID and RES_ID.
	 * <p>
	 * Produces: {@code EXISTS (SELECT ... WHERE correlated) OR EXISTS (...)}
	 *
	 * @param theChildBuilders the child SQL builders (one per subquery, must use "s" alias prefix)
	 * @param theOuterJoinColumns the outer query's join columns (must be partitioned)
	 */
	public Condition toInSubquery(
			List<SearchQueryBuilder> theChildBuilders, PartitionableJoinColumns theOuterJoinColumns) {
		Validate.isTrue(
				theOuterJoinColumns.isPartitioned(),
				"toInSubquery requires partitioned join columns with both PARTITION_ID and RES_ID");
		ComboCondition orExists = ComboCondition.or();
		for (SearchQueryBuilder childBuilder : theChildBuilders) {
			BaseJoiningPredicateBuilder childRoot = childBuilder.getOrCreateFirstPredicateBuilder();
			addCorrelationPredicates(childBuilder, childRoot, theOuterJoinColumns);
			orExists.addCondition(UnaryCondition.exists(new Subquery(childBuilder.getSelect())));
		}
		return orExists;
	}

	private static void addCorrelationPredicates(
			SearchQueryBuilder theSubQueryBuilder,
			BaseJoiningPredicateBuilder theSubQueryPredicateBuilder,
			PartitionableJoinColumns theOuterJoinColumns) {
		theSubQueryBuilder.addPredicate(BinaryCondition.equalTo(
				theSubQueryPredicateBuilder.getPartitionIdColumn(), theOuterJoinColumns.getPartitionIdColumn()));
		theSubQueryBuilder.addPredicate(BinaryCondition.equalTo(
				theSubQueryPredicateBuilder.getResourceIdColumn(), theOuterJoinColumns.getResourceIdColumn()));
	}

	/**
	 * Creates an IN/NOT IN condition for a collection of PIDs, choosing the optimal strategy
	 * based on database partition mode.
	 * <p>
	 * For multi-column (partition mode):
	 * <br>Converts: {@code (col1, col2) IN ((v1a,v2a), (v1b,v2b))}
	 * <br>To: {@code (col1=v1a AND col2=v2a) OR (col1=v1b AND col2=v2b)}
	 * <p>
	 * For single-column (non-partition mode): {@code col IN (v1, v2, ...)}
	 *
	 * @param theJoinColumns the join columns
	 * @param thePids the resource PIDs containing partition and resource IDs
	 * @param theNegate if true, wraps the result in a NOT condition
	 */
	public Condition toInPredicate(
			PartitionableJoinColumns theJoinColumns, Collection<JpaPid> thePids, boolean theNegate) {
		if (mySearchQueryBuilder.isIncludePartitionIdInJoins()) {
			return toExpandedTupleInPredicate(theJoinColumns, thePids, theNegate);
		}
		List<String> placeholders = mySearchQueryBuilder.generatePlaceholders(JpaPid.toLongList(thePids));
		return QueryParameterUtils.toEqualToOrInPredicate(
				theJoinColumns.getResourceIdColumn(), placeholders, theNegate);
	}

	private Condition toExpandedTupleInPredicate(
			PartitionableJoinColumns theJoinColumns, Collection<JpaPid> thePids, boolean theNegate) {
		ComboCondition orCondition = ComboCondition.or();
		for (JpaPid pid : thePids) {
			Condition rowMatch = ComboCondition.and(
					BinaryCondition.equalTo(
							theJoinColumns.getPartitionIdColumn(),
							mySearchQueryBuilder.generatePlaceholder(pid.getPartitionId())),
					BinaryCondition.equalTo(
							theJoinColumns.getResourceIdColumn(),
							mySearchQueryBuilder.generatePlaceholder(pid.getId())));
			orCondition.addCondition(rowMatch);
		}

		if (theNegate) {
			return new NotCondition(orCondition);
		}
		return orCondition;
	}
}
