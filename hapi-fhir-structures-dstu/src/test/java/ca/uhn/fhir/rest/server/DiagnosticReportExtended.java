package ca.uhn.fhir.rest.server;

import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.Extension;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.dstu.composite.ResourceReferenceDt;
import ca.uhn.fhir.util.ElementUtil;

/**
 * HAPI/FHIR <b>DiagnosticReport</b> 
 * 
 * <p>
 * <b>Definition:</b>Same as definition for diagnostic report but has additional data associated to the diagnostic report 
 * </p>
 * 
 * <p>
 * <b>Requirements:</b>
 * 
 * </p>
 */
@ResourceDef(name = "DiagnosticReport", profile = "http://fhir.connectinggta.ca/Profile/diagnosticreport", id = "cgtadiagnosticreport")
public class DiagnosticReportExtended extends ca.uhn.fhir.model.dstu.resource.DiagnosticReport {

	@Child(name = "encounter", type = {EncounterExtended.class} /*ResourceReferenceDt.class*/, order = 0, min = 0, max = 1)
	@Extension(url = "http://fhir.connectinggta.ca/Profile/diagnosticreport#encounter", isModifier = false, definedLocally = true)
	@Description(shortDefinition = "encounter", formalDefinition = "Reference to the Encounter associated to the DiagnosticReport")
	private ResourceReferenceDt encounter;


	public ResourceReferenceDt getEncounter() {
		if (encounter == null) {
			encounter = new ResourceReferenceDt();
		}
		return encounter;
	}

	public void setEncounter(ResourceReferenceDt theEncounter) {
		encounter = theEncounter;
	}
	
	
    /**
     * It is important to override the isEmpty() method, adding a check for any
     * newly added fields.
     */
    @Override
    public boolean isEmpty() {
        return super.isEmpty() && ElementUtil.isEmpty(encounter);
    }    
    
	

}