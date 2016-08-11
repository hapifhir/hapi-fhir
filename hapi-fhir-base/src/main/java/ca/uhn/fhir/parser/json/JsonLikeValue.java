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
public abstract class JsonLikeValue {
	
	public enum ValueType {
		ARRAY, OBJECT, SCALAR, NULL
	};
	
	public enum ScalarType {
		NUMBER, STRING, BOOLEAN
	}
	
	public abstract ValueType getJsonType ();
	
	public abstract ScalarType getDataType ();
	
	public boolean isArray () {
		return false;
	}
	
	public boolean isObject () {
		return false;
	}
	
	public boolean isScalar () {
		return this.getJsonType() == ValueType.SCALAR;
	}
	
	public boolean isString () {
		return this.getJsonType() == ValueType.SCALAR && this.getDataType() == ScalarType.STRING;
	}
	
	public boolean isNumber () {
		return this.getJsonType() == ValueType.SCALAR && this.getDataType() == ScalarType.NUMBER;
	}
	
	public boolean isNull () {
		return this.getJsonType() == ValueType.NULL;
	}
	
	public JsonLikeArray getAsArray () {
		return null;
	}
	public JsonLikeObject getAsObject () {
		return null;
	}
	public String getAsString () {
		return this.toString();
	}
	public boolean getAsBoolean () {
		return !isNull();
	}
	
	public static JsonLikeArray asArray (JsonLikeValue element) {
		if (element != null) {
			return element.getAsArray();
		}
		return null;
	}
	public static JsonLikeObject asObject (JsonLikeValue element) {
		if (element != null) {
			return element.getAsObject();
		}
		return null;
	}
	public static String asString (JsonLikeValue element) {
		if (element != null) {
			return element.getAsString();
		}
		return null;
	}
	public static boolean asBoolean (JsonLikeValue element) {
		if (element != null) {
			return element.getAsBoolean();
		}
		return false;
	}
	

    public static final JsonLikeValue NULL = new JsonLikeValue() {
        @Override
        public ValueType getJsonType() {
            return ValueType.NULL;
        }

        @Override
		public ScalarType getDataType() {
			return null;
		}

		@Override
        public boolean equals (Object obj) {
            if (this == obj){
                return true;
            }
            if (obj instanceof JsonLikeValue) {
                return getJsonType().equals(((JsonLikeValue)obj).getJsonType());
            }
            return false;
        }

        @Override
        public int hashCode() {
            return "null".hashCode();
        }

        @Override
        public String toString() {
            return "null";
        }
    };

    public static final JsonLikeValue TRUE = new JsonLikeValue() {
        @Override
        public ValueType getJsonType() {
        	return ValueType.SCALAR;
        }
        
        @Override
        public ScalarType getDataType() {
            return ScalarType.BOOLEAN;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj){
                return true;
            }
            if (obj instanceof JsonLikeValue) {
                return getJsonType().equals(((JsonLikeValue)obj).getJsonType())
                	&& getDataType().equals(((JsonLikeValue)obj).getDataType())
                	&& toString().equals(((JsonLikeValue)obj).toString());
            }
            return false;
        }

        @Override
        public int hashCode() {
            return "true".hashCode();
        }

        @Override
        public String toString() {
            return "true";
        }
    };

    public static final JsonLikeValue FALSE = new JsonLikeValue() {
        @Override
        public ValueType getJsonType() {
        	return ValueType.SCALAR;
        }
        
        @Override
        public ScalarType getDataType() {
            return ScalarType.BOOLEAN;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj){
                return true;
            }
            if (obj instanceof JsonLikeValue) {
                return getJsonType().equals(((JsonLikeValue)obj).getJsonType())
                    	&& getDataType().equals(((JsonLikeValue)obj).getDataType())
                    	&& toString().equals(((JsonLikeValue)obj).toString());
            }
            return false;
        }

        @Override
        public int hashCode() {
            return "false".hashCode();
        }

        @Override
        public String toString() {
            return "false";
        }
    };
}
