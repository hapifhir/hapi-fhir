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

import ca.uhn.fhir.jpa.search.builder.QueryStack;
import ca.uhn.fhir.jpa.search.builder.sql.SearchQueryBuilder;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbColumn;

/**
 * This is a specialization on {@link ResourceLinkPredicateBuilder} which is a predicate builder for
 * the {@link ca.uhn.fhir.jpa.model.entity.ResourceLink HFJ_RES_LINK} table. That builder assumes a
 * forward reference (i.e. on the source columns) whereas this builder assumes a reverse reference
 * (i.e. on the target columns which are needed for the <code>_has</code> search parameter).
 */
public class ResourceLinkForHasParameterPredicateBuilder extends ResourceLinkPredicateBuilder {

	/**
	 * Constructor
	 */
	public ResourceLinkForHasParameterPredicateBuilder(
			QueryStack theQueryStack, SearchQueryBuilder theSearchSqlBuilder) {
		super(theQueryStack, theSearchSqlBuilder);
	}

	@Override
	public DbColumn getResourceTypeColumn() {
		return myColumnTargetResourceType;
	}

	@Override
	public DbColumn getPartitionIdColumn() {
		return myColumnTargetPartitionId;
	}

	@Override
	public DbColumn getResourceIdColumn() {
		return myColumnTargetResourceId;
	}
}
