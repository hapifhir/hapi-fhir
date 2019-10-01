package org.hl7.fhir.dstu2016may.metamodel;

import java.io.InputStream;
import java.io.OutputStream;

import org.hl7.fhir.dstu2016may.formats.IParser.OutputStyle;
import org.hl7.fhir.dstu2016may.utils.IWorkerContext;

public class Manager {

  public enum FhirFormat { XML, JSON, JSONLD, TURTLE }
  
  public static Element parse(IWorkerContext context, InputStream source, FhirFormat inputFormat) throws Exception {
    return makeParser(context, inputFormat).parse(source);
  }

  public static void compose(IWorkerContext context, Element e, OutputStream destination, FhirFormat outputFormat, OutputStyle style, String base) throws Exception {
    makeParser(context, outputFormat).compose(e, destination, style, base);
  }

  public static ParserBase makeParser(IWorkerContext context, FhirFormat format) {
    switch (format) {
    case JSON : return new JsonParser(context);
    case XML : return new XmlParser(context);
    default:
      throw new IllegalArgumentException("Unknown type: " + format);
    }
  }
  
}
