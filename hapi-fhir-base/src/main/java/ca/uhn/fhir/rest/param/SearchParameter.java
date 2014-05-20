package ca.uhn.fhir.rest.param;

/*
 * #%L
 * HAPI FHIR Library
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IQueryParameterAnd;
import ca.uhn.fhir.model.api.IQueryParameterOr;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.model.dstu.composite.IdentifierDt;
import ca.uhn.fhir.model.dstu.composite.QuantityDt;
import ca.uhn.fhir.model.dstu.valueset.SearchParamTypeEnum;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.rest.method.QualifiedParamList;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;

/**
 * Created by dsotnikov on 2/25/2014.
 */
public class SearchParameter extends BaseQueryParameter {

	private String myDescription;
	private String myName;
	private IParamBinder myParamBinder;
	private SearchParamTypeEnum myParamType;
	private boolean myRequired;
	private Class<?> myType;

	public SearchParameter() {
	}

	public SearchParameter(String theName, boolean theRequired) {
		this.myName = theName;
		this.myRequired = theRequired;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ca.uhn.fhir.rest.param.IParameter#encode(java.lang.Object)
	 */
	@Override
	public List<QualifiedParamList> encode(FhirContext theContext, Object theObject) throws InternalErrorException {
		ArrayList<QualifiedParamList> retVal = new ArrayList<QualifiedParamList>();

		List<IQueryParameterOr> val = myParamBinder.encode(theContext, theObject);
		for (IQueryParameterOr nextOr : val) {
			retVal.add(new QualifiedParamList(theContext, nextOr));
		}

		return retVal;
	}

	public String getDescription() {
		return myDescription;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ca.uhn.fhir.rest.param.IParameter#getName()
	 */
	@Override
	public String getName() {
		return myName;
	}

	@Override
	public SearchParamTypeEnum getParamType() {
		return myParamType;
	}

	public Class<?> getType() {
		return myType;
	}

	@Override
	public boolean handlesMissing() {
		return false;
	}

	@Override
	public boolean isRequired() {
		return myRequired;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ca.uhn.fhir.rest.param.IParameter#parse(java.util.List)
	 */
	@Override
	public Object parse(List<QualifiedParamList> theString) throws InternalErrorException, InvalidRequestException {
		return myParamBinder.parse(theString);
	}

	public void setDescription(String theDescription) {
		myDescription = theDescription;
	}

	public void setName(String name) {
		this.myName = name;
	}

	public void setRequired(boolean required) {
		this.myRequired = required;
	}

	@SuppressWarnings("unchecked")
	public void setType(final Class<?> type, Class<? extends Collection<?>> theInnerCollectionType, Class<? extends Collection<?>> theOuterCollectionType) {
		this.myType = type;
		if (IQueryParameterType.class.isAssignableFrom(type)) {
			myParamBinder = new QueryParameterTypeBinder((Class<? extends IQueryParameterType>) type);
		} else if (IQueryParameterOr.class.isAssignableFrom(type)) {
			myParamBinder = new QueryParameterOrBinder((Class<? extends IQueryParameterOr>) type);
		} else if (IQueryParameterAnd.class.isAssignableFrom(type)) {
			myParamBinder = new QueryParameterAndBinder((Class<? extends IQueryParameterAnd>) type);
		} else if (String.class.equals(type)) {
			myParamBinder = new StringBinder();
		} else {
			throw new ConfigurationException("Unsupported data type for parameter: " + type.getCanonicalName());
		}

		if (String.class.equals(type)) {
			myParamType = SearchParamTypeEnum.STRING;
		} else if (StringDt.class.isAssignableFrom(type)) {
			myParamType = SearchParamTypeEnum.STRING;
		} else if (QualifiedDateParam.class.isAssignableFrom(type)) {
			myParamType = SearchParamTypeEnum.DATE;
		} else if (DateRangeParam.class.isAssignableFrom(type)) {
			myParamType = SearchParamTypeEnum.DATE;
		} else if (CodingListParam.class.isAssignableFrom(type)) {
			myParamType = SearchParamTypeEnum.TOKEN;
		} else if (IdentifierDt.class.isAssignableFrom(type)) {
			myParamType = SearchParamTypeEnum.TOKEN;
		} else if (QuantityDt.class.isAssignableFrom(type)) {
			myParamType = SearchParamTypeEnum.QUANTITY;
		} else if (ReferenceParam.class.isAssignableFrom(type)) {
			myParamType = SearchParamTypeEnum.REFERENCE;
		} else {
			throw new ConfigurationException("Unknown search parameter type: " + type);
		}

		// NB: Once this is enabled, we should return true from handlesMissing if
		// it's a collection type
		// if (theInnerCollectionType != null) {
		// this.parser = new CollectionBinder(this.parser, theInnerCollectionType);
		// }
		//
		// if (theOuterCollectionType != null) {
		// this.parser = new CollectionBinder(this.parser, theOuterCollectionType);
		// }

	}

}
