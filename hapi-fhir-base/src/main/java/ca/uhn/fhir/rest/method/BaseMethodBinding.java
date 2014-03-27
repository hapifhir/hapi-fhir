package ca.uhn.fhir.rest.method;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu.valueset.RestfulOperationSystemEnum;
import ca.uhn.fhir.model.dstu.valueset.RestfulOperationTypeEnum;
import ca.uhn.fhir.rest.annotation.Metadata;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.client.GetClientInvocation;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;

public abstract class BaseMethodBinding {

	private Method myMethod;
	private FhirContext myContext;

	public BaseMethodBinding(Method theMethod, FhirContext theConetxt) {
		assert theMethod!=null;
		assert theConetxt!=null;
		
		myMethod = theMethod;
		myContext=theConetxt;
	}

	public FhirContext getContext() {
		return myContext;
	}

	public Method getMethod() {
		return myMethod;
	}

	public abstract GetClientInvocation invokeClient(Object[] theArgs) throws InternalErrorException;

	public abstract RestfulOperationTypeEnum getResourceOperationType();

	public abstract RestfulOperationSystemEnum getSystemOperationType();

	public abstract boolean matches(Request theRequest);

	public static BaseMethodBinding bindMethod(Class<? extends IResource> theReturnType, Method theMethod, FhirContext theContext) {
		Read read = theMethod.getAnnotation(Read.class);
		Search search = theMethod.getAnnotation(Search.class);
		Metadata conformance = theMethod.getAnnotation(Metadata.class);
		if (!verifyMethodHasZeroOrOneOperationAnnotation(theMethod, read, search, conformance)) {
			return null;
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
			return new ReadMethodBinding(returnType, theMethod, theContext);
		} else if (search != null) {
			String queryName = search.queryName();
			return new SearchMethodBinding(returnType, theMethod, queryName,theContext);
		} else if (conformance != null) {
			return new ConformanceMethodBinding(theMethod, theContext);
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



	public abstract void invokeServer(RestfulServer theServer, Request theRequest, HttpServletResponse theResponse) throws BaseServerResponseException, IOException;

	public abstract Object invokeClient(String theResponseMimeType, Reader theResponseReader, int theResponseStatusCode) throws IOException;

}
