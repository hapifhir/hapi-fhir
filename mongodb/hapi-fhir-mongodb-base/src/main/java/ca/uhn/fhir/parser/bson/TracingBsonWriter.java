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

import org.bson.BsonBinary;
import org.bson.BsonDbPointer;
import org.bson.BsonReader;
import org.bson.BsonRegularExpression;
import org.bson.BsonTimestamp;
import org.bson.BsonWriter;
import org.bson.types.Decimal128;
import org.bson.types.ObjectId;
import org.slf4j.Logger;

/**
 * This is a wrapper for {@code BsonWriter} that can be used
 * to provide method-level tracing of the writer's actions 
 * 
 * Copyright (C) 2014 - 2020 University Health Network
 * @author williamEdenton@gmail.com
 */
public class TracingBsonWriter implements BsonWriter {
	private BsonWriter realWriter = null;
	private Logger log = null;

	public TracingBsonWriter (BsonWriter realWriter, Logger log) {
		this.realWriter = realWriter;
		this.log = log;
	}


	@Override
	public void flush() {
		log.trace("BSONWriter: flush()");
		realWriter.flush();
	}

	@Override
	public void writeBinaryData(BsonBinary binary) {
		log.trace("BSONWriter: writeBinaryData("+v(binary)+")");
		realWriter.writeBinaryData(binary);
	}

	@Override
	public void writeBinaryData(String name, BsonBinary binary) {
		log.trace("BSONWriter: writeBinaryData("+name+", "+v(binary)+")");
		realWriter.writeBinaryData(name, binary);
	}

	@Override
	public void writeBoolean(boolean value) {
		log.trace("BSONWriter: writeBoolean("+v(value)+")");
		realWriter.writeBoolean(value);
	}

	@Override
	public void writeBoolean(String name, boolean value) {
		log.trace("BSONWriter: writeBoolean("+name+", "+v(value)+")");
		realWriter.writeBoolean(name, value);
	}

	@Override
	public void writeDBPointer(BsonDbPointer value) {
		log.trace("BSONWriter: writeDBPointer("+v(value)+")");
		realWriter.writeDBPointer(value);
	}

	@Override
	public void writeDBPointer(String name, BsonDbPointer value) {
		log.trace("BSONWriter: writeDBPointer("+name+", "+v(value)+")");
		realWriter.writeDBPointer(name, value);
	}

	@Override
	public void writeDateTime(long value) {
		log.trace("BSONWriter: writeDateTime("+v(value)+")");
		realWriter.writeDateTime(value);
	}

	@Override
	public void writeDateTime(String name, long value) {
		log.trace("BSONWriter: writeDateTime("+name+", "+v(value)+")");
		realWriter.writeDateTime(name, value);
	}

	@Override
	public void writeDouble(double value) {
		log.trace("BSONWriter: writeDouble("+v(value)+")");
		realWriter.writeDouble(value);
	}

	@Override
	public void writeDouble(String name, double value) {
		log.trace("BSONWriter: writeDouble("+name+", "+v(value)+")");
		realWriter.writeDouble(name, value);
	}

	@Override
	public void writeEndArray() {
		log.trace("BSONWriter: writeEndArray()");
		realWriter.writeEndArray();
	}

	@Override
	public void writeEndDocument() {
		log.trace("BSONWriter: writeEndDocument()");
		realWriter.writeEndDocument();
	}

	@Override
	public void writeInt32(int value) {
		log.trace("BSONWriter: writeInt32("+v(value)+")");
		realWriter.writeInt32(value);
	}

	@Override
	public void writeInt32(String name, int value) {
		log.trace("BSONWriter: writeInt32("+name+", "+v(value)+")");
		realWriter.writeInt32(name, value);
	}

	@Override
	public void writeInt64(long value) {
		log.trace("BSONWriter: writeInt64("+v(value)+")");
		realWriter.writeInt64(value);
	}

	@Override
	public void writeInt64(String name, long value) {
		log.trace("BSONWriter: writeInt64("+name+", "+v(value)+")");
		realWriter.writeInt64(name, value);
	}
	
	@Override
	public void writeDecimal128(Decimal128 value) {
		log.trace("BSONWriter: writeDecimal128("+v(value)+")");
		realWriter.writeDecimal128(value);
	}

	@Override
	public void writeDecimal128(String name, Decimal128 value) {
		log.trace("BSONWriter: writeDecimal128("+name+", "+v(value)+")");
		realWriter.writeDecimal128(name, value);
	}

	@Override
	public void writeJavaScript(String code) {
		log.trace("BSONWriter: writeJavaScript("+v(code)+")");
		realWriter.writeJavaScript(code);
	}

	@Override
	public void writeJavaScript(String name, String code) {
		log.trace("BSONWriter: writeJavaScript("+name+", "+v(code)+")");
		realWriter.writeJavaScript(name, code);
	}

	@Override
	public void writeJavaScriptWithScope(String code) {
		log.trace("BSONWriter: writeJavaScriptWithScope("+v(code)+")");
		realWriter.writeJavaScriptWithScope(code);
	}

	@Override
	public void writeJavaScriptWithScope(String name, String code) {
		log.trace("BSONWriter: writeJavaScriptWithScope("+name+", "+v(code)+")");
		realWriter.writeJavaScriptWithScope(name, code);
	}

	@Override
	public void writeMaxKey() {
		log.trace("BSONWriter: writeMaxKey()");
		realWriter.writeMaxKey();
	}

	@Override
	public void writeMaxKey(String name) {
		log.trace("BSONWriter: writeMaxKey("+name+")");
		realWriter.writeMaxKey(name);
	}

	@Override
	public void writeMinKey() {
		log.trace("BSONWriter: writeMinKey()");
		realWriter.writeMinKey();
	}

	@Override
	public void writeMinKey(String name) {
		log.trace("BSONWriter: writeMinKey("+name+")");
		realWriter.writeMinKey(name);
	}

	@Override
	public void writeName(String name) {
		log.trace("BSONWriter: writeName("+name+")");
		realWriter.writeName(name);
	}

	@Override
	public void writeNull() {
		log.trace("BSONWriter: writeNull()");
		realWriter.writeNull();
	}

	@Override
	public void writeNull(String name) {
		log.trace("BSONWriter: writeNull("+name+")");
		realWriter.writeNull(name);
	}

	@Override
	public void writeObjectId(ObjectId objectId) {
		log.trace("BSONWriter: writeObjectId("+v(objectId)+")");
		realWriter.writeObjectId(objectId);
	}

	@Override
	public void writeObjectId(String name, ObjectId objectId) {
		log.trace("BSONWriter: writeObjectId("+name+", "+v(objectId)+")");
		realWriter.writeObjectId(name, objectId);
	}

	@Override
	public void writeRegularExpression(BsonRegularExpression regularExpression) {
		log.trace("BSONWriter: writeRegularExpression("+v(regularExpression)+")");
		realWriter.writeRegularExpression(regularExpression);
	}

	@Override
	public void writeRegularExpression(String name, BsonRegularExpression regularExpression) {
		log.trace("BSONWriter: writeRegularExpression("+name+", "+v(regularExpression)+")");
		realWriter.writeRegularExpression(name, regularExpression);
	}

	@Override
	public void writeStartArray() {
		log.trace("BSONWriter: writeStartArray()");
		realWriter.writeStartArray();
	}

	@Override
	public void writeStartArray(String name) {
		log.trace("BSONWriter: writeStartArray("+name+")");
		realWriter.writeStartArray(name);
	}

	@Override
	public void writeStartDocument() {
		log.trace("BSONWriter: writeStartDocument()");
		realWriter.writeStartDocument();
	}

	@Override
	public void writeStartDocument(String name) {
		log.trace("BSONWriter: writeStartDocument("+name+")");
		realWriter.writeStartDocument(name);
	}

	@Override
	public void writeString(String value) {
		log.trace("BSONWriter: writeString("+v(value)+")");
		realWriter.writeString(value);
	}

	@Override
	public void writeString(String name, String value) {
		log.trace("BSONWriter: writeString("+name+", "+v(value)+")");
		realWriter.writeString(name, value);
	}

	@Override
	public void writeSymbol(String value) {
		log.trace("BSONWriter: writeSymbol("+v(value)+")");
		realWriter.writeSymbol(value);
	}

	@Override
	public void writeSymbol(String name, String value) {
		log.trace("BSONWriter: writeSymbol("+name+", "+v(value)+")");
		realWriter.writeSymbol(name, value);
	}

	@Override
	public void writeTimestamp(BsonTimestamp value) {
		log.trace("BSONWriter: writeTimestamp("+v(value)+")");
		realWriter.writeTimestamp(value);
	}

	@Override
	public void writeTimestamp(String name, BsonTimestamp value) {
		log.trace("BSONWriter: writeTimestamp("+name+", "+v(value)+")");
		realWriter.writeTimestamp(name, value);
	}

	@Override
	public void writeUndefined() {
		log.trace("BSONWriter: writeUndefined()");
		realWriter.writeUndefined();
	}

	@Override
	public void writeUndefined(String name) {
		log.trace("BSONWriter: writeUndefined("+name+")");
		realWriter.writeUndefined(name);
	}

	@Override
	public void pipe(BsonReader reader) {
		log.trace("BSONWriter: pipe("+reader+")");
		realWriter.pipe(reader);
	}

	protected String v (Object o) {
		return null == o ? "<null>" : o.toString();
	}
}
