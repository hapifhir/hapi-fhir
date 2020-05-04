package ca.uhn.fhir.jpa.dao.dstu3;

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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoCodeSystem;
import ca.uhn.fhir.jpa.dao.BaseHapiFhirResourceDao;
import ca.uhn.fhir.jpa.dao.data.ITermCodeSystemDao;
import ca.uhn.fhir.jpa.entity.TermCodeSystem;
import ca.uhn.fhir.jpa.model.cross.IBasePersistedResource;
import ca.uhn.fhir.jpa.model.cross.ResourcePersistentId;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.term.api.ITermCodeSystemStorageSvc;
import ca.uhn.fhir.jpa.util.LogicUtil;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import org.hl7.fhir.dstu3.model.CodeSystem;
import org.hl7.fhir.dstu3.model.CodeableConcept;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nonnull;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.hl7.fhir.convertors.conv30_40.CodeSystem30_40.convertCodeSystem;

@Transactional
public class FhirResourceDaoCodeSystemDstu3 extends BaseHapiFhirResourceDao<CodeSystem> implements IFhirResourceDaoCodeSystem<CodeSystem, Coding, CodeableConcept> {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirResourceDaoCodeSystemDstu3.class);
	@Autowired
	protected ITermCodeSystemStorageSvc myTerminologyCodeSystemStorageSvc;
	@Autowired
	private ITermCodeSystemDao myCsDao;
	@Autowired
	private IValidationSupport myValidationSupport;
	@Autowired
	private FhirContext myFhirContext;

	public FhirResourceDaoCodeSystemDstu3() {
		super();
	}

	@Override
	public List<IIdType> findCodeSystemIdsContainingSystemAndCode(String theCode, String theSystem, RequestDetails theRequest) {
		Set<ResourcePersistentId> ids = searchForIds(new SearchParameterMap(CodeSystem.SP_CODE, new TokenParam(theSystem, theCode)), theRequest);
		List<IIdType> valueSetIds = new ArrayList<>();
		for (ResourcePersistentId next : ids) {
			IIdType id = myIdHelperService.translatePidIdToForcedId(myFhirContext, "CodeSystem", next);
			valueSetIds.add(id);
		}
		return valueSetIds;
	}

	@Nonnull
	@Override
	public IValidationSupport.LookupCodeResult lookupCode(IPrimitiveType<String> theCode, IPrimitiveType<String> theSystem, Coding theCoding, RequestDetails theRequestDetails) {
		boolean haveCoding = theCoding != null && isNotBlank(theCoding.getSystem()) && isNotBlank(theCoding.getCode());
		boolean haveCode = theCode != null && theCode.isEmpty() == false;
		boolean haveSystem = theSystem != null && theSystem.isEmpty() == false;

		if (!haveCoding && !(haveSystem && haveCode)) {
			throw new InvalidRequestException("No code, coding, or codeableConcept provided to validate");
		}
		if (!LogicUtil.multiXor(haveCoding, (haveSystem && haveCode)) || (haveSystem != haveCode)) {
			throw new InvalidRequestException("$lookup can only validate (system AND code) OR (coding.system AND coding.code)");
		}

		String code;
		String system;
		if (haveCoding) {
			code = theCoding.getCode();
			system = theCoding.getSystem();
		} else {
			code = theCode.getValue();
			system = theSystem.getValue();
		}

		ourLog.debug("Looking up {} / {}", system, code);

		if (myValidationSupport.isCodeSystemSupported(myValidationSupport, system)) {
			ourLog.debug("Code system {} is supported", system);
			IValidationSupport.LookupCodeResult result = myValidationSupport.lookupCode(myValidationSupport, system, code);
			if (result != null) {
				return result;
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

		String codeSystemUrl = theResourceToDelete.getUrl();
		if (isNotBlank(codeSystemUrl)) {
			TermCodeSystem persCs = myCsDao.findByCodeSystemUri(codeSystemUrl);
			if (persCs != null) {
				myTerminologyCodeSystemStorageSvc.deleteCodeSystem(persCs);
			}
		}
	}

	@Override
	public ResourceTable updateEntity(RequestDetails theRequest, IBaseResource theResource, IBasePersistedResource theEntity, Date theDeletedTimestampOrNull, boolean thePerformIndexing,
												 boolean theUpdateVersion, Date theUpdateTime, boolean theForceUpdate, boolean theCreateNewHistoryEntry) {
		ResourceTable retVal = super.updateEntity(theRequest, theResource, theEntity, theDeletedTimestampOrNull, thePerformIndexing, theUpdateVersion, theUpdateTime, theForceUpdate, theCreateNewHistoryEntry);
		if (!retVal.isUnchangedInCurrentOperation()) {

			CodeSystem csDstu3 = (CodeSystem) theResource;

			org.hl7.fhir.r4.model.CodeSystem cs = convertCodeSystem(csDstu3);
			addPidToResource(theEntity, cs);

			myTerminologyCodeSystemStorageSvc.storeNewCodeSystemVersionIfNeeded(cs, (ResourceTable) theEntity);

		}

		return retVal;
	}

}
