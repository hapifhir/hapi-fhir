package ca.uhn.fhir.parser.json.jackson;

import ca.uhn.fhir.parser.json.JsonLikeWriter;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.PrettyPrinter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;

import java.io.IOException;
import java.io.Writer;
import java.math.BigDecimal;
import java.math.BigInteger;

public class JacksonWriter extends JsonLikeWriter {

	private static final JsonFactory JSON_FACTORY = new JsonFactory();

	private JsonGenerator myJsonGenerator;

	public JacksonWriter(Writer writer) throws IOException {
		myJsonGenerator = JSON_FACTORY.createGenerator(writer);
		setWriter(writer);
	}

	public JacksonWriter() {
	}

	@Override
	public JsonLikeWriter init() {
		if (isPrettyPrint()) {
			PrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
			myJsonGenerator.setPrettyPrinter(prettyPrinter);
		}
		return this;
	}

	@Override
	public JsonLikeWriter flush() {
		return this;
	}

	@Override
	public void close() throws IOException {
		myJsonGenerator.close();
		getWriter().close();
	}

	@Override
	public JsonLikeWriter beginObject() throws IOException {
		myJsonGenerator.writeStartObject();
		return this;
	}

	@Override
	public JsonLikeWriter beginObject(String name) throws IOException {
		myJsonGenerator.writeObjectFieldStart(name);
		return this;
	}

	@Override
	public JsonLikeWriter beginArray(String name) throws IOException {
		myJsonGenerator.writeArrayFieldStart(name);
		return this;
	}

	@Override
	public JsonLikeWriter write(String value) throws IOException {
		myJsonGenerator.writeObject(value);
		return this;
	}

	@Override
	public JsonLikeWriter write(BigInteger value) throws IOException {
		myJsonGenerator.writeObject(value);
		return this;
	}

	@Override
	public JsonLikeWriter write(BigDecimal value) throws IOException {
		myJsonGenerator.writeObject(value);
		return this;
	}

	@Override
	public JsonLikeWriter write(long value) throws IOException {
		myJsonGenerator.writeObject(value);
		return this;
	}

	@Override
	public JsonLikeWriter write(double value) throws IOException {
		myJsonGenerator.writeObject(value);
		return this;
	}

	@Override
	public JsonLikeWriter write(Boolean value) throws IOException {
		myJsonGenerator.writeObject(value);
		return this;
	}

	@Override
	public JsonLikeWriter write(boolean value) throws IOException {
		myJsonGenerator.writeObject(value);
		return this;
	}

	@Override
	public JsonLikeWriter writeNull() throws IOException {
		myJsonGenerator.writeNull();
		return this;
	}

	@Override
	public JsonLikeWriter write(String name, String value) throws IOException {
		myJsonGenerator.writeObjectField(name, value);
		return this;
	}

	@Override
	public JsonLikeWriter write(String name, BigInteger value) throws IOException {
		myJsonGenerator.writeObjectField(name, value);
		return this;
	}

	@Override
	public JsonLikeWriter write(String name, BigDecimal value) throws IOException {
		myJsonGenerator.writeObjectField(name, value);
		return this;
	}

	@Override
	public JsonLikeWriter write(String name, long value) throws IOException {
		myJsonGenerator.writeObjectField(name, value);
		return this;
	}

	@Override
	public JsonLikeWriter write(String name, double value) throws IOException {
		myJsonGenerator.writeObjectField(name, value);
		return this;
	}

	@Override
	public JsonLikeWriter write(String name, Boolean value) throws IOException {
		myJsonGenerator.writeObjectField(name, value);
		return this;
	}

	@Override
	public JsonLikeWriter write(String name, boolean value) throws IOException {
		myJsonGenerator.writeObjectField(name, value);
		return this;
	}

	@Override
	public JsonLikeWriter endObject() throws IOException {
		myJsonGenerator.writeEndObject();
		return this;
	}

	@Override
	public JsonLikeWriter endArray() throws IOException {
		myJsonGenerator.writeEndArray();
		return this;
	}

	@Override
	public JsonLikeWriter endBlock() throws IOException {
		myJsonGenerator.writeEndObject();
		return this;
	}
}
