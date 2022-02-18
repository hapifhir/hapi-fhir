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

import ca.uhn.fhir.jpa.search.builder.sql.SearchQueryBuilder;
import com.healthmarketscience.sqlbuilder.BinaryCondition;
import com.healthmarketscience.sqlbuilder.Condition;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbColumn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SourcePredicateBuilder extends BaseJoiningPredicateBuilder {

	private static final Logger ourLog = LoggerFactory.getLogger(SourcePredicateBuilder.class);
	private final DbColumn myColumnSourceUri;
	private final DbColumn myColumnRequestId;
	private final DbColumn myResourceIdColumn;

	/**
	 * Constructor
	 */
	public SourcePredicateBuilder(SearchQueryBuilder theSearchSqlBuilder) {
		super(theSearchSqlBuilder, theSearchSqlBuilder.addTable("HFJ_RES_VER_PROV"));

		myResourceIdColumn = getTable().addColumn("RES_PID");
		myColumnSourceUri = getTable().addColumn("SOURCE_URI");
		myColumnRequestId = getTable().addColumn("REQUEST_ID");
	}


	@Override
	public DbColumn getResourceIdColumn() {
		return myResourceIdColumn;
	}

	public Condition createPredicateSourceUri(String theSourceUri) {
		return BinaryCondition.equalTo(myColumnSourceUri, generatePlaceholder(theSourceUri));
	}

	public Condition createPredicateRequestId(String theRequestId) {
		return BinaryCondition.equalTo(myColumnRequestId, generatePlaceholder(theRequestId));
	}

}
