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

import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.search.builder.models.MissingQueryParameterPredicateParams;
import com.healthmarketscience.sqlbuilder.Condition;

public interface ICanMakeMissingParamPredicate {
	/**
	 * Creates the condition for searching for a missing field
	 * for a given SearchParameter type.
	 *
	 * Only use if {@link DaoConfig#getIndexMissingFields()} is disabled
	 */
	Condition createPredicateParamMissingValue(MissingQueryParameterPredicateParams theParams);
}
