package ca.uhn.fhir.parser.json;

import java.io.IOException;
import java.io.Writer;
import java.math.BigDecimal;
import java.math.BigInteger;

/*
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

public abstract class JsonLikeWriter {

	private boolean prettyPrint;
	private Writer writer;

	public JsonLikeWriter() {
		super();
	}

	public boolean isPrettyPrint() {
		return prettyPrint;
	}

	public void setPrettyPrint(boolean tf) {
		prettyPrint = tf;
	}

	public Writer getWriter() {
		return writer;
	}

	public void setWriter(Writer writer) {
		this.writer = writer;
	}

	public abstract JsonLikeWriter init() throws IOException;

	public abstract JsonLikeWriter flush() throws IOException;

	public abstract void close() throws IOException;

	public abstract JsonLikeWriter beginObject() throws IOException;

	public abstract JsonLikeWriter beginObject(String name) throws IOException;

	public abstract JsonLikeWriter beginArray(String name) throws IOException;

	public abstract JsonLikeWriter write(String value) throws IOException;

	public abstract JsonLikeWriter write(BigInteger value) throws IOException;

	public abstract JsonLikeWriter write(BigDecimal value) throws IOException;

	public abstract JsonLikeWriter write(long value) throws IOException;

	public abstract JsonLikeWriter write(double value) throws IOException;

	public abstract JsonLikeWriter write(Boolean value) throws IOException;

	public abstract JsonLikeWriter write(boolean value) throws IOException;

	public abstract JsonLikeWriter writeNull() throws IOException;

	public abstract JsonLikeWriter write(String name, String value) throws IOException;

	public abstract JsonLikeWriter write(String name, BigInteger value) throws IOException;

	public abstract JsonLikeWriter write(String name, BigDecimal value) throws IOException;

	public abstract JsonLikeWriter write(String name, long value) throws IOException;

	public abstract JsonLikeWriter write(String name, double value) throws IOException;

	public abstract JsonLikeWriter write(String name, Boolean value) throws IOException;

	public abstract JsonLikeWriter write(String name, boolean value) throws IOException;

	public abstract JsonLikeWriter endObject() throws IOException;

	public abstract JsonLikeWriter endArray() throws IOException;

	public abstract JsonLikeWriter endBlock() throws IOException;

}
