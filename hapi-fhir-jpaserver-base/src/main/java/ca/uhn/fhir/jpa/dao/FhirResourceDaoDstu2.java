package ca.uhn.fhir.jpa.dao;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.util.ArrayList;

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

import java.util.Collections;
import java.util.List;

import org.hl7.fhir.instance.hapi.validation.DefaultProfileValidationSupport;
import org.hl7.fhir.instance.hapi.validation.FhirInstanceValidator;
import org.hl7.fhir.instance.hapi.validation.IValidationSupport;
import org.hl7.fhir.instance.hapi.validation.ValidationSupportChain;
import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.validation.IResourceValidator.BestPracticeWarningLevel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.jpa.entity.ResourceTable;
import ca.uhn.fhir.jpa.util.DeleteConflict;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.model.base.composite.BaseResourceReferenceDt;
import ca.uhn.fhir.model.dstu2.resource.OperationOutcome;
import ca.uhn.fhir.model.dstu2.valueset.IssueSeverityEnum;
import ca.uhn.fhir.model.primitive.IdDt;
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

public class FhirResourceDaoDstu2<T extends IResource> extends BaseHapiFhirResourceDao<T> {

	@Autowired()
	@Qualifier("myJpaValidationSupportDstu2")
	private IValidationSupport myJpaValidationSupport;

	
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
	protected IBaseOperationOutcome createOperationOutcome(String theSeverity, String theMessage, String theCode) {
		OperationOutcome oo = new OperationOutcome();
		oo.getIssueFirstRep().getSeverityElement().setValue(theSeverity);
		oo.getIssueFirstRep().getDiagnosticsElement().setValue(theMessage);
		oo.getIssueFirstRep().getCodeElement().setValue(theCode);
		return oo;
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
			oo.addIssue().setSeverity(IssueSeverityEnum.INFORMATION).setDiagnostics("Ok to delete");
			return new MethodOutcome(new IdDt(theId.getValue()), oo);
		}

		FhirValidator validator = getContext().newValidator();

		FhirInstanceValidator val = new FhirInstanceValidator();
		val.setBestPracticeWarningLevel(BestPracticeWarningLevel.Warning);
		val.setValidationSupport(new ValidationSupportChain(new DefaultProfileValidationSupport(), myJpaValidationSupport));
		validator.registerValidatorModule(val);

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

		@CoverageIgnore
		@Override
		public void validateBundle(IValidationContext<Bundle> theContext) {
			throw new UnsupportedOperationException();
		}

	}

}
