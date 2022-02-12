package ca.uhn.fhir.batch2.model;

import ca.uhn.fhir.model.api.IModelJson;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class JobInstanceParameter implements IModelJson {

	@JsonProperty("name")
	private String myName;
	@JsonProperty("value")
	private String myValue;

	public String getName() {
		return myName;
	}

	public JobInstanceParameter setName(String theName) {
		myName = theName;
		return this;
	}

	public String getValue() {
		return myValue;
	}

	public JobInstanceParameter setValue(String theValue) {
		myValue = theValue;
		return this;
	}

	@Override
	public boolean equals(Object theO) {
		if (!(theO instanceof JobInstanceParameter)) {
			return false;
		}

		JobInstanceParameter that = (JobInstanceParameter) theO;
		return new EqualsBuilder()
			.append(myName, that.myName)
			.append(myValue, that.myValue)
			.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(myName).append(myValue).toHashCode();
	}
}
