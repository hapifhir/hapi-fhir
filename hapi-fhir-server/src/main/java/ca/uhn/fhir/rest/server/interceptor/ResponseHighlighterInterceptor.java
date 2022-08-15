package ca.uhn.fhir.rest.server.interceptor;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Interceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.api.RequestTypeEnum;
import ca.uhn.fhir.rest.api.server.IRestfulResponse;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.ResponseDetails;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.RestfulServerUtils;
import ca.uhn.fhir.rest.server.RestfulServerUtils.ResponseEncoding;
import ca.uhn.fhir.rest.server.exceptions.AuthenticationException;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.method.BaseResourceReturningMethodBinding;
import ca.uhn.fhir.util.FhirTerser;
import ca.uhn.fhir.util.StopWatch;
import ca.uhn.fhir.util.UrlUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.hl7.fhir.instance.model.api.IBaseBinary;
import org.hl7.fhir.instance.model.api.IBaseConformance;
import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.defaultString;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.trim;

/*
 * #%L
 * HAPI FHIR - Server Framework
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

/**
 * This interceptor detects when a request is coming from a browser, and automatically returns a response with syntax
 * highlighted (coloured) HTML for the response instead of just returning raw XML/JSON.
 *
 * @since 1.0
 */
@Interceptor
public class ResponseHighlighterInterceptor {

	/**
	 * TODO: As of HAPI 1.6 (2016-06-10) this parameter has been replaced with simply
	 * requesting _format=json or xml so eventually this parameter should be removed
	 */
	public static final String PARAM_RAW = "_raw";
	public static final String PARAM_RAW_TRUE = "true";
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ResponseHighlighterInterceptor.class);
	private static final String[] PARAM_FORMAT_VALUE_JSON = new String[]{Constants.FORMAT_JSON};
	private static final String[] PARAM_FORMAT_VALUE_XML = new String[]{Constants.FORMAT_XML};
	private static final String[] PARAM_FORMAT_VALUE_TTL = new String[]{Constants.FORMAT_TURTLE};
	private boolean myShowRequestHeaders = false;
	private boolean myShowResponseHeaders = true;

	/**
	 * Constructor
	 */
	public ResponseHighlighterInterceptor() {
		super();
	}

	private String createLinkHref(Map<String, String[]> parameters, String formatValue) {
		StringBuilder rawB = new StringBuilder();
		for (String next : parameters.keySet()) {
			if (Constants.PARAM_FORMAT.equals(next)) {
				continue;
			}
			for (String nextValue : parameters.get(next)) {
				if (isBlank(nextValue)) {
					continue;
				}
				if (rawB.length() == 0) {
					rawB.append('?');
				} else {
					rawB.append('&');
				}
				rawB.append(UrlUtil.escapeUrlParam(next));
				rawB.append('=');
				rawB.append(UrlUtil.escapeUrlParam(nextValue));
			}
		}
		if (rawB.length() == 0) {
			rawB.append('?');
		} else {
			rawB.append('&');
		}
		rawB.append(Constants.PARAM_FORMAT).append('=').append(formatValue);

		String link = rawB.toString();
		return link;
	}

	private int format(String theResultBody, StringBuilder theTarget, EncodingEnum theEncodingEnum) {
		String str = StringEscapeUtils.escapeHtml4(theResultBody);
		if (str == null || theEncodingEnum == null) {
			theTarget.append(str);
			return 0;
		}

		theTarget.append("<div id=\"line1\">");

		boolean inValue = false;
		boolean inQuote = false;
		boolean inTag = false;
		boolean inTurtleDirective = false;
		boolean startingLineNext = true;
		boolean startingLine = false;
		int lineCount = 1;

		for (int i = 0; i < str.length(); i++) {
			char prevChar = (i > 0) ? str.charAt(i - 1) : ' ';
			char nextChar = str.charAt(i);
			char nextChar2 = (i + 1) < str.length() ? str.charAt(i + 1) : ' ';
			char nextChar3 = (i + 2) < str.length() ? str.charAt(i + 2) : ' ';
			char nextChar4 = (i + 3) < str.length() ? str.charAt(i + 3) : ' ';
			char nextChar5 = (i + 4) < str.length() ? str.charAt(i + 4) : ' ';
			char nextChar6 = (i + 5) < str.length() ? str.charAt(i + 5) : ' ';

			if (nextChar == '\n') {
				if (inTurtleDirective) {
					theTarget.append("</span>");
					inTurtleDirective = false;
				}
				lineCount++;
				theTarget.append("</div><div id=\"line");
				theTarget.append(lineCount);
				theTarget.append("\" onclick=\"updateHighlightedLineTo('#L");
				theTarget.append(lineCount);
				theTarget.append("');\">");
				startingLineNext = true;
				continue;
			} else if (startingLineNext) {
				startingLineNext = false;
				startingLine = true;
			} else {
				startingLine = false;
			}

			if (theEncodingEnum == EncodingEnum.JSON) {

				if (inQuote) {
					theTarget.append(nextChar);
					if (prevChar != '\\' && nextChar == '&' && nextChar2 == 'q' && nextChar3 == 'u' && nextChar4 == 'o' && nextChar5 == 't' && nextChar6 == ';') {
						theTarget.append("quot;</span>");
						i += 5;
						inQuote = false;
					} else if (nextChar == '\\' && nextChar2 == '"') {
						theTarget.append("quot;</span>");
						i += 5;
						inQuote = false;
					}
				} else {
					if (nextChar == ':') {
						inValue = true;
						theTarget.append(nextChar);
					} else if (nextChar == '[' || nextChar == '{') {
						theTarget.append("<span class='hlControl'>");
						theTarget.append(nextChar);
						theTarget.append("</span>");
						inValue = false;
					} else if (nextChar == '{' || nextChar == '}' || nextChar == ',') {
						theTarget.append("<span class='hlControl'>");
						theTarget.append(nextChar);
						theTarget.append("</span>");
						inValue = false;
					} else if (nextChar == '&' && nextChar2 == 'q' && nextChar3 == 'u' && nextChar4 == 'o' && nextChar5 == 't' && nextChar6 == ';') {
						if (inValue) {
							theTarget.append("<span class='hlQuot'>&quot;");
						} else {
							theTarget.append("<span class='hlTagName'>&quot;");
						}
						inQuote = true;
						i += 5;
					} else if (nextChar == ':') {
						theTarget.append("<span class='hlControl'>");
						theTarget.append(nextChar);
						theTarget.append("</span>");
						inValue = true;
					} else {
						theTarget.append(nextChar);
					}
				}

			} else if (theEncodingEnum == EncodingEnum.RDF) {

				if (inQuote) {
					theTarget.append(nextChar);
					if (prevChar != '\\' && nextChar == '&' && nextChar2 == 'q' && nextChar3 == 'u' && nextChar4 == 'o' && nextChar5 == 't' && nextChar6 == ';') {
						theTarget.append("quot;</span>");
						i += 5;
						inQuote = false;
					} else if (nextChar == '\\' && nextChar2 == '"') {
						theTarget.append("quot;</span>");
						i += 5;
						inQuote = false;
					}
				} else if (startingLine && nextChar == '@') {
					inTurtleDirective = true;
					theTarget.append("<span class='hlTagName'>");
					theTarget.append(nextChar);
				} else if (startingLine) {
					inTurtleDirective = true;
					theTarget.append("<span class='hlTagName'>");
					theTarget.append(nextChar);
				} else if (nextChar == '[' || nextChar == ']' || nextChar == ';' || nextChar == ':') {
					theTarget.append("<span class='hlControl'>");
					theTarget.append(nextChar);
					theTarget.append("</span>");
				} else {
					if (nextChar == '&' && nextChar2 == 'q' && nextChar3 == 'u' && nextChar4 == 'o' && nextChar5 == 't' && nextChar6 == ';') {
						theTarget.append("<span class='hlQuot'>&quot;");
						inQuote = true;
						i += 5;
					} else {
						theTarget.append(nextChar);
					}
				}

			} else {

				// Ok it's XML

				if (inQuote) {
					theTarget.append(nextChar);
					if (nextChar == '&' && nextChar2 == 'q' && nextChar3 == 'u' && nextChar4 == 'o' && nextChar5 == 't' && nextChar6 == ';') {
						theTarget.append("quot;</span>");
						i += 5;
						inQuote = false;
					}
				} else if (inTag) {
					if (nextChar == '&' && nextChar2 == 'g' && nextChar3 == 't' && nextChar4 == ';') {
						theTarget.append("</span><span class='hlControl'>&gt;</span>");
						inTag = false;
						i += 3;
					} else if (nextChar == ' ') {
						theTarget.append("</span><span class='hlAttr'>");
						theTarget.append(nextChar);
					} else if (nextChar == '&' && nextChar2 == 'q' && nextChar3 == 'u' && nextChar4 == 'o' && nextChar5 == 't' && nextChar6 == ';') {
						theTarget.append("<span class='hlQuot'>&quot;");
						inQuote = true;
						i += 5;
					} else {
						theTarget.append(nextChar);
					}
				} else {
					if (nextChar == '&' && nextChar2 == 'l' && nextChar3 == 't' && nextChar4 == ';') {
						theTarget.append("<span class='hlControl'>&lt;</span><span class='hlTagName'>");
						inTag = true;
						i += 3;
					} else {
						theTarget.append(nextChar);
					}
				}
			}
		}

		theTarget.append("</div>");
		return lineCount;
	}

	@Hook(value = Pointcut.SERVER_HANDLE_EXCEPTION, order = InterceptorOrders.RESPONSE_HIGHLIGHTER_INTERCEPTOR)
	public boolean handleException(RequestDetails theRequestDetails, BaseServerResponseException theException, HttpServletRequest theServletRequest, HttpServletResponse theServletResponse) {
		/*
		 * It's not a browser...
		 */
		Set<String> accept = RestfulServerUtils.parseAcceptHeaderAndReturnHighestRankedOptions(theServletRequest);
		if (!accept.contains(Constants.CT_HTML)) {
			return true;
		}

		/*
		 * It's an AJAX request, so no HTML
		 */
		String requestedWith = theServletRequest.getHeader("X-Requested-With");
		if (requestedWith != null) {
			return true;
		}

		/*
		 * Not a GET
		 */
		if (theRequestDetails.getRequestType() != RequestTypeEnum.GET) {
			return true;
		}

		IBaseOperationOutcome oo = theException.getOperationOutcome();
		if (oo == null) {
			return true;
		}

		ResponseDetails responseDetails = new ResponseDetails();
		responseDetails.setResponseResource(oo);
		responseDetails.setResponseCode(theException.getStatusCode());

		BaseResourceReturningMethodBinding.callOutgoingFailureOperationOutcomeHook(theRequestDetails, oo);
		streamResponse(theRequestDetails, theServletResponse, responseDetails.getResponseResource(), null, theServletRequest, responseDetails.getResponseCode());

		return false;
	}

	/**
	 * If set to <code>true</code> (default is <code>false</code>) response will include the
	 * request headers
	 */
	public boolean isShowRequestHeaders() {
		return myShowRequestHeaders;
	}

	/**
	 * If set to <code>true</code> (default is <code>false</code>) response will include the
	 * request headers
	 *
	 * @return Returns a reference to this for easy method chaining
	 */
	@SuppressWarnings("UnusedReturnValue")
	public ResponseHighlighterInterceptor setShowRequestHeaders(boolean theShowRequestHeaders) {
		myShowRequestHeaders = theShowRequestHeaders;
		return this;
	}

	/**
	 * If set to <code>true</code> (default is <code>true</code>) response will include the
	 * response headers
	 */
	public boolean isShowResponseHeaders() {
		return myShowResponseHeaders;
	}

	/**
	 * If set to <code>true</code> (default is <code>true</code>) response will include the
	 * response headers
	 *
	 * @return Returns a reference to this for easy method chaining
	 */
	@SuppressWarnings("UnusedReturnValue")
	public ResponseHighlighterInterceptor setShowResponseHeaders(boolean theShowResponseHeaders) {
		myShowResponseHeaders = theShowResponseHeaders;
		return this;
	}

	@Hook(value = Pointcut.SERVER_OUTGOING_GRAPHQL_RESPONSE, order = InterceptorOrders.RESPONSE_HIGHLIGHTER_INTERCEPTOR)
	public boolean outgoingGraphqlResponse(RequestDetails theRequestDetails, String theRequest, String theResponse, HttpServletRequest theServletRequest, HttpServletResponse theServletResponse)
		throws AuthenticationException {

		/*
		 * Return true here so that we still fire SERVER_OUTGOING_GRAPHQL_RESPONSE!
		 */

		if (handleOutgoingResponse(theRequestDetails, null, theServletRequest, theServletResponse, theResponse, null)) {
			return true;
		}

		theRequestDetails.setAttribute("ResponseHighlighterInterceptorHandled", Boolean.TRUE);

		return true;
	}

	@Hook(value = Pointcut.SERVER_OUTGOING_RESPONSE, order = InterceptorOrders.RESPONSE_HIGHLIGHTER_INTERCEPTOR)
	public boolean outgoingResponse(RequestDetails theRequestDetails, ResponseDetails theResponseObject, HttpServletRequest theServletRequest, HttpServletResponse theServletResponse)
		throws AuthenticationException {

		if (!Boolean.TRUE.equals(theRequestDetails.getAttribute("ResponseHighlighterInterceptorHandled"))) {
			String graphqlResponse = null;
			IBaseResource resourceResponse = theResponseObject.getResponseResource();
			if (handleOutgoingResponse(theRequestDetails, theResponseObject, theServletRequest, theServletResponse, graphqlResponse, resourceResponse)) {
				return true;
			}
		}

		return false;
	}

	@Hook(Pointcut.SERVER_CAPABILITY_STATEMENT_GENERATED)
	public void capabilityStatementGenerated(RequestDetails theRequestDetails, IBaseConformance theCapabilityStatement) {
		FhirTerser terser = theRequestDetails.getFhirContext().newTerser();

		Set<String> formats = terser.getValues(theCapabilityStatement, "format", IPrimitiveType.class)
			.stream()
			.map(t -> t.getValueAsString())
			.collect(Collectors.toSet());
		addFormatConditionally(theCapabilityStatement, terser, formats, Constants.CT_FHIR_JSON_NEW, Constants.FORMATS_HTML_JSON);
		addFormatConditionally(theCapabilityStatement, terser, formats, Constants.CT_FHIR_XML_NEW, Constants.FORMATS_HTML_XML);
		addFormatConditionally(theCapabilityStatement, terser, formats, Constants.CT_RDF_TURTLE, Constants.FORMATS_HTML_TTL);
	}

	private void addFormatConditionally(IBaseConformance theCapabilityStatement, FhirTerser terser, Set<String> formats, String wanted, String toAdd) {
		if (formats.contains(wanted)) {
			terser.addElement(theCapabilityStatement, "format", toAdd);
		}
	}


	private boolean handleOutgoingResponse(RequestDetails theRequestDetails, ResponseDetails theResponseObject, HttpServletRequest theServletRequest, HttpServletResponse theServletResponse, String theGraphqlResponse, IBaseResource theResourceResponse) {
		/*
		 * Request for _raw
		 */
		String[] rawParamValues = theRequestDetails.getParameters().get(PARAM_RAW);
		if (rawParamValues != null && rawParamValues.length > 0 && rawParamValues[0].equals(PARAM_RAW_TRUE)) {
			ourLog.warn("Client is using non-standard/legacy  _raw parameter - Use _format=json or _format=xml instead, as this parmameter will be removed at some point");
			return true;
		}

		boolean force = false;
		String[] formatParams = theRequestDetails.getParameters().get(Constants.PARAM_FORMAT);
		if (formatParams != null && formatParams.length > 0) {
			String formatParam = defaultString(formatParams[0]);
			int semiColonIdx = formatParam.indexOf(';');
			if (semiColonIdx != -1) {
				formatParam = formatParam.substring(0, semiColonIdx);
			}
			formatParam = trim(formatParam);

			if (Constants.FORMATS_HTML.contains(formatParam)) { // this is a set
				force = true;
			} else if (Constants.FORMATS_HTML_XML.equals(formatParam)) {
				force = true;
				theRequestDetails.addParameter(Constants.PARAM_FORMAT, PARAM_FORMAT_VALUE_XML);
			} else if (Constants.FORMATS_HTML_JSON.equals(formatParam)) {
				force = true;
				theRequestDetails.addParameter(Constants.PARAM_FORMAT, PARAM_FORMAT_VALUE_JSON);
			} else if (Constants.FORMATS_HTML_TTL.equals(formatParam)) {
				force = true;
				theRequestDetails.addParameter(Constants.PARAM_FORMAT, PARAM_FORMAT_VALUE_TTL);
			} else {
				return true;
			}
		}

		/*
		 * It's not a browser...
		 */
		Set<String> highestRankedAcceptValues = RestfulServerUtils.parseAcceptHeaderAndReturnHighestRankedOptions(theServletRequest);
		if (!force && highestRankedAcceptValues.contains(Constants.CT_HTML) == false) {
			return true;
		}

		/*
		 * It's an AJAX request, so no HTML
		 */
		if (!force && isNotBlank(theServletRequest.getHeader("X-Requested-With"))) {
			return true;
		}
		/*
		 * If the request has an Origin header, it is probably an AJAX request
		 */
		if (!force && isNotBlank(theServletRequest.getHeader(Constants.HEADER_ORIGIN))) {
			return true;
		}

		/*
		 * Not a GET
		 */
		if (!force && theRequestDetails.getRequestType() != RequestTypeEnum.GET) {
			return true;
		}

		/*
		 * Not binary
		 */
		if (!force && theResponseObject != null && (theResponseObject.getResponseResource() instanceof IBaseBinary)) {
			return true;
		}

		streamResponse(theRequestDetails, theServletResponse, theResourceResponse, theGraphqlResponse, theServletRequest, 200);
		return false;
	}

	private void streamRequestHeaders(ServletRequest theServletRequest, StringBuilder b) {
		if (theServletRequest instanceof HttpServletRequest) {
			HttpServletRequest sr = (HttpServletRequest) theServletRequest;
			b.append("<h1>Request</h1>");
			b.append("<div class=\"headersDiv\">");
			Enumeration<String> headerNamesEnum = sr.getHeaderNames();
			while (headerNamesEnum.hasMoreElements()) {
				String nextHeaderName = headerNamesEnum.nextElement();
				Enumeration<String> headerValuesEnum = sr.getHeaders(nextHeaderName);
				while (headerValuesEnum.hasMoreElements()) {
					String nextHeaderValue = headerValuesEnum.nextElement();
					appendHeader(b, nextHeaderName, nextHeaderValue);
				}
			}
			b.append("</div>");
		}
	}

	private void streamResponse(RequestDetails theRequestDetails, HttpServletResponse theServletResponse, IBaseResource theResource, String theGraphqlResponse, ServletRequest theServletRequest, int theStatusCode) {
		EncodingEnum encoding;
		String encoded;
		Map<String, String[]> parameters = theRequestDetails.getParameters();

		if (isNotBlank(theGraphqlResponse)) {

			encoded = theGraphqlResponse;
			encoding = EncodingEnum.JSON;

		} else {

			IParser p;
			if (parameters.containsKey(Constants.PARAM_FORMAT)) {
				FhirVersionEnum forVersion = theResource.getStructureFhirVersionEnum();
				p = RestfulServerUtils.getNewParser(theRequestDetails.getServer().getFhirContext(), forVersion, theRequestDetails);
			} else {
				EncodingEnum defaultResponseEncoding = theRequestDetails.getServer().getDefaultResponseEncoding();
				p = defaultResponseEncoding.newParser(theRequestDetails.getServer().getFhirContext());
				RestfulServerUtils.configureResponseParser(theRequestDetails, p);
			}

			// This interceptor defaults to pretty printing unless the user
			// has specifically requested us not to
			boolean prettyPrintResponse = true;
			String[] prettyParams = parameters.get(Constants.PARAM_PRETTY);
			if (prettyParams != null && prettyParams.length > 0) {
				if (Constants.PARAM_PRETTY_VALUE_FALSE.equals(prettyParams[0])) {
					prettyPrintResponse = false;
				}
			}
			if (prettyPrintResponse) {
				p.setPrettyPrint(true);
			}

			encoding = p.getEncoding();
			encoded = p.encodeResourceToString(theResource);

		}

		if (theRequestDetails.getServer() instanceof RestfulServer) {
			RestfulServer rs = (RestfulServer) theRequestDetails.getServer();
			rs.addHeadersToResponse(theServletResponse);
		}

		try {

			if (theStatusCode > 299) {
				theServletResponse.setStatus(theStatusCode);
			}
			theServletResponse.setContentType(Constants.CT_HTML_WITH_UTF8);

			StringBuilder outputBuffer = new StringBuilder();
			outputBuffer.append("<html lang=\"en\">\n");
			outputBuffer.append("	<head>\n");
			outputBuffer.append("		<meta charset=\"utf-8\" />\n");
			outputBuffer.append("       <style>\n");
			outputBuffer.append(".httpStatusDiv {");
			outputBuffer.append("  font-size: 1.2em;");
			outputBuffer.append("  font-weight: bold;");
			outputBuffer.append("}");
			outputBuffer.append(".hlQuot { color: #88F; }\n");
			outputBuffer.append(".hlQuot a { text-decoration: underline; text-decoration-color: #CCC; }\n");
			outputBuffer.append(".hlQuot a:HOVER { text-decoration: underline; text-decoration-color: #008; }\n");
			outputBuffer.append(".hlQuot .uuid, .hlQuot .dateTime {\n");
			outputBuffer.append("  user-select: all;\n");
			outputBuffer.append("  -moz-user-select: all;\n");
			outputBuffer.append("  -webkit-user-select: all;\n");
			outputBuffer.append("  -ms-user-select: element;\n");
			outputBuffer.append("}\n");
			outputBuffer.append(".hlAttr {\n");
			outputBuffer.append("  color: #888;\n");
			outputBuffer.append("}\n");
			outputBuffer.append(".hlTagName {\n");
			outputBuffer.append("  color: #006699;\n");
			outputBuffer.append("}\n");
			outputBuffer.append(".hlControl {\n");
			outputBuffer.append("  color: #660000;\n");
			outputBuffer.append("}\n");
			outputBuffer.append(".hlText {\n");
			outputBuffer.append("  color: #000000;\n");
			outputBuffer.append("}\n");
			outputBuffer.append(".hlUrlBase {\n");
			outputBuffer.append("}");
			outputBuffer.append(".headersDiv {\n");
			outputBuffer.append("  padding: 10px;");
			outputBuffer.append("  margin-left: 10px;");
			outputBuffer.append("  border: 1px solid #CCC;");
			outputBuffer.append("  border-radius: 10px;");
			outputBuffer.append("}");
			outputBuffer.append(".headersRow {\n");
			outputBuffer.append("}");
			outputBuffer.append(".headerName {\n");
			outputBuffer.append("  color: #888;\n");
			outputBuffer.append("  font-family: monospace;\n");
			outputBuffer.append("}");
			outputBuffer.append(".headerValue {\n");
			outputBuffer.append("  color: #88F;\n");
			outputBuffer.append("  font-family: monospace;\n");
			outputBuffer.append("}");
			outputBuffer.append(".responseBodyTable {");
			outputBuffer.append("  width: 100%;\n");
			outputBuffer.append("  margin-left: 0px;\n");
			outputBuffer.append("  margin-top: -10px;\n");
			outputBuffer.append("  position: relative;\n");
			outputBuffer.append("}");
			outputBuffer.append(".responseBodyTableFirstColumn {");
			outputBuffer.append("}");
			outputBuffer.append(".responseBodyTableSecondColumn {");
			outputBuffer.append("  position: absolute;\n");
			outputBuffer.append("  margin-left: 70px;\n");
			outputBuffer.append("  vertical-align: top;\n");
			outputBuffer.append("  left: 0px;\n");
			outputBuffer.append("  right: 0px;\n");
			outputBuffer.append("}");
			outputBuffer.append(".responseBodyTableSecondColumn PRE {");
			outputBuffer.append("  margin: 0px;");
			outputBuffer.append("}");
			outputBuffer.append(".sizeInfo {");
			outputBuffer.append("  margin-top: 20px;");
			outputBuffer.append("  font-size: 0.8em;");
			outputBuffer.append("}");
			outputBuffer.append(".lineAnchor A {");
			outputBuffer.append("  text-decoration: none;");
			outputBuffer.append("  padding-left: 20px;");
			outputBuffer.append("}");
			outputBuffer.append(".lineAnchor {");
			outputBuffer.append("  display: block;");
			outputBuffer.append("  padding-right: 20px;");
			outputBuffer.append("}");
			outputBuffer.append(".selectedLine {");
			outputBuffer.append("  background-color: #EEF;");
			outputBuffer.append("  font-weight: bold;");
			outputBuffer.append("}");
			outputBuffer.append("H1 {");
			outputBuffer.append("  font-size: 1.1em;");
			outputBuffer.append("  color: #666;");
			outputBuffer.append("}");
			outputBuffer.append("BODY {\n");
			outputBuffer.append("  font-family: Arial;\n");
			outputBuffer.append("}");
			outputBuffer.append("       </style>\n");
			outputBuffer.append("	</head>\n");
			outputBuffer.append("\n");
			outputBuffer.append("	<body>");

			outputBuffer.append("<p>");

			if (isBlank(theGraphqlResponse)) {
				outputBuffer.append("This result is being rendered in HTML for easy viewing. ");
				outputBuffer.append("You may access this content as ");

				if (theRequestDetails.getFhirContext().isFormatJsonSupported()) {
					outputBuffer.append("<a href=\"");
					outputBuffer.append(createLinkHref(parameters, Constants.FORMAT_JSON));
					outputBuffer.append("\">Raw JSON</a> or ");
				}

				if (theRequestDetails.getFhirContext().isFormatXmlSupported()) {
					outputBuffer.append("<a href=\"");
					outputBuffer.append(createLinkHref(parameters, Constants.FORMAT_XML));
					outputBuffer.append("\">Raw XML</a> or ");
				}

				if (theRequestDetails.getFhirContext().isFormatRdfSupported()) {
					outputBuffer.append("<a href=\"");
					outputBuffer.append(createLinkHref(parameters, Constants.FORMAT_TURTLE));
					outputBuffer.append("\">Raw Turtle</a> or ");
				}

				outputBuffer.append("view this content in ");

				if (theRequestDetails.getFhirContext().isFormatJsonSupported()) {
					outputBuffer.append("<a href=\"");
					outputBuffer.append(createLinkHref(parameters, Constants.FORMATS_HTML_JSON));
					outputBuffer.append("\">HTML JSON</a> ");
				}

				if (theRequestDetails.getFhirContext().isFormatXmlSupported()) {
					outputBuffer.append("or ");
					outputBuffer.append("<a href=\"");
					outputBuffer.append(createLinkHref(parameters, Constants.FORMATS_HTML_XML));
					outputBuffer.append("\">HTML XML</a> ");
				}

				if (theRequestDetails.getFhirContext().isFormatRdfSupported()) {
					outputBuffer.append("or ");
					outputBuffer.append("<a href=\"");
					outputBuffer.append(createLinkHref(parameters, Constants.FORMATS_HTML_TTL));
					outputBuffer.append("\">HTML Turtle</a> ");
				}

				outputBuffer.append(".");
			}

			Date startTime = (Date) theServletRequest.getAttribute(RestfulServer.REQUEST_START_TIME);
			if (startTime != null) {
				long time = System.currentTimeMillis() - startTime.getTime();
				outputBuffer.append(" Response generated in ");
				outputBuffer.append(time);
				outputBuffer.append("ms.");
			}

			outputBuffer.append("</p>");

			outputBuffer.append("\n");

			// status (e.g. HTTP 200 OK)
			String statusName = Constants.HTTP_STATUS_NAMES.get(theServletResponse.getStatus());
			statusName = defaultString(statusName);
			outputBuffer.append("<div class=\"httpStatusDiv\">");
			outputBuffer.append("HTTP ");
			outputBuffer.append(theServletResponse.getStatus());
			outputBuffer.append(" ");
			outputBuffer.append(statusName);
			outputBuffer.append("</div>");

			outputBuffer.append("\n");
			outputBuffer.append("\n");

			try {
				if (isShowRequestHeaders()) {
					streamRequestHeaders(theServletRequest, outputBuffer);
				}
				if (isShowResponseHeaders()) {
					streamResponseHeaders(theRequestDetails, theServletResponse, outputBuffer);
				}
			} catch (Throwable t) {
				// ignore (this will hit if we're running in a servlet 2.5 environment)
			}

			outputBuffer.append("<h1>Response Body</h1>");

			outputBuffer.append("<div class=\"responseBodyTable\">");

			// Response Body
			outputBuffer.append("<div class=\"responseBodyTableSecondColumn\"><pre>");
			StringBuilder target = new StringBuilder();
			int linesCount = format(encoded, target, encoding);
			outputBuffer.append(target);
			outputBuffer.append("</pre></div>");

			// Line Numbers
			outputBuffer.append("<div class=\"responseBodyTableFirstColumn\"><pre>");
			for (int i = 1; i <= linesCount; i++) {
				outputBuffer.append("<div class=\"lineAnchor\" id=\"anchor");
				outputBuffer.append(i);
				outputBuffer.append("\">");

				outputBuffer.append("<a href=\"#L");
				outputBuffer.append(i);
				outputBuffer.append("\" name=\"L");
				outputBuffer.append(i);
				outputBuffer.append("\" id=\"L");
				outputBuffer.append(i);
				outputBuffer.append("\">");
				outputBuffer.append(i);
				outputBuffer.append("</a></div>");
			}
			outputBuffer.append("</div></td>");

			outputBuffer.append("</div>");

			outputBuffer.append("\n");

			InputStream jsStream = ResponseHighlighterInterceptor.class.getResourceAsStream("ResponseHighlighter.js");
			String jsStr = jsStream != null ? IOUtils.toString(jsStream, StandardCharsets.UTF_8) : "console.log('ResponseHighlighterInterceptor: javascript theResource not found')";

			String baseUrl = theRequestDetails.getServerBaseForRequest();

			baseUrl = UrlUtil.sanitizeBaseUrl(baseUrl);

			jsStr = jsStr.replace("FHIR_BASE", baseUrl);
			outputBuffer.append("<script type=\"text/javascript\">");
			outputBuffer.append(jsStr);
			outputBuffer.append("</script>\n");

			StopWatch writeSw = new StopWatch();
			theServletResponse.getWriter().append(outputBuffer);
			theServletResponse.getWriter().flush();

			theServletResponse.getWriter().append("<div class=\"sizeInfo\">");
			theServletResponse.getWriter().append("Wrote ");
			writeLength(theServletResponse, encoded.length());
			theServletResponse.getWriter().append(" (");
			writeLength(theServletResponse, outputBuffer.length());
			theServletResponse.getWriter().append(" total including HTML)");

			theServletResponse.getWriter().append(" in estimated ");
			theServletResponse.getWriter().append(writeSw.toString());
			theServletResponse.getWriter().append("</div>");


			theServletResponse.getWriter().append("</body>");
			theServletResponse.getWriter().append("</html>");

			theServletResponse.getWriter().close();
		} catch (IOException e) {
			throw new InternalErrorException(Msg.code(322) + e);
		}
	}

	private void writeLength(HttpServletResponse theServletResponse, int theLength) throws IOException {
		double kb = ((double) theLength) / FileUtils.ONE_KB;
		if (kb <= 1000) {
			theServletResponse.getWriter().append(String.format("%.1f", kb)).append(" KB");
		} else {
			double mb = kb / 1000;
			theServletResponse.getWriter().append(String.format("%.1f", mb)).append(" MB");
		}
	}

	private void streamResponseHeaders(RequestDetails theRequestDetails, HttpServletResponse theServletResponse, StringBuilder b) {
		if (theServletResponse.getHeaderNames().isEmpty() == false) {
			b.append("<h1>Response Headers</h1>");

			b.append("<div class=\"headersDiv\">");
			for (String nextHeaderName : theServletResponse.getHeaderNames()) {
				for (String nextHeaderValue : theServletResponse.getHeaders(nextHeaderName)) {
					/*
					 * Let's pretend we're returning a FHIR content type even though we're
					 * actually returning an HTML one
					 */
					if (nextHeaderName.equalsIgnoreCase(Constants.HEADER_CONTENT_TYPE)) {
						ResponseEncoding responseEncoding = RestfulServerUtils.determineResponseEncodingNoDefault(theRequestDetails, theRequestDetails.getServer().getDefaultResponseEncoding());
						if (responseEncoding != null && isNotBlank(responseEncoding.getResourceContentType())) {
							nextHeaderValue = responseEncoding.getResourceContentType() + ";charset=utf-8";
						}
					}
					appendHeader(b, nextHeaderName, nextHeaderValue);
				}
			}
			IRestfulResponse response = theRequestDetails.getResponse();
			for (Map.Entry<String, List<String>> next : response.getHeaders().entrySet()) {
				String name = next.getKey();
				for (String nextValue : next.getValue()) {
					appendHeader(b, name, nextValue);
				}
			}

			b.append("</div>");
		}
	}

	private void appendHeader(StringBuilder theBuilder, String theHeaderName, String theHeaderValue) {
		theBuilder.append("<div class=\"headersRow\">");
		theBuilder.append("<span class=\"headerName\">").append(theHeaderName).append(": ").append("</span>");
		theBuilder.append("<span class=\"headerValue\">").append(theHeaderValue).append("</span>");
		theBuilder.append("</div>");
	}

}
