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

import static org.apache.commons.lang3.StringUtils.defaultIfEmpty;
import static org.apache.commons.lang3.StringUtils.defaultString;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
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
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.api.ExtensionDt;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.model.dstu.resource.Conformance;
import ca.uhn.fhir.model.dstu.resource.Conformance.Rest;
import ca.uhn.fhir.model.dstu.resource.Conformance.RestResource;
import ca.uhn.fhir.model.dstu.valueset.SearchParamTypeEnum;
import ca.uhn.fhir.model.primitive.DateTimeDt;
import ca.uhn.fhir.model.primitive.DecimalDt;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.rest.client.GenericClient;
import ca.uhn.fhir.rest.client.IGenericClient;
import ca.uhn.fhir.rest.gclient.IQuery;
import ca.uhn.fhir.rest.gclient.IUntypedQuery;
import ca.uhn.fhir.rest.gclient.StringParam;
import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.rest.server.EncodingEnum;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;

public class RestfulTesterServlet extends HttpServlet {

	private static final String PARAM_RESOURCE = "resource";

	private static final String RESOURCE_COUNT_EXT_URL = "http://hl7api.sourceforge.net/hapi-fhir/res/extdefs.html#resourceCount";

	private static final boolean DEBUGMODE = true;
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(RestfulTesterServlet.class);
	private static final String PUBLIC_TESTER_RESULT_HTML = "/PublicTesterResult.html";
	private static final long serialVersionUID = 1L;
	private FhirContext myCtx;
	private LinkedHashMap<String, String> myIdToServerName = new LinkedHashMap<String, String>();
	private LinkedHashMap<String, String> myIdToServerBase = new LinkedHashMap<String, String>();
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
		myStaticResources.put("js/RestfulTester.js", "text/javascript");

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

		myStaticResources.put("fa/css/font-awesome.css", "text/css");
		myStaticResources.put("fa/css/font-awesome.min.css", "text/css");
		myStaticResources.put("fa/fonts/fontawesome-webfont.eot", "application/octet-stream");
		myStaticResources.put("fa/fonts/fontawesome-webfont.svg", "application/octet-stream");
		myStaticResources.put("fa/fonts/fontawesome-webfont.ttf", "application/octet-stream");
		myStaticResources.put("fa/fonts/fontawesome-webfont.woff", "application/octet-stream");
		myStaticResources.put("fa/fonts/FontAwesome.otf", "application/octet-stream");
		myStaticResources.put("fa/less/bordered-pulled.less", "text/css");
		myStaticResources.put("fa/less/core.less", "text/css");
		myStaticResources.put("fa/less/fixed-width.less", "text/css");
		myStaticResources.put("fa/less/font-awesome.less", "text/css");
		myStaticResources.put("fa/less/icons.less", "text/css");
		myStaticResources.put("fa/less/larger.less", "text/css");
		myStaticResources.put("fa/less/list.less", "text/css");
		myStaticResources.put("fa/less/mixins.less", "text/css");
		myStaticResources.put("fa/less/path.less", "text/css");
		myStaticResources.put("fa/less/rotated-flipped.less", "text/css");
		myStaticResources.put("fa/less/spinning.less", "text/css");
		myStaticResources.put("fa/less/stacked.less", "text/css");
		myStaticResources.put("fa/less/variables.less", "text/css");
		myStaticResources.put("fa/scss/_bordered-pulled.scss", "text/css");
		myStaticResources.put("fa/scss/_core.scss", "text/css");
		myStaticResources.put("fa/scss/_fixed-width.scss", "text/css");
		myStaticResources.put("fa/scss/_icons.scss", "text/css");
		myStaticResources.put("fa/scss/_larger.scss", "text/css");
		myStaticResources.put("fa/scss/_list.scss", "text/css");
		myStaticResources.put("fa/scss/_mixins.scss", "text/css");
		myStaticResources.put("fa/scss/_path.scss", "text/css");
		myStaticResources.put("fa/scss/_rotated-flipped.scss", "text/css");
		myStaticResources.put("fa/scss/_spinning.scss", "text/css");
		myStaticResources.put("fa/scss/_stacked.scss", "text/css");
		myStaticResources.put("fa/scss/_variables.scss", "text/css");
		myStaticResources.put("fa/scss/font-awesome.scss", "text/css");

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

	public void addServerBase(String theId, String theDisplayName, String theServerBase) {
		Validate.notBlank(theId, "theId can not be blank");
		Validate.notBlank(theDisplayName, "theDisplayName can not be blank");
		Validate.notBlank(theServerBase, "theServerBase can not be blank");
		myIdToServerBase.put(theId, theServerBase);
		myIdToServerName.put(theId, theDisplayName);
	}

	
	private RuntimeResourceDefinition getResourceType(HttpServletRequest theReq) throws ServletException {
		String resourceName = StringUtils.defaultString(theReq.getParameter(PARAM_RESOURCE));
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

	private enum ResultType {
		RESOURCE, BUNDLE, TAGLIST, NONE
	}

	private void processAction(HttpServletRequest theReq, WebContext theContext, IGenericClient theClient, String theServerBase) {

		GenericClient client = (GenericClient) theClient;
		client.setKeepResponses(true);
		ResultType returnsResource;
		long latency = 0;

		String outcomeDescription = null;
		try {
			String method = theReq.getParameter("action");

			String prettyParam = theReq.getParameter("pretty");
			if ("true".equals(prettyParam)) {
				client.setPrettyPrint(true);
			} else if ("false".equals(prettyParam)) {
				client.setPrettyPrint(false);
			}
			EncodingEnum encoding = getRequestEncoding(theReq);
			client.setEncoding(encoding);

			long start = System.currentTimeMillis();
			if ("home".equals(method)) {
				return;
			} else if ("conformance".equals(method)) {
				returnsResource = ResultType.RESOURCE;
				client.conformance();
			} else if ("read".equals(method)) {
				RuntimeResourceDefinition def = getResourceType(theReq);
				String id = StringUtils.defaultString(theReq.getParameter("id"));
				if (StringUtils.isBlank(id)) {
					theContext.getVariables().put("errorMsg", "No ID specified");
					return;
				}
				returnsResource = ResultType.RESOURCE;

				String versionId = StringUtils.defaultString(theReq.getParameter("vid"));
				if (StringUtils.isBlank(versionId)) {
					versionId = null;
					outcomeDescription = "Read Resource";
				} else {
					outcomeDescription = "VRead Resource";
				}

				client.read(def.getImplementingClass(), new IdDt(def.getName(), id, versionId));

			} else if ("get-tags".equals(method)) {

				Class<? extends IResource> resType = null;
				if (isNotBlank(theReq.getParameter(PARAM_RESOURCE))) {
					RuntimeResourceDefinition def = getResourceType(theReq);
					resType = def.getImplementingClass();
					String id = theReq.getParameter("resource-tags-id");
					if (isNotBlank(id)) {
						String vid = theReq.getParameter("resource-tags-vid");
						if (isNotBlank(vid)) {
							client.getTags().forResource(resType, id, vid).execute();
						} else {
							client.getTags().forResource(resType, id).execute();
						}
					} else {
						client.getTags().forResource(resType).execute();
					}
				} else {
					client.getTags().execute();
				}
				returnsResource = ResultType.TAGLIST;
				outcomeDescription = "Tag List";

			} else if ("page".equals(method)) {

				String url = defaultString(theReq.getParameter("page-url"));
				if (!url.startsWith(theServerBase)) {
					theContext.getVariables().put("errorMsg", "Invalid page URL: " + url);
					return;
				}

				url = url.replace("&amp;", "&");
				client.loadPage().url(url).execute();

				returnsResource = ResultType.BUNDLE;
				outcomeDescription = "Bundle Page";

			} else if ("delete".equals(method)) {
				RuntimeResourceDefinition def = getResourceType(theReq);
				String id = StringUtils.defaultString(theReq.getParameter("resource-delete-id"));
				if (StringUtils.isBlank(id)) {
					theContext.getVariables().put("errorMsg", "No ID specified");
					return;
				}

				returnsResource = ResultType.BUNDLE;
				outcomeDescription = "Delete Resource";

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

				returnsResource = ResultType.BUNDLE;
				outcomeDescription = "Resource History";

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
						theContext.getVariables().put("errorMsg",
								"Message body does not appear to be a valid FHIR resource instance document. Body should start with '<' (for XML encoding) or '{' (for JSON encoding).");
						return;
					}
				} catch (DataFormatException e) {
					ourLog.warn("Failed to parse resource", e);
					theContext.getVariables().put("errorMsg", "Failed to parse message body. Error was: " + e.getMessage());
					return;
				}

				if (validate) {
					client.validate(resource);
					outcomeDescription = "Validate Resource";
				} else {
					String id = theReq.getParameter("resource-create-id");
					if (isNotBlank(id)) {
						outcomeDescription = "Update Resource";
						client.update(id, resource);
					} else {
						outcomeDescription = "Create Resource";
						client.create(resource);
					}
				}
				returnsResource = ResultType.RESOURCE;

			} else if ("search".equals(method)) {
				IUntypedQuery search = client.search();
				IQuery query;
				if (isNotBlank(theReq.getParameter("resource"))) {
					query = search.forResource(getResourceType(theReq).getImplementingClass());
				} else {
					query = search.forAllResources();
				}

				outcomeDescription = "Search for Resources";

				int paramIdx = -1;
				while (true) {
					paramIdx++;

					String paramIdxString = Integer.toString(paramIdx);
					boolean shouldContinue = handleSearchParam(paramIdxString, theReq, query);
					if (!shouldContinue) {
						break;
					}
				}

				String[] incValues = theReq.getParameterValues(Constants.PARAM_INCLUDE);
				if (incValues != null) {
					for (String next : incValues) {
						if (isNotBlank(next)) {
							query.include(new Include(next));
						}
					}
				}

				String limit = theReq.getParameter("resource-search-limit");
				if (isNotBlank(limit)) {
					if (!limit.matches("[0-9]+")) {
						theContext.getVariables().put("errorMsg", "Search limit must be a numeric value.");
						return;
					}
					query.limitTo(Integer.parseInt(limit));
				}

				query.execute();
				returnsResource = ResultType.BUNDLE;

			} else {
				theContext.getVariables().put("errorMsg", "Invalid action: " + method);
				return;
			}

			latency = System.currentTimeMillis() - start;
		} catch (DataFormatException e) {
			ourLog.error("Failed to invoke method", e);
			returnsResource = ResultType.NONE;
		} catch (BaseServerResponseException e) {
			ourLog.error("Failed to invoke method", e);
			returnsResource = ResultType.NONE;
		} catch (Exception e) {
			ourLog.error("Failure during processing", e);
			returnsResource = ResultType.NONE;
		}

		try {
			HttpRequestBase lastRequest = client.getLastRequest();
			String requestBody = null;
			String requestUrl = lastRequest != null ? lastRequest.getURI().toASCIIString() : null;
			String action = client.getLastRequest() != null ? client.getLastRequest().getMethod() : null;
			String resultStatus = client.getLastResponse() != null ? client.getLastResponse().getStatusLine().toString() : null;
			String resultBody = client.getLastResponseBody();

			if (lastRequest instanceof HttpEntityEnclosingRequest) {
				HttpEntity entity = ((HttpEntityEnclosingRequest) lastRequest).getEntity();
				if (entity.isRepeatable()) {
					requestBody = IOUtils.toString(entity.getContent());
				}
			}
			
			HttpResponse lastResponse = client.getLastResponse();
			ContentType ct = lastResponse != null ? ContentType.get(lastResponse.getEntity()) : null;
			String mimeType = ct != null ? ct.getMimeType() : null;
			EncodingEnum ctEnum = EncodingEnum.forContentType(mimeType);
			String narrativeString = "";

			StringBuilder resultDescription = new StringBuilder();
			Bundle bundle = null;

			if (ctEnum == null) {
				resultDescription.append("Non-FHIR response");
			} else {
				switch (ctEnum) {
				case JSON:
					if (returnsResource == ResultType.RESOURCE) {
						narrativeString = parseNarrative(ctEnum, resultBody);
						resultDescription.append("JSON resource");
					} else if (returnsResource == ResultType.BUNDLE) {
						resultDescription.append("JSON bundle");
						bundle = myCtx.newJsonParser().parseBundle(resultBody);
					}
					break;
				case XML:
				default:
					if (returnsResource == ResultType.RESOURCE) {
						narrativeString = parseNarrative(ctEnum, resultBody);
						resultDescription.append("XML resource");
					} else if (returnsResource == ResultType.BUNDLE) {
						resultDescription.append("XML bundle");
						bundle = myCtx.newXmlParser().parseBundle(resultBody);
					}
					break;
				}
			}

			resultDescription.append(" (").append(resultBody.length() + " bytes)");

			Header[] requestHeaders = lastRequest != null ? applyHeaderFilters(lastRequest.getAllHeaders()) : new Header[0];
			Header[] responseHeaders = lastResponse != null ? applyHeaderFilters(lastResponse.getAllHeaders()) : new Header[0];

			theContext.setVariable("outcomeDescription", outcomeDescription);
			theContext.setVariable("resultDescription", resultDescription.toString());
			theContext.setVariable("action", action);
			theContext.setVariable("bundle", bundle);
			theContext.setVariable("resultStatus", resultStatus);
			theContext.setVariable("requestUrl", requestUrl);
			String requestBodyText = format(requestBody, ctEnum);
			theContext.setVariable("requestBody", requestBodyText);
			String resultBodyText = format(resultBody, ctEnum);
			theContext.setVariable("resultBody", resultBodyText);
			theContext.setVariable("resultBodyIsLong", resultBodyText.length() > 1000);
			theContext.setVariable("requestHeaders", requestHeaders);
			theContext.setVariable("responseHeaders", responseHeaders);
			theContext.setVariable("narrative", narrativeString);
			theContext.setVariable("latencyMs", latency);

		} catch (Exception e) {
			ourLog.error("Failure during processing", e);
			theContext.getVariables().put("errorMsg", "Error during processing: " + e.getMessage());
		}
	}

	private boolean handleSearchParam(String paramIdxString, HttpServletRequest theReq, IQuery theQuery) {
		String nextName = theReq.getParameter("param." + paramIdxString + ".name");
		if (isBlank(nextName)) {
			return false;
		}
		
		String nextQualifier = StringUtils.defaultString(theReq.getParameter("param." + paramIdxString + ".qualifier"));

		String nextType = theReq.getParameter("param." + paramIdxString + ".type");

		StringBuilder b = new StringBuilder();
		for (int i = 0; i < 100; i++) {
			b.append(defaultString(theReq.getParameter("param." + paramIdxString + "." + i)));
		}

		String paramValue = b.toString();
		if (isBlank(paramValue)) {
			return true;
		}

		if ("token".equals(nextType)) {
			if (paramValue.length() < 2) {
				return true;
			}
		}

		// if ("xml".equals(theReq.getParameter("encoding"))) {
		// query.encodedXml();
		// }else if ("json".equals(theReq.getParameter("encoding"))) {
		// query.encodedJson();
		// }

		theQuery.where(new StringParam(nextName + nextQualifier).matches().value(paramValue));
		
		if (StringUtils.isNotBlank(theReq.getParameter("param." + paramIdxString + ".0.name"))) {
			handleSearchParam(paramIdxString+".0", theReq, theQuery);
		}
		
		return true;
	}

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
					} else if (nextChar == '}' || nextChar == '}' || nextChar == ',') {
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

	private EncodingEnum getRequestEncoding(HttpServletRequest theReq) {
		EncodingEnum encoding;
		if ("xml".equals(theReq.getParameter("encoding"))) {
			encoding = EncodingEnum.XML;
		} else if ("json".equals(theReq.getParameter("encoding"))) {
			encoding = (EncodingEnum.JSON);
		} else {
			encoding = null;
		}
		return encoding;
	}

	@Override
	protected void doGet(HttpServletRequest theReq, HttpServletResponse theResp) throws ServletException, IOException {
		if (DEBUGMODE) {
			myTemplateEngine.getCacheManager().clearAllCaches();
		}

		try {
			ourLog.trace("RequestURI: {}", theReq.getPathInfo());

			String resName = theReq.getPathInfo().substring(1);
			if (myStaticResources.containsKey(resName)) {
				streamResponse(resName, myStaticResources.get(resName), theResp);
				return;
			}

			String serverId = theReq.getParameter("server-id");
			String serverBase;
			String serverName;
			if (isBlank(serverId) && !myIdToServerBase.containsKey(serverId)) {
				serverBase = myIdToServerBase.entrySet().iterator().next().getValue();
				serverName = myIdToServerName.entrySet().iterator().next().getValue();
			}else {
				serverBase = myIdToServerBase.get(serverId);
				serverName = myIdToServerName.get(serverId);
			}
			
			IGenericClient client = myCtx.newRestfulGenericClient(serverBase);
			Conformance conformance = client.conformance();

			WebContext ctx = new WebContext(theReq, theResp, theReq.getServletContext(), theReq.getLocale());

			Map<String, Number> resourceCounts = new HashMap<String, Number>();
			long total = 0;
			for (Rest nextRest : conformance.getRest()) {
				for (RestResource nextResource : nextRest.getResource()) {
					List<ExtensionDt> exts = nextResource.getUndeclaredExtensionsByUrl(RESOURCE_COUNT_EXT_URL);
					if (exts != null && exts.size() > 0) {
						Number nextCount = ((DecimalDt) (exts.get(0).getValue())).getValueAsNumber();
						resourceCounts.put(nextResource.getType().getValue(), nextCount);
						total += nextCount.longValue();
					}
				}
			}
			if (total > 0) {
				for (Rest nextRest : conformance.getRest()) {
					Collections.sort(nextRest.getResource(), new Comparator<RestResource>() {
						@Override
						public int compare(RestResource theO1, RestResource theO2) {
							DecimalDt count1 = new DecimalDt();
							List<ExtensionDt> count1exts = theO1.getUndeclaredExtensionsByUrl(RESOURCE_COUNT_EXT_URL);
							if (count1exts != null && count1exts.size() > 0) {
								count1 = (DecimalDt) count1exts.get(0).getValue();
							}
							DecimalDt count2 = new DecimalDt();
							List<ExtensionDt> count2exts = theO2.getUndeclaredExtensionsByUrl(RESOURCE_COUNT_EXT_URL);
							if (count2exts != null && count2exts.size() > 0) {
								count2 = (DecimalDt) count2exts.get(0).getValue();
							}
							int retVal = count2.compareTo(count1);
							if (retVal == 0) {
								retVal = theO1.getType().getValue().compareTo(theO2.getType().getValue());
							}
							return retVal;
						}
					});
				}
			}

			ctx.setVariable("serverId", serverId);
			ctx.setVariable("resourceCounts", resourceCounts);
			ctx.setVariable("conf", conformance);
			ctx.setVariable("base", serverBase);
			ctx.setVariable("baseName", serverName);
			ctx.setVariable("serverEntries", myIdToServerName.entrySet());
			String resourceName = defaultString(theReq.getParameter(PARAM_RESOURCE));
			ctx.setVariable("resourceName", resourceName);
			ctx.setVariable("jsonEncodedConf", myCtx.newJsonParser().encodeResourceToString(conformance));
			addStandardVariables(ctx, theReq.getParameterMap());

			if (isNotBlank(theReq.getParameter("action"))) {
				processAction(theReq, ctx, client, serverBase);
			}

			if (isNotBlank(resourceName)) {
				RuntimeResourceDefinition def = myCtx.getResourceDefinition(resourceName);
				TreeSet<String> includes = new TreeSet<String>();
				for (Rest nextRest : conformance.getRest()) {
					for (RestResource nextRes : nextRest.getResource()) {
						if (nextRes.getType().getValue().equals(resourceName)) {
							for (StringDt next : nextRes.getSearchInclude()) {
								if (next.isEmpty() == false) {
									includes.add(next.getValue());
								}
							}
						}
					}
				}
				ctx.setVariable("includes", includes);

				if (isNotBlank(theReq.getParameter("update-id"))) {
					String updateId = theReq.getParameter("update-id");
					String updateVid = defaultIfEmpty(theReq.getParameter("update-vid"), null);
					IResource updateResource = client.read(def.getImplementingClass(), new IdDt(resourceName, updateId, updateVid));
					EncodingEnum encoding = getRequestEncoding(theReq);
					if (encoding == null) {
						encoding = EncodingEnum.XML;
					}
					String updateResourceString = encoding.newParser(myCtx).setPrettyPrint(true).encodeResourceToString(updateResource);
					ctx.setVariable("updateResource", updateResourceString);
					ctx.setVariable("updateResourceId", updateId);
				}

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
	 * If set, the headers named here will be stripped from requests/responses before they are displayed to the user. This can be used, for instance, to filter out "Authorization" headers. Note that
	 * names are not case sensitive.
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
