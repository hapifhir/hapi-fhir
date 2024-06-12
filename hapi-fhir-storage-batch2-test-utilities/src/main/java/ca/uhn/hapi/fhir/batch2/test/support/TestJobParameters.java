/*-
 * #%L
 * HAPI FHIR JPA Server - Batch2 specification tests
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
package ca.uhn.hapi.fhir.batch2.test.support;

import ca.uhn.fhir.model.api.IModelJson;
import ca.uhn.fhir.model.api.annotation.PasswordField;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public class TestJobParameters implements IModelJson {

	@JsonProperty("param1")
	@NotBlank
	private String myParam1;

	@JsonProperty("param2")
	@NotBlank
	@Length(min = 5, max = 100)
	private String myParam2;

	@JsonProperty(value = "password")
	@PasswordField
	private String myPassword;

	public String getPassword() {
		return myPassword;
	}

	public TestJobParameters setPassword(String thePassword) {
		myPassword = thePassword;
		return this;
	}

	public String getParam1() {
		return myParam1;
	}

	public TestJobParameters setParam1(String theParam1) {
		myParam1 = theParam1;
		return this;
	}

	public String getParam2() {
		return myParam2;
	}

	public TestJobParameters setParam2(String theParam2) {
		myParam2 = theParam2;
		return this;
	}

}
