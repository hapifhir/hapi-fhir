package ca.uhn.fhir.jpa.searchparam.util;

import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.searchparam.registry.SearchParameterCanonicalizer;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.param.TokenParam;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SearchParameterHelperTest {

	@Mock
	private SearchParameterCanonicalizer mockedCanonicalizer;
	@Mock
	private IBaseResource mockedSearchParam;
	@Mock
	private RuntimeSearchParam mockedRuntimeSearchParam;

	private SearchParameterHelper myTestedHelper;

	@BeforeEach
	void setUp() {
		myTestedHelper = new SearchParameterHelper(mockedCanonicalizer);
	}

	@Test
	void whenParamNonCanonicalizableReturnsEmpty() {
		when(mockedCanonicalizer.canonicalizeSearchParameter(mockedSearchParam)).thenReturn(null);

		Optional<SearchParameterMap> result = myTestedHelper.buildSearchParameterMapFromCanonical(mockedSearchParam);

		assertTrue(result.isEmpty());
	}

	@Test
	void whenParamCanonicalizableReturnsFromCanonical() {
		String codeParamValue = "code-param-value";
		String baseParamValue = "base-param-value";

		when(mockedCanonicalizer.canonicalizeSearchParameter(mockedSearchParam)).thenReturn(mockedRuntimeSearchParam);
		when(mockedRuntimeSearchParam.getName()).thenReturn(codeParamValue);
		when(mockedRuntimeSearchParam.getBase()).thenReturn(Set.of(baseParamValue));

		Optional<SearchParameterMap> result = myTestedHelper.buildSearchParameterMapFromCanonical(mockedSearchParam);

		assertTrue(result.isPresent());
		SearchParameterMap spMap = result.get();
		assertEquals(2, spMap.size());

		List<List<IQueryParameterType>> codeParam = spMap.get("code");
		assertEquals(1, codeParam.size());
		assertEquals(1, codeParam.get(0).size());
		assertTrue(codeParam.get(0).get(0) instanceof TokenParam);
		TokenParam codeTokenParam = (TokenParam) codeParam.get(0).get(0);
		assertEquals(codeParamValue, codeTokenParam.getValue());

		List<List<IQueryParameterType>> baseParam = spMap.get("base");
		assertEquals(1, baseParam.size());
		assertEquals(1, baseParam.get(0).size());
		assertTrue(baseParam.get(0).get(0) instanceof TokenParam);
		TokenParam baseTokenParam = (TokenParam) baseParam.get(0).get(0);
		assertEquals(baseParamValue, baseTokenParam.getValue());
	}
}
