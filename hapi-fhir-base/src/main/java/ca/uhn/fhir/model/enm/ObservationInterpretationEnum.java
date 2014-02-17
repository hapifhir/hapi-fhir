package ca.uhn.fhir.model.enm;

import ca.uhn.fhir.model.api.Description;
import ca.uhn.fhir.model.api.EnumeratedCodeValue;
import ca.uhn.fhir.model.api.EnumeratedValueSet;
import ca.uhn.fhir.model.api.ICodeEnum;

@EnumeratedValueSet(tableId = 117, name = "observation-interpretation")
public enum ObservationInterpretationEnum implements ICodeEnum {

	@EnumeratedCodeValue(value = "<", system = "http://hl7.org/fhir/v2/0078")
	@Description("Below absolute low-off instrument scale")
	BELOW_ABSOLUTE_LOW,

	@EnumeratedCodeValue(value = ">", system = "http://hl7.org/fhir/v2/0078")
	@Description("Above absolute high-off instrument scale")
	ABOVE_ABSOLUTE_HIGH,

	@EnumeratedCodeValue(value = "A", system = "http://hl7.org/fhir/v2/0078")
	@Description("Abnormal (applies to non-numeric results)")
	A,

	@EnumeratedCodeValue(value = "AA", system = "http://hl7.org/fhir/v2/0078")
	@Description("Very abnormal (applies to non-numeric units, analogous to panic limits for numeric units)")
	AA,

	@EnumeratedCodeValue(value = "AC", system = "http://hl7.org/fhir/v2/0078")
	@Description("Anti-complementary substances present")
	AC,

	@EnumeratedCodeValue(value = "B", system = "http://hl7.org/fhir/v2/0078")
	@Description("Better-use when direction not relevant")
	B,

	@EnumeratedCodeValue(value = "D", system = "http://hl7.org/fhir/v2/0078")
	@Description("Significant change down")
	D,

	@EnumeratedCodeValue(value = "DET", system = "http://hl7.org/fhir/v2/0078")
	@Description("Detected")
	DET,

	@EnumeratedCodeValue(value = "H", system = "http://hl7.org/fhir/v2/0078")
	@Description("Above high normal")
	H,

	@EnumeratedCodeValue(value = "HH", system = "http://hl7.org/fhir/v2/0078")
	@Description("Above upper panic limits")
	HH,

	@EnumeratedCodeValue(value = "I", system = "http://hl7.org/fhir/v2/0078")
	@Description("Intermediate. Indicates for microbiology susceptibilities only.")
	I,

	@EnumeratedCodeValue(value = "IND", system = "http://hl7.org/fhir/v2/0078")
	@Description("Indeterminate")
	IND,

	@EnumeratedCodeValue(value = "L", system = "http://hl7.org/fhir/v2/0078")
	@Description("Below low normal")
	L,

	@EnumeratedCodeValue(value = "LL", system = "http://hl7.org/fhir/v2/0078")
	@Description("Below lower panic limits")
	LL, @EnumeratedCodeValue(value = "MS", system = "http://hl7.org/fhir/v2/0078")
	@Description("Moderately susceptible. Indicates for microbiology susceptibilities only.")
	MS,

	@EnumeratedCodeValue(value = "N", system = "http://hl7.org/fhir/v2/0078")
	@Description("Normal (applies to non-numeric results)")
	N,

	@EnumeratedCodeValue(value = "ND", system = "http://hl7.org/fhir/v2/0078")
	@Description("Not Detected")
	ND,

	@EnumeratedCodeValue(value = "NEG", system = "http://hl7.org/fhir/v2/0078")
	@Description("Negative")
	NEG, @EnumeratedCodeValue(value = "NR", system = "http://hl7.org/fhir/v2/0078")
	@Description("Non-reactive")
	NR,

	@EnumeratedCodeValue(value = "POS", system = "http://hl7.org/fhir/v2/0078")
	@Description("Positive")
	POS,

	@EnumeratedCodeValue(value = "QCF", system = "http://hl7.org/fhir/v2/0078")
	@Description("Quality Control Failure")
	QCF,

	@EnumeratedCodeValue(value = "R", system = "http://hl7.org/fhir/v2/0078")
	@Description("Resistant. Indicates for microbiology susceptibilities only.")
	R,

	@EnumeratedCodeValue(value = "RR", system = "http://hl7.org/fhir/v2/0078")
	@Description("Reactive")
	RR,

	@EnumeratedCodeValue(value = "S", system = "http://hl7.org/fhir/v2/0078")
	@Description("Susceptible. Indicates for microbiology susceptibilities only.")
	S,

	@EnumeratedCodeValue(value = "TOX", system = "http://hl7.org/fhir/v2/0078")
	@Description("Cytotoxic substance present")
	TOX,

	@EnumeratedCodeValue(value = "U", system = "http://hl7.org/fhir/v2/0078")
	@Description("Significant change up")
	U,

	@EnumeratedCodeValue(value = "VS", system = "http://hl7.org/fhir/v2/0078")
	@Description("Very susceptible. Indicates for microbiology susceptibilities only.")
	VS,

	@EnumeratedCodeValue(value = "W", system = "http://hl7.org/fhir/v2/0078")
	@Description("Worse-use when direction not relevant")
	W,

	@EnumeratedCodeValue(value = "WR", system = "http://hl7.org/fhir/v2/0078")
	@Description("Weakly reactive")
	WR,

	@EnumeratedCodeValue(value = "null", system = "http://hl7.org/fhir/v2/0078")
	@Description("No range defined, or normal ranges don't apply")
	NULL
}
