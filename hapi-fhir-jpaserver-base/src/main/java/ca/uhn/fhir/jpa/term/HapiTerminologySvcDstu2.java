package ca.uhn.fhir.jpa.term;

import java.util.List;

import org.hl7.fhir.instance.hapi.validation.IValidationSupport;
import org.springframework.beans.factory.annotation.Autowired;

public class HapiTerminologySvcDstu2 extends BaseHapiTerminologySvc {

	@Autowired
	private IValidationSupport myValidationSupport;


	@Override
	public List<VersionIndependentConcept> expandValueSet(String theValueSet) {
		throw new UnsupportedOperationException();
	}

}
