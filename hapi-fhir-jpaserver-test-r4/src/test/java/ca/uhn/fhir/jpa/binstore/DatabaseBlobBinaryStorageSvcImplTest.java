package ca.uhn.fhir.jpa.binstore;

import ca.uhn.fhir.jpa.binary.api.IBinaryStorageSvc;
import ca.uhn.fhir.jpa.binary.api.StoredDetails;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.assertj.core.api.Assertions.fail;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = DatabaseBlobBinaryStorageSvcImplTest.MyConfig.class)
public class DatabaseBlobBinaryStorageSvcImplTest extends BaseJpaR4Test {
	private static final byte[] SOME_BYTES = {2, 3, 4, 5, 6, 7, 8, 9, 8, 7, 6, 5, 4, 3, 2, 1};

	@Autowired
	@Qualifier("databaseBlobBinaryStorageSvc")
	private IBinaryStorageSvc mySvc;

	@Test
	public void testStoreAndRetrieve() throws IOException {

		myCaptureQueriesListener.clear();

		/*
		 * Store the binary
		 */
		ByteArrayInputStream inputStream = new ByteArrayInputStream(SOME_BYTES);
		String contentType = "image/png";
		IdType resourceId = new IdType("Binary/123");
		StoredDetails outcome = mySvc.storeBlob(resourceId, null, contentType, inputStream, new ServletRequestDetails());

		myCaptureQueriesListener.logAllQueriesForCurrentThread();

		assertThat(myCaptureQueriesListener.getSelectQueriesForCurrentThread().size()).isEqualTo(0);
		assertThat(myCaptureQueriesListener.getInsertQueriesForCurrentThread().size()).isEqualTo(1);
		assertThat(myCaptureQueriesListener.getUpdateQueriesForCurrentThread().size()).isEqualTo(0);

		myCaptureQueriesListener.clear();

		assertThat(outcome.getBlobId()).matches("^[a-zA-Z0-9]{100}$");
		assertThat(outcome.getBytes()).isEqualTo(16);

		/*
		 * Read back the details
		 */

		StoredDetails details = mySvc.fetchBlobDetails(resourceId, outcome.getBlobId());
		assertThat(details.getBytes()).isEqualTo(16L);
		assertThat(details.getBlobId()).isEqualTo(outcome.getBlobId());
		assertThat(details.getContentType()).isEqualTo("image/png");
		assertThat(details.getHash()).isEqualTo("dc7197cfab936698bef7818975c185a9b88b71a0a0a2493deea487706ddf20cb");
		assertThat(details.getPublished()).isNotNull();

		/*
		 * Read back the contents
		 */

		ByteArrayOutputStream capture = new ByteArrayOutputStream();
		mySvc.writeBlob(resourceId, outcome.getBlobId(), capture);

		assertThat(capture.toByteArray()).containsExactly(SOME_BYTES);
		assertThat(mySvc.fetchBlob(resourceId, outcome.getBlobId())).containsExactly(SOME_BYTES);
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
		StoredDetails outcome = mySvc.storeBlob(resourceId, "ABCDEFG", contentType, inputStream, new ServletRequestDetails());
		assertThat(outcome.getBlobId()).isEqualTo("ABCDEFG");

		myCaptureQueriesListener.logAllQueriesForCurrentThread();

		assertThat(myCaptureQueriesListener.getSelectQueriesForCurrentThread().size()).isEqualTo(0);
		assertThat(myCaptureQueriesListener.getInsertQueriesForCurrentThread().size()).isEqualTo(1);
		assertThat(myCaptureQueriesListener.getUpdateQueriesForCurrentThread().size()).isEqualTo(0);

		myCaptureQueriesListener.clear();

		assertThat(outcome.getBytes()).isEqualTo(16);

		/*
		 * Read back the details
		 */

		StoredDetails details = mySvc.fetchBlobDetails(resourceId, outcome.getBlobId());
		assertThat(details.getBytes()).isEqualTo(16L);
		assertThat(details.getBlobId()).isEqualTo(outcome.getBlobId());
		assertThat(details.getContentType()).isEqualTo("image/png");
		assertThat(details.getHash()).isEqualTo("dc7197cfab936698bef7818975c185a9b88b71a0a0a2493deea487706ddf20cb");
		assertThat(details.getPublished()).isNotNull();

		/*
		 * Read back the contents
		 */

		ByteArrayOutputStream capture = new ByteArrayOutputStream();
		mySvc.writeBlob(resourceId, outcome.getBlobId(), capture);

		assertThat(capture.toByteArray()).containsExactly(SOME_BYTES);
		assertThat(mySvc.fetchBlob(resourceId, outcome.getBlobId())).containsExactly(SOME_BYTES);
	}

	@Test
	public void testFetchBlobUnknown() throws IOException {
		try {
			mySvc.fetchBlob(new IdType("Patient/123"), "1111111");
			fail("");
		} catch (ResourceNotFoundException e) {
			assertThat(e.getMessage()).isEqualTo("Unknown blob ID: 1111111 for resource ID Patient/123");
		}

		StoredDetails details = mySvc.fetchBlobDetails(new IdType("Patient/123"), "1111111");
		assertThat(details).isNull();
	}


	@Test
	public void testExpunge() throws IOException {

		/*
		 * Store the binary
		 */
		ByteArrayInputStream inputStream = new ByteArrayInputStream(SOME_BYTES);
		String contentType = "image/png";
		IdType resourceId = new IdType("Binary/123");
		StoredDetails outcome = mySvc.storeBlob(resourceId, null, contentType, inputStream, new ServletRequestDetails());
		String blobId = outcome.getBlobId();

		// Expunge
		mySvc.expungeBlob(resourceId, blobId);

		ByteArrayOutputStream capture = new ByteArrayOutputStream();
		assertThat(mySvc.writeBlob(resourceId, outcome.getBlobId(), capture)).isFalse();
		assertThat(capture.size()).isEqualTo(0);

	}


	@Test
	public void testWrongResourceId() throws IOException {

		/*
		 * Store the binary
		 */
		ByteArrayInputStream inputStream = new ByteArrayInputStream(SOME_BYTES);
		String contentType = "image/png";
		IdType resourceId = new IdType("Binary/123");
		StoredDetails outcome = mySvc.storeBlob(resourceId, null, contentType, inputStream, new ServletRequestDetails());

		// Right ID
		ByteArrayOutputStream capture = new ByteArrayOutputStream();
		assertThat(mySvc.writeBlob(resourceId, outcome.getBlobId(), capture)).isTrue();
		assertThat(capture.size()).isEqualTo(16);

		// Wrong ID
		capture = new ByteArrayOutputStream();
		assertThat(mySvc.writeBlob(new IdType("Patient/9999"), outcome.getBlobId(), capture)).isFalse();
		assertThat(capture.size()).isEqualTo(0);

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
			fail("");
		} catch (IOException e) {
			assertThat(e.getMessage()).contains("FOO");
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
			fail("");
		} catch (IOException e) {
			assertThat(e.getMessage()).contains("FOO");
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
