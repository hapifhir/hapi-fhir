package ca.uhn.fhir.batch2.jobs.parameters;

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

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.Validate;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.List;

public class UrlListJobParameters extends PartitionedJobParameters {
	@JsonProperty("url")
	@Nullable
	private List<@Pattern(regexp = "^[A-Z][A-Za-z0-9]+\\?.*", message = "If populated, URL must be a search URL in the form '{resourceType}?[params]'") String> myUrls;

	public List<String> getUrls() {
		if (myUrls == null) {
			myUrls = new ArrayList<>();
		}
		return myUrls;
	}

	public UrlListJobParameters addUrl(@Nonnull String theUrl) {
		Validate.notNull(theUrl);
		getUrls().add(theUrl);
		return this;
	}
}
