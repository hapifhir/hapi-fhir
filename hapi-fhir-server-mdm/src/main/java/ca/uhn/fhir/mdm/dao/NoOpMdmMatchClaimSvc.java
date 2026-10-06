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
package ca.uhn.fhir.mdm.dao;

import ca.uhn.fhir.rest.api.server.storage.IResourcePersistentId;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

/**
 * Claim service for storage backends that don't provide one. MDM processing behaves as it did before
 * match claims existed: safe with a single consumer, but not protected against concurrent processing.
 */
// Created by claude-opus-5-5
public class NoOpMdmMatchClaimSvc implements IMdmMatchClaimSvc {

	@Nonnull
	@Override
	public Map<MdmMatchClaimKey, Long> findExistingClaims(@Nonnull Collection<MdmMatchClaimKey> theKeys) {
		return Collections.emptyMap();
	}

	@Override
	public void claim(
			@Nonnull Collection<MdmMatchClaimKey> theKeys,
			@Nullable IResourcePersistentId<?> theClaimant,
			@Nonnull Map<MdmMatchClaimKey, Long> theTakeoverTokens) {
		// nothing to do
	}
}
