/*-
 * #%L
 * HAPI FHIR JPA Server Test Utilities
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
package ca.uhn.fhir.jpa.search;

import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.dao.TestDaoSearch;
import ca.uhn.fhir.jpa.model.entity.StorageSettings;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.param.DateParam;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.param.TokenOrListParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.param.TokenParamModifier;
import ca.uhn.fhir.test.utilities.ITestDataBuilder;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public abstract class TokenSearchParameterTestCases implements ITestDataBuilder.WithSupport {

	protected static final String TYPE_SYSTEM_V2_0203 = "http://terminology.hl7.org/CodeSystem/v2-0203";

	private final ITestDataBuilder.Support myTestDataBuilder;
	private final TestDaoSearch myTestDaoSearch;
	private final JpaStorageSettings myStorageSettings;

	protected TokenSearchParameterTestCases(
			ITestDataBuilder.Support theTestDataBuilder,
			TestDaoSearch theTestDaoSearch,
			JpaStorageSettings theStorageSettings) {
		myTestDataBuilder = theTestDataBuilder;
		myTestDaoSearch = theTestDaoSearch;
		myStorageSettings = theStorageSettings;
	}

	@Override
	public Support getTestDataBuilderSupport() {
		return myTestDataBuilder;
	}

	@Nested
	public class SystemAndValue {

		@Test
		void searchIdentifier_systemAndValue_narrowsBySystem() {
			// Two patients share the same identifier VALUE but have different SYSTEMS.
			IIdType aId = createPatient(withIdentifier("http://system-a.org", "SHARED"));
			createPatient(withIdentifier("http://system-b.org", "SHARED"));

			SearchParameterMap map = SearchParameterMap.newSynchronous();
			map.add(Patient.SP_IDENTIFIER, new TokenParam("http://system-a.org", "SHARED"));

			assertThat(searchForPatientIds(map))
					.as("system+value must narrow by system, not match the same value in another system")
					.containsExactly(aId.getIdPart());
		}
	}

	@Nested
	public class SystemOnly {

		@Test
		void searchIdentifier_systemOnly_findsOnlyResourcesWithThatSystem() {
			IIdType matchId = createPatient(withIdentifier("http://hospital.org/mrn", "12345"));
			createPatient(withIdentifier("http://other.org/mrn", "67890"));

			SearchParameterMap map = SearchParameterMap.newSynchronous();
			map.add(Patient.SP_IDENTIFIER, new TokenParam("http://hospital.org/mrn", null));

			assertThat(searchForPatientIds(map))
					.as("system-only search must find only resources whose identifier has that system")
					.containsExactly(matchId.getIdPart());
		}

		@Test
		void searchCommonToken_systemOnly_findsOnlyResourcesWithThatSystem() {
			IIdType loincId = createObservation(withObservationCode("http://loinc.org", "12345-6"));
			createObservation(withObservationCode("http://snomed.info/sct", "999"));

			SearchParameterMap map = SearchParameterMap.newSynchronous();
			map.add(Observation.SP_CODE, new TokenParam("http://loinc.org", null));

			assertThat(searchForObservationIds(map))
					.as("system-only search must find only resources whose token has that system (COMMON mode)")
					.containsExactly(loincId.getIdPart());
		}

		@Test
		void searchIdentifier_systemOnly_nonExistentSystem_findsNothing() {
			createPatient(withIdentifier("http://hospital.org/mrn", "12345"));

			SearchParameterMap map = SearchParameterMap.newSynchronous();
			map.add(Patient.SP_IDENTIFIER, new TokenParam("http://never-indexed.example.org", null));

			assertThat(searchForPatientIds(map))
					.as("system-only search for a never-indexed system must find nothing")
					.isEmpty();
		}
	}

	@Nested
	public class ExplicitEmptySystem {

		@Test
		void searchIdentifier_explicitNoSystem_matchesOnlyNoSystemValue() {
			IIdType noSystemId = createPatient(withIdentifierNoSystem("MRN123"));
			IIdType withSystemId = createPatient(withIdentifier("http://hospital.org/mrn", "MRN123"));

			// identifier=|MRN123 (explicit "no system")
			SearchParameterMap map = SearchParameterMap.newSynchronous();
			map.add(Patient.SP_IDENTIFIER, new TokenParam("", "MRN123"));

			assertThat(searchForPatientIds(map))
					.as("identifier=|value must match a no-system identifier but not the same value under a system")
					.contains(noSystemId.getIdPart())
					.doesNotContain(withSystemId.getIdPart());
		}
	}

	@Nested
	public class NotModifier {

		@Test
		void searchCommonToken_notSystemOnly_excludesMatchingSystemAndKeepsEmpty() {
			IIdType withMatchingSystem = createObservation(withObservationCode("http://example.com", "value"));
			IIdType withOtherSystem = createObservation(withObservationCode("http://example2.com", "value"));
			IIdType empty = createObservation();

			// code:not=http://example.com|
			SearchParameterMap map = SearchParameterMap.newSynchronous();
			map.add(
					Observation.SP_CODE,
					new TokenParam("http://example.com", null).setModifier(TokenParamModifier.NOT));

			assertThat(searchForObservationIds(map))
					.as(
							":not system-only must exclude the matching system but keep others and resources without the token")
					.doesNotContain(withMatchingSystem.getIdPart())
					.contains(withOtherSystem.getIdPart(), empty.getIdPart());
		}

		@Test
		void searchIdentifier_notSystemOnly_excludesMatchingSystemAndKeepsEmpty() {
			IIdType withMatchingSystem = createPatient(withIdentifier("http://hospital.org/mrn", "12345"));
			IIdType withOtherSystem = createPatient(withIdentifier("http://other.org/mrn", "67890"));
			IIdType empty = createPatient();

			// identifier:not=http://hospital.org/mrn|
			SearchParameterMap map = SearchParameterMap.newSynchronous();
			map.add(
					Patient.SP_IDENTIFIER,
					new TokenParam("http://hospital.org/mrn", null).setModifier(TokenParamModifier.NOT));

			assertThat(searchForPatientIds(map))
					.as(
							":not system-only must exclude the matching system but keep others and resources without the token")
					.doesNotContain(withMatchingSystem.getIdPart())
					.contains(withOtherSystem.getIdPart(), empty.getIdPart());
		}

		@Test
		void searchIdentifier_notSystemAndValue_excludesOnlyExactSystemAndValue() {
			// identifier value shared across systems; :not on one system+value must only exclude that exact pair.
			IIdType exact = createPatient(withIdentifier("http://system-a.org", "SHARED"));
			IIdType sameValueOtherSystem = createPatient(withIdentifier("http://system-b.org", "SHARED"));

			// identifier:not=http://system-a.org|SHARED
			SearchParameterMap map = SearchParameterMap.newSynchronous();
			map.add(
					Patient.SP_IDENTIFIER,
					new TokenParam("http://system-a.org", "SHARED").setModifier(TokenParamModifier.NOT));

			assertThat(searchForPatientIds(map))
					.as(
							":not system+value must exclude only the exact pair, keeping the same value under another system")
					.doesNotContain(exact.getIdPart())
					.contains(sameValueOtherSystem.getIdPart());
		}
	}

	@Nested
	public class OfType {

		private boolean myOriginalIndexIdentifierOfType;

		@BeforeEach
		void enableOfType() {
			myOriginalIndexIdentifierOfType = myStorageSettings.isIndexIdentifierOfType();
			myStorageSettings.setIndexIdentifierOfType(true);
		}

		@AfterEach
		void restoreOfType() {
			myStorageSettings.setIndexIdentifierOfType(myOriginalIndexIdentifierOfType);
		}

		@Test
		void searchIdentifier_ofType_findsResource() {
			IIdType patientId = createPatient(
					withIdentifierWithTypeCodes("http://example.com/ids", "MRN123", TYPE_SYSTEM_V2_0203, "MR"));

			// identifier:of-type=<typeSystem>|MR|MRN123
			SearchParameterMap map = SearchParameterMap.newSynchronous();
			map.add(
					Patient.SP_IDENTIFIER,
					new TokenParam(TYPE_SYSTEM_V2_0203, "MR|MRN123").setModifier(TokenParamModifier.OF_TYPE));

			assertThat(searchForPatientIds(map)).containsExactly(patientId.getIdPart());
		}

		@Test
		void searchIdentifier_ofType_withMultipleTypeCodingsFindsResourceByEitherType() {
			IIdType patientId = createPatient(
					withIdentifierWithTypeCodes("http://example.com/ids", "MRN123", TYPE_SYSTEM_V2_0203, "MR", "SS"));

			// Search by MR type - should find.
			SearchParameterMap mrMap = SearchParameterMap.newSynchronous();
			mrMap.add(
					Patient.SP_IDENTIFIER,
					new TokenParam(TYPE_SYSTEM_V2_0203, "MR|MRN123").setModifier(TokenParamModifier.OF_TYPE));
			assertThat(searchForPatientIds(mrMap)).as("should find by MR type").containsExactly(patientId.getIdPart());

			// Search by SS type - should also find.
			SearchParameterMap ssMap = SearchParameterMap.newSynchronous();
			ssMap.add(
					Patient.SP_IDENTIFIER,
					new TokenParam(TYPE_SYSTEM_V2_0203, "SS|MRN123").setModifier(TokenParamModifier.OF_TYPE));
			assertThat(searchForPatientIds(ssMap)).as("should find by SS type").containsExactly(patientId.getIdPart());
		}
	}

	@Nested
	public class Missing {

		private StorageSettings.IndexEnabledEnum myOriginalIndexMissingFields;

		@BeforeEach
		void captureIndexMissingFields() {
			myOriginalIndexMissingFields = myStorageSettings.getIndexMissingFields();
		}

		@AfterEach
		void restoreIndexMissingFields() {
			myStorageSettings.setIndexMissingFields(myOriginalIndexMissingFields);
		}

		@Test
		void tokenMissing_worksWithIndexMissingFieldsEnabled() {
			myStorageSettings.setIndexMissingFields(StorageSettings.IndexEnabledEnum.ENABLED);

			// Patient with identifier AND birthdate.
			createPatient(withIdentifier("http://sys", "ID1"), withBirthdate("2000-01-01"));
			// Patient with identifier but NO birthdate.
			createPatient(withIdentifier("http://sys", "ID2"));
			// Patient without identifier but WITH birthdate.
			IIdType id3 = createPatient(withBirthdate("1990-05-15"));

			// identifier:missing=true AND birthdate:missing=false -> only the patient with no identifier + a birthdate.
			SearchParameterMap map = SearchParameterMap.newSynchronous();
			map.add(Patient.SP_IDENTIFIER, new TokenParam().setMissing(true));
			map.add(Patient.SP_BIRTHDATE, new DateParam().setMissing(false));

			assertThat(searchForPatientIds(map))
					.as("Should find patient with birthdate but no identifier")
					.containsExactly(id3.getIdPart());
		}

		@Test
		void tokenMissing_worksWithIndexMissingFieldsDisabled() {
			// legacy HFJ_SPIDX_TOKEN needs the missing-field index, so skip the test for legacy.
			Assumptions.assumeTrue(
					myStorageSettings.getTokenIndexStrategy().readFromCompressedTokenTables(),
					":missing with IndexMissingFields disabled is only supported when querying compressed token tables");

			myStorageSettings.setIndexMissingFields(StorageSettings.IndexEnabledEnum.DISABLED);

			// p1: has identifier (IDENTIFIER mode) AND gender (COMMON mode).
			IIdType id1 = createPatient(withIdentifier("http://sys", "ID1"), withGender("male"));
			// p2: has neither identifier nor gender.
			IIdType id2 = createPatient(withFamily("NoTokens"));

			// IDENTIFIER mode.
			assertThat(searchForPatientIdsByMissingToken(Patient.SP_IDENTIFIER, true))
					.as("identifier:missing=true -> only the patient without an identifier")
					.containsExactly(id2.getIdPart());
			assertThat(searchForPatientIdsByMissingToken(Patient.SP_IDENTIFIER, false))
					.as("identifier:missing=false -> only the patient with an identifier")
					.containsExactly(id1.getIdPart());

			// COMMON mode.
			assertThat(searchForPatientIdsByMissingToken(Patient.SP_GENDER, true))
					.as("gender:missing=true -> only the patient without a gender")
					.containsExactly(id2.getIdPart());
			assertThat(searchForPatientIdsByMissingToken(Patient.SP_GENDER, false))
					.as("gender:missing=false -> only the patient with a gender")
					.containsExactly(id1.getIdPart());
		}

		private List<String> searchForPatientIdsByMissingToken(String theParamName, boolean theMissing) {
			SearchParameterMap map = SearchParameterMap.newSynchronous();
			map.add(theParamName, new TokenParam().setMissing(theMissing));
			return searchForPatientIds(map);
		}
	}

	@Nested
	public class FilterNe {

		private boolean myOriginalFilterParameterEnabled;

		@BeforeEach
		void enableFilter() {
			myOriginalFilterParameterEnabled = myStorageSettings.isFilterParameterEnabled();
			myStorageSettings.setFilterParameterEnabled(true);
		}

		@AfterEach
		void restoreFilter() {
			myStorageSettings.setFilterParameterEnabled(myOriginalFilterParameterEnabled);
		}

		@Test
		void filterNe_commonSystemOnly() {
			IIdType inSystem = createObservation(withObservationCode("http://example.com", "value"));
			IIdType otherSystem = createObservation(withObservationCode("http://example2.com", "value"));

			SearchParameterMap map = SearchParameterMap.newSynchronous();
			map.add(Constants.PARAM_FILTER, new StringParam("code ne http://example.com|"));

			assertThat(searchForObservationIds(map))
					.as("_filter code ne system|")
					.doesNotContain(inSystem.getIdPart())
					.contains(otherSystem.getIdPart());
		}

		@Test
		void filterNe_identifierSystemOnly() {
			IIdType inSystem = createPatient(withIdentifier("http://hospital.org/mrn", "12345"));
			IIdType otherSystem = createPatient(withIdentifier("http://other.org/mrn", "67890"));

			SearchParameterMap map = SearchParameterMap.newSynchronous();
			map.add(Constants.PARAM_FILTER, new StringParam("identifier ne http://hospital.org/mrn|"));

			assertThat(searchForPatientIds(map))
					.as("_filter identifier ne system|")
					.doesNotContain(inSystem.getIdPart())
					.contains(otherSystem.getIdPart());
		}

		@Test
		void filterNe_identifierSystemAndValue() {
			IIdType exact = createPatient(withIdentifier("http://system-a.org", "SHARED"));
			IIdType sameValueOtherSystem = createPatient(withIdentifier("http://system-b.org", "SHARED"));

			SearchParameterMap map = SearchParameterMap.newSynchronous();
			map.add(Constants.PARAM_FILTER, new StringParam("identifier ne http://system-a.org|SHARED"));

			assertThat(searchForPatientIds(map))
					.as("_filter identifier ne system|value")
					.doesNotContain(exact.getIdPart())
					.contains(sameValueOtherSystem.getIdPart());
		}

		@Test
		void filterNe_identifierSystemOnly_includesResourceWithNoSystem() {
			IIdType noSystemId = createPatient(withIdentifierNoSystem("V2"));
			IIdType inSystem = createPatient(withIdentifier("http://sys-a.org", "V1"));

			SearchParameterMap map = SearchParameterMap.newSynchronous();
			map.add(Constants.PARAM_FILTER, new StringParam("identifier ne http://sys-a.org|"));

			assertThat(searchForPatientIds(map))
					.as("_filter identifier ne system| must include a resource whose identifier has no system")
					.contains(noSystemId.getIdPart())
					.doesNotContain(inSystem.getIdPart());
		}

		@Test
		void filterNe_identifierSystemAndValue_includesSameValueWithNoSystem() {
			IIdType exact = createPatient(withIdentifier("http://system-a.org", "SHARED"));
			IIdType noSystemId = createPatient(withIdentifierNoSystem("SHARED"));

			SearchParameterMap map = SearchParameterMap.newSynchronous();
			map.add(Constants.PARAM_FILTER, new StringParam("identifier ne http://system-a.org|SHARED"));

			assertThat(searchForPatientIds(map))
					.as("_filter identifier ne system|value must include the same value with no system")
					.doesNotContain(exact.getIdPart())
					.contains(noSystemId.getIdPart());
		}
	}

	@Nested
	public class OrList {

		@Test
		void searchIdentifier_systemOnlyOrList_withUnknownSystem_stillMatchesKnown() {
			IIdType known = createPatient(withIdentifier("http://a.org", "1"));

			// identifier=http://a.org|,http://nonexistent.org|
			SearchParameterMap map = SearchParameterMap.newSynchronous();
			map.add(
					Patient.SP_IDENTIFIER,
					new TokenOrListParam()
							.add(new TokenParam("http://a.org", null))
							.add(new TokenParam("http://nonexistent.org", null)));

			assertThat(searchForPatientIds(map))
					.as("OR-list with an unknown system must still match the known system")
					.contains(known.getIdPart());
		}
	}

	@Nested
	public class RoutingSearch {

		private Set<String> myOriginalIdentifierTokenSearchParams;

		@BeforeEach
		void routeCodeToIdentifier() {
			myOriginalIdentifierTokenSearchParams = myStorageSettings.getIdentifierTokenSearchParams();
			myStorageSettings.setIdentifierTokenSearchParams(Set.of("identifier", "code"));
		}

		@AfterEach
		void restoreIdentifierTokenSearchParams() {
			myStorageSettings.setIdentifierTokenSearchParams(myOriginalIdentifierTokenSearchParams);
		}

		@Test
		void searchCode_whenRoutedToIdentifierSearchParams_stillResolves() {
			IIdType id = createObservation(withObservationCode("http://loinc.org", "12345-6"));

			SearchParameterMap map = SearchParameterMap.newSynchronous();
			map.add(Observation.SP_CODE, new TokenParam("http://loinc.org", "12345-6"));

			assertThat(searchForObservationIds(map))
					.as("search by code resolves even when 'code' is configured as an identifier token search param")
					.containsExactly(id.getIdPart());
		}
	}

	private List<String> searchForPatientIds(SearchParameterMap theMap) {
		return myTestDaoSearch.searchForIds("Patient", theMap);
	}

	private List<String> searchForObservationIds(SearchParameterMap theMap) {
		return myTestDaoSearch.searchForIds("Observation", theMap);
	}

	private ITestDataBuilder.ICreationArgument withIdentifierNoSystem(String theValue) {
		return (IBase t) -> ((Patient) t).addIdentifier().setValue(theValue);
	}

	private ITestDataBuilder.ICreationArgument withIdentifierWithTypeCodes(
			String theSystem, String theValue, String theTypeSystem, String... theTypeCodes) {
		return (IBase t) -> {
			Identifier identifier =
					((Patient) t).addIdentifier().setSystem(theSystem).setValue(theValue);
			for (String typeCode : theTypeCodes) {
				identifier.getType().addCoding().setSystem(theTypeSystem).setCode(typeCode);
			}
		};
	}
}
