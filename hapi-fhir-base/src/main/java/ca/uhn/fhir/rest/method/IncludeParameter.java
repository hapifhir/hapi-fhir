package ca.uhn.fhir.rest.method;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2015 University Health Network
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.model.api.PathSpecification;
import ca.uhn.fhir.rest.annotation.IncludeParam;
import ca.uhn.fhir.rest.param.BaseQueryParameter;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;

class IncludeParameter extends BaseQueryParameter {

	private Set<String> myAllow;
	private Class<? extends Collection<Include>> myInstantiableCollectionType;
	private Class<?> mySpecType;
	private boolean myReverse;

	public IncludeParameter(IncludeParam theAnnotation, Class<? extends Collection<Include>> theInstantiableCollectionType, Class<?> theSpecType) {
		myInstantiableCollectionType = theInstantiableCollectionType;
		myReverse = theAnnotation.reverse();
		if (theAnnotation.allow().length > 0) {
			myAllow = new HashSet<String>();
			for (String next : theAnnotation.allow()) {
				if (next != null) {
					myAllow.add(next);
				}
			}
		} else {
			myAllow = Collections.emptySet();
		}

		mySpecType = theSpecType;
		if (mySpecType != Include.class && mySpecType != PathSpecification.class && mySpecType != String.class) {
			throw new ConfigurationException("Invalid @" + IncludeParam.class.getSimpleName() + " parameter type: " + mySpecType);
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<QualifiedParamList> encode(FhirContext theContext, Object theObject) throws InternalErrorException {
		ArrayList<QualifiedParamList> retVal = new ArrayList<QualifiedParamList>();

		if (myInstantiableCollectionType == null) {
			if (mySpecType == Include.class) {
				retVal.add(QualifiedParamList.singleton(((Include) theObject).getValue()));
			} else if (mySpecType == PathSpecification.class) {
				retVal.add(QualifiedParamList.singleton(((PathSpecification) theObject).getValue()));
			} else {
				retVal.add(QualifiedParamList.singleton(((String) theObject)));
			}
		} else {
			Collection<Include> val = (Collection<Include>) theObject;
			for (Include pathSpec : val) {
				retVal.add(QualifiedParamList.singleton(pathSpec.getValue()));
			}
		}

		return retVal;
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
	public boolean handlesMissing() {
		return true;
	}

	@Override
	public boolean isRequired() {
		return false;
	}

	@Override
	public Object parse(FhirContext theContext, List<QualifiedParamList> theString) throws InternalErrorException, InvalidRequestException {
		Collection<Include> retValCollection = null;
		if (myInstantiableCollectionType != null) {
			try {
				retValCollection = myInstantiableCollectionType.newInstance();
			} catch (Exception e) {
				throw new InternalErrorException("Failed to instantiate " + myInstantiableCollectionType.getName(), e);
			}
		}

		for (List<String> nextParamList : theString) {
			if (nextParamList.isEmpty()) {
				continue;
			}
			if (nextParamList.size() > 1) {
				throw new InvalidRequestException(theContext.getLocalizer().getMessage(IncludeParameter.class, "orIncludeInRequest"));
			}

			String value = nextParamList.get(0);
			if (myAllow != null && !myAllow.isEmpty()) {
				if (!myAllow.contains(value)) {
					if (!myAllow.contains("*")) {
						String msg = theContext.getLocalizer().getMessage(IncludeParameter.class, "invalidIncludeNameInRequest", value, new TreeSet<String>(myAllow).toString(), getName());
						throw new InvalidRequestException(msg);
					}
				}
			}
			if (retValCollection == null) {
				if (mySpecType == String.class) {
					return value;
				} else if (mySpecType == PathSpecification.class) {
					return new PathSpecification(value);
				} else {
					return new Include(value);
				}
			} else {
				if (mySpecType == PathSpecification.class) {
					retValCollection.add(new PathSpecification(value));
				} else {
					retValCollection.add(new Include(value));
				}
			}
		}

		return retValCollection;
	}

}
