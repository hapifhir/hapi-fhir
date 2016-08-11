/**
 * This software has been produced by Akana, Inc. under a professional services
 * agreement with our customer. This work may contain material that is confidential 
 * and proprietary information of Akana, Inc. and is subject to copyright 
 * protection under laws of the United States of America and other countries. 
 * Akana, Inc. grants the customer non-exclusive rights to this material without
 * any warranty expressed or implied. 
 */
package ca.uhn.fhir.parser.json;

import java.util.Set;

/**
 * @author Akana, Inc. Professional Services
 *
 */
public abstract class JsonLikeObject extends JsonLikeValue {

	@Override
	public ValueType getJsonType() {
		return ValueType.OBJECT;
	}
	
	@Override
	public ScalarType getDataType() {
		return null;
	}

	@Override
	public boolean isObject() {
		return true;
	}

	@Override
	public JsonLikeObject getAsObject() {
		return this;
	}

	@Override
	public String getAsString() {
		return null;
	}

	public abstract Set<String> keySet ();
	
	public abstract JsonLikeValue get (String key);
	
	public String getString (String key) {
		JsonLikeValue value = this.get(key);
		if (null == value) {
			throw new NullPointerException("Json object missing element named \""+key+"\"");
		}
		return value.getAsString();
	}
	
	public String getString (String key, String defaultValue) {
		String result = defaultValue;
		JsonLikeValue value = this.get(key);
		if (value != null) {
			result = value.getAsString();
		}
		return result;
	}
}
