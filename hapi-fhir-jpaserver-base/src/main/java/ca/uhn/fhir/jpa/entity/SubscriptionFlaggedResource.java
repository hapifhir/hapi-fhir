package ca.uhn.fhir.jpa.entity;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2016 University Health Network
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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "HFJ_SUBSCRIPTION_FLAG_RES")
public class SubscriptionFlaggedResource {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@SequenceGenerator(name = "SEQ_SUBSCRIPTION_FLAG_ID", sequenceName = "SEQ_SUBSCRIPTION_FLAG_ID")
	@Column(name = "PID", insertable = false, updatable = false)
	private Long myId;

	@ManyToOne()
	@JoinColumn(name="RES_ID", nullable=false)
	private ResourceTable myResource;
	
	//@formatter:off
	@ManyToOne()
	@JoinColumn(name="SUBSCRIPTION_ID", 
		foreignKey=@ForeignKey(name="FK_SUBSFLAG_SUBS")
	)
	private SubscriptionTable mySubscription;
	//@formatter:om
	
	@Column(name="RES_VERSION", nullable=false)
	private Long myVersion;

	public ResourceTable getResource() {
		return myResource;
	}

	public SubscriptionTable getSubscription() {
		return mySubscription;
	}

	public Long getVersion() {
		return myVersion;
	}

	public void setResource(ResourceTable theResource) {
		myResource = theResource;
	}

	public void setSubscription(SubscriptionTable theSubscription) {
		mySubscription = theSubscription;
	}

	public void setVersion(Long theVersion) {
		myVersion = theVersion;
	}
	
}
