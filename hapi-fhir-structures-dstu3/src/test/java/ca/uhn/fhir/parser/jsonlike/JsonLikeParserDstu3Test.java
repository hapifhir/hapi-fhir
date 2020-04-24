package ca.uhn.fhir.parser.jsonlike;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IJsonLikeParser;
import ca.uhn.fhir.parser.json.JsonLikeStructure;
import ca.uhn.fhir.parser.json.JsonLikeWriter;
import ca.uhn.fhir.parser.json.jackson.JacksonStructure;
import ca.uhn.fhir.util.TestUtil;
import org.apache.commons.io.IOUtils;
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.dstu3.model.Reference;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class JsonLikeParserDstu3Test {
	private static FhirContext ourCtx = FhirContext.forDstu3();
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(JsonLikeParserDstu3Test.class);

	/**
	 * Test for JSON Parser with user-supplied JSON-like structure (use default GSON)
	 */
	@Test
	public void testJsonLikeParseAndEncodeBundleFromXmlToJson() throws Exception {
		String content = IOUtils.toString(JsonLikeParserDstu3Test.class.getResourceAsStream("/bundle_with_woven_obs.xml"));

		Bundle parsed = ourCtx.newXmlParser().parseResource(Bundle.class, content);

		String encoded = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(parsed);
		ourLog.info(encoded);
		
		JsonLikeStructure jsonLikeStructure = new JacksonStructure();
		jsonLikeStructure.load(new StringReader(encoded));
		
		IJsonLikeParser jsonLikeparser = (IJsonLikeParser)ourCtx.newJsonParser();
		
		Bundle bundle = jsonLikeparser.parseResource(Bundle.class, jsonLikeStructure);
		
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

		Assert.assertNotNull("Encoded resource missing 'resourceType' element", jsonLikeMap.get("resourceType"));
		Assert.assertEquals("Expecting 'resourceType'='Patient'; found '"+jsonLikeMap.get("resourceType")+"'", jsonLikeMap.get("resourceType"), "Patient");

		Assert.assertNotNull("Encoded resource missing 'extension' element", jsonLikeMap.get("extension"));
		Assert.assertTrue("'extension' element is not a List", (jsonLikeMap.get("extension") instanceof List));
		
		List<Object> extensions = (List<Object>)jsonLikeMap.get("extension");
		Assert.assertEquals("'extnesion' array has more than one entry", 1, extensions.size());
		Assert.assertTrue("'extension' array entry is not a Map", (extensions.get(0) instanceof Map));
		
		Map<String, Object> extension = (Map<String,Object>)extensions.get(0);
		Assert.assertNotNull("'extension' entry missing 'url' member", extension.get("url"));
		Assert.assertTrue("'extension' entry 'url' member is not a String", (extension.get("url") instanceof String));
		Assert.assertEquals("Expecting '/extension[]/url' = 'x1'; found '"+extension.get("url")+"'", "x1", (String)extension.get("url"));
	
	}
	
	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
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
		private Stack<Block> blockStack = new Stack<Block>(); 

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
		public JsonLikeWriter init() throws IOException {
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
			Map<String,Object> newObject = null;
			if (currentBlock.getType() == BlockType.NONE) {
				if (null == target) {
					// for this test, we don't care about ordering of map elements
					// target = new EntryOrderedMap<String,Object>();
					target = new HashMap<String,Object>();
				}
				newObject = target;
			} else {
				// for this test, we don't care about ordering of map elements
				// newObject = new EntryOrderedMap<String,Object>();
				newObject = new HashMap<String,Object>();
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
			currentBlock.setObject(new HashMap<String,Object>());
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
			currentBlock.setArray(new ArrayList<Object>());
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
		public JsonLikeWriter endArray() throws IOException {
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
				Object toPut = null;
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
	
}
