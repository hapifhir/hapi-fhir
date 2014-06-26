package ca.uhn.fhir.rest.server.tester;

/*
 * #%L
 * HAPI FHIR Library
 * %%
 * Copyright (C) 2014 University Health Network
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

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.WriterOutputStream;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ContentType;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.TemplateProcessingParameters;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.resourceresolver.IResourceResolver;
import org.thymeleaf.standard.StandardDialect;
import org.thymeleaf.templateresolver.TemplateResolver;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu.composite.IdentifierDt;
import ca.uhn.fhir.model.dstu.resource.Conformance;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.rest.annotation.Metadata;
import ca.uhn.fhir.rest.client.GenericClient;
import ca.uhn.fhir.rest.client.api.IBasicClient;
import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.rest.server.EncodingEnum;

public class RestfulServerTesterServlet extends HttpServlet {

	private static final boolean DEBUGMODE = true;
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(RestfulServerTesterServlet.class);
	private static final String PUBLIC_TESTER_RESULT_HTML = "/PublicTesterResult.html";
	private static final long serialVersionUID = 1L;
	private FhirContext myCtx;
	private String myServerBase;
	private HashMap<String, String> myStaticResources;

	private TemplateEngine myTemplateEngine;
	private Set<String> myFilterHeaders;

	public RestfulServerTesterServlet() {
		myStaticResources = new HashMap<String, String>();
		myStaticResources.put("jquery-2.1.0.min.js", "text/javascript");
		myStaticResources.put("PublicTester.js", "text/javascript");
		myStaticResources.put("PublicTester.css", "text/css");
		myStaticResources.put("hapi_fhir_banner.png", "image/png");
		myStaticResources.put("hapi_fhir_banner_right.png", "image/png");
		myStaticResources.put("shCore.js", "text/javascript");
		myStaticResources.put("shBrushJScript.js", "text/javascript");
		myStaticResources.put("shBrushXml.js", "text/javascript");
		myStaticResources.put("shBrushPlain.js", "text/javascript");
		myStaticResources.put("shCore.css", "text/css");
		myStaticResources.put("shThemeDefault.css", "text/css");
		myStaticResources.put("json2.js", "text/javascript");
		myStaticResources.put("minify.json.js", "text/javascript");

		myCtx = new FhirContext();
	}

	public FhirContext getFhirContext() {
		return myCtx;
	}

	@Override
	public void init(ServletConfig theConfig) throws ServletException {
		myTemplateEngine = new TemplateEngine();
		TemplateResolver resolver = new TemplateResolver();
		resolver.setResourceResolver(new ProfileResourceResolver());
		myTemplateEngine.setTemplateResolver(resolver);
		StandardDialect dialect = new StandardDialect();
		myTemplateEngine.setDialect(dialect);
		myTemplateEngine.initialize();
	}

	public void setServerBase(String theServerBase) {
		myServerBase = theServerBase;
	}

	private RuntimeResourceDefinition getResourceType(HttpServletRequest theReq) throws ServletException {
		String resourceName = StringUtils.defaultString(theReq.getParameter("resourceName"));
		RuntimeResourceDefinition def = myCtx.getResourceDefinition(resourceName);
		if (def == null) {
			throw new ServletException("Invalid resourceName: " + resourceName);
		}
		return def;
	}

	private void streamResponse(String theResourceName, String theContentType, HttpServletResponse theResp) throws IOException {
		InputStream res = RestfulServerTesterServlet.class.getResourceAsStream("/ca/uhn/fhir/rest/server/tester/" + theResourceName);
		theResp.setContentType(theContentType);
		IOUtils.copy(res, theResp.getOutputStream());
	}

	@Override
	protected void doGet(HttpServletRequest theReq, HttpServletResponse theResp) throws ServletException, IOException {
		if (DEBUGMODE) {
			myTemplateEngine.getCacheManager().clearAllCaches();
		}

		try {
			ourLog.info("RequestURI: {}", theReq.getPathInfo());

			String resName = theReq.getPathInfo().substring(1);
			if (myStaticResources.containsKey(resName)) {
				streamResponse(resName, myStaticResources.get(resName), theResp);
				return;
			}

			ConformanceClient client = myCtx.newRestfulClient(ConformanceClient.class, myServerBase);
			Conformance conformance = client.getConformance();

			WebContext ctx = new WebContext(theReq, theResp, theReq.getServletContext(), theReq.getLocale());
			ctx.setVariable("conf", conformance);
			ctx.setVariable("base", myServerBase);
			ctx.setVariable("jsonEncodedConf", myCtx.newJsonParser().encodeResourceToString(conformance));
			addStandardVariables(ctx, theReq.getParameterMap());
			
			theResp.setContentType("text/html");
			theResp.setCharacterEncoding("UTF-8");

			myTemplateEngine.process(theReq.getPathInfo(), ctx, theResp.getWriter());
		} catch (Exception e) {
			ourLog.error("Failed to respond", e);
			theResp.sendError(500, e.getMessage());
		}
	}

	private void addStandardVariables(WebContext theCtx, Map<String, String[]> theParameterMap) {
		addStandardVariable(theCtx, theParameterMap, "configEncoding");
		addStandardVariable(theCtx, theParameterMap, "configPretty");
	}

	private void addStandardVariable(WebContext theCtx, Map<String, String[]> theParameterMap, String key) {
		if (theParameterMap.containsKey(key) && theParameterMap.get(key).length > 0) {
			theCtx.setVariable(key, theParameterMap.get(key)[0]);
		}
	}

	@Override
	protected void doPost(HttpServletRequest theReq, HttpServletResponse theResp) throws ServletException, IOException {
		if (DEBUGMODE) {
			myTemplateEngine.getCacheManager().clearAllCaches();
		}

		GenericClient client = (GenericClient) myCtx.newRestfulGenericClient(myServerBase);
		client.setKeepResponses(true);
		boolean returnsResource;
		long latency=0;
		
		try {
			String method = theReq.getParameter("method");

			String prettyParam = theReq.getParameter("configPretty");
			if ("on".equals(prettyParam)) {
				client.setPrettyPrint(true);
			}
			if ("xml".equals(theReq.getParameter("configEncoding"))) {
				client.setEncoding(EncodingEnum.XML);
			} else if ("json".equals(theReq.getParameter("configEncoding"))) {
				client.setEncoding(EncodingEnum.JSON);
			}

			long start = System.currentTimeMillis();
			if ("conformance".equals(method)) {
				returnsResource = true;
				client.conformance();
			} else if ("read".equals(method)) {
				RuntimeResourceDefinition def = getResourceType(theReq);
				String id = StringUtils.defaultString(theReq.getParameter("id"));
				if (StringUtils.isBlank(id)) {
					theResp.sendError(Constants.STATUS_HTTP_400_BAD_REQUEST, "No ID specified");
				}
				returnsResource = true;

				client.read(def.getImplementingClass(), new IdDt(id));

			} else if ("vread".equals(method)) {
				RuntimeResourceDefinition def = getResourceType(theReq);
				String id = StringUtils.defaultString(theReq.getParameter("id"));
				if (StringUtils.isBlank(id)) {
					theResp.sendError(Constants.STATUS_HTTP_400_BAD_REQUEST, "No ID specified");
				}

				String versionId = StringUtils.defaultString(theReq.getParameter("versionid"));
				if (StringUtils.isBlank(versionId)) {
					theResp.sendError(Constants.STATUS_HTTP_400_BAD_REQUEST, "No Version ID specified");
				}
				returnsResource = true;

				client.vread(def.getImplementingClass(), new IdDt(id), new IdDt(versionId));

			} else if ("delete".equals(method)) {
				RuntimeResourceDefinition def = getResourceType(theReq);
				String id = StringUtils.defaultString(theReq.getParameter("id"));
				if (StringUtils.isBlank(id)) {
					theResp.sendError(Constants.STATUS_HTTP_400_BAD_REQUEST, "No ID specified");
				}

				returnsResource = false;

				client.delete(def.getImplementingClass(), new IdDt(id));

			} else if ("history-instance".equals(method) || "history-server".equals(method) || "history-type".equals(method)) {
				RuntimeResourceDefinition def = getResourceType(theReq);
				String id = StringUtils.defaultString(theReq.getParameter("id"));
				if (StringUtils.isBlank(id)) {
					theResp.sendError(Constants.STATUS_HTTP_400_BAD_REQUEST, "No ID specified");
				}

				returnsResource = false;

				client.history(def.getImplementingClass(), new IdDt(id),null,null);

			} else if ("create".equals(method)) {
				IResource resource = parseIncomingResource(theReq, theResp, client);
				returnsResource = false;

				client.create(resource);

			} else if ("validate".equals(method)) {
				IResource resource = parseIncomingResource(theReq, theResp, client);
				returnsResource = false;

				client.validate(resource);

			} else if ("update".equals(method)) {
				String id = StringUtils.defaultString(theReq.getParameter("id"));
				if (StringUtils.isBlank(id)) {
					theResp.sendError(Constants.STATUS_HTTP_400_BAD_REQUEST, "No ID specified");
				}

				IResource resource = parseIncomingResource(theReq, theResp, client);
				returnsResource = false;

				client.update(new IdDt(id), resource);

			} else if ("searchType".equals(method)) {
				Map<String, List<IQueryParameterType>> params = new HashMap<String, List<IQueryParameterType>>();

				HashSet<String> hashSet = new HashSet<String>(theReq.getParameterMap().keySet());
				String paramName = null;
				IQueryParameterType paramValue = null;
				while (hashSet.isEmpty() == false) {

					String nextKey = hashSet.iterator().next();
					String nextValue = theReq.getParameter(nextKey);
					paramName = null;
					paramValue = null;

					if (nextKey.startsWith("param.token.")) {
						int prefixLength = "param.token.".length();
						paramName = nextKey.substring(prefixLength + 2);
						String systemKey = "param.token." + "1." + paramName;
						String valueKey = "param.token." + "2." + paramName;
						String system = theReq.getParameter(systemKey);
						String value = theReq.getParameter(valueKey);
						paramValue = new IdentifierDt(system, value);
						hashSet.remove(systemKey);
						hashSet.remove(valueKey);
					} else if (nextKey.startsWith("param.string.")) {
						paramName = nextKey.substring("param.string.".length());
						paramValue = new StringDt(nextValue);
					}

					if (paramName != null) {
						if (params.containsKey(paramName) == false) {
							params.put(paramName, new ArrayList<IQueryParameterType>());
						}
						params.get(paramName).add(paramValue);
					}

					hashSet.remove(nextKey);
				}

				RuntimeResourceDefinition def = getResourceType(theReq);

				returnsResource = false;
				client.search(def.getImplementingClass(), params);

			} else {
				theResp.sendError(Constants.STATUS_HTTP_400_BAD_REQUEST, "Invalid method: " + method);
				return;
			}

			latency = System.currentTimeMillis() - start;
		} catch (DataFormatException e) {
			ourLog.error("Failed to invoke method", e);
			returnsResource = false;
		} catch (Exception e) {
			ourLog.error("Failure during processing", e);
			returnsResource = false;
		}

		try {
			HttpRequestBase lastRequest = client.getLastRequest();
			String requestBody = null;
			String requestSyntaxHighlighterClass = null;

			if (lastRequest instanceof HttpEntityEnclosingRequest) {
				HttpEntityEnclosingRequest lastEERequest = (HttpEntityEnclosingRequest) lastRequest;
				HttpEntity lastEE = lastEERequest.getEntity();
				if (lastEE.isRepeatable()) {
					StringWriter requestCapture = new StringWriter();
					lastEE.writeTo(new WriterOutputStream(requestCapture, "UTF-8"));
					requestBody = requestCapture.toString();
					ContentType ct = ContentType.get(lastEE);
					String mimeType = ct.getMimeType();
					EncodingEnum ctEnum = EncodingEnum.forContentType(mimeType);
					if (ctEnum == null) {
						requestSyntaxHighlighterClass = "brush: plain";
					} else {
						switch (ctEnum) {
						case JSON:
							requestSyntaxHighlighterClass = "brush: jscript";
							break;
						case XML:
						default:
							requestSyntaxHighlighterClass = "brush: xml";
							break;
						}
					}
				}
			}
			String resultSyntaxHighlighterClass;
			String requestUrl = lastRequest != null ? lastRequest.getURI().toASCIIString() : null;
			String action = client.getLastRequest() != null ? client.getLastRequest().getMethod() : null;
			String resultStatus = client.getLastResponse() != null ? client.getLastResponse().getStatusLine().toString() : null;
			String resultBody = client.getLastResponseBody();

			HttpResponse lastResponse = client.getLastResponse();
			ContentType ct = lastResponse != null ? ContentType.get(lastResponse.getEntity()) : null;
			String mimeType = ct != null ? ct.getMimeType() : null;
			EncodingEnum ctEnum = EncodingEnum.forContentType(mimeType);
			String narrativeString = "";

			if (ctEnum == null) {
				resultSyntaxHighlighterClass = "brush: plain";
			} else {
				switch (ctEnum) {
				case JSON:
					resultSyntaxHighlighterClass = "brush: jscript";
					if (returnsResource) {
						narrativeString = parseNarrative(ctEnum, resultBody);
					}
					break;
				case XML:
				default:
					resultSyntaxHighlighterClass = "brush: xml";
					if (returnsResource) {
						narrativeString = parseNarrative(ctEnum, resultBody);
					}
					break;
				}
			}

			Header[] requestHeaders = lastRequest != null ? applyHeaderFilters(lastRequest.getAllHeaders()) : new Header[0];
			Header[] responseHeaders = lastResponse != null ? applyHeaderFilters(lastResponse.getAllHeaders()) : new Header[0];

			WebContext ctx = new WebContext(theReq, theResp, theReq.getServletContext(), theReq.getLocale());
			ctx.setVariable("base", myServerBase);
			ctx.setVariable("requestUrl", requestUrl);
			ctx.setVariable("action", action);
			ctx.setVariable("resultStatus", resultStatus);
			ctx.setVariable("requestBody", StringEscapeUtils.escapeHtml4(requestBody));
			ctx.setVariable("requestSyntaxHighlighterClass", requestSyntaxHighlighterClass);
			ctx.setVariable("resultBody", StringEscapeUtils.escapeHtml4(resultBody));
			ctx.setVariable("resultSyntaxHighlighterClass", resultSyntaxHighlighterClass);
			ctx.setVariable("requestHeaders", requestHeaders);
			ctx.setVariable("responseHeaders", responseHeaders);
			ctx.setVariable("narrative", narrativeString);
			ctx.setVariable("latencyMs", latency);

			myTemplateEngine.process(PUBLIC_TESTER_RESULT_HTML, ctx, theResp.getWriter());
		} catch (Exception e) {
			ourLog.error("Failure during processing", e);
			theResp.sendError(500, e.toString());
		}
	}

	private IResource parseIncomingResource(HttpServletRequest theReq, HttpServletResponse theResp, GenericClient theClient) throws ServletException, IOException {
		RuntimeResourceDefinition def = getResourceType(theReq);
		String resourceText = StringUtils.defaultString(theReq.getParameter("resource"));
		if (StringUtils.isBlank(resourceText)) {
			theResp.sendError(Constants.STATUS_HTTP_400_BAD_REQUEST, "No resource content specified");
		}

		IResource resource;
		if (theClient.getEncoding() == null) {
			if (resourceText.trim().startsWith("{")) {
				resource = myCtx.newJsonParser().parseResource(def.getImplementingClass(), resourceText);
			} else {
				resource = myCtx.newXmlParser().parseResource(def.getImplementingClass(), resourceText);
			}
		} else if (theClient.getEncoding() == EncodingEnum.XML) {
			resource = myCtx.newXmlParser().parseResource(def.getImplementingClass(), resourceText);
		} else {
			resource = myCtx.newJsonParser().parseResource(def.getImplementingClass(), resourceText);
		}
		return resource;
	}

	private Header[] applyHeaderFilters(Header[] theAllHeaders) {
		if (myFilterHeaders == null || myFilterHeaders.isEmpty()) {
			return theAllHeaders;
		}
		ArrayList<Header> retVal = new ArrayList<Header>();
		for (Header next : theAllHeaders) {
			if (!myFilterHeaders.contains(next.getName().toLowerCase())) {
				retVal.add(next);
			}
		}
		return retVal.toArray(new Header[retVal.size()]);
	}

	/**
	 * If set, the headers named here will be stripped from requests/responses before they are displayed to the user.
	 * This can be used, for instance, to filter out "Authorization" headers. Note that names are not case sensitive.
	 */
	public void setFilterHeaders(String... theHeaderNames) {
		myFilterHeaders = new HashSet<String>();
		if (theHeaderNames != null) {
			for (String next : theHeaderNames) {
				myFilterHeaders.add(next.toLowerCase());
			}
		}
	}

	private String parseNarrative(EncodingEnum theCtEnum, String theResultBody) {
		try {
			IResource resource = theCtEnum.newParser(myCtx).parseResource(theResultBody);
			String retVal = resource.getText().getDiv().getValueAsString();
			return StringUtils.defaultString(retVal);
		} catch (Exception e) {
			ourLog.error("Failed to parse resource", e);
			return "";
		}
	}

	private interface ConformanceClient extends IBasicClient {
		@Metadata
		Conformance getConformance();
	}

	private final class ProfileResourceResolver implements IResourceResolver {

		@Override
		public String getName() {
			return getClass().getCanonicalName();
		}

		@Override
		public InputStream getResourceAsStream(TemplateProcessingParameters theTemplateProcessingParameters, String theName) {
			ourLog.debug("Loading template: {}", theName);
			if ("/".equals(theName)) {
				return RestfulServerTesterServlet.class.getResourceAsStream("/ca/uhn/fhir/rest/server/tester/PublicTester.html");
			}
			if (PUBLIC_TESTER_RESULT_HTML.equals(theName)) {
				return RestfulServerTesterServlet.class.getResourceAsStream("/ca/uhn/fhir/rest/server/tester/PublicTesterResult.html");
			}

			return null;
		}
	}

}
