/*-
 * #%L
 * HAPI FHIR Storage api
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.binary.svc;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.binary.api.IBinaryStorageSvc;
import ca.uhn.fhir.jpa.util.RandomTextUtils;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.PayloadTooLargeException;
import ca.uhn.fhir.rest.server.util.CompositeInterceptorBroadcaster;
import ca.uhn.fhir.util.BinaryUtil;
import ca.uhn.fhir.util.HapiExtensions;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import com.google.common.hash.HashingInputStream;
import com.google.common.io.ByteStreams;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.apache.commons.io.input.CountingInputStream;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBaseBinary;
import org.hl7.fhir.instance.model.api.IBaseHasExtensions;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public abstract class BaseBinaryStorageSvcImpl implements IBinaryStorageSvc {
	public static long DEFAULT_MAXIMUM_BINARY_SIZE = Long.MAX_VALUE - 1;
	public static String BINARY_CONTENT_ID_PREFIX_APPLIED = "binary-content-id-prefix-applied";

	private final int ID_LENGTH = 100;
	private long myMaximumBinarySize = DEFAULT_MAXIMUM_BINARY_SIZE;
	private int myMinimumBinarySize;

	@Autowired
	private FhirContext myFhirContext;

	@Autowired
	private IInterceptorBroadcaster myInterceptorBroadcaster;

	public BaseBinaryStorageSvcImpl() {
		super();
	}

	@Override
	public long getMaximumBinarySize() {
		return myMaximumBinarySize;
	}

	@Override
	public void setMaximumBinarySize(long theMaximumBinarySize) {
		Validate.inclusiveBetween(1, DEFAULT_MAXIMUM_BINARY_SIZE, theMaximumBinarySize);
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
	public String newBinaryContentId() {
		return RandomTextUtils.newSecureRandomAlphaNumericString(ID_LENGTH);
	}

	/**
	 * Default implementation is to return true for any binary content ID.
	 */
	@Override
	public boolean isValidBinaryContentId(String theNewBinaryContentId) {
		return true;
	}

	@Override
	public boolean shouldStoreBinaryContent(long theSize, IIdType theResourceId, String theContentType) {
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
			public long getByteCount() {
				long retVal = super.getByteCount();
				if (retVal > getMaximumBinarySize()) {
					throw new PayloadTooLargeException(
							Msg.code(1343) + "Binary size exceeds maximum: " + getMaximumBinarySize());
				}
				return retVal;
			}
		};
	}

	@Deprecated(
			since =
					"6.6.0 - Maintained for interface backwards compatibility. Note that invokes interceptor pointcut with empty parameters",
			forRemoval = true)
	protected String provideIdForNewBinaryContent(String theBinaryContentIdOrNull) {
		return isNotBlank(theBinaryContentIdOrNull) ? theBinaryContentIdOrNull : newBinaryContentId();
	}

	@Nonnull
	protected String provideIdForNewBinaryContent(
			String theBinaryContentIdOrNull, byte[] theBytes, RequestDetails theRequestDetails, String theContentType) {
		String binaryContentId = isNotBlank(theBinaryContentIdOrNull) ? theBinaryContentIdOrNull : newBinaryContentId();

		// make sure another pointcut didn't already apply a prefix to the binaryContentId
		if (isBinaryContentIdPrefixApplied(theRequestDetails)) {
			return binaryContentId;
		}

		String binaryContentIdPrefixFromHooksOrNull =
				callBinaryContentIdPointcut(theBytes, theRequestDetails, theContentType);
		String binaryContentIdPrefixFromHooks = StringUtils.defaultString(binaryContentIdPrefixFromHooksOrNull);
		return binaryContentIdPrefixFromHooks + binaryContentId;
	}

	protected boolean isBinaryContentIdPrefixApplied(RequestDetails theRequestDetails) {
		return theRequestDetails.getUserData().get(BINARY_CONTENT_ID_PREFIX_APPLIED) == Boolean.TRUE;
	}

	public static void setBinaryContentIdPrefixApplied(RequestDetails theRequestDetails) {
		theRequestDetails.getUserData().put(BINARY_CONTENT_ID_PREFIX_APPLIED, true);
	}

	/**
	 * This invokes the {@link Pointcut#STORAGE_BINARY_ASSIGN_BINARY_CONTENT_ID_PREFIX} hook and returns the prefix to use for the binary content ID, or null if there are no implementers.
	 * @return A string, which will be used to prefix the binary content ID. May be null.
	 */
	@Nullable
	private String callBinaryContentIdPointcut(
			byte[] theBytes, RequestDetails theRequestDetails, String theContentType) {
		// TODO: to be removed when pointcut STORAGE_BINARY_ASSIGN_BLOB_ID_PREFIX has exceeded the grace period.
		// Deprecated in 7.2.0.
		boolean hasStorageBinaryAssignBlobIdPrefixHooks = CompositeInterceptorBroadcaster.hasHooks(
				Pointcut.STORAGE_BINARY_ASSIGN_BLOB_ID_PREFIX, myInterceptorBroadcaster, theRequestDetails);

		boolean hasStorageBinaryAssignBinaryContentIdPrefixHooks = CompositeInterceptorBroadcaster.hasHooks(
				Pointcut.STORAGE_BINARY_ASSIGN_BINARY_CONTENT_ID_PREFIX, myInterceptorBroadcaster, theRequestDetails);

		if (!(hasStorageBinaryAssignBlobIdPrefixHooks || hasStorageBinaryAssignBinaryContentIdPrefixHooks)) {
			return null;
		}

		IBaseBinary binary =
				BinaryUtil.newBinary(myFhirContext).setContent(theBytes).setContentType(theContentType);

		HookParams hookParams =
				new HookParams().add(RequestDetails.class, theRequestDetails).add(IBaseResource.class, binary);

		setBinaryContentIdPrefixApplied(theRequestDetails);

		Pointcut pointcutToInvoke = Pointcut.STORAGE_BINARY_ASSIGN_BINARY_CONTENT_ID_PREFIX;

		// TODO: to be removed when pointcut STORAGE_BINARY_ASSIGN_BLOB_ID_PREFIX has exceeded the grace period
		if (hasStorageBinaryAssignBlobIdPrefixHooks) {
			pointcutToInvoke = Pointcut.STORAGE_BINARY_ASSIGN_BLOB_ID_PREFIX;
		}

		return (String) CompositeInterceptorBroadcaster.doCallHooksAndReturnObject(
				myInterceptorBroadcaster, theRequestDetails, pointcutToInvoke, hookParams);
	}

	@Override
	public byte[] fetchDataByteArrayFromBinary(IBaseBinary theBaseBinary) throws IOException {
		IPrimitiveType<byte[]> dataElement = BinaryUtil.getOrCreateData(myFhirContext, theBaseBinary);
		byte[] value = dataElement.getValue();
		if (value == null) {
			Optional<String> attachmentId = getAttachmentId((IBaseHasExtensions) dataElement);
			if (attachmentId.isPresent()) {
				value = fetchBinaryContent(theBaseBinary.getIdElement(), attachmentId.get());
			} else {
				throw new InternalErrorException(
						Msg.code(1344) + "Unable to load binary content data for " + theBaseBinary.getIdElement());
			}
		}
		return value;
	}

	@SuppressWarnings("unchecked")
	private Optional<String> getAttachmentId(IBaseHasExtensions theBaseBinary) {
		return theBaseBinary.getExtension().stream()
				.filter(t -> HapiExtensions.EXT_EXTERNALIZED_BINARY_ID.equals(t.getUrl()))
				.filter(t -> t.getValue() instanceof IPrimitiveType)
				.map(t -> (IPrimitiveType<String>) t.getValue())
				.map(IPrimitiveType::getValue)
				.filter(StringUtils::isNotBlank)
				.findFirst();
	}

	@VisibleForTesting
	public void setInterceptorBroadcasterForTests(IInterceptorBroadcaster theInterceptorBroadcaster) {
		myInterceptorBroadcaster = theInterceptorBroadcaster;
	}

	@VisibleForTesting
	public void setFhirContextForTests(FhirContext theFhirContext) {
		myFhirContext = theFhirContext;
	}
}
