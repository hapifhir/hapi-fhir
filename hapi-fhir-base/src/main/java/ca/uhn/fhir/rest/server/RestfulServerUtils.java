package ca.uhn.fhir.rest.server;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.zip.GZIPOutputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.http.client.utils.DateUtils;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.api.BundleEntry;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum;
import ca.uhn.fhir.model.api.Tag;
import ca.uhn.fhir.model.api.TagList;
import ca.uhn.fhir.model.base.composite.BaseResourceReferenceDt;
import ca.uhn.fhir.model.base.resource.BaseBinary;
import ca.uhn.fhir.model.base.resource.BaseOperationOutcome;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.model.valueset.BundleEntrySearchModeEnum;
import ca.uhn.fhir.model.valueset.BundleTypeEnum;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.method.Request;
import ca.uhn.fhir.rest.method.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;

public class RestfulServerUtils {

	static Integer tryToExtractNamedParameter(HttpServletRequest theRequest, String name) {
		String countString = theRequest.getParameter(name);
		Integer count = null;
		if (isNotBlank(countString)) {
			try {
				count = Integer.parseInt(countString);
			} catch (NumberFormatException e) {
				RestfulServer.ourLog.debug("Failed to parse _count value '{}': {}", countString, e);
			}
		}
		return count;
	}

	static void streamResponseAsResource(RestfulServer theServer, HttpServletResponse theHttpResponse, IResource theResource, EncodingEnum theResponseEncoding, boolean thePrettyPrint, boolean theRequestIsBrowser, RestfulServer.NarrativeModeEnum theNarrativeMode, int stausCode, boolean theRespondGzip,
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
	
		if (theResource instanceof BaseBinary && theResponseEncoding == null) {
			BaseBinary bin = (BaseBinary) theResource;
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
		theHttpResponse.setCharacterEncoding(Constants.CHARSET_UTF_8);
	
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

	private static void addProfileToBundleEntry(FhirContext theContext, IResource theResource, String theServerBase) {
	
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

	public static Bundle createBundleFromResourceList(FhirContext theContext, String theAuthor, List<IResource> theResult, String theServerBase, String theCompleteUrl, int theTotalResults, BundleTypeEnum theBundleType) {
		Bundle bundle = new Bundle();
		bundle.getAuthorName().setValue(theAuthor);
		bundle.getBundleId().setValue(UUID.randomUUID().toString());
		bundle.getPublished().setToCurrentTimeInLocalTimeZone();
		bundle.getLinkBase().setValue(theServerBase);
		bundle.getLinkSelf().setValue(theCompleteUrl);
		bundle.getType().setValueAsEnum(theBundleType);
	
		List<IResource> includedResources = new ArrayList<IResource>();
		Set<IdDt> addedResourceIds = new HashSet<IdDt>();
	
		for (IResource next : theResult) {
			if (next.getId().isEmpty() == false) {
				addedResourceIds.add(next.getId());
			}
		}
	
		for (IResource next : theResult) {
	
			Set<String> containedIds = new HashSet<String>();
			for (IResource nextContained : next.getContained().getContainedResources()) {
				if (nextContained.getId().isEmpty() == false) {
					containedIds.add(nextContained.getId().getValue());
				}
			}
	
			if (theContext.getNarrativeGenerator() != null) {
				String title = theContext.getNarrativeGenerator().generateTitle(next);
				RestfulServer.ourLog.trace("Narrative generator created title: {}", title);
				if (StringUtils.isNotBlank(title)) {
					ResourceMetadataKeyEnum.TITLE.put(next, title);
				}
			} else {
				RestfulServer.ourLog.trace("No narrative generator specified");
			}
	
			List<BaseResourceReferenceDt> references = theContext.newTerser().getAllPopulatedChildElementsOfType(next, BaseResourceReferenceDt.class);
			do {
				List<IResource> addedResourcesThisPass = new ArrayList<IResource>();
	
				for (BaseResourceReferenceDt nextRef : references) {
					IResource nextRes = nextRef.getResource();
					if (nextRes != null) {
						if (nextRes.getId().hasIdPart()) {
							if (containedIds.contains(nextRes.getId().getValue())) {
								// Don't add contained IDs as top level resources
								continue;
							}
	
							IdDt id = nextRes.getId();
							if (id.hasResourceType() == false) {
								String resName = theContext.getResourceDefinition(nextRes).getName();
								id = id.withResourceType(resName);
							}
	
							if (!addedResourceIds.contains(id)) {
								addedResourceIds.add(id);
								addedResourcesThisPass.add(nextRes);
							}
	
						}
					}
				}
	
				// Linked resources may themselves have linked resources
				references = new ArrayList<BaseResourceReferenceDt>();
				for (IResource iResource : addedResourcesThisPass) {
					List<BaseResourceReferenceDt> newReferences = theContext.newTerser().getAllPopulatedChildElementsOfType(iResource, BaseResourceReferenceDt.class);
					references.addAll(newReferences);
				}
	
				includedResources.addAll(addedResourcesThisPass);
	
			} while (references.isEmpty() == false);
	
			bundle.addResource(next, theContext, theServerBase);
	
		}
	
		/*
		 * Actually add the resources to the bundle
		 */
		for (IResource next : includedResources) {
			BundleEntry entry = bundle.addResource(next, theContext, theServerBase);
			if (theContext.getVersion().getVersion().isNewerThan(FhirVersionEnum.DSTU1)) {
				if (entry.getSearchMode().isEmpty()) {
					entry.getSearchMode().setValueAsEnum(BundleEntrySearchModeEnum.INCLUDE);
				}
			}
		}
	
		bundle.getTotalResults().setValue(theTotalResults);
		return bundle;
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
		String name = Constants.PARAM_COUNT;
		return RestfulServerUtils.tryToExtractNamedParameter(theRequest, name);
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
	
		theHttpResponse.setCharacterEncoding(Constants.CHARSET_UTF_8);
	
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

	private static void validateResourceListNotNull(List<IResource> theResourceList) {
		if (theResourceList == null) {
			throw new InternalErrorException("IBundleProvider returned a null list of resources - This is not allowed");
		}
	}

	public static Bundle createBundleFromBundleProvider(RestfulServer theServer, IBundleProvider theResult, EncodingEnum theResponseEncoding, String theServerBase, String theCompleteUrl, boolean thePrettyPrint, int theOffset, Integer theLimit, String theSearchId, BundleTypeEnum theBundleType) {
	
		int numToReturn;
		String searchId = null;
		List<IResource> resourceList;
		if (theServer.getPagingProvider() == null) {
			numToReturn = theResult.size();
			resourceList = theResult.getResources(0, numToReturn);
			RestfulServerUtils.validateResourceListNotNull(resourceList);
	
		} else {
			IPagingProvider pagingProvider = theServer.getPagingProvider();
			if (theLimit == null) {
				numToReturn = pagingProvider.getDefaultPageSize();
			} else {
				numToReturn = Math.min(pagingProvider.getMaximumPageSize(), theLimit);
			}
	
			numToReturn = Math.min(numToReturn, theResult.size() - theOffset);
			resourceList = theResult.getResources(theOffset, numToReturn + theOffset);
			RestfulServerUtils.validateResourceListNotNull(resourceList);
	
			if (theSearchId != null) {
				searchId = theSearchId;
			} else {
				if (theResult.size() > numToReturn) {
					searchId = pagingProvider.storeResultList(theResult);
					Validate.notNull(searchId, "Paging provider returned null searchId");
				}
			}
		}
	
		for (IResource next : resourceList) {
			if (next.getId() == null || next.getId().isEmpty()) {
				if (!(next instanceof BaseOperationOutcome)) {
					throw new InternalErrorException("Server method returned resource of type[" + next.getClass().getSimpleName() + "] with no ID specified (IResource#setId(IdDt) must be called)");
				}
			}
		}
	
		if (theServer.getAddProfileTag() != AddProfileTagEnum.NEVER) {
			for (IResource nextRes : resourceList) {
				RuntimeResourceDefinition def = theServer.getFhirContext().getResourceDefinition(nextRes);
				if (theServer.getAddProfileTag() == AddProfileTagEnum.ALWAYS || !def.isStandardProfile()) {
					RestfulServerUtils.addProfileToBundleEntry(theServer.getFhirContext(), nextRes, theServerBase);
				}
			}
		}
	
		Bundle bundle = RestfulServerUtils.createBundleFromResourceList(theServer.getFhirContext(), theServer.getServerName(), resourceList, theServerBase, theCompleteUrl, theResult.size(), theBundleType);
	
		bundle.setPublished(theResult.getPublished());
	
		if (theServer.getPagingProvider() != null) {
			int limit;
			limit = theLimit != null ? theLimit : theServer.getPagingProvider().getDefaultPageSize();
			limit = Math.min(limit, theServer.getPagingProvider().getMaximumPageSize());
	
			if (searchId != null) {
				if (theOffset + numToReturn < theResult.size()) {
					bundle.getLinkNext().setValue(RestfulServerUtils.createPagingLink(theServerBase, searchId, theOffset + numToReturn, numToReturn, theResponseEncoding, thePrettyPrint));
				}
				if (theOffset > 0) {
					int start = Math.max(0, theOffset - limit);
					bundle.getLinkPrevious().setValue(RestfulServerUtils.createPagingLink(theServerBase, searchId, start, limit, theResponseEncoding, thePrettyPrint));
				}
			}
		}
		return bundle;
	}

}
