package ca.uhn.fhir.rest.param;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.TemporalPrecisionEnum;
import ca.uhn.fhir.model.primitive.DateTimeDt;
import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.api.QualifiedParamList;
import ca.uhn.fhir.rest.server.FifoMemoryPagingProvider;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.test.utilities.HttpClientExtension;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import ca.uhn.fhir.util.TestUtil;
import com.google.common.base.Charsets;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
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

import static ca.uhn.fhir.rest.param.ParamPrefixEnum.EQUAL;
import static ca.uhn.fhir.rest.param.ParamPrefixEnum.GREATERTHAN_OR_EQUALS;
import static ca.uhn.fhir.rest.param.ParamPrefixEnum.LESSTHAN_OR_EQUALS;
import static java.lang.System.currentTimeMillis;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;

public class DateRangeParamR4Test {

	private static final SimpleDateFormat ourFmtLower;
	private static final SimpleDateFormat ourFmtUpper;
	private static final SimpleDateFormat ourFmtLowerForTime;
	private static final SimpleDateFormat ourFmtUpperForTime;
	private static final Logger ourLog = LoggerFactory.getLogger(DateRangeParamR4Test.class);
	private static final FhirContext ourCtx = FhirContext.forR4Cached();
	private static DateRangeParam ourLastDateRange;

	static {
		ourFmtLower = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSS");
		ourFmtLower.setTimeZone(TimeZone.getTimeZone("GMT-10:00"));

		ourFmtUpper = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSS");
		ourFmtUpper.setTimeZone(TimeZone.getTimeZone("GMT+12:00"));

		ourFmtLowerForTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSS");
		ourFmtUpperForTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSS");
	}

	@RegisterExtension
	private RestfulServerExtension ourServer  = new RestfulServerExtension(ourCtx)
		 .setDefaultResponseEncoding(EncodingEnum.XML)
		 .registerProvider(new DummyPatientResourceProvider())
		 .withPagingProvider(new FifoMemoryPagingProvider(100))
		 .setDefaultPrettyPrint(false);

	@RegisterExtension
	private HttpClientExtension ourClient = new HttpClientExtension();


	@BeforeEach
	public void before() {
		ourLastDateRange = null;
	}

	@Test
	public void testSearchForMultipleUnqualifiedDate() throws Exception {
		String baseUrl = ourServer.getBaseUrl() + "/Patient?" + Patient.SP_BIRTHDATE + "=";
		HttpGet httpGet = new HttpGet(baseUrl + "2012-01-01&" + Patient.SP_BIRTHDATE + "=2012-02-03");
		CloseableHttpResponse status = ourClient.execute(httpGet);
		consumeResponse(status);
		assertThat(status.getStatusLine().getStatusCode()).isEqualTo(400);

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
		HttpGet httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient?birthdate=2012-01-01");
		CloseableHttpResponse status = ourClient.execute(httpGet);
		consumeResponse(status);
		assertThat(status.getStatusLine().getStatusCode()).isEqualTo(200);

		assertThat(ourLastDateRange.getLowerBound().getValueAsString()).isEqualTo("2012-01-01");
		assertThat(ourLastDateRange.getUpperBound().getValueAsString()).isEqualTo("2012-01-01");

		assertThat(ourLastDateRange.getLowerBoundAsInstant()).isEqualTo(parseLowerForDatePrecision("2012-01-01 00:00:00.0000"));
		assertThat(ourLastDateRange.getUpperBoundAsInstant()).isEqualTo(parseUpperForDatePrecision("2012-01-03 00:00:00.0000"));
		assertThat(ourLastDateRange.getLowerBound().getPrefix()).isEqualTo(ParamPrefixEnum.EQUAL);
		assertThat(ourLastDateRange.getUpperBound().getPrefix()).isEqualTo(ParamPrefixEnum.EQUAL);
	}

	@Test
	public void testSearchForOneQualifiedDateEq() throws Exception {
		HttpGet httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient?birthdate=eq2012-01-01");
		CloseableHttpResponse status = ourClient.execute(httpGet);
		consumeResponse(status);
		assertThat(status.getStatusLine().getStatusCode()).isEqualTo(200);

		assertThat(ourLastDateRange.getLowerBound().getValueAsString()).isEqualTo("2012-01-01");
		assertThat(ourLastDateRange.getUpperBound().getValueAsString()).isEqualTo("2012-01-01");

		assertThat(ourLastDateRange.getLowerBoundAsInstant()).isEqualTo(parseLowerForDatePrecision("2012-01-01 00:00:00.0000"));
		assertThat(ourLastDateRange.getUpperBoundAsInstant()).isEqualTo(parseUpperForDatePrecision("2012-01-03 00:00:00.0000"));
		assertThat(ourLastDateRange.getLowerBound().getPrefix()).isEqualTo(ParamPrefixEnum.EQUAL);
		assertThat(ourLastDateRange.getUpperBound().getPrefix()).isEqualTo(ParamPrefixEnum.EQUAL);
	}

	@Test
	public void testSearchForOneQualifiedDateGt() throws Exception {
		HttpGet httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient?birthdate=gt2012-01-01");
		CloseableHttpResponse status = ourClient.execute(httpGet);
		consumeResponse(status);
		assertThat(status.getStatusLine().getStatusCode()).isEqualTo(200);

		assertThat(ourLastDateRange.getLowerBound().getValueAsString()).isEqualTo("2012-01-01");
		assertThat(ourLastDateRange.getUpperBound()).isEqualTo(null);

		assertThat(ourLastDateRange.getLowerBoundAsInstant()).isEqualTo(parseLowerForDatePrecision("2012-01-02 00:00:00.0000"));
		assertThat(ourLastDateRange.getUpperBoundAsInstant()).isEqualTo(null);
		assertThat(ourLastDateRange.getLowerBound().getPrefix()).isEqualTo(ParamPrefixEnum.GREATERTHAN);
		assertThat(ourLastDateRange.getUpperBound()).isEqualTo(null);
	}

	@Test
	public void testSearchForOneQualifiedDateLt() throws Exception {
		HttpGet httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient?birthdate=lt2012-01-01");
		CloseableHttpResponse status = ourClient.execute(httpGet);
		consumeResponse(status);
		assertThat(status.getStatusLine().getStatusCode()).isEqualTo(200);

		assertThat(ourLastDateRange.getLowerBound()).isEqualTo(null);
		assertThat(ourLastDateRange.getUpperBound().getValueAsString()).isEqualTo("2012-01-01");

		assertThat(ourLastDateRange.getLowerBoundAsInstant()).isEqualTo(null);
		assertThat(ourLastDateRange.getUpperBoundAsInstant()).isEqualTo(parseUpperForDatePrecision("2012-01-02 00:00:00.0000"));
		assertThat(ourLastDateRange.getLowerBound()).isEqualTo(null);
		assertThat(ourLastDateRange.getUpperBound().getPrefix()).isEqualTo(ParamPrefixEnum.LESSTHAN);
	}

	@Test
	public void testSearchForOneQualifiedDateGe() throws Exception {
		HttpGet httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient?birthdate=ge2012-01-01");
		CloseableHttpResponse status = ourClient.execute(httpGet);
		consumeResponse(status);
		assertThat(status.getStatusLine().getStatusCode()).isEqualTo(200);

		assertThat(ourLastDateRange.getLowerBound().getValueAsString()).isEqualTo("2012-01-01");
		assertThat(ourLastDateRange.getUpperBound()).isEqualTo(null);

		assertThat(ourLastDateRange.getLowerBoundAsInstant()).isEqualTo(parseLowerForDatePrecision("2012-01-01 00:00:00.0000"));
		assertThat(ourLastDateRange.getUpperBoundAsInstant()).isEqualTo(null);
		assertThat(ourLastDateRange.getLowerBound().getPrefix()).isEqualTo(ParamPrefixEnum.GREATERTHAN_OR_EQUALS);
		assertThat(ourLastDateRange.getUpperBound()).isEqualTo(null);
	}

	@Test
	public void testSearchForOneQualifiedDateLe() throws Exception {
		HttpGet httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient?birthdate=le2012-01-01");
		CloseableHttpResponse status = ourClient.execute(httpGet);
		consumeResponse(status);
		assertThat(status.getStatusLine().getStatusCode()).isEqualTo(200);

		assertThat(ourLastDateRange.getLowerBound()).isEqualTo(null);
		assertThat(ourLastDateRange.getUpperBound().getValueAsString()).isEqualTo("2012-01-01");

		assertThat(ourLastDateRange.getLowerBoundAsInstant()).isEqualTo(null);
		assertThat(ourLastDateRange.getUpperBoundAsInstant()).isEqualTo(parseUpperForDatePrecision("2012-01-03 00:00:00.0000"));
		assertThat(ourLastDateRange.getLowerBound()).isEqualTo(null);
		assertThat(ourLastDateRange.getUpperBound().getPrefix()).isEqualTo(ParamPrefixEnum.LESSTHAN_OR_EQUALS);
	}

	@Test
	public void testSearchForOneQualifiedDateNe() throws Exception {
		HttpGet httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient?birthdate=ne2012-01-01");
		CloseableHttpResponse status = ourClient.execute(httpGet);
		consumeResponse(status);
		assertThat(status.getStatusLine().getStatusCode()).isEqualTo(200);

		assertThat(ourLastDateRange.getLowerBound().getValueAsString()).isEqualTo("2012-01-01");
		assertThat(ourLastDateRange.getUpperBound().getValueAsString()).isEqualTo("2012-01-01");

		assertThat(ourLastDateRange.getLowerBound().getPrefix()).isEqualTo(ParamPrefixEnum.NOT_EQUAL);
		assertThat(ourLastDateRange.getUpperBound().getPrefix()).isEqualTo(ParamPrefixEnum.NOT_EQUAL);
	}

	@Test
	public void testRangeWithDatePrecision() throws Exception {
		HttpGet httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient?birthdate=gt2012-01-01&birthdate=lt2012-01-03");
		CloseableHttpResponse status = ourClient.execute(httpGet);
		consumeResponse(status);
		assertThat(status.getStatusLine().getStatusCode()).isEqualTo(200);

		assertThat(ourLastDateRange.getLowerBound().getValueAsString()).isEqualTo("2012-01-01");
		Date lowerBoundInstant = ourLastDateRange.getLowerBoundAsInstant();
		Date midnightLower = new InstantDt("2012-01-01T00:00:00Z").getValue();
		assertThat(lowerBoundInstant.after(midnightLower)).isTrue();

		assertThat(ourLastDateRange.getUpperBound().getValueAsString()).isEqualTo("2012-01-03");
		Date upperBoundInstant = ourLastDateRange.getUpperBoundAsInstant();
		Date midnightUpper = new InstantDt("2012-01-03T00:00:00Z").getValue();
		assertThat(upperBoundInstant.after(midnightUpper)).isTrue();

	}

	@Test
	public void testAddAnd() {
		assertThat(new DateAndListParam().addAnd(new DateOrListParam()).getValuesAsQueryTokens()).hasSize(1);
		assertThat(new NumberAndListParam().addAnd(new NumberOrListParam()).getValuesAsQueryTokens()).hasSize(1);
		assertThat(new ReferenceAndListParam().addAnd(new ReferenceOrListParam()).getValuesAsQueryTokens()).hasSize(1);
		assertThat(new QuantityAndListParam().addAnd(new QuantityOrListParam()).getValuesAsQueryTokens()).hasSize(1);
		assertThat(new UriAndListParam().addAnd(new UriOrListParam()).getValuesAsQueryTokens()).hasSize(1);
		assertThat(new StringAndListParam().addAnd(new StringOrListParam()).getValuesAsQueryTokens()).hasSize(1);
	}

	@Test
	public void testAndList() {
		assertThat(new DateAndListParam().newInstance()).isNotNull();
		assertThat(new NumberAndListParam().newInstance()).isNotNull();
		assertThat(new ReferenceAndListParam().newInstance()).isNotNull();
		assertThat(new QuantityAndListParam().newInstance()).isNotNull();
		assertThat(new UriAndListParam().newInstance()).isNotNull();
		assertThat(new StringAndListParam().newInstance()).isNotNull();
	}

	@Test
	public void testAndOr() {
		assertThat(new DateOrListParam().addOr(new DateParam()).getValuesAsQueryTokens()).hasSize(1);
		assertThat(new NumberOrListParam().addOr(new NumberParam()).getValuesAsQueryTokens()).hasSize(1);
		assertThat(new ReferenceOrListParam().addOr(new ReferenceParam()).getValuesAsQueryTokens()).hasSize(1);
		assertThat(new QuantityOrListParam().addOr(new QuantityParam()).getValuesAsQueryTokens()).hasSize(1);
		assertThat(new UriOrListParam().addOr(new UriParam()).getValuesAsQueryTokens()).hasSize(1);
		assertThat(new StringOrListParam().addOr(new StringParam()).getValuesAsQueryTokens()).hasSize(1);
	}

	@Test
	public void testDay() throws Exception {
		assertThat(create(">=2011-01-01", "<2011-01-02").getLowerBoundAsInstant()).isEqualTo(parseLowerForDatePrecision("2011-01-01 00:00:00.0000"));
		assertThat(create(">=2011-01-01", "<2011-01-02").getUpperBoundAsInstant()).isEqualTo(parseUpperForDatePrecision("2011-01-03 00:00:00.0000"));
		assertThat(create(">2011-01-01", "<=2011-01-02").getLowerBoundAsInstant()).isEqualTo(parseLowerForDatePrecision("2011-01-02 00:00:00.0000"));
		assertThat(create(">2011-01-01", "<=2011-01-02").getUpperBoundAsInstant()).isEqualTo(parseUpperForDatePrecision("2011-01-04 00:00:00.0000"));

		assertThat(create("ge2011-01-01", "lt2011-01-02").getLowerBoundAsInstant()).isEqualTo(parseLowerForDatePrecision("2011-01-01 00:00:00.0000"));
		assertThat(create("ge2011-01-01", "lt2011-01-02").getUpperBoundAsInstant()).isEqualTo(parseUpperForDatePrecision("2011-01-03 00:00:00.0000"));
		assertThat(create("gt2011-01-01", "le2011-01-02").getLowerBoundAsInstant()).isEqualTo(parseLowerForDatePrecision("2011-01-02 00:00:00.0000"));
		assertThat(create("gt2011-01-01", "le2011-01-02").getUpperBoundAsInstant()).isEqualTo(parseUpperForDatePrecision("2011-01-04 00:00:00.0000"));
	}

	@Test
	public void testFromQualifiedDateParam() throws Exception {
		assertThat(create("2011-01-01").getLowerBoundAsInstant()).isEqualTo(parseLowerForDatePrecision("2011-01-01 00:00:00.0000"));
		assertThat(create("2011-01-01").getUpperBoundAsInstant()).isEqualTo(parseUpperForDatePrecision("2011-01-03 00:00:00.0000"));

		assertThat(create("ge2011-01-01").getLowerBoundAsInstant()).isEqualTo(parseLowerForDatePrecision("2011-01-01 00:00:00.0000"));
		assertThat(create("ge2011-01-01").getUpperBoundAsInstant()).isEqualTo(null);

		assertThat(create("le2011-01-01").getLowerBoundAsInstant()).isEqualTo(null);
		assertThat(create("le2011-01-01").getUpperBoundAsInstant()).isEqualTo(parseUpperForDatePrecision("2011-01-03 00:00:00.0000"));
	}

	private DateRangeParam create(String theString) {
		return new DateRangeParam(new DateParam(theString));
	}

	@Test
	public void testMonth() throws Exception {
		assertThat(create("ge2011-01", "lt2011-02").getLowerBoundAsInstant()).isEqualTo(parseLowerForDatePrecision("2011-01-01 00:00:00.0000"));
		assertThat(create("ge2011-01", "lt2011-02").getUpperBoundAsInstant()).isEqualTo(parseUpperForDatePrecision("2011-02-02 00:00:00.0000"));

		assertThat(create("gt2011-01", "le2011-02").getLowerBoundAsInstant()).isEqualTo(parseLowerForDatePrecision("2011-02-01 00:00:00.0000"));
		assertThat(create("gt2011-01", "le2011-02").getUpperBoundAsInstant()).isEqualTo(parseUpperForDatePrecision("2011-03-02 00:00:00.0000"));
	}

	@Test
	public void testOnlyOneParam() throws Exception {
		assertThat(create("2011-01-01").getLowerBoundAsInstant()).isEqualTo(parseLowerForDatePrecision("2011-01-01 00:00:00.0000"));
		assertThat(create("2011-01-01").getUpperBoundAsInstant()).isEqualTo(parseUpperForDatePrecision("2011-01-03 00:00:00.0000"));
	}

	@Test
	public void testSetBoundsWithDatesInclusive() {
		DateRangeParam range = new DateRangeParam();
		range.setLowerBoundInclusive(new Date());
		range.setUpperBoundInclusive(new Date());

		assertThat(range.getLowerBound().getPrefix()).isEqualTo(ParamPrefixEnum.GREATERTHAN_OR_EQUALS);
		assertThat(range.getUpperBound().getPrefix()).isEqualTo(ParamPrefixEnum.LESSTHAN_OR_EQUALS);
	}

	@Test
	public void testSetBoundsWithDatesExclusive() {
		DateRangeParam range = new DateRangeParam();
		range.setLowerBoundExclusive(new Date(System.currentTimeMillis()));
		range.setUpperBoundExclusive(new Date(System.currentTimeMillis() + 1000));

		assertThat(range.getLowerBound().getPrefix()).isEqualTo(ParamPrefixEnum.GREATERTHAN);
		assertThat(range.getUpperBound().getPrefix()).isEqualTo(ParamPrefixEnum.LESSTHAN);
	}

	@Test
	public void testOrList() {
		assertThat(new DateOrListParam().newInstance()).isNotNull();
		assertThat(new NumberOrListParam().newInstance()).isNotNull();
		assertThat(new ReferenceOrListParam().newInstance()).isNotNull();
		assertThat(new QuantityOrListParam().newInstance()).isNotNull();
		assertThat(new UriOrListParam().newInstance()).isNotNull();
		assertThat(new StringOrListParam().newInstance()).isNotNull();
	}

	@Test
	public void testRange() {
		InstantDt start = new InstantDt("2015-09-23T07:43:34.811-04:00");
		InstantDt end = new InstantDt("2015-09-23T07:43:34.899-04:00");
		DateParam lowerBound = new DateParam(ParamPrefixEnum.GREATERTHAN, start.getValue());
		DateParam upperBound = new DateParam(ParamPrefixEnum.LESSTHAN, end.getValue());
		assertThat(lowerBound.getPrefix()).isEqualTo(ParamPrefixEnum.GREATERTHAN);
		assertThat(upperBound.getPrefix()).isEqualTo(ParamPrefixEnum.LESSTHAN);

		/*
		 * When DateParam (which extends DateTimeDt) gets passed in, make sure we preserve the comparators..
		 */
		DateRangeParam param = new DateRangeParam(lowerBound, upperBound);
		ourLog.info(param.toString());
		assertThat(param.getLowerBound().getPrefix()).isEqualTo(ParamPrefixEnum.GREATERTHAN);
		assertThat(param.getUpperBound().getPrefix()).isEqualTo(ParamPrefixEnum.LESSTHAN);

		param = new DateRangeParam(new DateTimeDt(lowerBound.getValue()), new DateTimeDt(upperBound.getValue()));
		ourLog.info(param.toString());
		assertThat(param.getLowerBound().getPrefix()).isEqualTo(ParamPrefixEnum.GREATERTHAN_OR_EQUALS);
		assertThat(param.getUpperBound().getPrefix()).isEqualTo(ParamPrefixEnum.LESSTHAN_OR_EQUALS);

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
			assertThat(range.getValuesAsQueryTokens().get(0).getValueAsString()).isEqualTo("2009-12-31T19:00:00.000-05:00");
			assertThat(range.getValuesAsQueryTokens().get(1).getValueAsString()).isEqualTo("2009-12-31T19:00:00.001-05:00");

			// Now try with arguments reversed (should still create same range)
			range = new DateRangeParam(endDateTime, startDateTime);
			assertThat(range.getValuesAsQueryTokens().get(0).getValueAsString()).isEqualTo("2009-12-31T19:00:00.000-05:00");
			assertThat(range.getValuesAsQueryTokens().get(1).getValueAsString()).isEqualTo("2009-12-31T19:00:00.001-05:00");

		} finally {
			TimeZone.setDefault(tz);
		}
	}

	@Test
	public void testSecond() throws Exception {
		assertThat(create("ge2011-01-01T00:00:00", "lt2011-01-01T01:00:00").getLowerBoundAsInstant()).isEqualTo(parseLowerForTimePrecision("2011-01-01 00:00:00.0000"));
		assertThat(create("ge2011-01-01T00:00:00", "lt2011-01-01T02:00:00").getUpperBoundAsInstant()).isEqualTo(parseUpperForTimePrecision("2011-01-01 02:00:00.0000"));

		assertThat(create("gt2011-01-01T00:00:00", "le2011-01-01T02:00:00").getLowerBoundAsInstant()).isEqualTo(parseLowerForTimePrecision("2011-01-01 00:00:01.0000"));
		assertThat(create("gt2011-01-01T00:00:00", "le2011-01-01T02:00:00").getUpperBoundAsInstant()).isEqualTo(parseUpperForTimePrecision("2011-01-01 02:00:01.0000"));
	}

	@Test
	public void testYear() throws Exception {
		assertThat(create("ge2011", "lt2012").getLowerBoundAsInstant()).isEqualTo(parseLowerForDatePrecision("2011-01-01 00:00:00.0000"));
		assertThat(create("ge2011", "lt2012").getUpperBoundAsInstant()).isEqualTo(parseUpperForDatePrecision("2012-01-02 00:00:00.0000"));

		assertThat(create("gt2011", "le2012").getLowerBoundAsInstant()).isEqualTo(parseLowerForDatePrecision("2012-01-01 00:00:00.0000"));
		assertThat(create("gt2011", "le2013").getUpperBoundAsInstant()).isEqualTo(parseUpperForDatePrecision("2014-01-02 00:00:00.0000"));
	}

	@Test()
	public void testEqualsAndHashCode() {
		Date lowerBound = new Date(currentTimeMillis());
		Date upperBound = new Date(lowerBound.getTime() + SECONDS.toMillis(1));
		assertThat(new DateRangeParam()).isEqualTo(new DateRangeParam());

		assertThat(new DateRangeParam(new DateParam(GREATERTHAN_OR_EQUALS, lowerBound), new DateParam(LESSTHAN_OR_EQUALS, upperBound))).isEqualTo(new DateRangeParam(lowerBound, upperBound));
		assertThat(new DateRangeParam(new DateParam(null, lowerBound))).isEqualTo(new DateRangeParam(new DateParam(EQUAL, lowerBound)));
		assertThat(new DateRangeParam(new DateParam(EQUAL, lowerBound), new DateParam(EQUAL, lowerBound))).isEqualTo(new DateRangeParam(new DateParam(EQUAL, lowerBound)));
		assertThat(new DateRangeParam(new DateParam(GREATERTHAN_OR_EQUALS, lowerBound), null)).isEqualTo(new DateRangeParam(lowerBound, null));
		assertThat(new DateRangeParam(null, new DateParam(LESSTHAN_OR_EQUALS, upperBound))).isEqualTo(new DateRangeParam(null, upperBound));
	}

	private static class DummyPatientResourceProvider implements IResourceProvider {

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
		TestUtil.randomizeLocaleAndTimezone();
	}

}
