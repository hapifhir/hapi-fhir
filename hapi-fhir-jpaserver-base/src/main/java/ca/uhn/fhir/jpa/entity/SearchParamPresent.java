package ca.uhn.fhir.jpa.entity;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2017 University Health Network
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

import javax.persistence.*;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Entity
@Table(name = "HFJ_RES_PARAM_PRESENT", indexes = {
		@Index(name = "IDX_RESPARMPRESENT_RESID", columnList = "RES_ID")
}, uniqueConstraints = {
		@UniqueConstraint(name = "IDX_RESPARMPRESENT_SPID_RESID", columnNames = { "SP_ID", "RES_ID" })
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

	@ManyToOne()
	@JoinColumn(name = "SP_ID", referencedColumnName = "PID", nullable = false, foreignKey = @ForeignKey(name = "FK_RESPARMPRES_SPID"))
	private SearchParam mySearchParam;

	/**
	 * Constructor
	 */
	public SearchParamPresent() {
		super();
	}
	
	public ResourceTable getResource() {
		return myResource;
	}

	public SearchParam getSearchParam() {
		return mySearchParam;
	}

	public boolean isPresent() {
		return myPresent;
	}

	public void setPresent(boolean thePresent) {
		myPresent = thePresent;
	}

	public void setResource(ResourceTable theResourceTable) {
		myResource = theResourceTable;
	}

	public void setSearchParam(SearchParam theSearchParam) {
		mySearchParam = theSearchParam;
	}

	@Override
	public String toString() {
		ToStringBuilder b = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE);
		
		b.append("res_pid", myResource.getIdDt().toUnqualifiedVersionless().getValue());
		b.append("param", mySearchParam.getParamName());
		b.append("present", myPresent);
		return b.build();
	}

}
