/**
 * This software has been produced by Akana, Inc. under a professional services
 * agreement with our customer. This work may contain material that is confidential 
 * and proprietary information of Akana, Inc. and is subject to copyright 
 * protection under laws of the United States of America and other countries. 
 * Akana, Inc. grants the customer non-exclusive rights to this material without
 * any warranty expressed or implied. 
 */
package ca.uhn.fhir.parser.json;

import java.io.PushbackReader;
import java.io.Reader;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.json.JsonValue;

import ca.uhn.fhir.parser.DataFormatException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSyntaxException;

/**
 * @author Akana, Inc. Professional Services
 *
 */
public class GsonStructure implements JsonLikeStructure {

	private JsonObject nativeObject;
	private JsonLikeObject jsonLikeObject = null;
	
	public GsonStructure() {
		super();
	}
	
	public GsonStructure (JsonObject json) {
		super();
		setNativeObject(json);
	}
	
	public void setNativeObject (JsonObject json) {
		this.nativeObject = json;
	}

	@Override
	public void load(Reader theReader) throws DataFormatException {
		PushbackReader pbr = new PushbackReader(theReader);
		try {
			while(true) {
				int nextInt;
					nextInt = pbr.read();
				if (nextInt == -1) {
					throw new DataFormatException("Did not find any content to parse");
				}
				if (nextInt == '{') {
					pbr.unread('{');
					break;
				}
				if (Character.isWhitespace(nextInt)) {
					continue;
				}
				throw new DataFormatException("Content does not appear to be FHIR JSON, first non-whitespace character was: '" + (char)nextInt + "' (must be '{')");
			}
		
			Gson gson = new GsonBuilder().disableHtmlEscaping().create();
		
			nativeObject = gson.fromJson(pbr, JsonObject.class);
		} catch (JsonSyntaxException e) {
			if (e.getMessage().startsWith("Unexpected char 39")) {
				throw new DataFormatException("Failed to parse JSON encoded FHIR content: " + e.getMessage() + " - This may indicate that single quotes are being used as JSON escapes where double quotes are required", e);
			}
			throw new DataFormatException("Failed to parse JSON encoded FHIR content: " + e.getMessage(), e);
		} catch (Exception e) {
			throw new DataFormatException("Failed to parse JSON content, error was: " + e.getMessage(), e);
		}
	}

	@Override
	public JsonLikeObject getRootObject() {
		if (null == jsonLikeObject) {
			jsonLikeObject = new GsonJsonObject(nativeObject);
		}
		return jsonLikeObject;
	}

	private class GsonJsonObject extends JsonLikeObject {
		private JsonObject nativeObject;
		private Set<String> keySet = null;
		private Map<String,JsonLikeValue> jsonLikeMap = new LinkedHashMap<String,JsonLikeValue>();
		
		public GsonJsonObject (JsonObject json) {
			this.nativeObject = json;
		}

		@Override
		public Set<String> keySet() {
			if (null == keySet) {
				keySet = new HashSet<String>();
				for (Entry<String,?> entry : nativeObject.entrySet()) {
					keySet.add(entry.getKey());
				}
			}
			return keySet;
		}

		@Override
		public JsonLikeValue get(String key) {
			JsonLikeValue result = jsonLikeMap.get(key);
			if (null == result) {
				result = new GsonJsonValue(nativeObject.get(key));
				jsonLikeMap.put(key, result);
			}
			return result;
		}
	}
	
	private class GsonJsonArray extends JsonLikeArray {
		private JsonArray nativeArray;
		private Map<Integer,JsonLikeValue> jsonLikeMap = new LinkedHashMap<Integer,JsonLikeValue>();
		
		public GsonJsonArray (JsonArray json) {
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
				result = new GsonJsonValue(nativeArray.get(index));
				jsonLikeMap.put(key, result);
			}
			return result;
		}
	}
	
	private class GsonJsonValue extends JsonLikeValue {
		private JsonElement nativeValue;
		private JsonLikeObject jsonLikeObject = null;
		private JsonLikeArray jsonLikeArray = null;
		
		public GsonJsonValue (JsonElement json) {
			this.nativeValue = json;
		}

		@Override
		public ValueType getJsonType() {
			if (null == nativeValue || nativeValue.isJsonNull()) {
				return ValueType.NULL;
			}
			if (nativeValue.isJsonObject()) {
				return ValueType.OBJECT;
			}
			if (nativeValue.isJsonArray()) {
				return ValueType.ARRAY;
			}
			if (nativeValue.isJsonPrimitive()) {
				return ValueType.SCALAR;
			}
			return null;
		}
		
		@Override
		public ScalarType getDataType() {
			if (nativeValue != null && nativeValue.isJsonPrimitive()) {
				if (((JsonPrimitive)nativeValue).isNumber()) {
					return ScalarType.NUMBER;
				}
				if (((JsonPrimitive)nativeValue).isString()) {
					return ScalarType.STRING;
				}
				if (((JsonPrimitive)nativeValue).isBoolean()) {
					return ScalarType.BOOLEAN;
				}
			}
			return null;
		}

		@Override
		public JsonLikeArray getAsArray() {
			if (nativeValue.isJsonArray()) {
				if (null == jsonLikeArray) {
					jsonLikeArray = new GsonJsonArray((JsonArray)nativeValue);
				}
			}
			return jsonLikeArray;
		}

		@Override
		public JsonLikeObject getAsObject() {
			if (nativeValue.isJsonObject()) {
				if (null == jsonLikeObject) {
					jsonLikeObject = new GsonJsonObject((JsonObject)nativeValue);
				}
			}
			return jsonLikeObject;
		}

		@Override
		public String getAsString() {
			return nativeValue.getAsString();
		}

		@Override
		public boolean getAsBoolean() {
			if (nativeValue.isJsonPrimitive() && ((JsonPrimitive)nativeValue).isBoolean()) {
				return nativeValue.getAsBoolean();
			}
			return super.getAsBoolean();
		}
	}
}
