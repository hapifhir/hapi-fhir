/*-
 * #%L
 * HAPI FHIR JPA Hibernate Services
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
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
package ca.uhn.hapi.fhir.sql.hibernatesvc;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * This is a marker for hibernate @{@link jakarta.persistence.Entity} classes.
 * This annotation should be placed on any hibernate entity fields which should
 * be used as a part of the entity ID (i.e. the compound Primary Key) when
 * operating in Database Partition Mode, and should be filtered from the PK
 * when not operating in that mode.
 * <p>
 * This annotation should be placed on a field, where that field is
 * a @{@link jakarta.persistence.Column} field that is either:
 * <ul>
 *     <li>
 *         Marked with @{@link jakarta.persistence.Id}
 *     </li>
 *     <li>
 *         Is a part of an @{@link jakarta.persistence.Embeddable} class
 *         intended to be used as an @{@link jakarta.persistence.EmbeddedId}
 *     </li>
 * </ul>
 * </p>
 * <p>
 * This annotation is processed by the {@link DatabasePartitionModeIdFilteringMappingContributor}
 * </p>
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface PartitionedIdProperty {}
