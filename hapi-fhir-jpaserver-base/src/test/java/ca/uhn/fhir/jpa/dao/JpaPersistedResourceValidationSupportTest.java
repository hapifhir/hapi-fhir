package ca.uhn.fhir.jpa.dao;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2021 Smile CDR, Inc.
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
import ca.uhn.fhir.jpa.term.api.ITermReadSvc;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import com.github.benmanes.caffeine.cache.Cache;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.ValueSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JpaPersistedResourceValidationSupportTest {

	private FhirContext theFhirContext = FhirContext.forR4();

	@Mock private ITermReadSvc myTermReadSvc;
	@Mock private DaoRegistry myDaoRegistry;
	@Mock private Cache<String, IBaseResource> myLoadCache;
	@Mock private IFhirResourceDao<ValueSet> myValueSetResourceDao;


	private IValidationSupport testedClass =
		new JpaPersistedResourceValidationSupport(theFhirContext);

	private Class<? extends IBaseResource> myCodeSystemType = CodeSystem.class;
	private Class<? extends IBaseResource> myValueSetType = ValueSet.class;


	@BeforeEach
	public void setup() {
		ReflectionTestUtils.setField(testedClass, "myTermReadSvc", myTermReadSvc);
		ReflectionTestUtils.setField(testedClass, "myDaoRegistry", myDaoRegistry);
		ReflectionTestUtils.setField(testedClass, "myLoadCache", myLoadCache);
		ReflectionTestUtils.setField(testedClass, "myCodeSystemType", myCodeSystemType);
		ReflectionTestUtils.setField(testedClass, "myValueSetType", myValueSetType);
	}


	@Nested
	public class FetchCodeSystemTests {

		@Test
		void fetchCodeSystemMustUseForcedId() {
			when(myTermReadSvc.isLoincNotGenericUnversionedCodeSystem(anyString())).thenReturn(true);

			testedClass.fetchCodeSystem("string-containing-loinc");

			verify(myTermReadSvc, times(1)).readCodeSystemByForcedId("loinc");
			verify(myLoadCache, never()).get(anyString(), isA(Function.class));
		}


		@Test
		void fetchCodeSystemMustNotUseForcedId() {
			when(myTermReadSvc.isLoincNotGenericUnversionedCodeSystem(anyString())).thenReturn(false);

			testedClass.fetchCodeSystem("string-not-containing-l-o-i-n-c");

			verify(myTermReadSvc, never()).readCodeSystemByForcedId("loinc");
			verify(myLoadCache, times(1)).get(anyString(), isA(Function.class));
		}

	}


	@Nested
	public class FetchValueSetTests {

		@Test
		void fetchValueSetMustUseForcedId() {
			when(myTermReadSvc.isLoincNotGenericUnversionedValueSet(anyString())).thenReturn(true);
			when(myDaoRegistry.getResourceDao(ValueSet.class)).thenReturn(myValueSetResourceDao);

			ResourceNotFoundException thrown = assertThrows(
				ResourceNotFoundException.class,
				() -> testedClass.fetchValueSet("string-containing-loinc"));

			assertTrue(thrown.getMessage().contains("Couldn't find current version ValueSet for url"));
		}


		@Test
		void fetchValueSetMustNotUseForcedId() {
			when(myTermReadSvc.isLoincNotGenericUnversionedValueSet(anyString())).thenReturn(false);

			testedClass.fetchValueSet("string-not-containing-l-o-i-n-c");

			verify(myLoadCache, times(1)).get(anyString(), isA(Function.class));
		}

	}


}
