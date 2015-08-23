package ca.uhn.fhir.to;

import static org.apache.commons.lang3.StringUtils.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import javax.json.Json;
import javax.json.stream.JsonGenerator;
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
import org.apache.http.entity.HttpEntityWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.thymeleaf.TemplateEngine;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.api.BundleEntry;
import ca.uhn.fhir.model.api.ExtensionDt;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.model.dstu.resource.Conformance;
import ca.uhn.fhir.model.dstu.resource.Conformance.Rest;
import ca.uhn.fhir.model.dstu.resource.Conformance.RestQuery;
import ca.uhn.fhir.model.dstu.resource.Conformance.RestResource;
import ca.uhn.fhir.model.dstu.resource.Conformance.RestResourceSearchParam;
import ca.uhn.fhir.model.dstu.valueset.SearchParamTypeEnum;
import ca.uhn.fhir.model.dstu2.valueset.ResourceTypeEnum;
import ca.uhn.fhir.model.primitive.BoundCodeDt;
import ca.uhn.fhir.model.primitive.DateTimeDt;
import ca.uhn.fhir.model.primitive.DecimalDt;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.narrative.INarrativeGenerator;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.rest.client.GenericClient;
import ca.uhn.fhir.rest.client.IClientInterceptor;
import ca.uhn.fhir.rest.client.IGenericClient;
import ca.uhn.fhir.rest.gclient.ICreateTyped;
import ca.uhn.fhir.rest.gclient.IQuery;
import ca.uhn.fhir.rest.gclient.IUntypedQuery;
import ca.uhn.fhir.rest.gclient.NumberClientParam.IMatches;
import ca.uhn.fhir.rest.gclient.QuantityClientParam;
import ca.uhn.fhir.rest.gclient.QuantityClientParam.IAndUnits;
import ca.uhn.fhir.rest.gclient.StringClientParam;
import ca.uhn.fhir.rest.gclient.TokenClientParam;
import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.rest.server.EncodingEnum;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.to.model.HomeRequest;
import ca.uhn.fhir.to.model.ResourceRequest;
import ca.uhn.fhir.to.model.TransactionRequest;
import ca.uhn.fhir.util.ExtensionConstants;

@org.springframework.stereotype.Controller()
public class Controller {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(Controller.class);
	private static final String PARAM_RESOURCE = "resource";
	private static final String RESOURCE_COUNT_EXT_URL = "http://hl7api.sourceforge.net/hapi-fhir/res/extdefs.html#resourceCount";

	@Autowired
	private TesterConfig myConfig;

	private Map<FhirVersionEnum, FhirContext> myContexts = new HashMap<FhirVersionEnum, FhirContext>();

	private List<String> myFilterHeaders;

	@Autowired
	private TemplateEngine myTemplateEngine;

	@RequestMapping(value = { "/about" })
	public String actionAbout(HttpServletRequest theServletRequest, final HomeRequest theRequest, final ModelMap theModel) {
		addCommonParams(theServletRequest, theRequest, theModel);

		theModel.put("notHome", true);
		theModel.put("extraBreadcrumb", "About");

		ourLog.info(logPrefix(theModel) + "Displayed about page");

		return "about";
	}

	@RequestMapping(value = { "/conformance" })
	public String actionConformance(HttpServletRequest theServletRequest, final HomeRequest theRequest, final BindingResult theBindingResult, final ModelMap theModel) {
		addCommonParams(theServletRequest, theRequest, theModel);

		CaptureInterceptor interceptor = new CaptureInterceptor();
		GenericClient client = theRequest.newClient(theServletRequest, getContext(theRequest), myConfig, interceptor);
		ResultType returnsResource = ResultType.RESOURCE;

		long start = System.currentTimeMillis();
		try {
			client.conformance();
		} catch (Exception e) {
			returnsResource = handleClientException(client, e, theModel);
		}
		long delay = System.currentTimeMillis() - start;

		processAndAddLastClientInvocation(client, returnsResource, theModel, delay, "Loaded conformance", interceptor, theRequest);

		ourLog.info(logPrefix(theModel) + "Displayed conformance profile");

		return "result";
	}

	@RequestMapping(value = { "/create" })
	public String actionCreate(final HttpServletRequest theReq, final HomeRequest theRequest, final BindingResult theBindingResult, final ModelMap theModel) {
		doActionCreateOrValidate(theReq, theRequest, theBindingResult, theModel, "create");
		return "result";
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = { "/delete" })
	public String actionDelete(HttpServletRequest theReq, HomeRequest theRequest, BindingResult theBindingResult, ModelMap theModel) {
		addCommonParams(theReq, theRequest, theModel);

		CaptureInterceptor interceptor = new CaptureInterceptor();
		GenericClient client = theRequest.newClient(theReq, getContext(theRequest), myConfig, interceptor);

		RuntimeResourceDefinition def;
		try {
			def = getResourceType(theRequest, theReq);
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
			client.delete((Class<? extends IResource>) def.getImplementingClass(), new IdDt(id));
		} catch (Exception e) {
			returnsResource = handleClientException(client, e, theModel);
		}
		long delay = System.currentTimeMillis() - start;
		processAndAddLastClientInvocation(client, returnsResource, theModel, delay, outcomeDescription, interceptor, theRequest);

		ourLog.info(logPrefix(theModel) + "Deleted resource of type " + def.getName());

		return "result";
	}

	@RequestMapping(value = { "/get-tags" })
	public String actionGetTags(HttpServletRequest theReq, HomeRequest theRequest, BindingResult theBindingResult, ModelMap theModel) {
		addCommonParams(theReq, theRequest, theModel);

		CaptureInterceptor interceptor = new CaptureInterceptor();
		GenericClient client = theRequest.newClient(theReq, getContext(theRequest), myConfig, interceptor);

		Class<? extends IResource> resType = null;
		ResultType returnsResource = ResultType.TAGLIST;
		String outcomeDescription = "Tag List";

		long start = System.currentTimeMillis();
		try {
			if (isNotBlank(theReq.getParameter(PARAM_RESOURCE))) {
				RuntimeResourceDefinition def;
				try {
					def = getResourceType(theRequest, theReq);
				} catch (ServletException e) {
					theModel.put("errorMsg", e.toString());
					return "resource";
				}

				resType = (Class<? extends IResource>) def.getImplementingClass();
				String id = theReq.getParameter("resource-tags-id");
				if (isNotBlank(id)) {
					String vid = theReq.getParameter("resource-tags-vid");
					if (isNotBlank(vid)) {
						client.getTags().forResource(resType, id, vid).execute();
						ourLog.info(logPrefix(theModel) + "Got tags for type " + def.getName() + " ID " + id + " version" + vid);
					} else {
						client.getTags().forResource(resType, id).execute();
						ourLog.info(logPrefix(theModel) + "Got tags for type " + def.getName() + " ID " + id);
					}
				} else {
					client.getTags().forResource(resType).execute();
					ourLog.info(logPrefix(theModel) + "Got tags for type " + def.getName());
				}
			} else {
				client.getTags().execute();
				ourLog.info(logPrefix(theModel) + "Got tags for server");
			}
		} catch (Exception e) {
			returnsResource = handleClientException(client, e, theModel);
		}
		long delay = System.currentTimeMillis() - start;

		processAndAddLastClientInvocation(client, returnsResource, theModel, delay, outcomeDescription, interceptor, theRequest);

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
	public String actionHome(HttpServletRequest theServletRequest, final HomeRequest theRequest, final BindingResult theBindingResult, final ModelMap theModel) {
		addCommonParams(theServletRequest, theRequest, theModel);
		ourLog.info(theServletRequest.toString());
		return "home";
	}

	@RequestMapping(value = { "/page" })
	public String actionPage(HttpServletRequest theReq, HomeRequest theRequest, BindingResult theBindingResult, ModelMap theModel) {
		addCommonParams(theReq, theRequest, theModel);

		CaptureInterceptor interceptor = new CaptureInterceptor();
		GenericClient client = theRequest.newClient(theReq, getContext(theRequest), myConfig, interceptor);

		String url = defaultString(theReq.getParameter("page-url"));
		if (!url.startsWith(theModel.get("base").toString())) {
			ourLog.warn(logPrefix(theModel) + "Refusing to load page URL: {}", url);
			theModel.put("errorMsg", "Invalid page URL: " + url);
			return "result";
		}

		url = url.replace("&amp;", "&");

		ResultType returnsResource = ResultType.BUNDLE;

		long start = System.currentTimeMillis();
		try {
			ourLog.info(logPrefix(theModel) + "Loading paging URL: {}", url);
			client.loadPage().url(url).execute();
		} catch (Exception e) {
			returnsResource = handleClientException(client, e, theModel);
		}
		long delay = System.currentTimeMillis() - start;

		String outcomeDescription = "Bundle Page";

		processAndAddLastClientInvocation(client, returnsResource, theModel, delay, outcomeDescription, interceptor, theRequest);

		return "result";
	}

	@RequestMapping(value = { "/read" })
	public String actionRead(HttpServletRequest theReq, HomeRequest theRequest, BindingResult theBindingResult, ModelMap theModel) {
		addCommonParams(theReq, theRequest, theModel);

		CaptureInterceptor interceptor = new CaptureInterceptor();
		GenericClient client = theRequest.newClient(theReq, getContext(theRequest), myConfig, interceptor);

		RuntimeResourceDefinition def;
		try {
			def = getResourceType(theRequest, theReq);
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
			IdDt resid = new IdDt(def.getName(), id, versionId);
			ourLog.info(logPrefix(theModel) + "Reading resource: {}", resid);
			client.read(def.getImplementingClass(), resid);
		} catch (Exception e) {
			returnsResource = handleClientException(client, e, theModel);
		}
		long delay = System.currentTimeMillis() - start;

		processAndAddLastClientInvocation(client, returnsResource, theModel, delay, outcomeDescription, interceptor, theRequest);

		return "result";
	}

	@RequestMapping({ "/resource" })
	public String actionResource(HttpServletRequest theServletRequest, final ResourceRequest theRequest, final BindingResult theBindingResult, final ModelMap theModel) {
		IResource conformance = addCommonParams(theServletRequest, theRequest, theModel);

		CaptureInterceptor interceptor = new CaptureInterceptor();
		GenericClient client = theRequest.newClient(theServletRequest, getContext(theRequest), myConfig, interceptor);

		String resourceName = theRequest.getResource();
		RuntimeResourceDefinition def = getContext(theRequest).getResourceDefinition(theRequest.getResource());

		TreeSet<String> includes = new TreeSet<String>();
		TreeSet<String> revIncludes = new TreeSet<String>();
		TreeSet<String> sortParams = new TreeSet<String>();
		List<RestQuery> queries = new ArrayList<Conformance.RestQuery>();
		boolean haveSearchParams = false;
		List<List<String>> queryIncludes = new ArrayList<List<String>>();

		switch (theRequest.getFhirVersion(myConfig)) {
		case DEV:
		case DSTU2:
			haveSearchParams = extractSearchParamsDev(conformance, resourceName, includes, revIncludes, sortParams, queries, haveSearchParams, queryIncludes);
			break;
		case DSTU1:
			haveSearchParams = extractSearchParamsDstu1(conformance, resourceName, includes, sortParams, queries, haveSearchParams, queryIncludes);
			break;
		default:
			throw new IllegalStateException("Unknown FHIR version: " + theRequest.getFhirVersion(myConfig));
		}

		theModel.put("includes", includes);
		theModel.put("revincludes", revIncludes);
		theModel.put("queries", queries);
		theModel.put("haveSearchParams", haveSearchParams);
		theModel.put("queryIncludes", queryIncludes);
		theModel.put("sortParams", sortParams);

		if (isNotBlank(theRequest.getUpdateId())) {
			String updateId = theRequest.getUpdateId();
			String updateVid = defaultIfEmpty(theRequest.getUpdateVid(), null);
			IResource updateResource = (IResource) client.read(def.getImplementingClass(), new IdDt(resourceName, updateId, updateVid));
			String updateResourceString = theRequest.newParser(getContext(theRequest)).setPrettyPrint(true).encodeResourceToString(updateResource);
			theModel.put("updateResource", updateResourceString);
			theModel.put("updateResourceId", updateId);
		}

		ourLog.info(logPrefix(theModel) + "Showing resource page: {}", resourceName);

		return "resource";
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = { "/search" })
	public String actionSearch(HttpServletRequest theReq, HomeRequest theRequest, BindingResult theBindingResult, ModelMap theModel) {
		addCommonParams(theReq, theRequest, theModel);

		StringWriter clientCodeJsonStringWriter = new StringWriter();
		JsonGenerator clientCodeJsonWriter = Json.createGenerator(clientCodeJsonStringWriter);
		clientCodeJsonWriter.writeStartObject();
		clientCodeJsonWriter.write("action", "search");
		clientCodeJsonWriter.write("base", (String) theModel.get("base"));

		CaptureInterceptor interceptor = new CaptureInterceptor();
		GenericClient client = theRequest.newClient(theReq, getContext(theRequest), myConfig, interceptor);

		IUntypedQuery search = client.search();
		IQuery query;
		if (isNotBlank(theReq.getParameter("resource"))) {
			try {
				query = search.forResource((Class<? extends IResource>) getResourceType(theRequest, theReq).getImplementingClass());
			} catch (ServletException e) {
				theModel.put("errorMsg", e.toString());
				return "resource";
			}
			clientCodeJsonWriter.write("resource", theReq.getParameter("resource"));
		} else {
			query = search.forAllResources();
			clientCodeJsonWriter.writeNull("resource");
		}

		if (client.getPrettyPrint() != null) {
			clientCodeJsonWriter.write("pretty", client.getPrettyPrint().toString());
		} else {
			clientCodeJsonWriter.writeNull("pretty");
		}

		if (client.getEncoding() != null) {
			clientCodeJsonWriter.write("format", client.getEncoding().getRequestContentType());
		} else {
			clientCodeJsonWriter.writeNull("format");
		}

		String outcomeDescription = "Search for Resources";

		clientCodeJsonWriter.writeStartArray("params");
		int paramIdx = -1;
		while (true) {
			paramIdx++;

			String paramIdxString = Integer.toString(paramIdx);
			boolean shouldContinue = handleSearchParam(paramIdxString, theReq, query, clientCodeJsonWriter);
			if (!shouldContinue) {
				break;
			}
		}
		clientCodeJsonWriter.writeEnd();

		clientCodeJsonWriter.writeStartArray("includes");
		String[] incValues = theReq.getParameterValues(Constants.PARAM_INCLUDE);
		if (incValues != null) {
			for (String next : incValues) {
				if (isNotBlank(next)) {
					query.include(new Include(next));
					clientCodeJsonWriter.write(next);
				}
			}
		}
		clientCodeJsonWriter.writeEnd();

		clientCodeJsonWriter.writeStartArray("revincludes");
		String[] revIncValues = theReq.getParameterValues(Constants.PARAM_REVINCLUDE);
		if (revIncValues != null) {
			for (String next : revIncValues) {
				if (isNotBlank(next)) {
					query.revInclude(new Include(next));
					clientCodeJsonWriter.write(next);
				}
			}
		}
		clientCodeJsonWriter.writeEnd();

		String limit = theReq.getParameter("resource-search-limit");
		if (isNotBlank(limit)) {
			if (!limit.matches("[0-9]+")) {
				theModel.put("errorMsg", "Search limit must be a numeric value.");
				return "resource";
			}
			int limitInt = Integer.parseInt(limit);
			query.limitTo(limitInt);
			clientCodeJsonWriter.write("limit", limit);
		} else {
			clientCodeJsonWriter.writeNull("limit");
		}

		String[] sort = theReq.getParameterValues("sort_by");
		if (sort != null) {
			for (String next : sort) {
				if (isBlank(next)) {
					continue;
				}
				String direction = theReq.getParameter("sort_direction");
				if ("asc".equals(direction)) {
					query.sort().ascending(new StringClientParam(next));
				} else if ("desc".equals(direction)) {
					query.sort().descending(new StringClientParam(next));
				} else {
					query.sort().defaultOrder(new StringClientParam(next));
				}
			}
		}

		long start = System.currentTimeMillis();
		ResultType returnsResource;
		try {
			ourLog.info(logPrefix(theModel) + "Executing a search");

			query.execute();
			returnsResource = ResultType.BUNDLE;
		} catch (Exception e) {
			returnsResource = handleClientException(client, e, theModel);
		}
		long delay = System.currentTimeMillis() - start;

		processAndAddLastClientInvocation(client, returnsResource, theModel, delay, outcomeDescription, interceptor, theRequest);

		clientCodeJsonWriter.writeEnd();
		clientCodeJsonWriter.close();
		String clientCodeJson = clientCodeJsonStringWriter.toString();
		theModel.put("clientCodeJson", clientCodeJson);

		return "result";
	}

	@RequestMapping(value = { "/transaction" })
	public String actionTransaction(HttpServletRequest theServletRequest, final TransactionRequest theRequest, final BindingResult theBindingResult, final ModelMap theModel) {
		addCommonParams(theServletRequest, theRequest, theModel);

		CaptureInterceptor interceptor = new CaptureInterceptor();
		GenericClient client = theRequest.newClient(theServletRequest, getContext(theRequest), myConfig, interceptor);

		String body = preProcessMessageBody(theRequest.getTransactionBody());

		Bundle bundle;
		try {
			if (body.startsWith("{")) {
				bundle = getContext(theRequest).newJsonParser().parseBundle(body);
			} else if (body.startsWith("<")) {
				bundle = getContext(theRequest).newXmlParser().parseBundle(body);
			} else {
				theModel.put("errorMsg", "Message body does not appear to be a valid FHIR resource instance document. Body should start with '<' (for XML encoding) or '{' (for JSON encoding).");
				return "home";
			}
		} catch (DataFormatException e) {
			ourLog.warn("Failed to parse bundle", e);
			theModel.put("errorMsg", "Failed to parse transaction bundle body. Error was: " + e.getMessage());
			return "home";
		}

		ResultType returnsResource = ResultType.BUNDLE;
		long start = System.currentTimeMillis();
		try {
			ourLog.info(logPrefix(theModel) + "Executing transaction with {} resources", bundle.size());
			client.transaction().withBundle(bundle).execute();
		} catch (Exception e) {
			returnsResource = handleClientException(client, e, theModel);
		}
		long delay = System.currentTimeMillis() - start;

		processAndAddLastClientInvocation(client, returnsResource, theModel, delay, "Transaction", interceptor, theRequest);

		return "result";
	}

	@RequestMapping(value = { "/update" })
	public String actionUpdate(final HttpServletRequest theReq, final HomeRequest theRequest, final BindingResult theBindingResult, final ModelMap theModel) {
		doActionCreateOrValidate(theReq, theRequest, theBindingResult, theModel, "update");
		return "result";
	}

	@RequestMapping(value = { "/validate" })
	public String actionValidate(final HttpServletRequest theReq, final HomeRequest theRequest, final BindingResult theBindingResult, final ModelMap theModel) {
		doActionCreateOrValidate(theReq, theRequest, theBindingResult, theModel, "validate");
		return "result";
	}

	private IResource addCommonParams(HttpServletRequest theServletRequest, final HomeRequest theRequest, final ModelMap theModel) {
		if (myConfig.getDebugTemplatesMode()) {
			myTemplateEngine.getCacheManager().clearAllCaches();
		}

		final String serverId = theRequest.getServerIdWithDefault(myConfig);
		final String serverBase = theRequest.getServerBase(theServletRequest, myConfig);
		final String serverName = theRequest.getServerName(myConfig);
		theModel.put("serverId", serverId);
		theModel.put("base", serverBase);
		theModel.put("baseName", serverName);
		theModel.put("resourceName", defaultString(theRequest.getResource()));
		theModel.put("encoding", theRequest.getEncoding());
		theModel.put("pretty", theRequest.getPretty());
		theModel.put("_summary", theRequest.get_summary());
		theModel.put("serverEntries", myConfig.getIdToServerName());

		return loadAndAddConf(theServletRequest, theRequest, theModel);
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

		addCommonParams(theReq, theRequest, theModel);

		CaptureInterceptor interceptor = new CaptureInterceptor();
		GenericClient client = theRequest.newClient(theReq, getContext(theRequest), myConfig, interceptor);
		client.setPrettyPrint(true);

		Class<? extends IResource> type = null; // def.getImplementingClass();
		if ("history-type".equals(theMethod)) {
			RuntimeResourceDefinition def = getContext(theRequest).getResourceDefinition(theRequest.getResource());
			type = (Class<? extends IResource>) def.getImplementingClass();
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
				resource = getContext(theRequest).newJsonParser().parseResource(type, body);
				client.setEncoding(EncodingEnum.JSON);
			} else if (body.startsWith("<")) {
				resource = getContext(theRequest).newXmlParser().parseResource(type, body);
				client.setEncoding(EncodingEnum.XML);
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
		boolean update = false;
		try {
			if (validate) {
				outcomeDescription = "Validate Resource";
				client.validate().resource(resource).prettyPrint().execute();
			} else {
				String id = theReq.getParameter("resource-create-id");
				if ("update".equals(theMethod)) {
					outcomeDescription = "Update Resource";
					client.update(id, resource);
					update = true;
				} else {
					outcomeDescription = "Create Resource";
					ICreateTyped create = client.create().resource(body);
					if (isNotBlank(id)) {
						create.withId(id);
					}
					create.execute();
				}
			}
		} catch (Exception e) {
			returnsResource = handleClientException(client, e, theModel);
		}
		long delay = System.currentTimeMillis() - start;

		processAndAddLastClientInvocation(client, returnsResource, theModel, delay, outcomeDescription, interceptor, theRequest);

		try {
			if (validate) {
				ourLog.info(logPrefix(theModel) + "Validated resource of type " + getResourceType(theRequest, theReq).getName());
			} else if (update) {
				ourLog.info(logPrefix(theModel) + "Updated resource of type " + getResourceType(theRequest, theReq).getName());
			} else {
				ourLog.info(logPrefix(theModel) + "Created resource of type " + getResourceType(theRequest, theReq).getName());
			}
		} catch (Exception e) {
			ourLog.warn("Failed to determine resource type from request", e);
		}

	}

	private void doActionHistory(HttpServletRequest theReq, HomeRequest theRequest, BindingResult theBindingResult, ModelMap theModel, String theMethod, String theMethodDescription) {
		addCommonParams(theReq, theRequest, theModel);

		CaptureInterceptor interceptor = new CaptureInterceptor();
		GenericClient client = theRequest.newClient(theReq, getContext(theRequest), myConfig, interceptor);

		String id = null;
		Class<? extends IResource> type = null; // def.getImplementingClass();
		if ("history-type".equals(theMethod)) {
			RuntimeResourceDefinition def = getContext(theRequest).getResourceDefinition(theRequest.getResource());
			type = (Class<? extends IResource>) def.getImplementingClass();
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

		ResultType returnsResource = ResultType.BUNDLE;

		long start = System.currentTimeMillis();
		try {
			ourLog.info(logPrefix(theModel) + "Retrieving history for type {} ID {} since {}", new Object[] { type, id, since });
			client.history(type, id, since, limit);
		} catch (Exception e) {
			returnsResource = handleClientException(client, e, theModel);
		}
		long delay = System.currentTimeMillis() - start;

		processAndAddLastClientInvocation(client, returnsResource, theModel, delay, theMethodDescription, interceptor, theRequest);

	}

	private boolean extractSearchParamsDev(IResource theConformance, String resourceName, TreeSet<String> includes, TreeSet<String> theRevIncludes, TreeSet<String> sortParams, List<RestQuery> queries, boolean haveSearchParams,
			List<List<String>> queryIncludes) {
		ca.uhn.fhir.model.dstu2.resource.Conformance conformance = (ca.uhn.fhir.model.dstu2.resource.Conformance) theConformance;
		for (ca.uhn.fhir.model.dstu2.resource.Conformance.Rest nextRest : conformance.getRest()) {
			for (ca.uhn.fhir.model.dstu2.resource.Conformance.RestResource nextRes : nextRest.getResource()) {
				if (nextRes.getTypeElement().getValue().equals(resourceName)) {
					for (StringDt next : nextRes.getSearchInclude()) {
						if (next.isEmpty() == false) {
							includes.add(next.getValue());
						}
					}
					for (ca.uhn.fhir.model.dstu2.resource.Conformance.RestResourceSearchParam next : nextRes.getSearchParam()) {
						if (next.getTypeElement().getValueAsEnum() != ca.uhn.fhir.model.dstu2.valueset.SearchParamTypeEnum.COMPOSITE) {
							sortParams.add(next.getNameElement().getValue());
						}
					}
					if (nextRes.getSearchParam().size() > 0) {
						haveSearchParams = true;
					}
				} else {
					// It's a different resource from the one we're searching, so
					// scan for revinclude candidates
					for (ca.uhn.fhir.model.dstu2.resource.Conformance.RestResourceSearchParam next : nextRes.getSearchParam()) {
						if (next.getTypeElement().getValueAsEnum() == ca.uhn.fhir.model.dstu2.valueset.SearchParamTypeEnum.REFERENCE) {
							for (BoundCodeDt<ResourceTypeEnum> nextTargetType : next.getTarget()) {
								if (nextTargetType.getValue().equals(resourceName)) {
									theRevIncludes.add(nextRes.getTypeElement().getValue() + ":" + next.getName());
								}
							}
						}
					}
				}
			}
		}
		return haveSearchParams;
	}

	private boolean extractSearchParamsDstu1(IResource theConformance, String resourceName, TreeSet<String> includes, TreeSet<String> sortParams, List<RestQuery> queries, boolean haveSearchParams,
			List<List<String>> queryIncludes) {
		Conformance conformance = (Conformance) theConformance;
		for (Rest nextRest : conformance.getRest()) {
			for (RestResource nextRes : nextRest.getResource()) {
				if (nextRes.getType().getValue().equals(resourceName)) {
					for (StringDt next : nextRes.getSearchInclude()) {
						if (next.isEmpty() == false) {
							includes.add(next.getValue());
						}
					}
					for (RestResourceSearchParam next : nextRes.getSearchParam()) {
						if (next.getType().getValueAsEnum() != SearchParamTypeEnum.COMPOSITE) {
							sortParams.add(next.getName().getValue());
						}
					}
					if (nextRes.getSearchParam().size() > 0) {
						haveSearchParams = true;
					}
				}
			}
			for (RestQuery nextQuery : nextRest.getQuery()) {
				boolean queryMatchesResource = false;
				List<ExtensionDt> returnTypeExt = nextQuery.getUndeclaredExtensionsByUrl(ExtensionConstants.QUERY_RETURN_TYPE);
				if (returnTypeExt != null) {
					for (ExtensionDt nextExt : returnTypeExt) {
						if (resourceName.equals(nextExt.getValueAsPrimitive().getValueAsString())) {
							queries.add(nextQuery);
							queryMatchesResource = true;
							break;
						}
					}
				}

				if (queryMatchesResource) {
					ArrayList<String> nextQueryIncludes = new ArrayList<String>();
					queryIncludes.add(nextQueryIncludes);
					List<ExtensionDt> includesExt = nextQuery.getUndeclaredExtensionsByUrl(ExtensionConstants.QUERY_ALLOWED_INCLUDE);
					if (includesExt != null) {
						for (ExtensionDt nextExt : includesExt) {
							nextQueryIncludes.add(nextExt.getValueAsPrimitive().getValueAsString());
						}
					}
				}
			}
		}
		return haveSearchParams;
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

	private String formatUrl(String theUrlBase, String theResultBody) {
		String str = theResultBody;
		if (str == null) {
			return str;
		}

		try {
			str = URLDecoder.decode(str, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			ourLog.error("Should not happen", e);
		}

		StringBuilder b = new StringBuilder();
		b.append("<span class='hlUrlBase'>");

		boolean inParams = false;
		for (int i = 0; i < str.length(); i++) {
			char nextChar = str.charAt(i);
			// char nextChar2 = i < str.length()-2 ? str.charAt(i+1):' ';
			// char nextChar3 = i < str.length()-2 ? str.charAt(i+2):' ';
			if (!inParams) {
				if (nextChar == '?') {
					inParams = true;
					b.append("</span><wbr /><span class='hlControl'>?</span><span class='hlTagName'>");
				} else {
					if (i == theUrlBase.length()) {
						b.append("</span><wbr /><span class='hlText'>");
					}
					b.append(nextChar);
				}
			} else {
				if (nextChar == '&') {
					b.append("</span><wbr /><span class='hlControl'>&amp;</span><span class='hlTagName'>");
				} else if (nextChar == '=') {
					b.append("</span><span class='hlControl'>=</span><span class='hlAttr'>");
					// }else if (nextChar=='%' && Character.isLetterOrDigit(nextChar2)&& Character.isLetterOrDigit(nextChar3)) {
					// URLDecoder.decode(s, enc)
				} else {
					b.append(nextChar);
				}
			}
		}

		if (inParams) {
			b.append("</span>");
		}
		return b.toString();
	}

	private FhirContext getContext(HomeRequest theRequest) {
		FhirVersionEnum version = theRequest.getFhirVersion(myConfig);
		FhirContext retVal = myContexts.get(version);
		if (retVal == null) {
			retVal = new FhirContext(version);
			myContexts.put(version, retVal);
		}
		return retVal;
	}

	private RuntimeResourceDefinition getResourceType(HomeRequest theRequest, HttpServletRequest theReq) throws ServletException {
		String resourceName = StringUtils.defaultString(theReq.getParameter(PARAM_RESOURCE));
		RuntimeResourceDefinition def = getContext(theRequest).getResourceDefinition(resourceName);
		if (def == null) {
			throw new ServletException("Invalid resourceName: " + resourceName);
		}
		return def;
	}

	private ResultType handleClientException(GenericClient theClient, Exception e, ModelMap theModel) {
		ResultType returnsResource;
		returnsResource = ResultType.NONE;
		ourLog.warn("Failed to invoke server", e);

		if (theClient.getLastResponse() == null) {
			theModel.put("errorMsg", "Error: " + e.getMessage());
		}

		return returnsResource;
	}

	private boolean handleSearchParam(String paramIdxString, HttpServletRequest theReq, IQuery theQuery, JsonGenerator theClientCodeJsonWriter) {
		String nextName = theReq.getParameter("param." + paramIdxString + ".name");
		if (isBlank(nextName)) {
			return false;
		}

		String nextQualifier = StringUtils.defaultString(theReq.getParameter("param." + paramIdxString + ".qualifier"));
		String nextType = theReq.getParameter("param." + paramIdxString + ".type");

		List<String> parts = new ArrayList<String>();
		for (int i = 0; i < 5; i++) {
			parts.add(defaultString(theReq.getParameter("param." + paramIdxString + "." + i)));
		}

		List<String> values;
		boolean addToWhere = true;
		if ("token".equals(nextType)) {
			if (isBlank(parts.get(1))) {
				return true;
			}
			addToWhere = false;
			if (isBlank(parts.get(0))) {
				values = Collections.singletonList(parts.get(1));
				theQuery.where(new TokenClientParam(nextName + nextQualifier).exactly().code(parts.get(1)));
			} else {
				values = Collections.singletonList(parts.get(0) + "|" + parts.get(1));
				theQuery.where(new TokenClientParam(nextName + nextQualifier).exactly().systemAndCode(parts.get(0), parts.get(1)));
			}
		} else if ("date".equals(nextType)) {
			values = new ArrayList<String>();
			if (isNotBlank(parts.get(1))) {
				values.add(StringUtils.join(parts.get(0), parts.get(1)));
			}
			if (isNotBlank(parts.get(3))) {
				values.add(StringUtils.join(parts.get(2), parts.get(3)));
			}
			if (values.isEmpty()) {
				return true;
			}
		} else if ("quantity".equals(nextType)) {
			values = new ArrayList<String>();
			addToWhere = false;

			QuantityClientParam param = new QuantityClientParam(nextName);
			IMatches<IAndUnits> matcher;
			if ("~".equals(parts.get(0))) {
				matcher = param.approximately();
			} else if ("=".equals(parts.get(0))) {
				matcher = param.exactly();
			} else if (">=".equals(parts.get(0))) {
				matcher = param.greaterThanOrEquals();
			} else if ("<=".equals(parts.get(0))) {
				matcher = param.lessThanOrEquals();
			} else if (">".equals(parts.get(0))) {
				matcher = param.greaterThan();
			} else if ("<".equals(parts.get(0))) {
				matcher = param.lessThan();
			} else {
				throw new Error("Unknown qualifier: " + parts.get(0));
			}
			IAndUnits number = matcher.number(parts.get(1));

			if (isBlank(parts.get(3))) {
				theQuery.where(number.andNoUnits());
			} else if (isBlank(parts.get(2))) {
				theQuery.where(number.andUnits(parts.get(3)));
			} else {
				theQuery.where(number.andUnits(parts.get(2), parts.get(3)));
			}

			values.add(parts.get(0) + parts.get(1) + "|" + parts.get(2) + "|" + parts.get(3));

			if (values.isEmpty()) {
				return true;
			}
		} else {
			values = Collections.singletonList(StringUtils.join(parts, ""));
			if (isBlank(values.get(0))) {
				return true;
			}
		}

		for (String nextValue : values) {

			theClientCodeJsonWriter.writeStartObject();
			theClientCodeJsonWriter.write("type", nextType);
			theClientCodeJsonWriter.write("name", nextName);
			theClientCodeJsonWriter.write("qualifier", nextQualifier);
			theClientCodeJsonWriter.write("value", nextValue);
			theClientCodeJsonWriter.writeEnd();
			if (addToWhere) {
				theQuery.where(new StringClientParam(nextName + nextQualifier).matches().value(nextValue));
			}

		}

		if (StringUtils.isNotBlank(theReq.getParameter("param." + paramIdxString + ".0.name"))) {
			handleSearchParam(paramIdxString + ".0", theReq, theQuery, theClientCodeJsonWriter);
		}

		return true;
	}

	private IResource loadAndAddConf(HttpServletRequest theServletRequest, final HomeRequest theRequest, final ModelMap theModel) {
		switch (theRequest.getFhirVersion(myConfig)) {
		case DEV:
			return loadAndAddConfDstu2(theServletRequest, theRequest, theModel);
		case DSTU1:
			return loadAndAddConfDstu1(theServletRequest, theRequest, theModel);
		case DSTU2:
			return loadAndAddConfDstu2(theServletRequest, theRequest, theModel);
		}
		throw new IllegalStateException("Unknown version: " + theRequest.getFhirVersion(myConfig));
	}

	private Conformance loadAndAddConfDstu1(HttpServletRequest theServletRequest, final HomeRequest theRequest, final ModelMap theModel) {
		IGenericClient client = getContext(theRequest).newRestfulGenericClient(theRequest.getServerBase(theServletRequest, myConfig));

		Conformance conformance;
		try {
			conformance = (Conformance) client.conformance();
		} catch (Exception e) {
			ourLog.warn("Failed to load conformance statement", e);
			theModel.put("errorMsg", "Failed to load conformance statement, error was: " + e.toString());
			conformance = new Conformance();
		}

		theModel.put("jsonEncodedConf", getContext(theRequest).newJsonParser().encodeResourceToString(conformance));

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
		theModel.put("requiredParamExtension", ExtensionConstants.PARAM_IS_REQUIRED);

		return conformance;
	}

	private IResource loadAndAddConfDstu2(HttpServletRequest theServletRequest, final HomeRequest theRequest, final ModelMap theModel) {
		IGenericClient client = getContext(theRequest).newRestfulGenericClient(theRequest.getServerBase(theServletRequest, myConfig));

		ca.uhn.fhir.model.dstu2.resource.Conformance conformance;
		try {
			conformance = (ca.uhn.fhir.model.dstu2.resource.Conformance) client.conformance();
		} catch (Exception e) {
			ourLog.warn("Failed to load conformance statement", e);
			theModel.put("errorMsg", "Failed to load conformance statement, error was: " + e.toString());
			conformance = new ca.uhn.fhir.model.dstu2.resource.Conformance();
		}

		theModel.put("jsonEncodedConf", getContext(theRequest).newJsonParser().encodeResourceToString(conformance));

		Map<String, Number> resourceCounts = new HashMap<String, Number>();
		long total = 0;
		for (ca.uhn.fhir.model.dstu2.resource.Conformance.Rest nextRest : conformance.getRest()) {
			for (ca.uhn.fhir.model.dstu2.resource.Conformance.RestResource nextResource : nextRest.getResource()) {
				List<ExtensionDt> exts = nextResource.getUndeclaredExtensionsByUrl(RESOURCE_COUNT_EXT_URL);
				if (exts != null && exts.size() > 0) {
					Number nextCount = ((DecimalDt) (exts.get(0).getValue())).getValueAsNumber();
					resourceCounts.put(nextResource.getTypeElement().getValue(), nextCount);
					total += nextCount.longValue();
				}
			}
		}
		theModel.put("resourceCounts", resourceCounts);

		if (total > 0) {
			for (ca.uhn.fhir.model.dstu2.resource.Conformance.Rest nextRest : conformance.getRest()) {
				Collections.sort(nextRest.getResource(), new Comparator<ca.uhn.fhir.model.dstu2.resource.Conformance.RestResource>() {
					@Override
					public int compare(ca.uhn.fhir.model.dstu2.resource.Conformance.RestResource theO1, ca.uhn.fhir.model.dstu2.resource.Conformance.RestResource theO2) {
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
							retVal = theO1.getTypeElement().getValue().compareTo(theO2.getTypeElement().getValue());
						}
						return retVal;
					}
				});
			}
		}

		theModel.put("conf", conformance);
		theModel.put("requiredParamExtension", ExtensionConstants.PARAM_IS_REQUIRED);

		return conformance;
	}

	private String logPrefix(ModelMap theModel) {
		return "[server=" + theModel.get("serverId") + "] - ";
	}

	private String parseNarrative(HomeRequest theRequest, EncodingEnum theCtEnum, String theResultBody) {
		try {
			IResource resource = (IResource) theCtEnum.newParser(getContext(theRequest)).parseResource(theResultBody);
			String retVal = resource.getText().getDiv().getValueAsString();
			return StringUtils.defaultString(retVal);
		} catch (Exception e) {
			ourLog.error("Failed to parse resource", e);
			return "";
		}
	}

	private String preProcessMessageBody(String theBody) {
		if (theBody == null) {
			return "";
		}
		String retVal = theBody.trim();

		StringBuilder b = new StringBuilder();
		for (int i = 0; i < retVal.length(); i++) {
			char nextChar = retVal.charAt(i);
			int nextCharI = nextChar;
			if (nextCharI == 65533) {
				b.append(' ');
				continue;
			}
			if (nextCharI == 160) {
				b.append(' ');
				continue;
			}
			if (nextCharI == 194) {
				b.append(' ');
				continue;
			}
			b.append(nextChar);
		}
		retVal = b.toString();
		return retVal;
	}

	private void processAndAddLastClientInvocation(GenericClient theClient, ResultType theResultType, ModelMap theModelMap, long theLatency, String outcomeDescription,
			CaptureInterceptor theInterceptor, HomeRequest theRequest) {
		try {
			HttpRequestBase lastRequest = theInterceptor.getLastRequest();
			HttpResponse lastResponse = theInterceptor.getLastResponse();
			String requestBody = null;
			String requestUrl = lastRequest != null ? lastRequest.getURI().toASCIIString() : null;
			String action = lastRequest != null ? lastRequest.getMethod() : null;
			String resultStatus = lastResponse != null ? lastResponse.getStatusLine().toString() : null;
			String resultBody = StringUtils.defaultString(theInterceptor.getLastResponseBody());

			if (lastRequest instanceof HttpEntityEnclosingRequest) {
				HttpEntity entity = ((HttpEntityEnclosingRequest) lastRequest).getEntity();
				if (entity.isRepeatable()) {
					requestBody = IOUtils.toString(entity.getContent());
				}
			}

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
						narrativeString = parseNarrative(theRequest, ctEnum, resultBody);
						resultDescription.append("JSON resource");
					} else if (theResultType == ResultType.BUNDLE) {
						resultDescription.append("JSON bundle");
						bundle = getContext(theRequest).newJsonParser().parseBundle(resultBody);
					}
					break;
				case XML:
				default:
					if (theResultType == ResultType.RESOURCE) {
						narrativeString = parseNarrative(theRequest, ctEnum, resultBody);
						resultDescription.append("XML resource");
					} else if (theResultType == ResultType.BUNDLE) {
						resultDescription.append("XML bundle");
						bundle = getContext(theRequest).newXmlParser().parseBundle(resultBody);
					}
					break;
				}
			}

			/*
			 * DSTU2 no longer has a title in the bundle format, but it's still useful here..
			 */
			if (bundle != null) {
				INarrativeGenerator gen = getContext(theRequest).getNarrativeGenerator();
				if (gen != null) {
					for (BundleEntry next : bundle.getEntries()) {
						if (next.getTitle().isEmpty() && next.getResource() != null) {
							String title = gen.generateTitle(next.getResource());
							next.getTitle().setValue(title);
						}
					}
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
			theModelMap.put("requestUrlText", formatUrl(theClient.getUrlBase(), requestUrl));

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

	public static class CaptureInterceptor implements IClientInterceptor {

		private HttpRequestBase myLastRequest;
		private HttpResponse myLastResponse;
		private String myResponseBody;

		public HttpRequestBase getLastRequest() {
			return myLastRequest;
		}

		public HttpResponse getLastResponse() {
			return myLastResponse;
		}

		public String getLastResponseBody() {
			return myResponseBody;
		}

		@Override
		public void interceptRequest(HttpRequestBase theRequest) {
			assert myLastRequest == null;
			myLastRequest = theRequest;
		}

		@Override
		public void interceptResponse(HttpResponse theResponse) throws IOException {
			assert myLastResponse == null;
			myLastResponse = theResponse;

			HttpEntity respEntity = theResponse.getEntity();
			if (respEntity != null) {
				final byte[] bytes;
				try {
					bytes = IOUtils.toByteArray(respEntity.getContent());
				} catch (IllegalStateException e) {
					throw new InternalErrorException(e);
				}

				myResponseBody = new String(bytes, "UTF-8");
				theResponse.setEntity(new MyEntityWrapper(respEntity, bytes));
			}
		}

		private static class MyEntityWrapper extends HttpEntityWrapper {

			private byte[] myBytes;

			public MyEntityWrapper(HttpEntity theWrappedEntity, byte[] theBytes) {
				super(theWrappedEntity);
				myBytes = theBytes;
			}

			@Override
			public InputStream getContent() throws IOException {
				return new ByteArrayInputStream(myBytes);
			}

			@Override
			public void writeTo(OutputStream theOutstream) throws IOException {
				theOutstream.write(myBytes);
			}

		}

	}

	private enum ResultType {
		BUNDLE, NONE, RESOURCE, TAGLIST
	}

}
