package ca.uhn.fhir.mdm.api.paging;

import ca.uhn.fhir.rest.server.IPagingProvider;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.dstu3.model.UnsignedIntType;
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

	public MdmPageRequest(@Nullable UnsignedIntType theOffset, @Nullable UnsignedIntType theCount, IPagingProvider thePagingProvider) {
		myOffset = theOffset == null ? 0 : theOffset.getValue();
		myCount = theCount == null
			? thePagingProvider.getDefaultPageSize() : theCount.getValue() > thePagingProvider.getMaximumPageSize()
			? thePagingProvider.getMaximumPageSize() : theCount.getValue();

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
			throw new InvalidRequestException(errorMessage);
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
