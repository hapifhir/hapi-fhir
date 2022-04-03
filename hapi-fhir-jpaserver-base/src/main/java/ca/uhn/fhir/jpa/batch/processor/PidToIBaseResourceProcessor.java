package ca.uhn.fhir.jpa.batch.processor;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.batch.log.Logs;
import ca.uhn.fhir.jpa.dao.ISearchBuilder;
import ca.uhn.fhir.jpa.dao.SearchBuilderFactory;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Reusable Item Processor which converts ResourcePersistentIds to their IBaseResources
 */
public class PidToIBaseResourceProcessor implements ItemProcessor<List<ResourcePersistentId>, List<IBaseResource>> {
	 private static final Logger ourLog = Logs.getBatchTroubleshootingLog();

	@Autowired
	private SearchBuilderFactory mySearchBuilderFactory;

	@Autowired
	private DaoRegistry myDaoRegistry;


	@Value("#{stepExecutionContext['resourceType']}")
	private String myResourceType;

	@Autowired
	private FhirContext myContext;

	@Override
	public List<IBaseResource> process(List<ResourcePersistentId> theResourcePersistentId) {
		String collect = theResourcePersistentId.stream().map(pid -> pid.getId().toString()).collect(Collectors.joining(","));
		ourLog.trace("Processing PIDs: {}" + collect);

		IFhirResourceDao<?> dao = myDaoRegistry.getResourceDao(myResourceType);
		Class<? extends IBaseResource> resourceTypeClass = myContext.getResourceDefinition(myResourceType).getImplementingClass();

		ISearchBuilder sb = mySearchBuilderFactory.newSearchBuilder(dao, myResourceType, resourceTypeClass);
		List<IBaseResource> outgoing = new ArrayList<>();
		sb.loadResourcesByPid(theResourcePersistentId, Collections.emptyList(), outgoing, false, null);

		ourLog.trace("Loaded resources: {}", outgoing.stream().filter(t -> t != null).map(t -> t.getIdElement().getValue()).collect(Collectors.joining(", ")));

		return outgoing;

	}

}
