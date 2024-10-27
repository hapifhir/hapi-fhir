/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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

/**
 * This package stores the mechanism for Externally Stored Resources (ESRs).
 * <p>
 * A normal resource stores its contents in the {@link ca.uhn.fhir.jpa.model.entity.ResourceHistoryTable}
 * entity as text (compressed JSON or otherwise) with one row in that table per version of the
 * resource. An ESR still creates a row in that table, but stores a reference to actual body
 * storage instead.
 * <p>
 * THIS IS AN EXPERIMENTAL API - IT MAY GO AWAY OR CHANGE IN THE FUTURE
 *
 * @since 6.6.0
 */
package ca.uhn.fhir.jpa.esr;
