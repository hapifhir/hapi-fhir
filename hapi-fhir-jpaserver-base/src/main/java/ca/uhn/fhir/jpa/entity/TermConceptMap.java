package ca.uhn.fhir.jpa.entity;

/*-
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
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "TRM_CONCEPT_MAP", uniqueConstraints = {
	@UniqueConstraint(name = "IDX_CONCEPT_MAP_URL", columnNames = {"URL"})
})
public class TermConceptMap implements Serializable {
	@Id()
	@SequenceGenerator(name = "SEQ_CONCEPT_MAP_PID", sequenceName = "SEQ_CONCEPT_MAP_PID")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_CONCEPT_MAP_PID")
	@Column(name = "PID")
	private Long myId;

	@OneToOne()
	@JoinColumn(name = "RES_ID", referencedColumnName = "RES_ID", nullable = false, updatable = false, foreignKey = @ForeignKey(name = "FK_TRMCONCEPTMAP_RES"))
	private ResourceTable myResource;

	@Column(name = "RES_ID", insertable = false, updatable = false)
	private Long myResourcePid;

	@Column(name = "SOURCE_URL", nullable = true, length = 200)
	private String mySource;

	@Column(name = "TARGET_URL", nullable = true, length = 200)
	private String myTarget;

	@Column(name = "URL", length = 200, nullable = false)
	private String myUrl;

	@OneToMany(mappedBy = "myConceptMap")
	private List<TermConceptMapGroup> myConceptMapGroups;

	public List<TermConceptMapGroup> getConceptMapGroups() {
		if (myConceptMapGroups == null) {
			myConceptMapGroups = new ArrayList<>();
		}

		return myConceptMapGroups;
	}

	public Long getId() {
		return myId;
	}

	public ResourceTable getResource() {
		return myResource;
	}

	public void setResource(ResourceTable resource) {
		myResource = resource;
	}

	public Long getResourcePid() {
		return myResourcePid;
	}

	public void setResourcePid(Long resourcePid) {
		myResourcePid = resourcePid;
	}

	public String getSource() {
		return mySource;
	}

	public void setSource(String source) {
		mySource = source;
	}

	public String getTarget() {
		return myTarget;
	}

	public void setTarget(String target) {
		myTarget = target;
	}

	public String getUrl() {
		return myUrl;
	}

	public void setUrl(String theUrl) {
		myUrl = theUrl;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
			.append("myId", myId)
			.append("myResource", myResource.toString())
			.append(myResource != null ? ("myResource=" + myResource.toString()) : ("myResource=(null)"))
			.append("myResourcePid", myResourcePid)
			.append("mySource", mySource)
			.append("myTarget", myTarget)
			.append("myUrl", myUrl)
			.append(myConceptMapGroups != null ? ("myConceptMapGroups - size=" + myConceptMapGroups.size()) : ("myConceptMapGroups=(null)"))
			.toString();
	}
}
