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
package ca.uhn.fhir.jpa.search.builder.predicate;

import ca.uhn.fhir.jpa.search.builder.sql.SearchQueryBuilder;

public class QuantityPredicateBuilder extends BaseQuantityPredicateBuilder {

	/**
	 * Constructor
	 */
	public QuantityPredicateBuilder(SearchQueryBuilder theSearchSqlBuilder) {
		super(theSearchSqlBuilder, theSearchSqlBuilder.addTable("HFJ_SPIDX_QUANTITY"));

		myColumnHashIdentitySystemUnits = getTable().addColumn("HASH_IDENTITY_SYS_UNITS");
		myColumnHashIdentityUnits = getTable().addColumn("HASH_IDENTITY_AND_UNITS");
		myColumnValue = getTable().addColumn("SP_VALUE");
	}
}
