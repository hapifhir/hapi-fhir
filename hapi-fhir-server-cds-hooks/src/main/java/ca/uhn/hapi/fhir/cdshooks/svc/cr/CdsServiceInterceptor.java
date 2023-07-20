/*-
 * #%L
 * HAPI FHIR - CDS Hooks
 * %%
 * Copyright (C) 2014 - 2023 Smile CDR, Inc.
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

import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.cache.IResourceChangeEvent;
import ca.uhn.fhir.jpa.cache.IResourceChangeListener;
import ca.uhn.fhir.jpa.cache.ResourceChangeEvent;
import ca.uhn.hapi.fhir.cdshooks.api.json.CdsServiceJson;
import ca.uhn.hapi.fhir.cdshooks.svc.CdsServiceCache;
import ca.uhn.hapi.fhir.cdshooks.svc.CdsServiceRegistryImpl;
import ca.uhn.hapi.fhir.cdshooks.svc.cr.discovery.DiscoveryResolutionR4;
import ca.uhn.hapi.fhir.cdshooks.svc.cr.discovery.DiscoveryResolutionR5;
import ca.uhn.hapi.fhir.cdshooks.svc.cr.discovery.DiscoveryResolutionStu3;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class CdsServiceInterceptor implements IResourceChangeListener {
	static final Logger ourLog = LoggerFactory.getLogger(CdsServiceCache.class);

	@Autowired
	CdsServiceRegistryImpl myCdsServiceRegistry;

	final DaoRegistry myDaoRegistry;
	final DiscoveryResolutionStu3 myDiscoveryResolutionStu3;
	final DiscoveryResolutionR4 myDiscoveryResolutionR4;
	final DiscoveryResolutionR5 myDiscoveryResolutionR5;

	public CdsServiceInterceptor(DaoRegistry theDaoRegistry) {
		myDaoRegistry = theDaoRegistry;
		myDiscoveryResolutionStu3 = new DiscoveryResolutionStu3(myDaoRegistry);
		myDiscoveryResolutionR4 = new DiscoveryResolutionR4(myDaoRegistry);
		myDiscoveryResolutionR5 = new DiscoveryResolutionR5(myDaoRegistry);
	}

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

	private CdsServiceJson resolveService(IBaseResource thePlanDefinition) {
		if (thePlanDefinition instanceof org.hl7.fhir.dstu3.model.PlanDefinition) {
			return myDiscoveryResolutionStu3.resolveService((org.hl7.fhir.dstu3.model.PlanDefinition) thePlanDefinition);
		}
		if (thePlanDefinition instanceof org.hl7.fhir.r4.model.PlanDefinition) {
			return myDiscoveryResolutionR4.resolveService((org.hl7.fhir.r4.model.PlanDefinition) thePlanDefinition);
		}
		if (thePlanDefinition instanceof org.hl7.fhir.r5.model.PlanDefinition) {
			return myDiscoveryResolutionR5.resolveService((org.hl7.fhir.r5.model.PlanDefinition) thePlanDefinition);
		}
		return null;
	}

	private void insert(List<IIdType> theCreatedIds) {
		for (IIdType id : theCreatedIds) {
			try {
				IBaseResource resource =
						myDaoRegistry.getResourceDao("PlanDefinition").read(id);
				myCdsServiceRegistry.registerCrService(id.getIdPart(), resolveService(resource));
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
			myCdsServiceRegistry.unregisterService(id.getIdPart(), "CR");
		}
	}
}
