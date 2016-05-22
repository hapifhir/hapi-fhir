package ca.uhn.fhir.jpa.term;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2016 University Health Network
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

import java.util.ArrayList;
import java.util.List;

import org.hl7.fhir.dstu3.model.CodeSystem;
import org.hl7.fhir.dstu3.model.ValueSet;
import org.hl7.fhir.dstu3.model.ValueSet.ValueSetExpansionContainsComponent;
import org.hl7.fhir.dstu3.terminologies.ValueSetExpander;
import org.hl7.fhir.dstu3.terminologies.ValueSetExpander.ValueSetExpansionOutcome;
import org.hl7.fhir.dstu3.utils.IWorkerContext;
import org.springframework.beans.factory.annotation.Autowired;

import ca.uhn.fhir.jpa.entity.TermCodeSystemVersion;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;

public class HapiTerminologySvcDstu3 extends BaseHapiTerminologySvc {
	
	@Autowired
	private IWorkerContext myWorkerContext;

	@Autowired
	private ValueSetExpander myValueSetExpander;
	
	@Override
	public List<VersionIndependentConcept> expandValueSet(String theValueSet) {
		ValueSet source = new ValueSet();
		source.getCompose().addImport(theValueSet);
		try {
			ArrayList<VersionIndependentConcept> retVal = new ArrayList<VersionIndependentConcept>();
			
			ValueSetExpansionOutcome outcome = myValueSetExpander.expand(source);
			for (ValueSetExpansionContainsComponent next : outcome.getValueset().getExpansion().getContains()) {
				retVal.add(new VersionIndependentConcept(next.getSystem(), next.getCode()));
			}
			
			return retVal;
			
		} catch (Exception e) {
			throw new InternalErrorException(e);
		}
		
	}

	@Override
	public void storeNewCodeSystemVersion(String theSystem, TermCodeSystemVersion theCodeSystemVersion) {
		CodeSystem cs = new org.hl7.fhir.dstu3.model.CodeSystem();
		
	}

}
