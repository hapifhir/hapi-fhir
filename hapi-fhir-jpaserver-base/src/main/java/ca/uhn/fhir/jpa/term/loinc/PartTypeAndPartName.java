package ca.uhn.fhir.jpa.term.loinc;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class PartTypeAndPartName {
	private final String myPartType;
	private final String myPartName;

	public PartTypeAndPartName(String thePartType, String thePartName) {
		myPartType = thePartType;
		myPartName = thePartName;
	}

	@Override
	public boolean equals(Object theO) {
		if (this == theO) {
			return true;
		}

		if (theO == null || getClass() != theO.getClass()) {
			return false;
		}

		PartTypeAndPartName that = (PartTypeAndPartName) theO;

		return new EqualsBuilder()
			.append(myPartType, that.myPartType)
			.append(myPartName, that.myPartName)
			.isEquals();
	}

	public String getPartName() {
		return myPartName;
	}

	public String getPartType() {
		return myPartType;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37)
			.append(myPartType)
			.append(myPartName)
			.toHashCode();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
			.append("partType", myPartType)
			.append("partName", myPartName)
			.toString();
	}
}
