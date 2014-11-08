package ca.uhn.fhir.rest.param;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import ca.uhn.fhir.rest.method.QualifiedParamList;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;

public class DateRangeParamTest {

	private static DateRangeParam create(String theLower, String theUpper) throws InvalidRequestException {
		DateRangeParam p = new DateRangeParam();
		List<QualifiedParamList> tokens = new ArrayList<QualifiedParamList>();
		tokens.add(QualifiedParamList.singleton(null, theLower));
		if (theUpper != null) {
			tokens.add(QualifiedParamList.singleton(null, theUpper));
		}
		p.setValuesAsQueryTokens(tokens);
		return p;
	}

	public static Date parse(String theString) throws ParseException {
		return ourFmt.parse(theString);
	}

	public static Date parseM1(String theString) throws ParseException {
		return new Date(ourFmt.parse(theString).getTime() - 1L);
	}

	private static SimpleDateFormat ourFmt;

	static {
		ourFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSS");
	}

	private DateRangeParam create(String theString) {
		return new DateRangeParam(new DateParam(theString));
	}

	@Test
	public void testDay() throws Exception {
		assertEquals(parse("2011-01-01 00:00:00.0000"), create(">=2011-01-01", "<2011-01-02").getLowerBoundAsInstant());
		assertEquals(parseM1("2011-01-02 00:00:00.0000"), create(">=2011-01-01", "<2011-01-02").getUpperBoundAsInstant());

		assertEquals(parse("2011-01-02 00:00:00.0000"), create(">2011-01-01", "<=2011-01-02").getLowerBoundAsInstant());
		assertEquals(parseM1("2011-01-03 00:00:00.0000"), create(">2011-01-01", "<=2011-01-02").getUpperBoundAsInstant());
	}

	@Test
	public void testFromQualifiedDateParam() throws Exception {
		assertEquals(parse("2011-01-01 00:00:00.0000"), create("2011-01-01").getLowerBoundAsInstant());
		assertEquals(parseM1("2011-01-02 00:00:00.0000"), create("2011-01-01").getUpperBoundAsInstant());

		assertEquals(parse("2011-01-01 00:00:00.0000"), create(">=2011-01-01").getLowerBoundAsInstant());
		assertEquals(null, create(">=2011-01-01").getUpperBoundAsInstant());

		assertEquals(null, create("<=2011-01-01").getLowerBoundAsInstant());
		assertEquals(parseM1("2011-01-02 00:00:00.0000"), create("<=2011-01-01").getUpperBoundAsInstant());
	}

	@Test
	public void testMonth() throws Exception {
		assertEquals(parse("2011-01-01 00:00:00.0000"), create(">=2011-01", "<2011-02").getLowerBoundAsInstant());
		assertEquals(parseM1("2011-02-01 00:00:00.0000"), create(">=2011-01", "<2011-02").getUpperBoundAsInstant());

		assertEquals(parse("2011-02-01 00:00:00.0000"), create(">2011-01", "<=2011-02").getLowerBoundAsInstant());
		assertEquals(parseM1("2011-03-01 00:00:00.0000"), create(">2011-01", "<=2011-02").getUpperBoundAsInstant());
	}

	@Test
	public void testOnlyOneParam() throws Exception {
		assertEquals(parse("2011-01-01 00:00:00.0000"), create("2011-01-01").getLowerBoundAsInstant());
		assertEquals(parseM1("2011-01-02 00:00:00.0000"), create("2011-01-01").getUpperBoundAsInstant());
	}

	@Test
	public void testSecond() throws Exception {
		assertEquals(parse("2011-01-01 00:00:00.0000"), create(">=2011-01-01T00:00:00", "<2011-01-01T01:00:00").getLowerBoundAsInstant());
		assertEquals(parseM1("2011-01-01 02:00:00.0000"), create(">=2011-01-01T00:00:00", "<2011-01-01T02:00:00").getUpperBoundAsInstant());

		assertEquals(parse("2011-01-01 00:00:01.0000"), create(">2011-01-01T00:00:00", "<=2011-01-01T02:00:00").getLowerBoundAsInstant());
		assertEquals(parseM1("2011-01-01 02:00:01.0000"), create(">2011-01-01T00:00:00", "<=2011-01-01T02:00:00").getUpperBoundAsInstant());
	}

	@Test
	public void testYear() throws Exception {
		assertEquals(parse("2011-01-01 00:00:00.0000"), create(">=2011", "<2012").getLowerBoundAsInstant());
		assertEquals(parseM1("2012-01-01 00:00:00.0000"), create(">=2011", "<2012").getUpperBoundAsInstant());

		assertEquals(parse("2012-01-01 00:00:00.0000"), create(">2011", "<=2012").getLowerBoundAsInstant());
		assertEquals(parseM1("2014-01-01 00:00:00.0000"), create(">2011", "<=2013").getUpperBoundAsInstant());
	}

}
