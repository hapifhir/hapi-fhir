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

	private void testParse(String expression) throws SearchFilterParser.FilterSyntaxException {
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
	public void testString() throws SearchFilterParser.FilterSyntaxException {
		testParse("userName eq \"bjensen\"");
	}

	@Test
	public void testToken() throws SearchFilterParser.FilterSyntaxException {
		testParse("name eq loinc|1234");
	}

	@Test
	public void testUrl() throws SearchFilterParser.FilterSyntaxException {
		testParse("name in http://loinc.org/vs/LP234");
	}

	@Test
	public void testDate() throws SearchFilterParser.FilterSyntaxException {
		testParse("date ge 2010-10-10");
	}

	@Test
	public void testSubsumes() throws SearchFilterParser.FilterSyntaxException {
		testParse("code sb snomed|diabetes");
	}

	@Test
	public void testSubsumesId() throws SearchFilterParser.FilterSyntaxException {
		testParse("code ss snomed|diabetes-NIDDM-stage-1");
	}

	@Test
	public void testFilter() throws SearchFilterParser.FilterSyntaxException {
		testParse("related[type eq comp].target pr false");
	}

	@Test
	public void testFilter2() throws SearchFilterParser.FilterSyntaxException {
		testParse("related[type eq comp and this lt that].target pr false");
	}

	@Test
	public void testParentheses() throws SearchFilterParser.FilterSyntaxException {
		testParse("((userName eq \"bjensen\") or (userName eq \"jdoe\")) and (code sb snomed|diabetes)");
	}

	@Test
	public void testPrecedence() throws SearchFilterParser.FilterSyntaxException {
		testParse("this eq that and this1 eq that1");
	}

}
