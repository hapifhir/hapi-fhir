package ca.uhn.fhir.jpa.provider.r4;

import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IAnonymousInterceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.binstore.MemoryBinaryStorageSvcImpl;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.rest.api.Constants;
import com.google.common.base.Charsets;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Attachment;
import org.hl7.fhir.r4.model.DocumentReference;
import org.hl7.fhir.r4.model.StringType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.matchesPattern;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class BinaryAccessProviderR4Test extends BaseResourceProviderR4Test {

	public static final byte[] SOME_BYTES = {1, 2, 3, 4, 5, 6, 7, 8, 7, 6, 5, 4, 3, 2, 1};
	public static final byte[] SOME_BYTES_2 = {5, 5, 5, 6};
	private static final Logger ourLog = LoggerFactory.getLogger(BinaryAccessProviderR4Test.class);

	@Autowired
	private MemoryBinaryStorageSvcImpl myStorageSvc;

	@Override
	@Before
	public void before() throws Exception {
		super.before();
		myStorageSvc.setMinSize(10);
	}

	@Override
	@After
	public void after() throws Exception {
		super.after();
		myStorageSvc.setMinSize(0);
	}

	@Test
	public void testRead() throws IOException {
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
	public void testReadUnknownBlobId() throws IOException {
		IIdType id = createDocumentReference(false);

		DocumentReference dr = ourClient.read().resource(DocumentReference.class).withId(id).execute();
		dr.getContentFirstRep()
			.getAttachment()
			.addExtension(JpaConstants.EXT_ATTACHMENT_EXTERNAL_BINARY_ID, new StringType("AAAAA"));
		ourClient.update().resource(dr).execute();

		String path = ourServerBase +
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
	public void testWriteLarge() throws IOException {
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
		post.setEntity(new ByteArrayEntity(SOME_BYTES, ContentType.IMAGE_JPEG));
		post.addHeader("Accept", "application/fhir+json; _pretty=true");
		String attachmentId;
		try (CloseableHttpResponse resp = ourHttpClient.execute(post)) {

			assertEquals(200, resp.getStatusLine().getStatusCode());
			assertThat(resp.getEntity().getContentType().getValue(), containsString("application/fhir+json"));
			String response = IOUtils.toString(resp.getEntity().getContent(), Constants.CHARSET_UTF8);
			ourLog.info("Response: {}", response);

			DocumentReference ref = myFhirCtx.newJsonParser().parseResource(DocumentReference.class, response);

			Attachment attachment = ref.getContentFirstRep().getAttachment();
			assertEquals(ContentType.IMAGE_JPEG.getMimeType(), attachment.getContentType());
			assertEquals(15, attachment.getSize());
			assertEquals(null, attachment.getData());
			assertEquals("2", ref.getMeta().getVersionId());
			attachmentId = attachment.getExtensionString(JpaConstants.EXT_ATTACHMENT_EXTERNAL_BINARY_ID);
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

	/**
	 * Stores a binary small enough that it shouldn't live in binary storage
	 */
	@Test
	public void testWriteSmall() throws IOException {
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

			DocumentReference ref = myFhirCtx.newJsonParser().parseResource(DocumentReference.class, response);

			Attachment attachment = ref.getContentFirstRep().getAttachment();
			assertEquals(ContentType.IMAGE_JPEG.getMimeType(), attachment.getContentType());
			assertEquals(4, attachment.getSize());
			assertArrayEquals(SOME_BYTES_2, attachment.getData());
			assertEquals("2", ref.getMeta().getVersionId());
			attachmentId = attachment.getExtensionString(JpaConstants.EXT_ATTACHMENT_EXTERNAL_BINARY_ID);
			assertEquals(null, attachmentId);

		}

		verify(interceptor, timeout(5000).times(1)).invoke(eq(Pointcut.STORAGE_PRESHOW_RESOURCES), any());
		verify(interceptor, timeout(5000).times(1)).invoke(eq(Pointcut.STORAGE_PRESTORAGE_RESOURCE_UPDATED), any());
		verifyNoMoreInteractions(interceptor);

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
		return ourClient.create().resource(documentReference).execute().getId().toUnqualifiedVersionless();
	}




}
