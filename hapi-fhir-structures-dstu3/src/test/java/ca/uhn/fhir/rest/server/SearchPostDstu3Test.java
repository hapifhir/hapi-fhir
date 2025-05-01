package ca.uhn.fhir.rest.server;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.annotation.OptionalParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.annotation.Sort;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.api.SortSpec;
import ca.uhn.fhir.rest.param.StringAndListParam;
import ca.uhn.fhir.rest.server.interceptor.InterceptorAdapter;
import ca.uhn.fhir.test.utilities.HttpClientExtension;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import ca.uhn.fhir.util.TestUtil;
import com.google.common.collect.Lists;
import org.apache.commons.io.IOUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.hl7.fhir.dstu3.model.HumanName;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class SearchPostDstu3Test {

	public class ParamLoggingInterceptor extends InterceptorAdapter {

		@Override
		public boolean incomingRequestPreProcessed(HttpServletRequest theRequest, HttpServletResponse theResponse) {
			ourLog.info("Params: {}", theRequest.getParameterMap());
			return true;
		}


	}

	private static final FhirContext ourCtx = FhirContext.forDstu3Cached();
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(SearchPostDstu3Test.class);
	private static String ourLastMethod;
	private static SortSpec ourLastSortSpec;
	private static StringAndListParam ourLastName;

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
		ourLastMethod = null;
		ourLastSortSpec = null;
		ourLastName = null;
		ourServer.getInterceptorService().unregisterAllInterceptors();
	}

	/**
	 * See #411
	 */
	@Test
	public void testSearchWithMixedParamsNoInterceptorsYesParams() throws Exception {
		HttpPost httpPost = new HttpPost(ourServer.getBaseUrl() + "/Patient/_search?_format=application/fhir+json");
		httpPost.addHeader("Cache-Control","no-cache");
		List<NameValuePair> parameters = Lists.newArrayList();
		parameters.add(new BasicNameValuePair("name", "Smith"));
		httpPost.setEntity(new UrlEncodedFormEntity(parameters));

		ourLog.info("Outgoing post: {}", httpPost);
		
		CloseableHttpResponse status = ourClient.execute(httpPost);
		try {
			String responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(responseContent);
			assertEquals(200, status.getStatusLine().getStatusCode());

			assertEquals("search", ourLastMethod);
			assertNull(ourLastSortSpec);
			assertThat(ourLastName.getValuesAsQueryTokens()).hasSize(1);
			assertThat(ourLastName.getValuesAsQueryTokens().get(0).getValuesAsQueryTokens()).hasSize(1);
			assertEquals("Smith", ourLastName.getValuesAsQueryTokens().get(0).getValuesAsQueryTokens().get(0).getValue());
			assertEquals(Constants.CT_FHIR_JSON_NEW, status.getEntity().getContentType().getValue().replaceAll(";.*", ""));
		} finally {
			IOUtils.closeQuietly(status.getEntity().getContent());
		}

	}

	/**
	 * See #411
	 */
	@Test
	public void testSearchWithMixedParamsNoInterceptorsNoParams() throws Exception {
		HttpPost httpPost = new HttpPost(ourServer.getBaseUrl() + "/Patient/_search");
		httpPost.addHeader("Cache-Control","no-cache");
		List<NameValuePair> parameters = Lists.newArrayList();
		parameters.add(new BasicNameValuePair("name", "Smith"));
		httpPost.setEntity(new UrlEncodedFormEntity(parameters));

		ourLog.info("Outgoing post: {}", httpPost);
		
		CloseableHttpResponse status = ourClient.execute(httpPost);
		try {
			String responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(responseContent);
			assertEquals(200, status.getStatusLine().getStatusCode());

			assertEquals("search", ourLastMethod);
			assertNull(ourLastSortSpec);
			assertThat(ourLastName.getValuesAsQueryTokens()).hasSize(1);
			assertThat(ourLastName.getValuesAsQueryTokens().get(0).getValuesAsQueryTokens()).hasSize(1);
			assertEquals("Smith", ourLastName.getValuesAsQueryTokens().get(0).getValuesAsQueryTokens().get(0).getValue());
			assertEquals(Constants.CT_FHIR_XML_NEW, status.getEntity().getContentType().getValue().replaceAll(";.*", ""));
		} finally {
			IOUtils.closeQuietly(status.getEntity().getContent());
		}

	}

	/**
	 * See #411
	 */
	@Test
	public void testSearchWithMixedParamsYesInterceptorsYesParams() throws Exception {
		ourServer.registerInterceptor(new ParamLoggingInterceptor());
		
		HttpPost httpPost = new HttpPost(ourServer.getBaseUrl() + "/Patient/_search?_format=application/fhir+json");
		httpPost.addHeader("Cache-Control","no-cache");
		List<NameValuePair> parameters = Lists.newArrayList();
		parameters.add(new BasicNameValuePair("name", "Smith"));
		httpPost.setEntity(new UrlEncodedFormEntity(parameters));

		ourLog.info("Outgoing post: {}", httpPost);
		
		CloseableHttpResponse status = ourClient.execute(httpPost);
		try {
			String responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(responseContent);
			assertEquals(200, status.getStatusLine().getStatusCode());

			assertEquals("search", ourLastMethod);
			assertNull(ourLastSortSpec);
			assertThat(ourLastName.getValuesAsQueryTokens()).hasSize(1);
			assertThat(ourLastName.getValuesAsQueryTokens().get(0).getValuesAsQueryTokens()).hasSize(1);
			assertEquals("Smith", ourLastName.getValuesAsQueryTokens().get(0).getValuesAsQueryTokens().get(0).getValue());
			assertEquals(Constants.CT_FHIR_JSON_NEW, status.getEntity().getContentType().getValue().replaceAll(";.*", ""));
		} finally {
			IOUtils.closeQuietly(status.getEntity().getContent());
		}

	}

	/**
	 * See #411
	 */
	@Test
	public void testSearchWithMixedParamsYesInterceptorsNoParams() throws Exception {
		ourServer.registerInterceptor(new ParamLoggingInterceptor());
		
		HttpPost httpPost = new HttpPost(ourServer.getBaseUrl() + "/Patient/_search");
		httpPost.addHeader("Cache-Control","no-cache");
		List<NameValuePair> parameters = Lists.newArrayList();
		parameters.add(new BasicNameValuePair("name", "Smith"));
		httpPost.setEntity(new UrlEncodedFormEntity(parameters));

		ourLog.info("Outgoing post: {}", httpPost);
		
		CloseableHttpResponse status = ourClient.execute(httpPost);
		try {
			String responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(responseContent);
			assertEquals(200, status.getStatusLine().getStatusCode());

			assertEquals("search", ourLastMethod);
			assertNull(ourLastSortSpec);
			assertThat(ourLastName.getValuesAsQueryTokens()).hasSize(1);
			assertThat(ourLastName.getValuesAsQueryTokens().get(0).getValuesAsQueryTokens()).hasSize(1);
			assertEquals("Smith", ourLastName.getValuesAsQueryTokens().get(0).getValuesAsQueryTokens().get(0).getValue());
			assertEquals(Constants.CT_FHIR_XML_NEW, status.getEntity().getContentType().getValue().replaceAll(";.*", ""));
		} finally {
			IOUtils.closeQuietly(status.getEntity().getContent());
		}

	}
	
	@AfterAll
	public static void afterClassClearContext() throws Exception {
		TestUtil.randomizeLocaleAndTimezone();
	}

	public static class DummyPatientResourceProvider implements IResourceProvider {

		@Override
		public Class<? extends IBaseResource> getResourceType() {
			return Patient.class;
		}

		//@formatter:off
		@SuppressWarnings("rawtypes")
		@Search()
		public List search(
				@Sort SortSpec theSortSpec,
				@OptionalParam(name=Patient.SP_NAME) StringAndListParam theName
				) {
			ourLastMethod = "search";
			ourLastSortSpec = theSortSpec;
			ourLastName = theName;
			ArrayList<Patient> retVal = new ArrayList<Patient>();
			retVal.add((Patient) new Patient().addName(new HumanName().setFamily("FAMILY")).setId("foo"));
			return retVal;
		}
		//@formatter:on

	}

}
