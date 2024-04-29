package ca.uhn.fhir.jpa.binstore;

import ca.uhn.fhir.jpa.binary.svc.NullBinaryStorageSvcImpl;
import org.hl7.fhir.r4.model.IdType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class NullBinaryStorageSvcImplTest {

	private final NullBinaryStorageSvcImpl mySvc = new NullBinaryStorageSvcImpl();

	@Test
	public void shouldStoreBinaryContent() {
		assertFalse(mySvc.shouldStoreBinaryContent(1, new IdType("Patient/2"), "application/json"));
	}

	@Test
	public void storeBinaryContent() {
		assertThrows(UnsupportedOperationException.class, () -> mySvc.storeBinaryContent(null, null, null, null, null));
	}

	@Test
	public void fetchBinaryContentDetails() {
		assertThrows(UnsupportedOperationException.class, () -> mySvc.fetchBinaryContentDetails(null, null));
	}

	@Test
	public void writeBinaryContent() {
		assertThrows(UnsupportedOperationException.class, () -> mySvc.writeBinaryContent(null, null, null));
	}

	@Test
	public void expungeBinaryContent() {
		assertThrows(UnsupportedOperationException.class, () -> mySvc.expungeBinaryContent(null, null));
	}

	@Test
	public void fetchBinaryContent() {
		assertThrows(UnsupportedOperationException.class, () -> mySvc.fetchBinaryContent(null, null));
	}

	@Test
	public void newBinaryContentId() {
		assertThrows(UnsupportedOperationException.class, () -> mySvc.newBinaryContentId());
	}
}
