package org.hl7.fhir.dstu2016may.formats;

/*-
 * #%L
 * org.hl7.fhir.dstu2016may
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
