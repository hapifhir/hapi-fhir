package ca.uhn.fhir.parser.json;
/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2016 University Health Network
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.io.PushbackReader;
import java.io.Reader;
import java.io.Writer;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import ca.uhn.fhir.parser.DataFormatException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSyntaxException;

public class GsonStructure implements JsonLikeStructure {

	private JsonObject nativeObject;
	private JsonLikeObject jsonLikeObject = null;
	private GsonWriter jsonLikeWriter = null;
	
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
	public JsonLikeStructure getInstance() {
		return new GsonStructure();
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
	public JsonLikeWriter getJsonLikeWriter (Writer writer) {
		if (null == jsonLikeWriter) {
			jsonLikeWriter = new GsonWriter(writer);
		}
		return jsonLikeWriter;
	}

	@Override
	public JsonLikeObject getRootObject() {
		if (null == jsonLikeObject) {
			jsonLikeObject = new GsonJsonObject(nativeObject);
		}
		return jsonLikeObject;
	}

	private static class GsonJsonObject extends JsonLikeObject {
		private JsonObject nativeObject;
		private Set<String> keySet = null;
		private Map<String,JsonLikeValue> jsonLikeMap = new LinkedHashMap<String,JsonLikeValue>();
		
		public GsonJsonObject (JsonObject json) {
			this.nativeObject = json;
		}

		@Override
		public Set<String> keySet() {
			if (null == keySet) {
				Set<Entry<String, JsonElement>> entrySet = nativeObject.entrySet();
				keySet = new EntryOrderedSet<String>(entrySet.size());
				for (Entry<String,?> entry : entrySet) {
					keySet.add(entry.getKey());
				}
			}
			return keySet;
		}

		@Override
		public JsonLikeValue get(String key) {
			JsonLikeValue result = null;
			if (jsonLikeMap.containsKey(key)) {
				result = jsonLikeMap.get(key); 
			} else {
				JsonElement child = nativeObject.get(key);
				if (child != null) {
					result = new GsonJsonValue(child);
				}
				jsonLikeMap.put(key, result);
			}
			return result;
		}
	}
	
	private static class GsonJsonArray extends JsonLikeArray {
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
			JsonLikeValue result = null;
			if (jsonLikeMap.containsKey(key)) {
				result = jsonLikeMap.get(key); 
			} else {
				JsonElement child = nativeArray.get(index);
				if (child != null) {
					result = new GsonJsonValue(child);
				}
				jsonLikeMap.put(key, result);
			}
			return result;
		}
	}
	
	private static class GsonJsonValue extends JsonLikeValue {
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
			if (nativeValue != null && nativeValue.isJsonArray()) {
				if (null == jsonLikeArray) {
					jsonLikeArray = new GsonJsonArray((JsonArray)nativeValue);
				}
			}
			return jsonLikeArray;
		}

		@Override
		public JsonLikeObject getAsObject() {
			if (nativeValue != null && nativeValue.isJsonObject()) {
				if (null == jsonLikeObject) {
					jsonLikeObject = new GsonJsonObject((JsonObject)nativeValue);
				}
			}
			return jsonLikeObject;
		}

		@Override
		public String getAsString() {
			return nativeValue != null ? nativeValue.getAsString() : null;
		}

		@Override
		public boolean getAsBoolean() {
			if (nativeValue != null && nativeValue.isJsonPrimitive() && ((JsonPrimitive)nativeValue).isBoolean()) {
				return nativeValue.getAsBoolean();
			}
			return super.getAsBoolean();
		}
	}
	
	private static class EntryOrderedSet<T> extends AbstractSet<T> {
		private transient ArrayList<T> data = null;
		
		public EntryOrderedSet (int initialCapacity) {
			data = new ArrayList<T>(initialCapacity);
		}
		@SuppressWarnings("unused")
		public EntryOrderedSet () {
			data = new ArrayList<T>();
		}
		
		@Override
		public int size() {
			return data.size();
		}

		@Override
		public boolean contains(Object o) {
			return data.contains(o);
		}

		@SuppressWarnings("unused")  // not really.. just not here
		public T get(int index) {
			return data.get(index);
		}
		
		@Override
		public boolean add(T element) {
			if (data.contains(element)) {
				return false;
			}
			return data.add(element);
		}
		
		@Override
		public boolean remove(Object o) {
			return data.remove(o);
		}

		@Override
		public void clear() {
			data.clear();
		}
		
		@Override
		public Iterator<T> iterator() {
			return data.iterator();
		}
		
	}
}
