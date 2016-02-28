package ca.uhn.fhir.rest.server;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2016 University Health Network
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
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBaseBinary;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum;
import ca.uhn.fhir.model.api.Tag;
import ca.uhn.fhir.model.api.TagList;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.model.valueset.BundleTypeEnum;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.PreferReturnEnum;
import ca.uhn.fhir.rest.api.SummaryEnum;
import ca.uhn.fhir.rest.method.ElementsParameter;
import ca.uhn.fhir.rest.method.RequestDetails;
import ca.uhn.fhir.rest.method.SummaryEnumParameter;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.util.DateUtils;

public class RestfulServerUtils {
	static final Pattern ACCEPT_HEADER_PATTERN = Pattern.compile("\\s*([a-zA-Z0-9+.*/-]+)\\s*(;\\s*([a-zA-Z]+)\\s*=\\s*([a-zA-Z0-9.]+)\\s*)?(,?)");

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(RestfulServerUtils.class);

	private static final HashSet<String> TEXT_ENCODE_ELEMENTS = new HashSet<String>(Arrays.asList("Bundle", "*.text"));

	public static void addProfileToBundleEntry(FhirContext theContext, IBaseResource theResource, String theServerBase) {
		if (theResource instanceof IResource) {
			if (theContext.getVersion().getVersion().isNewerThan(FhirVersionEnum.DSTU1)) {
				RuntimeResourceDefinition nextDef = theContext.getResourceDefinition(theResource);
				String profile = nextDef.getResourceProfile(theServerBase);
				if (isNotBlank(profile)) {
					List<IdDt> newList = new ArrayList<IdDt>();
					List<IdDt> existingList = ResourceMetadataKeyEnum.PROFILES.get((IResource) theResource);
					if (existingList != null) {
						newList.addAll(existingList);
					}
					newList.add(new IdDt(profile));
					ResourceMetadataKeyEnum.PROFILES.put((IResource) theResource, newList);
				}
			} else {
				TagList tl = ResourceMetadataKeyEnum.TAG_LIST.get((IResource) theResource);
				if (tl == null) {
					tl = new TagList();
					ResourceMetadataKeyEnum.TAG_LIST.put((IResource) theResource, tl);
				}

				RuntimeResourceDefinition nextDef = theContext.getResourceDefinition(theResource);
				String profile = nextDef.getResourceProfile(theServerBase);
				if (isNotBlank(profile)) {
					tl.add(new Tag(Tag.HL7_ORG_PROFILE_TAG, profile, null));
				}
			}
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
			if (summaryMode.contains(SummaryEnum.COUNT)) {
				parser.setEncodeElements(Collections.singleton("Bundle.total"));
			} else if (summaryMode.contains(SummaryEnum.TEXT)) {
				parser.setEncodeElements(TEXT_ENCODE_ELEMENTS);
			} else {
				parser.setSuppressNarratives(summaryMode.contains(SummaryEnum.DATA));
				parser.setSummaryMode(summaryMode.contains(SummaryEnum.TRUE));
			}
		}
		if (elements != null && elements.size() > 0) {
			Set<String> newElements = new HashSet<String>();
			for (String next : elements) {
				newElements.add("*." + next);
			}
			parser.setEncodeElements(newElements);
			parser.setEncodeElementsAppliesToResourceTypes(elementsAppliesTo);
		}
	}

	public static String createPagingLink(Set<Include> theIncludes, String theServerBase, String theSearchId, int theOffset, int theCount, EncodingEnum theResponseEncoding, boolean thePrettyPrint,
			BundleTypeEnum theBundleType) {
		try {
			StringBuilder b = new StringBuilder();
			b.append(theServerBase);
			b.append('?');
			b.append(Constants.PARAM_PAGINGACTION);
			b.append('=');
			b.append(URLEncoder.encode(theSearchId, "UTF-8"));

			b.append('&');
			b.append(Constants.PARAM_PAGINGOFFSET);
			b.append('=');
			b.append(theOffset);
			b.append('&');
			b.append(Constants.PARAM_COUNT);
			b.append('=');
			b.append(theCount);
			if (theResponseEncoding != null) {
				b.append('&');
				b.append(Constants.PARAM_FORMAT);
				b.append('=');
				b.append(theResponseEncoding.getRequestContentType());
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
						b.append(URLEncoder.encode(nextInclude.getValue(), "UTF-8"));
					}
				}
			}

			if (theBundleType != null) {
				b.append('&');
				b.append(Constants.PARAM_BUNDLETYPE);
				b.append('=');
				b.append(theBundleType.getCode());
			}

			return b.toString();
		} catch (UnsupportedEncodingException e) {
			throw new Error("UTF-8 not supported", e);// should not happen
		}
	}

	public static EncodingEnum determineRequestEncoding(RequestDetails theReq) {
		EncodingEnum retVal = determineRequestEncodingNoDefault(theReq);
		if (retVal != null) {
			return retVal;
		}
		return EncodingEnum.XML;
	}

	public static EncodingEnum determineRequestEncodingNoDefault(RequestDetails theReq) {
		EncodingEnum retVal = null;
		Iterator<String> acceptValues = theReq.getHeaders(Constants.HEADER_CONTENT_TYPE).iterator();
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
						retVal = Constants.FORMAT_VAL_TO_ENCODING.get(nextPart);
						if (retVal != null) {
							break;
						}
					}
				}
			}
		}
		return retVal;
	}

	/**
	 * Returns null if the request doesn't express that it wants FHIR. If it expresses that it wants XML and JSON equally, returns thePrefer.
	 */
	public static EncodingEnum determineResponseEncodingNoDefault(RequestDetails theReq, EncodingEnum thePrefer) {
		String[] format = theReq.getParameters().get(Constants.PARAM_FORMAT);
		if (format != null) {
			for (String nextFormat : format) {
				EncodingEnum retVal = Constants.FORMAT_VAL_TO_ENCODING.get(nextFormat);
				if (retVal != null) {
					return retVal;
				}
			}
		}

		/*
		 * The Accept header is kind of ridiculous, e.g.
		 */
		// text/xml, application/xml, application/xhtml+xml, text/html;q=0.9, text/plain;q=0.8, image/png, */*;q=0.5

		List<String> acceptValues = theReq.getHeaders(Constants.HEADER_ACCEPT);
		float bestQ = -1f;
		EncodingEnum retVal = null;
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
					EncodingEnum encoding;
					if (endSpaceIndex == -1) {
						if (startSpaceIndex == 0) {
							encoding = Constants.FORMAT_VAL_TO_ENCODING.get(nextToken);
						} else {
							encoding = Constants.FORMAT_VAL_TO_ENCODING.get(nextToken.substring(startSpaceIndex));
						}
					} else {
						encoding = Constants.FORMAT_VAL_TO_ENCODING.get(nextToken.substring(startSpaceIndex, endSpaceIndex));
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
						if (q > bestQ || (q == bestQ && encoding == thePrefer)) {
							retVal = encoding;
							bestQ = q;
						}
					}

				}

				//
				//
				//
				//
				// Matcher m = ACCEPT_HEADER_PATTERN.matcher(nextAcceptHeaderValue);
				// float q = 1.0f;
				// while (m.find()) {
				// String contentTypeGroup = m.group(1);
				// EncodingEnum encoding = Constants.FORMAT_VAL_TO_ENCODING.get(contentTypeGroup);
				// if (encoding != null) {
				//
				// String name = m.group(3);
				// String value = m.group(4);
				// if (name != null && value != null) {
				// if ("q".equals(name)) {
				// try {
				// q = Float.parseFloat(value);
				// q = Math.max(q, 0.0f);
				// } catch (NumberFormatException e) {
				// ourLog.debug("Invalid Accept header q value: {}", value);
				// }
				// }
				// }
				// }
				//
				// if (encoding != null) {
				// if (q > bestQ || (q == bestQ && encoding == thePrefer)) {
				// retVal = encoding;
				// bestQ = q;
				// }
				// }
				//
				// if (!",".equals(m.group(5))) {
				// break;
				// }
				// }
				//
			}

			return retVal;
		}
		return null;
	}

	/**
	 * Determine whether a response should be given in JSON or XML format based on the incoming HttpServletRequest's <code>"_format"</code> parameter and <code>"Accept:"</code> HTTP header.
	 */
	public static EncodingEnum determineResponseEncodingWithDefault(RequestDetails theReq) {
		EncodingEnum retVal = determineResponseEncodingNoDefault(theReq, theReq.getServer().getDefaultResponseEncoding());
		if (retVal == null) {
			retVal = theReq.getServer().getDefaultResponseEncoding();
		}
		return retVal;
	}

	public static Set<SummaryEnum> determineSummaryMode(RequestDetails theRequest) {
		Map<String, String[]> requestParams = theRequest.getParameters();

		Set<SummaryEnum> retVal = SummaryEnumParameter.getSummaryValueOrNull(theRequest);

		if (retVal == null) {
			/*
			 * HAPI originally supported a custom parameter called _narrative, but this has been superceded by an official parameter called _summary
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
					ourLog.debug("Invalid {} parameger: {}", Constants.PARAM_NARRATIVE, narrative[0]);
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

	public static IParser getNewParser(FhirContext theContext, RequestDetails theRequestDetails) {

		// Determine response encoding
		EncodingEnum responseEncoding = RestfulServerUtils.determineResponseEncodingWithDefault(theRequestDetails);
		IParser parser;
		switch (responseEncoding) {
		case JSON:
			parser = theContext.newJsonParser();
			break;
		case XML:
		default:
			parser = theContext.newXmlParser();
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
								}
								catch (NumberFormatException e) {
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
			if (Constants.PARAM_PRETTY_VALUE_TRUE.equals(pretty[0])) {
				prettyPrint = true;
			} else {
				prettyPrint = false;
			}
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

	public static Object streamResponseAsBundle(IRestfulServerDefaults theServer, Bundle bundle, Set<SummaryEnum> theSummaryMode, boolean respondGzip, RequestDetails theRequestDetails)
					throws IOException {

		int status = 200;

		// Determine response encoding
		EncodingEnum responseEncoding = RestfulServerUtils.determineResponseEncodingWithDefault(theRequestDetails);

		String contentType = responseEncoding.getBundleContentType();

		String charset = Constants.CHARSET_NAME_UTF8;
		Writer writer = theRequestDetails.getResponse().getResponseWriter(status, contentType, charset, respondGzip);

		try {
			IParser parser = RestfulServerUtils.getNewParser(theServer.getFhirContext(), theRequestDetails);
			if (theSummaryMode.contains(SummaryEnum.TEXT)) {
				parser.setEncodeElements(TEXT_ENCODE_ELEMENTS);
			}
			parser.encodeBundleToWriter(bundle, writer);
		}
		catch (Exception e) {
			//always send a response, even if the parsing went wrong
		}
		return theRequestDetails.getResponse().sendWriterResponse(status, contentType, charset, writer);
	}

	public static Object streamResponseAsResource(IRestfulServerDefaults theServer, IBaseResource theResource, Set<SummaryEnum> theSummaryMode,
			int stausCode, boolean theAddContentLocationHeader, boolean respondGzip,
			RequestDetails theRequestDetails)
					throws IOException {
		IRestfulResponse restUtil = theRequestDetails.getResponse();

		// Determine response encoding
		EncodingEnum responseEncoding = RestfulServerUtils.determineResponseEncodingNoDefault(theRequestDetails,
				theServer.getDefaultResponseEncoding());

		String serverBase = theRequestDetails.getFhirServerBase();
		if (theAddContentLocationHeader && theResource.getIdElement() != null && theResource.getIdElement().hasIdPart()
				&& isNotBlank(serverBase)) {
			String resName = theServer.getFhirContext().getResourceDefinition(theResource).getName();
			IIdType fullId = theResource.getIdElement().withServerBase(serverBase, resName);
			restUtil.addHeader(Constants.HEADER_CONTENT_LOCATION, fullId.getValue());
		}

		if (theServer.getETagSupport() == ETagSupportEnum.ENABLED) {
			if (theResource.getIdElement().hasVersionIdPart()) {
				restUtil.addHeader(Constants.HEADER_ETAG, "W/\"" + theResource.getIdElement().getVersionIdPart() + '"');
			}
		}

		if (theServer.getAddProfileTag() != AddProfileTagEnum.NEVER) {
			RuntimeResourceDefinition def = theServer.getFhirContext().getResourceDefinition(theResource);
			if (theServer.getAddProfileTag() == AddProfileTagEnum.ALWAYS || !def.isStandardProfile()) {
				addProfileToBundleEntry(theServer.getFhirContext(), theResource, serverBase);
			}
		}

		String contentType;
		if (theResource instanceof IBaseBinary && responseEncoding == null) {
			IBaseBinary bin = (IBaseBinary) theResource;
			if (isNotBlank(bin.getContentType())) {
				contentType = bin.getContentType();
			} else {
				contentType = Constants.CT_OCTET_STREAM;
			}
			// Force binary resources to download - This is a security measure to prevent
			// malicious images or HTML blocks being served up as content.
			restUtil.addHeader(Constants.HEADER_CONTENT_DISPOSITION, "Attachment;");

			return restUtil.sendAttachmentResponse(bin, stausCode, contentType);
		}

		// Ok, we're not serving a binary resource, so apply default encoding
		responseEncoding = responseEncoding != null ? responseEncoding : theServer.getDefaultResponseEncoding();

		boolean encodingDomainResourceAsText = theSummaryMode.contains(SummaryEnum.TEXT);
		if (encodingDomainResourceAsText) {
			/*
			 * If the user requests "text" for a bundle, only suppress the non text elements in the Element.entry.resource parts, we're not streaming just the narrative as HTML (since bundles don't even
			 * have one)
			 */
			if ("Bundle".equals(theServer.getFhirContext().getResourceDefinition(theResource).getName())) {
				encodingDomainResourceAsText = false;
			}
		}

		if (encodingDomainResourceAsText) {
			contentType = Constants.CT_HTML;
		} else {
			contentType = responseEncoding.getResourceContentType();
		}
		String charset = Constants.CHARSET_NAME_UTF8;

		if (theResource instanceof IResource) {
			InstantDt lastUpdated = ResourceMetadataKeyEnum.UPDATED.get((IResource) theResource);
			if (lastUpdated != null && lastUpdated.isEmpty() == false) {
				restUtil.addHeader(Constants.HEADER_LAST_MODIFIED, DateUtils.formatDate(lastUpdated.getValue()));
			}

			TagList list = (TagList) ((IResource) theResource).getResourceMetadata().get(ResourceMetadataKeyEnum.TAG_LIST);
			if (list != null) {
				for (Tag tag : list) {
					if (StringUtils.isNotBlank(tag.getTerm())) {
						restUtil.addHeader(Constants.HEADER_CATEGORY, tag.toHeaderValue());
					}
				}
			}
		} else {
			Date lastUpdated = ((IAnyResource) theResource).getMeta().getLastUpdated();
			if (lastUpdated != null) {
				restUtil.addHeader(Constants.HEADER_LAST_MODIFIED, DateUtils.formatDate(lastUpdated));
			}
		}

		Writer writer = restUtil.getResponseWriter(stausCode, contentType, charset, respondGzip);
		
		if (encodingDomainResourceAsText && theResource instanceof IResource) {
			writer.append(((IResource) theResource).getText().getDiv().getValueAsString());
		} else {
			IParser parser = getNewParser(theServer.getFhirContext(), theRequestDetails);
			parser.encodeResourceToWriter(theResource, writer);
		}
		
		return restUtil.sendWriterResponse(stausCode, contentType, charset, writer);
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

	public static Integer tryToExtractNamedParameter(RequestDetails theRequest, String theParamName) {
		String[] retVal = theRequest.getParameters().get(theParamName);
		if (retVal == null) {
			return null;
		}
		try {
			return Integer.parseInt(retVal[0]);
		} catch (NumberFormatException e) {
			ourLog.debug("Failed to parse {} value '{}': {}", new Object[] { theParamName, retVal[0], e });
			return null;
		}
	}

	public static void validateResourceListNotNull(List<? extends IBaseResource> theResourceList) {
		if (theResourceList == null) {
			throw new InternalErrorException("IBundleProvider returned a null list of resources - This is not allowed");
		}
	}

	private static enum NarrativeModeEnum {
		NORMAL, ONLY, SUPPRESS;

		public static NarrativeModeEnum valueOfCaseInsensitive(String theCode) {
			return valueOf(NarrativeModeEnum.class, theCode.toUpperCase());
		}
	}

}
