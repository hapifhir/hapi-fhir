package ca.uhn.fhir.to;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.model.api.ExtensionDt;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu2.resource.Conformance;
import ca.uhn.fhir.model.primitive.DecimalDt;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.client.api.IClientInterceptor;
import ca.uhn.fhir.rest.client.api.IHttpRequest;
import ca.uhn.fhir.rest.client.api.IHttpResponse;
import ca.uhn.fhir.rest.client.impl.GenericClient;
import ca.uhn.fhir.to.model.HomeRequest;
import ca.uhn.fhir.util.ExtensionConstants;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.entity.ContentType;
import org.apache.http.message.BasicHeader;
import org.hl7.fhir.dstu3.model.CapabilityStatement;
import org.hl7.fhir.dstu3.model.CapabilityStatement.CapabilityStatementRestComponent;
import org.hl7.fhir.dstu3.model.CapabilityStatement.CapabilityStatementRestResourceComponent;
import org.hl7.fhir.dstu3.model.DecimalType;
import org.hl7.fhir.dstu3.model.Extension;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IDomainResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.thymeleaf.ITemplateEngine;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;

import static ca.uhn.fhir.util.UrlUtil.sanitizeUrlPart;
import static org.apache.commons.lang3.StringUtils.defaultString;

public class BaseController {
	static final String PARAM_RESOURCE = "resource";
	static final String RESOURCE_COUNT_EXT_URL = "http://hl7api.sourceforge.net/hapi-fhir/res/extdefs.html#resourceCount";
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(BaseController.class);
	@Autowired
	protected TesterConfig myConfig;
	private final Map<FhirVersionEnum, FhirContext> myContexts = new HashMap<FhirVersionEnum, FhirContext>();
	private List<String> myFilterHeaders;
	@Autowired
	private ITemplateEngine myTemplateEngine;

	public BaseController() {
		super();
	}

	protected IBaseResource addCommonParams(HttpServletRequest theServletRequest, final HomeRequest theRequest, final ModelMap theModel) {
		final String serverId = theRequest.getServerIdWithDefault(myConfig);
		final String serverBase = theRequest.getServerBase(theServletRequest, myConfig);
		final String serverName = theRequest.getServerName(myConfig);
		final String apiKey = theRequest.getApiKey(theServletRequest, myConfig);
		theModel.put("serverId", sanitizeInput(serverId));
		theModel.put("baseName", sanitizeInput(serverName));
		theModel.put("apiKey", sanitizeInput(apiKey));
		theModel.put("resourceName", sanitizeInput(defaultString(theRequest.getResource())));
		theModel.put("encoding", sanitizeInput(theRequest.getEncoding()));
		theModel.put("pretty", sanitizeInput(theRequest.getPretty()));
		theModel.put("_summary", sanitizeInput(theRequest.get_summary()));
		theModel.put("serverEntries", myConfig.getIdToServerName());

		// doesn't need sanitizing
		theModel.put("base", serverBase);

		return loadAndAddConf(theServletRequest, theRequest, theModel);
	}

	private Header[] applyHeaderFilters(Map<String, List<String>> theAllHeaders) {
		ArrayList<Header> retVal = new ArrayList<Header>();
		for (String nextKey : theAllHeaders.keySet()) {
			for (String nextValue : theAllHeaders.get(nextKey)) {
				if (myFilterHeaders == null || !myFilterHeaders.contains(nextKey.toLowerCase())) {
					retVal.add(new BasicHeader(nextKey, nextValue));
				}
			}
		}
		return retVal.toArray(new Header[retVal.size()]);
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

	protected FhirContext getContext(HomeRequest theRequest) {
		FhirVersionEnum version = theRequest.getFhirVersion(myConfig);
		FhirContext retVal = myContexts.get(version);
		if (retVal == null) {
			retVal = newContext(version);
			myContexts.put(version, retVal);
		}
		return retVal;
	}

	protected RuntimeResourceDefinition getResourceType(HomeRequest theRequest, HttpServletRequest theReq) throws ServletException {
		String resourceName = sanitizeUrlPart(defaultString(theReq.getParameter(PARAM_RESOURCE)));
		RuntimeResourceDefinition def = getContext(theRequest).getResourceDefinition(resourceName);
		if (def == null) {
			throw new ServletException(Msg.code(192) + "Invalid resourceName: " + resourceName);
		}
		return def;
	}

	protected ResultType handleClientException(GenericClient theClient, Exception e, ModelMap theModel) {
		ResultType returnsResource;
		returnsResource = ResultType.NONE;
		ourLog.warn("Failed to invoke server", e);

		if (e != null) {
			theModel.put("errorMsg", toDisplayError("Error: " + e.getMessage(), e));
		}

		return returnsResource;
	}

	private IBaseResource loadAndAddConf(HttpServletRequest theServletRequest, final HomeRequest theRequest, final ModelMap theModel) {
		switch (theRequest.getFhirVersion(myConfig)) {
			case DSTU2:
				return loadAndAddConfDstu2(theServletRequest, theRequest, theModel);
			case DSTU3:
				return loadAndAddConfDstu3(theServletRequest, theRequest, theModel);
			case R4:
				return loadAndAddConfR4(theServletRequest, theRequest, theModel);
			case R5:
				return loadAndAddConfR5(theServletRequest, theRequest, theModel);
			case DSTU2_1:
			case DSTU2_HL7ORG:
				break;
		}
		throw new IllegalStateException(Msg.code(193) + "Unknown version: " + theRequest.getFhirVersion(myConfig));
	}

	private IResource loadAndAddConfDstu2(HttpServletRequest theServletRequest, final HomeRequest theRequest, final ModelMap theModel) {
		CaptureInterceptor interceptor = new CaptureInterceptor();
		GenericClient client = theRequest.newClient(theServletRequest, getContext(theRequest), myConfig, interceptor);

		ca.uhn.fhir.model.dstu2.resource.Conformance conformance;
		try {
			conformance = client.fetchConformance().ofType(Conformance.class).execute();
		} catch (Exception e) {
			ourLog.warn("Failed to load conformance statement, error was: {}", e.toString());
			theModel.put("errorMsg", toDisplayError("Failed to load conformance statement, error was: " + e.toString(), e));
			conformance = new ca.uhn.fhir.model.dstu2.resource.Conformance();
		}

		theModel.put("jsonEncodedConf", getContext(theRequest).newJsonParser().encodeResourceToString(conformance));

		Map<String, Number> resourceCounts = new HashMap<>();
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

	private IBaseResource loadAndAddConfDstu3(HttpServletRequest theServletRequest, final HomeRequest theRequest, final ModelMap theModel) {
		CaptureInterceptor interceptor = new CaptureInterceptor();
		GenericClient client = theRequest.newClient(theServletRequest, getContext(theRequest), myConfig, interceptor);

		org.hl7.fhir.dstu3.model.CapabilityStatement capabilityStatement = new CapabilityStatement();
		try {
			capabilityStatement = client.fetchConformance().ofType(org.hl7.fhir.dstu3.model.CapabilityStatement.class).execute();
		} catch (Exception ex) {
			ourLog.warn("Failed to load conformance statement, error was: {}", ex.toString());
			theModel.put("errorMsg", toDisplayError("Failed to load conformance statement, error was: " + ex.toString(), ex));
		}

		theModel.put("jsonEncodedConf", getContext(theRequest).newJsonParser().encodeResourceToString(capabilityStatement));

		Map<String, Number> resourceCounts = new HashMap<>();
		long total = 0;

		for (CapabilityStatementRestComponent nextRest : capabilityStatement.getRest()) {
			for (CapabilityStatementRestResourceComponent nextResource : nextRest.getResource()) {
				List<Extension> exts = nextResource.getExtensionsByUrl(RESOURCE_COUNT_EXT_URL);
				if (exts != null && exts.size() > 0) {
					Number nextCount = ((DecimalType) (exts.get(0).getValue())).getValueAsNumber();
					resourceCounts.put(nextResource.getTypeElement().getValue(), nextCount);
					total += nextCount.longValue();
				}
			}
		}

		theModel.put("resourceCounts", resourceCounts);

		if (total > 0) {
			for (CapabilityStatementRestComponent nextRest : capabilityStatement.getRest()) {
				Collections.sort(nextRest.getResource(), new Comparator<CapabilityStatementRestResourceComponent>() {
					@Override
					public int compare(CapabilityStatementRestResourceComponent theO1, CapabilityStatementRestResourceComponent theO2) {
						DecimalType count1 = new DecimalType();
						List<Extension> count1exts = theO1.getExtensionsByUrl(RESOURCE_COUNT_EXT_URL);
						if (count1exts != null && count1exts.size() > 0) {
							count1 = (DecimalType) count1exts.get(0).getValue();
						}
						DecimalType count2 = new DecimalType();
						List<Extension> count2exts = theO2.getExtensionsByUrl(RESOURCE_COUNT_EXT_URL);
						if (count2exts != null && count2exts.size() > 0) {
							count2 = (DecimalType) count2exts.get(0).getValue();
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

		theModel.put("requiredParamExtension", ExtensionConstants.PARAM_IS_REQUIRED);

		theModel.put("conf", capabilityStatement);
		return capabilityStatement;
	}

	private IBaseResource loadAndAddConfR4(HttpServletRequest theServletRequest, final HomeRequest theRequest, final ModelMap theModel) {
		CaptureInterceptor interceptor = new CaptureInterceptor();
		GenericClient client = theRequest.newClient(theServletRequest, getContext(theRequest), myConfig, interceptor);

		org.hl7.fhir.r4.model.CapabilityStatement capabilityStatement = new org.hl7.fhir.r4.model.CapabilityStatement();
		try {
			capabilityStatement = client.fetchConformance().ofType(org.hl7.fhir.r4.model.CapabilityStatement.class).execute();
		} catch (Exception ex) {
			ourLog.warn("Failed to load conformance statement, error was: {}", ex.toString());
			theModel.put("errorMsg", toDisplayError("Failed to load conformance statement, error was: " + ex.toString(), ex));
		}

		theModel.put("jsonEncodedConf", getContext(theRequest).newJsonParser().encodeResourceToString(capabilityStatement));

		Map<String, Number> resourceCounts = new HashMap<>();
		long total = 0;

		for (org.hl7.fhir.r4.model.CapabilityStatement.CapabilityStatementRestComponent nextRest : capabilityStatement.getRest()) {
			for (org.hl7.fhir.r4.model.CapabilityStatement.CapabilityStatementRestResourceComponent nextResource : nextRest.getResource()) {
				List<org.hl7.fhir.r4.model.Extension> exts = nextResource.getExtensionsByUrl(RESOURCE_COUNT_EXT_URL);
				if (exts != null && exts.size() > 0) {
					Number nextCount = ((org.hl7.fhir.r4.model.DecimalType) (exts.get(0).getValue())).getValueAsNumber();
					resourceCounts.put(nextResource.getTypeElement().getValue(), nextCount);
					total += nextCount.longValue();
				}
			}
		}

		theModel.put("resourceCounts", resourceCounts);

		if (total > 0) {
			for (org.hl7.fhir.r4.model.CapabilityStatement.CapabilityStatementRestComponent nextRest : capabilityStatement.getRest()) {
				Collections.sort(nextRest.getResource(), new Comparator<org.hl7.fhir.r4.model.CapabilityStatement.CapabilityStatementRestResourceComponent>() {
					@Override
					public int compare(org.hl7.fhir.r4.model.CapabilityStatement.CapabilityStatementRestResourceComponent theO1, org.hl7.fhir.r4.model.CapabilityStatement.CapabilityStatementRestResourceComponent theO2) {
						org.hl7.fhir.r4.model.DecimalType count1 = new org.hl7.fhir.r4.model.DecimalType();
						List<org.hl7.fhir.r4.model.Extension> count1exts = theO1.getExtensionsByUrl(RESOURCE_COUNT_EXT_URL);
						if (count1exts != null && count1exts.size() > 0) {
							count1 = (org.hl7.fhir.r4.model.DecimalType) count1exts.get(0).getValue();
						}
						org.hl7.fhir.r4.model.DecimalType count2 = new org.hl7.fhir.r4.model.DecimalType();
						List<org.hl7.fhir.r4.model.Extension> count2exts = theO2.getExtensionsByUrl(RESOURCE_COUNT_EXT_URL);
						if (count2exts != null && count2exts.size() > 0) {
							count2 = (org.hl7.fhir.r4.model.DecimalType) count2exts.get(0).getValue();
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

		theModel.put("requiredParamExtension", ExtensionConstants.PARAM_IS_REQUIRED);

		theModel.put("conf", capabilityStatement);
		return capabilityStatement;
	}

	private IBaseResource loadAndAddConfR5(HttpServletRequest theServletRequest, final HomeRequest theRequest, final ModelMap theModel) {
		CaptureInterceptor interceptor = new CaptureInterceptor();
		GenericClient client = theRequest.newClient(theServletRequest, getContext(theRequest), myConfig, interceptor);

		org.hl7.fhir.r5.model.CapabilityStatement capabilityStatement = new org.hl7.fhir.r5.model.CapabilityStatement();
		try {
			capabilityStatement = client.fetchConformance().ofType(org.hl7.fhir.r5.model.CapabilityStatement.class).execute();
		} catch (Exception ex) {
			ourLog.warn("Failed to load conformance statement, error was: {}", ex.toString());
			theModel.put("errorMsg", toDisplayError("Failed to load conformance statement, error was: " + ex.toString(), ex));
		}

		theModel.put("jsonEncodedConf", getContext(theRequest).newJsonParser().encodeResourceToString(capabilityStatement));

		Map<String, Number> resourceCounts = new HashMap<>();
		long total = 0;

		for (org.hl7.fhir.r5.model.CapabilityStatement.CapabilityStatementRestComponent nextRest : capabilityStatement.getRest()) {
			for (org.hl7.fhir.r5.model.CapabilityStatement.CapabilityStatementRestResourceComponent nextResource : nextRest.getResource()) {
				List<org.hl7.fhir.r5.model.Extension> exts = nextResource.getExtensionsByUrl(RESOURCE_COUNT_EXT_URL);
				if (exts != null && exts.size() > 0) {
					Number nextCount = ((org.hl7.fhir.r5.model.DecimalType) (exts.get(0).getValue())).getValueAsNumber();
					resourceCounts.put(nextResource.getTypeElement().getValue(), nextCount);
					total += nextCount.longValue();
				}
			}
		}

		theModel.put("resourceCounts", resourceCounts);

		if (total > 0) {
			for (org.hl7.fhir.r5.model.CapabilityStatement.CapabilityStatementRestComponent nextRest : capabilityStatement.getRest()) {
				Collections.sort(nextRest.getResource(), new Comparator<org.hl7.fhir.r5.model.CapabilityStatement.CapabilityStatementRestResourceComponent>() {
					@Override
					public int compare(org.hl7.fhir.r5.model.CapabilityStatement.CapabilityStatementRestResourceComponent theO1, org.hl7.fhir.r5.model.CapabilityStatement.CapabilityStatementRestResourceComponent theO2) {
						org.hl7.fhir.r5.model.DecimalType count1 = new org.hl7.fhir.r5.model.DecimalType();
						List<org.hl7.fhir.r5.model.Extension> count1exts = theO1.getExtensionsByUrl(RESOURCE_COUNT_EXT_URL);
						if (count1exts != null && count1exts.size() > 0) {
							count1 = (org.hl7.fhir.r5.model.DecimalType) count1exts.get(0).getValue();
						}
						org.hl7.fhir.r5.model.DecimalType count2 = new org.hl7.fhir.r5.model.DecimalType();
						List<org.hl7.fhir.r5.model.Extension> count2exts = theO2.getExtensionsByUrl(RESOURCE_COUNT_EXT_URL);
						if (count2exts != null && count2exts.size() > 0) {
							count2 = (org.hl7.fhir.r5.model.DecimalType) count2exts.get(0).getValue();
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

		theModel.put("requiredParamExtension", ExtensionConstants.PARAM_IS_REQUIRED);

		theModel.put("conf", capabilityStatement);
		return capabilityStatement;
	}

	protected String logPrefix(ModelMap theModel) {
		return "[server=" + theModel.get("serverId") + "] - ";
	}

	protected FhirContext newContext(FhirVersionEnum version) {
		FhirContext retVal;
		retVal = new FhirContext(version);
		return retVal;
	}

	private String parseNarrative(HomeRequest theRequest, EncodingEnum theCtEnum, String theResultBody) {
		try {
			IBaseResource par = theCtEnum.newParser(getContext(theRequest)).parseResource(theResultBody);
			String retVal;
			if (par instanceof IResource) {
				IResource resource = (IResource) par;
				retVal = resource.getText().getDiv().getValueAsString();
			} else if (par instanceof IDomainResource) {
				retVal = ((IDomainResource) par).getText().getDivAsString();
			} else {
				retVal = null;
			}
			return StringUtils.defaultString(retVal);
		} catch (Exception e) {
			ourLog.error("Failed to parse resource", e);
			return "";
		}
	}

	protected String preProcessMessageBody(String theBody) {
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

	protected void processAndAddLastClientInvocation(GenericClient theClient, ResultType theResultType, ModelMap theModelMap, long theLatency, String outcomeDescription,
																	 CaptureInterceptor theInterceptor, HomeRequest theRequest) {
		try {
			IHttpRequest lastRequest = theInterceptor.getLastRequest();
			IHttpResponse lastResponse = theInterceptor.getLastResponse();
			String requestBody = null;
			String requestUrl = null;
			String action = null;
			String resultStatus = null;
			String resultBody = null;
			String mimeType = null;
			ContentType ct = null;
			if (lastRequest != null) {
				requestBody = lastRequest.getRequestBodyFromStream();
				requestUrl = lastRequest.getUri();
				action = lastRequest.getHttpVerbName();
			}
			if (lastResponse != null) {
				resultStatus = "HTTP " + lastResponse.getStatus() + " " + lastResponse.getStatusInfo();
				lastResponse.bufferEntity();
				try (InputStream input = lastResponse.readEntity()) {
					resultBody = IOUtils.toString(input, Constants.CHARSET_UTF8);
				}

				List<String> ctStrings = lastResponse.getHeaders(Constants.HEADER_CONTENT_TYPE);
				if (ctStrings != null && ctStrings.isEmpty() == false) {
					ct = ContentType.parse(ctStrings.get(0));
					mimeType = ct.getMimeType();
				}
			}

			EncodingEnum ctEnum = EncodingEnum.forContentType(mimeType);
			String narrativeString = "";

			StringBuilder resultDescription = new StringBuilder();
			IBaseResource riBundle = null;

			FhirContext context = getContext(theRequest);
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
							riBundle = context.newJsonParser().parseResource(resultBody);
						}
						break;
					case XML:
					default:
						if (theResultType == ResultType.RESOURCE) {
							narrativeString = parseNarrative(theRequest, ctEnum, resultBody);
							resultDescription.append("XML resource");
						} else if (theResultType == ResultType.BUNDLE) {
							resultDescription.append("XML bundle");
							riBundle = context.newXmlParser().parseResource(resultBody);
						}
						break;
				}
			}

			resultDescription.append(" (").append(defaultString(resultBody).length() + " bytes)");

			Header[] requestHeaders = lastRequest != null ? applyHeaderFilters(lastRequest.getAllHeaders()) : new Header[0];
			Header[] responseHeaders = lastResponse != null ? applyHeaderFilters(lastResponse.getAllHeaders()) : new Header[0];

			theModelMap.put("resultDescription", resultDescription.toString());
			theModelMap.put("action", action);
			theModelMap.put("ri", riBundle instanceof IAnyResource);
			theModelMap.put("riBundle", riBundle);
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
			theModelMap.put("errorMsg", toDisplayError("Error during processing: " + e.getMessage(), e));
		}

	}

	/**
	 * A hook to be overridden by subclasses. The overriding method can modify the error message
	 * based on its content and/or the related exception.
	 *
	 * @param theErrorMsg  The original error message to be displayed to the user.
	 * @param theException The exception that occurred. May be null.
	 * @return The modified error message to be displayed to the user.
	 */
	protected String toDisplayError(String theErrorMsg, Exception theException) {
		return theErrorMsg;
	}

	protected enum ResultType {
		BUNDLE, NONE, RESOURCE, TAGLIST
	}

	public static class CaptureInterceptor implements IClientInterceptor {

		private IHttpRequest myLastRequest;
		private IHttpResponse myLastResponse;
//		private String myResponseBody;

		public IHttpRequest getLastRequest() {
			return myLastRequest;
		}

		public IHttpResponse getLastResponse() {
			return myLastResponse;
		}

//		public String getLastResponseBody() {
//			return myResponseBody;
//		}

		@Override
		public void interceptRequest(IHttpRequest theRequest) {
			assert myLastRequest == null;

			myLastRequest = theRequest;
		}

		@Override
		public void interceptResponse(IHttpResponse theResponse) throws IOException {
			assert myLastResponse == null;
			myLastResponse = theResponse;
//			myLastResponse = ((ApacheHttpResponse) theResponse).getResponse();
//
//			HttpEntity respEntity = myLastResponse.getEntity();
//			if (respEntity != null) {
//				final byte[] bytes;
//				try {
//					bytes = IOUtils.toByteArray(respEntity.getContent());
//				} catch (IllegalStateException e) {
//					throw new InternalErrorException(Msg.code(194) + e);
//				}
//
//				myResponseBody = new String(bytes, "UTF-8");
//				myLastResponse.setEntity(new MyEntityWrapper(respEntity, bytes));
//			}
		}

//		private static class MyEntityWrapper extends HttpEntityWrapper {
//
//			private byte[] myBytes;
//
//			public MyEntityWrapper(HttpEntity theWrappedEntity, byte[] theBytes) {
//				super(theWrappedEntity);
//				myBytes = theBytes;
//			}
//
//			@Override
//			public InputStream getContent() throws IOException {
//				return new ByteArrayInputStream(myBytes);
//			}
//
//			@Override
//			public void writeTo(OutputStream theOutstream) throws IOException {
//				theOutstream.write(myBytes);
//			}
//
//		}

	}

	private static String sanitizeInput(String theString) {
		String retVal = theString;
		if (retVal != null) {
			for (int i = 0; i < retVal.length(); i++) {
				char nextChar = retVal.charAt(i);
				switch (nextChar) {
					case '\'':
					case '"':
					case '<':
					case '>':
					case '&':
					case '/':
						retVal = retVal.replace(nextChar, '_');
				}
			}
		}
		return retVal;
	}

}
