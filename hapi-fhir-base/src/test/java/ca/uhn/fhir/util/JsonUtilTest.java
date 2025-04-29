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

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

class JsonUtilTest {

	private static final Logger ourLog = LoggerFactory.getLogger(JsonUtil.class);

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
}
