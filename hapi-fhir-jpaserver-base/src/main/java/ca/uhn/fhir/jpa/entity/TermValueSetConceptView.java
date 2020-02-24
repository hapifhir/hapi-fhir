package ca.uhn.fhir.jpa.entity;

/*
 * #%L
 * HAPI FHIR JPA Server
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

import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
@Immutable
@Subselect(
	/*
	 * Note about the CONCAT function below- We need a primary key (an @Id) column
	 * because hibernate won't allow the view the function without it, but
	 */
	"SELECT CONCAT(vsc.PID, CONCAT(' ', vscd.PID)) AS PID, " +
	"       vsc.PID              AS CONCEPT_PID, " +
	"       vsc.VALUESET_PID     AS CONCEPT_VALUESET_PID, " +
	"       vsc.VALUESET_ORDER   AS CONCEPT_VALUESET_ORDER, " +
	"       vsc.SYSTEM_URL       AS CONCEPT_SYSTEM_URL, " +
	"       vsc.CODEVAL          AS CONCEPT_CODEVAL, " +
	"       vsc.DISPLAY          AS CONCEPT_DISPLAY, " +
	"       vscd.PID             AS DESIGNATION_PID, " +
	"       vscd.LANG            AS DESIGNATION_LANG, " +
	"       vscd.USE_SYSTEM      AS DESIGNATION_USE_SYSTEM, " +
	"       vscd.USE_CODE        AS DESIGNATION_USE_CODE, " +
	"       vscd.USE_DISPLAY     AS DESIGNATION_USE_DISPLAY, " +
	"       vscd.VAL             AS DESIGNATION_VAL " +
	"FROM TRM_VALUESET_CONCEPT vsc " +
	"LEFT OUTER JOIN TRM_VALUESET_C_DESIGNATION vscd ON vsc.PID = vscd.VALUESET_CONCEPT_PID"
)
public class TermValueSetConceptView implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="PID", length = 1000 /* length only needed to satisfy JpaEntityTest, it's not used*/)
	private String id; // still set automatically

	@Column(name = "CONCEPT_PID")
	private Long myConceptPid;

	@Column(name = "CONCEPT_VALUESET_PID")
	private Long myConceptValueSetPid;

	@Column(name = "CONCEPT_VALUESET_ORDER")
	private int myConceptOrder;

	@Column(name = "CONCEPT_SYSTEM_URL", length = TermCodeSystem.MAX_URL_LENGTH)
	private String myConceptSystemUrl;

	@Column(name = "CONCEPT_CODEVAL", length = TermConcept.MAX_CODE_LENGTH)
	private String myConceptCode;

	@Column(name = "CONCEPT_DISPLAY", length = TermConcept.MAX_DESC_LENGTH)
	private String myConceptDisplay;

	@Column(name = "DESIGNATION_PID")
	private Long myDesignationPid;

	@Column(name = "DESIGNATION_LANG", length = TermConceptDesignation.MAX_LENGTH)
	private String myDesignationLang;

	@Column(name = "DESIGNATION_USE_SYSTEM", length = TermConceptDesignation.MAX_LENGTH)
	private String myDesignationUseSystem;

	@Column(name = "DESIGNATION_USE_CODE", length = TermConceptDesignation.MAX_LENGTH)
	private String myDesignationUseCode;

	@Column(name = "DESIGNATION_USE_DISPLAY", length = TermConceptDesignation.MAX_LENGTH)
	private String myDesignationUseDisplay;

	@Column(name = "DESIGNATION_VAL", length = TermConceptDesignation.MAX_VAL_LENGTH)
	private String myDesignationVal;


	public Long getConceptPid() {
		return myConceptPid;
	}

	public String getConceptSystemUrl() {
		return myConceptSystemUrl;
	}

	public String getConceptCode() {
		return myConceptCode;
	}

	public String getConceptDisplay() {
		return myConceptDisplay;
	}

	public Long getDesignationPid() {
		return myDesignationPid;
	}

	public String getDesignationLang() {
		return myDesignationLang;
	}

	public String getDesignationUseSystem() {
		return myDesignationUseSystem;
	}

	public String getDesignationUseCode() {
		return myDesignationUseCode;
	}

	public String getDesignationUseDisplay() {
		return myDesignationUseDisplay;
	}

	public String getDesignationVal() {
		return myDesignationVal;
	}
}
