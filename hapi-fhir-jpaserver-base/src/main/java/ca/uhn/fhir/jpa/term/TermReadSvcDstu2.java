package ca.uhn.fhir.jpa.term;

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

import ca.uhn.fhir.context.support.ConceptValidationOptions;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.context.support.ValueSetExpansionOptions;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.model.dstu2.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dstu2.composite.CodingDt;
import ca.uhn.fhir.util.FhirVersionIndependentConcept;
import org.hl7.fhir.instance.model.api.IBaseCoding;
import org.hl7.fhir.instance.model.api.IBaseDatatype;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.ValueSet;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class TermReadSvcDstu2 extends BaseTermReadSvcImpl {

	@Autowired
	private IValidationSupport myValidationSupport;

	private void addAllChildren(String theSystemString, ca.uhn.fhir.model.dstu2.resource.ValueSet.CodeSystemConcept theCode, List<FhirVersionIndependentConcept> theListToPopulate) {
		if (isNotBlank(theCode.getCode())) {
			theListToPopulate.add(new FhirVersionIndependentConcept(theSystemString, theCode.getCode()));
		}
		for (ca.uhn.fhir.model.dstu2.resource.ValueSet.CodeSystemConcept nextChild : theCode.getConcept()) {
			addAllChildren(theSystemString, nextChild, theListToPopulate);
		}
	}

	private boolean addTreeIfItContainsCode(String theSystemString, ca.uhn.fhir.model.dstu2.resource.ValueSet.CodeSystemConcept theNext, String theCode, List<FhirVersionIndependentConcept> theListToPopulate) {
		boolean foundCodeInChild = false;
		for (ca.uhn.fhir.model.dstu2.resource.ValueSet.CodeSystemConcept nextChild : theNext.getConcept()) {
			foundCodeInChild |= addTreeIfItContainsCode(theSystemString, nextChild, theCode, theListToPopulate);
		}

		if (theCode.equals(theNext.getCode()) || foundCodeInChild) {
			theListToPopulate.add(new FhirVersionIndependentConcept(theSystemString, theNext.getCode()));
			return true;
		}

		return false;
	}

	@Override
	protected ValueSet getValueSetFromResourceTable(ResourceTable theResourceTable) {
		throw new UnsupportedOperationException(Msg.code(852));
	}

	@Override
	protected ValueSet toCanonicalValueSet(IBaseResource theValueSet) {
		throw new UnsupportedOperationException(Msg.code(853));
	}

	@Override
	protected CodeSystem toCanonicalCodeSystem(IBaseResource theCodeSystem) {
		throw new UnsupportedOperationException(Msg.code(854));
	}

	@Override
	public IBaseResource expandValueSet(ValueSetExpansionOptions theExpansionOptions, IBaseResource theValueSetToExpand) {
		throw new UnsupportedOperationException(Msg.code(855));
	}

	@Override
	public void expandValueSet(ValueSetExpansionOptions theExpansionOptions, IBaseResource theValueSetToExpand, IValueSetConceptAccumulator theValueSetCodeAccumulator) {
		throw new UnsupportedOperationException(Msg.code(856));
	}

	private void findCodesAbove(ca.uhn.fhir.model.dstu2.resource.ValueSet theSystem, String theSystemString, String theCode, List<FhirVersionIndependentConcept> theListToPopulate) {
		List<ca.uhn.fhir.model.dstu2.resource.ValueSet.CodeSystemConcept> conceptList = theSystem.getCodeSystem().getConcept();
		for (ca.uhn.fhir.model.dstu2.resource.ValueSet.CodeSystemConcept next : conceptList) {
			addTreeIfItContainsCode(theSystemString, next, theCode, theListToPopulate);
		}
	}

	@Override
	public List<FhirVersionIndependentConcept> findCodesAboveUsingBuiltInSystems(String theSystem, String theCode) {
		ArrayList<FhirVersionIndependentConcept> retVal = new ArrayList<>();
		ca.uhn.fhir.model.dstu2.resource.ValueSet system = (ca.uhn.fhir.model.dstu2.resource.ValueSet) myValidationSupport.fetchCodeSystem(theSystem);
		if (system != null) {
			findCodesAbove(system, theSystem, theCode, retVal);
		}
		return retVal;
	}

	private void findCodesBelow(ca.uhn.fhir.model.dstu2.resource.ValueSet theSystem, String theSystemString, String theCode, List<FhirVersionIndependentConcept> theListToPopulate) {
		List<ca.uhn.fhir.model.dstu2.resource.ValueSet.CodeSystemConcept> conceptList = theSystem.getCodeSystem().getConcept();
		findCodesBelow(theSystemString, theCode, theListToPopulate, conceptList);
	}

	private void findCodesBelow(String theSystemString, String theCode, List<FhirVersionIndependentConcept> theListToPopulate, List<ca.uhn.fhir.model.dstu2.resource.ValueSet.CodeSystemConcept> conceptList) {
		for (ca.uhn.fhir.model.dstu2.resource.ValueSet.CodeSystemConcept next : conceptList) {
			if (theCode.equals(next.getCode())) {
				addAllChildren(theSystemString, next, theListToPopulate);
			} else {
				findCodesBelow(theSystemString, theCode, theListToPopulate, next.getConcept());
			}
		}
	}

	@Override
	public List<FhirVersionIndependentConcept> findCodesBelowUsingBuiltInSystems(String theSystem, String theCode) {
		ArrayList<FhirVersionIndependentConcept> retVal = new ArrayList<>();
		ca.uhn.fhir.model.dstu2.resource.ValueSet system = (ca.uhn.fhir.model.dstu2.resource.ValueSet) myValidationSupport.fetchCodeSystem(theSystem);
		if (system != null) {
			findCodesBelow(system, theSystem, theCode, retVal);
		}
		return retVal;
	}

	@Nullable
	@Override
	protected Coding toCanonicalCoding(@Nullable IBaseDatatype theCoding) {
		Coding retVal = null;
		if (theCoding != null) {
			CodingDt coding = (CodingDt) theCoding;
			retVal = new Coding(coding.getSystem(), coding.getCode(), coding.getDisplay());
		}
		return retVal;
	}

	@Nullable
	@Override
	protected Coding toCanonicalCoding(@Nullable IBaseCoding theCoding) {
		Coding retVal = null;
		if (theCoding != null) {
			CodingDt coding = (CodingDt) theCoding;
			retVal = new Coding(coding.getSystem(), coding.getCode(), coding.getDisplay());
		}
		return retVal;
	}

	@Nullable
	@Override
	protected CodeableConcept toCanonicalCodeableConcept(@Nullable IBaseDatatype theCodeableConcept) {
		CodeableConcept outcome = null;
		if (theCodeableConcept != null) {
			outcome = new CodeableConcept();
			CodeableConceptDt cc = (CodeableConceptDt) theCodeableConcept;
			outcome.setText(cc.getText());
			for (CodingDt next : cc.getCoding()) {
				outcome.addCoding(toCanonicalCoding((IBaseDatatype)next));
			}
		}
		return outcome;
	}

	@Override
	public CodeValidationResult validateCodeIsInPreExpandedValueSet(ConceptValidationOptions theOptions, IBaseResource theValueSet, String theSystem, String theCode, String theDisplay, IBaseDatatype theCoding, IBaseDatatype theCodeableConcept) {
		throw new UnsupportedOperationException(Msg.code(857));
	}

	@Override
	public boolean isValueSetPreExpandedForCodeValidation(IBaseResource theValueSet) {
		throw new UnsupportedOperationException(Msg.code(858));
	}
}
