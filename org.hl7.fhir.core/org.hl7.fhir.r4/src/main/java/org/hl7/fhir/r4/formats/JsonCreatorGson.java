package org.hl7.fhir.r4.formats;

/*-
 * #%L
 * org.hl7.fhir.r4
 * %%
 * Copyright (C) 2014 - 2019 Health Level 7
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

  @Override
  public void valueNum(String value) throws IOException {
    value(new BigDecimal(value));    
  }

}
