package ca.uhn.fhir.jpa.binstore;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.jpa.binary.api.IBinaryStorageSvc;
import ca.uhn.fhir.jpa.binary.api.StoredDetails;
import ca.uhn.fhir.jpa.binary.provider.BinaryAccessProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.DocumentReference;
import org.hl7.fhir.r4.model.StringType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class BinaryAccessProviderTest {
	public static final byte[] SOME_BYTES = {1, 2, 3, 4, 5, 6, 7, 8, 7, 6, 5, 4, 3, 2, 1};
	public static final byte[] SOME_BYTES_2 = {6, 7, 8, 7, 6, 5, 4, 3, 2, 1, 5, 5, 5, 6};

	BinaryAccessProvider myBinaryAccessProvider;
	protected FhirContext myCtx;

	@Spy
	protected ServletRequestDetails myRequestDetails;
	@Spy
	protected HttpServletRequest theServletRequest;
	@Spy
	protected HttpServletResponse theServletResponse;
	@Mock
	private DaoRegistry myDaoRegistry;
	@Spy
	private IFhirResourceDao myResourceDao;
	@Spy
	protected IBinaryStorageSvc myBinaryStorageSvc;
	@Autowired
	private IInterceptorBroadcaster myInterceptorBroadcaster;


	@BeforeEach
	public void before() {
		myBinaryAccessProvider = new BinaryAccessProvider();
		myCtx = FhirContext.forR4();
		myBinaryAccessProvider.setFhirContextForUnitTest(myCtx);
		myBinaryAccessProvider.setDaoRegistryForUnitTest(myDaoRegistry);
		myBinaryAccessProvider.setBinaryStorageSvcForUnitTest(myBinaryStorageSvc);
		myBinaryAccessProvider.setTargetAttachmentIdForUnitTest(false);
		RestfulServer server = spy(RestfulServer.class);
		server.setFhirContext(myCtx);
		myRequestDetails.setServer(server);
	}

	@BeforeEach
	public void beforeSetRequestDetails() {
		myRequestDetails = new ServletRequestDetails(myInterceptorBroadcaster);
	}

	@AfterEach
	public void after() throws IOException {
		myBinaryAccessProvider.setTargetAttachmentIdForUnitTest(false);
	}

	@Test
	public void testBinaryAccessRead_WithAttachmentId_BlobId() throws IOException {
		DocumentReference docRef = createDocRef();
		docRef.getIdElement().setParts(null, "DocumentReference", "123", null);

		StoredDetails blobDetails = spy(StoredDetails.class);
		blobDetails.setPublished(new Date());
		ServletOutputStream sos = spy(ServletOutputStream.class);
		when(myDaoRegistry.getResourceDao(eq("DocumentReference"))).thenReturn(myResourceDao);
		when(myResourceDao.read(any(), any(), anyBoolean())).thenReturn(docRef);
		when(myBinaryStorageSvc.fetchBinaryContentDetails(any(), any())).thenReturn(blobDetails);
		when(theServletResponse.getOutputStream()).thenReturn(sos);
		myBinaryAccessProvider.setTargetAttachmentIdForUnitTest(true);

		try {
			myBinaryAccessProvider.binaryAccessRead(docRef.getIdElement(), new StringType("DocumentReference.content.attachment"), myRequestDetails, theServletRequest, theServletResponse);
		} catch (IOException e) {
		}
		verify(myBinaryStorageSvc, times(1)).fetchBinaryContentDetails(any(), any());
		verify(myBinaryStorageSvc, times(1)).writeBinaryContent(any(), any(), any());
		verify(theServletResponse, times(1)).setStatus(200);
		verify(theServletResponse, times(1)).setContentType(any());
		verify(theServletResponse, times(1)).setContentLength(0);
	}

	@Test
	public void testBinaryAccessRead_WithAttachmentId_UnknownBlobId() throws IOException {
		DocumentReference docRef = createDocRef();
		docRef.getIdElement().setParts(null, "DocumentReference", "123", null);

		when(myDaoRegistry.getResourceDao(eq("DocumentReference"))).thenReturn(myResourceDao);
		when(myResourceDao.read(any(), any(), anyBoolean())).thenReturn(docRef);
		myBinaryAccessProvider.setTargetAttachmentIdForUnitTest(true);

		try {
			myBinaryAccessProvider.binaryAccessRead(docRef.getIdElement(), new StringType("DocumentReference.content.attachment"), myRequestDetails, theServletRequest, theServletResponse);
		} catch (InvalidRequestException | IOException e) {
			assertEquals(Msg.code(1331) + "Can not find the requested binary content. It may have been deleted.", e.getMessage());
		}
		verify(myBinaryStorageSvc, times(1)).fetchBinaryContentDetails(any(), any());
	}

	@Test
	public void testBinaryAccessRead_WithoutAttachmentId() throws IOException {
		DocumentReference docRef = createDocRef();
		docRef.getIdElement().setParts(null, "DocumentReference", "123", null);

		ServletOutputStream sos = spy(ServletOutputStream.class);
		when(myDaoRegistry.getResourceDao(eq("DocumentReference"))).thenReturn(myResourceDao);
		when(myResourceDao.read(any(), any(), anyBoolean())).thenReturn(docRef);
		when(theServletResponse.getOutputStream()).thenReturn(sos);

		try {
			myBinaryAccessProvider.binaryAccessRead(docRef.getIdElement(), new StringType("DocumentReference.content.attachment"), myRequestDetails, theServletRequest, theServletResponse);
		} catch (IOException e) {
		}
		verify(theServletResponse, times(1)).setStatus(200);
		verify(theServletResponse, times(1)).setContentType("application/octet-stream");
		verify(theServletResponse, times(1)).setContentLength(15);
		verify(sos, times(1)).write(SOME_BYTES);
	}

	@Test
	public void testBinaryAccessRead_WithoutAttachmentId_NullData() {
		DocumentReference docRef = new DocumentReference();
		DocumentReference.DocumentReferenceContentComponent content = docRef.addContent();
		content.getAttachment().setContentType("application/octet-stream");
		content.getAttachment().setData(null);
		docRef.getIdElement().setParts(null, "DocumentReference", "123", null);

		when(myDaoRegistry.getResourceDao(eq("DocumentReference"))).thenReturn(myResourceDao);
		when(myResourceDao.read(any(), any(), anyBoolean())).thenReturn(docRef);

		try {
			myBinaryAccessProvider.binaryAccessRead(docRef.getIdElement(), new StringType("DocumentReference.content.attachment"), myRequestDetails, theServletRequest, theServletResponse);
		} catch (InvalidRequestException | IOException e) {
			assertEquals(String.format(Msg.code(1332) + "The resource with ID %s has no data at path: DocumentReference.content.attachment", docRef.getId()), e.getMessage());
		}
	}

	@Test
	public void testBinaryAccessRead_EmptyContent() {
		DocumentReference docRef = new DocumentReference();
		DocumentReference.DocumentReferenceContentComponent content = docRef.addContent();
		content.getAttachment().setContentType("application/octet-stream");
		docRef.getIdElement().setParts(null, "DocumentReference", "123", null);
		try {
			myBinaryAccessProvider.binaryAccessRead(docRef.getIdElement(), new StringType("DocumentReference.content.attachment"), myRequestDetails, theServletRequest, theServletResponse);
		} catch (InvalidRequestException | IOException e) {
			assertEquals(Msg.code(1340) + "Unknown/unsupported resource type: DocumentReference", e.getMessage());
		}
	}

	@Test
	public void testBinaryAccessRead_EmptyResourceType() {
		DocumentReference docRef = createDocRef();
		try {
			myBinaryAccessProvider.binaryAccessRead(docRef.getIdElement(), new StringType("DocumentReference.content.attachment"), myRequestDetails, theServletRequest, theServletResponse);
		} catch (InvalidRequestException | IOException e) {
			assertEquals(Msg.code(1337) + "No resource type specified", e.getMessage());
		}
	}

	@Test
	public void testBinaryAccessRead_EmptyPath() {
		DocumentReference docRef = createDocRef();
		docRef.getIdElement().setParts(null, "DocumentReference", "123", null);
		try {
			myBinaryAccessProvider.binaryAccessRead(docRef.getIdElement(), new StringType(""), myRequestDetails, theServletRequest, theServletResponse);
		} catch (InvalidRequestException | IOException e) {
			assertEquals(Msg.code(1339) + "No path specified", e.getMessage());
		}
	}

	@Test
	public void testBinaryAccessRead_EmptyId() {
		DocumentReference docRef = createDocRef();
		docRef.getIdElement().setParts(null, "DocumentReference", null, null);
		try {
			myBinaryAccessProvider.binaryAccessRead(docRef.getIdElement(), new StringType(""), myRequestDetails, theServletRequest, theServletResponse);
		} catch (InvalidRequestException | IOException e) {
			assertEquals(Msg.code(1338) + "No ID specified", e.getMessage());
		}
	}

	@Test
	public void testBinaryAccessWrite_WithoutContentLength0() {
		DocumentReference docRef = createDocRef();
		docRef.getIdElement().setParts(null, "DocumentReference", "123", null);

		DaoMethodOutcome daoOutcome = new DaoMethodOutcome();
		daoOutcome.setResource(docRef);
		when(myDaoRegistry.getResourceDao(eq("DocumentReference"))).thenReturn(myResourceDao);
		when(myResourceDao.read(any(), any(), anyBoolean())).thenReturn(docRef);
		when(myResourceDao.update(docRef, myRequestDetails)).thenReturn(daoOutcome);
		when(theServletRequest.getContentType()).thenReturn("1");
		myRequestDetails.setRequestContents(SOME_BYTES);

		try {
			IBaseResource outcome = myBinaryAccessProvider.binaryAccessWrite(docRef.getIdElement(), new StringType("DocumentReference.content.attachment"), myRequestDetails, theServletRequest, theServletResponse);
			assertNotNull(outcome);
			assertEquals(docRef.getId(), outcome.getIdElement().getValue());
		} catch (IOException e) {
		}
	}

	@Test
	public void testBinaryAccessWrite_WithContentLength15() throws IOException {
		DocumentReference docRef = createDocRef();
		docRef.getIdElement().setParts(null, "DocumentReference", "123", null);

		DaoMethodOutcome daoOutcome = new DaoMethodOutcome();
		daoOutcome.setResource(docRef);
		StoredDetails sd = spy(StoredDetails.class);
		sd.setBinaryContentId("123");
		sd.setBytes(15);
		when(myDaoRegistry.getResourceDao(eq("DocumentReference"))).thenReturn(myResourceDao);
		when(myResourceDao.read(any(), any(), anyBoolean())).thenReturn(docRef);
		when(myResourceDao.update(docRef, myRequestDetails)).thenReturn(daoOutcome);
		when(theServletRequest.getContentType()).thenReturn("Integer");
		when(theServletRequest.getContentLength()).thenReturn(15);
		when(myBinaryStorageSvc.shouldStoreBinaryContent(15, docRef.getIdElement(), "Integer")).thenReturn(true);
		myRequestDetails.setServletRequest(theServletRequest);
		doReturn(sd).when(myBinaryStorageSvc).storeBinaryContent(eq(docRef.getIdElement()), isNull(), eq("Integer"), any(InputStream.class), any(RequestDetails.class));
		myRequestDetails.setRequestContents(SOME_BYTES);

		try {
			IBaseResource outcome = myBinaryAccessProvider.binaryAccessWrite(docRef.getIdElement(), new StringType("DocumentReference.content.attachment"), myRequestDetails, theServletRequest, theServletResponse);
			assertNotNull(outcome);
			assertEquals(docRef.getId(), outcome.getIdElement().getValue());
		} catch (IOException e) {
		}
		verify(myBinaryStorageSvc, times(1)).storeBinaryContent(any(), any(), any(), any(), any(ServletRequestDetails.class));
	}

	@Test
	public void testBinaryAccessWrite_EmptyContentType() {
		DocumentReference docRef = createDocRef();
		docRef.getIdElement().setParts(null, "DocumentReference", "123", null);

		when(myDaoRegistry.getResourceDao(eq("DocumentReference"))).thenReturn(myResourceDao);
		when(myResourceDao.read(any(), any(), anyBoolean())).thenReturn(docRef);
		when(theServletRequest.getContentType()).thenReturn("");

		try {
			myBinaryAccessProvider.binaryAccessWrite(docRef.getIdElement(), new StringType("DocumentReference.content.attachment"), myRequestDetails, theServletRequest, theServletResponse);
		} catch (InvalidRequestException | IOException e) {
			assertEquals(Msg.code(1333) + "No content-target supplied", e.getMessage());
		}
	}

	@Test
	public void testBinaryAccessWrite_EmptyContent() {
		DocumentReference docRef = new DocumentReference();
		DocumentReference.DocumentReferenceContentComponent content = docRef.addContent();
		content.getAttachment().setContentType("application/octet-stream");
		docRef.getIdElement().setParts(null, "DocumentReference", "123", null);
		try {
			myBinaryAccessProvider.binaryAccessWrite(docRef.getIdElement(), new StringType("DocumentReference.content.attachment"), myRequestDetails, theServletRequest, theServletResponse);
		} catch (InvalidRequestException | IOException e) {
			assertEquals(Msg.code(1340) + "Unknown/unsupported resource type: DocumentReference", e.getMessage());
		}
	}

	@Test
	public void testBinaryAccessWrite_EmptyResourceType() {
		DocumentReference docRef = createDocRef();
		try {
			myBinaryAccessProvider.binaryAccessWrite(docRef.getIdElement(), new StringType("DocumentReference.content.attachment"), myRequestDetails, theServletRequest, theServletResponse);
		} catch (InvalidRequestException | IOException e) {
			assertEquals(Msg.code(1337) + "No resource type specified", e.getMessage());
		}
	}

	@Test
	public void testBinaryAccessWrite_EmptyPath() {
		DocumentReference docRef = createDocRef();
		docRef.getIdElement().setParts(null, "DocumentReference", "123", null);
		try {
			myBinaryAccessProvider.binaryAccessWrite(docRef.getIdElement(), new StringType(""), myRequestDetails, theServletRequest, theServletResponse);
		} catch (InvalidRequestException | IOException e) {
			assertEquals(Msg.code(1339) + "No path specified", e.getMessage());
		}
	}

	@Test
	public void testBinaryAccessWrite_EmptyId() {
		DocumentReference docRef = createDocRef();
		docRef.getIdElement().setParts(null, "DocumentReference", null, null);
		try {
			myBinaryAccessProvider.binaryAccessWrite(docRef.getIdElement(), new StringType(""), myRequestDetails, theServletRequest, theServletResponse);
		} catch (InvalidRequestException | IOException e) {
			assertEquals(Msg.code(1338) + "No ID specified", e.getMessage());
		}
	}

	private DocumentReference createDocRef() {
		DocumentReference docRef = new DocumentReference();
		DocumentReference.DocumentReferenceContentComponent content = docRef.addContent();
		content.getAttachment().setContentType("application/octet-stream");
		content.getAttachment().setData(SOME_BYTES);
		DocumentReference.DocumentReferenceContentComponent content2 = docRef.addContent();
		content2.getAttachment().setContentType("application/octet-stream");
		content2.getAttachment().setData(SOME_BYTES_2);
		return docRef;
	}
}
