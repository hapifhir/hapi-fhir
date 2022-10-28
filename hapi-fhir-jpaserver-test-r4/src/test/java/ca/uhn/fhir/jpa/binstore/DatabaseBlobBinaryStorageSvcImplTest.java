package ca.uhn.fhir.jpa.binstore;

import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.binary.api.IBinaryStorageSvc;
import ca.uhn.fhir.jpa.binary.api.StoredDetails;
import ca.uhn.fhir.jpa.model.entity.BinaryStorageEntity;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import org.hl7.fhir.r4.model.IdType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.ContextConfiguration;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.matchesPattern;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = DatabaseBlobBinaryStorageSvcImplTest.MyConfig.class)
public class DatabaseBlobBinaryStorageSvcImplTest extends BaseJpaR4Test {
	private static final byte[] SOME_BYTES = {2, 3, 4, 5, 6, 7, 8, 9, 8, 7, 6, 5, 4, 3, 2, 1};

	@Autowired
	@Qualifier("databaseBlobBinaryStorageSvc")
	private IBinaryStorageSvc mySvc;

	@Autowired
	private DaoConfig myDaoConfig;

	@Test
	public void testStoreAndRetrieve() throws IOException {

		myCaptureQueriesListener.clear();

		/*
		 * Store the binary
		 */
		ByteArrayInputStream inputStream = new ByteArrayInputStream(SOME_BYTES);
		String contentType = "image/png";
		IdType resourceId = new IdType("Binary/123");
		StoredDetails outcome = mySvc.storeBlob(resourceId, null, contentType, inputStream);

		myCaptureQueriesListener.logAllQueriesForCurrentThread();

		assertEquals(0, myCaptureQueriesListener.getSelectQueriesForCurrentThread().size());
		assertEquals(1, myCaptureQueriesListener.getInsertQueriesForCurrentThread().size());
		assertEquals(0, myCaptureQueriesListener.getUpdateQueriesForCurrentThread().size());

		myCaptureQueriesListener.clear();

		assertThat(outcome.getBlobId(), matchesPattern("^[a-zA-Z0-9]{100}$"));
		assertEquals(16, outcome.getBytes());

		/*
		 * Read back the details
		 */

		StoredDetails details = mySvc.fetchBlobDetails(resourceId, outcome.getBlobId());
		assertEquals(16L, details.getBytes());
		assertEquals(outcome.getBlobId(), details.getBlobId());
		assertEquals("image/png", details.getContentType());
		assertEquals("dc7197cfab936698bef7818975c185a9b88b71a0a0a2493deea487706ddf20cb", details.getHash());
		assertNotNull(details.getPublished());

		/*
		 * Read back the contents
		 */

		ByteArrayOutputStream capture = new ByteArrayOutputStream();
		mySvc.writeBlob(resourceId, outcome.getBlobId(), capture);

		assertArrayEquals(SOME_BYTES, capture.toByteArray());
		assertArrayEquals(SOME_BYTES, mySvc.fetchBlob(resourceId, outcome.getBlobId()));
	}

	@Test
	public void testStoreAndRetrieveWithPreload() throws IOException {
		myDaoConfig.setPreloadBlobFromInputStream(true);
		testStoreAndRetrieve();
	}


	@Test
	public void testStoreAndRetrieveWithManualId() throws IOException {

		myCaptureQueriesListener.clear();

		/*
		 * Store the binary
		 */
		ByteArrayInputStream inputStream = new ByteArrayInputStream(SOME_BYTES);
		String contentType = "image/png";
		IdType resourceId = new IdType("Binary/123");
		StoredDetails outcome = mySvc.storeBlob(resourceId, "ABCDEFG", contentType, inputStream);
		assertEquals("ABCDEFG", outcome.getBlobId());

		myCaptureQueriesListener.logAllQueriesForCurrentThread();

		assertEquals(0, myCaptureQueriesListener.getSelectQueriesForCurrentThread().size());
		assertEquals(1, myCaptureQueriesListener.getInsertQueriesForCurrentThread().size());
		assertEquals(0, myCaptureQueriesListener.getUpdateQueriesForCurrentThread().size());

		myCaptureQueriesListener.clear();

		assertEquals(16, outcome.getBytes());

		/*
		 * Read back the details
		 */

		StoredDetails details = mySvc.fetchBlobDetails(resourceId, outcome.getBlobId());
		assertEquals(16L, details.getBytes());
		assertEquals(outcome.getBlobId(), details.getBlobId());
		assertEquals("image/png", details.getContentType());
		assertEquals("dc7197cfab936698bef7818975c185a9b88b71a0a0a2493deea487706ddf20cb", details.getHash());
		assertNotNull(details.getPublished());

		/*
		 * Read back the contents
		 */

		ByteArrayOutputStream capture = new ByteArrayOutputStream();
		mySvc.writeBlob(resourceId, outcome.getBlobId(), capture);

		assertArrayEquals(SOME_BYTES, capture.toByteArray());
		assertArrayEquals(SOME_BYTES, mySvc.fetchBlob(resourceId, outcome.getBlobId()));
	}

	@Test
	public void testFetchBlobUnknown() throws IOException {
		try {
			mySvc.fetchBlob(new IdType("Patient/123"), "1111111");
			fail();
		} catch (ResourceNotFoundException e) {
			assertEquals("Unknown blob ID: 1111111 for resource ID Patient/123", e.getMessage());
		}

		StoredDetails details = mySvc.fetchBlobDetails(new IdType("Patient/123"), "1111111");
		assertNull(details);
	}


	@Test
	public void testExpunge() throws IOException {

		/*
		 * Store the binary
		 */
		ByteArrayInputStream inputStream = new ByteArrayInputStream(SOME_BYTES);
		String contentType = "image/png";
		IdType resourceId = new IdType("Binary/123");
		StoredDetails outcome = mySvc.storeBlob(resourceId, null, contentType, inputStream);
		String blobId = outcome.getBlobId();

		// Expunge
		mySvc.expungeBlob(resourceId, blobId);

		ByteArrayOutputStream capture = new ByteArrayOutputStream();
		assertFalse(mySvc.writeBlob(resourceId, outcome.getBlobId(), capture));
		assertEquals(0, capture.size());

	}


	@Test
	public void testWrongResourceId() throws IOException {

		/*
		 * Store the binary
		 */
		ByteArrayInputStream inputStream = new ByteArrayInputStream(SOME_BYTES);
		String contentType = "image/png";
		IdType resourceId = new IdType("Binary/123");
		StoredDetails outcome = mySvc.storeBlob(resourceId, null, contentType, inputStream);

		// Right ID
		ByteArrayOutputStream capture = new ByteArrayOutputStream();
		assertTrue(mySvc.writeBlob(resourceId, outcome.getBlobId(), capture));
		assertEquals(16, capture.size());

		// Wrong ID
		capture = new ByteArrayOutputStream();
		assertFalse(mySvc.writeBlob(new IdType("Patient/9999"), outcome.getBlobId(), capture));
		assertEquals(0, capture.size());

	}

	@Test
	public void testCopyBlobToOutputStream_Exception() throws SQLException {
		DatabaseBlobBinaryStorageSvcImpl svc = new DatabaseBlobBinaryStorageSvcImpl();

		BinaryStorageEntity mockInput = new BinaryStorageEntity();
		Blob blob = mock(Blob.class);
		when(blob.getBinaryStream()).thenThrow(new SQLException("FOO"));
		mockInput.setBlob(blob);

		try {
			svc.copyBlobToOutputStream(new ByteArrayOutputStream(), (mockInput));
			fail();
		} catch (IOException e) {
			assertThat(e.getMessage(), containsString("FOO"));
		}
	}

	@Test
	public void testCopyBlobToByteArray_Exception() throws SQLException {
		DatabaseBlobBinaryStorageSvcImpl svc = new DatabaseBlobBinaryStorageSvcImpl();

		BinaryStorageEntity mockInput = new BinaryStorageEntity();
		Blob blob = mock(Blob.class);
		when(blob.getBinaryStream()).thenThrow(new SQLException("FOO"));
		mockInput.setBlob(blob);

		try {
			svc.copyBlobToByteArray(mockInput);
			fail();
		} catch (IOException e) {
			assertThat(e.getMessage(), containsString("FOO"));
		}
	}

	@Configuration
	public static class MyConfig {

		@Primary
		@Bean
		public IBinaryStorageSvc databaseBlobBinaryStorageSvc() {
			return new DatabaseBlobBinaryStorageSvcImpl();
		}

	}
}
