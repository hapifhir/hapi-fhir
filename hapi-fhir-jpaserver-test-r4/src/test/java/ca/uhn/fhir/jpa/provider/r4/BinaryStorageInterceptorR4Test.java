package ca.uhn.fhir.jpa.provider.r4;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.jpa.binary.api.IBinaryStorageSvc;
import ca.uhn.fhir.jpa.binary.interceptor.BinaryStorageInterceptor;
import ca.uhn.fhir.jpa.binstore.MemoryBinaryStorageSvcImpl;
import ca.uhn.fhir.jpa.model.entity.StorageSettings;
import ca.uhn.fhir.jpa.provider.BaseResourceProviderR4Test;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.client.api.IClientInterceptor;
import ca.uhn.fhir.rest.client.api.IHttpRequest;
import ca.uhn.fhir.rest.client.api.IHttpResponse;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.util.HapiExtensions;
import org.hl7.fhir.instance.model.api.IBaseHasExtensions;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Binary;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.DocumentReference;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.Extension;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Resource;
import org.hl7.fhir.r4.model.StringType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class BinaryStorageInterceptorR4Test extends BaseResourceProviderR4Test {

	public static final byte[] FEW_BYTES = {4, 3, 2, 1};
	public static final byte[] SOME_BYTES = {1, 2, 3, 4, 5, 6, 7, 8, 7, 6, 5, 4, 3, 2, 1, 8, 9, 0, 10, 9};
	public static final byte[] SOME_BYTES_2 = {6, 7, 8, 7, 6, 5, 4, 3, 2, 1, 5, 5, 5, 6};
	public static final byte[] SOME_BYTES_3 = {5, 5, 5, 6, 6, 7, 7, 7, 8, 8, 8};
	private static final Logger ourLog = LoggerFactory.getLogger(BinaryStorageInterceptorR4Test.class);

	@Autowired
	private JpaStorageSettings myStorageSettings;
	@Autowired
	private StorageSettings myOldStorageSettings;

	@Autowired
	private MemoryBinaryStorageSvcImpl myStorageSvc;
	@Autowired
	private IBinaryStorageSvc myBinaryStorageSvc;

	@Override
	@BeforeEach
	public void before() throws Exception {
		super.before();
		myStorageSvc.setMinimumBinarySize(10);
		myStorageSettings.setExpungeEnabled(true);

		myInterceptorRegistry.registerInterceptor(myBinaryStorageInterceptor);
	}

	@Override
	@AfterEach
	public void after() throws Exception {
		super.after();
		myStorageSvc.setMinimumBinarySize(0);
		myStorageSettings.setExpungeEnabled(new JpaStorageSettings().isExpungeEnabled());
		myBinaryStorageInterceptor.setAutoInflateBinariesMaximumSize(new BinaryStorageInterceptor<>(myFhirContext).getAutoInflateBinariesMaximumSize());
		myBinaryStorageInterceptor.setAllowAutoInflateBinaries(new BinaryStorageInterceptor<>(myFhirContext).isAllowAutoInflateBinaries());

		MemoryBinaryStorageSvcImpl binaryStorageSvc = (MemoryBinaryStorageSvcImpl) myBinaryStorageSvc;
		binaryStorageSvc.clear();

		myInterceptorRegistry.unregisterInterceptor(myBinaryStorageInterceptor);
	}

	private static class BinaryFilePrefixingInterceptor{

		@Hook(Pointcut.STORAGE_BINARY_ASSIGN_BLOB_ID_PREFIX)
		public String provideFilenameForBinary(RequestDetails theRequestDetails, IBaseResource theResource) {
			ourLog.info("Received binary for prefixing!" + theResource.getIdElement());
			String extensionValus = ((IBaseHasExtensions) theResource.getMeta()).getExtension().stream().map(ext -> ext.getValue().toString()).collect(Collectors.joining("-"));
			return "prefix-" + extensionValus + "-";
		}
	}

	@ParameterizedTest
	@EnumSource(value = RestOperationTypeEnum.class, names = {"CREATE", "UPDATE"})
	public void testCreatingExternalizedBinaryAppliesPrefix(RestOperationTypeEnum theOperationType) {
		BinaryFilePrefixingInterceptor interceptor = new BinaryFilePrefixingInterceptor();
		myInterceptorRegistry.registerInterceptor(interceptor);
		// Create a resource with two metadata extensions on the binary
		Binary binary = new Binary();
		binary.setContentType("application/octet-stream");
		Extension ext = binary.getMeta().addExtension();
		ext.setUrl("http://foo");
		ext.setValue(new StringType("bar"));

		Extension ext2 = binary.getMeta().addExtension();
		ext2.setUrl("http://foo2");
		ext2.setValue(new StringType("bar2"));

		binary.setData(SOME_BYTES);

		DaoMethodOutcome outcome = null;

		//Either CREATE or UPDATE
		if (theOperationType == RestOperationTypeEnum.CREATE) {
			outcome = myBinaryDao.create(binary, mySrd);
		} else if (theOperationType == RestOperationTypeEnum.UPDATE) {
			binary.setId("my-id");
			outcome = myBinaryDao.update(binary, mySrd);
		}

		// Then: Sure it was externalized
		outcome.getId().toUnqualifiedVersionless();
		String encoded = myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome.getResource());
		ourLog.info("Encoded: {}", encoded);
		assertThat(encoded).contains(HapiExtensions.EXT_EXTERNALIZED_BINARY_ID);

		// Then: Make sure the prefix was applied
		assertThat(encoded).contains("prefix-bar-bar2-");
		myInterceptorRegistry.unregisterInterceptor(interceptor);

	}

	private static class BinaryBlobIdPrefixInterceptor {
		@Hook(Pointcut.STORAGE_BINARY_ASSIGN_BLOB_ID_PREFIX)
		public String provideBlobIdForBinary(RequestDetails theRequestDetails, IBaseResource theResource) {
			ourLog.info("Received binary for prefixing!" + theResource.getIdElement());
			return "prefix-test-blob-id-";
		}
	}

	@Test
	public void testExternalizingBinaryFromRequestTriggersPointcutOnce() {
		BinaryBlobIdPrefixInterceptor interceptor = spy(new BinaryBlobIdPrefixInterceptor());
		myInterceptorRegistry.registerInterceptor(interceptor);
		// Create a resource with two metadata extensions on the binary
		Binary binary = new Binary();
		binary.setContentType("application/octet-stream");
		binary.setData(SOME_BYTES);
		DaoMethodOutcome outcome = myBinaryDao.create(binary, mySrd);

		// Make sure blobId prefix was set and pointcut called only once
		outcome.getId().toUnqualifiedVersionless();
		String encoded = myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome.getResource());
		ourLog.info("Encoded: {}", encoded);
		assertThat(encoded).contains("\"valueString\": \"prefix-test-blob-id-");
		verify(interceptor, times(1)).provideBlobIdForBinary(any(), any());

		myInterceptorRegistry.unregisterInterceptor(interceptor);
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
		assertThat(encoded).contains(HapiExtensions.EXT_EXTERNALIZED_BINARY_ID);
		assertThat(encoded).doesNotContain("\"data\"");

		// Now read it back and make sure it was de-externalized
		Binary output = myBinaryDao.read(id, mySrd);
		assertEquals("application/octet-stream", output.getContentType());
		assertThat(SOME_BYTES).containsExactly(output.getData());
	}


	@Test
	public void testCreateAndRetrieveBinary_ServerAssignedId_ExternalizedBinary_NoServletRequest() {

		// Create a resource with a big enough binary
		Binary binary = new Binary();
		binary.setContentType("application/octet-stream");
		binary.setData(SOME_BYTES);
		DaoMethodOutcome outcome = myBinaryDao.create(binary, mySrd);

		// Make sure it was externalized
		IIdType id = outcome.getId().toUnqualifiedVersionless();
		String encoded = myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome.getResource());
		ourLog.info("Encoded: {}", encoded);
		assertThat(encoded).contains(HapiExtensions.EXT_EXTERNALIZED_BINARY_ID);
		assertThat(encoded).doesNotContain("\"data\"");

		// Now read it back and make sure it was de-externalized
		Binary output = myBinaryDao.read(id, mySrd);
		assertEquals("application/octet-stream", output.getContentType());
		assertThat(SOME_BYTES).containsExactly(output.getData());

	}

	@Test
	public void testCreateAndRetrieveBinary_ServerAssignedId_ExternalizedBinary_DoNotRehydrate() {
		myBinaryStorageInterceptor.setAllowAutoInflateBinaries(false);

		// Create a resource with a big enough binary
		Binary binary = new Binary();
		binary.setContentType("application/octet-stream");
		binary.setData(SOME_BYTES);
		DaoMethodOutcome outcome = myBinaryDao.create(binary, mySrd);

		// Make sure it was externalized
		IIdType id = outcome.getId().toUnqualifiedVersionless();
		String encoded = myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome.getResource());
		ourLog.info("Encoded: {}", encoded);
		assertThat(encoded).contains(HapiExtensions.EXT_EXTERNALIZED_BINARY_ID);
		assertThat(encoded).doesNotContain("\"data\"");

		// Now read it back and make sure it was not  de-externalized
		Binary output = myBinaryDao.read(id, mySrd);
		assertEquals("application/octet-stream", output.getContentType());
		assertNull(output.getData());;
		assertNotNull(output.getDataElement().getExtensionByUrl(HapiExtensions.EXT_EXTERNALIZED_BINARY_ID));
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
		assertThat(encoded).contains("\"data\": \"BAMCAQ==\"");
		assertThat(encoded).doesNotContain(HapiExtensions.EXT_EXTERNALIZED_BINARY_ID);

		// Now read it back and make sure it was de-externalized
		Binary output = myBinaryDao.read(id, mySrd);
		assertEquals("application/octet-stream", output.getContentType());
		assertThat(FEW_BYTES).containsExactly(output.getData());

	}


	static class ContentTypeStrippingInterceptor implements IClientInterceptor {

		@Override
		public void interceptRequest(IHttpRequest theRequest) {
			theRequest.removeHeaders("Content-Type");
			theRequest.removeHeaders("Accept");
		}

		@Override
		public void interceptResponse(IHttpResponse theResponse) {

		}
	}
	@Test
	public void testCreateAndRetrieveExternalizedBinaryViaGetWithNoHeaders() {

		myBinaryStorageInterceptor.setAllowAutoInflateBinaries(false);
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
		assertThat(encoded).contains(HapiExtensions.EXT_EXTERNALIZED_BINARY_ID);
		assertThat(encoded).doesNotContain("\"data\"");

		// Now read it back and make sure it is not successfully de-externalized
		ContentTypeStrippingInterceptor interceptor = new ContentTypeStrippingInterceptor();
		myClient.registerInterceptor(interceptor);
		Binary execute = myClient.read().resource(Binary.class).withId(id).execute();
		myClient.unregisterInterceptor(interceptor);

		assertNull(execute.getContent());
		assertNull(execute.getDataElement().getValue());

		assertNotNull(execute.getDataElement().getExtensionByUrl(HapiExtensions.EXT_EXTERNALIZED_BINARY_ID).getValue());
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
		assertThat(encoded).contains(HapiExtensions.EXT_EXTERNALIZED_BINARY_ID);
		assertThat(encoded).doesNotContain("\"data\"");

		// Now read it back and make sure it was de-externalized
		Binary output = myBinaryDao.read(id, mySrd);
		assertEquals("application/octet-stream", output.getContentType());
		assertThat(SOME_BYTES).containsExactly(output.getData());
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
		assertThat(encoded).contains(HapiExtensions.EXT_EXTERNALIZED_BINARY_ID);
		assertThat(encoded).doesNotContain("\"data\"");

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
		assertThat(SOME_BYTES).containsExactly(output.getData());

		// Now read back the second version
		output = myBinaryDao.read(id.withVersion("2"), mySrd);
		assertEquals("application/octet-stream", output.getContentType());
		assertThat(SOME_BYTES_2).containsExactly(output.getData());

	}


	@Test
	public void testUpdatePreservingExistingExternalizedBinary() {

		// Create a resource with a big enough docRef
		DocumentReference docRef = new DocumentReference();
		addDocumentAttachmentData(docRef, SOME_BYTES);
		addDocumentAttachmentData(docRef, SOME_BYTES_2);
		DaoMethodOutcome outcome = myDocumentReferenceDao.create(docRef, mySrd);

		// Make sure it was externalized
		IIdType id = outcome.getId().toUnqualifiedVersionless();
		String encoded = myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome.getResource());
		ourLog.info("Encoded: {}", encoded);
		assertThat(encoded).contains(HapiExtensions.EXT_EXTERNALIZED_BINARY_ID);
		assertThat(encoded).doesNotContain("\"data\"");
		String binaryId = docRef.getContentFirstRep().getAttachment().getDataElement().getExtensionString(HapiExtensions.EXT_EXTERNALIZED_BINARY_ID);
		assertThat(binaryId).isNotBlank();

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
		assertThat(output.getContentFirstRep().getAttachment().getData()).containsExactly(SOME_BYTES);

		// Now read back the second version
		output = myDocumentReferenceDao.read(id.withVersion("2"), mySrd);
		assertEquals("application/octet-stream", output.getContentFirstRep().getAttachment().getContentType());
		assertThat(output.getContentFirstRep().getAttachment().getData()).containsExactly(SOME_BYTES);

	}

	@Test
	public void testCreateBinaryAttachments_bundleWithMultipleDocumentReferences_createdAndReadBackSuccessfully() {
		// Create Patient
		Patient patient = new Patient();
		patient.addIdentifier().setSystem("urn:system").setValue("001");
		patient.addName().addGiven("Johnny").setFamily("Walker");

		// Create first DocumentReference with a big enough attachments
		DocumentReference docRef = new DocumentReference();
		addDocumentAttachmentData(docRef, SOME_BYTES);
		addDocumentAttachmentData(docRef, SOME_BYTES_2);

		// Create second DocumentReference with a big enough attachment
		DocumentReference docRef2 = new DocumentReference();
		addDocumentAttachmentData(docRef2, SOME_BYTES_3);

		// Create Bundle
		Bundle bundle = new Bundle();
		bundle.setType(Bundle.BundleType.TRANSACTION);
		// Patient entry component
		addBundleEntry(bundle, patient, "Patient");
		// First DocumentReference entry component
		addBundleEntry(bundle, docRef, "DocumentReference");
		// Second DocumentReference entry component
		addBundleEntry(bundle, docRef2, "DocumentReference");

		// Execute transaction
		Bundle output = myClient.transaction().withBundle(bundle).execute();
		ourLog.debug(myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(output));

		// Verify bundle response
		assertThat(output.getEntry()).hasSize(3);
		output.getEntry().forEach(entry -> assertEquals("201 Created", entry.getResponse().getStatus()));

		// Read back and verify first DocumentReference and attachments
		IIdType firstDocRef = new IdType(output.getEntry().get(1).getResponse().getLocation());
		DocumentReference firstDoc = myDocumentReferenceDao.read(firstDocRef, mySrd);
		assertEquals("application/octet-stream", firstDoc.getContentFirstRep().getAttachment().getContentType());
		assertThat(firstDoc.getContentFirstRep().getAttachment().getData()).containsExactly(SOME_BYTES);
		assertEquals("application/octet-stream", firstDoc.getContent().get(1).getAttachment().getContentType());
		assertThat(firstDoc.getContent().get(1).getAttachment().getData()).containsExactly(SOME_BYTES_2);
		assertEquals("application/octet-stream", firstDoc.getContent().get(1).getAttachment().getContentType());

		// Read back and verify second DocumentReference and attachment
		IIdType secondDocRef = new IdType(output.getEntry().get(2).getResponse().getLocation());
		DocumentReference secondDoc = myDocumentReferenceDao.read(secondDocRef, mySrd);
		assertEquals("application/octet-stream", secondDoc.getContentFirstRep().getAttachment().getContentType());
		assertThat(secondDoc.getContentFirstRep().getAttachment().getData()).containsExactly(SOME_BYTES_3);
	}

	private void addBundleEntry(Bundle theBundle, Resource theResource, String theUrl) {
		Bundle.BundleEntryComponent getComponent = new Bundle.BundleEntryComponent();
		Bundle.BundleEntryRequestComponent requestComponent = new Bundle.BundleEntryRequestComponent();
		requestComponent.setMethod(Bundle.HTTPVerb.POST);
		requestComponent.setUrl(theUrl);
		getComponent.setRequest(requestComponent);
		getComponent.setResource(theResource);
		getComponent.setFullUrl(IdDt.newRandomUuid().getValue());
		theBundle.addEntry(getComponent);
	}

	@Test
	public void testUpdateRejectsIncorrectBinary() {

		// Create a resource with a big enough docRef
		DocumentReference docRef = new DocumentReference();
		addDocumentAttachmentData(docRef, SOME_BYTES);
		addDocumentAttachmentData(docRef, SOME_BYTES_2);
		DaoMethodOutcome outcome = myDocumentReferenceDao.create(docRef, mySrd);

		// Make sure it was externalized
		IIdType id = outcome.getId().toUnqualifiedVersionless();
		String encoded = myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome.getResource());
		ourLog.info("Encoded: {}", encoded);
		assertThat(encoded).contains(HapiExtensions.EXT_EXTERNALIZED_BINARY_ID);
		assertThat(encoded).doesNotContain("\"data\"");
		String binaryId = docRef.getContentFirstRep().getAttachment().getDataElement().getExtensionString(HapiExtensions.EXT_EXTERNALIZED_BINARY_ID);
		assertThat(binaryId).isNotBlank();

		// Now update
		docRef = new DocumentReference();
		docRef.setId(id.toUnqualifiedVersionless());
		docRef.setStatus(Enumerations.DocumentReferenceStatus.CURRENT);
		DocumentReference.DocumentReferenceContentComponent content = docRef.addContent();
		content.getAttachment().setContentType("application/octet-stream");
		content.getAttachment().getDataElement().addExtension(
			HapiExtensions.EXT_EXTERNALIZED_BINARY_ID,
			new StringType(binaryId)
		);
		DocumentReference.DocumentReferenceContentComponent content2 = docRef.addContent();
		content2.getAttachment().setContentType("application/octet-stream");
		content2.getAttachment().getDataElement().addExtension(
			HapiExtensions.EXT_EXTERNALIZED_BINARY_ID,
			new StringType("12345-67890")
		);

		try {
			myDocumentReferenceDao.update(docRef, mySrd);
			fail("Should not be able to update docRef");
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
		assertThat(encoded).contains(HapiExtensions.EXT_EXTERNALIZED_BINARY_ID);
		assertThat(encoded).doesNotContain("\"data\"");

		// Now read it back and make sure it was de-externalized
		Binary output = myBinaryDao.read(id, mySrd);
		assertEquals("application/octet-stream", output.getContentType());
		assertNull(output.getData());
		assertNotNull(output.getDataElement().getExtensionByUrl(HapiExtensions.EXT_EXTERNALIZED_BINARY_ID).getValue());

	}

	private void addDocumentAttachmentData(DocumentReference theDocumentReference, byte[] theData) {
		DocumentReference.DocumentReferenceContentComponent content = theDocumentReference.addContent();
		content.getAttachment().setContentType("application/octet-stream");
		content.getAttachment().setData(theData);
	}

}
