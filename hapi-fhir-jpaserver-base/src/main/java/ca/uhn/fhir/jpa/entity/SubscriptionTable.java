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

import ca.uhn.fhir.jpa.model.entity.BasePartitionable;
import ca.uhn.fhir.jpa.model.entity.IdAndPartitionId;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.UniqueConstraint;

import java.util.Date;

@Entity
@Table(
		name = "HFJ_SUBSCRIPTION_STATS",
		uniqueConstraints = {
			@UniqueConstraint(
					name = "IDX_SUBSC_RESID",
					columnNames = {"RES_ID"}),
		})
@IdClass(IdAndPartitionId.class)
public class SubscriptionTable extends BasePartitionable {

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_TIME", nullable = false, insertable = true, updatable = false)
	private Date myCreated;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_SUBSCRIPTION_ID")
	@SequenceGenerator(name = "SEQ_SUBSCRIPTION_ID", sequenceName = "SEQ_SUBSCRIPTION_ID")
	@Column(name = "PID", insertable = false, updatable = false)
	private Long myId;

	@Column(name = "RES_ID", nullable = true)
	private Long myResId;

	@OneToOne()
	@JoinColumns(
			value = {
				@JoinColumn(
						name = "RES_ID",
						insertable = false,
						updatable = false,
						nullable = true,
						referencedColumnName = "RES_ID"),
				@JoinColumn(
						name = "PARTITION_ID",
						insertable = false,
						updatable = false,
						nullable = true,
						referencedColumnName = "PARTITION_ID")
			},
			foreignKey = @ForeignKey(name = "FK_SUBSC_RESOURCE_ID"))
	private ResourceTable mySubscriptionResource;

	/**
	 * Constructor
	 */
	public SubscriptionTable() {
		super();
	}

	public Date getCreated() {
		return myCreated;
	}

	public void setCreated(Date theCreated) {
		myCreated = theCreated;
	}

	public Long getId() {
		return myId;
	}

	public ResourceTable getSubscriptionResource() {
		return mySubscriptionResource;
	}

	public void setSubscriptionResource(ResourceTable theSubscriptionResource) {
		mySubscriptionResource = theSubscriptionResource;
		myResId = theSubscriptionResource.getId().getId();
		setPartitionId(theSubscriptionResource.getPartitionId());
	}
}
