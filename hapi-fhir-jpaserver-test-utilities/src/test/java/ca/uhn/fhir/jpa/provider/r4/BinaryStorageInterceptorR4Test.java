package ca.uhn.fhir.jpa.provider.r4;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.jpa.binary.interceptor.BinaryStorageInterceptor;
import ca.uhn.fhir.jpa.binary.api.IBinaryStorageSvc;
import ca.uhn.fhir.jpa.binstore.MemoryBinaryStorageSvcImpl;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.util.HapiExtensions;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Binary;
import org.hl7.fhir.r4.model.DocumentReference;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.StringType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.blankOrNullString;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

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
	@BeforeEach
	public void before() throws Exception {
		super.before();
		myStorageSvc.setMinimumBinarySize(10);
		myDaoConfig.setExpungeEnabled(true);

		myInterceptorRegistry.registerInterceptor(myBinaryStorageInterceptor);
	}

	@Override
	@AfterEach
	public void after() throws Exception {
		super.after();
		myStorageSvc.setMinimumBinarySize(0);
		myDaoConfig.setExpungeEnabled(new DaoConfig().isExpungeEnabled());
		myBinaryStorageInterceptor.setAutoInflateBinariesMaximumSize(new BinaryStorageInterceptor().getAutoInflateBinariesMaximumSize());
		myBinaryStorageInterceptor.setAllowAutoInflateBinaries(new BinaryStorageInterceptor().isAllowAutoInflateBinaries());

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
		String encoded = myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome.getResource());
		ourLog.info("Encoded: {}", encoded);
		assertThat(encoded, containsString(HapiExtensions.EXT_EXTERNALIZED_BINARY_ID));
		assertThat(encoded, not(containsString("\"data\"")));

		// Now read it back and make sure it was de-externalized
		Binary output = myBinaryDao.read(id, mySrd);
		assertEquals("application/octet-stream", output.getContentType());
		assertArrayEquals(SOME_BYTES, output.getData());

	}


	@Test
	public void testCreateAndRetrieveBinary_ServerAssignedId_ExternalizedBinary_NoServletRequest() {

		// Create a resource with a big enough binary
		Binary binary = new Binary();
		binary.setContentType("application/octet-stream");
		binary.setData(SOME_BYTES);
		DaoMethodOutcome outcome = myBinaryDao.create(binary);

		// Make sure it was externalized
		IIdType id = outcome.getId().toUnqualifiedVersionless();
		String encoded = myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome.getResource());
		ourLog.info("Encoded: {}", encoded);
		assertThat(encoded, containsString(HapiExtensions.EXT_EXTERNALIZED_BINARY_ID));
		assertThat(encoded, not(containsString("\"data\"")));

		// Now read it back and make sure it was de-externalized
		Binary output = myBinaryDao.read(id, mySrd);
		assertEquals("application/octet-stream", output.getContentType());
		assertArrayEquals(SOME_BYTES, output.getData());

	}

	@Test
	public void testCreateAndRetrieveBinary_ServerAssignedId_ExternalizedBinary_DoNotRehydrate() {
		myBinaryStorageInterceptor.setAllowAutoInflateBinaries(false);

		// Create a resource with a big enough binary
		Binary binary = new Binary();
		binary.setContentType("application/octet-stream");
		binary.setData(SOME_BYTES);
		DaoMethodOutcome outcome = myBinaryDao.create(binary);

		// Make sure it was externalized
		IIdType id = outcome.getId().toUnqualifiedVersionless();
		String encoded = myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome.getResource());
		ourLog.info("Encoded: {}", encoded);
		assertThat(encoded, containsString(HapiExtensions.EXT_EXTERNALIZED_BINARY_ID));
		assertThat(encoded, not(containsString("\"data\"")));

		// Now read it back and make sure it was not  de-externalized
		Binary output = myBinaryDao.read(id, mySrd);
		assertEquals("application/octet-stream", output.getContentType());
		assertArrayEquals(null, output.getData());
		assertThat(output.getDataElement().getExtensionByUrl(HapiExtensions.EXT_EXTERNALIZED_BINARY_ID), is(notNullValue()));
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
		String encoded = myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome.getResource());
		ourLog.info("Encoded: {}", encoded);
		assertThat(encoded, containsString("\"data\": \"BAMCAQ==\""));
		assertThat(encoded, not(containsString(HapiExtensions.EXT_EXTERNALIZED_BINARY_ID)));

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
		String encoded = myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome.getResource());
		ourLog.info("Encoded: {}", encoded);
		assertThat(encoded, containsString(HapiExtensions.EXT_EXTERNALIZED_BINARY_ID));
		assertThat(encoded, not(containsString("\"data\"")));

		// Now read it back and make sure it was de-externalized
		Binary output = myBinaryDao.read(id, mySrd);
		assertEquals("application/octet-stream", output.getContentType());
		assertArrayEquals(SOME_BYTES, output.getData());
		assertNotNull(output.getDataElement().getExtensionByUrl(HapiExtensions.EXT_EXTERNALIZED_BINARY_ID).getValue());

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
		String encoded = myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome.getResource());
		ourLog.info("Encoded: {}", encoded);
		assertThat(encoded, containsString(HapiExtensions.EXT_EXTERNALIZED_BINARY_ID));
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
	public void testUpdatePreservingExistingExternalizedBinary() {

		// Create a resource with a big enough docRef
		DocumentReference docRef = new DocumentReference();
		DocumentReference.DocumentReferenceContentComponent content = docRef.addContent();
		content.getAttachment().setContentType("application/octet-stream");
		content.getAttachment().setData(SOME_BYTES);
		DocumentReference.DocumentReferenceContentComponent content2 = docRef.addContent();
		content2.getAttachment().setContentType("application/octet-stream");
		content2.getAttachment().setData(SOME_BYTES_2);
		DaoMethodOutcome outcome = myDocumentReferenceDao.create(docRef, mySrd);

		// Make sure it was externalized
		IIdType id = outcome.getId().toUnqualifiedVersionless();
		String encoded = myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome.getResource());
		ourLog.info("Encoded: {}", encoded);
		assertThat(encoded, containsString(HapiExtensions.EXT_EXTERNALIZED_BINARY_ID));
		assertThat(encoded, not(containsString("\"data\"")));
		String binaryId = docRef.getContentFirstRep().getAttachment().getDataElement().getExtensionString(HapiExtensions.EXT_EXTERNALIZED_BINARY_ID);
		assertThat(binaryId, not(blankOrNullString()));

		// Now update
		docRef = new DocumentReference();
		docRef.setId(id.toUnqualifiedVersionless());
		docRef.setStatus(Enumerations.DocumentReferenceStatus.CURRENT);
		docRef.getContentFirstRep().getAttachment().setContentType("application/octet-stream");
		docRef.getContentFirstRep().getAttachment().getDataElement().addExtension(
			HapiExtensions.EXT_EXTERNALIZED_BINARY_ID,
			new StringType(binaryId)
		);
		outcome = myDocumentReferenceDao.update(docRef, mySrd);
		assertEquals("2", outcome.getId().getVersionIdPart());

		// Now read it back the first version
		DocumentReference output = myDocumentReferenceDao.read(id.withVersion("1"), mySrd);
		assertEquals("application/octet-stream", output.getContentFirstRep().getAttachment().getContentType());
		assertArrayEquals(SOME_BYTES, output.getContentFirstRep().getAttachment().getData());

		// Now read back the second version
		output = myDocumentReferenceDao.read(id.withVersion("2"), mySrd);
		assertEquals("application/octet-stream", output.getContentFirstRep().getAttachment().getContentType());
		assertArrayEquals(SOME_BYTES, output.getContentFirstRep().getAttachment().getData());

	}


	@Test
	public void testUpdateRejectsIncorrectBinary() {

		// Create a resource with a big enough docRef
		DocumentReference docRef = new DocumentReference();
		DocumentReference.DocumentReferenceContentComponent content = docRef.addContent();
		content.getAttachment().setContentType("application/octet-stream");
		content.getAttachment().setData(SOME_BYTES);
		DocumentReference.DocumentReferenceContentComponent content2 = docRef.addContent();
		content2.getAttachment().setContentType("application/octet-stream");
		content2.getAttachment().setData(SOME_BYTES_2);
		DaoMethodOutcome outcome = myDocumentReferenceDao.create(docRef, mySrd);

		// Make sure it was externalized
		IIdType id = outcome.getId().toUnqualifiedVersionless();
		String encoded = myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome.getResource());
		ourLog.info("Encoded: {}", encoded);
		assertThat(encoded, containsString(HapiExtensions.EXT_EXTERNALIZED_BINARY_ID));
		assertThat(encoded, not(containsString("\"data\"")));
		String binaryId = docRef.getContentFirstRep().getAttachment().getDataElement().getExtensionString(HapiExtensions.EXT_EXTERNALIZED_BINARY_ID);
		assertThat(binaryId, not(blankOrNullString()));

		// Now update
		docRef = new DocumentReference();
		docRef.setId(id.toUnqualifiedVersionless());
		docRef.setStatus(Enumerations.DocumentReferenceStatus.CURRENT);
		content = docRef.addContent();
		content.getAttachment().setContentType("application/octet-stream");
		content.getAttachment().getDataElement().addExtension(
			HapiExtensions.EXT_EXTERNALIZED_BINARY_ID,
			new StringType(binaryId)
		);
		content2 = docRef.addContent();
		content2.getAttachment().setContentType("application/octet-stream");
		content2.getAttachment().getDataElement().addExtension(
			HapiExtensions.EXT_EXTERNALIZED_BINARY_ID,
			new StringType("12345-67890")
		);

		try {
			myDocumentReferenceDao.update(docRef, mySrd);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals(Msg.code(1329) + "Illegal extension found in request payload - URL \"http://hapifhir.io/fhir/StructureDefinition/externalized-binary-id\" and value \"12345-67890\"", e.getMessage());
		}
	}



	@Test
	public void testRetrieveBinaryAboveRetrievalThreshold() {
		myBinaryStorageInterceptor.setAutoInflateBinariesMaximumSize(5);

		// Create a resource with a big enough binary
		Binary binary = new Binary();
		binary.setContentType("application/octet-stream");
		binary.setData(SOME_BYTES);
		DaoMethodOutcome outcome = myBinaryDao.create(binary, mySrd);

		// Make sure it was externalized
		IIdType id = outcome.getId().toUnqualifiedVersionless();
		String encoded = myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome.getResource());
		ourLog.info("Encoded: {}", encoded);
		assertThat(encoded, containsString(HapiExtensions.EXT_EXTERNALIZED_BINARY_ID));
		assertThat(encoded, not(containsString("\"data\"")));

		// Now read it back and make sure it was de-externalized
		Binary output = myBinaryDao.read(id, mySrd);
		assertEquals("application/octet-stream", output.getContentType());
		assertEquals(null, output.getData());
		assertNotNull(output.getDataElement().getExtensionByUrl(HapiExtensions.EXT_EXTERNALIZED_BINARY_ID).getValue());

	}


}
