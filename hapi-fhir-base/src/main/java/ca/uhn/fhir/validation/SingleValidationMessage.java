package ca.uhn.fhir.validation;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class SingleValidationMessage {

	private Integer myLocationCol;
	private Integer myLocationLine;
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
		b.append(myLocationLine, other.myLocationLine);
		b.append(myLocationString, other.myLocationString);
		b.append(myMessage, other.myMessage);
		b.append(mySeverity, other.mySeverity);
		return b.isEquals();
	}

	public Integer getLocationCol() {
		return myLocationCol;
	}

	public Integer getLocationLine() {
		return myLocationLine;
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

	public void setLocationLine(Integer theLocationLine) {
		myLocationLine = theLocationLine;
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
		if (myLocationCol != null || myLocationLine != null) {
			b.append("col", myLocationCol);
			b.append("row", myLocationLine);
		}
		if (myLocationString != null) {
			b.append("locationString", myLocationString);
		}
		b.append("message", myMessage);
		if (mySeverity != null) {
			b.append("severity", mySeverity.getCode());
		}
		return b.toString();
	}

}
