/*-
 * #%L
 * HAPI FHIR - Core Library
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
package ca.uhn.fhir.context.support;

/**
 * What ValueSet membership does when a {@code ValueSet.compose.include} or {@code exclude} names a CodeSystem the
 * server does not have, or a version of it the server does not have. This applies to {@code $expand},
 * {@code $validate-code}, resource validation, pre-expansion, {@code :in} / {@code :not-in} searches and
 * authorization rules over a ValueSet.
 *
 * @since 9.0.0
 */
// Created by Claude Opus 5.5
public enum MissingCodeSystemPolicy {

	/**
	 * The include or exclude cannot be used: {@code $expand} fails and the code is not accepted, naming the
	 * {@code url} (or {@code url|version}) that could not be found. This is the default.
	 */
	REJECT,

	/**
	 * When another version of the CodeSystem is installed, the include or exclude is applied to the version the
	 * server uses when no version is named. A CodeSystem not installed in any version is rejected as with
	 * {@link #REJECT}. An authorization rule over the ValueSet does not treat a code the
	 * installed version lacks as outside the ValueSet, so it denies, as with {@link #REJECT}.
	 */
	USE_INSTALLED_VERSION,

	/**
	 * As {@link #USE_INSTALLED_VERSION}, and in addition, when the CodeSystem is not installed in any version or is
	 * stored with {@code content} {@code not-present}, a code that an include or exclude lists in {@code concept} is
	 * taken as a member without being checked against the CodeSystem. An include or exclude of the
	 * whole CodeSystem, or one with a filter, is still rejected as with {@link #REJECT}.
	 * <p>
	 * Authorization rules over the ValueSet then accept codes that nothing has checked.
	 * </p>
	 */
	ACCEPT_LISTED_CODES;

	/**
	 * Whether an include or exclude naming a CodeSystem version the server does not have is applied to the installed
	 * version, as under {@link #USE_INSTALLED_VERSION}
	 *
	 * @return {@literal true} for {@link #USE_INSTALLED_VERSION} and {@link #ACCEPT_LISTED_CODES}
	 */
	public boolean isInstalledVersionUsed() {
		return this == USE_INSTALLED_VERSION || this == ACCEPT_LISTED_CODES;
	}

	/**
	 * Whether the codes an include or exclude lists from a CodeSystem the server cannot check them against are
	 * accepted, as under {@link #ACCEPT_LISTED_CODES}
	 *
	 * @return {@literal true} for {@link #ACCEPT_LISTED_CODES}
	 */
	public boolean isListedCodesAccepted() {
		return this == ACCEPT_LISTED_CODES;
	}
}
