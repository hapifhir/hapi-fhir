/*-
 * #%L
 * HAPI FHIR - Master Data Management
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
package ca.uhn.fhir.mdm.rules.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonMappingException;

/**
 * Thrown while deserializing the {@code eidSystems} map of an MDM rules document when an entry is
 * neither an EID system URI nor an array of them.
 * <p>
 * A rules document is written by an implementer, so a malformed {@code eidSystems} entry is a
 * configuration error rather than an internal one. It is a distinct type so that
 * {@link ca.uhn.fhir.mdm.rules.config.MdmSettings} can recognise it and report it as such, without
 * having to guess at the cause of every other Jackson failure in the document.
 * </p>
 */
// Created by claude-opus-5
public class InvalidEidSystemsException extends JsonMappingException {

	private static final long serialVersionUID = 1L;

	public InvalidEidSystemsException(JsonParser theParser, String theMessage) {
		super(theParser, theMessage);
	}
}
