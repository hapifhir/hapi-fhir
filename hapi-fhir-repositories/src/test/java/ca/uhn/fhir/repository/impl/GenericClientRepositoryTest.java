package ca.uhn.fhir.repository.impl;

import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.gclient.IQuery;
import ca.uhn.fhir.rest.gclient.IUntypedQuery;
import ca.uhn.fhir.rest.param.DateParam;
import ca.uhn.fhir.rest.param.StringParam;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseConformance;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.Test;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.List;
import java.util.Map;

import static ca.uhn.fhir.rest.api.Constants.HEADER_IF_NONE_MATCH;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyMap;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@MockitoSettings(
	strictness = Strictness.WARN
)
@SuppressWarnings("unchecked")
class GenericClientRepositoryTest {

	@Mock(answer = Answers.RETURNS_DEEP_STUBS)
	IGenericClient myGenericClient;

	@InjectMocks
	GenericClientRepository myGenericClientRepository;

	@Test
	void testCreate() {
	    // given
		IBaseResource mockResource = mock(IBaseResource.class);
		MethodOutcome stubOutcome = new MethodOutcome();
		when(myGenericClient.create().resource(mockResource).withAdditionalHeader(any(), any())).thenAnswer(Answers.RETURNS_SELF);
		when(myGenericClient.create().resource(mockResource).withAdditionalHeader(HEADER_IF_NONE_MATCH, "abc123").execute()).thenReturn(stubOutcome);

	    // when
		MethodOutcome outcome = myGenericClientRepository.create(mockResource, Map.of(HEADER_IF_NONE_MATCH, "abc123"));

	    // then check the stubs call on our myGenericClient
		verify(myGenericClient.create().resource(mockResource).withAdditionalHeader(HEADER_IF_NONE_MATCH, "abc123")).execute();
		assertEquals(stubOutcome, outcome);
	}

	@Test
	void testRead() {
		// given
		IIdType mockId = mock(IIdType.class);
		IBaseResource mockResource = mock(IBaseResource.class);
		Class<IBaseResource> resourceType = IBaseResource.class;

		when(myGenericClient.read().resource(resourceType).withId(mockId).withAdditionalHeader(any(), any())).thenAnswer(Answers.RETURNS_SELF);
		when(myGenericClient.read().resource(resourceType).withId(mockId).withAdditionalHeader(HEADER_IF_NONE_MATCH, "abc123").execute()).thenReturn(mockResource);

		// when
		IBaseResource result = myGenericClientRepository.read(resourceType, mockId, Map.of(HEADER_IF_NONE_MATCH, "abc123"));

		// then
		verify(myGenericClient.read().resource(resourceType).withId(mockId).withAdditionalHeader(HEADER_IF_NONE_MATCH, "abc123")).execute();
		assertEquals(mockResource, result);
	}

	@Test
	void testUpdate() {
		// given
		IBaseResource mockResource = mock(IBaseResource.class);
		IIdType mockId = mock(IIdType.class);
		MethodOutcome stubOutcome = new MethodOutcome();

		when(mockResource.getIdElement()).thenReturn(mockId);
		when(myGenericClient.update().resource(mockResource).withId(mockId).withAdditionalHeader(any(), any())).thenAnswer(Answers.RETURNS_SELF);
		when(myGenericClient.update().resource(mockResource).withId(mockId).withAdditionalHeader(HEADER_IF_NONE_MATCH, "abc123").execute()).thenReturn(stubOutcome);

		// when
		MethodOutcome outcome = myGenericClientRepository.update(mockResource, Map.of(HEADER_IF_NONE_MATCH, "abc123"));

		// then
		verify(myGenericClient.update().resource(mockResource).withId(mockId).withAdditionalHeader(HEADER_IF_NONE_MATCH, "abc123")).execute();
		assertEquals(stubOutcome, outcome);
	}

	@Test
	void testDelete() {
		// given
		IIdType mockId = mock(IIdType.class);
		Class<IBaseResource> resourceType = IBaseResource.class;
		MethodOutcome stubOutcome = new MethodOutcome();

		when(myGenericClient.delete().resourceById(mockId).withAdditionalHeader(any(), any())).thenAnswer(Answers.RETURNS_SELF);
		when(myGenericClient.delete().resourceById(mockId).withAdditionalHeader(HEADER_IF_NONE_MATCH, "abc123").execute()).thenReturn(stubOutcome);

		// when
		MethodOutcome outcome = myGenericClientRepository.delete(resourceType, mockId, Map.of(HEADER_IF_NONE_MATCH, "abc123"));

		// then
		verify(myGenericClient.delete().resourceById(mockId).withAdditionalHeader(HEADER_IF_NONE_MATCH, "abc123")).execute();
		assertEquals(stubOutcome, outcome);
	}

	@Test
	void testPatch() {
		// given
		IIdType mockId = mock(IIdType.class);
		IBaseParameters mockParameters = mock(IBaseParameters.class);
		MethodOutcome stubOutcome = new MethodOutcome();

		when(myGenericClient.patch().withFhirPatch(mockParameters).withId(mockId).withAdditionalHeader(any(), any())).thenAnswer(Answers.RETURNS_SELF);
		when(myGenericClient.patch().withFhirPatch(mockParameters).withId(mockId).withAdditionalHeader(HEADER_IF_NONE_MATCH, "abc123").execute()).thenReturn(stubOutcome);

		// when
		MethodOutcome outcome = myGenericClientRepository.patch(mockId, mockParameters, Map.of(HEADER_IF_NONE_MATCH, "abc123"));

		// then
		verify(myGenericClient.patch().withFhirPatch(mockParameters).withId(mockId).withAdditionalHeader(HEADER_IF_NONE_MATCH, "abc123")).execute();
		assertEquals(stubOutcome, outcome);
	}

	@Test
	void testSearchWithMultimap() {
		// given
		Bundle mockBundle = new Bundle();
		Multimap<String, List<IQueryParameterType>> searchParameters = ArrayListMultimap.create();
		List<IQueryParameterType> lowerBound = List.of(new DateParam("ge1970"));
		searchParameters.put("birthdate", lowerBound);
		List<IQueryParameterType> upperBound = List.of(new DateParam("lt1980"));
		searchParameters.put("birthdate", upperBound);

		// Simplify the test by just verifying the result
		// Use a more specific matcher for forResource to avoid ambiguity
		IQuery<Bundle> queryMock = mock(IQuery.class);
		when(myGenericClient.search().forResource(Patient.class).returnBundle(Bundle.class)).thenReturn(queryMock);
		when(queryMock.withAdditionalHeader(any(), any())).thenReturn(queryMock);
		when(queryMock.where(anyMap())).thenReturn(queryMock);
		when(queryMock.execute()).thenReturn(mockBundle);

		// when
		IBaseBundle result = myGenericClientRepository.search(Bundle.class, Patient.class, searchParameters, Map.of(HEADER_IF_NONE_MATCH, "abc123"));

		// then
		assertEquals(mockBundle, result);
		// this is important.  If there are ANDs in the search parameters, we need to ensure that the query is built correctly
		verify(queryMock).where(Map.of("birthdate", lowerBound));
		verify(queryMock).where(Map.of("birthdate", upperBound));
	}

	@Test
	void testSearchWithMap() {
		// given
		Bundle resultBundle = new Bundle();
		Map<String, List<IQueryParameterType>> searchParameters = Map.of("name", List.of(new StringParam("franklin")));

		IUntypedQuery<IBaseBundle> searchMock = mock(IUntypedQuery.class);
		IQuery<IBaseBundle> queryMock = mock(IQuery.class);
		when(myGenericClient.search()).thenReturn(searchMock);
		when(searchMock.forResource(Patient.class)).thenReturn(queryMock);
		when(queryMock.returnBundle(any())).thenReturn(queryMock);
		when(queryMock.where(searchParameters)).thenReturn(queryMock);
		when(queryMock.withAdditionalHeader(any(), any())).thenReturn(queryMock);
		when(queryMock.execute()).thenReturn(resultBundle);

		// when
		IBaseBundle result = myGenericClientRepository.search(Bundle.class, Patient.class, searchParameters);

		// then
		// Just verify the result, not the exact chain of calls
		assertEquals(resultBundle, result);
	}

	@Test
	void testCapabilities() {
		// given
		Class<IBaseConformance> conformanceType = IBaseConformance.class;
		IBaseConformance mockConformance = mock(IBaseConformance.class);

		when(myGenericClient.capabilities().ofType(conformanceType).withAdditionalHeader(any(), any())).thenAnswer(Answers.RETURNS_SELF);
		when(myGenericClient.capabilities().ofType(conformanceType).withAdditionalHeader(HEADER_IF_NONE_MATCH, "abc123").execute()).thenReturn(mockConformance);

		// when
		IBaseConformance result = myGenericClientRepository.capabilities(conformanceType, Map.of(HEADER_IF_NONE_MATCH, "abc123"));

		// then
		verify(myGenericClient.capabilities().ofType(conformanceType).withAdditionalHeader(HEADER_IF_NONE_MATCH, "abc123")).execute();
		assertEquals(mockConformance, result);
	}

	@Test
	void testTransaction() {
		// given
		IBaseBundle mockInputBundle = mock(IBaseBundle.class);
		IBaseBundle mockOutputBundle = mock(IBaseBundle.class);

		when(myGenericClient.transaction().withBundle(mockInputBundle).withAdditionalHeader(any(), any())).thenAnswer(Answers.RETURNS_SELF);
		when(myGenericClient.transaction().withBundle(mockInputBundle).withAdditionalHeader(HEADER_IF_NONE_MATCH, "abc123").execute()).thenReturn(mockOutputBundle);

		// when
		IBaseBundle result = myGenericClientRepository.transaction(mockInputBundle, Map.of(HEADER_IF_NONE_MATCH, "abc123"));

		// then
		verify(myGenericClient.transaction().withBundle(mockInputBundle).withAdditionalHeader(HEADER_IF_NONE_MATCH, "abc123")).execute();
		assertEquals(mockOutputBundle, result);
	}

	@Test
	void testLink() {
		// given
		Class<IBaseBundle> bundleType = IBaseBundle.class;
		String url = "http://example.com/fhir/Patient?_count=10";
		IBaseBundle mockBundle = mock(IBaseBundle.class);

		when(myGenericClient.loadPage().byUrl(url).andReturnBundle(bundleType).withAdditionalHeader(any(), any())).thenAnswer(Answers.RETURNS_SELF);
		when(myGenericClient.loadPage().byUrl(url).andReturnBundle(bundleType).withAdditionalHeader(HEADER_IF_NONE_MATCH, "abc123").execute()).thenReturn(mockBundle);

		// when
		IBaseBundle result = myGenericClientRepository.link(bundleType, url, Map.of(HEADER_IF_NONE_MATCH, "abc123"));

		// then
		verify(myGenericClient.loadPage().byUrl(url).andReturnBundle(bundleType).withAdditionalHeader(HEADER_IF_NONE_MATCH, "abc123")).execute();
		assertEquals(mockBundle, result);
	}

	@Test
	void testInvokeServerLevelReturningResource() {
		// given
		String operationName = "test-operation";
		IBaseParameters mockParameters = mock(IBaseParameters.class);
		Class<IBaseResource> returnType = IBaseResource.class;
		IBaseResource mockResource = mock(IBaseResource.class);

		when(myGenericClient.operation().onServer().named(operationName).withParameters(mockParameters).returnResourceType(returnType).withAdditionalHeader(any(), any())).thenAnswer(Answers.RETURNS_SELF);
		when(myGenericClient.operation().onServer().named(operationName).withParameters(mockParameters).returnResourceType(returnType).withAdditionalHeader(HEADER_IF_NONE_MATCH, "abc123").execute()).thenReturn(mockResource);

		// when
		IBaseResource result = myGenericClientRepository.invoke(operationName, mockParameters, returnType, Map.of(HEADER_IF_NONE_MATCH, "abc123"));

		// then
		verify(myGenericClient.operation().onServer().named(operationName).withParameters(mockParameters).returnResourceType(returnType).withAdditionalHeader(HEADER_IF_NONE_MATCH, "abc123")).execute();
		assertEquals(mockResource, result);
	}

	@Test
	void testInvokeServerLevelReturningMethodOutcome() {
		// given
		String operationName = "test-operation";
		IBaseParameters mockParameters = mock(IBaseParameters.class);
		MethodOutcome stubOutcome = new MethodOutcome();

		when(myGenericClient.operation().onServer().named(operationName).withParameters(mockParameters).returnMethodOutcome().withAdditionalHeader(any(), any())).thenAnswer(Answers.RETURNS_SELF);
		when(myGenericClient.operation().onServer().named(operationName).withParameters(mockParameters).returnMethodOutcome().withAdditionalHeader(HEADER_IF_NONE_MATCH, "abc123").execute()).thenReturn(stubOutcome);

		// when
		MethodOutcome result = myGenericClientRepository.invoke(operationName, mockParameters, Map.of(HEADER_IF_NONE_MATCH, "abc123"));

		// then
		verify(myGenericClient.operation().onServer().named(operationName).withParameters(mockParameters).returnMethodOutcome().withAdditionalHeader(HEADER_IF_NONE_MATCH, "abc123")).execute();
		assertEquals(stubOutcome, result);
	}

	@Test
	void testInvokeTypeLevelReturningResource() {
		// given
		Class<IBaseResource> resourceType = IBaseResource.class;
		String operationName = "test-operation";
		IBaseParameters mockParameters = mock(IBaseParameters.class);
		Class<IBaseResource> returnType = IBaseResource.class;
		IBaseResource mockResource = mock(IBaseResource.class);

		when(myGenericClient.operation().onType(resourceType).named(operationName).withParameters(mockParameters).returnResourceType(returnType).withAdditionalHeader(any(), any())).thenAnswer(Answers.RETURNS_SELF);
		when(myGenericClient.operation().onType(resourceType).named(operationName).withParameters(mockParameters).returnResourceType(returnType).withAdditionalHeader(HEADER_IF_NONE_MATCH, "abc123").execute()).thenReturn(mockResource);

		// when
		IBaseResource result = myGenericClientRepository.invoke(resourceType, operationName, mockParameters, returnType, Map.of(HEADER_IF_NONE_MATCH, "abc123"));

		// then
		verify(myGenericClient.operation().onType(resourceType).named(operationName).withParameters(mockParameters).returnResourceType(returnType).withAdditionalHeader(HEADER_IF_NONE_MATCH, "abc123")).execute();
		assertEquals(mockResource, result);
	}

	@Test
	void testInvokeTypeLevelReturningMethodOutcome() {
		// given
		Class<IBaseResource> resourceType = IBaseResource.class;
		String operationName = "test-operation";
		IBaseParameters mockParameters = mock(IBaseParameters.class);
		MethodOutcome stubOutcome = new MethodOutcome();

		when(myGenericClient.operation().onType(resourceType).named(operationName).withParameters(mockParameters).returnMethodOutcome().withAdditionalHeader(any(), any())).thenAnswer(Answers.RETURNS_SELF);
		when(myGenericClient.operation().onType(resourceType).named(operationName).withParameters(mockParameters).returnMethodOutcome().withAdditionalHeader(HEADER_IF_NONE_MATCH, "abc123").execute()).thenReturn(stubOutcome);

		// when
		MethodOutcome result = myGenericClientRepository.invoke(resourceType, operationName, mockParameters, Map.of(HEADER_IF_NONE_MATCH, "abc123"));

		// then
		verify(myGenericClient.operation().onType(resourceType).named(operationName).withParameters(mockParameters).returnMethodOutcome().withAdditionalHeader(HEADER_IF_NONE_MATCH, "abc123")).execute();
		assertEquals(stubOutcome, result);
	}

	@Test
	void testInvokeInstanceLevelReturningResource() {
		// given
		IIdType mockId = mock(IIdType.class);
		String operationName = "test-operation";
		IBaseParameters mockParameters = mock(IBaseParameters.class);
		Class<IBaseResource> returnType = IBaseResource.class;
		IBaseResource mockResource = mock(IBaseResource.class);

		when(myGenericClient.operation().onInstance(mockId).named(operationName).withParameters(mockParameters).returnResourceType(returnType).withAdditionalHeader(any(), any())).thenAnswer(Answers.RETURNS_SELF);
		when(myGenericClient.operation().onInstance(mockId).named(operationName).withParameters(mockParameters).returnResourceType(returnType).withAdditionalHeader(HEADER_IF_NONE_MATCH, "abc123").execute()).thenReturn(mockResource);

		// when
		IBaseResource result = myGenericClientRepository.invoke(mockId, operationName, mockParameters, returnType, Map.of(HEADER_IF_NONE_MATCH, "abc123"));

		// then
		verify(myGenericClient.operation().onInstance(mockId).named(operationName).withParameters(mockParameters).returnResourceType(returnType).withAdditionalHeader(HEADER_IF_NONE_MATCH, "abc123")).execute();
		assertEquals(mockResource, result);
	}

	@Test
	void testInvokeInstanceLevelReturningMethodOutcome() {
		// given
		IIdType mockId = mock(IIdType.class);
		String operationName = "test-operation";
		IBaseParameters mockParameters = mock(IBaseParameters.class);
		MethodOutcome stubOutcome = new MethodOutcome();

		when(myGenericClient.operation().onInstance(mockId).named(operationName).withParameters(mockParameters).returnMethodOutcome().withAdditionalHeader(any(), any())).thenAnswer(Answers.RETURNS_SELF);
		when(myGenericClient.operation().onInstance(mockId).named(operationName).withParameters(mockParameters).returnMethodOutcome().withAdditionalHeader(HEADER_IF_NONE_MATCH, "abc123").execute()).thenReturn(stubOutcome);

		// when
		MethodOutcome result = myGenericClientRepository.invoke(mockId, operationName, mockParameters, Map.of(HEADER_IF_NONE_MATCH, "abc123"));

		// then
		verify(myGenericClient.operation().onInstance(mockId).named(operationName).withParameters(mockParameters).returnMethodOutcome().withAdditionalHeader(HEADER_IF_NONE_MATCH, "abc123")).execute();
		assertEquals(stubOutcome, result);
	}

}
