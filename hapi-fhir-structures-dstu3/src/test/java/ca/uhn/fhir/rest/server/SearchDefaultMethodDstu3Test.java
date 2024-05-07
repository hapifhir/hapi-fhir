package ca.uhn.fhir.rest.server;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.annotation.OptionalParam;
import ca.uhn.fhir.rest.annotation.RawParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.param.StringAndListParam;
import ca.uhn.fhir.test.utilities.HttpClientExtension;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import ca.uhn.fhir.util.TestUtil;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.hl7.fhir.dstu3.model.HumanName;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class SearchDefaultMethodDstu3Test {

	private static final FhirContext ourCtx = FhirContext.forDstu3Cached();
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(SearchDefaultMethodDstu3Test.class);
	private static int ourPort;
	private static String ourLastMethod;
	private static StringAndListParam ourLastParam1;
	private static StringAndListParam ourLastParam2;


	@RegisterExtension
	public RestfulServerExtension ourServer  = new RestfulServerExtension(ourCtx)
		 .setDefaultResponseEncoding(EncodingEnum.XML)
		 .registerProvider(new DummyPatientResourceProvider())
		 .withPagingProvider(new FifoMemoryPagingProvider(100))
		 .setDefaultPrettyPrint(false);

	@RegisterExtension
	public HttpClientExtension ourClient = new HttpClientExtension();

	@BeforeEach
	public void before() {
		ourLastMethod = null;
		ourLastParam1 = null;
		ourLastParam2 = null;
		ourLastAdditionalParams = null;
	}

	@Test
	public void testSearchNoParams() throws Exception {
		HttpGet httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient");
		CloseableHttpResponse status = ourClient.execute(httpGet);
		try {
			String responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(responseContent);
			assertEquals(200, status.getStatusLine().getStatusCode());

			assertThat(ourLastMethod).isIn("search01", "search02", "search03");
			assertNull(ourLastParam1);
			assertNull(ourLastParam2);
			assertNull(ourLastAdditionalParams);

		} finally {
			IOUtils.closeQuietly(status.getEntity().getContent());
		}

	}

	@Test
	public void testSearchOneOptionalParam() throws Exception {
		HttpGet httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient?param1=val1");
		CloseableHttpResponse status = ourClient.execute(httpGet);
		try {
			String responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(responseContent);
			assertEquals(200, status.getStatusLine().getStatusCode());

			assertThat(ourLastParam1.getValuesAsQueryTokens()).hasSize(1);
			assertThat(ourLastParam1.getValuesAsQueryTokens().get(0).getValuesAsQueryTokens()).hasSize(1);
			assertEquals("val1", ourLastParam1.getValuesAsQueryTokens().get(0).getValuesAsQueryTokens().get(0).getValue());
			assertNull(ourLastParam2);
			assertNull(ourLastAdditionalParams);

		} finally {
			IOUtils.closeQuietly(status.getEntity().getContent());
		}

	}

	@Test
	public void testSearchTwoOptionalParams() throws Exception {
		HttpGet httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient?param1=val1&param2=val2");
		CloseableHttpResponse status = ourClient.execute(httpGet);
		try {
			String responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(responseContent);
			assertEquals(200, status.getStatusLine().getStatusCode());

			assertThat(ourLastParam1.getValuesAsQueryTokens()).hasSize(1);
			assertThat(ourLastParam1.getValuesAsQueryTokens().get(0).getValuesAsQueryTokens()).hasSize(1);
			assertEquals("val1", ourLastParam1.getValuesAsQueryTokens().get(0).getValuesAsQueryTokens().get(0).getValue());

			assertThat(ourLastParam2.getValuesAsQueryTokens()).hasSize(1);
			assertThat(ourLastParam2.getValuesAsQueryTokens().get(0).getValuesAsQueryTokens()).hasSize(1);
			assertEquals("val2", ourLastParam2.getValuesAsQueryTokens().get(0).getValuesAsQueryTokens().get(0).getValue());

			assertNull(ourLastAdditionalParams);

		} finally {
			IOUtils.closeQuietly(status.getEntity().getContent());
		}

	}

	@Test
	public void testSearchTwoOptionalParamsAndExtraParam() throws Exception {
		HttpGet httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient?param1=val1&param2=val2&param3=val3&_pretty=true");
		CloseableHttpResponse status = ourClient.execute(httpGet);
		try {
			String responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(responseContent);
			assertEquals(200, status.getStatusLine().getStatusCode());

			assertEquals("search03", ourLastMethod);

			assertThat(ourLastParam1.getValuesAsQueryTokens()).hasSize(1);
			assertThat(ourLastParam1.getValuesAsQueryTokens().get(0).getValuesAsQueryTokens()).hasSize(1);
			assertEquals("val1", ourLastParam1.getValuesAsQueryTokens().get(0).getValuesAsQueryTokens().get(0).getValue());

			assertThat(ourLastParam2.getValuesAsQueryTokens()).hasSize(1);
			assertThat(ourLastParam2.getValuesAsQueryTokens().get(0).getValuesAsQueryTokens()).hasSize(1);
			assertEquals("val2", ourLastParam2.getValuesAsQueryTokens().get(0).getValuesAsQueryTokens().get(0).getValue());

			ourLog.info(ourLastAdditionalParams.toString());
			assertThat(ourLastAdditionalParams).hasSize(1);
			assertEquals("val3", ourLastAdditionalParams.get("param3").get(0));

		} finally {
			IOUtils.closeQuietly(status.getEntity().getContent());
		}

	}

	@Test
	public void testSearchTwoOptionalParamsWithQualifierAndExtraParam() throws Exception {
		HttpGet httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient?param1=val1&param2=val2&param2:exact=val2e&param3=val3&_pretty=true");
		CloseableHttpResponse status = ourClient.execute(httpGet);
		try {
			String responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(responseContent);
			assertEquals(200, status.getStatusLine().getStatusCode());

			assertEquals("search03", ourLastMethod);

			assertThat(ourLastParam1.getValuesAsQueryTokens()).hasSize(1);
			assertThat(ourLastParam1.getValuesAsQueryTokens().get(0).getValuesAsQueryTokens()).hasSize(1);
			assertEquals("val1", ourLastParam1.getValuesAsQueryTokens().get(0).getValuesAsQueryTokens().get(0).getValue());

			assertThat(ourLastParam2.getValuesAsQueryTokens()).as(ourLastParam2.toString()).hasSize(2);
			assertThat(ourLastParam2.getValuesAsQueryTokens().get(0).getValuesAsQueryTokens()).hasSize(1);
			assertEquals("val2", ourLastParam2.getValuesAsQueryTokens().get(0).getValuesAsQueryTokens().get(0).getValue());
			assertEquals("val2e", ourLastParam2.getValuesAsQueryTokens().get(1).getValuesAsQueryTokens().get(0).getValue());

			ourLog.info(ourLastAdditionalParams.toString());
			assertThat(ourLastAdditionalParams).hasSize(1);
			assertEquals("val3", ourLastAdditionalParams.get("param3").get(0));

		} finally {
			IOUtils.closeQuietly(status.getEntity().getContent());
		}

	}

	@AfterAll
	public static void afterClassClearContext() throws Exception {
		TestUtil.randomizeLocaleAndTimezone();
	}

	private static Map<String, List<String>> ourLastAdditionalParams;

	public static class DummyPatientResourceProvider implements IResourceProvider {

		@Override
		public Class<? extends IBaseResource> getResourceType() {
			return Patient.class;
		}

		@Search()
		public List<Patient> search01(
				@OptionalParam(name = "param1") StringAndListParam theParam1) {
			ourLastMethod = "search01";
			ourLastParam1 = theParam1;
			ArrayList<Patient> retVal = new ArrayList<Patient>();
			retVal.add((Patient) new Patient().addName(new HumanName().setFamily("FAMILY")).setId("1"));
			return retVal;
		}

		@Search()
		public List<Patient> search02(
				@OptionalParam(name = "param1") StringAndListParam theParam1,
				@OptionalParam(name = "param2") StringAndListParam theParam2) {
			ourLastMethod = "search02";
			ourLastParam1 = theParam1;
			ourLastParam2 = theParam2;
			ArrayList<Patient> retVal = new ArrayList<Patient>();
			retVal.add((Patient) new Patient().addName(new HumanName().setFamily("FAMILY")).setId("1"));
			return retVal;
		}

		@Search(allowUnknownParams = true)
		public List<Patient> search03(
				@OptionalParam(name = "param1") StringAndListParam theParam1,
				@OptionalParam(name = "param2") StringAndListParam theParam2,
				@RawParam() Map<String, List<String>> theAdditionalParams) {
			ourLastMethod = "search03";
			ourLastParam1 = theParam1;
			ourLastParam2 = theParam2;
			ourLastAdditionalParams = theAdditionalParams;
			ArrayList<Patient> retVal = new ArrayList<Patient>();
			retVal.add((Patient) new Patient().addName(new HumanName().setFamily("FAMILY")).setId("1"));
			return retVal;
		}

	}

}
