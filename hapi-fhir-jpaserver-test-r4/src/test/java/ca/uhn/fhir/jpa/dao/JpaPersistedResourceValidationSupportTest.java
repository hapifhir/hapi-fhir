package ca.uhn.fhir.jpa.dao;

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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.param.UriParam;
import org.hl7.fhir.r4.model.StructureDefinition;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JpaPersistedResourceValidationSupportTest {

	private FhirContext theFhirContext = FhirContext.forR4();

	@Nested
	class FetchStructureDefinitionTests {

		@Mock
		private DaoRegistry myDaoRegistry;

		@InjectMocks
		private final JpaPersistedResourceValidationSupport testClass = new JpaPersistedResourceValidationSupport(theFhirContext);

		@Captor
		ArgumentCaptor<SearchParameterMap> searchParameterMapCaptor;

		@Test
		@DisplayName("fetch StructureDefinition by version less url")
		void fetchStructureDefinitionForUrl() {
			final String profileUrl = "http://example.com/fhir/StructureDefinition/exampleProfile";
			IFhirResourceDao mockDao = mock(IFhirResourceDao.class);
			when(mockDao.search(any())).thenReturn(mock(IBundleProvider.class));
			when(myDaoRegistry.getResourceDao(anyString())).thenReturn(mockDao);

			testClass.fetchResource(StructureDefinition.class, profileUrl);

			verify(mockDao).search(searchParameterMapCaptor.capture());
			SearchParameterMap searchParams = searchParameterMapCaptor.getValue();
			String uriParam = searchParams.get(StructureDefinition.SP_URL)
				.get(0)
				.stream()
				.map(UriParam.class::cast)
				.map(UriParam::getValue)
				.findFirst()
				.orElse(null);
			assertThat(uriParam).isEqualTo(profileUrl);
		}

		@Test
		@DisplayName("fetch StructureDefinition by versioned url")
		void fetchStructureDefinitionForVersionedUrl() {
			final String profileUrl = "http://example.com/fhir/StructureDefinition/exampleProfile|1.1.0";
			IFhirResourceDao mockDao = mock(IFhirResourceDao.class);
			when(mockDao.search(any())).thenReturn(mock(IBundleProvider.class));
			when(myDaoRegistry.getResourceDao(anyString())).thenReturn(mockDao);

			testClass.fetchResource(StructureDefinition.class, profileUrl);

			verify(mockDao).search(searchParameterMapCaptor.capture());
			SearchParameterMap searchParams = searchParameterMapCaptor.getValue();
			String uriParam = searchParams.get(StructureDefinition.SP_URL)
				.get(0)
				.stream()
				.map(UriParam.class::cast)
				.map(UriParam::getValue)
				.findFirst()
				.orElse(null);
			assertThat(uriParam).isEqualTo("http://example.com/fhir/StructureDefinition/exampleProfile");

			String versionParam = searchParams.get(StructureDefinition.SP_VERSION)
				.get(0)
				.stream()
				.map(TokenParam.class::cast)
				.map(TokenParam::getValue)
				.findFirst()
				.orElse(null);
			assertThat(versionParam).isEqualTo("1.1.0");
		}
	}
}
