package ca.uhn.fhir.rest.server;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.annotation.Create;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.annotation.Update;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.test.utilities.HttpTestResponse;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import ca.uhn.fhir.util.TestUtil;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Binary;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Reference;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class 	BinaryServerR4Test {
	private static final FhirContext ourCtx = FhirContext.forR4Cached();
	private static Binary ourLastBinary;
	private static byte[] ourLastBinaryBytes;
	private static String ourLastBinaryString;
	private static IdType ourLastId;
	private static Binary ourNextBinary;

	@RegisterExtension
	public static final RestfulServerExtension ourServer = new RestfulServerExtension(ourCtx)
		 .registerProvider(new BinaryProvider())
		 .withPagingProvider(new FifoMemoryPagingProvider(100))
		 .setDefaultResponseEncoding(EncodingEnum.XML)
		 .setDefaultPrettyPrint(false);

	@BeforeEach
	public void before() {
		ourLastBinary = null;
		ourLastBinaryBytes = null;
		ourLastBinaryString = null;
		ourLastId = null;
		ourNextBinary = null;
	}

	@Test
	public void testGetWithNoAccept() throws Exception {

		ourNextBinary = new Binary();
		ourNextBinary.setId("Binary/A/_history/222");
		ourNextBinary.setContent(new byte[]{0, 1, 2, 3, 4});
		ourNextBinary.setSecurityContext(new Reference("Patient/1"));
		ourNextBinary.setContentType("application/foo");

		HttpTestResponse status = ourServer.fhirRequest("/Binary/A").withHeader("Content-Type", "application/foo").get().assertStatus(200);
		assertEquals("application/foo", status.getHeader(Constants.HEADER_CONTENT_TYPE));
		assertEquals("Patient/1", status.getHeader(Constants.HEADER_X_SECURITY_CONTEXT));
		assertEquals("W/\"222\"", status.getHeader(Constants.HEADER_ETAG));
		assertEquals(ourServer.getBaseUrl() + "/Binary/A/_history/222", status.getHeader(Constants.HEADER_CONTENT_LOCATION));
		assertNull(status.getHeader(Constants.HEADER_LOCATION));

		byte[] content = status.getBodyBytes();
		assertThat(content).containsExactly(new byte[]{0, 1, 2, 3, 4});
	}


	@Test
	public void testGetWithAccept() throws Exception {

		ourNextBinary = new Binary();
		ourNextBinary.setId("Binary/A/_history/222");
		ourNextBinary.setContent(new byte[]{0, 1, 2, 3, 4});
		ourNextBinary.setSecurityContext(new Reference("Patient/1"));
		ourNextBinary.setContentType("application/foo");

		HttpTestResponse status = ourServer.fhirRequest("/Binary/A").withHeader("Content-Type", "application/foo").withHeader("Accept", Constants.CT_FHIR_JSON).get().assertStatus(200);
		assertEquals("application/json+fhir;charset=utf-8", status.getHeader(Constants.HEADER_CONTENT_TYPE));
		assertEquals("Patient/1", status.getHeader(Constants.HEADER_X_SECURITY_CONTEXT));
		assertEquals("W/\"222\"", status.getHeader(Constants.HEADER_ETAG));
		assertEquals(ourServer.getBaseUrl() + "/Binary/A/_history/222", status.getHeader(Constants.HEADER_CONTENT_LOCATION));
		assertNull(status.getHeader(Constants.HEADER_LOCATION));

		String content = status.getBody();
		assertEquals("{\"resourceType\":\"Binary\",\"id\":\"A\",\"meta\":{\"versionId\":\"222\"},\"contentType\":\"application/foo\",\"securityContext\":{\"reference\":\"Patient/1\"},\"data\":\"AAECAwQ=\"}", content);
	}

	@Test
	public void testPostBinaryWithSecurityContext() throws Exception {
		ourServer.fhirRequest("/Binary").withHeader(Constants.HEADER_X_SECURITY_CONTEXT, "Encounter/2").post(new byte[]{0, 1, 2, 3, 4}, "application/foo");
		assertNull(ourLastId);
		assertEquals("application/foo", ourLastBinary.getContentType());
		assertEquals("Encounter/2", ourLastBinary.getSecurityContext().getReference());
		assertThat(ourLastBinary.getContent()).containsExactly(new byte[]{0, 1, 2, 3, 4});
		assertThat(ourLastBinaryBytes).containsExactly(new byte[]{0, 1, 2, 3, 4});
	}

	@Test
	public void testPostRawBytesBinaryContentType() throws Exception {
		ourServer.fhirRequest("/Binary").post(new byte[]{0, 1, 2, 3, 4}, "application/foo");
		assertNull(ourLastId);
		assertEquals("application/foo", ourLastBinary.getContentType());
		assertThat(ourLastBinary.getContent()).containsExactly(new byte[]{0, 1, 2, 3, 4});
		assertThat(ourLastBinaryBytes).containsExactly(new byte[]{0, 1, 2, 3, 4});
	}

	/**
	 * Technically the client shouldn't be doing it this way, but we'll be accepting
	 */
	@Test
	public void testPostRawBytesFhirContentType() throws Exception {

		Binary b = new Binary();
		b.setContentType("application/foo");
		b.setContent(new byte[]{0, 1, 2, 3, 4});
		String encoded = ourCtx.newJsonParser().encodeResourceToString(b);

		ourServer.fhirRequest("/Binary").post(encoded.getBytes(StandardCharsets.UTF_8), Constants.CT_FHIR_JSON);
		assertEquals("application/foo", ourLastBinary.getContentType());
		assertThat(ourLastBinary.getContent()).containsExactly(new byte[]{0, 1, 2, 3, 4});
	}

	@Test
	public void testPostRawBytesFhirContentTypeContainingFhir() throws Exception {

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
	public void testPostRawBytesNoContentType() throws Exception {
		ourServer.fhirRequest("/Binary").method("POST", new byte[]{0, 1, 2, 3, 4}, null);
		assertNull(ourLastBinary.getContentType());
		assertThat(ourLastBinary.getContent()).containsExactly(new byte[]{0, 1, 2, 3, 4});
	}

	@Test
	public void testPutBinaryWithSecurityContext() throws Exception {
		ourServer.fhirRequest("/Binary/A").withHeader(Constants.HEADER_X_SECURITY_CONTEXT, "Encounter/2").put(new byte[]{0, 1, 2, 3, 4}, "application/foo");
		assertEquals("Binary/A", ourLastId.getValue());
		assertEquals("Binary/A", ourLastBinary.getId());
		assertEquals("application/foo", ourLastBinary.getContentType());
		assertEquals("Encounter/2", ourLastBinary.getSecurityContext().getReference());
		assertThat(ourLastBinary.getContent()).containsExactly(new byte[]{0, 1, 2, 3, 4});
		assertThat(ourLastBinaryBytes).containsExactly(new byte[]{0, 1, 2, 3, 4});
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

		@Read
		public Binary read(@IdParam IdType theId) {
			return ourNextBinary;
		}

		@Update()
		public MethodOutcome updateBinary(@IdParam IdType theId, @ResourceParam Binary theBinary, @ResourceParam String theBinaryString, @ResourceParam byte[] theBinaryBytes) {
			ourLastId = theId;
			ourLastBinary = theBinary;
			ourLastBinaryString = theBinaryString;
			ourLastBinaryBytes = theBinaryBytes;
			return new MethodOutcome(new IdType("Binary/001/_history/002"));
		}

	}

}
