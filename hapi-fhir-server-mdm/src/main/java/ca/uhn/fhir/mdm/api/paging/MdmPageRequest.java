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

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.rest.server.IPagingProvider;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.dstu3.model.UnsignedIntType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.slf4j.Logger;
import org.springframework.data.domain.PageRequest;

import javax.annotation.Nullable;

import static ca.uhn.fhir.rest.api.Constants.PARAM_COUNT;
import static ca.uhn.fhir.rest.api.Constants.PARAM_OFFSET;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * This class is essentially just a data clump of offset + count, as well as the ability to convert itself into a standard
 * {@link PageRequest} for spring data to use. The reason we don't use PageRequest natively is because it is concerned with `pages` and `counts`,
 * but we are using `offset` and `count` which requires some minor translation.
 */
public class MdmPageRequest {

	private final int myPage;
	private final int myOffset;
	private final int myCount;

	public MdmPageRequest(@Nullable IPrimitiveType<Integer> theOffset, @Nullable IPrimitiveType<Integer> theCount, int theDefaultPageSize, int theMaximumPageSize) {
		myOffset = theOffset == null ? 0 : theOffset.getValue();
		myCount = theCount == null ? theDefaultPageSize : Math.min(theCount.getValue(), theMaximumPageSize);
		validatePagingParameters(myOffset, myCount);

		this.myPage = myOffset / myCount;
	}

	public MdmPageRequest(@Nullable Integer theOffset, @Nullable Integer theCount, int theDefaultPageSize, int theMaximumPageSize) {
		myOffset = theOffset == null ? 0 : theOffset;
		myCount = theCount == null ? theDefaultPageSize : Math.min(theCount, theMaximumPageSize);
		validatePagingParameters(myOffset, myCount);

		this.myPage = myOffset / myCount;
	}

	private void validatePagingParameters(int theOffset, int theCount) {
		String errorMessage = "";

		if (theOffset < 0) {
			errorMessage += PARAM_OFFSET + " must be greater than or equal to 0. ";
		}
		if (theCount <= 0 ) {
			errorMessage += PARAM_COUNT + " must be greater than 0.";
		}
		if (StringUtils.isNotEmpty(errorMessage)) {
			throw new InvalidRequestException(Msg.code(1524) + errorMessage);
		}
	}

	public int getOffset() {
		return myOffset;
	}

	public int getPage() {
		return myPage;
	}

	public int getCount() {
		return myCount;
	}

	public int getNextOffset() {
		return myOffset + myCount;
	}

	public int getPreviousOffset() {
		return myOffset - myCount;
	}

	public PageRequest toPageRequest() {
		return PageRequest.of(this.myPage, this.myCount);
	}
}
