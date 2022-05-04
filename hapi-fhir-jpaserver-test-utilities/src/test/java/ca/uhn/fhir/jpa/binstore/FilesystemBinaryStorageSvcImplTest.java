package ca.uhn.fhir.jpa.binstore;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.binary.api.StoredDetails;
import ca.uhn.fhir.rest.server.exceptions.PayloadTooLargeException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
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

import static org.junit.jupiter.api.Assertions.*;

public class FilesystemBinaryStorageSvcImplTest {

	private static final byte[] SOME_BYTES = {2, 3, 4, 5, 6, 7, 8, 9, 8, 7, 6, 5, 4, 3, 2, 1};
	private static final Logger ourLog = LoggerFactory.getLogger(FilesystemBinaryStorageSvcImplTest.class);
	private File myPath;
	private FilesystemBinaryStorageSvcImpl mySvc;

	@BeforeEach
	public void before() {
		myPath = new File("./target/fstmp");
		mySvc = new FilesystemBinaryStorageSvcImpl(myPath.getAbsolutePath());
	}

	@AfterEach
	public void after() throws IOException {
		FileUtils.deleteDirectory(myPath);
	}

	@Test
	public void testStoreAndRetrieve() throws IOException {
		IIdType id = new IdType("Patient/123");
		String contentType = "image/png";
		StoredDetails outcome = mySvc.storeBlob(id, null, contentType, new ByteArrayInputStream(SOME_BYTES));

		ourLog.info("Got id: {}", outcome);

		StoredDetails details = mySvc.fetchBlobDetails(id, outcome.getBlobId());
		assertEquals(16L, details.getBytes());
		assertEquals(outcome.getBlobId(), details.getBlobId());
		assertEquals("image/png", details.getContentType());
		assertEquals("dc7197cfab936698bef7818975c185a9b88b71a0a0a2493deea487706ddf20cb", details.getHash());
		assertNotNull(details.getPublished());

		ByteArrayOutputStream capture = new ByteArrayOutputStream();
		mySvc.writeBlob(id, outcome.getBlobId(), capture);

		assertArrayEquals(SOME_BYTES, capture.toByteArray());
		assertArrayEquals(SOME_BYTES, mySvc.fetchBlob(id, outcome.getBlobId()));
	}

	@Test
	public void testStoreAndRetrieveManualId() throws IOException {
		IIdType id = new IdType("Patient/123");
		String contentType = "image/png";
		String blobId = "ABCDEFGHIJKLMNOPQRSTUV";
		StoredDetails outcome = mySvc.storeBlob(id, blobId, contentType, new ByteArrayInputStream(SOME_BYTES));
		assertEquals(blobId, outcome.getBlobId());

		ourLog.info("Got id: {}", outcome);

		StoredDetails details = mySvc.fetchBlobDetails(id, outcome.getBlobId());
		assertEquals(16L, details.getBytes());
		assertEquals(outcome.getBlobId(), details.getBlobId());
		assertEquals("image/png", details.getContentType());
		assertEquals("dc7197cfab936698bef7818975c185a9b88b71a0a0a2493deea487706ddf20cb", details.getHash());
		assertNotNull(details.getPublished());

		ByteArrayOutputStream capture = new ByteArrayOutputStream();
		mySvc.writeBlob(id, outcome.getBlobId(), capture);

		assertArrayEquals(SOME_BYTES, capture.toByteArray());
		assertArrayEquals(SOME_BYTES, mySvc.fetchBlob(id, outcome.getBlobId()));
	}


	@Test
	public void testFetchBlobUnknown() throws IOException {
		try {
			mySvc.fetchBlob(new IdType("Patient/123"), "1111111");
			fail();
		} catch (ResourceNotFoundException e) {
			assertEquals(Msg.code(1327) + "Unknown blob ID: 1111111 for resource ID Patient/123", e.getMessage());
		}
	}


	@Test
	public void testExpunge() throws IOException {
		IIdType id = new IdType("Patient/123");
		String contentType = "image/png";
		StoredDetails outcome = mySvc.storeBlob(id, null, contentType, new ByteArrayInputStream(SOME_BYTES));

		ourLog.info("Got id: {}", outcome);

		StoredDetails details = mySvc.fetchBlobDetails(id, outcome.getBlobId());
		assertEquals(16L, details.getBytes());
		assertEquals(outcome.getBlobId(), details.getBlobId());
		assertEquals("image/png", details.getContentType());
		assertEquals("dc7197cfab936698bef7818975c185a9b88b71a0a0a2493deea487706ddf20cb", details.getHash());
		assertNotNull(details.getPublished());

		mySvc.expungeBlob(id, outcome.getBlobId());

		ByteArrayOutputStream capture = new ByteArrayOutputStream();
		mySvc.writeBlob(id, outcome.getBlobId(), capture);
		assertEquals(0, capture.size());
	}

	@Test
	public void testRejectOversized() throws IOException {
		mySvc.setMinimumBinarySize(0);
		mySvc.setMaximumBinarySize(5);

		IIdType id = new IdType("Patient/123");
		String contentType = "image/png";
		try {
			mySvc.storeBlob(id, null, contentType, new ByteArrayInputStream(SOME_BYTES));
			fail();
		} catch (PayloadTooLargeException e) {
			assertEquals(Msg.code(1343) + "Binary size exceeds maximum: 5", e.getMessage());
		}


	}


}
