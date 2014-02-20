package ca.uhn.fhir.model.enm;

import ca.uhn.fhir.model.api.ICodeEnum;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.EnumeratedCodeValue;
import ca.uhn.fhir.model.api.annotation.CodeTableDef;

@CodeTableDef(tableId = 371, name = "observation-relationshiptype")
public enum ObservationRelationshipTypeEnum implements ICodeEnum {

	@EnumeratedCodeValue("has-component")
	@Description("The target observation is a component of this observation (e.g. Systolic and Diastolic Blood Pressure).")
	HAS_COMPONENT,

	@EnumeratedCodeValue("has-member")
	@Description("This observation is a group observation (e.g. a battery, a panel of tests, a set of vital sign measurements) that includes the target as a member of the group.")
	HAS_MEMBER,

	@EnumeratedCodeValue("derived-from")
	@Description("The target observation is part of the information from which this observation value is derived (e.g. calculated anion gap, Apgar score).")
	DERIVED_FROM,

	@EnumeratedCodeValue("sequel-to")
	@Description("This observation follows the target observation (e.g. timed tests such as Glucose Tolerance Test).")
	SEQUEL_TO,

	@EnumeratedCodeValue("replaces")
	@Description("This observation replaces a previous observation (i.e. a revised value). The target observation is now obsolete.")
	REPLACES,

	@EnumeratedCodeValue("qualified-by")
	@Description("The value of the target observation qualifies (refines) the semantics of the source observation (e.g. a lipaemia measure target from a plasma measure).")
	QUALIFIED_BY,

	@EnumeratedCodeValue("interfered-by")
	@Description("The value of the target observation interferes (degardes quality, or prevents valid observation) with the semantics of the source observation (e.g. a hemolysis measure target from a plasma potassium measure which has no value).")
	INTERFERED_BY
}
