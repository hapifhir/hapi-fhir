package ca.uhn.fhir.jpa.util;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2018 University Health Network
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

public class ExpungeOptions {
	private int myLimit = 1000;
	private boolean myExpungeOldVersions;
	private boolean myExpungeDeletedResources;
	private boolean myExpungeEverything;

	/**
	 * The maximum number of resources versions to expunge
	 */
	public int getLimit() {
		return myLimit;
	}

	public boolean isExpungeEverything() {
		return myExpungeEverything;
	}

	public ExpungeOptions setExpungeEverything(boolean theExpungeEverything) {
		myExpungeEverything = theExpungeEverything;
		return this;
	}

	/**
	 * The maximum number of resource versions to expunge
	 */
	public void setLimit(int theLimit) {
		myLimit = theLimit;
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
