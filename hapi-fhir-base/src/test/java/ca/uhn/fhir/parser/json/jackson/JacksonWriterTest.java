package ca.uhn.fhir.parser.json.jackson;

import ca.uhn.fhir.parser.json.BaseJsonLikeWriter;
import org.junit.jupiter.api.Test;
import tools.jackson.core.json.JsonFactory;

import java.io.IOException;
import java.io.Writer;
import java.math.BigDecimal;
import java.math.BigInteger;

import static org.assertj.core.api.Assertions.assertThat;

class JacksonWriterTest {

	private static final String STRING_FIELD = "string";
	private static final String BIG_INTEGER_FIELD = "bigInteger";
	private static final String BIG_DECIMAL_FIELD = "bigDecimal";
	private static final String LONG_FIELD = "long";
	private static final String DOUBLE_FIELD = "double";
	private static final String BOOLEAN_OBJECT_FIELD = "booleanObject";
	private static final String BOOLEAN_PRIMITIVE_FIELD = "booleanPrimitive";
	private static final String NULLABLE_BOOLEAN_FIELD = "nullableBoolean";
	private static final String NULLABLE_STRING_FIELD = "nullableString";
	private static final String ARRAY_FIELD = "array";
	private static final String NESTED_FIELD = "nested";
	private static final String OBJECT_FIELD = "object";
	private static final String INNER_FIELD = "inner";
	private static final String TEXT_VALUE = "text";
	private static final String STRING_VALUE = "hello";
	private static final String BIG_INTEGER_VALUE = "12345678901234567890";
	private static final String BIG_DECIMAL_VALUE = "123.4500";
	private static final long LONG_VALUE = 42L;
	private static final double DOUBLE_VALUE = 3.14d;
	private static final String NESTED_VALUE = "value";
	private static final String EXPECTED_COMPACT_JSON = String.format(
		"{\"%s\":\"%s\",\"%s\":%s,\"%s\":%s,\"%s\":%s,\"%s\":%s,\"%s\":true,\"%s\":false,\"%s\":null,\"%s\":null,\"%s\":[\"%s\",2,3.45,4,5.5,true,false,null,{\"%s\":\"%s\"}],\"%s\":{\"%s\":\"%s\"}}",
		STRING_FIELD,
		STRING_VALUE,
		BIG_INTEGER_FIELD,
		BIG_INTEGER_VALUE,
		BIG_DECIMAL_FIELD,
		BIG_DECIMAL_VALUE,
		LONG_FIELD,
		LONG_VALUE,
		DOUBLE_FIELD,
		DOUBLE_VALUE,
		BOOLEAN_OBJECT_FIELD,
		BOOLEAN_PRIMITIVE_FIELD,
		NULLABLE_BOOLEAN_FIELD,
		NULLABLE_STRING_FIELD,
		ARRAY_FIELD,
		TEXT_VALUE,
		NESTED_FIELD,
		NESTED_VALUE,
		OBJECT_FIELD,
		INNER_FIELD,
		NESTED_VALUE);

	@Test
	void writesCompactJsonAcrossAllWriterOverloads() throws IOException {
		TrackingWriter trackingWriter = new TrackingWriter();
		JacksonWriter writer = new JacksonWriter(new JsonFactory(), trackingWriter);

		writer.init()
			.beginObject()
			.write(STRING_FIELD, STRING_VALUE)
			.write(BIG_INTEGER_FIELD, new BigInteger(BIG_INTEGER_VALUE))
			.write(BIG_DECIMAL_FIELD, new BigDecimal(BIG_DECIMAL_VALUE))
			.write(LONG_FIELD, LONG_VALUE)
			.write(DOUBLE_FIELD, DOUBLE_VALUE)
			.write(BOOLEAN_OBJECT_FIELD, Boolean.TRUE)
			.write(BOOLEAN_PRIMITIVE_FIELD, false)
			.write(NULLABLE_BOOLEAN_FIELD, (Boolean) null)
			.write(NULLABLE_STRING_FIELD, (String) null)
			.beginArray(ARRAY_FIELD)
			.write(TEXT_VALUE)
			.write(new BigInteger("2"))
			.write(new BigDecimal("3.45"))
			.write(4L)
			.write(5.5d)
			.write(Boolean.TRUE)
			.write(false)
			.writeNull()
			.beginObject()
			.write(NESTED_FIELD, NESTED_VALUE)
			.endObject()
			.endArray()
			.beginObject(OBJECT_FIELD)
			.write(INNER_FIELD, NESTED_VALUE)
			.endObject()
			.endObject();
		writer.close();

		String actualJson = trackingWriter.toString();
		assertThat(actualJson).isEqualTo(EXPECTED_COMPACT_JSON);
	}

	@Test
	void prettyPrintUsesConfiguredIndentationAndLineEndings() throws IOException {
		TrackingWriter trackingWriter = new TrackingWriter();
		JacksonWriter writer = new JacksonWriter(new JsonFactory(), trackingWriter);
		writer.setPrettyPrint(true);

		writer.init()
			.beginObject()
			.write("alpha", "beta")
			.beginObject("nested")
			.write("gamma", 1L)
			.endObject()
			.endObject();
		writer.close();

		assertThat(trackingWriter.toString())
			.isEqualTo("{\n  \"alpha\": \"beta\",\n  \"nested\": {\n    \"gamma\": 1\n  }\n}");
	}

	@Test
	void closeDoesNotCloseUnderlyingWriterWhenUsingJacksonStructureFactory() throws IOException {
		TrackingWriter trackingWriter = new TrackingWriter();
		BaseJsonLikeWriter writer = new JacksonStructure().getJsonLikeWriter(trackingWriter);

		writer.init()
			.beginObject()
			.write("status", "ok")
			.endObject();
		writer.close();

		assertThat(trackingWriter.isClosed()).isFalse();
		assertThat(trackingWriter.toString()).isEqualTo("{\"status\":\"ok\"}");
	}

	@Test
	void initAndFlushAreChainable() throws IOException {
		TrackingWriter trackingWriter = new TrackingWriter();
		JacksonWriter writer = new JacksonWriter(new JsonFactory(), trackingWriter);

		assertThat(writer.init()).isSameAs(writer);
		assertThat(writer.flush()).isSameAs(writer);
	}

	private static class TrackingWriter extends Writer {

		private final StringBuilder myBuffer = new StringBuilder();
		private boolean myClosed;

		@Override
		public void write(char[] theChars, int theOffset, int theLength) {
			myBuffer.append(theChars, theOffset, theLength);
		}

		@Override
		public void flush() {}

		@Override
		public void close() {
			myClosed = true;
		}

		boolean isClosed() {
			return myClosed;
		}

		@Override
		public String toString() {
			return myBuffer.toString();
		}
	}
}
