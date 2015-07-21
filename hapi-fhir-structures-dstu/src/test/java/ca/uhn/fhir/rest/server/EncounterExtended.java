package ca.uhn.fhir.rest.server;

import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.Extension;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.dstu.composite.CodingDt;
import ca.uhn.fhir.model.dstu.composite.IdentifierDt;
import ca.uhn.fhir.model.dstu.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.util.ElementUtil;

/**
 * HAPI/FHIR <b>DiagnosticReport</b> 
 * 
 * <p>
 * <b>Definition:</b>Same as definition for encounter but has additional data fields that are specific to UHN 
 * </p>
 * 
 * <p>
 * <b>Requirements:</b>
 * 
 * </p>
 */
@ResourceDef(name = "Encounter", profile = "http://fhir.connectinggta.ca/Profile/encounter", id = "cgtaencounter")
public class EncounterExtended extends ca.uhn.fhir.model.dstu.resource.Encounter {

	@Child(name = "preconversionEncounterIdentifier", type = IdentifierDt.class, order = 0, min = 0, max = 1)
	@Extension(url = "http://fhir.connectinggta.ca/Profile/encounter#preconversionEncounterIdentifier", isModifier = false, definedLocally = true)
	@Description(shortDefinition = "preconversionEncounterIdentifier", formalDefinition = "The facility defined identifier of the encounter belonging to the subject prior to an" +
			" encounter type conversion")
	private IdentifierDt preconversionEncounterIdentifier;
	
    @Child(name = "rehabStream", type = StringDt.class, order = 1, min = 0, max = 1)
    @Extension(url = "http://fhir.connectinggta.ca/Profile/encounter#rehabStream", isModifier = false, definedLocally = true)
    @Description(shortDefinition = "rehabStream", formalDefinition = "The name of the stream assigned to a patient from the Rehabilitation program the patient is classified under. " +
    		"Currently only used for Toronto Rehab encounters")
    private StringDt rehabStream;
    
    @Child(name = "rehabSubStream", type = StringDt.class, order = 2, min = 0, max = 1)
    @Extension(url = "http://fhir.connectinggta.ca/Profile/encounter#rehabSubStream", isModifier = false, definedLocally = true)
    @Description(shortDefinition = "rehabSubStream", formalDefinition = "Further classification of the Encounter.stream field, details more specifically the type of care being received" +
    		" within the Rehabilitation program. Currently only used for Toronto Rehab encounters")
    private StringDt rehabSubStream;
    
    
    @Child(name = "bedAssignmentReason", type = StringDt.class, order = 3, min = 0, max = 1)
    @Extension(url = "http://fhir.connectinggta.ca/Profile/encounter#bedAssignmentReason", isModifier = false, definedLocally = true)
    @Description(shortDefinition = "bedAssignmentReason", formalDefinition = "The reason why the room assignment was made to the patient.")
    private StringDt bedAssignmentReason;    
    
    
    @Child(name = "rehabCentreName", type = StringDt.class, order = 4, min = 0, max = 1)
    @Extension(url = "http://fhir.connectinggta.ca/Profile/encounter#rehabCentreName", isModifier = false, definedLocally = true)
    @Description(shortDefinition = "rehabCentreName", formalDefinition = "Name of the rehabilitation centre the patient is admitted into. " +
    		"Currently only used for Toronto Rehab encounters")
    private StringDt rehabCentreName;        
    
    
    @Child(name = "rehabRecheck", type = CodingDt.class, order = 5, min = 0, max = 1)
    @Extension(url = "http://fhir.connectinggta.ca/Profile/encounter#rehabRecheck", isModifier = false, definedLocally = true)
    @Description(shortDefinition = "rehabRecheck", formalDefinition = "A coded flag used for outpatient encounters from Toronto Rehab")
    private CodingDt rehabRecheck;
    
    
    @Child(name = "rehabEncounterType", type = CodingDt.class, order = 6, min = 0, max = 1)
    @Extension(url = "http://fhir.connectinggta.ca/Profile/encounter#rehabEncounterType", isModifier = false, definedLocally = true)
    @Description(shortDefinition = "rehabEncounterType", formalDefinition = "Refers to the type of care being provided at the corresponding Toronto Rehab site.")
    private CodingDt rehabEncounterType;
    
    
    
    @Child(name = "facilityLocation", type = {Location.class}, order = 7, min = 0, max = 1)
    @Extension(url = "http://fhir.connectinggta.ca/Profile/diagnosticreport#facilityLocation", isModifier = false, definedLocally = true)
    @Description(shortDefinition = "facilityLocation", formalDefinition = "Reference to the location of the facility the encounter belongs to")
    private ResourceReferenceDt facilityLocation;


    /**
     *
     *@return Returns the preconversionEncounterIdentifier. 
     */
    public IdentifierDt getPreconversionEncounterIdentifier() {
        
        if (preconversionEncounterIdentifier == null) {
            preconversionEncounterIdentifier = new IdentifierDt();
        }
        
        return preconversionEncounterIdentifier;
    }


    /** 
     * 
     * @param thePreconversionEncounterIdentifier The preconversionEncounterIdentifier to set.
     */
    public void setPreconversionEncounterIdentifier(IdentifierDt thePreconversionEncounterIdentifier) {
        preconversionEncounterIdentifier = thePreconversionEncounterIdentifier;
    }


    /**
     *
     *@return Returns the rehabStream. 
     */
    public StringDt getRehabStream() {
        
        if (rehabStream == null) {
            rehabStream = new StringDt();
        }
        
        return rehabStream;
    }


    /** 
     * 
     * @param theRehabStream The rehabStream to set.
     */
    public void setRehabStream(StringDt theRehabStream) {
        rehabStream = theRehabStream;
    }


    /**
     *
     *@return Returns the rehabSubStream. 
     */
    public StringDt getRehabSubStream() {
        
        if (rehabSubStream == null) {
            rehabSubStream = new StringDt();
        }
        
        return rehabSubStream;
    }


    /** 
     * 
     * @param theRehabSubStream The rehabSubStream to set.
     */
    public void setRehabSubStream(StringDt theRehabSubStream) {
        rehabSubStream = theRehabSubStream;
    }


    /**
     *
     *@return Returns the bedAssignmentReason. 
     */
    public StringDt getBedAssignmentReason() {
        
        if (bedAssignmentReason == null) {
            bedAssignmentReason = new StringDt();
        }
        
        return bedAssignmentReason;
    }


    /** 
     * 
     * @param theBedAssignmentReason The bedAssignmentReason to set.
     */
    public void setBedAssignmentReason(StringDt theBedAssignmentReason) {
        bedAssignmentReason = theBedAssignmentReason;
    }


    /**
     *
     *@return Returns the rehabCentreName. 
     */
    public StringDt getRehabCentreName() {
        
        if (rehabCentreName == null) {
            rehabCentreName = new StringDt();
        }
        
        return rehabCentreName;
    }


    /** 
     * 
     * @param theRehabCentreName The rehabCentreName to set.
     */
    public void setRehabCentreName(StringDt theRehabCentreName) {
        rehabCentreName = theRehabCentreName;
    }


    /**
     *
     *@return Returns the rehabRecheck. 
     */
    public CodingDt getRehabRecheck() {
        
        if (rehabRecheck == null) {
            rehabRecheck = new CodingDt();
        }
        
        
        return rehabRecheck;
    }


    /** 
     * 
     * @param theRehabRecheck The rehabRecheck to set.
     */
    public void setRehabRecheck(CodingDt theRehabRecheck) {
        rehabRecheck = theRehabRecheck;
    }


    /**
     *
     *@return Returns the rehabEncounterType. 
     */
    public CodingDt getRehabEncounterType() {
        
        if (rehabEncounterType == null) {
            rehabEncounterType = new CodingDt();
        }
        
        
        return rehabEncounterType;
    }


    /** 
     * 
     * @param theRehabEncounterType The rehabEncounterType to set.
     */
    public void setRehabEncounterType(CodingDt theRehabEncounterType) {
        rehabEncounterType = theRehabEncounterType;
    }
    
    
    /**
     * 
     * @return ...
     */
    public ResourceReferenceDt getFacilityLocation() {
        if (facilityLocation == null) {
            facilityLocation = new ResourceReferenceDt();
        }
        return facilityLocation;
    }

    
    /**
     * 
     * @param theFacilityLocation ...
     */
    public void setFacilityLocation(ResourceReferenceDt theFacilityLocation) {
        facilityLocation = theFacilityLocation;
    }    
    
    
    
    /**
     * It is important to override the isEmpty() method, adding a check for any
     * newly added fields.
     */
    @Override
    public boolean isEmpty() {
        return super.isEmpty() && ElementUtil.isEmpty(preconversionEncounterIdentifier, rehabStream, rehabSubStream, bedAssignmentReason, rehabCentreName, rehabRecheck, rehabEncounterType, facilityLocation);
    }        
	
	
	



}