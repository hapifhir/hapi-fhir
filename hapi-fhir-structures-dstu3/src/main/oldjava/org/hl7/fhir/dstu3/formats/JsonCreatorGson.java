package org.hl7.fhir.dstu3.formats;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;

import com.google.gson.stream.JsonWriter;

public class JsonCreatorGson implements JsonCreator {

  JsonWriter gson;
  
  public JsonCreatorGson(OutputStreamWriter osw) {
    gson = new JsonWriter(osw);
  }

  @Override
  public void setIndent(String indent) {
    gson.setIndent(indent);
  }

  @Override
  public void beginObject() throws IOException {
    gson.beginObject();    
  }

  @Override
  public void endObject() throws IOException {
    gson.endObject();
  }

  @Override
  public void nullValue() throws IOException {
    gson.nullValue();
  }

  @Override
  public void name(String name) throws IOException {
    gson.name(name);
  }

  @Override
  public void value(String value) throws IOException {
    gson.value(value);
  }

  @Override
  public void value(Boolean value) throws IOException {
    gson.value(value);
  }

  @Override
  public void value(BigDecimal value) throws IOException {
    gson.value(value);
  }

  @Override
  public void value(Integer value) throws IOException {
    gson.value(value);
  }

  @Override
  public void beginArray() throws IOException {
    gson.beginArray();
  }

  @Override
  public void endArray() throws IOException {
    gson.endArray();
  }

  @Override
  public void finish() {
    // nothing to do here
    
  }

  @Override
  public void link(String href) {
    // not used
  }

}
