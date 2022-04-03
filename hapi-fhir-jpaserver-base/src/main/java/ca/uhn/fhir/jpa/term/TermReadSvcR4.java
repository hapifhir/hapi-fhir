package ca.uhn.fhir.jpa.term;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.ConceptValidationOptions;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.context.support.ValidationSupportContext;
import ca.uhn.fhir.context.support.ValueSetExpansionOptions;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.term.api.ITermReadSvcR4;
import ca.uhn.fhir.jpa.term.ex.ExpansionTooCostlyException;
import org.hl7.fhir.instance.model.api.IBaseCoding;
import org.hl7.fhir.instance.model.api.IBaseDatatype;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.ValueSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;

import javax.annotation.Nonnull;
import javax.transaction.Transactional;

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
	public IValidationSupport.ValueSetExpansionOutcome expandValueSet(ValidationSupportContext theValidationSupportContext, ValueSetExpansionOptions theExpansionOptions, @Nonnull IBaseResource theValueSetToExpand) {
		ValueSet expanded = super.expandValueSet(theExpansionOptions, (ValueSet) theValueSetToExpand);
		return new IValidationSupport.ValueSetExpansionOutcome(expanded);
	}

	@Override
	protected ValueSet getValueSetFromResourceTable(ResourceTable theResourceTable) {
		return myDaoRegistry.getResourceDao("ValueSet").toResource(ValueSet.class, theResourceTable, null, false);
	}

	@Override
	public boolean isValueSetSupported(ValidationSupportContext theValidationSupportContext, String theValueSetUrl) {
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

	@Override
	public LookupCodeResult lookupCode(ValidationSupportContext theValidationSupportContext, String theSystem, String theCode, String theDisplayLanguage) {
		return super.lookupCode(theSystem, theCode, theDisplayLanguage);
	}

	@Override
	public IValidationSupport.CodeValidationResult validateCodeIsInPreExpandedValueSet(ConceptValidationOptions theOptions, IBaseResource theValueSet, String theSystem, String theCode, String theDisplay, IBaseDatatype theCoding, IBaseDatatype theCodeableConcept) {
		ValueSet valueSet = (ValueSet) theValueSet;
		Coding coding = toCanonicalCoding(theCoding);
		CodeableConcept codeableConcept = toCanonicalCodeableConcept(theCodeableConcept);
		return super.validateCodeIsInPreExpandedValueSet(theOptions, valueSet, theSystem, theCode, theDisplay, coding, codeableConcept);
	}

	@Override
	protected Coding toCanonicalCoding(IBaseDatatype theCoding) {
		return (Coding) theCoding;
	}

	@Override
	protected Coding toCanonicalCoding(IBaseCoding theCoding) {
		return (Coding) theCoding;
	}

	@Override
	protected CodeableConcept toCanonicalCodeableConcept(IBaseDatatype theCodeableConcept) {
		return (CodeableConcept) theCodeableConcept;
	}

	@Override
	public boolean isValueSetPreExpandedForCodeValidation(IBaseResource theValueSet) {
		ValueSet valueSet = (ValueSet) theValueSet;
		return super.isValueSetPreExpandedForCodeValidation(valueSet);
	}
}
