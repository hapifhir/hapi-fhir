package ca.uhn.fhir.jpa.provider.r4;

import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IAnonymousInterceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.binary.api.IBinaryStorageSvc;
import ca.uhn.fhir.jpa.binstore.MemoryBinaryStorageSvcImpl;
import ca.uhn.fhir.jpa.interceptor.UserRequestRetryVersionConflictsInterceptor;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.matchesPattern;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
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
		myDaoConfig.setExpungeEnabled(true);
		myInterceptorRegistry.registerInterceptor(myBinaryStorageInterceptor);
	}

	@Override
	@AfterEach
	public void after() throws Exception {
		super.after();
		myStorageSvc.setMinimumBinarySize(0);
		myDaoConfig.setExpungeEnabled(new DaoConfig().isExpungeEnabled());
	}

	@Test
	public void testRead() throws IOException, InterruptedException {
		IIdType id = createDocumentReference(true);

		IAnonymousInterceptor interceptor = mock(IAnonymousInterceptor.class);
		myInterceptorRegistry.registerAnonymousInterceptor(Pointcut.STORAGE_PRESHOW_RESOURCES, interceptor);

		doAnswer(t -> {
			Pointcut pointcut = t.getArgument(0, Pointcut.class);
			HookParams params = t.getArgument(1, HookParams.class);
			ourLog.info("Interceptor invoked with pointcut {} and params {}", pointcut, params);
			return null;
		}).when(interceptor).invoke(any(), any());


		// Read it back using the operation

		String path = ourServerBase +
			"/DocumentReference/" + id.getIdPart() + "/" +
			JpaConstants.OPERATION_BINARY_ACCESS_READ +
			"?path=DocumentReference.content.attachment";
		HttpGet get = new HttpGet(path);

		try (CloseableHttpResponse resp = ourHttpClient.execute(get)) {
			assertEquals(200, resp.getStatusLine().getStatusCode());
			assertEquals("image/png", resp.getEntity().getContentType().getValue());
			assertEquals(SOME_BYTES.length, resp.getEntity().getContentLength());

			byte[] actualBytes = IOUtils.toByteArray(resp.getEntity().getContent());
			assertArrayEquals(SOME_BYTES, actualBytes);

		}

		verify(interceptor, times(1)).invoke(eq(Pointcut.STORAGE_PRESHOW_RESOURCES), any());
	}


	@Test
	public void testReadSecondInstance() throws IOException {
		IIdType id = createDocumentReference(true);

		IAnonymousInterceptor interceptor = mock(IAnonymousInterceptor.class);
		myInterceptorRegistry.registerAnonymousInterceptor(Pointcut.STORAGE_PRESHOW_RESOURCES, interceptor);

		// Read it back using the operation
		String path = ourServerBase +
			"/DocumentReference/" + id.getIdPart() + "/" +
			JpaConstants.OPERATION_BINARY_ACCESS_READ +
			"?path=DocumentReference.content[1].attachment";
		HttpGet get = new HttpGet(path);
		try (CloseableHttpResponse resp = ourHttpClient.execute(get)) {

			assertEquals(200, resp.getStatusLine().getStatusCode());
			assertEquals("image/gif", resp.getEntity().getContentType().getValue());
			assertEquals(SOME_BYTES_2.length, resp.getEntity().getContentLength());

			byte[] actualBytes = IOUtils.toByteArray(resp.getEntity().getContent());
			assertArrayEquals(SOME_BYTES_2, actualBytes);
		}

		verify(interceptor, times(1)).invoke(eq(Pointcut.STORAGE_PRESHOW_RESOURCES), any());

	}

	@Test
	public void testReadNoPath() throws IOException {
		IIdType id = createDocumentReference(true);

		String path = ourServerBase +
			"/DocumentReference/" + id.getIdPart() + "/" +
			JpaConstants.OPERATION_BINARY_ACCESS_READ;
		HttpGet get = new HttpGet(path);
		try (CloseableHttpResponse resp = ourHttpClient.execute(get)) {

			assertEquals(400, resp.getStatusLine().getStatusCode());
			String response = IOUtils.toString(resp.getEntity().getContent(), Charsets.UTF_8);
			assertThat(response, containsString("No path specified"));

		}

	}

	@Test
	public void testReadNoData() throws IOException {
		IIdType id = createDocumentReference(false);

		String path = ourServerBase +
			"/DocumentReference/" + id.getIdPart() + "/" +
			JpaConstants.OPERATION_BINARY_ACCESS_READ +
			"?path=DocumentReference.content.attachment";

		HttpGet get = new HttpGet(path);
		try (CloseableHttpResponse resp = ourHttpClient.execute(get)) {

			assertEquals(400, resp.getStatusLine().getStatusCode());
			String response = IOUtils.toString(resp.getEntity().getContent(), Charsets.UTF_8);
			assertThat(response, matchesPattern(".*The resource with ID DocumentReference/[0-9]+ has no data at path.*"));

		}
	}


	@Test
	public void testManualResponseOperationsInvokeServerOutgoingResponsePointcut() throws IOException, InterruptedException {
		IIdType id = createDocumentReference(true);

		PointcutLatch latch = new PointcutLatch(Pointcut.SERVER_OUTGOING_RESPONSE);
		ourRestServer.getInterceptorService().registerAnonymousInterceptor(Pointcut.SERVER_OUTGOING_RESPONSE, latch);


		String path = ourServerBase +
			"/DocumentReference/" + id.getIdPart() + "/" +
			JpaConstants.OPERATION_BINARY_ACCESS_READ +
			"?path=DocumentReference.content.attachment";
		HttpGet get = new HttpGet(path);

		latch.setExpectedCount(1);
		try (CloseableHttpResponse resp = ourHttpClient.execute(get)) {
			assertEquals(200, resp.getStatusLine().getStatusCode());
			latch.awaitExpected();

			RequestDetails requestDetails = latch.getLatchInvocationParameterOfType(RequestDetails.class);
			ResponseDetails responseDetails= latch.getLatchInvocationParameterOfType(ResponseDetails.class);

			assertThat(responseDetails, is(notNullValue()));
			assertThat(requestDetails, is(notNullValue()));

			assertThat(requestDetails.getId().toString(), is(equalTo(id.toString())));
		}
	}
	@Test
	public void testReadUnknownBlobId() throws IOException {
		IIdType id = createDocumentReference(false);

		// Write a binary using the operation

		String path = ourServerBase +
			"/DocumentReference/" + id.getIdPart() + "/" +
			JpaConstants.OPERATION_BINARY_ACCESS_WRITE +
			"?path=DocumentReference.content.attachment";
		HttpPost post = new HttpPost(path);
		post.setEntity(new ByteArrayEntity(SOME_BYTES, ContentType.IMAGE_JPEG));
		post.addHeader("Accept", "application/fhir+json; _pretty=true");
		String attachmentId;
		try (CloseableHttpResponse resp = ourHttpClient.execute(post)) {

			assertEquals(200, resp.getStatusLine().getStatusCode());
			assertThat(resp.getEntity().getContentType().getValue(), containsString("application/fhir+json"));
			String response = IOUtils.toString(resp.getEntity().getContent(), Constants.CHARSET_UTF8);
			ourLog.info("Response: {}", response);

			DocumentReference ref = myFhirContext.newJsonParser().parseResource(DocumentReference.class, response);

			Attachment attachment = ref.getContentFirstRep().getAttachment();
			assertEquals(ContentType.IMAGE_JPEG.getMimeType(), attachment.getContentType());
			assertEquals(15, attachment.getSize());
			assertEquals(null, attachment.getData());
			assertEquals("2", ref.getMeta().getVersionId());
			attachmentId = attachment.getDataElement().getExtensionString(HapiExtensions.EXT_EXTERNALIZED_BINARY_ID);
			assertThat(attachmentId, matchesPattern("[a-zA-Z0-9]{100}"));
		}


		myBinaryStorageSvc.expungeBlob(id, attachmentId);

		path = ourServerBase +
			"/DocumentReference/" + id.getIdPart() + "/" +
			JpaConstants.OPERATION_BINARY_ACCESS_READ +
			"?path=DocumentReference.content.attachment";
		HttpGet get = new HttpGet(path);
		try (CloseableHttpResponse resp = ourHttpClient.execute(get)) {

			assertEquals(400, resp.getStatusLine().getStatusCode());
			String response = IOUtils.toString(resp.getEntity().getContent(), Charsets.UTF_8);
			assertThat(response, matchesPattern(".*Can not find the requested binary content. It may have been deleted.*"));

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

		// Write the binary using the operation

		String path = ourServerBase +
			"/DocumentReference/" + id.getIdPart() + "/" +
			JpaConstants.OPERATION_BINARY_ACCESS_WRITE +
			"?path=DocumentReference.content.attachment";
		HttpPost post = new HttpPost(path);
		post.setEntity(new ByteArrayEntity(SOME_BYTES, ContentType.IMAGE_JPEG));
		post.addHeader("Accept", "application/fhir+json; _pretty=true");
		String attachmentId;
		try (CloseableHttpResponse resp = ourHttpClient.execute(post)) {

			assertEquals(200, resp.getStatusLine().getStatusCode());
			assertThat(resp.getEntity().getContentType().getValue(), containsString("application/fhir+json"));
			String response = IOUtils.toString(resp.getEntity().getContent(), Constants.CHARSET_UTF8);
			ourLog.info("Response: {}", response);

			DocumentReference ref = myFhirContext.newJsonParser().parseResource(DocumentReference.class, response);

			Attachment attachment = ref.getContentFirstRep().getAttachment();
			assertEquals(ContentType.IMAGE_JPEG.getMimeType(), attachment.getContentType());
			assertEquals(15, attachment.getSize());
			assertEquals(null, attachment.getData());
			assertEquals("2", ref.getMeta().getVersionId());
			attachmentId = attachment.getDataElement().getExtensionString(HapiExtensions.EXT_EXTERNALIZED_BINARY_ID);
			assertThat(attachmentId, matchesPattern("[a-zA-Z0-9]{100}"));

		}

		verify(interceptor, timeout(5000).times(1)).invoke(eq(Pointcut.STORAGE_PRESHOW_RESOURCES), any());
		verify(interceptor, timeout(5000).times(1)).invoke(eq(Pointcut.STORAGE_PRESTORAGE_RESOURCE_UPDATED), any());
		verifyNoMoreInteractions(interceptor);

		// Read it back using the operation

		path = ourServerBase +
			"/DocumentReference/" + id.getIdPart() + "/" +
			JpaConstants.OPERATION_BINARY_ACCESS_READ +
			"?path=DocumentReference.content.attachment";
		HttpGet get = new HttpGet(path);
		try (CloseableHttpResponse resp = ourHttpClient.execute(get)) {

			assertEquals(200, resp.getStatusLine().getStatusCode());
			assertEquals("image/jpeg", resp.getEntity().getContentType().getValue());
			assertEquals(SOME_BYTES.length, resp.getEntity().getContentLength());

			byte[] actualBytes = IOUtils.toByteArray(resp.getEntity().getContent());
			assertArrayEquals(SOME_BYTES, actualBytes);
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
			fail();
		} catch (InvalidRequestException e) {
			assertThat(e.getMessage(), containsString("Can not find the requested binary content. It may have been deleted."));
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

		// Read it back using the operation

		String path = ourServerBase +
			"/DocumentReference/" + id.getIdPart() + "/" +
			JpaConstants.OPERATION_BINARY_ACCESS_WRITE +
			"?path=DocumentReference.content.attachment";
		HttpPost post = new HttpPost(path);
		post.setEntity(new ByteArrayEntity(SOME_BYTES_2, ContentType.IMAGE_JPEG));
		post.addHeader("Accept", "application/fhir+json; _pretty=true");
		String attachmentId;
		try (CloseableHttpResponse resp = ourHttpClient.execute(post)) {

			assertEquals(200, resp.getStatusLine().getStatusCode());
			assertThat(resp.getEntity().getContentType().getValue(), containsString("application/fhir+json"));
			String response = IOUtils.toString(resp.getEntity().getContent(), Constants.CHARSET_UTF8);
			ourLog.info("Response: {}", response);

			DocumentReference ref = myFhirContext.newJsonParser().parseResource(DocumentReference.class, response);

			Attachment attachment = ref.getContentFirstRep().getAttachment();
			assertEquals(ContentType.IMAGE_JPEG.getMimeType(), attachment.getContentType());
			assertEquals(4, attachment.getSize());
			assertArrayEquals(SOME_BYTES_2, attachment.getData());
			assertEquals("2", ref.getMeta().getVersionId());
			attachmentId = attachment.getExtensionString(HapiExtensions.EXT_EXTERNALIZED_BINARY_ID);
			assertEquals(null, attachmentId);

		}

		verify(interceptor, timeout(5000).times(1)).invoke(eq(Pointcut.STORAGE_PRESHOW_RESOURCES), any());
		verify(interceptor, timeout(5000).times(1)).invoke(eq(Pointcut.STORAGE_PRESTORAGE_RESOURCE_UPDATED), any());
		verifyNoMoreInteractions(interceptor);

	}

	/**
	 * Stores a binary large enough that it should live in binary storage
	 */
	@Test
	public void testWriteLargeBinaryUsingOperation() throws IOException {
		Binary binary = new Binary();
		binary.setContentType("image/png");

		ourLog.info(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(binary));

		IIdType id = myClient.create().resource(binary).execute().getId().toUnqualifiedVersionless();

		IAnonymousInterceptor interceptor = mock(IAnonymousInterceptor.class);
		myInterceptorRegistry.registerAnonymousInterceptor(Pointcut.STORAGE_PRESHOW_RESOURCES, interceptor);
		myInterceptorRegistry.registerAnonymousInterceptor(Pointcut.STORAGE_PRESTORAGE_RESOURCE_CREATED, interceptor);
		myInterceptorRegistry.registerAnonymousInterceptor(Pointcut.STORAGE_PRESTORAGE_RESOURCE_UPDATED, interceptor);

		// Write using the operation

		String path = ourServerBase +
			"/Binary/" + id.getIdPart() + "/" +
			JpaConstants.OPERATION_BINARY_ACCESS_WRITE +
			"?path=Binary";
		HttpPost post = new HttpPost(path);
		post.setEntity(new ByteArrayEntity(SOME_BYTES, ContentType.IMAGE_JPEG));
		post.addHeader("Accept", "application/fhir+json; _pretty=true");
		String attachmentId;
		try (CloseableHttpResponse resp = ourHttpClient.execute(post)) {

			assertEquals(200, resp.getStatusLine().getStatusCode());
			assertThat(resp.getEntity().getContentType().getValue(), containsString("application/fhir+json"));
			String response = IOUtils.toString(resp.getEntity().getContent(), Constants.CHARSET_UTF8);
			ourLog.info("Response: {}", response);

			Binary target = myFhirContext.newJsonParser().parseResource(Binary.class, response);

			assertEquals(ContentType.IMAGE_JPEG.getMimeType(), target.getContentType());
			assertEquals(null, target.getData());
			assertEquals("2", target.getMeta().getVersionId());
			attachmentId = target.getDataElement().getExtensionString(HapiExtensions.EXT_EXTERNALIZED_BINARY_ID);
			assertThat(attachmentId, matchesPattern("[a-zA-Z0-9]{100}"));

		}

		verify(interceptor, timeout(5000).times(1)).invoke(eq(Pointcut.STORAGE_PRESHOW_RESOURCES), any());
		verify(interceptor, timeout(5000).times(1)).invoke(eq(Pointcut.STORAGE_PRESTORAGE_RESOURCE_UPDATED), any());
		verifyNoMoreInteractions(interceptor);

		// Read it back using the operation

		path = ourServerBase +
			"/Binary/" + id.getIdPart() + "/" +
			JpaConstants.OPERATION_BINARY_ACCESS_READ +
			"?path=Binary";
		HttpGet get = new HttpGet(path);
		try (CloseableHttpResponse resp = ourHttpClient.execute(get)) {

			assertEquals(200, resp.getStatusLine().getStatusCode());
			assertEquals("image/jpeg", resp.getEntity().getContentType().getValue());
			assertEquals(SOME_BYTES.length, resp.getEntity().getContentLength());

			byte[] actualBytes = IOUtils.toByteArray(resp.getEntity().getContent());
			assertArrayEquals(SOME_BYTES, actualBytes);
		}

	}



	/**
	 * Stores a binary large enough that it should live in binary storage
	 */
	@Test
	public void testWriteLargeBinaryWithoutExplicitPath() throws IOException {
		Binary binary = new Binary();
		binary.setContentType("image/png");

		ourLog.info(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(binary));

		IIdType id = myClient.create().resource(binary).execute().getId().toUnqualifiedVersionless();

		// Write using the operation

		String path = ourServerBase +
			"/Binary/" + id.getIdPart() + "/" +
			JpaConstants.OPERATION_BINARY_ACCESS_WRITE;
		HttpPost post = new HttpPost(path);
		post.setEntity(new ByteArrayEntity(SOME_BYTES, ContentType.IMAGE_JPEG));
		post.addHeader("Accept", "application/fhir+json; _pretty=true");
		String attachmentId;
		try (CloseableHttpResponse resp = ourHttpClient.execute(post)) {

			assertEquals(200, resp.getStatusLine().getStatusCode());
			assertThat(resp.getEntity().getContentType().getValue(), containsString("application/fhir+json"));
			String response = IOUtils.toString(resp.getEntity().getContent(), Constants.CHARSET_UTF8);
			ourLog.info("Response: {}", response);

			Binary target = myFhirContext.newJsonParser().parseResource(Binary.class, response);

			assertEquals(ContentType.IMAGE_JPEG.getMimeType(), target.getContentType());
			assertEquals(null, target.getData());
			assertEquals("2", target.getMeta().getVersionId());
			attachmentId = target.getDataElement().getExtensionString(HapiExtensions.EXT_EXTERNALIZED_BINARY_ID);
			assertThat(attachmentId, matchesPattern("[a-zA-Z0-9]{100}"));

		}

		// Read it back using the operation

		path = ourServerBase +
			"/Binary/" + id.getIdPart() + "/" +
			JpaConstants.OPERATION_BINARY_ACCESS_READ;
		HttpGet get = new HttpGet(path);
		try (CloseableHttpResponse resp = ourHttpClient.execute(get)) {

			assertEquals(200, resp.getStatusLine().getStatusCode());
			assertEquals("image/jpeg", resp.getEntity().getContentType().getValue());
			assertEquals(SOME_BYTES.length, resp.getEntity().getContentLength());

			byte[] actualBytes = IOUtils.toByteArray(resp.getEntity().getContent());
			assertArrayEquals(SOME_BYTES, actualBytes);
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

		// Write using the operation

		String path = ourServerBase +
			"/DocumentReference/" + id.getIdPart() + "/" +
			JpaConstants.OPERATION_BINARY_ACCESS_WRITE +
			"?path=DocumentReference.content.attachment";
		HttpPost post = new HttpPost(path);
		post.setEntity(new ByteArrayEntity(bytes, ContentType.IMAGE_JPEG));
		post.addHeader("Accept", "application/fhir+json; _pretty=true");
		String attachmentId;
		try (CloseableHttpResponse resp = ourHttpClient.execute(post)) {
			assertEquals(200, resp.getStatusLine().getStatusCode());
			assertThat(resp.getEntity().getContentType().getValue(), containsString("application/fhir+json"));

			String response = IOUtils.toString(resp.getEntity().getContent(), Constants.CHARSET_UTF8);
			ourLog.info("Response: {}", response);

			DocumentReference target = myFhirContext.newJsonParser().parseResource(DocumentReference.class, response);

			assertEquals(null, target.getContentFirstRep().getAttachment().getData());
			assertEquals("2", target.getMeta().getVersionId());
			attachmentId = target.getContentFirstRep().getAttachment().getDataElement().getExtensionString(HapiExtensions.EXT_EXTERNALIZED_BINARY_ID);
			assertThat(attachmentId, matchesPattern("[a-zA-Z0-9]{100}"));

		}

		verify(interceptor, timeout(5000).times(1)).invoke(eq(Pointcut.STORAGE_PRESHOW_RESOURCES), any());
		verify(interceptor, timeout(5000).times(1)).invoke(eq(Pointcut.STORAGE_PRESTORAGE_RESOURCE_UPDATED), any());
		verifyNoMoreInteractions(interceptor);

		// Read it back using the operation

		path = ourServerBase +
			"/DocumentReference/" + id.getIdPart() + "/" +
			JpaConstants.OPERATION_BINARY_ACCESS_READ +
			"?path=DocumentReference.content.attachment";
		HttpGet get = new HttpGet(path);
		try (CloseableHttpResponse resp = ourHttpClient.execute(get)) {

			assertEquals(200, resp.getStatusLine().getStatusCode());
			assertEquals("image/jpeg", resp.getEntity().getContentType().getValue());
			assertEquals(bytes.length, resp.getEntity().getContentLength());

			byte[] actualBytes = IOUtils.toByteArray(resp.getEntity().getContent());
			assertArrayEquals(bytes, actualBytes);
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

		ourLog.info(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(documentReference));

		return myClient.create().resource(documentReference).execute().getId().toUnqualifiedVersionless();
	}


	@Test
	public void testResourceExpungeAlsoExpungesBinaryData() throws IOException {
		IIdType id = createDocumentReference(false);

		String path = ourServerBase +
			"/DocumentReference/" + id.getIdPart() + "/" +
			JpaConstants.OPERATION_BINARY_ACCESS_WRITE +
			"?path=DocumentReference.content.attachment";
		HttpPost post = new HttpPost(path);
		post.setEntity(new ByteArrayEntity(SOME_BYTES, ContentType.IMAGE_JPEG));
		post.addHeader("Accept", "application/fhir+json; _pretty=true");
		String attachmentId;
		try (CloseableHttpResponse resp = ourHttpClient.execute(post)) {
			assertEquals(200, resp.getStatusLine().getStatusCode());
			assertThat(resp.getEntity().getContentType().getValue(), containsString("application/fhir+json"));
			String response = IOUtils.toString(resp.getEntity().getContent(), Constants.CHARSET_UTF8);
			DocumentReference ref = myFhirContext.newJsonParser().parseResource(DocumentReference.class, response);
			Attachment attachment = ref.getContentFirstRep().getAttachment();
			attachmentId = attachment.getDataElement().getExtensionString(HapiExtensions.EXT_EXTERNALIZED_BINARY_ID);
			assertThat(attachmentId, matchesPattern("[a-zA-Z0-9]{100}"));
		}

		ByteArrayOutputStream capture = new ByteArrayOutputStream();
		myStorageSvc.writeBlob(id, attachmentId, capture);
		assertEquals(15, capture.size());

		// Now delete (logical delete- should not expunge the binary)
		myClient.delete().resourceById(id).execute();
		try {
			myClient.read().resource("DocumentReference").withId(id).execute();
			fail();
		} catch (ResourceGoneException e) {
			// good
		}

		capture = new ByteArrayOutputStream();
		myStorageSvc.writeBlob(id, attachmentId, capture);
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
		assertFalse(myStorageSvc.writeBlob(id, attachmentId, capture));
		assertEquals(0, capture.size());

	}



	@Test
	public void testWriteWithConflictInterceptor() throws IOException {
		UserRequestRetryVersionConflictsInterceptor interceptor = new UserRequestRetryVersionConflictsInterceptor();
		ourRestServer.registerInterceptor(interceptor);
		try {

			IIdType id = createDocumentReference(false);

			String path = ourServerBase +
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

				assertEquals(200, resp.getStatusLine().getStatusCode());
				assertThat(resp.getEntity().getContentType().getValue(), containsString("application/fhir+json"));
				DocumentReference ref = myFhirContext.newJsonParser().parseResource(DocumentReference.class, response);
				Attachment attachment = ref.getContentFirstRep().getAttachment();
				attachmentId = attachment.getDataElement().getExtensionString(HapiExtensions.EXT_EXTERNALIZED_BINARY_ID);
				assertThat(attachmentId, matchesPattern("[a-zA-Z0-9]{100}"));
			}

		} finally {
			ourRestServer.unregisterInterceptor(interceptor);
		}
	}


}
