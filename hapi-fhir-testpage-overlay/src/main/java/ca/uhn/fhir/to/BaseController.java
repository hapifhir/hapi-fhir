package ca.uhn.fhir.to;

import static org.apache.commons.lang3.StringUtils.defaultString;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.thymeleaf.TemplateEngine;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.api.BundleEntry;
import ca.uhn.fhir.model.api.ExtensionDt;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu.resource.Conformance;
import ca.uhn.fhir.model.dstu.resource.Conformance.Rest;
import ca.uhn.fhir.model.primitive.DecimalDt;
import ca.uhn.fhir.narrative.INarrativeGenerator;
import ca.uhn.fhir.rest.client.GenericClient;
import ca.uhn.fhir.rest.client.IClientInterceptor;
import ca.uhn.fhir.rest.server.EncodingEnum;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.to.model.HomeRequest;
import ca.uhn.fhir.util.ExtensionConstants;

public class BaseController {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(BaseController.class);
	static final String PARAM_RESOURCE = "resource";
	static final String RESOURCE_COUNT_EXT_URL = "http://hl7api.sourceforge.net/hapi-fhir/res/extdefs.html#resourceCount";

	@Autowired
	protected TesterConfig myConfig;
	private Map<FhirVersionEnum, FhirContext> myContexts = new HashMap<FhirVersionEnum, FhirContext>();
	private List<String> myFilterHeaders;
	@Autowired
	private TemplateEngine myTemplateEngine;

	public BaseController() {
		super();
	}

	protected IResource addCommonParams(HttpServletRequest theServletRequest, final HomeRequest theRequest, final ModelMap theModel) {
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

	protected FhirContext getContext(HomeRequest theRequest) {
		FhirVersionEnum version = theRequest.getFhirVersion(myConfig);
		FhirContext retVal = myContexts.get(version);
		if (retVal == null) {
			retVal = new FhirContext(version);
			myContexts.put(version, retVal);
		}
		return retVal;
	}

	protected RuntimeResourceDefinition getResourceType(HomeRequest theRequest, HttpServletRequest theReq) throws ServletException {
		String resourceName = StringUtils.defaultString(theReq.getParameter(PARAM_RESOURCE));
		RuntimeResourceDefinition def = getContext(theRequest).getResourceDefinition(resourceName);
		if (def == null) {
			throw new ServletException("Invalid resourceName: " + resourceName);
		}
		return def;
	}

	protected ResultType handleClientException(GenericClient theClient, Exception e, ModelMap theModel) {
		ResultType returnsResource;
		returnsResource = ResultType.NONE;
		ourLog.warn("Failed to invoke server", e);
	
		if (theClient.getLastResponse() == null) {
			theModel.put("errorMsg", "Error: " + e.getMessage());
		}
	
		return returnsResource;
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
		CaptureInterceptor interceptor = new CaptureInterceptor();
		GenericClient client = theRequest.newClient(theServletRequest, getContext(theRequest), myConfig, interceptor);
	
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
			for (ca.uhn.fhir.model.dstu.resource.Conformance.RestResource nextResource : nextRest.getResource()) {
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
				Collections.sort(nextRest.getResource(), new Comparator<ca.uhn.fhir.model.dstu.resource.Conformance.RestResource>() {
					@Override
					public int compare(ca.uhn.fhir.model.dstu.resource.Conformance.RestResource theO1, ca.uhn.fhir.model.dstu.resource.Conformance.RestResource theO2) {
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
		CaptureInterceptor interceptor = new CaptureInterceptor();
		GenericClient client = theRequest.newClient(theServletRequest, getContext(theRequest), myConfig, interceptor);
	
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

	protected String logPrefix(ModelMap theModel) {
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

	protected void processAndAddLastClientInvocation(GenericClient theClient, ResultType theResultType, ModelMap theModelMap, long theLatency, String outcomeDescription, CaptureInterceptor theInterceptor, HomeRequest theRequest) {
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

	protected enum ResultType {
			BUNDLE, NONE, RESOURCE, TAGLIST
		}

}