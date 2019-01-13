package org.hl7.fhir.dstu3.formats;

/*-
 * #%L
 * org.hl7.fhir.dstu3
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
import java.math.BigDecimal;

/**
 * Facade to GSON writer, or something that imposes property ordering first
 * 
 * @author Grahame
 *
 */
public interface JsonCreator {

  void setIndent(String string);

  void beginObject() throws IOException;

  void endObject() throws IOException;

  void nullValue() throws IOException;

  void name(String name) throws IOException;

  void value(String value) throws IOException;

  void value(Boolean value) throws IOException;

  void value(BigDecimal value) throws IOException;

  void value(Integer value) throws IOException;

  void beginArray() throws IOException;

  void endArray() throws IOException;

  void finish() throws IOException;

  // only used by an creator that actually produces xhtml
  void link(String href);
}
