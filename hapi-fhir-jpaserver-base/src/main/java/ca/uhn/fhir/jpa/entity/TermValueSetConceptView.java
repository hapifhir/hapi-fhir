package ca.uhn.fhir.jpa.entity;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import org.apache.commons.io.IOUtils;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.sql.Clob;
import java.sql.SQLException;

@Entity
@Immutable
@Subselect(
	/*
	 * Note about the CONCAT function below- We need a primary key (an @Id) column
	 * because hibernate won't allow the view the function without it, but
	 */
	"SELECT CONCAT_WS(' ', vsc.PID, vscd.PID) AS PID, " +
		"       vsc.PID                         AS CONCEPT_PID, " +
		"       vsc.VALUESET_PID                AS CONCEPT_VALUESET_PID, " +
		"       vsc.VALUESET_ORDER              AS CONCEPT_VALUESET_ORDER, " +
		"       vsc.SYSTEM_URL                  AS CONCEPT_SYSTEM_URL, " +
		"       vsc.CODEVAL                     AS CONCEPT_CODEVAL, " +
		"       vsc.DISPLAY                     AS CONCEPT_DISPLAY, " +
		"       vsc.SYSTEM_VER                  AS SYSTEM_VER, " +
		"       vsc.SOURCE_PID                  AS SOURCE_PID, " +
		"       vsc.SOURCE_DIRECT_PARENT_PIDS   AS SOURCE_DIRECT_PARENT_PIDS, " +
		"       vscd.PID                        AS DESIGNATION_PID, " +
		"       vscd.LANG                       AS DESIGNATION_LANG, " +
		"       vscd.USE_SYSTEM                 AS DESIGNATION_USE_SYSTEM, " +
		"       vscd.USE_CODE                   AS DESIGNATION_USE_CODE, " +
		"       vscd.USE_DISPLAY                AS DESIGNATION_USE_DISPLAY, " +
		"       vscd.VAL                        AS DESIGNATION_VAL " +
		"FROM TRM_VALUESET_CONCEPT vsc " +
		"LEFT OUTER JOIN TRM_VALUESET_C_DESIGNATION vscd ON vsc.PID = vscd.VALUESET_CONCEPT_PID"
)
public class TermValueSetConceptView implements Serializable, ITermValueSetConceptView {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "PID", length = 1000 /* length only needed to satisfy JpaEntityTest, it's not used*/)
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

	@Column(name="SYSTEM_VER", length = TermCodeSystemVersion.MAX_VERSION_LENGTH)
	private String myConceptSystemVersion;

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

	@Column(name = "SOURCE_PID", nullable = true)
	private Long mySourceConceptPid;

	@Lob
	@Column(name = "SOURCE_DIRECT_PARENT_PIDS", nullable = true)
	private Clob mySourceConceptDirectParentPids;

	@Override
	public Long getSourceConceptPid() {
		return mySourceConceptPid;
	}

	@Override
	public String getSourceConceptDirectParentPids() {
		if (mySourceConceptDirectParentPids != null) {
			try (Reader characterStream = mySourceConceptDirectParentPids.getCharacterStream()) {
				return IOUtils.toString(characterStream);
			} catch (IOException | SQLException e) {
				throw new InternalErrorException(Msg.code(828) + e);
			}
		}
		return null;
	}

	@Override
	public Long getConceptPid() {
		return myConceptPid;
	}

	@Override
	public String getConceptSystemUrl() {
		return myConceptSystemUrl;
	}

	@Override
	public String getConceptCode() {
		return myConceptCode;
	}

	@Override
	public String getConceptDisplay() {
		return myConceptDisplay;
	}

	@Override
	public Long getDesignationPid() {
		return myDesignationPid;
	}

	@Override
	public String getDesignationLang() {
		return myDesignationLang;
	}

	@Override
	public String getDesignationUseSystem() {
		return myDesignationUseSystem;
	}

	@Override
	public String getDesignationUseCode() {
		return myDesignationUseCode;
	}

	@Override
	public String getDesignationUseDisplay() {
		return myDesignationUseDisplay;
	}

	@Override
	public String getDesignationVal() {
		return myDesignationVal;
	}

	@Override
	public String getConceptSystemVersion() {
		return myConceptSystemVersion;
	}

}
