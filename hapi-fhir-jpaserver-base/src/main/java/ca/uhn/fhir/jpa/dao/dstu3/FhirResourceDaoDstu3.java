package ca.uhn.fhir.jpa.dao.dstu3;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2016 University Health Network
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

import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.hl7.fhir.dstu3.exceptions.FHIRException;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.OperationOutcome;
import org.hl7.fhir.dstu3.model.OperationOutcome.IssueSeverity;
import org.hl7.fhir.dstu3.model.OperationOutcome.OperationOutcomeIssueComponent;
import org.hl7.fhir.instance.model.OperationOutcome.IssueType;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.jpa.dao.BaseHapiFhirResourceDao;
import ca.uhn.fhir.jpa.entity.ResourceTable;
import ca.uhn.fhir.jpa.util.DeleteConflict;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.model.base.composite.BaseResourceReferenceDt;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.ValidationModeEnum;
import ca.uhn.fhir.rest.method.RequestDetails;
import ca.uhn.fhir.rest.server.EncodingEnum;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException;
import ca.uhn.fhir.rest.server.interceptor.IServerInterceptor.ActionRequestDetails;
import ca.uhn.fhir.util.CoverageIgnore;
import ca.uhn.fhir.util.FhirTerser;
import ca.uhn.fhir.validation.FhirValidator;
import ca.uhn.fhir.validation.IValidationContext;
import ca.uhn.fhir.validation.IValidatorModule;
import ca.uhn.fhir.validation.ValidationResult;

public class FhirResourceDaoDstu3<T extends IAnyResource> extends BaseHapiFhirResourceDao<T> {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirResourceDaoDstu3.class);

	@Autowired()
	@Qualifier("myInstanceValidatorDstu3")
	private IValidatorModule myInstanceValidator;

	@Override
	protected IBaseOperationOutcome createOperationOutcome(String theSeverity, String theMessage, String theCode) {
		OperationOutcome oo = new OperationOutcome();
		OperationOutcomeIssueComponent issue = oo.addIssue();
		issue.getSeverityElement().setValueAsString(theSeverity);
		issue.setDiagnostics(theMessage);
		try {
			issue.setCode(org.hl7.fhir.dstu3.model.OperationOutcome.IssueType.fromCode(theCode));
		} catch (FHIRException e) {
			ourLog.error("Unknown code: {}", theCode);
		}
		return oo;
	}

	@Override
	protected List<Object> getIncludeValues(FhirTerser theTerser, Include theInclude, IBaseResource theResource, RuntimeResourceDefinition theResourceDef) {
		List<Object> values;
		if ("*".equals(theInclude.getValue())) {
			values = new ArrayList<Object>();
			values.addAll(theTerser.getAllPopulatedChildElementsOfType(theResource, BaseResourceReferenceDt.class));
		} else if (theInclude.getValue().startsWith(theResourceDef.getName() + ":")) {
			values = new ArrayList<Object>();
			RuntimeSearchParam sp = theResourceDef.getSearchParam(theInclude.getValue().substring(theInclude.getValue().indexOf(':') + 1));
			for (String nextPath : sp.getPathsSplit()) {
				values.addAll(theTerser.getValues(theResource, nextPath));
			}
		} else {
			values = Collections.emptyList();
		}
		return values;
	}

	@Override
	public MethodOutcome validate(T theResource, IIdType theId, String theRawResource, EncodingEnum theEncoding, ValidationModeEnum theMode, String theProfile, RequestDetails theRequestDetails) {
		ActionRequestDetails requestDetails = new ActionRequestDetails(theId, null, theResource, getContext(), theRequestDetails);
		notifyInterceptors(RestOperationTypeEnum.VALIDATE, requestDetails);

		if (theMode == ValidationModeEnum.DELETE) {
			if (theId == null || theId.hasIdPart() == false) {
				throw new InvalidRequestException("No ID supplied. ID is required when validating with mode=DELETE");
			}
			final ResourceTable entity = readEntityLatestVersion(theId);

			// Validate that there are no resources pointing to the candidate that
			// would prevent deletion
			List<DeleteConflict> deleteConflicts = new ArrayList<DeleteConflict>();
			validateOkToDelete(deleteConflicts, entity);
			validateDeleteConflictsEmptyOrThrowException(deleteConflicts);

			OperationOutcome oo = new OperationOutcome();
			oo.addIssue().setSeverity(IssueSeverity.INFORMATION).setDiagnostics("Ok to delete");
			return new MethodOutcome(new IdType(theId.getValue()), oo);
		}

		FhirValidator validator = getContext().newValidator();

		validator.registerValidatorModule(myInstanceValidator);

		validator.registerValidatorModule(new IdChecker(theMode));

		ValidationResult result;
		if (isNotBlank(theRawResource)) {
			result = validator.validateWithResult(theRawResource);
		} else {
			result = validator.validateWithResult(theResource);
		}

		if (result.isSuccessful()) {
			MethodOutcome retVal = new MethodOutcome();
			retVal.setOperationOutcome(result.toOperationOutcome());
			return retVal;
		} else {
			throw new PreconditionFailedException("Validation failed", result.toOperationOutcome());
		}

	}

	private class IdChecker implements IValidatorModule {

		private ValidationModeEnum myMode;

		public IdChecker(ValidationModeEnum theMode) {
			myMode = theMode;
		}

		@CoverageIgnore
		@Override
		public void validateBundle(IValidationContext<Bundle> theContext) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void validateResource(IValidationContext<IBaseResource> theCtx) {
			boolean hasId = theCtx.getResource().getIdElement().hasIdPart();
			if (myMode == ValidationModeEnum.CREATE) {
				if (hasId) {
					throw new InvalidRequestException("Resource has an ID - ID must not be populated for a FHIR create");
				}
			} else if (myMode == ValidationModeEnum.UPDATE) {
				if (hasId == false) {
					throw new InvalidRequestException("Resource has no ID - ID must be populated for a FHIR update");
				}
			}

		}

	}

}
