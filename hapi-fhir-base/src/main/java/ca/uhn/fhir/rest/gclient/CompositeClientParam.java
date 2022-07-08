package ca.uhn.fhir.rest.gclient;

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

/**
 * Composite parameter type for use in fluent client interfaces
 */
public class CompositeClientParam<A extends IParam, B extends IParam> extends BaseClientParam implements IParam {

	private String myName;

	public CompositeClientParam(String theName) {
		myName=theName;
	}


	@Override
	public String getParamName() {
		return myName;
	}
	
	public ICompositeWithLeft<B> withLeft(ICriterion<A> theLeft) {
		return new CompositeCriterion<A,B>(myName, theLeft);
	}
	
	
}
