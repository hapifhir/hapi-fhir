package ca.uhn.fhir.jpa.batch.processors;

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
	public List<IBaseResource> process(List<ResourcePersistentId> theResourcePersistentId) throws Exception {

		IFhirResourceDao dao = myDaoRegistry.getResourceDao(myResourceType);
		Class<? extends IBaseResource> resourceTypeClass = myContext.getResourceDefinition(myResourceType).getImplementingClass();

		ISearchBuilder sb = mySearchBuilderFactory.newSearchBuilder(dao, myResourceType, resourceTypeClass);
		List<IBaseResource> outgoing = new ArrayList<>();
		sb.loadResourcesByPid(theResourcePersistentId, Collections.emptyList(), outgoing, false, null);
		ourLog.trace("Loaded resources: {}", outgoing.stream().map(Object::toString).collect(Collectors.joining(", ")));
		return outgoing;

	}

}
