package ca.uhn.fhir.jpa.binstore;

import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.PayloadTooLargeException;
import org.apache.commons.io.FileUtils;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.IdType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import static org.junit.Assert.*;

public class FilesystemBinaryStorageSvcImplTest {

	public static final byte[] SOME_BYTES = {2, 3, 4, 5, 6, 7, 8, 9, 8, 7, 6, 5, 4, 3, 2, 1};
	private static final Logger ourLog = LoggerFactory.getLogger(FilesystemBinaryStorageSvcImplTest.class);
	private File myPath;
	private FilesystemBinaryStorageSvcImpl mySvc;

	@Before
	public void before() {
		myPath = new File("./target/fstmp");
		mySvc = new FilesystemBinaryStorageSvcImpl(myPath.getAbsolutePath());
	}

	@After
	public void after() throws IOException {
		FileUtils.deleteDirectory(myPath);
	}

	@Test
	public void testStoreAndRetrieve() throws IOException {
		IIdType id = new IdType("Patient/123");
		String contentType = "image/png";
		IBinaryStorageSvc.StoredDetails outcome = mySvc.storeBlob(id, contentType, new ByteArrayInputStream(SOME_BYTES));

		ourLog.info("Got id: {}", outcome);

		IBinaryStorageSvc.StoredDetails details = mySvc.fetchBlobDetails(id, outcome.getBlobId());
		assertEquals(16L, details.getBytes());
		assertEquals(outcome.getBlobId(), details.getBlobId());
		assertEquals("image/png", details.getContentType());
		assertEquals("dc7197cfab936698bef7818975c185a9b88b71a0a0a2493deea487706ddf20cb", details.getHash());
		assertNotNull(details.getPublished());

		ByteArrayOutputStream capture = new ByteArrayOutputStream();
		mySvc.writeBlob(id, outcome.getBlobId(), capture);

		assertArrayEquals(SOME_BYTES, capture.toByteArray());
	}


	@Test
	public void testExpunge() throws IOException {
		IIdType id = new IdType("Patient/123");
		String contentType = "image/png";
		IBinaryStorageSvc.StoredDetails outcome = mySvc.storeBlob(id, contentType, new ByteArrayInputStream(SOME_BYTES));

		ourLog.info("Got id: {}", outcome);

		IBinaryStorageSvc.StoredDetails details = mySvc.fetchBlobDetails(id, outcome.getBlobId());
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
			mySvc.storeBlob(id, contentType, new ByteArrayInputStream(SOME_BYTES));
			fail();
		} catch (PayloadTooLargeException e) {
			assertEquals("Binary size exceeds maximum: 5", e.getMessage());
		}


	}


}
