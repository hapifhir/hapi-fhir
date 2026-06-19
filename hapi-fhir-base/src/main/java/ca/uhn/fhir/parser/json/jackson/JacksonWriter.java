/*-
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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
import tools.jackson.core.FormatSchema;
import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonGenerator;
import tools.jackson.core.ObjectWriteContext;
import tools.jackson.core.PrettyPrinter;
import tools.jackson.core.SerializableString;
import tools.jackson.core.TokenStreamFactory;
import tools.jackson.core.TreeNode;
import tools.jackson.core.io.CharacterEscapes;
import tools.jackson.core.json.JsonFactory;
import tools.jackson.core.tree.ArrayTreeNode;
import tools.jackson.core.tree.ObjectTreeNode;
import tools.jackson.core.util.DefaultIndenter;
import tools.jackson.core.util.DefaultPrettyPrinter;

import java.io.IOException;
import java.io.Writer;
import java.math.BigDecimal;
import java.math.BigInteger;

public class JacksonWriter extends BaseJsonLikeWriter {

	private JsonFactory myJsonFactory;
	private Writer myTargetWriter;
	private JsonGenerator myJsonGenerator;

	public JacksonWriter(JsonFactory theJsonFactory, Writer theWriter) {
		myJsonFactory = theJsonFactory;
		myTargetWriter = theWriter;
		setWriter(theWriter);
	}

	public JacksonWriter() {}

	@Override
	public BaseJsonLikeWriter init() {
		// In Jackson 3.x, JsonGenerator is immutable once created and no longer offers
		// setPrettyPrinter(..). The pretty printer must instead be supplied up front via
		// the ObjectWriteContext passed to createGenerator(..), so generator creation is
		// deferred to here (where isPrettyPrint() is known) rather than done in the
		// constructor. ObjectWriteContext is an interface with default methods, so we
		// only need to override the one accessor (getPrettyPrinter()) that matters here.
		ObjectWriteContext writeContext;
		if (isPrettyPrint()) {
			//JACKSONTOOLS3-TODO.  Implement Pretty-Print.  See : https://github.com/FasterXML/jackson-databind/issues/5331
			writeContext = ObjectWriteContext.empty();
		} else {
			writeContext = ObjectWriteContext.empty();
		}
		myJsonGenerator = myJsonFactory.createGenerator(writeContext, myTargetWriter);
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
		myJsonGenerator.writeStartObject(name);
		return this;
	}

	@Override
	public BaseJsonLikeWriter beginArray(String name) throws IOException {
		myJsonGenerator.writeStartArray(name);
		return this;
	}

	@Override
	public BaseJsonLikeWriter write(String value) throws IOException {
		myJsonGenerator.writePOJO(value);
		return this;
	}

	@Override
	public BaseJsonLikeWriter write(BigInteger value) throws IOException {
		myJsonGenerator.writePOJO(value);
		return this;
	}

	@Override
	public BaseJsonLikeWriter write(BigDecimal value) throws IOException {
		myJsonGenerator.writePOJO(value);
		return this;
	}

	@Override
	public BaseJsonLikeWriter write(long value) throws IOException {
		myJsonGenerator.writePOJO(value);
		return this;
	}

	@Override
	public BaseJsonLikeWriter write(double value) throws IOException {
		myJsonGenerator.writePOJO(value);
		return this;
	}

	@Override
	public BaseJsonLikeWriter write(Boolean value) throws IOException {
		myJsonGenerator.writeBoolean(value);
		return this;
	}

	@Override
	public BaseJsonLikeWriter write(boolean value) throws IOException {
		myJsonGenerator.writeBoolean(value);
		return this;
	}

	@Override
	public BaseJsonLikeWriter writeNull() throws IOException {
		myJsonGenerator.writeNull();
		return this;
	}

	@Override
	public BaseJsonLikeWriter write(String name, String value) throws IOException {
		myJsonGenerator.writeStringProperty(name, value);
		return this;
	}

	@Override
	public BaseJsonLikeWriter write(String name, BigInteger value) throws IOException {
		myJsonGenerator.writeNumberProperty(name, value);
		return this;
	}

	@Override
	public BaseJsonLikeWriter write(String name, BigDecimal value) throws IOException {
		myJsonGenerator.writeNumberProperty(name, value);
		return this;
	}

	@Override
	public BaseJsonLikeWriter write(String name, long value) throws IOException {
		myJsonGenerator.writeNumberProperty(name, value);
		return this;
	}

	@Override
	public BaseJsonLikeWriter write(String name, double value) throws IOException {
		myJsonGenerator.writeNumberProperty(name, value);
		return this;
	}

	@Override
	public BaseJsonLikeWriter write(String name, Boolean value) throws IOException {
		myJsonGenerator.writeBooleanProperty(name, value);
		return this;
	}

	@Override
	public BaseJsonLikeWriter write(String name, boolean value) throws IOException {
		myJsonGenerator.writeBooleanProperty(name, value);
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
