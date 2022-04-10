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

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.binary.api.IBinaryStorageSvc;
import ca.uhn.fhir.jpa.binary.api.StoredDetails;
import org.hl7.fhir.instance.model.api.IBaseBinary;
import org.hl7.fhir.instance.model.api.IIdType;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class NullBinaryStorageSvcImpl implements IBinaryStorageSvc {

	@Override
	public int getMaximumBinarySize() {
		return 0;
	}

	@Override
	public void setMaximumBinarySize(int theMaximumBinarySize) {
		// ignore
	}

	@Override
	public int getMinimumBinarySize() {
		return 0;
	}

	@Override
	public void setMinimumBinarySize(int theMinimumBinarySize) {
		// ignore
	}

	@Override
	public boolean shouldStoreBlob(long theSize, IIdType theResourceId, String theContentType) {
		return false;
	}

	@Override
	public String newBlobId() {
		throw new UnsupportedOperationException(Msg.code(1345));
	}

	@Override
	public StoredDetails storeBlob(IIdType theResourceId, String theBlobIdOrNull, String theContentType, InputStream theInputStream) {
		throw new UnsupportedOperationException(Msg.code(1346));
	}

	@Override
	public StoredDetails fetchBlobDetails(IIdType theResourceId, String theBlobId) {
		throw new UnsupportedOperationException(Msg.code(1347));
	}

	@Override
	public boolean writeBlob(IIdType theResourceId, String theBlobId, OutputStream theOutputStream) {
		throw new UnsupportedOperationException(Msg.code(1348));
	}

	@Override
	public void expungeBlob(IIdType theIdElement, String theBlobId) {
		throw new UnsupportedOperationException(Msg.code(1349));
	}

	@Override
	public byte[] fetchBlob(IIdType theResourceId, String theBlobId) {
		throw new UnsupportedOperationException(Msg.code(1350));
	}

	@Override
	public byte[] fetchDataBlobFromBinary(IBaseBinary theResource) throws IOException {
		throw new UnsupportedOperationException(Msg.code(1351));
	}
}
