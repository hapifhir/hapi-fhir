package ca.uhn.fhir.cql.dstu3.evaluation;

/*-
 * #%L
 * HAPI FHIR JPA Server - Clinical Quality Language
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.cql.common.provider.EvaluationProviderFactory;
import ca.uhn.fhir.cql.common.retrieve.JpaFhirRetrieveProvider;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import org.opencds.cqf.cql.engine.data.CompositeDataProvider;
import org.opencds.cqf.cql.engine.data.DataProvider;
import org.opencds.cqf.cql.engine.fhir.searchparam.SearchParameterResolver;
import org.opencds.cqf.cql.engine.model.ModelResolver;
import org.opencds.cqf.cql.engine.terminology.TerminologyProvider;
import org.opencds.cqf.cql.evaluator.engine.terminology.PrivateCachingTerminologyProviderDecorator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

// This class is a relatively dumb factory for data providers. It supports only
// creating JPA providers for FHIR and only basic auth for terminology
@Component
public class ProviderFactory implements EvaluationProviderFactory {

	private final DaoRegistry registry;
	private final TerminologyProvider defaultTerminologyProvider;
	private final FhirContext fhirContext;
	private final ModelResolver fhirModelResolver;

	@Autowired
	public ProviderFactory(FhirContext fhirContext, DaoRegistry registry,
								  TerminologyProvider defaultTerminologyProvider, ModelResolver fhirModelResolver) {
		this.defaultTerminologyProvider = defaultTerminologyProvider;
		this.registry = registry;
		this.fhirContext = fhirContext;
		this.fhirModelResolver = fhirModelResolver;
	}

	public DataProvider createDataProvider(String model, String version, RequestDetails theRequestDetails) {
		return this.createDataProvider(model, version, null, null, null, theRequestDetails);
	}

	public DataProvider createDataProvider(String model, String version, String url, String user, String pass, RequestDetails theRequestDetails) {
		TerminologyProvider terminologyProvider = this.createTerminologyProvider(model, version, url, user, pass);
		return this.createDataProvider(model, version, terminologyProvider, theRequestDetails);
	}

	public DataProvider createDataProvider(String model, String version, TerminologyProvider terminologyProvider, RequestDetails theRequestDetails) {
		if (model.equals("FHIR") && version.startsWith("3")) {
			JpaFhirRetrieveProvider retrieveProvider = new JpaFhirRetrieveProvider(this.registry,
				new SearchParameterResolver(this.fhirContext), theRequestDetails);
			retrieveProvider.setTerminologyProvider(terminologyProvider);
			retrieveProvider.setExpandValueSets(true);

			return new CompositeDataProvider(this.fhirModelResolver, retrieveProvider);
		}

		throw new IllegalArgumentException(Msg.code(1650) + String.format("Can't construct a data provider for model %s version %s", model, version));
    }

    public TerminologyProvider createTerminologyProvider(String model, String version, String url, String user,
            String pass) {
		  TerminologyProvider terminologyProvider = null;
		  terminologyProvider = this.defaultTerminologyProvider;
        return new PrivateCachingTerminologyProviderDecorator(terminologyProvider);
    }
}
