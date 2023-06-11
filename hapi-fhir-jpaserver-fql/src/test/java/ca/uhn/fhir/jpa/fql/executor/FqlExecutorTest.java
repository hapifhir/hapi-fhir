package ca.uhn.fhir.jpa.fql.executor;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.fql.provider.FqlRestProviderTest;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.server.SimpleBundleProvider;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.annotation.Nonnull;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FqlExecutorTest {

	@Spy
	private FhirContext myCtx = FhirContext.forR4Cached();

	@Mock
	private DaoRegistry myDaoRegistry;

	@InjectMocks
	private FqlExecutor myFqlExecutor = new FqlExecutor();
	private final RequestDetails mySrd = new SystemRequestDetails();
	@Captor
	private ArgumentCaptor<SearchParameterMap> mySearchParameterMapCaptor;

	@Test
	public void testFromSelect() {
		IFhirResourceDao<Patient> patientDao = initDao(Patient.class);
		when(patientDao.search(any(), any())).thenReturn(createTwoSimpsons());

		String statement = """
					from Patient
					select name.given[1], name.family
			""";

		IFqlResult result = myFqlExecutor.execute(statement, null, mySrd);
		assertThat(result.getColumnNames(), contains(
			"name.given[1]", "name.family"
		));
		assertTrue(result.hasNext());
		assertThat(result.getNextRowAsStrings(), contains("Jay", "Simpson"));
		assertTrue(result.hasNext());
		assertThat(result.getNextRowAsStrings(), contains("El Barto", "Simpson"));
		assertFalse(result.hasNext());

		verify(patientDao, times(1)).search(mySearchParameterMapCaptor.capture(), any());
		// Default count
		assertEquals(1000, mySearchParameterMapCaptor.getValue().getCount());
	}

	@Test
	public void testFromWhereSelectIn() {
		IFhirResourceDao<Patient> patientDao = initDao(Patient.class);
		when(patientDao.search(any(), any())).thenReturn(createTwoSimpsons());

		String statement = """
					from Patient
					where name.given in ('Foo' | 'Homer')
					select Given:name.given[1], Family:name.family
			""";

		IFqlResult result = myFqlExecutor.execute(statement, null, mySrd);
		assertThat(result.getColumnNames(), contains(
			"Given", "Family"
		));
		assertTrue(result.hasNext());
		assertThat(result.getNextRowAsStrings(), contains("Jay", "Simpson"));
		assertFalse(result.hasNext());

	}

	@Test
	public void testFromWhereSelectEquals() {
		IFhirResourceDao<Patient> patientDao = initDao(Patient.class);
		when(patientDao.search(any(), any())).thenReturn(createTwoSimpsons());

		String statement = """
					from Patient
					where name.given = 'Homer'
					select Given:name.given[1], Family:name.family
			""";

		IFqlResult result = myFqlExecutor.execute(statement, null, mySrd);
		assertThat(result.getColumnNames(), contains(
			"Given", "Family"
		));
		assertTrue(result.hasNext());
		assertThat(result.getNextRowAsStrings(), contains("Jay", "Simpson"));
		assertFalse(result.hasNext());

	}

	@Nonnull
	private static SimpleBundleProvider createTwoSimpsons() {
		Patient patient0 = createHomerSimpson();
		Patient patient1 = createBartSimpson();
		SimpleBundleProvider provider = new SimpleBundleProvider(List.of(patient0, patient1));
		return provider;
	}

	@Test
	public void testError() {
		String input = """
			from Foo
			select Foo.blah
			""";

		assertEquals("Invalid FROM statement. Unknown resource type 'Foo' at position: [line=0, column=5]",
			assertThrows(DataFormatException.class, () -> myFqlExecutor.execute(input, null, mySrd)).getMessage());
	}

	@SuppressWarnings("unchecked")
	private <T extends IBaseResource> IFhirResourceDao<T> initDao(Class<T> theType) {
		IFhirResourceDao<T> retVal = mock(IFhirResourceDao.class);
		String type = myCtx.getResourceType(theType);
		when(myDaoRegistry.getResourceDao(type)).thenReturn(retVal);
		return retVal;
	}

	@Nonnull
	private static Patient createBartSimpson() {
		Patient retVal = new Patient();
		retVal.addName().setFamily("Simpson").addGiven("Bart").addGiven("El Barto");
		return retVal;
	}

	@Nonnull
	private static Patient createHomerSimpson() {
		Patient retVal = new Patient();
		retVal.addName().setFamily("Simpson").addGiven("Homer").addGiven("Jay");
		return retVal;
	}

}
