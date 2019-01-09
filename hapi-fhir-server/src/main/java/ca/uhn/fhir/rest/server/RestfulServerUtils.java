package ca.uhn.fhir.rest.server;

/*
 * #%L
 * HAPI FHIR - Server Framework
 * %%
 * Copyright (C) 2014 - 2019 University Health Network
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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum;
import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.model.valueset.BundleTypeEnum;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.*;
import ca.uhn.fhir.rest.api.server.IRestfulResponse;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.method.ElementsParameter;
import ca.uhn.fhir.rest.server.method.SummaryEnumParameter;
import ca.uhn.fhir.util.BinaryUtil;
import ca.uhn.fhir.util.DateUtils;
import ca.uhn.fhir.util.UrlUtil;
import org.hl7.fhir.instance.model.api.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.Writer;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.apache.commons.lang3.StringUtils.*;

public class RestfulServerUtils {
	static final Pattern ACCEPT_HEADER_PATTERN = Pattern.compile("\\s*([a-zA-Z0-9+.*/-]+)\\s*(;\\s*([a-zA-Z]+)\\s*=\\s*([a-zA-Z0-9.]+)\\s*)?(,?)");

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(RestfulServerUtils.class);

	private static final HashSet<String> TEXT_ENCODE_ELEMENTS = new HashSet<String>(Arrays.asList("Bundle", "*.text", "*.(mandatory)"));
	private static Map<FhirVersionEnum, FhirContext> myFhirContextMap = Collections.synchronizedMap(new HashMap<FhirVersionEnum, FhirContext>());

	private enum NarrativeModeEnum {
		NORMAL, ONLY, SUPPRESS;

		public static NarrativeModeEnum valueOfCaseInsensitive(String theCode) {
			return valueOf(NarrativeModeEnum.class, theCode.toUpperCase());
		}
	}

	/**
	 * Return type for {@link RestfulServerUtils#determineRequestEncodingNoDefault(RequestDetails)}
	 */
	public static class ResponseEncoding {
		private final String myContentType;
		private final EncodingEnum myEncoding;
		private final Boolean myNonLegacy;

		public ResponseEncoding(FhirContext theCtx, EncodingEnum theEncoding, String theContentType) {
			super();
			myEncoding = theEncoding;
			myContentType = theContentType;
			if (theContentType != null) {
				FhirVersionEnum ctxtEnum = theCtx.getVersion().getVersion();
				if (theContentType.equals(EncodingEnum.JSON_PLAIN_STRING) || theContentType.equals(EncodingEnum.XML_PLAIN_STRING)) {
					myNonLegacy = ctxtEnum.isNewerThan(FhirVersionEnum.DSTU2_1);
				} else {
					myNonLegacy = ctxtEnum.isNewerThan(FhirVersionEnum.DSTU2_1) && !EncodingEnum.isLegacy(theContentType);
				}
			} else {
				FhirVersionEnum ctxtEnum = theCtx.getVersion().getVersion();
				if (ctxtEnum.isOlderThan(FhirVersionEnum.DSTU3)) {
					myNonLegacy = null;
				} else {
					myNonLegacy = Boolean.TRUE;
				}
			}
		}

		public String getContentType() {
			return myContentType;
		}

		public EncodingEnum getEncoding() {
			return myEncoding;
		}

		public String getResourceContentType() {
			if (Boolean.TRUE.equals(isNonLegacy())) {
				return getEncoding().getResourceContentTypeNonLegacy();
			}
			return getEncoding().getResourceContentType();
		}

		Boolean isNonLegacy() {
			return myNonLegacy;
		}
	}

	public static void configureResponseParser(RequestDetails theRequestDetails, IParser parser) {
		// Pretty print
		boolean prettyPrint = RestfulServerUtils.prettyPrintResponse(theRequestDetails.getServer(), theRequestDetails);

		parser.setPrettyPrint(prettyPrint);
		parser.setServerBaseUrl(theRequestDetails.getFhirServerBase());

		// Summary mode
		Set<SummaryEnum> summaryMode = RestfulServerUtils.determineSummaryMode(theRequestDetails);

		// _elements
		Set<String> elements = ElementsParameter.getElementsValueOrNull(theRequestDetails);
		if (elements != null && summaryMode != null && !summaryMode.equals(Collections.singleton(SummaryEnum.FALSE))) {
			throw new InvalidRequestException("Cannot combine the " + Constants.PARAM_SUMMARY + " and " + Constants.PARAM_ELEMENTS + " parameters");
		}
		Set<String> elementsAppliesTo = null;
		if (elements != null && isNotBlank(theRequestDetails.getResourceName())) {
			elementsAppliesTo = Collections.singleton(theRequestDetails.getResourceName());
		}

		if (summaryMode != null) {
			if (summaryMode.contains(SummaryEnum.COUNT) && summaryMode.size() == 1) {
				parser.setEncodeElements(Collections.singleton("Bundle.total"));
			} else if (summaryMode.contains(SummaryEnum.TEXT) && summaryMode.size() == 1) {
				parser.setEncodeElements(TEXT_ENCODE_ELEMENTS);
			} else {
				parser.setSuppressNarratives(summaryMode.contains(SummaryEnum.DATA));
				parser.setSummaryMode(summaryMode.contains(SummaryEnum.TRUE));
			}
		}
		if (elements != null && elements.size() > 0) {
			Set<String> newElements = new HashSet<>();
			for (String next : elements) {
				newElements.add("*." + next);
			}

			/*
			 * We try to be smart about what the user is asking for
			 * when they include an _elements parameter. If we're responding
			 * to something that returns a Bundle (e.g. a search) we assume
			 * the elements don't apply to the Bundle itself, unless
			 * the client has explicitly scoped the Bundle
			 * (i.e. with Bundle.total or something like that)
			 */
			switch (theRequestDetails.getRestOperationType()) {
				case SEARCH_SYSTEM:
				case SEARCH_TYPE:
				case HISTORY_SYSTEM:
				case HISTORY_TYPE:
				case HISTORY_INSTANCE:
				case GET_PAGE:
					boolean haveExplicitBundleElement = false;
					for (String next : newElements) {
						if (next.startsWith("Bundle.")) {
							haveExplicitBundleElement = true;
							break;
						}
					}
					if (!haveExplicitBundleElement) {
						parser.setEncodeElementsAppliesToChildResourcesOnly(true);
					}
					break;
				default:
					break;
			}

			parser.setEncodeElements(newElements);
			parser.setEncodeElementsAppliesToResourceTypes(elementsAppliesTo);
		}
	}

	public static String createPagingLink(Set<Include> theIncludes, String theServerBase, String theSearchId, int theOffset, int theCount, Map<String, String[]> theRequestParameters, boolean thePrettyPrint,
													  BundleTypeEnum theBundleType) {
		return createPagingLink(theIncludes, theServerBase, theSearchId, theOffset, theCount, theRequestParameters, thePrettyPrint,
			theBundleType, null);
	}

	public static String createPagingLink(Set<Include> theIncludes, String theServerBase, String theSearchId, String thePageId, Map<String, String[]> theRequestParameters, boolean thePrettyPrint,
													  BundleTypeEnum theBundleType) {
		return createPagingLink(theIncludes, theServerBase, theSearchId, null, null, theRequestParameters, thePrettyPrint,
			theBundleType, thePageId);
	}

	private static String createPagingLink(Set<Include> theIncludes, String theServerBase, String theSearchId, Integer theOffset, Integer theCount, Map<String, String[]> theRequestParameters, boolean thePrettyPrint,
														BundleTypeEnum theBundleType, String thePageId) {
		StringBuilder b = new StringBuilder();
		b.append(theServerBase);
		b.append('?');
		b.append(Constants.PARAM_PAGINGACTION);
		b.append('=');
		b.append(UrlUtil.escapeUrlParam(theSearchId));

		if (theOffset != null) {
			b.append('&');
			b.append(Constants.PARAM_PAGINGOFFSET);
			b.append('=');
			b.append(theOffset);
		}
		if (theCount != null) {
			b.append('&');
			b.append(Constants.PARAM_COUNT);
			b.append('=');
			b.append(theCount);
		}
		if (isNotBlank(thePageId)) {
			b.append('&');
			b.append(Constants.PARAM_PAGEID);
			b.append('=');
			b.append(UrlUtil.escapeUrlParam(thePageId));
		}
		String[] strings = theRequestParameters.get(Constants.PARAM_FORMAT);
		if (strings != null && strings.length > 0) {
			b.append('&');
			b.append(Constants.PARAM_FORMAT);
			b.append('=');
			String format = strings[0];
			format = replace(format, " ", "+");
			b.append(UrlUtil.escapeUrlParam(format));
		}
		if (thePrettyPrint) {
			b.append('&');
			b.append(Constants.PARAM_PRETTY);
			b.append('=');
			b.append(Constants.PARAM_PRETTY_VALUE_TRUE);
		}

		if (theIncludes != null) {
			for (Include nextInclude : theIncludes) {
				if (isNotBlank(nextInclude.getValue())) {
					b.append('&');
					b.append(Constants.PARAM_INCLUDE);
					b.append('=');
					b.append(UrlUtil.escapeUrlParam(nextInclude.getValue()));
				}
			}
		}

		if (theBundleType != null) {
			b.append('&');
			b.append(Constants.PARAM_BUNDLETYPE);
			b.append('=');
			b.append(theBundleType.getCode());
		}

		String paramName = Constants.PARAM_ELEMENTS;
		String[] params = theRequestParameters.get(paramName);
		if (params != null) {
			for (String nextValue : params) {
				if (isNotBlank(nextValue)) {
					b.append('&');
					b.append(paramName);
					b.append('=');
					b.append(UrlUtil.escapeUrlParam(nextValue));
				}
			}
		}

		return b.toString();
	}

	/**
	 * @TODO: this method is only called from one place and should be removed anyway
	 */
	public static EncodingEnum determineRequestEncoding(RequestDetails theReq) {
		EncodingEnum retVal = determineRequestEncodingNoDefault(theReq);
		if (retVal != null) {
			return retVal;
		}
		return EncodingEnum.XML;
	}

	public static EncodingEnum determineRequestEncodingNoDefault(RequestDetails theReq) {
		ResponseEncoding retVal = determineRequestEncodingNoDefaultReturnRE(theReq);
		if (retVal == null) {
			return null;
		}
		return retVal.getEncoding();
	}

	private static ResponseEncoding determineRequestEncodingNoDefaultReturnRE(RequestDetails theReq) {
		ResponseEncoding retVal = null;
		List<String> headers = theReq.getHeaders(Constants.HEADER_CONTENT_TYPE);
		if (headers != null) {
			Iterator<String> acceptValues = headers.iterator();
			if (acceptValues != null) {
				while (acceptValues.hasNext() && retVal == null) {
					String nextAcceptHeaderValue = acceptValues.next();
					if (nextAcceptHeaderValue != null && isNotBlank(nextAcceptHeaderValue)) {
						for (String nextPart : nextAcceptHeaderValue.split(",")) {
							int scIdx = nextPart.indexOf(';');
							if (scIdx == 0) {
								continue;
							}
							if (scIdx != -1) {
								nextPart = nextPart.substring(0, scIdx);
							}
							nextPart = nextPart.trim();
							EncodingEnum encoding = EncodingEnum.forContentType(nextPart);
							if (encoding != null) {
								retVal = new ResponseEncoding(theReq.getServer().getFhirContext(), encoding, nextPart);
								break;
							}
						}
					}
				}
			}
		}
		return retVal;
	}

	/**
	 * Returns null if the request doesn't express that it wants FHIR. If it expresses that it wants XML and JSON
	 * equally, returns thePrefer.
	 */
	public static ResponseEncoding determineResponseEncodingNoDefault(RequestDetails theReq, EncodingEnum thePrefer) {
		return determineResponseEncodingNoDefault(theReq, thePrefer, null);
	}

	/**
	 * Try to determing the response content type, given the request Accept header and
	 * _format parameter. If a value is provided to thePreferContents, we'll
	 * prefer to return that value over the native FHIR value.
	 */
	public static ResponseEncoding determineResponseEncodingNoDefault(RequestDetails theReq, EncodingEnum thePrefer, String thePreferContentType) {
		String[] format = theReq.getParameters().get(Constants.PARAM_FORMAT);
		if (format != null) {
			for (String nextFormat : format) {
				EncodingEnum retVal = EncodingEnum.forContentType(nextFormat);
				if (retVal != null) {
					return new ResponseEncoding(theReq.getServer().getFhirContext(), retVal, nextFormat);
				}
			}
		}

		/*
		 * Some browsers (e.g. FF) request "application/xml" in their Accept header,
		 * and we generally want to treat this as a preference for FHIR XML even if
		 * it's not the FHIR version of the CT, which should be "application/xml+fhir".
		 *
		 * When we're serving up Binary resources though, we are a bit more strict,
		 * since Binary is supposed to use native content types unless the client has
		 * explicitly requested FHIR.
		 */
		boolean strict = false;
		if ("Binary".equals(theReq.getResourceName())) {
			strict = true;
		}

		/*
		 * The Accept header is kind of ridiculous, e.g.
		 */
		// text/xml, application/xml, application/xhtml+xml, text/html;q=0.9, text/plain;q=0.8, image/png, */*;q=0.5

		List<String> acceptValues = theReq.getHeaders(Constants.HEADER_ACCEPT);
		float bestQ = -1f;
		ResponseEncoding retVal = null;
		if (acceptValues != null) {
			for (String nextAcceptHeaderValue : acceptValues) {
				StringTokenizer tok = new StringTokenizer(nextAcceptHeaderValue, ",");
				while (tok.hasMoreTokens()) {
					String nextToken = tok.nextToken();
					int startSpaceIndex = -1;
					for (int i = 0; i < nextToken.length(); i++) {
						if (nextToken.charAt(i) != ' ') {
							startSpaceIndex = i;
							break;
						}
					}

					if (startSpaceIndex == -1) {
						continue;
					}

					int endSpaceIndex = -1;
					for (int i = startSpaceIndex; i < nextToken.length(); i++) {
						if (nextToken.charAt(i) == ' ' || nextToken.charAt(i) == ';') {
							endSpaceIndex = i;
							break;
						}
					}

					float q = 1.0f;
					ResponseEncoding encoding;
					if (endSpaceIndex == -1) {
						if (startSpaceIndex == 0) {
							encoding = getEncodingForContentType(theReq.getServer().getFhirContext(), strict, nextToken, thePreferContentType);
						} else {
							encoding = getEncodingForContentType(theReq.getServer().getFhirContext(), strict, nextToken.substring(startSpaceIndex), thePreferContentType);
						}
					} else {
						encoding = getEncodingForContentType(theReq.getServer().getFhirContext(), strict, nextToken.substring(startSpaceIndex, endSpaceIndex), thePreferContentType);
						String remaining = nextToken.substring(endSpaceIndex + 1);
						StringTokenizer qualifierTok = new StringTokenizer(remaining, ";");
						while (qualifierTok.hasMoreTokens()) {
							String nextQualifier = qualifierTok.nextToken();
							int equalsIndex = nextQualifier.indexOf('=');
							if (equalsIndex != -1) {
								String nextQualifierKey = nextQualifier.substring(0, equalsIndex).trim();
								String nextQualifierValue = nextQualifier.substring(equalsIndex + 1, nextQualifier.length()).trim();
								if (nextQualifierKey.equals("q")) {
									try {
										q = Float.parseFloat(nextQualifierValue);
										q = Math.max(q, 0.0f);
									} catch (NumberFormatException e) {
										ourLog.debug("Invalid Accept header q value: {}", nextQualifierValue);
									}
								}
							}
						}
					}

					if (encoding != null) {
						if (q > bestQ || (q == bestQ && encoding.getEncoding() == thePrefer)) {
							retVal = encoding;
							bestQ = q;
						}
					}

				}

			}

		}

		/*
		 * If the client hasn't given any indication about which response
		 * encoding they want, let's try the request encoding in case that
		 * is useful (basically this catches the case where the request
		 * has a Content-Type header but not an Accept header)
		 */
		if (retVal == null) {
			retVal = determineRequestEncodingNoDefaultReturnRE(theReq);
		}

		return retVal;
	}

	/**
	 * Determine whether a response should be given in JSON or XML format based on the incoming HttpServletRequest's
	 * <code>"_format"</code> parameter and <code>"Accept:"</code> HTTP header.
	 */
	public static ResponseEncoding determineResponseEncodingWithDefault(RequestDetails theReq) {
		ResponseEncoding retVal = determineResponseEncodingNoDefault(theReq, theReq.getServer().getDefaultResponseEncoding());
		if (retVal == null) {
			retVal = new ResponseEncoding(theReq.getServer().getFhirContext(), theReq.getServer().getDefaultResponseEncoding(), null);
		}
		return retVal;
	}

	public static Set<SummaryEnum> determineSummaryMode(RequestDetails theRequest) {
		Map<String, String[]> requestParams = theRequest.getParameters();

		Set<SummaryEnum> retVal = SummaryEnumParameter.getSummaryValueOrNull(theRequest);

		if (retVal == null) {
			/*
			 * HAPI originally supported a custom parameter called _narrative, but this has been superceded by an official
			 * parameter called _summary
			 */
			String[] narrative = requestParams.get(Constants.PARAM_NARRATIVE);
			if (narrative != null && narrative.length > 0) {
				try {
					NarrativeModeEnum narrativeMode = NarrativeModeEnum.valueOfCaseInsensitive(narrative[0]);
					switch (narrativeMode) {
						case NORMAL:
							retVal = Collections.singleton(SummaryEnum.FALSE);
							break;
						case ONLY:
							retVal = Collections.singleton(SummaryEnum.TEXT);
							break;
						case SUPPRESS:
							retVal = Collections.singleton(SummaryEnum.DATA);
							break;
					}
				} catch (IllegalArgumentException e) {
					ourLog.debug("Invalid {} parameter: {}", Constants.PARAM_NARRATIVE, narrative[0]);
				}
			}
		}
		if (retVal == null) {
			retVal = Collections.singleton(SummaryEnum.FALSE);
		}

		return retVal;
	}

	public static Integer extractCountParameter(RequestDetails theRequest) {
		return RestfulServerUtils.tryToExtractNamedParameter(theRequest, Constants.PARAM_COUNT);
	}

	public static IPrimitiveType<Date> extractLastUpdatedFromResource(IBaseResource theResource) {
		IPrimitiveType<Date> lastUpdated = null;
		if (theResource instanceof IResource) {
			lastUpdated = ResourceMetadataKeyEnum.UPDATED.get((IResource) theResource);
		} else if (theResource instanceof IAnyResource) {
			lastUpdated = new InstantDt(theResource.getMeta().getLastUpdated());
		}
		return lastUpdated;
	}

	public static IIdType fullyQualifyResourceIdOrReturnNull(IRestfulServerDefaults theServer, IBaseResource theResource, String theServerBase, IIdType theResourceId) {
		IIdType retVal = null;
		if (theResourceId.hasIdPart() && isNotBlank(theServerBase)) {
			String resName = theResourceId.getResourceType();
			if (theResource != null && isBlank(resName)) {
				FhirContext context = theServer.getFhirContext();
				context = getContextForVersion(context, theResource.getStructureFhirVersionEnum());
				resName = context.getResourceDefinition(theResource).getName();
			}
			if (isNotBlank(resName)) {
				retVal = theResourceId.withServerBase(theServerBase, resName);
			}
		}
		return retVal;
	}

	private static FhirContext getContextForVersion(FhirContext theContext, FhirVersionEnum theForVersion) {
		FhirContext context = theContext;
		if (context.getVersion().getVersion() != theForVersion) {
			context = myFhirContextMap.get(theForVersion);
			if (context == null) {
				context = theForVersion.newContext();
				myFhirContextMap.put(theForVersion, context);
			}
		}
		return context;
	}

	private static ResponseEncoding getEncodingForContentType(FhirContext theFhirContext, boolean theStrict, String theContentType, String thePreferContentType) {
		EncodingEnum encoding;
		if (theStrict) {
			encoding = EncodingEnum.forContentTypeStrict(theContentType);
		} else {
			encoding = EncodingEnum.forContentType(theContentType);
		}
		if (isNotBlank(thePreferContentType)) {
			if (thePreferContentType.equals(theContentType)) {
				return new ResponseEncoding(theFhirContext, encoding, theContentType);
			}
		}
		if (encoding == null) {
			return null;
		}
		return new ResponseEncoding(theFhirContext, encoding, theContentType);
	}

	public static IParser getNewParser(FhirContext theContext, FhirVersionEnum theForVersion, RequestDetails theRequestDetails) {
		FhirContext context = getContextForVersion(theContext, theForVersion);

		// Determine response encoding
		EncodingEnum responseEncoding = RestfulServerUtils.determineResponseEncodingWithDefault(theRequestDetails).getEncoding();
		IParser parser;
		switch (responseEncoding) {
			case JSON:
				parser = context.newJsonParser();
				break;
			case XML:
			default:
				parser = context.newXmlParser();
				break;
		}

		configureResponseParser(theRequestDetails, parser);

		return parser;
	}

	public static Set<String> parseAcceptHeaderAndReturnHighestRankedOptions(HttpServletRequest theRequest) {
		Set<String> retVal = new HashSet<String>();

		Enumeration<String> acceptValues = theRequest.getHeaders(Constants.HEADER_ACCEPT);
		if (acceptValues != null) {
			float bestQ = -1f;
			while (acceptValues.hasMoreElements()) {
				String nextAcceptHeaderValue = acceptValues.nextElement();
				Matcher m = ACCEPT_HEADER_PATTERN.matcher(nextAcceptHeaderValue);
				float q = 1.0f;
				while (m.find()) {
					String contentTypeGroup = m.group(1);
					if (isNotBlank(contentTypeGroup)) {

						String name = m.group(3);
						String value = m.group(4);
						if (name != null && value != null) {
							if ("q".equals(name)) {
								try {
									q = Float.parseFloat(value);
									q = Math.max(q, 0.0f);
								} catch (NumberFormatException e) {
									ourLog.debug("Invalid Accept header q value: {}", value);
								}
							}
						}

						if (q > bestQ) {
							retVal.clear();
							bestQ = q;
						}

						if (q == bestQ) {
							retVal.add(contentTypeGroup.trim());
						}

					}

					if (!",".equals(m.group(5))) {
						break;
					}
				}

			}
		}

		return retVal;
	}

	public static PreferReturnEnum parsePreferHeader(String theValue) {
		if (isBlank(theValue)) {
			return null;
		}

		StringTokenizer tok = new StringTokenizer(theValue, ",");
		while (tok.hasMoreTokens()) {
			String next = tok.nextToken();
			int eqIndex = next.indexOf('=');
			if (eqIndex == -1 || eqIndex >= next.length() - 2) {
				continue;
			}

			String key = next.substring(0, eqIndex).trim();
			if (key.equals(Constants.HEADER_PREFER_RETURN) == false) {
				continue;
			}

			String value = next.substring(eqIndex + 1).trim();
			if (value.length() < 2) {
				continue;
			}
			if ('"' == value.charAt(0) && '"' == value.charAt(value.length() - 1)) {
				value = value.substring(1, value.length() - 1);
			}

			return PreferReturnEnum.fromHeaderValue(value);
		}

		return null;
	}

	public static boolean prettyPrintResponse(IRestfulServerDefaults theServer, RequestDetails theRequest) {
		Map<String, String[]> requestParams = theRequest.getParameters();
		String[] pretty = requestParams.get(Constants.PARAM_PRETTY);
		boolean prettyPrint;
		if (pretty != null && pretty.length > 0) {
			prettyPrint = Constants.PARAM_PRETTY_VALUE_TRUE.equals(pretty[0]);
		} else {
			prettyPrint = theServer.isDefaultPrettyPrint();
			List<String> acceptValues = theRequest.getHeaders(Constants.HEADER_ACCEPT);
			if (acceptValues != null) {
				for (String nextAcceptHeaderValue : acceptValues) {
					if (nextAcceptHeaderValue.contains("pretty=true")) {
						prettyPrint = true;
					}
				}
			}
		}
		return prettyPrint;
	}

	public static Object streamResponseAsResource(IRestfulServerDefaults theServer, IBaseResource theResource, Set<SummaryEnum> theSummaryMode, int stausCode, boolean theAddContentLocationHeader,
																 boolean respondGzip, RequestDetails theRequestDetails) throws IOException {
		return streamResponseAsResource(theServer, theResource, theSummaryMode, stausCode, null, theAddContentLocationHeader, respondGzip, theRequestDetails, null, null);
	}

	public static Object streamResponseAsResource(IRestfulServerDefaults theServer, IBaseResource theResource, Set<SummaryEnum> theSummaryMode, int theStatusCode, String theStatusMessage,
																 boolean theAddContentLocationHeader, boolean respondGzip, RequestDetails theRequestDetails, IIdType theOperationResourceId, IPrimitiveType<Date> theOperationResourceLastUpdated)
		throws IOException {
		IRestfulResponse response = theRequestDetails.getResponse();

		// Determine response encoding
		ResponseEncoding responseEncoding = RestfulServerUtils.determineResponseEncodingNoDefault(theRequestDetails, theServer.getDefaultResponseEncoding());

		String serverBase = theRequestDetails.getFhirServerBase();
		IIdType fullId = null;
		if (theOperationResourceId != null) {
			fullId = theOperationResourceId;
		} else if (theResource != null) {
			if (theResource.getIdElement() != null) {
				IIdType resourceId = theResource.getIdElement();
				fullId = fullyQualifyResourceIdOrReturnNull(theServer, theResource, serverBase, resourceId);
			}
		}

		if (theAddContentLocationHeader && fullId != null) {
			if (theRequestDetails.getRequestType() == RequestTypeEnum.POST) {
				response.addHeader(Constants.HEADER_LOCATION, fullId.getValue());
			}
			response.addHeader(Constants.HEADER_CONTENT_LOCATION, fullId.getValue());
		}

		if (theServer.getETagSupport() == ETagSupportEnum.ENABLED) {
			if (fullId != null && fullId.hasVersionIdPart()) {
				String versionIdPart = fullId.getVersionIdPart();
				response.addHeader(Constants.HEADER_ETAG, createEtag(versionIdPart));
			} else if (theResource != null && theResource.getMeta() != null && isNotBlank(theResource.getMeta().getVersionId())) {
				String versionId = theResource.getMeta().getVersionId();
				response.addHeader(Constants.HEADER_ETAG, createEtag(versionId));
			}
		}

		// Binary handling
		String contentType;
		if (theResource instanceof IBaseBinary) {
			IBaseBinary bin = (IBaseBinary) theResource;

			// Add a security context header
			IBaseReference securityContext = BinaryUtil.getSecurityContext(theServer.getFhirContext(), bin);
			if (securityContext != null) {
				String securityContextRef = securityContext.getReferenceElement().getValue();
				if (isNotBlank(securityContextRef)) {
					response.addHeader(Constants.HEADER_X_SECURITY_CONTEXT, securityContextRef);
				}
			}

			// If the user didn't explicitly request FHIR as a response, return binary
			// content directly
			if (responseEncoding == null) {
				if (isNotBlank(bin.getContentType())) {
					contentType = bin.getContentType();
				} else {
					contentType = Constants.CT_OCTET_STREAM;
				}

				// Force binary resources to download - This is a security measure to prevent
				// malicious images or HTML blocks being served up as content.
				response.addHeader(Constants.HEADER_CONTENT_DISPOSITION, "Attachment;");

				return response.sendAttachmentResponse(bin, theStatusCode, contentType);
			}
		}

		// Ok, we're not serving a binary resource, so apply default encoding
		if (responseEncoding == null) {
			responseEncoding = new ResponseEncoding(theServer.getFhirContext(), theServer.getDefaultResponseEncoding(), null);
		}

		boolean encodingDomainResourceAsText = theSummaryMode.size() == 1 && theSummaryMode.contains(SummaryEnum.TEXT);
		if (encodingDomainResourceAsText) {
			/*
			 * If the user requests "text" for a bundle, only suppress the non text elements in the Element.entry.resource
			 * parts, we're not streaming just the narrative as HTML (since bundles don't even
			 * have one)
			 */
			if ("Bundle".equals(theServer.getFhirContext().getResourceDefinition(theResource).getName())) {
				encodingDomainResourceAsText = false;
			}
		}

		/*
		 * Last-Modified header
		 */

		IPrimitiveType<Date> lastUpdated;
		if (theOperationResourceLastUpdated != null) {
			lastUpdated = theOperationResourceLastUpdated;
		} else {
			lastUpdated = extractLastUpdatedFromResource(theResource);
		}
		if (lastUpdated != null && lastUpdated.isEmpty() == false) {
			response.addHeader(Constants.HEADER_LAST_MODIFIED, DateUtils.formatDate(lastUpdated.getValue()));
		}

		/*
		 * Stream the response body
		 */

		if (theResource == null) {
			contentType = null;
		} else if (encodingDomainResourceAsText) {
			contentType = Constants.CT_HTML;
		} else {
			contentType = responseEncoding.getResourceContentType();
		}
		String charset = Constants.CHARSET_NAME_UTF8;

		Writer writer = response.getResponseWriter(theStatusCode, theStatusMessage, contentType, charset, respondGzip);
		if (theResource == null) {
			// No response is being returned
		} else if (encodingDomainResourceAsText && theResource instanceof IResource) {
			// DSTU2
			writer.append(((IResource) theResource).getText().getDiv().getValueAsString());
		} else if (encodingDomainResourceAsText && theResource instanceof IDomainResource) {
			// DSTU3+
			try {
				writer.append(((IDomainResource) theResource).getText().getDivAsString());
			} catch (Exception e) {
				throw new InternalErrorException(e);
			}
		} else {
			FhirVersionEnum forVersion = theResource.getStructureFhirVersionEnum();
			IParser parser = getNewParser(theServer.getFhirContext(), forVersion, theRequestDetails);
			parser.encodeResourceToWriter(theResource, writer);
		}
		//FIXME resource leak
		return response.sendWriterResponse(theStatusCode, contentType, charset, writer);
	}

	// static Integer tryToExtractNamedParameter(HttpServletRequest theRequest, String name) {
	// String countString = theRequest.getParameter(name);
	// Integer count = null;
	// if (isNotBlank(countString)) {
	// try {
	// count = Integer.parseInt(countString);
	// } catch (NumberFormatException e) {
	// ourLog.debug("Failed to parse _count value '{}': {}", countString, e);
	// }
	// }
	// return count;
	// }

	public static String createEtag(String theVersionId) {
		return "W/\"" + theVersionId + '"';
	}

	public static Integer tryToExtractNamedParameter(RequestDetails theRequest, String theParamName) {
		String[] retVal = theRequest.getParameters().get(theParamName);
		if (retVal == null) {
			return null;
		}
		try {
			return Integer.parseInt(retVal[0]);
		} catch (NumberFormatException e) {
			ourLog.debug("Failed to parse {} value '{}': {}", new Object[]{theParamName, retVal[0], e});
			return null;
		}
	}

	public static void validateResourceListNotNull(List<? extends IBaseResource> theResourceList) {
		if (theResourceList == null) {
			throw new InternalErrorException("IBundleProvider returned a null list of resources - This is not allowed");
		}
	}

}
