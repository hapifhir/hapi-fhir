package ca.uhn.fhir.parser.json;

import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
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
	
	protected static class EntryOrderedSet<T> extends AbstractSet<T> {
		private transient ArrayList<T> data = null;
		
		public EntryOrderedSet (int initialCapacity) {
			data = new ArrayList<T>(initialCapacity);
		}
		@SuppressWarnings("unused")
		public EntryOrderedSet () {
			data = new ArrayList<T>();
		}
		
		@Override
		public int size() {
			return data.size();
		}

		@Override
		public boolean contains(Object o) {
			return data.contains(o);
		}

		@SuppressWarnings("unused")  // not really.. just not here
		public T get(int index) {
			return data.get(index);
		}
		
		@Override
		public boolean add(T element) {
			if (data.contains(element)) {
				return false;
			}
			return data.add(element);
		}
		
		@Override
		public boolean remove(Object o) {
			return data.remove(o);
		}

		@Override
		public void clear() {
			data.clear();
		}
		
		@Override
		public Iterator<T> iterator() {
			return data.iterator();
		}
		
	}
	
}
