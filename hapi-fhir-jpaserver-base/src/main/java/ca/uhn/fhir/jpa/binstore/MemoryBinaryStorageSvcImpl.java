package ca.uhn.fhir.jpa.binstore;

import com.google.common.hash.HashingInputStream;
import org.apache.commons.io.IOUtils;
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
	public StoredDetails storeBlob(IIdType theResourceId, String theContentType, InputStream theInputStream) throws IOException {
		String id = newRandomId();
		String key = toKey(theResourceId, id);

		HashingInputStream is = createHashingInputStream(theInputStream);

		byte[] bytes = IOUtils.toByteArray(is);
		theInputStream.close();
		myDataMap.put(key, bytes);
		StoredDetails storedDetails = new StoredDetails(id, bytes.length, theContentType, is, new Date());
		myDetailsMap.put(key, storedDetails);
		return storedDetails;
	}

	@Override
	public StoredDetails fetchBlobDetails(IIdType theResourceId, String theBlobId) {
		String key = toKey(theResourceId, theBlobId);
		return myDetailsMap.get(key);
	}

	@Override
	public void writeBlob(IIdType theResourceId, String theBlobId, OutputStream theOutputStream) throws IOException {
		String key = toKey(theResourceId, theBlobId);
		byte[] bytes = myDataMap.get(key);
		theOutputStream.write(bytes);
	}

	private String toKey(IIdType theResourceId, String theBlobId) {
		return theBlobId + '-' + theResourceId.toUnqualifiedVersionless().getValue();
	}

}
