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

import org.apache.commons.lang3.Validate;

import javax.annotation.Nonnull;

import static ca.uhn.fhir.batch2.model.JobDefinition.ID_MAX_LENGTH;

public class JobDefinitionParameter {

	private final String myName;
	private final String myDescription;
	private final ParamTypeEnum myType;
	private final boolean myRequired;
	private final boolean myRepeating;

	/**
	 * Constructor
	 */
	public JobDefinitionParameter(@Nonnull String theName, @Nonnull String theDescription, @Nonnull ParamTypeEnum theType, boolean theRequired, boolean theRepeating) {
		Validate.isTrue(theName.length() <= ID_MAX_LENGTH, "Maximum name length is %d", ID_MAX_LENGTH);

		Validate.notBlank(theName);
		Validate.notBlank(theDescription);

		myName = theName;
		myDescription = theDescription;
		myType = theType;
		myRequired = theRequired;
		myRepeating = theRepeating;
	}

	public String getName() {
		return myName;
	}

	public String getDescription() {
		return myDescription;
	}

	public ParamTypeEnum getType() {
		return myType;
	}

	public boolean isRequired() {
		return myRequired;
	}

	public boolean isRepeating() {
		return myRepeating;
	}

	public enum ParamTypeEnum {

		STRING,
		POSITIVE_INTEGER,
		PASSWORD

	}
}
