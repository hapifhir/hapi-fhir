package ca.uhn.fhir.dao;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2015 University Health Network
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

public class BaseDaoConfig implements IDaoConfig{

	private boolean myAllowMultipleDelete;
	private int myHardSearchLimit = 1000;
	private int myHardTagListLimit = 1000;
	private int myIncludeLimit = 2000;

	/**
	 * See {@link #setIncludeLimit(int)}
	 */
	public int getHardSearchLimit() {
		return myHardSearchLimit;
	}

	public int getHardTagListLimit() {
		return myHardTagListLimit;
	}

	public int getIncludeLimit() {
		return myIncludeLimit;
	}

	public boolean isAllowMultipleDelete() {
		return myAllowMultipleDelete;
	}

	public void setAllowMultipleDelete(boolean theAllowMultipleDelete) {
		myAllowMultipleDelete = theAllowMultipleDelete;
	}

	public void setHardSearchLimit(int theHardSearchLimit) {
		myHardSearchLimit = theHardSearchLimit;
	}

	public void setHardTagListLimit(int theHardTagListLimit) {
		myHardTagListLimit = theHardTagListLimit;
	}

	/**
	 * This is the maximum number of resources that will be added to a single page of returned resources. Because of
	 * includes with wildcards and other possibilities it is possible for a client to make requests that include very
	 * large amounts of data, so this hard limit can be imposed to prevent runaway requests.
	 */
	public void setIncludeLimit(int theIncludeLimit) {
		myIncludeLimit = theIncludeLimit;
	}

}
