package ca.uhn.fhir.jpa.term;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.dao.IFhirResourceDaoValueSet.ValidateCodeResult;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.term.api.ITermReadSvcR4;
import ca.uhn.fhir.jpa.term.ex.ExpansionTooCostlyException;
import ca.uhn.fhir.util.CoverageIgnore;
import org.hl7.fhir.instance.model.api.IBaseDatatype;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.hapi.ctx.IValidationSupport;
import org.hl7.fhir.r4.model.*;
import org.hl7.fhir.r4.model.CodeSystem.ConceptDefinitionComponent;
import org.hl7.fhir.r4.model.ValueSet.ConceptSetComponent;
import org.hl7.fhir.r4.terminologies.ValueSetExpander;
import org.hl7.fhir.utilities.validation.ValidationMessage.IssueSeverity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

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
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

public class TermReadSvcR4 extends BaseTermReadSvcImpl implements ITermReadSvcR4 {

	@Autowired
	@Qualifier("myValueSetDaoR4")
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
	public List<VersionIndependentConcept> expandValueSet(String theValueSet) {
		// TODO: DM 2019-09-10 - This is problematic because an incorrect URL that matches ValueSet.id will not be found in the terminology tables but will yield a ValueSet here. Depending on the ValueSet, the expansion may time-out.
		ValueSet vs = myValidationSupport.fetchResource(myContext, ValueSet.class, theValueSet);
		if (vs == null) {
			super.throwInvalidValueSet(theValueSet);
		}

		return expandValueSetAndReturnVersionIndependentConcepts(vs, null);
	}

	@Override
	public IBaseResource expandValueSet(IBaseResource theInput) {
		ValueSet valueSetToExpand = (ValueSet) theInput;
		return super.expandValueSetInMemory(valueSetToExpand, null);
	}

	@Override
	public IBaseResource expandValueSet(IBaseResource theInput, int theOffset, int theCount) {
		ValueSet valueSetToExpand = (ValueSet) theInput;
		return super.expandValueSet(valueSetToExpand, theOffset, theCount);
	}

	@Override
	public void expandValueSet(IBaseResource theValueSetToExpand, IValueSetConceptAccumulator theValueSetCodeAccumulator) {
		ValueSet valueSetToExpand = (ValueSet) theValueSetToExpand;
		super.expandValueSet(valueSetToExpand, theValueSetCodeAccumulator);
	}

	@Transactional(dontRollbackOn = {ExpansionTooCostlyException.class})
	@Override
	public ValueSetExpander.ValueSetExpansionOutcome expandValueSet(FhirContext theContext, ConceptSetComponent theInclude) {
		ValueSet valueSetToExpand = new ValueSet();
		valueSetToExpand.getCompose().addInclude(theInclude);
		ValueSet expanded = super.expandValueSetInMemory(valueSetToExpand, null);
		return new ValueSetExpander.ValueSetExpansionOutcome(expanded);
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
	public CodeSystem getCodeSystemFromContext(String theSystem) {
		return myValidationSupport.fetchCodeSystem(myContext, theSystem);
	}

	@Override
	protected ValueSet getValueSetFromResourceTable(ResourceTable theResourceTable) {
		return myValueSetResourceDao.toResource(ValueSet.class, theResourceTable, null, false);
	}

	@Override
	public boolean isCodeSystemSupported(FhirContext theContext, String theSystem) {
		return supportsSystem(theSystem);
	}

	@Override
	public boolean isValueSetSupported(FhirContext theContext, String theValueSetUrl) {
		return myValidationSupport.fetchResource(myContext, ValueSet.class, theValueSetUrl) != null;
	}

	@Override
	public StructureDefinition generateSnapshot(StructureDefinition theInput, String theUrl, String theWebUrl, String theProfileName) {
		return null;
	}

	@Override
	protected ValueSet toCanonicalValueSet(IBaseResource theValueSet) {
		return (ValueSet) theValueSet;
	}

	@CoverageIgnore
	@Override
	public IValidationSupport.CodeValidationResult validateCode(FhirContext theContext, String theCodeSystem, String theCode, String theDisplay, String theValueSetUrl) {
		Optional<VersionIndependentConcept> codeOpt = Optional.empty();
		boolean haveValidated = false;

		if (isNotBlank(theValueSetUrl)) {
			codeOpt = super.validateCodeInValueSet(theValueSetUrl, theCodeSystem, theCode);
			haveValidated = true;
		}

		if (!haveValidated) {
		TransactionTemplate txTemplate = new TransactionTemplate(myTransactionManager);
		txTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
			codeOpt = txTemplate.execute(t -> findCode(theCodeSystem, theCode).map(c->c.toVersionIndependentConcept()));
		}

		if (codeOpt != null && codeOpt.isPresent()) {
			VersionIndependentConcept code = codeOpt.get();
				ConceptDefinitionComponent def = new ConceptDefinitionComponent();
				def.setCode(code.getCode());
				IValidationSupport.CodeValidationResult retVal = new IValidationSupport.CodeValidationResult(def);
				return retVal;
			}

			return new IValidationSupport.CodeValidationResult(IssueSeverity.ERROR, "Unknown code {" + theCodeSystem + "}" + theCode);
	}

	@Override
	public LookupCodeResult lookupCode(FhirContext theContext, String theSystem, String theCode) {
		return super.lookupCode(theContext, theSystem, theCode);
	}

	@Override
	public ValidateCodeResult validateCodeIsInPreExpandedValueSet(IBaseResource theValueSet, String theSystem, String theCode, String theDisplay, IBaseDatatype theCoding, IBaseDatatype theCodeableConcept) {
		ValueSet valueSet = (ValueSet) theValueSet;
		Coding coding = (Coding) theCoding;
		CodeableConcept codeableConcept = (CodeableConcept) theCodeableConcept;
		return super.validateCodeIsInPreExpandedValueSet(valueSet, theSystem, theCode, theDisplay, coding, codeableConcept);
	}

	@Override
	public boolean isValueSetPreExpandedForCodeValidation(IBaseResource theValueSet) {
		ValueSet valueSet = (ValueSet) theValueSet;
		return super.isValueSetPreExpandedForCodeValidation(valueSet);
	}
}
