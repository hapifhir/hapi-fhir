package ca.uhn.fhir.rest.server.method;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.server.IRestfulServer;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.param.ParameterUtil;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.RestfulServerUtils;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Method;

public class GraphQLMethodBinding extends BaseMethodBinding<String> {

	private final Integer myIdParamIndex;

	public GraphQLMethodBinding(Method theMethod, FhirContext theContext, Object theProvider) {
		super(theMethod, theContext, theProvider);

		myIdParamIndex = ParameterUtil.findIdParameterIndex(theMethod, theContext);
	}

	@Override
	public String getResourceName() {
		return null;
	}

	@Override
	public RestOperationTypeEnum getRestOperationType() {
		return RestOperationTypeEnum.GRAPHQL_REQUEST;
	}

	@Override
	public boolean isGlobalMethod() {
		return true;
	}

	@Override
	public boolean incomingServerRequestMatchesMethod(RequestDetails theRequest) {
		if ("$graphql".equals(theRequest.getOperation())) {
			return true;
		}

		return false;
	}

	@Override
	public Object invokeServer(IRestfulServer<?> theServer, RequestDetails theRequest) throws BaseServerResponseException, IOException {
		Object[] methodParams = createMethodParams(theRequest);
		if (myIdParamIndex != null) {
			methodParams[myIdParamIndex] = theRequest.getId();
		}

		Object response = invokeServerMethod(theServer, theRequest, methodParams);

		int statusCode = Constants.STATUS_HTTP_200_OK;
		String statusMessage = Constants.HTTP_STATUS_NAMES.get(statusCode);
		String contentType = Constants.CT_JSON;
		String charset = Constants.CHARSET_NAME_UTF8;
		boolean respondGzip = theRequest.isRespondGzip();

		Writer writer = theRequest.getResponse().getResponseWriter(statusCode, statusMessage, contentType, charset, respondGzip);

		String responseString = (String) response;
		writer.write(responseString);
		writer.close();

		return null;
	}
}
