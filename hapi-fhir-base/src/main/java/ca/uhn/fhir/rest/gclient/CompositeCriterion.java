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
import static org.apache.commons.lang3.StringUtils.defaultString;

import ca.uhn.fhir.context.FhirContext;

public class CompositeCriterion<A extends IParam, B extends IParam> implements ICompositeWithLeft<B>, ICriterion<B>, ICriterionInternal {

	private ICriterion<B> myRight;
	private String myName;
	private ICriterion<A> myLeft;

	public CompositeCriterion(String theName, ICriterion<A> theLeft) {
		myName = theName;
		myLeft = theLeft;
	}

	@Override
	public ICriterion<B> withRight(ICriterion<B> theRight) {
		myRight = theRight;
		return this;
	}

	@Override
	public String getParameterValue(FhirContext theContext) {
		ICriterionInternal left = (ICriterionInternal) myLeft;
		ICriterionInternal right = (ICriterionInternal) myRight;
		return defaultString(left.getParameterValue(theContext)) + '$' + defaultString(right.getParameterValue(theContext));
	}

	@Override
	public String getParameterName() {
		return myName;
	}

}
