/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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
import ca.uhn.fhir.context.support.ValidateCodeRequest;
import ca.uhn.fhir.context.support.ValidationSupportContext;
import ca.uhn.fhir.context.support.ValueSetExpansionOptions;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoValueSet;
import ca.uhn.fhir.jpa.model.cross.IBasePersistedResource;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.search.autocomplete.ValueSetAutocompleteOptions;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.storage.TransactionDetails;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException;
import ca.uhn.fhir.util.LogicUtil;
import ca.uhn.fhir.util.UrlUtil;
import ca.uhn.hapi.converters.canonical.VersionCanonicalizer;
import org.apache.commons.lang3.Strings;
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
import java.util.LinkedHashSet;
import java.util.Set;

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
			String url = UrlUtil.toCanonicalUrl(theUrl.getValue(), toStringValue(theValueSetVersion));
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
				codeableConcept != null && !codeableConcept.getCoding().isEmpty();

		Coding canonicalCodingToValidate = myVersionCanonicalizer.codingToCanonical(theCoding);
		boolean haveCoding = canonicalCodingToValidate != null && !canonicalCodingToValidate.isEmpty();

		boolean haveCode = theCode != null && !theCode.isEmpty();

		requireOneCodeToValidate(haveCode, haveCoding, haveCodeableConcept);

		String valueSetIdentifier = valueSetToValidate(theValueSetIdentifier, theValueSetId, theRequestDetails);

		// ValueSetOperationProvider packs the systemVersion operation parameter into the system it passes
		UrlUtil.CanonicalUrlParts system = UrlUtil.parseCanonicalUrl(toStringValue(theSystem));
		if (haveCodeableConcept) {
			return validateCodeableConcept(codeableConcept, system, valueSetIdentifier);
		} else if (haveCoding) {
			return validateCoding(canonicalCodingToValidate, system, valueSetIdentifier);
		} else {
			String code = toStringValue(theCode);
			String display = toStringValue(theDisplay);
			return validateCode(system.url(), system.versionId().orElse(null), code, display, valueSetIdentifier);
		}
	}

	/**
	 * @throws InvalidRequestException unless exactly one of a code, a coding and a codeable concept is given
	 */
	// Created by Claude Opus 5.5
	private static void requireOneCodeToValidate(
			boolean theHaveCode, boolean theHaveCoding, boolean theHaveCodeableConcept) {
		if (!theHaveCodeableConcept && !theHaveCoding && !theHaveCode) {
			throw new InvalidRequestException(
					Msg.code(899) + "No code, coding, or codeableConcept provided to validate");
		}
		if (!LogicUtil.multiXor(theHaveCodeableConcept, theHaveCoding, theHaveCode)) {
			throw new InvalidRequestException(Msg.code(900)
					+ "$validate-code can only validate (system AND code) OR (coding) OR (codeableConcept)");
		}
	}

	/**
	 * @return the canonical url of the value set to validate against, with its version if it has one
	 * @throws InvalidRequestException if neither {@literal theValueSetId} nor {@literal theValueSetIdentifier} is given
	 */
	// Created by Claude Opus 5.5
	private String valueSetToValidate(
			IPrimitiveType<String> theValueSetIdentifier, IIdType theValueSetId, RequestDetails theRequestDetails) {
		if (theValueSetId != null) {
			IBaseResource valueSet = read(theValueSetId, theRequestDetails);
			return UrlUtil.getCanonicalUrl(myFhirContext, valueSet).toCanonicalUrl();
		} else if (isNotBlank(toStringValue(theValueSetIdentifier))) {
			return toStringValue(theValueSetIdentifier);
		}
		throw new InvalidRequestException(Msg.code(901)
				+ "Either ValueSet ID or ValueSet identifier or system and code must be provided. Unable to validate.");
	}

	/**
	 * Validates each coding as {@link #validateCoding} does, and returns the first valid result. If none is valid, it
	 * returns the last result, or the last one caused by a code system the server does not have, naming the unknown
	 * code systems of every coding.
	 */
	// Created by Claude Opus 5.5
	private IValidationSupport.CodeValidationResult validateCodeableConcept(
			CodeableConcept theCodeableConcept, UrlUtil.CanonicalUrlParts theSystem, String theValueSetIdentifier) {
		IValidationSupport.CodeValidationResult anyValidation = null;
		Set<String> unknownSystems = new LinkedHashSet<>();
		for (Coding nextCoding : theCodeableConcept.getCoding()) {
			IValidationSupport.CodeValidationResult nextValidation =
					validateCoding(nextCoding, theSystem, theValueSetIdentifier);
			if (nextValidation.isOk()) {
				return nextValidation;
			}
			unknownSystems.addAll(nextValidation.getUnknownSystems());
			// A failure caused by a code system the server does not have is kept over a plain non-member, so
			// the response still names that code system
			boolean keepEarlierUnknownSystemFailure = anyValidation != null
					&& !anyValidation.getUnknownSystems().isEmpty()
					&& nextValidation.getUnknownSystems().isEmpty();
			if (!keepEarlierUnknownSystemFailure) {
				anyValidation = nextValidation;
			}
		}
		if (anyValidation != null && !anyValidation.getUnknownSystems().containsAll(unknownSystems)) {
			// The validation support chain caches its results, so the unknown systems of every coding go on a copy
			IValidationSupport.CodeValidationResult combined = anyValidation.copy();
			// unknownSystems already holds this result's own systems, so replacing them keeps the codings' order
			combined.getUnknownSystems().clear();
			unknownSystems.forEach(combined::addUnknownSystem);
			return combined;
		}
		return anyValidation;
	}

	/**
	 * Validates a coding against the version it names, or, for a coding of the code system in {@literal theSystem}
	 * which names none, against the version of {@literal theSystem}.
	 *
	 * @throws InvalidRequestException if the coding names a version other than the one in {@literal theSystem}
	 */
	// Created by Claude Opus 5.5
	private IValidationSupport.CodeValidationResult validateCoding(
			Coding theCoding, UrlUtil.CanonicalUrlParts theSystem, String theValueSetIdentifier) {
		String system = theCoding.getSystem();
		String version = theCoding.getVersion();
		if (isNotBlank(system)) {
			UrlUtil.CanonicalUrlParts codingSystem = UrlUtil.parseCanonicalUrl(system, version);
			if (Strings.CI.equals(codingSystem.url(), theSystem.url())) {
				codingSystem = UrlUtil.parseCanonicalUrl(
						codingSystem.toCanonicalUrl(), theSystem.versionId().orElse(null));
			}
			system = codingSystem.url();
			version = codingSystem.versionId().orElse(null);
		}
		return validateCode(system, version, theCoding.getCode(), theCoding.getDisplay(), theValueSetIdentifier);
	}

	private IValidationSupport.CodeValidationResult validateCode(
			String theSystem,
			String theSystemVersion,
			String theCode,
			String theDisplay,
			String theValueSetIdentifier) {
		ValidationSupportContext context = new ValidationSupportContext(myValidationSupport);
		ConceptValidationOptions options = new ConceptValidationOptions();
		options.setValidateDisplay(isNotBlank(theDisplay));
		IValidationSupport.CodeValidationResult result = myValidationSupport.validateCode(
				context,
				options,
				new ValidateCodeRequest(theSystem, theSystemVersion, theCode, theDisplay, theValueSetIdentifier));

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

		if (thePerformIndexing
				&& getStorageSettings().isPreExpandValueSets()
				&& !retVal.isUnchangedInCurrentOperation()) {
			if (retVal.getDeleted() == null) {
				ValueSet valueSet = myVersionCanonicalizer.valueSetToCanonical(theResource);
				myTermValueSetStorageSvc.storeTermValueSet(theRequestDetails, retVal, valueSet);
			} else {
				myTermValueSetStorageSvc.deleteValueSetAndChildren(retVal);
			}
		}

		return retVal;
	}
}
