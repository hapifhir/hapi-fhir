package ca.uhn.fhir.model.enm;

import ca.uhn.fhir.model.api.ICodeEnum;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.EnumeratedCodeValue;
import ca.uhn.fhir.model.api.annotation.CodeTableDef;

@CodeTableDef(name="identifier-use", tableId=13)
public enum IdentifierUseEnum implements ICodeEnum {
	@EnumeratedCodeValue("usual")
	@Description("the identifier recommended for display and use in real-world interactions.")
	USUAL,
	
	@EnumeratedCodeValue("official")		
	@Description("the identifier considered to be most trusted for the identification of this item.")
	OFFICIAL,
	
	@EnumeratedCodeValue("temp")		
	@Description("A temporary identifier.")
	TEMP,
	
	@EnumeratedCodeValue("secondary")		
	@Description("An identifier that was assigned in secondary use - it serves to identify the object in a relative context, but cannot be consistently assigned to the same object again in a different context.")
	SECONDARY
}
