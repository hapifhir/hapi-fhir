package ca.uhn.fhir.jpa.model.entity;

/*-
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

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "HFJ_RES_PARAM_PRESENT", indexes = {
	// We used to have a constraint named IDX_RESPARMPRESENT_SPID_RESID - Don't reuse
	@Index(name = "IDX_RESPARMPRESENT_RESID", columnList = "RES_ID"),
	@Index(name = "IDX_RESPARMPRESENT_HASHPRES", columnList = "HASH_PRESENCE")
})
public class SearchParamPresent implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "SEQ_RESPARMPRESENT_ID", sequenceName = "SEQ_RESPARMPRESENT_ID")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_RESPARMPRESENT_ID")
	@Column(name = "PID")
	private Long myId;
	@Column(name = "SP_PRESENT", nullable = false)
	private boolean myPresent;
	@ManyToOne()
	@JoinColumn(name = "RES_ID", referencedColumnName = "RES_ID", nullable = false, foreignKey = @ForeignKey(name = "FK_RESPARMPRES_RESID"))
	private ResourceTable myResource;
	@Column(name="RES_ID", nullable = false, insertable = false, updatable = false)
	private Long myResourcePid;
	@Transient
	private transient String myParamName;
	@Column(name = "HASH_PRESENCE")
	private Long myHashPresence;

	/**
	 * Constructor
	 */
	public SearchParamPresent() {
		super();
	}

	@SuppressWarnings("unused")
	@PrePersist
	public void calculateHashes() {
		if (myHashPresence == null) {
			String resourceType = getResource().getResourceType();
			String paramName = getParamName();
			boolean present = myPresent;
			setHashPresence(calculateHashPresence(resourceType, paramName, present));
		}
	}

	public Long getHashPresence() {
		return myHashPresence;
	}

	public void setHashPresence(Long theHashPresence) {
		myHashPresence = theHashPresence;
	}

	public String getParamName() {
		return myParamName;
	}

	public void setParamName(String theParamName) {
		myParamName = theParamName;
	}

	public ResourceTable getResource() {
		return myResource;
	}

	public void setResource(ResourceTable theResourceTable) {
		myResource = theResourceTable;
	}

	public boolean isPresent() {
		return myPresent;
	}

	public void setPresent(boolean thePresent) {
		myPresent = thePresent;
	}

	@Override
	public String toString() {
		ToStringBuilder b = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE);

		b.append("resPid", myResource.getIdDt().toUnqualifiedVersionless().getValue());
		b.append("paramName", myParamName);
		b.append("present", myPresent);
		return b.build();
	}

	public static long calculateHashPresence(String theResourceType, String theParamName, Boolean thePresent) {
		String string = thePresent != null ? Boolean.toString(thePresent) : Boolean.toString(false);
		return BaseResourceIndexedSearchParam.hash(theResourceType, theParamName, string);
	}

}
