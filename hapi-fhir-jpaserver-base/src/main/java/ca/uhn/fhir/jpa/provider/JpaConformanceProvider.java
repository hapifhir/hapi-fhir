package ca.uhn.fhir.jpa.provider;

import java.util.Map;

import ca.uhn.fhir.jpa.dao.IFhirSystemDao;
import ca.uhn.fhir.model.dstu.resource.Conformance;
import ca.uhn.fhir.model.dstu.resource.Conformance.Rest;
import ca.uhn.fhir.model.dstu.resource.Conformance.RestResource;
import ca.uhn.fhir.model.primitive.DecimalDt;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.provider.ServerConformanceProvider;
import ca.uhn.fhir.util.ExtensionConstants;

public class JpaConformanceProvider extends ServerConformanceProvider {

	private String myImplementationDescription;
	private IFhirSystemDao mySystemDao;
	private volatile Conformance myCachedValue;

	public JpaConformanceProvider(RestfulServer theRestfulServer, IFhirSystemDao theSystemDao) {
		super(theRestfulServer);
		mySystemDao = theSystemDao;
		super.setCache(false);

//		for (IFhirResourceDao<?> nextResourceDao : theResourceDaos) {
//			nextResourceDao.registerDaoListener(new IDaoListener() {
//				@Override
//				public void writeCompleted() {
//					myCachedValue = null;
//				}
//			});
//		}
	}

	@Override
	public Conformance getServerConformance() {
		Conformance retVal = myCachedValue;
//		if (retVal != null) {
//			return retVal;
//		}

		Map<String, Long> counts = mySystemDao.getResourceCounts();

		retVal = super.getServerConformance();
		for (Rest nextRest : retVal.getRest()) {
			for (RestResource nextResource : nextRest.getResource()) {
				Long count = counts.get(nextResource.getType().getValueAsString());
				if (count != null) {
					nextResource.addUndeclaredExtension(false, ExtensionConstants.CONF_RESOURCE_COUNT, new DecimalDt(count));
				}
			}
		}

		retVal.getImplementation().setDescription(myImplementationDescription);
		myCachedValue = retVal;
		return retVal;
	}

	public void setImplementationDescription(String theImplDesc) {
		myImplementationDescription = theImplDesc;
	}

}
