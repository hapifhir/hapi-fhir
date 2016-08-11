/**
 * This software has been produced by Akana, Inc. under a professional services
 * agreement with our customer. This work may contain material that is confidential 
 * and proprietary information of Akana, Inc. and is subject to copyright 
 * protection under laws of the United States of America and other countries. 
 * Akana, Inc. grants the customer non-exclusive rights to this material without
 * any warranty expressed or implied. 
 */
package ca.uhn.fhir.parser.json;


/**
 * @author Akana, Inc. Professional Services
 *
 */
public abstract class JsonLikeArray extends JsonLikeValue {

	@Override
	public ValueType getJsonType() {
		return ValueType.ARRAY;
	}
	
	@Override
	public ScalarType getDataType() {
		return null;
	}

	@Override
	public boolean isArray() {
		return true;
	}

	@Override
	public JsonLikeArray getAsArray() {
		return this;
	}

	@Override
	public String getAsString() {
		return null;
	}

	public abstract int size ();
	
	public abstract JsonLikeValue get (int index);
}
