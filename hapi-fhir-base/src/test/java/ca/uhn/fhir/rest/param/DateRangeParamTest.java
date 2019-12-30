package ca.uhn.fhir.rest.param;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.QualifiedParamList;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static ca.uhn.fhir.rest.param.ParamPrefixEnum.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class DateRangeParamTest {
	private FhirContext fhirContext;
	private Date myBefore;
	private Date myLower;
	private Date myBetween;
	private Date myUpper;
	private Date myAfter;

	@Rule
	public final ExpectedException expectedException = ExpectedException.none();

	@Before
	public void initMockContext() {
		fhirContext = Mockito.mock(FhirContext.class);
	}

	@Before
	public void initDates() throws InterruptedException {
		myBefore = new Date();
		Thread.sleep(1L);
		myLower = new Date();
		Thread.sleep(1L);
		myBetween = new Date();
		Thread.sleep(1L);
		myUpper = new Date();
		Thread.sleep(1L);
		myAfter = new Date();
	}

	@Test
	public void testIsDateWithinRangeExclusive() {
		DateParam lowerBound = new DateParam(GREATERTHAN, myLower);
		DateParam upperBound = new DateParam(LESSTHAN, myUpper);
		DateRangeParam dateRangeParam = new DateRangeParam(lowerBound, upperBound);

		assertFalse(dateRangeParam.isDateWithinRange(myBefore));
		assertFalse(dateRangeParam.isDateWithinRange(myLower));
		assertTrue(dateRangeParam.isDateWithinRange(myBetween));
		assertFalse(dateRangeParam.isDateWithinRange(myUpper));
		assertFalse(dateRangeParam.isDateWithinRange(myAfter));
	}

	@Test
	public void testIsDateWithinRangeInclusive() {
		DateParam lowerBound = new DateParam(EQUAL, myLower);
		DateParam upperBound = new DateParam(EQUAL, myUpper);
		DateRangeParam dateRangeParam = new DateRangeParam(lowerBound, upperBound);

		assertFalse(dateRangeParam.isDateWithinRange(myBefore));
		assertTrue(dateRangeParam.isDateWithinRange(myLower));
		assertTrue(dateRangeParam.isDateWithinRange(myBetween));
		assertTrue(dateRangeParam.isDateWithinRange(myUpper));
		assertFalse(dateRangeParam.isDateWithinRange(myAfter));
	}

	@Test
	public void testIsDateWithinRangeOnlyLower() {
		DateParam lowerBound = new DateParam(EQUAL, myLower);
		DateRangeParam dateRangeParam = new DateRangeParam();
		dateRangeParam.setLowerBound(lowerBound);

		assertFalse(dateRangeParam.isDateWithinRange(myBefore));
		assertTrue(dateRangeParam.isDateWithinRange(myLower));
		assertFalse(dateRangeParam.isDateWithinRange(myBetween));
		assertFalse(dateRangeParam.isDateWithinRange(myUpper));
		assertFalse(dateRangeParam.isDateWithinRange(myAfter));
	}

	@Test
	public void testIsDateWithinRangeOnlyUpper() {
		DateParam upperBound = new DateParam(EQUAL, myUpper);
		DateRangeParam dateRangeParam = new DateRangeParam();
		dateRangeParam.setUpperBound(upperBound);

		assertFalse(dateRangeParam.isDateWithinRange(myBefore));
		assertFalse(dateRangeParam.isDateWithinRange(myLower));
		assertFalse(dateRangeParam.isDateWithinRange(myBetween));
		assertTrue(dateRangeParam.isDateWithinRange(myUpper));
		assertFalse(dateRangeParam.isDateWithinRange(myAfter));
	}

	@Test
	public void testIsDateWithinLowerBoundGreaterThan() {
		DateParam lowerBound = new DateParam(GREATERTHAN, myLower);
		DateRangeParam dateRangeParam = new DateRangeParam();
		dateRangeParam.setLowerBound(lowerBound);

		assertFalse(dateRangeParam.isDateWithinLowerBound(myBefore));
		assertFalse(dateRangeParam.isDateWithinLowerBound(myLower));
		assertTrue(dateRangeParam.isDateWithinLowerBound(myBetween));
		assertTrue(dateRangeParam.isDateWithinLowerBound(myUpper));
		assertTrue(dateRangeParam.isDateWithinLowerBound(myAfter));
	}

	@Test
	public void testIsDateWithinLowerBoundStartsAfter() {
		DateParam lowerBound = new DateParam(STARTS_AFTER, myLower);
		DateRangeParam dateRangeParam = new DateRangeParam();
		dateRangeParam.setLowerBound(lowerBound);

		assertFalse(dateRangeParam.isDateWithinLowerBound(myBefore));
		assertFalse(dateRangeParam.isDateWithinLowerBound(myLower));
		assertTrue(dateRangeParam.isDateWithinLowerBound(myBetween));
		assertTrue(dateRangeParam.isDateWithinLowerBound(myUpper));
		assertTrue(dateRangeParam.isDateWithinLowerBound(myAfter));
	}

	@Test
	public void testIsDateWithinLowerBoundEqual() {
		DateParam lowerBound = new DateParam(EQUAL, myLower);
		DateRangeParam dateRangeParam = new DateRangeParam();
		dateRangeParam.setLowerBound(lowerBound);

		assertFalse(dateRangeParam.isDateWithinLowerBound(myBefore));
		assertTrue(dateRangeParam.isDateWithinLowerBound(myLower));
		assertFalse(dateRangeParam.isDateWithinLowerBound(myBetween));
		assertFalse(dateRangeParam.isDateWithinLowerBound(myUpper));
		assertFalse(dateRangeParam.isDateWithinLowerBound(myAfter));
	}

	@Test
	public void testIsDateWithinLowerBoundGreaterThanOrEquals() {
		DateParam lowerBound = new DateParam(GREATERTHAN_OR_EQUALS, myLower);
		DateRangeParam dateRangeParam = new DateRangeParam();
		dateRangeParam.setLowerBound(lowerBound);

		assertFalse(dateRangeParam.isDateWithinLowerBound(myBefore));
		assertTrue(dateRangeParam.isDateWithinLowerBound(myLower));
		assertTrue(dateRangeParam.isDateWithinLowerBound(myBetween));
		assertTrue(dateRangeParam.isDateWithinLowerBound(myUpper));
		assertTrue(dateRangeParam.isDateWithinLowerBound(myAfter));
	}

	@Test
	public void testIsDateWithinUpperBoundLessThan() {
		DateParam upperBound = new DateParam(LESSTHAN, myUpper);
		DateRangeParam dateRangeParam = new DateRangeParam();
		dateRangeParam.setUpperBound(upperBound);

		assertTrue(dateRangeParam.isDateWithinUpperBound(myBefore));
		assertTrue(dateRangeParam.isDateWithinUpperBound(myLower));
		assertTrue(dateRangeParam.isDateWithinUpperBound(myBetween));
		assertFalse(dateRangeParam.isDateWithinUpperBound(myUpper));
		assertFalse(dateRangeParam.isDateWithinUpperBound(myAfter));
	}

	@Test
	public void testIsDateWithinUpperBoundEndsBefore() {
		DateParam upperBound = new DateParam(ENDS_BEFORE, myUpper);
		DateRangeParam dateRangeParam = new DateRangeParam();
		dateRangeParam.setUpperBound(upperBound);

		assertTrue(dateRangeParam.isDateWithinUpperBound(myBefore));
		assertTrue(dateRangeParam.isDateWithinUpperBound(myLower));
		assertTrue(dateRangeParam.isDateWithinUpperBound(myBetween));
		assertFalse(dateRangeParam.isDateWithinUpperBound(myUpper));
		assertFalse(dateRangeParam.isDateWithinUpperBound(myAfter));
	}

	@Test
	public void testIsDateWithinUpperBoundEqual() {
		DateParam upperBound = new DateParam(EQUAL, myUpper);
		DateRangeParam dateRangeParam = new DateRangeParam();
		dateRangeParam.setUpperBound(upperBound);

		assertFalse(dateRangeParam.isDateWithinUpperBound(myBefore));
		assertFalse(dateRangeParam.isDateWithinUpperBound(myLower));
		assertFalse(dateRangeParam.isDateWithinUpperBound(myBetween));
		assertTrue(dateRangeParam.isDateWithinUpperBound(myUpper));
		assertFalse(dateRangeParam.isDateWithinUpperBound(myAfter));
	}

	@Test
	public void testIsDateWithinUpperBoundLessThanOrEquals() {
		DateParam upperBound = new DateParam(LESSTHAN_OR_EQUALS, myUpper);
		DateRangeParam dateRangeParam = new DateRangeParam();
		dateRangeParam.setUpperBound(upperBound);

		assertTrue(dateRangeParam.isDateWithinUpperBound(myBefore));
		assertTrue(dateRangeParam.isDateWithinUpperBound(myLower));
		assertTrue(dateRangeParam.isDateWithinUpperBound(myBetween));
		assertTrue(dateRangeParam.isDateWithinUpperBound(myUpper));
		assertFalse(dateRangeParam.isDateWithinUpperBound(myAfter));
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
