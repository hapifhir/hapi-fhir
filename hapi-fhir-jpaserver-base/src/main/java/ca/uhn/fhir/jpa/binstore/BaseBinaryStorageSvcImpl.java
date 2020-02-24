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

import ca.uhn.fhir.rest.server.exceptions.PayloadTooLargeException;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import com.google.common.hash.HashingInputStream;
import com.google.common.io.ByteStreams;
import org.apache.commons.io.input.CountingInputStream;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IIdType;

import javax.annotation.Nonnull;
import java.io.InputStream;
import java.security.SecureRandom;

import static org.apache.commons.lang3.StringUtils.isBlank;

abstract class BaseBinaryStorageSvcImpl implements IBinaryStorageSvc {
	private final SecureRandom myRandom;
	private final String CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	private final int ID_LENGTH = 100;
	private int myMaximumBinarySize = Integer.MAX_VALUE;
	private int myMinimumBinarySize;

	BaseBinaryStorageSvcImpl() {
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
	HashingInputStream createHashingInputStream(InputStream theInputStream) {
		HashFunction hash = Hashing.sha256();
		return new HashingInputStream(hash, theInputStream);
	}

	@Nonnull
	CountingInputStream createCountingInputStream(InputStream theInputStream) {
		InputStream is = ByteStreams.limit(theInputStream, getMaximumBinarySize() + 1L);
		return new CountingInputStream(is) {
			@Override
			public int getCount() {
				int retVal = super.getCount();
				if (retVal > getMaximumBinarySize()) {
					throw new PayloadTooLargeException("Binary size exceeds maximum: " + getMaximumBinarySize());
				}
				return retVal;
			}
		};
	}


	String provideIdForNewBlob(String theBlobIdOrNull) {
		String id = theBlobIdOrNull;
		if (isBlank(theBlobIdOrNull)) {
			id = newBlobId();
		}
		return id;
	}
}
