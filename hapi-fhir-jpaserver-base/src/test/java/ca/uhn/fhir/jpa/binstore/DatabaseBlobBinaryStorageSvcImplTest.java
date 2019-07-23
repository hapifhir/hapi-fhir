package ca.uhn.fhir.jpa.binstore;

import ca.uhn.fhir.jpa.config.TestR4Config;
import ca.uhn.fhir.jpa.dao.r4.BaseJpaR4Test;
import org.hl7.fhir.r4.model.IdType;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.hamcrest.Matchers.matchesPattern;
import static org.junit.Assert.*;
import static org.junit.Assert.assertArrayEquals;

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
		IBinaryStorageSvc.StoredDetails outcome = mySvc.storeBlob(resourceId, contentType, inputStream);

		myCaptureQueriesListener.logAllQueriesForCurrentThread();

		assertEquals(0, myCaptureQueriesListener.getSelectQueriesForCurrentThread().size());
		assertEquals(1, myCaptureQueriesListener.getInsertQueriesForCurrentThread().size());
		assertEquals(2, myCaptureQueriesListener.getUpdateQueriesForCurrentThread().size());

		myCaptureQueriesListener.clear();

		assertThat(outcome.getBlobId(), matchesPattern("^[a-zA-Z0-9]{100}$"));
		assertEquals(16, outcome.getBytes());

		/*
		 * Read back the details
		 */

		IBinaryStorageSvc.StoredDetails details = mySvc.fetchBlobDetails(resourceId, outcome.getBlobId());
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


	}

	@Test
	public void testExpunge() throws IOException {

		/*
		 * Store the binary
		 */
		ByteArrayInputStream inputStream = new ByteArrayInputStream(SOME_BYTES);
		String contentType = "image/png";
		IdType resourceId = new IdType("Binary/123");
		IBinaryStorageSvc.StoredDetails outcome = mySvc.storeBlob(resourceId, contentType, inputStream);
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
		IBinaryStorageSvc.StoredDetails outcome = mySvc.storeBlob(resourceId, contentType, inputStream);

		// Right ID
		ByteArrayOutputStream capture = new ByteArrayOutputStream();
		assertTrue(mySvc.writeBlob(resourceId, outcome.getBlobId(), capture));
		assertEquals(16, capture.size());

		// Wrong ID
		capture = new ByteArrayOutputStream();
		assertFalse(mySvc.writeBlob(new IdType("Patient/9999"), outcome.getBlobId(), capture));
		assertEquals(0, capture.size());

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
