package ca.uhn.fhir.jpa.term;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.dao.IFhirResourceDaoValueSet.ValidateCodeResult;
import ca.uhn.fhir.jpa.entity.TermConcept;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.util.CoverageIgnore;
import ca.uhn.fhir.util.UrlUtil;
import ca.uhn.fhir.util.ValidateUtil;
import org.hl7.fhir.instance.model.api.IBaseDatatype;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r5.hapi.ctx.IValidationSupport;
import org.hl7.fhir.r5.model.*;
import org.hl7.fhir.r5.model.CodeSystem.ConceptDefinitionComponent;
import org.hl7.fhir.r5.model.ValueSet.ConceptSetComponent;
import org.hl7.fhir.r5.terminologies.ValueSetExpander;
import org.hl7.fhir.utilities.validation.ValidationMessage.IssueSeverity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2019 University Health Network
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

public class HapiTerminologySvcR5 extends BaseHapiTerminologySvcImpl implements IValidationSupport, IHapiTerminologySvcR5 {

	@Autowired
	@Qualifier("myConceptMapDaoR5")
	private IFhirResourceDao<ConceptMap> myConceptMapResourceDao;
	@Autowired
	@Qualifier("myCodeSystemDaoR5")
	private IFhirResourceDao<CodeSystem> myCodeSystemResourceDao;
	@Autowired
	@Qualifier("myValueSetDaoR5")
	private IFhirResourceDao<ValueSet> myValueSetResourceDao;
	@Autowired
	private IValidationSupport myValidationSupport;
	@Autowired
	private PlatformTransactionManager myTransactionManager;

	private void addAllChildren(String theSystemString, ConceptDefinitionComponent theCode, List<VersionIndependentConcept> theListToPopulate) {
		if (isNotBlank(theCode.getCode())) {
			theListToPopulate.add(new VersionIndependentConcept(theSystemString, theCode.getCode()));
		}
		for (ConceptDefinitionComponent nextChild : theCode.getConcept()) {
			addAllChildren(theSystemString, nextChild, theListToPopulate);
		}
	}

	private boolean addTreeIfItContainsCode(String theSystemString, ConceptDefinitionComponent theNext, String theCode, List<VersionIndependentConcept> theListToPopulate) {
		boolean foundCodeInChild = false;
		for (ConceptDefinitionComponent nextChild : theNext.getConcept()) {
			foundCodeInChild |= addTreeIfItContainsCode(theSystemString, nextChild, theCode, theListToPopulate);
		}

		if (theCode.equals(theNext.getCode()) || foundCodeInChild) {
			theListToPopulate.add(new VersionIndependentConcept(theSystemString, theNext.getCode()));
			return true;
		}

		return false;
	}

	@Override
	protected IIdType createOrUpdateCodeSystem(org.hl7.fhir.r4.model.CodeSystem theCodeSystemResource) {
		validateCodeSystemForStorage(theCodeSystemResource);

		CodeSystem codeSystemR4 = org.hl7.fhir.convertors.conv40_50.CodeSystem.convertCodeSystem(theCodeSystemResource);
		if (isBlank(theCodeSystemResource.getIdElement().getIdPart())) {
			String matchUrl = "CodeSystem?url=" + UrlUtil.escapeUrlParam(theCodeSystemResource.getUrl());
			return myCodeSystemResourceDao.update(codeSystemR4, matchUrl).getId();
		} else {
			return myCodeSystemResourceDao.update(codeSystemR4).getId();
		}
	}

	@Override
	protected void createOrUpdateConceptMap(org.hl7.fhir.r4.model.ConceptMap theConceptMap) {

		ConceptMap conceptMapR4 = org.hl7.fhir.convertors.conv40_50.ConceptMap.convertConceptMap(theConceptMap);

		if (isBlank(theConceptMap.getIdElement().getIdPart())) {
			String matchUrl = "ConceptMap?url=" + UrlUtil.escapeUrlParam(theConceptMap.getUrl());
			myConceptMapResourceDao.update(conceptMapR4, matchUrl);
		} else {
			myConceptMapResourceDao.update(conceptMapR4);
		}
	}

	@Override
	protected void createOrUpdateValueSet(org.hl7.fhir.r4.model.ValueSet theValueSet) {

		ValueSet valueSetR4 = org.hl7.fhir.convertors.conv40_50.ValueSet.convertValueSet(theValueSet);

		if (isBlank(theValueSet.getIdElement().getIdPart())) {
			String matchUrl = "ValueSet?url=" + UrlUtil.escapeUrlParam(theValueSet.getUrl());
			myValueSetResourceDao.update(valueSetR4, matchUrl);
		} else {
			myValueSetResourceDao.update(valueSetR4);
		}
	}

	@Override
	public List<VersionIndependentConcept> expandValueSet(String theValueSet) {
		ValueSet valueSetR5 = myValidationSupport.fetchResource(myContext, ValueSet.class, theValueSet);
		if (valueSetR5 == null) {
			return Collections.emptyList();
		}

		return expandValueSetAndReturnVersionIndependentConcepts(org.hl7.fhir.convertors.conv40_50.ValueSet.convertValueSet(valueSetR5));
	}

	@Override
	public IBaseResource expandValueSet(IBaseResource theInput) {
		org.hl7.fhir.r4.model.ValueSet valueSetToExpand = org.hl7.fhir.convertors.conv40_50.ValueSet.convertValueSet((ValueSet) theInput);
		org.hl7.fhir.r4.model.ValueSet valueSetR4 = super.expandValueSet(valueSetToExpand);
		return org.hl7.fhir.convertors.conv40_50.ValueSet.convertValueSet(valueSetR4);
	}

	@Override
	public IBaseResource expandValueSet(IBaseResource theInput, int theOffset, int theCount) {
		org.hl7.fhir.r4.model.ValueSet valueSetToExpand = org.hl7.fhir.convertors.conv40_50.ValueSet.convertValueSet((ValueSet) theInput);
		org.hl7.fhir.r4.model.ValueSet valueSetR4 = super.expandValueSet(valueSetToExpand, theOffset, theCount);
		return org.hl7.fhir.convertors.conv40_50.ValueSet.convertValueSet(valueSetR4);
	}

	@Override
	public void expandValueSet(IBaseResource theValueSetToExpand, IValueSetConceptAccumulator theValueSetCodeAccumulator) {
		org.hl7.fhir.r4.model.ValueSet valueSetToExpand = org.hl7.fhir.convertors.conv40_50.ValueSet.convertValueSet((ValueSet) theValueSetToExpand);
		super.expandValueSet(valueSetToExpand, theValueSetCodeAccumulator);
	}

	@Override
	public ValueSetExpander.ValueSetExpansionOutcome expandValueSet(FhirContext theContext, ConceptSetComponent theInclude) {
		ValueSet valueSetToExpand = new ValueSet();
		valueSetToExpand.getCompose().addInclude(theInclude);
		org.hl7.fhir.r4.model.ValueSet expandedR4 = super.expandValueSet(org.hl7.fhir.convertors.conv40_50.ValueSet.convertValueSet(valueSetToExpand));
		return new ValueSetExpander.ValueSetExpansionOutcome(org.hl7.fhir.convertors.conv40_50.ValueSet.convertValueSet(expandedR4));
	}

	@Override
	public List<IBaseResource> fetchAllConformanceResources(FhirContext theContext) {
		return null;
	}

	@Override
	public List<StructureDefinition> fetchAllStructureDefinitions(FhirContext theContext) {
		return Collections.emptyList();
	}

	@CoverageIgnore
	@Override
	public CodeSystem fetchCodeSystem(FhirContext theContext, String theSystem) {
		return null;
	}

	@CoverageIgnore
	@Override
	public ValueSet fetchValueSet(FhirContext theContext, String theSystem) {
		return null;
	}

	@Override
	public <T extends IBaseResource> T fetchResource(FhirContext theContext, Class<T> theClass, String theUri) {
		return null;
	}

	@CoverageIgnore
	@Override
	public StructureDefinition fetchStructureDefinition(FhirContext theCtx, String theUrl) {
		return null;
	}

	private void findCodesAbove(CodeSystem theSystem, String theSystemString, String theCode, List<VersionIndependentConcept> theListToPopulate) {
		List<ConceptDefinitionComponent> conceptList = theSystem.getConcept();
		for (ConceptDefinitionComponent next : conceptList) {
			addTreeIfItContainsCode(theSystemString, next, theCode, theListToPopulate);
		}
	}

	@Override
	public List<VersionIndependentConcept> findCodesAboveUsingBuiltInSystems(String theSystem, String theCode) {
		ArrayList<VersionIndependentConcept> retVal = new ArrayList<>();
		CodeSystem system = myValidationSupport.fetchCodeSystem(myContext, theSystem);
		if (system != null) {
			findCodesAbove(system, theSystem, theCode, retVal);
		}
		return retVal;
	}

	private void findCodesBelow(CodeSystem theSystem, String theSystemString, String theCode, List<VersionIndependentConcept> theListToPopulate) {
		List<ConceptDefinitionComponent> conceptList = theSystem.getConcept();
		findCodesBelow(theSystemString, theCode, theListToPopulate, conceptList);
	}

	private void findCodesBelow(String theSystemString, String theCode, List<VersionIndependentConcept> theListToPopulate, List<ConceptDefinitionComponent> conceptList) {
		for (ConceptDefinitionComponent next : conceptList) {
			if (theCode.equals(next.getCode())) {
				addAllChildren(theSystemString, next, theListToPopulate);
			} else {
				findCodesBelow(theSystemString, theCode, theListToPopulate, next.getConcept());
			}
		}
	}

	@Override
	public List<VersionIndependentConcept> findCodesBelowUsingBuiltInSystems(String theSystem, String theCode) {
		ArrayList<VersionIndependentConcept> retVal = new ArrayList<>();
		CodeSystem system = myValidationSupport.fetchCodeSystem(myContext, theSystem);
		if (system != null) {
			findCodesBelow(system, theSystem, theCode, retVal);
		}
		return retVal;
	}

	@Override
	protected org.hl7.fhir.r4.model.CodeSystem getCodeSystemFromContext(String theSystem) {
		CodeSystem codeSystemR5 = myValidationSupport.fetchCodeSystem(myContext, theSystem);
		return org.hl7.fhir.convertors.conv40_50.CodeSystem.convertCodeSystem(codeSystemR5);
	}

	@Override
	protected org.hl7.fhir.r4.model.ValueSet getValueSetFromResourceTable(ResourceTable theResourceTable) {
		ValueSet valueSetR5 = myValueSetResourceDao.toResource(ValueSet.class, theResourceTable, null, false);
		return org.hl7.fhir.convertors.conv40_50.ValueSet.convertValueSet(valueSetR5);
	}

	@Override
	public boolean isCodeSystemSupported(FhirContext theContext, String theSystem) {
		return supportsSystem(theSystem);
	}

	@Override
	public StructureDefinition generateSnapshot(StructureDefinition theInput, String theUrl, String theWebUrl, String theProfileName) {
		return null;
	}

	@CoverageIgnore
	@Override
	public IValidationSupport.CodeValidationResult validateCode(FhirContext theContext, String theCodeSystem, String theCode, String theDisplay) {
		TransactionTemplate txTemplate = new TransactionTemplate(myTransactionManager);
		txTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		return txTemplate.execute(t-> {
			Optional<TermConcept> codeOpt = findCode(theCodeSystem, theCode);
			if (codeOpt.isPresent()) {
				TermConcept code = codeOpt.get();
				ConceptDefinitionComponent def = new ConceptDefinitionComponent();
				def.setCode(code.getCode());
				def.setDisplay(code.getDisplay());
				IValidationSupport.CodeValidationResult retVal = new IValidationSupport.CodeValidationResult(def);
				retVal.setProperties(code.toValidationProperties());
				return retVal;
			}

			return new IValidationSupport.CodeValidationResult(IssueSeverity.ERROR, "Unknown code {" + theCodeSystem + "}" + theCode);
		});
	}

	@Override
	public LookupCodeResult lookupCode(FhirContext theContext, String theSystem, String theCode) {
		return super.lookupCode(theContext, theSystem, theCode);
	}

	@Override
	public ValidateCodeResult validateCodeIsInPreExpandedValueSet(IBaseResource theValueSet, String theSystem, String theCode, String theDisplay, IBaseDatatype theCoding, IBaseDatatype theCodeableConcept) {
		ValidateUtil.isNotNullOrThrowUnprocessableEntity(theValueSet, "ValueSet must not be null");
		ValueSet valueSet = (ValueSet) theValueSet;
		org.hl7.fhir.r4.model.ValueSet valueSetR4 = org.hl7.fhir.convertors.conv40_50.ValueSet.convertValueSet(valueSet);

		Coding coding = (Coding) theCoding;
		org.hl7.fhir.r4.model.Coding codingR4 = null;
		if (coding != null) {
			codingR4 = new org.hl7.fhir.r4.model.Coding(coding.getSystem(), coding.getCode(), coding.getDisplay());
		}

		CodeableConcept codeableConcept = (CodeableConcept) theCodeableConcept;
		org.hl7.fhir.r4.model.CodeableConcept codeableConceptR4 = null;
		if (codeableConcept != null) {
			codeableConceptR4 = new org.hl7.fhir.r4.model.CodeableConcept();
			for (Coding nestedCoding : codeableConcept.getCoding()) {
				codeableConceptR4.addCoding(new org.hl7.fhir.r4.model.Coding(nestedCoding.getSystem(), nestedCoding.getCode(), nestedCoding.getDisplay()));
			}
		}

		return super.validateCodeIsInPreExpandedValueSet(valueSetR4, theSystem, theCode, theDisplay, codingR4, codeableConceptR4);
	}

	@Override
	public boolean isValueSetPreExpandedForCodeValidation(IBaseResource theValueSet) {
		ValidateUtil.isNotNullOrThrowUnprocessableEntity(theValueSet, "ValueSet must not be null");
		ValueSet valueSet = (ValueSet) theValueSet;
		org.hl7.fhir.r4.model.ValueSet valueSetR4 = org.hl7.fhir.convertors.conv40_50.ValueSet.convertValueSet(valueSet);
		return super.isValueSetPreExpandedForCodeValidation(valueSetR4);
	}
}
