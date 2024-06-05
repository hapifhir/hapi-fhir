package ca.uhn.fhir.mdm.model.mdmevents;

import ca.uhn.fhir.mdm.api.MdmLinkSourceEnum;
import ca.uhn.fhir.mdm.api.MdmMatchResultEnum;
import ca.uhn.fhir.rest.api.server.storage.BaseResourcePersistentId;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hl7.fhir.r4.model.IdType;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MdmLinkJsonTest {

	private static class TestIdType {
		private final String myId;

		public TestIdType(String theId) {
			myId = theId;
		}

		public String getId() {
			return myId;
		}
	}

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
	@SuppressWarnings("unchecked")
	public void serializeDeserialize_complexId_works() throws JsonProcessingException {
		// setup
		MdmLinkJson json = createLinkJson();
		TestPID<TestIdType> golden = new TestPID<TestIdType>("Patient", new TestIdType("1"));
		golden.setAssociatedResourceId(new IdType("Patient/1"));
		golden.setVersion(1L);
		TestPID<TestIdType> source = new TestPID<TestIdType>("Patient", new TestIdType("2"));
		source.setAssociatedResourceId(new IdType("Patient/2"));
		source.setVersion(1L);
		json.setGoldenPid(golden);
		json.setSourcePid(source);

		// test
		String strVal = myObjectMapper.writeValueAsString(json);
		assertFalse(isBlank(strVal));
		assertTrue(strVal.contains("\"id\":{\"id\":\"1\""));
		assertTrue(strVal.contains("\"id\":{\"id\":\"2\""));

		MdmLinkJson deserialized = myObjectMapper.readValue(strVal, MdmLinkJson.class);
		assertNotNull(deserialized);
		assertTrue(deserialized.getGoldenPid().getId() instanceof Map);
		assertEquals(golden.getId().getId(), ((Map<String, String>)deserialized.getGoldenPid().getId()).get("id"));
		assertTrue(deserialized.getSourcePid().getId() instanceof Map);
		assertEquals(source.getId().getId(), ((Map<String, String>)deserialized.getSourcePid().getId()).get("id"));
	}

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
		assertTrue(strVal.contains("\"id\":1"));
		assertTrue(strVal.contains("\"id\":2"));

		MdmLinkJson deserialized = myObjectMapper.readValue(strVal, MdmLinkJson.class);
		assertNotNull(deserialized);
		assertTrue(deserialized.getGoldenPid().getId() instanceof Long);
		assertEquals(golden.getId(), deserialized.getGoldenPid().getId());
		assertTrue(deserialized.getSourcePid().getId() instanceof Long);
		assertEquals(source.getId(), deserialized.getSourcePid().getId());
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
