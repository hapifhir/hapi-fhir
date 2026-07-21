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
package ca.uhn.fhir.jpa.dao.index;

import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.jpa.model.entity.BaseResourceIndex;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.search.builder.predicate.BaseSearchParamPredicateBuilder;
import ca.uhn.fhir.jpa.search.builder.sql.SearchQueryBuilder;
import ca.uhn.fhir.jpa.util.AddRemoveCount;
import ca.uhn.fhir.rest.api.RestSearchParameterTypeEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import org.junit.jupiter.api.Test;
import org.springframework.core.Ordered;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class SearchParamIndexProviderRegistryTest {

	private static final SearchParamIndexRouting TOKEN =
			SearchParamIndexRouting.forParamType(RestSearchParameterTypeEnum.TOKEN);
	private static final SearchParamIndexRouting STRING =
			SearchParamIndexRouting.forParamType(RestSearchParameterTypeEnum.STRING);
	private static final SearchParamIndexRouting NUMBER =
			SearchParamIndexRouting.forParamType(RestSearchParameterTypeEnum.NUMBER);

	@Test
	void resolveProvider_noProviders_returnsEmpty() {
		SearchParamIndexProviderRegistry registry = new SearchParamIndexProviderRegistry(List.of());
		assertThat(registry.resolveProvider(TOKEN)).isEmpty();
		assertThat(registry.isBuiltInIndexWriteSuppressed(TOKEN)).isFalse();
	}

	@Test
	void nullProviders_treatedAsEmpty() {
		SearchParamIndexProviderRegistry registry = new SearchParamIndexProviderRegistry(null);
		assertThat(registry.getProviders()).isEmpty();
		assertThat(registry.resolveProvider(TOKEN)).isEmpty();
	}

	@Test
	void resolveProvider_returnsFirstHandlingProvider_sortedByOrder() {
		TestProvider low = new TestProvider(10, RestSearchParameterTypeEnum.TOKEN, false);
		TestProvider high = new TestProvider(20, RestSearchParameterTypeEnum.TOKEN, false);

		// registered out of @Order; the registry must sort them
		SearchParamIndexProviderRegistry registry = new SearchParamIndexProviderRegistry(List.of(high, low));

		assertThat(registry.getProviders()).containsExactly(low, high);
		assertThat(registry.resolveProvider(TOKEN)).containsSame(low);
	}

	@Test
	void resolveProvider_skipsNonHandlingProvider_fallsThrough() {
		TestProvider tokenOnly = new TestProvider(10, RestSearchParameterTypeEnum.TOKEN, false);
		TestProvider stringOnly = new TestProvider(20, RestSearchParameterTypeEnum.STRING, false);
		SearchParamIndexProviderRegistry registry =
				new SearchParamIndexProviderRegistry(List.of(tokenOnly, stringOnly));

		assertThat(registry.resolveProvider(STRING)).containsSame(stringOnly);
		assertThat(registry.resolveProvider(NUMBER)).isEmpty();
	}

	@Test
	void isBuiltInIndexWriteSuppressed_trueWhenAnyProviderSuppresses() {
		TestProvider nonSuppressing = new TestProvider(10, RestSearchParameterTypeEnum.TOKEN, false);
		TestProvider suppressing = new TestProvider(20, RestSearchParameterTypeEnum.TOKEN, true);

		assertThat(new SearchParamIndexProviderRegistry(List.of(nonSuppressing)).isBuiltInIndexWriteSuppressed(TOKEN))
				.isFalse();
		assertThat(new SearchParamIndexProviderRegistry(List.of(nonSuppressing, suppressing)).isBuiltInIndexWriteSuppressed(TOKEN))
				.isTrue();
	}

	private static class TestProvider implements ISearchParamIndexProvider, Ordered {
		private final int myOrder;
		private final RestSearchParameterTypeEnum myType;
		private final boolean mySuppress;

		TestProvider(int theOrder, RestSearchParameterTypeEnum theType, boolean theSuppress) {
			myOrder = theOrder;
			myType = theType;
			mySuppress = theSuppress;
		}

		@Override
		public int getOrder() {
			return myOrder;
		}

		@Override
		public boolean supports(SearchParamIndexRouting theRouting) {
			return theRouting.getParamType() == myType;
		}

		@Override
		public Optional<BaseSearchParamPredicateBuilder> createPredicateBuilder(
				SearchQueryBuilder theSqlBuilder, String theParamName) {
			return Optional.empty();
		}

		@Override
		public AddRemoveCount synchronize(
				RequestDetails theRequestDetails,
				Collection<? extends BaseResourceIndex> theExtractedParams,
				ResourceTable theEntity,
				boolean theResourceIsBeingCreated) {
			return new AddRemoveCount();
		}

		@Override
		public void deleteByResourceId(JpaPid theResourceId) {
			// no-op test provider
		}

		@Override
		public boolean suppressesBuiltInIndex(SearchParamIndexRouting theRouting) {
			return mySuppress;
		}
	}
}
