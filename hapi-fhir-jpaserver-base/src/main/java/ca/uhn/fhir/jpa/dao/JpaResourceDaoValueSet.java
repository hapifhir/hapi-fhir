/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package ca.uhn.fhir.jpa.dao;

import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.context.support.ConceptValidationOptions;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.context.support.ValidationSupportContext;
import ca.uhn.fhir.context.support.ValueSetExpansionOptions;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoValueSet;
import ca.uhn.fhir.jpa.model.cross.IBasePersistedResource;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.search.autocomplete.ValueSetAutocompleteOptions;
import ca.uhn.fhir.jpa.util.LogicUtil;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.storage.TransactionDetails;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException;
import ca.uhn.hapi.converters.canonical.VersionCanonicalizer;
import org.hl7.fhir.common.hapi.validation.support.CommonCodeSystemsTerminologyService;
import org.hl7.fhir.instance.model.api.IBaseCoding;
import org.hl7.fhir.instance.model.api.IBaseDatatype;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.ValueSet;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

import static ca.uhn.fhir.jpa.dao.JpaResourceDaoCodeSystem.createVersionedSystemIfVersionIsPresent;
import static ca.uhn.fhir.jpa.provider.ValueSetOperationProvider.createValueSetExpansionOptions;
import static ca.uhn.fhir.util.DatatypeUtil.toStringValue;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class JpaResourceDaoValueSet<T extends IBaseResource> extends BaseHapiFhirResourceDao<T>
		implements IFhirResourceDaoValueSet<T> {
	@Autowired
	private IValidationSupport myValidationSupport;

	@Autowired
	private VersionCanonicalizer myVersionCanonicalizer;

	@Autowired(required = false)
	private IFulltextSearchSvc myFulltextSearch;

	@Override
	public T expand(IIdType theId, ValueSetExpansionOptions theOptions, RequestDetails theRequestDetails) {
		T source = read(theId, theRequestDetails);
		return expand(source, theOptions);
	}

	@SuppressWarnings("unchecked")
	@Override
	public T expandByIdentifier(String theUri, ValueSetExpansionOptions theOptions) {
		IValidationSupport.ValueSetExpansionOutcome expansionOutcome = myValidationSupport.expandValueSet(
				new ValidationSupportContext(myValidationSupport), theOptions, theUri);
		return extractValueSetOrThrowException(expansionOutcome);
	}

	@Override
	public T expand(T theSource, ValueSetExpansionOptions theOptions) {
		IValidationSupport.ValueSetExpansionOutcome expansionOutcome = myValidationSupport.expandValueSet(
				new ValidationSupportContext(myValidationSupport), theOptions, theSource);
		return extractValueSetOrThrowException(expansionOutcome);
	}

	@Override
	public T expand(
			IIdType theId,
			T theValueSet,
			IPrimitiveType<String> theUrl,
			IPrimitiveType<String> theValueSetVersion,
			IPrimitiveType<String> theFilter,
			IPrimitiveType<String> theContext,
			IPrimitiveType<String> theContextDirection,
			IPrimitiveType<Integer> theOffset,
			IPrimitiveType<Integer> theCount,
			IPrimitiveType<String> theDisplayLanguage,
			IPrimitiveType<Boolean> theIncludeHierarchy,
			RequestDetails theRequestDetails) {
		boolean haveId = theId != null && theId.hasIdPart();
		boolean haveIdentifier = theUrl != null && isNotBlank(theUrl.getValue());
		boolean haveValueSet = theValueSet != null && !theValueSet.isEmpty();
		boolean haveValueSetVersion = theValueSetVersion != null && !theValueSetVersion.isEmpty();
		boolean haveContextDirection = theContextDirection != null && !theContextDirection.isEmpty();
		boolean haveContext = theContext != null && !theContext.isEmpty();

		boolean isAutocompleteExtension =
				haveContext && haveContextDirection && "existing".equals(theContextDirection.getValue());

		if (isAutocompleteExtension) {
			// this is a funky extension for NIH.  Do our own thing and return.
			ValueSetAutocompleteOptions options = ValueSetAutocompleteOptions.validateAndParseOptions(
					myStorageSettings, theContext, theFilter, theCount, theId, theUrl, theValueSet);
			if (myFulltextSearch == null || myFulltextSearch.isDisabled()) {
				throw new InvalidRequestException(
						Msg.code(2083)
								+ " Autocomplete is not supported on this server, as the fulltext search service is not configured.");
			} else {
				return (T) myFulltextSearch.tokenAutocompleteValueSetSearch(options);
			}
		}

		if (!haveId && !haveIdentifier && !haveValueSet) {
			if (myFhirContext.getVersion().getVersion() == FhirVersionEnum.DSTU2) {
				// "url" parameter is called "identifier" in DSTU2
				throw new InvalidRequestException(
						Msg.code(1130)
								+ "$expand operation at the type level (no ID specified) requires an identifier or a valueSet as a part of the request");
			}
			throw new InvalidRequestException(
					Msg.code(1133)
							+ "$expand operation at the type level (no ID specified) requires a url or a valueSet as a part of the request.");
		}

		if (!LogicUtil.multiXor(haveId, haveIdentifier, haveValueSet)) {
			if (myFhirContext.getVersion().getVersion() == FhirVersionEnum.DSTU2) {
				// "url" parameter is called "identifier" in DSTU2
				throw new InvalidRequestException(
						Msg.code(1131)
								+ "$expand must EITHER be invoked at the type level, or have an identifier specified, or have a ValueSet specified. Can not combine these options.");
			}
			throw new InvalidRequestException(
					Msg.code(1134)
							+ "$expand must EITHER be invoked at the instance level, or have a url specified, or have a ValueSet specified. Can not combine these options.");
		}

		ValueSetExpansionOptions options = createValueSetExpansionOptions(
				myStorageSettings, theOffset, theCount, theIncludeHierarchy, theFilter, theDisplayLanguage);

		IValidationSupport.ValueSetExpansionOutcome outcome;
		if (haveId) {
			IBaseResource valueSet = read(theId, theRequestDetails);
			outcome = myValidationSupport.expandValueSet(
					new ValidationSupportContext(myValidationSupport), options, valueSet);
		} else if (haveIdentifier) {
			String url;
			if (haveValueSetVersion) {
				url = theUrl.getValue() + "|" + theValueSetVersion.getValue();
			} else {
				url = theUrl.getValue();
			}
			outcome =
					myValidationSupport.expandValueSet(new ValidationSupportContext(myValidationSupport), options, url);
		} else {
			outcome = myValidationSupport.expandValueSet(
					new ValidationSupportContext(myValidationSupport), options, theValueSet);
		}

		return extractValueSetOrThrowException(outcome);
	}

	@SuppressWarnings("unchecked")
	private T extractValueSetOrThrowException(IValidationSupport.ValueSetExpansionOutcome outcome) {
		if (outcome == null) {
			throw new InternalErrorException(
					Msg.code(2028) + "No validation support module was able to expand the given valueset");
		}

		if (outcome.getError() != null) {
			throw new PreconditionFailedException(Msg.code(2029) + outcome.getError());
		}

		return (T) outcome.getValueSet();
	}

	@Override
	public IValidationSupport.CodeValidationResult validateCode(
			IPrimitiveType<String> theValueSetIdentifier,
			IIdType theValueSetId,
			IPrimitiveType<String> theCode,
			IPrimitiveType<String> theSystem,
			IPrimitiveType<String> theDisplay,
			IBaseCoding theCoding,
			IBaseDatatype theCodeableConcept,
			RequestDetails theRequestDetails) {

		CodeableConcept codeableConcept = myVersionCanonicalizer.codeableConceptToCanonical(theCodeableConcept);
		boolean haveCodeableConcept =
				codeableConcept != null && codeableConcept.getCoding().size() > 0;

		Coding canonicalCodingToValidate = myVersionCanonicalizer.codingToCanonical((IBaseCoding) theCoding);
		boolean haveCoding = canonicalCodingToValidate != null && !canonicalCodingToValidate.isEmpty();

		boolean haveCode = theCode != null && !theCode.isEmpty();

		if (!haveCodeableConcept && !haveCoding && !haveCode) {
			throw new InvalidRequestException(
					Msg.code(899) + "No code, coding, or codeableConcept provided to validate");
		}
		if (!LogicUtil.multiXor(haveCodeableConcept, haveCoding, haveCode)) {
			throw new InvalidRequestException(Msg.code(900)
					+ "$validate-code can only validate (system AND code) OR (coding) OR (codeableConcept)");
		}

		String valueSetIdentifier;
		if (theValueSetId != null) {
			IBaseResource valueSet = read(theValueSetId, theRequestDetails);
			StringBuilder valueSetIdentifierBuilder =
					new StringBuilder(CommonCodeSystemsTerminologyService.getValueSetUrl(myFhirContext, valueSet));
			String valueSetVersion = CommonCodeSystemsTerminologyService.getValueSetVersion(myFhirContext, valueSet);
			if (valueSetVersion != null) {
				valueSetIdentifierBuilder.append("|").append(valueSetVersion);
			}
			valueSetIdentifier = valueSetIdentifierBuilder.toString();
		} else if (isNotBlank(toStringValue(theValueSetIdentifier))) {
			valueSetIdentifier = toStringValue(theValueSetIdentifier);
		} else {
			throw new InvalidRequestException(
					Msg.code(901)
							+ "Either ValueSet ID or ValueSet identifier or system and code must be provided. Unable to validate.");
		}

		if (haveCodeableConcept) {
			IValidationSupport.CodeValidationResult anyValidation = null;
			for (int i = 0; i < codeableConcept.getCoding().size(); i++) {
				Coding nextCoding = codeableConcept.getCoding().get(i);
				String system =
						createVersionedSystemIfVersionIsPresent(nextCoding.getSystem(), nextCoding.getVersion());
				String code = nextCoding.getCode();
				String display = nextCoding.getDisplay();

				IValidationSupport.CodeValidationResult nextValidation =
						validateCode(system, code, display, valueSetIdentifier);
				anyValidation = nextValidation;
				if (nextValidation.isOk()) {
					return nextValidation;
				}
			}
			return anyValidation;
		} else if (haveCoding) {
			String system = createVersionedSystemIfVersionIsPresent(
					canonicalCodingToValidate.getSystem(), canonicalCodingToValidate.getVersion());
			String code = canonicalCodingToValidate.getCode();
			String display = canonicalCodingToValidate.getDisplay();
			return validateCode(system, code, display, valueSetIdentifier);
		} else {
			String system = toStringValue(theSystem);
			String code = toStringValue(theCode);
			String display = toStringValue(theDisplay);
			return validateCode(system, code, display, valueSetIdentifier);
		}
	}

	private IValidationSupport.CodeValidationResult validateCode(
			String theSystem, String theCode, String theDisplay, String theValueSetIdentifier) {
		ValidationSupportContext context = new ValidationSupportContext(myValidationSupport);
		ConceptValidationOptions options = new ConceptValidationOptions();
		options.setValidateDisplay(isNotBlank(theDisplay));
		IValidationSupport.CodeValidationResult result = myValidationSupport.validateCode(
				context, options, theSystem, theCode, theDisplay, theValueSetIdentifier);

		if (result == null) {
			result = new IValidationSupport.CodeValidationResult();
			result.setMessage("Validator is unable to provide validation for " + theCode + "#" + theSystem
					+ " - Unknown or unusable ValueSet[" + theValueSetIdentifier + "]");
		}

		return result;
	}

	@Override
	public ResourceTable updateEntity(
			RequestDetails theRequestDetails,
			IBaseResource theResource,
			IBasePersistedResource theEntity,
			Date theDeletedTimestampOrNull,
			boolean thePerformIndexing,
			boolean theUpdateVersion,
			TransactionDetails theTransactionDetails,
			boolean theForceUpdate,
			boolean theCreateNewHistoryEntry) {
		ResourceTable retVal = super.updateEntity(
				theRequestDetails,
				theResource,
				theEntity,
				theDeletedTimestampOrNull,
				thePerformIndexing,
				theUpdateVersion,
				theTransactionDetails,
				theForceUpdate,
				theCreateNewHistoryEntry);

		if (getStorageSettings().isPreExpandValueSets() && !retVal.isUnchangedInCurrentOperation()) {
			if (retVal.getDeleted() == null) {
				ValueSet valueSet = myVersionCanonicalizer.valueSetToCanonical(theResource);
				myTerminologySvc.storeTermValueSet(retVal, valueSet);
			} else {
				myTerminologySvc.deleteValueSetAndChildren(retVal);
			}
		}

		return retVal;
	}
}
