/**
 * This software has been produced by Akana, Inc. under a professional services
 * agreement with our customer. This work may contain material that is confidential 
 * and proprietary information of Akana, Inc. and is subject to copyright 
 * protection under laws of the United States of America and other countries. 
 * Akana, Inc. grants the customer non-exclusive rights to this material without
 * any warranty expressed or implied. 
 */
package ca.uhn.fhir.parser.json;

import java.io.Reader;

import ca.uhn.fhir.parser.DataFormatException;

/**
 * @author Akana, Inc. Professional Services
 *
 */
public interface JsonLikeStructure {
	public void load (Reader theReader) throws DataFormatException;
	public JsonLikeObject getRootObject ();
}
