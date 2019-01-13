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
