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
import com.healthmarketscience.sqlbuilder.BinaryCondition;
import com.healthmarketscience.sqlbuilder.ComboCondition;
import com.healthmarketscience.sqlbuilder.Condition;
import com.healthmarketscience.sqlbuilder.InCondition;
import com.healthmarketscience.sqlbuilder.NotCondition;
import com.healthmarketscience.sqlbuilder.Subquery;
import com.healthmarketscience.sqlbuilder.UnaryCondition;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbColumn;

import java.util.Collection;

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
public final class TuplePredicateRewriter {

	private TuplePredicateRewriter() {}

	/**
	 * Creates a NOT IN condition using the optimal strategy for the column layout.
	 * <p>
	 * For single-column (non-partition mode): {@code col NOT IN (SELECT ...)}
	 * <br>
	 * For multi-column (partition mode): {@code NOT EXISTS (SELECT 1 FROM ... WHERE s0.col1 = t0.col1 AND s0.col2 = t0.col2)}
	 *
	 * @param theSubQueryBuilder the child SQL builder
	 * @param theSubQueryPredicateBuilder the predicate builder for the subquery's root table
	 * @param theOuterJoinColumns the outer query's join columns
	 *        ({@code [PARTITION_ID, RES_ID]} in partition mode, {@code [RES_ID]} otherwise)
	 */
	public static Condition toNotInSubquery(
			SearchQueryBuilder theSubQueryBuilder,
			BaseJoiningPredicateBuilder theSubQueryPredicateBuilder,
			DbColumn[] theOuterJoinColumns) {
		if (theOuterJoinColumns.length > 1) {
			addCorrelationPredicates(theSubQueryBuilder, theSubQueryPredicateBuilder, theOuterJoinColumns);
			return new NotCondition(UnaryCondition.exists(new Subquery(theSubQueryBuilder.getSelect())));
		}
		return new InCondition(theOuterJoinColumns[0], new Subquery(theSubQueryBuilder.getSelect())).setNegate(true);
	}

	/**
	 * Creates an EXISTS condition with a correlated subquery, replacing the tuple IN
	 * pattern that is not supported by MSSQL.
	 * <p>
	 * Converts: {@code (t0.PARTITION_ID, t0.RES_ID) IN (SELECT t0.PARTITION_ID, t0.RES_ID FROM ...)}
	 * <br>
	 * To: {@code EXISTS (SELECT 1 FROM ... WHERE s0.PARTITION_ID = t0.PARTITION_ID AND s0.RES_ID = t0.RES_ID)}
	 *
	 * @param theSubQueryBuilder the child SQL builder (must use "s" alias prefix)
	 * @param theSubQueryPredicateBuilder the predicate builder for the subquery's root table
	 * @param theOuterJoinColumns the outer query's join columns
	 */
	public static Condition toExistsSubquery(
			SearchQueryBuilder theSubQueryBuilder,
			BaseJoiningPredicateBuilder theSubQueryPredicateBuilder,
			DbColumn[] theOuterJoinColumns) {
		addCorrelationPredicates(theSubQueryBuilder, theSubQueryPredicateBuilder, theOuterJoinColumns);
		return UnaryCondition.exists(new Subquery(theSubQueryBuilder.getSelect()));
	}

	private static void addCorrelationPredicates(
			SearchQueryBuilder theSubQueryBuilder,
			BaseJoiningPredicateBuilder theSubQueryPredicateBuilder,
			DbColumn[] theOuterJoinColumns) {
		theSubQueryBuilder.addPredicate(
				BinaryCondition.equalTo(theSubQueryPredicateBuilder.getPartitionIdColumn(), theOuterJoinColumns[0]));
		theSubQueryBuilder.addPredicate(BinaryCondition.equalTo(
				theSubQueryPredicateBuilder.getResourceIdColumn(),
				theOuterJoinColumns[theOuterJoinColumns.length - 1]));
	}

	/**
	 * Creates an expanded AND/OR condition replacing a tuple IN/NOT IN predicate with
	 * inline values, which is not supported by MSSQL.
	 * <p>
	 * Converts: {@code (col1, col2) IN ((v1a,v2a), (v1b,v2b))}
	 * <br>
	 * To: {@code (col1=v1a AND col2=v2a) OR (col1=v1b AND col2=v2b)}
	 *
	 * @param theSearchQueryBuilder the SQL builder for generating bind variable placeholders
	 * @param thePartitionIdColumn the PARTITION_ID column
	 * @param theResourceIdColumn the RES_ID column
	 * @param thePids the resource PIDs containing partition and resource IDs
	 * @param theNegate if true, wraps the result in a NOT condition
	 */
	public static Condition toExpandedTupleInPredicate(
			SearchQueryBuilder theSearchQueryBuilder,
			DbColumn thePartitionIdColumn,
			DbColumn theResourceIdColumn,
			Collection<JpaPid> thePids,
			boolean theNegate) {
		ComboCondition orCondition = ComboCondition.or();
		for (JpaPid pid : thePids) {
			Condition rowMatch = ComboCondition.and(
					BinaryCondition.equalTo(
							thePartitionIdColumn, theSearchQueryBuilder.generatePlaceholder(pid.getPartitionId())),
					BinaryCondition.equalTo(
							theResourceIdColumn, theSearchQueryBuilder.generatePlaceholder(pid.getId())));
			orCondition.addCondition(rowMatch);
		}

		if (theNegate) {
			return new NotCondition(orCondition);
		}
		return orCondition;
	}
}
