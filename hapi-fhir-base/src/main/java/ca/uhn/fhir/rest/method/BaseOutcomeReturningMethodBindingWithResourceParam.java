package ca.uhn.fhir.rest.method;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.servlet.http.HttpServletResponse;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.BaseClientInvocation;
import ca.uhn.fhir.rest.param.IParameter;
import ca.uhn.fhir.rest.param.ResourceParameter;
import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.rest.server.EncodingUtil;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;

public abstract class BaseOutcomeReturningMethodBindingWithResourceParam extends BaseOutcomeReturningMethodBinding {
	private int myResourceParameterIndex;
	private String myResourceName;

	public BaseOutcomeReturningMethodBindingWithResourceParam(Method theMethod, FhirContext theContext, Class<?> theMethodAnnotation, Object theProvider) {
		super(theMethod, theContext, theMethodAnnotation, theProvider);
		
		ResourceParameter resourceParameter = null;

		int index = 0;
		for (IParameter next : getParameters()) {
			if (next instanceof ResourceParameter) {
				resourceParameter = (ResourceParameter) next;
				myResourceName = theContext.getResourceDefinition(resourceParameter.getResourceType()).getName();
				myResourceParameterIndex = index;
			}
			index++;
		}

		if (resourceParameter == null) {
			throw new ConfigurationException("Method " + theMethod.getName() + " in type " + theMethod.getDeclaringClass().getCanonicalName() + " does not have a parameter annotated with @"
					+ ResourceParam.class.getSimpleName());
		}

	}

	/**
	 * For subclasses to override
	 */
	@Override
	protected void addParametersForServerRequest(Request theRequest, Object[] theParams) {
		// nothing
	}
	
	@Override
	public void invokeServer(RestfulServer theServer, Request theRequest, HttpServletResponse theResponse) throws BaseServerResponseException, IOException {
		EncodingUtil encoding = BaseMethodBinding.determineResponseEncoding(theRequest.getServletRequest(), theRequest.getParameters());
		IParser parser = encoding.newParser(getContext());
		IResource resource = parser.parseResource(theRequest.getInputReader());

		Object[] params = new Object[getParameters().size()];
		for (int i = 0; i < getParameters().size(); i++) {
			IParameter param = getParameters().get(i);
			if (param == null) {
				continue;
			}
			params[i] = param.translateQueryParametersIntoServerArgument(theRequest.getParameters(), resource);
		}

		addParametersForServerRequest(theRequest, params);

		MethodOutcome response;
		try {
			response = (MethodOutcome) this.getMethod().invoke(getProvider(), params);
		} catch (IllegalAccessException e) {
			throw new InternalErrorException(e);
		} catch (IllegalArgumentException e) {
			throw new InternalErrorException(e);
		} catch (InvocationTargetException e) {
			throw new InternalErrorException(e);
		}

		if (response == null) {
			if (isReturnVoid() == false) {
				throw new ConfigurationException("Method " + getMethod().getName() + " in type " + getMethod().getDeclaringClass().getCanonicalName() + " returned null");
			}
		} else if (!isReturnVoid()) {
			if (response.isCreated()) {
				theResponse.setStatus(Constants.STATUS_HTTP_201_CREATED);
				StringBuilder b = new StringBuilder();
				b.append(theRequest.getFhirServerBase());
				b.append('/');
				b.append(getResourceName());
				b.append('/');
				b.append(response.getId().getValue());
				if (response.getVersionId() != null && response.getVersionId().isEmpty() == false) {
					b.append("/_history/");
					b.append(response.getVersionId().getValue());
				}
				theResponse.addHeader("Location", b.toString());
			} else {
				theResponse.setStatus(Constants.STATUS_HTTP_200_OK);
			}
		} else {
			theResponse.setStatus(Constants.STATUS_HTTP_204_NO_CONTENT);
		}

		theServer.addHapiHeader(theResponse);

		theResponse.setContentType(Constants.CT_TEXT);

		Writer writer = theResponse.getWriter();
		try {
			writer.append("Resource has been created");
		} finally {
			writer.close();
		}
		// getMethod().in
	}

	@Override
	public String getResourceName() {
		return myResourceName;
	}

	@Override
	public BaseClientInvocation invokeClient(Object[] theArgs) throws InternalErrorException {
		IResource resource = (IResource) theArgs[myResourceParameterIndex];
		if (resource == null) {
			throw new NullPointerException("Resource can not be null");
		}

		RuntimeResourceDefinition def = getContext().getResourceDefinition(resource);
		String resourceName = def.getName();

		return createClientInvocation(theArgs, resource, resourceName);
	}

	
}
