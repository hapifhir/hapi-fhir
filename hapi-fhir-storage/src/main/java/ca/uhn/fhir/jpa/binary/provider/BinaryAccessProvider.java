package ca.uhn.fhir.jpa.binary.provider;

/*-
 * #%L
 * HAPI FHIR Storage api
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.context.BaseRuntimeElementDefinition;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.jpa.binary.api.IBinaryStorageSvc;
import ca.uhn.fhir.jpa.binary.api.IBinaryTarget;
import ca.uhn.fhir.jpa.binary.api.StoredDetails;
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
import ca.uhn.fhir.util.HapiExtensions;
import com.google.common.annotations.VisibleForTesting;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseBinary;
import org.hl7.fhir.instance.model.api.IBaseExtension;
import org.hl7.fhir.instance.model.api.IBaseHasExtensions;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.ICompositeType;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nonnull;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
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

	private Boolean addTargetAttachmentIdForTest = false;

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
		Optional<String> attachmentId = target.getAttachmentId();

		//for unit test only
		if (addTargetAttachmentIdForTest){
			attachmentId = Optional.of("1");
		}

		if (attachmentId.isPresent()) {

			@SuppressWarnings("unchecked")
			String blobId = attachmentId.get();

			StoredDetails blobDetails = myBinaryStorageSvc.fetchBlobDetails(theResourceId, blobId);
			if (blobDetails == null) {
				String msg = myCtx.getLocalizer().getMessage(BinaryAccessProvider.class, "unknownBlobId");
				throw new InvalidRequestException(Msg.code(1331) + msg);
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
				throw new InvalidRequestException(Msg.code(1332) + msg);
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
			throw new InvalidRequestException(Msg.code(1333) + "No content-target supplied");
		}
		if (EncodingEnum.forContentTypeStrict(requestContentType) != null) {
			throw new InvalidRequestException(Msg.code(1334) + "This operation is for binary content, got: " + requestContentType);
		}

		long size = theServletRequest.getContentLength();
		ourLog.trace("Request specified content length: {}", size);

		String blobId = null;

		if (size > 0) {
			if (myBinaryStorageSvc != null) {
				InputStream inputStream = theRequestDetails.getInputStream();
				if (inputStream.available() == 0 ) {
					throw new IllegalStateException(Msg.code(2073) + "Input stream is empty! Ensure that you are uploading data, and if so, ensure that no interceptors are in use that may be consuming the input stream");
				}
				if (myBinaryStorageSvc.shouldStoreBlob(size, theResourceId, requestContentType)) {
					StoredDetails storedDetails = myBinaryStorageSvc.storeBlob(theResourceId, null, requestContentType, inputStream);
					size = storedDetails.getBytes();
					blobId = storedDetails.getBlobId();
					Validate.notBlank(blobId, "BinaryStorageSvc returned a null blob ID"); // should not happen
				}
			}
		}

		if (blobId == null) {
			byte[] bytes = theRequestDetails.loadRequestContents();
			size = bytes.length;
			target.setData(bytes);
		} else {
			replaceDataWithExtension(target, blobId);
		}

		target.setContentType(requestContentType);
		target.setSize(null);
		if (size <= Integer.MAX_VALUE) {
			target.setSize((int) size);
		}

		DaoMethodOutcome outcome = dao.update(resource, theRequestDetails);
		return outcome.getResource();
	}

	public void replaceDataWithExtension(IBinaryTarget theTarget, String theBlobId) {
		theTarget
			.getTarget()
			.getExtension()
			.removeIf(t -> HapiExtensions.EXT_EXTERNALIZED_BINARY_ID.equals(t.getUrl()));
		theTarget.setData(null);

		IBaseExtension<?, ?> ext = theTarget.getTarget().addExtension();
		ext.setUrl(HapiExtensions.EXT_EXTERNALIZED_BINARY_ID);
		ext.setUserData(JpaConstants.EXTENSION_EXT_SYSTEMDEFINED, Boolean.TRUE);
		IPrimitiveType<String> blobIdString = (IPrimitiveType<String>) myCtx.getElementDefinition("string").newInstance();
		blobIdString.setValueAsString(theBlobId);
		ext.setValue(blobIdString);
	}

	@Nonnull
	private IBinaryTarget findAttachmentForRequest(IBaseResource theResource, String thePath, ServletRequestDetails theRequestDetails) {
		Optional<IBase> type = myCtx.newFluentPath().evaluateFirst(theResource, thePath, IBase.class);
		String resType = this.myCtx.getResourceType(theResource);
		if (!type.isPresent()) {
			String msg = this.myCtx.getLocalizer().getMessageSanitized(BinaryAccessProvider.class, "unknownPath", resType, thePath);
			throw new InvalidRequestException(Msg.code(1335) + msg);
		}
		IBase element = type.get();

		Optional<IBinaryTarget> binaryTarget = toBinaryTarget(element);

		if (binaryTarget.isPresent() == false) {
			BaseRuntimeElementDefinition<?> def2 = myCtx.getElementDefinition(element.getClass());
			String msg = this.myCtx.getLocalizer().getMessageSanitized(BinaryAccessProvider.class, "unknownType", resType, thePath, def2.getName());
			throw new InvalidRequestException(Msg.code(1336) + msg);
		} else {
			return binaryTarget.get();
		}

	}

	public Optional<IBinaryTarget> toBinaryTarget(IBase theElement) {
		IBinaryTarget binaryTarget = null;

		// Path is attachment
		BaseRuntimeElementDefinition<?> def = myCtx.getElementDefinition(theElement.getClass());
		if (def.getName().equals("Attachment")) {
			ICompositeType attachment = (ICompositeType) theElement;
			binaryTarget = new IBinaryTarget() {
				@Override
				public void setSize(Integer theSize) {
					AttachmentUtil.setSize(BinaryAccessProvider.this.myCtx, attachment, theSize);
				}

				@Override
				public String getContentType() {
					return AttachmentUtil.getOrCreateContentType(BinaryAccessProvider.this.myCtx, attachment).getValueAsString();
				}

				@Override
				public byte[] getData() {
					IPrimitiveType<byte[]> dataDt = AttachmentUtil.getOrCreateData(myCtx, attachment);
					return dataDt.getValue();
				}

				@Override
				public IBaseHasExtensions getTarget() {
					return (IBaseHasExtensions) AttachmentUtil.getOrCreateData(myCtx, attachment);
				}

				@Override
				public void setContentType(String theContentType) {
					AttachmentUtil.setContentType(BinaryAccessProvider.this.myCtx, attachment, theContentType);
				}


				@Override
				public void setData(byte[] theBytes) {
					AttachmentUtil.setData(myCtx, attachment, theBytes);
				}


			};
		}

		// Path is Binary
		if (def.getName().equals("Binary")) {
			IBaseBinary binary = (IBaseBinary) theElement;
			binaryTarget = new IBinaryTarget() {
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
					return (IBaseHasExtensions) BinaryUtil.getOrCreateData(BinaryAccessProvider.this.myCtx, binary);
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

		return Optional.ofNullable(binaryTarget);
	}

	private String validateResourceTypeAndPath(@IdParam IIdType theResourceId, @OperationParam(name = "path", min = 1, max = 1) IPrimitiveType<String> thePath) {
		if (isBlank(theResourceId.getResourceType())) {
			throw new InvalidRequestException(Msg.code(1337) + "No resource type specified");
		}
		if (isBlank(theResourceId.getIdPart())) {
			throw new InvalidRequestException(Msg.code(1338) + "No ID specified");
		}
		if (thePath == null || isBlank(thePath.getValue())) {
			if ("Binary".equals(theResourceId.getResourceType())) {
				return "Binary";
			}
			throw new InvalidRequestException(Msg.code(1339) + "No path specified");
		}

		return thePath.getValue();
	}

	@Nonnull
	private IFhirResourceDao getDaoForRequest(@IdParam IIdType theResourceId) {
		String resourceType = theResourceId.getResourceType();
		IFhirResourceDao dao = myDaoRegistry.getResourceDao(resourceType);
		if (dao == null) {
			throw new InvalidRequestException(Msg.code(1340) + "Unknown/unsupported resource type: " + sanitizeUrlPart(resourceType));
		}
		return dao;
	}


	@VisibleForTesting
	public void setDaoRegistryForUnitTest(DaoRegistry theDaoRegistry) {
		myDaoRegistry = theDaoRegistry;
	}

	@VisibleForTesting
	public void setBinaryStorageSvcForUnitTest(IBinaryStorageSvc theBinaryStorageSvc) {
		myBinaryStorageSvc = theBinaryStorageSvc;
	}

	@VisibleForTesting
	public void setFhirContextForUnitTest(FhirContext theCtx) {
		myCtx = theCtx;
	}

	@VisibleForTesting
	public void setTargetAttachmentIdForUnitTest(Boolean theTargetAttachmentIdForTest) {
		addTargetAttachmentIdForTest = theTargetAttachmentIdForTest;
	}
}
