package ca.uhn.fhir.jpa.dao.dstu3;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2017 University Health Network
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
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.hl7.fhir.dstu3.hapi.validation.IValidationSupport.CodeValidationResult;
import org.hl7.fhir.dstu3.hapi.validation.ValidationSupportChain;
import org.hl7.fhir.dstu3.model.CodeSystem;
import org.hl7.fhir.dstu3.model.CodeSystem.CodeSystemContentMode;
import org.hl7.fhir.dstu3.model.CodeSystem.ConceptDefinitionComponent;
import org.hl7.fhir.dstu3.model.CodeableConcept;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.springframework.beans.factory.annotation.Autowired;

import ca.uhn.fhir.jpa.dao.IFhirResourceDaoCodeSystem;
import ca.uhn.fhir.jpa.dao.SearchParameterMap;
import ca.uhn.fhir.jpa.dao.data.ITermCodeSystemVersionDao;
import ca.uhn.fhir.jpa.entity.ResourceTable;
import ca.uhn.fhir.jpa.entity.TermCodeSystemVersion;
import ca.uhn.fhir.jpa.entity.TermConcept;
import ca.uhn.fhir.jpa.entity.TermConceptParentChildLink.RelationshipTypeEnum;
import ca.uhn.fhir.jpa.term.IHapiTerminologySvc;
import ca.uhn.fhir.jpa.util.LogicUtil;
import ca.uhn.fhir.rest.method.RequestDetails;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;

public class FhirResourceDaoCodeSystemDstu3 extends FhirResourceDaoDstu3<CodeSystem> implements IFhirResourceDaoCodeSystem<CodeSystem, Coding, CodeableConcept> {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirResourceDaoCodeSystemDstu3.class);

	@Autowired
	private ITermCodeSystemVersionDao myCsvDao;

	@Autowired
	private IHapiTerminologySvc myTerminologySvc;

	@Autowired
	private ValidationSupportChain myValidationSupport;

//	private LookupCodeResult lookup(List<ValueSetExpansionContainsComponent> theContains, String theSystem, String theCode) {
//		for (ValueSetExpansionContainsComponent nextCode : theContains) {
//
//			String system = nextCode.getSystem();
//			String code = nextCode.getCode();
//			if (theSystem.equals(system) && theCode.equals(code)) {
//				LookupCodeResult retVal = new LookupCodeResult();
//				retVal.setSearchedForCode(code);
//				retVal.setSearchedForSystem(system);
//				retVal.setFound(true);
//				if (nextCode.getAbstractElement().getValue() != null) {
//					retVal.setCodeIsAbstract(nextCode.getAbstractElement().booleanValue());
//				}
//				retVal.setCodeDisplay(nextCode.getDisplay());
//				retVal.setCodeSystemVersion(nextCode.getVersion());
//				retVal.setCodeSystemDisplayName("Unknown"); // TODO: implement
//				return retVal;
//			}
//
//		}
//
//		return null;
//	}

	@Override
	public List<IIdType> findCodeSystemIdsContainingSystemAndCode(String theCode, String theSystem) {
		List<IIdType> valueSetIds;
		Set<Long> ids = searchForIds(new SearchParameterMap(CodeSystem.SP_CODE, new TokenParam(theSystem, theCode)));
		valueSetIds = new ArrayList<IIdType>();
		for (Long next : ids) {
			valueSetIds.add(new IdType("CodeSystem", next));
		}
		return valueSetIds;
	}

	@Override
	public LookupCodeResult lookupCode(IPrimitiveType<String> theCode, IPrimitiveType<String> theSystem, Coding theCoding, RequestDetails theRequestDetails) {
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

		ourLog.info("Looking up {} / {}", system, code);
		
		if (myValidationSupport.isCodeSystemSupported(getContext(), system)) {

			ourLog.info("Code system {} is supported", system);

			CodeValidationResult result = myValidationSupport.validateCode(getContext(), system, code, null);
			if (result != null) {
				if (result.isOk()) {
					LookupCodeResult retVal = new LookupCodeResult();
					retVal.setFound(true);
					retVal.setSearchedForCode(code);
					retVal.setSearchedForSystem(system);
					retVal.setCodeDisplay(result.asConceptDefinition().getDisplay());
					retVal.setCodeSystemDisplayName("Unknown");
					retVal.setCodeSystemVersion("");
					return retVal;
				}
			}
			
//			HapiWorkerContext ctx = new HapiWorkerContext(getContext(), myValidationSupport);
//			ValueSetExpander expander = ctx.getExpander();
//			ValueSet source = new ValueSet();
//			source.getCompose().addInclude().setSystem(system).addConcept().setCode(code);
//
//			ValueSetExpansionOutcome expansion;
//			try {
//				expansion = expander.expand(source);
//			} catch (Exception e) {
//				throw new InternalErrorException(e);
//			}
//
//			if (expansion.getValueset() != null) {
//				List<ValueSetExpansionContainsComponent> contains = expansion.getValueset().getExpansion().getContains();
//				LookupCodeResult result = lookup(contains, system, code);
//				if (result != null) {
//					return result;
//				}
//			}

		}

		// We didn't find it..
		LookupCodeResult retVal = new LookupCodeResult();
		retVal.setFound(false);
		retVal.setSearchedForCode(code);
		retVal.setSearchedForSystem(system);
		return retVal;

	}

	private List<TermConcept> toPersistedConcepts(List<ConceptDefinitionComponent> theConcept, TermCodeSystemVersion theCodeSystemVersion) {
		ArrayList<TermConcept> retVal = new ArrayList<TermConcept>();

		for (ConceptDefinitionComponent next : theConcept) {
			if (isNotBlank(next.getCode())) {
				TermConcept termConcept = new TermConcept();
				termConcept.setCode(next.getCode());
				termConcept.setCodeSystem(theCodeSystemVersion);
				termConcept.setDisplay(next.getDisplay());
				termConcept.addChildren(toPersistedConcepts(next.getConcept(), theCodeSystemVersion), RelationshipTypeEnum.ISA);
				retVal.add(termConcept);
			}
		}

		return retVal;
	}
	
	@Override
	protected ResourceTable updateEntity(IBaseResource theResource, ResourceTable theEntity, Date theDeletedTimestampOrNull, boolean thePerformIndexing,
			boolean theUpdateVersion, Date theUpdateTime, boolean theForceUpdate, boolean theCreateNewHistoryEntry) {
		ResourceTable retVal = super.updateEntity(theResource, theEntity, theDeletedTimestampOrNull, thePerformIndexing, theUpdateVersion, theUpdateTime, theForceUpdate, theCreateNewHistoryEntry);

		CodeSystem cs = (CodeSystem) theResource;

		if (cs != null && isNotBlank(cs.getUrl())) {
			String codeSystemUrl = cs.getUrl();
			if (cs.getContent() == CodeSystemContentMode.COMPLETE || cs.getContent() == null) {
				ourLog.info("CodeSystem {} has a status of {}, going to store concepts in terminology tables", retVal.getIdDt().getValue(), cs.getContentElement().getValueAsString());
				
				Long codeSystemResourcePid = retVal.getId();
				TermCodeSystemVersion persCs = myCsvDao.findByCodeSystemResourceAndVersion(codeSystemResourcePid, retVal.getVersion());
				if (persCs != null) {
					ourLog.info("Code system version already exists in database");
				} else {
					
					persCs = new TermCodeSystemVersion();
					persCs.setResource(retVal);
					persCs.setResourceVersionId(retVal.getVersion());
					persCs.getConcepts().addAll(toPersistedConcepts(cs.getConcept(), persCs));
					ourLog.info("Code system has {} concepts", persCs.getConcepts().size());
					myTerminologySvc.storeNewCodeSystemVersion(codeSystemResourcePid, codeSystemUrl, persCs);
					
				}

			}
		}

		return retVal;
	}

}
