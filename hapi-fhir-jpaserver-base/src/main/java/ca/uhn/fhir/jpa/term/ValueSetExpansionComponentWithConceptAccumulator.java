package ca.uhn.fhir.jpa.term;

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
import ca.uhn.fhir.jpa.entity.TermConceptDesignation;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.jpa.term.ex.ExpansionTooCostlyException;
import ca.uhn.fhir.model.api.annotation.Block;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import org.hl7.fhir.r4.model.StringType;
import org.hl7.fhir.r4.model.ValueSet;

import javax.annotation.Nullable;
import java.util.Collection;

@Block()
public class ValueSetExpansionComponentWithConceptAccumulator extends ValueSet.ValueSetExpansionComponent implements IValueSetConceptAccumulator {
	private final int myMaxCapacity;
	private final FhirContext myContext;
	private int myConceptsCount;

	/**
	 * Constructor
	 *
	 * @param theMaxCapacity The maximum number of results this accumulator will accept before throwing
	 *                       an {@link InternalErrorException}
	 */
	ValueSetExpansionComponentWithConceptAccumulator(FhirContext theContext, int theMaxCapacity) {
		myContext = theContext;
		myMaxCapacity = theMaxCapacity;
		myConceptsCount = 0;
	}

	@Nullable
	@Override
	public Integer getCapacityRemaining() {
		return myMaxCapacity - myConceptsCount;
	}

	@Override
	public void addMessage(String theMessage) {
		addExtension()
			.setUrl(JpaConstants.EXT_VALUESET_EXPANSION_MESSAGE)
			.setValue(new StringType(theMessage));
	}

	@Override
	public void includeConcept(String theSystem, String theCode, String theDisplay) {
		incrementConceptsCount();
		ValueSet.ValueSetExpansionContainsComponent contains = this.addContains();
		contains.setSystem(theSystem);
		contains.setCode(theCode);
		contains.setDisplay(theDisplay);
	}

	@Override
	public void includeConceptWithDesignations(String theSystem, String theCode, String theDisplay, Collection<TermConceptDesignation> theDesignations) {
		incrementConceptsCount();
		ValueSet.ValueSetExpansionContainsComponent contains = this.addContains();
		contains.setSystem(theSystem);
		contains.setCode(theCode);
		contains.setDisplay(theDisplay);
		if (theDesignations != null) {
			for (TermConceptDesignation termConceptDesignation : theDesignations) {
				contains
					.addDesignation()
					.setValue(termConceptDesignation.getValue())
					.setLanguage(termConceptDesignation.getLanguage())
					.getUse()
					.setSystem(termConceptDesignation.getUseSystem())
					.setCode(termConceptDesignation.getUseCode())
					.setDisplay(termConceptDesignation.getUseDisplay());
			}
		}
	}

	@Override
	public void excludeConcept(String theSystem, String theCode) {
		this
			.getContains()
			.removeIf(t ->
				theSystem.equals(t.getSystem()) &&
					theCode.equals(t.getCode()));
	}

	private void incrementConceptsCount() {
		if (++myConceptsCount > myMaxCapacity) {
			String msg = myContext.getLocalizer().getMessage(BaseTermReadSvcImpl.class, "expansionTooLarge", myMaxCapacity);
			throw new ExpansionTooCostlyException(msg);
		}
	}
}
