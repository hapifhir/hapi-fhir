package ca.uhn.fhir.rest.param;

import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.util.CoverageIgnore;

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


public class CompositeOrListParam<A extends IQueryParameterType, B extends IQueryParameterType>  extends BaseOrListParam<CompositeOrListParam<?,?>, CompositeParam<A,B>> {

	private Class<A> myLeftType;
	private Class<B> myRightType;

	public CompositeOrListParam(Class<A> theLeftType, Class<B> theRightType) {
		super();
		myLeftType = theLeftType;
		myRightType = theRightType;
	}

	public Class<A> getLeftType() {
		return myLeftType;
	}

	public Class<B> getRightType() {
		return myRightType;
	}

	@CoverageIgnore
	@Override
	CompositeParam<A,B> newInstance() {
		return new CompositeParam<A,B>(myLeftType, myRightType);
	}

	@CoverageIgnore
	@Override
	public CompositeOrListParam<A, B> addOr(CompositeParam<A, B> theParameter) {
		add(theParameter);
		return this;
	}
	

}
