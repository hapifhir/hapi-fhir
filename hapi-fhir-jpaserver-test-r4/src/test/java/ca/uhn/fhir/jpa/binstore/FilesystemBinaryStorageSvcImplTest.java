package ca.uhn.fhir.jpa.binstore;

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
import static org.assertj.core.api.Assertions.fail;

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

<<<<<<< HEAD
		StoredDetails details = mySvc.fetchBlobDetails(id, outcome.getBlobId());
		assertThat(details.getBytes()).isEqualTo(16L);
		assertThat(details.getBlobId()).isEqualTo(outcome.getBlobId());
		assertThat(details.getContentType()).isEqualTo("image/png");
		assertThat(details.getHash()).isEqualTo("dc7197cfab936698bef7818975c185a9b88b71a0a0a2493deea487706ddf20cb");
		assertThat(details.getPublished()).isNotNull();
=======
		StoredDetails details = mySvc.fetchBinaryContentDetails(id, outcome.getBinaryContentId());
		assertEquals(16L, details.getBytes());
		assertEquals(outcome.getBinaryContentId(), details.getBinaryContentId());
		assertEquals("image/png", details.getContentType());
		assertEquals("dc7197cfab936698bef7818975c185a9b88b71a0a0a2493deea487706ddf20cb", details.getHash());
		assertNotNull(details.getPublished());
>>>>>>> master

		ByteArrayOutputStream capture = new ByteArrayOutputStream();
		mySvc.writeBinaryContent(id, outcome.getBinaryContentId(), capture);

<<<<<<< HEAD
		assertThat(capture.toByteArray()).containsExactly(SOME_BYTES);
		assertThat(mySvc.fetchBlob(id, outcome.getBlobId())).containsExactly(SOME_BYTES);
=======
		assertArrayEquals(SOME_BYTES, capture.toByteArray());
		assertArrayEquals(SOME_BYTES, mySvc.fetchBinaryContent(id, outcome.getBinaryContentId()));
>>>>>>> master
	}

	@Test
	public void testStoreAndRetrieveManualId() throws IOException {
		IIdType id = new IdType("Patient/123");
		String contentType = "image/png";
		String blobId = "ABCDEFGHIJKLMNOPQRSTUV";
<<<<<<< HEAD
		StoredDetails outcome = mySvc.storeBlob(id, blobId, contentType, new ByteArrayInputStream(SOME_BYTES), new ServletRequestDetails());
		assertThat(outcome.getBlobId()).isEqualTo(blobId);

		ourLog.info("Got id: {}", outcome);

		StoredDetails details = mySvc.fetchBlobDetails(id, outcome.getBlobId());
		assertThat(details.getBytes()).isEqualTo(16L);
		assertThat(details.getBlobId()).isEqualTo(outcome.getBlobId());
		assertThat(details.getContentType()).isEqualTo("image/png");
		assertThat(details.getHash()).isEqualTo("dc7197cfab936698bef7818975c185a9b88b71a0a0a2493deea487706ddf20cb");
		assertThat(details.getPublished()).isNotNull();
=======
		StoredDetails outcome = mySvc.storeBinaryContent(id, blobId, contentType, new ByteArrayInputStream(SOME_BYTES), new ServletRequestDetails());
		assertEquals(blobId, outcome.getBinaryContentId());

		ourLog.info("Got id: {}", outcome);

		StoredDetails details = mySvc.fetchBinaryContentDetails(id, outcome.getBinaryContentId());
		assertEquals(16L, details.getBytes());
		assertEquals(outcome.getBinaryContentId(), details.getBinaryContentId());
		assertEquals("image/png", details.getContentType());
		assertEquals("dc7197cfab936698bef7818975c185a9b88b71a0a0a2493deea487706ddf20cb", details.getHash());
		assertNotNull(details.getPublished());
>>>>>>> master

		ByteArrayOutputStream capture = new ByteArrayOutputStream();
		mySvc.writeBinaryContent(id, outcome.getBinaryContentId(), capture);

<<<<<<< HEAD
		assertThat(capture.toByteArray()).containsExactly(SOME_BYTES);
		assertThat(mySvc.fetchBlob(id, outcome.getBlobId())).containsExactly(SOME_BYTES);
=======
		assertArrayEquals(SOME_BYTES, capture.toByteArray());
		assertArrayEquals(SOME_BYTES, mySvc.fetchBinaryContent(id, outcome.getBinaryContentId()));
>>>>>>> master
	}


	@Test
	public void testFetchBinaryContentUnknown() throws IOException {
		try {
<<<<<<< HEAD
			mySvc.fetchBlob(new IdType("Patient/123"), "1111111");
			fail("");
=======
			mySvc.fetchBinaryContent(new IdType("Patient/123"), "1111111");
			fail();
>>>>>>> master
		} catch (ResourceNotFoundException e) {
			assertThat(e.getMessage()).isEqualTo(Msg.code(1327) + "Unknown blob ID: 1111111 for resource ID Patient/123");
		}
	}


	@Test
	public void testExpunge() throws IOException {
		IIdType id = new IdType("Patient/123");
		String contentType = "image/png";
		StoredDetails outcome = mySvc.storeBinaryContent(id, null, contentType, new ByteArrayInputStream(SOME_BYTES), new ServletRequestDetails());

		ourLog.info("Got id: {}", outcome);

<<<<<<< HEAD
		StoredDetails details = mySvc.fetchBlobDetails(id, outcome.getBlobId());
		assertThat(details.getBytes()).isEqualTo(16L);
		assertThat(details.getBlobId()).isEqualTo(outcome.getBlobId());
		assertThat(details.getContentType()).isEqualTo("image/png");
		assertThat(details.getHash()).isEqualTo("dc7197cfab936698bef7818975c185a9b88b71a0a0a2493deea487706ddf20cb");
		assertThat(details.getPublished()).isNotNull();
=======
		StoredDetails details = mySvc.fetchBinaryContentDetails(id, outcome.getBinaryContentId());
		assertEquals(16L, details.getBytes());
		assertEquals(outcome.getBinaryContentId(), details.getBinaryContentId());
		assertEquals("image/png", details.getContentType());
		assertEquals("dc7197cfab936698bef7818975c185a9b88b71a0a0a2493deea487706ddf20cb", details.getHash());
		assertNotNull(details.getPublished());
>>>>>>> master

		mySvc.expungeBinaryContent(id, outcome.getBinaryContentId());

		ByteArrayOutputStream capture = new ByteArrayOutputStream();
<<<<<<< HEAD
		mySvc.writeBlob(id, outcome.getBlobId(), capture);
		assertThat(capture.size()).isEqualTo(0);
=======
		mySvc.writeBinaryContent(id, outcome.getBinaryContentId(), capture);
		assertEquals(0, capture.size());
>>>>>>> master
	}

	@Test
	public void testRejectOversized() throws IOException {
		mySvc.setMinimumBinarySize(0);
		mySvc.setMaximumBinarySize(5);

		IIdType id = new IdType("Patient/123");
		String contentType = "image/png";
		try {
<<<<<<< HEAD
			mySvc.storeBlob(id, null, contentType, new ByteArrayInputStream(SOME_BYTES), new ServletRequestDetails());
			fail("");
=======
			mySvc.storeBinaryContent(id, null, contentType, new ByteArrayInputStream(SOME_BYTES), new ServletRequestDetails());
			fail();
>>>>>>> master
		} catch (PayloadTooLargeException e) {
			assertThat(e.getMessage()).isEqualTo(Msg.code(1343) + "Binary size exceeds maximum: 5");
		}


	}


}
