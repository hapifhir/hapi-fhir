package org.hl7.fhir.dstu2016may.formats;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

import com.google.gson.stream.JsonWriter;

public class JsonCreatorCanonical implements JsonCreator {

  public class JsonCanValue {
    String name;
    private JsonCanValue(String name) {
      this.name = name;  
   }
  }

  private class JsonCanNumberValue extends JsonCanValue {
    private BigDecimal value;
    private JsonCanNumberValue(String name, BigDecimal value) {
      super(name);
      this.value = value;  
    }
  }

  private class JsonCanIntegerValue extends JsonCanValue {
    private Integer value;
    private JsonCanIntegerValue(String name, Integer value) {
      super(name);
      this.value = value;  
    }
  }

  private class JsonCanBooleanValue extends JsonCanValue  {
    private Boolean value;
    private JsonCanBooleanValue(String name, Boolean value) {
      super(name);
      this.value = value;  
    }
  }

  private class JsonCanStringValue extends JsonCanValue {
    private String value;
    private JsonCanStringValue(String name, String value) {
      super(name);
      this.value = value;  
    }
  }

  private class JsonCanNullValue extends JsonCanValue  {
    private JsonCanNullValue(String name) {
      super(name);
    }
  }

  public class JsonCanObject extends JsonCanValue {

    boolean array;
    List<JsonCanValue> children = new ArrayList<JsonCanValue>();
    
    public JsonCanObject(String name, boolean array) {
      super(name);
      this.array = array;
    }

    public void addProp(JsonCanValue obj) {
      children.add(obj);
    }
  }

  Stack<JsonCanObject> stack;
  JsonCanObject root; 
  JsonWriter gson;
  String name;
  
  public JsonCreatorCanonical(OutputStreamWriter osw) {
    stack = new Stack<JsonCreatorCanonical.JsonCanObject>();
    gson = new JsonWriter(osw);
    name = null;
  }

  private String takeName() {
    String res = name;
    name = null;
    return res;
  }
  
  @Override
  public void setIndent(String indent) {
    if (!indent.equals(""))
      throw new Error("do not use pretty when canonical is set");
    gson.setIndent(indent);
  }

  @Override
  public void beginObject() throws IOException {
    JsonCanObject obj = new JsonCanObject(takeName(), false);
    if (stack.isEmpty())
      root = obj;
    else
      stack.peek().addProp(obj);
    stack.push(obj);
  }

  @Override
  public void endObject() throws IOException {
    stack.pop();
  }

  @Override
  public void nullValue() throws IOException {
    stack.peek().addProp(new JsonCanNullValue(takeName()));
  }

  @Override
  public void name(String name) throws IOException {
    this.name = name;
  }

  @Override
  public void value(String value) throws IOException {
    stack.peek().addProp(new JsonCanStringValue(takeName(), value));    
  }

  @Override
  public void value(Boolean value) throws IOException {
    stack.peek().addProp(new JsonCanBooleanValue(takeName(), value));    
  }

  @Override
  public void value(BigDecimal value) throws IOException {
    stack.peek().addProp(new JsonCanNumberValue(takeName(), value));    
  }

  @Override
  public void value(Integer value) throws IOException {
    stack.peek().addProp(new JsonCanIntegerValue(takeName(), value));    
  }

  @Override
  public void beginArray() throws IOException {
    JsonCanObject obj = new JsonCanObject(takeName(), true);
    if (!stack.isEmpty())
      stack.peek().addProp(obj);
    stack.push(obj);
    
  }

  @Override
  public void endArray() throws IOException {
    stack.pop();    
  }

  @Override
  public void finish() throws IOException {
    writeObject(root);
  }

  private void writeObject(JsonCanObject obj) throws IOException {
    gson.beginObject();
    List<String> names = new ArrayList<String>();
    for (JsonCanValue v : obj.children) 
      names.add(v.name);
    Collections.sort(names);
    for (String n : names) {
      gson.name(n);
      JsonCanValue v = getPropForName(n, obj.children);
      if (v instanceof JsonCanNumberValue)
        gson.value(((JsonCanNumberValue) v).value);
      else if (v instanceof JsonCanIntegerValue)
          gson.value(((JsonCanIntegerValue) v).value);
      else if (v instanceof JsonCanBooleanValue)
        gson.value(((JsonCanBooleanValue) v).value);
      else if (v instanceof JsonCanStringValue)
        gson.value(((JsonCanStringValue) v).value);
      else if (v instanceof JsonCanNullValue)
        gson.nullValue();
      else if (v instanceof JsonCanObject) {
        JsonCanObject o = (JsonCanObject) v;
        if (o.array) 
          writeArray(o);
        else
          writeObject(o);
      } else
        throw new Error("not possible");
    }
    gson.endObject();
  }

  private JsonCanValue getPropForName(String name, List<JsonCanValue> children) {
    for (JsonCanValue child : children)
      if (child.name.equals(name))
        return child;
    return null;
  }

  private void writeArray(JsonCanObject arr) throws IOException {
    gson.beginArray();
    for (JsonCanValue v : arr.children) { 
      if (v instanceof JsonCanNumberValue)
        gson.value(((JsonCanNumberValue) v).value);
      else if (v instanceof JsonCanIntegerValue)
          gson.value(((JsonCanIntegerValue) v).value);
      else if (v instanceof JsonCanBooleanValue)
        gson.value(((JsonCanBooleanValue) v).value);
      else if (v instanceof JsonCanStringValue)
        gson.value(((JsonCanStringValue) v).value);
      else if (v instanceof JsonCanNullValue)
        gson.nullValue();
      else if (v instanceof JsonCanObject) {
        JsonCanObject o = (JsonCanObject) v;
        if (o.array) 
          writeArray(o);
        else
          writeObject(o);
      } else
        throw new Error("not possible");
    }
    gson.endArray();    
  }
       
    
}
