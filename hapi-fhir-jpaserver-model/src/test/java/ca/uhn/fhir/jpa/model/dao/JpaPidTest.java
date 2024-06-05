package ca.uhn.fhir.jpa.model.dao;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.server.storage.SerializablePid;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.IdType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class JpaPidTest {

	private final FhirContext myFhirContext = FhirContext.forR4Cached();

	@Test
	public void fromSerializablePid_withValidJpaPid_returnsJpaPid() {
		// setup
		String resourceType = "Patient";
		long id = 1L;
		long version = 2;
		IdType idType = new IdType(resourceType + "/" + id);
		SerializablePid serializablePid = new SerializablePid(resourceType, String.valueOf(id));
		serializablePid.setAssociatedResourceId(idType);
		serializablePid.setVersion(version);

		// test
		JpaPid jpaPid = JpaPid.fromSerializablePid(serializablePid, myFhirContext);

		// verification
		assertEquals(id, jpaPid.getId());
		assertEquals(resourceType, jpaPid.getResourceType());
		assertEquals(version, jpaPid.getVersion());
		assertNotNull(jpaPid.getAssociatedResourceId());
		IIdType actual = jpaPid.getAssociatedResourceId();
		assertEquals(idType.getValueAsString(), actual.getValueAsString());
	}
}
