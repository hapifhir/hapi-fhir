package ca.uhn.fhir.rest.server.interceptor;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2017 University Health Network
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

import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringEscapeUtils;
import org.hl7.fhir.instance.model.api.IBaseResource;

import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.RequestTypeEnum;
import ca.uhn.fhir.rest.method.RequestDetails;
import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.rest.server.EncodingEnum;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.RestfulServerUtils;
import ca.uhn.fhir.rest.server.exceptions.AuthenticationException;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.util.UrlUtil;

/**
 * This interceptor detects when a request is coming from a browser, and automatically returns a response with syntax
 * highlighted (coloured) HTML for the response instead of just returning raw XML/JSON.
 * 
 * @since 1.0
 */
public class ResponseHighlighterInterceptor extends InterceptorAdapter {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ResponseHighlighterInterceptor.class);
	private static final String[] PARAM_FORMAT_VALUE_JSON = new String[] { Constants.FORMAT_JSON };
	private static final String[] PARAM_FORMAT_VALUE_XML = new String[] { Constants.FORMAT_XML };

	/**
	 * TODO: As of HAPI 1.6 (2016-06-10) this parameter has been replaced with simply
	 * requesting _format=json or xml so eventually this parameter should be removed
	 */
	public static final String PARAM_RAW = "_raw";

	public static final String PARAM_RAW_TRUE = "true";

	public static final String PARAM_TRUE = "true";

	private String format(String theResultBody, EncodingEnum theEncodingEnum) {
		String str = StringEscapeUtils.escapeHtml4(theResultBody);
		if (str == null || theEncodingEnum == null) {
			return str;
		}

		StringBuilder b = new StringBuilder();

		if (theEncodingEnum == EncodingEnum.JSON) {

			boolean inValue = false;
			boolean inQuote = false;
			for (int i = 0; i < str.length(); i++) {
				char prevChar = (i > 0) ? str.charAt(i - 1) : ' ';
				char nextChar = str.charAt(i);
				char nextChar2 = (i + 1) < str.length() ? str.charAt(i + 1) : ' ';
				char nextChar3 = (i + 2) < str.length() ? str.charAt(i + 2) : ' ';
				char nextChar4 = (i + 3) < str.length() ? str.charAt(i + 3) : ' ';
				char nextChar5 = (i + 4) < str.length() ? str.charAt(i + 4) : ' ';
				char nextChar6 = (i + 5) < str.length() ? str.charAt(i + 5) : ' ';
				if (inQuote) {
					b.append(nextChar);
					if (prevChar != '\\' && nextChar == '&' && nextChar2 == 'q' && nextChar3 == 'u' && nextChar4 == 'o' && nextChar5 == 't' && nextChar6 == ';') {
						b.append("quot;</span>");
						i += 5;
						inQuote = false;
					} else if (nextChar == '\\' && nextChar2 == '"') {
						b.append("quot;</span>");
						i += 5;
						inQuote = false;
					}
				} else {
					if (nextChar == ':') {
						inValue = true;
						b.append(nextChar);
					} else if (nextChar == '[' || nextChar == '{') {
						b.append("<span class='hlControl'>");
						b.append(nextChar);
						b.append("</span>");
						inValue = false;
					} else if (nextChar == '{' || nextChar == '}' || nextChar == ',') {
						b.append("<span class='hlControl'>");
						b.append(nextChar);
						b.append("</span>");
						inValue = false;
					} else if (nextChar == '&' && nextChar2 == 'q' && nextChar3 == 'u' && nextChar4 == 'o' && nextChar5 == 't' && nextChar6 == ';') {
						if (inValue) {
							b.append("<span class='hlQuot'>&quot;");
						} else {
							b.append("<span class='hlTagName'>&quot;");
						}
						inQuote = true;
						i += 5;
					} else if (nextChar == ':') {
						b.append("<span class='hlControl'>");
						b.append(nextChar);
						b.append("</span>");
						inValue = true;
					} else {
						b.append(nextChar);
					}
				}
			}

		} else {
			boolean inQuote = false;
			boolean inTag = false;
			for (int i = 0; i < str.length(); i++) {
				char nextChar = str.charAt(i);
				char nextChar2 = (i + 1) < str.length() ? str.charAt(i + 1) : ' ';
				char nextChar3 = (i + 2) < str.length() ? str.charAt(i + 2) : ' ';
				char nextChar4 = (i + 3) < str.length() ? str.charAt(i + 3) : ' ';
				char nextChar5 = (i + 4) < str.length() ? str.charAt(i + 4) : ' ';
				char nextChar6 = (i + 5) < str.length() ? str.charAt(i + 5) : ' ';
				if (inQuote) {
					b.append(nextChar);
					if (nextChar == '&' && nextChar2 == 'q' && nextChar3 == 'u' && nextChar4 == 'o' && nextChar5 == 't' && nextChar6 == ';') {
						b.append("quot;</span>");
						i += 5;
						inQuote = false;
					}
				} else if (inTag) {
					if (nextChar == '&' && nextChar2 == 'g' && nextChar3 == 't' && nextChar4 == ';') {
						b.append("</span><span class='hlControl'>&gt;</span>");
						inTag = false;
						i += 3;
					} else if (nextChar == ' ') {
						b.append("</span><span class='hlAttr'>");
						b.append(nextChar);
					} else if (nextChar == '&' && nextChar2 == 'q' && nextChar3 == 'u' && nextChar4 == 'o' && nextChar5 == 't' && nextChar6 == ';') {
						b.append("<span class='hlQuot'>&quot;");
						inQuote = true;
						i += 5;
					} else {
						b.append(nextChar);
					}
				} else {
					if (nextChar == '&' && nextChar2 == 'l' && nextChar3 == 't' && nextChar4 == ';') {
						b.append("<span class='hlControl'>&lt;</span><span class='hlTagName'>");
						inTag = true;
						i += 3;
					} else {
						b.append(nextChar);
					}
				}
			}
		}

		return b.toString();
	}

	@Override
	public boolean handleException(RequestDetails theRequestDetails, BaseServerResponseException theException, HttpServletRequest theServletRequest, HttpServletResponse theServletResponse)
			throws ServletException, IOException {
		/*
		 * It's not a browser...
		 */
		Set<String> accept = RestfulServerUtils.parseAcceptHeaderAndReturnHighestRankedOptions(theServletRequest);
		if (!accept.contains(Constants.CT_HTML)) {
			return super.handleException(theRequestDetails, theException, theServletRequest, theServletResponse);
		}

		/*
		 * It's an AJAX request, so no HTML
		 */
		String requestedWith = theServletRequest.getHeader("X-Requested-With");
		if (requestedWith != null) {
			return super.handleException(theRequestDetails, theException, theServletRequest, theServletResponse);
		}

		/*
		 * Not a GET
		 */
		if (theRequestDetails.getRequestType() != RequestTypeEnum.GET) {
			return super.handleException(theRequestDetails, theException, theServletRequest, theServletResponse);
		}

		if (theException.getOperationOutcome() == null) {
			return super.handleException(theRequestDetails, theException, theServletRequest, theServletResponse);
		}

		streamResponse(theRequestDetails, theServletResponse, theException.getOperationOutcome(), theServletRequest, theException.getStatusCode());

		return false;
	}

	@Override
	public boolean outgoingResponse(RequestDetails theRequestDetails, IBaseResource theResponseObject, HttpServletRequest theServletRequest, HttpServletResponse theServletResponse)
			throws AuthenticationException {

		/*
		 * Request for _raw
		 */
		String[] rawParamValues = theRequestDetails.getParameters().get(PARAM_RAW);
		if (rawParamValues != null && rawParamValues.length > 0 && rawParamValues[0].equals(PARAM_RAW_TRUE)) {
			ourLog.warn("Client is using non-standard/legacy  _raw parameter - Use _format=json or _format=xml instead, as this parmameter will be removed at some point");
			return super.outgoingResponse(theRequestDetails, theResponseObject, theServletRequest, theServletResponse);
		}

		boolean force = false;
		String[] formatParams = theRequestDetails.getParameters().get(Constants.PARAM_FORMAT);
		if (formatParams != null && formatParams.length > 0) {
			String formatParam = formatParams[0];
			if (Constants.FORMATS_HTML.contains(formatParam)) { // this is a set
				force = true;
			} else if (Constants.FORMATS_HTML_XML.equals(formatParam)) {
				force = true;
				theRequestDetails.getParameters().put(Constants.PARAM_FORMAT, PARAM_FORMAT_VALUE_XML);
			} else if (Constants.FORMATS_HTML_JSON.equals(formatParam)) {
				force = true;
				theRequestDetails.getParameters().put(Constants.PARAM_FORMAT, PARAM_FORMAT_VALUE_JSON);
			} else {
				return super.outgoingResponse(theRequestDetails, theResponseObject, theServletRequest, theServletResponse);
			}
		}

		/*
		 * It's not a browser...
		 */
		Set<String> highestRankedAcceptValues = RestfulServerUtils.parseAcceptHeaderAndReturnHighestRankedOptions(theServletRequest);
		if (!force && highestRankedAcceptValues.contains(Constants.CT_HTML) == false) {
			return super.outgoingResponse(theRequestDetails, theResponseObject, theServletRequest, theServletResponse);
		}

		/*
		 * It's an AJAX request, so no HTML
		 */
		if (!force && isNotBlank(theServletRequest.getHeader("X-Requested-With"))) {
			return super.outgoingResponse(theRequestDetails, theResponseObject, theServletRequest, theServletResponse);
		}
		/*
		 * If the request has an Origin header, it is probably an AJAX request
		 */
		if (!force && isNotBlank(theServletRequest.getHeader(Constants.HEADER_ORIGIN))) {
			return super.outgoingResponse(theRequestDetails, theResponseObject, theServletRequest, theServletResponse);
		}

		/*
		 * Not a GET
		 */
		if (!force && theRequestDetails.getRequestType() != RequestTypeEnum.GET) {
			return super.outgoingResponse(theRequestDetails, theResponseObject, theServletRequest, theServletResponse);
		}

		/*
		 * Not binary
		 */
		if (!force && "Binary".equals(theRequestDetails.getResourceName())) {
			return super.outgoingResponse(theRequestDetails, theResponseObject, theServletRequest, theServletResponse);
		}

		streamResponse(theRequestDetails, theServletResponse, theResponseObject, theServletRequest, 200);

		return false;
	}

	private void streamResponse(RequestDetails theRequestDetails, HttpServletResponse theServletResponse, IBaseResource resource, ServletRequest theServletRequest, int theStatusCode) {
		IParser p;
		Map<String, String[]> parameters = theRequestDetails.getParameters();
		if (parameters.containsKey(Constants.PARAM_FORMAT)) {
			p = RestfulServerUtils.getNewParser(theRequestDetails.getServer().getFhirContext(), theRequestDetails);
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
			p.setPrettyPrint(prettyPrintResponse);
		}

		EncodingEnum encoding = p.getEncoding();
		String encoded = p.encodeResourceToString(resource);

		try {

			if (theStatusCode > 299) {
				theServletResponse.setStatus(theStatusCode);
			}
			theServletResponse.setContentType(Constants.CT_HTML_WITH_UTF8);

			StringBuilder b = new StringBuilder();
			b.append("<html lang=\"en\">\n");
			b.append("	<head>\n");
			b.append("		<meta charset=\"utf-8\" />\n");
			b.append("       <style>\n");
			b.append(".hlQuot {\n");
			b.append("  color: #88F;\n");
			b.append("}\n");
			b.append(".hlAttr {\n");
			b.append("  color: #888;\n");
			b.append("}\n");
			b.append(".hlTagName {\n");
			b.append("  color: #006699;\n");
			b.append("}\n");
			b.append(".hlControl {\n");
			b.append("  color: #660000;\n");
			b.append("}\n");
			b.append(".hlText {\n");
			b.append("  color: #000000;\n");
			b.append("}\n");
			b.append(".hlUrlBase {\n");
			b.append("}");
			b.append(".headersDiv {\n");
			b.append("  background: #EEE;");
			b.append("}");
			b.append(".headerName {\n");
			b.append("  color: #888;\n");
			b.append("  font-family: monospace;\n");
			b.append("}");
			b.append(".headerValue {\n");
			b.append("  color: #88F;\n");
			b.append("  font-family: monospace;\n");
			b.append("}");
			b.append("BODY {\n");
			b.append("  font-family: Arial;\n");
			b.append("}");
			b.append("       </style>\n");
			b.append("	</head>\n");
			b.append("\n");
			b.append("	<body>");

			b.append("<p>");
			b.append("This result is being rendered in HTML for easy viewing. ");
			b.append("You may access this content as ");

			b.append("<a href=\"");
			b.append(createLinkHref(parameters, Constants.FORMAT_JSON));
			b.append("\">Raw JSON</a> or ");

			b.append("<a href=\"");
			b.append(createLinkHref(parameters, Constants.FORMAT_XML));
			b.append("\">Raw XML</a>, ");

			b.append(" or view this content in ");

			b.append("<a href=\"");
			b.append(createLinkHref(parameters, Constants.FORMATS_HTML_JSON));
			b.append("\">HTML JSON</a> ");

			b.append("or ");
			b.append("<a href=\"");
			b.append(createLinkHref(parameters, Constants.FORMATS_HTML_XML));
			b.append("\">HTML XML</a>.");

			Date startTime = (Date) theServletRequest.getAttribute(RestfulServer.REQUEST_START_TIME);
			if (startTime != null) {
				long time = System.currentTimeMillis() - startTime.getTime();
				b.append(" Response generated in ");
				b.append(time);
				b.append("ms.");
			}

			b.append("</p>");

			b.append("\n");

			// if (isEncodeHeaders()) {
			// b.append("<h1>Request Headers</h1>");
			// b.append("<div class=\"headersDiv\">");
			// for (int next : theRequestDetails.get)
			// b.append("</div>");
			// b.append("<h1>Response Headers</h1>");
			// b.append("<div class=\"headersDiv\">");
			// b.append("</div>");
			// b.append("<h1>Response Body</h1>");
			// }
			b.append("<pre>");
			b.append(format(encoded, encoding));
			b.append("</pre>");
			b.append("   </body>");
			b.append("</html>");
		//@formatter:off
		String out = b.toString();
		//@formatter:on

			theServletResponse.getWriter().append(out);
			theServletResponse.getWriter().close();
		} catch (IOException e) {
			throw new InternalErrorException(e);
		}
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
				rawB.append(UrlUtil.escape(next));
				rawB.append('=');
				rawB.append(UrlUtil.escape(nextValue));
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

}
