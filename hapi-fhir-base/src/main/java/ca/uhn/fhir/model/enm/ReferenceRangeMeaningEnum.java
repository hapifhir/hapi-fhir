package ca.uhn.fhir.model.enm;

import ca.uhn.fhir.model.api.ICodeEnum;
import ca.uhn.fhir.model.api.annotation.CodeTableDef;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.EnumeratedCodeValue;

@CodeTableDef(tableId = 119, name = "referencerange-meaning")
public enum ReferenceRangeMeaningEnum implements ICodeEnum {

	@EnumeratedCodeValue(value = "normal", system = "http://hl7.org/fhir/referencerange-meaning")
	@Description("Normal Range")
	NORMAL,

	@EnumeratedCodeValue(value = "recommended", system = "http://hl7.org/fhir/referencerange-meaning")
	@Description("Recommended Range")
	RECOMMENDED,

	@EnumeratedCodeValue(value = "treatment", system = "http://hl7.org/fhir/referencerange-meaning")
	@Description("Treatment Range")
	TREATMENT,

	@EnumeratedCodeValue(value = "therapeutic", system = "http://hl7.org/fhir/referencerange-meaning")
	@Description("Therapeutic Desired Level")
	THERAPEUTIC,

	@EnumeratedCodeValue(value = "pre", system = "http://hl7.org/fhir/referencerange-meaning")
	@Description("Pre Therapeutic Desired Level")
	PRE,

	@EnumeratedCodeValue(value = "post", system = "http://hl7.org/fhir/referencerange-meaning")
	@Description("Post Therapeutic Desired Level")
	POST,

	@EnumeratedCodeValue(value = "pre-puberty", system = "http://hl7.org/fhir/referencerange-meaning")
	@Description("Pre-Puberty")
	PRE_PUBERTY,

	@EnumeratedCodeValue(value = "follicular", system = "http://hl7.org/fhir/referencerange-meaning")
	@Description("Follicular Stage")
	FOLLICULAR,

	@EnumeratedCodeValue(value = "midcycle", system = "http://hl7.org/fhir/referencerange-meaning")
	@Description("MidCycle")
	MIDCYCLE,

	@EnumeratedCodeValue(value = "luteal", system = "http://hl7.org/fhir/referencerange-meaning")
	@Description("Luteal")
	LUTEAL,

	@EnumeratedCodeValue(value = "postmenopausal", system = "http://hl7.org/fhir/referencerange-meaning")
	@Description("Post-Menopause")
	POSTMENOPAUSAL

}
