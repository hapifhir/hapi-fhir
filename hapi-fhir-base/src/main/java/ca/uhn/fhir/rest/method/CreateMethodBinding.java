package ca.uhn.fhir.rest.method;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu.resource.OperationOutcome;
import ca.uhn.fhir.model.dstu.valueset.RestfulOperationSystemEnum;
import ca.uhn.fhir.model.dstu.valueset.RestfulOperationTypeEnum;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.annotation.Create;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.BaseClientInvocation;
import ca.uhn.fhir.rest.client.PostClientInvocation;
import ca.uhn.fhir.rest.method.SearchMethodBinding.RequestType;
import ca.uhn.fhir.rest.param.IParameter;
import ca.uhn.fhir.rest.param.ResourceParameter;
import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.rest.server.EncodingUtil;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.exceptions.UnclassifiedServerFailureException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;

public class CreateMethodBinding extends BaseMethodBinding {

	private static Set<String> ALLOWED_PARAMS;
	static {
		HashSet<String> set = new HashSet<String>();
		set.add(Constants.PARAM_FORMAT);
		ALLOWED_PARAMS = Collections.unmodifiableSet(set);
	}

	private int myResourceParameterIndex;
	private List<IParameter> myParameters;
	private String myResourceName;

	public CreateMethodBinding(Method theMethod, FhirContext theContext) {
		super(theMethod, theContext);
		myParameters = Util.getResourceParameters(theMethod);
		ResourceParameter resourceParameter = null;

		int index = 0;
		for (IParameter next : myParameters) {
			if (next instanceof ResourceParameter) {
				resourceParameter = (ResourceParameter) next;
				myResourceName = theContext.getResourceDefinition(resourceParameter.getResourceType()).getName();
				myResourceParameterIndex = index;
				index++;
			}
		}

		if (resourceParameter == null) {
			throw new ConfigurationException("Method " + theMethod.getName() + " in type " + theMethod.getDeclaringClass().getCanonicalName() + " does not have a parameter annotated with @"
					+ ResourceParam.class.getSimpleName());
		}

		if (!theMethod.getReturnType().equals(MethodOutcome.class)) {
			throw new ConfigurationException("Method " + theMethod.getName() + " in type " + theMethod.getDeclaringClass().getCanonicalName() + " is a @" + Create.class.getSimpleName()
					+ " method but it does not return " + MethodOutcome.class);
		}

	}

	@Override
	public RestfulOperationTypeEnum getResourceOperationType() {
		return RestfulOperationTypeEnum.CREATE;
	}

	@Override
	public RestfulOperationSystemEnum getSystemOperationType() {
		return null;
	}

	@Override
	public BaseClientInvocation invokeClient(Object[] theArgs) throws InternalErrorException {
		IResource resource = (IResource) theArgs[myResourceParameterIndex];
		if (resource == null) {
			throw new NullPointerException("Resource can not be null");
		}

		RuntimeResourceDefinition def = getContext().getResourceDefinition(resource);

		StringBuilder urlExtension = new StringBuilder();
		urlExtension.append(def.getName());

		return new PostClientInvocation(getContext(), resource, urlExtension.toString());
	}

	@Override
	public Object invokeClient(String theResponseMimeType, Reader theResponseReader, int theResponseStatusCode,Map<String, List<String>> theHeaders) throws IOException, BaseServerResponseException {
		switch (theResponseStatusCode) {
		case 400:
			throw new InvalidRequestException("Server responded with: " + IOUtils.toString(theResponseReader));
		case 404:
			throw new ResourceNotFoundException("Server responded with: " + IOUtils.toString(theResponseReader));
		case 422:
			IParser parser = createAppropriateParser(theResponseMimeType, theResponseReader, theResponseStatusCode);
			OperationOutcome operationOutcome = parser.parseResource(OperationOutcome.class, theResponseReader);
			throw new UnprocessableEntityException(operationOutcome);
		case 200: // Support 200 just to be lenient
		case 201: // but it should be 201
			MethodOutcome retVal = new MethodOutcome();
			List<String> locationHeaders = theHeaders.get("location");
			if (locationHeaders != null && locationHeaders.size() > 0) {
				String h = locationHeaders.get(0);
				String resourceNamePart = "/" + myResourceName + "/";
				int resourceIndex = h.lastIndexOf(resourceNamePart);
				if (resourceIndex > -1) {
					int idIndexStart = resourceIndex + resourceNamePart.length();
					int idIndexEnd = h.indexOf('/', idIndexStart);
					if (idIndexEnd == -1) {
						retVal.setId(new IdDt(h.substring(idIndexStart)));
					}else {
						retVal.setId(new IdDt(h.substring(idIndexStart, idIndexEnd)));
						String versionIdPart = "/_history/";
						int historyIdStart = h.indexOf(versionIdPart, idIndexEnd);
						if (historyIdStart != -1) {
							retVal.setVersionId(new IdDt(h.substring(historyIdStart + versionIdPart.length())));
						}
					}
				}
			}
			return retVal;
		default:
			throw new UnclassifiedServerFailureException(theResponseStatusCode, IOUtils.toString(theResponseReader));
		}
	
	}
	
	@Override
	public void invokeServer(RestfulServer theServer, Request theRequest, HttpServletResponse theResponse) throws BaseServerResponseException, IOException {
		EncodingUtil encoding = determineResponseEncoding(theRequest.getServletRequest(), theRequest.getParameters());
		IParser parser = encoding.newParser(getContext());
		IResource resource = parser.parseResource(theRequest.getInputReader());

		Object[] params = new Object[myParameters.size()];
		for (int i = 0; i < myParameters.size(); i++) {
			IParameter param = myParameters.get(i);
			params[i] = param.translateQueryParametersIntoServerArgument(theRequest.getParameters(), resource);
		}

		MethodOutcome response;
		try {
			response = (MethodOutcome) this.getMethod().invoke(theRequest.getResourceProvider(), params);
		} catch (IllegalAccessException e) {
			throw new InternalErrorException(e);
		} catch (IllegalArgumentException e) {
			throw new InternalErrorException(e);
		} catch (InvocationTargetException e) {
			throw new InternalErrorException(e);
		}

		if (response == null) {
			throw new ConfigurationException("Method " + getMethod().getName() + " in type " + getMethod().getDeclaringClass().getCanonicalName() + " returned null");
		}

		theResponse.setStatus(Constants.STATUS_HTTP_201_CREATED);

		StringBuilder b = new StringBuilder();
		b.append(theRequest.getFhirServerBase());
		b.append('/');
		b.append(myResourceName);
		b.append('/');
		b.append(response.getId().getValue());
		if (response.getVersionId().isEmpty() == false) {
			b.append("/_history/");
			b.append(response.getVersionId().getValue());
		}
		theResponse.addHeader("Location", b.toString());

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
	public boolean matches(Request theRequest) {
		if (theRequest.getRequestType() != RequestType.POST) {
			return false;
		}
		if (!myResourceName.equals(theRequest.getResourceName())) {
			return false;
		}
		if (StringUtils.isNotBlank(theRequest.getOperation())) {
			return false;
		}
		return true;
	}

}
