package ca.uhn.fhir.jpa.model.entity;

/*
 * #%L
 * HAPI FHIR Model
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
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

import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Entity()
@Table(name = "HFJ_FORCED_ID", uniqueConstraints = {
	@UniqueConstraint(name = "IDX_FORCEDID_RESID", columnNames = {"RESOURCE_PID"}),
	@UniqueConstraint(name = ForcedId.IDX_FORCEDID_TYPE_FID, columnNames = {"RESOURCE_TYPE", "FORCED_ID"})
}, indexes = {
	/*
	 * NB: We previously had indexes named
	 * - IDX_FORCEDID_TYPE_FORCEDID
	 * - IDX_FORCEDID_TYPE_RESID
	 * so don't reuse these names
	 */
})
public class ForcedId {

	public static final int MAX_FORCED_ID_LENGTH = 100;
	public static final String IDX_FORCEDID_TYPE_FID = "IDX_FORCEDID_TYPE_FID";

	@Column(name = "FORCED_ID", nullable = false, length = MAX_FORCED_ID_LENGTH, updatable = false)
	private String myForcedId;

	@SequenceGenerator(name = "SEQ_FORCEDID_ID", sequenceName = "SEQ_FORCEDID_ID")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_FORCEDID_ID")
	@Id
	@Column(name = "PID")
	private Long myId;

	@JoinColumn(name = "RESOURCE_PID", nullable = false, updatable = false, foreignKey = @ForeignKey(name = "FK_FORCEDID_RESOURCE"))
	@OneToOne(fetch = FetchType.LAZY)
	private ResourceTable myResource;

	@Column(name = "RESOURCE_PID", nullable = false, updatable = false, insertable = false)
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

	public void setForcedId(String theForcedId) {
		myForcedId = theForcedId;
	}

	public void setResource(ResourceTable theResource) {
		myResource = theResource;
	}

	public String getResourceType() {
		return myResourceType;
	}

	public void setResourceType(String theResourceType) {
		myResourceType = theResourceType;
	}

	public Long getId() {
		return myId;
	}
}
