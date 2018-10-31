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
	private CriteriaResourceMatcher myCriteriaResourceMatcher;

	@Override
	public SubscriptionMatchResult match(String criteria, ResourceModifiedMessage msg) {
		return match(criteria, msg.getNewPayload(myContext));
	}

	SubscriptionMatchResult match(String criteria, IBaseResource resource) {
		ResourceTable entity = new ResourceTable();
		entity.setResourceType(resource.getIdElement().getResourceType());
		ResourceIndexedSearchParams searchParams = beanFactory.getBean(ResourceIndexedSearchParams.class);
		searchParams.extractFromResource(entity, resource);
		RuntimeResourceDefinition resourceDefinition = myContext.getResourceDefinition(resource);
		return myCriteriaResourceMatcher.match(criteria, resourceDefinition, searchParams);
	}
}
