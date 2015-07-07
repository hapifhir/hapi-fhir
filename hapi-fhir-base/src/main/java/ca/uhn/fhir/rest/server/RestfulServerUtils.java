package ca.uhn.fhir.rest.server;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2015 University Health Network
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
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPOutputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.utils.DateUtils;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBaseBinary;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum;
import ca.uhn.fhir.model.api.Tag;
import ca.uhn.fhir.model.api.TagList;
import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.PreferReturnEnum;
import ca.uhn.fhir.rest.method.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;

public class RestfulServerUtils {
	static final Pattern ACCEPT_HEADER_PATTERN = Pattern.compile("\\s*([a-zA-Z0-9+.*/-]+)\\s*(;\\s*([a-zA-Z]+)\\s*=\\s*([a-zA-Z0-9.]+)\\s*)?(,?)");
	
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(RestfulServerUtils.class);

	public static void addProfileToBundleEntry(FhirContext theContext, IBaseResource theResource, String theServerBase) {
		if (theResource instanceof IResource) {
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
	
	public static String createPagingLink(Set<Include> theIncludes, String theServerBase, String theSearchId, int theOffset, int theCount, EncodingEnum theResponseEncoding, boolean thePrettyPrint) {
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

			return b.toString();
		} catch (UnsupportedEncodingException e) {
			throw new Error("UTF-8 not supported", e);// should not happen
		}
	}

	
	
	public static RestfulServer.NarrativeModeEnum determineNarrativeMode(RequestDetails theRequest) {
		Map<String, String[]> requestParams = theRequest.getParameters();
		String[] narrative = requestParams.get(Constants.PARAM_NARRATIVE);
		RestfulServer.NarrativeModeEnum narrativeMode = null;
		if (narrative != null && narrative.length > 0) {
			try {
				narrativeMode = RestfulServer.NarrativeModeEnum.valueOfCaseInsensitive(narrative[0]);
			} catch (IllegalArgumentException e) {
				ourLog.debug("Invalid {} parameger: {}", Constants.PARAM_NARRATIVE, narrative[0]);
				narrativeMode = null;
			}
		}
		if (narrativeMode == null) {
			narrativeMode = RestfulServer.NarrativeModeEnum.NORMAL;
		}
		return narrativeMode;
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
		Enumeration<String> acceptValues = theReq.getServletRequest().getHeaders(Constants.HEADER_CONTENT_TYPE);
		if (acceptValues != null) {
			while (acceptValues.hasMoreElements() && retVal == null) {
				String nextAcceptHeaderValue = acceptValues.nextElement();
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

	public static EncodingEnum determineResponseEncodingNoDefault(HttpServletRequest theReq) {
		String[] format = theReq.getParameterValues(Constants.PARAM_FORMAT);
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
		
		Enumeration<String> acceptValues = theReq.getHeaders(Constants.HEADER_ACCEPT);
		if (acceptValues != null) {
			float bestQ = -1f;
			EncodingEnum retVal = null;
			while (acceptValues.hasMoreElements()) {
				String nextAcceptHeaderValue = acceptValues.nextElement();
				Matcher m = ACCEPT_HEADER_PATTERN.matcher(nextAcceptHeaderValue);
				float q = 1.0f;
				while (m.find()) {
					String contentTypeGroup = m.group(1);
					EncodingEnum encoding = Constants.FORMAT_VAL_TO_ENCODING.get(contentTypeGroup);
					if (encoding != null) {
						
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
					}
					
					if (q > bestQ && encoding != null) {
						retVal = encoding;
						bestQ = q;
					}

					if (!",".equals(m.group(5))) {
						break;
					}
				}
				
			}
			
			return retVal;
		}
		return null;
	}

	/**
	 * Determine whether a response should be given in JSON or XML format based on the incoming HttpServletRequest's <code>"_format"</code> parameter and <code>"Accept:"</code> HTTP header.
	 */
	public static EncodingEnum determineResponseEncodingWithDefault(RestfulServer theServer, HttpServletRequest theReq) {
		EncodingEnum retVal = determineResponseEncodingNoDefault(theReq);
		if (retVal == null) {
			retVal = theServer.getDefaultResponseEncoding();
		}
		return retVal;
	}

	public static Integer extractCountParameter(HttpServletRequest theRequest) {
		return RestfulServerUtils.tryToExtractNamedParameter(theRequest, Constants.PARAM_COUNT);
	}

	public static IParser getNewParser(FhirContext theContext, EncodingEnum theResponseEncoding, boolean thePrettyPrint, RestfulServer.NarrativeModeEnum theNarrativeMode) {
		IParser parser;
		switch (theResponseEncoding) {
		case JSON:
			parser = theContext.newJsonParser();
			break;
		case XML:
		default:
			parser = theContext.newXmlParser();
			break;
		}
		return parser.setPrettyPrint(thePrettyPrint).setSuppressNarratives(theNarrativeMode == RestfulServer.NarrativeModeEnum.SUPPRESS);
	}

	static Writer getWriter(HttpServletResponse theHttpResponse, boolean theRespondGzip) throws UnsupportedEncodingException, IOException {
		Writer writer;
		if (theRespondGzip) {
			theHttpResponse.addHeader(Constants.HEADER_CONTENT_ENCODING, Constants.ENCODING_GZIP);
			writer = new OutputStreamWriter(new GZIPOutputStream(theHttpResponse.getOutputStream()), "UTF-8");
		} else {
			writer = theHttpResponse.getWriter();
		}
		return writer;
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

	public static boolean prettyPrintResponse(RestfulServer theServer, RequestDetails theRequest) {
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
			Enumeration<String> acceptValues = theRequest.getServletRequest().getHeaders(Constants.HEADER_ACCEPT);
			if (acceptValues != null) {
				while (acceptValues.hasMoreElements()) {
					String nextAcceptHeaderValue = acceptValues.nextElement();
					if (nextAcceptHeaderValue.contains("pretty=true")) {
						prettyPrint = true;
					}
				}
			}
		}
		return prettyPrint;
	}

	public static void streamResponseAsBundle(RestfulServer theServer, HttpServletResponse theHttpResponse, Bundle bundle, EncodingEnum theResponseEncoding, String theServerBase,
			boolean thePrettyPrint, RestfulServer.NarrativeModeEnum theNarrativeMode, boolean theRespondGzip, boolean theRequestIsBrowser) throws IOException {
		assert !theServerBase.endsWith("/");

		theHttpResponse.setStatus(200);

		EncodingEnum responseEncoding = theResponseEncoding != null ? theResponseEncoding : theServer.getDefaultResponseEncoding();

		if (theRequestIsBrowser && theServer.isUseBrowserFriendlyContentTypes()) {
			theHttpResponse.setContentType(responseEncoding.getBrowserFriendlyBundleContentType());
		} else if (theNarrativeMode == RestfulServer.NarrativeModeEnum.ONLY) {
			theHttpResponse.setContentType(Constants.CT_HTML);
		} else {
			theHttpResponse.setContentType(responseEncoding.getBundleContentType());
		}

		theHttpResponse.setCharacterEncoding(Constants.CHARSETNAME_UTF_8);

		theServer.addHeadersToResponse(theHttpResponse);

		Writer writer = RestfulServerUtils.getWriter(theHttpResponse, theRespondGzip);
		try {
			if (theNarrativeMode == RestfulServer.NarrativeModeEnum.ONLY) {
				for (IResource next : bundle.toListOfResources()) {
					writer.append(next.getText().getDiv().getValueAsString());
					writer.append("<hr/>");
				}
			} else {
				IParser parser = RestfulServerUtils.getNewParser(theServer.getFhirContext(), responseEncoding, thePrettyPrint, theNarrativeMode);
				parser.setServerBaseUrl(theServerBase);
				parser.encodeBundleToWriter(bundle, writer);
			}
		} finally {
			writer.close();
		}
	}

	public static void streamResponseAsResource(RestfulServer theServer, HttpServletResponse theHttpResponse, IBaseResource theResource, EncodingEnum theResponseEncoding, boolean thePrettyPrint,
			boolean theRequestIsBrowser, RestfulServer.NarrativeModeEnum theNarrativeMode, int stausCode, boolean theRespondGzip, String theServerBase, boolean theAddContentLocationHeader) throws IOException {
		theHttpResponse.setStatus(stausCode);

		if (theAddContentLocationHeader && theResource.getIdElement() != null && theResource.getIdElement().hasIdPart() && isNotBlank(theServerBase)) {
			String resName = theServer.getFhirContext().getResourceDefinition(theResource).getName();
			IIdType fullId = theResource.getIdElement().withServerBase(theServerBase, resName);
			theHttpResponse.addHeader(Constants.HEADER_CONTENT_LOCATION, fullId.getValue());
		}

		if (theServer.getETagSupport() == ETagSupportEnum.ENABLED) {
			if (theResource.getIdElement().hasVersionIdPart()) {
				theHttpResponse.addHeader(Constants.HEADER_ETAG, "W/\"" + theResource.getIdElement().getVersionIdPart() + '"');
			}
		}

		if (theServer.getAddProfileTag() != AddProfileTagEnum.NEVER) {
			RuntimeResourceDefinition def = theServer.getFhirContext().getResourceDefinition(theResource);
			if (theServer.getAddProfileTag() == AddProfileTagEnum.ALWAYS || !def.isStandardProfile()) {
				addProfileToBundleEntry(theServer.getFhirContext(), theResource, theServerBase);
			}
		}

		if (theResource instanceof IBaseBinary && theResponseEncoding == null) {
			IBaseBinary bin = (IBaseBinary) theResource;
			if (isNotBlank(bin.getContentType())) {
				theHttpResponse.setContentType(bin.getContentType());
			} else {
				theHttpResponse.setContentType(Constants.CT_OCTET_STREAM);
			}
			if (bin.getContent() == null || bin.getContent().length == 0) {
				return;
			}

			// Force binary resources to download - This is a security measure to prevent
			// malicious images or HTML blocks being served up as content.
			theHttpResponse.addHeader(Constants.HEADER_CONTENT_DISPOSITION, "Attachment;");

			theHttpResponse.setContentLength(bin.getContent().length);
			ServletOutputStream oos = theHttpResponse.getOutputStream();
			oos.write(bin.getContent());
			oos.close();
			return;
		}

		EncodingEnum responseEncoding = theResponseEncoding != null ? theResponseEncoding : theServer.getDefaultResponseEncoding();

		if (theRequestIsBrowser && theServer.isUseBrowserFriendlyContentTypes()) {
			theHttpResponse.setContentType(responseEncoding.getBrowserFriendlyBundleContentType());
		} else if (theNarrativeMode == RestfulServer.NarrativeModeEnum.ONLY) {
			theHttpResponse.setContentType(Constants.CT_HTML);
		} else {
			theHttpResponse.setContentType(responseEncoding.getResourceContentType());
		}
		theHttpResponse.setCharacterEncoding(Constants.CHARSETNAME_UTF_8);

		theServer.addHeadersToResponse(theHttpResponse);

		if (theResource instanceof IResource) {
			InstantDt lastUpdated = ResourceMetadataKeyEnum.UPDATED.get((IResource) theResource);
			if (lastUpdated != null && lastUpdated.isEmpty() == false) {
				theHttpResponse.addHeader(Constants.HEADER_LAST_MODIFIED, DateUtils.formatDate(lastUpdated.getValue()));
			}
	
			TagList list = (TagList) ((IResource)theResource).getResourceMetadata().get(ResourceMetadataKeyEnum.TAG_LIST);
			if (list != null) {
				for (Tag tag : list) {
					if (StringUtils.isNotBlank(tag.getTerm())) {
						theHttpResponse.addHeader(Constants.HEADER_CATEGORY, tag.toHeaderValue());
					}
				}
			}
		} else {
			Date lastUpdated = ((IAnyResource)theResource).getMeta().getLastUpdated();
			if (lastUpdated != null) {
				theHttpResponse.addHeader(Constants.HEADER_LAST_MODIFIED, DateUtils.formatDate(lastUpdated));
			}
		}

		Writer writer = getWriter(theHttpResponse, theRespondGzip);
		try {
			if (theNarrativeMode == RestfulServer.NarrativeModeEnum.ONLY && theResource instanceof IResource) {
				writer.append(((IResource)theResource).getText().getDiv().getValueAsString());
			} else {
				IParser parser = getNewParser(theServer.getFhirContext(), responseEncoding, thePrettyPrint, theNarrativeMode);
				parser.setServerBaseUrl(theServerBase);
				parser.encodeResourceToWriter(theResource, writer);
			}
		} finally {
			writer.close();
		}
	}

	static Integer tryToExtractNamedParameter(HttpServletRequest theRequest, String name) {
		String countString = theRequest.getParameter(name);
		Integer count = null;
		if (isNotBlank(countString)) {
			try {
				count = Integer.parseInt(countString);
			} catch (NumberFormatException e) {
				ourLog.debug("Failed to parse _count value '{}': {}", countString, e);
			}
		}
		return count;
	}

//	public static void streamResponseAsResource(RestfulServer theServer, HttpServletResponse theHttpResponse, IResource theResource, EncodingEnum theResponseEncoding, boolean thePrettyPrint,
//			boolean theRequestIsBrowser, RestfulServer.NarrativeModeEnum theNarrativeMode, boolean theRespondGzip, String theServerBase) throws IOException {
//		int stausCode = 200;
//		RestfulServerUtils.streamResponseAsResource(theServer, theHttpResponse, theResource, theResponseEncoding, thePrettyPrint, theRequestIsBrowser, theNarrativeMode, stausCode, theRespondGzip,
//				theServerBase);
//	}

	public static void validateResourceListNotNull(List<? extends IBaseResource> theResourceList) {
		if (theResourceList == null) {
			throw new InternalErrorException("IBundleProvider returned a null list of resources - This is not allowed");
		}
	}

}
