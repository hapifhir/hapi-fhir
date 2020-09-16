package ca.uhn.fhir.jpa.dao.search;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
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

import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.stereotype.Service;

@Service
public abstract class PredicateBuilderFactory {
	@Lookup
	public abstract PredicateBuilderCoords newPredicateBuilderCoords(SearchBuilder2 theSearchBuilder);

	@Lookup
	public abstract PredicateBuilderDate newPredicateBuilderDate(SearchBuilder2 theSearchBuilder);

	@Lookup
	public abstract PredicateBuilderNumber newPredicateBuilderNumber(SearchBuilder2 theSearchBuilder);

	@Lookup
	public abstract PredicateBuilderQuantity newPredicateBuilderQuantity(SearchBuilder2 theSearchBuilder);

	@Lookup
	public abstract PredicateBuilderReference newPredicateBuilderReference(SearchBuilder2 theSearchBuilder, PredicateBuilder thePredicateBuilder);

	@Lookup
	public abstract PredicateBuilderResourceId newPredicateBuilderResourceId(SearchBuilder2 theSearchBuilder);

	@Lookup
	public abstract PredicateBuilderString newPredicateBuilderString(SearchBuilder2 theSearchBuilder);

	@Lookup
	public abstract PredicateBuilderTag newPredicateBuilderTag(SearchBuilder2 theSearchBuilder);

	@Lookup
	public abstract PredicateBuilderToken newPredicateBuilderToken(SearchBuilder2 theSearchBuilder, PredicateBuilder thePredicateBuilder);

	@Lookup
	public abstract PredicateBuilderUri newPredicateBuilderUri(SearchBuilder2 theSearchBuilder);
}
