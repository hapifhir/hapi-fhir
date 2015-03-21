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
import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Index;

import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.server.Constants;

@Entity
@Table(name = "HFJ_RES_VER", uniqueConstraints = {@UniqueConstraint(name="IDX_RES_VER_ALL", columnNames = { "RES_ID", "RES_TYPE", "RES_VER" })})
@org.hibernate.annotations.Table(appliesTo="HFJ_RES_VER", indexes= {@Index(name="IDX_RES_VER_DATE", columnNames= {"RES_UPDATED"})})
public class ResourceHistoryTable extends BaseHasResource implements Serializable {

	private static final long serialVersionUID = 1L;


	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="PID")
	private Long myId;

	@Column(name = "RES_ID")
	private Long myResourceId;

	@Column(name = "RES_TYPE", length = 30, nullable = false)
	private String myResourceType;

	@Column(name = "RES_VER", nullable = false)
	private Long myResourceVersion;

	@OneToMany(mappedBy = "myResourceHistory", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
	private Collection<ResourceHistoryTag> myTags;

	public void addTag(ResourceHistoryTag theTag) {
		for (ResourceHistoryTag next : getTags()) {
			if (next.getTag().equals(theTag)) {
				return;
			}
		}
		getTags().add(theTag);
	}

	public void addTag(ResourceTag theTag) {
		ResourceHistoryTag tag = new ResourceHistoryTag(this, theTag.getTag());
		tag.setResourceType(theTag.getResourceType());
		getTags().add(tag);
	}

	@Override
	public BaseTag addTag(TagDefinition theDef) {
		ResourceHistoryTag historyTag = new ResourceHistoryTag(this, theDef);
		getTags().add(historyTag);
		return historyTag;
	}

	public Long getId() {
		return myId;
	}

	@Override
	public IdDt getIdDt() {
		Object id = getForcedId()==null? getResourceId() : getForcedId().getForcedId();
		return new IdDt(getResourceType() + '/' + id + '/' + Constants.PARAM_HISTORY + '/' + getVersion());
	}

	public Long getResourceId() {
		return myResourceId;
	}

	public String getResourceType() {
		return myResourceType;
	}


	public Collection<ResourceHistoryTag> getTags() {
		if (myTags == null) {
			myTags = new ArrayList<ResourceHistoryTag>();
		}
		return myTags;
	}

	@Override
	public long getVersion() {
		return myResourceVersion;
	}

	public boolean hasTag(String theTerm, String theScheme) {
		for (ResourceHistoryTag next : getTags()) {
			if (next.getTag().getSystem().equals(theScheme) && next.getTag().getCode().equals(theTerm)) {
				return true;
			}
		}
		return false;
	}

	public void setResourceId(Long theResourceId) {
		myResourceId = theResourceId;
	}

	public void setResourceType(String theResourceType) {
		myResourceType=theResourceType;
	}

	public void setVersion(long theVersion) {
		myResourceVersion=theVersion;
	}

}
