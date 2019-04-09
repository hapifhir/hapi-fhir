package ca.uhn.fhir.rest.param;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.QualifiedParamList;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjuster;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class DateRangeParamTest {
	private FhirContext fhirContext;

	@Before
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

	private void doLowerBoundTest(String prefix, TemporalAdjuster resultAdjuster) {
		Instant timestamp = Instant.parse("2013-01-01T13:28:17.000Z");
		QualifiedParamList qualifiedParamList = new QualifiedParamList(1);
		qualifiedParamList.add(prefix + timestamp.toString());

		DateRangeParam dateRangeParam = new DateRangeParam();
		dateRangeParam.setValuesAsQueryTokens(fhirContext, "_lastUpdated", Collections.singletonList(qualifiedParamList));

		assertEquals(dateRangeParam.getLowerBoundAsInstant().toInstant(), resultAdjuster.adjustInto(timestamp));
	}

	@Test
	public void testLowerBoundWithPrefixGreaterThan() {
		doLowerBoundTest("gt", instant -> instant.plus(1, ChronoUnit.SECONDS));
	}

	@Test
	public void testLowerBoundWithPrefixGreaterThanEqual() {
		doLowerBoundTest("ge", instant -> instant);
	}

	@Test
	public void testLowerBoundWithPrefixStartsAfter() {
		doLowerBoundTest("sa", instant -> instant.plus(1, ChronoUnit.SECONDS));
	}

	private void doUpperBoundTest(String prefix, TemporalAdjuster resultAdjuster) {
		Instant timestamp = Instant.parse("2013-01-01T13:28:17.000Z");
		QualifiedParamList qualifiedParamList = new QualifiedParamList(1);
		qualifiedParamList.add(prefix + timestamp.toString());

		DateRangeParam dateRangeParam = new DateRangeParam();
		dateRangeParam.setValuesAsQueryTokens(fhirContext, "_lastUpdated", Collections.singletonList(qualifiedParamList));

		assertEquals(dateRangeParam.getUpperBoundAsInstant().toInstant(), (Instant)resultAdjuster.adjustInto(timestamp));
	}

	@Test
	public void testUpperBoundWithPrefixLessThan() {
		doUpperBoundTest("lt", instant -> instant.minus(1, ChronoUnit.SECONDS));
	}

	@Test
	public void testUpperBoundWithPrefixLessThanEqual() {
		doUpperBoundTest("le", instant -> instant);
	}

	@Test
	public void testBoundsWithPrefixEndsBefore() {
		doUpperBoundTest("eb", instant -> instant.minus(1, ChronoUnit.SECONDS));
	}
}
