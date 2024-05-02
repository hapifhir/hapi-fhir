package ca.uhn.fhir.jpa.binstore;

import static org.junit.jupiter.api.Assertions.assertFalse;
import ca.uhn.fhir.jpa.binary.svc.NullBinaryStorageSvcImpl;
import org.hl7.fhir.r4.model.IdType;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;

public class NullBinaryStorageSvcImplTest {

	private final NullBinaryStorageSvcImpl mySvc = new NullBinaryStorageSvcImpl();

	@Test
	public void shouldStoreBinaryContent() {
		assertFalse(mySvc.shouldStoreBinaryContent(1, new IdType("Patient/2"), "application/json"));
	}

	@Test
	public void storeBlob() {
		assertThatExceptionOfType(UnsupportedOperationException.class).isThrownBy(() -> mySvc.storeBinaryContent(null, null, null, null, null));
	}

	@Test
	public void fetchBlobDetails() {
		assertThatExceptionOfType(UnsupportedOperationException.class).isThrownBy(() -> mySvc.fetchBinaryContentDetails(null, null));
	}

	@Test
	public void writeBlob() {
		assertThatExceptionOfType(UnsupportedOperationException.class).isThrownBy(() -> mySvc.writeBinaryContent(null, null, null));
	}

	@Test
	public void expungeBlob() {
		assertThatExceptionOfType(UnsupportedOperationException.class).isThrownBy(() -> mySvc.expungeBinaryContent(null, null));
	}

	@Test
	public void fetchBlob() {
		assertThatExceptionOfType(UnsupportedOperationException.class).isThrownBy(() -> mySvc.fetchBinaryContent(null, null));
	}

	@Test
	public void newBlobId() {
		assertThatExceptionOfType(UnsupportedOperationException.class).isThrownBy(() -> mySvc.newBinaryContentId());
	}
}
