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
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.builder.ToStringBuilder;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IQueryParameterAnd;
import ca.uhn.fhir.model.api.IQueryParameterOr;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.base.composite.BaseIdentifierDt;
import ca.uhn.fhir.model.base.composite.BaseQuantityDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.rest.annotation.OptionalParam;
import ca.uhn.fhir.rest.param.BaseQueryParameter;
import ca.uhn.fhir.rest.param.CompositeAndListParam;
import ca.uhn.fhir.rest.param.CompositeOrListParam;
import ca.uhn.fhir.rest.param.CompositeParam;
import ca.uhn.fhir.rest.param.DateAndListParam;
import ca.uhn.fhir.rest.param.DateOrListParam;
import ca.uhn.fhir.rest.param.DateParam;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.rest.param.NumberAndListParam;
import ca.uhn.fhir.rest.param.NumberOrListParam;
import ca.uhn.fhir.rest.param.NumberParam;
import ca.uhn.fhir.rest.param.QualifiedDateParam;
import ca.uhn.fhir.rest.param.QuantityAndListParam;
import ca.uhn.fhir.rest.param.QuantityOrListParam;
import ca.uhn.fhir.rest.param.QuantityParam;
import ca.uhn.fhir.rest.param.ReferenceAndListParam;
import ca.uhn.fhir.rest.param.ReferenceOrListParam;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.StringAndListParam;
import ca.uhn.fhir.rest.param.StringOrListParam;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.param.TokenAndListParam;
import ca.uhn.fhir.rest.param.TokenOrListParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.param.UriAndListParam;
import ca.uhn.fhir.rest.param.UriOrListParam;
import ca.uhn.fhir.rest.param.UriParam;
import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.util.CollectionUtil;

/**
 * Created by dsotnikov on 2/25/2014.
 */
@SuppressWarnings("deprecation")
public class SearchParameter extends BaseQueryParameter {

	private static final String EMPTY_STRING = "";
	private static HashMap<RestSearchParameterTypeEnum, Set<String>> ourParamQualifiers;
	private static HashMap<Class<?>, RestSearchParameterTypeEnum> ourParamTypes;
	static final String QUALIFIER_ANY_TYPE = ":*";

	static {
		ourParamTypes = new HashMap<Class<?>, RestSearchParameterTypeEnum>();
		ourParamQualifiers = new HashMap<RestSearchParameterTypeEnum, Set<String>>();

		ourParamTypes.put(StringParam.class, RestSearchParameterTypeEnum.STRING);
		ourParamTypes.put(StringOrListParam.class, RestSearchParameterTypeEnum.STRING);
		ourParamTypes.put(StringAndListParam.class, RestSearchParameterTypeEnum.STRING);
		ourParamQualifiers.put(RestSearchParameterTypeEnum.STRING, CollectionUtil.newSet(Constants.PARAMQUALIFIER_STRING_EXACT, Constants.PARAMQUALIFIER_MISSING, EMPTY_STRING));

		ourParamTypes.put(UriParam.class, RestSearchParameterTypeEnum.URI);
		ourParamTypes.put(UriOrListParam.class, RestSearchParameterTypeEnum.URI);
		ourParamTypes.put(UriAndListParam.class, RestSearchParameterTypeEnum.URI);
		// TODO: are these right for URI?
		ourParamQualifiers.put(RestSearchParameterTypeEnum.URI, CollectionUtil.newSet(Constants.PARAMQUALIFIER_STRING_EXACT, Constants.PARAMQUALIFIER_MISSING, EMPTY_STRING));

		ourParamTypes.put(TokenParam.class, RestSearchParameterTypeEnum.TOKEN);
		ourParamTypes.put(TokenOrListParam.class, RestSearchParameterTypeEnum.TOKEN);
		ourParamTypes.put(TokenAndListParam.class, RestSearchParameterTypeEnum.TOKEN);
		ourParamQualifiers.put(RestSearchParameterTypeEnum.TOKEN, CollectionUtil.newSet(Constants.PARAMQUALIFIER_TOKEN_TEXT, Constants.PARAMQUALIFIER_MISSING, EMPTY_STRING));

		ourParamTypes.put(DateParam.class, RestSearchParameterTypeEnum.DATE);
		ourParamTypes.put(DateOrListParam.class, RestSearchParameterTypeEnum.DATE);
		ourParamTypes.put(DateAndListParam.class, RestSearchParameterTypeEnum.DATE);
		ourParamTypes.put(DateRangeParam.class, RestSearchParameterTypeEnum.DATE);
		ourParamQualifiers.put(RestSearchParameterTypeEnum.DATE, CollectionUtil.newSet(Constants.PARAMQUALIFIER_MISSING, EMPTY_STRING));

		ourParamTypes.put(QuantityParam.class, RestSearchParameterTypeEnum.QUANTITY);
		ourParamTypes.put(QuantityOrListParam.class, RestSearchParameterTypeEnum.QUANTITY);
		ourParamTypes.put(QuantityAndListParam.class, RestSearchParameterTypeEnum.QUANTITY);
		ourParamQualifiers.put(RestSearchParameterTypeEnum.QUANTITY, CollectionUtil.newSet(Constants.PARAMQUALIFIER_MISSING, EMPTY_STRING));

		ourParamTypes.put(NumberParam.class, RestSearchParameterTypeEnum.NUMBER);
		ourParamTypes.put(NumberOrListParam.class, RestSearchParameterTypeEnum.NUMBER);
		ourParamTypes.put(NumberAndListParam.class, RestSearchParameterTypeEnum.NUMBER);
		ourParamQualifiers.put(RestSearchParameterTypeEnum.NUMBER, CollectionUtil.newSet(Constants.PARAMQUALIFIER_MISSING, EMPTY_STRING));

		ourParamTypes.put(ReferenceParam.class, RestSearchParameterTypeEnum.REFERENCE);
		ourParamTypes.put(ReferenceOrListParam.class, RestSearchParameterTypeEnum.REFERENCE);
		ourParamTypes.put(ReferenceAndListParam.class, RestSearchParameterTypeEnum.REFERENCE);
		// --vvvv-- no empty because that gets added from OptionalParam#chainWhitelist
		ourParamQualifiers.put(RestSearchParameterTypeEnum.REFERENCE, CollectionUtil.newSet(Constants.PARAMQUALIFIER_MISSING));

		ourParamTypes.put(CompositeParam.class, RestSearchParameterTypeEnum.COMPOSITE);
		ourParamTypes.put(CompositeOrListParam.class, RestSearchParameterTypeEnum.COMPOSITE);
		ourParamTypes.put(CompositeAndListParam.class, RestSearchParameterTypeEnum.COMPOSITE);
		ourParamQualifiers.put(RestSearchParameterTypeEnum.COMPOSITE, CollectionUtil.newSet(Constants.PARAMQUALIFIER_MISSING, EMPTY_STRING));
	}
	
	private List<Class<? extends IQueryParameterType>> myCompositeTypes;
	private List<Class<? extends IResource>> myDeclaredTypes;
	private String myDescription;
	private String myName;
	private IParamBinder myParamBinder;
	private RestSearchParameterTypeEnum myParamType;
	private Set<String> myQualifierBlacklist;
	private Set<String> myQualifierWhitelist;
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

		List<IQueryParameterOr<?>> val = myParamBinder.encode(theContext, theObject);
		for (IQueryParameterOr<?> nextOr : val) {
			retVal.add(new QualifiedParamList(nextOr));
		}

		return retVal;
	}

	public List<Class<? extends IResource>> getDeclaredTypes() {
		return Collections.unmodifiableList(myDeclaredTypes);
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
	public RestSearchParameterTypeEnum getParamType() {
		return myParamType;
	}

	@Override
	public Set<String> getQualifierBlacklist() {
		return myQualifierBlacklist;
	}

	@Override
	public Set<String> getQualifierWhitelist() {
		return myQualifierWhitelist;
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
	public Object parse(FhirContext theContext, List<QualifiedParamList> theString) throws InternalErrorException, InvalidRequestException {
		return myParamBinder.parse(getName(), theString);
	}

	public void setChainlists(String[] theChainWhitelist, String[] theChainBlacklist) {
		myQualifierWhitelist = new HashSet<String>(theChainWhitelist.length);
		myQualifierWhitelist.add(QUALIFIER_ANY_TYPE);

		for (int i = 0; i < theChainWhitelist.length; i++) {
			if (theChainWhitelist[i].equals(OptionalParam.ALLOW_CHAIN_ANY)) {
				myQualifierWhitelist.add('.' + OptionalParam.ALLOW_CHAIN_ANY);
			} else if (theChainWhitelist[i].equals(EMPTY_STRING)) {
				myQualifierWhitelist.add(".");
			} else {
				myQualifierWhitelist.add('.' + theChainWhitelist[i]);
			}
		}

		if (theChainBlacklist.length > 0) {
			myQualifierBlacklist = new HashSet<String>(theChainBlacklist.length);
			for (String next : theChainBlacklist) {
				if (next.equals(EMPTY_STRING)) {
					myQualifierBlacklist.add(EMPTY_STRING);
				} else {
					myQualifierBlacklist.add('.' + next);
				}
			}
		}
	}

	public void setCompositeTypes(Class<? extends IQueryParameterType>[] theCompositeTypes) {
		myCompositeTypes = Arrays.asList(theCompositeTypes);
	}

	public void setDeclaredTypes(Class<? extends IResource>[] theTypes) {
		myDeclaredTypes = Arrays.asList(theTypes);
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

	@SuppressWarnings({ "unchecked" })
	public void setType(final Class<?> type, Class<? extends Collection<?>> theInnerCollectionType, Class<? extends Collection<?>> theOuterCollectionType) {
		this.myType = type;
		if (IQueryParameterType.class.isAssignableFrom(type)) {
			myParamBinder = new QueryParameterTypeBinder((Class<? extends IQueryParameterType>) type, myCompositeTypes);
		} else if (IQueryParameterOr.class.isAssignableFrom(type)) {
			myParamBinder = new QueryParameterOrBinder((Class<? extends IQueryParameterOr<?>>) type, myCompositeTypes);
		} else if (IQueryParameterAnd.class.isAssignableFrom(type)) {
			myParamBinder = new QueryParameterAndBinder((Class<? extends IQueryParameterAnd<?>>) type, myCompositeTypes);
		} else if (String.class.equals(type)) {
			myParamBinder = new StringBinder();
			myParamType = RestSearchParameterTypeEnum.STRING;
		} else {
			throw new ConfigurationException("Unsupported data type for parameter: " + type.getCanonicalName());
		}

		RestSearchParameterTypeEnum typeEnum = ourParamTypes.get(type);
		if (typeEnum != null) {
			Set<String> builtInQualifiers = ourParamQualifiers.get(typeEnum);
			if (builtInQualifiers != null) {
				if (myQualifierWhitelist != null) {
					HashSet<String> qualifierWhitelist = new HashSet<String>();
					qualifierWhitelist.addAll(myQualifierWhitelist);
					qualifierWhitelist.addAll(builtInQualifiers);
					myQualifierWhitelist = qualifierWhitelist;
				} else {
					myQualifierWhitelist = Collections.unmodifiableSet(builtInQualifiers);
				}
			}
		}

		if (myParamType == null) {
			myParamType = typeEnum;
		}

		if (myParamType != null) {
			// ok
		} else if (StringDt.class.isAssignableFrom(type)) {
			myParamType = RestSearchParameterTypeEnum.STRING;
		} else if (QualifiedDateParam.class.isAssignableFrom(type)) {
			myParamType = RestSearchParameterTypeEnum.DATE;
		} else if (BaseIdentifierDt.class.isAssignableFrom(type)) {
			myParamType = RestSearchParameterTypeEnum.TOKEN;
		} else if (BaseQuantityDt.class.isAssignableFrom(type)) {
			myParamType = RestSearchParameterTypeEnum.QUANTITY;
		} else if (ReferenceParam.class.isAssignableFrom(type)) {
			myParamType = RestSearchParameterTypeEnum.REFERENCE;
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

	@Override
	public String toString() {
		ToStringBuilder retVal = new ToStringBuilder(this);
		retVal.append("name", myName);
		retVal.append("required", myRequired);
		return retVal.toString();
	}

}
