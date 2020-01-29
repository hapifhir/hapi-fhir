/*
 * #%L
 * HAPI FHIR - MongoDB Framework Bundle
 * %%
 * Copyright (C) 2016 - 2020 University Health Network
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
package ca.uhn.fhir.parser.bson;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Stack;

import org.bson.BsonWriter;
import org.bson.types.Decimal128;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uhn.fhir.parser.json.JsonLikeWriter;

/**
 * Used with the {@code JsonParser} to serialize a FHIR Resource
 * to MongoDB document.
 *
 * Copyright (C) 2014 - 2020 University Health Network
 * @author williamEdenton@gmail.com
 */
public class BsonFhirWriter extends JsonLikeWriter {
	private static final Logger log = LoggerFactory.getLogger(BsonFhirWriter.class);

	private BsonWriter eventWriter;
	private enum BlockType {
		NONE, OBJECT, ARRAY
	}
	private BlockType blockType = BlockType.NONE;
	private Stack<BlockType> blockStack = new Stack<BlockType>(); 

	public BsonFhirWriter () {
		super();
	}
	public BsonFhirWriter (BsonWriter writer) {
		this();
		setBsonWriter(writer);
	}
	public BsonFhirWriter setBsonWriter (BsonWriter writer) {
		this.eventWriter = writer;
		return this;
	}

// there may be other ways this basic class might be used..
// other constructors & configuration methods could be addedd
// to consume a BsonWriter within the extended Hapi/Fhir world
//
// all methods from here on assume that 'eventWriter' is set to
// the desired instance of a BsonWriter
	
	@Override
	public JsonLikeWriter init () throws IOException {
		blockType = BlockType.NONE;
		blockStack.clear();
		return this;
	}

	@Override
	public JsonLikeWriter flush () throws IOException {
		if (blockType != BlockType.NONE) {
			log.error("JsonLikeWriter.flush() called but JSON document is not finished");
		}
		eventWriter.flush();
		return this;
	}

	@Override
	public void close () throws IOException {
		eventWriter.flush();
	}

	@Override
	public JsonLikeWriter beginObject () throws IOException {
		blockStack.push(blockType);
		blockType = BlockType.OBJECT;
		eventWriter.writeStartDocument();
		return this;
	}

	@Override
	public JsonLikeWriter beginArray () throws IOException {
		blockStack.push(blockType);
		blockType = BlockType.ARRAY;
		eventWriter.writeStartArray();
		return this;
	}

	@Override
	public JsonLikeWriter beginObject (String name) throws IOException {
		blockStack.push(blockType);
		blockType = BlockType.OBJECT;
		eventWriter.writeStartDocument(name);
		return this;
	}

	@Override
	public JsonLikeWriter beginArray (String name) throws IOException {
		blockStack.push(blockType);
		blockType = BlockType.ARRAY;
		eventWriter.writeStartArray(name);
		return this;
	}

	@Override
	public JsonLikeWriter write (String value) throws IOException {
		eventWriter.writeString(value);
		return this;
	}

	@Override
	public JsonLikeWriter write (BigInteger value) throws IOException {
		try {
			eventWriter.writeInt32(value.intValueExact());
		} catch (ArithmeticException e) {
			try {
				eventWriter.writeInt64(value.longValueExact());
			} catch (ArithmeticException f) {
				Decimal128 work = Decimal128.parse(value.toString());
				eventWriter.writeDecimal128(work);
			}
		}
		return this;
	}
	
	@Override
	public JsonLikeWriter write (BigDecimal value) throws IOException {
		eventWriter.writeDecimal128(new Decimal128(value));
		return this;
	}

//	@Override later addition in superclass
	public JsonLikeWriter write (int value) throws IOException {
		eventWriter.writeInt32(value);
		return this;
	}

	@Override
	public JsonLikeWriter write (long value) throws IOException {
		eventWriter.writeInt64(value);
		return this;
	}

	@Override
	public JsonLikeWriter write (double value) throws IOException {
		eventWriter.writeDouble(value);
		return this;
	}

	@Override
	public JsonLikeWriter write (Boolean value) throws IOException {
		eventWriter.writeBoolean(value.booleanValue());
		return this;
	}

	@Override
	public JsonLikeWriter write (boolean value) throws IOException {
		eventWriter.writeBoolean(value);
		return this;
	}

	@Override
	public JsonLikeWriter writeNull () throws IOException {
		eventWriter.writeNull();
		return this;
	}

	@Override
	public JsonLikeWriter write (String name, String value) throws IOException {
		eventWriter.writeString(name, value);
		return this;
	}

	@Override
	public JsonLikeWriter write (String name, BigInteger value) throws IOException {
		eventWriter.writeName(name);
		this.write(value);
		return this;
	}
	@Override
	public JsonLikeWriter write (String name, BigDecimal value) throws IOException {
		eventWriter.writeName(name);
		this.write(value);
		return this;
	}

//	@Override
	public JsonLikeWriter write (String name, int value) throws IOException {
		eventWriter.writeInt32(name, value);
		return this;
	}

	@Override
	public JsonLikeWriter write (String name, long value) throws IOException {
		eventWriter.writeInt64(name, value);
		return this;
	}

	@Override
	public JsonLikeWriter write (String name, double value) throws IOException {
		eventWriter.writeDouble(name, value);
		return this;
	}

	@Override
	public JsonLikeWriter write (String name, Boolean value) throws IOException {
		eventWriter.writeBoolean(name, value.booleanValue());
		return this;
	}

	@Override
	public JsonLikeWriter write (String name, boolean value) throws IOException {
		eventWriter.writeBoolean(name, value);
		return this;
	}

	@Override
	public JsonLikeWriter writeNull (String name) throws IOException {
		eventWriter.writeNull(name);
		return this;
	}

	@Override
	public JsonLikeWriter endObject () throws IOException {
		if (blockType == BlockType.NONE) {
			log.error("JsonLikeWriter.endObject(); called with no active BSON document");
		} else {
			if (blockType != BlockType.OBJECT) {
				log.error("JsonLikeWriter.endObject(); called outside a BSON document. (Use endArray() instead?)");
				eventWriter.writeEndArray();
			} else {
				eventWriter.writeEndDocument();
			}
			blockType = blockStack.pop();
		}
		return this;
	}

	@Override
	public JsonLikeWriter endArray () throws IOException {
		if (blockType == BlockType.NONE) {
			log.error("JsonLikeWriter.endArray(); called with no active BSON document");
		} else {
			if (blockType != BlockType.ARRAY) {
				log.error("JsonLikeWriter.endArray(); called outside a BSON array. (Use endObject() instead?)");
				eventWriter.writeEndDocument();
			} else {
				eventWriter.writeEndArray();
			}
			blockType = blockStack.pop();
		}
		return this;
	}

	@Override
	public JsonLikeWriter endBlock () throws IOException {
		if (blockType == BlockType.NONE) {
			log.error("JsonLikeWriter.endBlock(); called with no active JSON document");
		} else {
			if (blockType == BlockType.ARRAY) {
				eventWriter.writeEndArray();
			} else {
				eventWriter.writeEndDocument();
			}
			blockType = blockStack.pop();
		}
		return this;
	}

}
