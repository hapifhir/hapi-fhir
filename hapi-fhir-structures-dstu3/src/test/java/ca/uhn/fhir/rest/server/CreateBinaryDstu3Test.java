package ca.uhn.fhir.rest.server;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.annotation.Create;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import ca.uhn.fhir.util.TestUtil;
import org.hl7.fhir.dstu3.model.Binary;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.nio.charset.StandardCharsets;

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

	@BeforeEach
	public void before() {
		ourLastBinary = null;
		ourLastBinaryBytes = null;
		ourLastBinaryString = null;
	}

	@Test
	public void testRawBytesBinaryContentType() throws Exception {
		ourServer.fhirRequest("/Binary").post(new byte[] { 0, 1, 2, 3, 4 }, "application/foo");
		assertEquals("application/foo", ourLastBinary.getContentType());
		assertThat(ourLastBinary.getContent()).containsExactly(new byte[]{0, 1, 2, 3, 4});
		assertThat(ourLastBinaryBytes).containsExactly(new byte[]{0, 1, 2, 3, 4});
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

		ourServer.fhirRequest("/Binary").post(encoded.getBytes(StandardCharsets.UTF_8), Constants.CT_FHIR_JSON);
		assertEquals("application/foo", ourLastBinary.getContentType());
		assertThat(ourLastBinary.getContent()).containsExactly(new byte[]{0, 1, 2, 3, 4});
	}

	@Test
	public void testRawBytesFhirContentTypeContainingFhir() throws Exception {

		Patient p = new Patient();
		p.getText().setDivAsString("A PATIENT");

		Binary b = new Binary();
		b.setContentType("application/xml+fhir");
		b.setContent(ourCtx.newXmlParser().encodeResourceToString(p).getBytes("UTF-8"));
		String encoded = ourCtx.newJsonParser().encodeResourceToString(b);

		ourServer.fhirRequest("/Binary").post(encoded.getBytes(StandardCharsets.UTF_8), Constants.CT_FHIR_JSON);
		assertEquals("application/xml+fhir", ourLastBinary.getContentType());
		assertThat(ourLastBinary.getContent()).containsExactly(b.getContent());
		assertEquals(encoded, ourLastBinaryString);
		assertThat(ourLastBinaryBytes).containsExactly(encoded.getBytes("UTF-8"));
	}

	@Test
	public void testRawBytesNoContentType() throws Exception {
		ourServer.fhirRequest("/Binary").method("POST", new byte[] { 0, 1, 2, 3, 4 }, null);
		assertNull(ourLastBinary.getContentType());
		assertThat(ourLastBinary.getContent()).containsExactly(new byte[]{0, 1, 2, 3, 4});
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
