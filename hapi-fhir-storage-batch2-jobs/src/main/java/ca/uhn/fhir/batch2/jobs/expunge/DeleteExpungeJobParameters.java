/*-
 * #%L
 * hapi-fhir-storage-batch2-jobs
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
package ca.uhn.fhir.batch2.jobs.expunge;

import ca.uhn.fhir.batch2.jobs.parameters.JobParameters;
import com.fasterxml.jackson.annotation.JsonProperty;

public class DeleteExpungeJobParameters extends JobParameters {
	@JsonProperty("cascade")
	private boolean myCascade;

	@JsonProperty("cascadeMaxRounds")
	private Integer myCascadeMaxRounds;

	/**
	 * Constructor
	 */
	public DeleteExpungeJobParameters() {
		super();
	}

	public Integer getCascadeMaxRounds() {
		return myCascadeMaxRounds;
	}

	public void setCascadeMaxRounds(Integer theCascadeMaxRounds) {
		myCascadeMaxRounds = theCascadeMaxRounds;
	}

	public boolean isCascade() {
		return myCascade;
	}

	public void setCascade(boolean theCascade) {
		myCascade = theCascade;
	}
}
