/*-
 * #%L
 * HAPI FHIR JPA Hibernate Services
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
package ca.uhn.hapi.fhir.sql.hibernatesvc;

// Created by claude-opus-4-8
/**
 * Selects how the database sequence id generator allocates new resource ids. Additional allocation
 * strategies can be added here - for example environment-specific strategies (such as one tuned for
 * AWS Aurora Limitless) that may not be backed by a Hibernate pooled optimizer at all.
 */
public enum IdSequencePoolingStrategy {

	/**
	 * The legacy single shared pool: a single node-wide id pool guarded by a lock, so concurrent writers
	 * serialize on that lock while the pool is refilled from the database. This is the default and matches
	 * the behavior of all releases prior to 8.12.0.
	 */
	SHARED_POOL,

	/**
	 * A per-thread (thread-local) pool: each writer thread allocates ids from its own block, so concurrent
	 * writers do not serialize on a shared lock during pool refills. Enabling this should be done across an
	 * entire cluster at once (see the upgrade notes), never node by node, because this strategy and
	 * {@link #SHARED_POOL} interpret the same database sequence value differently.
	 */
	PER_THREAD_POOL
}
