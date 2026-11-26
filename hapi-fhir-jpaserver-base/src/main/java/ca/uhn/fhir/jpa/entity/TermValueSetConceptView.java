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

import jakarta.persistence.Entity;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

/**
 * {@link TermValueSetConceptViewPartitioned} and {@link TermValueSetConceptViewOracle} have their own copy of this
 * SQL: change a column in all three.
 */
@Entity
@Immutable
@Subselect(
		/*
		 * Note about the CONCAT function below- We need a primary key (an @Id) column
		 * because hibernate won't allow the view the function without it, but
		 */
		"SELECT CONCAT_WS(' ', vsc.PID, vscd.PID) AS PID, " + "       vsc.PID                         AS CONCEPT_PID, "
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
				+ "LEFT OUTER JOIN TRM_VALUESET_C_DESIGNATION vscd ON vsc.PID = vscd.VALUESET_CONCEPT_PID")
public class TermValueSetConceptView extends BaseTermValueSetConceptView {
	private static final long serialVersionUID = 1L;
}
