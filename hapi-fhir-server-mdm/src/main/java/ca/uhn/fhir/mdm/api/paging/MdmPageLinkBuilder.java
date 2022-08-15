package ca.uhn.fhir.mdm.api.paging;

/*-
 * #%L
 * HAPI FHIR - Master Data Management
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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
		String urlWithoutPaging = RestfulServerUtils.createLinkSelfWithoutGivenParameters(theServletRequestDetails.getFhirServerBase(), theServletRequestDetails, Arrays.asList(PARAM_OFFSET, PARAM_COUNT));
		return buildMdmPageLinks(urlWithoutPaging, theCurrentPage, thePageRequest);
	}

	public static MdmPageLinkTuple buildMdmPageLinks(String theUrlWithoutPaging, Page<MdmLinkJson> theCurrentPage, MdmPageRequest thePageRequest) {
		MdmPageLinkTuple tuple = new MdmPageLinkTuple();
		tuple.setSelfLink(buildLinkWithOffsetAndCount(theUrlWithoutPaging, thePageRequest.getCount(), thePageRequest.getOffset()));
		if (theCurrentPage.hasNext()) {
			tuple.setNextLink(buildLinkWithOffsetAndCount(theUrlWithoutPaging,thePageRequest.getCount(), thePageRequest.getNextOffset()));
		}
		if (theCurrentPage.hasPrevious()) {
			tuple.setPreviousLink(buildLinkWithOffsetAndCount(theUrlWithoutPaging,thePageRequest.getCount(), thePageRequest.getPreviousOffset()));
		}
		return tuple;

	}

	public static String buildLinkWithOffsetAndCount(String theBaseUrl, int theCount, int theOffset) {
		StringBuilder builder = new StringBuilder();
		builder.append(theBaseUrl);
		if (!theBaseUrl.contains("?")) {
			builder.append("?");
		} else {
			builder.append("&");
		}
		builder.append(PARAM_OFFSET).append("=").append(theOffset);
		builder.append("&");
		builder.append(PARAM_COUNT).append("=").append(theCount);
		return builder.toString();
	}
}
