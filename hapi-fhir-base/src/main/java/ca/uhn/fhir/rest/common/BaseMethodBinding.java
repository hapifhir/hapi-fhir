package ca.uhn.fhir.rest.common;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.client.ClientInvocation;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.Resource;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.operations.Search;

public abstract class BaseMethodBinding {

	private String myResourceName;

	public BaseMethodBinding(Class<? extends IResource> theAnnotatedResourceType) {
		ResourceDef resourceDefAnnotation = theAnnotatedResourceType.getAnnotation(ResourceDef.class);
		if (resourceDefAnnotation == null) {
			throw new ConfigurationException(theAnnotatedResourceType.getCanonicalName() + " has no @" + ResourceDef.class.getSimpleName()+ " annotation");
		}
		myResourceName = resourceDefAnnotation.name();
	}

	public abstract ReturnTypeEnum getReturnType();

	public ClientInvocation invokeClient(Object[] theArgs) {
		// TODO Auto-generated method stub
		return null;
	}

	public abstract List<IResource> invokeServer(IResourceProvider theResourceProvider, IdDt theId, IdDt theVersionId, Map<String, String[]> theParameterValues) throws InvalidRequestException, InternalErrorException;

	public abstract boolean matches(String theResourceName, IdDt theId, IdDt theVersion, Set<String> theParameterNames);

	public String getResourceName() {
		return myResourceName;
	}

	public static BaseMethodBinding bindMethod(Method theMethod) {
		Read read = theMethod.getAnnotation(Read.class);
		Search search = theMethod.getAnnotation(Search.class);
		verifyExactlyOneValued(theMethod, read, search);

		Class<? extends IResource> annotatedResourceType;
		if (read != null) {
			annotatedResourceType = read.value();
		} else {
			annotatedResourceType = search.value();
		}

		Class<?> methodReturnType = theMethod.getReturnType();
		
		if (read != null) {
			return new ReadMethodBinding(annotatedResourceType, theMethod);
		} else if (search != null) {
			return new SearchMethodBinding(annotatedResourceType, theMethod);
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

	public static void verifyExactlyOneValued(Method theNextMethod, Object... theAnnotations) {
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
			throw new ConfigurationException("Method " + theNextMethod.getName() + " on type '" + theNextMethod.getDeclaringClass().getSimpleName() + " has no FHIR method annotations.");
		}
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

	public enum ReturnTypeEnum {
		BUNDLE, RESOURCE
	}
}
