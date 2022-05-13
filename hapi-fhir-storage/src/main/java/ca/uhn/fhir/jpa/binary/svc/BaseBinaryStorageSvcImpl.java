package ca.uhn.fhir.jpa.binary.svc;

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

import ca.uhn.fhir.context.BaseRuntimeChildDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementDefinition;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.binary.api.IBinaryStorageSvc;
import ca.uhn.fhir.jpa.binary.api.IBinaryTarget;
import ca.uhn.fhir.jpa.binary.api.StoredDetails;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.PayloadTooLargeException;
import ca.uhn.fhir.util.AttachmentUtil;
import ca.uhn.fhir.util.BinaryUtil;
import ca.uhn.fhir.util.HapiExtensions;
import ca.uhn.fhir.util.IModelVisitor2;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import com.google.common.hash.HashingInputStream;
import com.google.common.io.ByteStreams;
import org.apache.commons.io.input.CountingInputStream;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseBinary;
import org.hl7.fhir.instance.model.api.IBaseHasExtensions;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.ICompositeType;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nonnull;
import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public abstract class BaseBinaryStorageSvcImpl implements IBinaryStorageSvc {
	private final SecureRandom myRandom;
	private final String CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	private final int ID_LENGTH = 100;
	private int myMaximumBinarySize = Integer.MAX_VALUE;
	private int myMinimumBinarySize;

	private Class<? extends IPrimitiveType<byte[]>> myBinaryType;

	@PostConstruct
	public void start() {
		BaseRuntimeElementDefinition<?> base64Binary = myFhirContext.getElementDefinition("base64Binary");
		assert base64Binary != null;
		myBinaryType = (Class<? extends IPrimitiveType<byte[]>>) base64Binary.getImplementingClass();
	}
	@Autowired
	private FhirContext myFhirContext;

	public BaseBinaryStorageSvcImpl() {
		myRandom = new SecureRandom();
	}

	@Override
	public int getMaximumBinarySize() {
		return myMaximumBinarySize;
	}

	@Override
	public void setMaximumBinarySize(int theMaximumBinarySize) {
		Validate.inclusiveBetween(1, Integer.MAX_VALUE, theMaximumBinarySize);
		myMaximumBinarySize = theMaximumBinarySize;
	}

	@Override
	public int getMinimumBinarySize() {
		return myMinimumBinarySize;
	}

	@Override
	public void setMinimumBinarySize(int theMinimumBinarySize) {
		myMinimumBinarySize = theMinimumBinarySize;
	}

	@Override
	public String newBlobId() {
		StringBuilder b = new StringBuilder();
		for (int i = 0; i < ID_LENGTH; i++) {
			int nextInt = Math.abs(myRandom.nextInt());
			b.append(CHARS.charAt(nextInt % CHARS.length()));
		}
		return b.toString();
	}

	@Override
	public boolean shouldStoreBlob(long theSize, IIdType theResourceId, String theContentType) {
		return theSize >= getMinimumBinarySize();
	}

	@SuppressWarnings("UnstableApiUsage")
	@Nonnull
	protected HashingInputStream createHashingInputStream(InputStream theInputStream) {
		HashFunction hash = Hashing.sha256();
		return new HashingInputStream(hash, theInputStream);
	}

	@Nonnull
	protected CountingInputStream createCountingInputStream(InputStream theInputStream) {
		InputStream is = ByteStreams.limit(theInputStream, getMaximumBinarySize() + 1L);
		return new CountingInputStream(is) {
			@Override
			public int getCount() {
				int retVal = super.getCount();
				if (retVal > getMaximumBinarySize()) {
					throw new PayloadTooLargeException(Msg.code(1343) + "Binary size exceeds maximum: " + getMaximumBinarySize());
				}
				return retVal;
			}
		};
	}

	protected String provideIdForNewBlob(String theBlobIdOrNull) {
		String id = theBlobIdOrNull;
		if (isBlank(theBlobIdOrNull)) {
			id = newBlobId();
		}
		return id;
	}

	@Override
	public byte[] fetchDataBlobFromBinary(IBaseBinary theBaseBinary) throws IOException {
		IPrimitiveType<byte[]> dataElement = BinaryUtil.getOrCreateData(myFhirContext, theBaseBinary);
		byte[] value = dataElement.getValue();
		if (value == null) {
			Optional<String> attachmentId = getAttachmentId((IBaseHasExtensions) dataElement);
			if (attachmentId.isPresent()) {
				value = fetchBlob(theBaseBinary.getIdElement(), attachmentId.get());
			} else {
				throw new InternalErrorException(Msg.code(1344) + "Unable to load binary blob data for " + theBaseBinary.getIdElement());
			}
		}
		return value;
	}

	@SuppressWarnings("unchecked")
	private Optional<String> getAttachmentId(IBaseHasExtensions theBaseBinary) {
		return theBaseBinary
			.getExtension()
			.stream()
			.filter(t -> HapiExtensions.EXT_EXTERNALIZED_BINARY_ID.equals(t.getUrl()))
			.filter(t -> t.getValue() instanceof IPrimitiveType)
			.map(t -> (IPrimitiveType<String>) t.getValue())
			.map(t -> t.getValue())
			.filter(t -> isNotBlank(t))
			.findFirst();
	}

	@Override
	public long inflateAllBinariesInResource(IBaseResource theResource) throws IOException {
		return inflateBinariesInResourceUpTo(theResource, -1);
	}

	@Override
	public long inflateBinariesInResourceUpTo(IBaseResource theResource, long theMaximumBytes) throws IOException {
		long unmarshalledByteCount = 0;
		IIdType resourceId = theResource.getIdElement();
		List<IBinaryTarget> attachments = recursivelyScanResourceForBinaryData(theResource);
		for (IBinaryTarget nextTarget : attachments) {
			Optional<String> attachmentId = nextTarget.getExternalizedBlobId();
			if (attachmentId.isPresent()) {
				StoredDetails blobDetails = fetchBlobDetails(resourceId, attachmentId.get());
				if (blobDetails == null) {
					String msg = myFhirContext.getLocalizer().getMessage(BaseBinaryStorageSvcImpl.class, "unknownBlobId");
					throw new InvalidRequestException(Msg.code(1330) + msg);
				}

				if (theMaximumBytes < 0 || (unmarshalledByteCount + blobDetails.getBytes()) < theMaximumBytes) {
					byte[] bytes = fetchBlob(resourceId, attachmentId.get());
					nextTarget.setData(bytes);
					unmarshalledByteCount += blobDetails.getBytes();
				}
			}
		}
		return unmarshalledByteCount;
	}

	@Nonnull
	private List<IBinaryTarget> recursivelyScanResourceForBinaryData(IBaseResource theResource) {
		List<IBinaryTarget> binaryTargets = new ArrayList<>();
		myFhirContext.newTerser().visit(theResource, new IModelVisitor2() {
			@Override
			public boolean acceptElement(IBase theElement, List<IBase> theContainingElementPath, List<BaseRuntimeChildDefinition> theChildDefinitionPath, List<BaseRuntimeElementDefinition<?>> theElementDefinitionPath) {
				if (theElement.getClass().equals(myBinaryType)) {
					IBase parent = theContainingElementPath.get(theContainingElementPath.size() - 2);
					Optional<IBinaryTarget> binaryTarget = toBinaryTarget(parent);
					binaryTarget.ifPresent(binaryTargets::add);
				}
				return true;
			}
		});
		return binaryTargets;
	}

	@Override
	public Optional<IBinaryTarget> toBinaryTarget(IBase theElement) {
		IBinaryTarget binaryTarget = null;

		// Path is attachment
		BaseRuntimeElementDefinition<?> def = myFhirContext.getElementDefinition(theElement.getClass());
		if (def.getName().equals("Attachment")) {
			ICompositeType attachment = (ICompositeType) theElement;
			binaryTarget = new IBinaryTarget() {
				@Override
				public void setSize(Integer theSize) {
					AttachmentUtil.setSize(BaseBinaryStorageSvcImpl.this.myFhirContext, attachment, theSize);
				}

				@Override
				public String getContentType() {
					return AttachmentUtil.getOrCreateContentType(BaseBinaryStorageSvcImpl.this.myFhirContext, attachment).getValueAsString();
				}

				@Override
				public byte[] getData() {
					IPrimitiveType<byte[]> dataDt = AttachmentUtil.getOrCreateData(myFhirContext, attachment);
					return dataDt.getValue();
				}

				@Override
				public IBaseHasExtensions getTarget() {
					return (IBaseHasExtensions) AttachmentUtil.getOrCreateData(myFhirContext, attachment);
				}

				@Override
				public void setContentType(String theContentType) {
					AttachmentUtil.setContentType(BaseBinaryStorageSvcImpl.this.myFhirContext, attachment, theContentType);
				}


				@Override
				public void setData(byte[] theBytes) {
					AttachmentUtil.setData(myFhirContext, attachment, theBytes);
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
					return (IBaseHasExtensions) BinaryUtil.getOrCreateData(BaseBinaryStorageSvcImpl.this.myFhirContext, binary);
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
}
