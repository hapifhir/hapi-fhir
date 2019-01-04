package ca.uhn.fhir.jpa.entity;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2019 University Health Network
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

import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.util.CoverageIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

//@formatter:off
@Table(name = "TRM_CODESYSTEM_VER"
	// Note, we used to have a constraint named IDX_CSV_RESOURCEPID_AND_VER (don't reuse this)
)
@Entity()
//@formatter:on
public class TermCodeSystemVersion implements Serializable {
	private static final long serialVersionUID = 1L;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "myCodeSystem")
	private Collection<TermConcept> myConcepts;

	@Id()
	@SequenceGenerator(name = "SEQ_CODESYSTEMVER_PID", sequenceName = "SEQ_CODESYSTEMVER_PID")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_CODESYSTEMVER_PID")
	@Column(name = "PID")
	private Long myId;

	@OneToOne()
	@JoinColumn(name = "RES_ID", referencedColumnName = "RES_ID", nullable = false, updatable = false, foreignKey = @ForeignKey(name = "FK_CODESYSVER_RES_ID"))
	private ResourceTable myResource;

	@Column(name = "CS_VERSION_ID", nullable = true, updatable = false)
	private String myCodeSystemVersionId;
	/**
	 * This was added in HAPI FHIR 3.3.0 and is nullable just to avoid migration
	 * issued. It should be made non-nullable at some point.
	 */
	@ManyToOne
	@JoinColumn(name = "CODESYSTEM_PID", referencedColumnName = "PID", nullable = true, foreignKey = @ForeignKey(name = "FK_CODESYSVER_CS_ID"))
	private TermCodeSystem myCodeSystem;
	@SuppressWarnings("unused")
	@OneToOne(mappedBy = "myCurrentVersion", optional = true)
	private TermCodeSystem myCodeSystemHavingThisVersionAsCurrentVersionIfAny;

	/**
	 * Constructor
	 */
	public TermCodeSystemVersion() {
		super();
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

		if (myCodeSystemVersionId == null) {
			if (other.myCodeSystemVersionId != null) {
				return false;
			}
		} else if (!myCodeSystemVersionId.equals(other.myCodeSystemVersionId)) {
			return false;
		}
		return true;
	}

	public TermCodeSystem getCodeSystem() {
		return myCodeSystem;
	}

	public void setCodeSystem(TermCodeSystem theCodeSystem) {
		myCodeSystem = theCodeSystem;
	}

	public String getCodeSystemVersionId() {
		return myCodeSystemVersionId;
	}

	public void setCodeSystemVersionId(String theCodeSystemVersionId) {
		myCodeSystemVersionId = theCodeSystemVersionId;
	}

	public Collection<TermConcept> getConcepts() {
		if (myConcepts == null) {
			myConcepts = new ArrayList<>();
		}
		return myConcepts;
	}

	public Long getPid() {
		return myId;
	}

	public ResourceTable getResource() {
		return myResource;
	}

	public void setResource(ResourceTable theResource) {
		myResource = theResource;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((myResource.getId() == null) ? 0 : myResource.getId().hashCode());
		result = prime * result + ((myCodeSystemVersionId == null) ? 0 : myCodeSystemVersionId.hashCode());
		return result;
	}

}
