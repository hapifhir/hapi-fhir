package ca.uhn.fhir.util;

import ca.uhn.fhir.model.api.IModelJson;
import ca.uhn.fhir.model.api.annotation.SensitiveNoDisplay;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.collections4.MapUtils;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.time.ZonedDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

class JsonUtilTest {

	private static final Logger ourLog = LoggerFactory.getLogger(JsonUtilTest.class);

	@Test
	public void testSensitiveNoDisplayAnnotationIsHiddenFromBasicSerialization() {
		TestObject object = new TestObject();
		object.setPublicField("Public Value!");
		object.setSensitiveField("Sensitive Value!");

		String sensitiveExcluded  = JsonUtil.serializeOrInvalidRequest(object);
		assertThat(sensitiveExcluded).doesNotContain("Sensitive Value!");

		String sensitiveIncluded  = JsonUtil.serializeWithSensitiveData(object);
		assertThat(sensitiveIncluded).contains("Sensitive Value!");
	}

	@Test
	public void testSerializeOrInvalidRequest_withSerializableMap_doesNotThrow(){
		Map<String, Object> map = new HashMap<>();
		map.put("string_value", "some value");
		map.put("int_value", 1);
		map.put("decimal_value", 1.1);

		TestObject iModelJson = new TestObject();
		iModelJson.setPublicField("Public Value!");
		iModelJson.setSensitiveField("Sensitive Value!");
		map.put("imodeljson_value", iModelJson);

		String serializedJson = JsonUtil.serializeOrInvalidRequest(map);
		ourLog.info("Serialized serializedJson: {}", serializedJson);

		Map<?, ?> deserializedMap = JsonUtil.deserialize(serializedJson, Map.class);
		assertEquals("some value", deserializedMap.get("string_value"));
		assertEquals(1, deserializedMap.get("int_value"));
		assertEquals(1.1, deserializedMap.get("decimal_value"));
		assertEquals("Public Value!", ((Map<?, ?>)deserializedMap.get("imodeljson_value")).get("publicField"));
		assertNull(((Map<?, ?>)deserializedMap.get("imodeljson_value")).get("sensitiveField"));
	}

	@Test
	public void testSerializeOrInvalidRequest_withEmptyMap_doesNotThrow(){
		Map<String, Object> emptyMap = new HashMap<>();
		String serializedJson = JsonUtil.serializeOrInvalidRequest(emptyMap);
		Map<?, ?> deserializedMap = JsonUtil.deserialize(serializedJson, Map.class);
		assertTrue(MapUtils.isEmpty(deserializedMap));
	}

	@Test
	public void testSerializeOrInvalidRequest_withNonSerializableObject_throwsInvalidRequestException(){
		Map<String, Object> map = new HashMap<>();
		map.put("non_serializable", new NonSerializableObject("some value"));

		try {
			JsonUtil.serializeOrInvalidRequest(map);
			fail();
		} catch (InvalidRequestException e){
			String expected = "HAPI-1741: Failed to encode class java.util.HashMap";
			assertTrue(e.getMessage().contains(expected));
		}
	}

	@Test
	void testDeserializeList_withTypedPayload_roundTripsExpectedFields() throws IOException {
		String json = "[{\"publicField\":\"first\"},{\"publicField\":\"second\",\"sensitiveField\":\"masked\"}]";

		List<TestObject> result = JsonUtil.deserializeList(json, TestObject.class);

		assertEquals(2, result.size());
		assertEquals("first", result.get(0).getPublicField());
		assertNull(result.get(0).getPrivateField());
		assertEquals("second", result.get(1).getPublicField());
		assertEquals("masked", result.get(1).getPrivateField());
	}

	@Test
	void testDeserializeList_withJavaTimePayload_roundTripsExpectedValues() throws IOException {
		String json = """
				[
				  {"when":"2026-06-19T21:54:24-04:00"},
				  {"when":"2026-06-20T01:54:24Z"}
				]
				""";

		List<TemporalHolder> result = JsonUtil.deserializeList(json, TemporalHolder.class);

		assertEquals(2, result.size());
		assertThat(result.get(0).getWhen().toInstant()).isEqualTo(ZonedDateTime.parse("2026-06-19T21:54:24-04:00").toInstant());
		assertThat(result.get(1).getWhen().toInstant()).isEqualTo(ZonedDateTime.parse("2026-06-20T01:54:24Z").toInstant());
	}

	@Test
	void testDeserialize_fromInputStream_deserializesExpectedObject() throws IOException {
		String json = "{\"publicField\":\"streamed\"}";
		ByteArrayInputStream inputStream = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));

		TestObject result = JsonUtil.deserialize(inputStream, TestObject.class);

		assertEquals("streamed", result.getPublicField());
		assertNull(result.getPrivateField());
	}

	@Test
	void testSerialize_withPrettyPrintToggle_andNullExclusion() {
		Map<String, Object> payload = new LinkedHashMap<>();
		payload.put("alpha", "beta");
		payload.put("nullValue", null);

		String pretty = JsonUtil.serialize(payload, true);
		String nonPretty = JsonUtil.serialize(payload, false);

		assertThat(pretty).contains("\n").contains("\"alpha\"").doesNotContain("nullValue");
		assertEquals("{\"alpha\":\"beta\"}", nonPretty);
	}

	@Test
	void testSerializeToWriter_doesNotCloseWriter_andPreservesJsonContent() throws IOException {
		TrackingWriter trackingWriter = new TrackingWriter();
		TemporalHolder payload = new TemporalHolder();
		payload.setWhen(ZonedDateTime.parse("2026-06-19T21:54:24-04:00"));

		JsonUtil.serialize(payload, trackingWriter);

		assertThat(trackingWriter.isClosed()).isFalse();
		TemporalHolder decoded = JsonUtil.deserialize(trackingWriter.toString(), TemporalHolder.class);
		assertThat(decoded.getWhen().toInstant()).isEqualTo(payload.getWhen().toInstant());
	}

	@Test
	void testDeserialize_rejectsBlankInput() {
		assertThatThrownBy(() -> JsonUtil.deserialize(" ", Map.class)).isInstanceOf(IllegalArgumentException.class);
	}

	@JsonFilter(IModelJson.SENSITIVE_DATA_FILTER_NAME)
	static class TestObject implements IModelJson {

		@JsonProperty("sensitiveField")
		@SensitiveNoDisplay
		private String mySensitiveField;

		@JsonProperty(value = "publicField")
		private String myPublicField;

		public String getPrivateField() {
			return mySensitiveField;
		}

		public void setSensitiveField(String thePrivateField) {
			this.mySensitiveField = thePrivateField;
		}

		public String getPublicField() {
			return myPublicField;
		}

		public void setPublicField(String thePublicField) {
			this.myPublicField = thePublicField;
		}
	}

	static class NonSerializableObject {

		private final String myValue;

		public NonSerializableObject(String theValue) {
			myValue = theValue;
		}
	}

	static class TemporalHolder {
		@JsonProperty("when")
		private ZonedDateTime myWhen;

		public ZonedDateTime getWhen() {
			return myWhen;
		}

		public void setWhen(ZonedDateTime theWhen) {
			myWhen = theWhen;
		}
	}

	private static class TrackingWriter extends Writer {
		private final StringBuilder myBuffer = new StringBuilder();
		private boolean myClosed;

		@Override
		public void write(char[] theChars, int theOffset, int theLength) {
			myBuffer.append(theChars, theOffset, theLength);
		}

		@Override
		public void flush() {}

		@Override
		public void close() {
			myClosed = true;
		}

		boolean isClosed() {
			return myClosed;
		}

		@Override
		public String toString() {
			return myBuffer.toString();
		}
	}
}
