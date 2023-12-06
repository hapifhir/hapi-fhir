package ca.uhn.fhir.jpa.fql.executor;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.rest.server.SimpleBundleProvider;
import ca.uhn.fhir.rest.server.util.FhirContextSearchParamRegistry;
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;
import com.google.common.collect.Lists;
import org.hl7.fhir.r4.model.DateType;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Quantity;
import org.hl7.fhir.r4.model.StringType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import static ca.uhn.fhir.jpa.fql.executor.HfqlExecutorTest.createCardiologyNoteObservation;
import static ca.uhn.fhir.jpa.fql.executor.HfqlExecutorTest.createPatientHomerSimpson;
import static ca.uhn.fhir.jpa.fql.executor.HfqlExecutorTest.createPatientLisaSimpson;
import static ca.uhn.fhir.jpa.fql.executor.HfqlExecutorTest.createPatientNedFlanders;
import static ca.uhn.fhir.jpa.fql.executor.HfqlExecutorTest.createProviderWithSomeSimpsonsAndFlanders;
import static ca.uhn.fhir.jpa.fql.executor.HfqlExecutorTest.createProviderWithSomeSimpsonsAndFlandersWithSomeDuplicates;
import static ca.uhn.fhir.jpa.fql.executor.HfqlExecutorTest.createProviderWithSparseNames;
import static ca.uhn.fhir.jpa.fql.executor.HfqlExecutorTest.createWeightObservationWithKilos;
import static ca.uhn.fhir.jpa.fql.executor.HfqlExecutorTest.readAllRowValues;
import static ca.uhn.fhir.jpa.fql.util.HfqlConstants.ORDER_AND_GROUP_LIMIT;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class LocalHfqlExecutorTest {

	private final FhirContext myCtx = FhirContext.forR4Cached();

	private final ISearchParamRegistry mySearchParamRegistry = new FhirContextSearchParamRegistry(myCtx);

	private final LocalHfqlExecutor myLocalHfqlExecutor = new LocalHfqlExecutor(myCtx, mySearchParamRegistry);

	@Test
	public void testLocalSearch() {
		// Setup
		String statement = """
			SELECT name[0].given[1], name[0].family
			FROM Patient
			WHERE name.family = 'Simpson'
			""";

		// Test
		IHfqlExecutionResult result = myLocalHfqlExecutor.executeLocalSearch(statement, 100, createProviderWithSomeSimpsonsAndFlanders());

		// Verify
		assertThat(result.getStatement().toSelectedColumnAliases(), contains(
			"name[0].given[1]", "name[0].family"
		));
		assertTrue(result.hasNext());
		IHfqlExecutionResult.Row nextRow = result.getNextRow();
		assertEquals(0, nextRow.getRowOffset());
		assertThat(nextRow.getRowValues(), contains("Jay", "Simpson"));
		assertTrue(result.hasNext());
		nextRow = result.getNextRow();
		assertEquals(2, nextRow.getRowOffset());
		assertThat(nextRow.getRowValues(), contains("El Barto", "Simpson"));
		assertTrue(result.hasNext());
		nextRow = result.getNextRow();
		assertEquals(3, nextRow.getRowOffset());
		assertThat(nextRow.getRowValues(), contains("Marie", "Simpson"));
		assertTrue(result.hasNext());
		nextRow = result.getNextRow();
		assertEquals(4, nextRow.getRowOffset());
		assertThat(nextRow.getRowValues(), contains("Evelyn", "Simpson"));
		assertFalse(result.hasNext());
	}

	@Test
	public void testSelect_OrderBy_ManyValues() {

		// Setup

		List<Patient> patients = new ArrayList<>();
		for (int i = 0; i < 5000; i++) {
			Patient patient = new Patient();
			patient.getMeta().setVersionId(Integer.toString(i));
			patient.addName().setFamily("PT" + i);
			patients.add(patient);
		}
		String statement = """
			FROM Patient
			SELECT
				meta.versionId.toInteger() AS versionId,
				name[0].family AS family
			ORDER BY versionId DESC
			""";

		// Test

		IHfqlExecutionResult result = myLocalHfqlExecutor.executeLocalSearch(statement, null, new SimpleBundleProvider(patients));

		// Verify
		IHfqlExecutionResult.Row nextRow;
		assertThat(result.getStatement().toSelectedColumnAliases(), contains(
			"versionId", "family"
		));
		for (int i = 4999; i >= 0; i--) {
			assertTrue(result.hasNext());
			nextRow = result.getNextRow();
			assertThat(nextRow.getRowValues().toString(), nextRow.getRowValues(), contains(String.valueOf(i), "PT" + i));
		}
	}


	@Test
	public void testSelect_OrderBy_SparseValues_Date() {

		// Setup

		List<Patient> patients = new ArrayList<>();
		Patient patient;

		patient = new Patient();
		patient.setId("PT0");
		patient.setBirthDateElement(new DateType("2023-01-01"));
		patients.add(patient);

		patient = new Patient();
		patient.setId("PT1");
		patient.setBirthDateElement(new DateType("2022-01-01"));
		patients.add(patient);

		patient = new Patient();
		patient.setId("PT2");
		patient.getBirthDateElement().addExtension("http://foo", new StringType("123"));
		patients.add(patient);

		String statement = """
			FROM Patient
			SELECT id, birthDate
			ORDER BY birthDate DESC
			""";

		// Test

		IHfqlExecutionResult result = myLocalHfqlExecutor.executeLocalSearch(statement, null, new SimpleBundleProvider(patients));

		// Verify
		IHfqlExecutionResult.Row nextRow;
		assertTrue(result.hasNext());
		nextRow = result.getNextRow();
		assertThat(nextRow.getRowValues().toString(), nextRow.getRowValues(), contains("PT0", "2023-01-01"));
		assertTrue(result.hasNext());
		nextRow = result.getNextRow();
		assertThat(nextRow.getRowValues().toString(), nextRow.getRowValues(), contains("PT1", "2022-01-01"));
		assertTrue(result.hasNext());
		nextRow = result.getNextRow();
		assertThat(nextRow.getRowValues().toString(), nextRow.getRowValues(), contains("PT2", ""));
		assertFalse(result.hasNext());
	}


	@Test
	public void testFromSelect() {

		String statement = """
			FROM Patient
			WHERE name.family = 'Simpson'
			SELECT name[0].given[1], name[0].family, name, name.given
			""";

		IHfqlExecutionResult.Row nextRow;
		IHfqlExecutionResult result = myLocalHfqlExecutor.executeLocalSearch(statement, null, createProviderWithSomeSimpsonsAndFlanders());
		assertThat(result.getStatement().toSelectedColumnAliases(), contains(
			"name[0].given[1]", "name[0].family", "name", "name.given"
		));
		assertThat(result.getStatement().toSelectedColumnDataTypes(), contains(
			HfqlDataTypeEnum.STRING, HfqlDataTypeEnum.STRING, HfqlDataTypeEnum.JSON, HfqlDataTypeEnum.JSON
		));
		assertTrue(result.hasNext());
		nextRow = result.getNextRow();
		assertEquals(0, nextRow.getRowOffset());
		assertThat(nextRow.getRowValues(), contains(
			"Jay",
			"Simpson",
			"[{\"family\":\"Simpson\",\"given\":[\"Homer\",\"Jay\"]}]",
			"[\"Homer\", \"Jay\"]"
		));
		assertTrue(result.hasNext());
		nextRow = result.getNextRow();
		assertEquals(2, nextRow.getRowOffset());
		assertThat(nextRow.getRowValues(), contains(
			"El Barto",
			"Simpson",
			"[{\"family\":\"Simpson\",\"given\":[\"Bart\",\"El Barto\"]}]",
			"[\"Bart\", \"El Barto\"]"
		));
		assertTrue(result.hasNext());
	}

	@Test
	public void testSelect_InvalidSelectClause() {
		String statement = """
			SELECT foo()
			FROM Patient
			""";

		IHfqlExecutionResult result = myLocalHfqlExecutor.executeLocalSearch(statement, null, createProviderWithSomeSimpsonsAndFlanders());
		IHfqlExecutionResult.Row row = result.getNextRow();
		assertEquals(IHfqlExecutionResult.ROW_OFFSET_ERROR, row.getRowOffset());
		assertEquals("Failed to evaluate FHIRPath expression \"foo()\". Error: HAPI-2404: Error in ?? at 1, 1: The name foo is not a valid function name", row.getRowValues().get(0));
		assertFalse(result.hasNext());
	}

	@Test
	public void testSelect_InvalidHavingClause() {
		String statement = """
			SELECT name
			FROM Patient
			WHERE meta.versionId > 1
			""";

		IHfqlExecutionResult result = myLocalHfqlExecutor.executeLocalSearch(statement, null, createProviderWithSomeSimpsonsAndFlanders());
		IHfqlExecutionResult.Row row = result.getNextRow();
		assertEquals(IHfqlExecutionResult.ROW_OFFSET_ERROR, row.getRowOffset());
		assertEquals(Msg.code(2403) + "Unable to evaluate FHIRPath expression \"meta.versionId > 1\". Error: HAPI-0255: Error evaluating FHIRPath expression: Unable to compare values of type id and integer (@char 3)", row.getRowValues().get(0));
		assertFalse(result.hasNext());
	}

	@Test
	public void testFromSelectStar() {

		String statement = """
			SELECT *
			FROM Patient
			WHERE name.family = 'Simpson'
			""";

		IHfqlExecutionResult result = myLocalHfqlExecutor.executeLocalSearch(statement, null, createProviderWithSomeSimpsonsAndFlanders());
		assertThat(result.getStatement().toSelectedColumnAliases().toString(), result.getStatement().toSelectedColumnAliases(), hasItems(
			"id", "active", "address", "birthDate"
		));
		assertThat(result.getStatement().toSelectedColumnAliases().toString(), result.getStatement().toSelectedColumnAliases(), not(hasItem(
			"extension"
		)));
	}

	@Test
	public void testSelect_Limit() {

		String statement = """
			SELECT name[0].given[0]
			FROM Patient
			LIMIT 5
			""";

		IHfqlExecutionResult result = myLocalHfqlExecutor.executeLocalSearch(statement, null, createProviderWithSomeSimpsonsAndFlandersWithSomeDuplicates());
		assertTrue(result.hasNext());
		assertThat(result.getNextRow().getRowValues(), contains("Homer"));
		assertTrue(result.hasNext());
		assertThat(result.getNextRow().getRowValues(), contains("Homer"));
		assertTrue(result.hasNext());
		assertThat(result.getNextRow().getRowValues(), contains("Ned"));
		assertTrue(result.hasNext());
		assertThat(result.getNextRow().getRowValues(), contains("Ned"));
		assertTrue(result.hasNext());
		assertThat(result.getNextRow().getRowValues(), contains("Bart"));
		assertFalse(result.hasNext());
	}

	@Test
	public void testFromSelectNonPrimitivePath() {

		String statement = """
			SELECT name
			FROM Patient
			""";

		IHfqlExecutionResult result = myLocalHfqlExecutor.executeLocalSearch(statement, null, new SimpleBundleProvider(
			createPatientHomerSimpson(),
			createPatientNedFlanders()
		));
		assertThat(result.getStatement().toSelectedColumnAliases().toString(), result.getStatement().toSelectedColumnAliases(), hasItems(
			"name"
		));
		assertThat(result.getStatement().toSelectedColumnDataTypes().toString(), result.getStatement().toSelectedColumnDataTypes(), hasItems(
			HfqlDataTypeEnum.JSON
		));

		List<List<Object>> rowValues = readAllRowValues(result);
		assertThat(rowValues.toString(), rowValues, containsInAnyOrder(
			Lists.newArrayList("[{\"family\":\"Simpson\",\"given\":[\"Homer\",\"Jay\"]}]"),
			Lists.newArrayList("[{\"family\":\"Flanders\",\"given\":[\"Ned\"]}]")
		));
	}

	@Test
	public void testFromSelectCount() {
		String statement = """
			FROM Patient
			SELECT name.family, name.given, count(*)
			GROUP BY name.family, name.given
			""";

		IHfqlExecutionResult result = myLocalHfqlExecutor.executeLocalSearch(statement, null, createProviderWithSomeSimpsonsAndFlandersWithSomeDuplicates());
		assertThat(result.getStatement().toSelectedColumnAliases().toString(), result.getStatement().toSelectedColumnAliases(), hasItems(
			"name.family", "name.given", "count(*)"
		));
		assertThat(result.getStatement().toSelectedColumnDataTypes().toString(), result.getStatement().toSelectedColumnDataTypes(), hasItems(
			// TODO: It'd make more sense if we used STRING instead of JSON here
			HfqlDataTypeEnum.JSON, HfqlDataTypeEnum.JSON, HfqlDataTypeEnum.INTEGER
		));

		List<List<Object>> rowValues = readAllRowValues(result);
		assertThat(rowValues.toString(), rowValues, containsInAnyOrder(
			Lists.newArrayList("Flanders", "Ned", 2),
			Lists.newArrayList("Simpson", "Jay", 2),
			Lists.newArrayList("Simpson", "Marie", 1),
			Lists.newArrayList("Simpson", "Evelyn", 1),
			Lists.newArrayList("Simpson", "Homer", 2),
			Lists.newArrayList("Simpson", "Lisa", 1),
			Lists.newArrayList("Simpson", "Bart", 1),
			Lists.newArrayList("Simpson", "El Barto", 1),
			Lists.newArrayList("Simpson", "Maggie", 1)
		));
	}

	@Test
	public void testFromSelectCount_TooMany() {
		List<Patient> patients = new ArrayList<>();
		for (int i = 0; i < ORDER_AND_GROUP_LIMIT + 10; i++) {
			Patient patient = new Patient();
			patient.addName().setFamily("PT" + i);
			patients.add(patient);
		}
		String statement = """
			FROM Patient
			SELECT name.family, count(*)
			GROUP BY name.family
			""";

		IHfqlExecutionResult result = myLocalHfqlExecutor.executeLocalSearch(statement, null, new SimpleBundleProvider(patients));
		assertErrorMessage(result, Msg.code(2402) + "Can not group on > 10000 terms");
	}

	@Test
	public void testFromSelectCount_NoGroup() {

		// Only 0+1 have a family name
		Patient pt0 = new Patient();
		pt0.addName().setFamily("Simpson");
		Patient pt1 = new Patient();
		pt1.addName().setFamily("Smithers");
		Patient pt2 = new Patient();
		pt2.addName().addGiven("Blah");

		String statement = """
			SELECT count(*), count(name.family)
			FROM Patient
			""";

		IHfqlExecutionResult result = myLocalHfqlExecutor.executeLocalSearch(statement, null, new SimpleBundleProvider(pt0, pt1, pt2));
		assertThat(result.getStatement().toSelectedColumnAliases().toString(), result.getStatement().toSelectedColumnAliases(), hasItems(
			"count(*)", "count(name.family)"
		));
		assertThat(result.getStatement().toSelectedColumnDataTypes().toString(), result.getStatement().toSelectedColumnDataTypes(), hasItems(
			HfqlDataTypeEnum.INTEGER, HfqlDataTypeEnum.INTEGER
		));

		List<List<Object>> rowValues = readAllRowValues(result);
		assertThat(rowValues.toString(), rowValues, contains(
			Lists.newArrayList(3, 2)
		));
	}

	@Test
	public void testFromSelectCountOrderBy() {
		String statement = """
			FROM Patient
			SELECT name[0].family, name[0].given, count(*)
			GROUP BY name[0].family, name[0].given
			ORDER BY count(*) desc, name[0].family asc, name[0].given asc
			""";

		IHfqlExecutionResult result = myLocalHfqlExecutor.executeLocalSearch(statement, null, createProviderWithSomeSimpsonsAndFlandersWithSomeDuplicates());
		assertThat(result.getStatement().toSelectedColumnAliases().toString(), result.getStatement().toSelectedColumnAliases(), hasItems(
			"name[0].family", "name[0].given", "count(*)"
		));
		assertThat(result.getStatement().toSelectedColumnDataTypes().toString(), result.getStatement().toSelectedColumnDataTypes(), hasItems(
			HfqlDataTypeEnum.STRING, HfqlDataTypeEnum.STRING, HfqlDataTypeEnum.INTEGER
		));

		List<List<Object>> rowValues = readAllRowValues(result);
		assertThat(rowValues.toString(), rowValues, contains(
			Lists.newArrayList("Flanders", "Ned", 2),
			Lists.newArrayList("Simpson", "Homer", 2),
			Lists.newArrayList("Simpson", "Jay", 2),
			Lists.newArrayList("Simpson", "Bart", 1),
			Lists.newArrayList("Simpson", "El Barto", 1),
			Lists.newArrayList("Simpson", "Evelyn", 1),
			Lists.newArrayList("Simpson", "Lisa", 1),
			Lists.newArrayList("Simpson", "Maggie", 1),
			Lists.newArrayList("Simpson", "Marie", 1)
		));
	}

	@Test
	public void testFromSelectCountOrderBy_WithNulls() {

		String statement = """
			FROM Patient
			SELECT name[0].family, name[0].given[0]
			ORDER BY name[0].family desc, name[0].given[0] desc
			""";

		IHfqlExecutionResult result = myLocalHfqlExecutor.executeLocalSearch(statement, null, new SimpleBundleProvider(
			createPatientHomerSimpson(),
			createPatientLisaSimpson(),
			new Patient()
		));
		assertThat(result.getStatement().toSelectedColumnAliases().toString(), result.getStatement().toSelectedColumnAliases(), hasItems(
			"name[0].family", "name[0].given[0]"
		));
		assertThat(result.getStatement().toSelectedColumnDataTypes().toString(), result.getStatement().toSelectedColumnDataTypes(), hasItems(
			HfqlDataTypeEnum.STRING, HfqlDataTypeEnum.STRING
		));

		List<List<Object>> rowValues = readAllRowValues(result);
		assertThat(rowValues.toString(), rowValues, contains(
			Lists.newArrayList("Simpson", "Lisa"),
			Lists.newArrayList("Simpson", "Homer"),
			Lists.newArrayList(null, null)
		));
	}

	@Test
	public void testFromSelectCountOrderBy_DateWithNulls() {

		String statement = """
			FROM Patient
			SELECT name[0].family, name[0].given[0], birthDate
			ORDER BY birthDate desc
			""";

		IHfqlExecutionResult result = myLocalHfqlExecutor.executeLocalSearch(statement, null, new SimpleBundleProvider(
			createPatientHomerSimpson().setBirthDateElement(new DateType("1950-01-01")),
			createPatientLisaSimpson().setBirthDateElement(new DateType("1990-01-01")),
			new Patient()
		));
		assertThat(result.getStatement().toSelectedColumnAliases().toString(), result.getStatement().toSelectedColumnAliases(), hasItems(
			"name[0].family", "name[0].given[0]", "birthDate"
		));
		assertThat(result.getStatement().toSelectedColumnDataTypes().toString(), result.getStatement().toSelectedColumnDataTypes(), hasItems(
			HfqlDataTypeEnum.STRING, HfqlDataTypeEnum.STRING, HfqlDataTypeEnum.DATE
		));

		List<List<Object>> rowValues = readAllRowValues(result);
		assertThat(rowValues.toString(), rowValues, contains(
			Lists.newArrayList("Simpson", "Lisa", "1990-01-01"),
			Lists.newArrayList("Simpson", "Homer", "1950-01-01"),
			Lists.newArrayList(null, null, null)
		));
	}

	@Test
	public void testFromSelectCountOrderBy_BooleanWithNulls() {

		String statement = """
			FROM Patient
			SELECT name[0].family, name[0].given[0], active
			ORDER BY active asc, name[0].given[0] asc
			""";

		IHfqlExecutionResult result = myLocalHfqlExecutor.executeLocalSearch(statement, null, new SimpleBundleProvider(
			createPatientHomerSimpson().setActive(true),
			createPatientLisaSimpson().setActive(false),
			createPatientNedFlanders().setActive(true)
		));
		assertThat(result.getStatement().toSelectedColumnAliases().toString(), result.getStatement().toSelectedColumnAliases(), hasItems(
			"name[0].family", "name[0].given[0]", "active"
		));
		assertThat(result.getStatement().toSelectedColumnDataTypes().toString(), result.getStatement().toSelectedColumnDataTypes(), hasItems(
			HfqlDataTypeEnum.STRING, HfqlDataTypeEnum.STRING, HfqlDataTypeEnum.BOOLEAN
		));

		List<List<Object>> rowValues = readAllRowValues(result);
		assertThat(rowValues.toString(), rowValues, contains(
			Lists.newArrayList("Simpson", "Lisa", "false"),
			Lists.newArrayList("Simpson", "Homer", "true"),
			Lists.newArrayList("Flanders", "Ned", "true")
		));
	}

	@Test
	public void testFromSelectCount_NullValues() {

		String statement = """
			FROM Patient
			SELECT name[0].family, name[0].given[0], count(*), count(name[0].family)
			GROUP BY name[0].family, name[0].given[0]
			""";

		IHfqlExecutionResult result = myLocalHfqlExecutor.executeLocalSearch(statement, null, createProviderWithSparseNames());
		assertThat(result.getStatement().toSelectedColumnAliases().toString(), result.getStatement().toSelectedColumnAliases(), hasItems(
			"name[0].family", "name[0].given[0]", "count(*)", "count(name[0].family)"
		));
		assertThat(result.getStatement().toSelectedColumnDataTypes().toString(), result.getStatement().toSelectedColumnDataTypes(), hasItems(
			HfqlDataTypeEnum.STRING, HfqlDataTypeEnum.STRING, HfqlDataTypeEnum.INTEGER, HfqlDataTypeEnum.INTEGER
		));

		List<List<Object>> rowValues = readAllRowValues(result);
		assertThat(rowValues.toString(), rowValues, containsInAnyOrder(
			Lists.newArrayList(null, "Homer", 1, 0),
			Lists.newArrayList("Simpson", "Homer", 1, 1),
			Lists.newArrayList("Simpson", null, 1, 1),
			Lists.newArrayList(null, null, 1, 0)
		));
	}

	@Test
	public void testFromSelectCount_NullValues_NoGroup() {

		String statement = """
			FROM Patient
			SELECT count(*), count(name.family)
			""";

		IHfqlExecutionResult result = myLocalHfqlExecutor.executeLocalSearch(statement, null, createProviderWithSparseNames());
		assertThat(result.getStatement().toSelectedColumnAliases().toString(), result.getStatement().toSelectedColumnAliases(), hasItems(
			"count(*)", "count(name.family)"
		));
		assertThat(result.getStatement().toSelectedColumnDataTypes().toString(), result.getStatement().toSelectedColumnDataTypes(), hasItems(
			HfqlDataTypeEnum.INTEGER, HfqlDataTypeEnum.INTEGER
		));

		List<List<Object>> rowValues = readAllRowValues(result);
		assertThat(rowValues.toString(), rowValues, containsInAnyOrder(
			Lists.newArrayList(4, 2)
		));
	}

	@Test
	public void testFromSelectComplexFhirPath() {

		String statement = """
			FROM Patient
			WHERE name.family = 'Simpson'
			SELECT name[0].given[0], identifier.where(system = 'http://system' ).first().value
			""";

		IHfqlExecutionResult.Row nextRow;
		IHfqlExecutionResult result = myLocalHfqlExecutor.executeLocalSearch(statement, null, createProviderWithSomeSimpsonsAndFlanders());
		assertThat(result.getStatement().toSelectedColumnAliases().toString(), result.getStatement().toSelectedColumnAliases(), hasItems(
			"name[0].given[0]", "identifier.where(system = 'http://system' ).first().value"
		));
		nextRow = result.getNextRow();

		assertEquals("Homer", nextRow.getRowValues().get(0));
		assertEquals("value0", nextRow.getRowValues().get(1));
	}

	@Test
	public void testFromSelectComplexFhirPath2() {

		String statement = """
			FROM Patient
			WHERE identifier.where(system = 'http://system' ).value = 'value0'
			SELECT name[0].given[0], identifier[0].value
			""";

		IHfqlExecutionResult.Row nextRow;
		IHfqlExecutionResult result = myLocalHfqlExecutor.executeLocalSearch(statement, null, createProviderWithSomeSimpsonsAndFlanders());
		assertThat(result.getStatement().toSelectedColumnAliases().toString(), result.getStatement().toSelectedColumnAliases(), hasItems(
			"name[0].given[0]", "identifier[0].value"
		));
		nextRow = result.getNextRow();

		assertEquals("Homer", nextRow.getRowValues().get(0));
		assertEquals("value0", nextRow.getRowValues().get(1));
		assertFalse(result.hasNext());
	}

	/**
	 * This only works with parentheses
	 */
	@Test
	public void testFromSelectComplexFhirPath3() {

		Patient p = new Patient();
		p.addIdentifier().setSystem("http://foo").setValue("123");

		String statement = """
			SELECT
			   COL1: (identifier[0].system) + '|' + (identifier[0].value),
			   (identifier[0].system) + '|' + (identifier[0].value) AS COL2,
			   (identifier[0].system) + '|' + (identifier[0].value)
			FROM
			   Patient
			""";

		IHfqlExecutionResult.Row nextRow;
		IHfqlExecutionResult result = myLocalHfqlExecutor.executeLocalSearch(statement, null, new SimpleBundleProvider(p));
		assertThat(result.getStatement().toSelectedColumnAliases().toString(), result.getStatement().toSelectedColumnAliases(), hasItems(
			"COL1", "COL2", "(identifier[0].system) + '|' + (identifier[0].value)"
		));
		nextRow = result.getNextRow();
		assertThat(nextRow.getRowValues().toString(), nextRow.getRowValues(), contains("http://foo|123", "http://foo|123", "http://foo|123"));
		assertFalse(result.hasNext());
	}

	@Test
	public void testFromHavingComplexFhirPath_StringContains() {

		Observation obs1 = createCardiologyNoteObservation("Observation/1", "Patient is running a lot");
		Observation obs2 = createCardiologyNoteObservation("Observation/2", "Patient is eating a lot");
		Observation obs3 = createCardiologyNoteObservation("Observation/3", "Patient is running a little");
		Observation obs4 = createCardiologyNoteObservation("Observation/4", "Patient is walking a lot");

		String statement = """
			SELECT id
			FROM Observation
			WHERE
				id in search_match('code', 'http://loinc.org|34752-6')
				AND
			   value.ofType(string).lower().contains('running')
			""";

		IHfqlExecutionResult.Row nextRow;
		IHfqlExecutionResult result = myLocalHfqlExecutor.executeLocalSearch(statement, null, new SimpleBundleProvider(obs1, obs2, obs3, obs4));
		assertThat(result.getStatement().toSelectedColumnAliases().toString(), result.getStatement().toSelectedColumnAliases(), hasItems(
			"id"
		));
		assertThat(result.getStatement().toSelectedColumnDataTypes().toString(), result.getStatement().toSelectedColumnDataTypes(), hasItems(
			HfqlDataTypeEnum.STRING
		));

		nextRow = result.getNextRow();
		assertThat(nextRow.getRowValues().toString(), nextRow.getRowValues(), contains(
			"1"
		));
		nextRow = result.getNextRow();
		assertThat(nextRow.getRowValues().toString(), nextRow.getRowValues(), contains(
			"3"
		));
		assertFalse(result.hasNext());
	}

	@Test
	public void testFromWhereComplexFhirPath_Cast() {

		String statement = """
			SELECT name[0].given[0]
			FROM Patient
			WHERE meta.versionId.toInteger() > 1
			""";

		IHfqlExecutionResult.Row row;
		IHfqlExecutionResult result = myLocalHfqlExecutor.executeLocalSearch(statement, null, createProviderWithSomeSimpsonsAndFlanders());

		assertTrue(result.hasNext());
		row = result.getNextRow();
		assertThat(row.getRowValues().toString(), row.getRowValues(), contains("Homer"));

		assertTrue(result.hasNext());
		row = result.getNextRow();
		assertThat(row.getRowValues().toString(), row.getRowValues(), contains("Bart"));

		assertFalse(result.hasNext());
	}


	@Test
	public void testSelectComplexFhirPath_StringConcat() {

		String statement = """
			SELECT FullName: Patient.name.first().given.first() + ' ' + Patient.name.first().family
			FROM Patient
			""";

		IHfqlExecutionResult.Row nextRow;
		IHfqlExecutionResult result = myLocalHfqlExecutor.executeLocalSearch(statement, null, createPatientHomerSimpson());
		assertThat(result.getStatement().toSelectedColumnAliases().toString(), result.getStatement().toSelectedColumnAliases(), hasItems(
			"FullName"
		));
		assertThat(result.getStatement().toSelectedColumnDataTypes().toString(), result.getStatement().toSelectedColumnDataTypes(), hasItems(
			HfqlDataTypeEnum.STRING
		));
		nextRow = result.getNextRow();
		assertThat(nextRow.getRowValues().toString(), nextRow.getRowValues(), contains(
			"Homer Simpson"
		));
		assertFalse(result.hasNext());
	}

	@Test
	public void testHaving_ComplexFhirPath_Numeric() {

		Observation obs1 = createWeightObservationWithKilos("Observation/1", 10L);
		Observation obs2 = createWeightObservationWithKilos("Observation/2", 100L);
		Observation obs3 = createWeightObservationWithKilos("Observation/3", 101L);
		Observation obs4 = createWeightObservationWithKilos("Observation/4", 102L);

		String statement = """
			SELECT
			   id,
			   value.ofType(Quantity).value,
			   value.ofType(Quantity).system,
			   value.ofType(Quantity).code
			FROM Observation
			WHERE
			   value.ofType(Quantity).value > 100
			""";

		IHfqlExecutionResult.Row nextRow;
		IHfqlExecutionResult result = myLocalHfqlExecutor.executeLocalSearch(statement, null, new SimpleBundleProvider(obs1, obs2, obs3, obs4));
		assertThat(result.getStatement().toSelectedColumnAliases().toString(), result.getStatement().toSelectedColumnAliases(), hasItems(
			"id", "value.ofType(Quantity).value", "value.ofType(Quantity).system", "value.ofType(Quantity).code"
		));
		assertThat(result.getStatement().toSelectedColumnDataTypes().toString(), result.getStatement().toSelectedColumnDataTypes(), hasItems(
			HfqlDataTypeEnum.STRING, HfqlDataTypeEnum.DECIMAL, HfqlDataTypeEnum.STRING, HfqlDataTypeEnum.STRING
		));

		nextRow = result.getNextRow();
		assertThat(nextRow.getRowValues().toString(), nextRow.getRowValues(), contains(
			"3", "101", "http://unitsofmeasure.org", "kg"
		));
	}

	@Test
	public void testFromHavingSelectIn() {

		String statement = """
			FROM Patient
			WHERE name.given in ('Foo' | 'Bart')
			SELECT Given:name[0].given[1], Family:name[0].family[0]
			""";

		IHfqlExecutionResult result = myLocalHfqlExecutor.executeLocalSearch(statement, null, createProviderWithSomeSimpsonsAndFlanders());
		assertThat(result.getStatement().toSelectedColumnAliases(), contains(
			"Given", "Family"
		));
		assertTrue(result.hasNext());
		IHfqlExecutionResult.Row nextRow = result.getNextRow();
		assertEquals(2, nextRow.getRowOffset());
		assertThat(nextRow.getRowValues(), contains("El Barto", "Simpson"));
		assertFalse(result.hasNext());
	}

	@Test
	public void testFromHavingSelectEquals() {

		String statement = """
			FROM Patient
			WHERE name.given = 'Homer'
			SELECT Given:name[0].given[1], Family:name[0].family
			""";

		IHfqlExecutionResult result = myLocalHfqlExecutor.executeLocalSearch(statement, null, createProviderWithSomeSimpsonsAndFlanders());
		assertThat(result.getStatement().toSelectedColumnAliases(), contains(
			"Given", "Family"
		));
		assertTrue(result.hasNext());
		IHfqlExecutionResult.Row row = result.getNextRow();
		assertEquals(0, row.getRowOffset());
		assertThat(row.getRowValues(), contains("Jay", "Simpson"));
		assertFalse(result.hasNext());
	}

	@Test
	public void testIntrospectTables() {
		IHfqlExecutionResult tables = myLocalHfqlExecutor.introspectTables();
		assertEquals("TABLE_NAME", tables.getStatement().toSelectedColumnAliases().get(2));
		assertTrue(tables.hasNext());
		assertEquals("Account", tables.getNextRow().getRowValues().get(2));
	}

	@Test
	public void testIntrospectColumns_NoSelector() {
		IHfqlExecutionResult tables = myLocalHfqlExecutor.introspectColumns(null, null);
		assertEquals("TABLE_NAME", tables.getStatement().toSelectedColumnAliases().get(2), tables.getStatement().toSelectedColumnAliases().toString());
		assertEquals("COLUMN_NAME", tables.getStatement().toSelectedColumnAliases().get(3), tables.getStatement().toSelectedColumnAliases().toString());
		assertEquals("DATA_TYPE", tables.getStatement().toSelectedColumnAliases().get(4), tables.getStatement().toSelectedColumnAliases().toString());
		assertTrue(tables.hasNext());
		assertEquals("Account", tables.getNextRow().getRowValues().get(2));
		assertEquals("coverage", tables.getNextRow().getRowValues().get(3));
		assertEquals(Types.VARCHAR, tables.getNextRow().getRowValues().get(4));
	}

	@Test
	public void testIntrospectColumns_TableSelector() {
		IHfqlExecutionResult tables = myLocalHfqlExecutor.introspectColumns("Patient", null);
		assertEquals("TABLE_NAME", tables.getStatement().toSelectedColumnAliases().get(2), tables.getStatement().toSelectedColumnAliases().toString());
		assertEquals("COLUMN_NAME", tables.getStatement().toSelectedColumnAliases().get(3), tables.getStatement().toSelectedColumnAliases().toString());
		assertEquals("DATA_TYPE", tables.getStatement().toSelectedColumnAliases().get(4), tables.getStatement().toSelectedColumnAliases().toString());
		assertTrue(tables.hasNext());
		assertEquals("Patient", tables.getNextRow().getRowValues().get(2));
		assertEquals("address", tables.getNextRow().getRowValues().get(3));
		assertEquals(Types.VARCHAR, tables.getNextRow().getRowValues().get(4));
	}

	@ValueSource(strings = {
		"_blah", "foo"
	})
	@ParameterizedTest
	public void testWhere_Error_UnknownParam(String theParamName) {

		String statement =
			"FROM Patient " +
			"WHERE id in search_match('" + theParamName + "', 'abc') " +
			"SELECT name.given";

		IHfqlExecutionResult result = myLocalHfqlExecutor.executeLocalSearch(statement, null);
		assertErrorMessage(result, "Unknown/unsupported search parameter: " + theParamName);
	}

	private static void assertErrorMessage(IHfqlExecutionResult result, String expected) {
		assertTrue(result.hasNext());
		IHfqlExecutionResult.Row nextRow = result.getNextRow();
		assertEquals(IHfqlExecutionResult.ROW_OFFSET_ERROR, nextRow.getRowOffset());
		assertThat(nextRow.getRowValues(), contains(expected));
	}

	@Test
	public void testWhere_Id_In_CommaList() {

		Observation resource = new Observation();
		resource.getMeta().setVersionId("5");
		resource.setId("Observation/123");
		resource.setValue(new Quantity(null, 500.1, "http://unitsofmeasure.org", "kg", "kg"));

		String statement = """
			SELECT
				id, meta.versionId, value.ofType(Quantity).value
			FROM
				Observation
			WHERE
				id in search_match('_id', '123,Patient/456')
			""";

		IHfqlExecutionResult result = myLocalHfqlExecutor.executeLocalSearch(statement, null, new SimpleBundleProvider(resource));

		assertThat(result.getStatement().toSelectedColumnAliases(), contains("id", "meta.versionId", "value.ofType(Quantity).value"));
		assertThat(result.getStatement().toSelectedColumnDataTypes(), contains(HfqlDataTypeEnum.STRING, HfqlDataTypeEnum.LONGINT, HfqlDataTypeEnum.DECIMAL));
		assertTrue(result.hasNext());
		List<Object> nextRow = result.getNextRow().getRowValues();
		assertEquals("123", nextRow.get(0));
		assertEquals("5", nextRow.get(1));
		assertEquals("500.1", nextRow.get(2));
	}

	@Test
	public void testSearch_QualifiedSelect() {

		String statement = """
			FROM Patient
			SELECT Patient.name[0].given[0]
			""";

		IHfqlExecutionResult outcome = myLocalHfqlExecutor.executeLocalSearch(statement, null, createProviderWithSomeSimpsonsAndFlanders());
		assertTrue(outcome.hasNext());
		assertEquals("Homer", outcome.getNextRow().getRowValues().get(0));
	}

	@Test
	public void testSelect_RepeatingElement_NeedsEscaping() {

		Patient patient = new Patient();
		patient.addName().addGiven("1\"2").addGiven("1\\,2");

		String statement = """
			SELECT
				name.given
			FROM
				Patient
			""";

		IHfqlExecutionResult result = myLocalHfqlExecutor.executeLocalSearch(statement, null, new SimpleBundleProvider(patient));

		assertThat(result.getStatement().toSelectedColumnAliases(), contains("name.given"));
		assertThat(result.getStatement().toSelectedColumnDataTypes(), contains(HfqlDataTypeEnum.JSON));
		assertTrue(result.hasNext());
		List<Object> nextRow = result.getNextRow().getRowValues();
		assertEquals("[\"1\\\"2\", \"1\\\\,2\"]", nextRow.get(0));
	}

	@Test
	public void testSearch_UnknownSelector() {

		String statement = """
			SELECT
				name[0].given[0], foo
			FROM
				Patient
			""";

		IHfqlExecutionResult result = myLocalHfqlExecutor.executeLocalSearch(statement, null, createProviderWithSomeSimpsonsAndFlanders());

		assertThat(result.getStatement().toSelectedColumnAliases(), contains("name[0].given[0]", "foo"));
		assertThat(result.getStatement().toSelectedColumnDataTypes(), contains(HfqlDataTypeEnum.STRING, HfqlDataTypeEnum.STRING));
		assertTrue(result.hasNext());
		List<Object> nextRow = result.getNextRow().getRowValues();
		assertEquals("Homer", nextRow.get(0));
		assertNull(nextRow.get(1));
	}

	@Test
	public void testError_InvalidFromType() {
		String input = """
			FROM Foo
			SELECT Foo.blah
			""";

		IHfqlExecutionResult result = myLocalHfqlExecutor.executeLocalSearch(input, null);
		assertErrorMessage(result, "Invalid FROM statement. Unknown resource type 'Foo' at position: [line=0, column=5]");
	}

	@Test
	public void testError_NonGroupedSelectInCountClause() {

		String input = """
			FROM Patient
			SELECT count(*), name.family
			""";

		IHfqlExecutionResult result = myLocalHfqlExecutor.executeLocalSearch(input, null);
		assertErrorMessage(result, "Unable to select on non-grouped column in a count expression: name.family");
	}

	@Test
	public void testError_SearchMatchOnNonId() {

		String input = """
			SELECT name.family
			FROM Patient
			WHERE name in search_match('identifier', '1|1')
			""";

		IHfqlExecutionResult result = myLocalHfqlExecutor.executeLocalSearch(input, null);
		assertErrorMessage(result, "HAPI-2412: search_match function can only be applied to the id element");
	}

	@Test
	public void testError_SearchMatchNotEnoughArguments() {

		String input = """
			SELECT name.family
			FROM Patient
			WHERE id in search_match('identifier')
			""";

		IHfqlExecutionResult result = myLocalHfqlExecutor.executeLocalSearch(input, null);
		assertErrorMessage(result, "HAPI-2413: search_match function requires 2 arguments");
	}

}
