package ca.uhn.fhir.jpa.term;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.ConceptValidationOptions;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.context.support.ValidationSupportContext;
import ca.uhn.fhir.context.support.ValueSetExpansionOptions;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.term.api.ITermReadSvcR5;
import ca.uhn.fhir.jpa.term.ex.ExpansionTooCostlyException;
import ca.uhn.fhir.util.ValidateUtil;
import org.hl7.fhir.convertors.VersionConvertor_40_50;
import org.hl7.fhir.convertors.conv40_50.CodeSystem40_50;
import org.hl7.fhir.instance.model.api.IBaseDatatype;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r5.model.CodeSystem;
import org.hl7.fhir.r5.model.CodeableConcept;
import org.hl7.fhir.r5.model.Coding;
import org.hl7.fhir.r5.model.ValueSet;
import org.hl7.fhir.utilities.TerminologyServiceOptions;
import org.hl7.fhir.utilities.validation.ValidationOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;

import javax.annotation.Nullable;
import javax.transaction.Transactional;

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

public class TermReadSvcR5 extends BaseTermReadSvcImpl implements IValidationSupport, ITermReadSvcR5 {

	@Autowired
	private DaoRegistry myDaoRegistry;
	@Autowired
	private PlatformTransactionManager myTransactionManager;

	@Override
	@Transactional(dontRollbackOn = {ExpansionTooCostlyException.class})
	public ValueSetExpansionOutcome expandValueSet(ValidationSupportContext theValidationSupportContext, ValueSetExpansionOptions theExpansionOptions, IBaseResource theValueSetToExpand) {
		ValueSet valueSetToExpand = (ValueSet) theValueSetToExpand;
		org.hl7.fhir.r4.model.ValueSet expandedR4 = super.expandValueSet(theExpansionOptions, org.hl7.fhir.convertors.conv40_50.ValueSet40_50.convertValueSet(valueSetToExpand));
		return new ValueSetExpansionOutcome(org.hl7.fhir.convertors.conv40_50.ValueSet40_50.convertValueSet(expandedR4));
	}

	@Override
	public IBaseResource expandValueSet(ValueSetExpansionOptions theExpansionOptions, IBaseResource theInput) {
		org.hl7.fhir.r4.model.ValueSet valueSetToExpand = toCanonicalValueSet(theInput);
		org.hl7.fhir.r4.model.ValueSet valueSetR4 = super.expandValueSet(theExpansionOptions, valueSetToExpand);
		return org.hl7.fhir.convertors.conv40_50.ValueSet40_50.convertValueSet(valueSetR4);
	}

	@Override
	public void expandValueSet(ValueSetExpansionOptions theExpansionOptions, IBaseResource theValueSetToExpand, IValueSetConceptAccumulator theValueSetCodeAccumulator) {
		org.hl7.fhir.r4.model.ValueSet valueSetToExpand = toCanonicalValueSet(theValueSetToExpand);
		super.expandValueSet(theExpansionOptions, valueSetToExpand, theValueSetCodeAccumulator);
	}

	@Override
	protected org.hl7.fhir.r4.model.ValueSet getValueSetFromResourceTable(ResourceTable theResourceTable) {
		ValueSet valueSetR5 = myDaoRegistry.getResourceDao("ValueSet").toResource(ValueSet.class, theResourceTable, null, false);
		return org.hl7.fhir.convertors.conv40_50.ValueSet40_50.convertValueSet(valueSetR5);
	}

	@Override
	public FhirContext getFhirContext() {
		return myContext;
	}

	@Override
	public CodeValidationResult validateCodeIsInPreExpandedValueSet(ConceptValidationOptions theOptions, IBaseResource theValueSet, String theSystem, String theCode, String theDisplay, IBaseDatatype theCoding, IBaseDatatype theCodeableConcept) {
		ValidateUtil.isNotNullOrThrowUnprocessableEntity(theValueSet, "ValueSet must not be null");
		ValueSet valueSet = (ValueSet) theValueSet;
		org.hl7.fhir.r4.model.ValueSet valueSetR4 = toCanonicalValueSet(valueSet);

		org.hl7.fhir.r4.model.Coding codingR4 = toCanonicalCoding(theCoding);

		CodeableConcept codeableConcept = (CodeableConcept) theCodeableConcept;
		org.hl7.fhir.r4.model.CodeableConcept codeableConceptR4 = null;
		if (codeableConcept != null) {
			codeableConceptR4 = new org.hl7.fhir.r4.model.CodeableConcept();
			for (Coding nestedCoding : codeableConcept.getCoding()) {
				codeableConceptR4.addCoding(new org.hl7.fhir.r4.model.Coding(nestedCoding.getSystem(), nestedCoding.getCode(), nestedCoding.getDisplay()));
			}
		}

		return super.validateCodeIsInPreExpandedValueSet(theOptions, valueSetR4, theSystem, theCode, theDisplay, codingR4, codeableConceptR4);
	}

	@Override
	@Nullable
	protected org.hl7.fhir.r4.model.Coding toCanonicalCoding(IBaseDatatype theCoding) {
		return VersionConvertor_40_50.convertCoding((Coding) theCoding);
	}

	@Override
	@Nullable
	protected org.hl7.fhir.r4.model.CodeableConcept toCanonicalCodeableConcept(IBaseDatatype theCoding) {
		return VersionConvertor_40_50.convertCodeableConcept((CodeableConcept) theCoding);
	}


	@Override
	protected org.hl7.fhir.r4.model.ValueSet toCanonicalValueSet(IBaseResource theValueSet) throws org.hl7.fhir.exceptions.FHIRException {
		return org.hl7.fhir.convertors.conv40_50.ValueSet40_50.convertValueSet((ValueSet) theValueSet);
	}

	@Override
	protected org.hl7.fhir.r4.model.CodeSystem toCanonicalCodeSystem(IBaseResource theCodeSystem) {
		return CodeSystem40_50.convertCodeSystem((CodeSystem) theCodeSystem);
	}

	@Override
	public boolean isValueSetPreExpandedForCodeValidation(IBaseResource theValueSet) {
		ValidateUtil.isNotNullOrThrowUnprocessableEntity(theValueSet, "ValueSet must not be null");
		ValueSet valueSet = (ValueSet) theValueSet;
		org.hl7.fhir.r4.model.ValueSet valueSetR4 = toCanonicalValueSet(valueSet);
		return super.isValueSetPreExpandedForCodeValidation(valueSetR4);
	}
}
