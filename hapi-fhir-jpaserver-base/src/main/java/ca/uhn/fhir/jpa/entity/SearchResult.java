package ca.uhn.fhir.jpa.entity;

/*
 * #%L
 * HAPI FHIR JPA Server
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

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "HFJ_SEARCH_RESULT", uniqueConstraints = {
	@UniqueConstraint(name = "HFJ_SEARCH_RESULT_PKEY", columnNames = {"SEARCH_PID", "SEARCH_ORDER"})
})
@IdClass(SearchResult.PrimaryKey.class)
public class SearchResult implements Serializable {

	private static final long serialVersionUID = 1L;


	// TODO delete in 6.2
	@Deprecated(forRemoval = true)
	@Column(name = "PID", updatable = false, nullable = true)
	private Long myPidUnused;

	@Id
	@Column(name = "SEARCH_PID", updatable = false, nullable = false)
	private Long mySearchPid;
	@Id
	@Column(name = "SEARCH_ORDER", updatable = false, nullable = false)
	private int myOrder;
	@Column(name = "RESOURCE_PID", updatable = false, nullable = false)
	private Long myResourcePid;


	/**
	 * Constructor
	 */
	public SearchResult() {
		// nothing
	}

	/**
	 * Constructor
	 */
	public SearchResult(Search theSearch) {
		Validate.notNull(theSearch.getId());
		mySearchPid = theSearch.getId();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
			.append("search", mySearchPid)
			.append("order", myOrder)
			.append("resourcePid", myResourcePid)
			.toString();
	}

	@Override
	public boolean equals(Object theObj) {
		if (!(theObj instanceof SearchResult)) {
			return false;
		}
		return myResourcePid.equals(((SearchResult) theObj).myResourcePid);
	}

	public int getOrder() {
		return myOrder;
	}

	public void setOrder(int theOrder) {
		myOrder = theOrder;
	}

	public Long getResourcePid() {
		return myResourcePid;
	}

	public void setResourcePid(Long theResourcePid) {
		myResourcePid = theResourcePid;
	}

	@Override
	public int hashCode() {
		return myResourcePid.hashCode();
	}

	public static class PrimaryKey implements Serializable {
		private static final long serialVersionUID = 1L;
		private Long mySearchPid;
		private int myOrder;

		public Long getSearchPid() {
			return mySearchPid;
		}

		public void setSearchPid(Long theSearchPid) {
			mySearchPid = theSearchPid;
		}

		public int getOrder() {
			return myOrder;
		}

		public void setOrder(int theOrder) {
			myOrder = theOrder;
		}
	}

}
