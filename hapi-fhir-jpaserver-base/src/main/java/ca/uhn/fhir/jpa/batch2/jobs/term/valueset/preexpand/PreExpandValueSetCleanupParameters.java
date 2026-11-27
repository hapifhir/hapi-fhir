/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.batch2.jobs.term.valueset.preexpand;

import ca.uhn.fhir.model.api.IModelJson;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Parameters of the job that drops the staging versions which ValueSet pre-expansion jobs left behind.
 */
// Created by Claude Fable 5.2
public class PreExpandValueSetCleanupParameters implements IModelJson {

	@JsonProperty("url")
	private String myUrl;

	@JsonProperty("stagingVersionIds")
	private List<String> myStagingVersionIds;

	@Nullable
	public String getUrl() {
		return myUrl;
	}

	public void setUrl(@Nonnull String theUrl) {
		myUrl = theUrl;
	}

	@Nonnull
	public List<String> getStagingVersionIds() {
		if (myStagingVersionIds == null) {
			myStagingVersionIds = new ArrayList<>();
		}
		return myStagingVersionIds;
	}

	public void setStagingVersionIds(@Nonnull List<String> theStagingVersionIds) {
		myStagingVersionIds = theStagingVersionIds;
	}
}
