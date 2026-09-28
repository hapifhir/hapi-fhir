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
package ca.uhn.fhir.jpa.config.util;

import ca.uhn.hapi.fhir.sql.hibernatesvc.DatabasePartitionModeIdFilteringMappingContributor;
import org.hibernate.boot.spi.EffectiveMappingDefaults;
import org.hibernate.boot.spi.InFlightMetadataCollector;
import org.hibernate.boot.spi.MetadataImplementor;
import org.hibernate.envers.boot.internal.EnversServiceImpl;
import org.hibernate.envers.configuration.internal.MappingCollector;

import java.util.Map;
import java.util.function.Supplier;

/**
 * Envers' service, made to remove the partition id from entity ids before Envers reads them.
 * <p>
 * Envers and {@link DatabasePartitionModeIdFilteringMappingContributor} are both Hibernate mapping contributors,
 * which Hibernate runs in classpath order. Since Hibernate 7, Envers captures the type of every entity id while it
 * initializes, so if it runs first it keeps the ids with the partition id, and SessionFactory startup fails with
 * "identifier mapping has wrong number of columns". This service runs the filter at the start of Envers'
 * initialization, whichever contributor Hibernate runs first.
 * </p>
 * <p>
 * It is registered as a provided service, which Hibernate does not configure, so it configures itself from the
 * registry settings the first time it is used.
 * </p>
 */
// Created by Claude Opus 5.5
class HapiEnversService extends EnversServiceImpl {

	private final Supplier<Map<String, Object>> mySettings;
	private boolean myConfigured;

	/**
	 * @param theSettings the settings of the service registry this service is registered in; read on first use,
	 *                    once they are complete
	 */
	HapiEnversService(Supplier<Map<String, Object>> theSettings) {
		mySettings = theSettings;
	}

	@Override
	public boolean isEnabled() {
		configureOnce();
		return super.isEnabled();
	}

	@Override
	public void initialize(
			MetadataImplementor theMetadata,
			MappingCollector theMappingCollector,
			EffectiveMappingDefaults theEffectiveMappingDefaults) {
		configureOnce();
		new DatabasePartitionModeIdFilteringMappingContributor()
				.filterPartitionIds((InFlightMetadataCollector) theMetadata);
		super.initialize(theMetadata, theMappingCollector, theEffectiveMappingDefaults);
	}

	private synchronized void configureOnce() {
		if (!myConfigured) {
			configure(mySettings.get());
			myConfigured = true;
		}
	}
}
