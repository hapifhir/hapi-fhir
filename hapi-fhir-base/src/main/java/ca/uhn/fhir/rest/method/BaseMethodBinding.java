package ca.uhn.fhir.rest.method;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.Validate;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu.valueset.RestfulOperationSystemEnum;
import ca.uhn.fhir.model.dstu.valueset.RestfulOperationTypeEnum;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.annotation.Create;
import ca.uhn.fhir.rest.annotation.Delete;
import ca.uhn.fhir.rest.annotation.History;
import ca.uhn.fhir.rest.annotation.Metadata;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.annotation.Update;
import ca.uhn.fhir.rest.client.BaseClientInvocation;
import ca.uhn.fhir.rest.client.exceptions.NonFhirResponseException;
import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.rest.server.EncodingUtil;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.util.ReflectionUtil;

public abstract class BaseMethodBinding {

	private Method myMethod;
	private FhirContext myContext;
	private Object myProvider;

	public BaseMethodBinding(Method theMethod, FhirContext theConetxt, Object theProvider) {
		assert theMethod != null;
		assert theConetxt != null;

		myMethod = theMethod;
		myContext = theConetxt;
		myProvider = theProvider;
	}

	public Object getProvider() {
		return myProvider;
	}

	public FhirContext getContext() {
		return myContext;
	}

	public Method getMethod() {
		return myMethod;
	}

	public abstract BaseClientInvocation invokeClient(Object[] theArgs) throws InternalErrorException;

	public abstract RestfulOperationTypeEnum getResourceOperationType();

	public abstract RestfulOperationSystemEnum getSystemOperationType();

	public abstract boolean matches(Request theRequest);

	protected IParser createAppropriateParser(String theResponseMimeType, Reader theResponseReader, int theResponseStatusCode) throws IOException {
		IParser parser;
		if (Constants.CT_ATOM_XML.equals(theResponseMimeType)) {
			parser = getContext().newXmlParser();
		} else if (Constants.CT_FHIR_XML.equals(theResponseMimeType)) {
			parser = getContext().newXmlParser();
		} else {
			throw new NonFhirResponseException("Response contains non-FHIR content-type: " + theResponseMimeType, theResponseMimeType, theResponseStatusCode, IOUtils.toString(theResponseReader));
		}
		return parser;
	}

	public static BaseMethodBinding bindMethod(Method theMethod, FhirContext theContext, Object theProvider) {
		Read read = theMethod.getAnnotation(Read.class);
		Search search = theMethod.getAnnotation(Search.class);
		Metadata conformance = theMethod.getAnnotation(Metadata.class);
		Create create = theMethod.getAnnotation(Create.class);
		Update update = theMethod.getAnnotation(Update.class);
		Delete delete = theMethod.getAnnotation(Delete.class);
		History history = theMethod.getAnnotation(History.class);
		// ** if you add another annotation above, also add it to the next line:
		if (!verifyMethodHasZeroOrOneOperationAnnotation(theMethod, read, search, conformance, create, update, delete, history)) {
			return null;
		}

		Class<? extends IResource> returnTypeFromRp = null;
		if (theProvider instanceof IResourceProvider) {
			returnTypeFromRp = ((IResourceProvider) theProvider).getResourceType();
			if (!verifyIsValidResourceReturnType(returnTypeFromRp)) {
				throw new ConfigurationException("getResourceType() from " + IResourceProvider.class.getSimpleName() + " type " + theMethod.getDeclaringClass().getCanonicalName() + " returned " + toLogString(returnTypeFromRp) + " - Must return a resource type");
			}
		}
		
		Class<?> returnTypeFromMethod = theMethod.getReturnType();
		if (Collection.class.isAssignableFrom(returnTypeFromMethod)) {
			returnTypeFromMethod = ReflectionUtil.getGenericCollectionTypeOfMethodReturnType(theMethod);
		}

		
		if (Bundle.class.isAssignableFrom(returnTypeFromMethod)) {
			returnTypeFromMethod = returnTypeFromRp;
		}
		if (!IResource.class.isAssignableFrom(returnTypeFromMethod)) {
			throw new ConfigurationException("Method '" + theMethod.getName() + "' in type " + theMethod.getDeclaringClass().getCanonicalName() + " returns type " + returnTypeFromMethod.getCanonicalName() + " - Must return a resource type");
		}

		if (returnTypeFromRp != null) {
			if (returnTypeFromRp.equals(returnTypeFromMethod) == false) {
				throw new ConfigurationException("Method '" + theMethod.getName() + "' in type " + theMethod.getDeclaringClass().getCanonicalName() + " returns type " + returnTypeFromMethod.getCanonicalName() + " but must return " + returnTypeFromRp.getCanonicalName());
			}
		}
		
		if (returnTypeFromMethod == null) {
			if (read != null) {
				returnTypeFromMethod = read.type();
			} else if (search != null) {
				returnTypeFromMethod = search.type();
			}

			if (returnTypeFromMethod == IResource.class) {
				throw new ConfigurationException("Could not determine return type for method '" + theMethod.getName() + "'. Try explicitly specifying one in the operation annotation.");
			}
		}
		
		@SuppressWarnings("unchecked")
		Class<? extends IResource> retType = (Class<? extends IResource>) returnTypeFromMethod;

		if (read != null) {
			return new ReadMethodBinding(retType, theMethod, theContext, theProvider);
		} else if (search != null) {
			String queryName = search.queryName();
			return new SearchMethodBinding(retType, theMethod, queryName, theContext, theProvider);
		} else if (conformance != null) {
			return new ConformanceMethodBinding(theMethod, theContext, theProvider);
		} else if (create != null) {
			return new CreateMethodBinding(theMethod, theContext, theProvider);
		} else if (update != null) {
			return new UpdateMethodBinding(theMethod, theContext, theProvider);
		} else if (delete != null) {
			return new DeleteMethodBinding(theMethod, theContext, theProvider);
		} else if (history != null) {
			return new HistoryMethodBinding(theMethod, theContext, theProvider);
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

	private static boolean verifyIsValidResourceReturnType(Class<?> theReturnType) {
		if (theReturnType==null) {
			return false;
		}
		if (!IResource.class.isAssignableFrom(theReturnType)) {
			return false;
		}
	}

	private static String toLogString(Class<? extends IResource> theType) {
		if (theType==null) {
			return null;
		}
		return theType.getCanonicalName();
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

	public static EncodingUtil determineResponseEncoding(HttpServletRequest theRequest, Map<String, String[]> theParams) {
		String[] format = theParams.remove(Constants.PARAM_FORMAT);
		if (format != null) {
			for (String nextFormat : format) {
				EncodingUtil retVal = Constants.FORMAT_VAL_TO_ENCODING.get(nextFormat);
				if (retVal != null) {
					return retVal;
				}
			}
		}

		Enumeration<String> acceptValues = theRequest.getHeaders("Accept");
		if (acceptValues != null) {
			while (acceptValues.hasMoreElements()) {
				EncodingUtil retVal = Constants.FORMAT_VAL_TO_ENCODING.get(acceptValues.nextElement());
				if (retVal != null) {
					return retVal;
				}
			}
		}
		return EncodingUtil.XML;
	}

	public abstract void invokeServer(RestfulServer theServer, Request theRequest, HttpServletResponse theResponse) throws BaseServerResponseException, IOException;

	public abstract Object invokeClient(String theResponseMimeType, Reader theResponseReader, int theResponseStatusCode, Map<String, List<String>> theHeaders) throws IOException, BaseServerResponseException;

	public static BaseMethodBinding bindSystemMethod(Method theMethod, FhirContext theContext) {
		return bindMethod(null, theMethod, theContext, null);
	}


}
