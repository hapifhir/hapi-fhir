package ca.uhn.fhir.rest.param;

import java.util.Date;

import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.model.dstu.valueset.QuantityCompararatorEnum;
import ca.uhn.fhir.model.primitive.DateTimeDt;

public class QualifiedDateParam extends DateTimeDt implements IQueryParameterType {

	private QuantityCompararatorEnum myComparator;

	/**
	 * Constructor
	 */
	public QualifiedDateParam() {
	}
	
	/**
	 * Constructor
	 */
	public QualifiedDateParam(QuantityCompararatorEnum theComparator, Date theDate) {
		myComparator = theComparator;
		setValue(theDate);
	}

	/**
	 * Constructor
	 */
	public QualifiedDateParam(QuantityCompararatorEnum theComparator, String theDate) {
		myComparator = theComparator;
		setValueAsString(theDate);
	}

	/**
	 * Returns the comparator, or <code>null</code> if none has been set
	 */
	public QuantityCompararatorEnum getComparator() {
		return myComparator;
	}

	@Override
	public String getValueAsQueryToken() {
		if (myComparator != null && getValue() != null) {
			return myComparator.getCode() + getValueAsString();
		} else if (myComparator == null && getValue() != null) {
			return getValueAsString();
		}
		return "";
	}

	public void setComparator(QuantityCompararatorEnum theComparator) {
		myComparator = theComparator;
	}

	@Override
	public void setValueAsQueryToken(String theParameter) {
		if (theParameter.length() < 2) {
			throw new IllegalArgumentException("Invalid qualified date parameter: "+theParameter);
		}

		char char0 = theParameter.charAt(0);
		char char1 = theParameter.charAt(1);
		if (Character.isDigit(char0)) {
			setValueAsString(theParameter);
		} else {
			int dateStart = 2;
			if (Character.isDigit(char1)) {
				dateStart = 1;
			}
			
			String comparatorString = theParameter.substring(0, dateStart);
			QuantityCompararatorEnum comparator = QuantityCompararatorEnum.VALUESET_BINDER.fromCodeString(comparatorString);
			if (comparator==null) {
				throw new IllegalArgumentException("Invalid date qualifier: "+comparatorString);
			}
			
			String dateString = theParameter.substring(dateStart);
			setValueAsString(dateString);
			setComparator(comparator);
		}

	}

}
