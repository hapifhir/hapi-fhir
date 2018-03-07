package org.hl7.fhir.r4.formats;

import org.hl7.fhir.r4.elementmodel.Manager.FhirFormat;
import org.hl7.fhir.r4.formats.IParser.OutputStyle;

public class ParserFactory {

  public static IParser parser(FhirFormat format) {
    switch (format) {
    case JSON : return new JsonParser();
    case XML : return new XmlParser();
    case TURTLE : return new RdfParser();
    default:
      throw new Error("Not supported at this time");
    }
  }

  public static IParser parser(FhirFormat format, OutputStyle style) {
    switch (format) {
    case JSON : return new JsonParser().setOutputStyle(style);
    case XML : return new XmlParser().setOutputStyle(style);
    case TURTLE : return new RdfParser().setOutputStyle(style);
    default:
      throw new Error("Not supported at this time");
    }
  }
  
}
