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
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
import ca.uhn.fhir.i18n.Msg;
import java.util.*;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

import ca.uhn.fhir.context.*;
import ca.uhn.fhir.model.api.*;
import ca.uhn.fhir.model.base.composite.BaseIdentifierDt;
import ca.uhn.fhir.model.base.composite.BaseQuantityDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.rest.annotation.OptionalParam;
import ca.uhn.fhir.rest.api.*;
import ca.uhn.fhir.rest.param.*;
import ca.uhn.fhir.rest.param.binder.*;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.util.CollectionUtil;
import ca.uhn.fhir.util.ReflectionUtil;

public class SearchParameter extends BaseQueryParameter {

	private static final String EMPTY_STRING = "";
	private static HashMap<RestSearchParameterTypeEnum, Set<String>> ourParamQualifiers;
	private static HashMap<Class<?>, RestSearchParameterTypeEnum> ourParamTypes;
	static final String QUALIFIER_ANY_TYPE = ":*";

	static {
		ourParamTypes = new HashMap<>();
		ourParamQualifiers = new HashMap<>();

		ourParamTypes.put(StringParam.class, RestSearchParameterTypeEnum.STRING);
		ourParamTypes.put(StringOrListParam.class, RestSearchParameterTypeEnum.STRING);
		ourParamTypes.put(StringAndListParam.class, RestSearchParameterTypeEnum.STRING);
		ourParamQualifiers.put(RestSearchParameterTypeEnum.STRING, CollectionUtil.newSet(Constants.PARAMQUALIFIER_STRING_EXACT, Constants.PARAMQUALIFIER_STRING_CONTAINS, Constants.PARAMQUALIFIER_MISSING, EMPTY_STRING));

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

		ourParamTypes.put(HasParam.class, RestSearchParameterTypeEnum.HAS);
		ourParamTypes.put(HasOrListParam.class, RestSearchParameterTypeEnum.HAS);
		ourParamTypes.put(HasAndListParam.class, RestSearchParameterTypeEnum.HAS);
	}

	private List<Class<? extends IQueryParameterType>> myCompositeTypes = Collections.emptyList();
	private List<Class<? extends IBaseResource>> myDeclaredTypes;
	private String myName;
	private IParamBinder<?> myParamBinder;
	private RestSearchParameterTypeEnum myParamType;
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
		ArrayList<QualifiedParamList> retVal = new ArrayList<>();

		// TODO: declaring method should probably have a generic type..
		@SuppressWarnings("rawtypes")
		IParamBinder paramBinder = myParamBinder;

		@SuppressWarnings("unchecked")
		List<IQueryParameterOr<?>> val = paramBinder.encode(theContext, theObject);
		for (IQueryParameterOr<?> nextOr : val) {
			retVal.add(new QualifiedParamList(nextOr, theContext));
		}

		return retVal;
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

	public Class<?> getType() {
		return myType;
	}

	@Override
	public boolean isRequired() {
		return myRequired;
	}

	public void setChainlists(String[] theChainWhitelist) {
		myQualifierWhitelist = new HashSet<>(theChainWhitelist.length);
		myQualifierWhitelist.add(QUALIFIER_ANY_TYPE);

		for (String chain : theChainWhitelist) {
			if (chain.equals(OptionalParam.ALLOW_CHAIN_ANY)) {
				myQualifierWhitelist.add('.' + OptionalParam.ALLOW_CHAIN_ANY);
			} else if (chain.equals(EMPTY_STRING)) {
				myQualifierWhitelist.add(".");
			} else {
				myQualifierWhitelist.add('.' + chain);
			}
		}
	}

	public void setCompositeTypes(Class<? extends IQueryParameterType>[] theCompositeTypes) {
		myCompositeTypes = Arrays.asList(theCompositeTypes);
	}

	public void setDeclaredTypes(Class<? extends IBaseResource>[] theTypes) {
		myDeclaredTypes = Arrays.asList(theTypes);
	}

	public void setName(String name) {
		this.myName = name;
	}

	public void setRequired(boolean required) {
		this.myRequired = required;
	}

	@SuppressWarnings("unchecked")
	public void setType(FhirContext theContext, final Class<?> type, Class<? extends Collection<?>> theInnerCollectionType, Class<? extends Collection<?>> theOuterCollectionType) {

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
		} else if (Date.class.equals(type)) {
			myParamBinder = new DateBinder();
			myParamType = RestSearchParameterTypeEnum.DATE;
		} else if (Calendar.class.equals(type)) {
			myParamBinder = new CalendarBinder();
			myParamType = RestSearchParameterTypeEnum.DATE;
		} else if (IPrimitiveType.class.isAssignableFrom(type) && ReflectionUtil.isInstantiable(type)) {
			RuntimePrimitiveDatatypeDefinition def = (RuntimePrimitiveDatatypeDefinition) theContext.getElementDefinition((Class<? extends IPrimitiveType<?>>) type);
			if (def.getNativeType() != null) {
				if (def.getNativeType().equals(Date.class)) {
					myParamBinder = new FhirPrimitiveBinder((Class<IPrimitiveType<?>>) type);
					myParamType = RestSearchParameterTypeEnum.DATE;
				} else if (def.getNativeType().equals(String.class)) {
					myParamBinder = new FhirPrimitiveBinder((Class<IPrimitiveType<?>>) type);
					myParamType = RestSearchParameterTypeEnum.STRING;
				}
			}
		} else {
			throw new ConfigurationException(Msg.code(1406) + "Unsupported data type for parameter: " + type.getCanonicalName());
		}

		RestSearchParameterTypeEnum typeEnum = ourParamTypes.get(type);
		if (typeEnum != null) {
			Set<String> builtInQualifiers = ourParamQualifiers.get(typeEnum);
			if (builtInQualifiers != null) {
				if (myQualifierWhitelist != null) {
					HashSet<String> qualifierWhitelist = new HashSet<>();
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
		} else if (BaseIdentifierDt.class.isAssignableFrom(type)) {
			myParamType = RestSearchParameterTypeEnum.TOKEN;
		} else if (BaseQuantityDt.class.isAssignableFrom(type)) {
			myParamType = RestSearchParameterTypeEnum.QUANTITY;
		} else if (ReferenceParam.class.isAssignableFrom(type)) {
			myParamType = RestSearchParameterTypeEnum.REFERENCE;
		} else if (HasParam.class.isAssignableFrom(type)) {
			myParamType = RestSearchParameterTypeEnum.STRING;
		} else {
			throw new ConfigurationException(Msg.code(1407) + "Unknown search parameter type: " + type);
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
