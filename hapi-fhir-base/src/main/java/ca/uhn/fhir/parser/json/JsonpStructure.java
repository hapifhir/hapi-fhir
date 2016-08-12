/**
 * This software has been produced by Akana, Inc. under a professional services
 * agreement with our customer. This work may contain material that is confidential 
 * and proprietary information of Akana, Inc. and is subject to copyright 
 * protection under laws of the United States of America and other countries. 
 * Akana, Inc. grants the customer non-exclusive rights to this material without
 * any warranty expressed or implied. 
 */
package ca.uhn.fhir.parser.json;

import java.io.Reader;
import java.io.Writer;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;
import javax.json.stream.JsonParsingException;

import ca.uhn.fhir.parser.DataFormatException;

/**
 * @author Akana, Inc. Professional Services
 *
 */
public class JsonpStructure implements JsonLikeStructure {

	private static final Map<JsonValue.ValueType, JsonLikeValue.ValueType> jsonTypeMap;
	static {
		jsonTypeMap = new HashMap<JsonValue.ValueType, JsonLikeValue.ValueType>();
		jsonTypeMap.put(JsonValue.ValueType.ARRAY, JsonLikeValue.ValueType.ARRAY);
		jsonTypeMap.put(JsonValue.ValueType.OBJECT, JsonLikeValue.ValueType.OBJECT);
		jsonTypeMap.put(JsonValue.ValueType.STRING, JsonLikeValue.ValueType.SCALAR);
		jsonTypeMap.put(JsonValue.ValueType.NUMBER, JsonLikeValue.ValueType.SCALAR);
		jsonTypeMap.put(JsonValue.ValueType.TRUE, JsonLikeValue.ValueType.SCALAR);
		jsonTypeMap.put(JsonValue.ValueType.FALSE, JsonLikeValue.ValueType.SCALAR);
		jsonTypeMap.put(JsonValue.ValueType.NULL, JsonLikeValue.ValueType.NULL);
	}

	private static final Map<JsonValue.ValueType, JsonLikeValue.ScalarType> dataTypeMap;
	static {
		dataTypeMap = new HashMap<JsonValue.ValueType, JsonLikeValue.ScalarType>();
		dataTypeMap.put(JsonValue.ValueType.ARRAY, null);
		dataTypeMap.put(JsonValue.ValueType.OBJECT, null);
		dataTypeMap.put(JsonValue.ValueType.STRING, JsonLikeValue.ScalarType.STRING);
		dataTypeMap.put(JsonValue.ValueType.NUMBER, JsonLikeValue.ScalarType.NUMBER);
		dataTypeMap.put(JsonValue.ValueType.TRUE, JsonLikeValue.ScalarType.BOOLEAN);
		dataTypeMap.put(JsonValue.ValueType.FALSE, JsonLikeValue.ScalarType.BOOLEAN);
		dataTypeMap.put(JsonValue.ValueType.NULL, null);
	}
	
	private JsonObject nativeObject;
	private JsonLikeObject jsonLikeObject = null;
	private JsonpWriter jsonLikeWriter = null;
	
	public JsonpStructure() {
		super();
	}
	
	public JsonpStructure (JsonObject json) {
		super();
		setNativeObject(json);
	}
	
	public void setNativeObject (JsonObject json) {
		this.nativeObject = json;
	}

	@Override
	public JsonLikeStructure getInstance() {
		return new JsonpStructure();
	}

	@Override
	public void load(Reader theReader) throws DataFormatException {
		try {
			JsonReader reader = Json.createReader(theReader);
			this.nativeObject = reader.readObject();
		} catch (JsonParsingException e) {
			if (e.getMessage().startsWith("Unexpected char 39")) {
				throw new DataFormatException("Failed to parse JSON encoded FHIR content: " + e.getMessage() + " - This may indicate that single quotes are being used as JSON escapes where double quotes are required", e);
			}
			throw new DataFormatException("Failed to parse JSON encoded FHIR content: " + e.getMessage(), e);
		}
	}

	@Override
	public JsonLikeWriter getJsonLikeWriter (Writer writer) {
		if (null == jsonLikeWriter) {
			jsonLikeWriter = new JsonpWriter(writer);
		}
		return jsonLikeWriter;
	}

	@Override
	public JsonLikeObject getRootObject() {
		if (null == jsonLikeObject) {
			jsonLikeObject = new JsonpObject(nativeObject);
		}
		return jsonLikeObject;
	}

	private class JsonpObject extends JsonLikeObject {
		private JsonObject nativeObject;
		private Map<String,JsonLikeValue> jsonLikeMap = new LinkedHashMap<String,JsonLikeValue>();
		
		public JsonpObject (JsonObject json) {
			this.nativeObject = json;
		}

		@Override
		public Set<String> keySet() {
			return nativeObject.keySet();
		}

		@Override
		public JsonLikeValue get(String key) {
			JsonLikeValue result = jsonLikeMap.get(key);
			if (null == result) {
				result = new JsonpValue(nativeObject.get(key));
				jsonLikeMap.put(key, result);
			}
			return result;
		}
	}
	
	private class JsonpArray extends JsonLikeArray {
		private JsonArray nativeArray;
		private Map<Integer,JsonLikeValue> jsonLikeMap = new LinkedHashMap<Integer,JsonLikeValue>();
		
		public JsonpArray (JsonArray json) {
			this.nativeArray = json;
		}

		@Override
		public int size() {
			return nativeArray.size();
		}

		@Override
		public JsonLikeValue get(int index) {
			Integer key = Integer.valueOf(index);
			JsonLikeValue result = jsonLikeMap.get(key);
			if (null == result) {
				result = new JsonpValue(nativeArray.get(index));
				jsonLikeMap.put(key, result);
			}
			return result;
		}
	}
	
	private class JsonpValue extends JsonLikeValue {
		private JsonValue nativeValue;
		private JsonLikeObject jsonLikeObject = null;
		private JsonLikeArray jsonLikeArray = null;
		
		public JsonpValue (JsonValue json) {
			this.nativeValue = json;
		}
		
		@Override
		public ValueType getJsonType() {
			return jsonTypeMap.get(nativeValue.getValueType());
		}
		
		@Override
		public ScalarType getDataType() {
			return dataTypeMap.get(nativeValue.getValueType());
		}

		@Override
		public JsonLikeArray getAsArray() {
			if (nativeValue.getValueType() == JsonValue.ValueType.ARRAY) {
				if (null == jsonLikeArray) {
					jsonLikeArray = new JsonpArray((JsonArray)nativeValue);
				}
			}
			return jsonLikeArray;
		}

		@Override
		public JsonLikeObject getAsObject() {
			if (nativeValue.getValueType() == JsonValue.ValueType.OBJECT) {
				if (null == jsonLikeObject) {
					jsonLikeObject = new JsonpObject((JsonObject)nativeValue);
				}
			}
			return jsonLikeObject;
		}

		@Override
		public String getAsString() {
			return nativeValue.toString();
		}

		@Override
		public boolean getAsBoolean() {
			if (nativeValue.getValueType() == JsonValue.ValueType.TRUE) {
				return true;
			}
			if (nativeValue.getValueType() == JsonValue.ValueType.FALSE) {
				return false;
			}
			return super.getAsBoolean();
		}
	}
}
