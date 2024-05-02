package ca.uhn.fhir.jpa.provider.r4;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertFalse;
import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IAnonymousInterceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.binary.api.IBinaryStorageSvc;
import ca.uhn.fhir.jpa.binstore.MemoryBinaryStorageSvcImpl;
import ca.uhn.fhir.jpa.interceptor.UserRequestRetryVersionConflictsInterceptor;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.jpa.provider.BaseResourceProviderR4Test;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.ResponseDetails;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceGoneException;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import ca.uhn.fhir.util.HapiExtensions;
import ca.uhn.test.concurrency.PointcutLatch;
import com.google.common.base.Charsets;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Attachment;
import org.hl7.fhir.r4.model.Binary;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.DateTimeType;
import org.hl7.fhir.r4.model.DocumentReference;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.StringType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class BinaryAccessProviderR4Test extends BaseResourceProviderR4Test {

	public static final byte[] SOME_BYTES = {1, 2, 3, 4, 5, 6, 7, 8, 7, 6, 5, 4, 3, 2, 1};
	public static final byte[] SOME_BYTES_2 = {5, 5, 5, 6};
	private static final Logger ourLog = LoggerFactory.getLogger(BinaryAccessProviderR4Test.class);

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
		myInterceptorRegistry.unregisterInterceptor(myBinaryStorageInterceptor);
	}

	@Test
	public void testRead() throws IOException {
		IIdType id = createDocumentReference(true);

		IAnonymousInterceptor interceptor = mock(IAnonymousInterceptor.class);
		myInterceptorRegistry.registerAnonymousInterceptor(Pointcut.STORAGE_PRESHOW_RESOURCES, interceptor);
		try {
			doAnswer(t -> {
				Pointcut pointcut = t.getArgument(0, Pointcut.class);
				HookParams params = t.getArgument(1, HookParams.class);
				ourLog.info("Interceptor invoked with pointcut {} and params {}", pointcut, params);
				return null;
			}).when(interceptor).invoke(any(), any());


			// Read it back using the operation

			String path = myServerBase +
				"/DocumentReference/" + id.getIdPart() + "/" +
				JpaConstants.OPERATION_BINARY_ACCESS_READ +
				"?path=DocumentReference.content.attachment";
			HttpGet get = new HttpGet(path);

			try (CloseableHttpResponse resp = ourHttpClient.execute(get)) {
				assertThat(resp.getStatusLine().getStatusCode()).isEqualTo(200);
				assertThat(resp.getEntity().getContentType().getValue()).isEqualTo("image/png");
				assertThat(resp.getEntity().getContentLength()).isEqualTo(SOME_BYTES.length);

				byte[] actualBytes = IOUtils.toByteArray(resp.getEntity().getContent());
				assertThat(actualBytes).containsExactly(SOME_BYTES);

			}

			verify(interceptor, times(1)).invoke(eq(Pointcut.STORAGE_PRESHOW_RESOURCES), any());
		} finally {
			myInterceptorRegistry.unregisterInterceptor(interceptor);
		}
	}


	@Test
	public void testReadSecondInstance() throws IOException {
		IIdType id = createDocumentReference(true);

		IAnonymousInterceptor interceptor = mock(IAnonymousInterceptor.class);
		myInterceptorRegistry.registerAnonymousInterceptor(Pointcut.STORAGE_PRESHOW_RESOURCES, interceptor);
		try {
			// Read it back using the operation
			String path = myServerBase +
				"/DocumentReference/" + id.getIdPart() + "/" +
				JpaConstants.OPERATION_BINARY_ACCESS_READ +
				"?path=DocumentReference.content[1].attachment";
			HttpGet get = new HttpGet(path);
			try (CloseableHttpResponse resp = ourHttpClient.execute(get)) {

				assertThat(resp.getStatusLine().getStatusCode()).isEqualTo(200);
				assertThat(resp.getEntity().getContentType().getValue()).isEqualTo("image/gif");
				assertThat(resp.getEntity().getContentLength()).isEqualTo(SOME_BYTES_2.length);

				byte[] actualBytes = IOUtils.toByteArray(resp.getEntity().getContent());
				assertThat(actualBytes).containsExactly(SOME_BYTES_2);
			}

			verify(interceptor, times(1)).invoke(eq(Pointcut.STORAGE_PRESHOW_RESOURCES), any());
		} finally {
			myInterceptorRegistry.unregisterInterceptor(interceptor);
		}
	}

	@Test
	public void testReadNoPath() throws IOException {
		IIdType id = createDocumentReference(true);

		String path = myServerBase +
			"/DocumentReference/" + id.getIdPart() + "/" +
			JpaConstants.OPERATION_BINARY_ACCESS_READ;
		HttpGet get = new HttpGet(path);
		try (CloseableHttpResponse resp = ourHttpClient.execute(get)) {

			assertThat(resp.getStatusLine().getStatusCode()).isEqualTo(400);
			String response = IOUtils.toString(resp.getEntity().getContent(), Charsets.UTF_8);
			assertThat(response).contains("No path specified");

		}

	}

	@Test
	public void testReadNoData() throws IOException {
		IIdType id = createDocumentReference(false);

		String path = myServerBase +
			"/DocumentReference/" + id.getIdPart() + "/" +
			JpaConstants.OPERATION_BINARY_ACCESS_READ +
			"?path=DocumentReference.content.attachment";

		HttpGet get = new HttpGet(path);
		try (CloseableHttpResponse resp = ourHttpClient.execute(get)) {

			assertThat(resp.getStatusLine().getStatusCode()).isEqualTo(400);
			String response = IOUtils.toString(resp.getEntity().getContent(), Charsets.UTF_8);
			assertThat(response).matches(".*The resource with ID DocumentReference/[0-9]+ has no data at path.*");

		}
	}


	@Test
	public void testManualResponseOperationsInvokeServerOutgoingResponsePointcut() throws IOException, InterruptedException {
		IIdType id = createDocumentReference(true);

		PointcutLatch latch = new PointcutLatch(Pointcut.SERVER_OUTGOING_RESPONSE);
		myServer.getRestfulServer().getInterceptorService().registerAnonymousInterceptor(Pointcut.SERVER_OUTGOING_RESPONSE, latch);


		String path = myServerBase +
			"/DocumentReference/" + id.getIdPart() + "/" +
			JpaConstants.OPERATION_BINARY_ACCESS_READ +
			"?path=DocumentReference.content.attachment";
		HttpGet get = new HttpGet(path);

		latch.setExpectedCount(1);
		try (CloseableHttpResponse resp = ourHttpClient.execute(get)) {
			assertThat(resp.getStatusLine().getStatusCode()).isEqualTo(200);
			List<HookParams> hookParams = latch.awaitExpected();

			RequestDetails requestDetails = PointcutLatch.getInvocationParameterOfType(hookParams, RequestDetails.class);
			ResponseDetails responseDetails= PointcutLatch.getInvocationParameterOfType(hookParams, ResponseDetails.class);

			assertNotNull(responseDetails);
			assertNotNull(requestDetails);

			assertThat(requestDetails.getId().toString()).isEqualTo(id.toString());
		}
	}
	@Test
	public void testReadUnknownBlobId() throws IOException {
		IIdType id = createDocumentReference(false);

		// Write a binary using the operation

		String path = myServerBase +
			"/DocumentReference/" + id.getIdPart() + "/" +
			JpaConstants.OPERATION_BINARY_ACCESS_WRITE +
			"?path=DocumentReference.content.attachment";
		HttpPost post = new HttpPost(path);
		post.setEntity(new ByteArrayEntity(SOME_BYTES, ContentType.IMAGE_JPEG));
		post.addHeader("Accept", "application/fhir+json; _pretty=true");
		String attachmentId;
		try (CloseableHttpResponse resp = ourHttpClient.execute(post)) {

			assertThat(resp.getStatusLine().getStatusCode()).isEqualTo(200);
			assertThat(resp.getEntity().getContentType().getValue()).contains("application/fhir+json");
			String response = IOUtils.toString(resp.getEntity().getContent(), Constants.CHARSET_UTF8);
			ourLog.info("Response: {}", response);

			DocumentReference ref = myFhirContext.newJsonParser().parseResource(DocumentReference.class, response);

			Attachment attachment = ref.getContentFirstRep().getAttachment();
			assertThat(attachment.getContentType()).isEqualTo(ContentType.IMAGE_JPEG.getMimeType());
			assertThat(attachment.getSize()).isEqualTo(15);
			assertNull(attachment.getData());
			assertThat(ref.getMeta().getVersionId()).isEqualTo("2");
			attachmentId = attachment.getDataElement().getExtensionString(HapiExtensions.EXT_EXTERNALIZED_BINARY_ID);
			assertThat(attachmentId).matches("[a-zA-Z0-9]{100}");
		}


		myBinaryStorageSvc.expungeBinaryContent(id, attachmentId);

		path = myServerBase +
			"/DocumentReference/" + id.getIdPart() + "/" +
			JpaConstants.OPERATION_BINARY_ACCESS_READ +
			"?path=DocumentReference.content.attachment";
		HttpGet get = new HttpGet(path);
		try (CloseableHttpResponse resp = ourHttpClient.execute(get)) {

			assertThat(resp.getStatusLine().getStatusCode()).isEqualTo(400);
			String response = IOUtils.toString(resp.getEntity().getContent(), Charsets.UTF_8);
			assertThat(response).matches(".*Can not find the requested binary content. It may have been deleted.*");

		}

	}

	/**
	 * Stores a binary large enough that it should live in binary storage
	 */
	@Test
	public void testWriteLargeAttachment() throws IOException {
		IIdType id = createDocumentReference(false);

		IAnonymousInterceptor interceptor = mock(IAnonymousInterceptor.class);
		myInterceptorRegistry.registerAnonymousInterceptor(Pointcut.STORAGE_PRESHOW_RESOURCES, interceptor);
		myInterceptorRegistry.registerAnonymousInterceptor(Pointcut.STORAGE_PRESTORAGE_RESOURCE_CREATED, interceptor);
		myInterceptorRegistry.registerAnonymousInterceptor(Pointcut.STORAGE_PRESTORAGE_RESOURCE_UPDATED, interceptor);
		try {

			// Write the binary using the operation

			String path = myServerBase +
				"/DocumentReference/" + id.getIdPart() + "/" +
				JpaConstants.OPERATION_BINARY_ACCESS_WRITE +
				"?path=DocumentReference.content.attachment";
			HttpPost post = new HttpPost(path);
			post.setEntity(new ByteArrayEntity(SOME_BYTES, ContentType.IMAGE_JPEG));
			post.addHeader("Accept", "application/fhir+json; _pretty=true");
			String attachmentId;
			try (CloseableHttpResponse resp = ourHttpClient.execute(post)) {

				assertThat(resp.getStatusLine().getStatusCode()).isEqualTo(200);
				assertThat(resp.getEntity().getContentType().getValue()).contains("application/fhir+json");
				String response = IOUtils.toString(resp.getEntity().getContent(), Constants.CHARSET_UTF8);
				ourLog.info("Response: {}", response);

				DocumentReference ref = myFhirContext.newJsonParser().parseResource(DocumentReference.class, response);

				Attachment attachment = ref.getContentFirstRep().getAttachment();
				assertThat(attachment.getContentType()).isEqualTo(ContentType.IMAGE_JPEG.getMimeType());
				assertThat(attachment.getSize()).isEqualTo(15);
				assertNull(attachment.getData());
				assertThat(ref.getMeta().getVersionId()).isEqualTo("2");
				attachmentId = attachment.getDataElement().getExtensionString(HapiExtensions.EXT_EXTERNALIZED_BINARY_ID);
				assertThat(attachmentId).matches("[a-zA-Z0-9]{100}");

			}

			verify(interceptor, timeout(5000).times(1)).invoke(eq(Pointcut.STORAGE_PRESHOW_RESOURCES), any());
			verify(interceptor, timeout(5000).times(1)).invoke(eq(Pointcut.STORAGE_PRESTORAGE_RESOURCE_UPDATED), any());
			verifyNoMoreInteractions(interceptor);

			// Read it back using the operation

			path = myServerBase +
				"/DocumentReference/" + id.getIdPart() + "/" +
				JpaConstants.OPERATION_BINARY_ACCESS_READ +
				"?path=DocumentReference.content.attachment";
			HttpGet get = new HttpGet(path);
			try (CloseableHttpResponse resp = ourHttpClient.execute(get)) {

				assertThat(resp.getStatusLine().getStatusCode()).isEqualTo(200);
				assertThat(resp.getEntity().getContentType().getValue()).isEqualTo("image/jpeg");
				assertThat(resp.getEntity().getContentLength()).isEqualTo(SOME_BYTES.length);

				byte[] actualBytes = IOUtils.toByteArray(resp.getEntity().getContent());
				assertThat(actualBytes).containsExactly(SOME_BYTES);
			}
		} finally {
			myInterceptorRegistry.unregisterInterceptor(interceptor);
		}
	}

	@Test
	public void testDontAllowUpdateWithAttachmentId_NoneExists() {

		DocumentReference dr = new DocumentReference();
		dr.addContent()
			.getAttachment()
			.getDataElement()
			.addExtension(HapiExtensions.EXT_EXTERNALIZED_BINARY_ID, new StringType("0000-1111") );

		try {
			myClient.create().resource(dr).execute();
			fail("");
		} catch (InvalidRequestException e) {
			assertThat(e.getMessage()).contains("Can not find the requested binary content. It may have been deleted.");
		}
	}

	/**
	 * Stores a binary small enough that it shouldn't live in binary storage
	 */
	@Test
	public void testWriteSmallAttachment() throws IOException {
		IIdType id = createDocumentReference(false);

		IAnonymousInterceptor interceptor = mock(IAnonymousInterceptor.class);
		myInterceptorRegistry.registerAnonymousInterceptor(Pointcut.STORAGE_PRESHOW_RESOURCES, interceptor);
		myInterceptorRegistry.registerAnonymousInterceptor(Pointcut.STORAGE_PRESTORAGE_RESOURCE_CREATED, interceptor);
		myInterceptorRegistry.registerAnonymousInterceptor(Pointcut.STORAGE_PRESTORAGE_RESOURCE_UPDATED, interceptor);
		try {

			// Read it back using the operation

			String path = myServerBase +
				"/DocumentReference/" + id.getIdPart() + "/" +
				JpaConstants.OPERATION_BINARY_ACCESS_WRITE +
				"?path=DocumentReference.content.attachment";
			HttpPost post = new HttpPost(path);
			post.setEntity(new ByteArrayEntity(SOME_BYTES_2, ContentType.IMAGE_JPEG));
			post.addHeader("Accept", "application/fhir+json; _pretty=true");
			String attachmentId;
			try (CloseableHttpResponse resp = ourHttpClient.execute(post)) {

				assertThat(resp.getStatusLine().getStatusCode()).isEqualTo(200);
				assertThat(resp.getEntity().getContentType().getValue()).contains("application/fhir+json");
				String response = IOUtils.toString(resp.getEntity().getContent(), Constants.CHARSET_UTF8);
				ourLog.info("Response: {}", response);

				DocumentReference ref = myFhirContext.newJsonParser().parseResource(DocumentReference.class, response);

				Attachment attachment = ref.getContentFirstRep().getAttachment();
				assertThat(attachment.getContentType()).isEqualTo(ContentType.IMAGE_JPEG.getMimeType());
				assertThat(attachment.getSize()).isEqualTo(4);
				assertThat(attachment.getData()).containsExactly(SOME_BYTES_2);
				assertThat(ref.getMeta().getVersionId()).isEqualTo("2");
				attachmentId = attachment.getExtensionString(HapiExtensions.EXT_EXTERNALIZED_BINARY_ID);
				assertNull(attachmentId);

			}

			verify(interceptor, timeout(5000).times(1)).invoke(eq(Pointcut.STORAGE_PRESHOW_RESOURCES), any());
			verify(interceptor, timeout(5000).times(1)).invoke(eq(Pointcut.STORAGE_PRESTORAGE_RESOURCE_UPDATED), any());
			verifyNoMoreInteractions(interceptor);
		} finally {
			myInterceptorRegistry.unregisterInterceptor(interceptor);
		}
	}

	/**
	 * Stores a binary large enough that it should live in binary storage
	 */
	@Test
	public void testWriteLargeBinaryUsingOperation() throws IOException {
		Binary binary = new Binary();
		binary.setContentType("image/png");

		ourLog.debug(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(binary));

		IIdType id = myClient.create().resource(binary).execute().getId().toUnqualifiedVersionless();

		IAnonymousInterceptor interceptor = mock(IAnonymousInterceptor.class);
		myInterceptorRegistry.registerAnonymousInterceptor(Pointcut.STORAGE_PRESHOW_RESOURCES, interceptor);
		myInterceptorRegistry.registerAnonymousInterceptor(Pointcut.STORAGE_PRESTORAGE_RESOURCE_CREATED, interceptor);
		myInterceptorRegistry.registerAnonymousInterceptor(Pointcut.STORAGE_PRESTORAGE_RESOURCE_UPDATED, interceptor);
		try {
			// Write using the operation

			String path = myServerBase +
				"/Binary/" + id.getIdPart() + "/" +
				JpaConstants.OPERATION_BINARY_ACCESS_WRITE +
				"?path=Binary";
			HttpPost post = new HttpPost(path);
			post.setEntity(new ByteArrayEntity(SOME_BYTES, ContentType.IMAGE_JPEG));
			post.addHeader("Accept", "application/fhir+json; _pretty=true");
			String attachmentId;
			try (CloseableHttpResponse resp = ourHttpClient.execute(post)) {

				assertThat(resp.getStatusLine().getStatusCode()).isEqualTo(200);
				assertThat(resp.getEntity().getContentType().getValue()).contains("application/fhir+json");
				String response = IOUtils.toString(resp.getEntity().getContent(), Constants.CHARSET_UTF8);
				ourLog.info("Response: {}", response);

				Binary target = myFhirContext.newJsonParser().parseResource(Binary.class, response);

				assertThat(target.getContentType()).isEqualTo(ContentType.IMAGE_JPEG.getMimeType());
				assertNull(target.getData());
				assertThat(target.getMeta().getVersionId()).isEqualTo("2");
				attachmentId = target.getDataElement().getExtensionString(HapiExtensions.EXT_EXTERNALIZED_BINARY_ID);
				assertThat(attachmentId).matches("[a-zA-Z0-9]{100}");

			}

			verify(interceptor, timeout(5000).times(1)).invoke(eq(Pointcut.STORAGE_PRESHOW_RESOURCES), any());
			verify(interceptor, timeout(5000).times(1)).invoke(eq(Pointcut.STORAGE_PRESTORAGE_RESOURCE_UPDATED), any());
			verifyNoMoreInteractions(interceptor);

			// Read it back using the operation

			path = myServerBase +
				"/Binary/" + id.getIdPart() + "/" +
				JpaConstants.OPERATION_BINARY_ACCESS_READ +
				"?path=Binary";
			HttpGet get = new HttpGet(path);
			try (CloseableHttpResponse resp = ourHttpClient.execute(get)) {

				assertThat(resp.getStatusLine().getStatusCode()).isEqualTo(200);
				assertThat(resp.getEntity().getContentType().getValue()).isEqualTo("image/jpeg");
				assertThat(resp.getEntity().getContentLength()).isEqualTo(SOME_BYTES.length);

				byte[] actualBytes = IOUtils.toByteArray(resp.getEntity().getContent());
				assertThat(actualBytes).containsExactly(SOME_BYTES);
			}
		} finally {
			myInterceptorRegistry.unregisterInterceptor(interceptor);
		}
	}



	/**
	 * Stores a binary large enough that it should live in binary storage
	 */
	@Test
	public void testWriteLargeBinaryWithoutExplicitPath() throws IOException {
		Binary binary = new Binary();
		binary.setContentType("image/png");

		ourLog.debug(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(binary));

		IIdType id = myClient.create().resource(binary).execute().getId().toUnqualifiedVersionless();

		// Write using the operation

		String path = myServerBase +
			"/Binary/" + id.getIdPart() + "/" +
			JpaConstants.OPERATION_BINARY_ACCESS_WRITE;
		HttpPost post = new HttpPost(path);
		post.setEntity(new ByteArrayEntity(SOME_BYTES, ContentType.IMAGE_JPEG));
		post.addHeader("Accept", "application/fhir+json; _pretty=true");
		String attachmentId;
		try (CloseableHttpResponse resp = ourHttpClient.execute(post)) {

			assertThat(resp.getStatusLine().getStatusCode()).isEqualTo(200);
			assertThat(resp.getEntity().getContentType().getValue()).contains("application/fhir+json");
			String response = IOUtils.toString(resp.getEntity().getContent(), Constants.CHARSET_UTF8);
			ourLog.info("Response: {}", response);

			Binary target = myFhirContext.newJsonParser().parseResource(Binary.class, response);

			assertThat(target.getContentType()).isEqualTo(ContentType.IMAGE_JPEG.getMimeType());
			assertNull(target.getData());
			assertThat(target.getMeta().getVersionId()).isEqualTo("2");
			attachmentId = target.getDataElement().getExtensionString(HapiExtensions.EXT_EXTERNALIZED_BINARY_ID);
			assertThat(attachmentId).matches("[a-zA-Z0-9]{100}");

		}

		// Read it back using the operation

		path = myServerBase +
			"/Binary/" + id.getIdPart() + "/" +
			JpaConstants.OPERATION_BINARY_ACCESS_READ;
		HttpGet get = new HttpGet(path);
		try (CloseableHttpResponse resp = ourHttpClient.execute(get)) {

			assertThat(resp.getStatusLine().getStatusCode()).isEqualTo(200);
			assertThat(resp.getEntity().getContentType().getValue()).isEqualTo("image/jpeg");
			assertThat(resp.getEntity().getContentLength()).isEqualTo(SOME_BYTES.length);

			byte[] actualBytes = IOUtils.toByteArray(resp.getEntity().getContent());
			assertThat(actualBytes).containsExactly(SOME_BYTES);
		}

	}


	static class BinaryBlobIdInterceptor {
		@Hook(Pointcut.STORAGE_BINARY_ASSIGN_BLOB_ID_PREFIX)
		public String provideBlobIdForBinary(RequestDetails theRequestDetails, IBaseResource theResource) {
			ourLog.info("Received binary for prefixing!");
			return "test-blob-id-prefix";
		}
	}

	@Test
	public void testWriteLargeBinaryToDocumentReference_callsBlobIdPrefixHook() throws IOException {
		byte[] bytes = new byte[1234];
		for (int i = 0; i < bytes.length; i++) {
			bytes[i] = (byte) (((float)Byte.MAX_VALUE) * Math.random());
		}

		DocumentReference dr = new DocumentReference();
		dr.addContent().getAttachment()
			.setContentType("application/pdf")
			.setSize(12345)
			.setTitle("hello")
			.setCreationElement(new DateTimeType("2002"));
		IIdType id = myClient.create().resource(dr).execute().getId().toUnqualifiedVersionless();

		BinaryBlobIdInterceptor interceptor = spy(new BinaryBlobIdInterceptor());
		myInterceptorRegistry.registerInterceptor(interceptor);

		try {
			// Write using the operation

			String path = myServerBase +
				"/DocumentReference/" + id.getIdPart() + "/" +
				JpaConstants.OPERATION_BINARY_ACCESS_WRITE +
				"?path=DocumentReference.content.attachment";
			HttpPost post = new HttpPost(path);
			post.setEntity(new ByteArrayEntity(bytes, ContentType.IMAGE_JPEG));
			post.addHeader("Accept", "application/fhir+json; _pretty=true");
			String attachmentId;
			try (CloseableHttpResponse resp = ourHttpClient.execute(post)) {
				assertThat(resp.getStatusLine().getStatusCode()).isEqualTo(200);
				assertThat(resp.getEntity().getContentType().getValue()).contains("application/fhir+json");

				String response = IOUtils.toString(resp.getEntity().getContent(), Constants.CHARSET_UTF8);
				ourLog.info("Response: {}", response);

				DocumentReference target = myFhirContext.newJsonParser().parseResource(DocumentReference.class, response);

				assertNull(target.getContentFirstRep().getAttachment().getData());
				assertThat(target.getMeta().getVersionId()).isEqualTo("2");
				attachmentId = target.getContentFirstRep().getAttachment().getDataElement().getExtensionString(HapiExtensions.EXT_EXTERNALIZED_BINARY_ID);
				assertThat(attachmentId).startsWith("test-blob-id-prefix");
			}

			verify(interceptor, timeout(5_000).times(1)).provideBlobIdForBinary(any(), any());
			verifyNoMoreInteractions(interceptor);

		} finally {
			myInterceptorRegistry.unregisterInterceptor(interceptor);
		}
	}

	@Test
	public void testWriteLargeBinaryToDocumentReference() throws IOException {
		byte[] bytes = new byte[134696];
		for (int i = 0; i < bytes.length; i++) {
			bytes[i] = (byte) (((float)Byte.MAX_VALUE) * Math.random());
		}

		DocumentReference dr = new DocumentReference();
		dr.addContent().getAttachment()
			.setContentType("application/pdf")
			.setSize(12345)
			.setTitle("hello")
			.setCreationElement(new DateTimeType("2002"));
		IIdType id = myClient.create().resource(dr).execute().getId().toUnqualifiedVersionless();

		IAnonymousInterceptor interceptor = mock(IAnonymousInterceptor.class);
		myInterceptorRegistry.registerAnonymousInterceptor(Pointcut.STORAGE_PRESHOW_RESOURCES, interceptor);
		myInterceptorRegistry.registerAnonymousInterceptor(Pointcut.STORAGE_PRESTORAGE_RESOURCE_CREATED, interceptor);
		myInterceptorRegistry.registerAnonymousInterceptor(Pointcut.STORAGE_PRESTORAGE_RESOURCE_UPDATED, interceptor);
		try {
			// Write using the operation

			String path = myServerBase +
				"/DocumentReference/" + id.getIdPart() + "/" +
				JpaConstants.OPERATION_BINARY_ACCESS_WRITE +
				"?path=DocumentReference.content.attachment";
			HttpPost post = new HttpPost(path);
			post.setEntity(new ByteArrayEntity(bytes, ContentType.IMAGE_JPEG));
			post.addHeader("Accept", "application/fhir+json; _pretty=true");
			String attachmentId;
			try (CloseableHttpResponse resp = ourHttpClient.execute(post)) {
				assertThat(resp.getStatusLine().getStatusCode()).isEqualTo(200);
				assertThat(resp.getEntity().getContentType().getValue()).contains("application/fhir+json");

				String response = IOUtils.toString(resp.getEntity().getContent(), Constants.CHARSET_UTF8);
				ourLog.info("Response: {}", response);

				DocumentReference target = myFhirContext.newJsonParser().parseResource(DocumentReference.class, response);

				assertNull(target.getContentFirstRep().getAttachment().getData());
				assertThat(target.getMeta().getVersionId()).isEqualTo("2");
				attachmentId = target.getContentFirstRep().getAttachment().getDataElement().getExtensionString(HapiExtensions.EXT_EXTERNALIZED_BINARY_ID);
				assertThat(attachmentId).matches("[a-zA-Z0-9]{100}");

			}

			verify(interceptor, timeout(5000).times(1)).invoke(eq(Pointcut.STORAGE_PRESHOW_RESOURCES), any());
			verify(interceptor, timeout(5000).times(1)).invoke(eq(Pointcut.STORAGE_PRESTORAGE_RESOURCE_UPDATED), any());
			verifyNoMoreInteractions(interceptor);

			// Read it back using the operation

			path = myServerBase +
				"/DocumentReference/" + id.getIdPart() + "/" +
				JpaConstants.OPERATION_BINARY_ACCESS_READ +
				"?path=DocumentReference.content.attachment";
			HttpGet get = new HttpGet(path);
			try (CloseableHttpResponse resp = ourHttpClient.execute(get)) {

				assertThat(resp.getStatusLine().getStatusCode()).isEqualTo(200);
				assertThat(resp.getEntity().getContentType().getValue()).isEqualTo("image/jpeg");
				assertThat(resp.getEntity().getContentLength()).isEqualTo(bytes.length);

				byte[] actualBytes = IOUtils.toByteArray(resp.getEntity().getContent());
				assertThat(actualBytes).containsExactly(bytes);
			}
		} finally {
			myInterceptorRegistry.unregisterInterceptor(interceptor);
		}
	}


	private IIdType createDocumentReference(boolean theSetData) {
		DocumentReference documentReference = new DocumentReference();
		Attachment attachment = documentReference
			.addContent()
			.getAttachment()
			.setContentType("image/png");
		if (theSetData) {
			attachment.setData(SOME_BYTES);
		}
		attachment = documentReference
			.addContent()
			.getAttachment()
			.setContentType("image/gif");
		if (theSetData) {
			attachment.setData(SOME_BYTES_2);
		}

		ourLog.debug(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(documentReference));

		return myClient.create().resource(documentReference).execute().getId().toUnqualifiedVersionless();
	}


	@Test
	public void testResourceExpungeAlsoExpungesBinaryData() throws IOException {
		IIdType id = createDocumentReference(false);

		String path = myServerBase +
			"/DocumentReference/" + id.getIdPart() + "/" +
			JpaConstants.OPERATION_BINARY_ACCESS_WRITE +
			"?path=DocumentReference.content.attachment";
		HttpPost post = new HttpPost(path);
		post.setEntity(new ByteArrayEntity(SOME_BYTES, ContentType.IMAGE_JPEG));
		post.addHeader("Accept", "application/fhir+json; _pretty=true");
		String attachmentId;
		try (CloseableHttpResponse resp = ourHttpClient.execute(post)) {
			assertThat(resp.getStatusLine().getStatusCode()).isEqualTo(200);
			assertThat(resp.getEntity().getContentType().getValue()).contains("application/fhir+json");
			String response = IOUtils.toString(resp.getEntity().getContent(), Constants.CHARSET_UTF8);
			DocumentReference ref = myFhirContext.newJsonParser().parseResource(DocumentReference.class, response);
			Attachment attachment = ref.getContentFirstRep().getAttachment();
			attachmentId = attachment.getDataElement().getExtensionString(HapiExtensions.EXT_EXTERNALIZED_BINARY_ID);
			assertThat(attachmentId).matches("[a-zA-Z0-9]{100}");
		}

		ByteArrayOutputStream capture = new ByteArrayOutputStream();
		myStorageSvc.writeBinaryContent(id, attachmentId, capture);
		assertEquals(15, capture.size());

		// Now delete (logical delete- should not expunge the binary)
		myClient.delete().resourceById(id).execute();
		try {
			myClient.read().resource("DocumentReference").withId(id).execute();
			fail("");
		} catch (ResourceGoneException e) {
			// good
		}

		capture = new ByteArrayOutputStream();
		myStorageSvc.writeBinaryContent(id, attachmentId, capture);
		assertEquals(15, capture.size());

		// Now expunge
		Parameters parameters = new Parameters();
		parameters.addParameter().setName(ProviderConstants.OPERATION_EXPUNGE_PARAM_EXPUNGE_DELETED_RESOURCES).setValue(new BooleanType(true));
		myClient
			.operation()
			.onInstance(id)
			.named(ProviderConstants.OPERATION_EXPUNGE)
			.withParameters(parameters)
			.execute();

		capture = new ByteArrayOutputStream();
		assertFalse(myStorageSvc.writeBinaryContent(id, attachmentId, capture));
		assertEquals(0, capture.size());

	}



	@Test
	public void testWriteWithConflictInterceptor() throws IOException {
		UserRequestRetryVersionConflictsInterceptor interceptor = new UserRequestRetryVersionConflictsInterceptor();
		myServer.getRestfulServer().registerInterceptor(interceptor);
		try {

			IIdType id = createDocumentReference(false);

			String path = myServerBase +
				"/DocumentReference/" + id.getIdPart() + "/" +
				JpaConstants.OPERATION_BINARY_ACCESS_WRITE +
				"?path=DocumentReference.content.attachment";
			HttpPost post = new HttpPost(path);
			post.setEntity(new ByteArrayEntity(SOME_BYTES, ContentType.IMAGE_JPEG));
			post.addHeader("Accept", "application/fhir+json; _pretty=true");
			String attachmentId;
			try (CloseableHttpResponse resp = ourHttpClient.execute(post)) {
				String response = IOUtils.toString(resp.getEntity().getContent(), Constants.CHARSET_UTF8);
				ourLog.info("Response: {}\n{}", resp, response);

				assertThat(resp.getStatusLine().getStatusCode()).isEqualTo(200);
				assertThat(resp.getEntity().getContentType().getValue()).contains("application/fhir+json");
				DocumentReference ref = myFhirContext.newJsonParser().parseResource(DocumentReference.class, response);
				Attachment attachment = ref.getContentFirstRep().getAttachment();
				attachmentId = attachment.getDataElement().getExtensionString(HapiExtensions.EXT_EXTERNALIZED_BINARY_ID);
				assertThat(attachmentId).matches("[a-zA-Z0-9]{100}");
			}

		} finally {
			myServer.getRestfulServer().unregisterInterceptor(interceptor);
		}
	}


}
