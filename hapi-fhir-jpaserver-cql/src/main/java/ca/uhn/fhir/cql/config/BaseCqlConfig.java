package ca.uhn.fhir.cql.config;

/*-
 * #%L
 * HAPI FHIR JPA Server - Clinical Quality Language
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

import ca.uhn.fhir.cql.common.provider.CqlProviderFactory;
import ca.uhn.fhir.cql.common.provider.CqlProviderLoader;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.cqframework.cql.cql2elm.model.Model;
import org.cqframework.cql.elm.execution.Library;
import org.hl7.elm.r1.VersionedIdentifier;
import org.springframework.context.annotation.Bean;

public abstract class BaseCqlConfig {

	@Bean
	CqlProviderFactory cqlProviderFactory() {
		return new CqlProviderFactory();
	}

	@Bean
	CqlProviderLoader cqlProviderLoader() {
		return new CqlProviderLoader();
	}

	@Bean(name="globalModelCache")
	Map<VersionedIdentifier, Model> globalModelCache() {
		return new ConcurrentHashMap<VersionedIdentifier, Model>();
	}

	@Bean(name="globalLibraryCache") 
	Map<org.cqframework.cql.elm.execution.VersionedIdentifier, Library> globalLibraryCache() {
		return new ConcurrentHashMap<org.cqframework.cql.elm.execution.VersionedIdentifier, Library>();
	} 
}
