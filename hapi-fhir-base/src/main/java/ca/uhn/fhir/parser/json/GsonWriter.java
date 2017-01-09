package ca.uhn.fhir.parser.json;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2017 University Health Network
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

import java.io.IOException;
import java.io.Writer;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Stack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.stream.JsonWriter;

public class GsonWriter extends JsonLikeWriter {
	private static final Logger log = LoggerFactory.getLogger(GsonWriter.class);

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
		setWriter(writer);
	}

	@Override
	public JsonLikeWriter init() throws IOException {
		eventWriter = new JsonWriter(getWriter());
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
		getWriter().flush();
		return this;
	}

	@Override
	public void close() throws IOException {
		eventWriter.close();
		getWriter().close();
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
