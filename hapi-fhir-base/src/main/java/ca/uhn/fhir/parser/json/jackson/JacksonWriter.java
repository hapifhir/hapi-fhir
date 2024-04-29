/*-
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
package ca.uhn.fhir.parser.json.jackson;

import ca.uhn.fhir.parser.json.BaseJsonLikeWriter;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.core.util.Separators;

import java.io.IOException;
import java.io.Writer;
import java.math.BigDecimal;
import java.math.BigInteger;

public class JacksonWriter extends BaseJsonLikeWriter {

	private JsonGenerator myJsonGenerator;

	public JacksonWriter(JsonFactory theJsonFactory, Writer theWriter) throws IOException {
		myJsonGenerator = theJsonFactory.createGenerator(theWriter);
		setWriter(theWriter);
	}

	public JacksonWriter() {}

	@Override
	public BaseJsonLikeWriter init() {
		if (isPrettyPrint()) {
			DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter()
					.withSeparators(new Separators(
							Separators.DEFAULT_ROOT_VALUE_SEPARATOR,
							':',
							Separators.Spacing.AFTER,
							',',
							Separators.Spacing.NONE,
							',',
							Separators.Spacing.NONE));
			prettyPrinter = prettyPrinter.withObjectIndenter(new DefaultIndenter("  ", "\n"));

			myJsonGenerator.setPrettyPrinter(prettyPrinter);
		}
		return this;
	}

	@Override
	public BaseJsonLikeWriter flush() {
		return this;
	}

	@Override
	public void close() throws IOException {
		myJsonGenerator.close();
	}

	@Override
	public BaseJsonLikeWriter beginObject() throws IOException {
		myJsonGenerator.writeStartObject();
		return this;
	}

	@Override
	public BaseJsonLikeWriter beginObject(String name) throws IOException {
		myJsonGenerator.writeObjectFieldStart(name);
		return this;
	}

	@Override
	public BaseJsonLikeWriter beginArray(String name) throws IOException {
		myJsonGenerator.writeArrayFieldStart(name);
		return this;
	}

	@Override
	public BaseJsonLikeWriter write(String value) throws IOException {
		myJsonGenerator.writeObject(value);
		return this;
	}

	@Override
	public BaseJsonLikeWriter write(BigInteger value) throws IOException {
		myJsonGenerator.writeObject(value);
		return this;
	}

	@Override
	public BaseJsonLikeWriter write(BigDecimal value) throws IOException {
		myJsonGenerator.writeObject(value);
		return this;
	}

	@Override
	public BaseJsonLikeWriter write(long value) throws IOException {
		myJsonGenerator.writeObject(value);
		return this;
	}

	@Override
	public BaseJsonLikeWriter write(double value) throws IOException {
		myJsonGenerator.writeObject(value);
		return this;
	}

	@Override
	public BaseJsonLikeWriter write(Boolean value) throws IOException {
		myJsonGenerator.writeObject(value);
		return this;
	}

	@Override
	public BaseJsonLikeWriter write(boolean value) throws IOException {
		myJsonGenerator.writeObject(value);
		return this;
	}

	@Override
	public BaseJsonLikeWriter writeNull() throws IOException {
		myJsonGenerator.writeNull();
		return this;
	}

	@Override
	public BaseJsonLikeWriter write(String name, String value) throws IOException {
		myJsonGenerator.writeObjectField(name, value);
		return this;
	}

	@Override
	public BaseJsonLikeWriter write(String name, BigInteger value) throws IOException {
		myJsonGenerator.writeObjectField(name, value);
		return this;
	}

	@Override
	public BaseJsonLikeWriter write(String name, BigDecimal value) throws IOException {
		myJsonGenerator.writeObjectField(name, value);
		return this;
	}

	@Override
	public BaseJsonLikeWriter write(String name, long value) throws IOException {
		myJsonGenerator.writeObjectField(name, value);
		return this;
	}

	@Override
	public BaseJsonLikeWriter write(String name, double value) throws IOException {
		myJsonGenerator.writeObjectField(name, value);
		return this;
	}

	@Override
	public BaseJsonLikeWriter write(String name, Boolean value) throws IOException {
		myJsonGenerator.writeObjectField(name, value);
		return this;
	}

	@Override
	public BaseJsonLikeWriter write(String name, boolean value) throws IOException {
		myJsonGenerator.writeObjectField(name, value);
		return this;
	}

	@Override
	public BaseJsonLikeWriter endObject() throws IOException {
		myJsonGenerator.writeEndObject();
		return this;
	}

	@Override
	public BaseJsonLikeWriter endArray() throws IOException {
		myJsonGenerator.writeEndArray();
		return this;
	}

	@Override
	public BaseJsonLikeWriter endBlock() throws IOException {
		myJsonGenerator.writeEndObject();
		return this;
	}
}
