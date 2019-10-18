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

import ca.uhn.fhir.context.BaseRuntimeChildDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementDefinition;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Interceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.util.IModelVisitor2;
import org.hl7.fhir.instance.model.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nonnull;
import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

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
	private Class<? extends IBaseBinary> myBinaryResourceType;
	private Class<? extends ICompositeType> myAttachmentType;

	@SuppressWarnings("unchecked")
	@PostConstruct
	public void start() {
		myBinaryResourceType = (Class<? extends IBaseBinary>) myCtx.getResourceDefinition("Binary").getImplementingClass();
		myAttachmentType = (Class<? extends ICompositeType>) myCtx.getElementDefinition("Attachment").getImplementingClass();
		myBinaryType = (Class<? extends IPrimitiveType<byte[]>>) myCtx.getElementDefinition("base64Binary").getImplementingClass();
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
	public void extractLargeBinariesBeforeCreate(IBaseResource theResource) throws IOException {
		extractLargeBinaries(theResource);
	}

	@Hook(Pointcut.STORAGE_PRESTORAGE_RESOURCE_UPDATED)
	public void extractLargeBinariesBeforeUpdate(IBaseResource theResource) throws IOException {
		extractLargeBinaries(theResource);
	}

	private void extractLargeBinaries(IBaseResource theResource) throws IOException {
		IIdType resourceId = theResource.getIdElement();

		List<IBinaryTarget> attachments = recursivelyScanResourceForBinaryData(theResource);
		for (IBinaryTarget nextTarget : attachments) {
			if (nextTarget.getData() != null) {

				long nextPayloadLength = nextTarget.getData().length;
				String nextContentType = nextTarget.getContentType();
				boolean shouldStoreBlob = myBinaryStorageSvc.shouldStoreBlob(nextPayloadLength, resourceId, nextContentType);
				if (shouldStoreBlob) {

					ByteArrayInputStream inputStream = new ByteArrayInputStream(nextTarget.getData());
					StoredDetails storedDetails = myBinaryStorageSvc.storeBlob(resourceId, nextContentType, inputStream);
					if (storedDetails != null) {
						String blobId = storedDetails.getBlobId();
						myBinaryAccessProvider.replaceDataWithExtension(nextTarget, blobId);
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
					IBase parent = theContainingElementPath.get(theContainingElementPath.size() - 1);
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



}
