package ca.uhn.fhir.to;

import static org.apache.commons.lang3.StringUtils.defaultIfEmpty;
import static org.apache.commons.lang3.StringUtils.defaultString;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.dstu3.model.CapabilityStatement;
import org.hl7.fhir.dstu3.model.CapabilityStatement.CapabilityStatementRestComponent;
import org.hl7.fhir.dstu3.model.CapabilityStatement.CapabilityStatementRestResourceComponent;
import org.hl7.fhir.dstu3.model.CapabilityStatement.CapabilityStatementRestResourceSearchParamComponent;
import org.hl7.fhir.dstu3.model.CodeType;
import org.hl7.fhir.dstu3.model.StringType;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseConformance;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.gson.stream.JsonWriter;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.model.api.ExtensionDt;
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
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.rest.client.GenericClient;
import ca.uhn.fhir.rest.gclient.*;
import ca.uhn.fhir.rest.gclient.NumberClientParam.IMatches;
import ca.uhn.fhir.rest.gclient.QuantityClientParam.IAndUnits;
import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.rest.server.EncodingEnum;
import ca.uhn.fhir.to.model.HomeRequest;
import ca.uhn.fhir.to.model.ResourceRequest;
import ca.uhn.fhir.to.model.TransactionRequest;
import ca.uhn.fhir.util.ExtensionConstants;

@org.springframework.stereotype.Controller()
public class Controller extends BaseController {
	static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(Controller.class);

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
			Class<? extends IBaseConformance> type;
			switch (getContext(theRequest).getVersion().getVersion()) {
			default:
			case DSTU1:
				type = Conformance.class;
				break;
			case DSTU2:
				type = ca.uhn.fhir.model.dstu2.resource.Conformance.class;
				break;
			case DSTU3:
				type = org.hl7.fhir.dstu3.model.CapabilityStatement.class;
				break;
			}
			client.fetchConformance().ofType(type).execute();
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
			client.delete((Class<? extends IBaseResource>) def.getImplementingClass(), new IdDt(id));
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

		Class<? extends IBaseResource> resType = null;
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

				resType = (Class<? extends IBaseResource>) def.getImplementingClass();
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
		FhirContext context = getContext(theRequest);
		GenericClient client = theRequest.newClient(theReq, context, myConfig, interceptor);

		String url = defaultString(theReq.getParameter("page-url"));
		if (myConfig.isRefuseToFetchThirdPartyUrls()) {
			if (!url.startsWith(theModel.get("base").toString())) {
				ourLog.warn(logPrefix(theModel) + "Refusing to load page URL: {}", url);
				theModel.put("errorMsg", "Invalid page URL: " + url);
				return "result";
			}
		}
		
		url = url.replace("&amp;", "&");

		ResultType returnsResource = ResultType.BUNDLE;

		long start = System.currentTimeMillis();
		try {
			ourLog.info(logPrefix(theModel) + "Loading paging URL: {}", url);
			if (context.getVersion().getVersion() == FhirVersionEnum.DSTU1) {
				client.loadPage().byUrl(url).andReturnDstu1Bundle().execute();
			} else {
				@SuppressWarnings("unchecked")
				Class<? extends IBaseBundle> bundleType = (Class<? extends IBaseBundle>) context.getResourceDefinition("Bundle").getImplementingClass();
				client.loadPage().byUrl(url).andReturnBundle(bundleType).execute();
			}
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
			if (resid.hasVersionIdPart()) {
				client.vread(def.getImplementingClass(), resid);
			} else {
				client.read(def.getImplementingClass(), resid);
			}
		} catch (Exception e) {
			returnsResource = handleClientException(client, e, theModel);
		}
		long delay = System.currentTimeMillis() - start;

		processAndAddLastClientInvocation(client, returnsResource, theModel, delay, outcomeDescription, interceptor, theRequest);

		return "result";
	}

	@RequestMapping({ "/resource" })
	public String actionResource(HttpServletRequest theServletRequest, final ResourceRequest theRequest, final BindingResult theBindingResult, final ModelMap theModel) {
		IBaseResource conformance = addCommonParams(theServletRequest, theRequest, theModel);

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
		case DSTU1:
			haveSearchParams = extractSearchParamsDstu1(conformance, resourceName, includes, sortParams, queries, haveSearchParams, queryIncludes);
			break;
		case DSTU2:
			haveSearchParams = extractSearchParamsDstu2(conformance, resourceName, includes, revIncludes, sortParams, queries, haveSearchParams, queryIncludes);
			break;
		case DSTU3:
			haveSearchParams = extractSearchParamsDstu3CapabilityStatement(conformance, resourceName, includes, revIncludes, sortParams, queries, haveSearchParams, queryIncludes);
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
			IBaseResource updateResource = (IBaseResource) client.read(def.getImplementingClass(), new IdDt(resourceName, updateId, updateVid));
			String updateResourceString = theRequest.newParser(getContext(theRequest)).setPrettyPrint(true).encodeResourceToString(updateResource);
			theModel.put("updateResource", updateResourceString);
			theModel.put("updateResourceId", updateId);
		}

		ourLog.info(logPrefix(theModel) + "Showing resource page: {}", resourceName);

		return "resource";
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = { "/search" })
	public String actionSearch(HttpServletRequest theReq, HomeRequest theRequest, BindingResult theBindingResult, ModelMap theModel) throws IOException {
		addCommonParams(theReq, theRequest, theModel);

		StringWriter clientCodeJsonStringWriter = new StringWriter();
		JsonWriter clientCodeJsonWriter = new JsonWriter(clientCodeJsonStringWriter);
		clientCodeJsonWriter.beginObject();
		clientCodeJsonWriter.name("action");
		clientCodeJsonWriter.value("search");
		clientCodeJsonWriter.name("base");
		clientCodeJsonWriter.value((String) theModel.get("base"));

		CaptureInterceptor interceptor = new CaptureInterceptor();
		GenericClient client = theRequest.newClient(theReq, getContext(theRequest), myConfig, interceptor);

		IUntypedQuery search = client.search();
		IQuery query;
		if (isNotBlank(theReq.getParameter("resource"))) {
			try {
				query = search.forResource((Class<? extends IBaseResource>) getResourceType(theRequest, theReq).getImplementingClass());
			} catch (ServletException e) {
				theModel.put("errorMsg", e.toString());
				return "resource";
			}
			clientCodeJsonWriter.name("resource");
			clientCodeJsonWriter.value(theReq.getParameter("resource"));
		} else {
			query = search.forAllResources();
			clientCodeJsonWriter.name("resource");
			clientCodeJsonWriter.nullValue();
		}

		if (client.isPrettyPrint()) {
			clientCodeJsonWriter.name("pretty");
			clientCodeJsonWriter.value("true");
		} else {
			clientCodeJsonWriter.name("pretty");
			clientCodeJsonWriter.nullValue();
		}

		if (client.getEncoding() != null) {
			clientCodeJsonWriter.name("format");
			clientCodeJsonWriter.value(client.getEncoding().getRequestContentType());
		} else {
			clientCodeJsonWriter.name("format");
			clientCodeJsonWriter.nullValue();
		}

		String outcomeDescription = "Search for Resources";

		clientCodeJsonWriter.name("params");
		clientCodeJsonWriter.beginArray();
		int paramIdx = -1;
		while (true) {
			paramIdx++;

			String paramIdxString = Integer.toString(paramIdx);
			boolean shouldContinue = handleSearchParam(paramIdxString, theReq, query, clientCodeJsonWriter);
			if (!shouldContinue) {
				break;
			}
		}
		clientCodeJsonWriter.endArray();

		clientCodeJsonWriter.name("includes");
		clientCodeJsonWriter.beginArray();
		String[] incValues = theReq.getParameterValues(Constants.PARAM_INCLUDE);
		if (incValues != null) {
			for (String next : incValues) {
				if (isNotBlank(next)) {
					query.include(new Include(next));
					clientCodeJsonWriter.value(next);
				}
			}
		}
		clientCodeJsonWriter.endArray();

		clientCodeJsonWriter.name("revincludes");
		clientCodeJsonWriter.beginArray();
		String[] revIncValues = theReq.getParameterValues(Constants.PARAM_REVINCLUDE);
		if (revIncValues != null) {
			for (String next : revIncValues) {
				if (isNotBlank(next)) {
					query.revInclude(new Include(next));
					clientCodeJsonWriter.value(next);
				}
			}
		}
		clientCodeJsonWriter.endArray();

		String limit = theReq.getParameter("resource-search-limit");
		if (isNotBlank(limit)) {
			if (!limit.matches("[0-9]+")) {
				theModel.put("errorMsg", "Search limit must be a numeric value.");
				return "resource";
			}
			int limitInt = Integer.parseInt(limit);
			query.limitTo(limitInt);
			clientCodeJsonWriter.name("limit");
			clientCodeJsonWriter.value(limit);
		} else {
			clientCodeJsonWriter.name("limit");
			clientCodeJsonWriter.nullValue();
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

		if (client.getFhirContext().getVersion().getVersion() != FhirVersionEnum.DSTU1) {
			query.returnBundle(client.getFhirContext().getResourceDefinition("Bundle").getImplementingClass());
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

		clientCodeJsonWriter.endObject();
		clientCodeJsonWriter.close();
		String clientCodeJson = clientCodeJsonStringWriter.toString();
		theModel.put("clientCodeJson", clientCodeJson);

		return "result";
	}

	@RequestMapping(value = { "/transaction" })
	public String actionTransaction(HttpServletRequest theServletRequest, final TransactionRequest theRequest, final BindingResult theBindingResult, final ModelMap theModel) {
		addCommonParams(theServletRequest, theRequest, theModel);

		CaptureInterceptor interceptor = new CaptureInterceptor();
		FhirContext context = getContext(theRequest);
		GenericClient client = theRequest.newClient(theServletRequest, context, myConfig, interceptor);

		String body = preProcessMessageBody(theRequest.getTransactionBody());

		try {
			if (body.startsWith("{")) {
				// JSON content
			} else if (body.startsWith("<")) {
				// XML content
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
			ourLog.info(logPrefix(theModel) + "Executing transaction");
			client.transaction().withBundle(body).execute();
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

	private void doActionCreateOrValidate(HttpServletRequest theReq, HomeRequest theRequest, BindingResult theBindingResult, ModelMap theModel, String theMethod) {
		boolean validate = "validate".equals(theMethod);

		addCommonParams(theReq, theRequest, theModel);

		CaptureInterceptor interceptor = new CaptureInterceptor();
		GenericClient client = theRequest.newClient(theReq, getContext(theRequest), myConfig, interceptor);
		client.setPrettyPrint(true);

		Class<? extends IBaseResource> type = null; // def.getImplementingClass();
		if ("history-type".equals(theMethod)) {
			RuntimeResourceDefinition def = getContext(theRequest).getResourceDefinition(theRequest.getResource());
			type = (Class<? extends IBaseResource>) def.getImplementingClass();
		}

		String body = validate ? theReq.getParameter("resource-validate-body") : theReq.getParameter("resource-create-body");
		if (isBlank(body)) {
			theModel.put("errorMsg", "No message body specified");
			return;
		}

		body = preProcessMessageBody(body);

		IBaseResource resource;
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
		Class<? extends IBaseResource> type = null; // def.getImplementingClass();
		if ("history-type".equals(theMethod)) {
			RuntimeResourceDefinition def = getContext(theRequest).getResourceDefinition(theRequest.getResource());
			type = (Class<? extends IBaseResource>) def.getImplementingClass();
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

			IHistory hist0 = client.history();
			IHistoryUntyped hist1;
			if (isNotBlank(id)) {
				hist1 = hist0.onInstance(new IdDt(theRequest.getResource(), id));
			} else if (type != null) {
				hist1 = hist0.onType(type);
			} else {
				hist1 = hist0.onServer();
			}

			IHistoryTyped<?> hist2;
			if (client.getFhirContext().getVersion().getVersion() == FhirVersionEnum.DSTU1) {
				hist2 = hist1.andReturnDstu1Bundle();
			} else {
				hist2 = hist1.andReturnBundle(client.getFhirContext().getResourceDefinition("Bundle").getImplementingClass(IBaseBundle.class));
			}

			if (since != null) {
				hist2.since(since);
			}
			if (limit != null) {
				hist2.count(limit);
			}

			hist2.execute();
		} catch (Exception e) {
			returnsResource = handleClientException(client, e, theModel);
		}
		long delay = System.currentTimeMillis() - start;

		processAndAddLastClientInvocation(client, returnsResource, theModel, delay, theMethodDescription, interceptor, theRequest);

	}

	private boolean extractSearchParamsDstu1(IBaseResource theConformance, String resourceName, TreeSet<String> includes, TreeSet<String> sortParams, List<RestQuery> queries, boolean haveSearchParams, List<List<String>> queryIncludes) {
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

	private boolean extractSearchParamsDstu2(IBaseResource theConformance, String resourceName, TreeSet<String> includes, TreeSet<String> theRevIncludes, TreeSet<String> sortParams, List<RestQuery> queries, boolean haveSearchParams, List<List<String>> queryIncludes) {
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

	private boolean extractSearchParamsDstu3CapabilityStatement(IBaseResource theConformance, String resourceName, TreeSet<String> includes, TreeSet<String> theRevIncludes, TreeSet<String> sortParams, List<RestQuery> queries, boolean haveSearchParams, List<List<String>> queryIncludes) {
		CapabilityStatement conformance = (org.hl7.fhir.dstu3.model.CapabilityStatement) theConformance;
		for (CapabilityStatementRestComponent nextRest : conformance.getRest()) {
			for (CapabilityStatementRestResourceComponent nextRes : nextRest.getResource()) {
				if (nextRes.getTypeElement().getValue().equals(resourceName)) {
					for (StringType next : nextRes.getSearchInclude()) {
						if (next.isEmpty() == false) {
							includes.add(next.getValue());
						}
					}
					for (CapabilityStatementRestResourceSearchParamComponent next : nextRes.getSearchParam()) {
						if (next.getTypeElement().getValue() != org.hl7.fhir.dstu3.model.Enumerations.SearchParamType.COMPOSITE) {
							sortParams.add(next.getNameElement().getValue());
						}
					}
					if (nextRes.getSearchParam().size() > 0) {
						haveSearchParams = true;
					}
				} else {
					// It's a different resource from the one we're searching, so
					// scan for revinclude candidates
					for (CapabilityStatementRestResourceSearchParamComponent next : nextRes.getSearchParam()) {
						if (next.getTypeElement().getValue() == org.hl7.fhir.dstu3.model.Enumerations.SearchParamType.REFERENCE) {
//							for (CodeType nextTargetType : next.getTarget()) {
//								if (nextTargetType.getValue().equals(resourceName)) {
//									theRevIncludes.add(nextRes.getTypeElement().getValue() + ":" + next.getName());
//								}
//							}
						}
					}
				}
			}
		}
		return haveSearchParams;
	}

	private boolean handleSearchParam(String paramIdxString, HttpServletRequest theReq, IQuery<?> theQuery, JsonWriter theClientCodeJsonWriter) throws IOException {
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

			theClientCodeJsonWriter.beginObject();
			theClientCodeJsonWriter.name("type");
			theClientCodeJsonWriter.value(nextType);
			theClientCodeJsonWriter.name("name");
			theClientCodeJsonWriter.value(nextName);
			theClientCodeJsonWriter.name("qualifier");
			theClientCodeJsonWriter.value(nextQualifier);
			theClientCodeJsonWriter.name("value");
			theClientCodeJsonWriter.value(nextValue);
			theClientCodeJsonWriter.endObject();
			if (addToWhere) {
				theQuery.where(new StringClientParam(nextName + nextQualifier).matches().value(nextValue));
			}

		}

		if (StringUtils.isNotBlank(theReq.getParameter("param." + paramIdxString + ".0.name"))) {
			handleSearchParam(paramIdxString + ".0", theReq, theQuery, theClientCodeJsonWriter);
		}

		return true;
	}

}
