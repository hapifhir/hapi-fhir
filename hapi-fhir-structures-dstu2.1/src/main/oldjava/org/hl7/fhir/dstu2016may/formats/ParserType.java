package org.hl7.fhir.dstu2016may.formats;

/**
 * Used in factory methods for parsers, for requesting a parser of a particular type 
 * (see IWorkerContext)
 * 
 * @author Grahame
 *
 */
public enum ParserType {
  /**
   * XML as specified in specification 
   */
  XML, 

  /**
   * JSON as specified in the specification
   */
  JSON,

  /** 
   * XHTML - write narrative (generate if necessary). No read
   */
  XHTML,

   /**
   * RDF is not supported yet
   */
  RDF_TURTLE
}