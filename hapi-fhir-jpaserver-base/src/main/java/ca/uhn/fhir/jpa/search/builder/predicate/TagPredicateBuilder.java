package ca.uhn.fhir.jpa.search.builder.predicate;

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

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.dao.BaseHapiFhirDao;
import ca.uhn.fhir.jpa.model.entity.TagTypeEnum;
import ca.uhn.fhir.jpa.search.builder.sql.SearchQueryBuilder;
import com.google.common.collect.Lists;
import com.healthmarketscience.sqlbuilder.BinaryCondition;
import com.healthmarketscience.sqlbuilder.ComboCondition;
import com.healthmarketscience.sqlbuilder.Condition;
import com.healthmarketscience.sqlbuilder.UnaryCondition;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbColumn;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbTable;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class TagPredicateBuilder extends BaseJoiningPredicateBuilder {

	private final DbColumn myColumnResId;
	private final DbTable myTagDefinitionTable;
	private final DbColumn myTagDefinitionColumnTagId;
	private final DbColumn myTagDefinitionColumnTagSystem;
	private final DbColumn myTagDefinitionColumnTagCode;
	private final DbColumn myColumnTagId;
	private final DbColumn myTagDefinitionColumnTagType;

	public TagPredicateBuilder(SearchQueryBuilder theSearchSqlBuilder) {
		super(theSearchSqlBuilder, theSearchSqlBuilder.addTable("HFJ_RES_TAG"));

		myColumnResId = getTable().addColumn("RES_ID");
		myColumnTagId = getTable().addColumn("TAG_ID");

		myTagDefinitionTable = theSearchSqlBuilder.addTable("HFJ_TAG_DEF");
		myTagDefinitionColumnTagId = myTagDefinitionTable.addColumn("TAG_ID");
		myTagDefinitionColumnTagSystem = myTagDefinitionTable.addColumn("TAG_SYSTEM");
		myTagDefinitionColumnTagCode = myTagDefinitionTable.addColumn("TAG_CODE");
		myTagDefinitionColumnTagType = myTagDefinitionTable.addColumn("TAG_TYPE");
	}


	public Condition createPredicateTag(TagTypeEnum theTagType, List<Pair<String, String>> theTokens, String theParamName, RequestPartitionId theRequestPartitionId) {
		addJoin(getTable(), myTagDefinitionTable, myColumnTagId, myTagDefinitionColumnTagId);
		return createPredicateTagList(theTagType, theTokens);
	}

	private Condition createPredicateTagList(TagTypeEnum theTagType, List<Pair<String, String>> theTokens) {
		Condition typePredicate = BinaryCondition.equalTo(myTagDefinitionColumnTagType, generatePlaceholder(theTagType.ordinal()));

		List<Condition> orPredicates = Lists.newArrayList();
		for (Pair<String, String> next : theTokens) {
			String system = next.getLeft();
			String code = next.getRight();

			if (theTagType == TagTypeEnum.PROFILE) {
				system = BaseHapiFhirDao.NS_JPA_PROFILE;
			}

			Condition codePredicate = BinaryCondition.equalTo(myTagDefinitionColumnTagCode, generatePlaceholder(code));
			if (isNotBlank(system)) {
				Condition systemPredicate = BinaryCondition.equalTo(myTagDefinitionColumnTagSystem, generatePlaceholder(system));
				orPredicates.add(ComboCondition.and(typePredicate, systemPredicate, codePredicate));
			} else {
				// Note: We don't have an index for this combo, which means that this may not perform
				// well on MySQL (and maybe others) without an added index
				orPredicates.add(ComboCondition.and(typePredicate, codePredicate));
			}
		}

		return ComboCondition.or(orPredicates.toArray(new Condition[0]));
	}

	@Override
	public DbColumn getResourceIdColumn() {
		return myColumnResId;
	}
}
