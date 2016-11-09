package org.hl7.fhir.dstu3.formats;

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
