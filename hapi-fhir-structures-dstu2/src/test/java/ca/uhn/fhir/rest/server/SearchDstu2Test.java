package ca.uhn.fhir.rest.server;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu2.composite.IdentifierDt;
import ca.uhn.fhir.model.dstu2.resource.Bundle;
import ca.uhn.fhir.model.dstu2.resource.Bundle.Link;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.rest.annotation.Create;
import ca.uhn.fhir.rest.annotation.OptionalParam;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.param.DateAndListParam;
import ca.uhn.fhir.rest.param.ParamPrefixEnum;
import ca.uhn.fhir.rest.param.QuantityParam;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.test.utilities.HttpClientExtension;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import ca.uhn.fhir.util.TestUtil;
import com.google.common.base.Charsets;
import jakarta.annotation.Nonnull;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class SearchDstu2Test {

	private static final FhirContext ourCtx = FhirContext.forDstu2Cached();
	private static DateAndListParam ourLastDateAndList;
	private static String ourLastMethod;
	private static QuantityParam ourLastQuantity;
	private static ReferenceParam ourLastRef;
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(SearchDstu2Test.class);
	private static InstantDt ourReturnPublished;

	@RegisterExtension
	public static final RestfulServerExtension ourServer  = new RestfulServerExtension(ourCtx)
		.setDefaultResponseEncoding(EncodingEnum.XML)
		.registerProvider(new DummyPatientResourceProvider())
		.registerProvider(new DummyPatientResourceNoIdProvider())
		.withPagingProvider(new FifoMemoryPagingProvider(100))
		.setDefaultPrettyPrint(false);

	@RegisterExtension
	public static final HttpClientExtension ourClient = new HttpClientExtension();

	@BeforeEach
	public void before() {
		ourLastMethod = null;
		ourLastDateAndList = null;
		ourLastRef = null;
		ourLastQuantity = null;
		ourServer.getRestfulServer().setIgnoreServerParsedRequestParameters(true);
	}

	@Test
	public void testSearchWithInvalidPostUrl() throws Exception {
		// should end with _search
		HttpPost filePost = new HttpPost(ourServer.getBaseUrl() + "/Patient?name=Central"); 

		// add parameters to the post method
		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		parameters.add(new BasicNameValuePair("_id", "aaa"));

		UrlEncodedFormEntity sendentity = new UrlEncodedFormEntity(parameters, "UTF-8");
		filePost.setEntity(sendentity);

		HttpResponse status = ourClient.execute(filePost);
		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());
		ourLog.info(responseContent);
		assertEquals(400, status.getStatusLine().getStatusCode());
		assertThat(responseContent).contains("<diagnostics value=\"" + Msg.code(446) + "Incorrect Content-Type header value of &quot;application/x-www-form-urlencoded; charset=UTF-8&quot; was provided in the request. A FHIR Content-Type is required for &quot;CREATE&quot; operation\"/>");

	}

	@Test
	public void testEncodeConvertsReferencesToRelative() throws Exception {
		HttpGet httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient?_query=searchWithRef");
		HttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());
		ourLog.info(responseContent);

		assertThat(responseContent).doesNotContain("text");

		assertEquals(200, status.getStatusLine().getStatusCode());
		Patient patient = (Patient) ourCtx.newXmlParser().parseResource(Bundle.class, responseContent).getEntry().get(0).getResource();
		String ref = patient.getManagingOrganization().getReference().getValue();
		assertEquals("Organization/555", ref);
		assertNull(status.getFirstHeader(Constants.HEADER_CONTENT_LOCATION));
	}

	@Test
	public void testEncodeConvertsReferencesToRelativeJson() throws Exception {
		HttpGet httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient?_query=searchWithRef&_format=json");
		HttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());
		ourLog.info(responseContent);

		assertThat(responseContent).doesNotContain("text");

		assertEquals(200, status.getStatusLine().getStatusCode());
		Patient patient = (Patient) ourCtx.newJsonParser().parseResource(Bundle.class, responseContent).getEntry().get(0).getResource();
		String ref = patient.getManagingOrganization().getReference().getValue();
		assertEquals("Organization/555", ref);
		assertNull(status.getFirstHeader(Constants.HEADER_CONTENT_LOCATION));
	}

	@Test
	public void testResultBundleHasUpdateTime() throws Exception {
		ourReturnPublished = new InstantDt("2011-02-03T11:22:33Z");

		HttpGet httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient?_query=searchWithBundleProvider&_pretty=true");
		HttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());
		ourLog.info(responseContent);

		assertThat(responseContent).containsSubsequence("<lastUpdated value=\"2011-02-03T11:22:33Z\"/>");
	}

	@Test
	public void testResultBundleHasUuid() throws Exception {
		HttpGet httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient?_query=searchWithRef");
		HttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());
		ourLog.info(responseContent);

		assertEquals(200, status.getStatusLine().getStatusCode());
		assertThat(responseContent).matches(".*id value..[0-9a-f-]+\\\".*");
	}

	@Test
	public void testSearchBlacklist01Failing() throws Exception {
		HttpGet httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient?_query=searchBlacklist01&ref.black1=value");
		HttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent(), Charsets.UTF_8);
		IOUtils.closeQuietly(status.getEntity().getContent());
		ourLog.info(responseContent);
		assertEquals(400, status.getStatusLine().getStatusCode());
	}

	@Test
	public void testSearchBlacklist01Passing() throws Exception {
		HttpGet httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient?_query=searchBlacklist01&ref.white1=value");
		HttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent(), Charsets.UTF_8);
		IOUtils.closeQuietly(status.getEntity().getContent());
		ourLog.info(responseContent);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertEquals("searchBlacklist01", ourLastMethod);
	}

	@Test
	public void testSearchByPost() throws Exception {
		HttpPost httpGet = new HttpPost(ourServer.getBaseUrl() + "/Patient/_search");
		StringEntity entity = new StringEntity("searchDateAndList=2001,2002&searchDateAndList=2003,2004", ContentType.APPLICATION_FORM_URLENCODED);
		httpGet.setEntity(entity);

		HttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());
		ourLog.info(responseContent);
		assertEquals("searchDateAndList", ourLastMethod);
		assertThat(ourLastDateAndList.getValuesAsQueryTokens()).hasSize(2);
		assertThat(ourLastDateAndList.getValuesAsQueryTokens().get(0).getValuesAsQueryTokens()).hasSize(2);
		assertThat(ourLastDateAndList.getValuesAsQueryTokens().get(1).getValuesAsQueryTokens()).hasSize(2);
		assertEquals("2001", ourLastDateAndList.getValuesAsQueryTokens().get(0).getValuesAsQueryTokens().get(0).getValueAsString());
		assertEquals("2002", ourLastDateAndList.getValuesAsQueryTokens().get(0).getValuesAsQueryTokens().get(1).getValueAsString());
		assertThat(responseContent).contains("SYSTEM");
	}

	@Test
	public void testSearchMethodReturnsNull() throws Exception {
		HttpGet httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient?_query=searchReturnNull");

		HttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());
		ourLog.info(responseContent);
		assertEquals("searchReturnNull", ourLastMethod);
		assertThat(responseContent).contains("<total value=\"0\"/>");
	}

	@Test
	public void testSearchByPostWithBodyAndUrlParams() throws Exception {
		HttpPost httpGet = new HttpPost(ourServer.getBaseUrl() + "/Patient/_search?_format=json");
		StringEntity entity = new StringEntity("searchDateAndList=2001,2002&searchDateAndList=2003,2004", ContentType.APPLICATION_FORM_URLENCODED);
		httpGet.setEntity(entity);

		CloseableHttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());
		ourLog.info(responseContent);
		assertEquals("searchDateAndList", ourLastMethod);
		assertThat(ourLastDateAndList.getValuesAsQueryTokens()).hasSize(2);
		assertThat(ourLastDateAndList.getValuesAsQueryTokens().get(0).getValuesAsQueryTokens()).hasSize(2);
		assertThat(ourLastDateAndList.getValuesAsQueryTokens().get(1).getValuesAsQueryTokens()).hasSize(2);
		assertEquals("2001", ourLastDateAndList.getValuesAsQueryTokens().get(0).getValuesAsQueryTokens().get(0).getValueAsString());
		assertEquals("2002", ourLastDateAndList.getValuesAsQueryTokens().get(0).getValuesAsQueryTokens().get(1).getValueAsString());
		assertThat(responseContent).contains(":\"SYSTEM\"");

	}

	@Test
	public void testSearchByPostWithBodyAndUrlParamsNoManual() throws Exception {
		ourServer.getRestfulServer().setIgnoreServerParsedRequestParameters(false);

		HttpPost httpGet = new HttpPost(ourServer.getBaseUrl() + "/Patient/_search?_format=json");
		StringEntity entity = new StringEntity("searchDateAndList=2001,2002&searchDateAndList=2003,2004", ContentType.APPLICATION_FORM_URLENCODED);
		httpGet.setEntity(entity);

		CloseableHttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());
		ourLog.info(responseContent);
		assertEquals("searchDateAndList", ourLastMethod);
		assertThat(ourLastDateAndList.getValuesAsQueryTokens()).hasSize(2);
		assertThat(ourLastDateAndList.getValuesAsQueryTokens().get(0).getValuesAsQueryTokens()).hasSize(2);
		assertThat(ourLastDateAndList.getValuesAsQueryTokens().get(1).getValuesAsQueryTokens()).hasSize(2);
		assertEquals("2001", ourLastDateAndList.getValuesAsQueryTokens().get(0).getValuesAsQueryTokens().get(0).getValueAsString());
		assertEquals("2002", ourLastDateAndList.getValuesAsQueryTokens().get(0).getValuesAsQueryTokens().get(1).getValueAsString());
		assertThat(responseContent).contains(":\"SYSTEM\"");

	}

	@Test
	public void testSearchByPut() throws Exception {
		HttpPut httpGet = new HttpPut(ourServer.getBaseUrl() + "/Patient/_search");
		StringEntity entity = new StringEntity("searchDateAndList=2001,2002&searchDateAndList=2003,2004", ContentType.APPLICATION_FORM_URLENCODED);
		httpGet.setEntity(entity);

		HttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());
		ourLog.info(responseContent);
		assertEquals(400, status.getStatusLine().getStatusCode());
	}

	@Test
	public void testSearchDateAndList() throws Exception {
		HttpGet httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient?searchDateAndList=2001,2002&searchDateAndList=2003,2004");
		HttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());
		ourLog.info(responseContent);
		assertEquals("searchDateAndList", ourLastMethod);
		assertThat(ourLastDateAndList.getValuesAsQueryTokens()).hasSize(2);
		assertThat(ourLastDateAndList.getValuesAsQueryTokens().get(0).getValuesAsQueryTokens()).hasSize(2);
		assertThat(ourLastDateAndList.getValuesAsQueryTokens().get(1).getValuesAsQueryTokens()).hasSize(2);
		assertEquals("2001", ourLastDateAndList.getValuesAsQueryTokens().get(0).getValuesAsQueryTokens().get(0).getValueAsString());
		assertEquals("2002", ourLastDateAndList.getValuesAsQueryTokens().get(0).getValuesAsQueryTokens().get(1).getValueAsString());
	}

	/**
	 * See #247
	 */
	@Test
	public void testSearchPagesAllHaveCorrectBundleType() throws Exception {
		Bundle resp;
		{
			HttpGet httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient?searchHugeResults=yes&_count=10&_pretty=true");
			HttpResponse status = ourClient.execute(httpGet);
			String responseContent = IOUtils.toString(status.getEntity().getContent());
			IOUtils.closeQuietly(status.getEntity().getContent());
			ourLog.info(responseContent);
			resp = ourCtx.newXmlParser().parseResource(Bundle.class, responseContent);
			assertEquals("searchset", resp.getType());
			assertEquals(100, resp.getTotal().intValue());
		}
		Link nextLink = resp.getLink("next");
		assertThat(nextLink.getUrl()).startsWith("http://");

		// Now try the next page
		{
			HttpGet httpGet = new HttpGet(nextLink.getUrl());
			HttpResponse status = ourClient.execute(httpGet);
			String responseContent = IOUtils.toString(status.getEntity().getContent());
			IOUtils.closeQuietly(status.getEntity().getContent());
			ourLog.info(responseContent);
			resp = ourCtx.newXmlParser().parseResource(Bundle.class, responseContent);
			assertEquals("searchset", resp.getType());
			assertEquals(100, resp.getTotal().intValue());
		}

		nextLink = resp.getLink("next");
		assertThat(nextLink.getUrl()).startsWith("http://");

		// Now try a third page
		{
			HttpGet httpGet = new HttpGet(nextLink.getUrl());
			HttpResponse status = ourClient.execute(httpGet);
			String responseContent = IOUtils.toString(status.getEntity().getContent());
			IOUtils.closeQuietly(status.getEntity().getContent());
			ourLog.info(responseContent);
			resp = ourCtx.newXmlParser().parseResource(Bundle.class, responseContent);
			assertEquals("searchset", resp.getType());
			assertEquals(100, resp.getTotal().intValue());
		}
	}

	/**
	 * See #296
	 */
	@Test
	public void testSearchQuantityMissingTrue() throws Exception {
		HttpGet httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient?quantity:missing=true");
		HttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());
		ourLog.info(responseContent);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertEquals(Boolean.TRUE, ourLastQuantity.getMissing());
	}

	/**
	 * See #296
	 */
	@Test
	public void testSearchQuantityValue() throws Exception {
		HttpGet httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient?quantity=gt100");
		HttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());
		ourLog.info(responseContent);
		assertEquals(200, status.getStatusLine().getStatusCode());

		assertEquals(ParamPrefixEnum.GREATERTHAN, ourLastQuantity.getPrefix());
		assertEquals(100, ourLastQuantity.getValue().intValue());
	}

	@Test
	public void testSearchReferenceParams01() throws Exception {
		HttpGet httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient?_query=searchNoList&ref=123");
		HttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());
		ourLog.info(responseContent);
		assertEquals(200, status.getStatusLine().getStatusCode());

		assertEquals("123", ourLastRef.getIdPart());
		assertNull(ourLastRef.getResourceType());
	}

	@Test
	public void testSearchReferenceParams02() throws Exception {
		HttpGet httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient?_query=searchNoList&ref=Patient/123");
		HttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());
		ourLog.info(responseContent);
		assertEquals(200, status.getStatusLine().getStatusCode());

		assertEquals("123", ourLastRef.getIdPart());
		assertEquals("Patient", ourLastRef.getResourceType());
	}

	@Test
	public void testSearchReferenceParams03() throws Exception {
		HttpGet httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient?_query=searchNoList&ref:Patient=Patient/123");
		HttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());
		ourLog.info(responseContent);
		assertEquals(200, status.getStatusLine().getStatusCode());

		assertEquals("123", ourLastRef.getIdPart());
		assertEquals("Patient", ourLastRef.getResourceType());
	}

	@Test
	public void testSearchReferenceParams04() throws Exception {
		HttpGet httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient?_query=searchNoList&ref:Patient=123");
		HttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());
		ourLog.info(responseContent);
		assertEquals(200, status.getStatusLine().getStatusCode());

		assertEquals("123", ourLastRef.getIdPart());
		assertEquals("Patient", ourLastRef.getResourceType());
	}

	/**
	 * Verifies proper method binding to handle special search names(_id:[modifier], _language:[modifier])
	 */
	@Test
	public void testSearchByIdExact() throws Exception {
		HttpGet httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient?_id:exact=aaa&reference=value");
		HttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent(), Charset.defaultCharset());
		IOUtils.closeQuietly(status.getEntity().getContent());
		ourLog.info(responseContent);
		assertEquals(200, status.getStatusLine().getStatusCode());

		assertEquals("idProvider", ourLastMethod);
	}

	@Test
	public void testSearchByQualifiedIdQualifiedString() throws Exception {
		HttpGet httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient?_id:exact=aaa&stringParam:exact=value");
		HttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent(), Charset.defaultCharset());
		IOUtils.closeQuietly(status.getEntity().getContent());
		ourLog.info(responseContent);
		assertEquals(200, status.getStatusLine().getStatusCode());

		assertEquals("stringParam:true:true", ourLastMethod);
	}

	@Test
	public void testSearchByQualifiedString() throws Exception {
		HttpGet httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient?_id=aaa&stringParam:exact=value");
		HttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent(), Charset.defaultCharset());
		IOUtils.closeQuietly(status.getEntity().getContent());
		ourLog.info(responseContent);
		assertEquals(200, status.getStatusLine().getStatusCode());

		assertEquals("stringParam:false:true", ourLastMethod);
	}

	@Test
	public void testSearchByQualifiedIdString() throws Exception {
		HttpGet httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient?_id:exact=aaa&stringParam=value");
		HttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent(), Charset.defaultCharset());
		IOUtils.closeQuietly(status.getEntity().getContent());
		ourLog.info(responseContent);
		assertEquals(200, status.getStatusLine().getStatusCode());

		assertEquals("stringParam:true:false", ourLastMethod);
	}

	@Test
	public void testSearchByIdString() throws Exception {
		HttpGet httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient?_id=aaa&stringParam=value");
		HttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent(), Charset.defaultCharset());
		IOUtils.closeQuietly(status.getEntity().getContent());
		ourLog.info(responseContent);
		assertEquals(200, status.getStatusLine().getStatusCode());

		assertEquals("stringParam:false:false", ourLastMethod);
	}


	@Test
	public void testSearchWhitelist01Failing() throws Exception {
		HttpGet httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient?_query=searchWhitelist01&ref=value");
		HttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());
		ourLog.info(responseContent);
		assertEquals(400, status.getStatusLine().getStatusCode());
	}

	@Test
	public void testSearchWhitelist01Passing() throws Exception {
		HttpGet httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient?_query=searchWhitelist01&ref.white1=value");
		HttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());
		ourLog.info(responseContent);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertEquals("searchWhitelist01", ourLastMethod);
	}

	@AfterAll
	public static void afterClassClearContext() throws Exception {
		TestUtil.randomizeLocaleAndTimezone();
	}

	public static class DummyPatientResourceNoIdProvider implements IResourceProvider {

		@Override
		public Class<? extends IResource> getResourceType() {
			return Patient.class;
		}

		//@formatter:off
		@Search()
		public List<Patient> searchByRef(
			@RequiredParam(name = "reference") ReferenceParam theParam) {
			ourLastMethod = "noIdProvider";
			return Collections.emptyList();
		}
		//@formatter:on
	}

	public static class DummyPatientResourceProvider implements IResourceProvider {

		@Override
		public Class<? extends IResource> getResourceType() {
			return Patient.class;
		}

		//@formatter:off
		@Search(queryName="searchBlacklist01")
		public List<Patient> searchBlacklist01(
				@RequiredParam(chainBlacklist="black1", name = "ref") ReferenceParam theParam) {
			ourLastMethod = "searchBlacklist01";
			return Collections.emptyList();
		}
		//@formatter:on

		/**
		 * For testSearchWithInvalidPostUrl, ok to add a real body later
		 */
		@Create
		public MethodOutcome create(@ResourceParam Patient thePatient) {
			throw new UnsupportedOperationException();
		}

		//@formatter:off
		@Search()
		public List<Patient> searchByIdRef(
			@RequiredParam(name="_id") StringParam id,
			@OptionalParam(name = "reference") ReferenceParam theParam) {
			ourLastMethod = "idProvider";
			return Collections.emptyList();
		}
		//@formatter:on

		//@formatter:off
		@Search()
		public List<Patient> searchByQualifiedString(
			@RequiredParam(name="_id") StringParam id,
			@RequiredParam(name = "stringParam") StringParam stringParam) {
			ourLastMethod = "stringParam:" + id.isExact() + ":" + stringParam.isExact();
			return Collections.emptyList();
		}
		//@formatter:on

		//@formatter:off
		@Search()
		public List<Patient> searchDateAndList(
				@RequiredParam(name = "searchDateAndList") DateAndListParam theParam) {
			ourLastMethod = "searchDateAndList";
			ourLastDateAndList = theParam;
			ArrayList<Patient> retVal = new ArrayList<Patient>();
			Patient patient = new Patient();
			patient.setId("1");
			retVal.add(patient.addIdentifier(new IdentifierDt("SYSTEM", "CODE")));
			return retVal;
		}
		//@formatter:on

		//@formatter:off
		@Search()
		public List<Patient> searchHugeResults(
				@RequiredParam(name = "searchHugeResults") StringParam theParam) {
			ourLastMethod = "searchHugeResults";
			ArrayList<Patient> retVal = new ArrayList<Patient>();
			for (int i = 0; i < 100; i++) {
				Patient patient = new Patient();
				patient.setId("" + i);
				retVal.add(patient.addIdentifier(new IdentifierDt("SYSTEM", "CODE"+ i)));
			}
			return retVal;
		}
		//@formatter:on

		//@formatter:off
		@Search(queryName="searchNoList")
		public List<Patient> searchNoList(
				@RequiredParam(name = "ref") ReferenceParam theParam) {
			ourLastMethod = "searchNoList";
			ourLastRef = theParam;
			return Collections.emptyList();
		}
		//@formatter:on

		//@formatter:off
		@Search(queryName="searchReturnNull")
		public List<Patient> searchNoList() {
			ourLastMethod = "searchReturnNull";
			return null;
		}
		//@formatter:on

		//@formatter:off
		@Search()
		public List<Patient> searchQuantity(
				@RequiredParam(name="quantity") QuantityParam theParam) {
			ourLastMethod = "searchQuantity";
			ourLastQuantity = theParam;
			return Collections.emptyList();
		}
		//@formatter:on

		//@formatter:off
		@Search(queryName="searchWhitelist01")
		public List<Patient> searchWhitelist01(
				@RequiredParam(chainWhitelist="white1", name = "ref") ReferenceParam theParam) {
			ourLastMethod = "searchWhitelist01";
			return Collections.emptyList();
		}
		//@formatter:on

		@Search(queryName = "searchWithBundleProvider")
		public IBundleProvider searchWithBundleProvider() {
			return new IBundleProvider() {

				@Override
				public InstantDt getPublished() {
					return ourReturnPublished;
				}

				@Nonnull
				@Override
				public List<IBaseResource> getResources(int theFromIndex, int theToIndex) {
					throw new IllegalStateException();
				}

				@Override
				public Integer preferredPageSize() {
					return null;
				}

				@Override
				public Integer size() {
					return 0;
				}

				@Override
				public String getUuid() {
					return null;
				}
			};
		}

		@Search(queryName = "searchWithRef")
		public Patient searchWithRef() {
			Patient patient = new Patient();
			patient.setId("Patient/1/_history/1");
			patient.getManagingOrganization().setReference(ourServer.getBaseUrl() + "/Organization/555/_history/666");
			return patient;
		}

	}

}
