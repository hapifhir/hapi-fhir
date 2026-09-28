/*-
 * #%L
 * HAPI FHIR - Docs
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
package ca.uhn.hapi.fhir.docs;

public class ChangelogConstants {

	/** A new feature or capability being introduced. */
	public static final String TYPE_ADD = "add";

	/** A change to existing behaviour that is neither a bug fix nor a new feature. */
	public static final String TYPE_CHANGE = "change";

	/** A bug fix correcting incorrect or unintended behaviour. */
	public static final String TYPE_FIX = "fix";

	/** A performance improvement that does not otherwise change behaviour. */
	public static final String TYPE_PERFORMANCE = "perf";

	/** Removal of an existing feature, capability, or API. */
	public static final String TYPE_REMOVE = "remove";

	/** A security-related fix or hardening change. */
	public static final String TYPE_SECURITY = "security";

	/**
	 * Non-instantiable
	 */
	private ChangelogConstants() {}
}
