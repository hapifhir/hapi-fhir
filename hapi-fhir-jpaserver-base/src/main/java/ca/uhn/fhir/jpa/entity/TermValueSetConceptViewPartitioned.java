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

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

/**
 * {@link TermValueSetConceptView} for database partition mode, where the designation join must match on
 * {@literal PARTITION_ID}. Outside this mode {@literal PARTITION_ID} can be NULL, so that join would drop designations.
 */
// Created by Claude Opus 5.5
@Entity
@Immutable
@Subselect(
		/*
		 * Hibernate needs an @Id column, so PID combines the concept and designation PIDs.
		 */
		"SELECT CONCAT_WS(' ', vsc.PID, vscd.PID) AS PID, " + "       vsc.PID                         AS CONCEPT_PID, "
				+ "       vsc.PARTITION_ID                   AS CONCEPT_PARTITION_ID, "
				+ "       vsc.VALUESET_PID                   AS CONCEPT_VALUESET_PID, "
				+ "       vsc.VALUESET_ORDER                 AS CONCEPT_VALUESET_ORDER, "
				+ "       vsc.SYSTEM_URL                     AS CONCEPT_SYSTEM_URL, "
				+ "       vsc.CODEVAL                        AS CONCEPT_CODEVAL, "
				+ "       vsc.DISPLAY                        AS CONCEPT_DISPLAY, "
				+ "       vsc.SYSTEM_VER                     AS SYSTEM_VER, "
				+ "       vsc.SOURCE_PID                     AS SOURCE_PID, "
				+ "       vsc.SOURCE_DIRECT_PARENT_PIDS      AS SOURCE_DIRECT_PARENT_PIDS, "
				+ "       vsc.SOURCE_DIRECT_PARENT_PIDS_VC   AS SOURCE_DIRECT_PARENT_PIDS_VC, "
				+ "       vscd.PID                           AS DESIGNATION_PID, "
				+ "       vscd.LANG                          AS DESIGNATION_LANG, "
				+ "       vscd.USE_SYSTEM                    AS DESIGNATION_USE_SYSTEM, "
				+ "       vscd.USE_CODE                      AS DESIGNATION_USE_CODE, "
				+ "       vscd.USE_DISPLAY                   AS DESIGNATION_USE_DISPLAY, "
				+ "       vscd.VAL                           AS DESIGNATION_VAL "
				+ "FROM TRM_VALUESET_CONCEPT vsc "
				+ "LEFT OUTER JOIN TRM_VALUESET_C_DESIGNATION vscd "
				+ "  ON vsc.PID = vscd.VALUESET_CONCEPT_PID AND vsc.PARTITION_ID = vscd.PARTITION_ID")
public class TermValueSetConceptViewPartitioned extends BaseTermValueSetConceptView {
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unused")
	@Column(name = "CONCEPT_PARTITION_ID")
	private Integer myConceptPartitionId;
}
