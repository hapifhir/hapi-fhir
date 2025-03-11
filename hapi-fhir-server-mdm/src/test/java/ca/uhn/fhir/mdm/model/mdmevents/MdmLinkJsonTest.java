package ca.uhn.fhir.mdm.model.mdmevents;

import ca.uhn.fhir.mdm.api.MdmLinkSourceEnum;
import ca.uhn.fhir.mdm.api.MdmMatchResultEnum;
import ca.uhn.fhir.rest.api.server.storage.BaseResourcePersistentId;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hl7.fhir.r4.model.IdType;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class MdmLinkJsonTest {

	private static class TestPID<T> extends BaseResourcePersistentId<T> {

		private final T myId;

		protected TestPID(String theResourceType, T theId) {
			super(theResourceType);
			myId = theId;
		}

		@Override
		public T getId() {
			return myId;
		}
	}

	private final ObjectMapper myObjectMapper = new ObjectMapper();

	@Test
	public void serializeDeserialize_longId_works() throws JsonProcessingException {
		// setup
		MdmLinkJson json = createLinkJson();
		TestPID<Long> golden = new TestPID<>("Patient", 1L);
		golden.setAssociatedResourceId(new IdType("Patient/1"));
		golden.setVersion(1L);
		TestPID<Long> source = new TestPID<>("Patient", 2L);
		source.setAssociatedResourceId(new IdType("Patient/2"));
		source.setVersion(1L);
		json.setGoldenPid(golden);
		json.setSourcePid(source);

		// test
		String strVal = myObjectMapper.writeValueAsString(json);
		assertFalse(isBlank(strVal));

		MdmLinkJson deserialized = myObjectMapper.readValue(strVal, MdmLinkJson.class);
		assertNotNull(deserialized);
		assertNull(deserialized.getSourcePid());
		assertNull(deserialized.getGoldenPid());
	}

	private MdmLinkJson createLinkJson() {
		MdmLinkJson json = new MdmLinkJson();
		json.setGoldenResourceId("Patient/1");
		json.setSourceId("Patient/2");
		json.setMatchResult(MdmMatchResultEnum.MATCH);
		json.setLinkSource(MdmLinkSourceEnum.MANUAL);
		json.setCreated(new Date());
		json.setUpdated(new Date());
		return json;
	}
}
