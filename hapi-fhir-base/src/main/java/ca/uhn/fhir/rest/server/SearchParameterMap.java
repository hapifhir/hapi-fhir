package ca.uhn.fhir.rest.server;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2017 University Health Network
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

import java.util.LinkedHashMap;

import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.param.BaseAndListParam;
import ca.uhn.fhir.rest.param.CompositeAndListParam;
import ca.uhn.fhir.rest.param.CompositeOrListParam;
import ca.uhn.fhir.rest.param.DateAndListParam;
import ca.uhn.fhir.rest.param.DateOrListParam;
import ca.uhn.fhir.rest.param.NumberAndListParam;
import ca.uhn.fhir.rest.param.NumberOrListParam;
import ca.uhn.fhir.rest.param.QuantityAndListParam;
import ca.uhn.fhir.rest.param.QuantityOrListParam;
import ca.uhn.fhir.rest.param.ReferenceAndListParam;
import ca.uhn.fhir.rest.param.ReferenceOrListParam;
import ca.uhn.fhir.rest.param.StringAndListParam;
import ca.uhn.fhir.rest.param.StringOrListParam;
import ca.uhn.fhir.rest.param.TokenAndListParam;
import ca.uhn.fhir.rest.param.TokenOrListParam;
import ca.uhn.fhir.rest.param.UriAndListParam;
import ca.uhn.fhir.rest.param.UriOrListParam;

public class SearchParameterMap extends LinkedHashMap<String, BaseAndListParam<?>> {

	private static final long serialVersionUID = 1L;

	public <A extends IQueryParameterType, B extends IQueryParameterType> void add(String theName, CompositeOrListParam<A, B> theCompositeOrListParam) {
		@SuppressWarnings("unchecked")
		CompositeAndListParam<A, B> andList = (CompositeAndListParam<A, B>) get(theName);
		if (andList == null) {
			andList = new CompositeAndListParam<A, B>(theCompositeOrListParam.getLeftType(), theCompositeOrListParam.getRightType());
			put(theName, andList);
		}
		andList.addValue(theCompositeOrListParam);
	}

	public void add(String theName, DateOrListParam theOrListParam) {
		DateAndListParam andList = (DateAndListParam) get(theName);
		if (andList == null) {
			andList = new DateAndListParam();
			put(theName, andList);
		}
		andList.addValue(theOrListParam);
	}

	public void add(String theName, NumberOrListParam theOrListParam) {
		NumberAndListParam andList = (NumberAndListParam) get(theName);
		if (andList == null) {
			andList = new NumberAndListParam();
			put(theName, andList);
		}
		andList.addValue(theOrListParam);
	}

	public void add(String theName, QuantityOrListParam theOrListParam) {
		QuantityAndListParam andList = (QuantityAndListParam) get(theName);
		if (andList == null) {
			andList = new QuantityAndListParam();
			put(theName, andList);
		}
		andList.addValue(theOrListParam);
	}

	public void add(String theName, ReferenceOrListParam theOrListParam) {
		ReferenceAndListParam andList = (ReferenceAndListParam) get(theName);
		if (andList == null) {
			andList = new ReferenceAndListParam();
			put(theName, andList);
		}
		andList.addValue(theOrListParam);
	}

	public void add(String theName, StringOrListParam theOrListParam) {
		StringAndListParam andList = (StringAndListParam) get(theName);
		if (andList == null) {
			andList = new StringAndListParam();
			put(theName, andList);
		}
		andList.addValue(theOrListParam);
	}

	public void add(String theName, TokenOrListParam theOrListParam) {
		TokenAndListParam andList = (TokenAndListParam) get(theName);
		if (andList == null) {
			andList = new TokenAndListParam();
			put(theName, andList);
		}
		andList.addValue(theOrListParam);
	}

	public void add(String theName, UriOrListParam theOrListParam) {
		UriAndListParam andList = (UriAndListParam) get(theName);
		if (andList == null) {
			andList = new UriAndListParam();
			put(theName, andList);
		}
		andList.addValue(theOrListParam);
	}

	// public void add(String theName, IQueryParameterOr<?> theOr) {
	// if (theOr == null) {
	// return;
	// }
	//
	// switch (theOr.getClass()) {
	//
	// }
	//
	// if (!containsKey(theName)) {
	// put(theName, new ArrayList<List<? extends IQueryParameterType>>());
	// }
	//
	// StringAndListParam
	//
	// IQueryParameterAnd<IQueryParameterOr<?>> and = get(theName);
	// and.add(theOr);
	// }

}
