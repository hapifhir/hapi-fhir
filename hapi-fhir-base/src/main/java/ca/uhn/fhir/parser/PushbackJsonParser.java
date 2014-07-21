package ca.uhn.fhir.parser;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 University Health Network
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

import java.math.BigDecimal;

import javax.json.stream.JsonLocation;
import javax.json.stream.JsonParser;

public class PushbackJsonParser implements JsonParser {

	private JsonParser myWrap;

	public PushbackJsonParser(JsonParser theWrap) {
		myWrap=theWrap;
	}
	
	@Override
	public boolean hasNext() {
		return myWrap.hasNext();
	}

	@Override
	public Event next() {
		return myWrap.next();
	}

	@Override
	public String getString() {
		return myWrap.getString();
	}

	@Override
	public boolean isIntegralNumber() {
		return myWrap.isIntegralNumber();
	}

	@Override
	public int getInt() {
		return myWrap.getInt();
	}

	@Override
	public long getLong() {
		return myWrap.getLong();
	}

	@Override
	public BigDecimal getBigDecimal() {
		 return myWrap.getBigDecimal();
	}

	@Override
	public JsonLocation getLocation() {
		return myWrap.getLocation();
	}

	@Override
	public void close() {
			myWrap.close();
	}

}
