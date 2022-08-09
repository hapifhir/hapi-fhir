package ca.uhn.fhir.jpa.interceptor.validation;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.searchparam.registry.SearchParameterCanonicalizer;
import ca.uhn.fhir.jpa.searchparam.submit.interceptor.SearchParamValidatingInterceptor;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.SearchParameter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SearchParameterValidatingInterceptorTest {

	static final FhirContext ourFhirContext = FhirContext.forR4();

	@Mock
	RequestDetails myRequestDetails;

	@Mock
	IFhirResourceDao myIFhirResourceDao;

	@Mock
	DaoRegistry myDaoRegistry;

	SearchParamValidatingInterceptor mySearchParamValidatingInterceptor;

	@BeforeEach
	public void beforeEach(){

		mySearchParamValidatingInterceptor = new SearchParamValidatingInterceptor();
		mySearchParamValidatingInterceptor.setFhirContext(ourFhirContext);
		mySearchParamValidatingInterceptor.setSearchParameterCanonicalizer(new SearchParameterCanonicalizer(ourFhirContext));
		mySearchParamValidatingInterceptor.setDaoRegistry(myDaoRegistry);
	}

	@Test
	public void whenValidatingInterceptorCalledForNonSearchParamResoucre_thenNoException(){
		Patient patient = new Patient();

		mySearchParamValidatingInterceptor.resourcePreCreate(patient, null);
		mySearchParamValidatingInterceptor.resourcePreUpdate(null, patient, null);
	}

	@Test
	public void whenCreatingNonOverlappingSearchParam_thenNoException(){

		when(myDaoRegistry.getResourceDao(eq(SearchParamValidatingInterceptor.SEARCH_PARAM))).thenReturn(myIFhirResourceDao);
		when(myIFhirResourceDao.searchForIds(any(), any())).thenReturn(Collections.emptyList());

		SearchParameter newSearchParam = aSearchParameter("ID1", "patient", asList("AllergyIntolerance"));

		mySearchParamValidatingInterceptor.resourcePreCreate(newSearchParam, myRequestDetails);

	}

	@Test
	public void whenCreatingOverlappingSearchParam_thenExceptionIsThrown(){

		when(myDaoRegistry.getResourceDao(eq(SearchParamValidatingInterceptor.SEARCH_PARAM))).thenReturn(myIFhirResourceDao);
		when(myIFhirResourceDao.searchForIds(any(), any())).thenReturn(asList(new ResourcePersistentId("ID1")));

		SearchParameter newSearchParam = aSearchParameter("ID2", "patient", asList("AllergyIntolerance"));

		try {
			mySearchParamValidatingInterceptor.resourcePreCreate(newSearchParam, myRequestDetails);
			fail();
		}catch (UnprocessableEntityException e){
			assertTrue(e.getMessage().contains("2131"));
		}

	}

	@Test
	public void whenPutOperationToCreateNonOverlappingSearchParam_thenSuccess(){

		when(myDaoRegistry.getResourceDao(eq(SearchParamValidatingInterceptor.SEARCH_PARAM))).thenReturn(myIFhirResourceDao);
		when(myIFhirResourceDao.searchForIds(any(), any())).thenReturn(Collections.emptyList());

		SearchParameter newSearchParam = aSearchParameter("ID1", "patient", asList("AllergyIntolerance"));

		mySearchParamValidatingInterceptor.resourcePreUpdate(null, newSearchParam, myRequestDetails);
	}

	@Test
	public void whenUsingPutOperationToCreateOverlappingSearchParam_thenExceptionIsThrown(){

		when(myDaoRegistry.getResourceDao(eq(SearchParamValidatingInterceptor.SEARCH_PARAM))).thenReturn(myIFhirResourceDao);
		when(myIFhirResourceDao.searchForIds(any(), any())).thenReturn(asList(new ResourcePersistentId("ID1")));

		SearchParameter newSearchParam = aSearchParameter("ID2", "patient", asList("AllergyIntolerance"));

		try {
			mySearchParamValidatingInterceptor.resourcePreUpdate(null, newSearchParam, myRequestDetails);
			fail();
		}catch (UnprocessableEntityException e){
			assertTrue(e.getMessage().contains("2132"));
		}
	}


	private SearchParameter aSearchParameter(String id, String code, List<String> baseList) {
		SearchParameter retVal = new SearchParameter();
		retVal.setId(id);
		retVal.setCode(code);
		baseList.forEach(retVal::addBase);
		retVal.setStatus(Enumerations.PublicationStatus.DRAFT);
		retVal.setType(Enumerations.SearchParamType.REFERENCE);
		retVal.setExpression("AllergyIntolerance.patient.where(resolve() is Patient)");

		return retVal;
	}


}
