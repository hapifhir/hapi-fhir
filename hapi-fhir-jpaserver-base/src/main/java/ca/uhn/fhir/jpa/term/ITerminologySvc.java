package ca.uhn.fhir.jpa.term;

import java.util.Set;

import ca.uhn.fhir.jpa.entity.TermCodeSystemVersion;
import ca.uhn.fhir.jpa.entity.TermConcept;

public interface ITerminologySvc {

	void storeNewCodeSystemVersion(String theSystemUri, TermCodeSystemVersion theCodeSytem);

	Set<TermConcept> findCodesBelow(Long theCodeSystemResourcePid, Long theCodeSystemResourceVersion, String theCode);
	
}
