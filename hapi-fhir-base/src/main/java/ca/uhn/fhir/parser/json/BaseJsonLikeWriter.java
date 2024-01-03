/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
package ca.uhn.fhir.parser.json;

import java.io.IOException;
import java.io.Writer;
import java.math.BigDecimal;
import java.math.BigInteger;

public abstract class BaseJsonLikeWriter {

	private boolean prettyPrint;
	private Writer writer;

	public BaseJsonLikeWriter() {
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

	public abstract BaseJsonLikeWriter init() throws IOException;

	public abstract BaseJsonLikeWriter flush() throws IOException;

	public abstract void close() throws IOException;

	public abstract BaseJsonLikeWriter beginObject() throws IOException;

	public abstract BaseJsonLikeWriter beginObject(String name) throws IOException;

	public abstract BaseJsonLikeWriter beginArray(String name) throws IOException;

	public abstract BaseJsonLikeWriter write(String value) throws IOException;

	public abstract BaseJsonLikeWriter write(BigInteger value) throws IOException;

	public abstract BaseJsonLikeWriter write(BigDecimal value) throws IOException;

	public abstract BaseJsonLikeWriter write(long value) throws IOException;

	public abstract BaseJsonLikeWriter write(double value) throws IOException;

	public abstract BaseJsonLikeWriter write(Boolean value) throws IOException;

	public abstract BaseJsonLikeWriter write(boolean value) throws IOException;

	public abstract BaseJsonLikeWriter writeNull() throws IOException;

	public abstract BaseJsonLikeWriter write(String name, String value) throws IOException;

	public abstract BaseJsonLikeWriter write(String name, BigInteger value) throws IOException;

	public abstract BaseJsonLikeWriter write(String name, BigDecimal value) throws IOException;

	public abstract BaseJsonLikeWriter write(String name, long value) throws IOException;

	public abstract BaseJsonLikeWriter write(String name, double value) throws IOException;

	public abstract BaseJsonLikeWriter write(String name, Boolean value) throws IOException;

	public abstract BaseJsonLikeWriter write(String name, boolean value) throws IOException;

	public abstract BaseJsonLikeWriter endObject() throws IOException;

	public abstract BaseJsonLikeWriter endArray() throws IOException;

	public abstract BaseJsonLikeWriter endBlock() throws IOException;
}
