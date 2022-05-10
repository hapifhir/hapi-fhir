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

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

//@formatter:off
@Entity
@Table(name = "HFJ_SEARCH_INCLUDE")
//@formatter:on
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
	@JoinColumn(name = "SEARCH_PID", referencedColumnName = "PID", foreignKey = @ForeignKey(name = "FK_SEARCHINC_SEARCH"), insertable = true, updatable = false, nullable = false)
	private Search mySearch;

	@Column(name="SEARCH_PID", insertable=false, updatable=false, nullable=false)
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
