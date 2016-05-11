package ca.uhn.fhir.jpa.dao;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2016 University Health Network
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

import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

class HistoryTuple implements Comparable<HistoryTuple> {

	private Long myId;
	private boolean myIsHistory;
	private Date myUpdated;

	public HistoryTuple(boolean theIsHistory, Date theUpdated, Long theId) {
		super();
		myIsHistory = theIsHistory;
		myUpdated = theUpdated;
		myId = theId;
	}

	@Override
	public int compareTo(HistoryTuple theO) {
		return myUpdated.compareTo(theO.myUpdated);
	}

	public Long getId() {
		return myId;
	}

	public boolean isHistory() {
		return myIsHistory;
	}

	public Date getUpdated() {
		return myUpdated;
	}

	public void setId(Long theId) {
		myId = theId;
	}

	public void setIsHistory(boolean theIsHistory) {
		myIsHistory = theIsHistory;
	}

	public void setUpdated(Date theUpdated) {
		myUpdated = theUpdated;
	}

	@Override
	public String toString() {
		ToStringBuilder b = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE);
		b.append("id", myId);
		b.append("history", myIsHistory);
		b.append("updated", myUpdated);
		return b.build();
	}

}
