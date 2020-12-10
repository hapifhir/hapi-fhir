package ca.uhn.fhir.cql.r4.evaluation;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.cql.common.helper.ClientHelper;
import ca.uhn.fhir.cql.common.provider.EvaluationProviderFactory;
import ca.uhn.fhir.cql.common.provider.R4ApelonFhirTerminologyProvider;
import ca.uhn.fhir.cql.common.retrieve.JpaFhirRetrieveProvider;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import org.opencds.cqf.cql.engine.data.CompositeDataProvider;
import org.opencds.cqf.cql.engine.data.DataProvider;
import org.opencds.cqf.cql.engine.fhir.model.R4FhirModelResolver;
import org.opencds.cqf.cql.engine.fhir.searchparam.SearchParameterResolver;
import org.opencds.cqf.cql.engine.fhir.terminology.R4FhirTerminologyProvider;
import org.opencds.cqf.cql.engine.terminology.TerminologyProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

// This class is a relatively dumb factory for data providers. It supports only
// creating JPA providers for FHIR, and only basic auth for terminology
@Component
public class ProviderFactory implements EvaluationProviderFactory {

    DaoRegistry registry;
    TerminologyProvider defaultTerminologyProvider;
    FhirContext fhirContext;

    @Autowired
    public ProviderFactory(FhirContext fhirContext, DaoRegistry registry,
            TerminologyProvider defaultTerminologyProvider) {
        this.defaultTerminologyProvider = defaultTerminologyProvider;
        this.registry = registry;
        this.fhirContext = fhirContext;
    }

    public DataProvider createDataProvider(String model, String version) {
        return this.createDataProvider(model, version, null, null, null);
    }

    public DataProvider createDataProvider(String model, String version, String url, String user, String pass) {
        TerminologyProvider terminologyProvider = this.createTerminologyProvider(model, version, url, user, pass);
        return this.createDataProvider(model, version, terminologyProvider);
    }

    public DataProvider createDataProvider(String model, String version, TerminologyProvider terminologyProvider) {
        if (model.equals("FHIR") && version.startsWith("4")) {
            R4FhirModelResolver modelResolver = new R4FhirModelResolver();
            JpaFhirRetrieveProvider retrieveProvider = new JpaFhirRetrieveProvider(this.registry,
                    new SearchParameterResolver(this.fhirContext));
            retrieveProvider.setTerminologyProvider(terminologyProvider);
            retrieveProvider.setExpandValueSets(true);

            return new CompositeDataProvider(modelResolver, retrieveProvider);
        }

        throw new IllegalArgumentException(
                String.format("Can't construct a data provider for model %s version %s", model, version));
    }

    public TerminologyProvider createTerminologyProvider(String model, String version, String url, String user,
            String pass) {
        if (url != null && !url.isEmpty()) {
            IGenericClient client = ClientHelper.getClient(FhirContext.forR4(), url, user, pass);
            if (url.contains("apelon.com")) {
                return new R4ApelonFhirTerminologyProvider(client);
            }
            return new R4FhirTerminologyProvider(client);
        }
        return this.defaultTerminologyProvider;
    }
}
