/*-
 * #%L
 * HAPI FHIR - CDS Hooks
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
package ca.uhn.hapi.fhir.cdshooks.svc.cr;

import ca.uhn.fhir.jpa.cache.IResourceChangeEvent;
import ca.uhn.fhir.jpa.cache.IResourceChangeListener;
import ca.uhn.fhir.jpa.cache.ResourceChangeEvent;
import ca.uhn.hapi.fhir.cdshooks.svc.CdsServiceRegistryImpl;
import org.hl7.fhir.instance.model.api.IIdType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static ca.uhn.hapi.fhir.cdshooks.svc.cr.CdsCrConstants.CDS_CR_MODULE_ID;

public class CdsServiceInterceptor implements IResourceChangeListener {
	static final Logger ourLog = LoggerFactory.getLogger(CdsServiceInterceptor.class);

	@Autowired
	CdsServiceRegistryImpl myCdsServiceRegistry;

	public CdsServiceInterceptor() {}

	@Override
	public void handleInit(Collection<IIdType> theResourceIds) {
		handleChange(ResourceChangeEvent.fromCreatedUpdatedDeletedResourceIds(
				new ArrayList<>(theResourceIds), Collections.emptyList(), Collections.emptyList()));
	}

	@Override
	public void handleChange(IResourceChangeEvent theResourceChangeEvent) {
		if (theResourceChangeEvent == null) return;
		if (theResourceChangeEvent.getCreatedResourceIds() != null
				&& !theResourceChangeEvent.getCreatedResourceIds().isEmpty()) {
			insert(theResourceChangeEvent.getCreatedResourceIds());
		}
		if (theResourceChangeEvent.getUpdatedResourceIds() != null
				&& !theResourceChangeEvent.getUpdatedResourceIds().isEmpty()) {
			update(theResourceChangeEvent.getUpdatedResourceIds());
		}
		if (theResourceChangeEvent.getDeletedResourceIds() != null
				&& !theResourceChangeEvent.getDeletedResourceIds().isEmpty()) {
			delete(theResourceChangeEvent.getDeletedResourceIds());
		}
	}

	private void insert(List<IIdType> theCreatedIds) {
		for (IIdType id : theCreatedIds) {
			try {
				myCdsServiceRegistry.registerCrService(id.getIdPart());
			} catch (Exception e) {
				ourLog.info(String.format("Failed to create service for %s", id.getIdPart()));
			}
		}
	}

	private void update(List<IIdType> updatedIds) {
		try {
			delete(updatedIds);
			insert(updatedIds);
		} catch (Exception e) {
			ourLog.info(String.format("Failed to update service(s) for %s", updatedIds));
		}
	}

	private void delete(List<IIdType> deletedIds) {
		for (IIdType id : deletedIds) {
			myCdsServiceRegistry.unregisterService(id.getIdPart(), CDS_CR_MODULE_ID);
		}
	}
}
