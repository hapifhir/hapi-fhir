package ca.uhn.fhir.jpa.interceptor.validation;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.svc.IIdHelperService;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.jpa.searchparam.registry.SearchParameterCanonicalizer;
import ca.uhn.fhir.jpa.searchparam.submit.interceptor.SearchParamValidatingInterceptor;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.storage.IResourcePersistentId;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.fhir.util.HapiExtensions;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.Extension;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.SearchParameter;
import org.hl7.fhir.r4.model.SimpleQuantity;
import org.hl7.fhir.r4.model.StringType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SearchParameterValidatingInterceptorTest {

	static final FhirContext ourFhirContext = FhirContext.forR4Cached();
	static String ID1 = "ID1";
	static String ID2 = "ID2";
	@Mock
	RequestDetails myRequestDetails;
	@Mock
	IFhirResourceDao myIFhirResourceDao;
	@Mock
	DaoRegistry myDaoRegistry;
	@Mock
	IIdHelperService myIdHelperService;
	SearchParamValidatingInterceptor mySearchParamValidatingInterceptor;
	SearchParameter myExistingSearchParameter;

	@BeforeEach
	public void beforeEach() {

		mySearchParamValidatingInterceptor = new SearchParamValidatingInterceptor();
		mySearchParamValidatingInterceptor.setFhirContext(ourFhirContext);
		mySearchParamValidatingInterceptor.setSearchParameterCanonicalizer(new SearchParameterCanonicalizer(ourFhirContext));
		mySearchParamValidatingInterceptor.setIIDHelperService(myIdHelperService);
		mySearchParamValidatingInterceptor.setDaoRegistry(myDaoRegistry);

		myExistingSearchParameter = buildSearchParameterWithId(ID1);

	}

	@Test
	public void whenValidatingInterceptorCalledForNonSearchParamResoucre_thenIsAllowed() {
		Patient patient = new Patient();

		mySearchParamValidatingInterceptor.resourcePreCreate(patient, null);
		mySearchParamValidatingInterceptor.resourcePreUpdate(null, patient, null);
	}

	@Test
	public void whenCreatingNonOverlappingSearchParam_thenIsAllowed() {
		when(myDaoRegistry.getResourceDao(eq(SearchParamValidatingInterceptor.SEARCH_PARAM))).thenReturn(myIFhirResourceDao);

		setPersistedSearchParameterIds(emptyList());

		SearchParameter newSearchParam = buildSearchParameterWithId(ID1);

		mySearchParamValidatingInterceptor.resourcePreCreate(newSearchParam, myRequestDetails);

	}

	@Test
	public void whenCreatingOverlappingSearchParam_thenExceptionIsThrown() {
		when(myDaoRegistry.getResourceDao(eq(SearchParamValidatingInterceptor.SEARCH_PARAM))).thenReturn(myIFhirResourceDao);

		setPersistedSearchParameterIds(asList(myExistingSearchParameter));

		SearchParameter newSearchParam = buildSearchParameterWithId(ID2);

		try {
			mySearchParamValidatingInterceptor.resourcePreCreate(newSearchParam, myRequestDetails);
			fail();
		} catch (UnprocessableEntityException e) {
			assertThat(e.getMessage()).contains("2196");
		}

	}
	@Test
	public void whenCreateSpWithUpliftRefchains_Bad_WrongCodeDatatype() {
		SearchParameter sp = buildReferenceSearchParameter();
		Extension upliftRefChain = sp.addExtension().setUrl(HapiExtensions.EXTENSION_SEARCHPARAM_UPLIFT_REFCHAIN);
		upliftRefChain.addExtension(HapiExtensions.EXTENSION_SEARCHPARAM_UPLIFT_REFCHAIN_PARAM_CODE, new SimpleQuantity().setValue(123L));
		upliftRefChain.addExtension(HapiExtensions.EXTENSION_SEARCHPARAM_UPLIFT_REFCHAIN_ELEMENT_NAME, new StringType("element1"));
		try {
			mySearchParamValidatingInterceptor.resourcePreCreate(sp, myRequestDetails);
			fail();
		} catch (UnprocessableEntityException e) {
			assertThat(e.getMessage()).contains("2284");
		}

	}

	@Test
	public void whenCreateSpWithUpliftRefchains_Bad_NoCode() {
		SearchParameter sp = buildReferenceSearchParameter();
		Extension upliftRefChain = sp.addExtension().setUrl(HapiExtensions.EXTENSION_SEARCHPARAM_UPLIFT_REFCHAIN);
		upliftRefChain.addExtension(HapiExtensions.EXTENSION_SEARCHPARAM_UPLIFT_REFCHAIN_ELEMENT_NAME, new StringType("element1"));
		try {
			mySearchParamValidatingInterceptor.resourcePreCreate(sp, myRequestDetails);
			fail();
		} catch (UnprocessableEntityException e) {
			assertThat(e.getMessage()).contains("2283");
		}

	}

	@Nonnull
	private static SearchParameter buildReferenceSearchParameter() {
		SearchParameter sp = new SearchParameter();
		sp.setCode("subject");
		sp.setName("subject");
		sp.setDescription("Modified Subject");
		sp.setStatus(Enumerations.PublicationStatus.ACTIVE);
		sp.setType(Enumerations.SearchParamType.REFERENCE);
		sp.setExpression("Encounter.subject");
		sp.addBase("Encounter");
		sp.addTarget("Patient");
		return sp;
	}

	@Test
	public void whenUsingPutOperationToCreateNonOverlappingSearchParam_thenIsAllowed() {
		when(myDaoRegistry.getResourceDao(eq(SearchParamValidatingInterceptor.SEARCH_PARAM))).thenReturn(myIFhirResourceDao);

		setPersistedSearchParameterIds(emptyList());

		SearchParameter newSearchParam = buildSearchParameterWithId(ID1);

		mySearchParamValidatingInterceptor.resourcePreUpdate(null, newSearchParam, myRequestDetails);
	}

	@Test
	public void whenUsingPutOperationToCreateOverlappingSearchParam_thenExceptionIsThrown() {
		when(myDaoRegistry.getResourceDao(eq(SearchParamValidatingInterceptor.SEARCH_PARAM))).thenReturn(myIFhirResourceDao);

		setPersistedSearchParameterIds(asList(myExistingSearchParameter));

		SearchParameter newSearchParam = buildSearchParameterWithId(ID2);

		try {
			mySearchParamValidatingInterceptor.resourcePreUpdate(null, newSearchParam, myRequestDetails);
			fail();
		} catch (UnprocessableEntityException e) {
			assertThat(e.getMessage()).contains("2125");
		}
	}

	@Test
	public void whenUpdateSearchParam_thenIsAllowed() {
		when(myDaoRegistry.getResourceDao(eq(SearchParamValidatingInterceptor.SEARCH_PARAM))).thenReturn(myIFhirResourceDao);

		setPersistedSearchParameterIds(asList(myExistingSearchParameter));
		when(myIdHelperService.translatePidsToFhirResourceIds(any())).thenReturn(Set.of(myExistingSearchParameter.getId()));


		SearchParameter newSearchParam = buildSearchParameterWithId(ID1);

		mySearchParamValidatingInterceptor.resourcePreUpdate(null, newSearchParam, myRequestDetails);

	}

	private void setPersistedSearchParameterIds(List<SearchParameter> theSearchParams) {
		final AtomicLong counter = new AtomicLong();
		List<IResourcePersistentId> resourcePersistentIds = theSearchParams
			.stream()
			.map(SearchParameter::getId)
			.map(s -> JpaPid.fromId(counter.incrementAndGet()))
			.collect(Collectors.toList());
		when(myIFhirResourceDao.searchForIds(any(), any())).thenReturn(resourcePersistentIds);
	}


	private SearchParameter buildSearchParameterWithId(String id) {
		SearchParameter retVal = new SearchParameter();
		retVal.setId(id);
		retVal.setCode("patient");
		retVal.addBase("AllergyIntolerance");
		retVal.setStatus(Enumerations.PublicationStatus.DRAFT);
		retVal.setType(Enumerations.SearchParamType.REFERENCE);
		retVal.setExpression("AllergyIntolerance.patient.where(resolve() is Patient)");

		return retVal;
	}

}
