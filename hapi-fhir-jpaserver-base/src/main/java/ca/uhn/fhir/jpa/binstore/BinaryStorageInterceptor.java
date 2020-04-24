package ca.uhn.fhir.jpa.binstore;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
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

import ca.uhn.fhir.context.BaseRuntimeChildDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementDefinition;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Interceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.dao.BaseHapiFhirDao;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.rest.api.server.IPreResourceShowDetails;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.util.IModelVisitor2;
import org.apache.commons.io.FileUtils;
import org.hl7.fhir.instance.model.api.*;
import org.hl7.fhir.r4.model.IdType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nonnull;
import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static ca.uhn.fhir.jpa.model.util.JpaConstants.EXT_EXTERNALIZED_BINARY_ID;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Interceptor
public class BinaryStorageInterceptor {

	private static final Logger ourLog = LoggerFactory.getLogger(BinaryStorageInterceptor.class);
	@Autowired
	private IBinaryStorageSvc myBinaryStorageSvc;
	@Autowired
	private FhirContext myCtx;
	@Autowired
	private BinaryAccessProvider myBinaryAccessProvider;
	private Class<? extends IPrimitiveType<byte[]>> myBinaryType;
	private String myDeferredListKey;
	private long myAutoDeExternalizeMaximumBytes = 10 * FileUtils.ONE_MB;

	/**
	 * Any externalized binaries will be rehydrated if their size is below this thhreshold when
	 * reading the resource back. Default is 10MB.
	 */
	public long getAutoDeExternalizeMaximumBytes() {
		return myAutoDeExternalizeMaximumBytes;
	}

	/**
	 * Any externalized binaries will be rehydrated if their size is below this thhreshold when
	 * reading the resource back. Default is 10MB.
	 */
	public void setAutoDeExternalizeMaximumBytes(long theAutoDeExternalizeMaximumBytes) {
		myAutoDeExternalizeMaximumBytes = theAutoDeExternalizeMaximumBytes;
	}

	@SuppressWarnings("unchecked")
	@PostConstruct
	public void start() {
		myBinaryType = (Class<? extends IPrimitiveType<byte[]>>) myCtx.getElementDefinition("base64Binary").getImplementingClass();
		myDeferredListKey = getClass().getName() + "_" + hashCode() + "_DEFERRED_LIST";
	}

	@Hook(Pointcut.STORAGE_PRESTORAGE_EXPUNGE_RESOURCE)
	public void expungeResource(AtomicInteger theCounter, IBaseResource theResource) {

		List<? extends IBase> binaryElements = myCtx.newTerser().getAllPopulatedChildElementsOfType(theResource, myBinaryType);

		List<String> attachmentIds = binaryElements
			.stream()
			.flatMap(t -> ((IBaseHasExtensions) t).getExtension().stream())
			.filter(t -> JpaConstants.EXT_EXTERNALIZED_BINARY_ID.equals(t.getUrl()))
			.map(t -> ((IPrimitiveType) t.getValue()).getValueAsString())
			.collect(Collectors.toList());

		for (String next : attachmentIds) {
			myBinaryStorageSvc.expungeBlob(theResource.getIdElement(), next);
			theCounter.incrementAndGet();

			ourLog.info("Deleting binary blob {} because resource {} is being expunged", next, theResource.getIdElement().getValue());
		}

	}

	@Hook(Pointcut.STORAGE_PRESTORAGE_RESOURCE_CREATED)
	public void extractLargeBinariesBeforeCreate(ServletRequestDetails theRequestDetails, IBaseResource theResource, Pointcut thePointcut) throws IOException {
		extractLargeBinaries(theRequestDetails, theResource, thePointcut);
	}

	@Hook(Pointcut.STORAGE_PRESTORAGE_RESOURCE_UPDATED)
	public void extractLargeBinariesBeforeUpdate(ServletRequestDetails theRequestDetails, IBaseResource thePreviousResource, IBaseResource theResource, Pointcut thePointcut) throws IOException {
		blockIllegalExternalBinaryIds(thePreviousResource, theResource);
		extractLargeBinaries(theRequestDetails, theResource, thePointcut);
	}

	/**
	 * Don't allow clients to submit resources with binary storage attachments declared unless the ID was already in the
	 * resource. In other words, only HAPI itself may add a binary storage ID extension to a resource unless that
	 * extension was already present.
	 */
	private void blockIllegalExternalBinaryIds(IBaseResource thePreviousResource, IBaseResource theResource) {
		Set<String> existingBinaryIds = new HashSet<>();
		if (thePreviousResource != null) {
			List<? extends IPrimitiveType<byte[]>> base64fields = myCtx.newTerser().getAllPopulatedChildElementsOfType(thePreviousResource, myBinaryType);
			for (IPrimitiveType<byte[]> nextBase64 : base64fields) {
				if (nextBase64 instanceof IBaseHasExtensions) {
					((IBaseHasExtensions) nextBase64)
						.getExtension()
						.stream()
						.filter(t -> t.getUserData(JpaConstants.EXTENSION_EXT_SYSTEMDEFINED) == null)
						.filter(t -> EXT_EXTERNALIZED_BINARY_ID.equals(t.getUrl()))
						.map(t -> (IPrimitiveType) t.getValue())
						.map(t -> t.getValueAsString())
						.filter(t -> isNotBlank(t))
						.forEach(t -> existingBinaryIds.add(t));
				}
			}
		}

		List<? extends IPrimitiveType<byte[]>> base64fields = myCtx.newTerser().getAllPopulatedChildElementsOfType(theResource, myBinaryType);
		for (IPrimitiveType<byte[]> nextBase64 : base64fields) {
			if (nextBase64 instanceof IBaseHasExtensions) {
				Optional<String> hasExternalizedBinaryReference = ((IBaseHasExtensions) nextBase64)
					.getExtension()
					.stream()
					.filter(t -> t.getUserData(JpaConstants.EXTENSION_EXT_SYSTEMDEFINED) == null)
					.filter(t -> t.getUrl().equals(EXT_EXTERNALIZED_BINARY_ID))
					.map(t->(IPrimitiveType) t.getValue())
					.map(t->t.getValueAsString())
					.filter(t->isNotBlank(t))
					.filter(t->{
						return !existingBinaryIds.contains(t);
					}).findFirst();

				if (hasExternalizedBinaryReference.isPresent()) {
					String msg = myCtx.getLocalizer().getMessage(BaseHapiFhirDao.class, "externalizedBinaryStorageExtensionFoundInRequestBody", EXT_EXTERNALIZED_BINARY_ID, hasExternalizedBinaryReference.get());
					throw new InvalidRequestException(msg);
				}
			}
		}

	}

	private void extractLargeBinaries(ServletRequestDetails theRequestDetails, IBaseResource theResource, Pointcut thePointcut) throws IOException {
		if (theRequestDetails == null) {
			// RequestDetails will only be null for internal HAPI events.  If externalization is required for them it will need to be done in a different way.
			return;
		}
		IIdType resourceId = theResource.getIdElement();
		if (!resourceId.hasResourceType() && resourceId.hasIdPart()) {
			String resourceType = myCtx.getResourceDefinition(theResource).getName();
			resourceId = new IdType(resourceType + "/" + resourceId.getIdPart());
		}

		List<IBinaryTarget> attachments = recursivelyScanResourceForBinaryData(theResource);
		for (IBinaryTarget nextTarget : attachments) {
			byte[] data = nextTarget.getData();
			if (data != null && data.length > 0) {

				long nextPayloadLength = data.length;
				String nextContentType = nextTarget.getContentType();
				boolean shouldStoreBlob = myBinaryStorageSvc.shouldStoreBlob(nextPayloadLength, resourceId, nextContentType);
				if (shouldStoreBlob) {

					String newBlobId;
					if (resourceId.hasIdPart()) {
						ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
						StoredDetails storedDetails = myBinaryStorageSvc.storeBlob(resourceId, null, nextContentType, inputStream);
						newBlobId = storedDetails.getBlobId();
					} else {
						assert thePointcut == Pointcut.STORAGE_PRESTORAGE_RESOURCE_CREATED : thePointcut.name();
						newBlobId = myBinaryStorageSvc.newBlobId();
						List<DeferredBinaryTarget> deferredBinaryTargets = getOrCreateDeferredBinaryStorageMap(theRequestDetails);
						DeferredBinaryTarget newDeferredBinaryTarget = new DeferredBinaryTarget(newBlobId, nextTarget, data);
						deferredBinaryTargets.add(newDeferredBinaryTarget);
					}

					myBinaryAccessProvider.replaceDataWithExtension(nextTarget, newBlobId);
				}
			}

		}
	}

	@Nonnull
	@SuppressWarnings("unchecked")
	private List<DeferredBinaryTarget> getOrCreateDeferredBinaryStorageMap(ServletRequestDetails theRequestDetails) {
		List<DeferredBinaryTarget> deferredBinaryTargets = (List<DeferredBinaryTarget>) theRequestDetails.getUserData().get(getDeferredListKey());
		if (deferredBinaryTargets == null) {
			deferredBinaryTargets = new ArrayList<>();
			theRequestDetails.getUserData().put(getDeferredListKey(), deferredBinaryTargets);
		}
		return deferredBinaryTargets;
	}

	@SuppressWarnings("unchecked")
	@Hook(Pointcut.STORAGE_PRECOMMIT_RESOURCE_CREATED)
	public void storeLargeBinariesBeforeCreatePersistence(ServletRequestDetails theRequestDetails, IBaseResource theResource, Pointcut thePoincut) throws IOException {
		if (theRequestDetails == null) {
			return;
		}
		List<DeferredBinaryTarget> deferredBinaryTargets = (List<DeferredBinaryTarget>) theRequestDetails.getUserData().get(getDeferredListKey());
		if (deferredBinaryTargets != null) {
			IIdType resourceId = theResource.getIdElement();
			for (DeferredBinaryTarget next : deferredBinaryTargets) {
				String blobId = next.getBlobId();
				IBinaryTarget target = next.getBinaryTarget();
				InputStream dataStream = next.getDataStream();
				String contentType = target.getContentType();
				myBinaryStorageSvc.storeBlob(resourceId, blobId, contentType, dataStream);
			}
		}
	}

	private String getDeferredListKey() {
		return myDeferredListKey;
	}

	@Hook(Pointcut.STORAGE_PRESHOW_RESOURCES)
	public void preShow(IPreResourceShowDetails theDetails) throws IOException {
		long unmarshalledByteCount = 0;

		for (IBaseResource nextResource : theDetails) {

			IIdType resourceId = nextResource.getIdElement();
			List<IBinaryTarget> attachments = recursivelyScanResourceForBinaryData(nextResource);

			for (IBinaryTarget nextTarget : attachments) {
				Optional<String> attachmentId = nextTarget.getAttachmentId();
				if (attachmentId.isPresent()) {

					StoredDetails blobDetails = myBinaryStorageSvc.fetchBlobDetails(resourceId, attachmentId.get());
					if (blobDetails == null) {
						String msg = myCtx.getLocalizer().getMessage(BinaryAccessProvider.class, "unknownBlobId");
						throw new InvalidRequestException(msg);
					}

					if ((unmarshalledByteCount + blobDetails.getBytes()) < myAutoDeExternalizeMaximumBytes) {

						byte[] bytes = myBinaryStorageSvc.fetchBlob(resourceId, attachmentId.get());
						nextTarget.setData(bytes);
						unmarshalledByteCount += blobDetails.getBytes();
					}
				}
			}

		}
	}

	@Nonnull
	private List<IBinaryTarget> recursivelyScanResourceForBinaryData(IBaseResource theResource) {
		List<IBinaryTarget> binaryTargets = new ArrayList<>();
		myCtx.newTerser().visit(theResource, new IModelVisitor2() {
			@Override
			public boolean acceptElement(IBase theElement, List<IBase> theContainingElementPath, List<BaseRuntimeChildDefinition> theChildDefinitionPath, List<BaseRuntimeElementDefinition<?>> theElementDefinitionPath) {

				if (theElement.getClass().equals(myBinaryType)) {
					IBase parent = theContainingElementPath.get(theContainingElementPath.size() - 2);
					Optional<IBinaryTarget> binaryTarget = myBinaryAccessProvider.toBinaryTarget(parent);
					if (binaryTarget.isPresent()) {
						binaryTargets.add(binaryTarget.get());
					}
				}
				return true;
			}
		});
		return binaryTargets;
	}

	private static class DeferredBinaryTarget {
		private final String myBlobId;
		private final IBinaryTarget myBinaryTarget;
		private final InputStream myDataStream;

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
	}


}
