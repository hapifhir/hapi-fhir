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
package ca.uhn.fhir.jpa.search.builder.predicate;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.model.entity.PartitionablePartitionId;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedComboTokenNonUnique;
import ca.uhn.fhir.jpa.search.builder.sql.SearchQueryBuilder;
import ca.uhn.fhir.jpa.util.QueryParameterUtils;
import ca.uhn.fhir.rest.param.DateParam;
import ca.uhn.fhir.rest.param.ParamPrefixEnum;
import com.healthmarketscience.sqlbuilder.BinaryCondition;
import com.healthmarketscience.sqlbuilder.Condition;
import com.healthmarketscience.sqlbuilder.NotCondition;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbColumn;
import jakarta.annotation.Nonnull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.ObjectUtils.getIfNull;
import static org.apache.commons.lang3.StringUtils.rightPad;
import static org.apache.commons.lang3.StringUtils.substring;

public class ComboNonUniqueSearchParameterPredicateBuilder extends BaseSearchParamPredicateBuilder {

	private final DbColumn myColumnHashComplete;
	private final DbColumn myColumnDateOrdinal;

	/**
	 * Constructor
	 */
	public ComboNonUniqueSearchParameterPredicateBuilder(SearchQueryBuilder theSearchSqlBuilder) {
		super(theSearchSqlBuilder, theSearchSqlBuilder.addTable(ResourceIndexedComboTokenNonUnique.HFJ_IDX_CMB_TOK_NU));

		myColumnHashComplete = getTable().addColumn("HASH_COMPLETE");
		myColumnDateOrdinal = getTable().addColumn("DATE_ORDINAL");
	}

	public Condition createPredicateHashComplete(
			RequestPartitionId theRequestPartitionId, List<String> theIndexStrings) {
		PartitionablePartitionId partitionId =
				PartitionablePartitionId.toStoragePartition(theRequestPartitionId, getPartitionSettings());
		List<Long> hashes = theIndexStrings.stream()
				.map(t -> ResourceIndexedComboTokenNonUnique.calculateHashComplete(
						getPartitionSettings(), partitionId, t))
				.collect(Collectors.toList());
		Condition predicate =
				QueryParameterUtils.toEqualToOrInPredicate(myColumnHashComplete, generatePlaceholders(hashes));
		return combineWithRequestPartitionIdPredicate(theRequestPartitionId, predicate);
	}

	public Condition createPredicateDateParams(@Nonnull List<List<DateParam>> theDateParams) {
		List<Condition> andConditions = new ArrayList<>(theDateParams.size());
		for (List<DateParam> dateParams : theDateParams) {
			andConditions.add(createPredicateDateParamsOrList(dateParams));
		}
		return QueryParameterUtils.toAndPredicate(andConditions);
	}

	private Condition createPredicateDateParamsOrList(List<DateParam> theDateParams) {
		List<Condition> orValues = new ArrayList<>(theDateParams.size());
		for (DateParam dateParam : theDateParams) {
			String paramValue = dateParam.getValueAsString().replace("-", "");
			paramValue = substring(paramValue, 0, 8);
			paramValue = rightPad(paramValue, 8, '0');
			int ordinal = Integer.parseInt(paramValue);

			ParamPrefixEnum prefix = getIfNull(dateParam.getPrefix(), ParamPrefixEnum.EQUAL);

			int ordinalLow = 0;
			int ordinalHigh = 0;

			switch (dateParam.getPrecision()) {
				case YEAR -> {
					ordinalLow = ordinal;
					ordinalHigh = ordinal + 9999;
				}
				case MONTH -> {
					ordinalLow = ordinal;
					ordinalHigh = ordinal + 99;
				}
				case DAY -> {
					ordinalLow = ordinal;
					ordinalHigh = ordinal;
				}
				case MINUTE, SECOND, MILLI -> {
					ordinalLow = ordinal;
					ordinalHigh = ordinal;
					/*
					 * If the search was something like "?date=gt2020-01-01T12:00:00Z", we need to
					 * compare that the ordinal is greater than or equal to 20200101 since the
					 * actual dividing line is in the middle of the range denoted by the
					 * ordinal. We'll be joining on the date index table as well, and it will
					 * handle the additional specificity.
					 */
					switch (prefix) {
						case GREATERTHAN -> prefix = ParamPrefixEnum.GREATERTHAN_OR_EQUALS;
						case LESSTHAN -> prefix = ParamPrefixEnum.LESSTHAN_OR_EQUALS;
						case NOT_EQUAL -> {
							/*
							 * If we're doing a not-equal at a precision greater than date, we really can't rely on
							 * the ordinal comparison, since we might be looking for a value with a different date,
							 * or we might be looking for a value with the same date but a different time. So let's
							 * just look for anything other an day 0 (i.e everything). The subsequent comparison against
							 * the standard date index will handle the negation. This is tested in
							 * FhirResourceDaoR4ComboNonUniqueParamTest#testRangedDate_Search(...)
							 */
							ordinalLow = 0;
							ordinalHigh = 0;
						}
					}
				}
			}

			switch (prefix) {
				case EQUAL, NOT_EQUAL -> {
					Condition condition;
					if (ordinalLow == ordinalHigh) {
						condition = BinaryCondition.equalTo(myColumnDateOrdinal, generatePlaceholder(ordinalLow));
					} else {
						BinaryCondition conditionLow =
								BinaryCondition.greaterThanOrEq(myColumnDateOrdinal, generatePlaceholder(ordinalLow));
						BinaryCondition conditionHigh =
								BinaryCondition.lessThanOrEq(myColumnDateOrdinal, generatePlaceholder(ordinalHigh));
						condition = QueryParameterUtils.toAndPredicate(conditionLow, conditionHigh);
					}
					if (dateParam.getPrefix() == ParamPrefixEnum.NOT_EQUAL) {
						condition = new NotCondition(condition);
					}
					orValues.add(condition);
				}
				case GREATERTHAN -> {
					Condition conditionLow =
							BinaryCondition.greaterThan(myColumnDateOrdinal, generatePlaceholder(ordinalHigh));
					orValues.add(conditionLow);
				}
				case GREATERTHAN_OR_EQUALS -> {
					Condition conditionLow =
							BinaryCondition.greaterThanOrEq(myColumnDateOrdinal, generatePlaceholder(ordinalLow));
					orValues.add(conditionLow);
				}
				case LESSTHAN -> {
					Condition conditionHigh =
							BinaryCondition.lessThan(myColumnDateOrdinal, generatePlaceholder(ordinalLow));
					orValues.add(conditionHigh);
				}
				case LESSTHAN_OR_EQUALS -> {
					Condition conditionHigh =
							BinaryCondition.lessThanOrEq(myColumnDateOrdinal, generatePlaceholder(ordinalHigh));
					orValues.add(conditionHigh);
				}
			}
		}
		return QueryParameterUtils.toOrPredicate(orValues);
	}
}
