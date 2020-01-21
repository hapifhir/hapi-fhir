package ca.uhn.fhir.parser.json.jackson;

import ca.uhn.fhir.parser.json.JsonLikeWriter;

import java.io.IOException;
import java.io.Writer;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.LinkedList;
import java.util.List;

public class JacksonWriter extends JsonLikeWriter {

    private JacksonSerializer jacksonSerializer;
    private enum BlockType {
        NONE, OBJECT, ARRAY
    }

    private BlockType blockType = BlockType.NONE;
    private List<BlockType> blocks = new LinkedList<>();

    public JacksonWriter(Writer writer) {
        jacksonSerializer = new JacksonSerializer(writer);
        setWriter(writer);
    }

    public JacksonWriter() {}

    @Override
    public JsonLikeWriter init() {
        blockType = BlockType.NONE;
        blocks.clear();
        return this;
    }

    @Override
    public JsonLikeWriter flush() throws IOException {
        getWriter().flush();
        return this;
    }

    @Override
    public void close() throws IOException {
        getWriter().close();
    }

    @Override
    public JsonLikeWriter beginObject() throws IOException {
        blocks.add(blockType);
        blockType = BlockType.OBJECT;
        jacksonSerializer.writeStartObject();
        return this;
    }

    @Override
    public JsonLikeWriter beginArray() throws IOException {
        blocks.add(blockType);
        blockType = BlockType.ARRAY;
        jacksonSerializer.writeStartArray();
        return this;
    }

    @Override
    public JsonLikeWriter beginObject(String name) throws IOException {
        blocks.add(blockType);
        blockType = BlockType.OBJECT;
        jacksonSerializer.writeStartObject(name);
        return this;
    }

    @Override
    public JsonLikeWriter beginArray(String name) throws IOException {
        blocks.add(blockType);
        blockType = BlockType.ARRAY;
        jacksonSerializer.writeStartArray(name);
        return this;
    }

    @Override
    public JsonLikeWriter write(String value) throws IOException {
        jacksonSerializer.write(value);
        return this;
    }

    @Override
    public JsonLikeWriter write(BigInteger value) throws IOException {
        jacksonSerializer.write(value);
        return this;
    }

    @Override
    public JsonLikeWriter write(BigDecimal value) throws IOException {
        jacksonSerializer.write(value);
        return this;
    }

    @Override
    public JsonLikeWriter write(long value) throws IOException {
        jacksonSerializer.write(value);
        return this;
    }

    @Override
    public JsonLikeWriter write(double value) throws IOException {
        jacksonSerializer.write(value);
        return this;
    }

    @Override
    public JsonLikeWriter write(Boolean value) throws IOException {
        jacksonSerializer.write(value);
        return this;
    }

    @Override
    public JsonLikeWriter write(boolean value) throws IOException {
        jacksonSerializer.write(value);
        return this;
    }

    @Override
    public JsonLikeWriter writeNull() throws IOException {
        jacksonSerializer.writeNull();
        return this;
    }

    @Override
    public JsonLikeWriter write(String name, String value) throws IOException {
        jacksonSerializer.write(name, value);
        return this;
    }

    @Override
    public JsonLikeWriter write(String name, BigInteger value) throws IOException {
        jacksonSerializer.write(name, value);
        return this;
    }

    @Override
    public JsonLikeWriter write(String name, BigDecimal value) throws IOException {
        jacksonSerializer.write(name, value);
        return this;
    }

    @Override
    public JsonLikeWriter write(String name, long value) throws IOException {
        jacksonSerializer.write(name, value);
        return this;
    }

    @Override
    public JsonLikeWriter write(String name, double value) throws IOException {
        jacksonSerializer.write(name, value);
        return this;
    }

    @Override
    public JsonLikeWriter write(String name, Boolean value) throws IOException {
        jacksonSerializer.write(name, value);
        return this;
    }

    @Override
    public JsonLikeWriter write(String name, boolean value) throws IOException {
        jacksonSerializer.write(name, value);
        return this;
    }

    @Override
    public JsonLikeWriter writeNull(String name) throws IOException {
        jacksonSerializer.writeNull(name);
        return this;
    }

    @Override
    public JsonLikeWriter endObject() throws IOException {
        if (blockType != BlockType.OBJECT) {
            jacksonSerializer.endArray();
        } else {
            jacksonSerializer.endObject();
        }
        blockType = blocks.remove(blocks.size() - 1);

        if (blockType == BlockType.NONE) {
            jacksonSerializer.flush();
        }

        return this;
    }

    @Override
    public JsonLikeWriter endArray() throws IOException {
        if (blockType == BlockType.OBJECT) {
            jacksonSerializer.endObject();
        } else {
            jacksonSerializer.endArray();
        }
        blockType = blocks.remove(blocks.size() - 1);

        if (blockType == BlockType.NONE) {
            jacksonSerializer.flush();
        }

        return this;
    }

    @Override
    public JsonLikeWriter endBlock() throws IOException {
        if (blockType == BlockType.ARRAY) {
            jacksonSerializer.endArray();
        } else {
            jacksonSerializer.endObject();
        }
        blockType = blocks.remove(blocks.size() - 1);

        if (blockType == BlockType.NONE) {
            jacksonSerializer.flush();
        }

        return this;
    }
}
