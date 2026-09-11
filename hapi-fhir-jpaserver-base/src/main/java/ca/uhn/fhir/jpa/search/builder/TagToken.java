/*-
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
package ca.uhn.fhir.jpa.search.builder;

/**
 * A single parsed token from a {@code _tag}/{@code _security}/{@code _profile} search parameter.
 *
 * @param system the tag system (may be {@code null}/blank, meaning "any system")
 * @param qualifier the parameter qualifier (e.g. {@code :below}), or {@code null} if none
 * @param code the tag code
 */
public record TagToken(String system, String qualifier, String code) {}
