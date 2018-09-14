package ca.uhn.fhir.jpa.term;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2018 University Health Network
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

import org.hl7.fhir.instance.hapi.validation.IValidationSupport;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.ConceptMap;
import org.hl7.fhir.r4.model.ValueSet;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class HapiTerminologySvcDstu2 extends BaseHapiTerminologySvcImpl {

	@Autowired
	private IValidationSupport myValidationSupport;

	private void addAllChildren(String theSystemString, org.hl7.fhir.instance.model.ValueSet.ConceptDefinitionComponent theCode, List<VersionIndependentConcept> theListToPopulate) {
		if (isNotBlank(theCode.getCode())) {
			theListToPopulate.add(new VersionIndependentConcept(theSystemString, theCode.getCode()));
		}
		for (org.hl7.fhir.instance.model.ValueSet.ConceptDefinitionComponent nextChild : theCode.getConcept()) {
			addAllChildren(theSystemString, nextChild, theListToPopulate);
		}
	}

	private boolean addTreeIfItContainsCode(String theSystemString, org.hl7.fhir.instance.model.ValueSet.ConceptDefinitionComponent theNext, String theCode, List<VersionIndependentConcept> theListToPopulate) {
		boolean foundCodeInChild = false;
		for (org.hl7.fhir.instance.model.ValueSet.ConceptDefinitionComponent nextChild : theNext.getConcept()) {
			foundCodeInChild |= addTreeIfItContainsCode(theSystemString, nextChild, theCode, theListToPopulate);
		}

		if (theCode.equals(theNext.getCode()) || foundCodeInChild) {
			theListToPopulate.add(new VersionIndependentConcept(theSystemString, theNext.getCode()));
			return true;
		}

		return false;
	}

	@Override
	protected IIdType createOrUpdateCodeSystem(CodeSystem theCodeSystemResource) {
		throw new UnsupportedOperationException();
	}

	@Override
	protected void createOrUpdateConceptMap(ConceptMap theNextConceptMap) {
		throw new UnsupportedOperationException();
	}

	@Override
	protected void createOrUpdateValueSet(ValueSet theValueSet) {
		throw new UnsupportedOperationException();
	}

	@Override
	protected CodeSystem getCodeSystemFromContext(String theSystem) {
		return null;
	}

	@Override
	public IBaseResource expandValueSet(IBaseResource theValueSetToExpand) {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<VersionIndependentConcept> expandValueSet(String theValueSet) {
		throw new UnsupportedOperationException();
	}

	private void findCodesAbove(org.hl7.fhir.instance.model.ValueSet theSystem, String theSystemString, String theCode, List<VersionIndependentConcept> theListToPopulate) {
		List<org.hl7.fhir.instance.model.ValueSet.ConceptDefinitionComponent> conceptList = theSystem.getCodeSystem().getConcept();
		for (org.hl7.fhir.instance.model.ValueSet.ConceptDefinitionComponent next : conceptList) {
			addTreeIfItContainsCode(theSystemString, next, theCode, theListToPopulate);
		}
	}

	@Override
	public List<VersionIndependentConcept> findCodesAboveUsingBuiltInSystems(String theSystem, String theCode) {
		ArrayList<VersionIndependentConcept> retVal = new ArrayList<>();
		org.hl7.fhir.instance.model.ValueSet system = myValidationSupport.fetchCodeSystem(myContext, theSystem);
		if (system != null) {
			findCodesAbove(system, theSystem, theCode, retVal);
		}
		return retVal;
	}

	private void findCodesBelow(org.hl7.fhir.instance.model.ValueSet theSystem, String theSystemString, String theCode, List<VersionIndependentConcept> theListToPopulate) {
		List<org.hl7.fhir.instance.model.ValueSet.ConceptDefinitionComponent> conceptList = theSystem.getCodeSystem().getConcept();
		findCodesBelow(theSystemString, theCode, theListToPopulate, conceptList);
	}

	private void findCodesBelow(String theSystemString, String theCode, List<VersionIndependentConcept> theListToPopulate, List<org.hl7.fhir.instance.model.ValueSet.ConceptDefinitionComponent> conceptList) {
		for (org.hl7.fhir.instance.model.ValueSet.ConceptDefinitionComponent next : conceptList) {
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
		org.hl7.fhir.instance.model.ValueSet system = myValidationSupport.fetchCodeSystem(myContext, theSystem);
		if (system != null) {
			findCodesBelow(system, theSystem, theCode, retVal);
		}
		return retVal;
	}

}
