package ca.uhn.fhir.mdm.api.paging;

import ca.uhn.fhir.mdm.api.MdmLinkJson;
import ca.uhn.fhir.rest.server.RestfulServerUtils;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.util.ParametersUtil;
import org.springframework.data.domain.Page;

import java.util.Arrays;

import static ca.uhn.fhir.rest.api.Constants.PARAM_COUNT;
import static ca.uhn.fhir.rest.api.Constants.PARAM_OFFSET;

public final class MdmPageLinkBuilder {
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
	protected static String buildLinkWithOffsetAndCount(String theStartingUrl, int theCount, int theOffset) {
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
