package ca.uhn.fhir.rest.gclient;

import java.util.Collection;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.primitive.IdDt;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2018 University Health Network
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


public class ReferenceClientParam extends BaseClientParam  implements IParam {

	private String myName;

	public ReferenceClientParam(String theName) {
		myName = theName;
	}

	@Override
	public String getParamName() {
		return myName;
	}
	
	public ICriterion<ReferenceClientParam> hasChainedProperty(ICriterion<?> theCriterion) {
		return new ReferenceChainCriterion(getParamName(), theCriterion);
	}

	/**
	 * Match the referenced resource if the resource has the given ID (this can be
	 * the logical ID or the absolute URL of the resource)
	 */
	public ICriterion<ReferenceClientParam> hasId(IdDt theId) {
		return new StringCriterion<ReferenceClientParam>(getParamName(), theId.getValue());
	}

	/**
	 * Match the referenced resource if the resource has the given ID (this can be
	 * the logical ID or the absolute URL of the resource)
	 */
	public ICriterion<ReferenceClientParam> hasId(String theId) {
		return new StringCriterion<ReferenceClientParam>(getParamName(), theId);
	}

	/**
	 * Match the referenced resource if the resource has ANY of the given IDs
	 * (this is an OR search, not an AND search), (this can be the logical ID or
	 * the absolute URL of the resource). Note that to specify an AND search,
	 * simply add a subsequent {@link IQuery#where(ICriterion) where} criteria
	 * with the same parameter.
	 */
	public ICriterion<ReferenceClientParam> hasAnyOfIds(Collection<String> theIds) {
		return new StringCriterion<ReferenceClientParam>(getParamName(), theIds);
	}
	
	private static class ReferenceChainCriterion implements ICriterion<ReferenceClientParam>, ICriterionInternal {

		private String myParamName;
		private ICriterionInternal myWrappedCriterion;

		public ReferenceChainCriterion(String theParamName, ICriterion<?> theWrappedCriterion) {
			myParamName = theParamName;
			myWrappedCriterion = (ICriterionInternal) theWrappedCriterion;
		}

		@Override
		public String getParameterName() {
			return myParamName + "." + myWrappedCriterion.getParameterName();
		}

		@Override
		public String getParameterValue(FhirContext theContext) {
			return myWrappedCriterion.getParameterValue(theContext);
		}

	}

}
