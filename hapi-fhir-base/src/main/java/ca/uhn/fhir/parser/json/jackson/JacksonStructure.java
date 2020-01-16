package ca.uhn.fhir.parser.json.jackson;

import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.parser.json.*;
import com.google.gson.*;

import java.io.PushbackReader;
import java.io.Reader;
import java.io.Writer;
import java.util.*;

public class JacksonStructure implements JsonLikeStructure {

    private JacksonWriter jacksonWriter;
    private enum ROOT_TYPE {OBJECT, ARRAY};
    private ROOT_TYPE rootType = null;
    private JsonElement nativeRoot = null;
    private JsonLikeValue jsonLikeRoot = null;

    public void setNativeObject (JsonObject json) {
        this.rootType = ROOT_TYPE.OBJECT;
        this.nativeRoot = json;
    }

    public void setNativeArray (JsonArray json) {
        this.rootType = ROOT_TYPE.ARRAY;
        this.nativeRoot = json;
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

            //TODO remove the Gson references
            Gson gson = new GsonBuilder().disableHtmlEscaping().create();
            if (nextInt == '{') {
                JsonObject root = gson.fromJson(pbr, JsonObject.class);
                setNativeObject(root);
            } else {
                JsonArray root = gson.fromJson(pbr, JsonArray.class);
                setNativeArray(root);
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
    public JsonLikeWriter getJsonLikeWriter (Writer writer) {
        if (null == jacksonWriter) {
            jacksonWriter = new JacksonWriter(writer);
        }

        return jacksonWriter;
    }

    @Override
    public JsonLikeWriter getJsonLikeWriter () {
        if (null == jacksonWriter) {
            jacksonWriter = new JacksonWriter();
        }
        return jacksonWriter;
    }

    @Override
    public JsonLikeObject getRootObject() throws DataFormatException {
        if (rootType == ROOT_TYPE.OBJECT) {
            if (null == jsonLikeRoot) {
                jsonLikeRoot = new GsonJsonObject((JsonObject)nativeRoot);
            }
            return jsonLikeRoot.getAsObject();
        }
        throw new DataFormatException("Content must be a valid JSON Object. It must start with '{'.");
    }

    @Override
    public JsonLikeArray getRootArray() throws DataFormatException {
        if (rootType == ROOT_TYPE.ARRAY) {
            if (null == jsonLikeRoot) {
                jsonLikeRoot = new GsonJsonArray((JsonArray)nativeRoot);
            }
            return jsonLikeRoot.getAsArray();
        }
        throw new DataFormatException("Content must be a valid JSON Array. It must start with '['.");
    }

    private static class GsonJsonObject extends JsonLikeObject {
        private JsonObject nativeObject;
        private Set<String> keySet = null;
        private Map<String, JsonLikeValue> jsonLikeMap = new LinkedHashMap<String, JsonLikeValue>();

        public GsonJsonObject (JsonObject json) {
            this.nativeObject = json;
        }

        @Override
        public Object getValue() {
            return null;
        }

        @Override
        public Set<String> keySet() {
            if (null == keySet) {
                Set<Map.Entry<String, JsonElement>> entrySet = nativeObject.entrySet();
                keySet = new EntryOrderedSet<String>(entrySet.size());
                for (Map.Entry<String,?> entry : entrySet) {
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

    private static class GsonJsonArray extends JsonLikeArray {
        private JsonArray nativeArray;
        private Map<Integer, JsonLikeValue> jsonLikeMap = new LinkedHashMap<Integer, JsonLikeValue>();

        public GsonJsonArray (JsonArray json) {
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
        public Object getValue() {
            if (nativeValue != null && nativeValue.isJsonPrimitive()) {
                if (((JsonPrimitive)nativeValue).isNumber()) {
                    return nativeValue.getAsNumber();
                }

                if (((JsonPrimitive)nativeValue).isBoolean()) {
                    return nativeValue.getAsBoolean();
                }

                return nativeValue.getAsString();
            }
            return null;
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
        public Number getAsNumber() {
            return nativeValue != null ? nativeValue.getAsNumber() : null;
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
}
