package ca.uhn.fhir.validation;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class SingleValidationMessage {

	private Integer myLocationCol;
	private Integer myLocationRow;
	private String myLocationString;
	private String myMessage;
	private ResultSeverityEnum mySeverity;

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof SingleValidationMessage)) {
			return false;
		}
		SingleValidationMessage other = (SingleValidationMessage) obj;
		EqualsBuilder b = new EqualsBuilder();
		b.append(myLocationCol, other.myLocationCol);
		b.append(myLocationRow, other.myLocationRow);
		b.append(myLocationString, other.myLocationString);
		b.append(myMessage, other.myMessage);
		b.append(mySeverity, other.mySeverity);
		return b.isEquals();
	}

	public Integer getLocationCol() {
		return myLocationCol;
	}

	public Integer getLocationRow() {
		return myLocationRow;
	}

	public String getLocationString() {
		return myLocationString;
	}

	public String getMessage() {
		return myMessage;
	}

	public ResultSeverityEnum getSeverity() {
		return mySeverity;
	}

	@Override
	public int hashCode() {
		HashCodeBuilder b = new HashCodeBuilder();
		b.append(myLocationCol);
		b.append(myLocationCol);
		b.append(myLocationString);
		b.append(myMessage);
		b.append(mySeverity);
		return b.toHashCode();
	}

	public void setLocationCol(Integer theLocationCol) {
		myLocationCol = theLocationCol;
	}

	public void setLocationRow(Integer theLocationRow) {
		myLocationRow = theLocationRow;
	}

	public void setLocationString(String theLocationString) {
		myLocationString = theLocationString;
	}

	public void setMessage(String theMessage) {
		myMessage = theMessage;
	}

	public void setSeverity(ResultSeverityEnum theSeverity) {
		mySeverity = theSeverity;
	}

	@Override
	public String toString() {
		ToStringBuilder b = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE);
		if (myLocationCol != null || myLocationRow != null) {
			b.append("myLocationCol", myLocationCol);
			b.append("myLocationRow", myLocationRow);
		}
		if (myLocationString != null) {
			b.append("myLocationString", myLocationString);
		}
		b.append("myMessage", myMessage);
		if (mySeverity != null) {
			b.append("mySeverity", mySeverity.getCode());
		}
		return b.toString();
	}

}
