package org.hl7.fhir.convertors;

/*-
 * #%L
 * org.hl7.fhir.convertors
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

import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.r4.elementmodel.Manager.FhirFormat;
import org.hl7.fhir.r4.formats.IParser.OutputStyle;
import org.hl7.fhir.r4.model.FhirPublication;

public class VersionConversionService {
  /** 
   * use the package manager to load relevant conversion packages, and then initialise internally as required
   * 
   * not thread safe
   * 
   * @param system - true if the software is running in system context, not in a user context
   * @param txServer - Address of the terminology server to use (null = use http://tx.fhir.org
   */
  public VersionConversionService(boolean system, String txServer) throws FHIRException {
    
  }

  /**
   * convert from one version to another. 
   * 
   * This routine is thread safe
   * 
   * @param src - the resource to convert
   * @param srcVersion - the version of the resource to convert
   * @param dstVersion - the target version to convert to
   * @return the converted resource
   * @throws FHIRException - if the source resource cannot be parsed, no single path exists from source to dest version, or the conversion process fails
   * @throws IOException 
   */
  public byte[] convert(byte[] src, FhirFormat srcFormat, FhirPublication srcVersion, FhirFormat dstFormat, FhirPublication dstVersion, boolean useJava, OutputStyle style) throws FHIRException, IOException {
    if (src == null)
      throw new FHIRException("No source specified");
    if (srcVersion == null)
      throw new FHIRException("No source version specified");
    if (dstVersion == null)
      throw new FHIRException("No destination version specified");
    switch (srcVersion) {
    case DSTU1: throw new FHIRException("FHIR Version #1 is not supported by the inter-version convertor");
    case DSTU2: return convert10(parseResource10(src, srcFormat), dstFormat, dstVersion, useJava, style);
    case DSTU2016May: return convert14(parseResource14(src, srcFormat), dstFormat, dstVersion, useJava, style);
    case R4: return convert40(parseResource40(src, srcFormat), dstFormat, dstVersion, useJava, style);
    case STU3: return convert30(parseResource30(src, srcFormat), dstFormat, dstVersion, useJava, style);
    default: throw new FHIRException("FHIR Version 'unknown' is not supported by the inter-version convertor");    
    }
  }

  private org.hl7.fhir.dstu2.model.Resource parseResource10(byte[] src, FhirFormat srcFormat) throws FHIRException, IOException {
    switch (srcFormat) {
    case JSON: return new org.hl7.fhir.dstu2.formats.JsonParser().parse(src);
    case TEXT: throw new FHIRException("Text format not supported for DSTU2");
    case TURTLE: throw new FHIRException("Turtle format not supported for DSTU2");
    case VBAR: throw new FHIRException("Vertical Bar format not supported for DSTU2");
    case XML: return new org.hl7.fhir.dstu2.formats.XmlParser().parse(src);
    default: throw new FHIRException("Unknown format not supported for DSTU2");
    }
  }

  private org.hl7.fhir.dstu2016may.model.Resource parseResource14(byte[] src, FhirFormat srcFormat) throws FHIRException, IOException {
    switch (srcFormat) {
    case JSON: return new org.hl7.fhir.dstu2016may.formats.JsonParser().parse(src);
    case TEXT: throw new FHIRException("Text format not supported for DSTU2");
    case TURTLE: throw new FHIRException("Turtle format not supported for DSTU2");
    case VBAR: throw new FHIRException("Vertical Bar format not supported for DSTU2");
    case XML: return new org.hl7.fhir.dstu2016may.formats.XmlParser().parse(src);
    default: throw new FHIRException("Unknown format not supported for DSTU2");
    }
  }

  private org.hl7.fhir.dstu3.model.Resource parseResource30(byte[] src, FhirFormat srcFormat) throws FHIRException, IOException {
    switch (srcFormat) {
    case JSON: return new org.hl7.fhir.dstu3.formats.JsonParser().parse(src);
    case TEXT: throw new FHIRException("Text format not supported for DSTU2");
    case TURTLE: return new org.hl7.fhir.dstu3.formats.RdfParser().parse(src);
    case VBAR: throw new FHIRException("Vertical Bar format not supported for DSTU2");
    case XML: return new org.hl7.fhir.dstu3.formats.XmlParser().parse(src);
    default: throw new FHIRException("Unknown format not supported for DSTU2");
    }
  }

  private org.hl7.fhir.r4.model.Resource parseResource40(byte[] src, FhirFormat srcFormat) throws FHIRException, IOException {
    switch (srcFormat) {
    case JSON: return new org.hl7.fhir.r4.formats.JsonParser().parse(src);
    case TEXT: throw new FHIRException("Text format not supported for DSTU2");
    case TURTLE: return new org.hl7.fhir.r4.formats.RdfParser().parse(src);
    case VBAR: throw new FHIRException("Vertical Bar format not supported for DSTU2");
    case XML: return new org.hl7.fhir.r4.formats.XmlParser().parse(src);
    default: throw new FHIRException("Unknown format not supported for DSTU2");
    }
  }

  private org.hl7.fhir.dstu2.formats.IParser.OutputStyle style10(OutputStyle style) {
    return style == OutputStyle.CANONICAL ? org.hl7.fhir.dstu2.formats.IParser.OutputStyle.CANONICAL : style == OutputStyle.NORMAL ? org.hl7.fhir.dstu2.formats.IParser.OutputStyle.NORMAL : org.hl7.fhir.dstu2.formats.IParser.OutputStyle.PRETTY;
  }
  
  private org.hl7.fhir.dstu2016may.formats.IParser.OutputStyle style14(OutputStyle style) {
    return style == OutputStyle.CANONICAL ? org.hl7.fhir.dstu2016may.formats.IParser.OutputStyle.CANONICAL : style == OutputStyle.NORMAL ? org.hl7.fhir.dstu2016may.formats.IParser.OutputStyle.NORMAL : org.hl7.fhir.dstu2016may.formats.IParser.OutputStyle.PRETTY;
  }
  
  private org.hl7.fhir.dstu3.formats.IParser.OutputStyle style30(OutputStyle style) {
    return style == OutputStyle.CANONICAL ? org.hl7.fhir.dstu3.formats.IParser.OutputStyle.CANONICAL : style == OutputStyle.NORMAL ? org.hl7.fhir.dstu3.formats.IParser.OutputStyle.NORMAL : org.hl7.fhir.dstu3.formats.IParser.OutputStyle.PRETTY;
  }
  
  private byte[] saveResource10(org.hl7.fhir.dstu2.model.Resource src, FhirFormat dstFormat, OutputStyle style) throws FHIRException, IOException {
    switch (dstFormat) {
    case JSON: return new org.hl7.fhir.dstu2.formats.JsonParser().setOutputStyle(style10(style)).composeBytes(src);
    case TEXT: throw new FHIRException("Text format not supported for DSTU2");
    case TURTLE: throw new FHIRException("Turtle format not supported for DSTU2");
    case VBAR: throw new FHIRException("Vertical Bar format not supported for DSTU2");
    case XML: return new org.hl7.fhir.dstu2.formats.XmlParser().setOutputStyle(style10(style)).composeBytes(src);
    default: throw new FHIRException("Unknown format not supported for DSTU2");
    }
  }

  private byte[] saveResource14(org.hl7.fhir.dstu2016may.model.Resource src, FhirFormat dstFormat, OutputStyle style) throws FHIRException, IOException {
    switch (dstFormat) {
    case JSON: return new org.hl7.fhir.dstu2016may.formats.JsonParser().setOutputStyle(style14(style)).composeBytes(src);
    case TEXT: throw new FHIRException("Text format not supported for DSTU2");
    case TURTLE: throw new FHIRException("Turtle format not supported for DSTU2");
    case VBAR: throw new FHIRException("Vertical Bar format not supported for DSTU2");
    case XML: return new org.hl7.fhir.dstu2016may.formats.XmlParser().setOutputStyle(style14(style)).composeBytes(src);
    default: throw new FHIRException("Unknown format not supported for DSTU2");
    }
  }

  private byte[] saveResource30(org.hl7.fhir.dstu3.model.Resource src, FhirFormat dstFormat, OutputStyle style) throws FHIRException, IOException {
    switch (dstFormat) {
    case JSON: return new org.hl7.fhir.dstu3.formats.JsonParser().setOutputStyle(style30(style)).composeBytes(src);
    case TEXT: throw new FHIRException("Text format not supported for DSTU2");
    case TURTLE: return new org.hl7.fhir.dstu3.formats.RdfParser().setOutputStyle(style30(style)).composeBytes(src);
    case VBAR: throw new FHIRException("Vertical Bar format not supported for DSTU2");
    case XML: return new org.hl7.fhir.dstu3.formats.XmlParser().setOutputStyle(style30(style)).composeBytes(src);
    default: throw new FHIRException("Unknown format not supported for DSTU2");
    }
  }


  private byte[] saveResource40(org.hl7.fhir.r4.model.Resource src, FhirFormat dstFormat, OutputStyle style) throws FHIRException, IOException {
    switch (dstFormat) {
    case JSON: return new org.hl7.fhir.r4.formats.JsonParser().setOutputStyle(style).composeBytes(src);
    case TEXT: throw new FHIRException("Text format not supported for DSTU2");
    case TURTLE: return new org.hl7.fhir.r4.formats.RdfParser().setOutputStyle(style).composeBytes(src);
    case VBAR: throw new FHIRException("Vertical Bar format not supported for DSTU2");
    case XML: return new org.hl7.fhir.r4.formats.XmlParser().setOutputStyle(style).composeBytes(src);
    default: throw new FHIRException("Unknown format not supported for DSTU2");
    }
  }

  
  private byte[] convert10(org.hl7.fhir.dstu2.model.Resource src, FhirFormat dstFormat, FhirPublication dstVersion, boolean useJava, OutputStyle style) throws FHIRException, IOException {
    switch (dstVersion) {
    case DSTU1: throw new FHIRException("FHIR Version #1 is not supported by the inter-version convertor");
    case DSTU2: return saveResource10(src, dstFormat, style);
    case DSTU2016May: throw new FHIRException("Conversion from DSTU2 to 2016May version is not supported");
    case R4:
      if (useJava && VersionConvertor_10_40.convertsResource(src.fhirType()))
        return saveResource40(new VersionConvertor_10_40(null).convertResource(src), dstFormat, style); // todo: handle code system?
      else
        throw new FHIRException("Conversion from R4 to 2016May version is not supported for resources of type "+src.fhirType());
    case STU3:
      if (useJava && VersionConvertor_10_30.convertsResource(src.fhirType()))
        return saveResource30(new VersionConvertor_10_30(null).convertResource(src), dstFormat, style); // todo: handle code system?
      else
        throw new FHIRException("todo: use script based conversion....");
    default: throw new FHIRException("FHIR Version 'unknown' is not supported by the inter-version convertor");
    }
  }

  private byte[] convert14(org.hl7.fhir.dstu2016may.model.Resource src, FhirFormat dstFormat, FhirPublication dstVersion, boolean useJava, OutputStyle style) throws FHIRException, IOException {
    switch (dstVersion) {
    case DSTU1: throw new FHIRException("FHIR Version #1 is not supported by the inter-version convertor");
    case DSTU2: throw new FHIRException("Conversion from 2016May version to DSTU2 is not supported");
    case DSTU2016May: return saveResource14(src, dstFormat, style);
    case R4:
      if (useJava && VersionConvertor_14_40.convertsResource(src.fhirType()))
        return saveResource40(VersionConvertor_14_40.convertResource(src), dstFormat, style); 
      else
        throw new FHIRException("Conversion from 2016May version to R4 is not supported for resources of type "+src.fhirType());
    case STU3:
      if (useJava && VersionConvertor_14_30.convertsResource(src.fhirType()))
        return saveResource30(VersionConvertor_14_30.convertResource(src), dstFormat, style); 
      else
        throw new FHIRException("Conversion from 2016May version to STU3 is not supported for resources of type "+src.fhirType());
    default: throw new FHIRException("FHIR Version 'unknown' is not supported by the inter-version convertor");
    }
  }

  private byte[] convert30(org.hl7.fhir.dstu3.model.Resource src, FhirFormat dstFormat, FhirPublication dstVersion, boolean useJava, OutputStyle style) throws FHIRException, IOException {
    switch (dstVersion) {
    case DSTU1: throw new FHIRException("FHIR Version #1 is not supported by the inter-version convertor");
    case DSTU2:
      if (useJava && VersionConvertor_10_30.convertsResource(src.fhirType()))
        return saveResource10(new VersionConvertor_10_30(null).convertResource(src), dstFormat, style); // todo: handle code system?
      else
        throw new FHIRException("todo: use script based conversion....");
    case DSTU2016May: 
      if (useJava && VersionConvertor_14_30.convertsResource(src.fhirType()))
        return saveResource14(VersionConvertor_14_30.convertResource(src), dstFormat, style);
      else
        throw new FHIRException("Conversion from R3 to 2016May version is not supported for resources of type "+src.fhirType());
    case R4:
      if (useJava && VersionConvertor_30_40.convertsResource(src.fhirType()))
        return saveResource40(VersionConvertor_30_40.convertResource(src, false), dstFormat, style); 
      else
        throw new FHIRException("todo: use script based conversion....");
    case STU3: return saveResource30(src, dstFormat, style);
    default: throw new FHIRException("FHIR Version 'unknown' is not supported by the inter-version convertor");
    }
  }

  private byte[] convert40(org.hl7.fhir.r4.model.Resource src, FhirFormat dstFormat, FhirPublication dstVersion, boolean useJava, OutputStyle style) throws FHIRException, IOException {
    switch (dstVersion) {
    case DSTU1: throw new FHIRException("FHIR Version #1 is not supported by the inter-version convertor");
    case DSTU2: 
      if (useJava && VersionConvertor_10_40.convertsResource(src.fhirType()))
        return saveResource10(new VersionConvertor_10_40(null).convertResource(src), dstFormat, style); // todo: handle code system?
      else
        throw new FHIRException("Conversion from R4 to DSTU2 version is not supported for resources of type "+src.fhirType());
    case DSTU2016May: 
    if (useJava && VersionConvertor_14_40.convertsResource(src.fhirType()))
      return saveResource14(VersionConvertor_14_40.convertResource(src), dstFormat, style); 
    else
      throw new FHIRException("Conversion from DSTU2 to 2016May version is not supported for resources of type "+src.fhirType());
    case R4: return saveResource40(src, dstFormat, style);
    case STU3:
      if (useJava && VersionConvertor_30_40.convertsResource(src.fhirType()))
        return saveResource30(VersionConvertor_30_40.convertResource(src, false), dstFormat, style);
      else
        throw new FHIRException("todo: use script based conversion....");
    default: throw new FHIRException("FHIR Version 'unknown' is not supported by the inter-version convertor");
    }
  }
  
}
