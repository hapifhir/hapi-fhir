package ca.uhn.fhir.jpa.provider.r4;

import ca.uhn.fhir.jpa.binstore.BinaryStorageInterceptor;
import ca.uhn.fhir.jpa.binstore.IBinaryStorageSvc;
import ca.uhn.fhir.jpa.binstore.MemoryBinaryStorageSvcImpl;
import ca.uhn.fhir.jpa.dao.DaoConfig;
import ca.uhn.fhir.jpa.dao.DaoMethodOutcome;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Binary;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.*;

public class BinaryStorageInterceptorR4Test extends BaseResourceProviderR4Test {

	public static final byte[] FEW_BYTES = {4, 3, 2, 1};
	public static final byte[] SOME_BYTES = {1, 2, 3, 4, 5, 6, 7, 8, 7, 6, 5, 4, 3, 2, 1};
	public static final byte[] SOME_BYTES_2 = {6, 7, 8, 7, 6, 5, 4, 3, 2, 1, 5, 5, 5, 6};
	private static final Logger ourLog = LoggerFactory.getLogger(BinaryStorageInterceptorR4Test.class);

	@Autowired
	private MemoryBinaryStorageSvcImpl myStorageSvc;
	@Autowired
	private IBinaryStorageSvc myBinaryStorageSvc;

	@Override
	@Before
	public void before() throws Exception {
		super.before();
		myStorageSvc.setMinimumBinarySize(10);
		myDaoConfig.setExpungeEnabled(true);
		myInterceptorRegistry.registerInterceptor(myBinaryStorageInterceptor);
	}

	@Override
	@After
	public void after() throws Exception {
		super.after();
		myStorageSvc.setMinimumBinarySize(0);
		myDaoConfig.setExpungeEnabled(new DaoConfig().isExpungeEnabled());
		myBinaryStorageInterceptor.setAutoDeExternalizeMaximumBytes(new BinaryStorageInterceptor().getAutoDeExternalizeMaximumBytes());

		MemoryBinaryStorageSvcImpl binaryStorageSvc = (MemoryBinaryStorageSvcImpl) myBinaryStorageSvc;
		binaryStorageSvc.clear();
	}

	@Test
	public void testCreateAndRetrieveBinary_ServerAssignedId_ExternalizedBinary() {

		// Create a resource with a big enough binary
		Binary binary = new Binary();
		binary.setContentType("application/octet-stream");
		binary.setData(SOME_BYTES);
		DaoMethodOutcome outcome = myBinaryDao.create(binary, mySrd);

		// Make sure it was externalized
		IIdType id = outcome.getId().toUnqualifiedVersionless();
		String encoded = myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome.getResource());
		ourLog.info("Encoded: {}", encoded);
		assertThat(encoded, containsString(JpaConstants.EXT_EXTERNALIZED_BINARY_ID));
		assertThat(encoded, not(containsString("\"data\"")));

		// Now read it back and make sure it was de-externalized
		Binary output = myBinaryDao.read(id, mySrd);
		assertEquals("application/octet-stream", output.getContentType());
		assertArrayEquals(SOME_BYTES, output.getData());

	}


	@Test
	public void testCreateAndRetrieveBinary_ServerAssignedId_ExternalizedBinary_NullServletRequest() {

		// Create a resource with a big enough binary
		Binary binary = new Binary();
		binary.setContentType("application/octet-stream");
		binary.setData(SOME_BYTES);
		DaoMethodOutcome outcome = myBinaryDao.create(binary);

		// Make sure it was externalized
		IIdType id = outcome.getId().toUnqualifiedVersionless();
		String encoded = myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome.getResource());
		ourLog.info("Encoded: {}", encoded);
		assertThat(encoded, not(containsString(JpaConstants.EXT_EXTERNALIZED_BINARY_ID)));
		assertThat(encoded, containsString("\"data\""));

		Binary output = myBinaryDao.read(id);
		assertEquals("application/octet-stream", output.getContentType());
		assertArrayEquals(SOME_BYTES, output.getData());
	}

	@Test
	public void testCreateAndRetrieveBinary_ServerAssignedId_NonExternalizedBinary() {

		// Create a resource with a small binary
		Binary binary = new Binary();
		binary.setContentType("application/octet-stream");
		binary.setData(FEW_BYTES);
		DaoMethodOutcome outcome = myBinaryDao.create(binary, mySrd);

		// Make sure it was externalized
		IIdType id = outcome.getId().toUnqualifiedVersionless();
		String encoded = myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome.getResource());
		ourLog.info("Encoded: {}", encoded);
		assertThat(encoded, containsString("\"data\": \"BAMCAQ==\""));
		assertThat(encoded, not(containsString(JpaConstants.EXT_EXTERNALIZED_BINARY_ID)));

		// Now read it back and make sure it was de-externalized
		Binary output = myBinaryDao.read(id, mySrd);
		assertEquals("application/octet-stream", output.getContentType());
		assertArrayEquals(FEW_BYTES, output.getData());

	}

	@Test
	public void testCreateAndRetrieveBinary_ClientAssignedId_ExternalizedBinary() {

		// Create a resource with a big enough binary
		Binary binary = new Binary();
		binary.setId("FOO");
		binary.setContentType("application/octet-stream");
		binary.setData(SOME_BYTES);
		DaoMethodOutcome outcome = myBinaryDao.update(binary, mySrd);

		// Make sure it was externalized
		IIdType id = outcome.getId().toUnqualifiedVersionless();
		String encoded = myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome.getResource());
		ourLog.info("Encoded: {}", encoded);
		assertThat(encoded, containsString(JpaConstants.EXT_EXTERNALIZED_BINARY_ID));
		assertThat(encoded, not(containsString("\"data\"")));

		// Now read it back and make sure it was de-externalized
		Binary output = myBinaryDao.read(id, mySrd);
		assertEquals("application/octet-stream", output.getContentType());
		assertArrayEquals(SOME_BYTES, output.getData());
		assertNotNull(output.getDataElement().getExtensionByUrl(JpaConstants.EXT_EXTERNALIZED_BINARY_ID).getValue());

	}


	@Test
	public void testUpdateAndRetrieveBinary_ServerAssignedId_ExternalizedBinary() {

		// Create a resource with a big enough binary
		Binary binary = new Binary();
		binary.setContentType("application/octet-stream");
		binary.setData(SOME_BYTES);
		DaoMethodOutcome outcome = myBinaryDao.create(binary, mySrd);

		// Make sure it was externalized
		IIdType id = outcome.getId().toUnqualifiedVersionless();
		String encoded = myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome.getResource());
		ourLog.info("Encoded: {}", encoded);
		assertThat(encoded, containsString(JpaConstants.EXT_EXTERNALIZED_BINARY_ID));
		assertThat(encoded, not(containsString("\"data\"")));

		// Now update
		binary = new Binary();
		binary.setId(id.toUnqualifiedVersionless());
		binary.setContentType("application/octet-stream");
		binary.setData(SOME_BYTES_2);
		outcome = myBinaryDao.update(binary, mySrd);
		assertEquals("2", outcome.getId().getVersionIdPart());

		// Now read it back the first version
		Binary output = myBinaryDao.read(id.withVersion("1"), mySrd);
		assertEquals("application/octet-stream", output.getContentType());
		assertArrayEquals(SOME_BYTES, output.getData());

		// Now read back the second version
		output = myBinaryDao.read(id.withVersion("2"), mySrd);
		assertEquals("application/octet-stream", output.getContentType());
		assertArrayEquals(SOME_BYTES_2, output.getData());

	}


	@Test
	public void testRetrieveBinaryAboveRetrievalThreshold() {
		myBinaryStorageInterceptor.setAutoDeExternalizeMaximumBytes(5);

		// Create a resource with a big enough binary
		Binary binary = new Binary();
		binary.setContentType("application/octet-stream");
		binary.setData(SOME_BYTES);
		DaoMethodOutcome outcome = myBinaryDao.create(binary, mySrd);

		// Make sure it was externalized
		IIdType id = outcome.getId().toUnqualifiedVersionless();
		String encoded = myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome.getResource());
		ourLog.info("Encoded: {}", encoded);
		assertThat(encoded, containsString(JpaConstants.EXT_EXTERNALIZED_BINARY_ID));
		assertThat(encoded, not(containsString("\"data\"")));

		// Now read it back and make sure it was de-externalized
		Binary output = myBinaryDao.read(id, mySrd);
		assertEquals("application/octet-stream", output.getContentType());
		assertEquals(null, output.getData());
		assertNotNull(output.getDataElement().getExtensionByUrl(JpaConstants.EXT_EXTERNALIZED_BINARY_ID).getValue());

	}


}
