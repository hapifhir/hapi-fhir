package ca.uhn.fhir.jpa.fql.executor;

import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.param.DateParam;
import ca.uhn.fhir.rest.param.ParamPrefixEnum;
import ca.uhn.fhir.rest.param.TokenParam;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * We should auto-translate FHIRPath expressions like
 * <code>id</code> or <code>meta.lastUpdated</code>
 * to an equivalent search parameter since that's more efficient
 */
@ExtendWith(MockitoExtension.class)
public class HfqlExecutorFhirPathTranslationToSearchParamTest extends BaseHfqlExecutorTest {

	@ParameterizedTest
	@CsvSource(textBlock = """
		id          , true
		Resource.id , true
		Resource.id , true
		foo.id      , false
		"""
	)
	public void testId(String theExpression, boolean theShouldConvert) {
		IFhirResourceDao<Patient> patientDao = initDao(Patient.class);
		when(patientDao.search(any(), any())).thenReturn(createProviderWithSomeSimpsonsAndFlanders());

		String statement = """
			SELECT
			   id, birthDate, meta.lastUpdated
			FROM
			   Patient
			WHERE
			   id = 'ABC123'
			""";
		statement = statement.replace(" id =", " " + theExpression + " =");

		myHfqlExecutor.executeInitialSearch(statement, null, mySrd);

		verify(patientDao, times(1)).search(mySearchParameterMapCaptor.capture(), any());
		SearchParameterMap map = mySearchParameterMapCaptor.getValue();
		if (theShouldConvert) {
			assertThat(map.get("_id")).hasSize(1);
			assertThat(map.get("_id").get(0)).hasSize(1);
			assertNull(((TokenParam) map.get("_id").get(0).get(0)).getSystem());
			assertEquals("ABC123", ((TokenParam) map.get("_id").get(0).get(0)).getValue());
		} else {
			assertNull(map.get("_id"));
		}
	}

	@ParameterizedTest
	@CsvSource(textBlock = """
		meta.lastUpdated = '2023'  , 2023 ,
		meta.lastUpdated > '2023'  , 2023 , GREATERTHAN
		meta.lastUpdated >= '2023' , 2023 , GREATERTHAN_OR_EQUALS
		meta.lastUpdated < '2023'  , 2023 , LESSTHAN
		meta.lastUpdated <= '2023' , 2023 , LESSTHAN_OR_EQUALS
		meta.lastUpdated != '2023' , 2023 , NOT_EQUAL
		meta.lastUpdated ~ '2023'  , 2023 , APPROXIMATE
		"""
	)
	public void testLastUpdated(String theExpression, String theExpectedParamValue, ParamPrefixEnum theExpectedParamPrefix) {
		IFhirResourceDao<Patient> patientDao = initDao(Patient.class);
		when(patientDao.search(any(), any())).thenReturn(createProviderWithSomeSimpsonsAndFlanders());

		String statement = """
			SELECT
			   id, birthDate, meta.lastUpdated
			FROM
			   Patient
			WHERE
			   meta.lastUpdated = '2023'
			""";
		statement = statement.replace("meta.lastUpdated = '2023'", theExpression);

		myHfqlExecutor.executeInitialSearch(statement, null, mySrd);

		verify(patientDao, times(1)).search(mySearchParameterMapCaptor.capture(), any());
		SearchParameterMap map = mySearchParameterMapCaptor.getValue();
		assertThat(map.get("_lastUpdated")).hasSize(1);
		assertThat(map.get("_lastUpdated").get(0)).hasSize(1);
		assertEquals(theExpectedParamValue, ((DateParam) map.get("_lastUpdated").get(0).get(0)).getValueAsString());
		assertEquals(theExpectedParamPrefix, ((DateParam) map.get("_lastUpdated").get(0).get(0)).getPrefix());
	}


}
