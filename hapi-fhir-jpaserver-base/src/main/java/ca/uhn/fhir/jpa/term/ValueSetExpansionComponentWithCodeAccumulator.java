package ca.uhn.fhir.jpa.term;

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
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import ca.uhn.fhir.jpa.entity.TermConceptDesignation;
import org.hl7.fhir.r4.model.ValueSet;

import java.util.Collection;

public class ValueSetExpansionComponentWithCodeAccumulator extends ValueSet.ValueSetExpansionComponent implements IValueSetCodeAccumulator {
	@Override
	public void includeCode(String theSystem, String theCode, String theDisplay) {
		ValueSet.ValueSetExpansionContainsComponent contains = this.addContains();
		contains.setSystem(theSystem);
		contains.setCode(theCode);
		contains.setDisplay(theDisplay);
	}

	@Override
	public void includeCodeWithDesignations(String theSystem, String theCode, String theDisplay, Collection<TermConceptDesignation> theDesignations) {
		ValueSet.ValueSetExpansionContainsComponent contains = this.addContains();
		contains.setSystem(theSystem);
		contains.setCode(theCode);
		contains.setDisplay(theDisplay);
		if (theDesignations != null) {
			for (TermConceptDesignation termConceptDesignation : theDesignations) {
				contains
					.addDesignation()
					.setValue(termConceptDesignation.getValue())
					.getUse()
					.setSystem(termConceptDesignation.getUseSystem())
					.setCode(termConceptDesignation.getUseCode())
					.setDisplay(termConceptDesignation.getUseDisplay());
			}
		}
	}

	@Override
	public void excludeCode(String theSystem, String theCode) {
		this
			.getContains()
			.removeIf(t ->
				theSystem.equals(t.getSystem()) &&
					theCode.equals(t.getCode()));
	}
}
