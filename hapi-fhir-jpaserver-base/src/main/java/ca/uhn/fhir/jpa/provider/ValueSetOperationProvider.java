package ca.uhn.fhir.jpa.provider;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2021 Smile CDR, Inc.
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

import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.context.support.ValueSetExpansionOptions;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoValueSet;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.jpa.term.api.ITermReadSvc;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import ca.uhn.fhir.util.ParametersUtil;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.ICompositeType;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class ValueSetOperationProvider extends BaseJpaProvider {

	private static final Logger ourLog = LoggerFactory.getLogger(ValueSetOperationProvider.class);
	@Autowired
	private DaoConfig myDaoConfig;
	@Autowired
	private DaoRegistry myDaoRegistry;
	@Autowired
	private ITermReadSvc myTermReadSvc;

	@Operation(name = JpaConstants.OPERATION_EXPAND, idempotent = true, typeName = "ValueSet")
	public IBaseResource expand(
		HttpServletRequest theServletRequest,
		@IdParam(optional = true) IIdType theId,
		@OperationParam(name = "valueSet", min = 0, max = 1) IBaseResource theValueSet,
		@OperationParam(name = "url", min = 0, max = 1, typeName = "uri") IPrimitiveType<String> theUrl,
		@OperationParam(name = "valueSetVersion", min = 0, max = 1, typeName = "string") IPrimitiveType<String> theValueSetVersion,
		@OperationParam(name = "filter", min = 0, max = 1, typeName = "string") IPrimitiveType<String> theFilter,
		@OperationParam(name = "offset", min = 0, max = 1, typeName = "integer") IPrimitiveType<Integer> theOffset,
		@OperationParam(name = "count", min = 0, max = 1, typeName = "integer") IPrimitiveType<Integer> theCount,
		@OperationParam(name = JpaConstants.OPERATION_EXPAND_PARAM_INCLUDE_HIERARCHY, min = 0, max = 1, typeName = "boolean") IPrimitiveType<Boolean> theIncludeHierarchy,
		RequestDetails theRequestDetails) {

		boolean haveId = theId != null && theId.hasIdPart();
		boolean haveIdentifier = theUrl != null && isNotBlank(theUrl.getValue());
		boolean haveValueSet = theValueSet != null && !theValueSet.isEmpty();
		boolean haveValueSetVersion = theValueSetVersion != null && !theValueSetVersion.isEmpty();

		if (!haveId && !haveIdentifier && !haveValueSet) {
			throw new InvalidRequestException("$expand operation at the type level (no ID specified) requires a url or a valueSet as a part of the request.");
		}

		if (moreThanOneTrue(haveId, haveIdentifier, haveValueSet)) {
			throw new InvalidRequestException("$expand must EITHER be invoked at the instance level, or have a url specified, or have a ValueSet specified. Can not combine these options.");
		}

		ValueSetExpansionOptions options = createValueSetExpansionOptions(myDaoConfig, theOffset, theCount, theIncludeHierarchy, theFilter);

		startRequest(theServletRequest);
		try {

			IFhirResourceDaoValueSet<IBaseResource, ICompositeType, ICompositeType> dao = getDao();
			if (haveId) {
				return dao.expand(theId, options, theRequestDetails);
			} else if (haveIdentifier) {
				if (haveValueSetVersion) {
					return dao.expandByIdentifier(theUrl.getValue() + "|" + theValueSetVersion.getValue(), options);
				} else {
					return dao.expandByIdentifier(theUrl.getValue(), options);
				}
			} else {
				return dao.expand(theValueSet, options);
			}
		} finally {
			endRequest(theServletRequest);
		}
	}

	@SuppressWarnings("unchecked")
	private IFhirResourceDaoValueSet<IBaseResource, ICompositeType, ICompositeType> getDao() {
		return (IFhirResourceDaoValueSet<IBaseResource, ICompositeType, ICompositeType>) myDaoRegistry.getResourceDao("ValueSet");
	}

	@SuppressWarnings("unchecked")
	@Operation(name = JpaConstants.OPERATION_VALIDATE_CODE, idempotent = true, typeName = "ValueSet", returnParameters = {
		@OperationParam(name = "result", typeName = "boolean", min = 1),
		@OperationParam(name = "message", typeName = "string"),
		@OperationParam(name = "display", typeName = "string")
	})
	public IBaseParameters validateCode(
		HttpServletRequest theServletRequest,
		@IdParam(optional = true) IIdType theId,
		@OperationParam(name = "url", min = 0, max = 1, typeName = "uri") IPrimitiveType<String> theValueSetUrl,
		@OperationParam(name = "valueSetVersion", min = 0, max = 1, typeName = "string") IPrimitiveType<String> theValueSetVersion,
		@OperationParam(name = "code", min = 0, max = 1) IPrimitiveType<String> theCode,
		@OperationParam(name = "system", min = 0, max = 1, typeName = "uri") IPrimitiveType<String> theSystem,
		@OperationParam(name = "systemVersion", min = 0, max = 1, typeName = "string") IPrimitiveType<String> theSystemVersion,
		@OperationParam(name = "display", min = 0, max = 1, typeName = "string") IPrimitiveType<String> theDisplay,
		@OperationParam(name = "coding", min = 0, max = 1, typeName = "Coding") ICompositeType theCoding,
		@OperationParam(name = "codeableConcept", min = 0, max = 1, typeName = "CodeableConcept") ICompositeType theCodeableConcept,
		RequestDetails theRequestDetails
	) {

		startRequest(theServletRequest);
		try {
			IFhirResourceDaoValueSet<IBaseResource, ICompositeType, ICompositeType> dao = getDao();
			IPrimitiveType<String> valueSetIdentifier;
			if (theValueSetVersion != null) {
				valueSetIdentifier = (IPrimitiveType<String>) getContext().getElementDefinition("uri").newInstance();
				valueSetIdentifier.setValue(theValueSetUrl.getValue() + "|" + theValueSetVersion);
			} else {
				valueSetIdentifier = theValueSetUrl;
			}
			IPrimitiveType<String> codeSystemIdentifier;
			if (theSystemVersion != null) {
				codeSystemIdentifier = (IPrimitiveType<String>) getContext().getElementDefinition("uri").newInstance();
				codeSystemIdentifier.setValue(theSystem.getValue() + "|" + theSystemVersion);
			} else {
				codeSystemIdentifier = theSystem;
			}
			IValidationSupport.CodeValidationResult result = dao.validateCode(valueSetIdentifier, theId, theCode, codeSystemIdentifier, theDisplay, theCoding, theCodeableConcept, theRequestDetails);
			return BaseJpaResourceProviderValueSetDstu2.toValidateCodeResult(getContext(), result);
		} finally {
			endRequest(theServletRequest);
		}
	}

	@Operation(name = ProviderConstants.OPERATION_INVALIDATE_EXPANSION, idempotent = false, typeName = "ValueSet", returnParameters = {
		@OperationParam(name = "message", typeName = "string", min = 1, max = 1)
	})
	public IBaseParameters invalidateValueSetExpansion(
		@IdParam IIdType theValueSetId,
		RequestDetails theRequestDetails,
		HttpServletRequest theServletRequest) {
		startRequest(theServletRequest);
		try {

			String outcome = myTermReadSvc.invalidatePreCalculatedExpansion(theValueSetId, theRequestDetails);

			IBaseParameters retVal = ParametersUtil.newInstance(getContext());
			ParametersUtil.addParameterToParametersString(getContext(), retVal, "message", outcome);
			return retVal;

		} finally {
			endRequest(theServletRequest);
		}
	}


	public static ValueSetExpansionOptions createValueSetExpansionOptions(DaoConfig theDaoConfig, IPrimitiveType<Integer> theOffset, IPrimitiveType<Integer> theCount, IPrimitiveType<Boolean> theIncludeHierarchy, IPrimitiveType<String> theFilter) {
		int offset = theDaoConfig.getPreExpandValueSetsDefaultOffset();
		if (theOffset != null && theOffset.hasValue()) {
			if (theOffset.getValue() >= 0) {
				offset = theOffset.getValue();
			} else {
				throw new InvalidRequestException("offset parameter for $expand operation must be >= 0 when specified. offset: " + theOffset.getValue());
			}
		}

		int count = theDaoConfig.getPreExpandValueSetsDefaultCount();
		if (theCount != null && theCount.hasValue()) {
			if (theCount.getValue() >= 0) {
				count = theCount.getValue();
			} else {
				throw new InvalidRequestException("count parameter for $expand operation must be >= 0 when specified. count: " + theCount.getValue());
			}
		}
		int countMax = theDaoConfig.getPreExpandValueSetsMaxCount();
		if (count > countMax) {
			ourLog.warn("count parameter for $expand operation of {} exceeds maximum value of {}; using maximum value.", count, countMax);
			count = countMax;
		}

		ValueSetExpansionOptions options = ValueSetExpansionOptions.forOffsetAndCount(offset, count);

		if (theIncludeHierarchy != null && Boolean.TRUE.equals(theIncludeHierarchy.getValue())) {
			options.setIncludeHierarchy(true);
		}

		if (theFilter != null) {
			options.setFilter(theFilter.getValue());
		}

		return options;
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
