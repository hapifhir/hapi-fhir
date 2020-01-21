package ca.uhn.fhir.parser.json.jackson;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.io.Writer;

public class JacksonSerializer extends StdSerializer<Object> {

    private JsonGenerator jsonGenerator;

    public JacksonSerializer(Writer writer) {
        super(Object.class);
        try {
            this.jsonGenerator = new JsonFactory().createGenerator(writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void serialize(Object resource, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
            throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeObject(resource);
        jsonGenerator.writeEndObject();
    }

    public void writeStartObject() throws IOException {
        jsonGenerator.writeStartObject();
    }

    public void writeStartObject(String name) throws IOException {
        jsonGenerator.writeObjectFieldStart(name);
    }

    public void writeStartArray() throws IOException {
        jsonGenerator.writeStartArray();
    }

    public void writeStartArray(String name) throws IOException {
        jsonGenerator.writeArrayFieldStart(name);
    }

    public void write(Object value) throws IOException {
        jsonGenerator.writeObject(value);
    }

    public void writeNull() throws IOException {
        jsonGenerator.writeNull();
    }

    public void writeNull(String name) throws IOException {
        jsonGenerator.writeNullField(name);
    }

    public void write(String name, Object value) throws IOException {
        jsonGenerator.writeObjectField(name, value);
    }

    public void endObject() throws IOException {
        jsonGenerator.writeEndObject();
    }

    public void endArray() throws IOException {
        jsonGenerator.writeEndArray();
    }

    public void flush() throws IOException {
        jsonGenerator.close();
    }
}
