package ca.uhn.fhir.mdm.api.paging;

/*-
 * #%L
 * HAPI FHIR - Master Data Management
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

import java.util.Optional;

/**
 * Data clump class to keep the relevant paging URLs together for MDM.
 */
public class MdmPageLinkTuple {
	private String myPreviousLink = null;
	private String mySelfLink = null;
	private String myNextLink = null;

	MdmPageLinkTuple() {}

	public Optional<String> getPreviousLink() {
		return Optional.ofNullable(myPreviousLink);
	}

	public void setPreviousLink(String thePreviousLink) {
		this.myPreviousLink = thePreviousLink;
	}

	public String getSelfLink() {
		return mySelfLink;
	}

	public void setSelfLink(String theSelfLink) {
		this.mySelfLink = theSelfLink;
	}

	public Optional<String> getNextLink() {
		return Optional.ofNullable(myNextLink);
	}

	public void setNextLink(String theNextLink) {
		this.myNextLink = theNextLink;
	}
}
