/*-
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
package ca.uhn.fhir.model.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.Validate;

import java.util.HashMap;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public abstract class BaseBatchJobParameters implements IModelJson {
	/**
	 * A serializable map of key-value pairs that can be
	 * added to any extending job.
	 */
	@JsonProperty("userData")
	private Map<String, Object> myUserData;

	public Map<String, Object> getUserData() {
		if (myUserData == null) {
			myUserData = new HashMap<>();
		}
		return myUserData;
	}

	public void setUserData(String theKey, Object theValue) {
		Validate.isTrue(isNotBlank(theKey), "Invalid key; key must be non-empty, non-null.");
		if (theValue == null) {
			getUserData().remove(theKey);
		} else {
			Validate.isTrue(
					validateValue(theValue),
					String.format(
							"Invalid data type provided %s", theValue.getClass().getName()));
			getUserData().put(theKey, theValue);
		}
	}

	private boolean validateValue(Object theValue) {
		if (theValue instanceof Boolean) {
			return true;
		}
		if (theValue instanceof Number) {
			return true;
		}
		if (theValue instanceof String) {
			return true;
		}
		return false;
	}
}
