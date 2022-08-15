package ca.uhn.fhir.jpa.binstore;

import ca.uhn.fhir.jpa.binary.svc.NullBinaryStorageSvcImpl;
import org.hl7.fhir.r4.model.IdType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class NullBinaryStorageSvcImplTest {

	private NullBinaryStorageSvcImpl mySvc = new NullBinaryStorageSvcImpl();

	@Test
	public void shouldStoreBlob() {
		assertFalse(mySvc.shouldStoreBlob(1, new IdType("Patient/2"), "application/json"));
	}

	@Test
	public void storeBlob() {
		assertThrows(UnsupportedOperationException.class, () -> {
			mySvc.storeBlob(null, null, null, null);
		});
	}

	@Test
	public void fetchBlobDetails() {
		assertThrows(UnsupportedOperationException.class, () -> {
			mySvc.fetchBlobDetails(null, null);
		});
	}

	@Test
	public void writeBlob() {
		assertThrows(UnsupportedOperationException.class, () -> {
			mySvc.writeBlob(null, null, null);
		});
	}

	@Test
	public void expungeBlob() {
		assertThrows(UnsupportedOperationException.class, () -> {
			mySvc.expungeBlob(null, null);
		});
	}

	@Test
	public void fetchBlob() {
		assertThrows(UnsupportedOperationException.class, () -> {
			mySvc.fetchBlob(null, null);
		});
	}

	@Test
	public void newBlobId() {
		assertThrows(UnsupportedOperationException.class, () -> {
			mySvc.newBlobId();
		});
	}
}
