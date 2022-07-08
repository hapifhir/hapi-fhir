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
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.entity.SearchParamPresentEntity;
import ca.uhn.fhir.jpa.search.builder.sql.SearchQueryBuilder;
import com.healthmarketscience.sqlbuilder.BinaryCondition;
import com.healthmarketscience.sqlbuilder.Condition;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbColumn;
import org.springframework.beans.factory.annotation.Autowired;

public class SearchParamPresentPredicateBuilder extends BaseJoiningPredicateBuilder {

	private final DbColumn myColumnResourceId;
	private final DbColumn myColumnHashPresence;

	@Autowired
	private PartitionSettings myPartitionSettings;

	/**
	 * Constructor
	 */
	public SearchParamPresentPredicateBuilder(SearchQueryBuilder theSearchSqlBuilder) {
		super(theSearchSqlBuilder, theSearchSqlBuilder.addTable("HFJ_RES_PARAM_PRESENT"));
		myColumnResourceId = getTable().addColumn("RES_ID");
		myColumnHashPresence = getTable().addColumn("HASH_PRESENCE");
	}

	@Override
	public DbColumn getResourceIdColumn() {
		return myColumnResourceId;
	}


	public Condition createPredicateParamMissingForReference(String theResourceName, String theParamName, boolean theMissing, RequestPartitionId theRequestPartitionId) {
		Long hash = SearchParamPresentEntity.calculateHashPresence(myPartitionSettings, theRequestPartitionId, theResourceName, theParamName, !theMissing);
		BinaryCondition predicate = BinaryCondition.equalTo(myColumnHashPresence, generatePlaceholder(hash));
		return combineWithRequestPartitionIdPredicate(theRequestPartitionId, predicate);
	}

}
