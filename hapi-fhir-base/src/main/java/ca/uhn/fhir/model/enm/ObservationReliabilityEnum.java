package ca.uhn.fhir.model.enm;

import ca.uhn.fhir.model.api.ICodeEnum;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.EnumeratedCodeValue;
import ca.uhn.fhir.model.api.annotation.CodeTableDef;

@CodeTableDef(tableId=118, name="observation-reliability")
public enum ObservationReliabilityEnum implements ICodeEnum {
	
	@Description("The result has no reliability concerns.")
    @EnumeratedCodeValue("ok")
    OK,
    
	@Description("An early estimate of value; measurement is still occurring.")
    @EnumeratedCodeValue("ongoing")
    ONGOING,
    
	@Description("An early estimate of value; processing is still occurring.")
    @EnumeratedCodeValue("early")
    EARLY,
    
	@Description("The observation value should be treated with care.")
    @EnumeratedCodeValue("questionable")
    QUESTIONABLE,
    
	@Description("The result has been generated while calibration is occurring.")
    @EnumeratedCodeValue("calibrating")
    CALIBRATING,
    
	@Description("The observation could not be completed because of an error.")
    @EnumeratedCodeValue("error")
	ERROR,
	
	@Description("No observation value was available.")
    @EnumeratedCodeValue("unknown")
	UNKNOWN
	
}
