package ca.uhn.fhir.jpa.binstore;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.executor.InterceptorService;
import ca.uhn.fhir.jpa.binary.api.StoredDetails;
import ca.uhn.fhir.rest.server.exceptions.PayloadTooLargeException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import org.apache.commons.io.FileUtils;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.IdType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

public class FilesystemBinaryStorageSvcImplTest {

	private static final byte[] SOME_BYTES = {2, 3, 4, 5, 6, 7, 8, 9, 8, 7, 6, 5, 4, 3, 2, 1};
	private static final Logger ourLog = LoggerFactory.getLogger(FilesystemBinaryStorageSvcImplTest.class);
	private File myPath;
	private FilesystemBinaryStorageSvcImpl mySvc;

	@BeforeEach
	public void before() {
		myPath = new File("./target/fstmp");
		mySvc = new FilesystemBinaryStorageSvcImpl(myPath.getAbsolutePath());
		mySvc.setFhirContextForTests(FhirContext.forR4Cached());
		mySvc.setInterceptorBroadcasterForTests(new InterceptorService());
	}

	@AfterEach
	public void after() throws IOException {
		FileUtils.deleteDirectory(myPath);
	}

	@Test
	public void testStoreAndRetrieve() throws IOException {
		IIdType id = new IdType("Patient/123");
		String contentType = "image/png";
		StoredDetails outcome = mySvc.storeBinaryContent(id, null, contentType, new ByteArrayInputStream(SOME_BYTES), new ServletRequestDetails());

		ourLog.info("Got id: {}", outcome);

		StoredDetails details = mySvc.fetchBinaryContentDetails(id, outcome.getBinaryContentId());
		assertEquals(16L, details.getBytes());
		assertEquals(outcome.getBinaryContentId(), details.getBinaryContentId());
		assertEquals("image/png", details.getContentType());
		assertEquals("dc7197cfab936698bef7818975c185a9b88b71a0a0a2493deea487706ddf20cb", details.getHash());
		assertNotNull(details.getPublished());

		ByteArrayOutputStream capture = new ByteArrayOutputStream();
		mySvc.writeBinaryContent(id, outcome.getBinaryContentId(), capture);

		assertThat(capture.toByteArray()).containsExactly(SOME_BYTES);
		assertThat(mySvc.fetchBinaryContent(id, outcome.getBinaryContentId())).containsExactly(SOME_BYTES);
	}

	@Test
	public void testStoreAndRetrieveManualId() throws IOException {
		IIdType id = new IdType("Patient/123");
		String contentType = "image/png";
		String blobId = "ABCDEFGHIJKLMNOPQRSTUV";
		StoredDetails outcome = mySvc.storeBinaryContent(id, blobId, contentType, new ByteArrayInputStream(SOME_BYTES), new ServletRequestDetails());
		assertEquals(blobId, outcome.getBinaryContentId());

		ourLog.info("Got id: {}", outcome);

		StoredDetails details = mySvc.fetchBinaryContentDetails(id, outcome.getBinaryContentId());
		assertEquals(16L, details.getBytes());
		assertEquals(outcome.getBinaryContentId(), details.getBinaryContentId());
		assertEquals("image/png", details.getContentType());
		assertEquals("dc7197cfab936698bef7818975c185a9b88b71a0a0a2493deea487706ddf20cb", details.getHash());
		assertNotNull(details.getPublished());

		ByteArrayOutputStream capture = new ByteArrayOutputStream();
		mySvc.writeBinaryContent(id, outcome.getBinaryContentId(), capture);

		assertThat(capture.toByteArray()).containsExactly(SOME_BYTES);
		assertThat(mySvc.fetchBinaryContent(id, outcome.getBinaryContentId())).containsExactly(SOME_BYTES);
	}


	@Test
	public void testFetchBinaryContentUnknown() throws IOException {
		try {
			mySvc.fetchBinaryContent(new IdType("Patient/123"), "1111111");
			fail();
		} catch (ResourceNotFoundException e) {
			assertEquals(Msg.code(1327) + "Unknown blob ID: 1111111 for resource ID Patient/123", e.getMessage());
		}
	}


	@Test
	public void testExpunge() throws IOException {
		IIdType id = new IdType("Patient/123");
		String contentType = "image/png";
		StoredDetails outcome = mySvc.storeBinaryContent(id, null, contentType, new ByteArrayInputStream(SOME_BYTES), new ServletRequestDetails());

		ourLog.info("Got id: {}", outcome);

		StoredDetails details = mySvc.fetchBinaryContentDetails(id, outcome.getBinaryContentId());
		assertEquals(16L, details.getBytes());
		assertEquals(outcome.getBinaryContentId(), details.getBinaryContentId());
		assertEquals("image/png", details.getContentType());
		assertEquals("dc7197cfab936698bef7818975c185a9b88b71a0a0a2493deea487706ddf20cb", details.getHash());
		assertNotNull(details.getPublished());

		mySvc.expungeBinaryContent(id, outcome.getBinaryContentId());

		ByteArrayOutputStream capture = new ByteArrayOutputStream();
		mySvc.writeBinaryContent(id, outcome.getBinaryContentId(), capture);
		assertEquals(0, capture.size());
	}

	@Test
	public void testRejectOversized() throws IOException {
		mySvc.setMinimumBinarySize(0);
		mySvc.setMaximumBinarySize(5);

		IIdType id = new IdType("Patient/123");
		String contentType = "image/png";
		try {
			mySvc.storeBinaryContent(id, null, contentType, new ByteArrayInputStream(SOME_BYTES), new ServletRequestDetails());
			fail();
		} catch (PayloadTooLargeException e) {
			assertEquals(Msg.code(1343) + "Binary size exceeds maximum: 5", e.getMessage());
		}


	}


}
