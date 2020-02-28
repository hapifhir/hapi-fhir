package ca.uhn.fhir.parser.json.jackson;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.parser.json.JsonLikeWriter;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;

import java.io.IOException;
import java.io.Writer;
import java.math.BigDecimal;
import java.math.BigInteger;

public class JacksonWriter extends JsonLikeWriter {

    private JsonGenerator jsonGenerator;

    private enum BlockType {
        NONE, OBJECT, ARRAY
    }

    private BlockType blockType = BlockType.NONE;

    public JacksonWriter(Writer writer) {
        try {
            this.jsonGenerator = new JsonFactory().createGenerator(writer);
        } catch (IOException e) {
            e.printStackTrace();
            throw new ConfigurationException(e.getMessage());
        }

        setWriter(writer);
    }

    public JacksonWriter() {}

    @Override
    public JsonLikeWriter init() {
        blockType = BlockType.NONE;
        return this;
    }

    @Override
    public JsonLikeWriter flush() {
        return this;
    }

    @Override
    public void close() throws IOException {
        getWriter().close();
    }

    @Override
    public JsonLikeWriter beginObject() throws IOException {
        blockType = BlockType.OBJECT;
        jsonGenerator.writeStartObject();
        return this;
    }

    @Override
    public JsonLikeWriter beginArray() throws IOException {
        blockType = BlockType.ARRAY;
        jsonGenerator.writeStartArray();
        return this;
    }

    @Override
    public JsonLikeWriter beginObject(String name) throws IOException {
        blockType = BlockType.OBJECT;
        jsonGenerator.writeObjectFieldStart(name);
        return this;
    }

    @Override
    public JsonLikeWriter beginArray(String name) throws IOException {
        blockType = BlockType.ARRAY;
        jsonGenerator.writeArrayFieldStart(name);
        return this;
    }

    @Override
    public JsonLikeWriter write(String value) throws IOException {
        jsonGenerator.writeObject(value);
        return this;
    }

    @Override
    public JsonLikeWriter write(BigInteger value) throws IOException {
        jsonGenerator.writeObject(value);
        return this;
    }

    @Override
    public JsonLikeWriter write(BigDecimal value) throws IOException {
        jsonGenerator.writeObject(value);
        return this;
    }

    @Override
    public JsonLikeWriter write(long value) throws IOException {
        jsonGenerator.writeObject(value);
        return this;
    }

    @Override
    public JsonLikeWriter write(double value) throws IOException {
        jsonGenerator.writeObject(value);
        return this;
    }

    @Override
    public JsonLikeWriter write(Boolean value) throws IOException {
        jsonGenerator.writeObject(value);
        return this;
    }

    @Override
    public JsonLikeWriter write(boolean value) throws IOException {
        jsonGenerator.writeObject(value);
        return this;
    }

    @Override
    public JsonLikeWriter writeNull() throws IOException {
        jsonGenerator.writeNull();
        return this;
    }

    @Override
    public JsonLikeWriter write(String name, String value) throws IOException {
        jsonGenerator.writeObjectField(name, value);
        return this;
    }

    @Override
    public JsonLikeWriter write(String name, BigInteger value) throws IOException {
        jsonGenerator.writeObjectField(name, value);
        return this;
    }

    @Override
    public JsonLikeWriter write(String name, BigDecimal value) throws IOException {
        jsonGenerator.writeObjectField(name, value);
        return this;
    }

    @Override
    public JsonLikeWriter write(String name, long value) throws IOException {
        jsonGenerator.writeObjectField(name, value);
        return this;
    }

    @Override
    public JsonLikeWriter write(String name, double value) throws IOException {
        jsonGenerator.writeObjectField(name, value);
        return this;
    }

    @Override
    public JsonLikeWriter write(String name, Boolean value) throws IOException {
        jsonGenerator.writeObjectField(name, value);
        return this;
    }

    @Override
    public JsonLikeWriter write(String name, boolean value) throws IOException {
        jsonGenerator.writeObjectField(name, value);
        return this;
    }

    @Override
    public JsonLikeWriter writeNull(String name) throws IOException {
        jsonGenerator.writeNullField(name);
        return this;
    }

    @Override
    public JsonLikeWriter endObject() throws IOException {
        if (blockType != BlockType.OBJECT) {
            jsonGenerator.writeEndArray();
        } else {
            jsonGenerator.writeEndObject();
        }

        if (blockType == BlockType.NONE) {
            jsonGenerator.close();
        }

        return this;
    }

    @Override
    public JsonLikeWriter endArray() throws IOException {
        if (blockType == BlockType.OBJECT) {
            jsonGenerator.writeEndObject();
        } else {
            jsonGenerator.writeEndArray();
        }

        if (blockType == BlockType.NONE) {
            jsonGenerator.close();
        }

        return this;
    }

    @Override
    public JsonLikeWriter endBlock() throws IOException {
        if (blockType == BlockType.ARRAY) {
            jsonGenerator.writeEndArray();
        } else {
            jsonGenerator.writeEndObject();
        }

        if (blockType == BlockType.NONE) {
            jsonGenerator.close();
        }

        return this;
    }
}
