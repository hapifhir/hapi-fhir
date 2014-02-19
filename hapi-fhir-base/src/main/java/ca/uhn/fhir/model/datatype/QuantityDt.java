package ca.uhn.fhir.model.datatype;

import ca.uhn.fhir.model.api.BaseCompositeDatatype;
import ca.uhn.fhir.model.api.annotation.Constraint;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.DatatypeDef;
import ca.uhn.fhir.model.enm.QuantityComparatorEnum;

@DatatypeDef(name="Quantity")
public class QuantityDt extends BaseCompositeDatatype {

	@Child(name="value",order=0, min=0, max=1)
	private DecimalDt myValue;
	
	@Child(name="comparator",order=1, min=0, max=1)
	private CodeDt<QuantityComparatorEnum> myComparator;
	
	@Child(name="units",order=2, min=0, max=1)
	private StringDt myUnits;
	
	@Child(name="system",order=3, min=0, max=1)
	@Constraint(coRequirements= {"code"})
	private UriDt mySystem;
	
	@Child(name="code",order=4, min=0, max=1)
	@Constraint(coRequirements= {"system"})
	private CodeDt<?> myCode;

	public DecimalDt getValue() {
		return myValue;
	}

	public void setValue(DecimalDt theValue) {
		myValue = theValue;
	}

	public CodeDt<QuantityComparatorEnum> getComparator() {
		return myComparator;
	}

	public void setComparator(CodeDt<QuantityComparatorEnum> theComparator) {
		myComparator = theComparator;
	}

	public StringDt getUnits() {
		return myUnits;
	}

	public void setUnits(StringDt theUnits) {
		myUnits = theUnits;
	}

	public UriDt getSystem() {
		return mySystem;
	}

	public void setSystem(UriDt theSystem) {
		mySystem = theSystem;
	}

	public CodeDt<?> getCode() {
		return myCode;
	}

	public void setCode(CodeDt<?> theCode) {
		myCode = theCode;
	}
	
}
