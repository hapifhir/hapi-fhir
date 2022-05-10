package ca.uhn.fhir.jpa.dao.r4;

/*
 * #%L
 * HAPI FHIR JPA Server
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
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.context.support.IValidationSupport.CodeValidationResult;
import ca.uhn.fhir.context.support.ValidationSupportContext;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoCodeSystem;
import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.jpa.dao.BaseHapiFhirResourceDao;
import ca.uhn.fhir.jpa.model.cross.IBasePersistedResource;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.term.api.ITermCodeSystemStorageSvc;
import ca.uhn.fhir.jpa.term.api.ITermDeferredStorageSvc;
import ca.uhn.fhir.jpa.util.LogicUtil;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import ca.uhn.fhir.rest.api.server.storage.TransactionDetails;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nonnull;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static ca.uhn.fhir.jpa.dao.FhirResourceDaoValueSetDstu2.toStringOrNull;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.hl7.fhir.common.hapi.validation.support.ValidationConstants.LOINC_LOW;

public class FhirResourceDaoCodeSystemR4 extends BaseHapiFhirResourceDao<CodeSystem> implements IFhirResourceDaoCodeSystem<CodeSystem, Coding, CodeableConcept> {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirResourceDaoCodeSystemR4.class);
	@Autowired
	protected ITermCodeSystemStorageSvc myTerminologyCodeSystemStorageSvc;
	@Autowired
	protected ITermDeferredStorageSvc myTermDeferredStorageSvc;
	@Autowired
	private IValidationSupport myValidationSupport;
	@Autowired
	private FhirContext myFhirContext;

	@Override
	public List<IIdType> findCodeSystemIdsContainingSystemAndCode(String theCode, String theSystem, RequestDetails theRequest) {
		List<IIdType> valueSetIds;
		List<ResourcePersistentId> ids = searchForIds(new SearchParameterMap(CodeSystem.SP_CODE, new TokenParam(theSystem, theCode)), theRequest);
		valueSetIds = new ArrayList<>();
		for (ResourcePersistentId next : ids) {
			IIdType id = myIdHelperService.translatePidIdToForcedId(myFhirContext, "CodeSystem", next);
			valueSetIds.add(id);
		}
		return valueSetIds;
	}

	@Nonnull
	@Override
	public IValidationSupport.LookupCodeResult lookupCode(IPrimitiveType<String> theCode, IPrimitiveType<String> theSystem, Coding theCoding, RequestDetails theRequestDetails) {
		return lookupCode(theCode, theSystem, theCoding, null, theRequestDetails);
	}

	@Nonnull
	@Override
	public IValidationSupport.LookupCodeResult lookupCode(IPrimitiveType<String> theCode, IPrimitiveType<String> theSystem, Coding theCoding, IPrimitiveType<String> theDisplayLanguage, RequestDetails theRequestDetails) {
		boolean haveCoding = theCoding != null && isNotBlank(theCoding.getSystem()) && isNotBlank(theCoding.getCode());
		boolean haveCode = theCode != null && theCode.isEmpty() == false;
		boolean haveSystem = theSystem != null && theSystem.isEmpty() == false;
		boolean haveDisplayLanguage = theDisplayLanguage != null && theDisplayLanguage.isEmpty() == false;

		if (!haveCoding && !(haveSystem && haveCode)) {
			throw new InvalidRequestException(Msg.code(1108) + "No code, coding, or codeableConcept provided to validate");
		}
		if (!LogicUtil.multiXor(haveCoding, (haveSystem && haveCode)) || (haveSystem != haveCode)) {
			throw new InvalidRequestException(Msg.code(1109) + "$lookup can only validate (system AND code) OR (coding.system AND coding.code)");
		}

		String code;
		String system;
		if (haveCoding) {
			code = theCoding.getCode();
			if (theCoding.hasVersion()) {
				system = theCoding.getSystem() + "|" + theCoding.getVersion();
			} else {
				system = theCoding.getSystem();
			}
		} else {
			code = theCode.getValue();
			system = theSystem.getValue();
		}

		String displayLanguage = null;
		if (haveDisplayLanguage) {
			displayLanguage = theDisplayLanguage.getValue();
		}

		ourLog.debug("Looking up {} / {}", system, code);

		if (myValidationSupport.isCodeSystemSupported(new ValidationSupportContext(myValidationSupport), system)) {

			ourLog.debug("Code system {} is supported", system);
			IValidationSupport.LookupCodeResult retVal = myValidationSupport.lookupCode(new ValidationSupportContext(myValidationSupport), system, code, displayLanguage);
			if (retVal != null) {
				return retVal;
			}

		}

		// We didn't find it..
		return IValidationSupport.LookupCodeResult.notFound(system, code);

	}

	@Override
	public SubsumesResult subsumes(IPrimitiveType<String> theCodeA, IPrimitiveType<String> theCodeB, IPrimitiveType<String> theSystem, Coding theCodingA, Coding theCodingB, RequestDetails theRequestDetails) {
		return myTerminologySvc.subsumes(theCodeA, theCodeB, theSystem, theCodingA, theCodingB);
	}

	@Override
	protected void preDelete(CodeSystem theResourceToDelete, ResourceTable theEntityToDelete) {
		super.preDelete(theResourceToDelete, theEntityToDelete);

		myTermDeferredStorageSvc.deleteCodeSystemForResource(theEntityToDelete);

	}

	@Override
	public ResourceTable updateEntity(RequestDetails theRequest, IBaseResource theResource, IBasePersistedResource theEntity, Date theDeletedTimestampOrNull, boolean thePerformIndexing,
												 boolean theUpdateVersion, TransactionDetails theTransactionDetails, boolean theForceUpdate, boolean theCreateNewHistoryEntry) {
		ResourceTable retVal = super.updateEntity(theRequest, theResource, theEntity, theDeletedTimestampOrNull, thePerformIndexing, theUpdateVersion, theTransactionDetails, theForceUpdate, theCreateNewHistoryEntry);

		if (!retVal.isUnchangedInCurrentOperation()) {
			CodeSystem cs = (CodeSystem) theResource;
			addPidToResource(theEntity, theResource);

			myTerminologyCodeSystemStorageSvc.storeNewCodeSystemVersionIfNeeded(cs, (ResourceTable) theEntity, theRequest);
		}

		return retVal;
	}

	@Override
	public CodeValidationResult validateCode(IIdType theCodeSystemId, IPrimitiveType<String> theCodeSystemUrl,
														  IPrimitiveType<String> theVersion, IPrimitiveType<String> theCode, IPrimitiveType<String> theDisplay,
														  Coding theCoding, CodeableConcept theCodeableConcept, RequestDetails theRequestDetails) {

		return myTerminologySvc.codeSystemValidateCode(theCodeSystemId, toStringOrNull(theCodeSystemUrl), toStringOrNull(theVersion), toStringOrNull(theCode), toStringOrNull(theDisplay), theCoding, theCodeableConcept);

	}


	@Override
	public DaoMethodOutcome create(CodeSystem theResource, String theIfNoneExist, boolean thePerformIndexing,
											 @Nonnull TransactionDetails theTransactionDetails, RequestDetails theRequestDetails) {
		// loinc CodeSystem must have an ID
		if (isNotBlank(theResource.getUrl()) && theResource.getUrl().contains(LOINC_LOW)
			&& isBlank(theResource.getIdElement().getIdPart())) {
			throw new InvalidParameterException(Msg.code(1110) + "'loinc' CodeSystem must have an ID");
		}
		return myTransactionService.execute(theRequestDetails, theTransactionDetails,
			tx -> doCreateForPost(theResource, theIfNoneExist, thePerformIndexing, theTransactionDetails, theRequestDetails));
	}

}
