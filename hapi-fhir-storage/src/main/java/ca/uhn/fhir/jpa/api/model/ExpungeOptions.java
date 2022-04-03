package ca.uhn.fhir.jpa.api.model;

/*-
 * #%L
 * HAPI FHIR Storage api
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

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class ExpungeOptions {
	private int myLimit = 1000;
	private boolean myExpungeOldVersions;
	private boolean myExpungeDeletedResources;
	private boolean myExpungeEverything;

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
			.append("limit", myLimit)
			.append("oldVersions", myExpungeOldVersions)
			.append("deletedResources", myExpungeDeletedResources)
			.append("everything", myExpungeEverything)
			.toString();
	}

	/**
	 * The maximum number of resources versions to expunge
	 */
	public int getLimit() {
		return myLimit;
	}

	/**
	 * The maximum number of resource versions to expunge
	 */
	public ExpungeOptions setLimit(int theLimit) {
		myLimit = theLimit;
		return this;
	}

	public boolean isExpungeEverything() {
		return myExpungeEverything;
	}

	public ExpungeOptions setExpungeEverything(boolean theExpungeEverything) {
		myExpungeEverything = theExpungeEverything;
		return this;
	}

	public boolean isExpungeDeletedResources() {
		return myExpungeDeletedResources;
	}

	public ExpungeOptions setExpungeDeletedResources(boolean theExpungeDeletedResources) {
		myExpungeDeletedResources = theExpungeDeletedResources;
		return this;
	}

	public boolean isExpungeOldVersions() {
		return myExpungeOldVersions;
	}

	public ExpungeOptions setExpungeOldVersions(boolean theExpungeOldVersions) {
		myExpungeOldVersions = theExpungeOldVersions;
		return this;
	}
}
