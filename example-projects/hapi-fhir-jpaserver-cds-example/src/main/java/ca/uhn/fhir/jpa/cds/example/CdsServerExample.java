
package ca.uhn.fhir.jpa.cds.example;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.jpa.dao.DaoConfig;
import ca.uhn.fhir.jpa.dao.IFhirSystemDao;
import ca.uhn.fhir.jpa.provider.dstu3.JpaConformanceProviderDstu3;
import ca.uhn.fhir.jpa.provider.dstu3.JpaSystemProviderDstu3;
import ca.uhn.fhir.jpa.provider.dstu3.TerminologyUploaderProviderDstu3;
import ca.uhn.fhir.jpa.rp.dstu3.ActivityDefinitionResourceProvider;
import ca.uhn.fhir.jpa.rp.dstu3.MeasureResourceProvider;
import ca.uhn.fhir.jpa.rp.dstu3.PlanDefinitionResourceProvider;
import ca.uhn.fhir.jpa.search.DatabaseBackedPagingProvider;
import ca.uhn.fhir.narrative.DefaultThymeleafNarrativeGenerator;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.server.ETagSupportEnum;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.interceptor.IServerInterceptor;
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.Meta;
import org.opencds.cqf.providers.FHIRActivityDefinitionResourceProvider;
import org.opencds.cqf.providers.FHIRMeasureResourceProvider;
import org.opencds.cqf.providers.FHIRPlanDefinitionResourceProvider;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletException;
import java.util.Collection;
import java.util.List;

public class CdsServerExample extends RestfulServer {

	@SuppressWarnings("unchecked")
	@Override
	protected void initialize() throws ServletException {
		super.initialize();

		FhirVersionEnum fhirVersion = FhirVersionEnum.DSTU3;
		setFhirContext(new FhirContext(fhirVersion));

		// Get the spring context from the web container (it's declared in web.xml)
		WebApplicationContext myAppCtx = ContextLoaderListener.getCurrentWebApplicationContext();

		if (myAppCtx == null) {
			throw new ServletException("Error retrieving spring context from the web container");
		}

		String resourceProviderBeanName = "myResourceProvidersDstu3";
		List<IResourceProvider> beans = myAppCtx.getBean(resourceProviderBeanName, List.class);
		setResourceProviders(beans);
		
		Object systemProvider = myAppCtx.getBean("mySystemProviderDstu3", JpaSystemProviderDstu3.class);
		setPlainProviders(systemProvider);

		/*
		 * The conformance provider exports the supported resources, search parameters, etc for
		 * this server. The JPA version adds resource counts to the exported statement, so it
		 * is a nice addition.
		 */
		IFhirSystemDao<Bundle, Meta> systemDao = myAppCtx.getBean("mySystemDaoDstu3", IFhirSystemDao.class);
		JpaConformanceProviderDstu3 confProvider =
			new JpaConformanceProviderDstu3(this, systemDao, myAppCtx.getBean(DaoConfig.class));
		confProvider.setImplementationDescription("Example Server");
		setServerConformanceProvider(confProvider);

		/*
		 * Enable ETag Support (this is already the default)
		 */
		setETagSupport(ETagSupportEnum.ENABLED);

		/*
		 * This server tries to dynamically generate narratives
		 */
		FhirContext ctx = getFhirContext();
		ctx.setNarrativeGenerator(new DefaultThymeleafNarrativeGenerator());

		/*
		 * Default to JSON and pretty printing
		 */
		setDefaultPrettyPrint(true);
		setDefaultResponseEncoding(EncodingEnum.JSON);

		/*
		 * -- New in HAPI FHIR 1.5 --
		 * This configures the server to page search results to and from
		 * the database, instead of only paging them to memory. This may mean
		 * a performance hit when performing searches that return lots of results,
		 * but makes the server much more scalable.
		 */
		setPagingProvider(myAppCtx.getBean(DatabaseBackedPagingProvider.class));

		/*
		 * Load interceptors for the server from Spring (these are defined in FhirServerConfig.java)
		 */
		Collection<IServerInterceptor> interceptorBeans = myAppCtx.getBeansOfType(IServerInterceptor.class).values();
		for (IServerInterceptor interceptor : interceptorBeans) {
			this.registerInterceptor(interceptor);
		}

		/*
		* 	Adding resource providers from the cqf-ruler
		*/
		// Measure processing
		FHIRMeasureResourceProvider measureProvider = new FHIRMeasureResourceProvider(getResourceProviders());
		MeasureResourceProvider jpaMeasureProvider = (MeasureResourceProvider) getProvider("Measure");
		measureProvider.setDao(jpaMeasureProvider.getDao());
		measureProvider.setContext(jpaMeasureProvider.getContext());

		// PlanDefinition processing
		FHIRPlanDefinitionResourceProvider planDefProvider = new FHIRPlanDefinitionResourceProvider(getResourceProviders());
		PlanDefinitionResourceProvider jpaPlanDefProvider =
			(PlanDefinitionResourceProvider) getProvider("PlanDefinition");
		planDefProvider.setDao(jpaPlanDefProvider.getDao());
		planDefProvider.setContext(jpaPlanDefProvider.getContext());

		// ActivityDefinition processing
		FHIRActivityDefinitionResourceProvider actDefProvider = new FHIRActivityDefinitionResourceProvider(getResourceProviders());
		ActivityDefinitionResourceProvider jpaActDefProvider =
			(ActivityDefinitionResourceProvider) getProvider("ActivityDefinition");
		actDefProvider.setDao(jpaActDefProvider.getDao());
		actDefProvider.setContext(jpaActDefProvider.getContext());

		try {
			unregisterProvider(jpaMeasureProvider);
			unregisterProvider(jpaPlanDefProvider);
			unregisterProvider(jpaActDefProvider);
		} catch (Exception e) {
			throw new ServletException("Unable to unregister provider: " + e.getMessage());
		}

		registerProvider(measureProvider);
		registerProvider(planDefProvider);
		registerProvider(actDefProvider);

		/*
		 * If you are hosting this server at a specific DNS name, the server will try to 
		 * figure out the FHIR base URL based on what the web container tells it, but
		 * this doesn't always work. If you are setting links in your search bundles that
		 * just refer to "localhost", you might want to use a server address strategy:
		 */
		//setServerAddressStrategy(new HardcodedServerAddressStrategy("http://mydomain.com/fhir/baseDstu2"));
		
		/*
		 * If you are using DSTU3+, you may want to add a terminology uploader, which allows 
		 * uploading of external terminologies such as Snomed CT. Note that this uploader
		 * does not have any security attached (any anonymous user may use it by default)
		 * so it is a potential security vulnerability. Consider using an AuthorizationInterceptor
		 * with this feature.
		 */
		registerProvider(myAppCtx.getBean(TerminologyUploaderProviderDstu3.class));
	}

	public IResourceProvider getProvider(String name) {

		for (IResourceProvider res : getResourceProviders()) {
			if (res.getResourceType().getSimpleName().equals(name)) {
				return res;
			}
		}

		throw new IllegalArgumentException("This should never happen!");
	}
}
