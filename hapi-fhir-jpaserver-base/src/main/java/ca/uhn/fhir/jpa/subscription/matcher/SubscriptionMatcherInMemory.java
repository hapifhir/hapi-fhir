package ca.uhn.fhir.jpa.subscription.matcher;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.jpa.dao.SearchParameterMap;
import ca.uhn.fhir.jpa.dao.index.ResourceIndexedSearchParams;
import ca.uhn.fhir.jpa.entity.ResourceTable;
import ca.uhn.fhir.jpa.service.MatchUrlService;
import ca.uhn.fhir.jpa.subscription.ResourceModifiedMessage;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
@Lazy
public class SubscriptionMatcherInMemory implements ISubscriptionMatcher {

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

		// FIXME KHS implement

		// Incoming resource fields in searchParams
		// Search criteria in searchParameterMap
		// Now just need to marry the two and we're golden
		return true;
	}
}
