/*
 * #%L
 * HAPI FHIR - MongoDB Framework Bundle
 * %%
 * Copyright (C) 2016 - 2020 University Health Network
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
package ca.uhn.fhir.parser.bson;

import java.io.Reader;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.bson.BsonArray;
import org.bson.BsonBoolean;
import org.bson.BsonDecimal128;
import org.bson.BsonDocument;
import org.bson.BsonDouble;
import org.bson.BsonInt32;
import org.bson.BsonInt64;
import org.bson.BsonNull;
import org.bson.BsonReader;
import org.bson.BsonString;
import org.bson.BsonType;
import org.bson.BsonValue;
import org.bson.types.Decimal128;

import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.parser.json.JsonLikeArray;
import ca.uhn.fhir.parser.json.JsonLikeObject;
import ca.uhn.fhir.parser.json.JsonLikeStructure;
import ca.uhn.fhir.parser.json.JsonLikeValue;

/**
 * Provides the input source for {@code JsonParser} to deserialize 
 * FHIR Resources in MongoDB documents
 * <p>
 * The BSON version of the FHIR resource is accessed one of two ways:
 * <ol>
 * <li>By directly navigating a {@code BsonDocument} structure
 * <li>Indirectly by way of a {@code BsonReader} in which case, 
 * the BSON document will be read before interacting with the
 * FHIR parser. This allows the parser to, for instance, get 
 * the names of all the members of a JSON object.
 * </ol>
 * 
 * Copyright (C) 2014 - 2020 University Health Network
 * @author williamEdenton@gmail.com
 */
public class BsonFhirStructure implements JsonLikeStructure {

	private enum ROOT_TYPE {OBJECT, ARRAY};
	private ROOT_TYPE rootType = null;
	private BsonReader bsonReader = null;
	private BsonDocument nativeRoot = null;
	private JsonLikeValue jsonLikeRoot = null;
	private boolean readDone = false;
	
	public BsonFhirStructure () {
		super();
	}
	public BsonFhirStructure (BsonDocument nativeObject) {
		this();
		this.setNativeObject(nativeObject);
	}
	public BsonFhirStructure (BsonReader bsonReader) {
		this();
		this.setBsonReader(bsonReader);
	}
	
	public void setNativeObject (BsonDocument bson) {
		this.rootType = ROOT_TYPE.OBJECT;
		this.nativeRoot = bson;
	}

	public void setBsonReader(BsonReader bsonReader) {
		this.bsonReader = bsonReader;
		this.load();
	}

	public void load () {
        if (!readDone) {
	        readDone = true;
	        BsonType bsonType = bsonReader.readBsonType();
	        if (bsonType == BsonType.DOCUMENT) {
	    		this.rootType = ROOT_TYPE.OBJECT;
	        	nativeRoot = getObject();
	    		System.out.println("Loaded Resource:\n"+nativeRoot.toString());

	        } else {
	    		throw new DataFormatException("Content must be a valid BSON Document.");
	        }
        }
	}

	@Override
	public void load(Reader theReader) throws DataFormatException {
		// Reader is not applicable to BSON		
	}

	@Override
	public void load(Reader theReader, boolean allowArray) throws DataFormatException {
		// Reader is not applicable to BSON		
	}

    protected BsonDocument getObject () {
    	BsonDocument result = new BsonDocument();
        bsonReader.readStartDocument();
        while (bsonReader.readBsonType() != BsonType.END_OF_DOCUMENT) {
            String fieldName = bsonReader.readName();
            BsonValue value = getValue();
            result.put(fieldName, value);
        }
        bsonReader.readEndDocument();
        return result;
    }
    
    protected BsonArray getArray () {
    	BsonArray result = new BsonArray();
        bsonReader.readStartArray();
        while (bsonReader.readBsonType() != BsonType.END_OF_DOCUMENT) {
            BsonValue value = getValue();
            result.add(value);
        }
        bsonReader.readEndArray();
        return result;
    }
    
    protected BsonValue getValue () {
        BsonType bsonType = bsonReader.getCurrentBsonType();

        switch (bsonType) {
	        case NULL: {
	            bsonReader.readNull();
	            return BsonNull.VALUE;
	        }
	        case BOOLEAN: {
	        	boolean value = bsonReader.readBoolean();
	        	return BsonBoolean.valueOf(value);
	        }
	        case ARRAY: {
	            return getArray();
	        }
	        case DOCUMENT: {
	            return getObject();
	        }
	        case INT32: {
	        	int value = bsonReader.readInt32();
	        	return new BsonInt32(value);
	        }
	        case INT64: {
	        	long value = bsonReader.readInt64();
	        	return new BsonInt64(value);
	        }
	        case DOUBLE: {
	        	double value = bsonReader.readDouble();
	        	return new BsonDouble(value);
	        }
	        case DECIMAL128: {
	        	Decimal128 value = bsonReader.readDecimal128();
	        	return new BsonDecimal128(value);
	        }
	        case STRING: {
	        	String value = bsonReader.readString();
	        	return new BsonString(value);
	        }
	        default: {
	        	String value = "[unsupported type "+bsonType.toString()+"]";
	        	bsonReader.skipValue();
	        	return new BsonString(value);
	        }
        }
    }
	

	@Override
	public JsonLikeObject getRootObject() throws DataFormatException {
		if (rootType == ROOT_TYPE.OBJECT) {
			if (null == jsonLikeRoot) {
				jsonLikeRoot = new BsonJsonObject(nativeRoot);
			}
			return jsonLikeRoot.getAsObject();
		}
		throw new DataFormatException("Content must be a valid BSON Document. It must start with '{'.");
	}

	@Override
	public JsonLikeArray getRootArray() throws DataFormatException {
//		if (rootType == ROOT_TYPE.ARRAY) {
//			if (null == jsonLikeRoot) {
//				jsonLikeRoot = new BsonJsonArray((JsonArray)nativeRoot);
//			}
//			return jsonLikeRoot.getAsArray();
//		}
		throw new DataFormatException("Root BSON Document cannot be an array.");
	}

	private static class BsonJsonObject extends JsonLikeObject {
		private BsonDocument nativeObject;
		private Set<String> keySet = null;
		private Map<String,JsonLikeValue> jsonLikeMap = new LinkedHashMap<String,JsonLikeValue>();
		
		public BsonJsonObject (BsonDocument bson) {
			this.nativeObject = bson;
		}

		@Override
		public Object getValue () {
			return null;
		}

		@Override
		public Set<String> keySet () {
			if (null == keySet) {
				Set<Entry<String,BsonValue>> entrySet = nativeObject.entrySet();
				keySet = new EntryOrderedSet<String>(entrySet.size());
				for (Entry<String,?> entry : entrySet) {
					keySet.add(entry.getKey());
				}
			}
			return keySet;
		}

		@Override
		public JsonLikeValue get (String key) {
			JsonLikeValue result = null;
			if (jsonLikeMap.containsKey(key)) {
				result = jsonLikeMap.get(key); 
			} else {
				BsonValue child = nativeObject.get(key);
				if (child != null) {
					result = new BsonJsonValue(child);
				}
				jsonLikeMap.put(key, result);
			}
			return result;
		}
	}
	
	private static class BsonJsonArray extends JsonLikeArray {
		private BsonArray nativeArray;
		private Map<Integer,JsonLikeValue> jsonLikeMap = new LinkedHashMap<Integer,JsonLikeValue>();
		
		public BsonJsonArray (BsonArray json) {
			this.nativeArray = json;
		}

		@Override
		public Object getValue() {
			return null;
		}

		@Override
		public int size() {
			return nativeArray.size();
		}

		@Override
		public JsonLikeValue get(int index) {
			JsonLikeValue result = null;
			if (index >= 0 && index < nativeArray.size()) {
				Integer key = Integer.valueOf(index);
				if (jsonLikeMap.containsKey(key)) {
					result = jsonLikeMap.get(key); 
				} else {
					BsonValue child = nativeArray.get(index);
					if (child != null) {
						result = new BsonJsonValue(child);
					}
					jsonLikeMap.put(key, result);
				}
			}
			return result;
		}
	}
	
	private static class BsonJsonValue extends JsonLikeValue {
		private BsonValue nativeValue;
		private JsonLikeObject jsonLikeObject = null;
		private JsonLikeArray jsonLikeArray = null;
		
		public BsonJsonValue (BsonValue json) {
			this.nativeValue = json;
		}

		@Override
		public Object getValue () {
			if (nativeValue != null && !(nativeValue.isDocument() || nativeValue.isArray())) {
				// not a container
				if (nativeValue.isNumber()) {
					return getAsNumber();
				}
				if (nativeValue.isBoolean()) {
					return Boolean.valueOf(nativeValue.asBoolean().getValue());
				}
				return getAsString();
			}
			return null;
		}
		
		@Override
		public ValueType getJsonType() {
			if (nativeValue != null) {
				return typeMap.get(nativeValue.getBsonType()).getLeft();
			}
			return null;
		}
		
		@Override
		public ScalarType getDataType() {
			if (nativeValue != null) {
				return typeMap.get(nativeValue.getBsonType()).getRight();
			}
			return null;
		}

		@Override
		public JsonLikeArray getAsArray() {
			if (nativeValue != null && nativeValue.isArray()) {
				if (null == jsonLikeArray) {
					jsonLikeArray = new BsonJsonArray(nativeValue.asArray());
				}
			}
			return jsonLikeArray;
		}

		@Override
		public JsonLikeObject getAsObject() {
			if (nativeValue != null && nativeValue.isDocument()) {
				if (null == jsonLikeObject) {
					jsonLikeObject = new BsonJsonObject(nativeValue.asDocument());
				}
			}
			return jsonLikeObject;
		}

		@Override
		public Number getAsNumber() {
			return nativeValue != null ? convertBsonNumber(nativeValue) : null;
		}

		@Override
		public String getAsString() {
			String result = null;
			if (nativeValue != null) {
				if (nativeValue.isString()) {
					result = nativeValue.asString().getValue(); 
				} else {
					result = nativeValue.toString();
				}
			}
			return result;
		}

		@Override
		public boolean getAsBoolean() {
			if (nativeValue != null && nativeValue.isBoolean()) {
				return nativeValue.asBoolean().getValue();
			}
			return super.getAsBoolean();
		}
		
		protected Number convertBsonNumber (final BsonValue bson) {
			if (bson.isNumber()) {
				return new Number() {
					@Override public int intValue() {return bson.asNumber().intValue();}
					@Override public long longValue() {return bson.asNumber().longValue();}
					@Override public float floatValue() {return (float)bson.asNumber().doubleValue();}
					@Override public double doubleValue() {return bson.asNumber().doubleValue();}
				};
			}
			if (bson.isString()) {
				return new BigDecimal(bson.asString().getValue());
			}
			if (bson.isDecimal128()) {
				return bson.asDecimal128().decimal128Value().bigDecimalValue();
			}
			return null;
		}
		
		private static final Map<BsonType, Pair<ValueType,ScalarType>> typeMap;
		static {
			typeMap = new HashMap<BsonType, Pair<ValueType,ScalarType>>();
			typeMap.put(BsonType.DOUBLE, new ImmutablePair<>(ValueType.SCALAR, ScalarType.NUMBER));
			typeMap.put(BsonType.STRING, new ImmutablePair<>(ValueType.SCALAR, ScalarType.STRING));
			typeMap.put(BsonType.DOCUMENT, new ImmutablePair<>(ValueType.OBJECT, null));
			typeMap.put(BsonType.ARRAY, new ImmutablePair<>(ValueType.ARRAY, null));
			typeMap.put(BsonType.BINARY, new ImmutablePair<>(ValueType.SCALAR, null)); // maybe include
			typeMap.put(BsonType.UNDEFINED, new ImmutablePair<>(null, null));
			typeMap.put(BsonType.OBJECT_ID, new ImmutablePair<>(ValueType.SCALAR, null)); // _id only (I think)
			typeMap.put(BsonType.BOOLEAN, new ImmutablePair<>(ValueType.SCALAR, ScalarType.BOOLEAN));
			typeMap.put(BsonType.DATE_TIME, new ImmutablePair<>(ValueType.SCALAR, null)); // need to include
			typeMap.put(BsonType.NULL, new ImmutablePair<>(ValueType.NULL, null));
			typeMap.put(BsonType.REGULAR_EXPRESSION, new ImmutablePair<>(ValueType.SCALAR, null)); // maybe throw error
			typeMap.put(BsonType.DB_POINTER, new ImmutablePair<>(ValueType.SCALAR, null)); // maybe throw error
			typeMap.put(BsonType.JAVASCRIPT, new ImmutablePair<>(ValueType.SCALAR, null)); // maybe throw error
			typeMap.put(BsonType.SYMBOL, new ImmutablePair<>(ValueType.SCALAR, null)); // maybe throw error - deprecated
			typeMap.put(BsonType.JAVASCRIPT_WITH_SCOPE, new ImmutablePair<>(ValueType.SCALAR, null)); // maybe throw error
			typeMap.put(BsonType.INT32, new ImmutablePair<>(ValueType.SCALAR, ScalarType.NUMBER));
			typeMap.put(BsonType.TIMESTAMP, new ImmutablePair<>(ValueType.SCALAR, null)); // maybe incldue
			typeMap.put(BsonType.INT64, new ImmutablePair<>(ValueType.SCALAR, ScalarType.NUMBER));
			typeMap.put(BsonType.DECIMAL128, new ImmutablePair<>(ValueType.SCALAR, ScalarType.NUMBER));
			typeMap.put(BsonType.MIN_KEY, new ImmutablePair<>(ValueType.SCALAR, null)); // maybe throw error
			typeMap.put(BsonType.MAX_KEY, new ImmutablePair<>(ValueType.SCALAR, null)); // maybe throw error
			
		}
	}

}
