/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.MappedSuperclass;
import org.apache.commons.io.IOUtils;
import org.hibernate.Length;

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.sql.Clob;
import java.sql.SQLException;

/**
 * Fields shared by {@link TermValueSetConceptView} and {@link TermValueSetConceptViewPartitioned}, which each
 * supply the SQL.
 */
// Created by Claude Opus 5.5
@MappedSuperclass
public abstract class BaseTermValueSetConceptView implements Serializable, ITermValueSetConceptView {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "PID", length = 1000 /* length only needed to satisfy JpaEntityTest, it's not used*/)
	private String id; // still set automatically

	@Column(name = "CONCEPT_PID")
	private Long myConceptPid;

	@Column(name = "CONCEPT_VALUESET_PID")
	private Long myConceptValueSetPid;

	@Column(name = "CONCEPT_VALUESET_ORDER", nullable = false)
	private int myConceptOrder;

	@Column(name = "CONCEPT_SYSTEM_URL", length = TermCodeSystem.MAX_URL_LENGTH)
	private String myConceptSystemUrl;

	@Column(name = "CONCEPT_CODEVAL", length = TermConcept.MAX_CODE_LENGTH)
	private String myConceptCode;

	@Column(name = "CONCEPT_DISPLAY", length = TermConcept.MAX_DESC_LENGTH)
	private String myConceptDisplay;

	@Column(name = "SYSTEM_VER", length = TermCodeSystemVersion.MAX_VERSION_LENGTH)
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

	@Column(name = "SOURCE_DIRECT_PARENT_PIDS_VC", nullable = true, length = Length.LONG32)
	private String mySourceConceptDirectParentPidsVc;

	@Nullable
	@Override
	public Long getSourceConceptPid() {
		return mySourceConceptPid;
	}

	@Nullable
	@Override
	public String getSourceConceptDirectParentPids() {
		String retVal = null;

		if (mySourceConceptDirectParentPids != null) {
			try (Reader characterStream = mySourceConceptDirectParentPids.getCharacterStream()) {
				retVal = IOUtils.toString(characterStream);
			} catch (IOException | SQLException e) {
				throw new InternalErrorException(Msg.code(828) + e);
			}
		} else if (mySourceConceptDirectParentPidsVc != null) {
			retVal = mySourceConceptDirectParentPidsVc;
		}

		return retVal;
	}

	@Nonnull
	@Override
	public Long getConceptPid() {
		return myConceptPid;
	}

	@Nonnull
	@Override
	public String getConceptSystemUrl() {
		return myConceptSystemUrl;
	}

	@Nonnull
	@Override
	public String getConceptCode() {
		return myConceptCode;
	}

	@Nullable
	@Override
	public String getConceptDisplay() {
		return myConceptDisplay;
	}

	@Nullable
	@Override
	public Long getDesignationPid() {
		return myDesignationPid;
	}

	@Nullable
	@Override
	public String getDesignationLang() {
		return myDesignationLang;
	}

	@Nullable
	@Override
	public String getDesignationUseSystem() {
		return myDesignationUseSystem;
	}

	@Nullable
	@Override
	public String getDesignationUseCode() {
		return myDesignationUseCode;
	}

	@Nullable
	@Override
	public String getDesignationUseDisplay() {
		return myDesignationUseDisplay;
	}

	@Nullable
	@Override
	public String getDesignationVal() {
		return myDesignationVal;
	}

	@Nullable
	@Override
	public String getConceptSystemVersion() {
		return myConceptSystemVersion;
	}
}
