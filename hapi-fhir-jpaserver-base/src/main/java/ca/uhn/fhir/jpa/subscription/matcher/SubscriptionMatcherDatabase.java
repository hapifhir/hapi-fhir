package ca.uhn.fhir.jpa.subscription.matcher;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.jpa.dao.DaoRegistry;
import ca.uhn.fhir.jpa.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.dao.SearchParameterMap;
import ca.uhn.fhir.jpa.provider.ServletSubRequestDetails;
import ca.uhn.fhir.jpa.service.MatchUrlService;
import ca.uhn.fhir.jpa.subscription.ResourceModifiedMessage;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
@Lazy
public class SubscriptionMatcherDatabase implements ISubscriptionMatcher {
	private Logger ourLog = LoggerFactory.getLogger(SubscriptionMatcherDatabase.class);

	@Autowired
	private FhirContext myCtx;
	@Autowired
	DaoRegistry myDaoRegistry;
	@Autowired
	MatchUrlService myMatchUrlService;

	@Override
	public SubscriptionMatchResult match(String criteria, ResourceModifiedMessage msg) {
		IIdType id = msg.getId(myCtx);
		String resourceType = id.getResourceType();
		String resourceId = id.getIdPart();

		// run the subscriptions query and look for matches, add the id as part of the criteria to avoid getting matches of previous resources rather than the recent resource
		criteria += "&_id=" + resourceType + "/" + resourceId;

		IBundleProvider results = performSearch(criteria);

		ourLog.debug("Subscription check found {} results for query: {}", results.size(), criteria);

		return new SubscriptionMatchResult(results.size() > 0);
	}
	
	/**
	 * Search based on a query criteria
	 */
	protected IBundleProvider performSearch(String theCriteria) {
		IFhirResourceDao<?> subscriptionDao = myDaoRegistry.getResourceDao("Subscription");
		RuntimeResourceDefinition responseResourceDef = subscriptionDao.validateCriteriaAndReturnResourceDefinition(theCriteria);
		SearchParameterMap responseCriteriaUrl = myMatchUrlService.translateMatchUrl(theCriteria, responseResourceDef);

		RequestDetails req = new ServletSubRequestDetails();
		req.setSubRequest(true);

		IFhirResourceDao<? extends IBaseResource> responseDao = myDaoRegistry.getResourceDao(responseResourceDef.getImplementingClass());
		responseCriteriaUrl.setLoadSynchronousUpTo(1);

		IBundleProvider responseResults = responseDao.search(responseCriteriaUrl, req);
		return responseResults;
	}
}
