package ca.uhn.fhir.jpa.binstore;

import org.hl7.fhir.r4.model.IdType;
import org.junit.Test;

import static org.junit.Assert.assertFalse;

public class NullBinaryStorageSvcImplTest {

	private NullBinaryStorageSvcImpl mySvc = new NullBinaryStorageSvcImpl();

	@Test
	public void shouldStoreBlob() {
		assertFalse(mySvc.shouldStoreBlob(1, new IdType("Patient/2"), "application/json"));
	}

	@Test(expected = UnsupportedOperationException.class)
	public void storeBlob() {
		mySvc.storeBlob(null, null, null, null);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void fetchBlobDetails() {
		mySvc.fetchBlobDetails(null, null);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void writeBlob() {
		mySvc.writeBlob(null, null, null);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void expungeBlob() {
		mySvc.expungeBlob(null, null);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void fetchBlob() {
		mySvc.fetchBlob(null, null);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void newBlobId() {
		mySvc.newBlobId();
	}
}
