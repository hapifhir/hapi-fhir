/*-
 * #%L
 * HAPI FHIR JPA Server Test Utilities
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
package ca.uhn.fhir.jpa.search;

import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.dao.TestDaoSearch;
import ca.uhn.fhir.jpa.model.entity.NormalizedQuantitySearchLevel;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.test.utilities.ITestDataBuilder;
import org.hl7.fhir.instance.model.api.IIdType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static ca.uhn.fhir.jpa.model.util.UcumServiceUtil.UCUM_CODESYSTEM_URL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public abstract class QuantitySearchParameterTestCases implements ITestDataBuilder.WithSupport {

	final Support myTestDataBuilder;
	final TestDaoSearch myTestDaoSearch;
	final JpaStorageSettings myStorageSettings;

	private IIdType myResourceId;

	protected QuantitySearchParameterTestCases(
			Support theTestDataBuilder, TestDaoSearch theTestDaoSearch, JpaStorageSettings theStorageSettings) {
		myTestDataBuilder = theTestDataBuilder;
		myTestDaoSearch = theTestDaoSearch;
		myStorageSettings = theStorageSettings;
	}

	@Override
	public Support getTestDataBuilderSupport() {
		return myTestDataBuilder;
	}

	@Nested
	public class QuantitySearch {

		/**
		 * Tests for each basic comparison prefix: https://www.hl7.org/fhir/search.html#prefix
		 */
		@Nested
		public class SimpleQueries {

			@Test
			public void noQuantityThrows() {
				String invalidQtyParam = "|http://another.org";

				assertThatThrownBy(() -> myTestDaoSearch.searchForIds("/Observation?value-quantity=" + invalidQtyParam))
						.isInstanceOf(DataFormatException.class)
						.hasMessageStartingWith("HAPI-1940: Invalid")
						.hasMessageContaining(invalidQtyParam);
			}

			@Test
			public void invalidPrefixThrows() {
				assertThatThrownBy(() -> myTestDaoSearch.searchForIds("/Observation?value-quantity=st5.35"))
						.isInstanceOf(DataFormatException.class)
						.hasMessage("HAPI-1941: Invalid prefix: \"st\"");
			}

			@Test
			public void eq() {
				withObservationWithValueQuantity(0.6);

				assertNotFind("when lt unitless", "/Observation?value-quantity=0.5");
				assertNotFind("when wrong system", "/Observation?value-quantity=0.6|http://another.org");
				assertNotFind("when wrong units", "/Observation?value-quantity=0.6||mmHg");
				assertNotFind("when gt unitless", "/Observation?value-quantity=0.7");
				assertNotFind("when gt", "/Observation?value-quantity=0.7||mmHg");

				assertFind("when eq unitless", "/Observation?value-quantity=0.6");
				assertFind("when eq with units", "/Observation?value-quantity=0.6||mm[Hg]");
			}

			@Test
			public void ne() {
				withObservationWithValueQuantity(0.6);

				assertFind("when gt", "/Observation?value-quantity=ne0.5");
				assertNotFind("when eq", "/Observation?value-quantity=ne0.6");
				assertFind("when lt", "/Observation?value-quantity=ne0.7");
			}

			@Test
			public void ap() {
				withObservationWithValueQuantity(0.6);

				assertNotFind("when gt", "/Observation?value-quantity=ap0.5");
				assertFind("when a little gt", "/Observation?value-quantity=ap0.58");
				assertFind("when eq", "/Observation?value-quantity=ap0.6");
				assertFind("when a little lt", "/Observation?value-quantity=ap0.62");
				assertNotFind("when lt", "/Observation?value-quantity=ap0.7");
			}

			@Test
			public void gt() {
				withObservationWithValueQuantity(0.6);

				assertFind("when gt", "/Observation?value-quantity=gt0.5");
				assertNotFind("when eq", "/Observation?value-quantity=gt0.6");
				assertNotFind("when lt", "/Observation?value-quantity=gt0.7");
			}

			@Test
			public void ge() {
				withObservationWithValueQuantity(0.6);

				assertFind("when gt", "/Observation?value-quantity=ge0.5");
				assertFind("when eq", "/Observation?value-quantity=ge0.6");
				assertNotFind("when lt", "/Observation?value-quantity=ge0.7");
			}

			@Test
			public void lt() {
				withObservationWithValueQuantity(0.6);

				assertNotFind("when gt", "/Observation?value-quantity=lt0.5");
				assertNotFind("when eq", "/Observation?value-quantity=lt0.6");
				assertFind("when lt", "/Observation?value-quantity=lt0.7");
			}

			@Test
			public void le() {
				withObservationWithValueQuantity(0.6);

				assertNotFind("when gt", "/Observation?value-quantity=le0.5");
				assertFind("when eq", "/Observation?value-quantity=le0.6");
				assertFind("when lt", "/Observation?value-quantity=le0.7");
			}
		}

		@Nested
		public class CombinedQueries {

			@Test
			void gtAndLt() {
				withObservationWithValueQuantity(0.6);

				assertFind("when gt0.5 and lt0.7", "/Observation?value-quantity=gt0.5&value-quantity=lt0.7");
				assertNotFind("when gt0.5 and lt0.6", "/Observation?value-quantity=gt0.5&value-quantity=lt0.6");
				assertNotFind("when gt6.5 and lt0.7", "/Observation?value-quantity=gt6.5&value-quantity=lt0.7");
				assertNotFind("impossible matching", "/Observation?value-quantity=gt0.7&value-quantity=lt0.5");
			}

			@Test
			void orClauses() {
				withObservationWithValueQuantity(0.6);

				assertFind("when gt0.5 and lt0.7", "/Observation?value-quantity=0.5,0.6");
				// make sure it doesn't find everything when using or clauses
				assertNotFind("when gt0.5 and lt0.7", "/Observation?value-quantity=0.5,0.7");
			}

			@Nested
			public class CombinedAndPlusOr {

				@Test
				void ltAndOrClauses() {
					withObservationWithValueQuantity(0.6);

					assertFind(
							"when lt0.7 and eq (0.5 or 0.6)",
							"/Observation?value-quantity=lt0.7&value-quantity=0.5,0.6");
					// make sure it doesn't find everything when using or clauses
					assertNotFind(
							"when lt0.4 and eq (0.5 or 0.6)",
							"/Observation?value-quantity=lt0.4&value-quantity=0.5,0.6");
					assertNotFind(
							"when lt0.7 and eq (0.4 or 0.5)",
							"/Observation?value-quantity=lt0.7&value-quantity=0.4,0.5");
				}

				@Test
				void gtAndOrClauses() {
					withObservationWithValueQuantity(0.6);

					assertFind(
							"when gt0.4 and eq (0.5 or 0.6)",
							"/Observation?value-quantity=gt0.4&value-quantity=0.5,0.6");
					assertNotFind(
							"when gt0.7 and eq (0.5 or 0.7)",
							"/Observation?value-quantity=gt0.7&value-quantity=0.5,0.7");
					assertNotFind(
							"when gt0.3 and eq (0.4 or 0.5)",
							"/Observation?value-quantity=gt0.3&value-quantity=0.4,0.5");
				}
			}

			@Nested
			public class QualifiedOrClauses {

				@Test
				void gtOrLt() {
					withObservationWithValueQuantity(0.6);

					assertFind("when gt0.5 or lt0.3", "/Observation?value-quantity=gt0.5,lt0.3");
					assertNotFind("when gt0.7 or lt0.55", "/Observation?value-quantity=gt0.7,lt0.55");
				}

				@Test
				void gtOrLe() {
					withObservationWithValueQuantity(0.6);

					assertFind("when gt0.5 or le0.3", "/Observation?value-quantity=gt0.5,le0.3");
					assertNotFind("when gt0.7 or le0.55", "/Observation?value-quantity=gt0.7,le0.55");
				}

				@Test
				void ltOrGt() {
					withObservationWithValueQuantity(0.6);

					assertFind("when lt0.7 or gt0.9", "/Observation?value-quantity=lt0.7,gt0.9");
					// make sure it doesn't find everything when using or clauses
					assertNotFind("when lt0.6 or gt0.6", "/Observation?value-quantity=lt0.6,gt0.6");
					assertNotFind("when lt0.3 or gt0.9", "/Observation?value-quantity=lt0.3,gt0.9");
				}

				@Test
				void ltOrGe() {
					withObservationWithValueQuantity(0.6);

					assertFind("when lt0.7 or ge0.2", "/Observation?value-quantity=lt0.7,ge0.2");
					assertNotFind("when lt0.5 or ge0.8", "/Observation?value-quantity=lt0.5,ge0.8");
				}

				@Test
				void gtOrGt() {
					withObservationWithValueQuantity(0.6);

					assertFind("when gt0.5 or gt0.8", "/Observation?value-quantity=gt0.5,gt0.8");
				}

				@Test
				void geOrGe() {
					withObservationWithValueQuantity(0.6);

					assertFind("when ge0.5 or ge0.7", "/Observation?value-quantity=ge0.5,ge0.7");
					assertNotFind("when ge0.65 or ge0.7", "/Observation?value-quantity=ge0.65,ge0.7");
				}

				@Test
				void ltOrLt() {
					withObservationWithValueQuantity(0.6);

					assertFind("when lt0.5 or lt0.7", "/Observation?value-quantity=lt0.5,lt0.7");
					assertNotFind("when lt0.55 or lt0.3", "/Observation?value-quantity=lt0.55,lt0.3");
				}

				@Test
				void leOrLe() {
					withObservationWithValueQuantity(0.6);

					assertFind("when le0.5 or le0.6", "/Observation?value-quantity=le0.5,le0.6");
					assertNotFind("when le0.5 or le0.59", "/Observation?value-quantity=le0.5,le0.59");
				}
			}

			@Test
			void testMultipleComponentsHandlesAndOr() {
				IIdType obs1Id = createObservation(
						withObservationComponent(
								withCodingAt("code.coding", "http://loinc.org", "8480-6"),
								withQuantityAtPath("valueQuantity", 107, "http://unitsofmeasure.org", "mm[Hg]")),
						withObservationComponent(
								withCodingAt("code.coding", "http://loinc.org", "8462-4"),
								withQuantityAtPath("valueQuantity", 60, "http://unitsofmeasure.org", "mm[Hg]")));

				IIdType obs2Id = createObservation(
						withObservationComponent(
								withCodingAt("code.coding", "http://loinc.org", "8480-6"),
								withQuantityAtPath("valueQuantity", 307, "http://unitsofmeasure.org", "mm[Hg]")),
						withObservationComponent(
								withCodingAt("code.coding", "http://loinc.org", "8462-4"),
								withQuantityAtPath("valueQuantity", 260, "http://unitsofmeasure.org", "mm[Hg]")));

				// andClauses
				{
					String theUrl = "/Observation?component-value-quantity=107&component-value-quantity=60";
					List<String> resourceIds = myTestDaoSearch.searchForIds(theUrl);
					assertThat(resourceIds)
							.as("when same component with qtys 107 and 60")
							.contains(obs1Id.getIdPart());
				}
				{
					String theUrl = "/Observation?component-value-quantity=107&component-value-quantity=260";
					List<String> resourceIds = myTestDaoSearch.searchForIds(theUrl);
					assertThat(resourceIds)
							.as("when same component with qtys 107 and 260")
							.isEmpty();
				}

				// andAndOrClauses
				{
					String theUrl = "/Observation?component-value-quantity=107&component-value-quantity=gt50,lt70";
					List<String> resourceIds = myTestDaoSearch.searchForIds(theUrl);
					assertThat(resourceIds)
							.as("when same component with qtys 107 and lt70,gt80")
							.contains(obs1Id.getIdPart());
				}
				{
					String theUrl = "/Observation?component-value-quantity=50,70&component-value-quantity=260";
					List<String> resourceIds = myTestDaoSearch.searchForIds(theUrl);
					assertThat(resourceIds)
							.as("when same component with qtys 50,70 and 260")
							.isEmpty();
				}

				// multipleAndsWithMultipleOrsEach
				{
					String theUrl = "/Observation?component-value-quantity=50,60&component-value-quantity=105,107";
					List<String> resourceIds = myTestDaoSearch.searchForIds(theUrl);
					assertThat(resourceIds)
							.as("when same component with qtys 50,60 and 105,107")
							.contains(obs1Id.getIdPart());
				}
				{
					String theUrl = "/Observation?component-value-quantity=50,60&component-value-quantity=250,260";
					List<String> resourceIds = myTestDaoSearch.searchForIds(theUrl);
					assertThat(resourceIds)
							.as("when same component with qtys 50,60 and 250,260")
							.isEmpty();
				}
			}
		}

		@Nested
		public class Sorting {

			@Test
			public void sortByNumeric() {
				String idAlpha7 = withObservationWithValueQuantity(0.7).getIdPart();
				String idAlpha2 = withObservationWithValueQuantity(0.2).getIdPart();
				String idAlpha5 = withObservationWithValueQuantity(0.5).getIdPart();

				List<String> allIds = myTestDaoSearch.searchForIds("/Observation?_sort=value-quantity");
				assertThat(allIds).containsExactly(idAlpha2, idAlpha5, idAlpha7);
			}
		}

		@Nested
		public class CorrelatedQueries {
			@Test
			public void unitsMustMatch() {
				myResourceId = createObservation(
						withObservationComponent(withQuantityAtPath("valueQuantity", 42, null, "cats")),
						withObservationComponent(withQuantityAtPath("valueQuantity", 18, null, "dogs")));

				assertFind("no units matches value", "/Observation?component-value-quantity=42");
				assertFind("correct units matches value", "/Observation?component-value-quantity=42||cats");
				assertNotFind(
						"mixed unit from other element in same resource",
						"/Observation?component-value-quantity=42||dogs");
			}
		}

		@Nested
		public class SpecTestCases {
			@Test
			void specCase1() {
				String id1 = withObservationWithValueQuantity(5.34).getIdPart();
				String id2 = withObservationWithValueQuantity(5.36).getIdPart();
				String id3 = withObservationWithValueQuantity(5.40).getIdPart();
				String id4 = withObservationWithValueQuantity(5.44).getIdPart();
				String id5 = withObservationWithValueQuantity(5.46).getIdPart();
				// GET [base]/Observation?value-quantity=5.4 :: 5.4(+/-0.05)
				assertFindIds("when le", Set.of(id2, id3, id4), "/Observation?value-quantity=5.4");
			}

			@Test
			void specCase2() {
				String id1 = withObservationWithValueQuantity(0.005394).getIdPart();
				String id2 = withObservationWithValueQuantity(0.005395).getIdPart();
				String id3 = withObservationWithValueQuantity(0.0054).getIdPart();
				String id4 = withObservationWithValueQuantity(0.005404).getIdPart();
				String id5 = withObservationWithValueQuantity(0.005406).getIdPart();
				// GET [base]/Observation?value-quantity=5.40e-3 :: 0.0054(+/-0.000005)
				assertFindIds("when le", Set.of(id2, id3, id4), "/Observation?value-quantity=5.40e-3");
			}

			@Test
			void specCase6() {
				String id1 = withObservationWithValueQuantity(4.85).getIdPart();
				String id2 = withObservationWithValueQuantity(4.86).getIdPart();
				String id3 = withObservationWithValueQuantity(5.94).getIdPart();
				String id4 = withObservationWithValueQuantity(5.95).getIdPart();
				// GET [base]/Observation?value-quantity=ap5.4 :: 5.4(+/- 10%) :: [4.86 ... 5.94]
				assertFindIds("when le", Set.of(id2, id3), "/Observation?value-quantity=ap5.4");
			}
		}
	}

	@Nested
	public class QuantityNormalizedSearch {

		NormalizedQuantitySearchLevel mySavedNomalizedSetting;

		@BeforeEach
		void setUp() {
			mySavedNomalizedSetting = myStorageSettings.getNormalizedQuantitySearchLevel();
			myStorageSettings.setNormalizedQuantitySearchLevel(
					NormalizedQuantitySearchLevel.NORMALIZED_QUANTITY_SEARCH_SUPPORTED);
		}

		@AfterEach
		void tearDown() {
			myStorageSettings.setNormalizedQuantitySearchLevel(mySavedNomalizedSetting);
		}

		@Nested
		public class SimpleQueries {

			@Test
			public void ne() {
				withObservationWithQuantity(0.06, UCUM_CODESYSTEM_URL, "10*6/L");

				assertFind("when lt UCUM", "/Observation?value-quantity=ne70|" + UCUM_CODESYSTEM_URL + "|10*3/L");
				assertFind("when gt UCUM", "/Observation?value-quantity=ne50|" + UCUM_CODESYSTEM_URL + "|10*3/L");
				assertNotFind("when eq UCUM", "/Observation?value-quantity=ne60|" + UCUM_CODESYSTEM_URL + "|10*3/L");
			}

			@Test
			public void eq() {
				withObservationWithQuantity(0.06, UCUM_CODESYSTEM_URL, "10*6/L");

				assertFind("when eq UCUM 10*3/L ", "/Observation?value-quantity=60|" + UCUM_CODESYSTEM_URL + "|10*3/L");
				assertFind(
						"when eq UCUM 10*9/L",
						"/Observation?value-quantity=0.000060|" + UCUM_CODESYSTEM_URL + "|10*9/L");

				assertNotFind(
						"when ne UCUM 10*3/L", "/Observation?value-quantity=80|" + UCUM_CODESYSTEM_URL + "|10*3/L");
				assertNotFind(
						"when gt UCUM 10*3/L", "/Observation?value-quantity=50|" + UCUM_CODESYSTEM_URL + "|10*3/L");
				assertNotFind(
						"when lt UCUM 10*3/L", "/Observation?value-quantity=70|" + UCUM_CODESYSTEM_URL + "|10*3/L");

				assertFind(
						"Units required to match and do",
						"/Observation?value-quantity=60000|" + UCUM_CODESYSTEM_URL + "|/L");
				// request generates a quantity which value matches the "value-norm", but not the "code-norm"
				assertNotFind(
						"Units required to match and don't",
						"/Observation?value-quantity=6000000000|" + UCUM_CODESYSTEM_URL + "|cm");
			}

			@Test
			public void ap() {
				withObservationWithQuantity(0.06, UCUM_CODESYSTEM_URL, "10*6/L");

				assertNotFind("when gt UCUM", "/Observation?value-quantity=ap50|" + UCUM_CODESYSTEM_URL + "|10*3/L");
				assertFind(
						"when little gt UCUM", "/Observation?value-quantity=ap58|" + UCUM_CODESYSTEM_URL + "|10*3/L");
				assertFind("when eq UCUM", "/Observation?value-quantity=ap60|" + UCUM_CODESYSTEM_URL + "|10*3/L");
				assertFind(
						"when a little lt UCUM", "/Observation?value-quantity=ap63|" + UCUM_CODESYSTEM_URL + "|10*3/L");
				assertNotFind("when lt UCUM", "/Observation?value-quantity=ap71|" + UCUM_CODESYSTEM_URL + "|10*3/L");
			}

			@Test
			public void gt() {
				withObservationWithQuantity(0.06, UCUM_CODESYSTEM_URL, "10*6/L");

				assertFind("when gt UCUM", "/Observation?value-quantity=gt50|" + UCUM_CODESYSTEM_URL + "|10*3/L");
				assertNotFind("when eq UCUM", "/Observation?value-quantity=gt60|" + UCUM_CODESYSTEM_URL + "|10*3/L");
				assertNotFind("when lt UCUM", "/Observation?value-quantity=gt71|" + UCUM_CODESYSTEM_URL + "|10*3/L");
			}

			@Test
			public void ge() {
				withObservationWithQuantity(0.06, UCUM_CODESYSTEM_URL, "10*6/L");

				assertFind("when gt UCUM", "/Observation?value-quantity=ge50|" + UCUM_CODESYSTEM_URL + "|10*3/L");
				assertFind("when eq UCUM", "/Observation?value-quantity=ge60|" + UCUM_CODESYSTEM_URL + "|10*3/L");
				assertNotFind("when lt UCUM", "/Observation?value-quantity=ge62|" + UCUM_CODESYSTEM_URL + "|10*3/L");
			}

			@Test
			public void lt() {
				withObservationWithQuantity(0.06, UCUM_CODESYSTEM_URL, "10*6/L");

				assertNotFind("when gt", "/Observation?value-quantity=lt50|" + UCUM_CODESYSTEM_URL + "|10*3/L");
				assertNotFind("when eq", "/Observation?value-quantity=lt60|" + UCUM_CODESYSTEM_URL + "|10*3/L");
				assertFind("when lt", "/Observation?value-quantity=lt70|" + UCUM_CODESYSTEM_URL + "|10*3/L");
			}

			@Test
			public void le() {
				withObservationWithQuantity(0.06, UCUM_CODESYSTEM_URL, "10*6/L");

				assertNotFind("when gt", "/Observation?value-quantity=le50|" + UCUM_CODESYSTEM_URL + "|10*3/L");
				assertFind("when eq", "/Observation?value-quantity=le60|" + UCUM_CODESYSTEM_URL + "|10*3/L");
				assertFind("when lt", "/Observation?value-quantity=le70|" + UCUM_CODESYSTEM_URL + "|10*3/L");
			}

			/**
			 * "value-quantity" data is stored in a nested object, so if not queried  properly
			 * it could return false positives. For instance: two Observations for following
			 * combinations of code and value:
			 *  Obs 1   code AAA1  value: 123
			 *  Obs 2   code BBB2  value: 456
			 *  A search for code: AAA1 and value: 456 would bring both observations instead of the expected empty reply,
			 *  unless both predicates are enclosed in a "nested"
			 * */
			@Test
			void nestedMustCorrelate() {
				withObservationWithQuantity(0.06, UCUM_CODESYSTEM_URL, "10*6/L");
				withObservationWithQuantity(0.02, UCUM_CODESYSTEM_URL, "10*3/L");

				assertNotFind(
						"when one predicate matches each object",
						"/Observation" + "?value-quantity=0.06|" + UCUM_CODESYSTEM_URL + "|10*3/L");
			}

			@Nested
			public class TemperatureUnitConversions {

				@Test
				public void storeCelsiusSearchFahrenheit() {
					withObservationWithQuantity(37.5, UCUM_CODESYSTEM_URL, "Cel");

					assertFind(
							"when eq UCUM  99.5 degF",
							"/Observation?value-quantity=99.5|" + UCUM_CODESYSTEM_URL + "|[degF]");
					assertNotFind(
							"when eq UCUM 101.1 degF",
							"/Observation?value-quantity=101.1|" + UCUM_CODESYSTEM_URL + "|[degF]");
					assertNotFind(
							"when eq UCUM  97.8 degF",
							"/Observation?value-quantity=97.8|" + UCUM_CODESYSTEM_URL + "|[degF]");
				}

				@Test
				public void storeFahrenheitSearchCelsius() {
					withObservationWithQuantity(99.5, UCUM_CODESYSTEM_URL, "[degF]");

					assertFind(
							"when eq UCUM 37.5 Cel",
							"/Observation?value-quantity=37.5|" + UCUM_CODESYSTEM_URL + "|Cel");
					assertNotFind(
							"when eq UCUM 37.3 Cel",
							"/Observation?value-quantity=37.3|" + UCUM_CODESYSTEM_URL + "|Cel");
					assertNotFind(
							"when eq UCUM 37.7 Cel",
							"/Observation?value-quantity=37.7|" + UCUM_CODESYSTEM_URL + "|Cel");
				}
			}
		}

		@Nested
		public class CombinedQueries {

			@Test
			void gtAndLt() {
				withObservationWithQuantity(0.06, UCUM_CODESYSTEM_URL, "10*6/L");

				assertFind(
						"when gt 50 and lt 70",
						"/Observation" + "?value-quantity=gt50|"
								+ UCUM_CODESYSTEM_URL + "|10*3/L" + "&value-quantity=lt70|"
								+ UCUM_CODESYSTEM_URL + "|10*3/L");

				assertNotFind(
						"when gt50 and lt60",
						"/Observation" + "?value-quantity=gt50|"
								+ UCUM_CODESYSTEM_URL + "|10*3/L" + "&value-quantity=lt60|"
								+ UCUM_CODESYSTEM_URL + "|10*3/L");

				assertNotFind(
						"when gt65 and lt70",
						"/Observation" + "?value-quantity=gt65|"
								+ UCUM_CODESYSTEM_URL + "|10*3/L" + "&value-quantity=lt70|"
								+ UCUM_CODESYSTEM_URL + "|10*3/L");

				assertNotFind(
						"when gt 70 and lt 50",
						"/Observation" + "?value-quantity=gt70|"
								+ UCUM_CODESYSTEM_URL + "|10*3/L" + "&value-quantity=lt50|"
								+ UCUM_CODESYSTEM_URL + "|10*3/L");
			}

			@Test
			void gtAndLtWithMixedUnits() {
				withObservationWithQuantity(0.06, UCUM_CODESYSTEM_URL, "10*6/L");

				assertFind(
						"when gt 50|10*3/L and lt 70|10*9/L",
						"/Observation" + "?value-quantity=gt50|"
								+ UCUM_CODESYSTEM_URL + "|10*3/L" + "&value-quantity=lt0.000070|"
								+ UCUM_CODESYSTEM_URL + "|10*9/L");
			}

			@Test
			public void multipleSearchParamsAreSeparate() {
				// for debugging
				//	myLogbackLevelOverrideExtension.setLogLevel(DaoTestDataBuilder.class, Level.DEBUG);

				// this configuration must generate a combo-value-quantity entry with both quantity objects
				myResourceId = createObservation(List.of(
						withQuantityAtPath("valueQuantity", 0.02, UCUM_CODESYSTEM_URL, "10*6/L"),
						withQuantityAtPath("component.valueQuantity", 0.06, UCUM_CODESYSTEM_URL, "10*6/L")));

				//	myLogbackLevelOverrideExtension.resetLevel(DaoTestDataBuilder.class);

				assertFind("by value", "Observation?value-quantity=0.02|" + UCUM_CODESYSTEM_URL + "|10*6/L");
				assertFind(
						"by component value",
						"Observation?component-value-quantity=0.06|" + UCUM_CODESYSTEM_URL + "|10*6/L");

				assertNotFind("by value", "Observation?value-quantity=0.06|" + UCUM_CODESYSTEM_URL + "|10*6/L");
				assertNotFind(
						"by component value",
						"Observation?component-value-quantity=0.02|" + UCUM_CODESYSTEM_URL + "|10*6/L");
			}
		}

		/**
		 * Sorting is now implemented for normalized quantities
		 */
		@Nested
		public class Sorting {

			@Test
			public void sortByNumeric() {
				String idAlpha1 = withObservationWithQuantity(0.06, UCUM_CODESYSTEM_URL, "10*6/L")
						.getIdPart(); // 60,000
				String idAlpha2 = withObservationWithQuantity(50, UCUM_CODESYSTEM_URL, "10*3/L")
						.getIdPart(); // 50,000
				String idAlpha3 = withObservationWithQuantity(0.000070, UCUM_CODESYSTEM_URL, "10*9/L")
						.getIdPart(); // 70_000

				// this search is not freetext because there is no freetext-known parameter name
				List<String> allIds = myTestDaoSearch.searchForIds("/Observation?_sort=value-quantity");
				assertThat(allIds).containsExactly(idAlpha2, idAlpha1, idAlpha3);
			}
		}
	}

	private void assertFind(String theMessage, String theUrl) {
		List<String> resourceIds = myTestDaoSearch.searchForIds(theUrl);
		assertThat(resourceIds).as(theMessage).contains(myResourceId.getIdPart());
	}

	private void assertNotFind(String theMessage, String theUrl) {
		List<String> resourceIds = myTestDaoSearch.searchForIds(theUrl);
		assertThat(resourceIds).as(theMessage).doesNotContain(myResourceId.getIdPart());
	}

	private IIdType withObservationWithQuantity(double theValue, String theSystem, String theCode) {
		myResourceId = createObservation(withQuantityAtPath("valueQuantity", theValue, theSystem, theCode));
		return myResourceId;
	}

	private IIdType withObservationWithValueQuantity(double theValue) {
		myResourceId = createObservation(List.of(withElementAt(
				"valueQuantity",
				withPrimitiveAttribute("value", theValue),
				withPrimitiveAttribute("system", UCUM_CODESYSTEM_URL),
				withPrimitiveAttribute("code", "mm[Hg]"))));
		return myResourceId;
	}

	private void assertFindIds(String theMessage, Collection<String> theResourceIds, String theUrl) {
		List<String> resourceIds = myTestDaoSearch.searchForIds(theUrl);
		assertThat(new HashSet<>(resourceIds)).as(theMessage).isEqualTo(theResourceIds);
	}
}
