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

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.parser.json.BaseJsonLikeWriter;
import tools.jackson.core.FormatSchema;
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
import tools.jackson.core.util.Separators;

import java.io.FilterWriter;
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
		setWriter(theWriter);
	}

	public JacksonWriter() {}

	@Override
	public void setWriter(Writer theWriter) {
		super.setWriter(theWriter);
		myTargetWriter = theWriter != null ? new NonClosingWriter(theWriter) : null;
	}

	@Override
	public BaseJsonLikeWriter init() {
		// In Jackson 3.x, JsonGenerator is immutable once created and no longer offers
		// setPrettyPrinter(..). The pretty printer must instead be supplied up front via
		// the ObjectWriteContext passed to createGenerator(..), so generator creation is
		// deferred to here (where isPrettyPrint() is known) rather than done in the
		// constructor.
		//
		// ObjectWriteContext has no default methods in this Jackson version, so every
		// method must be implemented. Since write(...) below has been changed to call
		// only type-specific JsonGenerator methods (writeString/writeNumber/writeBoolean/
		// writeNull and their *Property variants) rather than writePOJO(..), this writer
		// never relies on ObjectWriteContext.writeValue(..)/writeTree(..) for correctness;
		// they still throw rather than silently no-op, so any future code path that does
		// reach them fails loudly instead of producing corrupt JSON.
		final PrettyPrinter prettyPrinter;
		if (isPrettyPrint()) {
			Separators separators =
					Separators.createDefaultInstance().withObjectNameValueSpacing(Separators.Spacing.AFTER);
			DefaultPrettyPrinter defaultPrettyPrinter = new DefaultPrettyPrinter(separators);
			prettyPrinter = defaultPrettyPrinter.withObjectIndenter(new DefaultIndenter("  ", "\n"));
		} else {
			prettyPrinter = null;
		}

		ObjectWriteContext writeContext = new ObjectWriteContext() {
			@Override
			public FormatSchema getSchema() {
				return null;
			}

			@Override
			public CharacterEscapes getCharacterEscapes() {
				return null;
			}

			@Override
			public PrettyPrinter getPrettyPrinter() {
				return prettyPrinter;
			}

			@Override
			public boolean hasPrettyPrinter() {
				return prettyPrinter != null;
			}

			@Override
			public SerializableString getRootValueSeparator(SerializableString defaultSeparator) {
				return defaultSeparator;
			}

			@Override
			public int getStreamWriteFeatures(int defaults) {
				return defaults;
			}

			@Override
			public int getFormatWriteFeatures(int defaults) {
				return defaults;
			}

			@Override
			public TokenStreamFactory tokenStreamFactory() {
				return myJsonFactory;
			}

			@Override
			public ArrayTreeNode createArrayNode() {
				throw new UnsupportedOperationException(Msg.code(2997) + "Tree-node creation is not supported by "
						+ JacksonWriter.class.getSimpleName());
			}

			@Override
			public ObjectTreeNode createObjectNode() {
				throw new UnsupportedOperationException(Msg.code(2998) + "Tree-node creation is not supported by "
						+ JacksonWriter.class.getSimpleName());
			}

			@Override
			public void writeValue(JsonGenerator gen, Object value) {
				throw new UnsupportedOperationException(
						Msg.code(2999) + JacksonWriter.class.getSimpleName()
								+ " writes values via type-specific JsonGenerator methods and does not support writePOJO(..)/writeValue(..)");
			}

			@Override
			public void writeTree(JsonGenerator gen, TreeNode tree) {
				throw new UnsupportedOperationException(
						Msg.code(3000) + "Tree writing is not supported by " + JacksonWriter.class.getSimpleName());
			}
		};

		Writer targetWriter = myTargetWriter;
		if (targetWriter == null && getWriter() != null) {
			targetWriter = new NonClosingWriter(getWriter());
			myTargetWriter = targetWriter;
		}
		myJsonGenerator = myJsonFactory.createGenerator(writeContext, targetWriter);
		return this;
	}

	@Override
	public BaseJsonLikeWriter flush() {
		return this;
	}

	@Override
	public void close() {
		myJsonGenerator.close();
	}

	@Override
	public BaseJsonLikeWriter beginObject() {
		myJsonGenerator.writeStartObject();
		return this;
	}

	@Override
	public BaseJsonLikeWriter beginObject(String name) {
		myJsonGenerator.writeObjectPropertyStart(name);
		return this;
	}

	@Override
	public BaseJsonLikeWriter beginArray(String name) {
		myJsonGenerator.writeArrayPropertyStart(name);
		return this;
	}

	@Override
	public BaseJsonLikeWriter write(String value) {
		myJsonGenerator.writeString(value);
		return this;
	}

	@Override
	public BaseJsonLikeWriter write(BigInteger value) {
		myJsonGenerator.writeNumber(value);
		return this;
	}

	@Override
	public BaseJsonLikeWriter write(BigDecimal value) {
		myJsonGenerator.writeNumber(value);
		return this;
	}

	@Override
	public BaseJsonLikeWriter write(long value) {
		myJsonGenerator.writeNumber(value);
		return this;
	}

	@Override
	public BaseJsonLikeWriter write(double value) {
		myJsonGenerator.writeNumber(value);
		return this;
	}

	@Override
	public BaseJsonLikeWriter write(Boolean value) {
		if (value == null) {
			myJsonGenerator.writeNull();
		} else {
			myJsonGenerator.writeBoolean(value);
		}
		return this;
	}

	@Override
	public BaseJsonLikeWriter write(boolean value) {
		myJsonGenerator.writeBoolean(value);
		return this;
	}

	@Override
	public BaseJsonLikeWriter writeNull() {
		myJsonGenerator.writeNull();
		return this;
	}

	@Override
	public BaseJsonLikeWriter write(String name, String value) {
		myJsonGenerator.writeStringProperty(name, value);
		return this;
	}

	@Override
	public BaseJsonLikeWriter write(String name, BigInteger value) {
		myJsonGenerator.writeName(name);
		myJsonGenerator.writeNumber(value);
		return this;
	}

	@Override
	public BaseJsonLikeWriter write(String name, BigDecimal value) {
		myJsonGenerator.writeName(name);
		myJsonGenerator.writeNumber(value);
		return this;
	}

	@Override
	public BaseJsonLikeWriter write(String name, long value) {
		myJsonGenerator.writeNumberProperty(name, value);
		return this;
	}

	@Override
	public BaseJsonLikeWriter write(String name, double value) {
		myJsonGenerator.writeNumberProperty(name, value);
		return this;
	}

	@Override
	public BaseJsonLikeWriter write(String name, Boolean value) {
		if (value == null) {
			myJsonGenerator.writeName(name);
			myJsonGenerator.writeNull();
		} else {
			myJsonGenerator.writeBooleanProperty(name, value);
		}
		return this;
	}

	@Override
	public BaseJsonLikeWriter write(String name, boolean value) {
		myJsonGenerator.writeBooleanProperty(name, value);
		return this;
	}

	@Override
	public BaseJsonLikeWriter endObject() {
		myJsonGenerator.writeEndObject();
		return this;
	}

	@Override
	public BaseJsonLikeWriter endArray() {
		myJsonGenerator.writeEndArray();
		return this;
	}

	@Override
	public BaseJsonLikeWriter endBlock() {
		myJsonGenerator.writeEndObject();
		return this;
	}

	private static class NonClosingWriter extends FilterWriter {

		private final Writer myDelegate;

		private NonClosingWriter(Writer theDelegate) {
			super(theDelegate);
			myDelegate = theDelegate;
		}

		@Override
		public void close() throws IOException {
			myDelegate.flush();
		}
	}
}
