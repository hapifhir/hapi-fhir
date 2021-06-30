package ca.uhn.fhir.mdm.api.paging;

import ca.uhn.fhir.mdm.api.MdmLinkJson;
import ca.uhn.fhir.rest.server.RestfulServerUtils;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import org.springframework.data.domain.Page;

import java.util.Arrays;

import static ca.uhn.fhir.rest.api.Constants.PARAM_COUNT;
import static ca.uhn.fhir.rest.api.Constants.PARAM_OFFSET;

/**
 * Builder to generate {@link MdmPageLinkTuple} objects, based on a given page of data and the incoming page request.
 */
public final class MdmPageLinkBuilder {

	/**
	 * Generates an {@link MdmPageLinkTuple} which contains previous/self/next links for pagination purposes.
	 *
	 * @param theServletRequestDetails the incoming request details. Used to determine server base.
	 * @param theCurrentPage the page of MDM link data. Used for determining if there are next/previous pages available.
	 * @param thePageRequest the incoming Page request, containing requested offset and count. Used for building offset for outgoing URLs.
	 *
	 * @return the {@link MdmPageLinkTuple}
	 */
	public static MdmPageLinkTuple buildMdmPageLinks(ServletRequestDetails theServletRequestDetails, Page<MdmLinkJson> theCurrentPage, MdmPageRequest thePageRequest) {
		MdmPageLinkTuple tuple = new MdmPageLinkTuple();
		String urlWithoutPaging = RestfulServerUtils.createLinkSelfWithoutGivenParameters(theServletRequestDetails.getFhirServerBase(), theServletRequestDetails, Arrays.asList(PARAM_OFFSET, PARAM_COUNT));
		tuple.setSelfLink(buildLinkWithOffsetAndCount(urlWithoutPaging, thePageRequest.getCount(), thePageRequest.getOffset()));
		if (theCurrentPage.hasNext()) {
			tuple.setNextLink(buildLinkWithOffsetAndCount(urlWithoutPaging,thePageRequest.getCount(), thePageRequest.getNextOffset()));
		}
		if (theCurrentPage.hasPrevious()) {
			tuple.setPreviousLink(buildLinkWithOffsetAndCount(urlWithoutPaging,thePageRequest.getCount(), thePageRequest.getPreviousOffset()));
		}
		return tuple;
	}

	private static String buildLinkWithOffsetAndCount(String theStartingUrl, int theCount, int theOffset) {
		StringBuilder builder = new StringBuilder();
		builder.append(theStartingUrl);
		if (!theStartingUrl.contains("?")) {
			builder.append("?");
		}
		builder.append(PARAM_OFFSET).append("=").append(theOffset);
		builder.append(PARAM_COUNT).append("=").append(theCount);
		return builder.toString();
	}
}
