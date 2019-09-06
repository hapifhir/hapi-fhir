package ca.uhn.fhir.jpa.entity;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2019 University Health Network
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

import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
@Immutable
@Subselect("select " +
	"   CONCAT(vsc.PID, ' ', cd.PID) as PID," +
	"   vsc.PID as CONCEPT_PID, " +
	"   cd.PID as DESIG_PID, " +
	"   vsc.CODEVAL as CONCEPT_CODE, " +
	"   vsc.DISPLAY as CONCEPT_DISPLAY, " +
	"   vsc.VALUESET_ORDER as CONCEPT_ORDER, " +
	"   vsc.SYSTEM_URL as CONCEPT_SYSTEM_URL, " +
	"   vsc.VALUESET_PID as CONCEPT_VS_PID, " +
//	"   cd.VALUESET_CONCEPT_PID as VALUESET2_35_1_, " +
	"   cd.LANG as DESIG_LANG, " +
	"   cd.USE_CODE as DESIG_USE_CODE, " +
	"   cd.USE_DISPLAY as DESIG_USE_DISPLAY, " +
	"   cd.USE_SYSTEM as DESIG_USE_SYSTEM, " +
	"   cd.VAL as DESIG_VAL " +
	"   from TRM_VALUESET_CONCEPT vsc " +
	"   left outer join TRM_VALUESET_C_DESIGNATION cd on vsc.PID=cd.VALUESET_CONCEPT_PID "
//	+
//	"   order by vsc.VALUESET_ORDER"
)
public class TermValueSetConceptView implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="PID")
	private String myPid;
	@Column(name = "CONCEPT_PID")
	private Long myConceptPid;
	@Column(name = "DESIG_PID")
	private Long myDesigPid;
	@Column(name = "CONCEPT_CODE")
	private String myConceptCode;
	@Column(name = "CONCEPT_DISPLAY")
	private String myConceptDisplay;
	@Column(name = "CONCEPT_ORDER")
	private int myConceptOrder;
	@Column(name = "CONCEPT_SYSTEM_URL")
	private String myConceptSystemUrl;
	@Column(name = "CONCEPT_VS_PID")
	private Long myConceptValueSetPid;
	@Column(name = "DESIG_LANG")
	private String myDesigLang;
	@Column(name = "DESIG_USE_CODE")
	private String myDesigUseCode;
	@Column(name = "DESIG_USE_DISPLAY")
	private String myDesigUseDisplay;
	@Column(name = "DESIG_USE_SYSTEM")
	private String myDesigUseSystem;
	@Column(name = "DESIG_VAL")
	private String myDesigVal;


	public Long getConceptPid() {
		return myConceptPid;
	}

	public Long getDesigPid() {
		return myDesigPid;
	}

	public String getConceptCode() {
		return myConceptCode;
	}

	public String getConceptDisplay() {
		return myConceptDisplay;
	}

	public int getConceptOrder() {
		return myConceptOrder;
	}

	public String getConceptSystemUrl() {
		return myConceptSystemUrl;
	}

	public Long getConceptValueSetPid() {
		return myConceptValueSetPid;
	}

	public String getDesigLang() {
		return myDesigLang;
	}

	public String getDesigUseCode() {
		return myDesigUseCode;
	}

	public String getDesigUseDisplay() {
		return myDesigUseDisplay;
	}

	public String getDesigUseSystem() {
		return myDesigUseSystem;
	}

	public String getDesigVal() {
		return myDesigVal;
	}
}
