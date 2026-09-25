package ca.uhn.fhir.rest.server;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu2.resource.Binary;
import ca.uhn.fhir.model.dstu2.resource.Bundle;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.annotation.Create;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.test.utilities.HttpTestRequest;
import ca.uhn.fhir.test.utilities.HttpTestResponse;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import ca.uhn.fhir.util.TestUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Created by dsotnikov on 2/25/2014.
 */
public class BinaryDstu2Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(BinaryDstu2Test.class);
	private static final FhirContext ourCtx = FhirContext.forDstu2Cached();
	private static Binary ourLast;

	@RegisterExtension
	public static final RestfulServerExtension ourServer = new RestfulServerExtension(ourCtx)
		.setDefaultResponseEncoding(EncodingEnum.XML)
		.registerProvider(new ResourceProvider())
		.withPagingProvider(new FifoMemoryPagingProvider(100))
		.setDefaultPrettyPrint(false);

	@BeforeEach
	public void before() {
		ourLast = null;
	}

	@Test
	public void testReadWithExplicitTypeXml() throws Exception {
		HttpTestResponse response = ourServer.fhirRequest("/Binary/foo?_format=xml").get();
		String responseContent = response.getBody();

		ourLog.info(responseContent);

		response.assertStatus(200);
		assertThat(response.getHeader("content-type")).startsWith(Constants.CT_FHIR_XML + ";");

		Binary bin = ourCtx.newXmlParser().parseResource(Binary.class, responseContent);
		assertEquals("foo", bin.getContentType());
		assertThat(bin.getContent()).containsExactly(new byte[]{1, 2, 3, 4});
	}

	@Test
	public void testReadWithExplicitTypeJson() throws Exception {
		HttpTestResponse response = ourServer.fhirRequest("/Binary/foo?_format=json").get();
		String responseContent = response.getBody();

		ourLog.info(responseContent);

		response.assertStatus(200);
		assertThat(response.getHeader("content-type")).startsWith(Constants.CT_FHIR_JSON + ";");

		Binary bin = ourCtx.newJsonParser().parseResource(Binary.class, responseContent);
		assertEquals("foo", bin.getContentType());
		assertThat(bin.getContent()).containsExactly(new byte[]{1, 2, 3, 4});
	}

	// posts Binary directly
	@Test
	public void testPostBinary() throws Exception {
		ourServer.fhirRequest("/Binary").post(new byte[]{1, 2, 3, 4}, "foo/bar; charset=UTF-8").assertStatus(201);

		assertEquals("foo/bar; charset=UTF-8", ourLast.getContentType());
		assertThat(ourLast.getContent()).containsExactly(new byte[]{1, 2, 3, 4});
	}

	// posts Binary as FHIR Resource
	@Test
	public void testPostFhirBinary() throws Exception {
		Binary res = new Binary();
		res.setContent(new byte[]{1, 2, 3, 4});
		res.setContentType("text/plain");
		String stringContent = ourCtx.newJsonParser().encodeResourceToString(res);

		ourServer.fhirRequest("/Binary").post(stringContent, Constants.CT_FHIR_JSON).assertStatus(201);

		assertEquals("text/plain", ourLast.getContentType().replace(" ", "").toLowerCase());
	}

	@Test
	public void testBinaryReadAcceptMissing() throws Exception {
		HttpTestRequest http = ourServer.fhirRequest("/Binary/foo");

		binaryRead(http);
	}

	@Test
	public void testBinaryReadAcceptBrowser() throws Exception {
		HttpTestRequest http = ourServer.fhirRequest("/Binary/foo").withHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:40.0) Gecko/20100101 Firefox/40.1")
			.withHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");

		binaryRead(http);
	}

	private void binaryRead(HttpTestRequest http) {
		HttpTestResponse status = http.get().assertStatus(200);
		byte[] responseContent = status.getBodyBytes();
		assertEquals("foo", status.getHeader("content-type"));
		assertEquals("Attachment;", status.getHeader("Content-Disposition")); // This is a security requirement!
		assertThat(responseContent).containsExactly(new byte[]{1, 2, 3, 4});
	}

	@Test
	public void testBinaryReadAcceptFhirJson() throws Exception {
		HttpTestResponse status = ourServer.fhirRequest("/Binary/foo").withHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:40.0) Gecko/20100101 Firefox/40.1")
			.withHeader("Accept", Constants.CT_FHIR_JSON).get().assertStatus(200);
		String responseContent = status.getBody();
		assertEquals(Constants.CT_FHIR_JSON + ";charset=utf-8", status.getHeader("content-type").replace(" ", "").toLowerCase());
		assertNull(status.getHeader("Content-Disposition"));
		assertEquals("{\"resourceType\":\"Binary\",\"id\":\"1\",\"contentType\":\"foo\",\"content\":\"AQIDBA==\"}", responseContent);
	}

	@Test
	public void testSearchJson() throws Exception {
		HttpTestResponse response = ourServer.fhirRequest("/Binary?_pretty=true&_format=json").get().assertStatus(200);
		String responseContent = response.getBody();
		assertEquals(Constants.CT_FHIR_JSON + ";charset=utf-8", response.getHeader("content-type").replace(" ", "").replace("UTF", "utf"));

		ourLog.info(responseContent);

		Bundle bundle = ourCtx.newJsonParser().parseResource(Bundle.class, responseContent);
		Binary bin = (Binary) bundle.getEntry().get(0).getResource();

		assertEquals("text/plain", bin.getContentType());
		assertThat(bin.getContent()).containsExactly(new byte[]{1, 2, 3, 4});
	}

	@Test
	public void testSearchXml() throws Exception {
		HttpTestResponse response = ourServer.fhirRequest("/Binary?_pretty=true").get().assertStatus(200);
		String responseContent = response.getBody();
		assertEquals(Constants.CT_FHIR_XML + ";charset=utf-8", response.getHeader("content-type").replace(" ", "").replace("UTF", "utf"));

		ourLog.info(responseContent);

		Bundle bundle = ourCtx.newXmlParser().parseResource(Bundle.class, responseContent);
		Binary bin = (Binary) bundle.getEntry().get(0).getResource();

		assertEquals("text/plain", bin.getContentType());
		assertThat(bin.getContent()).containsExactly(new byte[]{1, 2, 3, 4});
	}

	public static class ResourceProvider implements IResourceProvider {

		@Create
		public MethodOutcome create(@ResourceParam Binary theBinary) {
			ourLast = theBinary;
			return new MethodOutcome(new IdDt("1"));
		}

		@Override
		public Class<? extends IResource> getResourceType() {
			return Binary.class;
		}

		@Read
		public Binary read(@IdParam IdDt theId) {
			Binary retVal = new Binary();
			retVal.setId("1");
			retVal.setContent(new byte[]{1, 2, 3, 4});
			retVal.setContentType(theId.getIdPart());
			return retVal;
		}

		@Search
		public List<Binary> search() {
			Binary retVal = new Binary();
			retVal.setId("1");
			retVal.setContent(new byte[]{1, 2, 3, 4});
			retVal.setContentType("text/plain");
			return Collections.singletonList(retVal);
		}
	}

	@AfterAll
	public static void afterClassClearContext() throws Exception {
		TestUtil.randomizeLocaleAndTimezone();
	}

}
