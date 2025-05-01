package ca.uhn.fhir.rest.server;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.annotation.Create;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.test.utilities.HttpClientExtension;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import ca.uhn.fhir.util.TestUtil;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.hl7.fhir.dstu3.model.Binary;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import static org.assertj.core.api.Assertions.assertThat;

public class CreateBinaryDstu3Test {
	private static final FhirContext ourCtx = FhirContext.forDstu3Cached();
	private static Binary ourLastBinary;
	private static byte[] ourLastBinaryBytes;
	private static String ourLastBinaryString;

	@RegisterExtension
	private RestfulServerExtension ourServer  = new RestfulServerExtension(ourCtx)
		 .setDefaultResponseEncoding(EncodingEnum.XML)
		 .registerProvider(new BinaryProvider())
		 .withPagingProvider(new FifoMemoryPagingProvider(100))
		 .setDefaultPrettyPrint(false);

	@RegisterExtension
	private HttpClientExtension ourClient = new HttpClientExtension();

	@BeforeEach
	public void before() {
		ourLastBinary = null;
		ourLastBinaryBytes = null;
		ourLastBinaryString = null;
	}

	@Test
	public void testRawBytesBinaryContentType() throws Exception {
		HttpPost post = new HttpPost(ourServer.getBaseUrl() + "/Binary");
		post.setEntity(new ByteArrayEntity(new byte[] { 0, 1, 2, 3, 4 }));
		post.addHeader("Content-Type", "application/foo");
		CloseableHttpResponse status = ourClient.execute(post);
		try {
			assertEquals("application/foo", ourLastBinary.getContentType());
			assertThat(ourLastBinary.getContent()).containsExactly(new byte[]{0, 1, 2, 3, 4});
			assertThat(ourLastBinaryBytes).containsExactly(new byte[]{0, 1, 2, 3, 4});
		} finally {
			IOUtils.closeQuietly(status);
		}
	}

	/**
	 * Technically the client shouldn't be doing it this way, but we'll be accepting
	 */
	@Test
	public void testRawBytesFhirContentType() throws Exception {

		Binary b = new Binary();
		b.setContentType("application/foo");
		b.setContent(new byte[] { 0, 1, 2, 3, 4 });
		String encoded = ourCtx.newJsonParser().encodeResourceToString(b);

		HttpPost post = new HttpPost(ourServer.getBaseUrl() + "/Binary");
		post.setEntity(new StringEntity(encoded));
		post.addHeader("Content-Type", Constants.CT_FHIR_JSON);
		CloseableHttpResponse status = ourClient.execute(post);
		try {
			assertEquals("application/foo", ourLastBinary.getContentType());
			assertThat(ourLastBinary.getContent()).containsExactly(new byte[]{0, 1, 2, 3, 4});
		} finally {
			IOUtils.closeQuietly(status);
		}
	}

	@Test
	public void testRawBytesFhirContentTypeContainingFhir() throws Exception {

		Patient p = new Patient();
		p.getText().setDivAsString("A PATIENT");

		Binary b = new Binary();
		b.setContentType("application/xml+fhir");
		b.setContent(ourCtx.newXmlParser().encodeResourceToString(p).getBytes("UTF-8"));
		String encoded = ourCtx.newJsonParser().encodeResourceToString(b);

		HttpPost post = new HttpPost(ourServer.getBaseUrl() + "/Binary");
		post.setEntity(new StringEntity(encoded));
		post.addHeader("Content-Type", Constants.CT_FHIR_JSON);
		CloseableHttpResponse status = ourClient.execute(post);
		try {
			assertEquals("application/xml+fhir", ourLastBinary.getContentType());
			assertThat(ourLastBinary.getContent()).containsExactly(b.getContent());
			assertEquals(encoded, ourLastBinaryString);
			assertThat(ourLastBinaryBytes).containsExactly(encoded.getBytes("UTF-8"));
		} finally {
			IOUtils.closeQuietly(status);
		}
	}

	@Test
	public void testRawBytesNoContentType() throws Exception {
		HttpPost post = new HttpPost(ourServer.getBaseUrl() + "/Binary");
		post.setEntity(new ByteArrayEntity(new byte[] { 0, 1, 2, 3, 4 }));
		CloseableHttpResponse status = ourClient.execute(post);
		try {
			assertNull(ourLastBinary.getContentType());
			assertThat(ourLastBinary.getContent()).containsExactly(new byte[]{0, 1, 2, 3, 4});
		} finally {
			IOUtils.closeQuietly(status);
		}
	}

	@AfterAll
	public static void afterClassClearContext() throws Exception {
		TestUtil.randomizeLocaleAndTimezone();
	}

	public static class BinaryProvider implements IResourceProvider {

		@Create()
		public MethodOutcome createBinary(@ResourceParam Binary theBinary, @ResourceParam String theBinaryString, @ResourceParam byte[] theBinaryBytes) {
			ourLastBinary = theBinary;
			ourLastBinaryString = theBinaryString;
			ourLastBinaryBytes = theBinaryBytes;
			return new MethodOutcome(new IdType("Binary/001/_history/002"));
		}

		@Override
		public Class<? extends IBaseResource> getResourceType() {
			return Binary.class;
		}

	}

}
