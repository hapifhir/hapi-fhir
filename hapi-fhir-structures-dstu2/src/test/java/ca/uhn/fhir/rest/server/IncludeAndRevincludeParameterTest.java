package ca.uhn.fhir.rest.server;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.rest.annotation.IncludeParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.test.utilities.HttpClientExtension;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import ca.uhn.fhir.util.TestUtil;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Created by dsotnikov on 2/25/2014.
 */
public class IncludeAndRevincludeParameterTest {

	private static final FhirContext ourCtx = FhirContext.forDstu2Cached();
	private static Set<Include> ourIncludes;
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(IncludeAndRevincludeParameterTest.class);
	private static Set<Include> ourReverseIncludes;

	@RegisterExtension
	public static final RestfulServerExtension ourServer  = new RestfulServerExtension(ourCtx)
		.setDefaultResponseEncoding(EncodingEnum.XML)
		.registerProvider(new DummyPatientResourceProvider())
		.withPagingProvider(new FifoMemoryPagingProvider(100))
		.setDefaultPrettyPrint(false);

	@RegisterExtension
	public static final HttpClientExtension ourClient = new HttpClientExtension();

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

		assertThat(ourIncludes).hasSize(0);
		assertThat(ourReverseIncludes).hasSize(0);
	}
	
	@Test
	public void testWithBoth() throws Exception {
		HttpGet httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient?_query=normalInclude&_include=A.a&_include=B.b&_revinclude=C.c&_revinclude=D.d");
		HttpResponse status = ourClient.execute(httpGet);
		IOUtils.closeQuietly(status.getEntity().getContent());

		assertEquals(200, status.getStatusLine().getStatusCode());

		assertThat(ourIncludes).hasSize(2);
		assertThat(ourReverseIncludes).hasSize(2);
		assertThat(ourIncludes).containsExactlyInAnyOrder(new Include("A.a"), new Include("B.b"));
		assertThat(ourReverseIncludes).containsExactlyInAnyOrder(new Include("C.c"), new Include("D.d"));
	}


	@AfterAll
	public static void afterClassClearContext() throws Exception {
		TestUtil.randomizeLocaleAndTimezone();
	}
	
	public static class DummyPatientResourceProvider implements IResourceProvider {

		@Override
		public Class<? extends IResource> getResourceType() {
			return Patient.class;
		}

		@Search(queryName = "normalInclude")
		public List<Patient> normalInclude(
				@IncludeParam() Set<Include> theIncludes,
				@IncludeParam(reverse=true) Set<Include> theRevincludes
				) {
			ourIncludes = theIncludes;
			ourReverseIncludes = theRevincludes;
			return new ArrayList<Patient>();
		}

	}

}
