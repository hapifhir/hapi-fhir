package ca.uhn.fhir.cql.config;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.cql.provider.CqlProviderFactory;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.rp.r4.LibraryResourceProvider;
import ca.uhn.fhir.jpa.rp.r4.MeasureResourceProvider;
import ca.uhn.fhir.jpa.rp.r4.ValueSetResourceProvider;
import ca.uhn.fhir.jpa.term.api.ITermReadSvcR4;
import org.opencds.cqf.common.evaluation.EvaluationProviderFactory;
import org.opencds.cqf.cql.engine.terminology.TerminologyProvider;
import org.opencds.cqf.r4.evaluation.ProviderFactory;
import org.opencds.cqf.r4.providers.HQMFProvider;
import org.opencds.cqf.r4.providers.JpaTerminologyProvider;
import org.opencds.cqf.r4.providers.LibraryOperationsProvider;
import org.opencds.cqf.r4.providers.MeasureOperationsProvider;
import org.opencds.cqf.tooling.library.r4.NarrativeProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CqlR4Config extends BaseCqlConfig {

	@Bean
	CqlProviderFactory cqlProviderFactory() {
		return new CqlProviderFactory();
	}

	@Bean
	TerminologyProvider terminologyProvider(ITermReadSvcR4 theITermReadSvc, FhirContext theFhirContext, ValueSetResourceProvider theValueSetResourceProvider) {
		return new JpaTerminologyProvider(theITermReadSvc, theFhirContext, theValueSetResourceProvider);
	}

	@Bean
	EvaluationProviderFactory evaluationProviderFactory(FhirContext theFhirContext, DaoRegistry theDaoRegistry, TerminologyProvider theLocalSystemTerminologyProvider) {
		return new ProviderFactory(theFhirContext, theDaoRegistry, theLocalSystemTerminologyProvider);
	}

	@Bean
	NarrativeProvider narrativeProvider() {
		return new NarrativeProvider();
	}

	@Bean
	HQMFProvider theHQMFProvider() {
		return new HQMFProvider();
	}

	@Bean
	LibraryOperationsProvider LibraryOperationsProvider(LibraryResourceProvider theLibraryResourceProvider, NarrativeProvider theNarrativeProvider, DaoRegistry theDaoRegistry, JpaTerminologyProvider theJpaTerminologyProvider) {
		return new LibraryOperationsProvider(theLibraryResourceProvider, theNarrativeProvider, theDaoRegistry, theJpaTerminologyProvider);
	}

	@Bean
	public MeasureOperationsProvider measureOperationsProvider(DaoRegistry theDaoRegistry, EvaluationProviderFactory theEvaluationProviderFactory, NarrativeProvider theNarrativeProvider, HQMFProvider theHQMFProvider, LibraryOperationsProvider theLibraryOperationsProvider, MeasureResourceProvider theMeasureResourceProvider) {
		return new MeasureOperationsProvider(theDaoRegistry, theEvaluationProviderFactory, theNarrativeProvider, theHQMFProvider, theLibraryOperationsProvider, theMeasureResourceProvider);
	}
}
