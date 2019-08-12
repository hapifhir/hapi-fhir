package ca.uhn.fhir.jpa.binstore;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2019 University Health Network
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import ca.uhn.fhir.context.BaseRuntimeElementDefinition;
import ca.uhn.fhir.context.FhirContext;
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
import ca.uhn.fhir.util.BinaryUtil;
import ca.uhn.fhir.util.DateUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

	private static final Logger ourLog = LoggerFactory.getLogger(BinaryAccessProvider.class);
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

		String path = validateResourceTypeAndPath(theResourceId, thePath);
		IFhirResourceDao dao = getDaoForRequest(theResourceId);
		IBaseResource resource = dao.read(theResourceId, theRequestDetails, false);

		IBinaryTarget target = findAttachmentForRequest(resource, path, theRequestDetails);

		Optional<? extends IBaseExtension<?, ?>> attachmentId = target
			.getTarget()
			.getExtension()
			.stream()
			.filter(t -> JpaConstants.EXT_EXTERNALIZED_BINARY_ID.equals(t.getUrl()))
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

		} else {

			String contentType = target.getContentType();
			contentType = StringUtils.defaultIfBlank(contentType, Constants.CT_OCTET_STREAM);

			byte[] data = target.getData();
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

		String path = validateResourceTypeAndPath(theResourceId, thePath);
		IFhirResourceDao dao = getDaoForRequest(theResourceId);
		IBaseResource resource = dao.read(theResourceId, theRequestDetails, false);

		IBinaryTarget target = findAttachmentForRequest(resource, path, theRequestDetails);

		String requestContentType = theServletRequest.getContentType();
		if (isBlank(requestContentType)) {
			throw new InvalidRequestException("No content-target supplied");
		}
		if (EncodingEnum.forContentTypeStrict(requestContentType) != null) {
			throw new InvalidRequestException("This operation is for binary content, got: " + requestContentType);
		}

		long size = theServletRequest.getContentLength();
		ourLog.trace("Request specified content length: {}", size);

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
			target.setData(bytes);
		} else {

			target
				.getTarget()
				.getExtension()
				.removeIf(t -> JpaConstants.EXT_EXTERNALIZED_BINARY_ID.equals(t.getUrl()));
			target.setData(null);

			IBaseExtension<?, ?> ext = target.getTarget().addExtension();
			ext.setUrl(JpaConstants.EXT_EXTERNALIZED_BINARY_ID);
			IPrimitiveType<String> blobIdString = (IPrimitiveType<String>) myCtx.getElementDefinition("string").newInstance();
			blobIdString.setValueAsString(blobId);
			ext.setValue(blobIdString);
		}

		target.setContentType(requestContentType);
		target.setSize(null);
		if (size <= Integer.MAX_VALUE) {
			target.setSize((int) size);
		}

		DaoMethodOutcome outcome = dao.update(resource, theRequestDetails);
		return outcome.getResource();
	}

	@Nonnull
	private IBinaryTarget findAttachmentForRequest(IBaseResource theResource, String thePath, ServletRequestDetails theRequestDetails) {
		FhirContext ctx = theRequestDetails.getFhirContext();

		Optional<IBase> type = ctx.newFluentPath().evaluateFirst(theResource, thePath, IBase.class);
		String resType = myCtx.getResourceDefinition(theResource).getName();
		if (!type.isPresent()) {
			String msg = myCtx.getLocalizer().getMessageSanitized(BinaryAccessProvider.class, "unknownPath", resType, thePath);
			throw new InvalidRequestException(msg);
		}

		// Path is attachment
		BaseRuntimeElementDefinition<?> def = ctx.getElementDefinition(type.get().getClass());
		if (def.getName().equals("Attachment")) {
			ICompositeType attachment = (ICompositeType) type.get();
			return new IBinaryTarget() {
				@Override
				public void setSize(Integer theSize) {
					AttachmentUtil.setSize(myCtx, attachment, theSize);
				}

				@Override
				public String getContentType() {
					return AttachmentUtil.getOrCreateContentType(myCtx, attachment).getValueAsString();
				}

				@Override
				public byte[] getData() {
					IPrimitiveType<byte[]> dataDt = AttachmentUtil.getOrCreateData(theRequestDetails.getFhirContext(), attachment);
					return dataDt.getValue();
				}

				@Override
				public IBaseHasExtensions getTarget() {
					return (IBaseHasExtensions) AttachmentUtil.getOrCreateData(theRequestDetails.getFhirContext(), attachment);
				}

				@Override
				public void setContentType(String theContentType) {
					AttachmentUtil.setContentType(myCtx, attachment, theContentType);
				}


				@Override
				public void setData(byte[] theBytes) {
					AttachmentUtil.setData(theRequestDetails.getFhirContext(), attachment, theBytes);
				}


			};
		}

		// Path is Binary
		if (def.getName().equals("Binary")) {
			IBaseBinary binary = (IBaseBinary) type.get();
			return new IBinaryTarget() {
				@Override
				public void setSize(Integer theSize) {
					// ignore
				}

				@Override
				public String getContentType() {
					return binary.getContentType();
				}

				@Override
				public byte[] getData() {
					return binary.getContent();
				}

				@Override
				public IBaseHasExtensions getTarget() {
					return (IBaseHasExtensions) BinaryUtil.getOrCreateData(myCtx, binary);
				}

				@Override
				public void setContentType(String theContentType) {
					binary.setContentType(theContentType);
				}


				@Override
				public void setData(byte[] theBytes) {
					binary.setContent(theBytes);
				}


			};
		}

		String msg = myCtx.getLocalizer().getMessageSanitized(BinaryAccessProvider.class, "unknownType", resType, thePath, def.getName());
		throw new InvalidRequestException(msg);

	}

	private String validateResourceTypeAndPath(@IdParam IIdType theResourceId, @OperationParam(name = "path", min = 1, max = 1) IPrimitiveType<String> thePath) {
		if (isBlank(theResourceId.getResourceType())) {
			throw new InvalidRequestException("No resource type specified");
		}
		if (isBlank(theResourceId.getIdPart())) {
			throw new InvalidRequestException("No ID specified");
		}
		if (thePath == null || isBlank(thePath.getValue())) {
			if ("Binary".equals(theResourceId.getResourceType())) {
				return "Binary";
			}
			throw new InvalidRequestException("No path specified");
		}

		return thePath.getValue();
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

	/**
	 * Wraps an Attachment datatype or Binary resource, since they both
	 * hold binary content but don't look entirely similar
	 */
	private interface IBinaryTarget {

		void setSize(Integer theSize);

		String getContentType();

		void setContentType(String theContentType);

		byte[] getData();

		void setData(byte[] theBytes);

		IBaseHasExtensions getTarget();

	}


}
