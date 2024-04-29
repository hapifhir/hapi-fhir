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
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package ca.uhn.fhir.jpa.dao;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.context.support.ConceptValidationOptions;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.context.support.IValidationSupport.CodeValidationResult;
import ca.uhn.fhir.context.support.LookupCodeRequest;
import ca.uhn.fhir.context.support.ValidationSupportContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoCodeSystem;
import ca.uhn.fhir.jpa.api.svc.IIdHelperService;
import ca.uhn.fhir.jpa.model.cross.IBasePersistedResource;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.term.api.ITermCodeSystemStorageSvc;
import ca.uhn.fhir.jpa.term.api.ITermDeferredStorageSvc;
import ca.uhn.fhir.jpa.util.LogicUtil;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.storage.IResourcePersistentId;
import ca.uhn.fhir.rest.api.server.storage.TransactionDetails;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.util.FhirTerser;
import ca.uhn.hapi.converters.canonical.VersionCanonicalizer;
import jakarta.annotation.Nonnull;
import jakarta.annotation.PostConstruct;
import org.apache.commons.collections4.CollectionUtils;
import org.hl7.fhir.common.hapi.validation.support.CommonCodeSystemsTerminologyService;
import org.hl7.fhir.instance.model.api.IBaseCoding;
import org.hl7.fhir.instance.model.api.IBaseDatatype;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static ca.uhn.fhir.util.DatatypeUtil.toStringValue;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class JpaResourceDaoCodeSystem<T extends IBaseResource> extends BaseHapiFhirResourceDao<T>
		implements IFhirResourceDaoCodeSystem<T> {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(JpaResourceDaoCodeSystem.class);

	@Autowired
	protected ITermCodeSystemStorageSvc myTerminologyCodeSystemStorageSvc;

	@Autowired
	protected IIdHelperService myIdHelperService;

	@Autowired
	protected ITermDeferredStorageSvc myTermDeferredStorageSvc;

	@Autowired
	private IValidationSupport myValidationSupport;

	@Autowired
	private FhirContext myFhirContext;

	private FhirTerser myTerser;

	@Autowired
	private VersionCanonicalizer myVersionCanonicalizer;

	@Override
	@PostConstruct
	public void start() {
		super.start();
		myTerser = myFhirContext.newTerser();
	}

	@Override
	public List<IIdType> findCodeSystemIdsContainingSystemAndCode(
			String theCode, String theSystem, RequestDetails theRequest) {
		List<IIdType> valueSetIds;
		List<IResourcePersistentId> ids = searchForIds(
				new SearchParameterMap(org.hl7.fhir.r4.model.CodeSystem.SP_CODE, new TokenParam(theSystem, theCode)),
				theRequest);
		valueSetIds = new ArrayList<>();
		for (IResourcePersistentId next : ids) {
			IIdType id = myIdHelperService.translatePidIdToForcedId(myFhirContext, "CodeSystem", next);
			valueSetIds.add(id);
		}
		return valueSetIds;
	}

	@Nonnull
	@Override
	public IValidationSupport.LookupCodeResult lookupCode(
			IPrimitiveType<String> theCode,
			IPrimitiveType<String> theSystem,
			IBaseCoding theCoding,
			RequestDetails theRequestDetails) {
		return lookupCode(theCode, theSystem, theCoding, null, theRequestDetails);
	}

	@Nonnull
	@Override
	public IValidationSupport.LookupCodeResult lookupCode(
			IPrimitiveType<String> theCode,
			IPrimitiveType<String> theSystem,
			IBaseCoding theCoding,
			IPrimitiveType<String> theDisplayLanguage,
			RequestDetails theRequestDetails) {
		return lookupCode(
				theCode,
				theSystem,
				theCoding,
				theDisplayLanguage,
				CollectionUtils.emptyCollection(),
				theRequestDetails);
	}

	@Nonnull
	@Override
	public IValidationSupport.LookupCodeResult lookupCode(
			IPrimitiveType<String> theCode,
			IPrimitiveType<String> theSystem,
			IBaseCoding theCoding,
			IPrimitiveType<String> theDisplayLanguage,
			Collection<IPrimitiveType<String>> thePropertyNames,
			RequestDetails theRequestDetails) {
		return doLookupCode(
				myFhirContext,
				myTerser,
				myValidationSupport,
				theCode,
				theSystem,
				theCoding,
				theDisplayLanguage,
				thePropertyNames);
	}

	@Override
	public SubsumesResult subsumes(
			IPrimitiveType<String> theCodeA,
			IPrimitiveType<String> theCodeB,
			IPrimitiveType<String> theSystem,
			IBaseCoding theCodingA,
			IBaseCoding theCodingB,
			RequestDetails theRequestDetails) {
		return myTerminologySvc.subsumes(theCodeA, theCodeB, theSystem, theCodingA, theCodingB);
	}

	@Override
	protected void preDelete(T theResourceToDelete, ResourceTable theEntityToDelete, RequestDetails theRequestDetails) {
		super.preDelete(theResourceToDelete, theEntityToDelete, theRequestDetails);

		myTermDeferredStorageSvc.deleteCodeSystemForResource(theEntityToDelete);
	}

	@Override
	public ResourceTable updateEntity(
			RequestDetails theRequest,
			IBaseResource theResource,
			IBasePersistedResource theEntity,
			Date theDeletedTimestampOrNull,
			boolean thePerformIndexing,
			boolean theUpdateVersion,
			TransactionDetails theTransactionDetails,
			boolean theForceUpdate,
			boolean theCreateNewHistoryEntry) {
		ResourceTable retVal = super.updateEntity(
				theRequest,
				theResource,
				theEntity,
				theDeletedTimestampOrNull,
				thePerformIndexing,
				theUpdateVersion,
				theTransactionDetails,
				theForceUpdate,
				theCreateNewHistoryEntry);
		if (!retVal.isUnchangedInCurrentOperation()) {

			org.hl7.fhir.r4.model.CodeSystem cs = myVersionCanonicalizer.codeSystemToCanonical(theResource);
			addPidToResource(theEntity, cs);

			myTerminologyCodeSystemStorageSvc.storeNewCodeSystemVersionIfNeeded(
					cs, (ResourceTable) theEntity, theRequest);
		}

		return retVal;
	}

	@Nonnull
	@Override
	public CodeValidationResult validateCode(
			IIdType theCodeSystemId,
			IPrimitiveType<String> theCodeSystemUrl,
			IPrimitiveType<String> theVersion,
			IPrimitiveType<String> theCode,
			IPrimitiveType<String> theDisplay,
			IBaseCoding theCoding,
			IBaseDatatype theCodeableConcept,
			RequestDetails theRequestDetails) {

		CodeableConcept codeableConcept = myVersionCanonicalizer.codeableConceptToCanonical(theCodeableConcept);
		boolean haveCodeableConcept =
				codeableConcept != null && codeableConcept.getCoding().size() > 0;

		Coding coding = myVersionCanonicalizer.codingToCanonical(theCoding);
		boolean haveCoding = coding != null && !coding.isEmpty();

		String code = toStringValue(theCode);
		boolean haveCode = isNotBlank(code);

		if (!haveCodeableConcept && !haveCoding && !haveCode) {
			throw new InvalidRequestException(
					Msg.code(906) + "No code, coding, or codeableConcept provided to validate.");
		}
		if (!LogicUtil.multiXor(haveCodeableConcept, haveCoding, haveCode)) {
			throw new InvalidRequestException(
					Msg.code(907) + "$validate-code can only validate (code) OR (coding) OR (codeableConcept)");
		}

		String codeSystemUrl;
		if (theCodeSystemId != null) {
			IBaseResource codeSystem = read(theCodeSystemId, theRequestDetails);
			codeSystemUrl = CommonCodeSystemsTerminologyService.getCodeSystemUrl(myFhirContext, codeSystem);
		} else if (isNotBlank(toStringValue(theCodeSystemUrl))) {
			codeSystemUrl = toStringValue(theCodeSystemUrl);
		} else {
			throw new InvalidRequestException(Msg.code(908)
					+ "Either CodeSystem ID or CodeSystem identifier must be provided. Unable to validate.");
		}

		if (haveCodeableConcept) {
			CodeValidationResult anyValidation = null;
			for (int i = 0; i < codeableConcept.getCoding().size(); i++) {
				Coding nextCoding = codeableConcept.getCoding().get(i);
				if (nextCoding.hasSystem()) {
					if (!codeSystemUrl.equalsIgnoreCase(nextCoding.getSystem())) {
						throw new InvalidRequestException(Msg.code(909) + "Coding.system '" + nextCoding.getSystem()
								+ "' does not equal with CodeSystem.url '" + codeSystemUrl + "'. Unable to validate.");
					}
					codeSystemUrl = nextCoding.getSystem();
				}
				code = nextCoding.getCode();
				String display = nextCoding.getDisplay();
				CodeValidationResult nextValidation =
						codeSystemValidateCode(codeSystemUrl, toStringValue(theVersion), code, display);
				anyValidation = nextValidation;
				if (nextValidation.isOk()) {
					return nextValidation;
				}
			}
			return anyValidation;
		} else if (haveCoding) {
			if (coding.hasSystem()) {
				if (!codeSystemUrl.equalsIgnoreCase(coding.getSystem())) {
					throw new InvalidRequestException(Msg.code(910) + "Coding.system '" + coding.getSystem()
							+ "' does not equal with CodeSystem.url '" + codeSystemUrl + "'. Unable to validate.");
				}
				codeSystemUrl = coding.getSystem();
			}
			code = coding.getCode();
			String display = coding.getDisplay();
			return codeSystemValidateCode(codeSystemUrl, toStringValue(theVersion), code, display);
		} else {
			String display = toStringValue(theDisplay);
			return codeSystemValidateCode(codeSystemUrl, toStringValue(theVersion), code, display);
		}
	}

	private CodeValidationResult codeSystemValidateCode(
			String theCodeSystemUrl, String theVersion, String theCode, String theDisplay) {
		ValidationSupportContext context = new ValidationSupportContext(myValidationSupport);
		ConceptValidationOptions options = new ConceptValidationOptions();
		options.setValidateDisplay(isNotBlank(theDisplay));

		String codeSystemUrl = createVersionedSystemIfVersionIsPresent(theCodeSystemUrl, theVersion);

		CodeValidationResult retVal =
				myValidationSupport.validateCode(context, options, codeSystemUrl, theCode, theDisplay, null);
		if (retVal == null) {
			retVal = new CodeValidationResult();
			retVal.setMessage(
					"Terminology service was unable to provide validation for " + codeSystemUrl + "#" + theCode);
		}
		return retVal;
	}

	public static IValidationSupport.LookupCodeResult doLookupCode(
			FhirContext theFhirContext,
			FhirTerser theFhirTerser,
			IValidationSupport theValidationSupport,
			IPrimitiveType<String> theCode,
			IPrimitiveType<String> theSystem,
			IBaseCoding theCoding,
			IPrimitiveType<String> theDisplayLanguage,
			Collection<IPrimitiveType<String>> thePropertyNames) {
		boolean haveCoding = theCoding != null
				&& isNotBlank(extractCodingSystem(theCoding))
				&& isNotBlank(extractCodingCode(theCoding));
		boolean haveCode = theCode != null && theCode.isEmpty() == false;
		boolean haveSystem = theSystem != null && theSystem.isEmpty() == false;
		boolean haveDisplayLanguage = theDisplayLanguage != null && theDisplayLanguage.isEmpty() == false;

		if (!haveCoding && !(haveSystem && haveCode)) {
			throw new InvalidRequestException(
					Msg.code(1126) + "No code, coding, or codeableConcept provided to validate");
		}
		if (!LogicUtil.multiXor(haveCoding, (haveSystem && haveCode)) || (haveSystem != haveCode)) {
			throw new InvalidRequestException(
					Msg.code(1127) + "$lookup can only validate (system AND code) OR (coding.system AND coding.code)");
		}

		String code;
		String system;
		if (haveCoding) {
			code = extractCodingCode(theCoding);
			system = extractCodingSystem(theCoding);
			String version = extractCodingVersion(theFhirContext, theFhirTerser, theCoding);
			if (isNotBlank(version)) {
				system = system + "|" + version;
			}
		} else {
			code = theCode.getValue();
			system = theSystem.getValue();
		}

		String displayLanguage = null;
		if (haveDisplayLanguage) {
			displayLanguage = theDisplayLanguage.getValue();
		}

		ourLog.info("Looking up {} / {}", system, code);

		Collection<String> propertyNames = CollectionUtils.emptyIfNull(thePropertyNames).stream()
				.map(IPrimitiveType::getValueAsString)
				.collect(Collectors.toSet());

		if (theValidationSupport.isCodeSystemSupported(new ValidationSupportContext(theValidationSupport), system)) {

			ourLog.info("Code system {} is supported", system);
			IValidationSupport.LookupCodeResult retVal = theValidationSupport.lookupCode(
					new ValidationSupportContext(theValidationSupport),
					new LookupCodeRequest(system, code, displayLanguage, propertyNames));
			if (retVal != null) {
				return retVal;
			}
		}

		// We didn't find it..
		return IValidationSupport.LookupCodeResult.notFound(system, code);
	}

	private static String extractCodingSystem(IBaseCoding theCoding) {
		return theCoding.getSystem();
	}

	private static String extractCodingCode(IBaseCoding theCoding) {
		return theCoding.getCode();
	}

	private static String extractCodingVersion(
			FhirContext theFhirContext, FhirTerser theFhirTerser, IBaseCoding theCoding) {
		if (theFhirContext.getVersion().getVersion().isOlderThan(FhirVersionEnum.DSTU3)) {
			return null;
		}
		return theFhirTerser.getSinglePrimitiveValueOrNull(theCoding, "version");
	}

	public static String createVersionedSystemIfVersionIsPresent(String theCodeSystemUrl, String theVersion) {
		String codeSystemUrl = theCodeSystemUrl;
		if (isNotBlank(theVersion)) {
			codeSystemUrl = codeSystemUrl + "|" + theVersion;
		}
		return codeSystemUrl;
	}
}
