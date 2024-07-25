package ca.uhn.fhir.jpa.binstore;

import ca.uhn.fhir.jpa.binary.api.IBinaryStorageSvc;
import ca.uhn.fhir.jpa.binary.api.StoredDetails;
import ca.uhn.fhir.jpa.dao.data.IBinaryStorageEntityDao;
import ca.uhn.fhir.jpa.model.entity.BinaryStorageEntity;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import jakarta.persistence.EntityManager;
import org.hibernate.LobHelper;
import org.hibernate.Session;
import org.hl7.fhir.r4.model.IdType;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
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

		assertThat(myCaptureQueriesListener.getSelectQueriesForCurrentThread()).isEmpty();
		assertThat(myCaptureQueriesListener.getInsertQueriesForCurrentThread()).hasSize(1);
		assertThat(myCaptureQueriesListener.getUpdateQueriesForCurrentThread()).isEmpty();

		myCaptureQueriesListener.clear();

		assertThat(outcome.getBinaryContentId()).matches("^[a-zA-Z0-9]{100}$");
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

		assertThat(capture.toByteArray()).containsExactly(SOME_BYTES);
		assertThat(mySvc.fetchBinaryContent(resourceId, outcome.getBinaryContentId())).containsExactly(SOME_BYTES);
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

		assertThat(myCaptureQueriesListener.getSelectQueriesForCurrentThread()).isEmpty();
		assertThat(myCaptureQueriesListener.getInsertQueriesForCurrentThread()).hasSize(1);
		assertThat(myCaptureQueriesListener.getUpdateQueriesForCurrentThread()).isEmpty();

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

		assertThat(capture.toByteArray()).containsExactly(SOME_BYTES);
		assertThat(mySvc.fetchBinaryContent(resourceId, outcome.getBinaryContentId())).containsExactly(SOME_BYTES);
	}

	@Test
	public void testFetchBinaryContentUnknown() throws IOException {
		assertThatThrownBy(() ->
			mySvc.fetchBinaryContent(new IdType("Patient/123"), "1111111"))
			.isInstanceOf(ResourceNotFoundException.class)
			.hasMessage("Unknown BinaryContent ID: 1111111 for resource ID Patient/123");

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

		assertThatThrownBy(() ->
			svc.copyBinaryContentToOutputStream(new ByteArrayOutputStream(), (mockInput)))
			.isInstanceOf(IOException.class)
				.hasMessageContaining("FOO");
	}

	@Test
	public void testCopyBinaryContentToByteArray_Exception() throws SQLException {
		DatabaseBinaryContentStorageSvcImpl svc = new DatabaseBinaryContentStorageSvcImpl();

		BinaryStorageEntity mockInput = new BinaryStorageEntity();
		Blob blob = mock(Blob.class);
		when(blob.getBinaryStream()).thenThrow(new SQLException("FOO"));
		mockInput.setBlob(blob);

			assertThatThrownBy(() ->
			svc.copyBinaryContentToByteArray(mockInput))
				.isInstanceOf(IOException.class)
					.hasMessageContaining("FOO");
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
		when(mockInput.getBlob()).thenAnswer(t -> {
			Blob blob = mock(Blob.class);
			when(blob.getBinaryStream()).thenReturn(new ByteArrayInputStream(SOME_BYTES));
			return blob;
		});

		// when
		svc.copyBinaryContentToByteArray(mockInput);

		// then
		verify(mockInput, times(1)).hasBlob();
		verify(mockInput, times(1)).getBlob();
	}

	@Test
	public void testStoreBinaryContent_byDefault_writesByteArrayOnly() throws IOException {
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
			assertTrue(binaryStorageEntity.hasStorageContent());
			assertFalse(binaryStorageEntity.hasBlob());
		});

	}

	@Test
	public void testStoreBinaryContent_whenSupportingLegacyBlobServer_willStoreToBlobAndBinaryArray() throws IOException {
		ArgumentCaptor<BinaryStorageEntity> captor = ArgumentCaptor.forClass(BinaryStorageEntity.class);
		EntityManager mockedEntityManager = mock(EntityManager.class);
		Session mockedSession = mock(Session.class);
		LobHelper mockedLobHelper = mock(LobHelper.class);
		when(mockedEntityManager.getDelegate()).thenReturn(mockedSession);
		when(mockedSession.getLobHelper()).thenReturn(mockedLobHelper);
		when(mockedLobHelper.createBlob(any())).thenReturn(mock(Blob.class));

		// given
		DatabaseBinaryContentStorageSvcImpl svc = new DatabaseBinaryContentStorageSvcImpl()
			.setSupportLegacyLobServer(true)
			.setEntityManagerForTesting(mockedEntityManager);

		ByteArrayInputStream inputStream = new ByteArrayInputStream(SOME_BYTES);
		String contentType = "image/png";
		IdType resourceId = new IdType("Binary/123");

		// when
		svc.storeBinaryContent(resourceId, null, contentType, inputStream, new ServletRequestDetails());

		// then
		verify(mockedEntityManager, times(1)).persist(captor.capture());
		BinaryStorageEntity capturedBinaryStorageEntity = captor.getValue();

		assertTrue(capturedBinaryStorageEntity.hasBlob());
		assertTrue(capturedBinaryStorageEntity.hasStorageContent());
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
