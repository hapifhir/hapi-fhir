package ca.uhn.fhir.mdm.api.paging;

import ca.uhn.fhir.rest.server.IRestfulServerDefaults;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.dstu3.model.UnsignedIntType;
import org.slf4j.Logger;
import org.springframework.data.domain.PageRequest;

import static ca.uhn.fhir.rest.api.Constants.PARAM_COUNT;
import static ca.uhn.fhir.rest.api.Constants.PARAM_OFFSET;
import static org.slf4j.LoggerFactory.getLogger;

public class MdmPageRequest {
	private static final Logger ourLog = getLogger(MdmPageRequest.class);

	private int myPage;
	private int myOffset;
	private int myCount;
	private IRestfulServerDefaults myRestfulServerDefaults;

	public MdmPageRequest(UnsignedIntType theOffset, UnsignedIntType theCount, IRestfulServerDefaults theDefaults) {
		myOffset = theOffset == null ? 0 : theOffset.getValue();
		myCount = theCount == null ? theDefaults.getDefaultPageSize() : theCount.getValue();
		validatePagingParameters(myOffset, myCount);

		this.myPage = myOffset / myCount;
	}

	public PageRequest toPageRequest() {
		return PageRequest.of(this.myPage, this.myCount);
	}

	private void validatePagingParameters(int theOffset, int theCount) {
		String errorMessage = "";

		if (theOffset < 0) {
			errorMessage += PARAM_OFFSET + " must be greater than or equal to 0. ";
		}
		if (theCount <= 0 ) {
			errorMessage += PARAM_COUNT + " must be greater than 0.";
		}
		if (myRestfulServerDefaults.getMaximumPageSize() != null && theCount > myRestfulServerDefaults.getMaximumPageSize() ) {
			ourLog.debug("Shrinking page size down to {}, as this is the maximum allowed.", myRestfulServerDefaults.getMaximumPageSize());
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
}
