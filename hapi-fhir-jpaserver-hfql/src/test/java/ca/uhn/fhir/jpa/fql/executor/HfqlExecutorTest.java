package ca.uhn.fhir.jpa.fql.executor;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.fql.parser.HfqlStatement;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.param.DateParam;
import ca.uhn.fhir.rest.param.ParamPrefixEnum;
import ca.uhn.fhir.rest.param.QuantityParam;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.IPagingProvider;
import ca.uhn.fhir.rest.server.SimpleBundleProvider;
import ca.uhn.fhir.rest.server.util.FhirContextSearchParamRegistry;
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;
import com.google.common.collect.Lists;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.DateType;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Quantity;
import org.hl7.fhir.r4.model.StringType;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.annotation.Nonnull;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class HfqlExecutorTest {

	private final RequestDetails mySrd = new SystemRequestDetails();
	@Spy
	private FhirContext myCtx = FhirContext.forR4Cached();
	@Mock
	private DaoRegistry myDaoRegistry;
	@Mock
	private IPagingProvider myPagingProvider;
	@Spy
	private ISearchParamRegistry mySearchParamRegistry = new FhirContextSearchParamRegistry(myCtx);
	@InjectMocks
	private HfqlExecutor myHfqlExecutor = new HfqlExecutor();
	@Captor
	private ArgumentCaptor<SearchParameterMap> mySearchParameterMapCaptor;

	@Test
	public void testContinuation() {
		// Setup
		HfqlStatement statement = new HfqlStatement();
		statement.setFromResourceName("Patient");
		statement.addSelectClause("name[0].given[1]").setAlias("name[0].given[1]").setDataType(HfqlDataTypeEnum.STRING);
		statement.addSelectClause("name[0].family").setAlias("name[0].family").setDataType(HfqlDataTypeEnum.STRING);
		statement.addWhereClause("name.family = 'Simpson'", HfqlStatement.WhereClauseOperatorEnum.UNARY_BOOLEAN);

		String searchId = "the-search-id";
		when(myPagingProvider.retrieveResultList(any(), eq(searchId))).thenReturn(createProviderWithSomeSimpsonsAndFlanders());

		// Test
		IHfqlExecutionResult result = myHfqlExecutor.executeContinuation(statement, searchId, 3, 100, mySrd);

		// Verify
		assertThat(result.getStatement().toSelectedColumnAliases(), contains(
			"name[0].given[1]", "name[0].family"
		));
		assertTrue(result.hasNext());
		IHfqlExecutionResult.Row nextRow = result.getNextRow();
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

		IFhirResourceDao<Patient> patientDao = initDao(Patient.class);
		List<Patient> patients = new ArrayList<>();
		for (int i = 0; i < 5000; i++) {
			Patient patient = new Patient();
			patient.getMeta().setVersionId(Integer.toString(i));
			patient.addName().setFamily("PT" + i);
			patients.add(patient);
		}
		when(patientDao.search(any(), any())).thenReturn(new SimpleBundleProvider(patients));
		String statement = """
					FROM Patient
					SELECT
						meta.versionId.toInteger() AS versionId,
						name[0].family AS family
					ORDER BY versionId DESC
			""";

		// Test

		IHfqlExecutionResult result = myHfqlExecutor.executeInitialSearch(statement, null, mySrd);

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

		IFhirResourceDao<Patient> patientDao = initDao(Patient.class);
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

		when(patientDao.search(any(), any())).thenReturn(new SimpleBundleProvider(patients));
		String statement = """
					FROM Patient
					SELECT id, birthDate
					ORDER BY birthDate DESC
			""";

		// Test

		IHfqlExecutionResult result = myHfqlExecutor.executeInitialSearch(statement, null, mySrd);

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
		IFhirResourceDao<Patient> patientDao = initDao(Patient.class);
		when(patientDao.search(any(), any())).thenReturn(createProviderWithSomeSimpsonsAndFlanders());

		String statement = """
					from Patient
					where name.family = 'Simpson'
					select name[0].given[1], name[0].family, name, name.given
			""";

		IHfqlExecutionResult.Row nextRow;
		IHfqlExecutionResult result = myHfqlExecutor.executeInitialSearch(statement, null, mySrd);
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

		verify(patientDao, times(1)).search(mySearchParameterMapCaptor.capture(), any());
		// Default count
		assertNull(mySearchParameterMapCaptor.getValue().getCount());
	}

	@Test
	public void testSelect_InvalidSelectClause() {
		IFhirResourceDao<Patient> patientDao = initDao(Patient.class);
		when(patientDao.search(any(), any())).thenReturn(createProviderWithSomeSimpsonsAndFlanders());
		String statement = """
					select foo()
					from Patient
			""";

		IHfqlExecutionResult result = myHfqlExecutor.executeInitialSearch(statement, null, mySrd);
		IHfqlExecutionResult.Row row = result.getNextRow();
		assertEquals(IHfqlExecutionResult.ROW_OFFSET_ERROR, row.getRowOffset());
		assertEquals("Failed to evaluate FHIRPath expression \"foo()\". Error: HAPI-2404: Error in ?? at 1, 1: The name foo is not a valid function name", row.getRowValues().get(0));
		assertFalse(result.hasNext());
	}

	@Test
	public void testSelect_InvalidHavingClause() {
		IFhirResourceDao<Patient> patientDao = initDao(Patient.class);
		when(patientDao.search(any(), any())).thenReturn(createProviderWithSomeSimpsonsAndFlanders());
		String statement = """
					select name
					from Patient
					where meta.versionId > 1
			""";

		IHfqlExecutionResult result = myHfqlExecutor.executeInitialSearch(statement, null, mySrd);
		IHfqlExecutionResult.Row row = result.getNextRow();
		assertEquals(IHfqlExecutionResult.ROW_OFFSET_ERROR, row.getRowOffset());
		assertEquals(Msg.code(2403) + "Unable to evaluate FHIRPath expression \"meta.versionId > 1\". Error: HAPI-0255: Error evaluating FHIRPath expression: Unable to compare values of type id and integer (@char 3)", row.getRowValues().get(0));
		assertFalse(result.hasNext());
	}

	@Test
	public void testFromSelectStar() {
		IFhirResourceDao<Patient> patientDao = initDao(Patient.class);
		when(patientDao.search(any(), any())).thenReturn(createProviderWithSomeSimpsonsAndFlanders());

		String statement = """
					select *
					from Patient
					where name.family = 'Simpson'
			""";

		IHfqlExecutionResult result = myHfqlExecutor.executeInitialSearch(statement, null, mySrd);
		assertThat(result.getStatement().toSelectedColumnAliases().toString(), result.getStatement().toSelectedColumnAliases(), hasItems(
			"id", "active", "address", "birthDate"
		));
		assertThat(result.getStatement().toSelectedColumnAliases().toString(), result.getStatement().toSelectedColumnAliases(), not(hasItem(
			"extension"
		)));
	}

	@Test
	public void testSelect_Limit() {
		IFhirResourceDao<Patient> patientDao = initDao(Patient.class);
		when(patientDao.search(any(), any())).thenReturn(createProviderWithSomeSimpsonsAndFlandersWithSomeDuplicates());

		String statement = """
					select name[0].given[0]
					from Patient
					limit 5
			""";

		IHfqlExecutionResult result = myHfqlExecutor.executeInitialSearch(statement, null, mySrd);
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
		IFhirResourceDao<Patient> patientDao = initDao(Patient.class);
		when(patientDao.search(any(), any())).thenReturn(new SimpleBundleProvider(
			createPatientHomerSimpson(),
			createPatientNedFlanders()
		));

		String statement = """
					select name
					from Patient
			""";

		IHfqlExecutionResult result = myHfqlExecutor.executeInitialSearch(statement, null, mySrd);
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
		IFhirResourceDao<Patient> patientDao = initDao(Patient.class);
		when(patientDao.search(any(), any())).thenReturn(createProviderWithSomeSimpsonsAndFlandersWithSomeDuplicates());
		String statement = """
					from Patient
					select name.family, name.given, count(*)
					group by name.family, name.given
			""";

		IHfqlExecutionResult result = myHfqlExecutor.executeInitialSearch(statement, null, mySrd);
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
		IFhirResourceDao<Patient> patientDao = initDao(Patient.class);
		List<Patient> patients = new ArrayList<>();
		for (int i = 0; i < ORDER_AND_GROUP_LIMIT + 10; i++) {
			Patient patient = new Patient();
			patient.addName().setFamily("PT" + i);
			patients.add(patient);
		}
		when(patientDao.search(any(), any())).thenReturn(new SimpleBundleProvider(patients));
		String statement = """
					from Patient
					select name.family, count(*)
					group by name.family
			""";

		IHfqlExecutionResult result = myHfqlExecutor.executeInitialSearch(statement, null, mySrd);
		assertErrorMessage(result, Msg.code(2402) + "Can not group on > 10000 terms");
	}

	@Test
	public void testFromSelectCount_NoGroup() {
		IFhirResourceDao<Patient> patientDao = initDao(Patient.class);

		// Only 0+1 have a family name
		Patient pt0 = new Patient();
		pt0.addName().setFamily("Simpson");
		Patient pt1 = new Patient();
		pt1.addName().setFamily("Smithers");
		Patient pt2 = new Patient();
		pt2.addName().addGiven("Blah");

		when(patientDao.search(any(), any())).thenReturn(new SimpleBundleProvider(pt0, pt1, pt2));
		String statement = """
					select count(*), count(name.family)
					from Patient
			""";

		IHfqlExecutionResult result = myHfqlExecutor.executeInitialSearch(statement, null, mySrd);
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
		IFhirResourceDao<Patient> patientDao = initDao(Patient.class);
		when(patientDao.search(any(), any())).thenReturn(createProviderWithSomeSimpsonsAndFlandersWithSomeDuplicates());
		String statement = """
					from Patient
					select name[0].family, name[0].given, count(*)
					group by name[0].family, name[0].given
					order by count(*) desc, name[0].family asc, name[0].given asc
			""";

		IHfqlExecutionResult result = myHfqlExecutor.executeInitialSearch(statement, null, mySrd);
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
		IFhirResourceDao<Patient> patientDao = initDao(Patient.class);
		when(patientDao.search(any(), any())).thenReturn(new SimpleBundleProvider(
			createPatientHomerSimpson(),
			createPatientLisaSimpson(),
			new Patient()
		));
		String statement = """
					from Patient
					select name[0].family, name[0].given[0]
					order by name[0].family desc, name[0].given[0] desc
			""";

		IHfqlExecutionResult result = myHfqlExecutor.executeInitialSearch(statement, null, mySrd);
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
		IFhirResourceDao<Patient> patientDao = initDao(Patient.class);
		when(patientDao.search(any(), any())).thenReturn(new SimpleBundleProvider(
			createPatientHomerSimpson().setBirthDateElement(new DateType("1950-01-01")),
			createPatientLisaSimpson().setBirthDateElement(new DateType("1990-01-01")),
			new Patient()
		));
		String statement = """
					from Patient
					select name[0].family, name[0].given[0], birthDate
					order by birthDate desc
			""";

		IHfqlExecutionResult result = myHfqlExecutor.executeInitialSearch(statement, null, mySrd);
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
		IFhirResourceDao<Patient> patientDao = initDao(Patient.class);
		when(patientDao.search(any(), any())).thenReturn(new SimpleBundleProvider(
			createPatientHomerSimpson().setActive(true),
			createPatientLisaSimpson().setActive(false),
			createPatientNedFlanders().setActive(true)
		));
		String statement = """
					from Patient
					select name[0].family, name[0].given[0], active
					order by active asc, name[0].given[0] asc
			""";

		IHfqlExecutionResult result = myHfqlExecutor.executeInitialSearch(statement, null, mySrd);
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
		IFhirResourceDao<Patient> patientDao = initDao(Patient.class);

		when(patientDao.search(any(), any())).thenReturn(createProviderWithSparseNames());

		String statement = """
					from Patient
					select name[0].family, name[0].given[0], count(*), count(name[0].family)
					group by name[0].family, name[0].given[0]
			""";

		IHfqlExecutionResult result = myHfqlExecutor.executeInitialSearch(statement, null, mySrd);
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
		IFhirResourceDao<Patient> patientDao = initDao(Patient.class);

		when(patientDao.search(any(), any())).thenReturn(createProviderWithSparseNames());

		String statement = """
					from Patient
					select count(*), count(name.family)
			""";

		IHfqlExecutionResult result = myHfqlExecutor.executeInitialSearch(statement, null, mySrd);
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
		IFhirResourceDao<Patient> patientDao = initDao(Patient.class);
		when(patientDao.search(any(), any())).thenReturn(createProviderWithSomeSimpsonsAndFlanders());

		String statement = """
					from Patient
					where name.family = 'Simpson'
					select name[0].given[0], identifier.where(system = 'http://system' ).first().value
			""";

		IHfqlExecutionResult.Row nextRow;
		IHfqlExecutionResult result = myHfqlExecutor.executeInitialSearch(statement, null, mySrd);
		assertThat(result.getStatement().toSelectedColumnAliases().toString(), result.getStatement().toSelectedColumnAliases(), hasItems(
			"name[0].given[0]", "identifier.where(system = 'http://system' ).first().value"
		));
		nextRow = result.getNextRow();

		assertEquals("Homer", nextRow.getRowValues().get(0));
		assertEquals("value0", nextRow.getRowValues().get(1));
	}

	@Test
	public void testFromSelectComplexFhirPath2() {
		IFhirResourceDao<Patient> patientDao = initDao(Patient.class);
		when(patientDao.search(any(), any())).thenReturn(createProviderWithSomeSimpsonsAndFlanders());

		String statement = """
					from Patient
					where identifier.where(system = 'http://system' ).value = 'value0'
					select name[0].given[0], identifier[0].value
			""";

		IHfqlExecutionResult.Row nextRow;
		IHfqlExecutionResult result = myHfqlExecutor.executeInitialSearch(statement, null, mySrd);
		assertThat(result.getStatement().toSelectedColumnAliases().toString(), result.getStatement().toSelectedColumnAliases(), hasItems(
			"name[0].given[0]", "identifier[0].value"
		));
		nextRow = result.getNextRow();

		assertEquals("Homer", nextRow.getRowValues().get(0));
		assertEquals("value0", nextRow.getRowValues().get(1));
		assertFalse(result.hasNext());
	}

	/**
	 * This should work but the FHIRPath evaluator doesn't seem to be
	 * doing the right thing
	 */
	@Test
	@Disabled
	public void testFromSelectComplexFhirPath3() {
		IFhirResourceDao<Patient> patientDao = initDao(Patient.class);

		Patient p = new Patient();
		p.addIdentifier().setSystem("http://foo").setValue("123");

		when(patientDao.search(any(), any())).thenReturn(new SimpleBundleProvider(p));

		String statement = """
			SELECT
			   COL1: identifier[0].system + '|' + identifier[0].value,
			   identifier[0].system + '|' + identifier[0].value AS COL2,
			   identifier[0].system + '|' + identifier[0].value
			FROM
			   Patient
			""";

		IHfqlExecutionResult.Row nextRow;
		IHfqlExecutionResult result = myHfqlExecutor.executeInitialSearch(statement, null, mySrd);
		assertThat(result.getStatement().toSelectedColumnAliases().toString(), result.getStatement().toSelectedColumnAliases(), hasItems(
			"COL1", "COL2", "identifier[0].system + '|' + identifier[0].value"
		));
		nextRow = result.getNextRow();
		assertThat(nextRow.getRowValues().toString(), nextRow.getRowValues(), contains(
			""
		));
		assertFalse(result.hasNext());
	}

	@Test
	public void testFromHavingComplexFhirPath_StringContains() {
		IFhirResourceDao<Observation> observationDao = initDao(Observation.class);

		Observation obs1 = createCardiologyNoteObservation("Observation/1", "Patient is running a lot");
		Observation obs2 = createCardiologyNoteObservation("Observation/2", "Patient is eating a lot");
		Observation obs3 = createCardiologyNoteObservation("Observation/3", "Patient is running a little");
		Observation obs4 = createCardiologyNoteObservation("Observation/4", "Patient is walking a lot");

		when(observationDao.search(any(), any())).thenReturn(new SimpleBundleProvider(obs1, obs2, obs3, obs4));

		String statement = """
					SELECT id
					FROM Observation
					WHERE
						id in search_match('code', 'http://loinc.org|34752-6')
						AND
					   value.ofType(string).lower().contains('running')
			""";

		IHfqlExecutionResult.Row nextRow;
		IHfqlExecutionResult result = myHfqlExecutor.executeInitialSearch(statement, null, mySrd);
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

		verify(observationDao, times(1)).search(mySearchParameterMapCaptor.capture(), any());
		SearchParameterMap map = mySearchParameterMapCaptor.getValue();
		assertEquals(1, map.size());
		assertEquals("http://loinc.org|34752-6", map.get("code").get(0).get(0).getValueAsQueryToken(myCtx));
	}

	@Test
	public void testFromWhereComplexFhirPath_Cast() {
		IFhirResourceDao<Patient> patientDao = initDao(Patient.class);
		when(patientDao.search(any(), any())).thenReturn(createProviderWithSomeSimpsonsAndFlanders());
		String statement = """
					select name[0].given[0]
					from Patient
					where meta.versionId.toInteger() > 1
			""";

		IHfqlExecutionResult.Row row;
		IHfqlExecutionResult result = myHfqlExecutor.executeInitialSearch(statement, null, mySrd);

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
		IFhirResourceDao<Patient> patientDao = initDao(Patient.class);

		when(patientDao.search(any(), any())).thenReturn(new SimpleBundleProvider(createPatientHomerSimpson()));

		String statement = """
					SELECT FullName: Patient.name.first().given.first() + ' ' + Patient.name.first().family
					FROM Patient
			""";

		IHfqlExecutionResult.Row nextRow;
		IHfqlExecutionResult result = myHfqlExecutor.executeInitialSearch(statement, null, mySrd);
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
		IFhirResourceDao<Observation> observationDao = initDao(Observation.class);

		Observation obs1 = createWeightObservationWithKilos("Observation/1", 10L);
		Observation obs2 = createWeightObservationWithKilos("Observation/2", 100L);
		Observation obs3 = createWeightObservationWithKilos("Observation/3", 101L);
		Observation obs4 = createWeightObservationWithKilos("Observation/4", 102L);

		when(observationDao.search(any(), any())).thenReturn(new SimpleBundleProvider(obs1, obs2, obs3, obs4));

		String statement = """
					select
					   id,
					   value.ofType(Quantity).value,
					   value.ofType(Quantity).system,
					   value.ofType(Quantity).code
					from Observation
					where
					   value.ofType(Quantity).value > 100
			""";

		IHfqlExecutionResult.Row nextRow;
		IHfqlExecutionResult result = myHfqlExecutor.executeInitialSearch(statement, null, mySrd);
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
		IFhirResourceDao<Patient> patientDao = initDao(Patient.class);
		when(patientDao.search(any(), any())).thenReturn(createProviderWithSomeSimpsonsAndFlanders());

		String statement = """
					from Patient
					where name.given in ('Foo' | 'Bart')
					select Given:name[0].given[1], Family:name[0].family[0]
			""";

		IHfqlExecutionResult result = myHfqlExecutor.executeInitialSearch(statement, null, mySrd);
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
		IFhirResourceDao<Patient> patientDao = initDao(Patient.class);
		when(patientDao.search(any(), any())).thenReturn(createProviderWithSomeSimpsonsAndFlanders());

		String statement = """
					from Patient
					where name.given = 'Homer'
					select Given:name[0].given[1], Family:name[0].family
			""";

		IHfqlExecutionResult result = myHfqlExecutor.executeInitialSearch(statement, null, mySrd);
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
		IHfqlExecutionResult tables = myHfqlExecutor.introspectTables();
		assertEquals("TABLE_NAME", tables.getStatement().toSelectedColumnAliases().get(2));
		assertTrue(tables.hasNext());
		assertEquals("Account", tables.getNextRow().getRowValues().get(2));
	}

	@Test
	public void testIntrospectColumns_NoSelector() {
		IHfqlExecutionResult tables = myHfqlExecutor.introspectColumns(null, null);
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
		IHfqlExecutionResult tables = myHfqlExecutor.introspectColumns("Patient", null);
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
		initDao(Patient.class);

		String statement = "from Patient " +
			"where id in search_match('" + theParamName + "', 'abc') " +
			"select name.given";

		IHfqlExecutionResult result = myHfqlExecutor.executeInitialSearch(statement, null, mySrd);
		assertErrorMessage(result, "Unknown/unsupported search parameter: " + theParamName);
	}

	private static void assertErrorMessage(IHfqlExecutionResult result, String expected) {
		assertTrue(result.hasNext());
		IHfqlExecutionResult.Row nextRow = result.getNextRow();
		assertEquals(IHfqlExecutionResult.ROW_OFFSET_ERROR, nextRow.getRowOffset());
		assertThat(nextRow.getRowValues(), contains(expected));
	}

	@Test
	public void testWhere_Id_In_CommaList_SearchMatch() {
		IFhirResourceDao<Observation> patientDao = initDao(Observation.class);
		Observation resource = new Observation();
		resource.getMeta().setVersionId("5");
		resource.setId("Observation/123");
		resource.setValue(new Quantity(null, 500.1, "http://unitsofmeasure.org", "kg", "kg"));
		when(patientDao.search(any(), any())).thenReturn(new SimpleBundleProvider(resource));

		String statement = """
					select
						id, meta.versionId, value.ofType(Quantity).value
					from
						Observation
					where
						id in search_match('_id', '123,Patient/456')
			""";

		IHfqlExecutionResult result = myHfqlExecutor.executeInitialSearch(statement, null, mySrd);

		assertThat(result.getStatement().toSelectedColumnAliases(), contains("id", "meta.versionId", "value.ofType(Quantity).value"));
		assertThat(result.getStatement().toSelectedColumnDataTypes(), contains(HfqlDataTypeEnum.STRING, HfqlDataTypeEnum.LONGINT, HfqlDataTypeEnum.DECIMAL));
		assertTrue(result.hasNext());
		List<Object> nextRow = result.getNextRow().getRowValues();
		assertEquals("123", nextRow.get(0));
		assertEquals("5", nextRow.get(1));
		assertEquals("500.1", nextRow.get(2));

		verify(patientDao, times(1)).search(mySearchParameterMapCaptor.capture(), any());
		SearchParameterMap map = mySearchParameterMapCaptor.getValue();
		assertEquals(1, map.get("_id").size());
		assertEquals(2, map.get("_id").get(0).size());
		assertNull(((TokenParam) map.get("_id").get(0).get(0)).getSystem());
		assertEquals("123", ((TokenParam) map.get("_id").get(0).get(0)).getValue());
		assertNull(((TokenParam) map.get("_id").get(0).get(1)).getSystem());
		assertEquals("Patient/456", ((TokenParam) map.get("_id").get(0).get(1)).getValue());
	}

	@Test
	public void testWhere_FhirPathElevatedToSearchParam_Id_Equals() {
		IFhirResourceDao<Patient> patientDao = initDao(Patient.class);
		when(patientDao.search(any(), any())).thenReturn(createProviderWithSomeSimpsonsAndFlanders());

		String statement = """
					select id
					from Patient
					where	id IN ('HOMER0', 'HOMER1')
			""";

		IHfqlExecutionResult result = myHfqlExecutor.executeInitialSearch(statement, null, mySrd);

		assertTrue(result.hasNext());
		List<Object> nextRow = result.getNextRow().getRowValues();
		assertEquals("HOMER0", nextRow.get(0));

		verify(patientDao, times(1)).search(mySearchParameterMapCaptor.capture(), any());
		SearchParameterMap map = mySearchParameterMapCaptor.getValue();
		assertEquals(1, map.get("_id").size());
		assertEquals(2, map.get("_id").get(0).size());
		assertNull(((TokenParam) map.get("_id").get(0).get(0)).getSystem());
		assertEquals("HOMER0", ((TokenParam) map.get("_id").get(0).get(0)).getValue());
		assertNull(((TokenParam) map.get("_id").get(0).get(1)).getSystem());
		assertEquals("HOMER1", ((TokenParam) map.get("_id").get(0).get(1)).getValue());
	}


	@Test
	public void testSearch_QualifiedSelect() {
		IFhirResourceDao<Patient> patientDao = initDao(Patient.class);
		when(patientDao.search(any(), any())).thenReturn(createProviderWithSomeSimpsonsAndFlanders());

		String statement = """
					from Patient
					select Patient.name[0].given[0]
			""";

		IHfqlExecutionResult outcome = myHfqlExecutor.executeInitialSearch(statement, null, mySrd);
		assertTrue(outcome.hasNext());
		assertEquals("Homer", outcome.getNextRow().getRowValues().get(0));

	}

	@Test
	public void testSelect_RepeatingElement_NeedsEscaping() {
		IFhirResourceDao<Patient> patientDao = initDao(Patient.class);
		Patient patient = new Patient();
		patient.addName().addGiven("1\"2").addGiven("1\\,2");
		when(patientDao.search(any(), any())).thenReturn(new SimpleBundleProvider(patient));


		String statement = """
					SELECT
						name.given
					FROM
						Patient
			""";

		IHfqlExecutionResult result = myHfqlExecutor.executeInitialSearch(statement, null, mySrd);

		assertThat(result.getStatement().toSelectedColumnAliases(), contains("name.given"));
		assertThat(result.getStatement().toSelectedColumnDataTypes(), contains(HfqlDataTypeEnum.JSON));
		assertTrue(result.hasNext());
		List<Object> nextRow = result.getNextRow().getRowValues();
		assertEquals("[\"1\\\"2\", \"1\\\\,2\"]", nextRow.get(0));

	}

	@Test
	public void testSearch_UnknownSelector() {
		IFhirResourceDao<Patient> patientDao = initDao(Patient.class);
		when(patientDao.search(any(), any())).thenReturn(createProviderWithSomeSimpsonsAndFlanders());


		String statement = """
					select
						name[0].given[0], foo
					from
						Patient
			""";

		IHfqlExecutionResult result = myHfqlExecutor.executeInitialSearch(statement, null, mySrd);

		assertThat(result.getStatement().toSelectedColumnAliases(), contains("name[0].given[0]", "foo"));
		assertThat(result.getStatement().toSelectedColumnDataTypes(), contains(HfqlDataTypeEnum.STRING, HfqlDataTypeEnum.STRING));
		assertTrue(result.hasNext());
		List<Object> nextRow = result.getNextRow().getRowValues();
		assertEquals("Homer", nextRow.get(0));
		assertNull(nextRow.get(1));
	}

	@Test
	public void testWhere_LastUpdated_In() {
		IFhirResourceDao<Patient> patientDao = initDao(Patient.class);
		when(patientDao.search(any(), any())).thenReturn(createProviderWithSomeSimpsonsAndFlanders());

		String statement = """
					from Patient
					where id in search_match('_lastUpdated', 'lt2021,gt2023')
					select name.given
			""";

		myHfqlExecutor.executeInitialSearch(statement, null, mySrd);

		verify(patientDao, times(1)).search(mySearchParameterMapCaptor.capture(), any());
		SearchParameterMap map = mySearchParameterMapCaptor.getValue();
		assertEquals(1, map.get("_lastUpdated").size());
		assertEquals(2, map.get("_lastUpdated").get(0).size());
		assertEquals(ParamPrefixEnum.LESSTHAN, ((DateParam) map.get("_lastUpdated").get(0).get(0)).getPrefix());
		assertEquals("2021", ((DateParam) map.get("_lastUpdated").get(0).get(0)).getValueAsString());
		assertEquals(ParamPrefixEnum.GREATERTHAN, ((DateParam) map.get("_lastUpdated").get(0).get(1)).getPrefix());
		assertEquals("2023", ((DateParam) map.get("_lastUpdated").get(0).get(1)).getValueAsString());
	}

	@Test
	public void testWhere_Boolean() {
		IFhirResourceDao<Patient> patientDao = initDao(Patient.class);
		when(patientDao.search(any(), any())).thenReturn(createProviderWithSomeSimpsonsAndFlanders());

		String statement = """
					from Patient
					where id in search_match('active', 'true')
					select name.given
			""";

		myHfqlExecutor.executeInitialSearch(statement, null, mySrd);

		verify(patientDao, times(1)).search(mySearchParameterMapCaptor.capture(), any());
		SearchParameterMap map = mySearchParameterMapCaptor.getValue();
		assertEquals(1, map.get("active").size());
		assertEquals(1, map.get("active").get(0).size());
		assertNull(((TokenParam) map.get("active").get(0).get(0)).getSystem());
		assertEquals("true", ((TokenParam) map.get("active").get(0).get(0)).getValue());
	}

	@Test
	public void testWhere_Quantity() {
		IFhirResourceDao<Observation> observationDao = initDao(Observation.class);
		when(observationDao.search(any(), any())).thenReturn(new SimpleBundleProvider());

		String statement = """
					from Observation
					where id in search_match('value-quantity', 'lt500|http://unitsofmeasure.org|kg')
					select id
			""";

		myHfqlExecutor.executeInitialSearch(statement, null, mySrd);

		verify(observationDao, times(1)).search(mySearchParameterMapCaptor.capture(), any());
		SearchParameterMap map = mySearchParameterMapCaptor.getValue();
		assertEquals(1, map.get("value-quantity").size());
		assertEquals(1, map.get("value-quantity").get(0).size());
		assertEquals("500", ((QuantityParam) map.get("value-quantity").get(0).get(0)).getValue().toString());
		assertEquals(ParamPrefixEnum.LESSTHAN, ((QuantityParam) map.get("value-quantity").get(0).get(0)).getPrefix());
		assertEquals("http://unitsofmeasure.org", ((QuantityParam) map.get("value-quantity").get(0).get(0)).getSystem());
		assertEquals("kg", ((QuantityParam) map.get("value-quantity").get(0).get(0)).getUnits());
	}

	@Test
	public void testWhere_String() {
		IFhirResourceDao<Patient> patientDao = initDao(Patient.class);
		when(patientDao.search(any(), any())).thenReturn(createProviderWithSomeSimpsonsAndFlanders());

		String statement = """
					from Patient
					where id in search_match('name', 'abc')
					select name.given
			""";

		myHfqlExecutor.executeInitialSearch(statement, null, mySrd);

		verify(patientDao, times(1)).search(mySearchParameterMapCaptor.capture(), any());
		SearchParameterMap map = mySearchParameterMapCaptor.getValue();
		assertEquals(1, map.get("name").size());
		assertEquals(1, map.get("name").get(0).size());
		assertEquals("abc", ((StringParam) map.get("name").get(0).get(0)).getValue());
	}

	@Test
	public void testWhere_String_Exact() {
		IFhirResourceDao<Patient> patientDao = initDao(Patient.class);
		when(patientDao.search(any(), any())).thenReturn(createProviderWithSomeSimpsonsAndFlanders());

		String statement = """
					select name.given
					from Patient
					where id in search_match('name:exact', 'abc')
			""";

		myHfqlExecutor.executeInitialSearch(statement, null, mySrd);

		verify(patientDao, times(1)).search(mySearchParameterMapCaptor.capture(), any());
		SearchParameterMap map = mySearchParameterMapCaptor.getValue();
		assertEquals(1, map.get("name").size());
		assertEquals(1, map.get("name").get(0).size());
		assertEquals("abc", ((StringParam) map.get("name").get(0).get(0)).getValue());
		assertTrue(((StringParam) map.get("name").get(0).get(0)).isExact());
	}

	@Test
	public void testWhere_String_AndOr() {
		IFhirResourceDao<Patient> patientDao = initDao(Patient.class);
		when(patientDao.search(any(), any())).thenReturn(createProviderWithSomeSimpsonsAndFlanders());

		String statement = """
					from Patient
					where
						id in search_match('name', 'A,B\\,B')
					and
						id in search_match('name', 'C,D')
					select name.given
			""";

		myHfqlExecutor.executeInitialSearch(statement, null, mySrd);

		verify(patientDao, times(1)).search(mySearchParameterMapCaptor.capture(), any());
		SearchParameterMap map = mySearchParameterMapCaptor.getValue();
		assertEquals(2, map.get("name").size());
		assertEquals(2, map.get("name").get(0).size());
		assertEquals("A", ((StringParam) map.get("name").get(0).get(0)).getValue());
		assertEquals("B,B", ((StringParam) map.get("name").get(0).get(1)).getValue());
		assertEquals("C", ((StringParam) map.get("name").get(1).get(0)).getValue());
		assertEquals("D", ((StringParam) map.get("name").get(1).get(1)).getValue());
	}

	@Test
	public void testError_InvalidFromType() {
		String input = """
			from Foo
			select Foo.blah
			""";

		IHfqlExecutionResult result = myHfqlExecutor.executeInitialSearch(input, null, mySrd);
		assertErrorMessage(result, "Invalid FROM statement. Unknown resource type 'Foo' at position: [line=0, column=5]");
	}

	@Test
	public void testError_NonGroupedSelectInCountClause() {
		initDao(Patient.class);

		String input = """
			from Patient
			select count(*), name.family
			""";

		IHfqlExecutionResult result = myHfqlExecutor.executeInitialSearch(input, null, mySrd);
		assertErrorMessage(result, "Unable to select on non-grouped column in a count expression: name.family");
	}

	@Test
	public void testError_SearchMatchOnNonId() {
		initDao(Patient.class);

		String input = """
			select name.family
			from Patient
			where name in search_match('identifier', '1|1')
			""";

		IHfqlExecutionResult result = myHfqlExecutor.executeInitialSearch(input, null, mySrd);
		assertErrorMessage(result, "HAPI-2412: search_match function can only be applied to the id element");
	}

	@Test
	public void testError_SearchMatchNotEnoughArguments() {
		initDao(Patient.class);

		String input = """
			select name.family
			from Patient
			where id in search_match('identifier')
			""";

		IHfqlExecutionResult result = myHfqlExecutor.executeInitialSearch(input, null, mySrd);
		assertErrorMessage(result, "HAPI-2413: search_match function requires 2 arguments");
	}

	@Test
	public void testError_InvalidWhereParameter() {
		initDao(Patient.class);

		String input = """
			select name.family
			from Patient
			where Blah = '123'
			""";

		IHfqlExecutionResult result = myHfqlExecutor.executeInitialSearch(input, null, mySrd);
		assertErrorMessage(result, "HAPI-2429: Resource type Patient does not have a root element named 'Blah'");
	}

	@SuppressWarnings("unchecked")
	private <T extends IBaseResource> IFhirResourceDao<T> initDao(Class<T> theType) {
		IFhirResourceDao<T> retVal = mock(IFhirResourceDao.class);
		String type = myCtx.getResourceType(theType);
		when(myDaoRegistry.getResourceDao(type)).thenReturn(retVal);
		return retVal;
	}

	@Nonnull
	private static List<List<Object>> readAllRowValues(IHfqlExecutionResult result) {
		List<List<Object>> rowValues = new ArrayList<>();
		while (result.hasNext()) {
			rowValues.add(new ArrayList<>(result.getNextRow().getRowValues()));
		}
		return rowValues;
	}

	@Nonnull
	private static Observation createCardiologyNoteObservation(String id, String noteText) {
		Observation obs = new Observation();
		obs.setId(id);
		obs.getCode().addCoding()
			.setSystem("http://loinc.org")
			.setCode("34752-6");
		obs.setValue(new StringType(noteText));
		return obs;
	}

	@Nonnull
	private static Observation createWeightObservationWithKilos(String obsId, long kg) {
		Observation obs = new Observation();
		obs.setId(obsId);
		obs.getCode().addCoding()
			.setSystem("http://loinc.org")
			.setCode("29463-7");
		obs.setValue(new Quantity(null, kg, "http://unitsofmeasure.org", "kg", "kg"));
		return obs;
	}

	@Nonnull
	private static SimpleBundleProvider createProviderWithSparseNames() {
		Patient patientNoValues = new Patient();
		patientNoValues.setActive(true);
		Patient patientFamilyNameOnly = new Patient();
		patientFamilyNameOnly.addName().setFamily("Simpson");
		Patient patientGivenNameOnly = new Patient();
		patientGivenNameOnly.addName().addGiven("Homer");
		Patient patientBothNames = new Patient();
		patientBothNames.addName().setFamily("Simpson").addGiven("Homer");
		return new SimpleBundleProvider(List.of(
			patientNoValues, patientFamilyNameOnly, patientGivenNameOnly, patientBothNames));
	}

	@Nonnull
	private static SimpleBundleProvider createProviderWithSomeSimpsonsAndFlanders() {
		return new SimpleBundleProvider(
			createPatientHomerSimpson(),
			createPatientNedFlanders(),
			createPatientBartSimpson(),
			createPatientLisaSimpson(),
			createPatientMaggieSimpson()
		);
	}

	@Nonnull
	private static SimpleBundleProvider createProviderWithSomeSimpsonsAndFlandersWithSomeDuplicates() {
		return new SimpleBundleProvider(
			createPatientHomerSimpson(),
			createPatientHomerSimpson(),
			createPatientNedFlanders(),
			createPatientNedFlanders(),
			createPatientBartSimpson(),
			createPatientLisaSimpson(),
			createPatientMaggieSimpson());
	}

	@Nonnull
	private static Patient createPatientMaggieSimpson() {
		Patient maggie = new Patient();
		maggie.addName().setFamily("Simpson").addGiven("Maggie").addGiven("Evelyn");
		maggie.addIdentifier().setSystem("http://system").setValue("value4");
		return maggie;
	}

	@Nonnull
	private static Patient createPatientLisaSimpson() {
		Patient lisa = new Patient();
		lisa.getMeta().setVersionId("1");
		lisa.addName().setFamily("Simpson").addGiven("Lisa").addGiven("Marie");
		lisa.addIdentifier().setSystem("http://system").setValue("value3");
		return lisa;
	}

	@Nonnull
	private static Patient createPatientBartSimpson() {
		Patient bart = new Patient();
		bart.getMeta().setVersionId("3");
		bart.addName().setFamily("Simpson").addGiven("Bart").addGiven("El Barto");
		bart.addIdentifier().setSystem("http://system").setValue("value2");
		return bart;
	}

	@Nonnull
	private static Patient createPatientNedFlanders() {
		Patient nedFlanders = new Patient();
		nedFlanders.getMeta().setVersionId("1");
		nedFlanders.addName().setFamily("Flanders").addGiven("Ned");
		nedFlanders.addIdentifier().setSystem("http://system").setValue("value1");
		return nedFlanders;
	}

	@Nonnull
	private static Patient createPatientHomerSimpson() {
		Patient homer = new Patient();
		homer.setId("HOMER0");
		homer.getMeta().setVersionId("2");
		homer.addName().setFamily("Simpson").addGiven("Homer").addGiven("Jay");
		homer.addIdentifier().setSystem("http://system").setValue("value0");
		homer.setBirthDateElement(new DateType("1950-01-01"));
		return homer;
	}

}
