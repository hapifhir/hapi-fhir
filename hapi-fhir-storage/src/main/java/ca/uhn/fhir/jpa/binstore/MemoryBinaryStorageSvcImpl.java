package ca.uhn.fhir.jpa.binstore;

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

import ca.uhn.fhir.jpa.binary.api.IBinaryStorageSvc;
import ca.uhn.fhir.jpa.binary.api.StoredDetails;
import ca.uhn.fhir.jpa.binary.svc.BaseBinaryStorageSvcImpl;
import com.google.common.hash.HashingInputStream;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.CountingInputStream;
import org.hl7.fhir.instance.model.api.IIdType;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Purely in-memory implementation of binary storage service. This is really
 * only appropriate for testing, since it doesn't persist anywhere and is
 * limited by the amount of available RAM.
 */
public class MemoryBinaryStorageSvcImpl extends BaseBinaryStorageSvcImpl implements IBinaryStorageSvc {

	private ConcurrentHashMap<String, byte[]> myDataMap = new ConcurrentHashMap<>();
	private ConcurrentHashMap<String, StoredDetails> myDetailsMap = new ConcurrentHashMap<>();

	/**
	 * Constructor
	 */
	public MemoryBinaryStorageSvcImpl() {
		super();
	}

	@Override
	public StoredDetails storeBlob(IIdType theResourceId, String theBlobIdOrNull, String theContentType, InputStream theInputStream) throws IOException {
		String id = super.provideIdForNewBlob(theBlobIdOrNull);
		String key = toKey(theResourceId, id);

		HashingInputStream hashingIs = createHashingInputStream(theInputStream);
		CountingInputStream countingIs = createCountingInputStream(hashingIs);

		byte[] bytes = IOUtils.toByteArray(countingIs);
		theInputStream.close();
		myDataMap.put(key, bytes);
		StoredDetails storedDetails = new StoredDetails(id, countingIs.getCount(), theContentType, hashingIs, new Date());
		myDetailsMap.put(key, storedDetails);
		return storedDetails;
	}

	@Override
	public StoredDetails fetchBlobDetails(IIdType theResourceId, String theBlobId) {
		String key = toKey(theResourceId, theBlobId);
		return myDetailsMap.get(key);
	}

	@Override
	public boolean writeBlob(IIdType theResourceId, String theBlobId, OutputStream theOutputStream) throws IOException {
		String key = toKey(theResourceId, theBlobId);
		byte[] bytes = myDataMap.get(key);
		if (bytes == null) {
			return false;
		}
		theOutputStream.write(bytes);
		return true;
	}

	@Override
	public void expungeBlob(IIdType theResourceId, String theBlobId) {
		String key = toKey(theResourceId, theBlobId);
		myDataMap.remove(key);
		myDetailsMap.remove(key);
	}

	@Override
	public byte[] fetchBlob(IIdType theResourceId, String theBlobId) {
		String key = toKey(theResourceId, theBlobId);
		return myDataMap.get(key);
	}

	private String toKey(IIdType theResourceId, String theBlobId) {
		return theBlobId + '-' + theResourceId.toUnqualifiedVersionless().getValue();
	}

	public void clear() {
		myDetailsMap.clear();
		myDataMap.clear();
	}
}
