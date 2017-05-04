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

import javax.persistence.*;

@Entity
@Table(name = "HFJ_SEARCH_PARM", uniqueConstraints= {
	@UniqueConstraint(name="IDX_SEARCHPARM_RESTYPE_SPNAME", columnNames= {"RES_TYPE", "PARAM_NAME"})
})
public class SearchParam {

	@Id
	@SequenceGenerator(name = "SEQ_SEARCHPARM_ID", sequenceName = "SEQ_SEARCHPARM_ID")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_SEARCHPARM_ID")
	@Column(name = "PID")
	private Long myId;

	@Column(name="PARAM_NAME", length=BaseResourceIndexedSearchParam.MAX_SP_NAME, nullable=false, updatable=false)
	private String myParamName;

	@Column(name="RES_TYPE", length=ResourceTable.RESTYPE_LEN, nullable=false, updatable=false)
	private String myResourceName;

	public String getParamName() {
		return myParamName;
	}

	public void setParamName(String theParamName) {
		myParamName = theParamName;
	}

	public void setResourceName(String theResourceName) {
		myResourceName = theResourceName;
	}

	public Long getId() {
		return myId;
	}
	
}
