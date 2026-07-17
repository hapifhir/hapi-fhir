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
package ca.uhn.fhir.jpa.dao.index;

import ca.uhn.fhir.rest.api.RestSearchParameterTypeEnum;

/**
 * Controls whether {@link DaoSearchParamSynchronizer} writes a given built-in JPA search-parameter
 * index. This is the suppression counterpart to {@link ICustomIndexSynchronizer}: it lets an
 * extension stop HAPI from writing a built-in index once a custom index has fully taken over that
 * responsibility (for example, the compressed-only phase of Smile CDR's token index migration).
 *
 * <p>The policy is consulted once per scalar built-in index type for each resource being indexed:
 * {@link RestSearchParameterTypeEnum#STRING STRING}, {@link RestSearchParameterTypeEnum#TOKEN TOKEN},
 * {@link RestSearchParameterTypeEnum#NUMBER NUMBER}, {@link RestSearchParameterTypeEnum#QUANTITY
 * QUANTITY} (gating both the regular and the normalized quantity tables),
 * {@link RestSearchParameterTypeEnum#DATE DATE}, {@link RestSearchParameterTypeEnum#URI URI} and
 * {@link RestSearchParameterTypeEnum#SPECIAL SPECIAL} (coords). It is never consulted for resource
 * links or combo (unique / non-unique) indexes, which carry no single search-parameter semantics.
 * When an index type is vetoed, its synchronization is skipped entirely: no new rows are written and
 * pre-existing rows are left untouched (cleanup of stale rows is the extension's responsibility).
 *
 * <p>Suppression and contribution are independent axes — during a zero-downtime migration both the
 * built-in and the custom index are written at once (contribution on, suppression off), so this
 * policy is deliberately separate from {@link ICustomIndexSynchronizer}.
 *
 * <p>Implementations are discovered as Spring beans. A built-in index is written only if
 * <em>every</em> registered policy allows it; any policy may veto. Vanilla HAPI registers no
 * policies, so it always writes its built-in indexes.
 *
 * <p><b>Note:</b> this is an internal extension point and its signature may evolve. If finer-grained
 * suppression (per search parameter rather than per index type) is ever needed, it is expected to
 * arrive as an additional {@code default} method so existing type-level implementations keep working.
 */
@FunctionalInterface
public interface IBuiltInIndexWritePolicy {

	/**
	 * @param theResourceType the resource type being indexed (e.g. {@code "Patient"})
	 * @param theParamType    the built-in index type in question (e.g. {@link RestSearchParameterTypeEnum#TOKEN})
	 * @return {@code true} if HAPI should write its built-in index of this type; {@code false} to suppress it
	 */
	boolean shouldWriteBuiltInIndex(String theResourceType, RestSearchParameterTypeEnum theParamType);
}
