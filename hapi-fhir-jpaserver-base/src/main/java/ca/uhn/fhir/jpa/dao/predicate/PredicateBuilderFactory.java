package ca.uhn.fhir.jpa.dao.predicate;

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

import ca.uhn.fhir.jpa.dao.LegacySearchBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class PredicateBuilderFactory {

	private final ApplicationContext myApplicationContext;

	@Autowired
	public PredicateBuilderFactory(ApplicationContext theApplicationContext) {
		myApplicationContext = theApplicationContext;
	}

	public PredicateBuilderCoords newPredicateBuilderCoords(LegacySearchBuilder theSearchBuilder) {
		return myApplicationContext.getBean(PredicateBuilderCoords.class, theSearchBuilder);
	}

	public PredicateBuilderDate newPredicateBuilderDate(LegacySearchBuilder theSearchBuilder) {
		return myApplicationContext.getBean(PredicateBuilderDate.class, theSearchBuilder);
	}

	public PredicateBuilderNumber newPredicateBuilderNumber(LegacySearchBuilder theSearchBuilder) {
		return myApplicationContext.getBean(PredicateBuilderNumber.class, theSearchBuilder);
	}

	public PredicateBuilderQuantity newPredicateBuilderQuantity(LegacySearchBuilder theSearchBuilder) {
		return myApplicationContext.getBean(PredicateBuilderQuantity.class, theSearchBuilder);
	}

	public PredicateBuilderReference newPredicateBuilderReference(LegacySearchBuilder theSearchBuilder, PredicateBuilder thePredicateBuilder) {
		return myApplicationContext.getBean(PredicateBuilderReference.class, theSearchBuilder, thePredicateBuilder);
	}

	public PredicateBuilderResourceId newPredicateBuilderResourceId(LegacySearchBuilder theSearchBuilder) {
		return myApplicationContext.getBean(PredicateBuilderResourceId.class, theSearchBuilder);
	}

	public PredicateBuilderString newPredicateBuilderString(LegacySearchBuilder theSearchBuilder) {
		return myApplicationContext.getBean(PredicateBuilderString.class, theSearchBuilder);
	}

	public PredicateBuilderTag newPredicateBuilderTag(LegacySearchBuilder theSearchBuilder) {
		return myApplicationContext.getBean(PredicateBuilderTag.class, theSearchBuilder);
	}

	public PredicateBuilderToken newPredicateBuilderToken(LegacySearchBuilder theSearchBuilder, PredicateBuilder thePredicateBuilder) {
		return myApplicationContext.getBean(PredicateBuilderToken.class, theSearchBuilder, thePredicateBuilder);
	}

	public PredicateBuilderUri newPredicateBuilderUri(LegacySearchBuilder theSearchBuilder) {
		return myApplicationContext.getBean(PredicateBuilderUri.class, theSearchBuilder);
	}

}
