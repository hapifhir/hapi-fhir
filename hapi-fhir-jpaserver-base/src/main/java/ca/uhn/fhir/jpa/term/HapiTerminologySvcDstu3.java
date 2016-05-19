package ca.uhn.fhir.jpa.term;

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
