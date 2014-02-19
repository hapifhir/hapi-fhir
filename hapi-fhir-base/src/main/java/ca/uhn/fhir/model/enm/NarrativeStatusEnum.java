package ca.uhn.fhir.model.enm;

import ca.uhn.fhir.model.api.ICodeEnum;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.EnumeratedCodeValue;
import ca.uhn.fhir.model.api.annotation.CodeTableDef;

@CodeTableDef(tableId = 28, name = "narrative-status")
public enum NarrativeStatusEnum implements ICodeEnum {

	@EnumeratedCodeValue(value = "generated")
	@Description("The contents of the narrative are entirely generated from the structured data in the resource.")
	GENERATED,
	
	@EnumeratedCodeValue(value = "extensions")
	@Description("The contents of the narrative are entirely generated from the structured data in the resource and some of the content is generated from extensions.")
	EXTENSIONS,
	
	@EnumeratedCodeValue(value = "additional")
	@Description("The contents of the narrative contain additional information not found in the structured data.")
	ADDITIONAL,
	
	@EnumeratedCodeValue(value = "empty")
	@Description("the contents of the narrative are some equivalent of \"No human-readable text provided for this resource\".")
	EMPTY
}
