package ca.uhn.fhir.batch2.model;

/*-
 * #%L
 * HAPI FHIR JPA Server - Batch2 Task Processor
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

import ca.uhn.fhir.model.api.IModelJson;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class JobInstanceParameter implements IModelJson {

	@JsonProperty("name")
	private String myName;
	@JsonProperty("value")
	private String myValue;

	/**
	 * Constructor
	 */
	public JobInstanceParameter() {
		super();
	}

	/**
	 * Constructor
	 */
	public JobInstanceParameter(String theName, String theValue) {
		myName = theName;
		myValue = theValue;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
			.append("name", myName)
			.append("value", myValue)
			.toString();
	}

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
