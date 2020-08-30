package ca.uhn.fhir.rest.param;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.QualifiedParamList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class DateRangeParamTest {
	private FhirContext fhirContext;

	@BeforeEach
	public void initMockContext() {
		fhirContext = Mockito.mock(FhirContext.class);
	}

	/** Can happen e.g. when the query parameter for {@code _lastUpdated} is left empty. */
	@Test
	public void testParamWithoutPrefixAndWithoutValue() {
		QualifiedParamList qualifiedParamList = new QualifiedParamList(1);
		qualifiedParamList.add("");

		List<QualifiedParamList> params = new ArrayList<>(1);
		params.add(qualifiedParamList);
		DateRangeParam dateRangeParam = new DateRangeParam();
		dateRangeParam.setValuesAsQueryTokens(fhirContext, "_lastUpdated", params);

		assertTrue(dateRangeParam.isEmpty());
	}

	/** Can happen e.g. when the query parameter for {@code _lastUpdated} is given as {@code lt} without any value. */
	@Test
	public void testUpperBoundWithPrefixWithoutValue() {
		QualifiedParamList qualifiedParamList = new QualifiedParamList(1);
		qualifiedParamList.add("lt");

		List<QualifiedParamList> params = new ArrayList<>(1);
		params.add(qualifiedParamList);
		DateRangeParam dateRangeParam = new DateRangeParam();
		dateRangeParam.setValuesAsQueryTokens(fhirContext, "_lastUpdated", params);

		assertTrue(dateRangeParam.isEmpty());
	}

	/** Can happen e.g. when the query parameter for {@code _lastUpdated} is given as {@code gt} without any value. */
	@Test
	public void testLowerBoundWithPrefixWithoutValue() {
		QualifiedParamList qualifiedParamList = new QualifiedParamList(1);
		qualifiedParamList.add("gt");

		List<QualifiedParamList> params = new ArrayList<>(1);
		params.add(qualifiedParamList);
		DateRangeParam dateRangeParam = new DateRangeParam();
		dateRangeParam.setValuesAsQueryTokens(fhirContext, "_lastUpdated", params);

		assertTrue(dateRangeParam.isEmpty());
	}
}
