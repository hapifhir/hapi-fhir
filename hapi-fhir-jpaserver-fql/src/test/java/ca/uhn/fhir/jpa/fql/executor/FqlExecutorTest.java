package ca.uhn.fhir.jpa.fql.executor;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.fql.parser.FqlStatement;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.param.DateParam;
import ca.uhn.fhir.rest.param.ParamPrefixEnum;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.IPagingProvider;
import ca.uhn.fhir.rest.server.SimpleBundleProvider;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.util.FhirContextSearchParamRegistry;
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.annotation.Nonnull;
import java.sql.Types;
import java.util.List;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FqlExecutorTest {

	private final RequestDetails mySrd = new SystemRequestDetails();
	@Spy
	private FhirContext myCtx = FhirContext.forR4Cached();
	@Spy
	private ISearchParamRegistry mySearchParamRegistry = new FhirContextSearchParamRegistry(myCtx);
	@Mock
	private DaoRegistry myDaoRegistry;
	@Mock
	private IPagingProvider myPagingProvider;
	@InjectMocks
	private FqlExecutor myFqlExecutor = new FqlExecutor();
	@Captor
	private ArgumentCaptor<SearchParameterMap> mySearchParameterMapCaptor;

	@Test
	public void testContinuation() {
		// Setup
		FqlStatement statement = new FqlStatement();
		statement.setFromResourceName("Patient");
		statement.addSelectClause("name.given[1]");
		statement.addSelectClause("name.family");
		statement.addWhereClause("name.family", FqlStatement.WhereClauseOperator.EQUALS, "'Simpson'");

		String searchId = "the-search-id";
		when(myPagingProvider.retrieveResultList(any(), eq(searchId))).thenReturn(createSomeSimpsonsAndFlanders());

		// Test
		IFqlExecutionResult result = myFqlExecutor.executeContinuation(statement, searchId, 3, 100, mySrd);

		// Verify
		assertThat(result.getColumnNames(), contains(
			"name.given[1]", "name.family"
		));
		assertTrue(result.hasNext());
		IFqlExecutionResult.Row nextRow = result.getNextRow();
		assertEquals(3, nextRow.getRowOffset());
		assertThat(nextRow.getRowValues(), contains("Marie", "Simpson"));
		assertTrue(result.hasNext());
		nextRow = result.getNextRow();
		assertEquals(4, nextRow.getRowOffset());
		assertThat(nextRow.getRowValues(), contains("Evelyn", "Simpson"));
		assertFalse(result.hasNext());

	}


	@Test
	public void testFromSelect() {
		IFhirResourceDao<Patient> patientDao = initDao(Patient.class);
		when(patientDao.search(any(), any())).thenReturn(createSomeSimpsonsAndFlanders());

		String statement = """
					from Patient
					where name.family = 'Simpson'
					select name.given[1], name.family
			""";

		IFqlExecutionResult.Row nextRow;
		IFqlExecutionResult result = myFqlExecutor.executeInitialSearch(statement, null, mySrd);
		assertThat(result.getColumnNames(), contains(
			"name.given[1]", "name.family"
		));
		assertTrue(result.hasNext());
		nextRow = result.getNextRow();
		assertEquals(0, nextRow.getRowOffset());
		assertThat(nextRow.getRowValues(), contains("Jay", "Simpson"));
		assertTrue(result.hasNext());
		nextRow = result.getNextRow();
		assertEquals(2, nextRow.getRowOffset());
		assertThat(nextRow.getRowValues(), contains("El Barto", "Simpson"));
		assertTrue(result.hasNext());

		verify(patientDao, times(1)).search(mySearchParameterMapCaptor.capture(), any());
		// Default count
		assertNull(mySearchParameterMapCaptor.getValue().getCount());
	}

	@Test
	public void testFromSelectStar() {
		IFhirResourceDao<Patient> patientDao = initDao(Patient.class);
		when(patientDao.search(any(), any())).thenReturn(createSomeSimpsonsAndFlanders());

		String statement = """
					from Patient
					where name.family = 'Simpson'
					select *
			""";

		IFqlExecutionResult.Row nextRow;
		IFqlExecutionResult result = myFqlExecutor.executeInitialSearch(statement, null, mySrd);
		assertThat(result.getColumnNames().toString(), result.getColumnNames(), hasItems(
			"active", "address.city", "address.country"
		));
		assertThat(result.getColumnNames().toString(), result.getColumnNames(), not(hasItem(
			"address.period.start"
		)));
	}

	@Test
	public void testFromSelectComplexFhirPath() {
		IFhirResourceDao<Patient> patientDao = initDao(Patient.class);
		when(patientDao.search(any(), any())).thenReturn(createSomeSimpsonsAndFlanders());

		String statement = """
					from Patient
					where name.family = 'Simpson'
					select name.given, identifier.where(system = 'http://system' ).value
			""";

		IFqlExecutionResult.Row nextRow;
		IFqlExecutionResult result = myFqlExecutor.executeInitialSearch(statement, null, mySrd);
		assertThat(result.getColumnNames().toString(), result.getColumnNames(), hasItems(
			"name.given", "identifier.where(system = 'http://system' ).value"
		));
		nextRow = result.getNextRow();

		assertEquals("Homer", nextRow.getRowValues().get(0));
		assertEquals("value0", nextRow.getRowValues().get(1));
	}

	@Test
	public void testFromWhereComplexFhirPath() {
		IFhirResourceDao<Patient> patientDao = initDao(Patient.class);
		when(patientDao.search(any(), any())).thenReturn(createSomeSimpsonsAndFlanders());

		String statement = """
					from Patient
					where identifier.where(system = 'http://system' ).value = 'value0'
					select name.given, identifier.value
			""";

		IFqlExecutionResult.Row nextRow;
		IFqlExecutionResult result = myFqlExecutor.executeInitialSearch(statement, null, mySrd);
		assertThat(result.getColumnNames().toString(), result.getColumnNames(), hasItems(
			"name.given", "identifier.value"
		));
		nextRow = result.getNextRow();

		assertEquals("Homer", nextRow.getRowValues().get(0));
		assertEquals("value0", nextRow.getRowValues().get(1));
		assertFalse(result.hasNext());
	}

	@Test
	public void testFromWhereSelectIn() {
		IFhirResourceDao<Patient> patientDao = initDao(Patient.class);
		when(patientDao.search(any(), any())).thenReturn(createSomeSimpsonsAndFlanders());

		String statement = """
					from Patient
					where name.given in ('Foo' | 'Bart')
					select Given:name.given[1], Family:name.family
			""";

		IFqlExecutionResult result = myFqlExecutor.executeInitialSearch(statement, null, mySrd);
		assertThat(result.getColumnNames(), contains(
			"Given", "Family"
		));
		assertTrue(result.hasNext());
		IFqlExecutionResult.Row nextRow = result.getNextRow();
		assertEquals(2, nextRow.getRowOffset());
		assertThat(nextRow.getRowValues(), contains("El Barto", "Simpson"));
		assertFalse(result.hasNext());

	}

	@Test
	public void testFromWhereSelectEquals() {
		IFhirResourceDao<Patient> patientDao = initDao(Patient.class);
		when(patientDao.search(any(), any())).thenReturn(createSomeSimpsonsAndFlanders());

		String statement = """
					from Patient
					where name.given = 'Homer'
					select Given:name.given[1], Family:name.family
			""";

		IFqlExecutionResult result = myFqlExecutor.executeInitialSearch(statement, null, mySrd);
		assertThat(result.getColumnNames(), contains(
			"Given", "Family"
		));
		assertTrue(result.hasNext());
		IFqlExecutionResult.Row row = result.getNextRow();
		assertEquals(0, row.getRowOffset());
		assertThat(row.getRowValues(), contains("Jay", "Simpson"));
		assertFalse(result.hasNext());

	}

	@Test
	public void testIntrospectTables() {
		IFqlExecutionResult tables = myFqlExecutor.introspectTables();
		assertEquals("TABLE_NAME", tables.getColumnNames().get(2));
		assertTrue(tables.hasNext());
		assertEquals("Account", tables.getNextRow().getRowValues().get(2));
	}

	@Test
	public void testIntrospectColumns_NoSelector() {
		IFqlExecutionResult tables = myFqlExecutor.introspectColumns(null, null);
		assertEquals("TABLE_NAME", tables.getColumnNames().get(2), tables.getColumnNames().toString());
		assertEquals("COLUMN_NAME", tables.getColumnNames().get(3), tables.getColumnNames().toString());
		assertEquals("DATA_TYPE", tables.getColumnNames().get(4), tables.getColumnNames().toString());
		assertTrue(tables.hasNext());
		assertEquals("Account", tables.getNextRow().getRowValues().get(2));
		assertEquals("description", tables.getNextRow().getRowValues().get(3));
		assertEquals(Types.VARCHAR, tables.getNextRow().getRowValues().get(4));
	}

	@Test
	public void testIntrospectColumns_TableSelector() {
		IFqlExecutionResult tables = myFqlExecutor.introspectColumns("Patient", null);
		assertEquals("TABLE_NAME", tables.getColumnNames().get(2), tables.getColumnNames().toString());
		assertEquals("COLUMN_NAME", tables.getColumnNames().get(3), tables.getColumnNames().toString());
		assertEquals("DATA_TYPE", tables.getColumnNames().get(4), tables.getColumnNames().toString());
		assertTrue(tables.hasNext());
		assertEquals("Patient", tables.getNextRow().getRowValues().get(2));
		assertEquals("address.city", tables.getNextRow().getRowValues().get(3));
		assertEquals(Types.VARCHAR, tables.getNextRow().getRowValues().get(4));
	}

	@ValueSource(strings = {
		"_blah", "foo"
	})
	@ParameterizedTest
	public void testSearch_Error_UnknownParam(String theParamName) {
		initDao(Patient.class);

		String statement = "from Patient " +
			"search " + theParamName + " = 'abc' " +
			"select name.given";

		try {
			myFqlExecutor.executeInitialSearch(statement, null, mySrd);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("Unknown/unsupported search parameter: " + theParamName, e.getMessage());
		}
	}


	@Test
	public void testSearch_Id_In() {
		IFhirResourceDao<Patient> patientDao = initDao(Patient.class);
		when(patientDao.search(any(), any())).thenReturn(createSomeSimpsonsAndFlanders());

		String statement = """
					from Patient
					search _id in ('123' | 'Patient/456')
					select name.given
			""";

		myFqlExecutor.executeInitialSearch(statement, null, mySrd);

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
	public void testSearch_LastUpdated_In() {
		IFhirResourceDao<Patient> patientDao = initDao(Patient.class);
		when(patientDao.search(any(), any())).thenReturn(createSomeSimpsonsAndFlanders());

		String statement = """
					from Patient
					search _lastUpdated in ('lt2021' | 'gt2023')
					select name.given
			""";

		myFqlExecutor.executeInitialSearch(statement, null, mySrd);

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
	public void testSearch_String() {
		IFhirResourceDao<Patient> patientDao = initDao(Patient.class);
		when(patientDao.search(any(), any())).thenReturn(createSomeSimpsonsAndFlanders());

		String statement = """
					from Patient
					search name = 'abc'
					select name.given
			""";

		myFqlExecutor.executeInitialSearch(statement, null, mySrd);

		verify(patientDao, times(1)).search(mySearchParameterMapCaptor.capture(), any());
		SearchParameterMap map = mySearchParameterMapCaptor.getValue();
		assertEquals(1, map.get("name").size());
		assertEquals(1, map.get("name").get(0).size());
		assertEquals("abc", ((StringParam) map.get("name").get(0).get(0)).getValue());
	}

	@Test
	public void testSearch_String_Exact() {
		IFhirResourceDao<Patient> patientDao = initDao(Patient.class);
		when(patientDao.search(any(), any())).thenReturn(createSomeSimpsonsAndFlanders());

		String statement = """
					from Patient
					search name:exact = 'abc'
					select name.given
			""";

		myFqlExecutor.executeInitialSearch(statement, null, mySrd);

		verify(patientDao, times(1)).search(mySearchParameterMapCaptor.capture(), any());
		SearchParameterMap map = mySearchParameterMapCaptor.getValue();
		assertEquals(1, map.get("name").size());
		assertEquals(1, map.get("name").get(0).size());
		assertEquals("abc", ((StringParam) map.get("name").get(0).get(0)).getValue());
		assertTrue(((StringParam) map.get("name").get(0).get(0)).isExact());
	}

	@Test
	public void testSearch_String_AndOr() {
		IFhirResourceDao<Patient> patientDao = initDao(Patient.class);
		when(patientDao.search(any(), any())).thenReturn(createSomeSimpsonsAndFlanders());

		String statement = """
					from Patient
					search name in ('A' | 'B') and name in ('C' | 'D')
					select name.given
			""";

		myFqlExecutor.executeInitialSearch(statement, null, mySrd);

		verify(patientDao, times(1)).search(mySearchParameterMapCaptor.capture(), any());
		SearchParameterMap map = mySearchParameterMapCaptor.getValue();
		assertEquals(2, map.get("name").size());
		assertEquals(2, map.get("name").get(0).size());
		assertEquals("A", ((StringParam) map.get("name").get(0).get(0)).getValue());
		assertEquals("B", ((StringParam) map.get("name").get(0).get(1)).getValue());
		assertEquals("C", ((StringParam) map.get("name").get(1).get(0)).getValue());
		assertEquals("D", ((StringParam) map.get("name").get(1).get(1)).getValue());
	}

	@Test
	public void testError() {
		String input = """
			from Foo
			select Foo.blah
			""";

		assertEquals("Invalid FROM statement. Unknown resource type 'Foo' at position: [line=0, column=5]",
			assertThrows(DataFormatException.class, () -> myFqlExecutor.executeInitialSearch(input, null, mySrd)).getMessage());
	}

	@SuppressWarnings("unchecked")
	private <T extends IBaseResource> IFhirResourceDao<T> initDao(Class<T> theType) {
		IFhirResourceDao<T> retVal = mock(IFhirResourceDao.class);
		String type = myCtx.getResourceType(theType);
		when(myDaoRegistry.getResourceDao(type)).thenReturn(retVal);
		return retVal;
	}

	@Nonnull
	private static SimpleBundleProvider createSomeSimpsonsAndFlanders() {
		Patient homer = new Patient();
		homer.addName().setFamily("Simpson").addGiven("Homer").addGiven("Jay");
		homer.addIdentifier().setSystem("http://system").setValue("value0");

		Patient nedFlanders = new Patient();
		nedFlanders.addName().setFamily("Flanders").addGiven("Ned");
		homer.addIdentifier().setSystem("http://system").setValue("value1");

		Patient bart = new Patient();
		bart.addName().setFamily("Simpson").addGiven("Bart").addGiven("El Barto");
		homer.addIdentifier().setSystem("http://system").setValue("value2");

		Patient lisa = new Patient();
		lisa.addName().setFamily("Simpson").addGiven("Lisa").addGiven("Marie");
		homer.addIdentifier().setSystem("http://system").setValue("value3");

		Patient maggie = new Patient();
		maggie.addName().setFamily("Simpson").addGiven("Maggie").addGiven("Evelyn");
		homer.addIdentifier().setSystem("http://system").setValue("value4");

		return new SimpleBundleProvider(List.of(
			homer, nedFlanders, bart, lisa, maggie
		));
	}

}
