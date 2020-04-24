package ca.uhn.fhir.jpa.term;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.ConceptValidationOptions;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.context.support.ValueSetExpansionOptions;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoValueSet;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.term.api.ITermReadSvcR4;
import ca.uhn.fhir.jpa.term.ex.ExpansionTooCostlyException;
import ca.uhn.fhir.util.CoverageIgnore;
import ca.uhn.fhir.util.VersionIndependentConcept;
import org.hl7.fhir.instance.model.api.IBaseDatatype;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.ValueSet;
import org.hl7.fhir.utilities.validation.ValidationOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

import javax.transaction.Transactional;
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
	private PlatformTransactionManager myTransactionManager;

	@Override
	public IBaseResource expandValueSet(ValueSetExpansionOptions theExpansionOptions, IBaseResource theInput) {
		ValueSet valueSetToExpand = (ValueSet) theInput;
		return super.expandValueSet(theExpansionOptions, valueSetToExpand);
	}

	@Override
	public void expandValueSet(ValueSetExpansionOptions theExpansionOptions, IBaseResource theValueSetToExpand, IValueSetConceptAccumulator theValueSetCodeAccumulator) {
		ValueSet valueSetToExpand = (ValueSet) theValueSetToExpand;
		super.expandValueSet(theExpansionOptions, valueSetToExpand, theValueSetCodeAccumulator);
	}

	@Transactional(dontRollbackOn = {ExpansionTooCostlyException.class})
	@Override
	public IValidationSupport.ValueSetExpansionOutcome expandValueSet(IValidationSupport theRootValidationSupport, ValueSetExpansionOptions theExpansionOptions, IBaseResource theValueSetToExpand)  {
		ValueSet expanded = super.expandValueSet(theExpansionOptions, (ValueSet) theValueSetToExpand);
		return new IValidationSupport.ValueSetExpansionOutcome(expanded);
	}

	@Override
	protected ValueSet getValueSetFromResourceTable(ResourceTable theResourceTable) {
		return myDaoRegistry.getResourceDao("ValueSet").toResource(ValueSet.class, theResourceTable, null, false);
	}

	@Override
	public boolean isValueSetSupported(IValidationSupport theRootValidationSupport, String theValueSetUrl) {
		return fetchValueSet(theValueSetUrl) != null;
	}

	@Override
	public FhirContext getFhirContext() {
		return myContext;
	}


	@Override
	protected ValueSet toCanonicalValueSet(IBaseResource theValueSet) {
		return (ValueSet) theValueSet;
	}

	@Override
	protected CodeSystem toCanonicalCodeSystem(IBaseResource theCodeSystem) {
		return (CodeSystem) theCodeSystem;
	}

	@CoverageIgnore
	@Override
	public IValidationSupport.CodeValidationResult validateCode(IValidationSupport theRootValidationSupport, ConceptValidationOptions theOptions, String theCodeSystem, String theCode, String theDisplay, String theValueSetUrl) {
		Optional<VersionIndependentConcept> codeOpt = Optional.empty();
		boolean haveValidated = false;

		if (isNotBlank(theValueSetUrl)) {
			codeOpt = super.validateCodeInValueSet(theRootValidationSupport, theOptions, theValueSetUrl, theCodeSystem, theCode);
			haveValidated = true;
		}

		if (!haveValidated) {
		TransactionTemplate txTemplate = new TransactionTemplate(myTransactionManager);
		txTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
			codeOpt = txTemplate.execute(t -> findCode(theCodeSystem, theCode).map(c->c.toVersionIndependentConcept()));
		}

		if (codeOpt != null && codeOpt.isPresent()) {
			VersionIndependentConcept code = codeOpt.get();
				IValidationSupport.CodeValidationResult retVal = new IValidationSupport.CodeValidationResult()
					.setCode(code.getCode()); // AAAAAAAAAAA format
				return retVal;
			}

			return new IValidationSupport.CodeValidationResult()
		.setSeverity(IssueSeverity.ERROR)
		.setMessage("Unknown code {" + theCodeSystem + "}" + theCode);
	}

	@Override
	public LookupCodeResult lookupCode(IValidationSupport theRootValidationSupport, String theSystem, String theCode) {
		return super.lookupCode(theSystem, theCode);
	}

	@Override
	public IFhirResourceDaoValueSet.ValidateCodeResult validateCodeIsInPreExpandedValueSet(ValidationOptions theOptions, IBaseResource theValueSet, String theSystem, String theCode, String theDisplay, IBaseDatatype theCoding, IBaseDatatype theCodeableConcept) {
		ValueSet valueSet = (ValueSet) theValueSet;
		Coding coding = (Coding) theCoding;
		CodeableConcept codeableConcept = (CodeableConcept) theCodeableConcept;
		return super.validateCodeIsInPreExpandedValueSet(theOptions, valueSet, theSystem, theCode, theDisplay, coding, codeableConcept);
	}

	@Override
	public boolean isValueSetPreExpandedForCodeValidation(IBaseResource theValueSet) {
		ValueSet valueSet = (ValueSet) theValueSet;
		return super.isValueSetPreExpandedForCodeValidation(valueSet);
	}
}
