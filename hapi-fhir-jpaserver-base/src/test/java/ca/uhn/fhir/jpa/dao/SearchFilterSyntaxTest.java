package ca.uhn.fhir.jpa.dao;

import ca.uhn.fhir.util.TestUtil;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Test;

public class SearchFilterSyntaxTest {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(SearchFilterSyntaxTest.class);

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

	private void testParse(String expression) {
		SearchFilterParser.Filter filter = SearchFilterParser.parse(expression);
		Assert.assertNotNull("Parsing failed - returned null",
			filter);
		if (filter != null) {
			Assert.assertEquals(String.format("Expression mismatch: found %s, expecting %s",
				filter.toString(),
				expression),
				expression,
				filter.toString());
		}
	}

	@Test
	public void testString() {
		testParse("userName eq \"bjensen\"");
	}

	@Test
	public void testToken() {
		testParse("name eq loinc|1234");
	}

	@Test
	public void testUrl() {
		testParse("name in http://loinc.org/vs/LP234");
	}

	@Test
	public void testDate() {
		testParse("date ge 2010-10-10");
	}

	@Test
	public void testSubsumes() {
		testParse("code sb snomed|diabetes");
	}

	@Test
	public void testSubsumesId() {
		testParse("code ss snomed|diabetes-NIDDM-stage-1");
	}

	@Test
	public void testFilter() {
		testParse("related[type eq comp].target pr false");
	}

	@Test
	public void testFilter2() {
		testParse("related[type eq comp and this lt that].target pr false");
	}

	@Test
	public void testParentheses() {
		testParse("((userName eq \"bjensen\") or (userName eq \"jdoe\")) and (code sb snomed|diabetes)");
	}

	@Test
	public void testPrecedence() {
		testParse("this eq that and this1 eq that1");
	}

}
