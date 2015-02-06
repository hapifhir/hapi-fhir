package ca.uhn.fhir.jpa.entity;

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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "HFJ_RES_TAG")
public class ResourceTag extends BaseTag {

	private static final long serialVersionUID = 1L;

	@GeneratedValue(strategy = GenerationType.AUTO)
	@Id
	@Column(name = "PID")
	private Long myId;

	@ManyToOne(cascade = {})
	@JoinColumn(name = "RES_ID", referencedColumnName = "RES_ID")
	private ResourceTable myResource;

	@Column(name = "RES_TYPE", length = ResourceTable.RESTYPE_LEN,nullable=false)
	private String myResourceType;

	@Column(name="RES_ID", insertable=false,updatable=false)
	private Long myResourceId;
	
	public Long getResourceId() {
		return myResourceId;
	}

	public void setResourceId(Long theResourceId) {
		myResourceId = theResourceId;
	}

	public ResourceTag() {
	}

	public ResourceTag(ResourceTable theResourceTable, TagDefinition theTag) {
		myResource = theResourceTable;
		setTag(theTag);
	}

	public ResourceTable getResource() {
		return myResource;
	}

	public String getResourceType() {
		return myResourceType;
	}

	public void setResource(ResourceTable theResource) {
		myResource = theResource;
	}

	public void setResourceType(String theResourceType) {
		myResourceType = theResourceType;
	}

}
