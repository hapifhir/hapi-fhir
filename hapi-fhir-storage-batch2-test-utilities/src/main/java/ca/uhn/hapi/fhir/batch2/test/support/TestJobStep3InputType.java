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
import com.fasterxml.jackson.annotation.JsonProperty;

public class TestJobStep3InputType implements IModelJson {

	@JsonProperty("data3")
	private String myData3;
	@JsonProperty("data4")
	private String myData4;

	public String getData3() {
		return myData3;
	}

	public TestJobStep3InputType setData3(String theData1) {
		myData3 = theData1;
		return this;
	}

	public String getData4() {
		return myData4;
	}

	public TestJobStep3InputType setData4(String theData2) {
		myData4 = theData2;
		return this;
	}

}
