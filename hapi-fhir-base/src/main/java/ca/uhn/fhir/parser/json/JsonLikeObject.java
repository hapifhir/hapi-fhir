package ca.uhn.fhir.parser.json;

import java.util.Iterator;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

	public abstract Iterator<String> keyIterator();

	public abstract JsonLikeValue get(String key);

}
