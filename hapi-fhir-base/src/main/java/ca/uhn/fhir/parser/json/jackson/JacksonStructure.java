package ca.uhn.fhir.parser.json.jackson;

import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.parser.json.JsonLikeArray;
import ca.uhn.fhir.parser.json.JsonLikeObject;
import ca.uhn.fhir.parser.json.JsonLikeStructure;
import ca.uhn.fhir.parser.json.JsonLikeValue;
import ca.uhn.fhir.parser.json.JsonLikeWriter;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.PushbackReader;
import java.io.Reader;
import java.io.Writer;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class JacksonStructure implements JsonLikeStructure {

    private JacksonWriter jacksonWriter;
    private enum ROOT_TYPE {OBJECT, ARRAY};
    private ROOT_TYPE rootType = null;
    private JsonNode nativeRoot = null;
    private JsonNode jsonLikeRoot = null;

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        OBJECT_MAPPER.enable(SerializationFeature.INDENT_OUTPUT);
        OBJECT_MAPPER.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
    }

    public void setNativeObject (ObjectNode objectNode) {
        this.rootType = ROOT_TYPE.OBJECT;
        this.nativeRoot = objectNode;
    }

    public void setNativeArray (ArrayNode arrayNode) {
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
            while(true) {
                nextInt = pbr.read();
                if (nextInt == -1) {
                    throw new DataFormatException("Did not find any content to parse");
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
                    throw new DataFormatException("Content does not appear to be FHIR JSON, first non-whitespace character was: '" + (char)nextInt + "' (must be '{' or '[')");
                }
                throw new DataFormatException("Content does not appear to be FHIR JSON, first non-whitespace character was: '" + (char)nextInt + "' (must be '{')");
            }

            if (nextInt == '{') {
                setNativeObject((ObjectNode) OBJECT_MAPPER.readTree(pbr));
            } else {
                setNativeArray((ArrayNode) OBJECT_MAPPER.readTree(pbr));
            }
        } catch (Exception e) {
            if (e.getMessage().startsWith("Unexpected char 39")) {
                throw new DataFormatException("Failed to parse JSON encoded FHIR content: " + e.getMessage() + " - " +
                        "This may indicate that single quotes are being used as JSON escapes where double quotes are required", e);
            }
            throw new DataFormatException("Failed to parse JSON encoded FHIR content: " + e.getMessage(), e);
        }
    }

    @Override
    public JsonLikeWriter getJsonLikeWriter(Writer writer) {
        if (null == jacksonWriter) {
            jacksonWriter = new JacksonWriter(writer);
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

        throw new DataFormatException("Content must be a valid JSON Object. It must start with '{'.");
    }

    @Override
    public JsonLikeArray getRootArray() throws DataFormatException {
        if (rootType == ROOT_TYPE.ARRAY) {
            if (null == jsonLikeRoot) {
                jsonLikeRoot = nativeRoot;
            }
            return new JacksonJsonArray((ArrayNode) nativeRoot);
        }
        throw new DataFormatException("Content must be a valid JSON Array. It must start with '['.");
    }

    private static class JacksonJsonObject extends JsonLikeObject {
        private final ObjectNode nativeObject;
        private final Map<String, JsonLikeValue> jsonLikeMap = new LinkedHashMap<>();
        private Set<String> keySet = null;

        public JacksonJsonObject(ObjectNode json) {
            this.nativeObject = json;
        }

        @Override
        public Object getValue() {
            return null;
        }

        @Override
        public Set<String> keySet() {
            if (null == keySet) {
                keySet = new EntryOrderedSet<>();

                for (Iterator<JsonNode> iterator = nativeObject.elements(); iterator.hasNext();) {
                    keySet.add(iterator.next().textValue());
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
                JsonNode child = nativeObject.get(key);
                if (child != null) {
                    result = new JacksonJsonValue(child);
                }
                jsonLikeMap.put(key, result);
            }
            return result;
        }
    }

    private static class EntryOrderedSet<T> extends AbstractSet<T> {
        private transient ArrayList<T> data = null;

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
            if (null == nativeValue || nativeValue.isNull()) {
                return ValueType.NULL;
            }
            if (nativeValue.isObject()) {
                return ValueType.OBJECT;
            }
            if (nativeValue.isArray()) {
                return ValueType.ARRAY;
            }
            if (nativeValue.isValueNode()) {
                return ValueType.SCALAR;
            }
            return null;
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
            return nativeValue != null ? nativeValue.asText() : null;
        }

        @Override
        public boolean getAsBoolean() {
            if (nativeValue != null && nativeValue.isValueNode() && nativeValue.isBoolean()) {
                return nativeValue.asBoolean();
            }
            return super.getAsBoolean();
        }
    }
}
