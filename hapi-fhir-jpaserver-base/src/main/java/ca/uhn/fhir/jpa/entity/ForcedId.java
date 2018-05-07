package ca.uhn.fhir.jpa.entity;

/*
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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.ColumnDefault;

//@formatter:off
@Entity()
@Table(name = "HFJ_FORCED_ID", uniqueConstraints = {
	@UniqueConstraint(name = "IDX_FORCEDID_RESID", columnNames = {"RESOURCE_PID"}),
	@UniqueConstraint(name = "IDX_FORCEDID_TYPE_RESID", columnNames = {"RESOURCE_TYPE", "RESOURCE_PID"})
}, indexes= {
	@Index(name = "IDX_FORCEDID_TYPE_FORCEDID", columnList = "RESOURCE_TYPE,FORCED_ID"),
})
//@formatter:on
public class ForcedId {

	public static final int MAX_FORCED_ID_LENGTH = 100;

	@Column(name = "FORCED_ID", nullable = false, length = MAX_FORCED_ID_LENGTH, updatable = false)
	private String myForcedId;

	@SequenceGenerator(name = "SEQ_FORCEDID_ID", sequenceName = "SEQ_FORCEDID_ID")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_FORCEDID_ID")
	@Id
	@Column(name = "PID")
	private Long myId;

	@JoinColumn(name = "RESOURCE_PID", nullable = false, updatable = false, foreignKey=@ForeignKey(name="FK_FORCEDID_RESOURCE"))
	@OneToOne()
	private ResourceTable myResource;

	@Column(name = "RESOURCE_PID", nullable = false, updatable = false, insertable=false)
	private Long myResourcePid;

	// This is updatable=true because it was added in 1.6 and needs to be set.. At some
	// point we should remove the default and make it not updatable
	@ColumnDefault("''")
	@Column(name = "RESOURCE_TYPE", nullable = true, length = 100, updatable = true)
	private String myResourceType;

	/**
	 * Constructor
	 */
	public ForcedId() {
		super();
	}

	public String getForcedId() {
		return myForcedId;
	}

	public ResourceTable getResource() {
		return myResource;
	}
	
	public Long getResourcePid() {
		if (myResourcePid==null) {
			return myResource.getId();
		}
		return myResourcePid;
	}

	public String getResourceType() {
		return myResourceType;
	}

	public void setForcedId(String theForcedId) {
		myForcedId = theForcedId;
	}

	public void setResource(ResourceTable theResource) {
		myResource = theResource;
	}

	public void setResourcePid(Long theResourcePid) {
		myResourcePid = theResourcePid;
	}

	public void setResourcePid(ResourceTable theResourcePid) {
		myResource = theResourcePid;
	}

	public void setResourceType(String theResourceType) {
		myResourceType = theResourceType;
	}

}
