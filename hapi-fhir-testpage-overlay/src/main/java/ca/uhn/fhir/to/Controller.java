package ca.uhn.fhir.to;

import static org.apache.commons.lang3.StringUtils.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.thymeleaf.TemplateEngine;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.api.ExtensionDt;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.model.dstu.resource.Conformance;
import ca.uhn.fhir.model.dstu.resource.Conformance.Rest;
import ca.uhn.fhir.model.dstu.resource.Conformance.RestResource;
import ca.uhn.fhir.model.primitive.DateTimeDt;
import ca.uhn.fhir.model.primitive.DecimalDt;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.rest.client.GenericClient;
import ca.uhn.fhir.rest.gclient.IQuery;
import ca.uhn.fhir.rest.gclient.IUntypedQuery;
import ca.uhn.fhir.rest.gclient.StringParam;
import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.rest.server.EncodingEnum;
import ca.uhn.fhir.to.model.HomeRequest;
import ca.uhn.fhir.to.model.ResourceRequest;
import ca.uhn.fhir.to.model.TransactionRequest;

@org.springframework.stereotype.Controller()
public class Controller {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(Controller.class);
	private static final String PARAM_RESOURCE = "resource";
	private static final String RESOURCE_COUNT_EXT_URL = "http://hl7api.sourceforge.net/hapi-fhir/res/extdefs.html#resourceCount";

	@Autowired
	private TesterConfig myConfig;

	@Autowired
	private FhirContext myCtx;
	private List<String> myFilterHeaders;

	@Autowired
	private TemplateEngine myTemplateEngine;

	@RequestMapping(value = { "/transaction" })
	public String actionTransaction(final TransactionRequest theRequest, final BindingResult theBindingResult, final ModelMap theModel) {
		addCommonParams(theRequest, theModel);

		GenericClient client = theRequest.newClient(myCtx, myConfig);
		loadAndAddConformance(theRequest, theModel, client);

		String body = preProcessMessageBody(theRequest.getTransactionBody());

		Bundle bundle;
		try {
			if (body.startsWith("{")) {
				bundle = myCtx.newJsonParser().parseBundle(body);
			} else if (body.startsWith("<")) {
				bundle = myCtx.newXmlParser().parseBundle(body);
			} else {
				theModel.put("errorMsg", "Message body does not appear to be a valid FHIR resource instance document. Body should start with '<' (for XML encoding) or '{' (for JSON encoding).");
				return "home";
			}
		} catch (DataFormatException e) {
			ourLog.warn("Failed to parse bundle", e);
			theModel.put("errorMsg", "Failed to parse transaction bundle body. Error was: " + e.getMessage());
			return "home";
		}

		long start = System.currentTimeMillis();
		client.transaction().withBundle(bundle).execute();
		long delay = System.currentTimeMillis() - start;

		processAndAddLastClientInvocation(client, ResultType.BUNDLE, theModel, delay, "Transaction");

		return "result";
	}

	@RequestMapping(value = { "/conformance" })
	public String actionConformance(final HomeRequest theRequest, final BindingResult theBindingResult, final ModelMap theModel) {
		addCommonParams(theRequest, theModel);

		GenericClient client = theRequest.newClient(myCtx, myConfig);

		long start = System.currentTimeMillis();
		loadAndAddConformance(theRequest, theModel, client);
		long delay = System.currentTimeMillis() - start;

		processAndAddLastClientInvocation(client, ResultType.RESOURCE, theModel, delay, "Loaded conformance");

		return "result";
	}

	@RequestMapping(value = { "/create" })
	public String actionCreate(final HttpServletRequest theReq, final HomeRequest theRequest, final BindingResult theBindingResult, final ModelMap theModel) {
		doActionCreateOrValidate(theReq, theRequest, theBindingResult, theModel, "create");
		return "result";
	}

	@RequestMapping(value = { "/delete" })
	public String actionDelete(HttpServletRequest theReq, HomeRequest theRequest, BindingResult theBindingResult, ModelMap theModel) {
		addCommonParams(theRequest, theModel);

		GenericClient client = theRequest.newClient(myCtx, myConfig);

		loadAndAddConformance(theRequest, theModel, client);

		RuntimeResourceDefinition def;
		try {
			def = getResourceType(theReq);
		} catch (ServletException e) {
			theModel.put("errorMsg", e.toString());
			return "resource";
		}

		String id = StringUtils.defaultString(theReq.getParameter("resource-delete-id"));
		if (StringUtils.isBlank(id)) {
			theModel.put("errorMsg", "No ID specified");
			return "resource";
		}

		ResultType returnsResource = ResultType.BUNDLE;
		String outcomeDescription = "Delete Resource";

		long start = System.currentTimeMillis();
		try {
			client.delete(def.getImplementingClass(), new IdDt(id));
		} catch (Exception e) {
			returnsResource = ResultType.NONE;
			ourLog.warn("Failed to invoke server", e);
		}
		long delay = System.currentTimeMillis() - start;
		processAndAddLastClientInvocation(client, returnsResource, theModel, delay, outcomeDescription);

		return "result";
	}

	@RequestMapping(value = { "/get-tags" })
	public String actionGetTags(HttpServletRequest theReq, HomeRequest theRequest, BindingResult theBindingResult, ModelMap theModel) {
		addCommonParams(theRequest, theModel);

		GenericClient client = theRequest.newClient(myCtx, myConfig);

		loadAndAddConformance(theRequest, theModel, client);

		Class<? extends IResource> resType = null;
		long start = System.currentTimeMillis();
		if (isNotBlank(theReq.getParameter(PARAM_RESOURCE))) {
			RuntimeResourceDefinition def;
			try {
				def = getResourceType(theReq);
			} catch (ServletException e) {
				theModel.put("errorMsg", e.toString());
				return "resource";
			}

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
		long delay = System.currentTimeMillis() - start;

		ResultType returnsResource = ResultType.TAGLIST;
		String outcomeDescription = "Tag List";

		processAndAddLastClientInvocation(client, returnsResource, theModel, delay, outcomeDescription);

		return "result";
	}

	@RequestMapping(value = { "/history-server" })
	public String actionHistoryServer(final HttpServletRequest theReq, final HomeRequest theRequest, final BindingResult theBindingResult, final ModelMap theModel) {
		doActionHistory(theReq, theRequest, theBindingResult, theModel, "history-server", "Server History");
		return "result";
	}

	@RequestMapping(value = { "/history-type" })
	public String actionHistoryType(final HttpServletRequest theReq, final HomeRequest theRequest, final BindingResult theBindingResult, final ModelMap theModel) {
		doActionHistory(theReq, theRequest, theBindingResult, theModel, "history-type", "History");
		return "result";
	}

	@RequestMapping(value = { "/", "/home" })
	public String actionHome(final HomeRequest theRequest, final BindingResult theBindingResult, final ModelMap theModel) {
		addCommonParams(theRequest, theModel);

		GenericClient client = theRequest.newClient(myCtx, myConfig);
		loadAndAddConformance(theRequest, theModel, client);

		return "home";
	}

	@RequestMapping(value = { "/page" })
	public String actionPage(HttpServletRequest theReq, HomeRequest theRequest, BindingResult theBindingResult, ModelMap theModel) {
		addCommonParams(theRequest, theModel);

		GenericClient client = theRequest.newClient(myCtx, myConfig);

		loadAndAddConformance(theRequest, theModel, client);

		String url = defaultString(theReq.getParameter("page-url"));
		if (!url.startsWith(theModel.get("base").toString())) {
			theModel.put("errorMsg", "Invalid page URL: " + url);
			return "result";
		}

		url = url.replace("&amp;", "&");

		long start = System.currentTimeMillis();
		client.loadPage().url(url).execute();
		long delay = System.currentTimeMillis() - start;

		ResultType returnsResource = ResultType.BUNDLE;
		String outcomeDescription = "Bundle Page";

		processAndAddLastClientInvocation(client, returnsResource, theModel, delay, outcomeDescription);

		return "result";
	}

	@RequestMapping(value = { "/read" })
	public String actionRead(HttpServletRequest theReq, HomeRequest theRequest, BindingResult theBindingResult, ModelMap theModel) {
		addCommonParams(theRequest, theModel);

		GenericClient client = theRequest.newClient(myCtx, myConfig);

		loadAndAddConformance(theRequest, theModel, client);

		RuntimeResourceDefinition def;
		try {
			def = getResourceType(theReq);
		} catch (ServletException e) {
			theModel.put("errorMsg", e.toString());
			return "resource";
		}
		String id = StringUtils.defaultString(theReq.getParameter("id"));
		if (StringUtils.isBlank(id)) {
			theModel.put("errorMsg", "No ID specified");
			return "resource";
		}
		ResultType returnsResource = ResultType.RESOURCE;

		String versionId = StringUtils.defaultString(theReq.getParameter("vid"));
		String outcomeDescription;
		if (StringUtils.isBlank(versionId)) {
			versionId = null;
			outcomeDescription = "Read Resource";
		} else {
			outcomeDescription = "VRead Resource";
		}

		long start = System.currentTimeMillis();
		try {
			client.read(def.getImplementingClass(), new IdDt(def.getName(), id, versionId));
		} catch (Exception e) {
			returnsResource = ResultType.NONE;
			ourLog.warn("Failed to invoke server", e);
		}
		long delay = System.currentTimeMillis() - start;

		processAndAddLastClientInvocation(client, returnsResource, theModel, delay, outcomeDescription);

		return "result";
	}

	@RequestMapping({ "/resource" })
	public String actionResource(final ResourceRequest theRequest, final BindingResult theBindingResult, final ModelMap theModel) {
		addCommonParams(theRequest, theModel);

		GenericClient client = theRequest.newClient(myCtx, myConfig);
		Conformance conformance = loadAndAddConformance(theRequest, theModel, client);

		String resourceName = theRequest.getResource();
		RuntimeResourceDefinition def = myCtx.getResourceDefinition(theRequest.getResource());

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
		theModel.put("includes", includes);

		if (isNotBlank(theRequest.getUpdateId())) {
			String updateId = theRequest.getUpdateId();
			String updateVid = defaultIfEmpty(theRequest.getUpdateVid(), null);
			IResource updateResource = client.read(def.getImplementingClass(), new IdDt(resourceName, updateId, updateVid));
			String updateResourceString = theRequest.newParser(myCtx).setPrettyPrint(true).encodeResourceToString(updateResource);
			theModel.put("updateResource", updateResourceString);
			theModel.put("updateResourceId", updateId);
		}

		return "resource";
	}

	@RequestMapping(value = { "/search" })
	public String actionSearch(HttpServletRequest theReq, HomeRequest theRequest, BindingResult theBindingResult, ModelMap theModel) {
		addCommonParams(theRequest, theModel);

		GenericClient client = theRequest.newClient(myCtx, myConfig);

		loadAndAddConformance(theRequest, theModel, client);

		IUntypedQuery search = client.search();
		IQuery query;
		if (isNotBlank(theReq.getParameter("resource"))) {
			try {
				query = search.forResource(getResourceType(theReq).getImplementingClass());
			} catch (ServletException e) {
				theModel.put("errorMsg", e.toString());
				return "resource";
			}
		} else {
			query = search.forAllResources();
		}

		String outcomeDescription = "Search for Resources";

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
				theModel.put("errorMsg", "Search limit must be a numeric value.");
				return "resource";
			}
			query.limitTo(Integer.parseInt(limit));
		}

		long start = System.currentTimeMillis();
		ResultType returnsResource;
		try {
			query.execute();
			returnsResource = ResultType.BUNDLE;
		} catch (Exception e) {
			returnsResource = ResultType.NONE;
			ourLog.warn("Failed to invoke server", e);
		}
		long delay = System.currentTimeMillis() - start;

		processAndAddLastClientInvocation(client, returnsResource, theModel, delay, outcomeDescription);

		return "result";
	}

	@RequestMapping(value = { "/validate" })
	public String actionValidate(final HttpServletRequest theReq, final HomeRequest theRequest, final BindingResult theBindingResult, final ModelMap theModel) {
		doActionCreateOrValidate(theReq, theRequest, theBindingResult, theModel, "validate");
		return "result";
	}

	private void addCommonParams(final HomeRequest theRequest, final ModelMap theModel) {
		if (myConfig.getDebugTemplatesMode()) {
			myTemplateEngine.getCacheManager().clearAllCaches();
		}

		final String serverId = theRequest.getServerIdWithDefault(myConfig);
		final String serverBase = theRequest.getServerBase(myConfig);
		final String serverName = theRequest.getServerName(myConfig);
		theModel.put("serverId", serverId);
		theModel.put("base", serverBase);
		theModel.put("baseName", serverName);
		theModel.put("resourceName", defaultString(theRequest.getResource()));
		theModel.put("encoding", theRequest.getEncoding());
		theModel.put("pretty", theRequest.getPretty());
		theModel.put("serverEntries", myConfig.getIdToServerName());

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

	private void doActionCreateOrValidate(HttpServletRequest theReq, HomeRequest theRequest, BindingResult theBindingResult, ModelMap theModel, String theMethod) {
		boolean validate = "validate".equals(theMethod);

		addCommonParams(theRequest, theModel);

		GenericClient client = theRequest.newClient(myCtx, myConfig);

		loadAndAddConformance(theRequest, theModel, client);

		Class<? extends IResource> type = null; // def.getImplementingClass();
		if ("history-type".equals(theMethod)) {
			RuntimeResourceDefinition def = myCtx.getResourceDefinition(theRequest.getResource());
			type = def.getImplementingClass();
		}

		String body = validate ? theReq.getParameter("resource-validate-body") : theReq.getParameter("resource-create-body");
		if (isBlank(body)) {
			theModel.put("errorMsg", "No message body specified");
			return;
		}

		body = preProcessMessageBody(body);

		IResource resource;
		try {
			if (body.startsWith("{")) {
				resource = myCtx.newJsonParser().parseResource(type, body);
			} else if (body.startsWith("<")) {
				resource = myCtx.newXmlParser().parseResource(type, body);
			} else {
				theModel.put("errorMsg", "Message body does not appear to be a valid FHIR resource instance document. Body should start with '<' (for XML encoding) or '{' (for JSON encoding).");
				return;
			}
		} catch (DataFormatException e) {
			ourLog.warn("Failed to parse resource", e);
			theModel.put("errorMsg", "Failed to parse message body. Error was: " + e.getMessage());
			return;
		}

		String outcomeDescription;

		long start = System.currentTimeMillis();
		ResultType returnsResource = ResultType.RESOURCE;
		outcomeDescription = "";
		try {
			if (validate) {
				outcomeDescription = "Validate Resource";
				client.validate(resource);
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
		} catch (Exception e) {
			returnsResource = ResultType.NONE;
			ourLog.warn("Failed to invoke server", e);
		}
		long delay = System.currentTimeMillis() - start;

		processAndAddLastClientInvocation(client, returnsResource, theModel, delay, outcomeDescription);

	}

	private String preProcessMessageBody(String theBody) {
		if(theBody==null) {
			return "";
		}
		String retVal = theBody.trim();

		StringBuilder b = new StringBuilder();
		for (int i = 0; i < retVal.length(); i++) {
			char nextChar = retVal.charAt(i);
			int nextCharI = nextChar;
			if (nextCharI == 65533) {
				continue;
			}
			b.append(nextChar);
		}
		retVal = b.toString();
		return retVal;
	}

	private void doActionHistory(HttpServletRequest theReq, HomeRequest theRequest, BindingResult theBindingResult, ModelMap theModel, String theMethod, String theMethodDescription) {
		addCommonParams(theRequest, theModel);

		GenericClient client = theRequest.newClient(myCtx, myConfig);

		loadAndAddConformance(theRequest, theModel, client);

		String id = null;
		Class<? extends IResource> type = null; // def.getImplementingClass();
		if ("history-type".equals(theMethod)) {
			RuntimeResourceDefinition def = myCtx.getResourceDefinition(theRequest.getResource());
			type = def.getImplementingClass();
			id = StringUtils.defaultString(theReq.getParameter("resource-history-id"));
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

		long start = System.currentTimeMillis();
		client.history(type, id, since, limit);
		long delay = System.currentTimeMillis() - start;

		processAndAddLastClientInvocation(client, ResultType.BUNDLE, theModel, delay, theMethodDescription);

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

	private RuntimeResourceDefinition getResourceType(HttpServletRequest theReq) throws ServletException {
		String resourceName = StringUtils.defaultString(theReq.getParameter(PARAM_RESOURCE));
		RuntimeResourceDefinition def = myCtx.getResourceDefinition(resourceName);
		if (def == null) {
			throw new ServletException("Invalid resourceName: " + resourceName);
		}
		return def;
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
			handleSearchParam(paramIdxString + ".0", theReq, theQuery);
		}

		return true;
	}

	private Conformance loadAndAddConformance(final HomeRequest theRequest, final ModelMap theModel, GenericClient theClient) {
		Conformance conformance;
		try {
			conformance = theClient.conformance();
		} catch (Exception e) {
			ourLog.warn("Failed to load conformance statement", e);
			theModel.put("errorMsg", "Failed to load conformance statement, error was: " + e.toString());
			conformance = new Conformance();
		}

		theModel.put("jsonEncodedConf", myCtx.newJsonParser().encodeResourceToString(conformance));

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
		theModel.put("resourceCounts", resourceCounts);

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
		theModel.put("conf", conformance);
		return conformance;
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

	private void processAndAddLastClientInvocation(GenericClient theClient, ResultType theResultType, ModelMap theModelMap, long theLatency, String outcomeDescription) {
		try {
			HttpRequestBase lastRequest = theClient.getLastRequest();
			String requestBody = null;
			String requestUrl = lastRequest != null ? lastRequest.getURI().toASCIIString() : null;
			String action = theClient.getLastRequest() != null ? theClient.getLastRequest().getMethod() : null;
			String resultStatus = theClient.getLastResponse() != null ? theClient.getLastResponse().getStatusLine().toString() : null;
			String resultBody = theClient.getLastResponseBody();

			if (lastRequest instanceof HttpEntityEnclosingRequest) {
				HttpEntity entity = ((HttpEntityEnclosingRequest) lastRequest).getEntity();
				if (entity.isRepeatable()) {
					requestBody = IOUtils.toString(entity.getContent());
				}
			}

			HttpResponse lastResponse = theClient.getLastResponse();
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
					if (theResultType == ResultType.RESOURCE) {
						narrativeString = parseNarrative(ctEnum, resultBody);
						resultDescription.append("JSON resource");
					} else if (theResultType == ResultType.BUNDLE) {
						resultDescription.append("JSON bundle");
						bundle = myCtx.newJsonParser().parseBundle(resultBody);
					}
					break;
				case XML:
				default:
					if (theResultType == ResultType.RESOURCE) {
						narrativeString = parseNarrative(ctEnum, resultBody);
						resultDescription.append("XML resource");
					} else if (theResultType == ResultType.BUNDLE) {
						resultDescription.append("XML bundle");
						bundle = myCtx.newXmlParser().parseBundle(resultBody);
					}
					break;
				}
			}

			resultDescription.append(" (").append(resultBody.length() + " bytes)");

			Header[] requestHeaders = lastRequest != null ? applyHeaderFilters(lastRequest.getAllHeaders()) : new Header[0];
			Header[] responseHeaders = lastResponse != null ? applyHeaderFilters(lastResponse.getAllHeaders()) : new Header[0];

			theModelMap.put("outcomeDescription", outcomeDescription);
			theModelMap.put("resultDescription", resultDescription.toString());
			theModelMap.put("action", action);
			theModelMap.put("bundle", bundle);
			theModelMap.put("resultStatus", resultStatus);
			theModelMap.put("requestUrl", requestUrl);
			String requestBodyText = format(requestBody, ctEnum);
			theModelMap.put("requestBody", requestBodyText);
			String resultBodyText = format(resultBody, ctEnum);
			theModelMap.put("resultBody", resultBodyText);
			theModelMap.put("resultBodyIsLong", resultBodyText.length() > 1000);
			theModelMap.put("requestHeaders", requestHeaders);
			theModelMap.put("responseHeaders", responseHeaders);
			theModelMap.put("narrative", narrativeString);
			theModelMap.put("latencyMs", theLatency);

		} catch (Exception e) {
			ourLog.error("Failure during processing", e);
			theModelMap.put("errorMsg", "Error during processing: " + e.getMessage());
		}

	}

	private enum ResultType {
		BUNDLE, NONE, RESOURCE, TAGLIST
	}

}
