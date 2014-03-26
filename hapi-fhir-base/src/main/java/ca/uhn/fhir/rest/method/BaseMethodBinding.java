package ca.uhn.fhir.rest.method;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.dstu.valueset.RestfulOperationSystemEnum;
import ca.uhn.fhir.model.dstu.valueset.RestfulOperationTypeEnum;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.annotation.Metadata;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.client.GetClientInvocation;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;

public abstract class BaseMethodBinding {

	private String myResourceName;
	private MethodReturnTypeEnum myMethodReturnType;

	public BaseMethodBinding(MethodReturnTypeEnum theMethodReturnType, Class<? extends IResource> theReturnResourceType) {
		ResourceDef resourceDefAnnotation = theReturnResourceType.getAnnotation(ResourceDef.class);
		if (resourceDefAnnotation == null) {
			throw new ConfigurationException(theReturnResourceType.getCanonicalName() + " has no @" + ResourceDef.class.getSimpleName() + " annotation");
		}
		myResourceName = resourceDefAnnotation.name();
		myMethodReturnType = theMethodReturnType;
	}

	public abstract ReturnTypeEnum getReturnType();

	public abstract GetClientInvocation invokeClient(Object[] theArgs) throws InternalErrorException;

	public abstract List<IResource> invokeServer(IResourceProvider theResourceProvider, IdDt theId, IdDt theVersionId, Map<String, String[]> theParameterValues) throws InvalidRequestException, InternalErrorException;

	public abstract RestfulOperationTypeEnum getResourceOperationType();
	
	public abstract RestfulOperationSystemEnum getSystemOperationType();
	
	public abstract boolean matches(Request theRequest);

	public String getResourceName() {
		return myResourceName;
	}

	public static BaseMethodBinding bindMethod(Class<? extends IResource> theReturnType, Method theMethod) {
		Read read = theMethod.getAnnotation(Read.class);
		Search search = theMethod.getAnnotation(Search.class);
		Metadata conformance = theMethod.getAnnotation(Metadata.class);
		if (!verifyMethodHasZeroOrOneOperationAnnotation(theMethod, read, search, conformance)) {
			return null;
		}

		Class<?> methodReturnType = theMethod.getReturnType();
		MethodReturnTypeEnum methodReturnTypeEnum;
		if (Collection.class.isAssignableFrom(methodReturnType)) {
			methodReturnTypeEnum = MethodReturnTypeEnum.LIST_OF_RESOURCES;
		} else if (IResource.class.isAssignableFrom(methodReturnType)) {
			methodReturnTypeEnum = MethodReturnTypeEnum.RESOURCE;
		} else if (Bundle.class.isAssignableFrom(methodReturnType)) {
			methodReturnTypeEnum = MethodReturnTypeEnum.BUNDLE;
		} else {
			throw new ConfigurationException("Invalid return type '" + methodReturnType.getCanonicalName() + "' on method '" + theMethod.getName() + "' on type: " + theMethod.getDeclaringClass().getCanonicalName());
		}

		Class<? extends IResource> returnType = theReturnType;
		if (returnType == null) {
			if (read != null) {
				returnType = read.type();
			} else if (search != null) {
				returnType = search.type();
			}

			if (returnType == IResource.class) {
				throw new ConfigurationException("Could not determine return type for method '" + theMethod.getName() + "'. Try explicitly specifying one in the operation annotation.");
			}
		}
		
		if (read != null) {
			return new ReadMethodBinding(methodReturnTypeEnum, returnType, theMethod);
		} else if (search != null) {
			String queryName = search.queryName();
			return new SearchMethodBinding(methodReturnTypeEnum, returnType, theMethod, queryName);
		} else if (conformance != null) {
			return new ConformanceMethodBinding(methodReturnTypeEnum, returnType, theMethod);
		} else {
			throw new ConfigurationException("Did not detect any FHIR annotations on method '" + theMethod.getName() + "' on type: " + theMethod.getDeclaringClass().getCanonicalName());
		}

		// // each operation name must have a request type annotation and be
		// unique
		// if (null != read) {
		// return rm;
		// }
		//
		// SearchMethodBinding sm = new SearchMethodBinding();
		// if (null != search) {
		// sm.setRequestType(SearchMethodBinding.RequestType.GET);
		// } else if (null != theMethod.getAnnotation(PUT.class)) {
		// sm.setRequestType(SearchMethodBinding.RequestType.PUT);
		// } else if (null != theMethod.getAnnotation(POST.class)) {
		// sm.setRequestType(SearchMethodBinding.RequestType.POST);
		// } else if (null != theMethod.getAnnotation(DELETE.class)) {
		// sm.setRequestType(SearchMethodBinding.RequestType.DELETE);
		// } else {
		// return null;
		// }
		//
		// return sm;
	}

	

	public static boolean verifyMethodHasZeroOrOneOperationAnnotation(Method theNextMethod, Object... theAnnotations) {
		Object obj1 = null;
		for (Object object : theAnnotations) {
			if (object != null) {
				if (obj1 == null) {
					obj1 = object;
				} else {
					throw new ConfigurationException("Method " + theNextMethod.getName() + " on type '" + theNextMethod.getDeclaringClass().getSimpleName() + " has annotations @" + obj1.getClass().getSimpleName() + " and @" + object.getClass().getSimpleName()
							+ ". Can not have both.");
				}

			}
		}
		if (obj1 == null) {
			return false;
			// throw new ConfigurationException("Method '" +
			// theNextMethod.getName() + "' on type '" +
			// theNextMethod.getDeclaringClass().getSimpleName() +
			// " has no FHIR method annotations.");
		}
		return true;
	}

	protected static List<IResource> toResourceList(Object response) throws InternalErrorException {
		if (response == null) {
			return Collections.emptyList();
		} else if (response instanceof IResource) {
			return Collections.singletonList((IResource) response);
		} else if (response instanceof Collection) {
			List<IResource> retVal = new ArrayList<IResource>();
			for (Object next : ((Collection<?>) response)) {
				retVal.add((IResource) next);
			}
			return retVal;
		} else {
			throw new InternalErrorException("Unexpected return type: " + response.getClass().getCanonicalName());
		}
	}

	public enum MethodReturnTypeEnum {
		RESOURCE, BUNDLE, LIST_OF_RESOURCES
	}

	public enum ReturnTypeEnum {
		BUNDLE, RESOURCE
	}

	public MethodReturnTypeEnum getMethodReturnType() {
		return myMethodReturnType;
	}
}
