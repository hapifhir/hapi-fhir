package ca.uhn.fhir.rest.server.interceptor;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.FifoMemoryPagingProvider;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.test.utilities.HttpClientExtension;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import ca.uhn.fhir.util.TestUtil;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ServerWithResponseHighlightingInterceptorExceptionTest {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ServerWithResponseHighlightingInterceptorExceptionTest.class);
	private static final FhirContext ourCtx = FhirContext.forR4Cached();

	@RegisterExtension
	public RestfulServerExtension ourServer = new RestfulServerExtension(ourCtx)
		 .registerProvider(new DummyPatientResourceProvider())
		 .withPagingProvider(new FifoMemoryPagingProvider(100))
		 .setDefaultResponseEncoding(EncodingEnum.XML);

	@RegisterExtension
	private HttpClientExtension ourClient = new HttpClientExtension();

	@Test
	public void testExpectedException() throws Exception {
		HttpGet httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient/123");
		HttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());
		ourLog.info(responseContent);

		assertEquals(400, status.getStatusLine().getStatusCode());
		assertThat(responseContent).contains("<diagnostics value=\"AAABBB\"/>");
	}


	@Test
	public void testUnexpectedException() throws Exception {
		HttpGet httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient?identifier=123");
		HttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());
		ourLog.info(responseContent);

		assertEquals(500, status.getStatusLine().getStatusCode());
		assertThat(responseContent).contains("<diagnostics value=\"" + Msg.code(389) + "Failed to call access method: java.lang.Error: AAABBB\"/>");
	}

	@AfterAll
	public static void afterClassClearContext() throws Exception {
		TestUtil.randomizeLocaleAndTimezone();
	}

	public static class DummyPatientResourceProvider implements IResourceProvider {

		@Override
		public Class<? extends Patient> getResourceType() {
			return Patient.class;
		}

		@Read
		public Patient read(@IdParam IdType theId) {
			throw new InvalidRequestException("AAABBB");
		}

		@Search
		public Patient search(@RequiredParam(name = "identifier") TokenParam theToken) {
			throw new Error("AAABBB");
		}


	}

}
