package org.hl7.fhir.dstu3.formats;


import java.io.IOException;

/*
  Copyright (c) 2011+, HL7, Inc.
  All rights reserved.
  
  Redistribution and use in source and binary forms, with or without modification, 
  are permitted provided that the following conditions are met:
  
   * Redistributions of source code must retain the above copyright notice, this 
     list of conditions and the following disclaimer.
   * Redistributions in binary form must reproduce the above copyright notice, 
     this list of conditions and the following disclaimer in the documentation 
     and/or other materials provided with the distribution.
   * Neither the name of HL7 nor the names of its contributors may be used to 
     endorse or promote products derived from this software without specific 
     prior written permission.
  
  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND 
  ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED 
  WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. 
  IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, 
  INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT 
  NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR 
  PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, 
  WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
  ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE 
  POSSIBILITY OF SUCH DAMAGE.
  
*/


import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import org.hl7.fhir.exceptions.FHIRFormatError;
import org.hl7.fhir.dstu3.model.Resource;
import org.hl7.fhir.dstu3.model.Type;
import org.xmlpull.v1.XmlPullParserException;


/**
 * General interface - either an XML or JSON parser: read or write instances
 *  
 * Defined to allow a factory to create a parser of the right type
 */
public interface IParser {

	/** 
	 * check what kind of parser this is
	 *  
	 * @return what kind of parser this is
	 */
	public ParserType getType();
	
  // -- Parser Configuration ----------------------------------
  /**
   * Whether to parse or ignore comments - either reading or writing
   */
  public boolean getHandleComments(); 
  public IParser setHandleComments(boolean value);

  /**
   * @param allowUnknownContent Whether to throw an exception if unknown content is found (or just skip it) when parsing
   */
  public boolean isAllowUnknownContent();
  public IParser setAllowUnknownContent(boolean value);
  
  
  public enum OutputStyle {
    /**
     * Produce normal output - no whitespace, except in HTML where whitespace is untouched
     */
    NORMAL,
    
    /**
     * Produce pretty output - human readable whitespace, HTML whitespace untouched
     */
    PRETTY,
    
    /**
     * Produce canonical output - no comments, no whitspace, HTML whitespace normlised, JSON attributes sorted alphabetically (slightly slower) 
     */
    CANONICAL,
  }

  /**
   * Writing: 
   */
  public OutputStyle getOutputStyle();
  public IParser setOutputStyle(OutputStyle value);
  
  /**
   * This method is used by the publication tooling to stop the xhrtml narrative being generated. 
   * It is not valid to use in production use. The tooling uses it to generate json/xml representations in html that are not cluttered by escaped html representations of the html representation
   */
  public IParser setSuppressXhtml(String message);

  // -- Reading methods ----------------------------------------
  
  /**
   * parse content that is known to be a resource  
 * @throws XmlPullParserException 
 * @throws FHIRFormatError 
 * @throws IOException 
   */
  public Resource parse(InputStream input) throws IOException, FHIRFormatError;

  /**
   * parse content that is known to be a resource  
 * @throws UnsupportedEncodingException 
 * @throws IOException 
 * @throws FHIRFormatError 
   */
  public Resource parse(String input) throws UnsupportedEncodingException, FHIRFormatError, IOException;
  
  /**
   * parse content that is known to be a resource  
 * @throws IOException 
 * @throws FHIRFormatError 
   */
  public Resource parse(byte[] bytes) throws FHIRFormatError, IOException;

  /**
   * This is used to parse a type - a fragment of a resource. 
   * There's no reason to use this in production - it's used 
   * in the build tools 
   * 
   * Not supported by all implementations
   * 
   * @param input
   * @param knownType. if this is blank, the parser may try to infer the type (xml only)
   * @return
 * @throws XmlPullParserException 
 * @throws FHIRFormatError 
 * @throws IOException 
   */
  public Type parseType(InputStream input, String knownType) throws IOException, FHIRFormatError;
  /**
   * This is used to parse a type - a fragment of a resource. 
   * There's no reason to use this in production - it's used 
   * in the build tools 
   * 
   * Not supported by all implementations
   * 
   * @param input
   * @param knownType. if this is blank, the parser may try to infer the type (xml only)
   * @return
 * @throws UnsupportedEncodingException 
 * @throws IOException 
 * @throws FHIRFormatError 
   */
  public Type parseType(String input, String knownType) throws UnsupportedEncodingException, FHIRFormatError, IOException;
  /**
   * This is used to parse a type - a fragment of a resource. 
   * There's no reason to use this in production - it's used 
   * in the build tools 
   * 
   * Not supported by all implementations
   * 
   * @param input
   * @param knownType. if this is blank, the parser may try to infer the type (xml only)
   * @return
 * @throws IOException 
 * @throws FHIRFormatError 
	 */
  public Type parseType(byte[] bytes, String knownType) throws FHIRFormatError, IOException;
  
  // -- Writing methods ----------------------------------------

	/**
	 * Compose a resource to a stream, possibly using pretty presentation for a human reader (used in the spec, for example, but not normally in production)
	 * @throws IOException 
	 */
	public void compose(OutputStream stream, Resource resource) throws IOException;
	
  /**
   * Compose a resource to a stream, possibly using pretty presentation for a human reader (used in the spec, for example, but not normally in production)
 * @throws IOException 
   */
	public String composeString(Resource resource) throws IOException;

	/**
   * Compose a resource to a stream, possibly using pretty presentation for a human reader (used in the spec, for example, but not normally in production)
	 * @throws IOException 
   */
	public byte[] composeBytes(Resource resource) throws IOException;


	/**
	 * Compose a type to a stream, possibly using pretty presentation for a human reader (used in the spec, for example, but not normally in production)
	 * 
	 * Not supported by all implementations. rootName is ignored in the JSON format
	 * @throws XmlPullParserException 
	 * @throws FHIRFormatError 
	 * @throws IOException 
	 */
	public void compose(OutputStream stream, Type type, String rootName) throws IOException;

	/**
   * Compose a type to a stream, possibly using pretty presentation for a human reader (used in the spec, for example, but not normally in production)
   * 
   * Not supported by all implementations. rootName is ignored in the JSON format
	 * @throws IOException 
   */
  public String composeString(Type type, String rootName) throws IOException;

	/**
	 * Compose a type to a stream, possibly using pretty presentation for a human reader (used in the spec, for example, but not normally in production)
   * 
   * Not supported by all implementations. rootName is ignored in the JSON format
	 * @throws IOException 
	 */
	public byte[] composeBytes(Type type, String rootName) throws IOException;


}
