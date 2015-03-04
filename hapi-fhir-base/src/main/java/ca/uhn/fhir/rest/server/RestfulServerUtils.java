package ca.uhn.fhir.rest.server;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPOutputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.utils.DateUtils;
import org.hl7.fhir.instance.model.api.IBaseBinary;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum;
import ca.uhn.fhir.model.api.Tag;
import ca.uhn.fhir.model.api.TagList;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.method.Request;
import ca.uhn.fhir.rest.method.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;

public class RestfulServerUtils {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(RestfulServerUtils.class);
	
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

	public static void streamResponseAsResource(RestfulServer theServer, HttpServletResponse theHttpResponse, IResource theResource, EncodingEnum theResponseEncoding, boolean thePrettyPrint, boolean theRequestIsBrowser, RestfulServer.NarrativeModeEnum theNarrativeMode, int stausCode, boolean theRespondGzip,
			String theServerBase) throws IOException {
		theHttpResponse.setStatus(stausCode);
	
		if (theResource.getId() != null && theResource.getId().hasIdPart() && isNotBlank(theServerBase)) {
			String resName = theServer.getFhirContext().getResourceDefinition(theResource).getName();
			IdDt fullId = theResource.getId().withServerBase(theServerBase, resName);
			theHttpResponse.addHeader(Constants.HEADER_CONTENT_LOCATION, fullId.getValue());
		}
	
		if (theServer.getETagSupport() == ETagSupportEnum.ENABLED) {
			if (theResource.getId().hasVersionIdPart()) {
				theHttpResponse.addHeader(Constants.HEADER_ETAG, "W/\"" + theResource.getId().getVersionIdPart() + '"');
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
	
		InstantDt lastUpdated = ResourceMetadataKeyEnum.UPDATED.get(theResource);
		if (lastUpdated != null && lastUpdated.isEmpty() == false) {
			theHttpResponse.addHeader(Constants.HEADER_LAST_MODIFIED, DateUtils.formatDate(lastUpdated.getValue()));
		}
	
		TagList list = (TagList) theResource.getResourceMetadata().get(ResourceMetadataKeyEnum.TAG_LIST);
		if (list != null) {
			for (Tag tag : list) {
				if (StringUtils.isNotBlank(tag.getTerm())) {
					theHttpResponse.addHeader(Constants.HEADER_CATEGORY, tag.toHeaderValue());
				}
			}
		}
	
		Writer writer = getWriter(theHttpResponse, theRespondGzip);
		try {
			if (theNarrativeMode == RestfulServer.NarrativeModeEnum.ONLY) {
				writer.append(theResource.getText().getDiv().getValueAsString());
			} else {
				getNewParser(theServer.getFhirContext(), responseEncoding, thePrettyPrint, theNarrativeMode).encodeResourceToWriter(theResource, writer);
			}
		} finally {
			writer.close();
		}
	}

	public static boolean prettyPrintResponse(Request theRequest) {
		Map<String, String[]> requestParams = theRequest.getParameters();
		String[] pretty = requestParams.remove(Constants.PARAM_PRETTY);
		boolean prettyPrint;
		if (pretty != null && pretty.length > 0) {
			if (Constants.PARAM_PRETTY_VALUE_TRUE.equals(pretty[0])) {
				prettyPrint = true;
			} else {
				prettyPrint = false;
			}
		} else {
			prettyPrint = false;
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

	public static EncodingEnum determineRequestEncoding(Request theReq) {
		Enumeration<String> acceptValues = theReq.getServletRequest().getHeaders(Constants.HEADER_CONTENT_TYPE);
		if (acceptValues != null) {
			while (acceptValues.hasMoreElements()) {
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
						EncodingEnum retVal = Constants.FORMAT_VAL_TO_ENCODING.get(nextPart);
						if (retVal != null) {
							return retVal;
						}
					}
				}
			}
		}
		return EncodingEnum.XML;
	}

	public static String createPagingLink(String theServerBase, String theSearchId, int theOffset, int theCount, EncodingEnum theResponseEncoding, boolean thePrettyPrint) {
		StringBuilder b = new StringBuilder();
		b.append(theServerBase);
		b.append('?');
		b.append(Constants.PARAM_PAGINGACTION);
		b.append('=');
		try {
			b.append(URLEncoder.encode(theSearchId, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			throw new Error("UTF-8 not supported", e);// should not happen
		}
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
		return b.toString();
	}

	public static void addProfileToBundleEntry(FhirContext theContext, IResource theResource, String theServerBase) {
	
		TagList tl = ResourceMetadataKeyEnum.TAG_LIST.get(theResource);
		if (tl == null) {
			tl = new TagList();
			ResourceMetadataKeyEnum.TAG_LIST.put(theResource, tl);
		}
	
		RuntimeResourceDefinition nextDef = theContext.getResourceDefinition(theResource);
		String profile = nextDef.getResourceProfile(theServerBase);
		if (isNotBlank(profile)) {
			tl.add(new Tag(Tag.HL7_ORG_PROFILE_TAG, profile, null));
		}
	}


	public static RestfulServer.NarrativeModeEnum determineNarrativeMode(RequestDetails theRequest) {
		Map<String, String[]> requestParams = theRequest.getParameters();
		String[] narrative = requestParams.remove(Constants.PARAM_NARRATIVE);
		RestfulServer.NarrativeModeEnum narrativeMode = null;
		if (narrative != null && narrative.length > 0) {
			narrativeMode = RestfulServer.NarrativeModeEnum.valueOfCaseInsensitive(narrative[0]);
		}
		if (narrativeMode == null) {
			narrativeMode = RestfulServer.NarrativeModeEnum.NORMAL;
		}
		return narrativeMode;
	}

	/**
	 * Determine whether a response should be given in JSON or XML format based on the incoming HttpServletRequest's
	 * <code>"_format"</code> parameter and <code>"Accept:"</code> HTTP header.
	 */
	public static EncodingEnum determineResponseEncodingWithDefault(RestfulServer theServer, HttpServletRequest theReq) {
		EncodingEnum retVal = determineResponseEncodingNoDefault(theReq);
		if (retVal == null) {
			retVal =theServer.getDefaultResponseEncoding();
		}
		return retVal;
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
	
		Enumeration<String> acceptValues = theReq.getHeaders(Constants.HEADER_ACCEPT);
		if (acceptValues != null) {
			while (acceptValues.hasMoreElements()) {
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
						EncodingEnum retVal = Constants.FORMAT_VAL_TO_ENCODING.get(nextPart);
						if (retVal != null) {
							return retVal;
						}
					}
				}
			}
		}
		return null;
	}

	public static Integer extractCountParameter(HttpServletRequest theRequest) {
		return RestfulServerUtils.tryToExtractNamedParameter(theRequest, Constants.PARAM_COUNT);
	}

	public static void streamResponseAsBundle(RestfulServer theServer, HttpServletResponse theHttpResponse, Bundle bundle, EncodingEnum theResponseEncoding, String theServerBase, boolean thePrettyPrint, RestfulServer.NarrativeModeEnum theNarrativeMode, boolean theRespondGzip, boolean theRequestIsBrowser) throws IOException {
		assert !theServerBase.endsWith("/");
	
		theHttpResponse.setStatus(200);
	
		EncodingEnum responseEncoding = theResponseEncoding!= null? theResponseEncoding : theServer.getDefaultResponseEncoding();
		
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
				RestfulServerUtils.getNewParser(theServer.getFhirContext(), responseEncoding, thePrettyPrint, theNarrativeMode).encodeBundleToWriter(bundle, writer);
			}
		} finally {
			writer.close();
		}
	}

	public static void streamResponseAsResource(RestfulServer theServer, HttpServletResponse theHttpResponse, IResource theResource, EncodingEnum theResponseEncoding, boolean thePrettyPrint, boolean theRequestIsBrowser, RestfulServer.NarrativeModeEnum theNarrativeMode, boolean theRespondGzip, String theServerBase)
			throws IOException {
		int stausCode = 200;
		RestfulServerUtils.streamResponseAsResource(theServer, theHttpResponse, theResource, theResponseEncoding, thePrettyPrint, theRequestIsBrowser, theNarrativeMode, stausCode, theRespondGzip, theServerBase);
	}

	public static void validateResourceListNotNull(List<IResource> theResourceList) {
		if (theResourceList == null) {
			throw new InternalErrorException("IBundleProvider returned a null list of resources - This is not allowed");
		}
	}



}
