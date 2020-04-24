package ca.uhn.fhir.jpa.term;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.ConceptValidationOptions;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.context.support.ValueSetExpansionOptions;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoValueSet;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.term.api.ITermReadSvcDstu3;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.util.CoverageIgnore;
import ca.uhn.fhir.util.ValidateUtil;
import ca.uhn.fhir.util.VersionIndependentConcept;
import org.hl7.fhir.convertors.conv30_40.CodeSystem30_40;
import org.hl7.fhir.dstu3.model.CodeSystem;
import org.hl7.fhir.dstu3.model.CodeableConcept;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.ValueSet;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.instance.model.api.IBaseDatatype;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.utilities.validation.ValidationOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.hl7.fhir.convertors.conv30_40.ValueSet30_40.convertValueSet;

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

public class TermReadSvcDstu3 extends BaseTermReadSvcImpl implements IValidationSupport, ITermReadSvcDstu3 {

	@Autowired
	private PlatformTransactionManager myTransactionManager;

	/**
	 * Constructor
	 */
	public TermReadSvcDstu3() {
		super();
	}



	@Override
	public ValueSetExpansionOutcome expandValueSet(IValidationSupport theRootValidationSupport, ValueSetExpansionOptions theExpansionOptions, IBaseResource theValueSetToExpand) {
		try {
			org.hl7.fhir.r4.model.ValueSet valueSetToExpandR4;
			valueSetToExpandR4 = toCanonicalValueSet(theValueSetToExpand);
			org.hl7.fhir.r4.model.ValueSet expandedR4 = super.expandValueSet(theExpansionOptions, valueSetToExpandR4);
			return new ValueSetExpansionOutcome(convertValueSet(expandedR4), null);
		} catch (FHIRException e) {
			throw new InternalErrorException(e);
		}
	}

	@Override
	public IBaseResource expandValueSet(ValueSetExpansionOptions theExpansionOptions, IBaseResource theInput) {
		ValueSet valueSetToExpand = (ValueSet) theInput;

		try {
			org.hl7.fhir.r4.model.ValueSet valueSetToExpandR4;
			valueSetToExpandR4 = toCanonicalValueSet(valueSetToExpand);
			org.hl7.fhir.r4.model.ValueSet expandedR4 = super.expandValueSet(theExpansionOptions, valueSetToExpandR4);
			return convertValueSet(expandedR4);
		} catch (FHIRException e) {
			throw new InternalErrorException(e);
		}
	}

	@Override
	protected org.hl7.fhir.r4.model.ValueSet toCanonicalValueSet(IBaseResource theValueSet) throws FHIRException {
		org.hl7.fhir.r4.model.ValueSet valueSetToExpandR4;
		valueSetToExpandR4 = convertValueSet((ValueSet) theValueSet);
		return valueSetToExpandR4;
	}

	@Override
	protected org.hl7.fhir.r4.model.CodeSystem toCanonicalCodeSystem(IBaseResource theCodeSystem) {
		return CodeSystem30_40.convertCodeSystem((CodeSystem)theCodeSystem);
	}

	@Override
	public void expandValueSet(ValueSetExpansionOptions theExpansionOptions, IBaseResource theValueSetToExpand, IValueSetConceptAccumulator theValueSetCodeAccumulator) {
		ValueSet valueSetToExpand = (ValueSet) theValueSetToExpand;

		try {
			org.hl7.fhir.r4.model.ValueSet valueSetToExpandR4;
			valueSetToExpandR4 = toCanonicalValueSet(valueSetToExpand);
			super.expandValueSet(theExpansionOptions, valueSetToExpandR4, theValueSetCodeAccumulator);
		} catch (FHIRException e) {
			throw new InternalErrorException(e);
		}
	}

	@Override
	protected org.hl7.fhir.r4.model.ValueSet getValueSetFromResourceTable(ResourceTable theResourceTable) {
		ValueSet valueSet = myDaoRegistry.getResourceDao("ValueSet").toResource(ValueSet.class, theResourceTable, null, false);

		org.hl7.fhir.r4.model.ValueSet valueSetR4;
		try {
			valueSetR4 = toCanonicalValueSet(valueSet);
		} catch (FHIRException e) {
			throw new InternalErrorException(e);
		}

		return valueSetR4;
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
			codeOpt = txTemplate.execute(t -> findCode(theCodeSystem, theCode).map(c -> c.toVersionIndependentConcept()));
		}

		if (codeOpt != null && codeOpt.isPresent()) {
			VersionIndependentConcept code = codeOpt.get();
			IValidationSupport.CodeValidationResult retVal = new IValidationSupport.CodeValidationResult()
				.setCode(code.getCode());
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
	public FhirContext getFhirContext() {
		return myContext;
	}

	@Override
	public IFhirResourceDaoValueSet.ValidateCodeResult validateCodeIsInPreExpandedValueSet(ValidationOptions theOptions, IBaseResource theValueSet, String theSystem, String theCode, String theDisplay, IBaseDatatype theCoding, IBaseDatatype theCodeableConcept) {
		ValidateUtil.isNotNullOrThrowUnprocessableEntity(theValueSet, "ValueSet must not be null");
		ValueSet valueSet = (ValueSet) theValueSet;
		org.hl7.fhir.r4.model.ValueSet valueSetR4 = convertValueSet(valueSet);

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

		return super.validateCodeIsInPreExpandedValueSet(theOptions, valueSetR4, theSystem, theCode, theDisplay, codingR4, codeableConceptR4);
	}

	@Override
	public boolean isValueSetPreExpandedForCodeValidation(IBaseResource theValueSet) {
		ValidateUtil.isNotNullOrThrowUnprocessableEntity(theValueSet, "ValueSet must not be null");
		ValueSet valueSet = (ValueSet) theValueSet;
		org.hl7.fhir.r4.model.ValueSet valueSetR4 = convertValueSet(valueSet);
		return super.isValueSetPreExpandedForCodeValidation(valueSetR4);
	}
}
