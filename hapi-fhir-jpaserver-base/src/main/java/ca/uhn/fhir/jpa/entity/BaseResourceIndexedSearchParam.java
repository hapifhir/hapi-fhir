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

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class BaseResourceIndexedSearchParam implements Serializable {

	static final int MAX_SP_NAME = 100;

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "SP_ID")
	private Long myId;

	@Column(name = "SP_NAME", length = MAX_SP_NAME, nullable=false)
	private String myParamName;

	@ManyToOne(optional = false)
	@JoinColumn(name = "RES_ID", referencedColumnName="RES_ID")
	private ResourceTable myResource;

	@Column(name = "RES_TYPE", nullable=false)
	private String myResourceType;

	@Column(name = "RES_ID", insertable = false, updatable = false)
	private Long myResourcePid;

	public String getParamName() {
		return myParamName;
	}

	public void setParamName(String theName) {
		myParamName = theName;
	}

	public ResourceTable getResource() {
		return myResource;
	}

	public void setResource(ResourceTable theResource) {
		myResource = theResource;
		myResourceType = theResource.getResourceType();
	}

}
