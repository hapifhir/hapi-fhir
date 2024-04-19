package ca.uhn.fhir.jpa.binstore;

import ca.uhn.fhir.jpa.binary.api.IBinaryStorageSvc;
import ca.uhn.fhir.jpa.binary.api.StoredDetails;
import ca.uhn.fhir.jpa.dao.data.IBinaryStorageEntityDao;
import ca.uhn.fhir.jpa.model.entity.BinaryStorageEntity;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
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
import java.util.Optional;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.matchesPattern;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = DatabaseBinaryContentStorageSvcImplTest.MyConfig.class)
public class DatabaseBinaryContentStorageSvcImplTest extends BaseJpaR4Test {
	private static final byte[] SOME_BYTES = {2, 3, 4, 5, 6, 7, 8, 9, 8, 7, 6, 5, 4, 3, 2, 1};

	@Autowired
	@Qualifier("databaseBinaryContentStorageSvc")
	private IBinaryStorageSvc mySvc;

	@Autowired
	private IBinaryStorageEntityDao myBinaryStorageEntityDao;

	@Test
	public void testStoreAndRetrieve() throws IOException {

		myCaptureQueriesListener.clear();

		/*
		 * Store the binary
		 */
		ByteArrayInputStream inputStream = new ByteArrayInputStream(SOME_BYTES);
		String contentType = "image/png";
		IdType resourceId = new IdType("Binary/123");
		StoredDetails outcome = mySvc.storeBinaryContent(resourceId, null, contentType, inputStream, new ServletRequestDetails());

		myCaptureQueriesListener.logAllQueriesForCurrentThread();

		assertEquals(0, myCaptureQueriesListener.getSelectQueriesForCurrentThread().size());
		assertEquals(1, myCaptureQueriesListener.getInsertQueriesForCurrentThread().size());
		assertEquals(0, myCaptureQueriesListener.getUpdateQueriesForCurrentThread().size());

		myCaptureQueriesListener.clear();

		assertThat(outcome.getBinaryContentId(), matchesPattern("^[a-zA-Z0-9]{100}$"));
		assertEquals(16, outcome.getBytes());

		/*
		 * Read back the details
		 */

		StoredDetails details = mySvc.fetchBinaryContentDetails(resourceId, outcome.getBinaryContentId());
		assertEquals(16L, details.getBytes());
		assertEquals(outcome.getBinaryContentId(), details.getBinaryContentId());
		assertEquals("image/png", details.getContentType());
		assertEquals("dc7197cfab936698bef7818975c185a9b88b71a0a0a2493deea487706ddf20cb", details.getHash());
		assertNotNull(details.getPublished());

		/*
		 * Read back the contents
		 */

		ByteArrayOutputStream capture = new ByteArrayOutputStream();
		mySvc.writeBinaryContent(resourceId, outcome.getBinaryContentId(), capture);

		assertArrayEquals(SOME_BYTES, capture.toByteArray());
		assertArrayEquals(SOME_BYTES, mySvc.fetchBinaryContent(resourceId, outcome.getBinaryContentId()));
	}

	@Test
	public void testStoreAndRetrieveWithPreload() throws IOException {
		myStorageSettings.setPreloadBlobFromInputStream(true);
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
		StoredDetails outcome = mySvc.storeBinaryContent(resourceId, "ABCDEFG", contentType, inputStream, new ServletRequestDetails());
		assertEquals("ABCDEFG", outcome.getBinaryContentId());

		myCaptureQueriesListener.logAllQueriesForCurrentThread();

		assertEquals(0, myCaptureQueriesListener.getSelectQueriesForCurrentThread().size());
		assertEquals(1, myCaptureQueriesListener.getInsertQueriesForCurrentThread().size());
		assertEquals(0, myCaptureQueriesListener.getUpdateQueriesForCurrentThread().size());

		myCaptureQueriesListener.clear();

		assertEquals(16, outcome.getBytes());

		/*
		 * Read back the details
		 */

		StoredDetails details = mySvc.fetchBinaryContentDetails(resourceId, outcome.getBinaryContentId());
		assertEquals(16L, details.getBytes());
		assertEquals(outcome.getBinaryContentId(), details.getBinaryContentId());
		assertEquals("image/png", details.getContentType());
		assertEquals("dc7197cfab936698bef7818975c185a9b88b71a0a0a2493deea487706ddf20cb", details.getHash());
		assertNotNull(details.getPublished());

		/*
		 * Read back the contents
		 */

		ByteArrayOutputStream capture = new ByteArrayOutputStream();
		mySvc.writeBinaryContent(resourceId, outcome.getBinaryContentId(), capture);

		assertArrayEquals(SOME_BYTES, capture.toByteArray());
		assertArrayEquals(SOME_BYTES, mySvc.fetchBinaryContent(resourceId, outcome.getBinaryContentId()));
	}

	@Test
	public void testFetchBinaryContentUnknown() throws IOException {
		try {
			mySvc.fetchBinaryContent(new IdType("Patient/123"), "1111111");
			fail();
		} catch (ResourceNotFoundException e) {
			assertEquals("Unknown BinaryContent ID: 1111111 for resource ID Patient/123", e.getMessage());
		}

		StoredDetails details = mySvc.fetchBinaryContentDetails(new IdType("Patient/123"), "1111111");
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
		StoredDetails outcome = mySvc.storeBinaryContent(resourceId, null, contentType, inputStream, new ServletRequestDetails());
		String blobId = outcome.getBinaryContentId();

		// Expunge
		mySvc.expungeBinaryContent(resourceId, blobId);

		ByteArrayOutputStream capture = new ByteArrayOutputStream();
		assertFalse(mySvc.writeBinaryContent(resourceId, outcome.getBinaryContentId(), capture));
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
		StoredDetails outcome = mySvc.storeBinaryContent(resourceId, null, contentType, inputStream, new ServletRequestDetails());

		// Right ID
		ByteArrayOutputStream capture = new ByteArrayOutputStream();
		assertTrue(mySvc.writeBinaryContent(resourceId, outcome.getBinaryContentId(), capture));
		assertEquals(16, capture.size());

		// Wrong ID
		capture = new ByteArrayOutputStream();
		assertFalse(mySvc.writeBinaryContent(new IdType("Patient/9999"), outcome.getBinaryContentId(), capture));
		assertEquals(0, capture.size());

	}

	@Test
	public void testCopyBinaryContentToOutputStream_Exception() throws SQLException {
		DatabaseBinaryContentStorageSvcImpl svc = new DatabaseBinaryContentStorageSvcImpl();

		BinaryStorageEntity mockInput = new BinaryStorageEntity();
		Blob blob = mock(Blob.class);
		when(blob.getBinaryStream()).thenThrow(new SQLException("FOO"));
		mockInput.setBlob(blob);

		try {
			svc.copyBinaryContentToOutputStream(new ByteArrayOutputStream(), (mockInput));
			fail();
		} catch (IOException e) {
			assertThat(e.getMessage(), containsString("FOO"));
		}
	}

	@Test
	public void testCopyBinaryContentToByteArray_Exception() throws SQLException {
		DatabaseBinaryContentStorageSvcImpl svc = new DatabaseBinaryContentStorageSvcImpl();

		BinaryStorageEntity mockInput = new BinaryStorageEntity();
		Blob blob = mock(Blob.class);
		when(blob.getBinaryStream()).thenThrow(new SQLException("FOO"));
		mockInput.setBlob(blob);

		try {
			svc.copyBinaryContentToByteArray(mockInput);
			fail();
		} catch (IOException e) {
			assertThat(e.getMessage(), containsString("FOO"));
		}
	}

	@Test
	public void testReadBinaryStorageEntity_whenHasBinaryContent_defaultsToBinaryContent() throws IOException {
		// given
		DatabaseBinaryContentStorageSvcImpl svc = new DatabaseBinaryContentStorageSvcImpl();

		BinaryStorageEntity mockInput = mock(BinaryStorageEntity.class);
		when(mockInput.hasStorageContent()).thenReturn(true);
		when(mockInput.getStorageContentBin()).thenReturn(SOME_BYTES);

		// when
		svc.copyBinaryContentToByteArray(mockInput);

		// then
		verify(mockInput, times(0)).hasBlob();
		verify(mockInput, times(0)).getBlob();

	}

	@Test
	public void testReadBinaryStorageEntity_whenHasBlobOnly_willReadBlobContent() throws IOException {
		// given
		DatabaseBinaryContentStorageSvcImpl svc = new DatabaseBinaryContentStorageSvcImpl();

		BinaryStorageEntity mockInput = mock(BinaryStorageEntity.class);
		when(mockInput.hasStorageContent()).thenReturn(false);
		when(mockInput.hasBlob()).thenReturn(true);
		when(mockInput.getBlob()).thenAnswer(t ->{
			Blob blob = mock(Blob.class);
			when(blob.getBinaryStream()).thenReturn(new ByteArrayInputStream(SOME_BYTES));
			return blob;
		} );

		// when
		svc.copyBinaryContentToByteArray(mockInput);

		// then
		verify(mockInput, times(1)).hasBlob();
		verify(mockInput, times(1)).getBlob();
	}

	@Test
	public void testStoreBinaryContent_writesBlobAndByteArray() throws IOException {
		// given
		ByteArrayInputStream inputStream = new ByteArrayInputStream(SOME_BYTES);
		String contentType = "image/png";
		IdType resourceId = new IdType("Binary/123");

		// when
		StoredDetails outcome = mySvc.storeBinaryContent(resourceId, null, contentType, inputStream, new ServletRequestDetails());

		runInTransaction(() -> {
				Optional<BinaryStorageEntity> binaryStorageEntityOptional = myBinaryStorageEntityDao.findByIdAndResourceId(outcome.getBinaryContentId(), resourceId.toUnqualifiedVersionless().getValue());
				BinaryStorageEntity binaryStorageEntity = binaryStorageEntityOptional.get();

				// then
				assertThat(binaryStorageEntity.hasStorageContent(), is(true));
				assertThat(binaryStorageEntity.hasBlob(), is(true));
		});

	}

	@Configuration
	public static class MyConfig {

		@Primary
		@Bean
		public IBinaryStorageSvc databaseBinaryContentStorageSvc() {
			return new DatabaseBinaryContentStorageSvcImpl();
		}

	}
}
