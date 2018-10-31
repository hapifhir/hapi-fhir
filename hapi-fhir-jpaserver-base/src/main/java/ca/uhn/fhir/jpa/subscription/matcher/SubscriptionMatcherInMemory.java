package ca.uhn.fhir.jpa.subscription.matcher;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.jpa.dao.SearchParameterMap;
import ca.uhn.fhir.jpa.dao.index.ResourceIndexedSearchParams;
import ca.uhn.fhir.jpa.entity.ResourceTable;
import ca.uhn.fhir.jpa.service.MatchUrlService;
import ca.uhn.fhir.jpa.subscription.ResourceModifiedMessage;
import ca.uhn.fhir.model.api.IQueryParameterType;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@Lazy
public class SubscriptionMatcherInMemory implements ISubscriptionMatcher {
	private Logger ourLog = LoggerFactory.getLogger(SubscriptionMatcherInMemory.class);

	@Autowired
	private FhirContext myContext;

	@Autowired
	private AutowireCapableBeanFactory beanFactory;

	@Autowired
	private MatchUrlService myMatchUrlService;
	
	@Override
	public boolean match(String criteria, ResourceModifiedMessage msg) {
		IBaseResource resource = msg.getNewPayload(myContext);
		ResourceTable entity = new ResourceTable();
		RuntimeResourceDefinition resourceDefinition = myContext.getResourceDefinition(resource);
		entity.setResourceType(resourceDefinition.getName());
		ResourceIndexedSearchParams searchParams = beanFactory.getBean(ResourceIndexedSearchParams.class);
		searchParams.extractFromResource(entity, resource);

		SearchParameterMap searchParameterMap = myMatchUrlService.translateMatchUrl(criteria, resourceDefinition);

		for (Map.Entry<String, List<List<? extends IQueryParameterType>>> entry : searchParameterMap.entrySet()) {
			for (List<? extends IQueryParameterType> parameters : entry.getValue()) {
				for (IQueryParameterType param : parameters) {
					ourLog.debug("Param {}: {}", entry, param);
				}
			}
		}

		// FIXME KHS implement

		// Incoming resource fields in searchParams
		// Search criteria in searchParameterMap
		// Now just need to marry the two and we're golden
		return true;
	}
}
