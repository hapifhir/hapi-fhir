package ca.uhn.fhir.cr.r4;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.cr.repo.SearchConverter;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.param.NumberAndListParam;
import ca.uhn.fhir.rest.param.NumberOrListParam;
import ca.uhn.fhir.rest.param.SpecialAndListParam;
import ca.uhn.fhir.rest.param.SpecialOrListParam;
import ca.uhn.fhir.rest.param.TokenAndListParam;
import ca.uhn.fhir.rest.param.TokenOrListParam;
import ca.uhn.fhir.rest.param.UriAndListParam;
import ca.uhn.fhir.rest.param.UriOrListParam;
import ca.uhn.fhir.rest.param.UriParam;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SearchConverterTest {
	private SearchConverter myFixture;

	@BeforeEach
	public void setupFixture() {
		myFixture = new SearchConverter();
	}

	@Test
	void isSearchParameterShouldReturnTrue() {
		boolean result = myFixture.isSearchResultParameter("_elements");
		assertTrue(result);
	}

	@Test
	void isSearchParameterShouldReturnFalse() {
		boolean result = myFixture.isSearchResultParameter("_id");
		assertFalse(result);
	}

	@Test
	void isOrListShouldReturnTrue() {
		boolean uriOrList = myFixture.isOrList(new UriOrListParam());
		boolean numberOrList = myFixture.isOrList(new NumberOrListParam());
		boolean specialOrList = myFixture.isOrList(new SpecialOrListParam());
		boolean tokenOrList = myFixture.isOrList(new TokenOrListParam());
		assertTrue(uriOrList);
		assertTrue(numberOrList);
		assertTrue(specialOrList);
		assertTrue(tokenOrList);
	}

	@Test
	void isAndListShouldReturnTrue() {
		boolean uriAndList = myFixture.isAndList(new UriAndListParam());
		boolean numberAndList = myFixture.isAndList(new NumberAndListParam());
		boolean specialAndList = myFixture.isAndList(new SpecialAndListParam());
		boolean tokenAndList = myFixture.isAndList(new TokenAndListParam());
		assertTrue(uriAndList);
		assertTrue(numberAndList);
		assertTrue(specialAndList);
		assertTrue(tokenAndList);
	}

	@Test
	void isOrListShouldReturnFalse() {
		boolean uriAndList = myFixture.isOrList(new UriAndListParam());
		assertFalse(uriAndList);
	}

	@Test
	void isAndListShouldReturnFalse() {
		boolean uriAndList = myFixture.isAndList(new UriOrListParam());
		assertFalse(uriAndList);
	}

	@Test
	void setParameterTypeValueShouldSetWithOrValue() {
		String theKey = "theOrKey";
		UriOrListParam theValue = withUriOrListParam();
		myFixture.setParameterTypeValue(theKey, theValue);
		String result = myFixture.searchParameterMap.toNormalizedQueryString(withFhirContext());
		String expected = "?theOrKey=theSecondValue,theValue";
		assertEquals(expected, result);
	}

	@Test
	void setParameterTypeValueShouldSetWithAndValue() {
		String theKey = "theAndKey";
		UriAndListParam theValue = withUriAndListParam();
		myFixture.setParameterTypeValue(theKey, theValue);
		String result = myFixture.searchParameterMap.toNormalizedQueryString(withFhirContext());
		String expected =
				"?theAndKey=theSecondValue,theValue&theAndKey=theSecondValueAgain,theValueAgain";
		assertEquals(expected, result);
	}

	@Test
	void setParameterTypeValueShouldSetWithBaseValue() {
		String expected = "?theKey=theValue";
		UriParam theValue = new UriParam("theValue");
		String theKey = "theKey";
		myFixture.setParameterTypeValue(theKey, theValue);
		String result = myFixture.searchParameterMap.toNormalizedQueryString(withFhirContext());
		assertEquals(expected, result);
	}

	@Test
	void separateParameterTypesShouldSeparateSearchAndResultParams() {
		myFixture.separateParameterTypes(withParamList());
		assertThat(myFixture.separatedSearchParameters).hasSize(2);
		assertThat(myFixture.separatedResultParameters).hasSize(3);
	}

	@Test
	void convertToStringMapShouldConvert() {
		Map<String, String[]> expected = withParamListAsStrings();
		myFixture.convertToStringMap(withParamList(), withFhirContext());
		Map<String, String[]> result = myFixture.resultParameters;
		assertEquals(result.keySet(), expected.keySet());
		assertThat(result.entrySet().stream()
				.allMatch(e -> Arrays.equals(e.getValue(),expected.get(e.getKey())))).isTrue();
	}

	Map<String, List<IQueryParameterType>> withParamList() {
		Map<String, List<IQueryParameterType>> paramList = new HashMap<>();
		paramList.put("_id", withUriParam(1));
		paramList.put("_elements", withUriParam(2));
		paramList.put("_lastUpdated", withUriParam(1));
		paramList.put("_total", withUriParam(1));
		paramList.put("_count", withUriParam(3));
		return paramList;
	}

	Map<String, String[]> withParamListAsStrings() {
		Map<String, String[]> paramList = new HashMap<>();
		paramList.put("_id", withStringParam(1));
		paramList.put("_elements", withStringParam(2));
		paramList.put("_lastUpdated", withStringParam(1));
		paramList.put("_total", withStringParam(1));
		paramList.put("_count", withStringParam(3));
		return paramList;
	}

	List<IQueryParameterType> withUriParam(int theNumberOfParams) {
		List<IQueryParameterType> paramList = new ArrayList<>();
		for (int i = 0; i < theNumberOfParams; i++) {
			paramList.add(new UriParam(Integer.toString(i)));
		}
		return paramList;
	}

	UriOrListParam withUriOrListParam() {
		UriOrListParam orList = new UriOrListParam();
		orList.add(new UriParam("theValue"));
		orList.add(new UriParam("theSecondValue"));
		return orList;
	}

	UriOrListParam withUriOrListParamSecond() {
		UriOrListParam orList = new UriOrListParam();
		orList.add(new UriParam("theValueAgain"));
		orList.add(new UriParam("theSecondValueAgain"));
		return orList;
	}

	UriAndListParam withUriAndListParam() {
		UriAndListParam andList = new UriAndListParam();
		andList.addAnd(withUriOrListParam());
		andList.addAnd(withUriOrListParamSecond());
		return andList;
	}

	String[] withStringParam(int theNumberOfParams) {
		String[] paramList = new String[theNumberOfParams];
		for (int i = 0; i < theNumberOfParams; i++) {
			paramList[i] = Integer.toString(i);
		}
		return paramList;
	}

	FhirContext withFhirContext() {
		return new FhirContext();
	}
}
