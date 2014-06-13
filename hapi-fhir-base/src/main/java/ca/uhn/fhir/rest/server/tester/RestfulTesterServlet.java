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

import static org.apache.commons.lang3.StringUtils.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
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
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu.resource.Conformance;
import ca.uhn.fhir.model.dstu.resource.Conformance.Rest;
import ca.uhn.fhir.model.dstu.resource.Conformance.RestResource;
import ca.uhn.fhir.model.primitive.DateTimeDt;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.rest.annotation.Metadata;
import ca.uhn.fhir.rest.client.GenericClient;
import ca.uhn.fhir.rest.client.api.IBasicClient;
import ca.uhn.fhir.rest.gclient.IQuery;
import ca.uhn.fhir.rest.gclient.IUntypedQuery;
import ca.uhn.fhir.rest.gclient.StringParam;
import ca.uhn.fhir.rest.server.EncodingEnum;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;

public class RestfulTesterServlet extends HttpServlet {

	private static final boolean DEBUGMODE = true;
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(RestfulTesterServlet.class);
	private static final String PUBLIC_TESTER_RESULT_HTML = "/PublicTesterResult.html";
	private static final long serialVersionUID = 1L;
	private FhirContext myCtx;
	private String myServerBase;
	private HashMap<String, String> myStaticResources;

	private TemplateEngine myTemplateEngine;
	private Set<String> myFilterHeaders;

	public RestfulTesterServlet() {
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

		myStaticResources.put("css/bootstrap.min.css", "text/css");
		myStaticResources.put("css/tester.css", "text/css");
		myStaticResources.put("img/hapi_fhir_banner.png", "image/png");
		myStaticResources.put("img/hapi_fhir_banner_right.png", "image/png");
		myStaticResources.put("js/bootstrap.min.js", "text/javascript");
		myStaticResources.put("js/jquery-2.1.0.min.js", "text/javascript");

		myStaticResources.put("css/bootstrap-datetimepicker.min.css", "text/css");
		myStaticResources.put("js/bootstrap-datetimepicker.min.js", "text/javascript");
		myStaticResources.put("js/moment.min.js", "text/javascript");

		myStaticResources.put("js/select2.min.js", "text/javascript");
		myStaticResources.put("css/select2.css", "text/css");
		myStaticResources.put("css/select2.png", "image/png");
		myStaticResources.put("css/select2x2.png", "image/png");
		myStaticResources.put("css/select2-spinner.gif", "image/gif");

		myStaticResources.put("fonts/glyphicons-halflings-regular.eot", "application/octet-stream");
		myStaticResources.put("fonts/glyphicons-halflings-regular.svg", "application/octet-stream");
		myStaticResources.put("fonts/glyphicons-halflings-regular.ttf", "application/octet-stream");
		myStaticResources.put("fonts/glyphicons-halflings-regular.woff", "application/octet-stream");
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
		String resourceName = StringUtils.defaultString(theReq.getParameter("resource"));
		RuntimeResourceDefinition def = myCtx.getResourceDefinition(resourceName);
		if (def == null) {
			throw new ServletException("Invalid resourceName: " + resourceName);
		}
		return def;
	}

	private void streamResponse(String theResourceName, String theContentType, HttpServletResponse theResp) throws IOException {
		InputStream res = RestfulTesterServlet.class.getResourceAsStream("/ca/uhn/fhir/rest/server/tester/" + theResourceName);
		theResp.setContentType(theContentType);
		IOUtils.copy(res, theResp.getOutputStream());
	}

	private void processAction(HttpServletRequest theReq, WebContext theContext) {

		GenericClient client = (GenericClient) myCtx.newRestfulGenericClient(myServerBase);
		client.setKeepResponses(true);
		boolean returnsResource;
		long latency = 0;

		try {
			String method = theReq.getParameter("action");

			String prettyParam = theReq.getParameter("pretty");
			if ("true".equals(prettyParam)) {
				client.setPrettyPrint(true);
			} else if ("false".equals(prettyParam)) {
				client.setPrettyPrint(false);
			}
			if ("xml".equals(theReq.getParameter("encoding"))) {
				client.setEncoding(EncodingEnum.XML);
			} else if ("json".equals(theReq.getParameter("configEncoding"))) {
				client.setEncoding(EncodingEnum.JSON);
			}

			long start = System.currentTimeMillis();
			if ("home".equals(method)) {
				return;
			} else if ("conformance".equals(method)) {
				returnsResource = true;
				client.conformance();
			} else if ("read".equals(method)) {
				RuntimeResourceDefinition def = getResourceType(theReq);
				String id = StringUtils.defaultString(theReq.getParameter("id"));
				if (StringUtils.isBlank(id)) {
					theContext.getVariables().put("errorMsg", "No ID specified");
					return;
				}
				returnsResource = true;

				String versionId = StringUtils.defaultString(theReq.getParameter("vid"));
				if (StringUtils.isBlank(versionId)) {
					versionId = null;
				}

				client.read(def.getImplementingClass(), new IdDt(def.getName(), id, versionId));

			} else if ("delete".equals(method)) {
				RuntimeResourceDefinition def = getResourceType(theReq);
				String id = StringUtils.defaultString(theReq.getParameter("resource-delete-id"));
				if (StringUtils.isBlank(id)) {
					theContext.getVariables().put("errorMsg", "No ID specified");
					return;
				}

				returnsResource = false;

				client.delete(def.getImplementingClass(), new IdDt(id));

			} else if ("history-resource".equals(method) || "history-server".equals(method)) {
				String id = null;
				Class<? extends IResource> type = null; // def.getImplementingClass();
				if (!"history-server".equals(method)) {
					RuntimeResourceDefinition def = getResourceType(theReq);
					type = def.getImplementingClass();
					id = StringUtils.defaultString(theReq.getParameter("resource-history-id"));
					if (StringUtils.isBlank(id)) {
						if ("history-instance".equals(method)) {
							theContext.getVariables().put("errorMsg", "No ID specified");
							return;
						} else {
							id = null;
						}
					}
				}

				DateTimeDt since = null;
				String sinceStr = theReq.getParameter("since");
				if (isNotBlank(sinceStr)) {
					since = new DateTimeDt(sinceStr);
				}

				Integer limit = null;
				String limitStr = theReq.getParameter("limit");
				if (isNotBlank(limitStr)) {
					limit = Integer.parseInt(limitStr);
				}

				returnsResource = false;

				client.history(type, id, since, limit);

			} else if ("create".equals(method) || "validate".equals(method)) {
				boolean validate = "validate".equals(method);

				String body = validate ? theReq.getParameter("resource-validate-body") : theReq.getParameter("resource-create-body");
				if (isBlank(body)) {
					theContext.getVariables().put("errorMsg", "No message body specified");
					return;
				}

				body = body.trim();
				IResource resource;
				try {
					if (body.startsWith("{")) {
						resource = myCtx.newJsonParser().parseResource(body);
					} else if (body.startsWith("<")) {
						resource = myCtx.newXmlParser().parseResource(body);
					} else {
						theContext.getVariables().put("errorMsg", "Message body does not appear to be a valid FHIR resource instance document. Body should start with '<' (for XML encoding) or '{' (for JSON encoding).");
						return;
					}
				} catch (DataFormatException e) {
					ourLog.warn("Failed to parse resource", e);
					theContext.getVariables().put("errorMsg", "Failed to parse message body. Error was: " + e.getMessage());
					return;
				}

				if (validate) {
					client.validate(resource);
				} else {
					String id = theReq.getParameter("resource-create-id");
					if (isNotBlank(id)) {
						client.update(id, resource);
					} else {
						client.create(resource);
					}
				}
				returnsResource = false;

			} else if ("search".equals(method)) {
				IUntypedQuery search = client.search();
				IQuery query;
				if (isNotBlank(theReq.getParameter("resource"))) {
					query = search.forResource(getResourceType(theReq).getImplementingClass());
				} else {
					query = search.forAllResources();
				}

				int paramIdx = 0;
				while (true) {
					String nextName = theReq.getParameter("param." + paramIdx + ".name");
					if (isBlank(nextName)) {
						break;
					}

					StringBuilder b = new StringBuilder();
					for (int i = 0; i < 100; i++) {
						b.append(defaultString(theReq.getParameter("param." + paramIdx + "." + i)));
					}

					query.where(new StringParam(nextName).matches().value(b.toString()));

					paramIdx++;
				}

				query.execute();
				returnsResource = false;

			} else {
				theContext.getVariables().put("errorMsg", "Invalid action: " + method);
				return;
			}

			latency = System.currentTimeMillis() - start;
		} catch (DataFormatException e) {
			ourLog.error("Failed to invoke method", e);
			returnsResource = false;
		} catch (BaseServerResponseException e) {
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

			theContext.setVariable("action", action);
			theContext.setVariable("resultStatus", resultStatus);
			theContext.setVariable("requestUrl", requestUrl);
			requestBody = StringEscapeUtils.escapeHtml4(requestBody);
			theContext.setVariable("requestBody", requestBody);
			theContext.setVariable("requestSyntaxHighlighterClass", requestSyntaxHighlighterClass);
			theContext.setVariable("resultBody", StringEscapeUtils.escapeHtml4(resultBody));
			theContext.setVariable("resultSyntaxHighlighterClass", resultSyntaxHighlighterClass);
			theContext.setVariable("requestHeaders", requestHeaders);
			theContext.setVariable("responseHeaders", responseHeaders);
			theContext.setVariable("narrative", narrativeString);
			theContext.setVariable("latencyMs", latency);

		} catch (Exception e) {
			ourLog.error("Failure during processing", e);
			theContext.getVariables().put("errorMsg", "Error during processing: " + e.getMessage());
		}
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
			String resourceName = defaultString(theReq.getParameter("resource"));
			ctx.setVariable("resourceName", resourceName);
			ctx.setVariable("jsonEncodedConf", myCtx.newJsonParser().encodeResourceToString(conformance));
			addStandardVariables(ctx, theReq.getParameterMap());

			if (isNotBlank(theReq.getParameter("action"))) {
				processAction(theReq, ctx);
			}

			if (isNotBlank(resourceName)) {
				RuntimeResourceDefinition def = myCtx.getResourceDefinition(resourceName);
//				myCtx.newTerser().
			}
			
			theResp.setContentType("text/html");
			theResp.setCharacterEncoding("UTF-8");

			myTemplateEngine.process(theReq.getPathInfo(), ctx, theResp.getWriter());
		} catch (Exception e) {
			ourLog.error("Failed to respond", e);
			theResp.sendError(500, e.getMessage());
		}
	}

	private void addStandardVariables(WebContext theCtx, Map<String, String[]> theParameterMap) {
		addStandardVariable(theCtx, theParameterMap, "encoding");
		addStandardVariable(theCtx, theParameterMap, "pretty");
	}

	private void addStandardVariable(WebContext theCtx, Map<String, String[]> theParameterMap, String key) {
		if (theParameterMap.containsKey(key) && theParameterMap.get(key).length > 0) {
			theCtx.setVariable(key, theParameterMap.get(key)[0]);
		}
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
				return RestfulTesterServlet.class.getResourceAsStream("/ca/uhn/fhir/rest/server/tester/RestfulTester.html");
			}
			if (PUBLIC_TESTER_RESULT_HTML.equals(theName)) {
				return RestfulTesterServlet.class.getResourceAsStream("/ca/uhn/fhir/rest/server/tester/PublicTesterResult.html");
			}

			return null;
		}
	}

}
