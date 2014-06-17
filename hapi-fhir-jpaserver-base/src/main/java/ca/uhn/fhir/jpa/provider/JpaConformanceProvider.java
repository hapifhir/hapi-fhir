package ca.uhn.fhir.jpa.provider;

import java.util.Map;

import ca.uhn.fhir.jpa.dao.IFhirSystemDao;
import ca.uhn.fhir.model.dstu.resource.Conformance;
import ca.uhn.fhir.model.dstu.resource.Conformance.Rest;
import ca.uhn.fhir.model.dstu.resource.Conformance.RestResource;
import ca.uhn.fhir.model.primitive.DecimalDt;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.provider.ServerConformanceProvider;

public class JpaConformanceProvider extends ServerConformanceProvider {

	private IFhirSystemDao mySystemDao;

	public JpaConformanceProvider(RestfulServer theRestfulServer, IFhirSystemDao theSystemDao) {
		super(theRestfulServer);
		mySystemDao = theSystemDao;
		super.setCache(false);
	}
	
	
	@Override
	public Conformance getServerConformance() {
		
		Map<String, Long> counts = mySystemDao.getResourceCounts();
		
		Conformance retVal = super.getServerConformance();
		for (Rest nextRest : retVal.getRest()) {
			for (RestResource nextResource : nextRest.getResource()) {
				Long count = counts.get(nextResource.getType().getValueAsString());
				if (count!=null) {
					nextResource.addUndeclaredExtension(false, "http://hl7api.sourceforge.net/hapi-fhir/res/extdefs.html#resourceCount", new DecimalDt(count));
				}
			}
		}
		
		return retVal;
	}

}
