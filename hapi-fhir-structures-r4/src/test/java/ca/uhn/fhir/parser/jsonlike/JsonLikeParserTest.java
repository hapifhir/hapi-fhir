package ca.uhn.fhir.parser.jsonlike;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.parser.IJsonLikeParser;
import ca.uhn.fhir.parser.json.JsonLikeArray;
import ca.uhn.fhir.parser.json.JsonLikeObject;
import ca.uhn.fhir.parser.json.JsonLikeStructure;
import ca.uhn.fhir.parser.json.JsonLikeValue;
import ca.uhn.fhir.parser.json.JsonLikeWriter;
import ca.uhn.fhir.parser.json.jackson.JacksonStructure;
import ca.uhn.fhir.parser.view.ExtPatient;
import ca.uhn.fhir.util.TestUtil;
import org.apache.commons.io.IOUtils;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Extension;
import org.hl7.fhir.r4.model.IntegerType;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Reference;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class JsonLikeParserTest {
	private static FhirContext ourCtx = FhirContext.forR4();
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(JsonLikeParserTest.class);

	/**
	 * Test for JSON Parser with user-supplied JSON-like structure (use default GSON)
	 */
	@Test
	public void testJsonLikeParseAndEncodeResourceFromXmlToJson() throws Exception {
		String content = IOUtils.toString(JsonLikeParserTest.class.getResourceAsStream("/extension-on-line.txt"));
		
		IBaseResource parsed = ourCtx.newJsonParser().parseResource(content);

		String encoded = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(parsed);
		ourLog.info(encoded);
		
		JsonLikeStructure jsonLikeStructure = new JacksonStructure();
		jsonLikeStructure.load(new StringReader(encoded));
		
		IJsonLikeParser jsonLikeparser = (IJsonLikeParser)ourCtx.newJsonParser();
		
		IBaseResource resource = jsonLikeparser.parseResource(jsonLikeStructure);
		assertEquals(parsed.getClass().getName(), resource.getClass().getName(), "reparsed resource classes not equal");
	}

	/**
	 * Test JSON-Like writer using custom stream writer
	 * 
	 */
	@Test
	public void testJsonLikeParseWithCustomJSONStreamWriter() throws Exception {
		String refVal = "http://my.org/FooBar";

		Patient fhirPat = new Patient();
		fhirPat.addExtension().setUrl("x1").setValue(new Reference(refVal));

		IJsonLikeParser jsonLikeParser = (IJsonLikeParser)ourCtx.newJsonParser();
		JsonLikeMapWriter jsonLikeWriter = new JsonLikeMapWriter();

		jsonLikeParser.encodeResourceToJsonLikeWriter(fhirPat, jsonLikeWriter);
		Map<String,Object> jsonLikeMap = jsonLikeWriter.getResultMap();
		
		System.out.println("encoded map: " + jsonLikeMap.toString());

		assertNotNull(jsonLikeMap.get("resourceType"), "Encoded resource missing 'resourceType' element");
		assertEquals(jsonLikeMap.get("resourceType"), "Patient", "Expecting 'resourceType'='Patient'; found '"+jsonLikeMap.get("resourceType")+"'");

		assertNotNull(jsonLikeMap.get("extension"), "Encoded resource missing 'extension' element");
		assertTrue((jsonLikeMap.get("extension") instanceof List), "'extension' element is not a List");
		
		List<Object> extensions = (List<Object>)jsonLikeMap.get("extension");
		assertEquals( 1, extensions.size(), "'extnesion' array has more than one entry");
		assertTrue((extensions.get(0) instanceof Map), "'extension' array entry is not a Map");
		
		Map<String, Object> extension = (Map<String,Object>)extensions.get(0);
		assertNotNull(extension.get("url"), "'extension' entry missing 'url' member");
		assertTrue((extension.get("url") instanceof String), "'extension' entry 'url' member is not a String");
		assertEquals("x1", extension.get("url"), "Expecting '/extension[]/url' = 'x1'; found '"+extension.get("url")+"'");
	
	}

	/**
	 * Repeat the "View" tests with custom JSON-Like structure
	 */
	@Test
	public void testViewJson() throws Exception {

		ExtPatient src = new ExtPatient();
		src.addIdentifier().setSystem("urn:sys").setValue("id1");
		src.addIdentifier().setSystem("urn:sys").setValue("id2");
		src.getExt().setValue(100);
		src.getModExt().setValue(200);

		IJsonLikeParser jsonLikeParser = (IJsonLikeParser)ourCtx.newJsonParser();
		JsonLikeMapWriter jsonLikeWriter = new JsonLikeMapWriter();
		jsonLikeParser.encodeResourceToJsonLikeWriter(src, jsonLikeWriter);
		Map<String,Object> jsonLikeMap = jsonLikeWriter.getResultMap();
		

		ourLog.info("encoded: "+jsonLikeMap);

		JsonLikeStructure jsonStructure = new JsonLikeMapStructure(jsonLikeMap);
		IJsonLikeParser parser = (IJsonLikeParser)ourCtx.newJsonParser();
		Patient nonExt = parser.parseResource(Patient.class, jsonStructure);

		assertEquals(Patient.class, nonExt.getClass());
		assertEquals("urn:sys", nonExt.getIdentifier().get(0).getSystem());
		assertEquals("id1", nonExt.getIdentifier().get(0).getValue());
		assertEquals("urn:sys", nonExt.getIdentifier().get(1).getSystem());
		assertEquals("id2", nonExt.getIdentifier().get(1).getValue());

		List<Extension> ext = nonExt.getExtensionsByUrl("urn:ext");
		assertEquals(1, ext.size());
		assertEquals("urn:ext", ext.get(0).getUrl());
		assertEquals(IntegerType.class, ext.get(0).getValueAsPrimitive().getClass());
		assertEquals("100", ext.get(0).getValueAsPrimitive().getValueAsString());

		List<Extension> modExt = nonExt.getExtensionsByUrl("urn:modExt");
		assertEquals(1, modExt.size());
		assertEquals("urn:modExt", modExt.get(0).getUrl());
		assertEquals(IntegerType.class, modExt.get(0).getValueAsPrimitive().getClass());
		assertEquals("200", modExt.get(0).getValueAsPrimitive().getValueAsString());

		ExtPatient va = ourCtx.newViewGenerator().newView(nonExt, ExtPatient.class);
		assertEquals("urn:sys", va.getIdentifier().get(0).getSystem());
		assertEquals("id1", va.getIdentifier().get(0).getValue());
		assertEquals("urn:sys", va.getIdentifier().get(1).getSystem());
		assertEquals("id2", va.getIdentifier().get(1).getValue());
		assertEquals(100, va.getExt().getValue().intValue());
		assertEquals(200, va.getModExt().getValue().intValue());

		assertEquals(0, va.getExtension().size());
	}
	
	@AfterAll
	public static void afterClassClearContext() {
		TestUtil.randomizeLocaleAndTimezone();
	}
	
	
	
	public static class JsonLikeMapWriter extends JsonLikeWriter {

		private Map<String,Object> target;
		
		private static class Block {
			private BlockType type;
			private String name;
			private Map<String,Object> object;
			private List<Object> array;
			public Block(BlockType type) {
				this.type = type;
			}
			public BlockType getType() {
				return type;
			}
			public String getName() {
				return name;
			}
			public void setName(String currentName) {
				this.name = currentName;
			}
			public Map<String, Object> getObject() {
				return object;
			}
			public void setObject(Map<String, Object> currentObject) {
				this.object = currentObject;
			}
			public List<Object> getArray() {
				return array;
			}
			public void setArray(List<Object> currentArray) {
				this.array = currentArray;
			}
		}
		private enum BlockType {
			NONE, OBJECT, ARRAY
		}
		private Block currentBlock = new Block(BlockType.NONE);
		private Stack<Block> blockStack = new Stack<>();

		public JsonLikeMapWriter () {
			super();
		}
		
		public Map<String,Object> getResultMap() {
			return target;
		}
		public void setResultMap(Map<String,Object> target) {
			this.target = target;
		}

		@Override
		public JsonLikeWriter init() {
			if (target != null) {
				target.clear();
			}
			currentBlock = new Block(BlockType.NONE);
			blockStack.clear();
			return this;
		}

		@Override
		public JsonLikeWriter flush() throws IOException {
			if (currentBlock.getType() != BlockType.NONE) {
				throw new IOException("JsonLikeStreamWriter.flush() called but JSON document is not finished");
			}
			return this;
		}

		@Override
		public void close() {
			// nothing to do
		}

		@Override
		public JsonLikeWriter beginObject() throws IOException {
			if (currentBlock.getType() == BlockType.OBJECT) {
				throw new IOException("Unnamed JSON elements can only be created in JSON arrays");
			}
			Map<String,Object> newObject;
			if (currentBlock.getType() == BlockType.NONE) {
				if (null == target) {
					// for this test, we don't care about ordering of map elements
					// target = new EntryOrderedMap<String,Object>();
					target = new HashMap<>();
				}
				newObject = target;
			} else {
				// for this test, we don't care about ordering of map elements
				// newObject = new EntryOrderedMap<String,Object>();
				newObject = new HashMap<>();
			}
			blockStack.push(currentBlock);
			currentBlock = new Block(BlockType.OBJECT);
			currentBlock.setObject(newObject);
			return this;
		}

		@Override
		public JsonLikeWriter beginObject(String name) throws IOException {
			if (currentBlock.getType() == BlockType.ARRAY) {
				throw new IOException("Named JSON elements can only be created in JSON objects");
			}
			blockStack.push(currentBlock);
			currentBlock = new Block(BlockType.OBJECT);
			currentBlock.setName(name);
			// for this test, we don't care about ordering of map elements
			// currentBlock.setObject(new EntryOrderedMap<String,Object>());
			currentBlock.setObject(new HashMap<>());
			return this;
		}

		@Override
		public JsonLikeWriter beginArray(String name) throws IOException {
			if (currentBlock.getType() == BlockType.ARRAY) {
				throw new IOException("Named JSON elements can only be created in JSON objects");
			}
			blockStack.push(currentBlock);
			currentBlock = new Block(BlockType.ARRAY);
			currentBlock.setName(name);
			currentBlock.setArray(new ArrayList<>());
			return this;
		}

		@Override
		public JsonLikeWriter write(String value) throws IOException {
			if (currentBlock.getType() == BlockType.OBJECT) {
				throw new IOException("Unnamed JSON elements can only be created in JSON arrays");
			}
			currentBlock.getArray().add(value);
			return this;
		}

		@Override
		public JsonLikeWriter write(BigInteger value) throws IOException {
			if (currentBlock.getType() == BlockType.OBJECT) {
				throw new IOException("Unnamed JSON elements can only be created in JSON arrays");
			}
			currentBlock.getArray().add(value);
			return this;
		}
		
		@Override
		public JsonLikeWriter write(BigDecimal value) throws IOException {
			if (currentBlock.getType() == BlockType.OBJECT) {
				throw new IOException("Unnamed JSON elements can only be created in JSON arrays");
			}
			currentBlock.getArray().add(value);
			return this;
		}

		@Override
		public JsonLikeWriter write(long value) throws IOException {
			if (currentBlock.getType() == BlockType.OBJECT) {
				throw new IOException("Unnamed JSON elements can only be created in JSON arrays");
			}
			currentBlock.getArray().add(Long.valueOf(value));
			return this;
		}

		@Override
		public JsonLikeWriter write(double value) throws IOException {
			if (currentBlock.getType() == BlockType.OBJECT) {
				throw new IOException("Unnamed JSON elements can only be created in JSON arrays");
			}
			currentBlock.getArray().add(Double.valueOf(value));
			return this;
		}

		@Override
		public JsonLikeWriter write(Boolean value) throws IOException {
			if (currentBlock.getType() == BlockType.OBJECT) {
				throw new IOException("Unnamed JSON elements can only be created in JSON arrays");
			}
			currentBlock.getArray().add(value);
			return this;
		}

		@Override
		public JsonLikeWriter write(boolean value) throws IOException {
			if (currentBlock.getType() == BlockType.OBJECT) {
				throw new IOException("Unnamed JSON elements can only be created in JSON arrays");
			}
			currentBlock.getArray().add(Boolean.valueOf(value));
			return this;
		}

		@Override
		public JsonLikeWriter writeNull() throws IOException {
			if (currentBlock.getType() == BlockType.OBJECT) {
				throw new IOException("Unnamed JSON elements can only be created in JSON arrays");
			}
			currentBlock.getArray().add(null);
			return this;
		}

		@Override
		public JsonLikeWriter write(String name, String value) throws IOException {
			if (currentBlock.getType() == BlockType.ARRAY) {
				throw new IOException("Named JSON elements can only be created in JSON objects");
			}
			currentBlock.getObject().put(name, value);
			return this;
		}

		@Override
		public JsonLikeWriter write(String name, BigInteger value) throws IOException {
			if (currentBlock.getType() == BlockType.ARRAY) {
				throw new IOException("Named JSON elements can only be created in JSON objects");
			}
			currentBlock.getObject().put(name, value);
			return this;
		}
		@Override
		public JsonLikeWriter write(String name, BigDecimal value) throws IOException {
			if (currentBlock.getType() == BlockType.ARRAY) {
				throw new IOException("Named JSON elements can only be created in JSON objects");
			}
			currentBlock.getObject().put(name, value);
			return this;
		}

		@Override
		public JsonLikeWriter write(String name, long value) throws IOException {
			if (currentBlock.getType() == BlockType.ARRAY) {
				throw new IOException("Named JSON elements can only be created in JSON objects");
			}
			currentBlock.getObject().put(name, Long.valueOf(value));
			return this;
		}

		@Override
		public JsonLikeWriter write(String name, double value) throws IOException {
			if (currentBlock.getType() == BlockType.ARRAY) {
				throw new IOException("Named JSON elements can only be created in JSON objects");
			}
			currentBlock.getObject().put(name, Double.valueOf(value));
			return this;
		}

		@Override
		public JsonLikeWriter write(String name, Boolean value) throws IOException {
			if (currentBlock.getType() == BlockType.ARRAY) {
				throw new IOException("Named JSON elements can only be created in JSON objects");
			}
			currentBlock.getObject().put(name, value);
			return this;
		}

		@Override
		public JsonLikeWriter write(String name, boolean value) throws IOException {
			if (currentBlock.getType() == BlockType.ARRAY) {
				throw new IOException("Named JSON elements can only be created in JSON objects");
			}
			currentBlock.getObject().put(name, Boolean.valueOf(value));
			return this;
		}

		@Override
		public JsonLikeWriter endObject() throws IOException {
			if (currentBlock.getType() == BlockType.NONE) {
				ourLog.error("JsonLikeStreamWriter.endObject(); called with no active JSON document");
			} else {
				if (currentBlock.getType() != BlockType.OBJECT) {
					ourLog.error("JsonLikeStreamWriter.endObject(); called outside a JSON object. (Use endArray() instead?)");
				}
				endBlock();
			}
			return this;
		}

		@Override
		public JsonLikeWriter endArray() {
			if (currentBlock.getType() == BlockType.NONE) {
				ourLog.error("JsonLikeStreamWriter.endArray(); called with no active JSON document");
			} else {
				if (currentBlock.getType() != BlockType.ARRAY) {
					ourLog.error("JsonLikeStreamWriter.endArray(); called outside a JSON array. (Use endObject() instead?)");
				}
				endBlock();
			}
			return this;
		}

		@Override
		public JsonLikeWriter endBlock() {
			if (currentBlock.getType() == BlockType.NONE) {
				ourLog.error("JsonLikeStreamWriter.endBlock(); called with no active JSON document");
			} else {
				Object toPut;
				if (currentBlock.getType() == BlockType.ARRAY) {
					toPut = currentBlock.getArray();
				} else {
					toPut = currentBlock.getObject();
				}
				Block parentBlock = blockStack.pop(); 
				if (parentBlock.getType() == BlockType.OBJECT) {
					parentBlock.getObject().put(currentBlock.getName(), toPut);
				} else 
				if (parentBlock.getType() == BlockType.ARRAY) {
					parentBlock.getArray().add(toPut);
				} 
				currentBlock = parentBlock;
			}
			return this;
		}

	}
	
	public static class JsonLikeMapStructure implements JsonLikeStructure {

		private Map<String,Object> nativeObject;
		private JsonLikeObject jsonLikeObject = null;
		private JsonLikeMapWriter jsonLikeWriter = null;
		
		public JsonLikeMapStructure() {
			super();
		}
		
		public JsonLikeMapStructure (Map<String,Object> json) {
			super();
			setNativeObject(json);
		}
		
		public void setNativeObject (Map<String,Object> json) {
			this.nativeObject = json;
		}

		@Override
		public JsonLikeStructure getInstance() {
			return new JsonLikeMapStructure();
		}

		@Override
		public JsonLikeWriter getJsonLikeWriter (Writer ignored) {
			return getJsonLikeWriter();
		}
		
		@Override
		public JsonLikeWriter getJsonLikeWriter () {
			if (null == jsonLikeWriter) {
				jsonLikeWriter = new JsonLikeMapWriter();
			}
			return jsonLikeWriter;
		}

		@Override
		public void load(Reader reader) throws DataFormatException {
			this.load(reader, true);
		}

		@Override
		public void load(Reader theReader, boolean allowArray) throws DataFormatException {
			throw new DataFormatException("JSON structure loading is not supported for native Java Map structures");
		}

		@Override
		public JsonLikeObject getRootObject() {
			if (null == jsonLikeObject) {
				jsonLikeObject = new JsonMapObject(nativeObject);
			}
			return jsonLikeObject;
		}

		private class JsonMapObject extends JsonLikeObject {
			private Map<String,Object> nativeObject;
			private Map<String,JsonLikeValue> jsonLikeMap = new LinkedHashMap<>();
			
			public JsonMapObject (Map<String,Object> json) {
				this.nativeObject = json;
			}

			@Override
			public Object getValue() {
				return nativeObject;
			}

			@Override
			public Iterator<String> keyIterator() {
				return nativeObject.keySet().iterator();
			}

			@Override
			public JsonLikeValue get(String key) {
				JsonLikeValue result = null;
				if (jsonLikeMap.containsKey(key)) {
					result = jsonLikeMap.get(key); 
				} else {
					Object child = nativeObject.get(key);
					if (child != null) {
						result = new JsonMapValue(child);
					}
					jsonLikeMap.put(key, result);
				}
				return result;
			}
		}
		
		private class JsonMapArray extends JsonLikeArray {
			private List<Object> nativeArray;
			private Map<Integer,JsonLikeValue> jsonLikeMap = new LinkedHashMap<>();
			
			public JsonMapArray (List<Object> json) {
				this.nativeArray = json;
			}

			@Override
			public Object getValue() {
				return nativeArray;
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
					Object child = nativeArray.get(index);
					if (child != null) {
						result = new JsonMapValue(child);
					}
					jsonLikeMap.put(key, result);
				}
				return result;
			}
		}
		
		private class JsonMapValue extends JsonLikeValue {
			private Object nativeValue;
			private JsonLikeObject jsonLikeObject = null;
			private JsonLikeArray jsonLikeArray = null;
			
			public JsonMapValue (Object json) {
				this.nativeValue = json;
			}

			@Override
			public Object getValue() {
				return nativeValue;
			}
			
			@Override
			public ValueType getJsonType() {
				if (isNull()) {
					return ValueType.NULL;
				}
				if (isObject()) {
					return ValueType.OBJECT;
				}
				if (isArray()) {
					return ValueType.ARRAY;
				}
				return ValueType.SCALAR;
			}
			
			@Override
			public ScalarType getDataType() {
				if (isString()) {
					return ScalarType.STRING;
				}
				if (isNumber()) {
					return ScalarType.NUMBER;
				}
				if (isBoolean()) {
					return ScalarType.BOOLEAN;
				}
				return null;
			}

			@SuppressWarnings("unchecked")
			@Override
			public JsonLikeArray getAsArray() {
				if (nativeValue != null && isArray()) {
					if (null == jsonLikeArray) {
						jsonLikeArray = new JsonMapArray((List<Object>)nativeValue);
					}
				}
				return jsonLikeArray;
			}

			@SuppressWarnings("unchecked")
			@Override
			public JsonLikeObject getAsObject() {
				if (nativeValue != null && isObject()) {
					if (null == jsonLikeObject) {
						jsonLikeObject = new JsonMapObject((Map<String,Object>)nativeValue);
					}
				}
				return jsonLikeObject;
			}

			@Override
			public String getAsString() {
				String result = null;
				if (nativeValue != null) {
					result = nativeValue.toString();
				}
				return result;
			}

			@Override
			public boolean getAsBoolean() {
				if (nativeValue != null && isBoolean()) {
					return (Boolean) nativeValue;
				}
				return super.getAsBoolean();
			}

			@Override
			public boolean isObject () {
				return (nativeValue != null)
					&& ( (nativeValue instanceof Map) || Map.class.isAssignableFrom(nativeValue.getClass()) );
			}

			@Override
			public boolean isArray () {
				return (nativeValue != null)
					&& ( (nativeValue instanceof List) || List.class.isAssignableFrom(nativeValue.getClass()));
			}

			@Override
			public boolean isString () {
				return (nativeValue != null)
					&& ( (nativeValue instanceof String) || String.class.isAssignableFrom(nativeValue.getClass()));
			}

			@Override
			public boolean isNumber () {
				return (nativeValue != null)
					&& ( (nativeValue instanceof Number) || Number.class.isAssignableFrom(nativeValue.getClass()) );
			}

			public boolean isBoolean () {
				return (nativeValue != null)
					&& ( (nativeValue instanceof Boolean) || Boolean.class.isAssignableFrom(nativeValue.getClass()) );
			}
			
			@Override
			public boolean isNull () {
				return (null == nativeValue);
			}
		}
	}
}
