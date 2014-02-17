package ca.uhn.fhir.model.datatype;

import ca.uhn.fhir.model.api.BasePrimitiveDatatype;
import ca.uhn.fhir.model.api.DatatypeDefinition;
import ca.uhn.fhir.model.api.ICodeEnum;

@DatatypeDefinition(name="code")
public class CodeDt<T extends ICodeEnum> extends BasePrimitiveDatatype {

}
