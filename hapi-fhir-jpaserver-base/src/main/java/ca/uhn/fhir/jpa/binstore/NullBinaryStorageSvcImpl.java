package ca.uhn.fhir.jpa.binstore;

import org.hl7.fhir.instance.model.api.IIdType;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

public class NullBinaryStorageSvcImpl implements IBinaryStorageSvc {

	@Override
	public boolean shouldStoreBlob(long theSize, IIdType theResourceId, String theContentType) {
		return false;
	}

	@Override
	public StoredDetails storeBlob(IIdType theResourceId, String theContentType, InputStream theInputStream) {
		throw new UnsupportedOperationException();
	}

	@Override
	public StoredDetails fetchBlobDetails(IIdType theResourceId, String theBlobId) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void writeBlob(IIdType theResourceId, String theBlobId, OutputStream theOutputStream) {
		throw new UnsupportedOperationException();
	}
}
