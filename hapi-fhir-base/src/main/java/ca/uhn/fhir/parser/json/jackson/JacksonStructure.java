package ca.uhn.fhir.parser.json.jackson;

/*-
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.parser.json.JsonLikeArray;
import ca.uhn.fhir.parser.json.JsonLikeObject;
import ca.uhn.fhir.parser.json.JsonLikeStructure;
import ca.uhn.fhir.parser.json.JsonLikeValue;
import ca.uhn.fhir.parser.json.JsonLikeWriter;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.DecimalNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.io.PushbackReader;
import java.io.Reader;
import java.io.Writer;
import java.math.BigDecimal;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class JacksonStructure implements JsonLikeStructure {

	private static final ObjectMapper OBJECT_MAPPER = createObjectMapper();
	private JacksonWriter jacksonWriter;
	private ROOT_TYPE rootType = null;
	private JsonNode nativeRoot = null;
	private JsonNode jsonLikeRoot = null;

	public void setNativeObject(ObjectNode objectNode) {
		this.rootType = ROOT_TYPE.OBJECT;
		this.nativeRoot = objectNode;
	}

	public void setNativeArray(ArrayNode arrayNode) {
		this.rootType = ROOT_TYPE.ARRAY;
		this.nativeRoot = arrayNode;
	}

	@Override
	public JsonLikeStructure getInstance() {
		return new JacksonStructure();
	}

	@Override
	public void load(Reader theReader) throws DataFormatException {
		this.load(theReader, false);
	}

	@Override
	public void load(Reader theReader, boolean allowArray) throws DataFormatException {
		PushbackReader pbr = new PushbackReader(theReader);
		int nextInt;
		try {
			while (true) {
				nextInt = pbr.read();
				if (nextInt == -1) {
					throw new DataFormatException(Msg.code(1857) + "Did not find any content to parse");
				}
				if (nextInt == '{') {
					pbr.unread(nextInt);
					break;
				}
				if (Character.isWhitespace(nextInt)) {
					continue;
				}
				if (allowArray) {
					if (nextInt == '[') {
						pbr.unread(nextInt);
						break;
					}
					throw new DataFormatException(Msg.code(1858) + "Content does not appear to be FHIR JSON, first non-whitespace character was: '" + (char) nextInt + "' (must be '{' or '[')");
				}
				throw new DataFormatException(Msg.code(1859) + "Content does not appear to be FHIR JSON, first non-whitespace character was: '" + (char) nextInt + "' (must be '{')");
			}

			if (nextInt == '{') {
				setNativeObject((ObjectNode) OBJECT_MAPPER.readTree(pbr));
			} else {
				setNativeArray((ArrayNode) OBJECT_MAPPER.readTree(pbr));
			}
		} catch (Exception e) {
			if (e.getMessage().startsWith("Unexpected char 39")) {
				throw new DataFormatException(Msg.code(1860) + "Failed to parse JSON encoded FHIR content: " + e.getMessage() + " - " +
					"This may indicate that single quotes are being used as JSON escapes where double quotes are required", e);
			}
			throw new DataFormatException(Msg.code(1861) + "Failed to parse JSON encoded FHIR content: " + e.getMessage(), e);
		}
	}

	@Override
	public JsonLikeWriter getJsonLikeWriter(Writer writer) throws IOException {
		if (null == jacksonWriter) {
			jacksonWriter = new JacksonWriter(OBJECT_MAPPER.getFactory(), writer);
		}

		return jacksonWriter;
	}

	@Override
	public JsonLikeWriter getJsonLikeWriter() {
		if (null == jacksonWriter) {
			jacksonWriter = new JacksonWriter();
		}
		return jacksonWriter;
	}

	@Override
	public JsonLikeObject getRootObject() throws DataFormatException {
		if (rootType == ROOT_TYPE.OBJECT) {
			if (null == jsonLikeRoot) {
				jsonLikeRoot = nativeRoot;
			}

			return new JacksonJsonObject((ObjectNode) jsonLikeRoot);
		}

		throw new DataFormatException(Msg.code(1862) + "Content must be a valid JSON Object. It must start with '{'.");
	}

	private enum ROOT_TYPE {OBJECT, ARRAY}

	private static class JacksonJsonObject extends JsonLikeObject {
		private final ObjectNode nativeObject;

		public JacksonJsonObject(ObjectNode json) {
			this.nativeObject = json;
		}

		@Override
		public Object getValue() {
			return null;
		}

		@Override
		public Iterator<String> keyIterator() {
			return nativeObject.fieldNames();
		}

		@Override
		public JsonLikeValue get(String key) {
			JsonNode child = nativeObject.get(key);
			if (child != null) {
				return new JacksonJsonValue(child);
			}
			return null;
		}
	}

	private static class EntryOrderedSet<T> extends AbstractSet<T> {
		private final transient ArrayList<T> data;

		public EntryOrderedSet() {
			data = new ArrayList<>();
		}

		@Override
		public int size() {
			return data.size();
		}

		@Override
		public boolean contains(Object o) {
			return data.contains(o);
		}

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

	private static class JacksonJsonArray extends JsonLikeArray {
		private final ArrayNode nativeArray;
		private final Map<Integer, JsonLikeValue> jsonLikeMap = new LinkedHashMap<Integer, JsonLikeValue>();

		public JacksonJsonArray(ArrayNode json) {
			this.nativeArray = json;
		}

		@Override
		public Object getValue() {
			return null;
		}

		@Override
		public int size() {
			return nativeArray.size();
		}

		@Override
		public JsonLikeValue get(int index) {
			Integer key = index;
			JsonLikeValue result = null;
			if (jsonLikeMap.containsKey(key)) {
				result = jsonLikeMap.get(key);
			} else {
				JsonNode child = nativeArray.get(index);
				if (child != null) {
					result = new JacksonJsonValue(child);
				}
				jsonLikeMap.put(key, result);
			}
			return result;
		}
	}

	private static class JacksonJsonValue extends JsonLikeValue {
		private final JsonNode nativeValue;
		private JsonLikeObject jsonLikeObject = null;
		private JsonLikeArray jsonLikeArray = null;

		public JacksonJsonValue(JsonNode jsonNode) {
			this.nativeValue = jsonNode;
		}

		@Override
		public Object getValue() {
			if (nativeValue != null && nativeValue.isValueNode()) {
				if (nativeValue.isNumber()) {
					return nativeValue.numberValue();
				}

				if (nativeValue.isBoolean()) {
					return nativeValue.booleanValue();
				}

				return nativeValue.asText();
			}
			return null;
		}

		@Override
		public ValueType getJsonType() {
			if (null == nativeValue) {
				return ValueType.NULL;
			}

			switch (nativeValue.getNodeType()) {
				case NULL:
				case MISSING:
					return ValueType.NULL;
				case OBJECT:
					return ValueType.OBJECT;
				case ARRAY:
					return ValueType.ARRAY;
				case POJO:
				case BINARY:
				case STRING:
				case NUMBER:
				case BOOLEAN:
				default:
					break;
			}

			return ValueType.SCALAR;
		}

		@Override
		public ScalarType getDataType() {
			if (nativeValue != null && nativeValue.isValueNode()) {
				if (nativeValue.isNumber()) {
					return ScalarType.NUMBER;
				}
				if (nativeValue.isTextual()) {
					return ScalarType.STRING;
				}
				if (nativeValue.isBoolean()) {
					return ScalarType.BOOLEAN;
				}
			}
			return null;
		}

		@Override
		public JsonLikeArray getAsArray() {
			if (nativeValue != null && nativeValue.isArray()) {
				if (null == jsonLikeArray) {
					jsonLikeArray = new JacksonJsonArray((ArrayNode) nativeValue);
				}
			}
			return jsonLikeArray;
		}

		@Override
		public JsonLikeObject getAsObject() {
			if (nativeValue != null && nativeValue.isObject()) {
				if (null == jsonLikeObject) {
					jsonLikeObject = new JacksonJsonObject((ObjectNode) nativeValue);
				}
			}
			return jsonLikeObject;
		}

		@Override
		public Number getAsNumber() {
			return nativeValue != null ? nativeValue.numberValue() : null;
		}

		@Override
		public String getAsString() {
			if (nativeValue != null) {
				if (nativeValue instanceof DecimalNode) {
					BigDecimal value = nativeValue.decimalValue();
					return value.toPlainString();
				}
				return nativeValue.asText();
			}
			return null;
		}

		@Override
		public boolean getAsBoolean() {
			if (nativeValue != null && nativeValue.isValueNode() && nativeValue.isBoolean()) {
				return nativeValue.asBoolean();
			}
			return super.getAsBoolean();
		}
	}

	private static ObjectMapper createObjectMapper() {
		ObjectMapper retVal =
			JsonMapper
				.builder()
				.build();
		retVal = retVal.setNodeFactory(new JsonNodeFactory(true));
		retVal = retVal.enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS);
		retVal = retVal.enable(DeserializationFeature.FAIL_ON_TRAILING_TOKENS);
		retVal = retVal.disable(JsonParser.Feature.INCLUDE_SOURCE_IN_LOCATION);
		retVal = retVal.disable(JsonGenerator.Feature.AUTO_CLOSE_TARGET);
		retVal = retVal.disable(JsonParser.Feature.AUTO_CLOSE_SOURCE);
		retVal = retVal.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
		return retVal;
	}
}
