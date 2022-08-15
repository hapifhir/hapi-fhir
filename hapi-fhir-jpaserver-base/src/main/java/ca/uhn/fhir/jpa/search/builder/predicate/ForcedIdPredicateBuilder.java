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
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbColumn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ForcedIdPredicateBuilder extends BaseJoiningPredicateBuilder {

	private static final Logger ourLog = LoggerFactory.getLogger(ForcedIdPredicateBuilder.class);
	private final DbColumn myColumnResourceId;
	private final DbColumn myColumnForcedId;

	/**
	 * Constructor
	 */
	public ForcedIdPredicateBuilder(SearchQueryBuilder theSearchSqlBuilder) {
		super(theSearchSqlBuilder, theSearchSqlBuilder.addTable("HFJ_FORCED_ID"));

		myColumnResourceId = getTable().addColumn("RESOURCE_PID");
		myColumnForcedId = getTable().addColumn("FORCED_ID");
	}

	@Override
	public DbColumn getResourceIdColumn() {
		return myColumnResourceId;
	}


	public DbColumn getColumnForcedId() {
		return myColumnForcedId;
	}


}

