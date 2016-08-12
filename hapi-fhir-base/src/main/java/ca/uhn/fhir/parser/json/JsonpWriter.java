/**
 * This software has been produced by Akana, Inc. under a professional services
 * agreement with our customer. This work may contain material that is confidential 
 * and proprietary information of Akana, Inc. and is subject to copyright 
 * protection under laws of the United States of America and other countries. 
 * Akana, Inc. grants the customer non-exclusive rights to this material without
 * any warranty expressed or implied. 
 */
package ca.uhn.fhir.parser.json;

import java.io.IOException;
import java.io.Writer;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import javax.json.Json;
import javax.json.stream.JsonGenerator;
import javax.json.stream.JsonGeneratorFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Akana, Inc. Professional Services
 *
 */
public class JsonpWriter extends JsonLikeWriter {
	private static final Logger log = LoggerFactory.getLogger(JsonpWriter.class);

	private Writer writer;
	private JsonGenerator eventWriter;
	private enum BlockType {
		NONE, OBJECT, ARRAY
	}
	private BlockType blockType = BlockType.NONE;
	private Stack<BlockType> blockStack = new Stack<BlockType>(); 

	public JsonpWriter () {
		super();
	}
	public JsonpWriter (Writer writer) {
		this.writer = writer;
	}
	
	public Writer getWriter() {
		return writer;
	}
	public void setWriter(Writer writer) {
		this.writer = writer;
	}

	@Override
	public JsonLikeWriter init() {
		Map<String, Object> properties = new HashMap<String, Object>(1);
		properties.put(JsonGenerator.PRETTY_PRINTING, isPrettyPrint());
		JsonGeneratorFactory jgf = Json.createGeneratorFactory(properties);
		eventWriter = jgf.createGenerator(writer);
		blockType = BlockType.NONE;
		blockStack.clear();
		return this;
	}

	@Override
	public JsonLikeWriter flush() {
		if (blockType != BlockType.NONE) {
			log.error("JsonLikeStreamWriter.flush() called but JSON document is not finished");
		}
		try {
			eventWriter.flush();
			writer.flush();
		} catch (IOException e) {
			log.error("Error flushing writer", e);
		}
		return this;
	}

	@Override
	public void close() {
		try {
			eventWriter.close();
			writer.close();
		} catch (IOException e) {
			log.error("Error closing writer", e);
		}
	}

	@Override
	public JsonLikeWriter beginObject() {
		blockStack.push(blockType);
		blockType = BlockType.OBJECT;
		eventWriter.writeStartObject();
		return this;
	}

	@Override
	public JsonLikeWriter beginArray() {
		blockStack.push(blockType);
		blockType = BlockType.ARRAY;
		eventWriter.writeStartArray();
		return this;
	}

	@Override
	public JsonLikeWriter beginObject(String name) {
		blockStack.push(blockType);
		blockType = BlockType.OBJECT;
		eventWriter.writeStartObject(name);
		return this;
	}

	@Override
	public JsonLikeWriter beginArray(String name) {
		blockStack.push(blockType);
		blockType = BlockType.ARRAY;
		eventWriter.writeStartArray(name);
		return this;
	}

	@Override
	public JsonLikeWriter write(String value) {
		eventWriter.write(value);
		return this;
	}

	@Override
	public JsonLikeWriter write(BigInteger value) {
		eventWriter.write(value);
		return this;
	}
	
	@Override
	public JsonLikeWriter write(BigDecimal value) {
		eventWriter.write(value);
		return this;
	}

	@Override
	public JsonLikeWriter write(long value) {
		eventWriter.write(value);
		return this;
	}

	@Override
	public JsonLikeWriter write(double value) {
		eventWriter.write(value);
		return this;
	}

	@Override
	public JsonLikeWriter write(Boolean value) {
		eventWriter.write(value);
		return this;
	}

	@Override
	public JsonLikeWriter write(boolean value) {
		eventWriter.write(value);
		return this;
	}

	@Override
	public JsonLikeWriter writeNull() {
		eventWriter.writeNull();
		return this;
	}

	@Override
	public JsonLikeWriter write(String name, String value) {
		eventWriter.write(name, value);
		return this;
	}

	@Override
	public JsonLikeWriter write(String name, BigInteger value) {
		eventWriter.write(name, value);
		return this;
	}
	@Override
	public JsonLikeWriter write(String name, BigDecimal value) {
		eventWriter.write(name, value);
		return this;
	}

	@Override
	public JsonLikeWriter write(String name, long value) {
		eventWriter.write(name, value);
		return this;
	}

	@Override
	public JsonLikeWriter write(String name, double value) {
		eventWriter.write(name, value);
		return this;
	}

	@Override
	public JsonLikeWriter write(String name, Boolean value) {
		eventWriter.write(name, value);
		return this;
	}

	@Override
	public JsonLikeWriter write(String name, boolean value) {
		eventWriter.write(name, value);
		return this;
	}

	@Override
	public JsonLikeWriter writeNull(String name) {
		eventWriter.writeNull(name);
		return this;
	}

	@Override
	public JsonLikeWriter endObject() {
		if (blockType == BlockType.NONE) {
			log.error("JsonLikeStreamWriter.endObject(); called with no active JSON document");
		} else {
			if (blockType != BlockType.OBJECT) {
				log.error("JsonLikeStreamWriter.endObject(); called outside a JSON object. (Use endArray() instead?)");
			}
			eventWriter.writeEnd();
			blockType = blockStack.pop();
		}
		return this;
	}

	@Override
	public JsonLikeWriter endArray() {
		if (blockType == BlockType.NONE) {
			log.error("JsonLikeStreamWriter.endArray(); called with no active JSON document");
		} else {
			if (blockType != BlockType.ARRAY) {
				log.error("JsonLikeStreamWriter.endArray(); called outside a JSON array. (Use endObject() instead?)");
			}
			eventWriter.writeEnd();
			blockType = blockStack.pop();
		}
		return this;
	}

	@Override
	public JsonLikeWriter endBlock() {
		if (blockType == BlockType.NONE) {
			log.error("JsonLikeStreamWriter.endBlock(); called with no active JSON document");
		} else {
			eventWriter.writeEnd();
			blockType = blockStack.pop();
		}
		return this;
	}

}
