package ca.uhn.fhir.jpa.provider.dstu3;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.hl7.fhir.dstu3.model.*;
import org.hl7.fhir.dstu3.model.CapabilityStatement.*;
import org.hl7.fhir.dstu3.model.Enumerations.SearchParamType;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.dao.DaoConfig;
import ca.uhn.fhir.jpa.dao.IFhirSystemDao;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.util.CoverageIgnore;
import ca.uhn.fhir.util.ExtensionConstants;

public class JpaConformanceProviderDstu3 extends org.hl7.fhir.dstu3.hapi.rest.server.ServerCapabilityStatementProvider {

	private volatile CapabilityStatement myCachedValue;
	private DaoConfig myDaoConfig;
	private String myImplementationDescription;
	private RestfulServer myRestfulServer;
	private IFhirSystemDao<Bundle, Meta> mySystemDao;

	/**
	 * Constructor
	 */
	@CoverageIgnore
	public JpaConformanceProviderDstu3(){
		super();
		super.setCache(false);
	}

	/**
	 * Constructor
	 */
	public JpaConformanceProviderDstu3(RestfulServer theRestfulServer, IFhirSystemDao<Bundle, Meta> theSystemDao, DaoConfig theDaoConfig) {
		super(theRestfulServer);
		myRestfulServer = theRestfulServer;
		mySystemDao = theSystemDao;
		myDaoConfig = theDaoConfig;
		super.setCache(false);
	}

	@Override
	public CapabilityStatement getServerConformance(HttpServletRequest theRequest) {
		CapabilityStatement retVal = myCachedValue;

		Map<String, Long> counts = mySystemDao.getResourceCounts();

		FhirContext ctx = myRestfulServer.getFhirContext();

		retVal = super.getServerConformance(theRequest);
		for (CapabilityStatementRestComponent nextRest : retVal.getRest()) {

			for (CapabilityStatementRestResourceComponent nextResource : nextRest.getResource()) {

				nextResource.setVersioning(ResourceVersionPolicy.VERSIONEDUPDATE);
				
				ConditionalDeleteStatus conditionalDelete = nextResource.getConditionalDelete();
				if (conditionalDelete == ConditionalDeleteStatus.MULTIPLE && myDaoConfig.isAllowMultipleDelete() == false) {
					nextResource.setConditionalDelete(ConditionalDeleteStatus.SINGLE);
				}

				// Add resource counts
				Long count = counts.get(nextResource.getTypeElement().getValueAsString());
				if (count != null) {
					nextResource.addExtension(new Extension(ExtensionConstants.CONF_RESOURCE_COUNT, new DecimalType(count)));
				}

				// Add chained params
				for (CapabilityStatementRestResourceSearchParamComponent nextParam : nextResource.getSearchParam()) {
					if (nextParam.getType() == SearchParamType.REFERENCE) {
//						List<CodeType> targets = nextParam.getTarget();
//						for (CodeType next : targets) {
//							RuntimeResourceDefinition def = ctx.getResourceDefinition(next.getValue());
//							for (RuntimeSearchParam nextChainedParam : def.getSearchParams()) {
//								nextParam.addChain(nextChainedParam.getName());
//							}
//						}
					}
				}

			}
		}

		massage(retVal);
		
		retVal.getImplementation().setDescription(myImplementationDescription);
		myCachedValue = retVal;
		return retVal;
	}

	/**
	 * Subclasses may override
	 */
	protected void massage(CapabilityStatement theStatement) {
		// nothing
	}

	public void setDaoConfig(DaoConfig myDaoConfig) {
		this.myDaoConfig = myDaoConfig;
	}

	@CoverageIgnore
	public void setImplementationDescription(String theImplDesc) {
		myImplementationDescription = theImplDesc;
	}

	@Override
	public void setRestfulServer(RestfulServer theRestfulServer) {
		this.myRestfulServer = theRestfulServer;
		super.setRestfulServer(theRestfulServer);
	}

	@CoverageIgnore
	public void setSystemDao(IFhirSystemDao<Bundle, Meta> mySystemDao) {
		this.mySystemDao = mySystemDao;
	}
}
