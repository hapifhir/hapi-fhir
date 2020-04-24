package ca.uhn.fhir.jpa.provider.r4;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
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

import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoValueSet;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import org.hl7.fhir.r4.model.*;

import javax.servlet.http.HttpServletRequest;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class BaseJpaResourceProviderValueSetR4 extends JpaResourceProviderR4<ValueSet> {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(BaseJpaResourceProviderValueSetR4.class);

	@Operation(name = JpaConstants.OPERATION_EXPAND, idempotent = true)
	public ValueSet expand(
		HttpServletRequest theServletRequest,
		@IdParam(optional = true) IdType theId,
		@OperationParam(name = "valueSet", min = 0, max = 1) ValueSet theValueSet,
		@OperationParam(name = "url", min = 0, max = 1) UriType theUrl,
		@OperationParam(name = "filter", min = 0, max = 1) StringType theFilter,
		@OperationParam(name = "offset", min = 0, max = 1) IntegerType theOffset,
		@OperationParam(name = "count", min = 0, max = 1) IntegerType theCount,
		RequestDetails theRequestDetails) {

		boolean haveId = theId != null && theId.hasIdPart();
		boolean haveIdentifier = theUrl != null && isNotBlank(theUrl.getValue());
		boolean haveValueSet = theValueSet != null && !theValueSet.isEmpty();

		if (!haveId && !haveIdentifier && !haveValueSet) {
			throw new InvalidRequestException("$expand operation at the type level (no ID specified) requires a url or a valueSet as a part of the request.");
		}

		if (moreThanOneTrue(haveId, haveIdentifier, haveValueSet)) {
			throw new InvalidRequestException("$expand must EITHER be invoked at the instance level, or have a url specified, or have a ValueSet specified. Can not combine these options.");
		}

		int offset = myDaoConfig.getPreExpandValueSetsDefaultOffset();
		if (theOffset != null && theOffset.hasValue()) {
			if (theOffset.getValue() >= 0) {
				offset = theOffset.getValue();
			} else {
				throw new InvalidRequestException("offset parameter for $expand operation must be >= 0 when specified. offset: " + theOffset.getValue());
			}
		}

		int count = myDaoConfig.getPreExpandValueSetsDefaultCount();
		if (theCount != null && theCount.hasValue()) {
			if (theCount.getValue() >= 0) {
				count = theCount.getValue();
			} else {
				throw new InvalidRequestException("count parameter for $expand operation must be >= 0 when specified. count: " + theCount.getValue());
			}
		}
		int countMax = myDaoConfig.getPreExpandValueSetsMaxCount();
		if (count > countMax) {
			ourLog.warn("count parameter for $expand operation of {} exceeds maximum value of {}; using maximum value.", count, countMax);
			count = countMax;
		}

		startRequest(theServletRequest);
		try {
			IFhirResourceDaoValueSet<ValueSet, Coding, CodeableConcept> dao = (IFhirResourceDaoValueSet<ValueSet, Coding, CodeableConcept>) getDao();
			if (myDaoConfig.isPreExpandValueSets()) {
				if (haveId) {
					return dao.expand(theId, toFilterString(theFilter), offset, count, theRequestDetails);
				} else if (haveIdentifier) {
					return dao.expandByIdentifier(theUrl.getValue(), toFilterString(theFilter), offset, count);
				} else {
					return dao.expand(theValueSet, toFilterString(theFilter), offset, count);
				}
			} else {
				if (haveId) {
					return dao.expand(theId, toFilterString(theFilter), theRequestDetails);
				} else if (haveIdentifier) {
					return dao.expandByIdentifier(theUrl.getValue(), toFilterString(theFilter));
				} else {
					return dao.expand(theValueSet, toFilterString(theFilter));
				}
			}
		} finally {
			endRequest(theServletRequest);
		}
	}


	private String toFilterString(StringType theFilter) {
		return theFilter != null ? theFilter.getValue() : null;
	}


	@SuppressWarnings("unchecked")
	@Operation(name = JpaConstants.OPERATION_VALIDATE_CODE, idempotent = true, returnParameters = {
		@OperationParam(name = "result", type = BooleanType.class, min = 1),
		@OperationParam(name = "message", type = StringType.class),
		@OperationParam(name = "display", type = StringType.class)
	})
	public Parameters validateCode(
		HttpServletRequest theServletRequest,
		@IdParam(optional = true) IdType theId,
		@OperationParam(name = "url", min = 0, max = 1) UriType theValueSetUrl,
		@OperationParam(name = "code", min = 0, max = 1) CodeType theCode,
		@OperationParam(name = "system", min = 0, max = 1) UriType theSystem,
		@OperationParam(name = "display", min = 0, max = 1) StringType theDisplay,
		@OperationParam(name = "coding", min = 0, max = 1) Coding theCoding,
		@OperationParam(name = "codeableConcept", min = 0, max = 1) CodeableConcept theCodeableConcept,
		RequestDetails theRequestDetails
	) {

		startRequest(theServletRequest);
		try {
			IFhirResourceDaoValueSet<ValueSet, Coding, CodeableConcept> dao = (IFhirResourceDaoValueSet<ValueSet, Coding, CodeableConcept>) getDao();
			IFhirResourceDaoValueSet.ValidateCodeResult result = dao.validateCode(theValueSetUrl, theId, theCode, theSystem, theDisplay, theCoding, theCodeableConcept, theRequestDetails);
			Parameters retVal = new Parameters();
			retVal.addParameter().setName("result").setValue(new BooleanType(result.isResult()));
			if (isNotBlank(result.getMessage())) {
				retVal.addParameter().setName("message").setValue(new StringType(result.getMessage()));
			}
			if (isNotBlank(result.getDisplay())) {
				retVal.addParameter().setName("display").setValue(new StringType(result.getDisplay()));
			}
			return retVal;
		} finally {
			endRequest(theServletRequest);
		}
	}


	private static boolean moreThanOneTrue(boolean... theBooleans) {
		boolean haveOne = false;
		for (boolean next : theBooleans) {
			if (next) {
				if (haveOne) {
					return true;
				} else {
					haveOne = true;
				}
			}
		}
		return false;
	}


}
