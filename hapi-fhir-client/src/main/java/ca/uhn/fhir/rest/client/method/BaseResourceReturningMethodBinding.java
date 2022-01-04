package ca.uhn.fhir.rest.client.method;

/*-
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
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.valueset.BundleTypeEnum;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.exceptions.InvalidResponseException;
import ca.uhn.fhir.util.BundleUtil;
import ca.uhn.fhir.util.ReflectionUtil;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class BaseResourceReturningMethodBinding extends BaseMethodBinding<Object> {
	protected static final Set<String> ALLOWED_PARAMS;
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(BaseResourceReturningMethodBinding.class);

	static {
		HashSet<String> set = new HashSet<String>();
		set.add(Constants.PARAM_FORMAT);
		set.add(Constants.PARAM_NARRATIVE);
		set.add(Constants.PARAM_PRETTY);
		set.add(Constants.PARAM_SORT);
		set.add(Constants.PARAM_SORT_ASC);
		set.add(Constants.PARAM_SORT_DESC);
		set.add(Constants.PARAM_COUNT);
		set.add(Constants.PARAM_OFFSET);
		set.add(Constants.PARAM_SUMMARY);
		set.add(Constants.PARAM_ELEMENTS);
		ALLOWED_PARAMS = Collections.unmodifiableSet(set);
	}

	private MethodReturnTypeEnum myMethodReturnType;
	private Class<?> myResourceListCollectionType;
	private String myResourceName;
	private Class<? extends IBaseResource> myResourceType;
	private List<Class<? extends IBaseResource>> myPreferTypesList;

	@SuppressWarnings("unchecked")
	public BaseResourceReturningMethodBinding(Class<?> theReturnResourceType, Method theMethod, FhirContext theContext, Object theProvider) {
		super(theMethod, theContext, theProvider);

		Class<?> methodReturnType = theMethod.getReturnType();
		if (Collection.class.isAssignableFrom(methodReturnType)) {

			myMethodReturnType = MethodReturnTypeEnum.LIST_OF_RESOURCES;
			Class<?> collectionType = ReflectionUtil.getGenericCollectionTypeOfMethodReturnType(theMethod);
			if (collectionType != null) {
				if (!Object.class.equals(collectionType) && !IBaseResource.class.isAssignableFrom(collectionType)) {
					throw new ConfigurationException(Msg.code(1458) + "Method " + theMethod.getDeclaringClass().getSimpleName() + "#" + theMethod.getName() + " returns an invalid collection generic type: " + collectionType);
				}
			}
			myResourceListCollectionType = collectionType;

		} else if (IBaseResource.class.isAssignableFrom(methodReturnType)) {
			if (Modifier.isAbstract(methodReturnType.getModifiers()) == false && theContext.getResourceDefinition((Class<? extends IBaseResource>) methodReturnType).isBundle()) {
				myMethodReturnType = MethodReturnTypeEnum.BUNDLE_RESOURCE;
			} else {
				myMethodReturnType = MethodReturnTypeEnum.RESOURCE;
			}
		} else if (MethodOutcome.class.isAssignableFrom(methodReturnType)) {
			myMethodReturnType = MethodReturnTypeEnum.METHOD_OUTCOME;
		} else {
			throw new ConfigurationException(Msg.code(1459) + "Invalid return type '" + methodReturnType.getCanonicalName() + "' on method '" + theMethod.getName() + "' on type: " + theMethod.getDeclaringClass().getCanonicalName());
		}

		if (theReturnResourceType != null) {
			if (IBaseResource.class.isAssignableFrom(theReturnResourceType)) {
				if (Modifier.isAbstract(theReturnResourceType.getModifiers()) || Modifier.isInterface(theReturnResourceType.getModifiers())) {
					// If we're returning an abstract type, that's ok
				} else {
					myResourceType = (Class<? extends IResource>) theReturnResourceType;
					myResourceName = theContext.getResourceType(myResourceType);
				}
			}
		}

		myPreferTypesList = createPreferTypesList();
	}

	public MethodReturnTypeEnum getMethodReturnType() {
		return myMethodReturnType;
	}

	@Override
	public String getResourceName() {
		return myResourceName;
	}

	/**
	 * If the response is a bundle, this type will be placed in the root of the bundle (can be null)
	 */
	protected abstract BundleTypeEnum getResponseBundleType();

	public abstract ReturnTypeEnum getReturnType();

	@Override
	public Object invokeClient(String theResponseMimeType, InputStream theResponseInputStream, int theResponseStatusCode, Map<String, List<String>> theHeaders) throws IOException {
		
		if (Constants.STATUS_HTTP_204_NO_CONTENT == theResponseStatusCode) {
			return toReturnType(null);
		}
		
		IParser parser = createAppropriateParserForParsingResponse(theResponseMimeType, theResponseInputStream, theResponseStatusCode, myPreferTypesList);

		switch (getReturnType()) {
		case BUNDLE: {

			IBaseBundle bundle;
			List<? extends IBaseResource> listOfResources;
			Class<? extends IBaseResource> type = getContext().getResourceDefinition("Bundle").getImplementingClass();
			bundle = (IBaseBundle) parser.parseResource(type, theResponseInputStream);
			listOfResources = BundleUtil.toListOfResources(getContext(), bundle);

			switch (getMethodReturnType()) {
			case BUNDLE_RESOURCE:
				return bundle;
			case LIST_OF_RESOURCES:
				if (myResourceListCollectionType != null) {
					for (Iterator<? extends IBaseResource> iter = listOfResources.iterator(); iter.hasNext();) {
						IBaseResource next = iter.next();
						if (!myResourceListCollectionType.isAssignableFrom(next.getClass())) {
							ourLog.debug("Not returning resource of type {} because it is not a subclass or instance of {}", next.getClass(), myResourceListCollectionType);
							iter.remove();
						}
					}
				}
				return listOfResources;
			case RESOURCE:
				List<IBaseResource> list = BundleUtil.toListOfResources(getContext(), bundle);
				if (list.size() == 0) {
					return null;
				} else if (list.size() == 1) {
					return list.get(0);
				} else {
					throw new InvalidResponseException(Msg.code(1460) + "FHIR server call returned a bundle with multiple resources, but this method is only able to returns one.", theResponseStatusCode);
				}
			default:
				break;
			}
			break;
		}
		case RESOURCE: {
			IBaseResource resource;
			if (myResourceType != null) {
				resource = parser.parseResource(myResourceType, theResponseInputStream);
			} else {
				resource = parser.parseResource(theResponseInputStream);
			}

			MethodUtil.parseClientRequestResourceHeaders(null, theHeaders, resource);

			return toReturnType(resource);
		}
		}

		throw new IllegalStateException(Msg.code(1461) + "Should not get here!");
	}

	private Object toReturnType(IBaseResource resource) {
		Object retVal = null;
		
		switch (getMethodReturnType()) {
		case LIST_OF_RESOURCES:
			retVal = Collections.emptyList();
			if (resource != null) {
				retVal = Collections.singletonList(resource);
			}
			break;
		case RESOURCE:
			retVal = resource;
			break;
		case BUNDLE_RESOURCE:
			retVal = resource;
			break;
		case METHOD_OUTCOME:
			MethodOutcome outcome = new MethodOutcome();
			outcome.setOperationOutcome((IBaseOperationOutcome) resource);
			retVal = outcome;
			break;
		}
		return retVal;
	}

	@SuppressWarnings("unchecked")
	private List<Class<? extends IBaseResource>> createPreferTypesList() {
		List<Class<? extends IBaseResource>> preferTypes = null;
		if (myResourceType != null && !BaseMethodBinding.isResourceInterface(myResourceType)) {
			preferTypes = new ArrayList<Class<? extends IBaseResource>>(1);
			preferTypes.add(myResourceType);
		} else if (myResourceListCollectionType != null && IBaseResource.class.isAssignableFrom(myResourceListCollectionType) && !BaseMethodBinding.isResourceInterface(myResourceListCollectionType)) {
			preferTypes = new ArrayList<Class<? extends IBaseResource>>(1);
			preferTypes.add((Class<? extends IBaseResource>) myResourceListCollectionType);
		}
		return preferTypes;
	}

	/**
	 * Should the response include a Content-Location header. Search method bunding (and any others?) may override this to disable the content-location, since it doesn't make sense
	 */
	protected boolean isAddContentLocationHeader() {
		return true;
	}

	protected void setResourceName(String theResourceName) {
		myResourceName = theResourceName;
	}

	public enum MethodReturnTypeEnum {
		BUNDLE_RESOURCE,
		LIST_OF_RESOURCES,
		METHOD_OUTCOME,
		RESOURCE
	}

	public static class ResourceOrDstu1Bundle {

		private final IBaseResource myResource;

		public ResourceOrDstu1Bundle(IBaseResource theResource) {
			myResource = theResource;
		}

		public IBaseResource getResource() {
			return myResource;
		}

	}

	public enum ReturnTypeEnum {
		BUNDLE,
		RESOURCE
	}

}
