package ca.uhn.fhir.jpa.dao;

import ca.uhn.fhir.jpa.dao.predicate.SearchFilterParser;
import ca.uhn.fhir.util.TestUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class SearchFilterSyntaxTest {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(SearchFilterSyntaxTest.class);

	private void testParse(String theExpression) throws SearchFilterParser.FilterSyntaxException {
		SearchFilterParser.Filter filter = SearchFilterParser.parse(theExpression);
		ourLog.info("Source: {}", theExpression);
		ourLog.info("Parsed: {}", filter.toString());
		assertNotNull(filter, "Parsing failed - returned null");
		assertEquals(theExpression, filter.toString(), String.format("Expression mismatch: found %s, expecting %s", filter.toString(), theExpression));
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
