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
import java.util.Stack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.stream.JsonWriter;

/**
 * @author Akana, Inc. Professional Services
 *
 */
public class GsonWriter extends JsonLikeWriter {
	private static final Logger log = LoggerFactory.getLogger(GsonWriter.class);

	private Writer writer;
	private JsonWriter eventWriter;
	private enum BlockType {
		NONE, OBJECT, ARRAY
	}
	private BlockType blockType = BlockType.NONE;
	private Stack<BlockType> blockStack = new Stack<BlockType>(); 

	public GsonWriter () {
		super();
	}
	public GsonWriter (Writer writer) {
		this.writer = writer;
	}
	
	public Writer getWriter() {
		return writer;
	}
	public void setWriter(Writer writer) {
		this.writer = writer;
	}

	@Override
	public JsonLikeWriter init() throws IOException {
		eventWriter = new JsonWriter(writer);
		eventWriter.setSerializeNulls(true);
		if (isPrettyPrint()) {
			eventWriter.setIndent("  ");
		}
		blockType = BlockType.NONE;
		blockStack.clear();
		return this;
	}

	@Override
	public JsonLikeWriter flush() throws IOException {
		if (blockType != BlockType.NONE) {
			log.error("JsonLikeStreamWriter.flush() called but JSON document is not finished");
		}
		eventWriter.flush();
		writer.flush();
		return this;
	}

	@Override
	public void close() throws IOException {
		eventWriter.close();
		writer.close();
	}

	@Override
	public JsonLikeWriter beginObject() throws IOException {
		blockStack.push(blockType);
		blockType = BlockType.OBJECT;
		eventWriter.beginObject();
		return this;
	}

	@Override
	public JsonLikeWriter beginArray() throws IOException {
		blockStack.push(blockType);
		blockType = BlockType.ARRAY;
		eventWriter.beginArray();
		return this;
	}

	@Override
	public JsonLikeWriter beginObject(String name) throws IOException {
		blockStack.push(blockType);
		blockType = BlockType.OBJECT;
		eventWriter.name(name);
		eventWriter.beginObject();
		return this;
	}

	@Override
	public JsonLikeWriter beginArray(String name) throws IOException {
		blockStack.push(blockType);
		blockType = BlockType.ARRAY;
		eventWriter.name(name);
		eventWriter.beginArray();
		return this;
	}

	@Override
	public JsonLikeWriter write(String value) throws IOException {
		eventWriter.value(value);
		return this;
	}

	@Override
	public JsonLikeWriter write(BigInteger value) throws IOException {
		eventWriter.value(value);
		return this;
	}
	
	@Override
	public JsonLikeWriter write(BigDecimal value) throws IOException {
		eventWriter.value(value);
		return this;
	}

	@Override
	public JsonLikeWriter write(long value) throws IOException {
		eventWriter.value(value);
		return this;
	}

	@Override
	public JsonLikeWriter write(double value) throws IOException {
		eventWriter.value(value);
		return this;
	}

	@Override
	public JsonLikeWriter write(Boolean value) throws IOException {
		eventWriter.value(value);
		return this;
	}

	@Override
	public JsonLikeWriter write(boolean value) throws IOException {
		eventWriter.value(value);
		return this;
	}

	@Override
	public JsonLikeWriter writeNull() throws IOException {
		eventWriter.nullValue();
		return this;
	}

	@Override
	public JsonLikeWriter write(String name, String value) throws IOException {
		eventWriter.name(name);
		eventWriter.value(value);
		return this;
	}

	@Override
	public JsonLikeWriter write(String name, BigInteger value) throws IOException {
		eventWriter.name(name);
		eventWriter.value(value);
		return this;
	}
	@Override
	public JsonLikeWriter write(String name, BigDecimal value) throws IOException {
		eventWriter.name(name);
		eventWriter.value(value);
		return this;
	}

	@Override
	public JsonLikeWriter write(String name, long value) throws IOException {
		eventWriter.name(name);
		eventWriter.value(value);
		return this;
	}

	@Override
	public JsonLikeWriter write(String name, double value) throws IOException {
		eventWriter.name(name);
		eventWriter.value(value);
		return this;
	}

	@Override
	public JsonLikeWriter write(String name, Boolean value) throws IOException {
		eventWriter.name(name);
		eventWriter.value(value);
		return this;
	}

	@Override
	public JsonLikeWriter write(String name, boolean value) throws IOException {
		eventWriter.name(name);
		eventWriter.value(value);
		return this;
	}

	@Override
	public JsonLikeWriter writeNull(String name) throws IOException {
		eventWriter.name(name);
		eventWriter.nullValue();
		return this;
	}

	@Override
	public JsonLikeWriter endObject() throws IOException {
		if (blockType == BlockType.NONE) {
			log.error("JsonLikeStreamWriter.endObject(); called with no active JSON document");
		} else {
			if (blockType != BlockType.OBJECT) {
				log.error("JsonLikeStreamWriter.endObject(); called outside a JSON object. (Use endArray() instead?)");
				eventWriter.endArray();
			} else {
				eventWriter.endObject();
			}
			blockType = blockStack.pop();
		}
		return this;
	}

	@Override
	public JsonLikeWriter endArray() throws IOException {
		if (blockType == BlockType.NONE) {
			log.error("JsonLikeStreamWriter.endArray(); called with no active JSON document");
		} else {
			if (blockType != BlockType.ARRAY) {
				log.error("JsonLikeStreamWriter.endArray(); called outside a JSON array. (Use endObject() instead?)");
				eventWriter.endObject();
			} else {
				eventWriter.endArray();
			}
			blockType = blockStack.pop();
		}
		return this;
	}

	@Override
	public JsonLikeWriter endBlock() throws IOException {
		if (blockType == BlockType.NONE) {
			log.error("JsonLikeStreamWriter.endBlock(); called with no active JSON document");
		} else {
			if (blockType == BlockType.ARRAY) {
				eventWriter.endArray();
			} else {
				eventWriter.endObject();
			}
			blockType = blockStack.pop();
		}
		return this;
	}

}
