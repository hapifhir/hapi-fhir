package ca.uhn.fhir.model.enm;

import ca.uhn.fhir.model.api.ICodeEnum;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.EnumeratedCodeValue;
import ca.uhn.fhir.model.api.annotation.CodeTableDef;

@CodeTableDef(tableId=7, name="observation-status")
public enum ObservationStatusEnum implements ICodeEnum {
	
    @Description("The existence of the observation is registered, but there is no result yet available")
	@EnumeratedCodeValue("registered") 
	REGISTERED,
	
	@Description("This is an initial or interim observation: data may be incomplete or unverified")
    @EnumeratedCodeValue("interim")
	INTERIM,
	
	@Description("The observation is complete and verified by an authorised person")
    @EnumeratedCodeValue("final")
	FINAL,
	
	@Description("The observation has been modified subsequent to being Final, and is complete and verified by an authorised person")
    @EnumeratedCodeValue("amended") 
	AMENDED,
	
	@Description("The observation is unavailable because the measurement was not started or not completed (also sometimes called \"aborted\")")
    @EnumeratedCodeValue("cancelled") 
	CANCELLED,
	
	@Description("The observation has been withdrawn following previous Final release")
    @EnumeratedCodeValue("withdrawn")
	WITHDRAWN

	
}
