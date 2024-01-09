/*
 * #%L
 * HAPI FHIR JPA Server
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
package ca.uhn.fhir.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

import java.io.Serializable;

// @formatter:off
@Entity
@Table(
		name = "HFJ_SEARCH_INCLUDE",
		indexes = {@Index(name = "FK_SEARCHINC_SEARCH", columnList = "SEARCH_PID")})
// @formatter:on
public class SearchInclude implements Serializable {

	private static final long serialVersionUID = 1L;

	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_SEARCH_INC")
	@SequenceGenerator(name = "SEQ_SEARCH_INC", sequenceName = "SEQ_SEARCH_INC")
	@Id
	@Column(name = "PID")
	private Long myId;

	@Column(name = "REVINCLUDE", insertable = true, updatable = false, nullable = false)
	private boolean myReverse;

	public boolean isReverse() {
		return myReverse;
	}

	@Column(name = "SEARCH_INCLUDE", length = 200, insertable = true, updatable = false, nullable = false)
	private String myInclude;

	@ManyToOne
	@JoinColumn(
			name = "SEARCH_PID",
			referencedColumnName = "PID",
			foreignKey = @ForeignKey(name = "FK_SEARCHINC_SEARCH"),
			insertable = true,
			updatable = false,
			nullable = false)
	private Search mySearch;

	@Column(name = "SEARCH_PID", insertable = false, updatable = false, nullable = false)
	private Long mySearchPid;

	@Column(name = "INC_RECURSE", insertable = true, updatable = false, nullable = false)
	private boolean myRecurse;

	/**
	 * Constructor
	 */
	public SearchInclude() {
		// nothing
	}

	/**
	 * Constructor
	 */
	public SearchInclude(Search theSearch, String theInclude, boolean theReverse, boolean theRecurse) {
		mySearch = theSearch;
		myInclude = theInclude;
		myReverse = theReverse;
		myRecurse = theRecurse;
	}

	@Override
	public boolean equals(Object theObj) {
		if (!(theObj instanceof SearchInclude)) {
			return false;
		}
		if (myId == null) {
			return false;
		}
		return myId.equals(((SearchInclude) theObj).myId);
	}

	public String getInclude() {
		return myInclude;
	}

	@Override
	public int hashCode() {
		return myId == null ? 0 : myId.hashCode();
	}

	public boolean isRecurse() {
		return myRecurse;
	}
}
