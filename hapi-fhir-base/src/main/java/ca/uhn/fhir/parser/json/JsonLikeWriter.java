/**
 * This software has been produced by Akana, Inc. under a professional services
 * agreement with our customer. This work may contain material that is confidential 
 * and proprietary information of Akana, Inc. and is subject to copyright 
 * protection under laws of the United States of America and other countries. 
 * Akana, Inc. grants the customer non-exclusive rights to this material without
 * any warranty expressed or implied. 
 */
package ca.uhn.fhir.parser.json;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author Akana, Inc. Professional Services
 *
 */
public abstract class JsonLikeWriter {

	private boolean prettyPrint;
	
	public void setPrettyPrint (boolean tf) {
		prettyPrint = tf; 
	}
	public boolean isPrettyPrint () {
		return prettyPrint;
	}
	
	public abstract JsonLikeWriter init () throws IOException;
	public abstract JsonLikeWriter flush () throws IOException;
	public abstract void close () throws IOException;
	
	public abstract JsonLikeWriter beginObject () throws IOException;
	public abstract JsonLikeWriter beginArray () throws IOException;

	public abstract JsonLikeWriter beginObject (String name) throws IOException;
	public abstract JsonLikeWriter beginArray (String name) throws IOException;
	
	public abstract JsonLikeWriter write (String value) throws IOException;
	public abstract JsonLikeWriter write (BigInteger value) throws IOException;
	public abstract JsonLikeWriter write (BigDecimal value) throws IOException;
	public abstract JsonLikeWriter write (long value) throws IOException;
	public abstract JsonLikeWriter write (double value) throws IOException;
	public abstract JsonLikeWriter write (Boolean value) throws IOException;
	public abstract JsonLikeWriter write (boolean value) throws IOException;
	public abstract JsonLikeWriter writeNull () throws IOException;
	
	public abstract JsonLikeWriter write (String name, String value) throws IOException;
	public abstract JsonLikeWriter write (String name, BigInteger value) throws IOException;
	public abstract JsonLikeWriter write (String name, BigDecimal value) throws IOException;
	public abstract JsonLikeWriter write (String name, long value) throws IOException;
	public abstract JsonLikeWriter write (String name, double value) throws IOException;
	public abstract JsonLikeWriter write (String name, Boolean value) throws IOException;
	public abstract JsonLikeWriter write (String name, boolean value) throws IOException;
	public abstract JsonLikeWriter writeNull (String name) throws IOException;

	public abstract JsonLikeWriter endObject () throws IOException;
	public abstract JsonLikeWriter endArray () throws IOException;
	public abstract JsonLikeWriter endBlock () throws IOException;
	
	public JsonLikeWriter() {
		super();
	}

}
