package ca.uhn.fhir.rest.param;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.TemporalPrecisionEnum;
import ca.uhn.fhir.model.primitive.DateTimeDt;
import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.narrative.DefaultThymeleafNarrativeGenerator;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.api.QualifiedParamList;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.test.utilities.JettyUtil;
import ca.uhn.fhir.util.TestUtil;
import com.google.common.base.Charsets;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import static ca.uhn.fhir.rest.param.ParamPrefixEnum.EQUAL;
import static ca.uhn.fhir.rest.param.ParamPrefixEnum.GREATERTHAN_OR_EQUALS;
import static ca.uhn.fhir.rest.param.ParamPrefixEnum.LESSTHAN_OR_EQUALS;
import static java.lang.System.currentTimeMillis;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DateRangeParamR4Test {

	private static final SimpleDateFormat ourFmtLower;
	private static final SimpleDateFormat ourFmtUpper;
	private static final SimpleDateFormat ourFmtLowerForTime;
	private static final SimpleDateFormat ourFmtUpperForTime;
	private static final Logger ourLog = LoggerFactory.getLogger(DateRangeParamR4Test.class);
	private static CloseableHttpClient ourClient;
	private static FhirContext ourCtx = FhirContext.forR4();
	private static DateRangeParam ourLastDateRange;
	private static int ourPort;
	private static Server ourServer;
	private static String ourBaseUrl;

	static {
		ourFmtLower = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSS");
		ourFmtLower.setTimeZone(TimeZone.getTimeZone("GMT-10:00"));

		ourFmtUpper = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSS");
		ourFmtUpper.setTimeZone(TimeZone.getTimeZone("GMT+12:00"));

		ourFmtLowerForTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSS");
		ourFmtUpperForTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSS");
	}

	@BeforeEach
	public void before() {
		ourLastDateRange = null;
	}

	@Test
	public void testSearchForMultipleUnqualifiedDate() throws Exception {
		String baseUrl = "http://localhost:" + ourPort + "/Patient?" + Patient.SP_BIRTHDATE + "=";
		HttpGet httpGet = new HttpGet(baseUrl + "2012-01-01&" + Patient.SP_BIRTHDATE + "=2012-02-03");
		CloseableHttpResponse status = ourClient.execute(httpGet);
		consumeResponse(status);
		assertEquals(400, status.getStatusLine().getStatusCode());

	}

	private void consumeResponse(CloseableHttpResponse theStatus) throws IOException {
		try (InputStream content = theStatus.getEntity().getContent()) {
			String response = IOUtils.toString(content, Charsets.UTF_8);
			ourLog.trace(response);
		}
		theStatus.close();
	}

	@Test
	public void testSearchForOneUnqualifiedDate() throws Exception {
		HttpGet httpGet = new HttpGet(ourBaseUrl + "?birthdate=2012-01-01");
		CloseableHttpResponse status = ourClient.execute(httpGet);
		consumeResponse(status);
		assertEquals(200, status.getStatusLine().getStatusCode());

		assertEquals("2012-01-01", ourLastDateRange.getLowerBound().getValueAsString());
		assertEquals("2012-01-01", ourLastDateRange.getUpperBound().getValueAsString());

		assertEquals(parseLowerForDatePrecision("2012-01-01 00:00:00.0000"), ourLastDateRange.getLowerBoundAsInstant());
		assertEquals(parseUpperForDatePrecision("2012-01-03 00:00:00.0000"), ourLastDateRange.getUpperBoundAsInstant());
		assertEquals(ParamPrefixEnum.EQUAL, ourLastDateRange.getLowerBound().getPrefix());
		assertEquals(ParamPrefixEnum.EQUAL, ourLastDateRange.getUpperBound().getPrefix());
	}

	@Test
	public void testSearchForOneQualifiedDateEq() throws Exception {
		HttpGet httpGet = new HttpGet(ourBaseUrl + "?birthdate=eq2012-01-01");
		CloseableHttpResponse status = ourClient.execute(httpGet);
		consumeResponse(status);
		assertEquals(200, status.getStatusLine().getStatusCode());

		assertEquals("2012-01-01", ourLastDateRange.getLowerBound().getValueAsString());
		assertEquals("2012-01-01", ourLastDateRange.getUpperBound().getValueAsString());

		assertEquals(parseLowerForDatePrecision("2012-01-01 00:00:00.0000"), ourLastDateRange.getLowerBoundAsInstant());
		assertEquals(parseUpperForDatePrecision("2012-01-03 00:00:00.0000"), ourLastDateRange.getUpperBoundAsInstant());
		assertEquals(ParamPrefixEnum.EQUAL, ourLastDateRange.getLowerBound().getPrefix());
		assertEquals(ParamPrefixEnum.EQUAL, ourLastDateRange.getUpperBound().getPrefix());
	}

	@Test
	public void testSearchForOneQualifiedDateGt() throws Exception {
		HttpGet httpGet = new HttpGet(ourBaseUrl + "?birthdate=gt2012-01-01");
		CloseableHttpResponse status = ourClient.execute(httpGet);
		consumeResponse(status);
		assertEquals(200, status.getStatusLine().getStatusCode());

		assertEquals("2012-01-01", ourLastDateRange.getLowerBound().getValueAsString());
		assertEquals(null, ourLastDateRange.getUpperBound());

		assertEquals(parseLowerForDatePrecision("2012-01-02 00:00:00.0000"), ourLastDateRange.getLowerBoundAsInstant());
		assertEquals(null, ourLastDateRange.getUpperBoundAsInstant());
		assertEquals(ParamPrefixEnum.GREATERTHAN, ourLastDateRange.getLowerBound().getPrefix());
		assertEquals(null, ourLastDateRange.getUpperBound());
	}

	@Test
	public void testSearchForOneQualifiedDateLt() throws Exception {
		HttpGet httpGet = new HttpGet(ourBaseUrl + "?birthdate=lt2012-01-01");
		CloseableHttpResponse status = ourClient.execute(httpGet);
		consumeResponse(status);
		assertEquals(200, status.getStatusLine().getStatusCode());

		assertEquals(null, ourLastDateRange.getLowerBound());
		assertEquals("2012-01-01", ourLastDateRange.getUpperBound().getValueAsString());

		assertEquals(null, ourLastDateRange.getLowerBoundAsInstant());
		assertEquals(parseUpperForDatePrecision("2012-01-02 00:00:00.0000"), ourLastDateRange.getUpperBoundAsInstant());
		assertEquals(null, ourLastDateRange.getLowerBound());
		assertEquals(ParamPrefixEnum.LESSTHAN, ourLastDateRange.getUpperBound().getPrefix());
	}

	@Test
	public void testSearchForOneQualifiedDateGe() throws Exception {
		HttpGet httpGet = new HttpGet(ourBaseUrl + "?birthdate=ge2012-01-01");
		CloseableHttpResponse status = ourClient.execute(httpGet);
		consumeResponse(status);
		assertEquals(200, status.getStatusLine().getStatusCode());

		assertEquals("2012-01-01", ourLastDateRange.getLowerBound().getValueAsString());
		assertEquals(null, ourLastDateRange.getUpperBound());

		assertEquals(parseLowerForDatePrecision("2012-01-01 00:00:00.0000"), ourLastDateRange.getLowerBoundAsInstant());
		assertEquals(null, ourLastDateRange.getUpperBoundAsInstant());
		assertEquals(ParamPrefixEnum.GREATERTHAN_OR_EQUALS, ourLastDateRange.getLowerBound().getPrefix());
		assertEquals(null, ourLastDateRange.getUpperBound());
	}

	@Test
	public void testSearchForOneQualifiedDateLe() throws Exception {
		HttpGet httpGet = new HttpGet(ourBaseUrl + "?birthdate=le2012-01-01");
		CloseableHttpResponse status = ourClient.execute(httpGet);
		consumeResponse(status);
		assertEquals(200, status.getStatusLine().getStatusCode());

		assertEquals(null, ourLastDateRange.getLowerBound());
		assertEquals("2012-01-01", ourLastDateRange.getUpperBound().getValueAsString());

		assertEquals(null, ourLastDateRange.getLowerBoundAsInstant());
		assertEquals(parseUpperForDatePrecision("2012-01-03 00:00:00.0000"), ourLastDateRange.getUpperBoundAsInstant());
		assertEquals(null, ourLastDateRange.getLowerBound());
		assertEquals(ParamPrefixEnum.LESSTHAN_OR_EQUALS, ourLastDateRange.getUpperBound().getPrefix());
	}

	@Test
	public void testSearchForOneQualifiedDateNe() throws Exception {
		HttpGet httpGet = new HttpGet(ourBaseUrl + "?birthdate=ne2012-01-01");
		CloseableHttpResponse status = ourClient.execute(httpGet);
		consumeResponse(status);
		assertEquals(200, status.getStatusLine().getStatusCode());

		assertEquals("2012-01-01", ourLastDateRange.getLowerBound().getValueAsString());
		assertEquals("2012-01-01", ourLastDateRange.getUpperBound().getValueAsString());

		assertEquals(ParamPrefixEnum.NOT_EQUAL, ourLastDateRange.getLowerBound().getPrefix());
		assertEquals(ParamPrefixEnum.NOT_EQUAL, ourLastDateRange.getUpperBound().getPrefix());
	}

	@Test
	public void testRangeWithDatePrecision() throws Exception {
		HttpGet httpGet = new HttpGet(ourBaseUrl + "?birthdate=gt2012-01-01&birthdate=lt2012-01-03");
		CloseableHttpResponse status = ourClient.execute(httpGet);
		consumeResponse(status);
		assertEquals(200, status.getStatusLine().getStatusCode());

		assertEquals("2012-01-01", ourLastDateRange.getLowerBound().getValueAsString());
		Date lowerBoundInstant = ourLastDateRange.getLowerBoundAsInstant();
		Date midnightLower = new InstantDt("2012-01-01T00:00:00Z").getValue();
		assertTrue(lowerBoundInstant.after(midnightLower));

		assertEquals("2012-01-03", ourLastDateRange.getUpperBound().getValueAsString());
		Date upperBoundInstant = ourLastDateRange.getUpperBoundAsInstant();
		Date midnightUpper = new InstantDt("2012-01-03T00:00:00Z").getValue();
		assertTrue(upperBoundInstant.after(midnightUpper));

	}

	@Test
	public void testAddAnd() {
		assertEquals(1, new DateAndListParam().addAnd(new DateOrListParam()).getValuesAsQueryTokens().size());
		assertEquals(1, new NumberAndListParam().addAnd(new NumberOrListParam()).getValuesAsQueryTokens().size());
		assertEquals(1, new ReferenceAndListParam().addAnd(new ReferenceOrListParam()).getValuesAsQueryTokens().size());
		assertEquals(1, new QuantityAndListParam().addAnd(new QuantityOrListParam()).getValuesAsQueryTokens().size());
		assertEquals(1, new UriAndListParam().addAnd(new UriOrListParam()).getValuesAsQueryTokens().size());
		assertEquals(1, new StringAndListParam().addAnd(new StringOrListParam()).getValuesAsQueryTokens().size());
	}

	@Test
	public void testAndList() {
		assertNotNull(new DateAndListParam().newInstance());
		assertNotNull(new NumberAndListParam().newInstance());
		assertNotNull(new ReferenceAndListParam().newInstance());
		assertNotNull(new QuantityAndListParam().newInstance());
		assertNotNull(new UriAndListParam().newInstance());
		assertNotNull(new StringAndListParam().newInstance());
	}

	@Test
	public void testAndOr() {
		assertEquals(1, new DateOrListParam().addOr(new DateParam()).getValuesAsQueryTokens().size());
		assertEquals(1, new NumberOrListParam().addOr(new NumberParam()).getValuesAsQueryTokens().size());
		assertEquals(1, new ReferenceOrListParam().addOr(new ReferenceParam()).getValuesAsQueryTokens().size());
		assertEquals(1, new QuantityOrListParam().addOr(new QuantityParam()).getValuesAsQueryTokens().size());
		assertEquals(1, new UriOrListParam().addOr(new UriParam()).getValuesAsQueryTokens().size());
		assertEquals(1, new StringOrListParam().addOr(new StringParam()).getValuesAsQueryTokens().size());
	}

	@Test
	public void testDay() throws Exception {
		assertEquals(parseLowerForDatePrecision("2011-01-01 00:00:00.0000"), create(">=2011-01-01", "<2011-01-02").getLowerBoundAsInstant());
		assertEquals(parseUpperForDatePrecision("2011-01-03 00:00:00.0000"), create(">=2011-01-01", "<2011-01-02").getUpperBoundAsInstant());
		assertEquals(parseLowerForDatePrecision("2011-01-02 00:00:00.0000"), create(">2011-01-01", "<=2011-01-02").getLowerBoundAsInstant());
		assertEquals(parseUpperForDatePrecision("2011-01-04 00:00:00.0000"), create(">2011-01-01", "<=2011-01-02").getUpperBoundAsInstant());

		assertEquals(parseLowerForDatePrecision("2011-01-01 00:00:00.0000"), create("ge2011-01-01", "lt2011-01-02").getLowerBoundAsInstant());
		assertEquals(parseUpperForDatePrecision("2011-01-03 00:00:00.0000"), create("ge2011-01-01", "lt2011-01-02").getUpperBoundAsInstant());
		assertEquals(parseLowerForDatePrecision("2011-01-02 00:00:00.0000"), create("gt2011-01-01", "le2011-01-02").getLowerBoundAsInstant());
		assertEquals(parseUpperForDatePrecision("2011-01-04 00:00:00.0000"), create("gt2011-01-01", "le2011-01-02").getUpperBoundAsInstant());
	}

	@Test
	public void testFromQualifiedDateParam() throws Exception {
		assertEquals(parseLowerForDatePrecision("2011-01-01 00:00:00.0000"), create("2011-01-01").getLowerBoundAsInstant());
		assertEquals(parseUpperForDatePrecision("2011-01-03 00:00:00.0000"), create("2011-01-01").getUpperBoundAsInstant());

		assertEquals(parseLowerForDatePrecision("2011-01-01 00:00:00.0000"), create("ge2011-01-01").getLowerBoundAsInstant());
		assertEquals(null, create("ge2011-01-01").getUpperBoundAsInstant());

		assertEquals(null, create("le2011-01-01").getLowerBoundAsInstant());
		assertEquals(parseUpperForDatePrecision("2011-01-03 00:00:00.0000"), create("le2011-01-01").getUpperBoundAsInstant());
	}

	private DateRangeParam create(String theString) {
		return new DateRangeParam(new DateParam(theString));
	}

	@Test
	public void testMonth() throws Exception {
		assertEquals(parseLowerForDatePrecision("2011-01-01 00:00:00.0000"), create("ge2011-01", "lt2011-02").getLowerBoundAsInstant());
		assertEquals(parseUpperForDatePrecision("2011-02-02 00:00:00.0000"), create("ge2011-01", "lt2011-02").getUpperBoundAsInstant());

		assertEquals(parseLowerForDatePrecision("2011-02-01 00:00:00.0000"), create("gt2011-01", "le2011-02").getLowerBoundAsInstant());
		assertEquals(parseUpperForDatePrecision("2011-03-02 00:00:00.0000"), create("gt2011-01", "le2011-02").getUpperBoundAsInstant());
	}

	@Test
	public void testOnlyOneParam() throws Exception {
		assertEquals(parseLowerForDatePrecision("2011-01-01 00:00:00.0000"), create("2011-01-01").getLowerBoundAsInstant());
		assertEquals(parseUpperForDatePrecision("2011-01-03 00:00:00.0000"), create("2011-01-01").getUpperBoundAsInstant());
	}

	@Test
	public void testSetBoundsWithDatesInclusive() {
		DateRangeParam range = new DateRangeParam();
		range.setLowerBoundInclusive(new Date());
		range.setUpperBoundInclusive(new Date());

		assertEquals(ParamPrefixEnum.GREATERTHAN_OR_EQUALS, range.getLowerBound().getPrefix());
		assertEquals(ParamPrefixEnum.LESSTHAN_OR_EQUALS, range.getUpperBound().getPrefix());
	}

	@Test
	public void testSetBoundsWithDatesExclusive() {
		DateRangeParam range = new DateRangeParam();
		range.setLowerBoundExclusive(new Date());
		range.setUpperBoundExclusive(new Date());

		assertEquals(ParamPrefixEnum.GREATERTHAN, range.getLowerBound().getPrefix());
		assertEquals(ParamPrefixEnum.LESSTHAN, range.getUpperBound().getPrefix());
	}

	@Test
	public void testOrList() {
		assertNotNull(new DateOrListParam().newInstance());
		assertNotNull(new NumberOrListParam().newInstance());
		assertNotNull(new ReferenceOrListParam().newInstance());
		assertNotNull(new QuantityOrListParam().newInstance());
		assertNotNull(new UriOrListParam().newInstance());
		assertNotNull(new StringOrListParam().newInstance());
	}

	@Test
	public void testRange() {
		InstantDt start = new InstantDt("2015-09-23T07:43:34.811-04:00");
		InstantDt end = new InstantDt("2015-09-23T07:43:34.899-04:00");
		DateParam lowerBound = new DateParam(ParamPrefixEnum.GREATERTHAN, start.getValue());
		DateParam upperBound = new DateParam(ParamPrefixEnum.LESSTHAN, end.getValue());
		assertEquals(ParamPrefixEnum.GREATERTHAN, lowerBound.getPrefix());
		assertEquals(ParamPrefixEnum.LESSTHAN, upperBound.getPrefix());

		/*
		 * When DateParam (which extends DateTimeDt) gets passed in, make sure we preserve the comparators..
		 */
		DateRangeParam param = new DateRangeParam(lowerBound, upperBound);
		ourLog.info(param.toString());
		assertEquals(ParamPrefixEnum.GREATERTHAN, param.getLowerBound().getPrefix());
		assertEquals(ParamPrefixEnum.LESSTHAN, param.getUpperBound().getPrefix());

		param = new DateRangeParam(new DateTimeDt(lowerBound.getValue()), new DateTimeDt(upperBound.getValue()));
		ourLog.info(param.toString());
		assertEquals(ParamPrefixEnum.GREATERTHAN_OR_EQUALS, param.getLowerBound().getPrefix());
		assertEquals(ParamPrefixEnum.LESSTHAN_OR_EQUALS, param.getUpperBound().getPrefix());

	}

	@Test
	public void testRangeFromDates() {
		TimeZone tz = TimeZone.getDefault();
		TimeZone.setDefault(TimeZone.getTimeZone("America/Toronto"));
		try {
			Date startDate = new InstantDt("2010-01-01T00:00:00.000Z").getValue();
			Date endDate = new InstantDt("2010-01-01T00:00:00.001Z").getValue();
			DateTimeDt startDateTime = new DateTimeDt(startDate, TemporalPrecisionEnum.MILLI);
			DateTimeDt endDateTime = new DateTimeDt(endDate, TemporalPrecisionEnum.MILLI);

			DateRangeParam range = new DateRangeParam(startDateTime, endDateTime);
			assertEquals("2009-12-31T19:00:00.000-05:00", range.getValuesAsQueryTokens().get(0).getValueAsString());
			assertEquals("2009-12-31T19:00:00.001-05:00", range.getValuesAsQueryTokens().get(1).getValueAsString());

			// Now try with arguments reversed (should still create same range)
			range = new DateRangeParam(endDateTime, startDateTime);
			assertEquals("2009-12-31T19:00:00.000-05:00", range.getValuesAsQueryTokens().get(0).getValueAsString());
			assertEquals("2009-12-31T19:00:00.001-05:00", range.getValuesAsQueryTokens().get(1).getValueAsString());

		} finally {
			TimeZone.setDefault(tz);
		}
	}

	@Test
	public void testSecond() throws Exception {
		assertEquals(parseLowerForTimePrecision("2011-01-01 00:00:00.0000"), create("ge2011-01-01T00:00:00", "lt2011-01-01T01:00:00").getLowerBoundAsInstant());
		assertEquals(parseUpperForTimePrecision("2011-01-01 02:00:00.0000"), create("ge2011-01-01T00:00:00", "lt2011-01-01T02:00:00").getUpperBoundAsInstant());

		assertEquals(parseLowerForTimePrecision("2011-01-01 00:00:01.0000"), create("gt2011-01-01T00:00:00", "le2011-01-01T02:00:00").getLowerBoundAsInstant());
		assertEquals(parseUpperForTimePrecision("2011-01-01 02:00:01.0000"), create("gt2011-01-01T00:00:00", "le2011-01-01T02:00:00").getUpperBoundAsInstant());
	}

	@Test
	public void testYear() throws Exception {
		assertEquals(parseLowerForDatePrecision("2011-01-01 00:00:00.0000"), create("ge2011", "lt2012").getLowerBoundAsInstant());
		assertEquals(parseUpperForDatePrecision("2012-01-02 00:00:00.0000"), create("ge2011", "lt2012").getUpperBoundAsInstant());

		assertEquals(parseLowerForDatePrecision("2012-01-01 00:00:00.0000"), create("gt2011", "le2012").getLowerBoundAsInstant());
		assertEquals(parseUpperForDatePrecision("2014-01-02 00:00:00.0000"), create("gt2011", "le2013").getUpperBoundAsInstant());
	}

	@Test()
	public void testEqualsAndHashCode() {
		Date lowerBound = new Date(currentTimeMillis());
		Date upperBound = new Date(lowerBound.getTime() + SECONDS.toMillis(1));
		assertEquals(new DateRangeParam(), new DateRangeParam());

		assertEquals(new DateRangeParam(lowerBound, upperBound), new DateRangeParam(new DateParam(GREATERTHAN_OR_EQUALS, lowerBound), new DateParam(LESSTHAN_OR_EQUALS, upperBound)));
		assertEquals(new DateRangeParam(new DateParam(EQUAL, lowerBound)), new DateRangeParam(new DateParam(null, lowerBound)));
		assertEquals(new DateRangeParam(new DateParam(EQUAL, lowerBound)), new DateRangeParam(new DateParam(EQUAL, lowerBound), new DateParam(EQUAL, lowerBound)));
		assertEquals(new DateRangeParam(lowerBound, null), new DateRangeParam(new DateParam(GREATERTHAN_OR_EQUALS, lowerBound), null));
		assertEquals(new DateRangeParam(null, upperBound), new DateRangeParam(null, new DateParam(LESSTHAN_OR_EQUALS, upperBound)));
	}

	public static class DummyPatientResourceProvider implements IResourceProvider {

		@Override
		public Class<Patient> getResourceType() {
			return Patient.class;
		}


		@Search()
		public List<Patient> search(@RequiredParam(name = Patient.SP_BIRTHDATE) DateRangeParam theDateRange) {
			ourLastDateRange = theDateRange;

			ArrayList<Patient> retVal = new ArrayList<>();

			Patient patient = new Patient();
			patient.setId("1");
			patient.addIdentifier().setSystem("system").setValue("hello");
			retVal.add(patient);
			return retVal;
		}

	}

	private static DateRangeParam create(String theLower, String theUpper) throws InvalidRequestException {
		DateRangeParam p = new DateRangeParam();
		List<QualifiedParamList> tokens = new ArrayList<>();
		tokens.add(QualifiedParamList.singleton(null, theLower));
		if (theUpper != null) {
			tokens.add(QualifiedParamList.singleton(null, theUpper));
		}
		p.setValuesAsQueryTokens(ourCtx, null, tokens);
		return p;
	}

	private static Date parseLowerForDatePrecision(String theString) throws ParseException {
		Date retVal = ourFmtLower.parse(theString);
		retVal = DateUtils.addDays(retVal, -1);
		return retVal;
	}

	private static Date parseLowerForTimePrecision(String theString) throws ParseException {
		return ourFmtLowerForTime.parse(theString);
	}

	private static Date parseUpperForDatePrecision(String theString) throws ParseException {
		return new Date(ourFmtUpper.parse(theString).getTime() - 1L);
	}

	private static Date parseUpperForTimePrecision(String theString) throws ParseException {
		return new Date(ourFmtUpperForTime.parse(theString).getTime() - 1L);
	}

	@AfterAll
	public static void afterClassClearContext() throws Exception {
		JettyUtil.closeServer(ourServer);
		TestUtil.randomizeLocaleAndTimezone();
	}

	@BeforeAll
	public static void beforeClass() throws Exception {
		ourServer = new Server(0);

		DummyPatientResourceProvider patientProvider = new DummyPatientResourceProvider();

		ServletHandler proxyHandler = new ServletHandler();
		RestfulServer servlet = new RestfulServer(ourCtx);
		servlet.getFhirContext().setNarrativeGenerator(new DefaultThymeleafNarrativeGenerator());

		servlet.setResourceProviders(patientProvider);
		ServletHolder servletHolder = new ServletHolder(servlet);
		proxyHandler.addServletWithMapping(servletHolder, "/*");
		ourServer.setHandler(proxyHandler);
		JettyUtil.startServer(ourServer);
		ourPort = JettyUtil.getPortForStartedServer(ourServer);

		PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(5000, TimeUnit.MILLISECONDS);
		HttpClientBuilder builder = HttpClientBuilder.create();
		builder.setConnectionManager(connectionManager);
		ourClient = builder.build();

		ourBaseUrl = "http://localhost:" + ourPort + "/Patient";
	}

}
