package ca.uhn.fhir.jpa.subscription.matcher;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.dao.DaoConfig;
import ca.uhn.fhir.jpa.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.dao.data.IResourceHistoryTableDao;
import ca.uhn.fhir.jpa.dao.index.ResourceIndexedSearchParams;
import ca.uhn.fhir.jpa.entity.ResourceTable;
import ca.uhn.fhir.jpa.subscription.DaoProvider;
import ca.uhn.fhir.jpa.subscription.ResourceModifiedMessage;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import java.util.Date;

@Service
@Lazy
public class SubscriptionMatcherInMemory implements ISubscriptionMatcher {

	@Autowired
	private FhirContext myContext;

	@Autowired
	private AutowireCapableBeanFactory beanFactory;
	
	@Override
	public boolean match(String criteria, ResourceModifiedMessage msg) {
		IBaseResource resource = msg.getNewPayload(myContext);
		ResourceTable entity = new ResourceTable();
		entity.setResourceType(myContext.getResourceDefinition(resource).getName());
		ResourceIndexedSearchParams searchParams = beanFactory.getBean(ResourceIndexedSearchParams.class);
		searchParams.extractFromResource(entity, resource);
		// FIXME KHS implement
		// We have our search parameters in criteria
		// And we have our searchable fields broken out in searchParams
		// Now just need to apply the criteria to the searchParams
		return true;
	}
}
