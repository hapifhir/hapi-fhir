package ca.uhn.fhir.rest.server;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.api.BundleInclusionRule;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.rest.annotation.IncludeParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.test.utilities.HttpClientExtension;
import ca.uhn.fhir.test.utilities.JettyUtil;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import ca.uhn.fhir.util.TestUtil;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.ee10.servlet.ServletHandler;
import org.eclipse.jetty.ee10.servlet.ServletHolder;
import org.hl7.fhir.dstu2.model.Patient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class IncludeAndRevincludeParameterHl7OrgTest {

  private static final FhirContext ourCtx = FhirContext.forDstu2Hl7OrgCached();
	private static Set<Include> ourIncludes;
	private static Set<Include> ourReverseIncludes;

  @RegisterExtension
  public static RestfulServerExtension ourServer = new RestfulServerExtension(ourCtx)
      .registerProvider(new DummyPatientResourceProvider())
      .withPagingProvider(new FifoMemoryPagingProvider(100))
      .setDefaultResponseEncoding(EncodingEnum.JSON)
      .withServer(s->s.setBundleInclusionRule(BundleInclusionRule.BASED_ON_RESOURCE_PRESENCE))
      .setDefaultPrettyPrint(false);

  @RegisterExtension
  public static HttpClientExtension ourClient = new HttpClientExtension();

  @BeforeEach
	public void before() {
		ourIncludes = null;
		ourReverseIncludes = null;
	}
	
	@Test
	public void testNoIncludes() throws Exception {
		HttpGet httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient?_query=normalInclude");
		HttpResponse status = ourClient.execute(httpGet);
		IOUtils.closeQuietly(status.getEntity().getContent());

		assertEquals(200, status.getStatusLine().getStatusCode());
		
		assertThat(ourIncludes, hasSize(0));
		assertThat(ourReverseIncludes, hasSize(0));
	}

	@Test
	public void testWithBoth() throws Exception {
		HttpGet httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient?_query=normalInclude&_include=A.a&_include=B.b&_revinclude=C.c&_revinclude=D.d");
		HttpResponse status = ourClient.execute(httpGet);
		IOUtils.closeQuietly(status.getEntity().getContent());

		assertEquals(200, status.getStatusLine().getStatusCode());
		
		assertThat(ourIncludes, hasSize(2));
		assertThat(ourReverseIncludes, hasSize(2));
		assertThat(ourIncludes, containsInAnyOrder(new Include("A.a"), new Include("B.b")));
		assertThat(ourReverseIncludes, containsInAnyOrder(new Include("C.c"), new Include("D.d")));
	}

	@AfterAll
	public static void afterClass() throws Exception {
    TestUtil.randomizeLocaleAndTimezone();
	}

	public static class DummyPatientResourceProvider implements IResourceProvider {

		@Search(queryName = "normalInclude")
		public List<Patient> normalInclude(
				@IncludeParam() Set<Include> theIncludes,
				@IncludeParam(reverse=true) Set<Include> theRevincludes
				) {
			ourIncludes = theIncludes;
			ourReverseIncludes = theRevincludes;
			return new ArrayList<Patient>();
		}

		@Override
		public Class<Patient> getResourceType() {
			return Patient.class;
		}

	}

}
