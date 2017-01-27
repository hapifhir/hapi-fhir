package ca.uhn.fhir.jpa.entity;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2017 University Health Network
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
import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import ca.uhn.fhir.util.CoverageIgnore;

//@formatter:off
@Table(name="TRM_CODESYSTEM_VER", uniqueConstraints= {
	@UniqueConstraint(name="IDX_CSV_RESOURCEPID_AND_VER", columnNames= {"RES_ID", "RES_VERSION_ID"})
})
@Entity()
//@formatter:on
public class TermCodeSystemVersion implements Serializable {
	private static final long serialVersionUID = 1L;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "myCodeSystem")
	private Collection<TermConcept> myConcepts;

	@Id()
	@SequenceGenerator(name = "SEQ_CODESYSTEMVER_PID", sequenceName = "SEQ_CODESYSTEMVER_PID")
	@GeneratedValue(strategy=GenerationType.AUTO, generator="SEQ_CODESYSTEMVER_PID")
	@Column(name = "PID")
	private Long myId;

	@OneToOne()
	@JoinColumn(name = "RES_ID", referencedColumnName = "RES_ID", nullable = false, updatable = false, foreignKey=@ForeignKey(name="FK_CODESYSVER_RES_ID"))
	private ResourceTable myResource;

	@Column(name = "RES_VERSION_ID", nullable = false, updatable = false)
	private Long myResourceVersionId;

	/**
	 * Constructor
	 */
	public TermCodeSystemVersion() {
		super();
	}

	public Collection<TermConcept> getConcepts() {
		if (myConcepts == null) {
			myConcepts = new ArrayList<TermConcept>();
		}
		return myConcepts;
	}

	public Long getPid() {
		return myId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((myResource.getId() == null) ? 0 : myResource.getId().hashCode());
		result = prime * result + ((myResourceVersionId == null) ? 0 : myResourceVersionId.hashCode());
		return result;
	}

	@CoverageIgnore
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof TermCodeSystemVersion)) {
			return false;
		}
		TermCodeSystemVersion other = (TermCodeSystemVersion) obj;
		if ((myResource.getId() == null) != (other.myResource.getId() == null)) {
			return false;
		} else if (!myResource.getId().equals(other.myResource.getId())) {
			return false;
		}
		
		if (myResourceVersionId == null) {
			if (other.myResourceVersionId != null) {
				return false;
			}
		} else if (!myResourceVersionId.equals(other.myResourceVersionId)) {
			return false;
		}
		return true;
	}

	public ResourceTable getResource() {
		return myResource;
	}

	public Long getResourceVersionId() {
		return myResourceVersionId;
	}

	public void setResource(ResourceTable theResource) {
		myResource = theResource;
	}

	public void setResourceVersionId(Long theResourceVersionId) {
		myResourceVersionId = theResourceVersionId;
	}

}
