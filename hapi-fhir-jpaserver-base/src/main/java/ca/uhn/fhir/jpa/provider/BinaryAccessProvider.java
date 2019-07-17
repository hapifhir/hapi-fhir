package ca.uhn.fhir.jpa.provider;

import ca.uhn.fhir.context.BaseRuntimeElementDefinition;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.binstore.IBinaryStorageSvc;
import ca.uhn.fhir.jpa.dao.DaoMethodOutcome;
import ca.uhn.fhir.jpa.dao.DaoRegistry;
import ca.uhn.fhir.jpa.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.util.AttachmentUtil;
import ca.uhn.fhir.util.DateUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nonnull;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

import static ca.uhn.fhir.util.UrlUtil.sanitizeUrlPart;
import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * This plain provider class can be registered with a JPA RestfulServer
 * to provide the <code>$binary-access-read</code> and <code>$binary-access-write</code>
 * operations that can be used to access attachment data as a raw binary.
 */
public class BinaryAccessProvider {

	@Autowired
	private FhirContext myCtx;
	@Autowired
	private DaoRegistry myDaoRegistry;
	@Autowired(required = false)
	private IBinaryStorageSvc myBinaryStorageSvc;

	/**
	 * $binary-access-read
	 */
	@Operation(name = JpaConstants.OPERATION_BINARY_ACCESS_READ, global = true, manualResponse = true, idempotent = true)
	public void binaryAccessRead(
		@IdParam IIdType theResourceId,
		@OperationParam(name = "path", min = 1, max = 1) IPrimitiveType<String> thePath,
		ServletRequestDetails theRequestDetails,
		HttpServletRequest theServletRequest,
		HttpServletResponse theServletResponse) throws IOException {

		validateResourceTypeAndPath(theResourceId, thePath);
		IFhirResourceDao dao = getDaoForRequest(theResourceId);
		IBaseResource resource = dao.read(theResourceId, theRequestDetails, false);

		ICompositeType attachment = findAttachmentForRequest(resource, thePath, theRequestDetails);

		IBaseHasExtensions attachmentHasExt = (IBaseHasExtensions) attachment;
		Optional<? extends IBaseExtension<?, ?>> attachmentId = attachmentHasExt
			.getExtension()
			.stream()
			.filter(t -> JpaConstants.EXT_ATTACHMENT_EXTERNAL_BINARY_ID.equals(t.getUrl()))
			.findFirst();

		if (attachmentId.isPresent()) {

			@SuppressWarnings("unchecked")
			IPrimitiveType<String> value = (IPrimitiveType<String>) attachmentId.get().getValue();
			String blobId = value.getValueAsString();

			IBinaryStorageSvc.StoredDetails blobDetails = myBinaryStorageSvc.fetchBlobDetails(theResourceId, blobId);
			if (blobDetails == null) {
				String msg = myCtx.getLocalizer().getMessage(BinaryAccessProvider.class, "unknownBlobId");
				throw new InvalidRequestException(msg);
			}

			theServletResponse.setStatus(200);
			theServletResponse.setContentType(blobDetails.getContentType());
			if (blobDetails.getBytes() <= Integer.MAX_VALUE) {
				theServletResponse.setContentLength((int) blobDetails.getBytes());
			}

			RestfulServer server = theRequestDetails.getServer();
			server.addHeadersToResponse(theServletResponse);

			theServletResponse.addHeader(Constants.HEADER_CACHE_CONTROL, Constants.CACHE_CONTROL_PRIVATE);
			theServletResponse.addHeader(Constants.HEADER_ETAG, '"' + blobDetails.getHash() + '"');
			theServletResponse.addHeader(Constants.HEADER_LAST_MODIFIED, DateUtils.formatDate(blobDetails.getPublished()));

			myBinaryStorageSvc.writeBlob(theResourceId, blobId, theServletResponse.getOutputStream());
			theServletResponse.getOutputStream().close();

			myBinaryStorageSvc.writeBlob(theResourceId, blobId, theServletResponse.getOutputStream());

		} else {

			IPrimitiveType<String> contentTypeDt = AttachmentUtil.getOrCreateContentType(theRequestDetails.getFhirContext(), attachment);
			String contentType = contentTypeDt.getValueAsString();
			contentType = StringUtils.defaultIfBlank(contentType, Constants.CT_OCTET_STREAM);

			IPrimitiveType<byte[]> dataDt = AttachmentUtil.getOrCreateData(theRequestDetails.getFhirContext(), attachment);
			byte[] data = dataDt.getValue();
			if (data == null) {
				String msg = myCtx.getLocalizer().getMessage(BinaryAccessProvider.class, "noAttachmentDataPresent", sanitizeUrlPart(theResourceId), sanitizeUrlPart(thePath));
				throw new InvalidRequestException(msg);
			}

			theServletResponse.setStatus(200);
			theServletResponse.setContentType(contentType);
			theServletResponse.setContentLength(data.length);

			RestfulServer server = theRequestDetails.getServer();
			server.addHeadersToResponse(theServletResponse);

			theServletResponse.getOutputStream().write(data);
			theServletResponse.getOutputStream().close();

		}
	}

	/**
	 * $binary-access-write
	 */
	@SuppressWarnings("unchecked")
	@Operation(name = JpaConstants.OPERATION_BINARY_ACCESS_WRITE, global = true, manualRequest = true, idempotent = false)
	public IBaseResource binaryAccessWrite(
		@IdParam IIdType theResourceId,
		@OperationParam(name = "path", min = 1, max = 1) IPrimitiveType<String> thePath,
		ServletRequestDetails theRequestDetails,
		HttpServletRequest theServletRequest,
		HttpServletResponse theServletResponse) throws IOException {

		validateResourceTypeAndPath(theResourceId, thePath);
		IFhirResourceDao dao = getDaoForRequest(theResourceId);
		IBaseResource resource = dao.read(theResourceId, theRequestDetails, false);

		ICompositeType attachment = findAttachmentForRequest(resource, thePath, theRequestDetails);

		String requestContentType = theServletRequest.getContentType();
		if (isBlank(requestContentType)) {
			throw new InvalidRequestException("No content-attachment supplied");
		}
		if (EncodingEnum.forContentTypeStrict(requestContentType) != null) {
			throw new InvalidRequestException("This operation is for binary content, got: " + requestContentType);
		}

		long size = theServletRequest.getContentLength();
		String blobId = null;

		if (size > 0) {
			if (myBinaryStorageSvc != null) {
				if (myBinaryStorageSvc.shouldStoreBlob(size, theResourceId, requestContentType)) {
					IBinaryStorageSvc.StoredDetails storedDetails = myBinaryStorageSvc.storeBlob(theResourceId, requestContentType, theRequestDetails.getInputStream());
					size = storedDetails.getBytes();
					blobId = storedDetails.getBlobId();
					Validate.notBlank(blobId, "BinaryStorageSvc returned a null blob ID"); // should not happen
				}
			}
		}

		if (blobId == null) {
			byte[] bytes = IOUtils.toByteArray(theRequestDetails.getInputStream());
			size = bytes.length;
			AttachmentUtil.setData(theRequestDetails.getFhirContext(), attachment, bytes);
		} else {

			IBaseHasExtensions attachmentHasExt = (IBaseHasExtensions) attachment;
			attachmentHasExt.getExtension().removeIf(t -> JpaConstants.EXT_ATTACHMENT_EXTERNAL_BINARY_ID.equals(t.getUrl()));
			AttachmentUtil.setData(myCtx, attachment, null);

			IBaseExtension<?, ?> ext = attachmentHasExt.addExtension();
			ext.setUrl(JpaConstants.EXT_ATTACHMENT_EXTERNAL_BINARY_ID);
			IPrimitiveType<String> blobIdString = (IPrimitiveType<String>) myCtx.getElementDefinition("string").newInstance();
			blobIdString.setValueAsString(blobId);
			ext.setValue(blobIdString);
		}

		AttachmentUtil.setContentType(theRequestDetails.getFhirContext(), attachment, requestContentType);

		AttachmentUtil.setSize(theRequestDetails.getFhirContext(), attachment, null);
		if (size <= Integer.MAX_VALUE) {
			AttachmentUtil.setSize(theRequestDetails.getFhirContext(), attachment, (int) size);
		}

		DaoMethodOutcome outcome = dao.update(resource, theRequestDetails);
		return outcome.getResource();
	}

	@Nonnull
	private ICompositeType findAttachmentForRequest(IBaseResource theResource, @OperationParam(name = "path", min = 1, max = 1) IPrimitiveType<String> thePath, ServletRequestDetails theRequestDetails) {
		FhirContext ctx = theRequestDetails.getFhirContext();
		String path = thePath.getValueAsString();

		Optional<ICompositeType> type = ctx.newFluentPath().evaluateFirst(theResource, path, ICompositeType.class);
		if (!type.isPresent()) {
			throw new InvalidRequestException("Unable to find Attachment at path: " + sanitizeUrlPart(path));
		}

		BaseRuntimeElementDefinition<?> def = ctx.getElementDefinition(type.get().getClass());
		if (!def.getName().equals("Attachment")) {
			throw new InvalidRequestException("Path does not return an Attachment: " + sanitizeUrlPart(path));
		}
		return type.get();
	}

	private void validateResourceTypeAndPath(@IdParam IIdType theResourceId, @OperationParam(name = "path", min = 1, max = 1) IPrimitiveType<String> thePath) {
		if (isBlank(theResourceId.getResourceType())) {
			throw new InvalidRequestException("No resource type specified");
		}
		if (isBlank(theResourceId.getIdPart())) {
			throw new InvalidRequestException("No ID specified");
		}
		if (thePath == null || isBlank(thePath.getValue())) {
			throw new InvalidRequestException("No path specified");
		}
	}

	@Nonnull
	private IFhirResourceDao getDaoForRequest(@IdParam IIdType theResourceId) {
		String resourceType = theResourceId.getResourceType();
		IFhirResourceDao dao = myDaoRegistry.getResourceDao(resourceType);
		if (dao == null) {
			throw new InvalidRequestException("Unknown/unsupported resource type: " + sanitizeUrlPart(resourceType));
		}
		return dao;
	}


}
