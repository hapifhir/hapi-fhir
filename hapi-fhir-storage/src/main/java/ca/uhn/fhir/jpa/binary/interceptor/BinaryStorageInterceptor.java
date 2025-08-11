/*-
 * #%L
 * HAPI FHIR Storage api
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.binary.interceptor;

import ca.uhn.fhir.context.BaseRuntimeChildDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementDefinition;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.api.Interceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.binary.api.IBinaryStorageSvc;
import ca.uhn.fhir.jpa.binary.api.IBinaryTarget;
import ca.uhn.fhir.jpa.binary.api.StoredDetails;
import ca.uhn.fhir.jpa.binary.provider.BinaryAccessProvider;
import ca.uhn.fhir.jpa.binary.svc.BaseBinaryStorageSvcImpl;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.rest.api.server.IPreResourceShowDetails;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.storage.TransactionDetails;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.rest.server.util.CompositeInterceptorBroadcaster;
import ca.uhn.fhir.util.HapiExtensions;
import ca.uhn.fhir.util.IModelVisitor2;
import jakarta.annotation.Nonnull;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseExtension;
import org.hl7.fhir.instance.model.api.IBaseHasExtensions;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r4.model.IdType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static ca.uhn.fhir.util.HapiExtensions.EXT_EXTERNALIZED_BINARY_HASH_SHA_256;
import static ca.uhn.fhir.util.HapiExtensions.EXT_EXTERNALIZED_BINARY_ID;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Interceptor
public class BinaryStorageInterceptor<T extends IPrimitiveType<byte[]>> {

	private static final Logger ourLog = LoggerFactory.getLogger(BinaryStorageInterceptor.class);
	public static final String AUTO_INFLATE_BINARY_CONTENT_KEY = "AUTO_INFLATE_BINARY_CONTENT";

	@Autowired
	private IBinaryStorageSvc myBinaryStorageSvc;

	private final FhirContext myCtx;

	@Autowired
	private BinaryAccessProvider myBinaryAccessProvider;

	@Autowired
	private IInterceptorBroadcaster myInterceptorBroadcaster;

	private Class<T> myBinaryType;
	private String myDeferredListKey;
	private long myAutoInflateBinariesMaximumBytes = 10 * FileUtils.ONE_MB;
	private boolean myAllowAutoInflateBinaries = true;

	public BinaryStorageInterceptor(FhirContext theCtx) {
		myCtx = theCtx;
		BaseRuntimeElementDefinition<?> base64Binary = myCtx.getElementDefinition("base64Binary");
		assert base64Binary != null;
		myBinaryType = (Class<T>) base64Binary.getImplementingClass();
		myDeferredListKey = getClass().getName() + "_" + hashCode() + "_DEFERRED_LIST";
	}

	/**
	 * Any externalized binaries will be rehydrated if their size is below this thhreshold when
	 * reading the resource back. Default is 10MB.
	 */
	public long getAutoInflateBinariesMaximumSize() {
		return myAutoInflateBinariesMaximumBytes;
	}

	/**
	 * Any externalized binaries will be rehydrated if their size is below this thhreshold when
	 * reading the resource back. Default is 10MB.
	 */
	public void setAutoInflateBinariesMaximumSize(long theAutoInflateBinariesMaximumBytes) {
		myAutoInflateBinariesMaximumBytes = theAutoInflateBinariesMaximumBytes;
	}

	@Hook(Pointcut.STORAGE_PRESTORAGE_EXPUNGE_RESOURCE)
	public void expungeResource(AtomicInteger theCounter, IBaseResource theResource) {

		List<? extends IBase> binaryElements =
				myCtx.newTerser().getAllPopulatedChildElementsOfType(theResource, myBinaryType);

		List<String> attachmentIds = binaryElements.stream()
				.flatMap(t -> ((IBaseHasExtensions) t).getExtension().stream())
				.filter(t -> HapiExtensions.EXT_EXTERNALIZED_BINARY_ID.equals(t.getUrl()))
				.map(t -> ((IPrimitiveType<?>) t.getValue()).getValueAsString())
				.collect(Collectors.toList());

		for (String next : attachmentIds) {
			myBinaryStorageSvc.expungeBinaryContent(theResource.getIdElement(), next);
			theCounter.incrementAndGet();

			ourLog.info(
					"Deleting binary blob {} because resource {} is being expunged",
					next,
					theResource.getIdElement().getValue());
		}
	}

	@Hook(Pointcut.STORAGE_PRESTORAGE_RESOURCE_CREATED)
	public void extractLargeBinariesBeforeCreate(
			RequestDetails theRequestDetails,
			TransactionDetails theTransactionDetails,
			IBaseResource theResource,
			Pointcut thePointcut)
			throws IOException {
		extractLargeBinaries(theRequestDetails, theTransactionDetails, theResource, null, thePointcut);
	}

	@Hook(Pointcut.STORAGE_PRESTORAGE_RESOURCE_UPDATED)
	public void extractLargeBinariesBeforeUpdate(
			RequestDetails theRequestDetails,
			TransactionDetails theTransactionDetails,
			IBaseResource thePreviousResource,
			IBaseResource theResource,
			Pointcut thePointcut)
			throws IOException {
		blockIllegalExternalExtensions(thePreviousResource, theResource);
		extractLargeBinaries(theRequestDetails, theTransactionDetails, theResource, thePreviousResource, thePointcut);
	}

	/**
	 * Prevents clients from submitting resources with binary storage ID or binary storage hash extensions,
	 * unless those extensions were already present in the existing resource. In other words, only HAPI itself
	 * may add these extensions to a resource.
	 */
	private void blockIllegalExternalExtensions(IBaseResource thePreviousResource, IBaseResource theResource) {
		Set<String> existingBinaryIds = new HashSet<>();
		Set<String> existingHashes = new HashSet<>();
		if (thePreviousResource != null) {
			myCtx.newTerser().getAllPopulatedChildElementsOfType(thePreviousResource, myBinaryType).stream()
					.filter(IBaseHasExtensions.class::isInstance)
					.map(IBaseHasExtensions.class::cast)
					.flatMap(base -> base.getExtension().stream())
					.filter(ext -> ext.getUserData(JpaConstants.EXTENSION_EXT_SYSTEMDEFINED) == null)
					.forEach(ext -> {
						if (EXT_EXTERNALIZED_BINARY_ID.equals(ext.getUrl())) {
							extractExtensionValue(ext).ifPresent(existingBinaryIds::add);
						} else if (EXT_EXTERNALIZED_BINARY_HASH_SHA_256.equals(ext.getUrl())) {
							extractExtensionValue(ext).ifPresent(existingHashes::add);
						}
					});
		}

		myCtx.newTerser().getAllPopulatedChildElementsOfType(theResource, myBinaryType).stream()
				.filter(IBaseHasExtensions.class::isInstance)
				.map(IBaseHasExtensions.class::cast)
				.flatMap(base -> base.getExtension().stream())
				.filter(ext -> ext.getUserData(JpaConstants.EXTENSION_EXT_SYSTEMDEFINED) == null)
				.forEach(ext -> {
					Optional<String> extensionValue = extractExtensionValue(ext);
					if (EXT_EXTERNALIZED_BINARY_ID.equals(ext.getUrl())) {
						extensionValue
								.filter(v -> !existingBinaryIds.contains(v))
								.ifPresent(v -> throwIllegalExtension(EXT_EXTERNALIZED_BINARY_ID, v));
					} else if (EXT_EXTERNALIZED_BINARY_HASH_SHA_256.equals(ext.getUrl())) {
						extensionValue
								.filter(v -> !existingHashes.contains(v))
								.ifPresent(v -> throwIllegalExtension(EXT_EXTERNALIZED_BINARY_HASH_SHA_256, v));
					}
				});
	}

	@SuppressWarnings("unchecked")
	private Optional<String> extractExtensionValue(IBaseExtension<?, ?> theExtension) {
		if (theExtension.getValue() instanceof IPrimitiveType) {
			return Optional.ofNullable(((IPrimitiveType<String>) theExtension.getValue()).getValueAsString())
					.filter(StringUtils::isNotBlank);
		}
		return Optional.empty();
	}

	private void throwIllegalExtension(String theExtensionUrl, String theValue) {
		String msg = myCtx.getLocalizer()
				.getMessage(
						BinaryStorageInterceptor.class,
						"externalizedBinaryStorageExtensionFoundInRequestBody",
						theExtensionUrl,
						theValue);
		throw new InvalidRequestException(Msg.code(1329) + msg);
	}

	private void extractLargeBinaries(
			RequestDetails theRequestDetails,
			TransactionDetails theTransactionDetails,
			IBaseResource theResource,
			IBaseResource thePreviousResource,
			Pointcut thePointcut)
			throws IOException {

		IIdType resourceId = theResource.getIdElement();
		if (!resourceId.hasResourceType() && resourceId.hasIdPart()) {
			String resourceType = myCtx.getResourceType(theResource);
			resourceId = new IdType(resourceType + "/" + resourceId.getIdPart());
		}

		Map<String, String> existingHashToAttachmentId = buildHashToAttachmentIdMap(thePreviousResource);
		List<IBinaryTarget> attachments = recursivelyScanResourceForBinaryData(theResource);
		for (IBinaryTarget nextTarget : attachments) {
			byte[] data = nextTarget.getData();
			if (data != null && data.length > 0) {

				long nextPayloadLength = data.length;
				String nextContentType = nextTarget.getContentType();
				boolean shouldStoreBlob =
						myBinaryStorageSvc.shouldStoreBinaryContent(nextPayloadLength, resourceId, nextContentType);
				if (shouldStoreBlob) {
					String binaryContentHash = myBinaryAccessProvider.getBinaryContentHash(data);
					String newBinaryContentId;
					if (thePointcut == Pointcut.STORAGE_PRESTORAGE_RESOURCE_UPDATED) {
						newBinaryContentId = storeBinaryContentIfRequired(
								theRequestDetails,
								existingHashToAttachmentId,
								binaryContentHash,
								data,
								resourceId,
								nextContentType);
					} else {
						assert thePointcut == Pointcut.STORAGE_PRESTORAGE_RESOURCE_CREATED : thePointcut.name();
						newBinaryContentId = myBinaryStorageSvc.newBinaryContentId();

						String prefix = invokeAssignBinaryContentPrefix(theRequestDetails, theResource);
						if (isNotBlank(prefix)) {
							newBinaryContentId = prefix + newBinaryContentId;
						}
						if (myBinaryStorageSvc.isValidBinaryContentId(newBinaryContentId)) {
							List<DeferredBinaryTarget> deferredBinaryTargets =
									getOrCreateDeferredBinaryStorageList(theResource);
							DeferredBinaryTarget newDeferredBinaryTarget =
									new DeferredBinaryTarget(newBinaryContentId, nextTarget, data);
							deferredBinaryTargets.add(newDeferredBinaryTarget);
							newDeferredBinaryTarget.setBlobIdPrefixHookApplied(true);
						} else {
							throw new InternalErrorException(Msg.code(2341)
									+ "Invalid binaryContent ID for backing storage service.[binaryContentId="
									+ newBinaryContentId + ",service="
									+ myBinaryStorageSvc.getClass().getName() + "]");
						}
					}

					myBinaryAccessProvider.replaceDataWithExtension(nextTarget, newBinaryContentId);
					myBinaryAccessProvider.addHashExtension(nextTarget, binaryContentHash);
				}
			}
		}
	}

	/**
	 * Builds a map of SHA-256 hashes to corresponding attachment IDs from the given FHIR resource.
	 * @return A {@link Map} with keys as SHA-256 binary content hashes and values as attachment IDs.
	 */
	private Map<String, String> buildHashToAttachmentIdMap(IBaseResource thePreviousResource) {
		Map<String, String> hashToAttachmentIdMap = new HashMap<>();

		if (thePreviousResource == null) {
			return hashToAttachmentIdMap;
		}

		List<IBinaryTarget> previousAttachments = recursivelyScanResourceForBinaryData(thePreviousResource);
		for (IBinaryTarget attachment : previousAttachments) {
			Optional<String> hashOpt = attachment.getHashExtension();
			Optional<String> idOpt = attachment.getAttachmentId();

			if (hashOpt.isPresent() && idOpt.isPresent()) {
				hashToAttachmentIdMap.put(hashOpt.get(), idOpt.get());
			}
		}
		return hashToAttachmentIdMap;
	}

	/**
	 * This method checks if the given binary content (based on its SHA-256 hash) is already stored in previous
	 * resource version. If it is, it reuses the existing attachment ID to avoid saving the same content again.
	 * If it's not found, it stores the new content and returns the newly generated attachment ID.
	 */
	private String storeBinaryContentIfRequired(
			RequestDetails theRequestDetails,
			Map<String, String> existingHashToAttachmentId,
			String binaryContentHash,
			byte[] data,
			IIdType resourceId,
			String nextContentType)
			throws IOException {
		if (existingHashToAttachmentId.get(binaryContentHash) != null) {
			// input binary content is the same as existing binary content, reuse existing binaryId
			return existingHashToAttachmentId.get(binaryContentHash);
		} else {
			// there is no existing binary content or content is different, store new content in binary storage
			ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
			StoredDetails storedDetails = myBinaryStorageSvc.storeBinaryContent(
					resourceId, null, nextContentType, inputStream, theRequestDetails);
			return storedDetails.getBinaryContentId();
		}
	}

	/**
	 * This invokes the {@link Pointcut#STORAGE_BINARY_ASSIGN_BLOB_ID_PREFIX} hook and returns the prefix to use for the blob ID, or null if there are no implementers.
	 * @return A string, which will be used to prefix the blob ID. May be null.
	 */
	private String invokeAssignBinaryContentPrefix(RequestDetails theRequest, IBaseResource theResource) {
		IInterceptorBroadcaster compositeBroadcaster =
				CompositeInterceptorBroadcaster.newCompositeBroadcaster(myInterceptorBroadcaster, theRequest);

		// TODO: to be removed when pointcut STORAGE_BINARY_ASSIGN_BLOB_ID_PREFIX has exceeded the grace period
		boolean hasStorageBinaryAssignBlobIdPrefixHooks =
				compositeBroadcaster.hasHooks(Pointcut.STORAGE_BINARY_ASSIGN_BLOB_ID_PREFIX);

		boolean hasStorageBinaryAssignBinaryContentIdPrefixHooks =
				compositeBroadcaster.hasHooks(Pointcut.STORAGE_BINARY_ASSIGN_BINARY_CONTENT_ID_PREFIX);

		if (!(hasStorageBinaryAssignBlobIdPrefixHooks || hasStorageBinaryAssignBinaryContentIdPrefixHooks)) {
			return null;
		}

		HookParams params =
				new HookParams().add(RequestDetails.class, theRequest).add(IBaseResource.class, theResource);

		BaseBinaryStorageSvcImpl.setBinaryContentIdPrefixApplied(theRequest);

		Pointcut pointcutToInvoke = Pointcut.STORAGE_BINARY_ASSIGN_BINARY_CONTENT_ID_PREFIX;

		// TODO: to be removed when pointcut STORAGE_BINARY_ASSIGN_BLOB_ID_PREFIX has exceeded the grace period
		if (hasStorageBinaryAssignBlobIdPrefixHooks) {
			pointcutToInvoke = Pointcut.STORAGE_BINARY_ASSIGN_BLOB_ID_PREFIX;
		}

		return (String) compositeBroadcaster.callHooksAndReturnObject(pointcutToInvoke, params);
	}

	@Nonnull
	@SuppressWarnings("unchecked")
	private List<DeferredBinaryTarget> getOrCreateDeferredBinaryStorageList(IBaseResource theResource) {
		Object deferredBinaryTargetList = theResource.getUserData(getDeferredListKey());
		if (deferredBinaryTargetList == null) {
			deferredBinaryTargetList = new ArrayList<>();
			theResource.setUserData(getDeferredListKey(), deferredBinaryTargetList);
		}
		return (List<DeferredBinaryTarget>) deferredBinaryTargetList;
	}

	@SuppressWarnings("unchecked")
	@Hook(Pointcut.STORAGE_PRECOMMIT_RESOURCE_CREATED)
	public void storeLargeBinariesBeforeCreatePersistence(
			TransactionDetails theTransactionDetails, IBaseResource theResource, Pointcut thePointcut)
			throws IOException {
		if (theResource == null) {
			return;
		}
		Object deferredBinaryTargetList = theResource.getUserData(getDeferredListKey());

		if (deferredBinaryTargetList != null) {
			IIdType resourceId = theResource.getIdElement();
			for (DeferredBinaryTarget next : (List<DeferredBinaryTarget>) deferredBinaryTargetList) {
				String blobId = next.getBlobId();
				IBinaryTarget target = next.getBinaryTarget();
				InputStream dataStream = next.getDataStream();
				String contentType = target.getContentType();
				RequestDetails requestDetails = initRequestDetails(next);
				myBinaryStorageSvc.storeBinaryContent(resourceId, blobId, contentType, dataStream, requestDetails);
			}
		}
	}

	private RequestDetails initRequestDetails(DeferredBinaryTarget theDeferredBinaryTarget) {
		ServletRequestDetails requestDetails = new ServletRequestDetails();
		if (theDeferredBinaryTarget.isBlobIdPrefixHookApplied()) {
			BaseBinaryStorageSvcImpl.setBinaryContentIdPrefixApplied(requestDetails);
		}
		return requestDetails;
	}

	public String getDeferredListKey() {
		return myDeferredListKey;
	}

	@Hook(Pointcut.STORAGE_PRESHOW_RESOURCES)
	public void preShow(IPreResourceShowDetails theDetails, RequestDetails theRequestDetails) throws IOException {
		boolean isAllowAutoInflateBinaries = isAllowAutoInflateBinaries();
		// Override isAllowAutoInflateBinaries setting if AUTO_INFLATE_BINARY_CONTENT flag is present in userData
		if (theRequestDetails != null && theRequestDetails.getUserData().containsKey(AUTO_INFLATE_BINARY_CONTENT_KEY)) {
			isAllowAutoInflateBinaries =
					Boolean.TRUE.equals(theRequestDetails.getUserData().get(AUTO_INFLATE_BINARY_CONTENT_KEY));
		}
		if (!isAllowAutoInflateBinaries) {
			return;
		}

		long cumulativeInflatedBytes = 0;
		int inflatedResourceCount = 0;

		for (IBaseResource nextResource : theDetails) {
			if (nextResource == null) {
				ourLog.warn(
						"Received a null resource during STORAGE_PRESHOW_RESOURCES. This is a bug and should be reported. Skipping resource.");
				continue;
			}
			cumulativeInflatedBytes = inflateBinariesInResource(cumulativeInflatedBytes, nextResource);
			inflatedResourceCount += 1;
			if (cumulativeInflatedBytes >= myAutoInflateBinariesMaximumBytes) {
				ourLog.debug(
						"Exiting binary data inflation early.[byteCount={}, resourcesInflated={}, resourcesSkipped={}]",
						cumulativeInflatedBytes,
						inflatedResourceCount,
						theDetails.size() - inflatedResourceCount);
				return;
			}
		}
		ourLog.debug(
				"Exiting binary data inflation having inflated everything.[byteCount={}, resourcesInflated={}, resourcesSkipped=0]",
				cumulativeInflatedBytes,
				inflatedResourceCount);
	}

	private long inflateBinariesInResource(long theCumulativeInflatedBytes, IBaseResource theResource)
			throws IOException {
		IIdType resourceId = theResource.getIdElement();
		List<IBinaryTarget> attachments = recursivelyScanResourceForBinaryData(theResource);
		for (IBinaryTarget nextTarget : attachments) {
			Optional<String> attachmentId = nextTarget.getAttachmentId();
			if (attachmentId.isPresent()) {

				StoredDetails blobDetails =
						myBinaryStorageSvc.fetchBinaryContentDetails(resourceId, attachmentId.get());
				if (blobDetails == null) {
					String msg = myCtx.getLocalizer().getMessage(BinaryAccessProvider.class, "unknownBlobId");
					throw new InvalidRequestException(Msg.code(1330) + msg);
				}

				if ((theCumulativeInflatedBytes + blobDetails.getBytes()) < myAutoInflateBinariesMaximumBytes) {
					byte[] bytes = myBinaryStorageSvc.fetchBinaryContent(resourceId, attachmentId.get());
					nextTarget.setData(bytes);
					theCumulativeInflatedBytes += blobDetails.getBytes();
				}
			}
		}
		return theCumulativeInflatedBytes;
	}

	@Nonnull
	private List<IBinaryTarget> recursivelyScanResourceForBinaryData(IBaseResource theResource) {
		List<IBinaryTarget> binaryTargets = new ArrayList<>();
		myCtx.newTerser().visit(theResource, new IModelVisitor2() {
			@Override
			public boolean acceptElement(
					IBase theElement,
					List<IBase> theContainingElementPath,
					List<BaseRuntimeChildDefinition> theChildDefinitionPath,
					List<BaseRuntimeElementDefinition<?>> theElementDefinitionPath) {

				if (theElement.getClass().equals(myBinaryType)) {
					IBase parent = theContainingElementPath.get(theContainingElementPath.size() - 2);
					Optional<IBinaryTarget> binaryTarget = myBinaryAccessProvider.toBinaryTarget(parent);
					binaryTarget.ifPresent(binaryTargets::add);
				}
				return true;
			}
		});
		return binaryTargets;
	}

	public void setAllowAutoInflateBinaries(boolean theAllowAutoInflateBinaries) {
		myAllowAutoInflateBinaries = theAllowAutoInflateBinaries;
	}

	public boolean isAllowAutoInflateBinaries() {
		return myAllowAutoInflateBinaries;
	}

	private static class DeferredBinaryTarget {
		private final String myBlobId;
		private final IBinaryTarget myBinaryTarget;
		private final InputStream myDataStream;
		private boolean myBlobIdPrefixHookApplied;

		private DeferredBinaryTarget(String theBlobId, IBinaryTarget theBinaryTarget, byte[] theData) {
			myBlobId = theBlobId;
			myBinaryTarget = theBinaryTarget;
			myDataStream = new ByteArrayInputStream(theData);
		}

		String getBlobId() {
			return myBlobId;
		}

		IBinaryTarget getBinaryTarget() {
			return myBinaryTarget;
		}

		InputStream getDataStream() {
			return myDataStream;
		}

		boolean isBlobIdPrefixHookApplied() {
			return myBlobIdPrefixHookApplied;
		}

		void setBlobIdPrefixHookApplied(boolean theBlobIdPrefixHookApplied) {
			myBlobIdPrefixHookApplied = theBlobIdPrefixHookApplied;
		}
	}
}
