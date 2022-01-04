package ca.uhn.fhir.rest.client.method;

/*
 * #%L
 * HAPI FHIR - Client Framework
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

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.rest.annotation.IncludeParam;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.QualifiedParamList;
import ca.uhn.fhir.rest.api.RestSearchParameterTypeEnum;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

class IncludeParameter extends BaseQueryParameter {

	private Set<String> myAllow;
	private Class<? extends Collection<Include>> myInstantiableCollectionType;
	private Class<?> mySpecType;
	private boolean myReverse;


	public IncludeParameter(IncludeParam theAnnotation, Class<? extends Collection<Include>> theInstantiableCollectionType, Class<?> theSpecType) {
		myInstantiableCollectionType = theInstantiableCollectionType;
		myReverse = theAnnotation.reverse();
		if (theAnnotation.allow().length > 0) {
			myAllow = new HashSet<>();
			for (String next : theAnnotation.allow()) {
				if (next != null) {
					myAllow.add(next);
				}
			}
		} else {
			myAllow = Collections.emptySet();
		}

		mySpecType = theSpecType;
		if (mySpecType != Include.class && mySpecType != String.class) {
			throw new ConfigurationException(Msg.code(1462) + "Invalid @" + IncludeParam.class.getSimpleName() + " parameter type: " + mySpecType);
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<QualifiedParamList> encode(FhirContext theContext, Object theObject) throws InternalErrorException {
		ArrayList<QualifiedParamList> retVal = new ArrayList<>();

		if (myInstantiableCollectionType == null) {
			if (mySpecType == Include.class) {
				convertAndAddIncludeToList(retVal, (Include) theObject, theContext);
			} else {
				retVal.add(QualifiedParamList.singleton(((String) theObject)));
			}
		} else {
			Collection<Include> val = (Collection<Include>) theObject;
			for (Include include : val) {
				convertAndAddIncludeToList(retVal, include, theContext);
			}
		}

		return retVal;
	}

	private void convertAndAddIncludeToList(ArrayList<QualifiedParamList> theQualifiedParamLists, Include theInclude, FhirContext theContext) {
		String qualifier = null;
		if (theInclude.isRecurse()) {
			if (theContext.getVersion().getVersion().isEqualOrNewerThan(FhirVersionEnum.R4)) {
				qualifier = Constants.PARAM_INCLUDE_QUALIFIER_ITERATE;
			} else {
				qualifier = Constants.PARAM_INCLUDE_QUALIFIER_RECURSE;
			}
		}
		theQualifiedParamLists.add(QualifiedParamList.singleton(qualifier, theInclude.getValue()));
	}

	public Set<String> getAllow() {
		return myAllow;
	}

	@Override
	public String getName() {
		return myReverse ? "_revinclude" : "_include";
	}

	@Override
	public RestSearchParameterTypeEnum getParamType() {
		return null;
	}

	@Override
	public boolean isRequired() {
		return false;
	}

}
