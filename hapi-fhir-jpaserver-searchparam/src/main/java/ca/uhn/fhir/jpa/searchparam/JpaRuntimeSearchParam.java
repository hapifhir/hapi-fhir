package ca.uhn.fhir.jpa.searchparam;

/*-
 * #%L
 * HAPI FHIR Search Parameters
 * %%
 * Copyright (C) 2014 - 2021 Smile CDR, Inc.
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

import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.rest.api.RestSearchParameterTypeEnum;
import org.hl7.fhir.instance.model.api.IBaseReference;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class JpaRuntimeSearchParam extends RuntimeSearchParam {

	private final boolean myUnique;
	private final List<Component> myComponents;

	/**
	 * Constructor
	 */
	public JpaRuntimeSearchParam(IIdType theId, String theUri, String theName, String theDescription, String thePath, RestSearchParameterTypeEnum theParamType, Set<String> theProvidesMembershipInCompartments, Set<String> theTargets, RuntimeSearchParamStatusEnum theStatus, boolean theUnique, List<Component> theComponents, Collection<String> theBase) {
		super(theId, theUri, theName, theDescription, thePath, theParamType, createCompositeList(theParamType), theProvidesMembershipInCompartments, theTargets, theStatus, theBase);
		myUnique = theUnique;
		myComponents = Collections.unmodifiableList(theComponents);
	}

	public List<Component> getComponents() {
		return myComponents;
	}

	public boolean isUnique() {
		return myUnique;
	}

	public static class Component {
		private final String myExpression;
		private final IBaseReference myReference;

		public Component(String theExpression, IBaseReference theReference) {
			myExpression = theExpression;
			myReference = theReference;

		}

		public String getExpression() {
			return myExpression;
		}

		public IBaseReference getReference() {
			return myReference;
		}
	}

	private static ArrayList<RuntimeSearchParam> createCompositeList(RestSearchParameterTypeEnum theParamType) {
		if (theParamType == RestSearchParameterTypeEnum.COMPOSITE) {
			return new ArrayList<>();
		} else {
			return null;
		}
	}


}
